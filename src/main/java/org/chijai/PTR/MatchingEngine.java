package org.chijai.PTR;

import java.util.*;

/**
 * Matching Engine — 30 min CoderPad version
 *
 * Write order (say this out loud as you type):
 *   2 min  Order  — side, price, qty, who sent it
 *   3 min  OrderBook — two sorted lists, bids + asks
 *   5 min  enterOrder — validate → add → match
 *   8 min  match() — while loop, best bid vs best ask
 *   2 min  main()  — 3 test cases
 *
 * One sentence to interviewer before you start:
 * "Two sorted lists. Buyers highest price first, sellers lowest first.
 *  Every new order: validate, add to book, try to match."
 */
public class MatchingEngine {

    // --- Order -------------------------------------------------------
    // buyer or seller, price, how much is left to fill
    enum Side { BUY, SELL }

    static class Order {
        final int    id;
        final String trader;
        final Side   side;
        final int    price;      // 0 = market order
        int          qty;        // decreases as fills happen

        Order(int id, String trader, Side side, int price, int qty) {
            this.id = id; this.trader = trader;
            this.side = side; this.price = price; this.qty = qty;
        }

        boolean isMarket() { return price == 0; }

        @Override public String toString() {
            return trader + " " + side + " " + qty + " @" + (isMarket() ? "MKT" : price);
        }
    }

    // --- Trade -------------------------------------------------------
    // result when two orders match
    static class Trade {
        final int price, qty, buyId, sellId;
        Trade(int price, int qty, int buyId, int sellId) {
            this.price = price; this.qty = qty;
            this.buyId = buyId; this.sellId = sellId;
        }
        @Override public String toString() {
            return "TRADE  qty=" + qty + "  @" + price
                    + "  [buy#" + buyId + " x sell#" + sellId + "]";
        }
    }

    // --- OrderBook ---------------------------------------------------
    // bids : highest price first  (reverse order)
    // asks : lowest  price first  (natural order)
    // within same price: FIFO queue
    static class OrderBook {
        final TreeMap<Integer, Queue<Order>> bids = new TreeMap<>(Comparator.reverseOrder());
        final TreeMap<Integer, Queue<Order>> asks = new TreeMap<>();
        final List<Trade> trades = new ArrayList<>();

        String enterOrder(Order o) {
            // 1. basic validation
            if (!o.isMarket() && o.price <= 0) return "REJECTED: bad price";
            if (o.qty <= 0)                    return "REJECTED: bad qty";

            // 2. add to book
            add(o);

            // 3. try to match
            match();

            return "ACCEPTED";
        }

        // match() — the core loop
        // keep matching while best bid price >= best ask price
        private void match() {
            while (!bids.isEmpty() && !asks.isEmpty()) {
                Order bid = bids.firstEntry().getValue().peek();
                Order ask = asks.firstEntry().getValue().peek();

                int bidPrice = bid.isMarket() ? Integer.MAX_VALUE : bid.price;
                int askPrice = ask.isMarket() ? 0                 : ask.price;

                if (bidPrice < askPrice) break;   // spread not crossed — stop

                // execute at the resting (passive) side price
                // both are limit orders: whichever is crossing gets the better-priced fill
                // standard rule: ask < bid → sell aggressed into resting bid → bid.price
                //                bid > ask → buy  aggressed into resting ask → ask.price
                // market order: fill at the other side's limit
                int execPrice;
                if      (bid.isMarket()) execPrice = ask.price;
                else if (ask.isMarket()) execPrice = bid.price;
                else                     execPrice = bid.price; // bid was resting, sell aggressed
                int execQty   = Math.min(bid.qty, ask.qty);

                bid.qty -= execQty;
                ask.qty -= execQty;

                trades.add(new Trade(execPrice, execQty, bid.id, ask.id));

                if (bid.qty == 0) remove(bids, bid);
                if (ask.qty == 0) remove(asks, ask);
            }
        }

        private void add(Order o) {
            int key = o.isMarket()
                    ? (o.side == Side.BUY ? Integer.MAX_VALUE : 0)
                    : o.price;
            TreeMap<Integer, Queue<Order>> book = o.side == Side.BUY ? bids : asks;
            book.computeIfAbsent(key, k -> new LinkedList<>()).add(o);
        }

        private void remove(TreeMap<Integer, Queue<Order>> book, Order o) {
            int key = o.isMarket()
                    ? (o.side == Side.BUY ? Integer.MAX_VALUE : 0)
                    : o.price;
            Queue<Order> q = book.get(key);
            if (q != null) { q.poll(); if (q.isEmpty()) book.remove(key); }
        }

        void printBook() {
            String bid = bids.isEmpty() ? "  bids: empty"
                    : "  best bid: " + bids.firstKey();
            String ask = asks.isEmpty() ? "  asks: empty"
                    : "  best ask: " + asks.firstKey();
            System.out.println(bid + "   |   " + ask);
            trades.forEach(t -> System.out.println("  " + t));
        }
    }

    // --- main --------------------------------------------------------
    public static void main(String[] args) {
        OrderBook book = new OrderBook();
        int id = 1;

        // 1. orders rest on book — no match yet (spread = 150 bid, 155 ask)
        System.out.println("--- resting orders ---");
        System.out.println(book.enterOrder(new Order(id++, "Alice", Side.BUY,  150, 200)));
        System.out.println(book.enterOrder(new Order(id++, "Bob",   Side.BUY,  148, 300)));
        System.out.println(book.enterOrder(new Order(id++, "Alice", Side.SELL, 155, 100)));
        book.printBook();

        // 2. aggressive sell @ 149 crosses Alice's bid @ 150 → TRADE
        System.out.println("\n--- aggressive sell crosses spread ---");
        System.out.println(book.enterOrder(new Order(id++, "Bob", Side.SELL, 149, 150)));
        book.printBook();

        // 3. market order — fills immediately at best available price
        System.out.println("\n--- market buy fills at best ask ---");
        System.out.println(book.enterOrder(new Order(id++, "Carol", Side.BUY, 0, 50)));
        book.printBook();
    }
}
