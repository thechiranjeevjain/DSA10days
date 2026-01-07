package org.chijai.day1.session1;

import java.util.Arrays;

/**
 * LeetCode 75 – Sort Colors
 *
 * Problem:
 * Given an array nums with values 0, 1, 2
 * Sort them in-place so that objects of the same color are adjacent.
 *
 * Constraints:
 * - In-place
 * - No library sort
 *
 * ------------------------------------------------------------
 * 4️⃣ POINTER ROLES (FIXED FOREVER)
 * ------------------------------------------------------------
 *
 * leftBoundary
 *   - Marks the END of the LEFT confirmed zone
 *   - Everything before this index is already correct
 *
 * scanIndex
 *   - Scans the UNKNOWN zone
 *   - The ONLY pointer allowed to inspect new elements
 *
 * rightBoundary
 *   - Marks the START of the RIGHT confirmed zone
 *   - Everything after this index is already correct
 *
 * Each pointer has EXACTLY ONE job.
 * Never mix responsibilities.
 *
 * ------------------------------------------------------------
 * 5️⃣ THE ONLY 3 LEGAL ACTIONS
 * ------------------------------------------------------------
 *
 * When inspecting nums[scanIndex]:
 *
 * 1. Element belongs to LEFT zone
 *    -> swap(scanIndex, leftBoundary)
 *    -> move BOTH scanIndex and leftBoundary forward
 *
 * 2. Element belongs to MID zone
 *    -> element is already in correct middle position
 *    -> move scanIndex only
 *
 * 3. Element belongs to RIGHT zone
 *    -> swap(scanIndex, rightBoundary)
 *    -> move rightBoundary backward ONLY
 *
 * 🔥 No other moves are valid.
 *
 * ------------------------------------------------------------
 * 6️⃣ THE GOLDEN INVARIANT (MEMORIZE THIS)
 * ------------------------------------------------------------
 *
 * - Everything LEFT of leftBoundary is correct
 * - Everything RIGHT of rightBoundary is correct
 * - ONLY scanIndex may touch the UNKNOWN zone
 *
 * If this sentence remains true,
 * the algorithm is guaranteed correct.
 *
 * ------------------------------------------------------------
 * 7️⃣ THE MOST COMMON BUG (ALWAYS CHECK)
 * ------------------------------------------------------------
 *
 * ❌ Incrementing scanIndex after swapping with rightBoundary
 *
 * Why this is wrong:
 * - The element swapped from the RIGHT zone is UNKNOWN
 * - Skipping it violates the invariant
 *
 * Mental check:
 * “Did I just introduce an unknown?”
 * If YES → do NOT move scanIndex
 *
 * ------------------------------------------------------------
 * 8️⃣ WHEN TO STOP
 * ------------------------------------------------------------
 *
 * Stop when:
 *   scanIndex > rightBoundary
 *
 * Meaning:
 * - UNKNOWN zone is empty
 * - All elements are in confirmed zones
 * - Partitioning is complete
 */

public class SortColors {

    /* ============================================================
     * SOLUTION 1: COUNTING SORT (Two Pass)
     * ============================================================
     *
     * Idea:
     * - Count how many 0s, 1s, 2s
     * - Overwrite the array in order
     *
     * Why interviewers accept it:
     * - O(n) time
     * - O(1) extra space (fixed size array)
     *
     * Limitation:
     * - Requires TWO passes
     * - Does NOT demonstrate in-place partitioning skills
     */
    static class CountingSortSolution {
        public void sortColors(int[] nums) {
            int[] count = new int[3];

            // First pass: count occurrences
            for (int num : nums) {
                count[num]++;
            }

            // Second pass: overwrite array
            int index = 0;
            while (count[0]-- > 0) nums[index++] = 0;
            while (count[1]-- > 0) nums[index++] = 1;
            while (count[2]-- > 0) nums[index++] = 2;
        }
    }

    /* ============================================================
     * SOLUTION 2: DUTCH NATIONAL FLAG (One Pass)
     * ============================================================
     *
     * Core Idea:
     * Maintain 3 regions:
     * [0 ... low-1]   -> all 0s
     * [low ... i-1]   -> all 1s
     * [high+1 ... n]  -> all 2s
     *
     * Pointer roles:
     * low  -> next position for 0
     * i    -> current element under inspection
     * high -> next position for 2
     *
     * Why interviewers LOVE this:
     * - One pass
     * - In-place
     * - Tests understanding of invariants and pointer control
     * - Core building block of QuickSort partitioning
     */
    static class DutchNationalFlagSolution {
        public void sortColors(int[] nums) {
            int low = 0;
            int i = 0;
            int high = nums.length - 1;

            while (i <= high) {
                if (nums[i] == 0) {
                    swap(nums, i++, low++);
                } else if (nums[i] == 1) {
                    i++;
                } else { // nums[i] == 2
                    // Do NOT increment i here
                    // because swapped value needs re-evaluation
                    swap(nums, i, high--);
                }
            }
        }

    }

    /* ============================================================
     * 2️⃣ Move Zeroes (LeetCode 283)
     * Two-way stable partition
     * ============================================================
     *
     * Zones:
     * [0 ... nextNonZeroIndex-1] -> non-zero
     * [nextNonZeroIndex ... end] -> zero / unknown
     */
    static class MoveZeroes {
        public void moveZeroes(int[] nums) {
            int nextNonZeroIndex = 0;

            for (int currentIndex = 0; currentIndex < nums.length; currentIndex++) {
                if (nums[currentIndex] != 0) {
                    swap(nums, currentIndex, nextNonZeroIndex++);
                }
            }
        }
    }

    /* ============================================================
     * 3️⃣ Partition Array According to Given Pivot (IN-PLACE)
     * Dutch National Flag with variable pivot
     * ============================================================
     *
     * Zones maintained:
     * [0 ... lessThanPivotEnd-1]        -> < pivot
     * [lessThanPivotEnd ... equalEnd-1] -> == pivot
     * [currentIndex ... greaterStart]   -> unknown
     * [greaterStart+1 ... end]          -> > pivot
     */
    static class PartitionByPivot {

        public void partitionByPivot(int[] nums, int pivot) {
            int lessThanPivotEnd = 0;
            int currentIndex = 0;
            int greaterThanPivotStart = nums.length - 1;

            while (currentIndex <= greaterThanPivotStart) {

                if (nums[currentIndex] < pivot) {
                    swap(nums, currentIndex++, lessThanPivotEnd++);

                } else if (nums[currentIndex] == pivot) {
                    currentIndex++;

                } else { // nums[currentIndex] > pivot
                    // DO NOT increment currentIndex here
                    swap(nums, currentIndex, greaterThanPivotStart--);
                }
            }
        }
    }

    /* ============================================================
     * 4️⃣ Sort Array by Parity (LeetCode 905)
     * Two-way partition (even / odd)
     * ============================================================
     *
     * Zones:
     * [ even | unknown | odd ]
     */
    static class SortByParity {
        public int[] sortArrayByParity(int[] nums) {
            int left = 0;
            int right = nums.length - 1;

            while (left < right) {
                if (nums[left] % 2 == 0) {
                    left++;
                } else {
                    swap(nums, left, right--);
                }
            }
            return nums;
        }
    }

    /* ============================================================
     * 5️⃣ QuickSort-style 3-way Partition (foundation problem)
     * Used in QuickSort / QuickSelect / Kth Largest
     * ============================================================
     *
     * Zones:
     * [ < pivot | == pivot | unknown | > pivot ]
     */
    static class ThreeWayPartition {
        public void partition(int[] nums, int pivot) {
            int lessThanIndex = 0;
            int currentIndex = 0;
            int greaterThanIndex = nums.length - 1;

            while (currentIndex <= greaterThanIndex) {
                if (nums[currentIndex] < pivot) {
                    swap(nums, currentIndex++, lessThanIndex++);
                } else if (nums[currentIndex] == pivot) {
                    currentIndex++;
                } else {
                    swap(nums, currentIndex, greaterThanIndex--);
                }
            }
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /* ============================================================
     * TEST RUNNER (IntelliJ Runnable)
     * ============================================================
     *
     * Right-click → Run SortColorsDemo.main()
     */
    public static void main(String[] args) {

        int[][] testCases = {

                // 1️⃣ Empty array
                // Edge case: no elements, algorithm should not crash
                {},

                // 2️⃣ Single element arrays
                // Minimum valid input size
                {0},
                {1},
                {2},

                // 3️⃣ All elements same
                // Idempotency check – array should remain unchanged
                {0, 0, 0},
                {1, 1, 1},
                {2, 2, 2},

                // 4️⃣ Two-element arrays
                // Boundary cases for pointer movement
                {0, 2},
                {2, 0},
                {1, 0},

                // 5️⃣ Missing one color
                // Ensures algorithm does not assume all 3 values exist
                {0, 0, 1, 1},
                {1, 1, 2, 2},
                {0, 0, 2, 2},

                // 6️⃣ Typical mixed case
                // Standard example from problem statement
                {2, 0, 2, 1, 1, 0},

                // 7️⃣ Small mixed input
                // Verifies correctness on short arrays
                {2, 0, 1},

                // 8️⃣ Already sorted array
                // Best case – should not break invariants
                {0, 0, 1, 1, 2, 2},

                // 9️⃣ Reverse sorted array
                // Worst ordering for partition-based logic
                {2, 2, 1, 1, 0, 0},

                // 🔟 Alternating pattern
                // Stress test for Dutch National Flag pointer logic
                {2, 0, 2, 0, 2, 0},
                {0, 2, 0, 2, 0, 2}
        };


        System.out.println("=== COUNTING SORT RESULTS ===");
        CountingSortSolution counting = new CountingSortSolution();
        for (int[] test : testCases) {
            int[] copy = Arrays.copyOf(test, test.length);
            counting.sortColors(copy);
            System.out.println(Arrays.toString(copy));
        }

        System.out.println("\n=== DUTCH NATIONAL FLAG RESULTS ===");
        DutchNationalFlagSolution dnf = new DutchNationalFlagSolution();
        for (int[] test : testCases) {
            int[] copy = Arrays.copyOf(test, test.length);
            dnf.sortColors(copy);
            System.out.println(Arrays.toString(copy));
        }

        // ---- Move Zeroes ----
        int[] moveZeroes = {0, 1, 0, 3, 12};
        new MoveZeroes().moveZeroes(moveZeroes);
        System.out.println("Move Zeroes: " + Arrays.toString(moveZeroes));

        // ---- Partition by Pivot (IN-PLACE DNF) ----
        int[] pivotInput = {9, 12, 5, 10, 14, 3, 10};
        new PartitionByPivot().partitionByPivot(pivotInput, 10);
        System.out.println("Partition by Pivot (In-place): " + Arrays.toString(pivotInput));

        // ---- Sort by Parity ----
        int[] parity = {3, 1, 2, 4};
        new SortByParity().sortArrayByParity(parity);
        System.out.println("Sort by Parity: " + Arrays.toString(parity));

        // ---- Three-way Partition ----
        int[] partition = {4, 9, 4, 5, 3, 4, 8};
        new ThreeWayPartition().partition(partition, 4);
        System.out.println("Three-way Partition: " + Arrays.toString(partition));
    }
}
