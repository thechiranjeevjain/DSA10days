package org.chijai.day7.session1.heap;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class TopKFrequentElementsTest {

    private final TopKFrequentElements solution = new TopKFrequentElements();

    @Test
    void solvesOfficialExample() {
        assertSameElements(
                new int[]{1, 2},
                solution.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2));
    }

    @Test
    void handlesOneValue() {
        assertSameElements(
                new int[]{1},
                solution.topKFrequent(new int[]{1}, 1));
    }

    @Test
    void handlesNegativeValues() {
        assertSameElements(
                new int[]{-1, 4},
                solution.topKFrequent(new int[]{4, 4, -1, -1, -1, 2}, 2));
    }

    @Test
    void baselineAndOptimalAgree() {
        int[] nums = {5, 5, 5, 4, 4, 3, 2, 2};
        int k = 3;
        assertSameElements(
                new TopKFrequentElements.SortingBaseline().topKFrequent(nums, k),
                new TopKFrequentElements.MinHeapSolution().topKFrequent(nums, k));
    }

    private static void assertSameElements(int[] expected, int[] actual) {
        Arrays.sort(expected);
        Arrays.sort(actual);
        assertArrayEquals(expected, actual);
    }
}
