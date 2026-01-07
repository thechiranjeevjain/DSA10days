package org.chijai.day1.session2;
import java.util.*;

/*
================================================================================
📘 DSA TEXTBOOK CHAPTER — 3SUM
Pattern: Fixed Anchor + Two Pointers on Sorted Array
================================================================================
*/

public class Three3Sum2Sum {

    // ============================
    // 🔵 CORE PATTERN OVERVIEW
    // ============================
    /*
     🔵 PATTERN NAME:
        Fixed Anchor + Two Pointers

     🔵 CORE IDEA:
        Fix one element.
        Reduce the remaining search to a sorted 2Sum problem.

     🔵 WHY THIS PATTERN EXISTS:
        Sorting introduces order.
        Order enables monotonic pointer movement.
        Monotonic movement guarantees O(n²) without missing solutions.

     🔵 WHEN TO USE:
        - Triplets / quadruplets
        - Sum == target
        - Order of output does not matter
        - Uniqueness required

     🧭 PATTERN RECOGNITION SIGNALS:
        - Array + sum target
        - Asked for pairs / triplets / quadruplets
        - “Return unique combinations”
        → Immediately think: sort + fix + two pointers
    */

    // ----------------------------
    // 🟢 MENTAL MODEL & INVARIANTS
    // ----------------------------
    /*
     🟢 MENTAL MODEL:
        One number is FIXED.
        The remaining range is SEARCHED using two pointers.

        [ fixedIndex | leftPointer ... rightPointer ]

     🟢 INVARIANTS (ALWAYS TRUE):
        1️⃣ Array is sorted
        2️⃣ leftPointer < rightPointer
        3️⃣ Moving leftPointer → increases sum
        4️⃣ Moving rightPointer → decreases sum
        5️⃣ Duplicate triplets are skipped deterministically

     🟢 VARIABLE ROLES:
        fixedIndex   → anchor element
        leftPointer  → increases sum
        rightPointer → decreases sum
        currentSum   → decision driver

     🟢 TERMINATION:
        Inner loop ends when leftPointer >= rightPointer
        Outer loop ends at n-2
        Early exit when fixedIndex > 0

     🟢 FORBIDDEN ACTIONS:
        ❌ Skipping sort
        ❌ Moving both pointers blindly
        ❌ Emitting duplicates

     ❌ Why NOT HashSet-only solutions:
        - Hide invariant reasoning
        - Mask duplicate bugs instead of preventing them
        - Consume extra memory
        - Harder to explain under interview pressure
    */

    // ============================================================================
    // PRIMARY PROBLEM — SOLUTION CLASSES
    // ============================================================================

    // --------------------------------------------------
    // 🟥 BRUTE FORCE SOLUTION
    // --------------------------------------------------
    static class BruteForceSolution {
        /*
         ⏱ Time: O(n³)
         🧠 Space: O(k)

         ❌ Not interview preferred
         ✅ Baseline understanding only
        */

        public List<List<Integer>> threeSum(int[] nums) {
            Set<List<Integer>> result = new HashSet<>();
            int n = nums.length;

            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    for (int k = j + 1; k < n; k++) {
                        if (nums[i] + nums[j] + nums[k] == 0) {
                            List<Integer> triplet =
                                    Arrays.asList(nums[i], nums[j], nums[k]);
                            Collections.sort(triplet);
                            result.add(triplet);
                        }
                    }
                }
            }
            return new ArrayList<>(result);
        }
    }

    // --------------------------------------------------
    // 🟠 IMPROVED SOLUTION (HASHING)
    // --------------------------------------------------
    static class ImprovedSolution {
        /*
         ⏱ Time: O(n²)
         🧠 Space: O(n)

         ❌ Implicit duplicate handling
         ❌ Weak invariants
         ❌ Not interview preferred
        */

        public List<List<Integer>> threeSum(int[] nums) {
            Set<List<Integer>> result = new HashSet<>();
            Arrays.sort(nums);

            for (int i = 0; i < nums.length; i++) {
                Set<Integer> seen = new HashSet<>();
                for (int j = i + 1; j < nums.length; j++) {
                    int needed = -nums[i] - nums[j];
                    if (seen.contains(needed)) {
                        result.add(Arrays.asList(nums[i], nums[j], needed));
                    }
                    seen.add(nums[j]);
                }
            }
            return new ArrayList<>(result);
        }
    }

    // --------------------------------------------------
    // 🟢 OPTIMAL SOLUTION (INTERVIEW-PREFERRED)
    // --------------------------------------------------
    static class OptimalSolution {
        /*
         ⏱ Time: O(n²)
         🧠 Space: O(1) extra
         ✅ Deterministic duplicate handling
         ✅ Strong invariants

         ❌ Anti-pattern to avoid:
            Using HashSet<List<Integer>> to deduplicate results.
            It “works” but hides correctness bugs and weakens explanation.
        */

        public List<List<Integer>> threeSum(int[] nums) {
            List<List<Integer>> result = new ArrayList<>();
            Arrays.sort(nums);

            for (int fixedIndex = 0; fixedIndex < nums.length - 2; fixedIndex++) {

                // ❌ COMMON BUG: not skipping duplicate anchors
                if (fixedIndex > 0 && nums[fixedIndex] == nums[fixedIndex - 1]) {
                    continue;
                }

                // 🟡 Optimization: no possible zero sum
                if (nums[fixedIndex] > 0) break;

                int leftPointer = fixedIndex + 1;
                int rightPointer = nums.length - 1;

                while (leftPointer < rightPointer) {
                    int currentSum =
                            nums[fixedIndex] + nums[leftPointer] + nums[rightPointer];

                    if (currentSum == 0) {
                        result.add(Arrays.asList(
                                nums[fixedIndex],
                                nums[leftPointer],
                                nums[rightPointer]
                        ));

                        leftPointer++;
                        rightPointer--;

                        // ❌ COMMON BUG: forgetting duplicate skips
                        while (leftPointer < rightPointer &&
                                nums[leftPointer] == nums[leftPointer - 1]) {
                            leftPointer++;
                        }
                        while (leftPointer < rightPointer &&
                                nums[rightPointer] == nums[rightPointer + 1]) {
                            rightPointer--;
                        }

                    } else if (currentSum < 0) {
                        leftPointer++;
                    } else {
                        rightPointer--;
                    }
                }
            }
            return result;
        }
    }

    // ============================================================
    // 📊 APPROACH COMPARISON SUMMARY
    // ============================================================
    /*
     Approach     | Time  | Space | Interview Preferred
     -------------|-------|-------|-------------------
     Brute Force  | O(n³) | O(k)  | ❌
     Hashing      | O(n²) | O(n)  | ❌
     Two Pointer  | O(n²) | O(1)  | ✅
    */

    // ============================================================
    // 🟣 INTERVIEW ARTICULATION & FOLLOW-UPS
    // ============================================================
    /*
     Q: Why does this approach work?
     A: Sorting creates ordered space; pointers exploit monotonic sum changes.

     Q: Which invariant guarantees correctness?
     A: Pointer movement strictly increases or decreases sum.

     Q: Can it be done in-place?
     A: Yes, except for output.

     Q: Can it handle streaming input?
     A: No, sorting requires full input.

     Q: When should this pattern NOT be used?
     A: When order matters or duplicates are allowed.
    */

    // ============================================================
    // 🔄 VARIATIONS & TWEAKS — COMPLETE COVERAGE
    // ============================================================
    /*
     🟢 Change target sum:
        Replace 0 with target. Invariant still holds.

     🟡 Extend to 4Sum:
        Add another fixed loop. Reasoning unchanged.

     🔴 Pattern break:
        If array must remain unsorted → switch to hashing.
    */

    // ============================================================
    // ⚫ PATTERN REINFORCEMENT PROBLEMS
    // ============================================================

    // --------------------------------------------------
    // ⚫ Reinforcement 1 — Two Sum II (Sorted Array)
    // --------------------------------------------------
    static class TwoSumIISolution {
        /*
         🔵 Problem:
            Given sorted array, find two numbers summing to target.

         ⚫ Mapping:
            Same two-pointer invariant, no fixed anchor.

         ❌ Edge Case:
            No solution → return empty array.
        */

        public int[] twoSum(int[] numbers, int target) {
            int left = 0, right = numbers.length - 1;

            while (left < right) {
                int sum = numbers[left] + numbers[right];
                if (sum == target) return new int[]{left + 1, right + 1};
                if (sum < target) left++;
                else right--;
            }
            return new int[]{};
        }
    }

    // --------------------------------------------------
    // ⚫ Reinforcement 2 — 3Sum Closest
    // --------------------------------------------------
    static class ThreeSumClosestSolution {
        /*
         🔵 Problem:
            Find triplet with sum closest to target.

         ⚫ Mapping:
            Same fixed anchor + two pointers.

         ❌ Edge Case:
            Exact match → return immediately.
        */

        public int threeSumClosest(int[] nums, int target) {
            Arrays.sort(nums);
            int closestSum = nums[0] + nums[1] + nums[2];

            for (int i = 0; i < nums.length - 2; i++) {
                int left = i + 1, right = nums.length - 1;

                while (left < right) {
                    int sum = nums[i] + nums[left] + nums[right];
                    int currentDistance = Math.abs(sum - target);
                    int bestDistanceSoFar = Math.abs(closestSum - target);

                    if (currentDistance < bestDistanceSoFar) {
                        closestSum = sum;
                    }

                    if (sum < target) left++;
                    else if (sum > target) right--;
                    else return sum;
                }
            }
            return closestSum;
        }
    }

    // --------------------------------------------------
    // ⚫ Reinforcement 3 — 4Sum
    // --------------------------------------------------
    static class FourSumSolution {
        /*
         🔵 Problem:
            Find unique quadruplets summing to target.

         ⚫ Mapping:
            Two fixed anchors + two pointers.

         ❌ Edge Case:
            Length < 4 → empty result.
        */

        public List<List<Integer>> fourSum(int[] nums, int target) {
            List<List<Integer>> result = new ArrayList<>();
            Arrays.sort(nums);

            for (int i = 0; i < nums.length - 3; i++) {
                if (i > 0 && nums[i] == nums[i - 1]) continue;

                for (int j = i + 1; j < nums.length - 2; j++) {
                    if (j > i + 1 && nums[j] == nums[j - 1]) continue;

                    int left = j + 1, right = nums.length - 1;

                    while (left < right) {
                        long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];
                        if (sum == target) {
                            result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));
                            left++;
                            right--;
                            while (left < right && nums[left] == nums[left - 1]) left++;
                            while (left < right && nums[right] == nums[right + 1]) right--;
                        } else if (sum < target) {
                            left++;
                        } else {
                            right--;
                        }
                    }
                }
            }
            return result;
        }
    }

    // ============================================================
    // 🟢 LEARNING VERIFICATION
    // ============================================================
    /*
     ✔ Invariant recall: sorted + monotonic pointers
     ✔ Duplicate handling: skip neighbors after movement
     ✔ Adaptation: add fixed loops for higher sums
     ✔ Pattern recognition: “fix + balance remaining”
    */

    // ============================================================
    // 🧪 main() METHOD & TESTS (MUST BE LAST)
    // ============================================================
    public static void main(String[] args) {
        OptimalSolution solution = new OptimalSolution();

        // 🟡 Core case
        System.out.println(solution.threeSum(
                new int[]{-1, 0, 1, 2, -1, -4}
        ));
        // Expected: [[-1,-1,2], [-1,0,1]]

        // 🟡 All zeros
        System.out.println(solution.threeSum(
                new int[]{0, 0, 0}
        ));
        // Expected: [[0,0,0]]

        // 🟡 No solution
        System.out.println(solution.threeSum(
                new int[]{1, 2, -2, -1}
        ));
        // Expected: []
    }
}

