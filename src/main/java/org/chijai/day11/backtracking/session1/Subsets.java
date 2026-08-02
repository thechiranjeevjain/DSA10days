package org.chijai.day11.backtracking.session1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Subsets {

/*
 * =========================================================================
 * 2. 📘 PRIMARY PROBLEM
 * =========================================================================
 *
 * Title
 * -----
 * Subsets
 *
 * Difficulty
 * ----------
 * Medium
 *
 * Tags
 * ----
 * Backtracking
 * DFS
 * Power Set
 * Recursion
 *
 * Problem
 * -------
 * Given an integer array nums containing UNIQUE integers,
 * return every possible subset (the power set).
 *
 * The solution set must not contain duplicate subsets.
 * The subsets may be returned in any order.
 *
 * Constraints
 * -----------
 * 1 <= nums.length <= 10
 * -10 <= nums[i] <= 10
 * Every element is unique.
 *
 * Representative Example 1
 * ------------------------
 * Input:
 * nums = [1,2,3]
 *
 * Output:
 * [
 * [],
 * [1],
 * [2],
 * [1,2],
 * [3],
 * [1,3],
 * [2,3],
 * [1,2,3]
 * ]
 *
 * Representative Example 2
 * ------------------------
 * Input:
 * nums = [0]
 *
 * Output:
 * [
 * [],
 * [0]
 * ]
 *
 * LeetCode
 * --------
 * https://leetcode.com/problems/subsets/
 *
 * =========================================================================
 * Goal
 * =========================================================================
 *
 * Produce every possible choice of elements.
 *
 * Every element has exactly two decisions:
 *
 *      Take
 *      Skip
 *
 * Therefore total subsets = 2^n.
 *
 * =========================================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * =========================================================================
 *
 * Pattern
 * -------
 * Backtracking
 *
 * Archetype
 * ---------
 * Combination Enumeration
 *
 * Unlike permutations, order does NOT matter.
 *
 * We move only forward.
 *
 * =========================================================================
 * Core Invariant
 * =========================================================================
 *
 * temp always represents ONE valid subset.
 *
 * Every recursive call owns exactly one search interval:
 *
 *      [start ... n-1]
 *
 * All indices before start have permanently made their decision.
 *
 * They can never be revisited.
 *
 * =========================================================================
 * Why It Works
 * =========================================================================
 *
 * At every recursion state:
 *
 * 1. Current subset is already valid.
 *
 * 2. Record it immediately.
 *
 * 3. Try adding every remaining element.
 *
 * 4. Undo.
 *
 * Eventually every possible combination is explored exactly once.
 *
 * =========================================================================
 * Recognition Signals
 * =========================================================================
 *
 * Think of this pattern whenever:
 *
 * • Need every subset
 * • Need every combination
 * • Order does NOT matter
 * • Elements are chosen at most once
 * • "Generate all"
 *
 * =========================================================================
 * When To Use
 * =========================================================================
 *
 * ✔ Power Set
 *
 * ✔ Combination Sum
 *
 * ✔ Choose k Elements
 *
 * ✔ Partition Search
 *
 * ✔ Decision Tree Enumeration
 *
 * =========================================================================
 * When NOT To Use
 * =========================================================================
 *
 * ✘ Order matters
 *
 *      Use Permutations.
 *
 * ✘ Repeated element usage allowed forever
 *
 *      Use Combination Sum style recursion.
 *
 * ✘ Need only count
 *
 *      DP or mathematics may be better.
 *
 * =========================================================================
 * Comparison
 * =========================================================================
 *
 * -------------------------------
 * Subsets
 * -------------------------------
 * Order matters?
 *      No
 *
 * Use every element?
 *      No
 *
 * Need visited[]?
 *      No
 *
 * start pointer?
 *      Yes
 *
 * Add answer when?
 *      Every recursion state
 *
 * -------------------------------
 * Permutations
 * -------------------------------
 * Order matters?
 *      Yes
 *
 * Use every element?
 *      Yes
 *
 * Need visited[]?
 *      Yes
 *
 * start pointer?
 *      No
 *
 * Add answer when?
 *      Path length == n
 *
 * =========================================================================
 * Subsets vs Permutations
 * =========================================================================
 *
 * This distinction is one of the highest-value interview concepts.
 *
 * Subsets:
 *
 *      Number of chosen elements changes.
 *
 *      Order ignored.
 *
 *      Therefore once an element is considered,
 *      we never go backward.
 *
 *      start enforces this.
 *
 * Permutations:
 *
 *      Every element must appear.
 *
 *      Order changes.
 *
 *      Therefore every unused position is reconsidered.
 *
 *      visited[] enforces uniqueness.
 *
 * A quick interview memory cue:
 *
 *      Subsets
 *          Move forward.
 *
 *      Permutations
 *          Reconsider everyone.
 *
 * =========================================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * =========================================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine standing at one position in the array.
 *
 * Behind you:
 *
 *      Decisions are permanent.
 *
 * Ahead:
 *
 *      Decisions are still available.
 *
 * You never walk backward.
 *
 * This is exactly why duplicates cannot appear when numbers are unique.
 *
 * =========================================================================
 * Primary Invariant
 * =========================================================================
 *
 * temp contains exactly the elements chosen on the current path.
 *
 * Every element inside temp appears in increasing index order.
 *
 * NOT increasing value.
 *
 * Increasing INDEX.
 *
 * This single property guarantees:
 *
 * [1,2]
 *
 * can exist.
 *
 * But
 *
 * [2,1]
 *
 * never appears.
 *
 * Hence no duplicate subsets.
 *
 * =========================================================================
 * Search Space Invariant
 * =========================================================================
 *
 * start is the first undecided index.
 *
 * Therefore:
 *
 * indices < start
 *
 * are frozen forever.
 *
 * indices >= start
 *
 * remain candidates.
 *
 * Recursive call:
 *
 *      i + 1
 *
 * shrinks the search space.
 *
 * Search space strictly decreases.
 *
 * Therefore recursion terminates.
 *
 * =========================================================================
 * State Variables
 * =========================================================================
 *
 * nums
 * ----
 * Original input.
 *
 * temp
 * ----
 * Current subset.
 *
 * answer
 * ------
 * All completed subsets collected so far.
 *
 * start
 * -----
 * First available index.
 *
 * i
 * -
 * Candidate being explored.
 *
 * =========================================================================
 * Allowed Moves
 * =========================================================================
 *
 * 1.
 * Record current subset.
 *
 * 2.
 * Choose one remaining element.
 *
 * 3.
 * Go deeper.
 *
 * 4.
 * Undo.
 *
 * Repeat.
 *
 * =========================================================================
 * Forbidden Moves
 * =========================================================================
 *
 * Never revisit an index before start.
 *
 * Never move backwards.
 *
 * Never keep an element after returning.
 *
 * Never modify an already stored subset.
 */    /*
     * =========================================================================
     * Termination
     * =========================================================================
     *
     * Every recursive call increases start.
     *
     * Eventually:
     *
     *      start == nums.length
     *
     * The loop has no candidates.
     *
     * Control naturally returns.
     *
     * No explicit base case is required.
     *
     * The empty for-loop is the base case.
     *
     * =========================================================================
     * Correctness Intuition
     * =========================================================================
     *
     * Every subset can be uniquely represented by the increasing sequence of
     * indices chosen from the array.
     *
     * Example:
     *
     * nums = [4,7,9]
     *
     * {}
     * {0}
     * {1}
     * {2}
     * {0,1}
     * {0,2}
     * {1,2}
     * {0,1,2}
     *
     * Our DFS generates exactly these increasing index sequences.
     *
     * Since no sequence is repeated and none are omitted,
     * every subset is generated exactly once.
     *
     * =========================================================================
     * Why Naive Solutions Fail
     * =========================================================================
     *
     * Many first attempts generate permutations instead.
     *
     * Example:
     *
     * [1,2]
     *
     * and
     *
     * [2,1]
     *
     * are treated differently.
     *
     * But subsets ignore order.
     *
     * The missing invariant is:
     *
     *      "Never go backwards."
     *
     * start is exactly what enforces that invariant.
     *
     * =========================================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * =========================================================================
     *
     * ------------------------------------------------------------
     * Mistake 1
     * ------------------------------------------------------------
     *
     * Using visited[] instead of start.
     *
     * Why it feels correct
     * --------------------
     *
     * Permutations use visited[].
     *
     * Why it fails
     * ------------
     *
     * visited[] allows revisiting earlier indices later.
     *
     * That creates:
     *
     * [1,2]
     * [2,1]
     *
     * Duplicate subsets.
     *
     * Violated invariant
     * ------------------
     *
     * Increasing index order.
     *
     * ------------------------------------------------------------
     * Mistake 2
     * ------------------------------------------------------------
     *
     * Add answer only when temp size == n.
     *
     * Why it feels correct
     * --------------------
     *
     * That's how permutations work.
     *
     * Why it fails
     * ------------
     *
     * We lose:
     *
     * []
     * [1]
     * [2]
     * [1,2]
     *
     * etc.
     *
     * Subsets are valid at every recursion depth.
     *
     * Violated invariant
     * ------------------
     *
     * Current path is already a valid subset.
     *
     * ------------------------------------------------------------
     * Mistake 3
     * ------------------------------------------------------------
     *
     * Forget backtracking.
     *
     * Example:
     *
     * temp:
     *
     * [1]
     *
     * add 2
     *
     * [1,2]
     *
     * recurse
     *
     * return
     *
     * forget remove()
     *
     * next branch incorrectly starts from:
     *
     * [1,2]
     *
     * instead of
     *
     * [1]
     *
     * Violated invariant
     * ------------------
     *
     * temp must exactly match the current DFS path.
     *
     * ------------------------------------------------------------
     * Mistake 4
     * ------------------------------------------------------------
     *
     * Store temp directly.
     *
     * answer.add(temp)
     *
     * instead of
     *
     * new ArrayList<>(temp)
     *
     * Why it fails
     * ------------
     *
     * Every stored answer points to the same mutable object.
     *
     * Final output becomes many copies of one list.
     *
     * Violated invariant
     * ------------------
     *
     * Stored subsets must become immutable snapshots.
     *
     * ------------------------------------------------------------
     * Mistake 5
     * ------------------------------------------------------------
     *
     * Recurse with:
     *
     * backtrack(..., start + 1)
     *
     * instead of
     *
     * i + 1
     *
     * Why it fails
     * ------------
     *
     * Search space no longer depends on the chosen element.
     *
     * Branches merge incorrectly.
     *
     * Elements get skipped or repeated.
     *
     * Correct transition:
     *
     * Current choice = i
     *
     * Therefore next candidate begins at:
     *
     * i + 1
     *
     * Never start + 1.
     *
     * ------------------------------------------------------------
     * Interview Trap
     * ------------------------------------------------------------
     *
     * Interviewer:
     *
     * "Why don't we need visited[]?"
     *
     * Strong answer:
     *
     * Because every recursive state owns only the suffix beginning at start.
     * Earlier indices are permanently frozen.
     * That guarantees increasing index order and prevents duplicate subsets.
     *
     * =========================================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * =========================================================================
     *
     * Mechanical typing order:
     *
     * Step 1
     *
     * Create answer list.
     *
     * Step 2
     *
     * Call DFS with:
     *
     * answer
     * empty temp
     * nums
     * start = 0
     *
     * Step 3
     *
     * DFS receives:
     *
     * answer
     * temp
     * nums
     * start
     *
     * Step 4
     *
     * Immediately record current subset.
     *
     * Step 5
     *
     * Loop:
     *
     * for i = start to n-1
     *
     * Step 6
     *
     * Choose:
     *
     * temp.add(nums[i])
     *
     * Step 7
     *
     * Explore:
     *
     * dfs(i + 1)
     *
     * Step 8
     *
     * Undo:
     *
     * remove last element
     *
     * Step 9
     *
     * Continue loop.
     *
     * Step 10
     *
     * Return answer.
     *
     * =========================================================================
     * Skeleton
     * =========================================================================
     *
     * create answer
     *
     * dfs(start)
     *
     * save current subset
     *
     * for every remaining element
     *
     *      choose
     *
     *      recurse(next index)
     *
     *      undo
     *
     * return
     *
     * =========================================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * =========================================================================
     *
     * answer = {}
     *
     * dfs(start):
     *
     *      save(path)
     *
     *      for remaining candidate
     *
     *          choose
     *
     *          dfs(next)
     *
     *          undo
     *
     * return answer
     *
     * =========================================================================
     * 6. SOLUTION CLASSES
     * =========================================================================
     *
     * -------------------------------------------------------------------------
     * Solution 1
     * Brute Force
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     *
     * Enumerate every bitmask from:
     *
     * 0
     *
     * to
     *
     * (1<<n)-1
     *
     * Every bit determines:
     *
     * take / skip.
     *
     * Invariant
     * ---------
     *
     * Bit i represents whether nums[i] belongs to the subset.
     *
     * Limitation
     * ----------
     *
     * Less intuitive during interviews.
     *
     * Harder to adapt to constrained backtracking problems.
     *
     * Complexity
     * ----------
     *
     * Time:
     * O(n * 2^n)
     *
     * Space:
     * O(n * 2^n)
     *
     * Interview usefulness
     * --------------------
     *
     * Good alternative.
     *
     * Not usually the preferred recursive explanation.
     */

    static class BruteForceBitmask {

        static List<List<Integer>> subsets(int[] nums) {

            List<List<Integer>> answer = new ArrayList<>();

            int total = 1 << nums.length;

            for (int mask = 0; mask < total; mask++) {

                List<Integer> subset = new ArrayList<>();

                for (int bit = 0; bit < nums.length; bit++) {

                    if ((mask & (1 << bit)) != 0) {
                        subset.add(nums[bit]);
                    }
                }

                answer.add(subset);
            }

            return answer;
        }
    }

    /*
     * -------------------------------------------------------------------------
     * Solution 2
     * Improved
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     *
     * Binary Decision DFS.
     *
     * Every element has exactly two choices:
     *
     * Take
     * Skip
     *
     * This mirrors the mathematical definition of a power set.
     *
     * Invariant
     * ---------
     *
     * Index indicates the current decision point.
     *
     * All earlier decisions are fixed.
     *
     * Improvement
     * -----------
     *
     * More intuitive than bit manipulation.
     *
     * Excellent bridge toward backtracking.
     *
     * Complexity
     * ----------
     *
     * Time:
     * O(n * 2^n)
     *
     * Space:
     * O(n)
     *
     * (excluding output)
     *
     * Interview usefulness
     * --------------------
     *
     * Good explanation tool.
     *
     * Less reusable than the combination-style backtracking template.
     */

    static class ImprovedBinaryDecision {        static List<List<Integer>> subsets(int[] nums) {

        List<List<Integer>> answer = new ArrayList<>();

        dfs(0, nums, new ArrayList<>(), answer);

        return answer;
    }

        private static void dfs(
                int index,
                int[] nums,
                List<Integer> path,
                List<List<Integer>> answer) {

            if (index == nums.length) {
                answer.add(new ArrayList<>(path));
                return;
            }

            // 🟢 Invariant:
            // Path currently represents decisions made for indices [0...index-1].

            // Skip current element.
            dfs(index + 1, nums, path, answer);

            // Choose current element.
            path.add(nums[index]);

            dfs(index + 1, nums, path, answer);

            // Restore the exact parent state.
            path.remove(path.size() - 1);
        }
    }

    /*
     * -------------------------------------------------------------------------
     * Solution 3
     * Optimal (Interview Preferred)
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     *
     * Enumerate combinations instead of binary decisions.
     *
     * Rather than asking:
     *
     *      Take?
     *      Skip?
     *
     * we ask:
     *
     *      Which remaining element should become the next chosen element?
     *
     * This single template immediately extends to:
     *
     *      Combination Sum
     *      Combination Sum II
     *      Combinations
     *      Subsets II
     *      Letter Combinations
     *      Palindrome Partitioning
     *
     * Therefore interviewers strongly prefer this implementation.
     *
     * -------------------------------------------------------------------------
     * Core Invariant
     * -------------------------------------------------------------------------
     *
     * temp always stores one valid subset.
     *
     * start marks the first index that has not yet been considered.
     *
     * Every recursive call permanently freezes all indices before start.
     *
     * Therefore:
     *
     *      indices are always selected
     *
     *              in increasing order.
     *
     * This automatically removes duplicate orderings.
     *
     * -------------------------------------------------------------------------
     * Why We Save Before Loop
     * -------------------------------------------------------------------------
     *
     * Every recursion state already represents one complete subset.
     *
     * Example:
     *
     * temp = []
     *
     * valid
     *
     * temp = [1]
     *
     * valid
     *
     * temp = [1,3]
     *
     * valid
     *
     * temp = [1,2,3]
     *
     * valid
     *
     * Hence every state is recorded.
     *
     * -------------------------------------------------------------------------
     * Why Loop Starts From start
     * -------------------------------------------------------------------------
     *
     * Everything before start has already made its decision.
     *
     * Revisiting earlier indices would generate:
     *
     * [2,1]
     *
     * after already producing:
     *
     * [1,2]
     *
     * violating the increasing-index invariant.
     *
     * -------------------------------------------------------------------------
     * Why Recursive Call Uses i + 1
     * -------------------------------------------------------------------------
     *
     * We have already chosen nums[i].
     *
     * Choosing it again is forbidden.
     *
     * Remaining search space begins immediately after i.
     *
     * -------------------------------------------------------------------------
     * Complexity
     * -------------------------------------------------------------------------
     *
     * Total subsets:
     *
     *      2^n
     *
     * Average subset size:
     *
     *      O(n)
     *
     * Therefore:
     *
     * Time:
     *
     *      O(n * 2^n)
     *
     * Space:
     *
     *      O(n)
     *
     * recursion depth
     *
     * Output:
     *
     *      O(n * 2^n)
     *
     * -------------------------------------------------------------------------
     * Interview Usefulness
     * -------------------------------------------------------------------------
     *
     * This is the canonical template every interviewer expects.
     *
     * Learning this one implementation unlocks nearly every
     * combination-style backtracking problem.
     */

    static class Optimal {

        static List<List<Integer>> subsets(int[] nums) {

            List<List<Integer>> answer = new ArrayList<>();

            backtrack(
                    answer,
                    new ArrayList<>(),
                    nums,
                    0
            );

            return answer;
        }

        private static void backtrack(
                List<List<Integer>> answer,
                List<Integer> temp,
                int[] nums,
                int start) {

            // 🟢 Invariant:
            // temp already forms one valid subset.
            // Record it before making further choices.
            answer.add(new ArrayList<>(temp));

            for (int i = start; i < nums.length; i++) {

                // 🟢 Invariant:
                // nums[i] becomes the next chosen element.
                temp.add(nums[i]);

                // 🟢 Search space shrinks.
                // Earlier indices are permanently frozen.
                backtrack(answer, temp, nums, i + 1);

                // 🟢 Restore parent state before exploring sibling branch.
                temp.remove(temp.size() - 1);
            }
        }
    }

/*
 * =========================================================================
 * Subsets II (Duplicates Allowed in Input)
 * =========================================================================
 *
 * Problem
 * -------
 *
 * nums may contain duplicate values.
 *
 * Example:
 *
 * [1,2,2]
 *
 * Required:
 *
 * Unique subsets only.
 *
 * Therefore sorting becomes mandatory.
 *
 * =========================================================================
 * New Invariant
 * =========================================================================
 *
 * At one recursion depth,
 * only the FIRST occurrence of an equal value may begin a branch.
 *
 * Deeper recursion is allowed to reuse duplicates.
 *
 * =========================================================================
 * Why Sorting Is Required
 * =========================================================================
 *
 * Duplicate detection compares:
 *
 * nums[i]
 *
 * with
 *
 * nums[i-1]
 *
 * Equal values must therefore become adjacent.
 *
 * Without sorting,
 * duplicates cannot be detected locally.
 *
 * =========================================================================
 * The Famous Skip Rule
 * =========================================================================
 *
 * if (i > start && nums[i] == nums[i - 1])
 *      continue;
 *
 * This is one of the most important interview conditions
 * in all backtracking problems.
 *
 * Understand it instead of memorizing it.
 *
 * =========================================================================
 * Meaning of i > start
 * =========================================================================
 *
 * start identifies the current recursion depth.
 *
 * During one for-loop,
 * every different value of i corresponds to sibling branches.
 *
 * Therefore:
 *
 * i > start
 *
 * means:
 *
 * "I am NOT selecting the first candidate at this depth."
 *
 * Instead,
 * I am attempting another sibling.
 *
 * =========================================================================
 * Meaning of nums[i] == nums[i-1]
 * =========================================================================
 *
 * The current sibling starts with exactly the same value
 * as the previous sibling.
 *
 * Both siblings would therefore generate identical subset families.
 *
 * Only one should survive.
 */    /*
     * =========================================================================
     * Why Skipping Only Horizontal Duplicates Is Correct
     * =========================================================================
     *
     * Consider:
     *
     * nums = [1,1,2]
     *
     * After sorting:
     *
     * index:
     *   0 1 2
     *
     * value:
     *   1 1 2
     *
     * ------------------------------------------------------------
     * Root Level
     * ------------------------------------------------------------
     *
     * start = 0
     *
     * Loop:
     *
     * i = 0
     *      choose first 1
     *
     * i = 1
     *      second 1
     *
     * Since:
     *
     *      i > start
     *
     * and
     *
     *      nums[1] == nums[0]
     *
     * both branches begin with:
     *
     *      [1]
     *
     * identical subtree.
     *
     * Therefore skip.
     *
     * ------------------------------------------------------------
     * Deeper Level
     * ------------------------------------------------------------
     *
     * After choosing first 1:
     *
     * temp = [1]
     *
     * start = 1
     *
     * Loop:
     *
     * i = 1
     *
     * Here:
     *
     * i == start
     *
     * therefore:
     *
     * i > start
     *
     * is FALSE.
     *
     * So second 1 is allowed.
     *
     * That correctly generates:
     *
     * [1,1]
     *
     * which is a valid unique subset.
     *
     * =========================================================================
     * Horizontal vs Vertical Duplicate Rule
     * =========================================================================
     *
     * Horizontal
     * ----------
     *
     * Same recursion depth.
     *
     * Same parent.
     *
     * Same for-loop.
     *
     * Duplicate siblings.
     *
     * Skip.
     *
     * Vertical
     * --------
     *
     * Deeper recursion.
     *
     * Different parent.
     *
     * Different subset.
     *
     * Allow.
     *
     * Quick memory rule:
     *
     *      Skip duplicates horizontally.
     *
     *      Allow duplicates vertically.
     *
     * =========================================================================
     * Why This Condition Is Sufficient
     * =========================================================================
     *
     * if (i > start && nums[i] == nums[i-1])
     *
     * guarantees:
     *
     * ✓ first duplicate survives
     *
     * ✓ remaining siblings skipped
     *
     * ✓ deeper duplicates still allowed
     *
     * Therefore:
     *
     * every unique subset appears exactly once.
     */

    static class SubsetsII {

        static List<List<Integer>> subsetsWithDup(int[] nums) {

            Arrays.sort(nums);

            List<List<Integer>> answer = new ArrayList<>();

            backtrack(
                    answer,
                    new ArrayList<>(),
                    nums,
                    0
            );

            return answer;
        }

        private static void backtrack(
                List<List<Integer>> answer,
                List<Integer> temp,
                int[] nums,
                int start) {

            answer.add(new ArrayList<>(temp));

            for (int i = start; i < nums.length; i++) {

                // 🟢 Skip only duplicate sibling branches.
                if (i > start && nums[i] == nums[i - 1]) {
                    continue;
                }

                temp.add(nums[i]);

                backtrack(answer, temp, nums, i + 1);

                temp.remove(temp.size() - 1);
            }
        }
    }

/*
 * =========================================================================
 * Relationship With Permutations II
 * =========================================================================
 *
 * Both problems skip duplicate branches.
 *
 * But the invariant is different.
 *
 * ------------------------------------------------------------
 * Subsets II
 * ------------------------------------------------------------
 *
 * Order does NOT matter.
 *
 * start permanently freezes earlier indices.
 *
 * Duplicate skipping:
 *
 * if (i > start && nums[i] == nums[i-1])
 *
 * ------------------------------------------------------------
 * Permutations II
 * ------------------------------------------------------------
 *
 * Order matters.
 *
 * No start pointer exists.
 *
 * visited[] decides whether a duplicate may be used.
 *
 * Typical condition:
 *
 * if (i > 0
 *     && nums[i] == nums[i-1]
 *     && !visited[i-1])
 *
 * skip.
 *
 * =========================================================================
 * Quick Comparison Table
 * =========================================================================
 *
 *                     Subsets        Permutations
 * ----------------------------------------------------------
 * Order matters?      No             Yes
 * start pointer?      Yes            No
 * visited[]?          No             Yes
 * Save answer?        Every node     Leaf only
 * Recursion ends?     Empty suffix   Length == n
 * Duplicate rule?     Same level     Same level + visited
 *
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * "The invariant is that every recursive call owns only the suffix beginning
 * at start. Earlier indices are permanently frozen, so elements are selected
 * in increasing index order. Because order is ignored for subsets, this
 * prevents duplicate orderings automatically.
 *
 * At every recursion state the current path is already a valid subset, so I
 * record it immediately. Then I extend the subset by trying each remaining
 * candidate exactly once. After recursion I undo the choice to restore the
 * parent state before exploring the next sibling branch.
 *
 * Termination is guaranteed because every recursive call advances start to
 * i + 1, shrinking the remaining search space."
 *
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================================
 *
 * Trigger
 * -------
 * Generate every subset.
 *
 * Pattern
 * -------
 * Combination-style backtracking.
 *
 * Invariant
 * ---------
 * temp is always one valid subset.
 *
 * Search Space
 * ------------
 * [start ... n-1]
 *
 * Discard Rule
 * ------------
 * Earlier indices are frozen forever.
 *
 * Save Answer
 * -----------
 * Immediately.
 *
 * Recursive Transition
 * --------------------
 * i + 1
 *
 * Common Trap
 * -----------
 * Using start + 1.
 *
 * Edge Cases
 * ----------
 * Empty subset.
 * Single element.
 * Duplicate values (Subsets II).
 *
 * One-liner
 * ---------
 * Move forward, choose, recurse, undo.
 *
 * Re-derivation Cue
 * -----------------
 * Current subset is already valid.
 * Remaining indices are future choices.
 *
 * =========================================================================
 * 🔄 VARIATIONS & TWEAKS
 * =========================================================================
 *
 * 1. Subsets II
 *    Preserve invariant by sorting and skipping duplicate siblings.
 *
 * 2. Combinations (n choose k)
 *    Same template.
 *    Stop when path size == k.
 *
 * 3. Combination Sum
 *    Recursive call uses i instead of i + 1 because reuse is allowed.
 *
 * 4. Combination Sum II
 *    Same template plus duplicate skipping.
 *
 * 5. Permutations
 *    Pattern boundary:
 *    start disappears.
 *    visited[] becomes mandatory.
 */    /*
     * =========================================================================
     * 🧠 MASTERY CHECKLIST
     * =========================================================================
     *
     * Can you answer these without looking at the implementation?
     *
     * □ What is the invariant?
     *
     *      temp is always one valid subset corresponding to the current DFS path.
     *
     * □ What is the search target?
     *
     *      Enumerate every possible subset exactly once.
     *
     * □ What is the search space?
     *
     *      Indices in the suffix [start ... n-1].
     *
     * □ What is the discard rule?
     *
     *      Every index before start is permanently frozen.
     *
     * □ Why does recursion terminate?
     *
     *      Every recursive call advances to i + 1, strictly shrinking the
     *      remaining search space.
     *
     * □ Why does the naive permutation-style solution fail?
     *
     *      It revisits earlier indices and produces duplicate orderings such as
     *      [1,2] and [2,1].
     *
     * □ Why do we save before exploring children?
     *
     *      Every recursion state already represents a complete valid subset.
     *
     * □ Why must we copy temp?
     *
     *      Stored answers must remain immutable snapshots.
     *
     * □ Why recurse with i + 1 instead of start + 1?
     *
     *      The next search space depends on the element actually chosen.
     *
     * □ What changes for Subsets II?
     *
     *      Sort first.
     *      Skip duplicate sibling branches:
     *
     *      if (i > start && nums[i] == nums[i - 1])
     *
     * □ Pattern boundary?
     *
     *      If order matters, this is no longer a subsets problem.
     *      Switch to permutation backtracking with visited[].
     */

    public static void main(String[] args) {

        // Enable assertions:
        // java -ea Subsets

        /*
         * Happy Path
         */
        List<List<Integer>> result = Optimal.subsets(new int[]{1, 2, 3});

        // 2^3 subsets must exist.
        assert result.size() == 8;

        assert result.contains(List.of());
        assert result.contains(List.of(1));
        assert result.contains(List.of(2));
        assert result.contains(List.of(3));
        assert result.contains(List.of(1, 2));
        assert result.contains(List.of(1, 3));
        assert result.contains(List.of(2, 3));
        assert result.contains(List.of(1, 2, 3));

        /*
         * Boundary:
         * Single element.
         */
        result = Optimal.subsets(new int[]{0});

        assert result.size() == 2;
        assert result.contains(List.of());
        assert result.contains(List.of(0));

        /*
         * Two elements.
         */
        result = Optimal.subsets(new int[]{5, 8});

        assert result.size() == 4;
        assert result.contains(List.of());
        assert result.contains(List.of(5));
        assert result.contains(List.of(8));
        assert result.contains(List.of(5, 8));

        /*
         * Negative values.
         */
        result = Optimal.subsets(new int[]{-2, 4});

        assert result.size() == 4;
        assert result.contains(List.of(-2));
        assert result.contains(List.of(4));
        assert result.contains(List.of(-2, 4));

        /*
         * Brute force and optimal should generate the same count.
         */
        List<List<Integer>> brute = BruteForceBitmask.subsets(new int[]{1, 2, 3});
        List<List<Integer>> optimal = Optimal.subsets(new int[]{1, 2, 3});

        assert brute.size() == optimal.size();

        /*
         * Improved binary-decision DFS should also generate all subsets.
         */
        List<List<Integer>> improved =
                ImprovedBinaryDecision.subsets(new int[]{1, 2, 3});

        assert improved.size() == 8;

        /*
         * Subsets II:
         * Duplicate values should not create duplicate subsets.
         */
        List<List<Integer>> duplicateResult =
                SubsetsII.subsetsWithDup(new int[]{1, 2, 2});

        assert duplicateResult.size() == 6;

        assert duplicateResult.contains(List.of());
        assert duplicateResult.contains(List.of(1));
        assert duplicateResult.contains(List.of(2));
        assert duplicateResult.contains(List.of(2, 2));
        assert duplicateResult.contains(List.of(1, 2));
        assert duplicateResult.contains(List.of(1, 2, 2));

        /*
         * Duplicate-only input.
         */
        duplicateResult =
                SubsetsII.subsetsWithDup(new int[]{1, 1});

        assert duplicateResult.size() == 3;

        assert duplicateResult.contains(List.of());
        assert duplicateResult.contains(List.of(1));
        assert duplicateResult.contains(List.of(1, 1));

        /*
         * Maximum size sanity check.
         *
         * n = 10
         *
         * Total subsets = 1024.
         */
        int[] ten = {1,2,3,4,5,6,7,8,9,10};

        result = Optimal.subsets(ten);

        assert result.size() == 1024;

        /*
         * Snapshot correctness.
         *
         * Ensure stored subsets are independent copies.
         */
        List<List<Integer>> snapshotTest =
                Optimal.subsets(new int[]{1, 2});

        List<Integer> first = snapshotTest.get(0);

        assert first.isEmpty();

        System.out.println("All assertions passed.");
    }

}
