package org.chijai.day11.backtracking.session1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinationSum {

/*
 * ============================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================
 *
 * Title:
 * Combination Sum
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * Backtracking
 * DFS
 * Recursion
 * Combination Generation
 *
 * LeetCode:
 * https://leetcode.com/problems/combination-sum/
 *
 * ------------------------------------------------------------
 * Problem Statement
 * ------------------------------------------------------------
 *
 * Given an array of DISTINCT positive integers candidates
 * and a target integer target,
 * return every unique combination whose sum equals target.
 *
 * Every candidate may be chosen UNLIMITED number of times.
 *
 * Two combinations are considered identical if they contain
 * exactly the same multiset of numbers regardless of order.
 *
 * Therefore:
 *
 * [2,2,3]
 * and
 * [2,3,2]
 *
 * represent the SAME combination and only one should appear.
 *
 * Return the combinations in any order.
 *
 * ------------------------------------------------------------
 * Constraints
 * ------------------------------------------------------------
 *
 * 1 <= candidates.length <= 30
 *
 * 2 <= candidates[i] <= 40
 *
 * candidates are distinct.
 *
 * 1 <= target <= 40
 *
 * ------------------------------------------------------------
 * Example 1
 * ------------------------------------------------------------
 *
 * candidates = [2,3,6,7]
 * target = 7
 *
 * Output:
 *
 * [
 *   [2,2,3],
 *   [7]
 * ]
 *
 * ------------------------------------------------------------
 * Example 2
 * ------------------------------------------------------------
 *
 * candidates = [2,3,5]
 * target = 8
 *
 * Output:
 *
 * [
 *   [2,2,2,2],
 *   [2,3,3],
 *   [3,5]
 * ]
 *
 * ------------------------------------------------------------
 * Example 3
 * ------------------------------------------------------------
 *
 * candidates = [2]
 * target = 1
 *
 * Output:
 *
 * []
 *
 * ============================================================
 * Closely Related Problems
 * ============================================================
 *
 * Combination Sum II
 * ------------------
 *
 * Each element may be used ONLY ONCE.
 *
 * Input may contain duplicates.
 *
 * Requires duplicate skipping.
 *
 *
 * Combination Sum III
 * -------------------
 *
 * Numbers:
 * 1...9
 *
 * Exactly k numbers.
 *
 * Every number used once.
 *
 *
 * Coin Change II
 * --------------
 *
 * Count combinations instead of generating them.
 *
 * Same ordering invariant.
 *
 * DP replaces backtracking.
 *
 * ============================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ============================================================
 *
 * Pattern
 * -------
 *
 * Backtracking with Monotonic Search Space
 *
 * Archetype
 * ---------
 *
 * Combination Generation
 *
 * Not permutation generation.
 *
 * ------------------------------------------------------------
 * Core Invariant
 * ------------------------------------------------------------
 *
 * Every recursive level is responsible for selecting the NEXT
 * element only from the current index onward.
 *
 * Therefore:
 *
 * indices never decrease.
 *
 * Since indices never move backwards,
 * every combination has exactly one construction path.
 *
 * This single invariant removes duplicate permutations.
 *
 * ------------------------------------------------------------
 * Search Space
 * ------------------------------------------------------------
 *
 * State =
 *
 * (remaining target,
 *  current combination,
 *  starting index)
 *
 * Transition =
 *
 * choose one candidate
 *
 * recurse
 *
 * undo choice
 *
 * ------------------------------------------------------------
 * Why It Works
 * ------------------------------------------------------------
 *
 * Unlimited reuse is achieved by allowing recursion to stay
 * on the SAME index.
 *
 * Duplicate combinations disappear because future choices are
 * restricted to the suffix beginning at the current index.
 *
 * ------------------------------------------------------------
 * Recognition Signals
 * ------------------------------------------------------------
 *
 * ✓ Generate every combination.
 *
 * ✓ Order inside answer does not matter.
 *
 * ✓ Need actual lists instead of counts.
 *
 * ✓ Candidate can be reused.
 *
 * ✓ Search tree naturally grows by choices.
 *
 * ------------------------------------------------------------
 * When To Use
 * ------------------------------------------------------------
 *
 * Combination enumeration.
 *
 * Subset generation.
 *
 * Partition problems.
 *
 * Expression generation.
 *
 * DFS over implicit search trees.
 *
 * ------------------------------------------------------------
 * When NOT To Use
 * ------------------------------------------------------------
 *
 * Need only minimum value.
 *
 * Need only count.
 *
 * Large state overlap.
 *
 * Optimization problems suited for DP.
 *
 * ------------------------------------------------------------
 * Comparison
 * ------------------------------------------------------------
 *
 * Subsets
 * --------
 * Every path is valid.
 *
 * Combination Sum
 * ---------------
 * Only paths reaching target are valid.
 *
 * Permutations
 * ------------
 * Position matters.
 *
 * Combination Sum
 * ---------------
 * Position does NOT matter.
 *
 * Coin Change II
 * --------------
 * Same ordering invariant.
 *
 * Replace DFS with DP counting.
 *
 * ============================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================
 *
 * Mental Model
 * ------------------------------------------------------------
 *
 * Imagine candidates arranged from left to right.
 *
 * Once you move right,
 * you are NEVER allowed to move left.
 *
 * You may:
 *
 * stay
 *
 * or
 *
 * move right.
 *
 * You may never move left.
 *
 * That single restriction completely eliminates duplicate
 * ordering.
 *
 * ------------------------------------------------------------
 * Primary Invariant
 * ------------------------------------------------------------
 *
 * start represents the first candidate that is still legal.
 *
 * Every recursive call guarantees:
 *
 * all future selections come from
 *
 * [start ... end]
 *
 * Never before start.
 *
 * ------------------------------------------------------------
 * Variable Meanings
 * ------------------------------------------------------------
 *
 * temp
 * ----
 * Current partial combination.
 *
 * remain
 * ------
 * Remaining sum still required.
 *
 * start
 * -----
 * First legal index.
 *
 * i
 * -
 * Candidate currently under consideration.
 *
 * ------------------------------------------------------------
 * Allowed Moves
 * ------------------------------------------------------------
 *
 * Pick candidate i.
 *
 * Reduce remain.
 *
 * Stay at i.
 *
 * OR later move beyond i.
 *
 * ------------------------------------------------------------
 * Forbidden Moves
 * ------------------------------------------------------------
 *
 * Return to earlier indices.
 *
 * Generate reordered versions.
 *
 * Revisit discarded prefix.
 *
 * ------------------------------------------------------------
 * The Famous "i" vs "start"
 * ------------------------------------------------------------
 *
 * This is the most common interview question.
 *
 * Suppose:
 *
 * candidates =
 *
 * [2,3,6,7]
 *
 * target = 7
 *
 * Inside the loop:
 *
 * for (int i = start; i < n; i++)
 *
 * we recurse using
 *
 * backtrack(..., i)
 *
 * NOT
 *
 * backtrack(..., start)
 *
 * Why?
 *
 * Because i identifies WHICH candidate was chosen.
 *
 * start only identifies where the loop began.
 *
 * The recursion must inherit the chosen candidate,
 * not the beginning of the loop.
 *
 * ------------------------------------------------------------
 * Example
 * ------------------------------------------------------------
 *
 * Current path:
 *
 * [2]
 *
 * start = 0
 *
 * Loop reaches:
 *
 * i = 1
 *
 * meaning candidate 3 was chosen.
 *
 * If recursion uses:
 *
 * backtrack(..., i)
 *
 * future choices become
 *
 * [3,6,7]
 *
 * which is correct.
 *
 * If recursion instead uses:
 *
 * backtrack(..., start)
 *
 * future choices become
 *
 * [2,3,6,7]
 *
 * allowing 2 again AFTER 3,
 * creating reordered duplicates.
 *
 * Example duplicates:
 *
 * [2,2,3]
 * [2,3,2]
 * [3,2,2]
 *
 * Same combination.
 *
 * Multiple construction paths.
 *
 * Broken invariant.
 *
 * ------------------------------------------------------------
 * Why not i + 1 ?
 * ------------------------------------------------------------
 *
 * i + 1 means
 *
 * current element can never be chosen again.
 *
 * That changes the problem into
 * Combination Sum II.
 *
 * Example:
 *
 * target = 7
 *
 * candidates =
 *
 * [2,3,6,7]
 *
 * Combination
 *
 * [2,2,3]
 *
 * immediately becomes impossible.
 */

    /*
     * ------------------------------------------------------------
     * Correctness Intuition
     * ------------------------------------------------------------
     *
     * Every valid combination has exactly one non-decreasing index
     * sequence.
     *
     * Since recursion never moves left,
     * every valid combination is discovered exactly once.
     *
     * Since every legal choice is explored,
     * no valid combination is missed.
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * A branch terminates when:
     *
     * remain < 0
     *      Impossible to recover because all candidates are positive.
     *
     * remain == 0
     *      A valid combination has been constructed.
     *
     * start == candidates.length
     *      No further candidates remain.
     *
     * ------------------------------------------------------------
     * Why Naive Solutions Fail
     * ------------------------------------------------------------
     *
     * Naive recursion usually makes every recursive call start from
     * index 0.
     *
     * That allows:
     *
     * [2,3]
     * [3,2]
     *
     * to be generated independently.
     *
     * The algorithm is solving permutations instead of combinations.
     *
     * The ordering invariant is lost.
     *
     * ============================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ============================================================
     *
     * ------------------------------------------------------------
     * Mistake 1
     * ------------------------------------------------------------
     *
     * Recursive call:
     *
     * backtrack(..., start)
     *
     * instead of
     *
     * backtrack(..., i)
     *
     * Looks correct because start was the loop boundary.
     *
     * Actually wrong because recursion must inherit the element
     * that was just selected.
     *
     * Violated Invariant
     * ------------------
     *
     * Future search must begin from the chosen index.
     *
     * Counterexample
     * --------------
     *
     * candidates = [2,3]
     *
     * target = 7
     *
     * Produces
     *
     * [2,2,3]
     * [2,3,2]
     * [3,2,2]
     *
     * instead of only
     *
     * [2,2,3]
     *
     * ------------------------------------------------------------
     * Mistake 2
     * ------------------------------------------------------------
     *
     * Recursive call:
     *
     * backtrack(..., i + 1)
     *
     * for Combination Sum.
     *
     * Looks reasonable because many subset problems do this.
     *
     * Violated Invariant
     * ------------------
     *
     * Unlimited reuse disappears.
     *
     * Counterexample
     * --------------
     *
     * candidates = [2,3,6,7]
     *
     * target = 7
     *
     * [2,2,3]
     *
     * is never generated.
     *
     * ------------------------------------------------------------
     * Mistake 3
     * ------------------------------------------------------------
     *
     * Forgetting to remove the last chosen value.
     *
     * Violated Invariant
     * ------------------
     *
     * temp must always equal the current recursion path.
     *
     * Debug Symptom
     * -------------
     *
     * Answer contains values that belong to previous branches.
     *
     * ------------------------------------------------------------
     * Mistake 4
     * ------------------------------------------------------------
     *
     * Store temp directly.
     *
     * list.add(temp)
     *
     * instead of
     *
     * new ArrayList<>(temp)
     *
     * Violated Invariant
     * ------------------
     *
     * Stored answer must become immutable.
     *
     * Debug Symptom
     * -------------
     *
     * Every answer eventually becomes identical.
     *
     * ------------------------------------------------------------
     * Mistake 5
     * ------------------------------------------------------------
     *
     * Forgetting:
     *
     * remain < 0
     *
     * pruning.
     *
     * Consequence
     * -----------
     *
     * Huge unnecessary search tree.
     *
     * ============================================================
     * ⚙ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Step 1
     * ------
     *
     * Create answer list.
     *
     * Step 2
     * ------
     *
     * Create current path.
     *
     * Step 3
     * ------
     *
     * Call
     *
     * dfs(
     *      remain = target,
     *      start = 0
     * )
     *
     * Step 4
     * ------
     *
     * Base cases
     *
     * remain < 0
     * return
     *
     * remain == 0
     * copy current path
     * return
     *
     * Step 5
     * ------
     *
     * Iterate
     *
     * i = start ... end
     *
     * Step 6
     * ------
     *
     * Choose
     *
     * path.add(candidate)
     *
     * Step 7
     * ------
     *
     * Recurse
     *
     * remain - candidate
     *
     * start = i
     *
     * Step 8
     * ------
     *
     * Undo
     *
     * remove last element.
     *
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * dfs(remain,start)
     *
     * if remain<0
     *      return
     *
     * if remain==0
     *      save copy
     *      return
     *
     * for every i from start
     *
     *      choose
     *
     *      dfs(remain-value,i)
     *
     *      undo
     *
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    /**
     * ============================================================
     * Brute Force
     * ============================================================
     *
     * Idea
     * ----
     *
     * Generate every sequence.
     *
     * Afterwards:
     *
     * sort every sequence
     *
     * remove duplicates.
     *
     * Invariant
     * ---------
     *
     * None.
     *
     * Search freely.
     *
     * Limitation
     * ----------
     *
     * Massive duplicate work.
     *
     * Complexity
     * ----------
     *
     * Exponential.
     *
     * Worse because duplicate permutations are explored.
     *
     * Interview Usefulness
     * --------------------
     *
     * Demonstrates why ordering invariant matters.
     */
    static class BruteForce {

        List<List<Integer>> combinationSum(int[] candidates, int target) {

            List<List<Integer>> answer = new ArrayList<>();

            dfs(answer, new ArrayList<>(), candidates, target);

            return answer;
        }

        private void dfs(List<List<Integer>> answer,
                         List<Integer> path,
                         int[] candidates,
                         int remain) {

            if (remain < 0) {
                return;
            }

            if (remain == 0) {
                List<Integer> copy = new ArrayList<>(path);
                copy.sort(Integer::compareTo);

                if (!answer.contains(copy)) {
                    answer.add(copy);
                }
                return;
            }

            for (int value : candidates) {

                path.add(value);

                dfs(answer, path, candidates, remain - value);

                path.remove(path.size() - 1);
            }
        }
    }

    /**
     * ============================================================
     * Improved
     * ============================================================
     *
     * Idea
     * ----
     *
     * Introduce the monotonic index invariant.
     *
     * Instead of generating every ordering,
     * directly prevent illegal reorderings.
     *
     * Invariant
     * ---------
     *
     * Future indices never decrease.
     *
     * Improvement
     * -----------
     *
     * Duplicate permutations disappear naturally.
     *
     * Complexity
     * ----------
     *
     * Exponential search.
     *
     * Far smaller than brute force.
     *
     * Interview Usefulness
     * --------------------
     *
     * This is already an acceptable interview solution.
     */
    static class Improved {

        List<List<Integer>> combinationSum(int[] candidates, int target) {

            List<List<Integer>> answer = new ArrayList<>();

            dfs(answer,
                    new ArrayList<>(),
                    candidates,
                    target,
                    0);

            return answer;
        }

        private void dfs(List<List<Integer>> answer,
                         List<Integer> path,
                         int[] candidates,
                         int remain,
                         int start) {

            if (remain < 0) {
                return;
            }

            if (remain == 0) {
                answer.add(new ArrayList<>(path));
                return;
            }

            for (int i = start; i < candidates.length; i++) {

                path.add(candidates[i]);

                dfs(answer,
                        path,
                        candidates,
                        remain - candidates[i],
                        i);

                path.remove(path.size() - 1);
            }
        }
    }    /**
     * ============================================================
     * Optimal (Interview Preferred)
     * ============================================================
     *
     * Idea
     * ----
     *
     * Maintain a monotonic search space.
     *
     * At every recursion level we may:
     *
     * 1. stay on the same candidate (reuse allowed)
     * 2. move to a larger index
     *
     * We never move left.
     *
     * Therefore every mathematical combination has exactly one
     * construction path.
     *
     * ------------------------------------------------------------
     * Pattern
     * ------------------------------------------------------------
     *
     * Backtracking
     *
     * +
     *
     * Monotonic Search Space
     *
     * ------------------------------------------------------------
     * State
     * ------------------------------------------------------------
     *
     * remain
     *
     * Current remaining target.
     *
     * path
     *
     * Current partial combination.
     *
     * start
     *
     * First legal candidate index.
     *
     * ------------------------------------------------------------
     * Transition
     * ------------------------------------------------------------
     *
     * Choose candidate i.
     *
     * Append to path.
     *
     * Reduce remain.
     *
     * Continue from i.
     *
     * Undo.
     *
     * ------------------------------------------------------------
     * Invariant
     * ------------------------------------------------------------
     *
     * Every path contains candidate indices in non-decreasing
     * order.
     *
     * Since index order is unique,
     * every combination is unique.
     *
     * ------------------------------------------------------------
     * Correctness
     * ------------------------------------------------------------
     *
     * Soundness
     * ---------
     *
     * Every stored path sums exactly to target because we only
     * store when remain becomes zero.
     *
     * Completeness
     * ------------
     *
     * Every legal combination has exactly one monotonic index
     * ordering.
     *
     * Our DFS explores all such orderings.
     *
     * Therefore no valid combination is missed.
     *
     * Uniqueness
     * ----------
     *
     * Left movement is forbidden.
     *
     * Hence reordered permutations cannot exist.
     *
     * ------------------------------------------------------------
     * Complexity
     * ------------------------------------------------------------
     *
     * Time
     *
     * Exponential in the search tree.
     *
     * Often written as:
     *
     * O(2^N)
     *
     * though practical complexity depends on target and branching.
     *
     * Space
     *
     * O(target / minimumCandidate)
     *
     * recursion depth
     *
     * excluding output.
     *
     * ------------------------------------------------------------
     * Interview Usefulness
     * ------------------------------------------------------------
     *
     * Standard expected solution.
     *
     * Easy to derive under pressure because every line follows
     * directly from the invariant.
     */
    static class Optimal {

        List<List<Integer>> combinationSum(int[] candidates,
                                           int target) {

            List<List<Integer>> answer = new ArrayList<>();

            backtrack(answer,
                    new ArrayList<>(),
                    candidates,
                    target,
                    0);

            return answer;
        }

        private void backtrack(List<List<Integer>> answer,
                               List<Integer> path,
                               int[] candidates,
                               int remain,
                               int start) {

            // 🟢 Invariant:
            // path currently represents exactly one DFS branch.

            if (remain < 0) {

                // Remaining target became impossible.
                // Positive numbers cannot recover it.

                return;
            }

            if (remain == 0) {

                // Copy because path will be modified during backtracking.

                answer.add(new ArrayList<>(path));

                return;
            }

            for (int i = start; i < candidates.length; i++) {

                // Choose current candidate.

                path.add(candidates[i]);

                // Reuse is allowed.
                // Stay on the same index.

                backtrack(answer,
                        path,
                        candidates,
                        remain - candidates[i],
                        i);

                // Restore invariant before exploring sibling branch.

                path.remove(path.size() - 1);
            }
        }
    }

    /*
     * BACKTRACKING PATH INVARIANT
     *
     * When backtrack() returns,
     * path must be exactly as it was
     * when that call started.
     *
     *
     * TREE VIEW
     *
     * Suppose candidates allow:
     *
     *                  []
     *               /       \
     *             [2]       [3]
     *            /   \
     *         [2,2]  [2,3]
     *           |
     *        [2,2,3]
     *
     *
     * DFS movement:
     *
     * []
     *  |
     *  +-- add 2 --------> [2]
     *                       |
     *                       +-- add 2 --------> [2,2]
     *                                           |
     *                                           +-- add 3 --> [2,2,3]
     *                                                        |
     *                                                        +-- remove 3
     *                                           <------------+
     *                                           [2,2]
     *                                           |
     *                                           +-- remove 2
     *                       <-------------------+
     *                       [2]
     *                       |
     *                       +-- try next choice
     *
     *
     * Important:
     *
     * A child may modify path many times,
     * but before the child returns,
     * it restores path to the state
     * it received from its parent.
     *
     * Example:
     *
     * Parent enters recursion with:
     *
     * path = [2]
     *
     * Child temporarily creates:
     *
     * [2,2]
     * [2,2,3]
     *
     * But child restores everything before returning:
     *
     * [2,2,3]
     *    -> remove 3
     * [2,2]
     *    -> remove 2
     * [2]
     *
     * So parent gets back exactly:
     *
     * path = [2]
     *
     *
     * LIST USED LIKE A STACK
     *
     * We only modify the END of the list.
     *
     * PUSH:
     * path.add(value);
     *
     * POP:
     * path.remove(path.size() - 1);
     *
     * TOP:
     * path.get(path.size() - 1);
     *
     * Therefore path behaves as LIFO.
     *
     *
     * BACKTRACKING PATTERN
     *
     * CHOOSE
     * path.add(choice);
     *
     * EXPLORE
     * backtrack(...);
     *
     * UNDO
     * path.remove(path.size() - 1);
     *
     *
     * Mental model:
     *
     * GO DOWN TREE  -> PUSH choice
     * COME BACK UP  -> POP choice
     *
     * Every recursion level removes
     * exactly the choice that it added.
     */

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Q.
 * Explain the invariant.
 *
 * A.
 *
 * Every recursive call owns a suffix of the candidate array.
 *
 * Future choices are restricted to that suffix.
 *
 * Therefore indices never decrease.
 *
 * ------------------------------------------------------------
 * Q.
 * Why pass i instead of start?
 *
 * A.
 *
 * Because recursion must continue from the element that was
 * actually selected.
 *
 * start only indicates where the loop began.
 *
 * Passing start destroys the monotonic index invariant.
 *
 * ------------------------------------------------------------
 * Q.
 * Why not i + 1?
 *
 * A.
 *
 * That forbids reuse.
 *
 * It solves Combination Sum II instead.
 *
 * ------------------------------------------------------------
 * Q.
 * What is the discard rule?
 *
 * A.
 *
 * remain < 0
 *
 * Since every candidate is positive,
 * this branch can never recover.
 *
 * ------------------------------------------------------------
 * Q.
 * Why is the solution correct?
 *
 * A.
 *
 * Every valid combination has one unique non-decreasing index
 * sequence.
 *
 * DFS explores every such sequence exactly once.
 *
 * ------------------------------------------------------------
 * Q.
 * Why terminate?
 *
 * A.
 *
 * Remaining target decreases whenever a value is chosen.
 *
 * Eventually:
 *
 * remain == 0
 *
 * or
 *
 * remain < 0
 *
 * ------------------------------------------------------------
 * Q.
 * Can this be in-place?
 *
 * A.
 *
 * Yes.
 *
 * The current path is reused through choose → recurse → undo.
 *
 * Only successful answers are copied.
 *
 * ------------------------------------------------------------
 * Q.
 * Streaming feasible?
 *
 * A.
 *
 * Yes.
 *
 * Instead of storing,
 * every valid path may immediately be emitted to a callback.
 *
 * ------------------------------------------------------------
 * Q.
 * When should this pattern NOT be used?
 *
 * A.
 *
 * When only the count is required.
 *
 * Coin Change II uses DP more efficiently.
 *
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 *
 * Generate combinations.
 *
 * Unlimited reuse.
 *
 * Order irrelevant.
 *
 * ------------------------------------------------------------
 * Pattern
 * ------------------------------------------------------------
 *
 * Backtracking
 *
 * +
 *
 * Monotonic Search Space
 *
 * ------------------------------------------------------------
 * Invariant
 * ------------------------------------------------------------
 *
 * Indices never decrease.
 *
 * ------------------------------------------------------------
 * Search Target
 * ------------------------------------------------------------
 *
 * remain == 0
 *
 * ------------------------------------------------------------
 * Discard Rule
 * ------------------------------------------------------------
 *
 * remain < 0
 *
 * ------------------------------------------------------------
 * Common Trap
 * ------------------------------------------------------------
 *
 * Passing start instead of i.
 *
 * ------------------------------------------------------------
 * Edge Cases
 * ------------------------------------------------------------
 *
 * Empty answer.
 *
 * Single candidate.
 *
 * Target smaller than every candidate.
 *
 * Large reuse chain.
 *
 * ------------------------------------------------------------
 * One-Liner
 * ------------------------------------------------------------
 *
 * Stay on i to reuse.
 *
 * Never move left.
 *
 * ------------------------------------------------------------
 * Re-derivation Cue
 * ------------------------------------------------------------
 *
 * "Future choices begin from the chosen index."
 *
 * Everything else follows naturally.
 */    /*
     * ============================================================
     * 🔄 VARIATIONS & TWEAKS
     * ============================================================
     *
     * ============================================================
     * Variation 1
     * Combination Sum II
     * ============================================================
     *
     * Problem Change
     * --------------
     *
     * • Every element may be used at most once.
     * • Input may contain duplicate values.
     *
     * Pattern
     * -------
     *
     * Same backtracking pattern.
     *
     * Two invariant changes are required.
     *
     * ------------------------------------------------------------
     * Invariant Change #1
     * ------------------------------------------------------------
     *
     * Since reuse is forbidden,
     * after choosing index i,
     * future recursion starts from
     *
     * i + 1
     *
     * instead of
     *
     * i.
     *
     * ------------------------------------------------------------
     * Invariant Change #2
     * ------------------------------------------------------------
     *
     * Equal values at the same recursion depth produce identical
     * search trees.
     *
     * Therefore skip duplicate siblings.
     *
     * if (i > start && nums[i] == nums[i - 1])
     *     continue;
     *
     * The important phrase is:
     *
     * SAME DEPTH.
     *
     * We skip duplicate siblings,
     * not duplicate ancestors.
     *
     * ------------------------------------------------------------
     * Core Implementation
     * ------------------------------------------------------------
     */

    static class CombinationSumII {

        List<List<Integer>> combinationSum2(int[] candidates,
                                            int target) {

            Arrays.sort(candidates);

            List<List<Integer>> answer = new ArrayList<>();

            dfs(answer,
                    new ArrayList<>(),
                    candidates,
                    target,
                    0);

            return answer;
        }

        private void dfs(List<List<Integer>> answer,
                         List<Integer> path,
                         int[] candidates,
                         int remain,
                         int start) {

            // THIS ENTIRE dfs() CALL = ONE NODE / ONE DEPTH LEVEL

            if (remain < 0) {
                return;                     // GO BACK TO PARENT
            }

            if (remain == 0) {
                answer.add(new ArrayList<>(path));
                return;                     // GO BACK TO PARENT
            }

            // THIS LOOP = GENERATE CHILDREN OF THE CURRENT NODE
            //
            // Different i values here are SIBLINGS.
            for (int i = start; i < candidates.length; i++) {

                // SAME dfs call + different i
                // => same depth => siblings.
                // Therefore skip duplicate siblings.
                //start = first index available at this depth
                // i>start -> This is not the first child being tried from the current parent node.
                // candidates[i] == candidates[i-1] -> This child is a duplicate of the previous sibling.
                //
                if (i > start &&
                        candidates[i] == candidates[i - 1]) {
                    continue;               // SKIP THIS SIBLING
                }

                // CHOOSE EDGE TO ONE CHILD.
                path.add(candidates[i]);

                // RECURSIVE CALL = GO ONE LEVEL DEEPER.
                // Current node -> child node.
                dfs(answer,
                        path,
                        candidates,
                        remain - candidates[i],
                        i + 1);

                // CHILD RETURNED.
                // We are back at the CURRENT NODE.
                path.remove(path.size() - 1);

                // for-loop continues:
                // try NEXT SIBLING.
            }

            // Loop finished.
            // This dfs() call ends and returns to its PARENT.
        }
    }

    /*
     * Example:
     *
     * candidates = [1, 1, 2]
     *
     *
     * CASE 1: SAME DEPTH -> SKIP DUPLICATE SIBLING
     *
     * start = 0
     *
     *              []
     *            /  |  \
     *          i=0 i=1 i=2
     *           1   1   2
     *               ^
     *               duplicate sibling -> skip
     *
     * Both 1s would fill the SAME position in path:
     *
     * [1]
     * [1]
     *
     * and generate the same subtree.
     *
     *
     * CASE 2: NEXT DEPTH -> ALLOW DUPLICATE VALUE
     *
     * Choose first 1:
     *
     *              []
     *               |
     *              [1]
     *             /   \
     *          i=1   i=2
     *           1     2
     *           |     |
     *        [1,1]  [1,2]
     *
     * The second 1 is allowed here because it fills
     * the NEXT position in the path.
     *
     * It is not a duplicate sibling anymore.
     * It is a valid deeper choice.
     *
     *
     * SAME VALUE + SAME DEPTH -> skip
     * SAME VALUE + NEXT DEPTH -> allow
     */

    /*
     * DFS MOVEMENT
     *
     * candidates = [2, 3, 5]
     *
     *                         []
     *                          |
     *  i = 0: add 2 --------> [2]          // DOWN: child
     *                           |
     *           i = 1: add 3 -> [2,3]      // DOWN: child
     *                           |
     *                        dfs(...)
     *                           |
     *                     remove 3
     *                           |
     *                         [2]           // UP: back to parent
     *                           |
     *           i = 2: add 5 -> [2,5]      // SIDEWAYS: next sibling
     *                           |
     *                     remove 5
     *                           |
     *                         [2]
     *                           |
     *                     remove 2
     *  <------------------------+
     *                         []             // UP: back to root
     *                          |
     *  i = 1: add 3 --------> [3]           // SIDEWAYS: next root sibling
     *
     *
     * READ THE CODE AS:
     *
     * path.add(...)       -> go DOWN to child
     * dfs(...)            -> explore child's subtree
     * path.remove(...)    -> come UP to current node
     * i++                 -> move SIDEWAYS to next sibling
     * dfs() returns       -> go back to parent
     *
     * SAME for-loop       = siblings
     * NEW dfs() call      = one level deeper
     * RETURN from dfs()   = one level upward
     */
    /*
     * ------------------------------------------------------------
     * Alternative Duplicate Detection
     * ------------------------------------------------------------
     *
     * Another common implementation uses a visited array.
     *
     * if (i > 0 &&
     *     nums[i] == nums[i-1] &&
     *     !visited[i-1])
     *      continue;
     *
     * This version is more common in permutation problems.
     *
     * For Combination Sum II,
     * the "i > start" solution is simpler and usually preferred.
     *
     * ============================================================
     * Variation 2
     * Combination Sum III
     * ============================================================
     *
     * Changes
     * -------
     *
     * Numbers:
     *
     * 1...9
     *
     * Each used once.
     *
     * Exactly k numbers.
     *
     * Therefore recursion state additionally tracks:
     *
     * remainingCount.
     *
     * ============================================================
     */

    static class CombinationSumIII {

        List<List<Integer>> combinationSum3(int k,
                                            int target) {

            List<List<Integer>> answer = new ArrayList<>();

            dfs(answer,
                    new ArrayList<>(),
                    1,
                    k,
                    target);

            return answer;
        }

        private void dfs(List<List<Integer>> answer,
                         List<Integer> path,
                         int start,
                         int remainingCount,
                         int remain) {

            if (remain < 0 || remainingCount < 0) {
                return;
            }

            if (remain == 0 &&
                    remainingCount == 0) {

                answer.add(new ArrayList<>(path));
                return;
            }

            for (int value = start;
                 value <= 9;
                 value++) {

                path.add(value);

                dfs(answer,
                        path,
                        value + 1,
                        remainingCount - 1,
                        remain - value);

                path.remove(path.size() - 1);
            }
        }
    }

    /*
     * ============================================================
     * Variation 3
     * Coin Change II
     * ============================================================
     *
     * Goal
     * ----
     *
     * Count combinations.
     *
     * Do NOT generate them.
     *
     * Pattern Mapping
     * ---------------
     *
     * Same ordering invariant.
     *
     * Different state representation.
     *
     * Backtracking
     *      ↓
     *
     * Dynamic Programming
     *
     * ------------------------------------------------------------
     * DP State
     * ------------------------------------------------------------
     *
     * dp[x]
     *
     * =
     *
     * number of combinations that make amount x.
     *
     * ------------------------------------------------------------
     * Critical Loop Order
     * ------------------------------------------------------------
     *
     * for every coin
     *     for amount increasing
     *
     * This loop order preserves exactly the same monotonic ordering
     * invariant that prevented duplicate combinations in DFS.
     *
     * ============================================================
     * Pattern Boundary
     * ============================================================
     *
     * Unlimited reuse?
     *      recurse(i)
     *
     * Single use?
     *      recurse(i + 1)
     *
     * Order matters?
     *      permutations
     *
     * Only count?
     *      DP
     *
     * Optimization?
     *      DP / BFS / shortest path
     *
     * ============================================================
     * 🧠 MASTERY CHECKLIST
     * ============================================================
     *
     * □ I know why start exists.
     *
     * □ I know why recursion receives i.
     *
     * □ I know why Combination Sum II uses i + 1.
     *
     * □ I know why moving left creates duplicate permutations.
     *
     * □ I know why remain < 0 immediately terminates.
     *
     * □ I know why answer stores a copy.
     *
     * □ I can derive choose → recurse → undo.
     *
     * □ I can explain correctness formally.
     *
     * □ I can distinguish:
     *
     *      subsets
     *      combinations
     *      permutations
     *
     * □ I can map this directly to Coin Change II.
     *
     * □ I understand that:
     *
     * "Future search begins from the chosen index."
     *
     * Everything else is implementation detail.
     *
     * ============================================================
     * 🧪 SELF-VERIFYING TEST HELPERS
     * ============================================================
     */

    private static boolean contains(List<List<Integer>> result,
                                    Integer... values) {

        List<Integer> expected = Arrays.asList(values);

        for (List<Integer> list : result) {
            if (list.equals(expected)) {
                return true;
            }
        }

        return false;
    }    public static void main(String[] args) {

        Optimal optimal = new Optimal();

        /*
         * Happy Path
         *
         * Standard example from the problem statement.
         */
        List<List<Integer>> result1 =
                optimal.combinationSum(
                        new int[]{2, 3, 6, 7},
                        7);

        assert result1.size() == 2
                : "Expected exactly two combinations.";

        assert contains(result1, 2, 2, 3)
                : "Missing [2,2,3].";

        assert contains(result1, 7)
                : "Missing [7].";

        /*
         * Multiple valid answers.
         *
         * Verifies unlimited reuse.
         */
        List<List<Integer>> result2 =
                optimal.combinationSum(
                        new int[]{2, 3, 5},
                        8);

        assert result2.size() == 3
                : "Expected three unique combinations.";

        assert contains(result2, 2, 2, 2, 2);

        assert contains(result2, 2, 3, 3);

        assert contains(result2, 3, 5);

        /*
         * Impossible target.
         *
         * Ensures remain < 0 pruning eventually removes every branch.
         */
        List<List<Integer>> result3 =
                optimal.combinationSum(
                        new int[]{2},
                        1);

        assert result3.isEmpty()
                : "Expected empty answer.";

        /*
         * Single reusable candidate.
         */
        List<List<Integer>> result4 =
                optimal.combinationSum(
                        new int[]{4},
                        12);

        assert result4.size() == 1;

        assert contains(result4, 4, 4, 4);

        /*
         * Target equals one candidate.
         */
        List<List<Integer>> result5 =
                optimal.combinationSum(
                        new int[]{5, 8, 9},
                        8);

        assert result5.size() == 1;

        assert contains(result5, 8);

        /*
         * Candidate larger than target.
         */
        List<List<Integer>> result6 =
                optimal.combinationSum(
                        new int[]{9, 10},
                        8);

        assert result6.isEmpty();

        /*
         * Combination Sum II.
         *
         * Duplicate values should not generate duplicate answers.
         */
        CombinationSumII cs2 = new CombinationSumII();

        List<List<Integer>> cs2Result =
                cs2.combinationSum2(
                        new int[]{10, 1, 2, 7, 6, 1, 5},
                        8);

        assert cs2Result.size() == 4
                : "Expected four unique combinations.";

        assert contains(cs2Result, 1, 1, 6);

        assert contains(cs2Result, 1, 2, 5);

        assert contains(cs2Result, 1, 7);

        assert contains(cs2Result, 2, 6);

        /*
         * Combination Sum III.
         */
        CombinationSumIII cs3 = new CombinationSumIII();

        List<List<Integer>> cs3Result =
                cs3.combinationSum3(3, 7);

        assert cs3Result.size() == 1;

        assert contains(cs3Result, 1, 2, 4);

        /*
         * Interview Trap:
         *
         * There must never exist reordered duplicates like:
         *
         * [2,3,2]
         * [3,2,2]
         *
         * Only the canonical ordering survives because indices
         * never decrease.
         */
        for (List<Integer> list : result1) {

            for (int i = 1; i < list.size(); i++) {

                assert list.get(i - 1) <= list.get(i)
                        : "Invariant broken: indices moved left.";
            }
        }

        /*
         * Stressing repeated reuse.
         */
        List<List<Integer>> result7 =
                optimal.combinationSum(
                        new int[]{1},
                        5);

        assert result7.size() == 1;

        assert contains(result7, 1, 1, 1, 1, 1);

        /*
         * Boundary:
         *
         * Empty solution set.
         */
        List<List<Integer>> result8 =
                optimal.combinationSum(
                        new int[]{8},
                        7);

        assert result8.isEmpty();

        /*
         * Boundary:
         *
         * Multiple branches terminate via remain < 0.
         */
        List<List<Integer>> result9 =
                optimal.combinationSum(
                        new int[]{4, 6},
                        5);

        assert result9.isEmpty();

        /*
         * Final invariant verification.
         *
         * Every stored combination must sum exactly to target.
         */
        for (List<Integer> combination : result2) {

            int sum = 0;

            for (int value : combination) {
                sum += value;
            }

            assert sum == 8
                    : "Incorrect combination stored.";
        }

        System.out.println("All assertions passed.");
    }

    /*
     * ============================================================
     * Final Invariant Summary
     * ============================================================
     *
     * Pattern
     * -------
     *
     * Backtracking with Monotonic Search Space.
     *
     * State
     * -----
     *
     * (remain, path, start)
     *
     * Transition
     * ----------
     *
     * choose
     * recurse
     * undo
     *
     * Search Space
     * ------------
     *
     * Candidates from start onward.
     *
     * Discard Rule
     * ------------
     *
     * remain < 0
     *
     * Success
     * -------
     *
     * remain == 0
     *
     * Reuse
     * -----
     *
     * recurse(i)
     *
     * Single Use
     * ----------
     *
     * recurse(i + 1)
     *
     * Golden Invariant
     * ----------------
     *
     * Future search always begins from the chosen index.
     *
     * Therefore indices never decrease.
     *
     * Therefore every mathematical combination has exactly one
     * construction path.
     */

}

