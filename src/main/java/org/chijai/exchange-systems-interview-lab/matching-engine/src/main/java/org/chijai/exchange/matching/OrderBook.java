package org.chijai.exchange;

import java.util.*;

/**
 * Small price-time-priority order book.
 *
 * Incoming orders match existing resting orders first.
 * A remaining LIMIT order can rest on the book.
 * A MARKET remainder does not rest.
 */
public final class OrderBook {

    private final NavigableMap<Long, Deque<Order>> bids =
            new TreeMap<>(Comparator.reverseOrder());

    private final NavigableMap<Long, Deque<Order>> asks =
            new TreeMap<>();

    private final List<Trade> trades = new ArrayList<>();

    public List<Trade> enter(Order incoming) {
        int tradeStart = trades.size();

        NavigableMap<Long, Deque<Order>> opposite =
                incoming.side() == Side.BUY ? asks : bids;

        while (incoming.remainingQuantity() > 0
                && !opposite.isEmpty()) {

            Map.Entry<Long, Deque<Order>> bestLevel =
                    opposite.firstEntry();

            Order resting = bestLevel.getValue().peekFirst();

            if (!crosses(incoming, resting)) {
                break;
            }

            long executionQuantity =
                    Math.min(
                            incoming.remainingQuantity(),
                            resting.remainingQuantity());

            long executionPrice = resting.price();

            incoming.fill(executionQuantity);
            resting.fill(executionQuantity);

            long buyOrderId =
                    incoming.side() == Side.BUY
                            ? incoming.id()
                            : resting.id();

            long sellOrderId =
                    incoming.side() == Side.SELL
                            ? incoming.id()
                            : resting.id();

            trades.add(new Trade(
                    executionPrice,
                    executionQuantity,
                    buyOrderId,
                    sellOrderId));

            if (resting.remainingQuantity() == 0) {
                bestLevel.getValue().removeFirst();

                if (bestLevel.getValue().isEmpty()) {
                    opposite.remove(bestLevel.getKey());
                }
            }
        }

        if (incoming.remainingQuantity() > 0
                && !incoming.isMarket()) {
            rest(incoming);
        }

        return List.copyOf(
                trades.subList(tradeStart, trades.size()));
    }

    private boolean crosses(Order incoming, Order resting) {
        if (incoming.isMarket()) {
            return true;
        }

        return incoming.side() == Side.BUY
                ? incoming.price() >= resting.price()
                : incoming.price() <= resting.price();
    }

    private void rest(Order order) {
        NavigableMap<Long, Deque<Order>> ownBook =
                order.side() == Side.BUY ? bids : asks;

        ownBook.computeIfAbsent(
                        order.price(),
                        ignored -> new ArrayDeque<>())
                .addLast(order);
    }

    public OptionalLong bestBid() {
        return bids.isEmpty()
                ? OptionalLong.empty()
                : OptionalLong.of(bids.firstKey());
    }

    public OptionalLong bestAsk() {
        return asks.isEmpty()
                ? OptionalLong.empty()
                : OptionalLong.of(asks.firstKey());
    }

    public List<Trade> trades() {
        return List.copyOf(trades);
    }
}
