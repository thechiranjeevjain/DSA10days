package org.chijai.java;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PriceTimePriority {

    enum Side {
        BUY,
        SELL
    }

    static class Order {
        private final long orderId;
        private final Side side;
        private final double price;
        private final int quantity;
        private final long arrivalTime;

        Order(long orderId,
              Side side,
              double price,
              int quantity,
              long arrivalTime) {

            this.orderId = orderId;
            this.side = side;
            this.price = price;
            this.quantity = quantity;
            this.arrivalTime = arrivalTime;
        }

        public long getOrderId() {
            return orderId;
        }

        public Side getSide() {
            return side;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public long getArrivalTime() {
            return arrivalTime;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "id=" + orderId +
                    ", side=" + side +
                    ", price=" + price +
                    ", quantity=" + quantity +
                    ", arrivalTime=" + arrivalTime +
                    '}';
        }
    }

    static class OrderBook {

        /*
         * BUY PRIORITY:
         *
         * 1. Higher price first
         * 2. If same price -> earlier arrival first
         *
         * Example:
         *
         * BUY 101 @ time 5
         * BUY 101 @ time 2
         * BUY 100 @ time 1
         *
         * Priority:
         *
         * BUY 101 @ time 2
         * BUY 101 @ time 5
         * BUY 100 @ time 1
         */
        private final PriorityQueue<Order> buyOrders =
                new PriorityQueue<>(
                        Comparator
                                .comparingDouble(Order::getPrice)
                                .reversed()
                                .thenComparingLong(Order::getArrivalTime)
                                .thenComparingLong(Order::getOrderId)
                );

        /*
         * SELL PRIORITY:
         *
         * 1. Lower price first
         * 2. If same price -> earlier arrival first
         *
         * Example:
         *
         * SELL 99 @ time 5
         * SELL 99 @ time 2
         * SELL 100 @ time 1
         *
         * Priority:
         *
         * SELL 99 @ time 2
         * SELL 99 @ time 5
         * SELL 100 @ time 1
         */
        private final PriorityQueue<Order> sellOrders =
                new PriorityQueue<>(
                        Comparator
                                .comparingDouble(Order::getPrice)
                                .thenComparingLong(Order::getArrivalTime)
                                .thenComparingLong(Order::getOrderId)
                );

        public void addOrder(Order order) {
            if (order.getSide() == Side.BUY) {
                buyOrders.offer(order);
            } else {
                sellOrders.offer(order);
            }
        }

        /*
         * Best BUY:
         *
         * Highest price.
         * Earliest arrival wins tie.
         */
        public Order getNextBuy() {
            return buyOrders.peek();
        }

        /*
         * Best SELL:
         *
         * Lowest price.
         * Earliest arrival wins tie.
         */
        public Order getNextSell() {
            return sellOrders.peek();
        }

        /*
         * Determine whether the best buy and best sell can trade.
         *
         * Trade condition:
         *
         * bestBuy.price >= bestSell.price
         */
        public boolean canExecute() {

            if (buyOrders.isEmpty() || sellOrders.isEmpty()) {
                return false;
            }

            return buyOrders.peek().getPrice()
                    >= sellOrders.peek().getPrice();
        }

        /*
         * For this simplified interview problem,
         * remove the two highest-priority crossing orders.
         *
         * Real order books must additionally handle:
         *
         * - partial fills
         * - remaining quantity
         * - execution price
         * - cancellations
         * - modifications
         */
        public void executeNext() {

            if (!canExecute()) {
                System.out.println("No executable pair.");
                return;
            }

            Order buy = buyOrders.poll();
            Order sell = sellOrders.poll();

            int executedQuantity =
                    Math.min(buy.getQuantity(), sell.getQuantity());

            /*
             * Simplification:
             *
             * Execute at resting/older order price.
             *
             * Exact execution-price rules depend on
             * the exchange / matching engine.
             */
            double executionPrice;

            if (buy.getArrivalTime() < sell.getArrivalTime()) {
                executionPrice = buy.getPrice();
            } else {
                executionPrice = sell.getPrice();
            }

            System.out.println("\nEXECUTION");
            System.out.println("Buy  : " + buy);
            System.out.println("Sell : " + sell);
            System.out.println("Qty  : " + executedQuantity);
            System.out.println("Price: " + executionPrice);
        }

        public void printTopOfBook() {

            System.out.println("\n========== TOP OF BOOK ==========");

            System.out.println(
                    "Best BUY  : " +
                            (buyOrders.isEmpty()
                                    ? "NONE"
                                    : buyOrders.peek())
            );

            System.out.println(
                    "Best SELL : " +
                            (sellOrders.isEmpty()
                                    ? "NONE"
                                    : sellOrders.peek())
            );

            System.out.println("=================================");
        }
    }

    public static void main(String[] args) {

        OrderBook orderBook = new OrderBook();

        List<Order> orders = List.of(

                // BUY orders
                new Order(
                        1,
                        Side.BUY,
                        100.00,
                        100,
                        1
                ),

                new Order(
                        2,
                        Side.BUY,
                        101.00,
                        100,
                        2
                ),

                // Same price as order 2,
                // but arrived later.
                new Order(
                        3,
                        Side.BUY,
                        101.00,
                        50,
                        3
                ),

                new Order(
                        4,
                        Side.BUY,
                        99.00,
                        200,
                        4
                ),

                // SELL orders
                new Order(
                        5,
                        Side.SELL,
                        103.00,
                        100,
                        5
                ),

                new Order(
                        6,
                        Side.SELL,
                        101.00,
                        75,
                        6
                ),

                // Same sell price,
                // but arrived later.
                new Order(
                        7,
                        Side.SELL,
                        101.00,
                        100,
                        7
                ),

                new Order(
                        8,
                        Side.SELL,
                        102.00,
                        150,
                        8
                )
        );

        for (Order order : orders) {
            orderBook.addOrder(order);
        }

        orderBook.printTopOfBook();

        System.out.println("\nNext BUY according to price-time priority:");
        System.out.println(orderBook.getNextBuy());

        System.out.println("\nNext SELL according to price-time priority:");
        System.out.println(orderBook.getNextSell());

        /*
         * Best BUY:
         *
         * Order 2
         * price = 101
         * time = 2
         *
         * beats Order 3 because both have price 101
         * but Order 2 arrived earlier.
         *
         *
         * Best SELL:
         *
         * Order 6
         * price = 101
         * time = 6
         *
         * beats Order 7 because both have price 101
         * but Order 6 arrived earlier.
         */

        if (orderBook.canExecute()) {
            orderBook.executeNext();
        } else {
            System.out.println("\nOrders do not cross.");
        }

        orderBook.printTopOfBook();
    }
}

/*
============================================================
PRICE-TIME PRIORITY — INTERVIEW RECALL
============================================================


PROBLEM
------------------------------------------------------------

Given BUY and SELL orders with:

price
arrival time

determine which order gets execution priority.


This is the fundamental matching priority used by
many electronic order books.


============================================================
THE RULE
============================================================


BUY:

HIGHER PRICE wins.

If price ties:

EARLIER TIME wins.



SELL:

LOWER PRICE wins.

If price ties:

EARLIER TIME wins.



Mental shortcut:

BUY  -> MAX PRICE -> MIN TIME

SELL -> MIN PRICE -> MIN TIME


============================================================
EXAMPLE — BUY SIDE
============================================================

Orders:

BUY A: 100 @ time 1
BUY B: 102 @ time 5
BUY C: 102 @ time 2
BUY D: 101 @ time 0


Priority:

C
B
D
A


Why?

First compare PRICE.

102 beats 101 and 100.

Then between B and C:

same price = 102

C arrived earlier.

Therefore:

C before B.


============================================================
EXAMPLE — SELL SIDE
============================================================

Orders:

SELL A: 103 @ time 1
SELL B: 101 @ time 5
SELL C: 101 @ time 2
SELL D: 102 @ time 0


Priority:

C
B
D
A


Why?

For SELL:

LOWER PRICE is better.

101 beats 102 and 103.

Between B and C:

same price.

Earlier arrival wins.

Therefore C wins.


============================================================
THE TWO COMPARATORS
============================================================


BUY:

Comparator
    .comparingDouble(Order::getPrice)
    .reversed()
    .thenComparingLong(Order::getArrivalTime);


SELL:

Comparator
    .comparingDouble(Order::getPrice)
    .thenComparingLong(Order::getArrivalTime);


============================================================
MUG-UP VERSION
============================================================

BUY:

price DESC
time  ASC


SELL:

price ASC
time  ASC


That's the entire price-time rule.


============================================================
DATA STRUCTURE
============================================================

Two priority queues:

PriorityQueue<Order> buys;

PriorityQueue<Order> sells;


BUY heap:

highest price
earliest time


SELL heap:

lowest price
earliest time


Then:

buys.peek()

=

best bid


sells.peek()

=

best ask


============================================================
TOP OF BOOK
============================================================

Best BUY:

BID


Best SELL:

ASK


Therefore:

bestBid = buyOrders.peek()

bestAsk = sellOrders.peek()


============================================================
WHEN CAN ORDERS EXECUTE?
============================================================

Orders CROSS when:

bestBid >= bestAsk


Example:

BUY 101

SELL 100

Buyer is willing to pay up to 101.

Seller is willing to sell for 100.

Therefore trade is possible.


------------------------------------------------------------

No trade:

BUY 99

SELL 100


Buyer maximum:

99

Seller minimum:

100


No overlap.


============================================================
CORE MATCH CONDITION
============================================================

while (
    !buys.isEmpty()
    && !sells.isEmpty()
    && buys.peek().price >= sells.peek().price
) {

    match();
}


This is one of the most important order-book
retrieval patterns.


============================================================
COMPLEXITY
============================================================

PriorityQueue.offer():

O(log n)


PriorityQueue.poll():

O(log n)


peek():

O(1)


Therefore inserting / removing an order:

O(log n)


Space:

O(n)


============================================================
IMPORTANT INTERVIEW CATCH
============================================================

PriorityQueue is good for:

"Give me the next highest-priority order."


But it is NOT sufficient for a real production
order book.


Why?


Suppose interviewer asks:

Cancel order ID 12345.


PriorityQueue cannot efficiently locate arbitrary
order 12345.


Searching it would be:

O(n)


Production order books usually need additional
index structures.


============================================================
PRODUCTION ORDER BOOK MENTAL MODEL
============================================================

Typical conceptual structure:


BUY SIDE

price levels DESC

101
    -> order A
    -> order B
    -> order C

100
    -> order D
    -> order E

99
    -> order F


SELL SIDE

price levels ASC

102
    -> order X
    -> order Y

103
    -> order Z


Within each price level:

FIFO queue


Therefore price-time priority naturally becomes:

ordered prices
+
FIFO per price


============================================================
WHY FIFO PER PRICE?
============================================================

All orders at the same price have equal
price priority.

Therefore only TIME matters.

Example:

Price = 101

A arrives 10:00:01
B arrives 10:00:02
C arrives 10:00:03


Queue:

A -> B -> C


This is exactly:

FIFO


============================================================
BETTER REAL-WORLD DATA STRUCTURE
============================================================

Conceptually:


BUY:

TreeMap<Price, Queue<Order>>

with prices descending.


SELL:

TreeMap<Price, Queue<Order>>

with prices ascending.


Plus:

Map<OrderId, Order>


for fast cancellation / lookup.


============================================================
PRICE-TIME AS TWO-LEVEL ORDERING
============================================================

Do not mentally treat it as one complicated rule.

Think:


LEVEL 1

PRICE PRIORITY


        ↓


LEVEL 2

TIME PRIORITY


Price decides the queue.

Time decides position inside that queue.


============================================================
REAL MATCHING EXAMPLE
============================================================

Existing SELL book:

100 -> S1(50)
       S2(100)

101 -> S3(200)


Incoming:

BUY 101 quantity 120


It crosses SELL 100.

First execution:

BUY vs S1

50 shares


Remaining BUY:

70


Then:

BUY vs S2

70 shares


S2 originally had:

100


Remaining S2:

30


The BUY is now fully filled.


Notice:

S1 executes before S2

because:

same price
+
S1 arrived earlier.


============================================================
PARTIAL FILLS
============================================================

Real matching requires:

executedQty =
    min(
        buy.remainingQty,
        sell.remainingQty
    );


Then subtract from both.


If:

remainingQty == 0

remove order.


Otherwise:

keep remaining quantity
at SAME priority position.


Very important:

A partial fill normally does NOT cause the
resting order to lose its original time priority.


============================================================
ORDER MODIFICATION
============================================================

Interview extension:

"What happens if an order changes price?"


Usually conceptually:

cancel old order
+
insert new order


because changing price means entering a
different price level.


Many markets also treat quantity increases
as losing time priority.

Exact rules are venue-specific.


============================================================
ARRIVAL TIME CATCH
============================================================

Production matching engines generally should NOT rely
on ordinary wall-clock timestamps to break ties.

Instead they often have deterministic ordering such as:

monotonic sequence number

or

engine-assigned arrival sequence


Example:

arrivalSequence =

10001
10002
10003


Why?

Two orders could have identical timestamps.

A sequence number gives a deterministic total order.


============================================================
BETTER FIELD NAME IN PRODUCTION
============================================================

Instead of:

long arrivalTime


often think:

long arrivalSequence


Then:

smaller sequence

=

arrived earlier.


============================================================
PRICE REPRESENTATION CATCH
============================================================

For interview simplicity this example uses:

double price


For a financial matching engine this is usually
NOT ideal.


Avoid floating-point money representation because:

0.1 + 0.2 != exactly 0.3


Better:

long priceInTicks


Example:

₹101.25

tick size = ₹0.05


Represent price as:

2025 ticks


or use another exact fixed-point representation.


============================================================
LOW-LATENCY VERSION
============================================================

A production low-latency matching engine may avoid:

PriorityQueue
TreeMap
object-heavy structures


because of:

allocations
pointer chasing
cache misses
GC
branching


Instead it may use:

array-indexed price levels
intrusive linked lists
primitive collections
preallocated objects
ring buffers


depending on the bounded price domain and
latency requirements.


But the LOGICAL invariant remains unchanged:


BEST PRICE FIRST

THEN

EARLIEST ORDER AT THAT PRICE


============================================================
CORE INVARIANT
============================================================

For BUY:

No order behind the head may have:

a better price

OR

the same price with an earlier arrival.


For SELL:

same invariant with lower price being better.


============================================================
INTERVIEW GOLDEN ANSWER
============================================================

"Price-time priority is lexicographic ordering.

For buys, I prioritize higher prices and then earlier
arrival times.

For sells, I prioritize lower prices and then earlier
arrival times.

For a simple implementation I can use two priority
queues.

For a real order book, I would normally model ordered
price levels with FIFO queues inside each level and
maintain an order-ID index for efficient cancellation."


============================================================
MUG-UP CODE
============================================================

BUY:

Comparator
    .comparingDouble(Order::getPrice)
    .reversed()
    .thenComparingLong(Order::getArrivalTime);


SELL:

Comparator
    .comparingDouble(Order::getPrice)
    .thenComparingLong(Order::getArrivalTime);


============================================================
FASTEST RETRIEVAL
============================================================

PRICE-TIME PRIORITY

BUY:
MAX PRICE
MIN TIME

SELL:
MIN PRICE
MIN TIME


                  BUY
                   |
          highest price first
                   |
             earliest time


                  SELL
                   |
           lowest price first
                   |
             earliest time


============================================================
ONE-LINE MEMORY HOOK
============================================================

BUY  = PRICE ↓, TIME ↑

SELL = PRICE ↑, TIME ↑


where:

↓ = descending
↑ = ascending


============================================================
TRADING INTERVIEW FOLLOW-UPS
============================================================

Be ready for:

1. How do you handle partial fills?

2. How do you cancel an order efficiently?

3. What happens on order modification?

4. Why not use double for price?

5. How do you guarantee deterministic ordering?

6. What happens if two orders have the same timestamp?

7. How would you implement O(1) cancellation?

8. PriorityQueue vs TreeMap<Price, FIFO>?

9. How would you reduce allocations?

10. How would you implement the matching loop?


============================================================
RETRIEVAL TRIGGER
============================================================

"Order-book priority?"

Think immediately:

BUY  -> highest price -> FIFO
SELL -> lowest price  -> FIFO

PRICE LEVEL
+
TIME QUEUE


============================================================
*/