package org.chijai.day5.stack.session1.monotonic;

import java.util.*;

public class OnlineStockSpan {

    /**
     * LeetCode 901 - Online Stock Span
     *
     * Pattern: Monotonic decreasing stack + span compression.
     *
     * Stack entry = (price, span).
     *
     * Invariant:
     * Stack prices are strictly decreasing from bottom to top.
     *
     * next(price):
     * 1. Today's span starts at 1.
     * 2. While top.price <= today's price:
     *      absorb that whole compressed block:
     *      span += top.span
     * 3. Push (price, span).
     *
     * Why compression works:
     * If (75,4) is on top, those 4 consecutive days are all <= 75.
     * If today is 85, then all 4 are also <= 85, so add 4 at once.
     *
     * Complexity:
     * - One call worst case: O(n)
     * - Amortized next(): O(1)
     * - Total across n calls: O(n)
     * - Space: O(n)
     *
     * Memory hook:
     * "Today's bigger/equal price eats smaller/equal previous prices."
     */
    static class StockSpanner {

        private record PriceSpan(int price, int span) {}

        private final Deque<PriceSpan> stack = new ArrayDeque<>();

        public int next(int price) {
            int span = 1; // today always counts

            while (!stack.isEmpty() && stack.peek().price() <= price) {
                span += stack.pop().span();
            }

            stack.push(new PriceSpan(price, span));
            return span;
        }
    }

    private static void assertEquals(int expected, int actual, String name) {
        if (expected != actual) {
            throw new AssertionError(
                    name + " FAILED: expected=" + expected + ", actual=" + actual
            );
        }
        System.out.println("PASS: " + name + " -> " + actual);
    }

    private static void testLeetCodeExample() {
        System.out.println("\n=== Test 1: LeetCode Example ===");
        StockSpanner s = new StockSpanner();

        assertEquals(1, s.next(100), "100");
        assertEquals(1, s.next(80), "80");
        assertEquals(1, s.next(60), "60");
        assertEquals(2, s.next(70), "70");
        assertEquals(1, s.next(60), "60 again");
        assertEquals(4, s.next(75), "75");
        assertEquals(6, s.next(85), "85");
    }

    private static void testIncreasing() {
        System.out.println("\n=== Test 2: Increasing Prices ===");
        StockSpanner s = new StockSpanner();

        assertEquals(1, s.next(10), "10");
        assertEquals(2, s.next(20), "20");
        assertEquals(3, s.next(30), "30");
        assertEquals(4, s.next(40), "40");
        assertEquals(5, s.next(50), "50");
    }

    private static void testDecreasing() {
        System.out.println("\n=== Test 3: Decreasing Prices ===");
        StockSpanner s = new StockSpanner();

        assertEquals(1, s.next(50), "50");
        assertEquals(1, s.next(40), "40");
        assertEquals(1, s.next(30), "30");
        assertEquals(1, s.next(20), "20");
    }

    private static void testEqualPrices() {
        System.out.println("\n=== Test 4: Equal Prices ===");
        StockSpanner s = new StockSpanner();

        assertEquals(1, s.next(100), "first 100");
        assertEquals(2, s.next(100), "second 100");
        assertEquals(3, s.next(100), "third 100");
    }

    private static void testPromptExample() {
        System.out.println("\n=== Test 5: Prompt Example [7,2,1,2] ===");
        StockSpanner s = new StockSpanner();

        assertEquals(1, s.next(7), "7");
        assertEquals(1, s.next(2), "2");
        assertEquals(1, s.next(1), "1");
        assertEquals(3, s.next(2), "final 2");
    }

    private static void testGreaterBarrier() {
        System.out.println("\n=== Test 6: Greater Barrier ===");
        StockSpanner s = new StockSpanner();

        // Before today's 8, history is [7,34,1,2].
        s.next(7);
        s.next(34);
        s.next(1);
        s.next(2);

        // Going backward from 8:
        // 2 <= 8, 1 <= 8, but 34 > 8 => stop.
        assertEquals(3, s.next(8), "8 stopped by 34");
    }

    private static void testNewMaximum() {
        System.out.println("\n=== Test 7: New Global Maximum ===");
        StockSpanner s = new StockSpanner();

        s.next(100);
        s.next(80);
        s.next(120);

        assertEquals(4, s.next(150), "150 spans entire history");
    }

    public static void main(String[] args) {
        testLeetCodeExample();
        testIncreasing();
        testDecreasing();
        testEqualPrices();
        testPromptExample();
        testGreaterBarrier();
        testNewMaximum();

        System.out.println("\nALL TESTS PASSED");
    }
}
