package org.chijai.day2.session1;

/**
 * =================================================================================================
 * ALGORITHM TEXTBOOK CHAPTER
 * Pattern: Binary Search on Boundaries (Lower / Upper Bound)
 * =================================================================================================
 * <p>
 * PURPOSE
 * -------
 * This file is a COMPLETE, SELF-CONTAINED algorithm chapter.
 * <p>
 * After reading THIS FILE ALONE, you should be able to:
 * • Master the boundary binary search pattern
 * • Re-derive solutions months later
 * • Explain correctness invariants in interviews
 * • Handle follow-up and trick questions
 * • Teach this pattern to others
 * <p>
 * ⚠️ Read strictly TOP → BOTTOM
 * =================================================================================================
 */
public class SearchRange {

    // =============================================================================================
    // 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
    // =============================================================================================

    /*
     * 34. Find First and Last Position of Element in Sorted Array
     *
     * Given an array of integers nums sorted in non-decreasing order,
     * find the starting and ending position of a given target value.
     *
     * If target is not found in the array, return [-1, -1].
     *
     * You must write an algorithm with O(log n) runtime complexity.
     *
     * Example 1:
     * Input: nums = [5,7,7,8,8,10], target = 8
     * Output: [3,4]
     *
     * Example 2:
     * Input: nums = [5,7,7,8,8,10], target = 6
     * Output: [-1,-1]
     *
     * Example 3:
     * Input: nums = [], target = 0
     * Output: [-1,-1]
     *
     * Constraints:
     * 0 <= nums.length <= 10^5
     * -10^9 <= nums[i] <= 10^9
     * nums is a non-decreasing array.
     * -10^9 <= target <= 10^9
     *
     * 🔗 https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Array, Binary Search
     */

    // =============================================================================================
    // 3️⃣ 🔵 CORE PATTERN OVERVIEW
    // =============================================================================================


    /*
     * 🔵 Pattern Name:
     * Binary Search on Boundaries (Lower Bound / Upper Bound)
     *
     * 🔵 Core Idea:
     * Binary search is used to find the FIRST index where a condition becomes true,
     * not merely to find a value.
     *
     * 🔵 Why It Works:
     * Sorted arrays induce monotonic predicates of the form:
     *   false false false true true true
     *
     * 🔵 When To Use:
     * - First or last occurrence
     * - Range queries
     * - Insert position
     * - Counting duplicates
     *
     * 🔵 Pattern Recognition Signals:
     * - "first", "last", "leftmost", "rightmost"
     * - "range"
     * - "smallest index such that..."
     *
     * 🔵 How It Differs from Normal Binary Search:
     * - Normal BS stops at equality
     * - Boundary BS continues to tighten after equality
     * *
     * 🔵 What Binary Search Actually Needs:
     * - A way to divide the search space into two halves
     * - A rule that lets us DISCARD one half with certainty
     *
     */

    // =============================================================================================
    // 4️⃣ 🟢 MENTAL MODEL & INVARIANTS
    // =============================================================================================

    /*
     * 🟢 Mental Model:
     * You are searching for a BOUNDARY where a predicate flips.
     *
     * 🟢 Core Invariants:
     * - Search space strictly shrinks
     * - Left side violates predicate
     * - Right side satisfies predicate
     *
     * 🟢 Variable Roles:
     * left   → earliest possible boundary
     * right  → latest possible violation
     * answer → best boundary found so far
     *
     * 🟢 Termination Logic:
     * Loop ends when all candidates are exhausted.
     *
     * 🟢 Forbidden Actions:
     * ❌ Linear expansion from mid
     * ❌ Breaking on first equality
     *
     * 🟢 Why Alternatives Are Inferior:
     * Linear scanning violates O(log n) guarantee.
     */

    // =============================================================================================
    // 5️⃣ 🔴 WHY NAIVE / WRONG SOLUTIONS FAIL (FORENSIC)
    // =============================================================================================

    /*
     * 🔴 Common Wrong Approach:
     * 1. Binary search to find target
     * 2. Expand left and right linearly
     *
     * 🔴 Why It Looks Correct:
     * - Simple
     * - Passes many tests
     *
     * 🔴 Exact Invariant Violated:
     * Worst-case time complexity becomes O(n)
     *
     * 🔴 Counterexample:
     * nums = [8,8,8,8,8,8,8,8]
     *
     * 🔴 Interview Trap:
     * "What if all elements are the same?"
     */

    /*
     * 🔴 Case Study: A Very Common but Incorrect Candidate Approach
     *
     * Candidate Idea:
     * “Use binary search to find the target once,
     * then expand left and right to find the full range.”
     *
     * Representative Code (Simplified):
     *
     * if (nums[mid] == target) {
     *     int first = mid;
     *     int last = mid;
     *     while (nums[first] == target) first--;
     *     while (nums[last] == target) last++;
     * }
     *
     * 🔴 Why This Seems Reasonable:
     * - Binary search is used
     * - Expansion feels local and cheap
     * - Works on many test cases
     *
     * 🔴 Why This Thinking Is Fundamentally Flawed:
     * 1. Linear expansion destroys O(log n) worst-case guarantee
     * 2. Binary search invariants are abandoned after equality
     * 3. No guaranteed progress → potential infinite loops
     * 4. Unsafe index access (out-of-bounds risk)
     *
     * 🔴 Counterexample:
     * nums = [8,8,8,8,8,8,8,8]
     * target = 8
     *
     * Binary search finds mid,
     * expansion scans entire array → O(n)
     *
     * 🔴 Interview Verdict:
     * ❌ Violates problem constraint
     * ❌ Fails worst-case analysis
     * ❌ Shows misunderstanding of boundary problems
     */

    /*
     * 🟢 How the Optimal Solution Fixes the Above Errors
     *
     * Error 1: Linear expansion
     * 🟢 Fix: Never expand linearly — always shrink search space logarithmically
     *
     * Error 2: Equality treated as termination
     * 🟢 Fix: Equality is treated as a boundary candidate, not a stopping point
     *
     * Error 3: No guaranteed termination
     * 🟢 Fix: Each iteration MUST move left or right
     *
     * Error 4: Unsafe index movement
     * 🟢 Fix: All index movement is bounded by left/right pointers
     *
     * 🟢 Core Mental Shift:
     * ❌ “Find the target, then adjust”
     * ✅ “Find the first index where a condition becomes true”
     *
     * 🟢 Boundary Predicates Used:
     * - First occurrence: nums[i] >= target
     * - Last occurrence:  nums[i] <= target
     *
     * 🟢 Result:
     * - Strict O(log n)
     * - No boundary violations
     * - Fully invariant-driven
     */


    // =============================================================================================
    // 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES
    // =============================================================================================

    static class BruteForce {
        static int[] searchRange(int[] nums, int target) {
            int first = -1, last = -1;
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] == target) {
                    if (first == -1) first = i;
                    last = i;
                }
            }
            return new int[]{first, last};
        }
    }

    // problem wants worst case 0(logn)
    // this solution is O(n) in worst case, so it is not optimal
    // as when input is [8,8,8,8,8,8,8,8] and target is 8, it will scan the entire array
    static class Improved {
        static int[] searchRange(int[] nums, int target) {
            int index = binarySearch(nums, target);
            if (index == -1) return new int[]{-1, -1};

            int left = index, right = index;
            while (left - 1 >= 0 && nums[left - 1] == target) left--;
            while (right + 1 < nums.length && nums[right + 1] == target) right++;

            return new int[]{left, right};
        }

        private static int binarySearch(int[] nums, int target) {
            int left = 0, right = nums.length - 1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (nums[mid] == target) return mid;
                if (nums[mid] < target) left = mid + 1;
                else right = mid - 1;
            }
            return -1;
        }
    }

    /*
     * 🟢 Why the Inequalities in Boundary Binary Search Are FORCED
     *
     * These conditions are NOT choices or tricks.
     * They are dictated by the definition of the boundary.
     *
     * ---------------------------------------------------------------------------------
     * 🔵 findFirst — First Occurrence of Target
     * ---------------------------------------------------------------------------------
     *
     * What we want:
     *     Smallest index i such that nums[i] == target
     *
     * Rephrased as a predicate:
     *     nums[i] >= target
     *
     * This predicate looks like:
     *     false false false true true true
     *                      ↑ boundary we want
     *
     * Therefore:
     * - When nums[mid] >= target:
     *     mid could be the answer OR
     *     the answer lies to the LEFT
     *
     * So we MUST move LEFT:
     *
     *     if (nums[mid] >= target) {
     *         if (nums[mid] == target) ans = mid;
     *         right = mid - 1;
     *     }
     *
     * Moving right here would skip the first occurrence.
     *
     * ---------------------------------------------------------------------------------
     * 🔵 findLast — Last Occurrence of Target
     * ---------------------------------------------------------------------------------
     *
     * What we want:
     *     Largest index i such that nums[i] == target
     *
     * Rephrased as a predicate:
     *     nums[i] <= target
     *
     * This predicate looks like:
     *     true true true true false false
     *                         ↑ boundary we want
     *
     * Therefore:
     * - When nums[mid] <= target:
     *     mid could be the answer OR
     *     the answer lies to the RIGHT
     *
     * So we MUST move RIGHT:
     *
     *     if (nums[mid] <= target) {
     *         if (nums[mid] == target) ans = mid;
     *         left = mid + 1;
     *     }
     *
     * Moving left here would skip the last occurrence.
     *
     * ---------------------------------------------------------------------------------
     * 🔴 Why Swapping These Inequalities Breaks Binary Search
     * ---------------------------------------------------------------------------------
     *
     * Using the wrong inequality destroys the monotonic predicate.
     *
     * Example:
     * nums = [8, 8, 8], target = 8
     *
     * - nums[mid] <= target is always true
     * - No boundary exists
     * - Binary search cannot converge
     *
     * ---------------------------------------------------------------------------------
     * 🟢 Universal Rule (Memorize This):
     *
     * Binary search ALWAYS moves toward the side
     * where the boundary might still exist.
     *
     * The inequality is chosen by the boundary,
     * not by preference.
     */

    static class Optimal {

        enum Boundary {
            FIRST,
            LAST
        }

        static int[] searchRange(int[] nums, int target) {

            int first = findBoundary(nums, target, Boundary.FIRST);

            if (first == -1) {
                return new int[]{-1, -1};
            }

            int last = findBoundary(nums, target, Boundary.LAST);

            return new int[]{first, last};
        }

        private static int findBoundary(
                int[] nums,
                int target,
                Boundary boundary
        ) {

            int left = 0;
            int right = nums.length - 1;
            int answer = -1;

            while (left <= right) {

                int mid = left + (right - left) / 2;

                if (nums[mid] < target) {
                    left = mid + 1;

                } else if (nums[mid] > target) {
                    right = mid - 1;

                } else {

                    answer = mid;

                    // The key difference between finding the first and last occurrence is which side we continue searching after finding a match.
                    if (boundary == Boundary.FIRST) {
                        // If we are looking for the first occurrence, we continue searching to the left.
                        // half of the search space is discarded, and we keep the left half.
                        right = mid - 1;
                    } else {
                        // If we are looking for the last occurrence, we continue searching to the right.
                        // half of the search space is discarded, and we keep the right half.
                        left = mid + 1;
                    }
                }
            }

            return answer;
        }
    }

    /*
     * ================================================================
     * BOUNDARY BINARY SEARCH — INTUITION
     * ================================================================
     *
     * Normal binary search:
     *
     * nums[mid] < target
     * -> search RIGHT
     *
     * nums[mid] > target
     * -> search LEFT
     *
     * nums[mid] == target
     * -> normally return immediately
     *
     *
     * Boundary binary search changes ONLY the equality case.
     *
     * When target is found:
     *
     *      answer = mid;
     *
     * We save the occurrence, but DO NOT return yet.
     *
     *
     * FIRST occurrence:
     *
     *      Found target.
     *      Could there be another target before this one?
     *
     *      YES -> continue searching LEFT.
     *
     *      right = mid - 1;
     *
     *
     * LAST occurrence:
     *
     *      Found target.
     *      Could there be another target after this one?
     *
     *      YES -> continue searching RIGHT.
     *
     *      left = mid + 1;
     *
     *
     * Mental rule:
     *
     *      FIRST -> found -> save -> go LEFT
     *
     *      LAST  -> found -> save -> go RIGHT
     *
     *
     * ================================================================
     * WHY USE ONE COMMON findBoundary() METHOD?
     * ================================================================
     *
     * findFirst() and findLast() have almost identical binary-search
     * logic.
     *
     * The ONLY meaningful difference is what happens after:
     *
     *      nums[mid] == target
     *
     *
     * FIRST:
     *
     *      right = mid - 1;
     *
     *
     * LAST:
     *
     *      left = mid + 1;
     *
     *
     * Therefore the common binary-search logic is extracted once,
     * and Boundary.FIRST / Boundary.LAST controls only the direction
     * after finding the target.
     *
     *
     * ================================================================
     * WHY THIS REMAINS O(log n) EVEN IF ALL VALUES ARE EQUAL
     * ================================================================
     *
     * Example:
     *
     *      nums = [8, 8, 8, 8, 8, 8, 8, 8]
     *      target = 8
     *
     *
     * Suppose we first land somewhere in the middle:
     *
     *      [8 8 8 8 8 8 8 8]
     *               ^
     *
     *
     * For FIRST:
     *
     * We DO NOT walk left one element at a time.
     *
     * We discard the entire right half:
     *
     *      n
     *      ->
     *      n / 2
     *      ->
     *      n / 4
     *      ->
     *      n / 8
     *      ->
     *      ...
     *
     *
     * Same idea for LAST, except we keep the right half.
     *
     *
     * Therefore:
     *
     *      find FIRST = O(log n)
     *
     *      find LAST  = O(log n)
     *
     *
     * Total:
     *
     *      O(log n) + O(log n)
     *
     *      = O(log n)
     *
     *
     * Space:
     *
     *      O(1)
     *
     *
     * ================================================================
     * COMPARED WITH BINARY SEARCH + LINEAR EXPANSION
     * ================================================================
     *
     * Alternative:
     *
     *      1. Binary search to find any target.
     *      2. Move left one-by-one.
     *      3. Move right one-by-one.
     *
     *
     * If all n elements equal target:
     *
     *      binary search = O(log n)
     *
     *      expansion     = O(n)
     *
     *      total         = O(n)
     *
     *
     * Boundary binary search instead keeps HALVING the search space.
     *
     *
     * Key distinction:
     *
     *      Linear expansion
     *      -> WALK through duplicates.
     *
     *      Boundary binary search
     *      -> DISCARD HALF of the remaining search space.
     *
     *
     * ================================================================
     * INTERVIEW RECONSTRUCTION RULE
     * ================================================================
     *
     * Start with ordinary binary search.
     *
     * The only modification is:
     *
     *      nums[mid] == target
     *
     *      -> save mid
     *      -> do NOT return
     *
     *
     * Then:
     *
     *      need FIRST -> search LEFT
     *
     *      need LAST  -> search RIGHT
     *
     * ================================================================
     */

    // =============================================================================================
    // 7️⃣ 🟣 INTERVIEW ARTICULATION
    // =============================================================================================

    /*
     * 🟣 Explanation:
     * I perform two binary searches:
     *  - one for the left boundary
     *  - one for the right boundary
     *
     * 🟣 Correctness:
     * Because the predicate is monotonic.
     *
     * 🟣 When NOT to Use:
     * - Unsorted arrays
     * - Rotated arrays without modification
     */

    // =============================================================================================
    // 8️⃣ 🔄 VARIATIONS & TWEAKS
    // =============================================================================================

    /*
     * 🟢 Invariant-Preserving:
     * - Count occurrences
     * - Insert position
     *
     * 🔴 Pattern Break:
     * - Global monotonicity destroyed
     */

    // =============================================================================================
    // 9️⃣ ⚫ REINFORCEMENT PROBLEMS (FULL STATEMENTS)
    // =============================================================================================

    /*
     * 35. Search Insert Position
     *
     * Given a sorted array of distinct integers and a target value,
     * return the index if the target is found.
     * If not, return the index where it would be if it were inserted in order.
     *
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
     * Constraints:
     * 1 <= nums.length <= 10^4
     * -10^4 <= nums[i] <= 10^4
     * nums contains distinct values sorted in ascending order.
     *
     * 🔗 https://leetcode.com/problems/search-insert-position/
     * 🧩 Difficulty: Easy
     * 🏷️ Tags: Array, Binary Search
     */
    static class SearchInsertPosition {
        static int searchInsert(int[] nums, int target) {
            int left = 0, right = nums.length - 1, answer = nums.length;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (nums[mid] >= target) {
                    answer = mid;
                    right = mid - 1;
                } else left = mid + 1;
            }
            return answer;
        }
    }

    /*
     * 69. Sqrt(x)
     *
     * Given a non-negative integer x, compute and return the square root of x.
     * Since the return type is an integer, the decimal digits are truncated,
     * and only the integer part of the result is returned.
     *
     * Example 1:
     * Input: x = 4
     * Output: 2
     *
     * Example 2:
     * Input: x = 8
     * Output: 2
     *
     * Constraints:
     * 0 <= x <= 2^31 - 1
     *
     * 🔗 https://leetcode.com/problems/sqrtx/
     * 🧩 Difficulty: Easy
     * 🏷️ Tags: Math, Binary Search
     */
    static class SqrtX {
        static int mySqrt(int x) {
            int left = 0, right = x, answer = 0;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                long square = (long) mid * mid;
                if (square <= x) {
                    answer = mid;
                    left = mid + 1;
                } else right = mid - 1;
            }
            return answer;
        }
    }

    /*
     * 162. Find Peak Element
     *
     * A peak element is an element that is strictly greater than its neighbors.
     *
     * Given an integer array nums, find a peak element, and return its index.
     * If the array contains multiple peaks, return the index to any of the peaks.
     *
     * Example 1:
     * Input: nums = [1,2,3,1]
     * Output: 2
     *
     * Example 2:
     * Input: nums = [1,2,1,3,5,6,4]
     * Output: 1 or 5
     *
     * Constraints:
     * 1 <= nums.length <= 1000
     * -2^31 <= nums[i] <= 2^31 - 1
     *
     * 🔗 https://leetcode.com/problems/find-peak-element/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Array, Binary Search
     *
     /*
     * ⚫ Why Binary Search Works for Find Peak Element (Despite No Sorting)
     *
     * At first glance, Find Peak Element looks incompatible with binary search
     * because the array is NOT sorted.
     *
     * This is a trap caused by a misunderstanding of binary search.
     *
     * 🔵 The Hidden Monotonic Signal:
     * Compare nums[mid] with nums[mid + 1]
     *
     * Case 1: nums[mid] > nums[mid + 1]
     *   → We are on a DESCENDING slope
     *   → A peak MUST exist on the LEFT side (including mid)
     *
     * Case 2: nums[mid] < nums[mid + 1]
     *   → We are on an ASCENDING slope
     *   → A peak MUST exist on the RIGHT side
     *
     * 🔵 Why This Is Monotonic:
     * Each comparison tells us, with certainty, which HALF contains a peak.
     * One half can always be discarded.
     *
     * 🔵 Invariant Maintained:
     * “There is guaranteed to be at least one peak in the current search space.”
     *
     * 🔵 Termination:
     * When left == right, that index must be a peak.
     *
     * 🔵 Key Takeaway:
     * Binary search does NOT need sorted data.
     * It needs a monotonic decision rule.
     */

    static class FindPeakElement {
        static int findPeakElement(int[] nums) {
            int left = 0, right = nums.length - 1;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (nums[mid] > nums[mid + 1]) right = mid;
                else left = mid + 1;
            }
            return left;
        }
    }

    // =============================================================================================
    // 10️⃣ 🧩 RELATED PROBLEMS (FULL STATEMENTS)
    // =============================================================================================

    /*
     * 33. Search in Rotated Sorted Array
     *
     * There is an integer array nums sorted in ascending order (with distinct values).
     *
     * Prior to being passed to your function, nums is possibly rotated at an unknown pivot index.
     *
     * Given the array nums after rotation and an integer target,
     * return the index of target if it is in nums, or -1 if it is not.
     *
     * You must write an algorithm with O(log n) runtime complexity.
     *
     * Example:
     * Input: nums = [4,5,6,7,0,1,2], target = 0
     * Output: 4
     *
     * 🔗 https://leetcode.com/problems/search-in-rotated-sorted-array/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Array, Binary Search
     *
     *
     * 🧩 Rotated Sorted Array — Deep Clarification of the `else` Branch
     *
     * In rotated sorted arrays (with distinct elements),
     * a crucial invariant always holds:
     *
     * 🟢 At least ONE of the two halves is fully sorted.
     *
     * The check:
     *     if (nums[left] <= nums[mid])
     * asks:
     *     “Is the LEFT half sorted?”
     *
     * If false, then the LEFT half is broken by rotation,
     * which guarantees:
     *
     * 🟢 The RIGHT half [mid … right] is sorted.
     *
     * Once the sorted half is identified, reasoning is restricted
     * strictly to that half.
     *
     * For the RIGHT-sorted case, the membership test is:
     *
     *     if (nums[mid] < target && target <= nums[right])
     *
     * Meaning:
     * “Does the target lie inside the sorted right half?”
     *
     * If YES:
     *     keep the right half → left = mid + 1
     *
     * If NO:
     *     discard the right half entirely → right = mid - 1
     */

    static class SearchRotatedArray {

        static int search(int[] nums, int target) {

            int left = 0;
            int right = nums.length - 1;

            while (left <= right) {

                int mid = left + (right - left) / 2;

                if (nums[mid] == target) {
                    return mid;
                }

                // LEFT half is sorted.
                if (nums[left] <= nums[mid]) {

                    // Target lies inside sorted LEFT half.
                    if (nums[left] <= target && target < nums[mid]) {
                        right = mid - 1;
                    } else {
                        left = mid + 1;
                    }

                }

                // Otherwise RIGHT half is sorted.
                else {

                    // Target lies inside sorted RIGHT half.
                    if (nums[mid] < target && target <= nums[right]) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
            }

            return -1;
        }
    }

    /**
     * ---------------------------------------------------------------------------
     * Mental Model
     * ---------------------------------------------------------------------------
     *
     * Binary Search
     *        ↓
     * Which half is sorted?
     *        ↓
     * Can the target exist in that sorted half?
     *        ↓
     *      Yes → Search that half.
     *      No  → Search the other half.
     *
     * ---------------------------------------------------------------------------
     * Decision Tree
     * ---------------------------------------------------------------------------
     *
     *                mid
     *                 │
     *      Is left half sorted?
     *     nums[left] <= nums[mid]
     *           /                \
     *        Yes                  No
     *         │                    │
     * Is target inside?     Right half is sorted.
     *         │                    │
     *      Yes   No         Is target inside?
     *       │     │              │
     *    Search  Search       Yes     No
     *     Left    Right        │       │
     *                        Search  Search
     *                         Right    Left
     *
     * ---------------------------------------------------------------------------
     * Why This Works
     * ---------------------------------------------------------------------------
     *
     * A rotated sorted array has exactly one discontinuity (the pivot).
     *
     * Therefore, at every binary search step, at least one half must still be
     * completely sorted. Both halves can never be unsorted simultaneously.
     *
     * The algorithm simply identifies the sorted half and asks:
     *
     *     "Can the target lie within this sorted range?"
     *
     * If yes, continue searching there.
     * Otherwise, discard it and search the other half.
     * ---------------------------------------------------------------------------
     */

    /*
     * 81. Search in Rotated Sorted Array II
     *
     * This problem is the same as Search in Rotated Sorted Array,
     * but nums may contain duplicates.
     *
     * Return true if target exists, otherwise false.
     *
     * 🔗 https://leetcode.com/problems/search-in-rotated-sorted-array-ii/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Array, Binary Search
     *
     * /*
     * 🧩 Rotated Sorted Array II (LC-81)
     * Deep Explanation: Why This Problem Is Fundamentally Harder Than LC-33
     *
     * This problem looks similar to Search in Rotated Sorted Array (LC-33),
     * but the presence of DUPLICATES changes the nature of the problem.
     *
     * ---------------------------------------------------------------------------------
     * 🔴 What Guarantee We Lost
     * ---------------------------------------------------------------------------------
     *
     * 🟢 At least ONE of the two halves is strictly sorted.
     *
     *
     * In LC-81, this invariant DOES NOT always hold.
     *
     * ---------------------------------------------------------------------------------
     * 🔴 The Ambiguous Case (Information Collapse)
     * ---------------------------------------------------------------------------------
     *
     * Consider this situation:
     *
     * nums[left] == nums[mid] == nums[right]
     *
     * Example:
     * [2, 2, 2, 3, 2, 2]
     *  L     M        R
     *
     * Here:
     * - Left half may be sorted
     * - Right half may be sorted
     * - Or the pivot may lie anywhere
     *
     * ❌ There is no reliable way to determine which half is sorted.
     * ❌ Any attempt to discard half risks losing the target.
     *
     * This is NOT a coding limitation — it is an information-theoretic limitation.
     *
     * ---------------------------------------------------------------------------------
     * 🟢 Why We Shrink Both Ends (left++, right--)
     * ---------------------------------------------------------------------------------
     *
     * When nums[left] == nums[mid] == nums[right]:
     *
     * - We cannot safely discard half
     * - But we CAN safely discard the boundaries
     *
     * Why?
     * - Removing duplicate values does not remove unique information
     * - If the target exists, it must still exist inside the reduced window
     *
     * This step is conservative damage control:
     *
     *     left++;
     *     right--;
     *
     * 🔴 This is the exact reason the worst-case time complexity degrades to O(n).
     *
     * ---------------------------------------------------------------------------------
     * 🔴 Why O(log n) Is Impossible in the Worst Case
     * ---------------------------------------------------------------------------------
     *
     * Consider:
     * nums = [1,1,1,1,1,1,1,1]
     * target = 2
     *
     * Every iteration:
     * - nums[left] == nums[mid] == nums[right]
     * - No ordering information is gained
     * - We can only shrink one step at a time
     *
     * ❌ No algorithm can do better than O(n) here.
     *
     * This degradation is unavoidable and EXPECTED.
     *
     * ---------------------------------------------------------------------------------
     * 🟢 Once Ambiguity Is Resolved, We Reuse LC-33 Logic
     * ---------------------------------------------------------------------------------
     *
     * After duplicates are skipped, one of the following becomes true:
     *
     * 1. Left half is sorted:
     *    nums[left] <= nums[mid]
     *
     * 2. Right half is sorted:
     *    nums[left] > nums[mid]
     *
     * From this point onward, the logic is IDENTICAL to LC-33:
     *
     * - Identify the sorted half
     * - Check if target lies within its boundaries
     * - Keep or discard that half accordingly
     *
     * ---------------------------------------------------------------------------------
     * 🟣 Interview-Grade Explanation
     * ---------------------------------------------------------------------------------
     *
     * “With duplicates, I may lose the ability to determine which half is sorted.
     * In that ambiguous case, I shrink the search space conservatively.
     * This degrades worst-case time to O(n), which is unavoidable.
     * Once ordering information reappears, I resume binary search.”
     *
     * Saying this demonstrates deep understanding.
     *
     * ---------------------------------------------------------------------------------
     * 🧠 Final Takeaway
     * ---------------------------------------------------------------------------------
     *
     * LC-81 is NOT a minor variation of LC-33.
     *
     * It is a weaker problem:
     * - Less information per comparison
     * - Weaker guarantees
     * - Worse worst-case complexity
     *
     * The solution reflects this reality honestly.

     ---------------------------------------------------------------------------------------------
     * 🔴 Ambiguity Mode (Binary Elimination FORBIDDEN)
     * ---------------------------------------------------------------------------------------------
     *    Ordering information is completely destroyed if and only if:
     *
     *        nums[left] == nums[mid] == nums[right]
     *
     *    In this state:
     *      - Left half may or may not be sorted
     *      - Right half may or may not be sorted
     *      - Pivot may exist anywhere
     *      - Target may exist anywhere
     *
     *    No half can be safely discarded without risking correctness.
     *
     *    Only safe action:
     *        left++;
     *        right--;
     *
     *    This shrinks uncertainty while preserving the invariant that the target,
     *    if present, remains within [left, right].
     *
     * ---------------------------------------------------------------------------------------------
     * 🔴 Why Partial Equality Is NOT True Ambiguity
     * ---------------------------------------------------------------------------------------------
     *    Conditions like:
     *        nums[left] == nums[mid]
     *    or
     *        nums[mid] == nums[right]
     *
     *    do NOT eliminate all ordering information.
     *
     *    As long as at least one boundary differs, at least ONE half remains ordered.
     *    Binary reasoning is still valid in these cases.
     *
     *    Shrinking boundaries under partial equality is premature and can discard
     *    the only sorted half containing the target.
     *
     */
    static class SearchRotatedArrayII {

        static boolean search(int[] nums, int target) {

            int left = 0;
            int right = nums.length - 1;

            while (left <= right) {

                int mid = left + (right - left) / 2;

                if (nums[mid] == target) {
                    return true;
                }

                /*
                 * DUPLICATE AMBIGUITY:
                 *
                 * Cannot determine which half is sorted.
                 * Safely remove equal boundary values.
                 */
                if (nums[left] == nums[mid]
                        && nums[mid] == nums[right]) {

                    left++;
                    right--;

                    continue;
                }

                // LEFT half is sorted.
                if (nums[left] <= nums[mid]) {

                    // Target lies inside sorted LEFT half.
                    if (nums[left] <= target && target < nums[mid]) {
                        right = mid - 1;
                    } else {
                        left = mid + 1;
                    }

                }

                // Otherwise RIGHT half is sorted.
                else {

                    // Target lies inside sorted RIGHT half.
                    if (nums[mid] < target && target <= nums[right]) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
            }

            return false;
        }
    }
    // =============================================================================================
    // 11️⃣ 🟢 LEARNING VERIFICATION
    // =============================================================================================

    /*
     * You have mastered this chapter if you can:
     * - Explain lower vs upper bound without code
     * - Detect monotonic predicates
     * - Avoid equality traps
     */

    // =============================================================================================
    // 12️⃣ 🧪 MAIN + SELF-VERIFYING TESTS
    // =============================================================================================

    public static void main(String[] args) {
        assertArrayEquals(new int[]{3, 4},
                Optimal.searchRange(new int[]{5, 7, 7, 8, 8, 10}, 8), "Primary happy");

        assertArrayEquals(new int[]{-1, -1},
                Optimal.searchRange(new int[]{5, 7, 7, 8, 8, 10}, 6), "Primary missing");

        if (SearchInsertPosition.searchInsert(new int[]{1, 3, 5, 6}, 2) != 1)
            throw new AssertionError("SearchInsert failed");

        if (SqrtX.mySqrt(8) != 2)
            throw new AssertionError("Sqrt failed");

        if (!SearchRotatedArrayII.search(new int[]{2, 5, 6, 0, 0, 1, 2}, 0))
            throw new AssertionError("Rotated II failed");

        System.out.println("✅ ALL TESTS PASSED");
    }

    private static void assertArrayEquals(int[] expected, int[] actual, String msg) {
        if (expected.length != actual.length) throw new AssertionError(msg);
        for (int i = 0; i < expected.length; i++)
            if (expected[i] != actual[i]) throw new AssertionError(msg);
    }
}
