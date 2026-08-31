package org.chijai.day1.Arrays.session1;

import java.util.Arrays;

/**
 * Sort Colors / Partition Family — V3
 *
 * Motto:
 * Learn one invariant, one pointer shape, reuse everywhere.
 *
 * Core family:
 *
 * 2-way partition:
 *
 *     [ accepted | unknown ]
 *
 * 3-way partition / Dutch National Flag:
 *
 *     [ LEFT | MIDDLE | UNKNOWN | RIGHT ]
 *
 * Reusable question at every step:
 *
 *     Where does nums[scan] belong?
 *
 * Then move only the pointers justified by that answer.
 */
public class SortColors {

    /*
     * ============================================================
     * 📘 PRIMARY PROBLEM — LEETCODE 75: SORT COLORS
     * ============================================================
     *
     * nums contains only:
     *
     * 0, 1, 2
     *
     * Sort in-place without using library sort.
     *
     * Example:
     *
     * [2,0,2,1,1,0]
     *
     * ->
     *
     * [0,0,1,1,2,2]
     *
     * Target:
     *
     * Time  : O(n)
     * Space : O(1)
     */

    /*
     * ============================================================
     * 🧠 ONE REUSABLE DNF MENTAL MODEL
     * ============================================================
     *
     * Maintain four regions:
     *
     * [  LEFT  |  MIDDLE  |  UNKNOWN  |  RIGHT  ]
     *    0s         1s                   2s
     *
     * Using:
     *
     * low   -> next position where a LEFT element belongs
     * scan  -> current UNKNOWN element being inspected
     * high  -> next position where a RIGHT element belongs
     *
     * Exact invariant:
     *
     * [0 ... low-1]
     *     confirmed LEFT / 0
     *
     * [low ... scan-1]
     *     confirmed MIDDLE / 1
     *
     * [scan ... high]
     *     UNKNOWN
     *
     * [high+1 ... n-1]
     *     confirmed RIGHT / 2
     *
     * ------------------------------------------------------------
     * ONLY THREE LEGAL ACTIONS
     * ------------------------------------------------------------
     *
     * nums[scan] belongs LEFT:
     *
     *     swap(scan, low)
     *     low++
     *     scan++
     *
     * Why scan++ is safe:
     *
     * the value coming from low is already from the confirmed
     * MIDDLE region (or low == scan), so no new unknown appears.
     *
     * ------------------------------------------------------------
     *
     * nums[scan] belongs MIDDLE:
     *
     *     scan++
     *
     * It is already in the correct region.
     *
     * ------------------------------------------------------------
     *
     * nums[scan] belongs RIGHT:
     *
     *     swap(scan, high)
     *     high--
     *
     * DO NOT scan++.
     *
     * Why?
     *
     * The value arriving from high came from the UNKNOWN region.
     * It has not been inspected yet.
     *
     * Mental rule:
     *
     *     "Did the swap bring me an unknown?"
     *
     * YES -> keep scan where it is.
     *
     * ------------------------------------------------------------
     * STOP
     * ------------------------------------------------------------
     *
     * while (scan <= high)
     *
     * When scan > high:
     *
     * UNKNOWN is empty.
     */

    /*
     * ============================================================
     * 📈 APPROACH PROGRESSION
     * ============================================================
     *
     * 1. COUNTING
     *
     * Count 0 / 1 / 2, then overwrite.
     *
     * Time  : O(n)
     * Space : O(1)
     * Passes: 2
     *
     * ------------------------------------------------------------
     *
     * 2. DUTCH NATIONAL FLAG
     *
     * Partition directly while scanning.
     *
     * Time  : O(n)
     * Space : O(1)
     * Passes: 1
     *
     * Interview-preferred when the follow-up asks:
     *
     *     "Can you do it in one pass?"
     */

    static class CountingSortSolution {

        public void sortColors(int[] nums) {

            int[] count = new int[3];

            for (int num : nums) {
                count[num]++;
            }

            int index = 0;

            while (count[0]-- > 0) {
                nums[index++] = 0;
            }

            while (count[1]-- > 0) {
                nums[index++] = 1;
            }

            while (count[2]-- > 0) {
                nums[index++] = 2;
            }
        }
    }

    static class DutchNationalFlagSolution {

        public void sortColors(int[] nums) {

            int low = 0;
            int scan = 0;
            int high = nums.length - 1;

            while (scan <= high) {

                if (nums[scan] == 0) {

                    swap(nums, scan, low);
                    low++;
                    scan++;

                } else if (nums[scan] == 1) {

                    scan++;

                } else {

                    // Swapped-in value is still UNKNOWN.
                    swap(nums, scan, high);
                    high--;
                }
            }
        }
    }

    /*
     * ============================================================
     * ♻️ GENERIC 3-WAY PARTITION
     * ============================================================
     *
     * Sort Colors is just:
     *
     * pivot = 1
     *
     * LEFT   -> value < pivot
     * MIDDLE -> value == pivot
     * RIGHT  -> value > pivot
     *
     * Same exact code shape:
     *
     * < pivot
     *     swap(scan, low)
     *     low++
     *     scan++
     *
     * == pivot
     *     scan++
     *
     * > pivot
     *     swap(scan, high)
     *     high--
     *
     * This is the partitioning idea behind 3-way QuickSort and
     * useful around QuickSelect when duplicates are present.
     */

    static class PartitionByPivot {

        public void partitionByPivot(int[] nums, int pivot) {

            int low = 0;
            int scan = 0;
            int high = nums.length - 1;

            while (scan <= high) {

                if (nums[scan] < pivot) {

                    swap(nums, scan, low);
                    low++;
                    scan++;

                } else if (nums[scan] == pivot) {

                    scan++;

                } else {

                    // Swapped-in value is UNKNOWN.
                    swap(nums, scan, high);
                    high--;
                }
            }
        }
    }

    /*
     * ============================================================
     * ♻️ REUSABLE 3-WAY PARTITION RANGE HELPER
     * ============================================================
     *
     * Same invariant, now restricted to a subarray:
     *
     * [left ... low-1]      < pivot
     * [low ... scan-1]      == pivot
     * [scan ... high]       UNKNOWN
     * [high+1 ... right]    > pivot
     *
     * Returns:
     *
     * [equalStart, equalEnd]
     *
     * so callers know exactly where the == pivot block ended up.
     */
    private static int[] partitionRange(int[] nums,
                                        int left,
                                        int right,
                                        int pivot) {

        int low = left;
        int scan = left;
        int high = right;

        while (scan <= high) {

            if (nums[scan] < pivot) {

                swap(nums, scan, low);
                low++;
                scan++;

            } else if (nums[scan] == pivot) {

                scan++;

            } else {

                // Swapped-in value is UNKNOWN.
                swap(nums, scan, high);
                high--;
            }
        }

        return new int[]{low, high};
    }

    /*
     * ============================================================
     * ⚡ 3-WAY QUICKSORT
     * ============================================================
     *
     * Same DNF partition.
     *
     * After partitioning:
     *
     * [ < pivot | == pivot | > pivot ]
     *
     * The == pivot block is already finished forever.
     *
     * Therefore recurse only on:
     *
     * left side
     * right side
     *
     * This is especially useful when many duplicate values exist.
     *
     * Average time:
     * O(n log n)
     *
     * Worst case:
     * O(n^2) with poor pivot choices.
     *
     * Extra space:
     * recursion stack.
     */
    static class ThreeWayQuickSort {

        public void sort(int[] nums) {
            quickSort(nums, 0, nums.length - 1);
        }

        private void quickSort(int[] nums, int left, int right) {

            if (left >= right) {
                return;
            }

            int pivot = nums[left + (right - left) / 2];

            int[] equal = partitionRange(nums, left, right, pivot);

            int equalStart = equal[0];
            int equalEnd = equal[1];

            quickSort(nums, left, equalStart - 1);
            quickSort(nums, equalEnd + 1, right);
        }
    }

    /*
     * ============================================================
     * 🎯 QUICKSELECT — KTH LARGEST
     * ============================================================
     *
     * Again, SAME 3-way partition:
     *
     * [ < pivot | == pivot | > pivot ]
     *
     * Convert kth largest into zero-based sorted index:
     *
     * target = nums.length - k
     *
     * After partition:
     *
     * target < equalStart
     *     -> answer lies LEFT
     *
     * target > equalEnd
     *     -> answer lies RIGHT
     *
     * target inside [equalStart, equalEnd]
     *     -> pivot itself is the answer
     *
     * Unlike QuickSort:
     *
     * recurse / continue into ONLY ONE side.
     *
     * Average time:
     * O(n)
     *
     * Worst case:
     * O(n^2)
     *
     * Extra space:
     * O(1) here because this implementation is iterative.
     */
    static class QuickSelectKthLargest {

        public int findKthLargest(int[] nums, int k) {

            if (k < 1 || k > nums.length) {
                throw new IllegalArgumentException("k out of range");
            }

            int target = nums.length - k;

            int left = 0;
            int right = nums.length - 1;

            while (left <= right) {

                int pivot = nums[left + (right - left) / 2];

                int[] equal = partitionRange(nums, left, right, pivot);

                int equalStart = equal[0];
                int equalEnd = equal[1];

                if (target < equalStart) {

                    right = equalStart - 1;

                } else if (target > equalEnd) {

                    left = equalEnd + 1;

                } else {

                    return nums[target];
                }
            }

            throw new IllegalStateException("Unreachable");
        }
    }

    /*
     * ============================================================
     * 🔗 ONE LEARNING CHAIN
     * ============================================================
     *
     * Sort Colors
     *
     *     0 | 1 | unknown | 2
     *
     *          ↓ generalize values
     *
     * 3-way partition
     *
     *     < pivot | == pivot | unknown | > pivot
     *
     *          ↓ recurse BOTH sides
     *
     * 3-way QuickSort
     *
     *          ↓ keep only ONE relevant side
     *
     * QuickSelect / Kth Largest
     *
     * Same partition engine throughout.
     *
     * The only thing that changes is:
     *
     * WHAT DO I DO AFTER PARTITIONING?
     *
     * Sort Colors:
     *     done after one full partition
     *
     * QuickSort:
     *     solve both outer regions
     *
     * QuickSelect:
     *     continue only into region containing target index
     */

    /*
     * ============================================================
     * 2-WAY PARTITION — MOVE ZEROES
     * ============================================================
     *
     * Different but related shape:
     *
     * [ confirmed non-zero | unknown ]
     *
     * write -> next slot for a non-zero
     * scan  -> inspect every element
     *
     * If nums[scan] is accepted:
     *
     *     swap(scan, write)
     *     write++
     *
     * Because write never passes scan, and every position before
     * write already contains a non-zero, relative order of
     * non-zero values is preserved.
     */

    static class MoveZeroes {

        public void moveZeroes(int[] nums) {

            int write = 0;

            for (int scan = 0; scan < nums.length; scan++) {

                if (nums[scan] != 0) {
                    swap(nums, scan, write);
                    write++;
                }
            }
        }
    }

    /*
     * ============================================================
     * 2-WAY PARTITION — SORT ARRAY BY PARITY
     * ============================================================
     *
     * Desired regions:
     *
     * [ EVEN | UNKNOWN | ODD ]
     *
     * Unlike Move Zeroes, stability is NOT required.
     *
     * left  -> search for misplaced odd from the left
     * right -> search for misplaced even from the right
     *
     * When both are misplaced:
     *
     *     swap(left, right)
     */

    static class SortByParity {

        public int[] sortArrayByParity(int[] nums) {

            int left = 0;
            int right = nums.length - 1;

            while (left < right) {

                if (nums[left] % 2 == 0) {

                    left++;

                } else if (nums[right] % 2 != 0) {

                    right--;

                } else {

                    swap(nums, left, right);
                    left++;
                    right--;
                }
            }

            return nums;
        }
    }

    /*
     * ============================================================
     * 🎯 INTERVIEW RECALL
     * ============================================================
     *
     * Trigger:
     *
     * "partition in-place"
     * "three categories"
     * "< pivot / == pivot / > pivot"
     * "0 / 1 / 2"
     *
     * ------------------------------------------------------------
     *
     * 3-way invariant:
     *
     * [ LEFT | MIDDLE | UNKNOWN | RIGHT ]
     *
     * ------------------------------------------------------------
     *
     * Pointer roles:
     *
     * low  -> next LEFT slot
     * scan -> inspect UNKNOWN
     * high -> next RIGHT slot
     *
     * ------------------------------------------------------------
     *
     * Moves:
     *
     * LEFT:
     * swap(scan, low)
     * low++
     * scan++
     *
     * MIDDLE:
     * scan++
     *
     * RIGHT:
     * swap(scan, high)
     * high--
     *
     * ------------------------------------------------------------
     *
     * Most important bug:
     *
     * NEVER increment scan after swapping with high.
     *
     * Reason:
     *
     * high gave us an UNKNOWN value.
     *
     * ------------------------------------------------------------
     *
     * One-liner:
     *
     * "Only advance scan when the value now at scan is known."
     */

    /*
     * ============================================================
     * 🧭 RELATED PROBLEMS — WHAT IS ACTUALLY THE SAME FAMILY?
     * ============================================================
     *
     * DIRECT SAME PATTERN:
     *
     * Sort Colors
     * 3-Way Partition Around Pivot
     * ThreeWayQuickSort
     * QuickSelectKthLargest
     *
     * ------------------------------------------------------------
     *
     * RELATED 2-WAY PARTITION:
     *
     * Move Zeroes
     * Sort Array By Parity
     * Move negatives to one side
     * Partition by predicate
     *
     * ------------------------------------------------------------
     *
     * DO NOT CONFUSE WITH:
     *
     * Two Sum / sorted two pointers
     * Sliding window
     *
     * They may also use two pointers,
     * but their invariant and pointer jobs are different.
     */

    private static void swap(int[] nums, int i, int j) {

        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /*
     * ============================================================
     * 🧪 SELF-VERIFYING TESTS
     * ============================================================
     */

    private static void assertArrayEquals(int[] expected,
                                          int[] actual,
                                          String reason) {

        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(
                    reason +
                    "\nExpected: " + Arrays.toString(expected) +
                    "\nActual:   " + Arrays.toString(actual)
            );
        }
    }

    private static void assertSortedColors(int[] input,
                                           int[] expected) {

        int[] counting = Arrays.copyOf(input, input.length);
        new CountingSortSolution().sortColors(counting);

        assertArrayEquals(
                expected,
                counting,
                "Counting sort failed"
        );

        int[] dnf = Arrays.copyOf(input, input.length);
        new DutchNationalFlagSolution().sortColors(dnf);

        assertArrayEquals(
                expected,
                dnf,
                "Dutch National Flag failed"
        );
    }

    private static void assertPivotPartition(int[] nums, int pivot) {

        boolean seenEqual = false;
        boolean seenGreater = false;

        for (int num : nums) {

            if (num < pivot) {

                if (seenEqual || seenGreater) {
                    throw new AssertionError(
                            "Invalid pivot partition: " +
                            Arrays.toString(nums)
                    );
                }

            } else if (num == pivot) {

                seenEqual = true;

                if (seenGreater) {
                    throw new AssertionError(
                            "Invalid pivot partition: " +
                            Arrays.toString(nums)
                    );
                }

            } else {

                seenGreater = true;
            }
        }
    }

    private static void assertIntEquals(int expected,
                                        int actual,
                                        String reason) {

        if (expected != actual) {
            throw new AssertionError(
                    reason +
                    "\nExpected: " + expected +
                    "\nActual:   " + actual
            );
        }
    }

    private static void assertParityPartition(int[] nums) {

        boolean seenOdd = false;

        for (int num : nums) {

            if (num % 2 != 0) {
                seenOdd = true;

            } else if (seenOdd) {

                throw new AssertionError(
                        "Even value found after odd region: " +
                        Arrays.toString(nums)
                );
            }
        }
    }

    public static void main(String[] args) {

        // --------------------------------------------------------
        // Sort Colors
        // --------------------------------------------------------

        assertSortedColors(
                new int[]{},
                new int[]{}
        );

        assertSortedColors(
                new int[]{2},
                new int[]{2}
        );

        assertSortedColors(
                new int[]{2, 0, 2, 1, 1, 0},
                new int[]{0, 0, 1, 1, 2, 2}
        );

        assertSortedColors(
                new int[]{2, 0, 1},
                new int[]{0, 1, 2}
        );

        assertSortedColors(
                new int[]{0, 0, 1, 1, 2, 2},
                new int[]{0, 0, 1, 1, 2, 2}
        );

        assertSortedColors(
                new int[]{2, 2, 1, 1, 0, 0},
                new int[]{0, 0, 1, 1, 2, 2}
        );

        assertSortedColors(
                new int[]{2, 0, 2, 0, 2, 0},
                new int[]{0, 0, 0, 2, 2, 2}
        );

        // --------------------------------------------------------
        // Move Zeroes
        // --------------------------------------------------------

        int[] moveZeroes = {0, 1, 0, 3, 12};

        new MoveZeroes().moveZeroes(moveZeroes);

        assertArrayEquals(
                new int[]{1, 3, 12, 0, 0},
                moveZeroes,
                "Move Zeroes failed"
        );

        // --------------------------------------------------------
        // Generic 3-way pivot partition
        // --------------------------------------------------------

        int[] pivotInput = {9, 12, 5, 10, 14, 3, 10};

        new PartitionByPivot().partitionByPivot(pivotInput, 10);

        assertPivotPartition(pivotInput, 10);

        // --------------------------------------------------------
        // Sort by parity
        // --------------------------------------------------------

        int[] parity = {3, 1, 2, 4};

        new SortByParity().sortArrayByParity(parity);

        assertParityPartition(parity);

        // --------------------------------------------------------
        // 3-way QuickSort
        // --------------------------------------------------------

        int[] quickSortInput = {4, 9, 4, 5, 3, 4, 8, 1, 4};

        new ThreeWayQuickSort().sort(quickSortInput);

        assertArrayEquals(
                new int[]{1, 3, 4, 4, 4, 4, 5, 8, 9},
                quickSortInput,
                "Three-way QuickSort failed"
        );

        // Duplicate-heavy input: where 3-way partition shines.
        int[] duplicateHeavy = {5, 3, 5, 5, 2, 5, 1, 5, 4};

        new ThreeWayQuickSort().sort(duplicateHeavy);

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 5, 5, 5, 5},
                duplicateHeavy,
                "Duplicate-heavy QuickSort failed"
        );

        // --------------------------------------------------------
        // QuickSelect — kth largest
        // --------------------------------------------------------

        assertIntEquals(
                5,
                new QuickSelectKthLargest().findKthLargest(
                        new int[]{3, 2, 1, 5, 6, 4},
                        2
                ),
                "QuickSelect 2nd largest failed"
        );

        assertIntEquals(
                4,
                new QuickSelectKthLargest().findKthLargest(
                        new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6},
                        4
                ),
                "QuickSelect duplicate case failed"
        );

        System.out.println("All SortColorsV3 assertions passed.");
    }
}
