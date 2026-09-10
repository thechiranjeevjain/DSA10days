package org.chijai.exchange

import java.util.*;

/**
 * Matching Engine — 30 minute interview version.
 *
 * Write order:
 * 1. Order
 * 2. bids + asks
 * 3. enter()
 * 4. match incoming against opposite resting book
 * 5. rest remaining LIMIT quantity
 *
 * Invariant:
 * best price first, FIFO within same price.
 */
public class MatchingEngineInterview {

    enum Side { BUY, SELL }

    static class Order {
        final int id;
        final Side side;
        final int price;   // 0 = market
        int qty;

        Order(int id, Side side, int price, int qty) {
            this.id = id;
            this.side = side;
            this.price = price;
            this.qty = qty;
        }

        boolean isMarket() {
            return price == 0;
        }
    }

    record Trade(int price, int qty, int buyId, int sellId) {}

    static class OrderBook {

        final TreeMap<Integer, Deque<Order>> bids =
                new TreeMap<>(Comparator.reverseOrder());

        final TreeMap<Integer, Deque<Order>> asks =
                new TreeMap<>();

        final List<Trade> trades = new ArrayList<>();

        void enter(Order incoming) {
            if (incoming.qty <= 0) return;
            if (!incoming.isMarket() && incoming.price <= 0) return;

            TreeMap<Integer, Deque<Order>> opposite =
                    incoming.side == Side.BUY ? asks : bids;

            while (incoming.qty > 0 && !opposite.isEmpty()) {

                Map.Entry<Integer, Deque<Order>> best =
                        opposite.firstEntry();

                Order resting = best.getValue().peekFirst();

                if (!crosses(incoming, resting)) break;

                int qty = Math.min(incoming.qty, resting.qty);

                // Incoming trades at the resting order's price.
                int price = resting.price;

                incoming.qty -= qty;
                resting.qty -= qty;

                int buyId =
                        incoming.side == Side.BUY
                                ? incoming.id
                                : resting.id;

                int sellId =
                        incoming.side == Side.SELL
                                ? incoming.id
                                : resting.id;

                trades.add(new Trade(price, qty, buyId, sellId));

                if (resting.qty == 0) {
                    best.getValue().removeFirst();

                    if (best.getValue().isEmpty()) {
                        opposite.remove(best.getKey());
                    }
                }
            }

            // Market orders never rest.
            if (incoming.qty > 0 && !incoming.isMarket()) {
                TreeMap<Integer, Deque<Order>> own =
                        incoming.side == Side.BUY ? bids : asks;

                own.computeIfAbsent(
                                incoming.price,
                                ignored -> new ArrayDeque<>())
                        .addLast(incoming);
            }
        }

        boolean crosses(Order incoming, Order resting) {
            if (incoming.isMarket()) return true;

            return incoming.side == Side.BUY
                    ? incoming.price >= resting.price
                    : incoming.price <= resting.price;
        }
    }

    public static void main(String[] args) {
        OrderBook book = new OrderBook();

        book.enter(new Order(1, Side.SELL, 100, 50));
        book.enter(new Order(2, Side.BUY, 105, 20));

        System.out.println(book.trades);
        // trade price = 100 because the SELL was resting.
    }
}
