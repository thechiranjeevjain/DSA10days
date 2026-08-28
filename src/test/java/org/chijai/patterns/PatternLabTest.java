package org.chijai.patterns;

import org.chijai.patterns.binarysearch.BinarySearchPatternLab;
import org.chijai.patterns.dynamicprogramming.DynamicProgrammingPatternLab;
import org.chijai.patterns.slidingwindow.SlidingWindowPatternLab;
import org.chijai.patterns.twopointers.TwoPointersPatternLab;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatternLabTest {
    @Test
    void binarySearchSkeletonsPreserveBoundaryInvariants() {
        assertEquals(2, BinarySearchPatternLab.lowerBound(new int[]{1, 3, 5, 7}, 4));
        assertEquals(2, BinarySearchPatternLab.firstTrue(0, 5, value -> value * value >= 4));
        assertEquals(4, BinarySearchPatternLab.minimumFeasible(1, 10, speed -> speed >= 4));
    }

    @Test
    void slidingWindowSkeletonsMaintainCurrentWindowState() {
        assertEquals(3, SlidingWindowPatternLab.longestAtMostKDistinct("eceba", 2));
        assertEquals(2, SlidingWindowPatternLab.minLengthSubarrayAtLeastTarget(7, new int[]{2, 3, 1, 2, 4, 3}));
        assertEquals(2, SlidingWindowPatternLab.countFixedWindowAnagrams("cbaebabacd", "abc"));
    }

    @Test
    void twoPointerSkeletonsShrinkOrCompactSearchSpace() {
        assertArrayEquals(new int[]{1, 3}, TwoPointersPatternLab.twoSumSortedZeroBased(new int[]{1, 2, 4, 6}, 8));
        assertTrue(TwoPointersPatternLab.isPalindromeIgnoringNonAlphanumeric("A man, a plan, a canal: Panama"));
        assertArrayEquals(new int[]{1, 3, 12, 0, 0}, TwoPointersPatternLab.moveZeroesStableCopy(new int[]{0, 1, 0, 3, 12}));
    }

    @Test
    void dynamicProgrammingSkeletonsNameStateBeforeTransition() {
        assertEquals(8, DynamicProgrammingPatternLab.climbStairs(5));
        assertEquals(12, DynamicProgrammingPatternLab.houseRobber(new int[]{2, 7, 9, 3, 1}));
        assertEquals(3, DynamicProgrammingPatternLab.coinChangeMinCoins(new int[]{1, 2, 5}, 11));
    }

    @Test
    void chapterTaxonomyIsVisibleFromPatternLabs() {
        assertEquals("Sliding Window", SlidingWindowPatternLab.chapter().topic());
        assertTrue(BinarySearchPatternLab.chapter().primaryHome().contains("First True Predicate"));
        assertTrue(DynamicProgrammingPatternLab.chapter().chapterFlow().contains("DEFEND"));
    }
}
