package org.chijai.day3.session1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SlidingWindowMaximumTest {

    private final SlidingWindowMaximum solution = new SlidingWindowMaximum();
    private final SlidingWindowMaximum.BruteForce bruteForce =
            new SlidingWindowMaximum.BruteForce();
    private final SlidingWindowMaximum.Optimal optimal =
            new SlidingWindowMaximum.Optimal();

    @Test
    void solvesOfficialExample() {
        assertArrayEquals(
                new int[]{3, 3, 5, 5, 6, 7},
                solution.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
    }

    @Test
    void handlesSingleElementWindow() {
        assertArrayEquals(
                new int[]{1},
                solution.maxSlidingWindow(new int[]{1}, 1));
    }

    @Test
    void handlesWindowOfOneAndDuplicateMaximums() {
        assertArrayEquals(
                new int[]{4, 4, 2},
                solution.maxSlidingWindow(new int[]{4, 4, 2}, 1));
        assertArrayEquals(
                new int[]{4, 4},
                solution.maxSlidingWindow(new int[]{4, 4, 2}, 2));
    }

    @Test
    void baselineAndOptimalAgreeOnRepresentativeCases() {
        assertArrayEquals(
                bruteForce.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3),
                optimal.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
        assertArrayEquals(
                bruteForce.maxSlidingWindow(new int[]{-4, -2, -5}, 2),
                optimal.maxSlidingWindow(new int[]{-4, -2, -5}, 2));
        assertArrayEquals(
                bruteForce.maxSlidingWindow(new int[]{7, 7, 7}, 3),
                optimal.maxSlidingWindow(new int[]{7, 7, 7}, 3));
    }
}
