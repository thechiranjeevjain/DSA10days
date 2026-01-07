package org.chijai.day2.session1;

public class BinarySearch {

    // ============================================================
    // 📘 PROBLEM STATEMENT (SOURCE OF TRUTH — PRIMARY PROBLEM)
    // ============================================================

    /*
     * 🔵 LeetCode Problem: Binary Search
     *
     * 🔗 https://leetcode.com/problems/binary-search/
     *
     * 🧩 Difficulty: Easy
     * 🏷️ Tags: Array, Binary Search
     *
     * ------------------------------------------------------------
     * Official Problem Description:
     *
     * Given an array of integers nums which is sorted in ascending order,
     * and an integer target, write a function to search target in nums.
     *
     * If target exists, then return its index. Otherwise, return -1.
     *
     * You must write an algorithm with O(log n) runtime complexity.
     *
     * ------------------------------------------------------------
     * Example 1:
     * Input: nums = [-1,0,3,5,9,12], target = 9
     * Output: 4
     *
     * Explanation:
     * 9 exists in nums and its index is 4.
     *
     * ------------------------------------------------------------
     * Example 2:
     * Input: nums = [-1,0,3,5,9,12], target = 2
     * Output: -1
     *
     * Explanation:
     * 2 does not exist in nums so return -1.
     *
     * ------------------------------------------------------------
     * Constraints:
     *
     * 1 <= nums.length <= 10^4
     * -10^4 < nums[i], target < 10^4
     * All the integers in nums are unique.
     * nums is sorted in ascending order.
     */

    // ============================
    // 🔵 CORE PATTERN OVERVIEW
    // ============================

    /*
     * Pattern Name:
     * Binary Search on Monotonic Space
     *
     * Core Idea:
     * Use ordering guarantees to eliminate HALF of the remaining
     * search space at each step.
     *
     * Why It Works:
     * Sorted order creates a monotonic relationship:
     * - Everything left of mid is smaller
     * - Everything right of mid is larger
     *
     * When to Use:
     * - Sorted arrays
     * - Boundary finding
     * - “First / Last / Minimum / Maximum” questions
     *
     * Pattern Recognition Signals:
     * - “sorted”
     * - “log n”
     * - “find index / boundary”
     *
     * How This Differs from Similar Patterns:
     * - NOT two pointers (linear shrink)
     * - NOT sliding window (range maintenance)
     * - Binary search relies on monotonic elimination
     */

    // ----------------------------
    // 🟢 MENTAL MODEL & INVARIANTS
    // ----------------------------

    /*
     * Mental Model:
     * Think in terms of a CLOSED interval:
     *
     *   [ left .......... right ]
     *
     * At every step:
     * - mid is chosen INSIDE the interval
     * - one half is PROVEN impossible
     * - that half is discarded permanently
     *
     * Core Invariant (non-negotiable):
     * IF the target exists,
     * it must lie within nums[left..right]
     *
     * Variable Roles:
     * left  → start of valid search space
     * right → end of valid search space
     * mid   → decision pivot
     *
     * Termination:
     * Loop continues while left <= right
     * When left > right → space is empty → not found
     *
     * Forbidden Actions:
     * ❌ Looping on mid instead of interval
     * ❌ Computing mid without left offset
     * ❌ Failing to shrink the interval
     */

    // ============================================================
    // 🔴 WHY THE GIVEN SOLUTION WAS WRONG (FORENSIC ANALYSIS)
    // ============================================================

    /*
     * The original (wrong) solution used:
     *
     *   while (mid >= 0 && mid < n) {
     *       mid = (r + 1 - l) / 2;
     *   }
     *
     * ❌ Root Causes:
     *
     * 1️⃣ mid was NOT anchored to left
     *    Correct:
     *      mid = left + (right - left) / 2
     *
     * 2️⃣ Loop condition was wrong
     *    Binary search loops on SEARCH SPACE:
     *      while (left <= right)
     *
     * 3️⃣ Invariant was never enforced
     *    There was no guarantee the target
     *    stayed inside the active interval.
     *
     * Interview Verdict:
     * This is not a syntax bug.
     * It indicates missing understanding of invariants.
     */

    // ============================================================
    // 🟢 PRIMARY SOLUTION (INTERVIEW-PREFERRED)
    // ============================================================

    static class BinarySearchOptimal {

        static int search(int[] nums, int target) {
            int left = 0;
            int right = nums.length - 1;

            while (left <= right) {

                // 🟡 Anchoring mid to left preserves the invariant
                int mid = left + (right - left) / 2;

                if (nums[mid] == target) {
                    return mid;
                }

                if (nums[mid] > target) {
                    // 🟡 Discard right half including mid
                    right = mid - 1;
                } else {
                    // 🟡 Discard left half including mid
                    left = mid + 1;
                }
            }
            return -1;
        }
    }

    // ============================================================
    // 🟣 INTERVIEW ARTICULATION
    // ============================================================

    /*
     * One-liner explanation:
     * “I maintain an invariant that the target, if it exists,
     * must stay inside a shrinking interval.”
     *
     * Why it works:
     * Each comparison discards half the impossible space.
     *
     * When NOT to use:
     * - Unsorted data
     * - No monotonicity
     * - Linked lists
     */

    // ============================================================
    // 🔄 VARIATIONS & TWEAKS
    // ============================================================

    /*
     * Invariant-preserving:
     * - First occurrence
     * - Last occurrence
     *
     * Pattern-breaking:
     * - Rotated arrays
     * - Duplicate ambiguity
     */

    // ============================================================
    // ⚫ REINFORCEMENT PROBLEM 1 (FULL SUB-CHAPTER)
    // ============================================================

    /*
     * 🔵 LeetCode Problem: Search Insert Position
     *
     * 🔗 https://leetcode.com/problems/search-insert-position/
     *
     * 🧩 Difficulty: Easy
     * 🏷️ Tags: Array, Binary Search
     *
     * ------------------------------------------------------------
     * Problem Description:
     *
     * Given a sorted array of distinct integers and a target value,
     * return the index if the target is found.
     *
     * If not, return the index where it would be inserted in order.
     *
     * You must write an algorithm with O(log n) runtime complexity.
     *
     * ------------------------------------------------------------
     * Example 1:
     * Input: nums = [1,3,5,6], target = 5
     * Output: 2
     *
     * Example 2:
     * Input: nums = [1,3,5,6], target = 2
     * Output: 1
     *
     * Example 3:
     * Input: nums = [1,3,5,6], target = 7
     * Output: 4
     *
     * ------------------------------------------------------------
     * Constraints:
     * 1 <= nums.length <= 10^4
     * -10^4 <= nums[i], target <= 10^4
     */

    static class SearchInsertPosition {

        static int searchInsert(int[] nums, int target) {
            int left = 0;
            int right = nums.length - 1;

            while (left <= right) {
                int mid = left + (right - left) / 2;

                if (nums[mid] == target) {
                    return mid;
                }

                if (nums[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            // 🟡 Invariant guarantees left is insertion point
            return left;
        }
    }

    // ============================================================
    // ⚫ RELATED PROBLEM (BOUNDARY VARIANT)
    // ============================================================

    /*
     * 🔵 LeetCode Problem: First Bad Version
     *
     * 🔗 https://leetcode.com/problems/first-bad-version/
     *
     * 🧩 Difficulty: Easy
     * 🏷️ Tags: Binary Search
     *
     * ------------------------------------------------------------
     * Problem Description:
     *
     * You are a product manager and have n versions [1..n].
     * One version is bad, and all versions after it are also bad.
     *
     * Find the first bad version.
     *
     * You are given an API isBadVersion(version).
     *
     * ------------------------------------------------------------
     * Constraints:
     * 1 <= bad <= n <= 2^31 - 1
     */

    static class FirstBadVersion {

        // Simulated API
        static int firstBad = 4;

        static boolean isBadVersion(int version) {
            return version >= firstBad;
        }

        static int firstBadVersion(int n) {
            int left = 1;
            int right = n;

            while (left < right) {
                int mid = left + (right - left) / 2;

                if (isBadVersion(mid)) {
                    // 🟡 mid might be the answer — keep it
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            return left;
        }
    }

    // ============================================================
    // 🧪 TESTS (AUTOMATIC VERIFICATION)
    // ============================================================

    static void assertEquals(int expected, int actual, String testName) {
        if (expected != actual) {
            throw new AssertionError(
                    testName + " ❌ FAILED | expected=" + expected + ", actual=" + actual
            );
        }
        System.out.println(testName + " ✅ PASSED");
    }

    public static void main(String[] args) {

        // Binary Search
        assertEquals(
                4,
                BinarySearchOptimal.search(new int[]{-1,0,3,5,9,12}, 9),
                "BinarySearch: target exists"
        );

        assertEquals(
                -1,
                BinarySearchOptimal.search(new int[]{-1,0,3,5,9,12}, 2),
                "BinarySearch: target absent"
        );

        // Search Insert Position
        assertEquals(
                1,
                SearchInsertPosition.searchInsert(new int[]{1,3,5,6}, 2),
                "SearchInsert: middle insert"
        );

        // First Bad Version
        assertEquals(
                4,
                FirstBadVersion.firstBadVersion(10),
                "FirstBadVersion"
        );
    }
}
