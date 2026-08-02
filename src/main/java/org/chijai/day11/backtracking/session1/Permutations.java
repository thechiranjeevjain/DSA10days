package org.chijai.day11.backtracking.session1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Permutations {

/*
 * ============================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================
 *
 * Title:
 * Permutations
 *
 * LeetCode:
 * https://leetcode.com/problems/permutations/
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * Backtracking
 * DFS
 * Recursion
 * State Space Search
 * Combinatorics
 *
 * ------------------------------------------------------------
 * Problem
 * ------------------------------------------------------------
 *
 * Given an array of DISTINCT integers nums,
 * return every possible permutation.
 *
 * The answer may be returned in any order.
 *
 * ------------------------------------------------------------
 * Constraints
 * ------------------------------------------------------------
 *
 * 1 <= nums.length <= 6
 * -10 <= nums[i] <= 10
 * All integers are distinct.
 *
 * ------------------------------------------------------------
 * Examples
 * ------------------------------------------------------------
 *
 * Input:
 * [1,2,3]
 *
 * Output:
 * [
 *  [1,2,3],
 *  [1,3,2],
 *  [2,1,3],
 *  [2,3,1],
 *  [3,1,2],
 *  [3,2,1]
 * ]
 *
 * --------------------
 *
 * Input:
 * [0,1]
 *
 * Output:
 * [
 *  [0,1],
 *  [1,0]
 * ]
 *
 * --------------------
 *
 * Input:
 * [1]
 *
 * Output:
 * [
 *  [1]
 * ]
 *
 * ============================================================
 * Related Problem
 * ============================================================
 *
 * Permutations II
 *
 * Input may contain duplicates.
 *
 * Goal:
 * Produce UNIQUE permutations only.
 *
 * The primary backtracking skeleton remains identical.
 *
 * The only major addition is duplicate pruning after sorting.
 *
 * Duplicate Rule:
 *
 * if (i > 0 &&
 *     nums[i] == nums[i-1] &&
 *     !used[i-1])
 *     continue;
 *
 * This chapter will later derive WHY this rule is correct
 * rather than asking you to memorize it.
 */

/*
 * ============================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ============================================================
 *
 * Pattern
 * -------
 * DFS Backtracking on Decision Tree
 *
 * Archetype
 * ---------
 * Choose
 * Explore
 * Undo
 *
 * Core Invariant
 * --------------
 * Every recursive level fixes exactly ONE position
 * of the permutation.
 *
 * The path always represents a VALID prefix of one future answer.
 *
 * No element appears twice inside the current path.
 *
 * Every unused element is still available for future positions.
 *
 * Why It Works
 * ------------
 * A permutation is nothing more than repeatedly answering:
 *
 * "Which unused element should occupy the next position?"
 *
 * Every recursive call permanently fixes one additional position.
 *
 * Since each position tries every unused candidate exactly once,
 * every permutation is explored.
 *
 * Recognition Signals
 * -------------------
 * Look for:
 *
 * • Need ALL arrangements
 * • Order matters
 * • Elements cannot repeat
 * • Explore every possibility
 * • Small n (typically <= 10)
 * • Complete search required
 *
 * Typical Problems
 * ----------------
 * Permutations
 * Permutations II
 * N Queens
 * Sudoku
 * Letter Case Permutation
 * Restore IP Addresses
 * Combination Sum
 * Generate Parentheses
 *
 * When To Use
 * -----------
 * Every complete answer must be generated.
 *
 * Choices shrink after every decision.
 *
 * Current decision influences future choices.
 *
 * When NOT To Use
 * ---------------
 * Only one optimal answer required.
 *
 * Greedy suffices.
 *
 * Dynamic Programming has overlapping subproblems.
 *
 * Binary Search problems.
 *
 * Graph shortest path.
 *
 * Comparison With Similar Patterns
 * --------------------------------
 *
 * Combination
 * -----------
 * Order ignored.
 *
 * Pick or skip.
 *
 * Usually advance starting index.
 *
 * --------------------
 *
 * Subset
 * ------
 * Every element has two decisions.
 *
 * Include / Exclude.
 *
 * --------------------
 *
 * Permutation
 * -----------
 * Every level chooses ANY unused element.
 *
 * The search tree is much wider.
 *
 * State =
 * current ordered path
 * +
 * used[] array.
 */

/*
 * ============================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine filling seats.
 *
 * Position 0
 * Position 1
 * Position 2
 * ...
 *
 * At every recursive call,
 * exactly one seat is empty.
 *
 * Choose ONE unused person.
 *
 * Seat them.
 *
 * Move to the next seat.
 *
 * When every seat is occupied,
 * one permutation is complete.
 *
 * ------------------------------------------------------------
 * State
 * ------------------------------------------------------------
 *
 * nums
 * ----
 * Source values.
 *
 * Never modified.
 *
 * --------------------
 *
 * path
 * ----
 * Current partial permutation.
 *
 * Represents the prefix already fixed.
 *
 * --------------------
 *
 * used[i]
 * -------
 * Whether nums[i] already occupies
 * some earlier position.
 *
 * ------------------------------------------------------------
 * Primary Invariant
 * ------------------------------------------------------------
 *
 * path contains NO repeated indices.
 *
 * Every value in path has exactly one matching used[i]=true.
 *
 * Every unused element is still available.
 *
 * Therefore:
 *
 * path is always a legal permutation prefix.
 *
 * ------------------------------------------------------------
 * Secondary Invariant
 * ------------------------------------------------------------
 *
 * path.size()
 *
 * ==
 *
 * number of true values in used[]
 *
 * If this equality ever breaks,
 * your backtracking is incorrect.
 *
 * This becomes an excellent debugging checkpoint.
 *
 * ------------------------------------------------------------
 * Allowed Moves
 * ------------------------------------------------------------
 *
 * Pick an unused index.
 *
 * Mark it used.
 *
 * Append value.
 *
 * Recurse.
 *
 * Remove value.
 *
 * Mark unused.
 *
 * Every choose has exactly one matching unchoose.
 *
 * ------------------------------------------------------------
 * Forbidden Moves
 * ------------------------------------------------------------
 *
 * Using an already used index.
 *
 * Forgetting to undo.
 *
 * Copying path incorrectly.
 *
 * Sharing mutable lists inside answer.
 *
 * ------------------------------------------------------------
 * Why
 *
 * ans.add(new ArrayList<>(path))
 *
 * instead of
 *
 * ans.add(path)
 * ------------------------------------------------------------
 *
 * This is one of the most common interview bugs.
 *
 * path is reused throughout recursion.
 *
 * Example:
 *
 * path
 *
 * []
 *
 * add 1
 *
 * [1]
 *
 * add 2
 *
 * [1,2]
 *
 * add 3
 *
 * [1,2,3]
 *
 * Suppose we store:
 *
 * ans.add(path);
 *
 * We did NOT copy anything.
 *
 * ans merely stores another reference
 * pointing to the SAME object.
 *
 * Later,
 * backtracking removes elements.
 *
 * Eventually path becomes
 *
 * []
 *
 * Every stored answer also becomes
 *
 * []
 *
 * because every reference points
 * to the same list.
 *
 * Therefore we must snapshot the state:
 *
 * new ArrayList<>(path)
 *
 * This allocates independent memory.
 *
 * Future modifications cannot affect
 * previously generated answers.
 *
 * This is one of the highest-frequency
 * debugging questions in interviews.
 */    /*
 * ------------------------------------------------------------
 * Choose → Explore → Undo
 * ------------------------------------------------------------
 *
 * The entire algorithm is governed by one mechanical cycle.
 *
 * Step 1
 * -------
 * Choose an unused element.
 *
 * Step 2
 * -------
 * Mark it unavailable.
 *
 * Step 3
 * -------
 * Extend the current permutation.
 *
 * Step 4
 * -------
 * Explore every permutation beginning with that prefix.
 *
 * Step 5
 * -------
 * Undo every modification.
 *
 * After undo,
 * the parent recursive call must observe exactly the same state
 * it had before the child call started.
 *
 * Parent State
 *      │
 *      ▼
 * choose
 *      │
 *      ▼
 * child recursion
 *      │
 *      ▼
 * undo
 *      │
 *      ▼
 * Parent State (identical)
 *
 * This restoration property is the heart of backtracking.
 *
 * ------------------------------------------------------------
 * Variable Meanings
 * ------------------------------------------------------------
 *
 * nums
 * ----
 * Immutable input.
 *
 * used
 * ----
 * Resource allocation table.
 *
 * true
 * ----
 * Already placed somewhere earlier.
 *
 * false
 * ----
 * Still available.
 *
 * path
 * ----
 * Current permutation prefix.
 *
 * ans
 * ---
 * Completed permutations.
 *
 * ------------------------------------------------------------
 * Transition
 * ------------------------------------------------------------
 *
 * Current State
 *
 * path
 *
 * +
 *
 * used[]
 *
 * ↓
 *
 * Select one unused index.
 *
 * ↓
 *
 * New State
 *
 * path + nums[i]
 *
 * used[i]=true
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * When
 *
 * path.size()==nums.length
 *
 * every position has been assigned.
 *
 * There are no remaining decisions.
 *
 * Therefore one complete permutation exists.
 *
 * Snapshot it.
 *
 * Return.
 *
 * ------------------------------------------------------------
 * Correctness Intuition
 * ------------------------------------------------------------
 *
 * We never violate uniqueness because
 * every recursive level only considers unused indices.
 *
 * We never miss any permutation because
 * every unused index is eventually explored.
 *
 * We never duplicate permutations because
 * each recursive path corresponds to exactly one sequence
 * of chosen indices.
 *
 * ------------------------------------------------------------
 * Why Naive Thinking Fails
 * ------------------------------------------------------------
 *
 * Many beginners think:
 *
 * "I'll swap numbers until everything works."
 *
 * Swapping is a perfectly valid algorithm,
 * but its invariant is completely different.
 *
 * Mixing swap-based reasoning with used[] reasoning
 * almost always produces bugs.
 *
 * Choose one invariant.
 *
 * Stay inside it.
 *
 * This chapter focuses entirely on:
 *
 * used[]
 * +
 * path
 *
 * because it generalizes naturally to:
 *
 * Combination Sum
 * Subsets
 * Letter Case Permutation
 * N Queens
 * Sudoku
 * Word Search
 */

/*
 * ============================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================
 *
 * ------------------------------------------------------------
 * Mistake 1
 * ------------------------------------------------------------
 *
 * Forgetting
 *
 * used[i]=false;
 *
 * Why it looks correct
 * --------------------
 *
 * The recursive call finished,
 * so beginners assume the choice disappeared.
 *
 * Reality
 * -------
 *
 * used[] belongs to the parent frame.
 *
 * Without restoring it,
 * later branches incorrectly believe
 * the element is still occupied.
 *
 * Violated Invariant
 * ------------------
 *
 * Parent state must be restored exactly.
 *
 * Counterexample
 *
 * nums=[1,2]
 *
 * After generating
 *
 * [1,2]
 *
 * index 0 remains true.
 *
 * Branch
 *
 * [2,1]
 *
 * is never explored.
 *
 * ------------------------------------------------------------
 * Mistake 2
 * ------------------------------------------------------------
 *
 * Forgetting
 *
 * path.remove(...)
 *
 * Why it looks correct
 * --------------------
 *
 * The recursive call already returned.
 *
 * Reality
 * -------
 *
 * Parent receives an oversized path.
 *
 * Future branches start with garbage.
 *
 * Violated Invariant
 *
 * path must exactly equal
 * current recursion depth.
 *
 * ------------------------------------------------------------
 * Mistake 3
 * ------------------------------------------------------------
 *
 * ans.add(path)
 *
 * instead of
 *
 * ans.add(new ArrayList<>(path))
 *
 * Why it looks correct
 * --------------------
 *
 * The list currently contains
 * the desired permutation.
 *
 * Reality
 * -------
 *
 * Every answer stores
 * the same mutable object.
 *
 * During backtracking,
 * every stored answer changes.
 *
 * Final output frequently becomes
 *
 * [[],[],[],...]
 *
 * or
 *
 * repeated identical lists.
 *
 * Violated Invariant
 *
 * Completed answers
 * must never mutate again.
 *
 * ------------------------------------------------------------
 * Mistake 4
 * ------------------------------------------------------------
 *
 * Mark used AFTER recursion.
 *
 * Wrong Order
 *
 * add
 *
 * recurse
 *
 * used=true
 *
 * Reality
 * -------
 *
 * Recursive descendants still see
 * the element as available.
 *
 * Duplicate usage occurs.
 *
 * Correct Order
 *
 * used=true
 *
 * add
 *
 * recurse
 *
 * remove
 *
 * used=false
 *
 * Think:
 *
 * Reserve resource
 * BEFORE entering child.
 *
 * ------------------------------------------------------------
 * Mistake 5
 * ------------------------------------------------------------
 *
 * Using
 *
 * tempList.contains(...)
 *
 * to detect usage.
 *
 * It works.
 *
 * But complexity becomes worse.
 *
 * contains()
 *
 * costs O(n).
 *
 * used[]
 *
 * costs O(1).
 *
 * Interview preference:
 *
 * always use boolean[].
 *
 * ------------------------------------------------------------
 * Mistake 6
 * ------------------------------------------------------------
 *
 * Thinking recursion depth
 * equals answer count.
 *
 * False.
 *
 * Depth
 *
 * = n
 *
 * Leaves
 *
 * = n!
 *
 * ------------------------------------------------------------
 * Mistake 7
 * ------------------------------------------------------------
 *
 * Returning immediately
 * after first successful permutation.
 *
 * Correct only if the problem asks
 * for one solution.
 *
 * Permutations requires
 * exhaustive search.
 *
 * Therefore every sibling branch
 * must also be explored.
 *
 * ------------------------------------------------------------
 * Interview Trap
 * ------------------------------------------------------------
 *
 * Interviewer:
 *
 * "Why does
 *
 * used[i]=false
 *
 * come AFTER remove()?"
 *
 * Answer:
 *
 * Either order is actually valid,
 * provided both operations happen
 * before the next iteration begins.
 *
 * However,
 * reversing the operations can make
 * debugging slightly harder because
 * path and used[] temporarily disagree.
 *
 * Keeping:
 *
 * remove
 * then
 * used=false
 *
 * makes the transition visually mirror:
 *
 * choose
 * mark
 *
 * recurse
 *
 * unchoose
 * unmark
 *
 * making the invariant easier to inspect.
 */

/*
 * ============================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================
 *
 * Goal:
 * Be able to type the optimal solution from memory.
 *
 * Mechanical Typing Order
 * -----------------------
 *
 * 1.
 * Create answer list.
 *
 * 2.
 * Create boolean used[].
 *
 * 3.
 * Create empty path.
 *
 * 4.
 * Call dfs().
 *
 * 5.
 * Return answer.
 *
 * ------------------------------------------------------------
 * DFS Skeleton
 * ------------------------------------------------------------
 *
 * if complete
 *      snapshot
 *      return
 *
 * for every index
 *
 *      skip used
 *
 *      choose
 *
 *      recurse
 *
 *      undo     *
 * ------------------------------------------------------------
 * Function Skeleton
 * ------------------------------------------------------------
 *
 * permute(nums)
 *
 *      create answer
 *
 *      create used[]
 *
 *      create path
 *
 *      dfs(...)
 *
 *      return answer
 *
 * --------------------
 *
 * dfs(...)
 *
 *      base case
 *
 *      loop over indices
 *
 *          skip unavailable
 *
 *          choose
 *
 *          recurse
 *
 *          undo
 *
 * ------------------------------------------------------------
 * Variable Initialization
 * ------------------------------------------------------------
 *
 * List<List<Integer>> ans
 *
 * boolean[] used
 *
 * List<Integer> path
 *
 * ------------------------------------------------------------
 * Loop Skeleton
 * ------------------------------------------------------------
 *
 * for every index
 *
 *      already used?
 *
 *          continue
 *
 *      choose
 *
 *      recurse
 *
 *      undo
 *
 * ------------------------------------------------------------
 * Transition Computation
 * ------------------------------------------------------------
 *
 * Before recursion
 *
 * used[i]=true
 *
 * path.add(...)
 *
 * --------------------
 *
 * After recursion
 *
 * path.remove(last)
 *
 * used[i]=false
 *
 * ------------------------------------------------------------
 * Return
 * ------------------------------------------------------------
 *
 * Base case returns.
 *
 * Final method returns answer.
 */

    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * create answer
     *
     * create used
     *
     * dfs
     *
     * dfs:
     *
     * if complete
     *      copy path
     *      return
     *
     * for each unused element
     *
     *      choose
     *
     *      dfs
     *
     *      undo
     */

    /*
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */

    /*
     * ============================================================
     * Brute Force
     * ============================================================
     *
     * Idea
     * ----
     *
     * Generate every ordering by repeatedly
     * trying every remaining element.
     *
     * This conceptual solution naturally
     * leads to recursive backtracking.
     *
     * A truly brute-force alternative would
     * generate every sequence of length n
     * and filter invalid ones, but that is
     * exponentially worse and rarely useful.
     *
     * Invariant
     * ---------
     *
     * Current path is always a valid prefix.
     *
     * Limitation
     * ----------
     *
     * Visits all n! permutations.
     *
     * Complexity
     * ----------
     *
     * Time:
     * O(n × n!)
     *
     * There are n! leaves and copying each
     * permutation costs O(n).
     *
     * Space:
     * O(n)
     *
     * recursion stack
     *
     * +
     *
     * used[]
     *
     * Interview Usefulness
     * --------------------
     *
     * Mainly useful for explaining the search tree.
     */

    static class BruteForce {

        public List<List<Integer>> permute(int[] nums) {

            List<List<Integer>> answer = new ArrayList<>();

            dfs(
                    nums,
                    new boolean[nums.length],
                    new ArrayList<>(),
                    answer
            );

            return answer;
        }

        private void dfs(
                int[] nums,
                boolean[] used,
                List<Integer> path,
                List<List<Integer>> answer
        ) {

            if (path.size() == nums.length) {

                answer.add(new ArrayList<>(path));

                return;
            }

            for (int i = 0; i < nums.length; i++) {

                if (used[i]) {
                    continue;
                }

                used[i] = true;

                path.add(nums[i]);

                dfs(nums, used, path, answer);

                path.remove(path.size() - 1);

                used[i] = false;
            }
        }
    }

    /*
     * ============================================================
     * Improved
     * ============================================================
     *
     * Idea
     * ----
     *
     * A commonly seen implementation stores
     * only the current permutation and checks
     * whether a value already exists using:
     *
     * tempList.contains(...)
     *
     * This avoids the explicit boolean array
     * but every membership test becomes O(n).
     *
     * Invariant
     * ---------
     *
     * tempList always contains unique values.
     *
     * Improvement
     * -----------
     *
     * Simpler to explain,
     * slower to execute.
     *
     * Complexity
     * ----------
     *
     * Time:
     * O(n² × n!)
     *
     * contains()
     * is linear.
     *
     * Space:
     * O(n)
     *
     * Interview Usefulness
     * --------------------
     *
     * Acceptable,
     * but interviewers usually expect
     * the boolean[] optimization.
     */

    static class Improved {

        public List<List<Integer>> permute(int[] nums) {

            List<List<Integer>> answer = new ArrayList<>();

            backtrack(answer, new ArrayList<>(), nums);

            return answer;
        }

        private void backtrack(
                List<List<Integer>> answer,
                List<Integer> path,
                int[] nums
        ) {

            if (path.size() == nums.length) {

                answer.add(new ArrayList<>(path));

                return;
            }

            for (int value : nums) {

                if (path.contains(value)) {
                    continue;
                }

                path.add(value);

                backtrack(answer, path, nums);

                path.remove(path.size() - 1);
            }
        }
    }

    /*
     * ============================================================
     * Optimal (Interview Preferred)
     * ============================================================
     *
     * Idea
     * ----
     *
     * Maintain two synchronized structures:
     *
     * path
     *
     * +
     *
     * used[]
     *
     * This allows O(1) availability checks.
     *
     * Invariant
     * ---------
     *
     * path.size()
     * ==
     * number of true entries in used[]
     *
     * Every used index appears exactly once.
     *
     * Every unused index is still available.
     *
     * Correctness
     * -----------
     *
     * Every recursion level permanently fixes
     * exactly one additional position.
     *
     * Each unused element is explored once.
     *
     * Therefore every permutation is generated
     * exactly once.
     *
     * Complexity
     * ----------
     *
     * Time:
     * O(n × n!)
     *
     * Space:
     * O(n)
     *
     * excluding output.
     *
     * Interview Usefulness
     * --------------------
     *
     * This is the canonical implementation.
     */

    static class Optimal {

        public List<List<Integer>> permute(int[] nums) {

            List<List<Integer>> answer = new ArrayList<>();

            boolean[] used = new boolean[nums.length];

            List<Integer> path = new ArrayList<>();

            dfs(nums, used, path, answer);

            return answer;
        }        private void dfs(
                int[] nums,
                boolean[] used,
                List<Integer> path,
                List<List<Integer>> answer
        ) {

            // 🟢 Invariant:
            // path always represents a valid permutation prefix.
            if (path.size() == nums.length) {

                // 🟢 Snapshot the current state because path
                // will immediately change during backtracking.
                answer.add(new ArrayList<>(path));

                return;
            }

            for (int i = 0; i < nums.length; i++) {

                // 🟢 Already allocated to an earlier position.
                if (used[i]) {
                    continue;
                }

                // 🟢 Reserve this index before exploring children.
                used[i] = true;

                // 🟢 Extend the current permutation prefix.
                path.add(nums[i]);

                dfs(nums, used, path, answer);

                // 🟢 Restore parent's path exactly.
                path.remove(path.size() - 1);

                // 🟢 Make this index available for sibling branches.
                used[i] = false;
            }
        }
    }

/*
 * ============================================================
 * Permutations II (Unique Permutations)
 * ============================================================
 *
 * LeetCode:
 * https://leetcode.com/problems/permutations-ii/
 *
 * Input may contain duplicate values.
 *
 * Goal:
 * Return every UNIQUE permutation.
 *
 * ------------------------------------------------------------
 * What Changes?
 * ------------------------------------------------------------
 *
 * The recursion,
 * choose,
 * recurse,
 * undo,
 * invariant,
 * and complexity analysis
 * remain almost identical.
 *
 * Only duplicate pruning changes.
 *
 * ------------------------------------------------------------
 * Why Sorting Is Required
 * ------------------------------------------------------------
 *
 * Equal values must become adjacent.
 *
 * Example
 *
 * Before
 *
 * 2 1 2 1
 *
 * After sorting
 *
 * 1 1 2 2
 *
 * Now duplicate neighbors can be detected
 * using only
 *
 * nums[i]
 *
 * and
 *
 * nums[i-1]
 *
 * ------------------------------------------------------------
 * Duplicate Rule
 * ------------------------------------------------------------
 *
 * if (
 *     used[i]
 *     ||
 *     (
 *        i>0
 *        &&
 *        nums[i]==nums[i-1]
 *        &&
 *        !used[i-1]
 *     )
 * )
 *      continue;
 *
 * This single condition removes all
 * symmetric duplicate branches.
 *
 * ------------------------------------------------------------
 * Read The Rule Literally
 * ------------------------------------------------------------
 *
 * If
 *
 * current value
 *
 * equals
 *
 * previous value
 *
 * AND
 *
 * previous duplicate has NOT yet been chosen
 *
 * then skip the current duplicate.
 *
 * In other words,
 *
 * never choose the later twin
 * before the earlier twin.
 *
 * ------------------------------------------------------------
 * Why?
 * ------------------------------------------------------------
 *
 * Duplicate values create mirror branches.
 *
 * Example
 *
 * nums
 *
 * [1,1,2]
 *
 * Root
 *
 * choose first 1
 *
 * produces
 *
 * [1,...]
 *
 * Choosing the second 1 first
 * also produces
 *
 * [1,...]
 *
 * Those two search trees are identical.
 *
 * Exploring both only duplicates work.
 *
 * We intentionally explore only one.
 *
 * ------------------------------------------------------------
 * Symmetry
 * ------------------------------------------------------------
 *
 * Path A
 *
 * choose index 0
 *
 * then index 1
 *
 * →
 *
 * [1,1,2]
 *
 * --------------------
 *
 * Path B
 *
 * choose index 1
 *
 * then index 0
 *
 * →
 *
 * [1,1,2]
 *
 * Same permutation.
 *
 * Same output.
 *
 * Second branch is redundant.
 *
 * The duplicate rule deletes it.
 *
 * ------------------------------------------------------------
 * Why !used[i-1] ?
 * ------------------------------------------------------------
 *
 * This is the interview favorite.
 *
 * Suppose
 *
 * nums=[1,1,2]
 *
 * We are considering
 * the second 1.
 *
 * If the first 1 has NOT been used,
 * then starting with the second 1
 * would create a mirror image of
 * starting with the first 1.
 *
 * Therefore skip.
 *
 * However,
 * if the first 1 is already inside
 * the current path,
 * then choosing the second 1 is no
 * longer symmetric.
 *
 * It becomes necessary.
 *
 * Therefore we allow it.
 *
 * That explains the condition:
 *
 * !used[i-1]
 *
 * ------------------------------------------------------------
 * Why NOT
 *
 * nums[i]==nums[i-1]
 *
 * alone?
 * ------------------------------------------------------------
 *
 * Suppose we wrote
 *
 * if (
 *     used[i]
 *     ||
 *     (
 *        i>0
 *        &&
 *        nums[i]==nums[i-1]
 *     )
 * )
 *      continue;
 *
 * This rejects every later duplicate.
 *
 * Example
 *
 * nums=[1,1,2]
 *
 * Even after choosing
 * the first 1,
 *
 * we still cannot choose
 * the second 1.
 *
 * Therefore
 *
 * [1,1,2]
 *
 * can never be formed.
 *
 * The algorithm misses valid answers.
 *
 * ------------------------------------------------------------
 * Canonical Interpretation
 * ------------------------------------------------------------
 *
 * Earlier duplicates unlock later duplicates.
 *
 * Later duplicates remain locked
 * until the earlier duplicate
 * appears in the current path.
 *
 * Think of identical numbers
 * standing in a queue.
 *
 * Nobody may cut the queue.
 */    /*
     * ------------------------------------------------------------
     * Visual Tree
     * ------------------------------------------------------------
     *
     * nums = [1,1,2]
     *
     *                        []
     *                  /      |      \
     *               1(i0)   1(i1)    2
     *                 |       X       |
     *                 |               |
     *                 |          duplicate branch
     *                 |
     *           ------------
     *          /            \
     *      1(i1)             2
     *        |               |
     *     [1,1]          [1,2]
     *        |               |
     *        2             1(i1)
     *        |               |
     *    [1,1,2]         [1,2,1]
     *
     * Root branch beginning with
     * the second duplicate is skipped.
     *
     * Therefore every unique permutation
     * is generated exactly once.
     *
     * ------------------------------------------------------------
     * Narrated Dry Run
     * ------------------------------------------------------------
     *
     * nums=[1,1,2]
     *
     * path=[]
     *
     * used=[F,F,F]
     *
     * --------------------------------
     *
     * i=0
     *
     * choose first 1
     *
     * path=[1]
     *
     * used=[T,F,F]
     *
     * --------------------------------
     *
     * second recursive level
     *
     * second 1 is now allowed because
     *
     * used[0]==true
     *
     * path=[1,1]
     *
     * used=[T,T,F]
     *
     * choose 2
     *
     * [1,1,2]
     *
     * completed
     *
     * --------------------------------
     *
     * backtrack
     *
     * path=[1]
     *
     * choose 2
     *
     * path=[1,2]
     *
     * choose second 1
     *
     * [1,2,1]
     *
     * completed
     *
     * --------------------------------
     *
     * return to root
     *
     * i=1
     *
     * nums[1]==nums[0]
     *
     * used[0]==false
     *
     * skip
     *
     * entire mirror subtree removed
     *
     * --------------------------------
     *
     * choose 2
     *
     * later obtain
     *
     * [2,1,1]
     *
     * Final Answer
     *
     * [1,1,2]
     * [1,2,1]
     * [2,1,1]
     */

    static class PermutationsII {

        public List<List<Integer>> permuteUnique(int[] nums) {

            Arrays.sort(nums);

            List<List<Integer>> answer = new ArrayList<>();

            boolean[] used = new boolean[nums.length];

            dfs(nums, used, new ArrayList<>(), answer);

            return answer;
        }

        private void dfs(
                int[] nums,
                boolean[] used,
                List<Integer> path,
                List<List<Integer>> answer
        ) {

            if (path.size() == nums.length) {

                answer.add(new ArrayList<>(path));

                return;
            }

            for (int i = 0; i < nums.length; i++) {

                // 🟢 Index already consumed.
                if (used[i]) {
                    continue;
                }

                // 🟢 Break symmetry between equal values.
                if (
                        i > 0 &&
                                nums[i] == nums[i - 1] &&
                                !used[i - 1]
                ) {
                    continue;
                }

                used[i] = true;

                path.add(nums[i]);

                dfs(nums, used, path, answer);

                path.remove(path.size() - 1);

                used[i] = false;
            }
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Explain The Invariant
 * ---------------------
 *
 * My recursion fixes one position of the
 * permutation at every recursive level.
 *
 * The current path is always a valid prefix.
 *
 * The boolean array tells me exactly which
 * indices have already been consumed.
 *
 * ------------------------------------------------------------
 * Explain The Search Space
 * ------------------------------------------------------------
 *
 * Every node represents one partial
 * permutation.
 *
 * Every edge represents choosing one
 * unused element.
 *
 * Every leaf represents one completed
 * permutation.
 *
 * ------------------------------------------------------------
 * Correctness
 * ------------------------------------------------------------
 *
 * Since every unused element is explored,
 * no permutation is missed.
 *
 * Since every index can only be chosen once,
 * no invalid permutation is produced.
 *
 * Therefore every valid permutation appears
 * exactly once.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * Every recursive call increases
 * path.size() by one.
 *
 * Maximum depth equals n.
 *
 * Eventually every branch reaches
 * path.size()==n.
 *
 * ------------------------------------------------------------
 * In-place Feasibility
 * ------------------------------------------------------------
 *
 * Yes.
 *
 * A swap-based permutation algorithm
 * generates permutations in-place.
 *
 * However its invariant differs from
 * the used[] approach.
 *
 * ------------------------------------------------------------
 * Streaming Feasibility
 * ------------------------------------------------------------
 *
 * Yes.
 *
 * Instead of storing permutations,
 * each completed path could be emitted
 * immediately to a consumer.
 *
 * Memory becomes proportional only to
 * recursion depth.
 *
 * ------------------------------------------------------------
 * When NOT To Use
 * ------------------------------------------------------------
 *
 * When only one optimal answer is needed.
 *
 * When exhaustive enumeration is impossible
 * because n is large.
 */

/*
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 * Generate every ordering.
 *
 * Pattern
 * -------
 * DFS Backtracking.
 *
 * Invariant
 * ---------
 * path is always a valid permutation prefix.
 *
 * Search Space
 * ------------
 * Every unused index.
 *
 * State
 * -----
 * path + used[].
 *
 * Transition
 * ----------
 * choose
 * recurse
 * undo
 *
 * Discard Rule
 * ------------
 * Skip used indices.
 *
 * Duplicate Variant
 * -----------------
 * Skip later duplicate while earlier
 * duplicate remains unused.
 *
 * Common Trap
 * -----------
 * Forgetting to copy path before storing.
 *
 * Edge Cases
 * ----------
 * n=1
 * n=6
 * duplicates
 * negative values
 *
 * One-liner
 * ---------
 * Every recursion level fixes exactly one
 * more position.
 *
 * Re-derivation Cue
 * -----------------
 * Fill seats from left to right.
 */    /*
     * ============================================================
     * 🔄 VARIATIONS & TWEAKS
     * ============================================================
     *
     * ------------------------------------------------------------
     * Variation 1
     * ------------------------------------------------------------
     *
     * Permutations II
     *
     * Change:
     * Input contains duplicates.
     *
     * Additional Invariant:
     * Equal values are consumed in index order.
     *
     * Extra Rule:
     *
     * if (i > 0 &&
     *     nums[i] == nums[i - 1] &&
     *     !used[i - 1])
     *     continue;
     *
     * ------------------------------------------------------------
     * Variation 2
     * ------------------------------------------------------------
     *
     * Heap's Algorithm
     *
     * Uses swapping instead of used[].
     *
     * Pattern changes.
     *
     * Invariant changes.
     *
     * Good for in-place generation,
     * but less reusable for interview
     * backtracking problems.
     *
     * ------------------------------------------------------------
     * Variation 3
     * ------------------------------------------------------------
     *
     * Swap-Based DFS
     *
     * Fix one array position.
     *
     * Swap current position with every
     * remaining position.
     *
     * Recurse.
     *
     * Swap back.
     *
     * Same complexity.
     *
     * Different state representation.
     *
     * ------------------------------------------------------------
     * Variation 4
     * ------------------------------------------------------------
     *
     * Streaming Output
     *
     * Instead of storing:
     *
     * answer.add(...)
     *
     * invoke a callback,
     * print,
     * or send to a consumer.
     *
     * Useful when n! answers are too
     * expensive to retain simultaneously.
     *
     * ------------------------------------------------------------
     * Pattern Boundary
     * ------------------------------------------------------------
     *
     * Use Backtracking when:
     *
     * • every valid arrangement is required
     * • decisions depend on previous choices
     * • exhaustive search is acceptable
     *
     * Do not use Backtracking when:
     *
     * • greedy provides an optimal answer
     * • dynamic programming exploits overlapping subproblems
     * • binary search exploits monotonicity
     */

    /*
     * ============================================================
     * 🧠 MASTERY CHECKLIST
     * ============================================================
     *
     * □ I know the Pattern.
     *
     * □ I know the State.
     *
     * □ I know the Invariant.
     *
     * □ I know why path must be copied.
     *
     * □ I know why choose must happen before recursion.
     *
     * □ I know why undo restores the parent state.
     *
     * □ I know why used[] is O(1).
     *
     * □ I can derive the recursion from the invariant.
     *
     * □ I can explain O(n × n!).
     *
     * □ I understand why recursion depth is n,
     *   not n!.
     *
     * □ I can explain duplicate pruning.
     *
     * □ I understand the meaning of:
     *
     *      !used[i - 1]
     *
     * □ I can debug missing permutations by checking:
     *
     *      choose
     *      recurse
     *      undo
     *
     * □ I know when the pattern stops being appropriate.
     */

    /*
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */

    public static void main(String[] args) {

        Optimal optimal = new Optimal();

        // Representative example from the problem.
        List<List<Integer>> result123 = optimal.permute(new int[]{1, 2, 3});
        assert result123.size() == 6 : "3! permutations expected.";

        // Smallest non-empty input.
        List<List<Integer>> result1 = optimal.permute(new int[]{1});
        assert result1.size() == 1 : "Single element has one permutation.";
        assert result1.get(0).equals(List.of(1));

        // Two elements.
        List<List<Integer>> result01 = optimal.permute(new int[]{0, 1});
        assert result01.size() == 2 : "2! permutations expected.";

        // Ensure snapshotting works.
        result123.get(0).set(0, 99);
        assert !result123.get(1).contains(99)
                : "Each answer must be an independent copy.";

        // Duplicate variant.
        PermutationsII unique = new PermutationsII();

        List<List<Integer>> duplicateResult =
                unique.permuteUnique(new int[]{1, 1, 2});

        assert duplicateResult.size() == 3
                : "Unique permutations should be exactly three.";

        assert duplicateResult.contains(List.of(1, 1, 2));
        assert duplicateResult.contains(List.of(1, 2, 1));
        assert duplicateResult.contains(List.of(2, 1, 1));

        // No duplicates should behave exactly like the original problem.
        List<List<Integer>> unique123 =
                unique.permuteUnique(new int[]{1, 2, 3});

        assert unique123.size() == 6;

        // Negative numbers.
        List<List<Integer>> negatives =
                optimal.permute(new int[]{-1, 5});

        assert negatives.size() == 2;

        // Boundary: n = 6
        List<List<Integer>> six =
                optimal.permute(new int[]{1, 2, 3, 4, 5, 6});

        assert six.size() == 720
                : "6! = 720 permutations.";

        // Verify no duplicate permutations for distinct input.
        assert result123.stream().distinct().count() == result123.size();

        System.out.println("All assertions passed.");
    }

}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/
