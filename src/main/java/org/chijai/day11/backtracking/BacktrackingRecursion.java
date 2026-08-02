package org.chijai.day11.backtracking;

import java.util.*;

/**
 * ============================================================================
 * RecursionBackTracking
 * ============================================================================
 *
 * 📘 PRIMARY PROBLEM
 * ----------------------------------------------------------------------------
 * Title:
 *      Recursion / Backtracking Pattern Master Chapter
 *
 * Difficulty:
 *      Foundation Pattern
 *
 * Tags:
 *      Recursion
 *      DFS
 *      Backtracking
 *      Combinatorial Search
 *      State Space Tree
 *      Permutations
 *      Combinations
 *      Subsets
 *      Sudoku
 *      N-Queens
 *
 * Official Reference:
 * https://leetcode.com/problems/permutations/
 *
 * Pattern Reference:
 * https://leetcode.com/problems/permutations/discuss/18239/A-general-approach-to-backtracking-questions-in-Java-(Subsets-Permutations-Combination-Sum-Palindrome-Partioning)
 *
 * ----------------------------------------------------------------------------
 *
 * Problem Description
 *
 * Backtracking is the fundamental algorithm used to solve combinatorial
 * search problems.
 *
 * Examples include:
 *
 *  • Generate all subsets
 *  • Generate all permutations
 *  • Combination Sum
 *  • Letter Combinations
 *  • Palindrome Partitioning
 *  • Restore IP Addresses
 *  • Sudoku Solver
 *  • N Queens
 *  • Word Search
 *
 * Instead of traversing an existing tree, we dynamically build the tree
 * while searching.
 *
 * Every recursive call represents one state.
 *
 * Every decision creates a child state.
 *
 * Every completed solution reaches a leaf.
 *
 * Every return undoes one decision.
 *
 * Therefore:
 *
 *      Backtracking == DFS over an implicit state-space tree.
 *
 * ----------------------------------------------------------------------------
 *
 * State Space Tree
 *
 * Example:
 *
 * nums = [1,2,3]
 *
 *                              []
 *                   /           |          \
 *                 1             2            3
 *              /    \         /   \       /    \
 *           2         3      1     3    1      2
 *          /          |      |     |    |      |
 *      [1,2,3]    [1,3,2] ...
 *
 * The tree does not exist beforehand.
 *
 * We create every child during DFS.
 *
 * ----------------------------------------------------------------------------
 *
 * Goal
 *
 * Learn ONE invariant-driven framework capable of reconstructing nearly every
 * interview backtracking solution.
 *
 * ============================================================================
 *
 * 🔵 CORE PATTERN OVERVIEW
 * ----------------------------------------------------------------------------
 *
 * Pattern
 *
 *      Choose
 *          ↓
 *      Explore
 *          ↓
 *      Unchoose
 *
 * This three-step cycle is the entire heart of backtracking.
 *
 * ----------------------------------------------------------------------------
 *
 * Archetype
 *
 * DFS over an implicit tree.
 *
 * Unlike graph/tree DFS,
 * there are no predefined edges.
 *
 * We generate legal moves while exploring.
 *
 * ----------------------------------------------------------------------------
 *
 * Core Invariant
 *
 * Before entering recursion:
 *
 *      path exactly represents decisions already made.
 *
 * During recursion:
 *
 *      every recursive level owns exactly one decision.
 *
 * After recursion returns:
 *
 *      path is restored to the identical state it had before this choice.
 *
 * Restoration is the invariant that makes siblings independent.
 *
 * ----------------------------------------------------------------------------
 *
 * Why It Works
 *
 * Every solution corresponds to exactly one root-to-leaf path.
 *
 * DFS eventually visits every leaf.
 *
 * Because state is restored after each branch,
 * branches never interfere.
 *
 * ----------------------------------------------------------------------------
 *
 * Recognition Signals
 *
 * Immediately think Backtracking when the problem asks:
 *
 *      Generate all ...
 *      Return every ...
 *      Enumerate ...
 *      Find every configuration ...
 *      List every ordering ...
 *      Explore all possibilities ...
 *
 * especially when constraints are relatively small.
 *
 * ----------------------------------------------------------------------------
 *
 * Common Problems
 *
 * ✔ Subsets
 * ✔ Permutations
 * ✔ Combination Sum
 * ✔ Combination Sum II
 * ✔ Letter Combinations
 * ✔ Restore IP Addresses
 * ✔ Palindrome Partitioning
 * ✔ Word Search
 * ✔ Sudoku Solver
 * ✔ N Queens
 *
 * ----------------------------------------------------------------------------
 *
 * When NOT To Use
 *
 * If only one optimal answer is required:
 *
 *      Dynamic Programming
 *
 * If shortest path is required:
 *
 *      BFS
 *
 * If monotonic ordering exists:
 *
 *      Binary Search
 *
 * If greedy proof exists:
 *
 *      Greedy
 *
 * ----------------------------------------------------------------------------
 *
 * Comparison
 *
 * Tree DFS
 * --------
 * Tree already exists.
 *
 * Backtracking
 * ------------
 * Tree is created while exploring.
 *
 * Graph DFS
 * ---------
 * Neighbors come from adjacency list.
 *
 * Backtracking
 * ------------
 * Neighbors come from candidate generation logic.
 *
 * ============================================================================
 *
 * 🟢 MENTAL MODEL & INVARIANTS
 * ----------------------------------------------------------------------------
 *
 * Think of yourself standing at one node inside an enormous decision tree.
 *
 * The current recursion frame represents exactly one node.
 *
 * Your responsibilities are only:
 *
 *      1. determine whether this node is a solution
 *
 *      2. generate legal children
 *
 *      3. recursively explore children
 *
 *      4. restore state before leaving
 *
 * Never think globally.
 *
 * Every recursive frame solves only one tiny problem.
 *
 * ----------------------------------------------------------------------------
 *
 * Fundamental State
 *
 * path
 *
 *      decisions already taken
 *
 * level
 *
 *      current depth in tree
 *
 * choices
 *
 *      possible outgoing edges
 *
 * answer
 *
 *      collection of leaf states
 *
 * ----------------------------------------------------------------------------
 *
 * Master Invariant
 *
 * At recursion entry:
 *
 *      path is valid.
 *
 *      path contains no illegal decisions.
 *
 *      every earlier level has already fixed its decision.
 *
 *      future levels have made no decisions.
 *
 * ----------------------------------------------------------------------------
 *
 * Choose Step
 *
 * Choose one legal candidate.
 *
 * Extend path.
 *
 * Nothing else changes.
 *
 * ----------------------------------------------------------------------------
 *
 * Explore Step
 *
 * Recursive call explores every solution beginning with the current path.
 *
 * Parent does not worry about grandchildren.
 *
 * ----------------------------------------------------------------------------
 *
 * Unchoose Step
 *
 * Remove exactly what was added.
 *
 * Restore every modified variable.
 *
 * The parent state becomes identical to its original state.
 *
 * If restoration is incomplete,
 * sibling branches become corrupted.
 *
 * ----------------------------------------------------------------------------
 *
 * Variable Meanings
 *
 * startIndex
 *
 *      current recursion level
 *
 * path
 *
 *      partial solution
 *
 * answer
 *
 *      completed solutions
 *
 * used[]
 *
 *      whether an element already belongs to current permutation
 *
 * ----------------------------------------------------------------------------
 *
 * Allowed Moves
 *
 * ✔ choose legal candidate
 *
 * ✔ recurse
 *
 * ✔ undo exactly one decision
 *
 * ✔ continue loop
 *
 * ----------------------------------------------------------------------------
 *
 * Forbidden Moves
 *
 * ✘ forget to undo
 *
 * ✘ modify shared state permanently
 *
 * ✘ add incomplete path to answer
 *
 * ✘ skip legal children accidentally
 *
 * ✘ revisit forbidden states
 *
 * ----------------------------------------------------------------------------
 *
 * Termination
 *
 * Recursion stops only when the current state already represents a complete
 * solution or cannot produce one.
 *
 * Two common stopping conditions:
 *
 *      complete solution
 *
 *      impossible continuation (pruning)
 *
 * ----------------------------------------------------------------------------
 *
 * Why Naive Solutions Fail
 *
 * Nested loops cannot adapt to unknown depth.
 *
 * Example:
 *
 * permutations of n numbers
 *
 * require
 *
 * n nested loops.
 *
 * Since n is input-dependent,
 * recursion naturally replaces arbitrarily deep looping.
 *
 * ============================================================================
 *
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * ----------------------------------------------------------------------------
 *
 * Mistake 1
 *
 * Forgetting to unchoose.
 *
 * Why it looks correct:
 *
 * Every recursive call individually appears correct.
 *
 * Broken invariant:
 *
 * Parent state is no longer restored.
 *
 * Counterexample:
 *
 * nums = [1,2]
 *
 * After exploring
 *
 *      [1]
 *
 * forgetting removal means sibling starts with
 *
 *      [1]
 *
 * instead of
 *
 *      []
 *
 * Entire search becomes invalid.
 *
 * ----------------------------------------------------------------------------
 *
 * Mistake 2
 *
 * Adding path directly to answer.
 *
 * Instead of
 *
 *      new ArrayList<>(path)
 *
 * storing
 *
 *      path
 *
 * stores one mutable object.
 *
 * Every answer eventually becomes identical.
 *
 * Broken invariant:
 *
 * Leaves must freeze state permanently.
 *
 * ----------------------------------------------------------------------------
 *
 * Mistake 3
 *
 * Incorrect leaf condition.
 *
 * Example:
 *
 * permutation leaf should be
 *
 *      path.size()==nums.length
 *
 * not
 *
 *      index==nums.length
 *
 * unless index actually represents chosen count.
 *
 * Wrong termination either misses leaves
 * or produces incomplete solutions.
 *
 * ----------------------------------------------------------------------------
 *
 * Mistake 4
 *
 * Incorrect candidate generation.
 *
 * Child generation defines the search space.
 *
 * One missing candidate silently removes solutions.
 *
 * One extra candidate duplicates solutions.
 *
 * Candidate generation is therefore the most important problem-specific logic.
 *
 * ----------------------------------------------------------------------------
 *
 * Interview Trap
 *
 * Candidates often memorize recursion.
 *
 * Interviewers instead ask:
 *
 *      "What exactly is your invariant?"
 *
 * If you cannot answer
 *
 *      "path is always a valid partial solution"
 *
 * debugging becomes almost impossible.
 *
 * ============================================================================
 *
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ----------------------------------------------------------------------------
 *
 * Mechanical typing order
 *
 * Step 1
 *
 *      create answer
 *
 * Step 2
 *
 *      create path
 *
 * Step 3
 *
 *      call dfs(...)
 *
 * Step 4
 *
 *      inside dfs:
 *
 *          leaf?
 *
 * Step 5
 *
 *      generate candidates
 *
 * Step 6
 *
 *      choose
 *
 * Step 7
 *
 *      recurse
 *
 * Step 8
 *
 *      unchoose
 *
 * Step 9
 *
 *      return answer
 *
 * ----------------------------------------------------------------------------
 *
 * Generic Skeleton
 *
 * dfs(state):
 *
 *      if leaf:
 *          record
 *          return
 *
 *      for each candidate:
 *
 *          choose
 *
 *          dfs(next)
 *
 *          unchoose
 *
 * ============================================================================
 *
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ----------------------------------------------------------------------------
 *
 * dfs(state)
 *
 * if leaf
 *      save
 *
 * for candidate
 *      choose
 *      dfs
 *      unchoose
 *
 * ============================================================================
 */
public class BacktrackingRecursion {

    /**
     * =========================================================================
     * 6. SOLUTION CLASSES
     * =========================================================================
     */

    /**
     * -------------------------------------------------------------------------
     * Brute Force
     * -------------------------------------------------------------------------
     *
     * Idea
     *
     * Generate every possible sequence without carefully maintaining
     * incremental state.
     *
     * Usually requires repeatedly copying structures or testing invalid
     * configurations afterwards.
     *
     * Invariant
     *
     * No strong incremental invariant.
     *
     * Limitation
     *
     * Performs unnecessary work because invalid states are explored before
     * rejection.
     *
     * Interview Usefulness
     *
     * Good only for explaining progression toward proper backtracking.
     */
    static class BruteForceTemplate {

        public <T> List<List<T>> conceptualOnly(List<T> items) {
            return new ArrayList<>();
        }
    }

    /**
     * -------------------------------------------------------------------------
     * Improved
     * -------------------------------------------------------------------------
     *
     * Idea
     *
     * Build the solution incrementally while rejecting illegal states
     * immediately.
     *
     * Invariant
     *
     * path is always valid.
     *
     * Improvement
     *
     * Prunes impossible branches early.
     *
     * Complexity
     *
     * Problem dependent.
     *
     * Interview Usefulness
     *
     * Represents the transition from exhaustive generation to disciplined
     * search.
     */
    static class ImprovedTemplate {


        protected <T> void dfs(
                int level,
                List<T> path,
                List<List<T>> answer) {

            // Problem-specific leaf condition.

            // Problem-specific candidate generation.

            // Choose.

            // Explore.

            // Unchoose.
        }
    }

    /**
     * -------------------------------------------------------------------------
     * Optimal (Interview Preferred)
     * -------------------------------------------------------------------------
     *
     * Idea
     *
     * Every recursive frame owns exactly one level of the state-space tree.
     *
     * That frame performs only four responsibilities:
     *
     *      1. Check whether this state is a leaf.
     *      2. Generate legal candidates.
     *      3. Explore each candidate.
     *      4. Restore the state.
     *
     * Everything else is problem-specific.
     *
     * -------------------------------------------------------------------------
     *
     * 🟢 Master Invariant
     *
     * Before dfs() begins:
     *
     *      path completely represents the current state.
     *
     * Before every recursive call:
     *
     *      path remains valid.
     *
     * After every recursive return:
     *
     *      path is restored exactly.
     *
     * -------------------------------------------------------------------------
     *
     * Correctness
     *
     * Every root-to-leaf path corresponds to exactly one solution.
     *
     * DFS visits every reachable leaf exactly once (assuming duplicate handling
     * is implemented correctly for the specific problem).
     *
     * Because restoration is perfect, sibling branches remain independent.
     *
     * -------------------------------------------------------------------------
     *
     * Complexity
     *
     * Depends entirely on the branching factor and tree depth.
     *
     * Typical examples:
     *
     *      Subsets:
     *          O(2^n)
     *
     *      Permutations:
     *          O(n!)
     *
     *      Combination Sum:
     *          Exponential
     *
     * -------------------------------------------------------------------------
     *
     * Interview Usefulness
     *
     * This template reconstructs nearly every interview backtracking problem.
     */
    static class OptimalTemplate {

        /**
         * Generic entry.
         */
        public <T> List<List<T>> solve(List<T> input) {

            List<List<T>> answer = new ArrayList<>();

            List<T> path = new ArrayList<>();

            dfs(
                    0,
                    input,
                    path,
                    answer
            );

            return answer;
        }

        /**
         * Generic framework.
         *
         * Only the three marked locations change from one interview problem
         * to another.
         */
        protected <T> void dfs(
                int startIndex,
                List<T> input,
                List<T> path,
                List<List<T>> answer) {

            // 🟢 Invariant:
            // path is a valid partial solution.

            if (isLeaf(startIndex, input, path)) {

                // Freeze the current state.
                answer.add(new ArrayList<>(path));

                return;
            }

            for (T candidate : getCandidates(startIndex, input, path)) {

                // Choose.
                path.add(candidate);

                // Explore every solution beginning with this state.
                dfs(
                        nextLevel(startIndex),
                        input,
                        path,
                        answer
                );

                // Restore parent state exactly.
                path.remove(path.size() - 1);
            }
        }

        /**
         * Placeholder hooks showing which logic changes.
         */

        protected <T> boolean isLeaf(
                int startIndex,
                List<T> input,
                List<T> path) {

            return false;
        }

        protected int nextLevel(int level) {
            return level + 1;
        }

        protected <T> List<T> getCandidates(
                int startIndex,
                List<T> input,
                List<T> path) {

            return Collections.emptyList();
        }
    }

    /**
     * =========================================================================
     * Pattern Specialization #1
     * =========================================================================
     *
     * SUBSETS
     *
     * State Space
     *
     * Every level decides:
     *
     *      include?
     *
     * or
     *
     *      skip?
     *
     * Search Tree
     *
     *                      []
     *                  /         \
     *                take       skip
     *               /              \
     *             ...
     *
     * Important Invariant
     *
     * startIndex guarantees every element is considered only once.
     *
     * Therefore:
     *
     *      no duplicates
     *
     * and
     *
     *      order is preserved.
     */
    static class Subsets {

        public List<List<Integer>> subsets(int[] nums) {

            List<List<Integer>> answer = new ArrayList<>();

            List<Integer> path = new ArrayList<>();

            dfs(
                    0,
                    nums,
                    path,
                    answer
            );

            return answer;
        }

        private void dfs(
                int startIndex,
                int[] nums,
                List<Integer> path,
                List<List<Integer>> answer) {

            // 🟢 Every node represents one valid subset.
            answer.add(new ArrayList<>(path));

            for (int i = startIndex; i < nums.length; i++) {

                // Choose.
                path.add(nums[i]);

                dfs(
                        i + 1,
                        nums,
                        path,
                        answer
                );

                // Restore exactly one decision.
                path.remove(path.size() - 1);
            }
        }
    }

    /**
     * =========================================================================
     * Pattern Specialization #2
     * =========================================================================
     *
     * PERMUTATIONS
     *
     * Difference from subsets:
     *
     * Every level may choose from every unused element.
     *
     * Therefore
     *
     * startIndex is NOT sufficient.
     *
     * We need
     *
     *      used[]
     *
     * to preserve the invariant.
     *
     * Master Invariant
     *
     * used[i]
     *
     * means
     *
     * nums[i]
     *
     * already belongs to the current permutation.
     */
    static class Permutations {

        public List<List<Integer>> permute(int[] nums) {

            List<List<Integer>> answer = new ArrayList<>();

            List<Integer> path = new ArrayList<>();

            boolean[] used = new boolean[nums.length];

            dfs(
                    nums,
                    used,
                    path,
                    answer
            );

            return answer;
        }

        private void dfs(
                int[] nums,
                boolean[] used,
                List<Integer> path,
                List<List<Integer>> answer) {

            if (path.size() == nums.length) {

                // 🟢 Every position has been chosen.
                answer.add(new ArrayList<>(path));

                return;
            }

            for (int i = 0; i < nums.length; i++) {

                if (used[i]) {
                    continue;
                }

                // Choose.
                used[i] = true;

                path.add(nums[i]);

                dfs(
                        nums,
                        used,
                        path,
                        answer
                );

                // Restore parent state.
                path.remove(path.size() - 1);

                used[i] = false;
            }
        }
    }

    /**
     * =========================================================================
     * Pattern Specialization #3
     * =========================================================================
     *
     * Combination Sum
     *
     * Observation
     *
     * Candidate reuse is allowed.
     *
     * Therefore,
     *
     * recursive call remains at
     *
     *      i
     *
     * instead of
     *
     *      i + 1.
     */
    static class CombinationSum {        public List<List<Integer>> combinationSum(
            int[] candidates,
            int target) {

        List<List<Integer>> answer = new ArrayList<>();

        List<Integer> path = new ArrayList<>();

        dfs(
                0,
                target,
                candidates,
                path,
                answer
        );

        return answer;
    }

        private void dfs(
                int startIndex,
                int remaining,
                int[] candidates,
                List<Integer> path,
                List<List<Integer>> answer) {

            // 🟢 Invariant:
            // remaining is the sum still required to complete this path.

            if (remaining == 0) {

                answer.add(new ArrayList<>(path));

                return;
            }

            // 🔴 Prune impossible branches.
            if (remaining < 0) {
                return;
            }

            for (int i = startIndex; i < candidates.length; i++) {

                // Choose.
                path.add(candidates[i]);

                // Candidate may be reused, so stay at i.
                dfs(
                        i,
                        remaining - candidates[i],
                        candidates,
                        path,
                        answer
                );

                // Restore parent state.
                path.remove(path.size() - 1);
            }
        }
    }

    /**
     * =========================================================================
     * Pattern Specialization #4
     * =========================================================================
     *
     * Combination Sum II
     *
     * Difference
     *
     * Every candidate may be used only once.
     *
     * Duplicate values exist.
     *
     * Additional Invariant
     *
     * At the same recursion level,
     * identical values should only be explored once.
     *
     * Sorting enables duplicate pruning.
     */
    static class CombinationSumII {

        public List<List<Integer>> combinationSum2(
                int[] candidates,
                int target) {

            Arrays.sort(candidates);

            List<List<Integer>> answer = new ArrayList<>();

            dfs(
                    0,
                    target,
                    candidates,
                    new ArrayList<>(),
                    answer
            );

            return answer;
        }

        private void dfs(
                int startIndex,
                int remaining,
                int[] nums,
                List<Integer> path,
                List<List<Integer>> answer) {

            if (remaining == 0) {

                answer.add(new ArrayList<>(path));

                return;
            }

            for (int i = startIndex; i < nums.length; i++) {

                // 🔴 Skip duplicate siblings.
                if (i > startIndex && nums[i] == nums[i - 1]) {
                    continue;
                }

                if (nums[i] > remaining) {
                    break;
                }

                path.add(nums[i]);

                dfs(
                        i + 1,
                        remaining - nums[i],
                        nums,
                        path,
                        answer
                );

                path.remove(path.size() - 1);
            }
        }
    }

    /**
     * =========================================================================
     * Pattern Specialization #5
     * =========================================================================
     *
     * Palindrome Partitioning
     *
     * State
     *
     * Current starting index inside the string.
     *
     * Edge Generation
     *
     * Every palindrome beginning at startIndex.
     *
     * Child State
     *
     * End + 1
     */
    static class PalindromePartitioning {

        public List<List<String>> partition(String s) {

            List<List<String>> answer = new ArrayList<>();

            dfs(
                    0,
                    s,
                    new ArrayList<>(),
                    answer
            );

            return answer;
        }

        private void dfs(
                int startIndex,
                String s,
                List<String> path,
                List<List<String>> answer) {

            if (startIndex == s.length()) {

                answer.add(new ArrayList<>(path));

                return;
            }

            for (int end = startIndex; end < s.length(); end++) {

                if (!isPalindrome(s, startIndex, end)) {
                    continue;
                }

                path.add(s.substring(startIndex, end + 1));

                dfs(
                        end + 1,
                        s,
                        path,
                        answer
                );

                path.remove(path.size() - 1);
            }
        }

        private boolean isPalindrome(
                String s,
                int left,
                int right) {

            while (left < right) {

                if (s.charAt(left) != s.charAt(right)) {
                    return false;
                }

                left++;
                right--;
            }

            return true;
        }
    }

    /**
     * =========================================================================
     * Pattern Specialization #6
     * =========================================================================
     *
     * N Queens
     *
     * State
     *
     * Current row.
     *
     * Candidate
     *
     * Every safe column.
     *
     * Invariant
     *
     * Rows above are already valid.
     *
     * We never modify earlier rows except during restoration.
     */
    static class NQueens {

        public List<List<String>> solveNQueens(int n) {

            List<List<String>> answer = new ArrayList<>();

            char[][] board = new char[n][n];

            for (char[] row : board) {
                Arrays.fill(row, '.');
            }

            dfs(
                    0,
                    board,
                    answer
            );

            return answer;
        }

        private void dfs(
                int row,
                char[][] board,
                List<List<String>> answer) {

            if (row == board.length) {

                answer.add(build(board));

                return;
            }

            for (int col = 0; col < board.length; col++) {

                if (!safe(board, row, col)) {
                    continue;
                }

                // Choose.
                board[row][col] = 'Q';

                dfs(
                        row + 1,
                        board,
                        answer
                );

                // Restore.
                board[row][col] = '.';
            }
        }

        private boolean safe(
                char[][] board,
                int row,
                int col) {

            for (int r = 0; r < row; r++) {

                if (board[r][col] == 'Q') {
                    return false;
                }
            }

            for (
                    int r = row - 1, c = col - 1;
                    r >= 0 && c >= 0;
                    r--, c--
            ) {

                if (board[r][c] == 'Q') {
                    return false;
                }
            }

            for (
                    int r = row - 1, c = col + 1;
                    r >= 0 && c < board.length;
                    r--, c++
            ) {

                if (board[r][c] == 'Q') {
                    return false;
                }
            }

            return true;
        }

        private List<String> build(char[][] board) {

            List<String> configuration = new ArrayList<>();

            for (char[] row : board) {
                configuration.add(new String(row));
            }

            return configuration;
        }
    }

/**
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * If asked,
 *
 * "Explain your backtracking solution."
 *
 * A strong answer:
 *
 * Backtracking is DFS over an implicit state-space tree.
 *
 * Every recursion frame represents one state.
 *
 * My invariant is that the current path always represents a valid partial
 * solution.
 *
 * At every level I generate all legal candidates, choose one candidate,
 * recursively explore every solution reachable from that state, and then
 * undo exactly that choice before exploring the next sibling.
 *
 * Restoration guarantees sibling independence.
 *
 * Termination occurs when the current state is already a complete solution
 * or when pruning proves no solution can exist below this node.
 *
 * Backtracking is appropriate whenever the search space forms an implicit
 * decision tree and every solution must be explored.
 *
 * It is usually inappropriate when only an optimal value is required and
 * Dynamic Programming or Greedy methods have a correctness proof.
 *
 * In-place feasibility
 *
 * Often yes.
 *
 * Examples:
 *
 *      board mutation
 *      swap permutation
 *      visited array
 *
 * Streaming feasibility
 *
 * Yes.
 *
 * Instead of storing answers,
 * each leaf may immediately be emitted to a callback.
 */    /**
     *      * =========================================================================
     *      * 🎯 INTERVIEW RECALL SHEET
     *      * =========================================================================
     *      *
     *      * Trigger
     *      * -------
     *      * Generate all...
     *      * Enumerate...
     *      * Return every...
     *      * Explore all possibilities...
     *      *
     *      * Pattern
     *      * -------
     *      * DFS on an implicit state-space tree.
     *      *
     *      * Search Space
     *      * ------------
     *      * Every root-to-leaf path is one candidate solution.
     *      *
     *      * State
     *      * -----
     *      * path
     *      *
     *      * Invariant
     *      * ---------
     *      * path is always a valid partial solution.
     *      *
     *      * Search Target
     *      * -------------
     *      * Reach every legal leaf exactly once.
     *      *
     *      * Discard Rule
     *      * ------------
     *      * Stop exploring immediately whenever:
     *      *
     *      *      state becomes invalid
     *      *
     *      * or
     *      *
     *      *      pruning proves no solution exists below.
     *      *
     *      * Termination
     *      * -----------
     *      * Current state already represents a complete solution.
     *      *
     *      * Common Trap
     *      * -----------
     *      * Forgetting to restore state after recursion.
     *      *
     *      * Edge Cases
     *      * ----------
     *      * Empty input.
     *      * Duplicate values.
     *      * Candidate reuse.
     *      * Impossible target.
     *      * Single element.
     *      *
     *      * One-Liner
     *      * ---------
     *      * Choose → Explore → Unchoose.
     *      *
     *      * Re-derivation Cue
     *      * -----------------
     *      * Ask only four questions:
     *      *
     *      *      What is my state?
     *      *      When am I finished?
     *      *      What are my legal choices?
     *      *      What must be restored?
     *      */
 /**
  *      *=========================================================================
  *      * 🔄 VARIATIONS & TWEAKS
  *      * =========================================================================
  *      *
  *      * -------------------------------------------------------------------------
  *      * Variation
  *      * -------------------------------------------------------------------------
  *      * Subsets
  *      *
  *      * State
  *      *      startIndex
  *      *
  *      * Candidate
  *      *      every remaining element
  *      *
  *      * Restoration
  *      *      remove last
  *      *
  *      * -------------------------------------------------------------------------
  *      * Variation
  *      * -------------------------------------------------------------------------
  *      * Permutations
  *      *
  *      * State
  *      *      path + used[]
  *      *
  *      * Candidate
  *      *      every unused element
  *      *
  *      * Extra Invariant
  *      *      each element appears once
  *      *
  *      * -------------------------------------------------------------------------
  *      * Variation
  *      * -------------------------------------------------------------------------
  *      * Combination Sum
  *      *
  *      * State
  *      *      remaining target
  *      *
  *      * Candidate
  *      *      current and later elements
  *      *
  *      * Difference
  *      *      reuse allowed
  *      *
  *      * -------------------------------------------------------------------------
  *      * Variation
  *      * -------------------------------------------------------------------------
  *      * Combination Sum II
  *      *
  *      * Difference
  *      *      reuse forbidden
  *      *
  *      * Extra Invariant
  *      *      duplicate siblings skipped
  *      *
  *      * -------------------------------------------------------------------------
  *      * Variation
  *      * -------------------------------------------------------------------------
  *      * Sudoku
  *      *
  *      * State
  *      *      partially filled board
  *      *
  *      * Candidate
  *      *      digits 1-9
  *      *
  *      * Pruning
  *      *      invalid placement immediately rejected
  *      *
  *      * -------------------------------------------------------------------------
  *      * Variation
  *      * -------------------------------------------------------------------------
  *      * N Queens
  *      *
  *      * State
  *      *      rows already filled
  *      *
  *      * Candidate
  *      *      safe columns
  *      *
  *      * Extra Invariant
  *      *      previous rows always valid
  *      *
  *      * -------------------------------------------------------------------------
  *      * Pattern Break
  *      * -------------------------------------------------------------------------
  *      * If no restoration is possible,
  *      * recursion usually becomes incorrect.
  *      *
  *      * -------------------------------------------------------------------------
  *      * Pattern Break
  *      * -------------------------------------------------------------------------
  *      * If future choices depend upon destroyed historical state,
  *      * restoration must rebuild that state completely.
  *      *
  *      * -------------------------------------------------------------------------
  *      * Pattern Break
  *      * -------------------------------------------------------------------------
  *      * If search space is acyclic and shortest path is requested,
  *      * BFS replaces backtracking.
  *      *
  *      * -------------------------------------------------------------------------
  *      * Pattern Break
  *      * -------------------------------------------------------------------------
  *      * If overlapping subproblems dominate,
  *      * Dynamic Programming is usually superior.
  *      */

           /**
  *      * =========================================================================
  *      * 🧠 MASTERY CHECKLIST
  *      * =========================================================================
  *      *
  *      * □ Can I define the state?
  *      *
  *      * □ Can I define the invariant?
  *      *
  *      * □ Can I identify the leaf?
  *      *
  *      * □ Can I generate children?
  *      *
  *      * □ Can I explain why every child is legal?
  *      *
  *      * □ Can I explain the discard rule?
  *      *
  *      * □ Can I explain pruning?
  *      *
  *      * □ Can I restore every modified variable?
  *      *
  *      * □ Can I prove sibling independence?
  *      *
  *      * □ Can I prove termination?
  *      *
  *      * □ Can I explain why naive nested loops fail?
  *      *
  *      * □ Can I explain why recursion replaces unknown-depth loops?
  *      *
  *      * □ Can I derive subsets from the template?
  *      *
  *      * □ Can I derive permutations from the template?
  *      *
  *      * □ Can I derive combination sum from the template?
  *      *
  *      * □ Can I derive palindrome partitioning from the template?
  *      *
  *      * □ Can I derive N Queens from the template?
  *      *
  *      * □ Can I identify when this pattern should NOT be used?
  *      */

             /**
  *      * =========================================================================
  *      * ⚫ Pattern Mapping Table
  *      * =========================================================================
  *      *
  *      * +------------------------+-------------------+--------------------------+
  *      * | Problem                | State             | Key Invariant            |
  *      * +------------------------+-------------------+--------------------------+
  *      * | Subsets                | startIndex        | Order preserved          |
  *      * | Permutations           | used[]            | No repeated element      |
  *      * | Combination Sum        | remaining target  | Remaining >= 0           |
  *      * | Combination Sum II     | sorted + index    | Skip duplicate siblings  |
  *      * | Palindrome Partition   | string index      | Prefix already valid     |
  *      * | Sudoku                 | board             | Board always legal       |
  *      * | N Queens               | current row       | Previous rows valid      |
  *      * +------------------------+-------------------+--------------------------+
  *      */

            /**
  *      * =========================================================================
  *      * Generic Mental Template
  *      * =========================================================================
  *      *
  *      * dfs(state):
  *      *
  *      *      if solution:
  *      *          record
  *      *          return
  *      *
  *      *      for each legal choice:
  *      *
  *      *          choose
  *      *
  *      *          dfs(next state)
  *      *
  *      *          unchoose
  *      *
  *      * Every interview problem merely changes:
  *      *
  *      *      1. state
  *      *      2. leaf condition
  *      *      3. candidate generation
  *      *      4. pruning
  *      *
  *      * Everything else stays identical.
  *      */

              /**
  *      * =========================================================================
  *      * Backtracking Decision Framework
  *      * =========================================================================
  *      *
  *      * Before writing code ask:
  *      *
  *      * Q1.
  *      * What exactly is my state?
  *      *
  *      * Q2.
  *      * What makes a complete solution?
  *      *
  *      * Q3.
  *      * What legal choices exist from this state?
  *      *
  *      * Q4.
  *      * Which variables change after choosing?
  *      *
  *      * Q5.
  *      * How do I restore them?
  *      *
  *      * If all five questions are answered,
  *      * implementation becomes mechanical.
  *      */

             /**
 * * =========================================================================
 * * Debugging Framework
 * * =========================================================================
 * *
 * * If answers are missing:
 * *
 * *      Did I forget to copy path?
 * *
 * *      Did I forget to restore?
 * *
 * *      Is my leaf condition correct?
 * *
 * *      Are candidates generated correctly?
 * *
 * *      Am I pruning too aggressively?
 * *
 * *      Am I skipping duplicate siblings correctly?
 * *
 * *      Is recursion advancing to the correct next state?
 * *
 * * Almost every interview bug belongs to one of these categories.
 *
 */

    /**
     * =========================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * =========================================================================
     */

    public static void main(String[] args) {

        testSubsets();

        testPermutations();

        testCombinationSum();

        testCombinationSumII();

        testPalindromePartitioning();

        testNQueens();

        System.out.println("All backtracking pattern tests passed.");

        System.out.println();

        System.out.println("I understand the invariant.");
        System.out.println();
        System.out.println("I can re-derive the solution.");
        System.out.println();
        System.out.println("I can physically reconstruct the implementation under pressure.");
        System.out.println();
        System.out.println("This chapter is complete.");
    }

    /**
     * -------------------------------------------------------------------------
     * Representative subset generation.
     * -------------------------------------------------------------------------
     */
    private static void testSubsets() {

        Subsets solver = new Subsets();

        List<List<Integer>> answer = solver.subsets(new int[]{1, 2, 3});

        // 2^3 subsets.
        assert answer.size() == 8;

        // Empty subset always exists.
        assert answer.stream().anyMatch(List::isEmpty);

        // Full subset exists.
        assert answer.stream().anyMatch(
                x -> x.equals(Arrays.asList(1, 2, 3))
        );
    }

    /**
     * -------------------------------------------------------------------------
     * Representative permutation generation.
     * -------------------------------------------------------------------------
     */
    private static void testPermutations() {

        Permutations solver = new Permutations();

        List<List<Integer>> answer = solver.permute(new int[]{1, 2, 3});

        // 3! permutations.
        assert answer.size() == 6;

        assert answer.stream().allMatch(
                x -> x.size() == 3
        );
    }

    /**
     * -------------------------------------------------------------------------
     * Candidate reuse.
     * -------------------------------------------------------------------------
     */
    private static void testCombinationSum() {

        CombinationSum solver = new CombinationSum();

        List<List<Integer>> answer =
                solver.combinationSum(
                        new int[]{2, 3, 6, 7},
                        7
                );

        assert answer.size() == 2;

        assert answer.stream().anyMatch(
                x -> x.equals(Collections.singletonList(7))
        );

        assert answer.stream().anyMatch(
                x -> x.equals(Arrays.asList(2, 2, 3))
        );
    }

    /**
     * -------------------------------------------------------------------------
     * Duplicate skipping.
     * -------------------------------------------------------------------------
     */
    private static void testCombinationSumII() {

        CombinationSumII solver = new CombinationSumII();

        List<List<Integer>> answer =
                solver.combinationSum2(
                        new int[]{10, 1, 2, 7, 6, 1, 5},
                        8
                );

        assert answer.size() == 4;

        assert answer.stream().anyMatch(
                x -> x.equals(Arrays.asList(1, 1, 6))
        );

        assert answer.stream().anyMatch(
                x -> x.equals(Arrays.asList(1, 2, 5))
        );

        assert answer.stream().anyMatch(
                x -> x.equals(Arrays.asList(1, 7))
        );

        assert answer.stream().anyMatch(
                x -> x.equals(Arrays.asList(2, 6))
        );
    }

    /**
     * -------------------------------------------------------------------------
     * Representative palindrome partition.
     * -------------------------------------------------------------------------
     */
    private static void testPalindromePartitioning() {

        PalindromePartitioning solver =
                new PalindromePartitioning();

        List<List<String>> answer =
                solver.partition("aab");

        assert answer.size() == 2;

        assert answer.stream().anyMatch(
                x -> x.equals(Arrays.asList("a", "a", "b"))
        );

        assert answer.stream().anyMatch(
                x -> x.equals(Arrays.asList("aa", "b"))
        );
    }

    /**
     * -------------------------------------------------------------------------
     * Smallest classical N-Queens instance.
     * -------------------------------------------------------------------------
     */
    private static void testNQueens() {

        NQueens solver = new NQueens();

        List<List<String>> answer =
                solver.solveNQueens(4);

        // Exactly two solutions for n = 4.
        assert answer.size() == 2;

        for (List<String> board : answer) {

            assert board.size() == 4;

            for (String row : board) {

                assert row.length() == 4;

                long queens =
                        row.chars()
                                .filter(c -> c == 'Q')
                                .count();

                // Exactly one queen per row.
                assert queens == 1;
            }
        }
    }
}