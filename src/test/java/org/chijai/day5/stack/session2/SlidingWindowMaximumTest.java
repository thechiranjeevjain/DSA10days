package org.chijai.day5.stack.session2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SlidingWindowMaximumTest {

    private final SlidingWindowMaximum solution = new SlidingWindowMaximum();

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
}
