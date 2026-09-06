package org.chijai.design.lld;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * LeetCode 359 - Logger Rate Limiter
 *
 * CLASSIFICATION
 * --------------
 * Primary   : Design -> HashMap -> Per-Key State / Cooldown
 * Secondary : Online stream processing
 *
 * Recognition:
 *
 *     "Each key becomes eligible again after a cooldown."
 *
 * Reusable state:
 *
 *     key -> nextAllowedTime
 */
public class LoggerRateLimiterV4 {

    private static final int COOLDOWN_SECONDS = 10;

    /**
     * ==============================================================
     * 1. Detailed Problem Statement
     * ==============================================================
     *
     * Design:
     *
     *     shouldPrintMessage(timestamp, message)
     *
     * Return true only when this message is allowed to print now.
     *
     * Rule:
     *
     *     If message m prints successfully at timestamp t,
     *     the SAME message cannot print again before t + 10.
     *
     * Therefore:
     *
     *     blocked interval = [t, t + 10)
     *     first legal time = t + 10
     *
     * Different messages are independent.
     *
     * Input guarantee:
     *
     *     timestamps arrive in non-decreasing order.
     *
     * Multiple messages may arrive at the same timestamp.
     *
     * --------------------------------------------------------------
     * Example 1 - Standard
     * --------------------------------------------------------------
     *
     *     (1,  "foo") -> true
     *     (2,  "bar") -> true
     *     (3,  "foo") -> false
     *     (8,  "bar") -> false
     *     (10, "foo") -> false
     *     (11, "foo") -> true
     *
     * State:
     *
     *     call          result    nextAllowed
     *     --------------------------------------
     *     1,  foo       true      foo -> 11
     *     2,  bar       true      bar -> 12
     *     3,  foo       false     foo stays 11
     *     8,  bar       false     bar stays 12
     *     10, foo       false     foo stays 11
     *     11, foo       true      foo -> 21
     *
     * --------------------------------------------------------------
     * Example 2 - Rejections do NOT restart cooldown
     * --------------------------------------------------------------
     *
     *     (1,  "x") -> true
     *     (5,  "x") -> false
     *     (9,  "x") -> false
     *     (11, "x") -> true
     *
     * The successful print at 1 creates:
     *
     *     nextAllowed = 11
     *
     * Rejections at 5 and 9 do not modify that boundary.
     *
     * --------------------------------------------------------------
     * Example 3 - Exact boundary
     * --------------------------------------------------------------
     *
     *     (20, "a") -> true
     *     (29, "a") -> false
     *     (30, "a") -> true
     *
     * Equality is allowed because blocked interval is [20, 30).
     *
     * --------------------------------------------------------------
     * Example 4 - Per-message independence
     * --------------------------------------------------------------
     *
     *     (1, "foo") -> true
     *     (1, "bar") -> true
     *     (1, "foo") -> false
     *
     * "foo" being blocked does not affect "bar".
     *
     * That independence is the clue for:
     *
     *     HashMap<message, state>
     */

    /**
     * ==============================================================
     * 2. First-Principles Invention Path
     * ==============================================================
     *
     * Obstacle:
     *
     *     "Can THIS message print now?"
     *
     * 1. Different messages are independent.
     *
     *        -> state per message
     *        -> HashMap
     *
     * 2. Complete history is unnecessary.
     *
     *        -> latest successful print is enough
     *
     * 3. Store the decision boundary directly.
     *
     *        nextAllowed = lastSuccessfulPrint + 10
     *
     * 4. Decision:
     *
     *        timestamp >= nextAllowed
     *
     * 5. Rejected call printed nothing.
     *
     *        -> update only on success
     *
     * Final:
     *
     *        message -> nextAllowedTime
     */

    /**
     * ==============================================================
     * 3. Optimal Solution (Interview Primary)
     * ==============================================================
     *
     * Invariant:
     *
     *     nextAllowedTime[message]
     *
     * is the earliest timestamp at which that message may print again,
     * based only on successful prints.
     *
     * Time : O(1) average per call
     * Space: O(M), M = distinct retained messages
     */
    static class Logger {

        private final Map<String, Integer> nextAllowedTime =
                new HashMap<>();

        public boolean shouldPrintMessage(
                int timestamp,
                String message) {

            int nextAllowed =
                    nextAllowedTime.getOrDefault(message, 0);

            if (timestamp < nextAllowed) {
                return false;
            }

            nextAllowedTime.put(
                    message,
                    timestamp + COOLDOWN_SECONDS);

            return true;
        }
    }

    /**
     * ==============================================================
     * 4. 30-Second Recall
     * ==============================================================
     *
     * PER-KEY COOLDOWN -> HASHMAP
     *
     *     key -> nextAllowed
     *
     *     if current < nextAllowed:
     *         reject
     *
     *     map[key] = current + cooldown
     *     accept
     *
     * Critical:
     *
     *     UPDATE ONLY ON ACCEPT
     *
     * Boundary:
     *
     *     current == nextAllowed -> ACCEPT
     */

    /**
     * ==============================================================
     * 5. Correctness + Traps
     * ==============================================================
     *
     * Successful print at t stores:
     *
     *     t + 10
     *
     * Therefore:
     *
     *     current <  t + 10 -> reject
     *     current >= t + 10 -> accept
     *
     * TRAP 1
     * ------
     * Updating state on rejection turns the rule into:
     *
     *     "10 seconds after latest arrival"
     *
     * instead of:
     *
     *     "10 seconds after latest successful print"
     *
     * TRAP 2
     * ------
     *
     *     timestamp > nextAllowed      // wrong
     *     timestamp >= nextAllowed     // correct
     *
     * TRAP 3
     * ------
     * Do not introduce Sliding Window for the original problem.
     *
     * One timestamp fully summarizes the needed history.
     */

    /**
     * ==============================================================
     * 6. Interview Articulation
     * ==============================================================
     *
     * "Each message has an independent cooldown, so I keep one HashMap
     * entry per message. The value is the earliest timestamp at which
     * it can print again. If the current timestamp is earlier, I reject
     * it. Otherwise I print and move the boundary to timestamp + 10.
     * Rejected calls do not update the map."
     */

    /**
     * ==============================================================
     * 7. Follow-Ups - Requirement Delta -> Code Delta
     * ==============================================================
     *
     * PRIMARY
     *
     *     Map<String, Integer> nextAllowedTime
     *
     * =================================================================
     * FOLLOW-UP                    + ADD / CHANGE        - REMOVE / SAME
     * =================================================================
     *
     * Configurable cooldown
     *
     *     + cooldownSeconds field
     *     - same HashMap and algorithm
     *
     * -----------------------------------------------------------------
     *
     * Different cooldown per key
     *
     *     + lookup cooldown(key)
     *     - same nextAllowed state
     *
     * -----------------------------------------------------------------
     *
     * N prints per key in W seconds
     *
     *     + Map<key, Deque<accepted timestamps>>
     *     + expire old timestamps
     *     + count active accepted events
     *     - one integer per key
     *
     *     Pattern becomes:
     *
     *         HashMap + Sliding Window / Deque
     *
     * -----------------------------------------------------------------
     *
     * Bound memory for infinite stream
     *
     *     + global expiry Queue
     *     + active Set
     *     + evict expired messages
     *     - permanent Map entries
     *
     *     Uses chronological timestamp guarantee.
     *
     * -----------------------------------------------------------------
     *
     * Concurrent callers
     *
     *     + ConcurrentHashMap
     *     + atomic check-and-update
     *     - plain get -> check -> put
     *
     * -----------------------------------------------------------------
     *
     * Distributed across servers
     *
     *     + shared external state
     *     + atomic conditional update
     *     + TTL / consistency policy
     *     - local JVM HashMap
     *
     * -----------------------------------------------------------------
     *
     * Rate limit by (user, message)
     *
     *     + composite key
     *     - same algorithm
     *
     * -----------------------------------------------------------------
     *
     * Need "seconds until allowed"
     *
     *     + return max(0, nextAllowed - timestamp)
     *     - same state
     *
     * -----------------------------------------------------------------
     *
     * Global N requests per W seconds
     *
     *     + one global Deque/counter
     *     - per-key independence
     *
     *     Different rate-limiter problem.
     *
     * -----------------------------------------------------------------
     *
     * Timestamps no longer chronological
     *
     *     + ordered/event-time cleanup semantics
     *     - FIFO expiry assumption
     *
     *     Only solve if interviewer removes the original guarantee.
     */

    /**
     * ==============================================================
     * 8. Follow-Up: Configurable Cooldown
     * ==============================================================
     *
     * Only the cooldown source changes.
     */
    static class ConfigurableCooldownLogger {

        private final int cooldownSeconds;

        private final Map<String, Integer> nextAllowedTime =
                new HashMap<>();

        ConfigurableCooldownLogger(int cooldownSeconds) {
            this.cooldownSeconds = cooldownSeconds;
        }

        public boolean shouldPrintMessage(
                int timestamp,
                String message) {

            int nextAllowed =
                    nextAllowedTime.getOrDefault(message, 0);

            if (timestamp < nextAllowed) {
                return false;
            }

            nextAllowedTime.put(
                    message,
                    timestamp + cooldownSeconds);

            return true;
        }
    }

    /**
     * ==============================================================
     * 9. Follow-Up: N Prints Per Key Per Window
     * ==============================================================
     *
     * Requirement:
     *
     *     Up to N accepted prints for the same key
     *     during any W-second window.
     *
     * Why state changes:
     *
     *     We must know how many accepted prints remain
     *     inside the active window.
     *
     * New state:
     *
     *     key -> deque of accepted timestamps
     *
     * Time : O(1) amortized
     */
    static class NPerWindowLogger {

        private final int maxPrints;
        private final int windowSeconds;

        private final Map<String, Deque<Integer>> acceptedByMessage =
                new HashMap<>();

        NPerWindowLogger(
                int maxPrints,
                int windowSeconds) {

            this.maxPrints = maxPrints;
            this.windowSeconds = windowSeconds;
        }

        public boolean shouldPrintMessage(
                int timestamp,
                String message) {

            Deque<Integer> accepted =
                    acceptedByMessage.computeIfAbsent(
                            message,
                            ignored -> new ArrayDeque<>());

            while (!accepted.isEmpty()
                    && accepted.peekFirst() + windowSeconds
                    <= timestamp) {

                accepted.removeFirst();
            }

            if (accepted.size() >= maxPrints) {
                return false;
            }

            accepted.addLast(timestamp);
            return true;
        }
    }

    /**
     * ==============================================================
     * 10. Follow-Up: Bounded Memory
     * ==============================================================
     *
     * Problem:
     *
     *     Primary HashMap retains keys forever.
     *
     * New requirement:
     *
     *     retain only currently blocked messages.
     *
     * Because input is chronological:
     *
     *     Queue -> what expires next
     *     Set   -> is message blocked now
     *
     * Time : O(1) amortized
     * Space: O(messages accepted during active cooldown)
     */
    static class BoundedMemoryLogger {

        private record Accepted(
                int timestamp,
                String message) {
        }

        private final Deque<Accepted> active =
                new ArrayDeque<>();

        private final Set<String> blocked =
                new HashSet<>();

        public boolean shouldPrintMessage(
                int timestamp,
                String message) {

            while (!active.isEmpty()
                    && active.peekFirst().timestamp()
                    + COOLDOWN_SECONDS <= timestamp) {

                blocked.remove(
                        active.removeFirst().message());
            }

            if (blocked.contains(message)) {
                return false;
            }

            blocked.add(message);

            active.addLast(
                    new Accepted(timestamp, message));

            return true;
        }
    }

    /**
     * ==============================================================
     * 11. Follow-Up: Concurrent Calls
     * ==============================================================
     *
     * ConcurrentHashMap alone is NOT enough.
     *
     * The whole:
     *
     *     read -> check -> update
     *
     * must be atomic.
     *
     * Otherwise two threads can both observe "allowed"
     * and both return true.
     */
    static class ConcurrentLogger {

        private final ConcurrentHashMap<String, Integer>
                nextAllowedTime = new ConcurrentHashMap<>();

        public boolean shouldPrintMessage(
                int timestamp,
                String message) {

            AtomicBoolean allowed =
                    new AtomicBoolean(false);

            nextAllowedTime.compute(
                    message,
                    (key, nextAllowed) -> {

                        if (nextAllowed != null
                                && timestamp < nextAllowed) {

                            return nextAllowed;
                        }

                        allowed.set(true);

                        return timestamp
                                + COOLDOWN_SECONDS;
                    });

            return allowed.get();
        }
    }

    /**
     * ==============================================================
     * 12. Follow-Up: Distributed Limiter
     * ==============================================================
     *
     * Local:
     *
     *     Map<String, Integer> nextAllowedTime
     *
     * becomes conceptually:
     *
     *     SharedRateLimitStore store
     *
     *     store.tryAcquire(
     *             message,
     *             timestamp,
     *             cooldown)
     *
     * tryAcquire must atomically:
     *
     *     READ boundary
     *     CHECK eligibility
     *     WRITE new boundary + expiry
     *
     * Separate network GET then SET is unsafe.
     *
     * Typical system-design direction:
     *
     *     Redis atomic operation / Lua
     *     dedicated rate-limit service
     *
     * This is a system-design follow-up, not necessary for LC359.
     */

    /**
     * ==============================================================
     * 13. Follow-Up: Composite Key
     * ==============================================================
     *
     * Requirement:
     *
     *     cooldown is independent per (user, message)
     *
     * Only the key changes.
     */
    static class PerUserLogger {

        private record Key(
                String userId,
                String message) {
        }

        private final Map<Key, Integer> nextAllowedTime =
                new HashMap<>();

        public boolean shouldPrintMessage(
                int timestamp,
                String userId,
                String message) {

            Key key =
                    new Key(userId, message);

            int nextAllowed =
                    nextAllowedTime.getOrDefault(key, 0);

            if (timestamp < nextAllowed) {
                return false;
            }

            nextAllowedTime.put(
                    key,
                    timestamp + COOLDOWN_SECONDS);

            return true;
        }
    }

    /**
     * ==============================================================
     * 14. Follow-Up Selection Rule
     * ==============================================================
     *
     * Ask:
     *
     *     "What NEW information must I remember?"
     *
     * Different cooldown only
     *     -> same Map<key, nextAllowed>
     *
     * Multiple accepted events in a window
     *     -> per-key Deque
     *
     * Need expiry / bounded memory
     *     -> chronological Queue + active Set/Map
     *
     * Multiple threads
     *     -> atomic check-and-update
     *
     * Multiple machines
     *     -> shared atomic state
     *
     * Core interview habit:
     *
     *     REQUIREMENT DELTA
     *         ->
     *     STATE DELTA
     *         ->
     *     CODE DELTA
     */

    /**
     * ==============================================================
     * 15. Alternative Optimal Representation
     * ==============================================================
     *
     * Equivalent:
     *
     *     message -> lastSuccessfulPrint
     *
     * allow when:
     *
     *     timestamp - lastSuccessfulPrint >= 10
     *
     * Same complexity.
     *
     * Prefer nextAllowedTime because it stores the comparison boundary
     * directly.
     */
    static class LastPrintedLogger {

        private final Map<String, Integer> lastPrintedTime =
                new HashMap<>();

        public boolean shouldPrintMessage(
                int timestamp,
                String message) {

            Integer lastPrinted =
                    lastPrintedTime.get(message);

            if (lastPrinted != null
                    && timestamp - lastPrinted
                    < COOLDOWN_SECONDS) {

                return false;
            }

            lastPrintedTime.put(message, timestamp);
            return true;
        }
    }

    /**
     * ==============================================================
     * 16. Pattern Transfer
     * ==============================================================
     *
     *     userId   -> nextAllowedRequestTime
     *     email    -> nextAllowedSendTime
     *     endpoint -> nextAllowedRetryTime
     *     jobId    -> nextAllowedExecutionTime
     *
     * Per-key eligibility after fixed cooldown
     *
     *     -> HashMap<key, nextAllowedTime>
     */

    /**
     * ==============================================================
     * 17. Mastery Checklist
     * ==============================================================
     *
     * [ ] Independent key -> HashMap.
     * [ ] One boundary summarizes original history.
     * [ ] Update only after acceptance.
     * [ ] t + 10 is legal.
     * [ ] N-per-window -> per-key Deque.
     * [ ] Bounded memory -> chronological expiry.
     * [ ] ConcurrentHashMap alone does not make compound logic atomic.
     * [ ] Requirement delta -> state delta -> code delta.
     */

    /**
     * ==============================================================
     * 18. Self-Verifying Tests
     * ==============================================================
     */
    public static void main(String[] args) {

        Logger logger = new Logger();

        assert logger.shouldPrintMessage(1, "foo");
        assert logger.shouldPrintMessage(2, "bar");
        assert !logger.shouldPrintMessage(3, "foo");
        assert !logger.shouldPrintMessage(8, "bar");
        assert !logger.shouldPrintMessage(10, "foo");
        assert logger.shouldPrintMessage(11, "foo");

        Logger boundary = new Logger();

        assert boundary.shouldPrintMessage(20, "a");
        assert !boundary.shouldPrintMessage(29, "a");
        assert boundary.shouldPrintMessage(30, "a");

        Logger noExtension = new Logger();

        assert noExtension.shouldPrintMessage(1, "x");
        assert !noExtension.shouldPrintMessage(5, "x");
        assert !noExtension.shouldPrintMessage(9, "x");
        assert noExtension.shouldPrintMessage(11, "x");

        ConfigurableCooldownLogger configurable =
                new ConfigurableCooldownLogger(5);

        assert configurable.shouldPrintMessage(1, "x");
        assert !configurable.shouldPrintMessage(5, "x");
        assert configurable.shouldPrintMessage(6, "x");

        NPerWindowLogger nPerWindow =
                new NPerWindowLogger(2, 10);

        assert nPerWindow.shouldPrintMessage(1, "x");
        assert nPerWindow.shouldPrintMessage(2, "x");
        assert !nPerWindow.shouldPrintMessage(3, "x");
        assert nPerWindow.shouldPrintMessage(11, "x");

        BoundedMemoryLogger bounded =
                new BoundedMemoryLogger();

        assert bounded.shouldPrintMessage(1, "foo");
        assert !bounded.shouldPrintMessage(10, "foo");
        assert bounded.shouldPrintMessage(11, "foo");

        ConcurrentLogger concurrent =
                new ConcurrentLogger();

        assert concurrent.shouldPrintMessage(1, "foo");
        assert !concurrent.shouldPrintMessage(2, "foo");
        assert concurrent.shouldPrintMessage(11, "foo");

        PerUserLogger perUser =
                new PerUserLogger();

        assert perUser.shouldPrintMessage(
                1, "u1", "foo");

        assert perUser.shouldPrintMessage(
                1, "u2", "foo");

        assert !perUser.shouldPrintMessage(
                2, "u1", "foo");

        System.out.println(
                "All Logger Rate Limiter tests passed.");
    }
}
