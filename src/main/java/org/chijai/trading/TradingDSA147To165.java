package org.chijai.trading;

import java.util.*;

/**
 * TradingDSA147To165
 *
 * One IntelliJ-ready Java 17+ file containing DSA-147 through DSA-165.
 *
 * Design goal:
 *   - interview-simple implementations
 *   - production mapping comments from the supplied codebase notes
 *   - corrected edge cases / invariants
 *   - runnable smoke tests in main()
 */
public class TradingDSA147To165 {

    // =========================================================================
    // DSA-147 — Best Bid / Best Ask
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   Two ordered maps, already ordered best-first:
     *   bids: price DESC, asks: price ASC.
     *   price -> aggregate qty.
     *
     *   NEW         -> add qty
     *   CANCEL/FILL -> subtract qty, remove level at zero
     *
     * PRODUCTION MAPPING (from supplied notes):
     *   OrderBookCore -> BestBidOffer + dirty flag.
     *   PriceList -> sorted doubly-linked pooled PriceListNode levels.
     *
     * PRODUCTION GAP:
     *   TreeMap is the clean general-purpose interview answer.
     *   Custom price lists can optimize best-node access, allocation, cache locality,
     *   and known insertion patterns.
     */
    static final class DSA147_BestBidAsk {

        enum Side { BUY, SELL }
        enum EventType { NEW, CANCEL, FILL }

        record OrderEvent(EventType type, Side side, long price, long qty) {}

        static final class OrderBook {
            private final TreeMap<Long, Long> bids =
                    new TreeMap<>(Comparator.reverseOrder());
            private final TreeMap<Long, Long> asks = new TreeMap<>();

            void process(OrderEvent e) {
                requirePositive(e.price(), "price");
                requirePositive(e.qty(), "qty");

                TreeMap<Long, Long> book = e.side() == Side.BUY ? bids : asks;

                switch (e.type()) {
                    case NEW -> book.merge(e.price(), e.qty(), Math::addExact);
                    case CANCEL, FILL -> subtract(book, e.price(), e.qty());
                }
            }

            private static void subtract(TreeMap<Long, Long> book, long price, long qty) {
                Long old = book.get(price);
                if (old == null) {
                    throw new IllegalStateException("No level at price " + price);
                }
                if (qty > old) {
                    throw new IllegalStateException("Cannot subtract " + qty + " from level qty " + old);
                }
                long remaining = old - qty;
                if (remaining == 0) book.remove(price);
                else book.put(price, remaining);
            }

            long bestBid()    { return bids.isEmpty() ? -1L : bids.firstKey(); }
            long bestAsk()    { return asks.isEmpty() ? -1L : asks.firstKey(); }
            long bestBidQty() { return bids.isEmpty() ? 0L : bids.firstEntry().getValue(); }
            long bestAskQty() { return asks.isEmpty() ? 0L : asks.firstEntry().getValue(); }

            long spread() {
                return (bids.isEmpty() || asks.isEmpty())
                        ? Long.MAX_VALUE
                        : Math.subtractExact(bestAsk(), bestBid());
            }
        }
    }

    // =========================================================================
    // DSA-148 — Top N Price Levels
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   Same structure as DSA-147.
     *   Because each side is stored best-first, iterate entrySet() and take N.
     *
     * PRODUCTION MAPPING:
     *   Sorted PriceList -> walk first N nodes.
     *
     * PRODUCTION GAP:
     *   TreeMap favors general ordered updates.
     *   A custom linked/pool-backed level list can favor direct best access,
     *   sequential depth traversal, and zero/low allocation.
     */
    static final class DSA148_TopNPriceLevels {

        enum Side { BUY, SELL }
        enum EventType { NEW, CANCEL, FILL }

        record OrderEvent(EventType type, Side side, long price, long qty) {}
        record PriceLevel(long price, long qty) {
            @Override public String toString() { return price + "@" + qty; }
        }

        static final class OrderBook {
            private final TreeMap<Long, Long> bids =
                    new TreeMap<>(Comparator.reverseOrder());
            private final TreeMap<Long, Long> asks = new TreeMap<>();

            void process(OrderEvent e) {
                requirePositive(e.price(), "price");
                requirePositive(e.qty(), "qty");

                TreeMap<Long, Long> book = e.side() == Side.BUY ? bids : asks;
                switch (e.type()) {
                    case NEW -> book.merge(e.price(), e.qty(), Math::addExact);
                    case CANCEL, FILL -> subtract(book, e.price(), e.qty());
                }
            }

            List<PriceLevel> topBids(int n) { return snapshot(bids, n); }
            List<PriceLevel> topAsks(int n) { return snapshot(asks, n); }

            private static List<PriceLevel> snapshot(TreeMap<Long, Long> book, int n) {
                if (n <= 0) return List.of();

                List<PriceLevel> out = new ArrayList<>(Math.min(n, book.size()));
                int count = 0;
                for (Map.Entry<Long, Long> e : book.entrySet()) {
                    if (count++ >= n) break;
                    out.add(new PriceLevel(e.getKey(), e.getValue()));
                }
                return out;
            }

            private static void subtract(TreeMap<Long, Long> book, long price, long qty) {
                Long old = book.get(price);
                if (old == null || qty > old) {
                    throw new IllegalStateException("Invalid subtraction at price " + price);
                }
                long remaining = old - qty;
                if (remaining == 0) book.remove(price);
                else book.put(price, remaining);
            }
        }
    }

    // =========================================================================
    // DSA-149 — Price-Time Priority Queue
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   BUY : higher price first, then earlier sequence.
     *   SELL: lower price first, then earlier sequence.
     *
     * PRODUCTION MAPPING:
     *   RankingTime = transactionTimeNs + sequenceNumber.
     *   Per-level OrderList.insertRanked() with head/tail fast paths.
     *
     * PRODUCTION GAP:
     *   PriorityQueue is ideal for "give me next best".
     *   Real books also need cancellation, amendments, explicit levels, and stable FIFO.
     */
    static final class DSA149_PriceTimePriorityQueue {

        enum Side { BUY, SELL }

        record Order(String id, Side side, long price, long seq, long qty) {
            @Override public String toString() {
                return id + "(price=" + price + ", seq=" + seq + ", qty=" + qty + ")";
            }
        }

        static final class PriceTimeQueue {
            private final PriorityQueue<Order> buys = new PriorityQueue<>((a, b) -> {
                int c = Long.compare(b.price(), a.price());
                return c != 0 ? c : Long.compare(a.seq(), b.seq());
            });

            private final PriorityQueue<Order> sells = new PriorityQueue<>((a, b) -> {
                int c = Long.compare(a.price(), b.price());
                return c != 0 ? c : Long.compare(a.seq(), b.seq());
            });

            void add(Order o) {
                requirePositive(o.price(), "price");
                requirePositive(o.qty(), "qty");
                (o.side() == Side.BUY ? buys : sells).add(o);
            }

            Order peekBuy()  { return buys.peek(); }
            Order peekSell() { return sells.peek(); }
            Order pollBuy()  { return buys.poll(); }
            Order pollSell() { return sells.poll(); }
            int buySize()    { return buys.size(); }
            int sellSize()   { return sells.size(); }
        }
    }

    // =========================================================================
    // DSA-150 — Deduplicate Execution Events
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   HashSet<executionId>. Check/add BEFORE state mutation.
     *
     * PRODUCTION MAPPING:
     *   Framework/session sequencing may suppress transport re-delivery.
     *   Supplied notes also describe small transaction-local fixed reference arrays,
     *   cleared on commit()/rollback(), to keep hot paths allocation-free.
     *
     * CORRECTION:
     *   A bounded remembered set is approximate after eviction.
     *   accessOrder=false in LinkedHashMap is insertion order, not LRU.
     */
    static final class DSA150_DeduplicateExecutionEvents {

        enum Side { BUY, SELL }

        record ExecutionEvent(long execId, String account, String tradable,
                              Side side, long price, long qty) {}

        static final class ExecutionProcessor {
            private final Set<Long> seen = new HashSet<>();
            private final Map<String, Long> positions = new HashMap<>();
            private int duplicatesDropped;
            private int accepted;

            boolean process(ExecutionEvent e) {
                requirePositive(e.qty(), "qty");

                if (!seen.add(e.execId())) {
                    duplicatesDropped++;
                    return false;
                }

                long delta = e.side() == Side.BUY ? e.qty() : -e.qty();
                positions.merge(key(e.account(), e.tradable()), delta, Math::addExact);
                accepted++;
                return true;
            }

            long position(String account, String tradable) {
                return positions.getOrDefault(key(account, tradable), 0L);
            }

            int duplicatesDropped() { return duplicatesDropped; }
            int accepted() { return accepted; }
        }

        /**
         * Fixed-capacity insertion-order remembered set.
         * Exact only while the duplicate ID is still retained.
         */
        static final class BoundedRememberedSet {
            private final LinkedHashMap<Long, Boolean> remembered;

            BoundedRememberedSet(int maxSize) {
                if (maxSize <= 0) throw new IllegalArgumentException("maxSize must be > 0");

                this.remembered = new LinkedHashMap<>(maxSize, 0.75f, false) {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry<Long, Boolean> eldest) {
                        return size() > maxSize;
                    }
                };
            }

            boolean isDuplicate(long id) {
                if (remembered.containsKey(id)) return true;
                remembered.put(id, Boolean.TRUE);
                return false;
            }

            int size() { return remembered.size(); }
        }
    }

    // =========================================================================
    // DSA-151 — Detect Sequence Gap
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   Ordered stream: compare current with previous + 1 -> O(N), O(1) extra.
     *
     *   Unordered batch variant:
     *   TreeSet sorts + dedups -> O(N log N).
     *
     * PRODUCTION MAPPING:
     *   Snapshot/session sequence numbers + authoritative snapshot/replay recovery.
     *
     * IMPORTANT:
     *   Detect-and-advance is a detector, not a reorderer.
     */
    static final class DSA151_DetectSequenceGap {

        record GapRange(long first, long last) {
            long count() { return Math.addExact(Math.subtractExact(last, first), 1L); }
            @Override public String toString() {
                return first == last ? String.valueOf(first) : first + ".." + last;
            }
        }

        static List<GapRange> detectOrdered(long[] seqs) {
            if (seqs == null || seqs.length < 2) return List.of();

            List<GapRange> gaps = new ArrayList<>();
            long prev = seqs[0];

            for (int i = 1; i < seqs.length; i++) {
                long cur = seqs[i];
                if (cur <= prev) {
                    throw new IllegalArgumentException("Input must be strictly increasing");
                }
                if (prev != Long.MAX_VALUE && cur > prev + 1) {
                    gaps.add(new GapRange(prev + 1, cur - 1));
                }
                prev = cur;
            }
            return gaps;
        }

        static List<GapRange> detectUnorderedBatch(Collection<Long> received, long expectedMin) {
            if (received == null || received.isEmpty()) return List.of();

            TreeSet<Long> sorted = new TreeSet<>(received);
            List<GapRange> gaps = new ArrayList<>();
            long expected = expectedMin;

            for (long seq : sorted) {
                if (seq < expected) continue;
                if (seq > expected) gaps.add(new GapRange(expected, seq - 1));
                if (seq == Long.MAX_VALUE) break;
                expected = seq + 1;
            }
            return gaps;
        }

        static final class OnlineGapDetector {
            private long nextExpected;
            private final List<GapRange> gaps = new ArrayList<>();

            OnlineGapDetector(long firstExpected) {
                this.nextExpected = firstExpected;
            }

            GapRange receive(long seqNo) {
                if (seqNo < nextExpected) return null;

                if (seqNo == nextExpected) {
                    if (nextExpected != Long.MAX_VALUE) nextExpected++;
                    return null;
                }

                GapRange gap = new GapRange(nextExpected, seqNo - 1);
                gaps.add(gap);
                nextExpected = seqNo == Long.MAX_VALUE ? Long.MAX_VALUE : seqNo + 1;
                return gap;
            }

            long nextExpected() { return nextExpected; }
            List<GapRange> allGaps() { return Collections.unmodifiableList(gaps); }
        }
    }

    // =========================================================================
    // DSA-152 — Reorder Out-of-Order Messages
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *
     * Preferred exact-sequence formulation:
     *   nextExpected + buffer.
     *   seq < expected -> stale/duplicate
     *   seq > expected -> buffer
     *   seq = expected -> emit, increment, drain contiguous buffer
     *
     * Alternative bounded-lateness heap:
     *   valid only when W has a precise guarantee:
     *   no future message can arrive more than W sequence numbers "late".
     *
     * PRODUCTION MAPPING:
     *   Supplied notes say DSF/ICore delivers in-order per session;
     *   application reacts to gaps with snapshot/recovery instead of a reorder heap.
     */
    static final class DSA152_ReorderOutOfOrderMessages {

        record Message(long seqNo, String payload) {}

        static final class ExactReorderBuffer {
            private long nextExpected;
            private final int maxBuffered;
            private final Map<Long, Message> buffer = new HashMap<>();

            ExactReorderBuffer(long firstExpected, int maxBuffered) {
                if (maxBuffered <= 0) throw new IllegalArgumentException("maxBuffered must be > 0");
                this.nextExpected = firstExpected;
                this.maxBuffered = maxBuffered;
            }

            List<Message> receive(Message msg) {
                List<Message> out = new ArrayList<>();

                if (msg.seqNo() < nextExpected) {
                    return out; // stale / duplicate
                }

                if (msg.seqNo() == nextExpected) {
                    emitAndAdvance(msg, out);
                    drain(out);
                    return out;
                }

                buffer.putIfAbsent(msg.seqNo(), msg);
                if (buffer.size() > maxBuffered) {
                    throw new IllegalStateException("Reorder buffer exceeded maxBuffered=" + maxBuffered);
                }
                return out;
            }

            private void emitAndAdvance(Message msg, List<Message> out) {
                out.add(msg);
                if (nextExpected == Long.MAX_VALUE) {
                    throw new IllegalStateException("Sequence overflow");
                }
                nextExpected++;
            }

            private void drain(List<Message> out) {
                while (true) {
                    Message next = buffer.remove(nextExpected);
                    if (next == null) return;
                    emitAndAdvance(next, out);
                }
            }

            long nextExpected() { return nextExpected; }
            int bufferSize() { return buffer.size(); }
        }

        /**
         * Bounded-lateness variant.
         *
         * Contract:
         *   Once highestSeen = H, no future arrival may have seq < H - W + 1.
         *
         * Therefore a buffered seq S is safe when S <= H - W.
         */
        static final class BoundedLatenessHeapBuffer {
            private final long latenessDistanceW;
            private final PriorityQueue<Message> heap =
                    new PriorityQueue<>(Comparator.comparingLong(Message::seqNo));
            private long highestSeen = Long.MIN_VALUE;

            BoundedLatenessHeapBuffer(long latenessDistanceW) {
                if (latenessDistanceW < 0) {
                    throw new IllegalArgumentException("W must be >= 0");
                }
                this.latenessDistanceW = latenessDistanceW;
            }

            List<Message> receive(Message msg) {
                highestSeen = Math.max(highestSeen, msg.seqNo());
                heap.add(msg);
                return flushSafe();
            }

            private List<Message> flushSafe() {
                List<Message> out = new ArrayList<>();
                if (highestSeen == Long.MIN_VALUE) return out;

                long safeThrough = highestSeen - latenessDistanceW;
                while (!heap.isEmpty() && heap.peek().seqNo() <= safeThrough) {
                    out.add(heap.poll());
                }
                return out;
            }

            List<Message> endOfStream() {
                List<Message> out = new ArrayList<>(heap.size());
                while (!heap.isEmpty()) out.add(heap.poll());
                return out;
            }
        }
    }

    // =========================================================================
    // DSA-153 — Rolling Exposure
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   Example metric: per-open-order contribution = price * leavesQty.
     *   orderId -> contribution + running total.
     *
     * PRODUCTION MAPPING:
     *   Supplied notes describe calculator -> validator -> hierarchical risk propagation,
     *   pool-borrowed exposure objects, and UPDATE as remove-old + add-new.
     *
     * PRODUCTION GAP:
     *   price*qty is only one simplified exposure definition.
     */
    static final class DSA153_RollingExposure {

        enum EventType { NEW, UPDATE, CANCEL }

        static final class ExposureTracker {
            private final Map<String, Long> contribution = new HashMap<>();
            private long total;
            private final long limit;

            ExposureTracker(long limit) {
                if (limit < 0) throw new IllegalArgumentException("limit must be >= 0");
                this.limit = limit;
            }

            String process(EventType type, String orderId, long price, long leavesQty) {
                switch (type) {
                    case NEW -> {
                        requirePositive(price, "price");
                        requireNonNegative(leavesQty, "leavesQty");
                        if (contribution.containsKey(orderId)) {
                            throw new IllegalStateException("Duplicate NEW " + orderId);
                        }
                        long v = Math.multiplyExact(price, leavesQty);
                        contribution.put(orderId, v);
                        total = Math.addExact(total, v);
                    }
                    case UPDATE -> {
                        requirePositive(price, "price");
                        requireNonNegative(leavesQty, "leavesQty");
                        Long old = contribution.get(orderId);
                        if (old == null) throw new IllegalStateException("Unknown order " + orderId);

                        long v = Math.multiplyExact(price, leavesQty);
                        contribution.put(orderId, v);
                        total = Math.addExact(total, Math.subtractExact(v, old));
                    }
                    case CANCEL -> {
                        Long old = contribution.remove(orderId);
                        if (old == null) throw new IllegalStateException("Unknown order " + orderId);
                        total = Math.subtractExact(total, old);
                    }
                }

                return total > limit ? "BLOCK" : "OK";
            }

            long total() { return total; }
            int openOrders() { return contribution.size(); }
        }
    }

    // =========================================================================
    // DSA-154 — Sliding Price Deviation Check / Price Collar
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   Integer/scaled price + basis points.
     *
     *   lower = ref * (10000-bps) / 10000
     *   upper = ref * (10000+bps) / 10000
     *
     * PRODUCTION MAPPING:
     *   Reference price on Tradable, updated by dedicated action;
     *   collar/circuit behavior can be enforced closer to matching/execution.
     *
     * CORRECTION:
     *   Integer arithmetic avoids FP drift but can still overflow.
     *   Math.multiplyExact makes overflow explicit here.
     */
    static final class DSA154_SlidingPriceDeviationCheck {

        enum Result { PASS, TOO_LOW, TOO_HIGH, NO_REF }

        static final class PriceCollar {
            private final Map<String, Long> refPrices = new HashMap<>();
            private final long thresholdBps;

            PriceCollar(long thresholdBps) {
                if (thresholdBps < 0 || thresholdBps > 10_000) {
                    throw new IllegalArgumentException("thresholdBps must be in [0,10000]");
                }
                this.thresholdBps = thresholdBps;
            }

            void updateRef(String tradableId, long price) {
                requirePositive(price, "reference price");
                refPrices.put(tradableId, price);
            }

            Result check(String tradableId, long price) {
                requirePositive(price, "order price");

                Long ref = refPrices.get(tradableId);
                if (ref == null || ref <= 0) return Result.NO_REF;

                long lower = Math.multiplyExact(ref, 10_000L - thresholdBps) / 10_000L;
                long upper = Math.multiplyExact(ref, 10_000L + thresholdBps) / 10_000L;

                if (price < lower) return Result.TOO_LOW;
                if (price > upper) return Result.TOO_HIGH;
                return Result.PASS;
            }
        }
    }

    // =========================================================================
    // DSA-155 — Exchange Throttle
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   Exact sliding-log limiter with Deque<accepted timestamps>.
     *   Evict expired front; accept only if current count < limit.
     *
     * PRODUCTION MAPPING:
     *   Supplied notes describe a fixed circular time-slot buffer, power-of-two slots,
     *   slot-index arithmetic, and fixed memory.
     *
     * PRODUCTION GAP:
     *   Deque is exact/simple. Bucketed slots trade precision for predictable memory.
     */
    static final class DSA155_ExchangeThrottle {

        enum Decision { ACCEPT, REJECT }

        static final class SlidingWindowThrottle {
            private final int limit;
            private final long windowNs;
            private final Deque<Long> accepted = new ArrayDeque<>();
            private long lastTimestamp = Long.MIN_VALUE;

            SlidingWindowThrottle(int limit, long windowNs) {
                if (limit <= 0 || windowNs <= 0) {
                    throw new IllegalArgumentException("limit/windowNs must be > 0");
                }
                this.limit = limit;
                this.windowNs = windowNs;
            }

            Decision submit(long timestampNs) {
                if (timestampNs < lastTimestamp) {
                    throw new IllegalArgumentException("timestamps must be nondecreasing");
                }
                lastTimestamp = timestampNs;

                long cutoff = timestampNs - windowNs;
                while (!accepted.isEmpty() && accepted.peekFirst() <= cutoff) {
                    accepted.pollFirst();
                }

                if (accepted.size() >= limit) return Decision.REJECT;

                accepted.addLast(timestampNs);
                return Decision.ACCEPT;
            }

            int windowCount() { return accepted.size(); }
        }

        /**
         * Different rate-limit policy: burst capacity + continuous refill.
         */
        static final class TokenBucket {
            private final double capacity;
            private final double ratePerNs;
            private double tokens;
            private long lastNs;
            private boolean initialized;

            TokenBucket(int capacity, long refillWindowNs) {
                if (capacity <= 0 || refillWindowNs <= 0) {
                    throw new IllegalArgumentException("capacity/window must be > 0");
                }
                this.capacity = capacity;
                this.ratePerNs = (double) capacity / refillWindowNs;
                this.tokens = capacity;
            }

            Decision submit(long nowNs) {
                if (!initialized) {
                    initialized = true;
                    lastNs = nowNs;
                } else {
                    if (nowNs < lastNs) throw new IllegalArgumentException("timestamps must be nondecreasing");
                    tokens = Math.min(capacity, tokens + (nowNs - lastNs) * ratePerNs);
                    lastNs = nowNs;
                }

                if (tokens >= 1.0) {
                    tokens -= 1.0;
                    return Decision.ACCEPT;
                }
                return Decision.REJECT;
            }
        }
    }

    // =========================================================================
    // DSA-156 — Rolling VWAP
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   VWAP = Σ(price*qty) / Σqty.
     *   Maintain running numerator/denominator + deque for expiry.
     *
     * PRODUCTION MAPPING:
     *   Supplied notes say VALUE and VOLUME are separate risk metrics,
     *   potentially including scaling/PQF/FX, rather than being divided into VWAP.
     *
     * CORRECTION:
     *   Math.multiplyExact/addExact make long overflow explicit.
     */
    static final class DSA156_RollingVWAP {

        record Trade(long price, long qty, long timestampNs) {}

        static final class CountVWAP {
            private final int maxTrades;
            private final Deque<Trade> window = new ArrayDeque<>();
            private long totalValue;
            private long totalQty;

            CountVWAP(int maxTrades) {
                if (maxTrades <= 0) throw new IllegalArgumentException("maxTrades must be > 0");
                this.maxTrades = maxTrades;
            }

            void add(Trade t) {
                validateTrade(t);

                window.addLast(t);
                totalValue = Math.addExact(totalValue, Math.multiplyExact(t.price(), t.qty()));
                totalQty = Math.addExact(totalQty, t.qty());

                if (window.size() > maxTrades) {
                    remove(window.pollFirst());
                }
            }

            double vwap() {
                return totalQty == 0 ? 0.0 : (double) totalValue / totalQty;
            }

            long vwapScaled(long scale) {
                if (scale <= 0) throw new IllegalArgumentException("scale must be > 0");
                return totalQty == 0
                        ? 0L
                        : Math.multiplyExact(totalValue, scale) / totalQty;
            }

            private void remove(Trade t) {
                totalValue = Math.subtractExact(totalValue, Math.multiplyExact(t.price(), t.qty()));
                totalQty = Math.subtractExact(totalQty, t.qty());
            }
        }

        static final class TimeVWAP {
            private final long windowNs;
            private final Deque<Trade> window = new ArrayDeque<>();
            private long totalValue;
            private long totalQty;
            private long lastTimestamp = Long.MIN_VALUE;

            TimeVWAP(long windowNs) {
                if (windowNs <= 0) throw new IllegalArgumentException("windowNs must be > 0");
                this.windowNs = windowNs;
            }

            void add(Trade t) {
                validateTrade(t);
                if (t.timestampNs() < lastTimestamp) {
                    throw new IllegalArgumentException("timestamps must be nondecreasing");
                }
                lastTimestamp = t.timestampNs();

                evict(t.timestampNs());
                window.addLast(t);
                totalValue = Math.addExact(totalValue, Math.multiplyExact(t.price(), t.qty()));
                totalQty = Math.addExact(totalQty, t.qty());
            }

            double vwap(long nowNs) {
                if (nowNs < lastTimestamp) {
                    throw new IllegalArgumentException("nowNs cannot move backwards");
                }
                lastTimestamp = nowNs;
                evict(nowNs);
                return totalQty == 0 ? 0.0 : (double) totalValue / totalQty;
            }

            private void evict(long nowNs) {
                long cutoff = nowNs - windowNs;
                while (!window.isEmpty() && window.peekFirst().timestampNs() <= cutoff) {
                    Trade t = window.pollFirst();
                    totalValue = Math.subtractExact(totalValue, Math.multiplyExact(t.price(), t.qty()));
                    totalQty = Math.subtractExact(totalQty, t.qty());
                }
            }
        }

        private static void validateTrade(Trade t) {
            requirePositive(t.price(), "price");
            requirePositive(t.qty(), "qty");
        }
    }

    // =========================================================================
    // DSA-157 — Market Data Sliding Maximum
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   Monotonic decreasing deque.
     *   Each element enters once, leaves once -> O(N) total.
     *
     * PRODUCTION MAPPING:
     *   If a live book already maintains sorted active prices, best/current extreme
     *   comes from the book's best node; no historical-window deque is needed.
     *
     * IMPORTANT:
     *   "max over current active book" != "max over historical sliding window".
     */
    static final class DSA157_MarketDataSlidingMaximum {

        static final class SlidingMax {
            record Entry(long price, long index) {}

            private final int windowSize;
            private final Deque<Entry> dq = new ArrayDeque<>();
            private long nextIndex;

            SlidingMax(int windowSize) {
                if (windowSize <= 0) throw new IllegalArgumentException("windowSize must be > 0");
                this.windowSize = windowSize;
            }

            void add(long price) {
                long idx = nextIndex++;
                while (!dq.isEmpty() && dq.peekFirst().index() <= idx - windowSize) {
                    dq.pollFirst();
                }
                while (!dq.isEmpty() && dq.peekLast().price() <= price) {
                    dq.pollLast();
                }
                dq.addLast(new Entry(price, idx));
            }

            long max() {
                if (dq.isEmpty()) throw new NoSuchElementException();
                return dq.peekFirst().price();
            }
        }

        record Tick(long price, long timestampNs) {}

        static final class TimeSlidingMax {
            private final long windowNs;
            private final Deque<Tick> dq = new ArrayDeque<>();
            private long lastTimestamp = Long.MIN_VALUE;

            TimeSlidingMax(long windowNs) {
                if (windowNs <= 0) throw new IllegalArgumentException("windowNs must be > 0");
                this.windowNs = windowNs;
            }

            void add(Tick t) {
                if (t.timestampNs() < lastTimestamp) {
                    throw new IllegalArgumentException("timestamps must be nondecreasing");
                }
                lastTimestamp = t.timestampNs();

                evict(t.timestampNs());
                while (!dq.isEmpty() && dq.peekLast().price() <= t.price()) {
                    dq.pollLast();
                }
                dq.addLast(t);
            }

            long max(long nowNs) {
                if (nowNs < lastTimestamp) {
                    throw new IllegalArgumentException("nowNs cannot move backwards");
                }
                lastTimestamp = nowNs;
                evict(nowNs);
                return dq.isEmpty() ? Long.MIN_VALUE : dq.peekFirst().price();
            }

            private void evict(long nowNs) {
                long cutoff = nowNs - windowNs;
                while (!dq.isEmpty() && dq.peekFirst().timestampNs() <= cutoff) {
                    dq.pollFirst();
                }
            }
        }
    }

    // =========================================================================
    // DSA-158 — Order State Aggregation
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   orderId -> state machine + filled/leaves quantities.
     *
     * PRODUCTION MAPPING:
     *   Supplied notes separate OrderStatus ("where") from ExecutionType ("why").
     *   Partial fill can be represented by leavesQuantity rather than a unique status.
     *
     * CORRECTION:
     *   Validate positive fills, overfills, and legal transitions.
     */
    static final class DSA158_OrderStateAggregation {

        enum State {
            PENDING_NEW,
            ACTIVE,
            PARTIALLY_FILLED,
            FILLED,
            CANCELLED,
            REJECTED;

            boolean terminal() {
                return this == FILLED || this == CANCELLED || this == REJECTED;
            }
        }

        enum Event { NEW, ACK, PARTIAL_FILL, FILL, CANCEL, REJECT }

        static final class Order {
            final String id;
            final long orderQty;
            long filledQty;
            long leavesQty;
            State state;

            Order(String id, long orderQty) {
                requirePositive(orderQty, "orderQty");
                this.id = id;
                this.orderQty = orderQty;
                this.leavesQty = orderQty;
                this.state = State.PENDING_NEW;
            }

            @Override public String toString() {
                return "Order{id='" + id + "', state=" + state +
                        ", orderQty=" + orderQty +
                        ", filledQty=" + filledQty +
                        ", leavesQty=" + leavesQty + "}";
            }
        }

        static final class OrderBook {
            private final Map<String, Order> orders = new HashMap<>();

            void process(String id, Event event, long qty) {
                switch (event) {
                    case NEW -> {
                        if (orders.containsKey(id)) throw new IllegalStateException("Duplicate NEW " + id);
                        orders.put(id, new Order(id, qty));
                    }
                    case ACK -> requireTransition(id, State.PENDING_NEW, State.ACTIVE);
                    case REJECT -> requireTransition(id, State.PENDING_NEW, State.REJECTED);
                    case PARTIAL_FILL -> partialFill(id, qty);
                    case FILL -> fill(id, qty);
                    case CANCEL -> cancel(id);
                }
            }

            private void partialFill(String id, long qty) {
                requirePositive(qty, "fill qty");
                Order o = requireLiveOrder(id);

                if (o.state != State.ACTIVE && o.state != State.PARTIALLY_FILLED) {
                    throw new IllegalStateException("Cannot partial-fill from state " + o.state);
                }
                if (qty >= o.leavesQty) {
                    throw new IllegalStateException("PARTIAL_FILL qty must be < leavesQty");
                }

                o.filledQty = Math.addExact(o.filledQty, qty);
                o.leavesQty -= qty;
                o.state = State.PARTIALLY_FILLED;
            }

            private void fill(String id, long qty) {
                requirePositive(qty, "fill qty");
                Order o = requireLiveOrder(id);

                if (o.state != State.ACTIVE && o.state != State.PARTIALLY_FILLED) {
                    throw new IllegalStateException("Cannot fill from state " + o.state);
                }
                if (qty != o.leavesQty) {
                    throw new IllegalStateException("FILL qty must equal leavesQty=" + o.leavesQty);
                }

                o.filledQty = Math.addExact(o.filledQty, qty);
                o.leavesQty = 0;
                o.state = State.FILLED;
            }

            private void cancel(String id) {
                Order o = requireLiveOrder(id);
                o.state = State.CANCELLED;
            }

            private void requireTransition(String id, State from, State to) {
                Order o = requireLiveOrder(id);
                if (o.state != from) {
                    throw new IllegalStateException("Expected " + from + " but was " + o.state);
                }
                o.state = to;
            }

            private Order requireLiveOrder(String id) {
                Order o = orders.get(id);
                if (o == null) throw new IllegalStateException("Unknown order " + id);
                if (o.state.terminal()) throw new IllegalStateException("Order already terminal: " + o.state);
                return o;
            }

            Order get(String id) { return orders.get(id); }

            List<Order> inState(State s) {
                return orders.values().stream().filter(o -> o.state == s).toList();
            }
        }
    }

    // =========================================================================
    // DSA-159 — Match Buy and Sell Orders
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   bid heap: price DESC, seq ASC
     *   ask heap: price ASC, seq ASC
     *
     *   Match at resting order price.
     *   Partial resting order keeps ORIGINAL sequence/time priority.
     *
     * PRODUCTION MAPPING:
     *   Supplied notes describe PriceList + OrderList, match metadata,
     *   passive/aggressive flags, self-match prevention, MAQ, leavesQuantity.
     *
     * PRODUCTION GAP:
     *   Two heaps are an interview simplification; explicit price levels/order lists
     *   are better for cancellation, amendment, depth, and stable FIFO.
     */
    static final class DSA159_MatchBuyAndSellOrders {

        enum Side { BUY, SELL }

        record Trade(String buyId, String sellId, long execPrice, long execQty) {
            @Override public String toString() {
                return "TRADE buy=" + buyId + " sell=" + sellId +
                        " @" + execPrice + " qty=" + execQty;
            }
        }

        static final class MatchingEngine {
            private static final class OrderEntry {
                final String id;
                final Side side;
                final long price;
                final long seq;
                long qty;

                OrderEntry(String id, Side side, long price, long seq, long qty) {
                    this.id = id;
                    this.side = side;
                    this.price = price;
                    this.seq = seq;
                    this.qty = qty;
                }
            }

            private final PriorityQueue<OrderEntry> bidPQ = new PriorityQueue<>((a, b) -> {
                int c = Long.compare(b.price, a.price);
                return c != 0 ? c : Long.compare(a.seq, b.seq);
            });

            private final PriorityQueue<OrderEntry> askPQ = new PriorityQueue<>((a, b) -> {
                int c = Long.compare(a.price, b.price);
                return c != 0 ? c : Long.compare(a.seq, b.seq);
            });

            private long nextSeq;
            private final List<Trade> allTrades = new ArrayList<>();

            List<Trade> submit(String id, Side side, long price, long qty) {
                requirePositive(price, "price");
                requirePositive(qty, "qty");

                OrderEntry incoming = new OrderEntry(id, side, price, nextSeq++, qty);
                List<Trade> result = new ArrayList<>();

                if (side == Side.BUY) matchBuy(incoming, result);
                else matchSell(incoming, result);

                allTrades.addAll(result);
                return result;
            }

            private void matchBuy(OrderEntry buy, List<Trade> result) {
                while (buy.qty > 0 && !askPQ.isEmpty() && buy.price >= askPQ.peek().price) {
                    OrderEntry ask = askPQ.poll();
                    long execQty = Math.min(buy.qty, ask.qty);

                    result.add(new Trade(buy.id, ask.id, ask.price, execQty));

                    buy.qty -= execQty;
                    ask.qty -= execQty;

                    // Same original sequence -> same time priority.
                    if (ask.qty > 0) askPQ.add(ask);
                }

                if (buy.qty > 0) bidPQ.add(buy);
            }

            private void matchSell(OrderEntry sell, List<Trade> result) {
                while (sell.qty > 0 && !bidPQ.isEmpty() && sell.price <= bidPQ.peek().price) {
                    OrderEntry bid = bidPQ.poll();
                    long execQty = Math.min(sell.qty, bid.qty);

                    result.add(new Trade(bid.id, sell.id, bid.price, execQty));

                    sell.qty -= execQty;
                    bid.qty -= execQty;

                    if (bid.qty > 0) bidPQ.add(bid);
                }

                if (sell.qty > 0) askPQ.add(sell);
            }

            long bestBid() { return bidPQ.isEmpty() ? -1L : bidPQ.peek().price; }
            long bestAsk() { return askPQ.isEmpty() ? -1L : askPQ.peek().price; }
            int bidDepth() { return bidPQ.size(); }
            int askDepth() { return askPQ.size(); }
            List<Trade> allTrades() { return Collections.unmodifiableList(allTrades); }
        }
    }

    // =========================================================================
    // DSA-160 — Position From Executions
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   BUY fill -> +qty
     *   SELL fill -> -qty
     *   key = (account, instrument)
     *
     * PRODUCTION MAPPING:
     *   Supplied notes describe richer execution/exposure objects and hierarchical state.
     *
     * CORRECTION:
     *   Do NOT derive average fill price as unsigned Σ(price*qty)/abs(netPosition).
     *   Average cost/P&L needs a defined accounting model.
     */
    static final class DSA160_PositionFromExecutions {

        enum Side { BUY, SELL }

        record Fill(String execId, String account, String tradable,
                    Side side, long qty, long price) {}

        record AccountInstrument(String account, String tradable) {}

        static final class PositionTracker {
            private final Map<AccountInstrument, Long> positions = new HashMap<>();
            private final Map<String, Long> instrumentPositions = new HashMap<>();

            void process(Fill f) {
                requirePositive(f.qty(), "qty");
                requirePositive(f.price(), "price");

                long delta = f.side() == Side.BUY ? f.qty() : -f.qty();
                AccountInstrument key = new AccountInstrument(f.account(), f.tradable());

                positions.merge(key, delta, Math::addExact);
                instrumentPositions.merge(f.tradable(), delta, Math::addExact);
            }

            long position(String account, String tradable) {
                return positions.getOrDefault(new AccountInstrument(account, tradable), 0L);
            }

            long instrumentPosition(String tradable) {
                return instrumentPositions.getOrDefault(tradable, 0L);
            }

            List<AccountInstrument> overLimit(long absoluteLimit) {
                requireNonNegative(absoluteLimit, "absoluteLimit");
                return positions.entrySet().stream()
                        .filter(e -> safeAbsExceeds(e.getValue(), absoluteLimit))
                        .map(Map.Entry::getKey)
                        .toList();
            }
        }
    }

    // =========================================================================
    // DSA-161 — Detect Duplicate Orders
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   First define authoritative identity.
     *   Then use exact sets / bounded business-key window as required.
     *
     * PRODUCTION MAPPING:
     *   Supplied notes point to gateway/protocol IDs/tokens.
     *   Self-match prevention is a separate concern, not order dedup.
     *
     * CORRECTION:
     *   Economically identical orders are NOT automatically duplicates.
     *   Business-key dedup is optional and must be explicitly specified.
     */
    static final class DSA161_DetectDuplicateOrders {

        enum Side { BUY, SELL }

        enum Reason {
            NONE,
            DUPLICATE_ORDER_ID,
            DUPLICATE_CLIENT_ORDER_ID,
            DUPLICATE_BUSINESS_KEY
        }

        record Order(String orderId, String clientOrderId, String account, String tradable,
                     Side side, long price, long qty, long timestampNs) {}

        record DupResult(boolean duplicate, Reason reason) {
            static DupResult ok() { return new DupResult(false, Reason.NONE); }
            static DupResult dup(Reason reason) { return new DupResult(true, reason); }
        }

        static final class DuplicateDetector {
            private final Set<String> seenOrderIds = new HashSet<>();
            private final Set<String> seenClientIds = new HashSet<>();

            private final boolean useBusinessKeyRule;
            private final long businessWindowNs;
            private final int maxBusinessKeys;

            // insertion-order so oldest timestamps can be purged from the front
            private final LinkedHashMap<String, Long> businessKeys = new LinkedHashMap<>();

            DuplicateDetector(boolean useBusinessKeyRule,
                              long businessWindowNs,
                              int maxBusinessKeys) {
                if (businessWindowNs < 0 || maxBusinessKeys <= 0) {
                    throw new IllegalArgumentException();
                }
                this.useBusinessKeyRule = useBusinessKeyRule;
                this.businessWindowNs = businessWindowNs;
                this.maxBusinessKeys = maxBusinessKeys;
            }

            DupResult check(Order o) {
                requirePositive(o.price(), "price");
                requirePositive(o.qty(), "qty");

                if (seenOrderIds.contains(o.orderId())) {
                    return DupResult.dup(Reason.DUPLICATE_ORDER_ID);
                }

                boolean hasClientId = o.clientOrderId() != null && !o.clientOrderId().isBlank();
                if (hasClientId && seenClientIds.contains(o.clientOrderId())) {
                    return DupResult.dup(Reason.DUPLICATE_CLIENT_ORDER_ID);
                }

                String businessKey = null;
                if (useBusinessKeyRule) {
                    purgeExpired(o.timestampNs());

                    businessKey = o.account() + "|" + o.tradable() + "|" +
                            o.side() + "|" + o.price() + "|" + o.qty();

                    Long first = businessKeys.get(businessKey);
                    if (first != null && o.timestampNs() - first <= businessWindowNs) {
                        return DupResult.dup(Reason.DUPLICATE_BUSINESS_KEY);
                    }
                }

                // Commit dedup state only after all validation passes.
                seenOrderIds.add(o.orderId());
                if (hasClientId) seenClientIds.add(o.clientOrderId());

                if (useBusinessKeyRule) {
                    businessKeys.put(businessKey, o.timestampNs());
                    trimToCapacity();
                }

                return DupResult.ok();
            }

            private void purgeExpired(long nowNs) {
                Iterator<Map.Entry<String, Long>> it = businessKeys.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Long> e = it.next();
                    if (nowNs - e.getValue() > businessWindowNs) it.remove();
                    else break; // insertion order + nondecreasing timestamps assumption
                }
            }

            private void trimToCapacity() {
                Iterator<String> it = businessKeys.keySet().iterator();
                while (businessKeys.size() > maxBusinessKeys && it.hasNext()) {
                    it.next();
                    it.remove();
                }
            }
        }
    }

    // =========================================================================
    // DSA-162 — Most Active Instruments
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   rolling deque + instrument -> running score.
     *   Top-K query uses a min-heap of size K.
     *
     * PRODUCTION MAPPING:
     *   Supplied notes describe rolling activity primarily for rate/risk limits,
     *   rolled up through a risk hierarchy.
     *
     * PRODUCTION GAP:
     *   Same rolling-counter primitive; ranking is an extra query concern.
     */
    static final class DSA162_MostActiveInstruments {

        record Event(String tradable, long qty, long timestampNs) {}
        record Ranked(String tradable, long score) {
            @Override public String toString() { return tradable + "=" + score; }
        }

        static final class ActivityTracker {
            private final long windowNs;
            private final Map<String, Deque<Event>> windows = new HashMap<>();
            private final Map<String, Long> scores = new HashMap<>();
            private long lastTimestamp = Long.MIN_VALUE;

            ActivityTracker(long windowNs) {
                if (windowNs <= 0) throw new IllegalArgumentException("windowNs must be > 0");
                this.windowNs = windowNs;
            }

            void add(Event e) {
                requireNonNegative(e.qty(), "qty");
                requireMonotonic(e.timestampNs());

                Deque<Event> dq = windows.computeIfAbsent(e.tradable(), k -> new ArrayDeque<>());
                evict(e.tradable(), e.timestampNs());

                dq.addLast(e);
                scores.merge(e.tradable(), e.qty(), Math::addExact);
            }

            List<Ranked> topK(int k, long nowNs) {
                if (k <= 0) return List.of();
                requireMonotonic(nowNs);

                for (String id : new ArrayList<>(windows.keySet())) {
                    evict(id, nowNs);
                }

                PriorityQueue<Ranked> heap =
                        new PriorityQueue<>(Comparator.comparingLong(Ranked::score));

                for (Map.Entry<String, Long> e : scores.entrySet()) {
                    long score = e.getValue();
                    if (score <= 0) continue;

                    Ranked ranked = new Ranked(e.getKey(), score);
                    if (heap.size() < k) {
                        heap.add(ranked);
                    } else if (score > heap.peek().score()) {
                        heap.poll();
                        heap.add(ranked);
                    }
                }

                List<Ranked> out = new ArrayList<>(heap);
                out.sort((a, b) -> {
                    int c = Long.compare(b.score(), a.score());
                    return c != 0 ? c : a.tradable().compareTo(b.tradable());
                });
                return out;
            }

            long score(String tradable, long nowNs) {
                requireMonotonic(nowNs);
                evict(tradable, nowNs);
                return scores.getOrDefault(tradable, 0L);
            }

            private void evict(String id, long nowNs) {
                Deque<Event> dq = windows.get(id);
                if (dq == null) return;

                long cutoff = nowNs - windowNs;
                while (!dq.isEmpty() && dq.peekFirst().timestampNs() <= cutoff) {
                    Event old = dq.pollFirst();
                    long next = Math.subtractExact(scores.getOrDefault(id, 0L), old.qty());
                    if (next == 0) scores.remove(id);
                    else scores.put(id, next);
                }

                if (dq.isEmpty()) windows.remove(id);
            }

            private void requireMonotonic(long ts) {
                if (ts < lastTimestamp) throw new IllegalArgumentException("timestamps must be nondecreasing");
                lastTimestamp = ts;
            }
        }
    }

    // =========================================================================
    // DSA-163 — Merge Multiple Ordered Market Feeds
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   Min-heap of K current heads.
     *   Poll -> emit -> advance only that source -> push next.
     *   O(N log K), O(K).
     *
     * PRODUCTION MAPPING:
     *   If framework/session layer already supplies one ordered stream,
     *   application code does not need raw K-way merge.
     *
     * CORRECTION:
     *   Comparator includes feed index as deterministic final tie-break.
     */
    static final class DSA163_MergeMultipleOrderedMarketFeeds {

        record Event(long ts, long seq, String feed, String data) {
            @Override public String toString() {
                return "E{ts=" + ts + ", seq=" + seq + ", feed=" + feed + ", " + data + "}";
            }
        }

        private record HeapEntry(Event event, int feedIdx, int pos) {}

        static List<Event> merge(List<List<Event>> feeds) {
            Comparator<HeapEntry> cmp = Comparator
                    .comparingLong((HeapEntry h) -> h.event().ts())
                    .thenComparingLong(h -> h.event().seq())
                    .thenComparingInt(HeapEntry::feedIdx);

            PriorityQueue<HeapEntry> heap = new PriorityQueue<>(cmp);

            for (int i = 0; i < feeds.size(); i++) {
                List<Event> feed = feeds.get(i);
                validateFeedSorted(feed);
                if (!feed.isEmpty()) {
                    heap.add(new HeapEntry(feed.get(0), i, 0));
                }
            }

            List<Event> out = new ArrayList<>();

            while (!heap.isEmpty()) {
                HeapEntry min = heap.poll();
                out.add(min.event());

                int nextPos = min.pos() + 1;
                List<Event> feed = feeds.get(min.feedIdx());

                if (nextPos < feed.size()) {
                    heap.add(new HeapEntry(feed.get(nextPos), min.feedIdx(), nextPos));
                }
            }

            return out;
        }

        private static void validateFeedSorted(List<Event> feed) {
            for (int i = 1; i < feed.size(); i++) {
                Event a = feed.get(i - 1);
                Event b = feed.get(i);

                int c = Long.compare(a.ts(), b.ts());
                if (c > 0 || (c == 0 && a.seq() > b.seq())) {
                    throw new IllegalArgumentException("Each feed must already be sorted");
                }
            }
        }
    }

    // =========================================================================
    // DSA-164 — Snapshot + Incremental Merge
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *
     * before snapshot:
     *   buffer incrementals
     *
     * after snapshot S:
     *   seq < nextExpected  -> stale/duplicate
     *   seq = nextExpected  -> apply + increment + drain
     *   seq > nextExpected  -> buffer gap
     *
     * PRODUCTION MAPPING:
     *   Supplied notes describe staged/committed lifecycle + version guards.
     *
     * CRITICAL CORRECTION:
     *   stale check is seq < nextExpected, not merely seq <= snapshotSeq.
     *   Otherwise a replayed post-snapshot incremental can be double-applied.
     */
    static final class DSA164_SnapshotIncrementalMerge {

        enum Op { UPSERT, DELETE }

        record Update(long seqNo, Op op, String key, String value) {
            Update(long seqNo, Op op, String key) {
                this(seqNo, op, key, null);
            }
        }

        static final class SnapshotMerger {
            private Map<String, String> state;
            private long nextExpected = -1L;
            private boolean ready;
            private final TreeMap<Long, Update> pending = new TreeMap<>();

            void loadSnapshot(Map<String, String> snapshot, long snapshotSeq) {
                if (ready) throw new IllegalStateException("Snapshot already loaded");

                state = new HashMap<>(snapshot);
                if (snapshotSeq == Long.MAX_VALUE) {
                    throw new IllegalArgumentException("Cannot continue after Long.MAX_VALUE");
                }

                nextExpected = snapshotSeq + 1;
                ready = true;

                pending.headMap(nextExpected, false).clear(); // discard <= snapshotSeq
                drain();
            }

            /**
             * @return true iff this call directly applied the supplied update.
             *         false means buffered or stale/duplicate.
             */
            boolean apply(Update u) {
                if (!ready) {
                    pending.putIfAbsent(u.seqNo(), u);
                    return false;
                }

                if (u.seqNo() < nextExpected) {
                    return false; // stale / duplicate
                }

                if (u.seqNo() > nextExpected) {
                    pending.putIfAbsent(u.seqNo(), u);
                    return false; // gap
                }

                applyOne(u);
                incrementExpected();
                drain();
                return true;
            }

            private void drain() {
                while (true) {
                    Update next = pending.remove(nextExpected);
                    if (next == null) return;

                    applyOne(next);
                    incrementExpected();
                }
            }

            private void incrementExpected() {
                if (nextExpected == Long.MAX_VALUE) {
                    throw new IllegalStateException("Sequence overflow");
                }
                nextExpected++;
            }

            private void applyOne(Update u) {
                switch (u.op()) {
                    case UPSERT -> state.put(u.key(), u.value());
                    case DELETE -> state.remove(u.key());
                }
            }

            String get(String key) { return state == null ? null : state.get(key); }
            boolean isReady() { return ready; }
            long nextExpected() { return nextExpected; }
            int pendingCount() { return pending.size(); }
            Map<String, String> snapshotView() {
                return state == null ? Map.of() : Collections.unmodifiableMap(state);
            }
        }
    }

    // =========================================================================
    // DSA-165 — Order Latency Percentile
    // =========================================================================
    /**
     * INTERVIEW CORE:
     *   First clarify exact/approximate, offline/streaming, memory bound.
     *
     *   A) exact sort
     *   B) fixed-width histogram (bounded range; approximate)
     *   C) reservoir sample (approximate)
     *
     * PRODUCTION MAPPING:
     *   Supplied notes reference HdrHistogram-style production telemetry.
     *
     * CORRECTION:
     *   A simple equal-width histogram is conceptually related to HdrHistogram,
     *   but HdrHistogram has a more sophisticated bucket/sub-bucket layout.
     */
    static final class DSA165_OrderLatencyPercentile {

        static final class ExactPercentile {
            private final List<Long> data = new ArrayList<>();
            private boolean sorted;

            void record(long latencyNs) {
                requireNonNegative(latencyNs, "latencyNs");
                data.add(latencyNs);
                sorted = false;
            }

            long percentile(double p) {
                requirePercentileFraction(p);
                if (data.isEmpty()) return 0L;

                if (!sorted) {
                    Collections.sort(data);
                    sorted = true;
                }

                int idx = Math.max(0, (int) Math.ceil(p * data.size()) - 1);
                return data.get(idx);
            }

            long p50() { return percentile(0.50); }
            long p95() { return percentile(0.95); }
            long p99() { return percentile(0.99); }
        }

        static final class Histogram {
            private final long bucketWidth;
            private final long[] counts;
            private long total;
            private long sum;
            private long min = Long.MAX_VALUE;
            private long max;

            Histogram(long maxValueInclusive, long bucketWidth) {
                if (maxValueInclusive < 0 || bucketWidth <= 0) {
                    throw new IllegalArgumentException();
                }

                long bucketCount = maxValueInclusive / bucketWidth + 2;
                if (bucketCount > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("Too many buckets");
                }

                this.bucketWidth = bucketWidth;
                this.counts = new long[(int) bucketCount];
            }

            void record(long latencyNs) {
                requireNonNegative(latencyNs, "latencyNs");

                total++;
                sum = Math.addExact(sum, latencyNs);
                min = Math.min(min, latencyNs);
                max = Math.max(max, latencyNs);

                long rawBucket = latencyNs / bucketWidth;
                int bucket = (int) Math.min(rawBucket, counts.length - 1L);
                counts[bucket]++;
            }

            /**
             * Returns upper bound of selected fixed-width bucket.
             */
            long percentile(double p) {
                requirePercentileFraction(p);
                if (total == 0) return 0L;

                long target = (long) Math.ceil(p * total);
                long running = 0;

                for (int i = 0; i < counts.length; i++) {
                    running += counts[i];
                    if (running >= target) {
                        if (i == counts.length - 1) return max; // overflow bucket
                        return Math.multiplyExact(i + 1L, bucketWidth);
                    }
                }
                return max;
            }

            long p50() { return percentile(0.50); }
            long p95() { return percentile(0.95); }
            long p99() { return percentile(0.99); }
            long mean() { return total == 0 ? 0L : sum / total; }
            long min() { return min == Long.MAX_VALUE ? 0L : min; }
            long max() { return max; }
            long total() { return total; }
        }

        static final class Reservoir {
            private final long[] sample;
            private long seen;
            private final Random rng;

            Reservoir(int size, long seed) {
                if (size <= 0) throw new IllegalArgumentException("size must be > 0");
                this.sample = new long[size];
                this.rng = new Random(seed);
            }

            void record(long latencyNs) {
                requireNonNegative(latencyNs, "latencyNs");

                if (seen < sample.length) {
                    sample[(int) seen] = latencyNs;
                } else {
                    long j = nextLongBounded(rng, seen + 1);
                    if (j < sample.length) {
                        sample[(int) j] = latencyNs;
                    }
                }
                seen++;
            }

            long percentile(double p) {
                requirePercentileFraction(p);

                int n = (int) Math.min(seen, sample.length);
                if (n == 0) return 0L;

                long[] copy = Arrays.copyOf(sample, n);
                Arrays.sort(copy);

                int idx = Math.max(0, (int) Math.ceil(p * n) - 1);
                return copy[idx];
            }
        }
    }

    // =========================================================================
    // Shared helpers
    // =========================================================================

    private static String key(String a, String b) {
        return a + '\u0000' + b;
    }

    private static void requirePositive(long value, String name) {
        if (value <= 0) throw new IllegalArgumentException(name + " must be > 0");
    }

    private static void requireNonNegative(long value, String name) {
        if (value < 0) throw new IllegalArgumentException(name + " must be >= 0");
    }

    private static void requirePercentileFraction(double p) {
        if (!(p > 0.0 && p <= 1.0)) {
            throw new IllegalArgumentException("percentile must be in (0,1]");
        }
    }

    private static boolean safeAbsExceeds(long value, long limit) {
        if (value == Long.MIN_VALUE) return true;
        return Math.abs(value) > limit;
    }

    /**
     * Uniform long in [0, bound) without requiring a newer Random API.
     */
    private static long nextLongBounded(Random random, long bound) {
        if (bound <= 0) throw new IllegalArgumentException("bound must be positive");

        long r = random.nextLong();
        long m = bound - 1;

        if ((bound & m) == 0L) {
            return r & m;
        }

        long u = r >>> 1;
        while (u + m - (u % bound) < 0L) {
            u = random.nextLong() >>> 1;
        }
        return u % bound;
    }

    // =========================================================================
    // MAIN — smoke tests for all DSA-147 through DSA-165
    // =========================================================================

    public static void main(String[] args) {

        System.out.println("=== DSA-147 Best Bid / Ask ===");
        var bbo = new DSA147_BestBidAsk.OrderBook();
        bbo.process(new DSA147_BestBidAsk.OrderEvent(
                DSA147_BestBidAsk.EventType.NEW,
                DSA147_BestBidAsk.Side.BUY, 100, 10));
        bbo.process(new DSA147_BestBidAsk.OrderEvent(
                DSA147_BestBidAsk.EventType.NEW,
                DSA147_BestBidAsk.Side.BUY, 101, 5));
        bbo.process(new DSA147_BestBidAsk.OrderEvent(
                DSA147_BestBidAsk.EventType.NEW,
                DSA147_BestBidAsk.Side.SELL, 103, 7));
        bbo.process(new DSA147_BestBidAsk.OrderEvent(
                DSA147_BestBidAsk.EventType.NEW,
                DSA147_BestBidAsk.Side.SELL, 102, 8));
        System.out.println("bestBid=" + bbo.bestBid() + " qty=" + bbo.bestBidQty());
        System.out.println("bestAsk=" + bbo.bestAsk() + " qty=" + bbo.bestAskQty());
        System.out.println("spread=" + bbo.spread());

        System.out.println("\n=== DSA-148 Top N Price Levels ===");
        var depth = new DSA148_TopNPriceLevels.OrderBook();
        depth.process(new DSA148_TopNPriceLevels.OrderEvent(
                DSA148_TopNPriceLevels.EventType.NEW,
                DSA148_TopNPriceLevels.Side.BUY, 100, 10));
        depth.process(new DSA148_TopNPriceLevels.OrderEvent(
                DSA148_TopNPriceLevels.EventType.NEW,
                DSA148_TopNPriceLevels.Side.BUY, 101, 20));
        depth.process(new DSA148_TopNPriceLevels.OrderEvent(
                DSA148_TopNPriceLevels.EventType.NEW,
                DSA148_TopNPriceLevels.Side.BUY, 99, 30));
        depth.process(new DSA148_TopNPriceLevels.OrderEvent(
                DSA148_TopNPriceLevels.EventType.NEW,
                DSA148_TopNPriceLevels.Side.SELL, 102, 5));
        depth.process(new DSA148_TopNPriceLevels.OrderEvent(
                DSA148_TopNPriceLevels.EventType.NEW,
                DSA148_TopNPriceLevels.Side.SELL, 103, 15));
        System.out.println("topBids=" + depth.topBids(2));
        System.out.println("topAsks=" + depth.topAsks(2));

        System.out.println("\n=== DSA-149 Price-Time Priority ===");
        var pt = new DSA149_PriceTimePriorityQueue.PriceTimeQueue();
        pt.add(new DSA149_PriceTimePriorityQueue.Order(
                "B1", DSA149_PriceTimePriorityQueue.Side.BUY, 100, 2, 10));
        pt.add(new DSA149_PriceTimePriorityQueue.Order(
                "B2", DSA149_PriceTimePriorityQueue.Side.BUY, 101, 3, 10));
        pt.add(new DSA149_PriceTimePriorityQueue.Order(
                "B3", DSA149_PriceTimePriorityQueue.Side.BUY, 101, 1, 10));
        System.out.println("nextBuy=" + pt.peekBuy());

        System.out.println("\n=== DSA-150 Execution Dedup ===");
        var exec = new DSA150_DeduplicateExecutionEvents.ExecutionProcessor();
        var ex1 = new DSA150_DeduplicateExecutionEvents.ExecutionEvent(
                1, "ACC1", "AAPL",
                DSA150_DeduplicateExecutionEvents.Side.BUY, 100, 10);
        System.out.println("accepted1=" + exec.process(ex1));
        System.out.println("acceptedDuplicate=" + exec.process(ex1));
        System.out.println("position=" + exec.position("ACC1", "AAPL"));

        System.out.println("\n=== DSA-151 Sequence Gap ===");
        System.out.println(DSA151_DetectSequenceGap.detectOrdered(
                new long[]{1, 2, 5, 6, 10}));

        System.out.println("\n=== DSA-152 Reorder ===");
        var reorder = new DSA152_ReorderOutOfOrderMessages.ExactReorderBuffer(1, 10);
        System.out.println(reorder.receive(
                new DSA152_ReorderOutOfOrderMessages.Message(3, "C")));
        System.out.println(reorder.receive(
                new DSA152_ReorderOutOfOrderMessages.Message(2, "B")));
        System.out.println(reorder.receive(
                new DSA152_ReorderOutOfOrderMessages.Message(1, "A")));

        System.out.println("\n=== DSA-153 Rolling Exposure ===");
        var exposure = new DSA153_RollingExposure.ExposureTracker(15_000);
        System.out.println(exposure.process(
                DSA153_RollingExposure.EventType.NEW, "O1", 100, 100));
        System.out.println(exposure.process(
                DSA153_RollingExposure.EventType.UPDATE, "O1", 100, 120));
        System.out.println("total=" + exposure.total());

        System.out.println("\n=== DSA-154 Price Collar ===");
        var collar = new DSA154_SlidingPriceDeviationCheck.PriceCollar(500);
        collar.updateRef("AAPL", 10_000);
        System.out.println("104=" + collar.check("AAPL", 10_400));
        System.out.println("106=" + collar.check("AAPL", 10_600));

        System.out.println("\n=== DSA-155 Exchange Throttle ===");
        var throttle = new DSA155_ExchangeThrottle.SlidingWindowThrottle(2, 1_000);
        System.out.println(throttle.submit(1_000));
        System.out.println(throttle.submit(1_100));
        System.out.println(throttle.submit(1_200));
        System.out.println(throttle.submit(2_001));

        System.out.println("\n=== DSA-156 Rolling VWAP ===");
        var vwap = new DSA156_RollingVWAP.CountVWAP(3);
        vwap.add(new DSA156_RollingVWAP.Trade(100, 10, 1));
        vwap.add(new DSA156_RollingVWAP.Trade(110, 20, 2));
        System.out.println("vwap=" + vwap.vwap());

        System.out.println("\n=== DSA-157 Sliding Maximum ===");
        var max = new DSA157_MarketDataSlidingMaximum.SlidingMax(3);
        for (long x : new long[]{1, 3, -1, -3, 5, 3, 6, 7}) {
            max.add(x);
            System.out.println("add=" + x + " currentMax=" + max.max());
        }

        System.out.println("\n=== DSA-158 Order State Aggregation ===");
        var states = new DSA158_OrderStateAggregation.OrderBook();
        states.process("O1", DSA158_OrderStateAggregation.Event.NEW, 100);
        states.process("O1", DSA158_OrderStateAggregation.Event.ACK, 0);
        states.process("O1", DSA158_OrderStateAggregation.Event.PARTIAL_FILL, 40);
        states.process("O1", DSA158_OrderStateAggregation.Event.FILL, 60);
        System.out.println(states.get("O1"));

        System.out.println("\n=== DSA-159 Matching Engine ===");
        var engine = new DSA159_MatchBuyAndSellOrders.MatchingEngine();
        engine.submit("S1", DSA159_MatchBuyAndSellOrders.Side.SELL, 101, 50);
        engine.submit("S2", DSA159_MatchBuyAndSellOrders.Side.SELL, 102, 50);
        System.out.println(engine.submit(
                "B1", DSA159_MatchBuyAndSellOrders.Side.BUY, 102, 70));
        System.out.println("bestAsk=" + engine.bestAsk());

        System.out.println("\n=== DSA-160 Position ===");
        var positions = new DSA160_PositionFromExecutions.PositionTracker();
        positions.process(new DSA160_PositionFromExecutions.Fill(
                "E1", "ACC1", "AAPL",
                DSA160_PositionFromExecutions.Side.BUY, 100, 100));
        positions.process(new DSA160_PositionFromExecutions.Fill(
                "E2", "ACC1", "AAPL",
                DSA160_PositionFromExecutions.Side.SELL, 35, 105));
        System.out.println("position=" + positions.position("ACC1", "AAPL"));

        System.out.println("\n=== DSA-161 Duplicate Orders ===");
        var dupOrders = new DSA161_DetectDuplicateOrders.DuplicateDetector(
                true, 1_000, 10_000);
        var o1 = new DSA161_DetectDuplicateOrders.Order(
                "O1", "C1", "ACC1", "AAPL",
                DSA161_DetectDuplicateOrders.Side.BUY,
                100, 10, 1_000);
        System.out.println(dupOrders.check(o1));
        System.out.println(dupOrders.check(o1));

        System.out.println("\n=== DSA-162 Most Active Instruments ===");
        var active = new DSA162_MostActiveInstruments.ActivityTracker(1_000);
        active.add(new DSA162_MostActiveInstruments.Event("AAPL", 100, 1_000));
        active.add(new DSA162_MostActiveInstruments.Event("MSFT", 300, 1_100));
        active.add(new DSA162_MostActiveInstruments.Event("AAPL", 250, 1_200));
        System.out.println(active.topK(2, 1_200));

        System.out.println("\n=== DSA-163 Merge K Feeds ===");
        List<List<DSA163_MergeMultipleOrderedMarketFeeds.Event>> feeds = List.of(
                List.of(
                        new DSA163_MergeMultipleOrderedMarketFeeds.Event(1000, 1, "F1", "A"),
                        new DSA163_MergeMultipleOrderedMarketFeeds.Event(1300, 2, "F1", "C")
                ),
                List.of(
                        new DSA163_MergeMultipleOrderedMarketFeeds.Event(1100, 1, "F2", "B"),
                        new DSA163_MergeMultipleOrderedMarketFeeds.Event(1400, 2, "F2", "D")
                )
        );
        System.out.println(DSA163_MergeMultipleOrderedMarketFeeds.merge(feeds));

        System.out.println("\n=== DSA-164 Snapshot + Incrementals ===");
        var merger = new DSA164_SnapshotIncrementalMerge.SnapshotMerger();
        merger.apply(new DSA164_SnapshotIncrementalMerge.Update(
                12, DSA164_SnapshotIncrementalMerge.Op.UPSERT, "MSFT", "200"));
        merger.loadSnapshot(Map.of("AAPL", "100"), 10);
        merger.apply(new DSA164_SnapshotIncrementalMerge.Update(
                11, DSA164_SnapshotIncrementalMerge.Op.UPSERT, "AAPL", "120"));
        System.out.println("state=" + merger.snapshotView());
        System.out.println("nextExpected=" + merger.nextExpected());

        System.out.println("\n=== DSA-165 Latency Percentiles ===");
        var histogram = new DSA165_OrderLatencyPercentile.Histogram(1_000, 10);
        for (long x : new long[]{10, 20, 15, 100, 50, 30, 40, 200, 25, 35}) {
            histogram.record(x);
        }
        System.out.println("p50=" + histogram.p50());
        System.out.println("p95=" + histogram.p95());
        System.out.println("p99=" + histogram.p99());

        System.out.println("\nALL SMOKE TESTS COMPLETED.");
    }
}
