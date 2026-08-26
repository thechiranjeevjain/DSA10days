package org.chijai.day1.Arrays.session2;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Three3Sum2SumTest {

    @Test
    void threeSumSkipsDuplicatesAndFindsAllUniqueTriplets() {
        List<List<Integer>> actual = new Three3Sum2Sum.OptimalSolution()
                .threeSum(new int[]{-1, 0, 1, 2, -1, -4});

        assertEquals(
                Set.of(List.of(-1, -1, 2), List.of(-1, 0, 1)),
                new HashSet<>(actual)
        );
        assertEquals(2, actual.size());
    }

    @Test
    void twoSumIIUsesSortedTwoPointerInvariant() {
        assertArrayEquals(
                new int[]{1, 2},
                new Three3Sum2Sum.TwoSumIISolution().twoSum(new int[]{2, 7, 11, 15}, 9)
        );
    }
}
