package org.chijai.day2.session1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinarySearchTest {

    @Test
    void binarySearchReturnsIndexWhenTargetExists() {
        assertEquals(
                4,
                BinarySearch.BinarySearchOptimal.search(new int[]{-1, 0, 3, 5, 9, 12}, 9)
        );
    }

    @Test
    void binarySearchReturnsMinusOneWhenTargetIsAbsent() {
        assertEquals(
                -1,
                BinarySearch.BinarySearchOptimal.search(new int[]{-1, 0, 3, 5, 9, 12}, 2)
        );
    }

    @Test
    void searchInsertReturnsBoundaryPosition() {
        assertEquals(1, BinarySearch.SearchInsertPosition.searchInsert(new int[]{1, 3, 5, 6}, 2));
        assertEquals(4, BinarySearch.SearchInsertPosition.searchInsert(new int[]{1, 3, 5, 6}, 7));
    }
}
