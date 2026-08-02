package org.chijai.day6.trees.session4;

import java.util.*;

/**
 * =============================================================================
 * BinaryTreePathProblems
 * =============================================================================
 *
 * Pattern Chapter:
 *      Binary Tree Path Aggregation
 *
 * Covers the following related LeetCode problems:
 *
 * 112. Path Sum
 * 113. Path Sum II
 * 437. Path Sum III
 * 129. Sum Root to Leaf Numbers
 * 124. Binary Tree Maximum Path Sum
 *
 * ---------------------------------------------------------------------------
 * WHY THESE BELONG TOGETHER
 * ---------------------------------------------------------------------------
 *
 * Every problem asks us to aggregate information while walking along a path
 * inside a binary tree.
 *
 * The aggregation changes:
 *
 *      boolean
 *      count
 *      list of paths
 *      numeric value
 *      maximum value
 *
 * but the traversal pattern remains nearly identical.
 *
 * This chapter focuses on the reusable DFS invariants rather than memorizing
 * five unrelated solutions.
 *
 * =============================================================================
 * PRIMARY PATTERN
 * =============================================================================
 *
 * Pattern
 * --------
 * DFS Path Aggregation
 *
 * Archetype
 * ---------
 * Tree DFS carrying state.
 *
 * State moves from parent -> child.
 *
 * Sometimes children also return information back upward.
 *
 * =============================================================================
 * INCLUDED PROBLEMS
 * =============================================================================
 *
 * 112
 * Path Sum
 *
 * State:
 * remainingSum
 *
 * Return:
 * boolean
 *
 * ------------------------------------------------------------
 *
 * 113
 * Path Sum II
 *
 * State:
 * remainingSum
 * currentPath
 *
 * Return:
 * list of paths
 *
 * ------------------------------------------------------------
 *
 * 437
 * Path Sum III
 *
 * State:
 * prefixSum
 *
 * Return:
 * count
 *
 * ------------------------------------------------------------
 *
 * 129
 * Sum Root to Leaf Numbers
 *
 * State:
 * currentNumber
 *
 * Return:
 * accumulated answer
 *
 * ------------------------------------------------------------
 *
 * 124
 * Binary Tree Maximum Path Sum
 *
 * State propagated downward:
 * none
 *
 * State returned upward:
 * maximum gain
 *
 * Global state:
 * best answer
 *
 * =============================================================================
 * OFFICIAL LINKS
 * =============================================================================
 *
 * 112
 * https://leetcode.com/problems/path-sum/
 *
 * 113
 * https://leetcode.com/problems/path-sum-ii/
 *
 * 437
 * https://leetcode.com/problems/path-sum-iii/
 *
 * 129
 * https://leetcode.com/problems/sum-root-to-leaf-numbers/
 *
 * 124
 * https://leetcode.com/problems/binary-tree-maximum-path-sum/
 *
 * =============================================================================
 * CORE PATTERN OVERVIEW
 * =============================================================================
 *
 * Every DFS call represents:
 *
 *      "I have reached THIS node carrying some path state."
 *
 * The path state can be:
 *
 *      remaining sum
 *
 *      accumulated sum
 *
 *      prefix sum
 *
 *      decimal number
 *
 *      current path
 *
 *      subtree gain
 *
 * -----------------------------------------------------------------------------
 *
 * There are only three operations performed repeatedly.
 *
 * STEP 1
 *
 * Update state using current node.
 *
 * STEP 2
 *
 * Decide whether current node itself completes the objective.
 *
 * STEP 3
 *
 * Continue to children using updated state.
 *
 * -----------------------------------------------------------------------------
 *
 * Almost every solution differs ONLY in:
 *
 *      state representation
 *
 *      update rule
 *
 *      answer update
 *
 * =============================================================================
 * MENTAL MODEL
 * =============================================================================
 *
 * Imagine carrying a backpack while walking from root toward leaves.
 *
 * Each node modifies the backpack.
 *
 * The backpack may contain:
 *
 * remaining sum
 *
 * current number
 *
 * prefix sum
 *
 * current path
 *
 * gain
 *
 * The DFS never loses correctness because every recursive call owns the exact
 * path from root to itself.
 *
 * =============================================================================
 * MASTER INVARIANTS
 * =============================================================================
 *
 * Invariant 1
 * -----------
 *
 * Every recursive call represents ONE unique root-to-current path.
 *
 * -----------------------------------------------------------------------------
 *
 * Invariant 2
 * -----------
 *
 * The state parameter exactly matches that path.
 *
 * Never partially updated.
 *
 * Never delayed.
 *
 * -----------------------------------------------------------------------------
 *
 * Invariant 3
 * -----------
 *
 * Parent computes state.
 *
 * Child trusts it.
 *
 * Child never recomputes ancestor information.
 *
 * -----------------------------------------------------------------------------
 *
 * Invariant 4
 * -----------
 *
 * Enter:
 *
 * update state
 *
 * Exit:
 *
 * restore mutable state if backtracking is used.
 *
 * -----------------------------------------------------------------------------
 *
 * Invariant 5
 * -----------
 *
 * Leaf detection always occurs AFTER incorporating current node.
 *
 * -----------------------------------------------------------------------------
 *
 * Invariant 6
 * -----------
 *
 * Prefix-sum maps describe ONLY the current recursion stack.
 *
 * Never sibling branches.
 *
 * =============================================================================
 * WHY NAIVE THINKING FAILS
 * =============================================================================
 *
 * Mistake:
 *
 * "Every path starts from root."
 *
 * False.
 *
 * Problem 437 allows paths to begin anywhere.
 *
 * -----------------------------------------------------------------------------
 *
 * Mistake:
 *
 * "Maximum path must include root."
 *
 * False.
 *
 * Problem 124 proves otherwise.
 *
 * -----------------------------------------------------------------------------
 *
 * Mistake:
 *
 * "DFS always returns the final answer."
 *
 * False.
 *
 * Sometimes DFS returns:
 *
 * boolean
 *
 * Sometimes:
 *
 * gain
 *
 * Sometimes:
 *
 * partial count
 *
 * Sometimes:
 *
 * nothing
 *
 * =============================================================================
 * IMPLEMENTATION BLUEPRINT
 * =============================================================================
 *
 * Mechanical reconstruction:
 *
 * dfs(node, state)
 *
 * if node == null
 *      return base
 *
 * update state
 *
 * if node satisfies completion
 *      update answer
 *
 * recurse left
 *
 * recurse right
 *
 * restore state if necessary
 *
 * return required value
 *
 * =============================================================================
 * ULTRA COMPACT PSEUDOCODE
 * =============================================================================
 *
 * DFS(node,state)
 *
 * if null
 *      return
 *
 * state <- transition(state,node)
 *
 * update answer if needed
 *
 * DFS(left,state)
 *
 * DFS(right,state)
 *
 * restore if mutable
 *
 * =============================================================================
 * SHARED TREE STRUCTURES
 * =============================================================================
 */
public class BinaryTreePathProblems {

    static final class TreeNode {

        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * **********************************************************************
     * PROBLEM 112
     *
     * Path Sum
     * **********************************************************************
     *
     * Goal
     * ----
     *
     * Determine whether there exists at least one root-to-leaf path whose
     * values add exactly to targetSum.
     *
     * Pattern
     * -------
     *
     * DFS carrying remaining sum.
     *
     * State
     * -----
     *
     * remainingSum
     *
     * Transition
     * ----------
     *
     * remainingSum -= current.val
     *
     * Completion
     * ----------
     *
     * leaf && remainingSum == 0
     */

    static final class PathSum {

        /**
         * ==========================================================
         * Brute Force
         * ==========================================================
         *
         * Same as optimal.
         *
         * There is no meaningful brute-force alternative because every
         * node must be inspected in the worst case.
         */

        static boolean hasPathSum(TreeNode root, int targetSum) {

            // Empty tree cannot contain any root-to-leaf path.
            if (root == null) {
                return false;
            }

            // Invariant:
            // remaining target after consuming current node.
            targetSum -= root.val;

            // Completion is checked only after current node contributes.
            if (root.left == null && root.right == null) {
                return targetSum == 0;
            }

            return hasPathSum(root.left, targetSum)
                    || hasPathSum(root.right, targetSum);
        }
    }

/**
 * **********************************************************************
 * PROBLEM 113
 *
 * Path Sum II
 * **********************************************************************
 */

static final class PathSumII {

    /**
     * ==========================================================
     * IDEA
     * ==========================================================
     *
     * Carry two pieces of state simultaneously.
     *
     * 1. remainingSum
     * 2. currentPath
     *
     * Unlike Problem 112, we cannot stop after finding one solution.
     * Every valid root-to-leaf path must be collected.
     *
     * Therefore:
     *
     *      DFS + Backtracking
     *
     * becomes mandatory.
     *
     * ==========================================================
     * INVARIANT
     * ==========================================================
     *
     * At the beginning of every DFS call:
     *
     * currentPath exactly represents the nodes from root to parent.
     *
     * After adding current node:
     *
     * currentPath exactly represents the path from root to current node.
     *
     * Before returning:
     *
     * currentPath must be restored.
     *
     * This restoration guarantees sibling branches never observe each
     * other's nodes.
     *
     * ==========================================================
     * WHY BACKTRACKING?
     * ==========================================================
     *
     * Without removing the current node before returning:
     *
     * left subtree nodes would incorrectly remain while exploring
     * the right subtree.
     *
     * That violates the path invariant.
     */

    static List<List<Integer>> pathSum(TreeNode root, int targetSum) {

        List<List<Integer>> answer = new ArrayList<>();

        dfs(root,
                targetSum,
                new ArrayList<>(),
                answer);

        return answer;
    }

    private static void dfs(
            TreeNode node,
            int remainingSum,
            List<Integer> path,
            List<List<Integer>> answer) {

        if (node == null) {
            return;
        }

        // Invariant:
        // path now represents root -> current node.
        path.add(node.val);

        remainingSum -= node.val;

        // Completion only after current node participates.
        if (node.left == null
                && node.right == null
                && remainingSum == 0) {

            // Copy because path is mutable.
            answer.add(new ArrayList<>(path));

            // Restore invariant before returning.
            path.remove(path.size() - 1);
            return;
        }

        dfs(node.left,
                remainingSum,
                path,
                answer);

        dfs(node.right,
                remainingSum,
                path,
                answer);

        // Restore path for sibling recursion.
        path.remove(path.size() - 1);
    }
}

    /**
     * **********************************************************************
     * PROBLEM 437
     *
     * Path Sum III
     * **********************************************************************
     *
     * Difficulty
     * ----------
     *
     * Hard
     *
     * This problem introduces an entirely different invariant.
     *
     * Paths:
     *
     *      do NOT need to begin at root.
     *
     *      do NOT need to end at leaf.
     *
     * They only need to move downward.
     *
     * **********************************************************************
     * APPROACH 1
     *
     * Restart DFS from every node.
     * **********************************************************************
     */

    static final class PathSumIIIBruteForce {

        /**
         * ==========================================================
         * IDEA
         * ==========================================================
         *
         * Every node is treated as a possible starting point.
         *
         * For each node:
         *
         * explore every downward path.
         *
         * ==========================================================
         * INVARIANT
         * ==========================================================
         *
         * countFrom(node,sum)
         *
         * counts only paths beginning exactly at node.
         *
         * The outer DFS guarantees every possible starting node is visited.
         *
         * ==========================================================
         * COMPLEXITY
         * ==========================================================
         *
         * Balanced tree:
         *
         * O(n log n)
         *
         * Worst case:
         *
         * O(n²)
         */

        static int pathSum(TreeNode root, int targetSum) {

            if (root == null) {
                return 0;
            }

            return countFrom(root, targetSum)
                    + pathSum(root.left, targetSum)
                    + pathSum(root.right, targetSum);
        }

        private static int countFrom(TreeNode node, long remainingSum) {

            if (node == null) {
                return 0;
            }

            int count = 0;

            if (remainingSum == node.val) {
                count++;
            }

            remainingSum -= node.val;

            count += countFrom(node.left, remainingSum);

            count += countFrom(node.right, remainingSum);

            return count;
        }
    }

/**
 * **********************************************************************
 * APPROACH 2
 *
 * Prefix Sum
 * **********************************************************************
 *
 * This is one of the most important tree techniques in interviews.
 *
 * It is exactly the tree analogue of:
 *
 * Subarray Sum Equals K.
 *
 * Instead of:
 *
 * array prefix sums
 *
 * we maintain:
 *
 * root-to-current prefix sums.
 *
 * **********************************************************************
 * CORE INVARIANT
 * **********************************************************************
 *
 * prefixFrequency contains ONLY prefix sums that belong to the
 * current recursion stack.
 *
 * Never previous sibling branches.
 *
 * Therefore:
 *
 * insertion happens before recursion.
 *
 * removal happens after recursion.
 *
 * Missing the removal step is the single most common bug.
 */
static final class PathSumIIIPrefixSum {

    /**
     * ==========================================================
     * MENTAL MODEL
     * ==========================================================
     *
     * Let:
     *
     * currentPrefix
     *
     * denote the sum from the root to the current node.
     *
     * Consider any ancestor with prefix:
     *
     * ancestorPrefix
     *
     * Then the sum of the downward path beginning immediately
     * after that ancestor and ending at the current node equals:
     *
     * currentPrefix - ancestorPrefix
     *
     * We need:
     *
     * currentPrefix - ancestorPrefix = target
     *
     * Therefore:
     *
     * ancestorPrefix = currentPrefix - target
     *
     * Instead of searching ancestors every time,
     * we store their frequencies in a HashMap.
     *
     * ==========================================================
     * MAP INVARIANT
     * ==========================================================
     *
     * prefixFrequency[p]
     *
     * =
     *
     * number of ancestors on the CURRENT recursion path
     * having prefix sum p.
     *
     * No node outside the current DFS stack may remain inside
     * the map.
     *
     * ==========================================================
     * TRANSITION
     * ==========================================================
     *
     * Update current prefix.
     *
     * Query answer.
     *
     * Insert current prefix.
     *
     * Explore children.
     *
     * Remove current prefix.
     *
     * This order is mandatory.
     */

    static int pathSum(TreeNode root, int targetSum) {

        Map<Long, Integer> prefixFrequency = new HashMap<>();

        // Empty prefix before starting traversal.
        prefixFrequency.put(0L, 1);

        return dfs(root,
                0L,
                targetSum,
                prefixFrequency);
    }

    private static int dfs(
            TreeNode node,
            long currentPrefix,
            int targetSum,
            Map<Long, Integer> prefixFrequency) {

        if (node == null) {
            return 0;
        }

        // Invariant:
        // currentPrefix equals root -> current path sum.
        currentPrefix += node.val;

        // Every previous ancestor having this prefix completes
        // one valid downward path.
        int pathsEndingHere =
                prefixFrequency.getOrDefault(
                        currentPrefix - targetSum,
                        0);

        // Make current prefix available to descendants.
        prefixFrequency.put(
                currentPrefix,
                prefixFrequency.getOrDefault(currentPrefix, 0) + 1);

        int total =
                pathsEndingHere
                        + dfs(node.left,
                        currentPrefix,
                        targetSum,
                        prefixFrequency)
                        + dfs(node.right,
                        currentPrefix,
                        targetSum,
                        prefixFrequency);

        // Restore recursion-stack invariant.
        prefixFrequency.put(
                currentPrefix,
                prefixFrequency.get(currentPrefix) - 1);

        return total;
    }
}

    /**
     * **********************************************************************
     * PROBLEM 129
     *
     * Sum Root to Leaf Numbers
     * **********************************************************************
     *
     * Every root-to-leaf path represents a decimal number.
     *
     * Example:
     *
     * 1 -> 2 -> 3
     *
     * becomes
     *
     * 123
     *
     * **********************************************************************
     * PATTERN
     * **********************************************************************
     *
     * DFS carrying accumulated decimal value.
     *
     * **********************************************************************
     * STATE
     * **********************************************************************
     *
     * currentNumber
     *
     * **********************************************************************
     * TRANSITION
     * **********************************************************************
     *
     * currentNumber =
     *
     * currentNumber * 10 + node.val
     *
     * **********************************************************************
     * INVARIANT
     * **********************************************************************
     *
     * At every node,
     *
     * currentNumber exactly equals the decimal number represented
     * by the path from root to the current node.
     */

    static final class SumRootToLeafNumbers {

        static int sumNumbers(TreeNode root) {

            return dfs(root, 0);
        }

        private static int dfs(
                TreeNode node,
                int currentNumber) {

            if (node == null) {
                return 0;
            }

            // Extend decimal representation.
            currentNumber =
                    currentNumber * 10 + node.val;

            // Leaf contributes exactly one completed number.
            if (node.left == null && node.right == null) {
                return currentNumber;
            }

            return dfs(node.left, currentNumber)
                    + dfs(node.right, currentNumber);
        }
    }

/**
 * **********************************************************************
 * PROBLEM 124
 *
 * Binary Tree Maximum Path Sum
 * **********************************************************************
 *
 * This problem changes the direction of information flow.
 *
 * Previous problems:
 *
 * parent -> child
 *
 * This problem:
 *
 * child -> parent
 *
 * Therefore this becomes:
 *
 * Tree Dynamic Programming.
 *
 * The recursion RETURNS useful information upward.
 *
 * The returned value is NOT the final answer.
 *
 * That distinction is the most important invariant in this problem.
 */
static final class BinaryTreeMaximumPathSum {

    /**
     * ==========================================================
     * MENTAL MODEL
     * ==========================================================
     *
     * Think of every node as asking:
     *
     * "If my parent wants to continue a path through me,
     * what is the maximum gain I can contribute?"
     *
     * A parent cannot simultaneously continue through both
     * children because a path cannot fork upward.
     *
     * Therefore the returned value is:
     *
     *      node +
     *      max(leftGain, rightGain)
     *
     * NOT
     *
     *      node + left + right
     *
     * ----------------------------------------------------------
     *
     * However,
     *
     * when THIS node is considered as the highest point of a path,
     * BOTH children may participate.
     *
     * That candidate path becomes
     *
     *      leftGain
     *          +
     *      node
     *          +
     *      rightGain
     *
     * and is used ONLY for updating the global answer.
     *
     * This distinction is the entire algorithm.
     *
     * ==========================================================
     * MASTER INVARIANT
     * ==========================================================
     *
     * dfs(node)
     *
     * returns
     *
     * maximum downward gain beginning at node.
     *
     * globalAnswer
     *
     * stores
     *
     * maximum complete path discovered anywhere.
     *
     * Returned value != global answer.
     *
     * ==========================================================
     * WHY NEGATIVE GAINS ARE DISCARDED
     * ==========================================================
     *
     * Suppose left subtree contributes -7.
     *
     * Keeping it only decreases every possible path.
     *
     * Therefore:
     *
     * gain = max(gain,0)
     *
     * behaves exactly like Kadane's Algorithm.
     */

    private int globalMaximum;

    int maxPathSum(TreeNode root) {

        globalMaximum = Integer.MIN_VALUE;

        gain(root);

        return globalMaximum;
    }

    private int gain(TreeNode node) {

        if (node == null) {
            return 0;
        }

        // Invariant:
        // Ignore branches that decrease the path.
        int leftGain = Math.max(gain(node.left), 0);

        int rightGain = Math.max(gain(node.right), 0);

        // Candidate whose highest node is the current node.
        int candidate =
                node.val
                        + leftGain
                        + rightGain;

        globalMaximum =
                Math.max(globalMaximum, candidate);

        // Parent may continue through only ONE child.
        return node.val
                + Math.max(leftGain, rightGain);
    }
}

/**
 * **********************************************************************
 * ⚫ PATTERN MAPPING
 * **********************************************************************
 *
 *                     STATE                     RETURN
 * ---------------------------------------------------------------
 *
 * 112
 * remainingSum                        boolean
 *
 * ---------------------------------------------------------------
 *
 * 113
 * remainingSum + path                 void
 *
 * answer stored externally
 *
 * ---------------------------------------------------------------
 *
 * 437 (Brute)
 * remainingSum                        count
 *
 * ---------------------------------------------------------------
 *
 * 437 (Prefix)
 * prefixSum                           count
 *
 * ---------------------------------------------------------------
 *
 * 129
 * decimalNumber                       accumulated sum
 *
 * ---------------------------------------------------------------
 *
 * 124
 * subtreeGain                         gain upward
 *
 * global maximum stored separately
 *
 * **********************************************************************
 * STATE TRANSITION COMPARISON
 * **********************************************************************
 *
 * 112
 *
 * remaining -= value
 *
 * ---------------------------------------------------------------
 *
 * 113
 *
 * remaining -= value
 * path.add(value)
 *
 * ---------------------------------------------------------------
 *
 * 437 Prefix
 *
 * prefix += value
 *
 * ---------------------------------------------------------------
 *
 * 129
 *
 * number = number * 10 + value
 *
 * ---------------------------------------------------------------
 *
 * 124
 *
 * gain = node + max(left,right)
 *
 * **********************************************************************
 * WHAT CHANGES?
 * **********************************************************************
 *
 * Only the state.
 *
 * DFS skeleton remains almost identical.
 *
 * **********************************************************************
 * WHY THESE PROBLEMS ARE OFTEN CONFUSED
 * **********************************************************************
 *
 * Every one of them performs DFS.
 *
 * The interview difficulty comes from recognizing
 * WHICH information should travel through recursion.
 *
 * Once the state is chosen correctly,
 * implementation becomes mechanical.
 *
 * Incorrect state selection almost always produces
 * unnecessarily complicated solutions.
 */

/**
 * **********************************************************************
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * **********************************************************************
 *
 * Mistake 1
 * ---------
 *
 * Check leaf before updating state.
 *
 * Violated invariant:
 *
 * Current node has not yet contributed.
 *
 * --------------------------------------------------------------
 *
 * Mistake 2
 * ---------
 *
 * Forget backtracking in Path Sum II.
 *
 * Symptom:
 *
 * Paths contain nodes from sibling branches.
 *
 * --------------------------------------------------------------
 *
 * Mistake 3
 * ---------
 *
 * Forget removing prefix sums in Path Sum III.
 *
 * Symptom:
 *
 * Cross-branch paths become falsely valid.
 *
 * --------------------------------------------------------------
 *
 * Mistake 4
 * ---------
 *
 * Return left + right + node in Problem 124.
 *
 * Violated invariant:
 *
 * Parent cannot continue through two children.
 *
 * --------------------------------------------------------------
 *
 * Mistake 5
 * ---------
 *
 * Do not discard negative gains.
 *
 * Symptom:
 *
 * Maximum path becomes smaller than necessary.
 */

/**
 * **********************************************************************
 * ⚙ IMPLEMENTATION BLUEPRINT
 * **********************************************************************
 *
 * Rather than memorizing five different algorithms,
 * memorize these reusable DFS templates.
 *
 * ======================================================================
 * TEMPLATE 1
 *
 * Carry State Downward
 * ======================================================================
 *
 * dfs(node, state)
 *
 * if node == null
 *      return
 *
 * state = transition(state,node)
 *
 * if completion
 *      update answer
 *
 * dfs(left,state)
 *
 * dfs(right,state)
 *
 * restore mutable state if required
 *
 * ----------------------------------------------------------------------
 *
 * Used by:
 *
 * Path Sum
 *
 * Path Sum II
 *
 * Sum Root To Leaf Numbers
 *
 * Prefix Sum
 *
 * ======================================================================
 * TEMPLATE 2
 *
 * Return Information Upward
 * ======================================================================
 *
 * dfs(node)
 *
 * if null
 *      return identity
 *
 * left = dfs(left)
 *
 * right = dfs(right)
 *
 * combine
 *
 * update global answer
 *
 * return contribution to parent
 *
 * ----------------------------------------------------------------------
 *
 * Used by
 *
 * Binary Tree Maximum Path Sum
 *
 * ======================================================================
 * TEMPLATE 3
 *
 * Restart DFS
 * ======================================================================
 *
 * answer(node)
 *
 * answer += countStartingHere(node)
 *
 * answer(left)
 *
 * answer(right)
 *
 * ----------------------------------------------------------------------
 *
 * Used by
 *
 * Brute-force Path Sum III
 */

/**
 * **********************************************************************
 * 🟣 INTERVIEW ARTICULATION
 * **********************************************************************
 *
 * Question:
 *
 * "How do you recognize this family of problems?"
 *
 * Good answer:
 *
 * Every recursive call represents exactly one root-to-current path.
 *
 * I choose a state that completely summarizes that path.
 *
 * The state is updated once when entering a node.
 *
 * Then children inherit that updated state.
 *
 * If mutable state is shared,
 * it is restored during backtracking.
 *
 * ----------------------------------------------------------------------
 *
 * Question:
 *
 * "Why is Prefix Sum O(n)?"
 *
 * Good answer:
 *
 * Every node performs
 *
 * one lookup
 *
 * one insertion
 *
 * one deletion
 *
 * inside a HashMap.
 *
 * Therefore every node is processed once.
 *
 * ----------------------------------------------------------------------
 *
 * Question:
 *
 * "Why remove the prefix during backtracking?"
 *
 * Good answer:
 *
 * The HashMap must describe only the current recursion stack.
 *
 * Leaving prefixes from sibling branches creates paths that
 * never actually exist.
 *
 * ----------------------------------------------------------------------
 *
 * Question:
 *
 * "Why does Maximum Path Sum return only one child?"
 *
 * Good answer:
 *
 * The returned value is a contribution to the parent.
 *
 * A parent can continue through only one child.
 *
 * Two-child paths terminate at the current node and therefore
 * update only the global answer.
 */

/**
 * **********************************************************************
 * 🎯 30 SECOND RECALL SHEET
 * **********************************************************************
 *
 * Pattern
 * -------
 *
 * DFS Path Aggregation
 *
 * ----------------------------------------------------------------------
 *
 * Trigger
 * -------
 *
 * Information accumulates while walking along tree paths.
 *
 * ----------------------------------------------------------------------
 *
 * Search Space
 * ------------
 *
 * Root-to-current recursion path.
 *
 * ----------------------------------------------------------------------
 *
 * State
 * -----
 *
 * remainingSum
 *
 * prefixSum
 *
 * currentPath
 *
 * decimalNumber
 *
 * subtreeGain
 *
 * ----------------------------------------------------------------------
 *
 * Transition
 * ----------
 *
 * Update exactly once upon entering the node.
 *
 * ----------------------------------------------------------------------
 *
 * Discard Rule
 * ------------
 *
 * Ignore negative gains in Maximum Path Sum.
 *
 * ----------------------------------------------------------------------
 *
 * Backtracking Rule
 * -----------------
 *
 * Undo every mutable change before returning.
 *
 * ----------------------------------------------------------------------
 *
 * Prefix Rule
 * -----------
 *
 * Insert before children.
 *
 * Remove after children.
 *
 * ----------------------------------------------------------------------
 *
 * One-liner
 * ---------
 *
 * Choose the correct path state.
 * DFS becomes mechanical afterwards.
 */

/**
 * **********************************************************************
 * 🔄 VARIATIONS
 * **********************************************************************
 *
 * Variant
 * -------
 *
 * Root -> Leaf
 *
 * State
 *
 * Remaining Sum
 *
 * ----------------------------------------------------------------------
 *
 * Variant
 * -------
 *
 * Enumerate Paths
 *
 * Additional State
 *
 * Current Path
 *
 * ----------------------------------------------------------------------
 *
 * Variant
 * -------
 *
 * Count Arbitrary Downward Paths
 *
 * Additional State
 *
 * Prefix Sum
 *
 * ----------------------------------------------------------------------
 *
 * Variant
 * -------
 *
 * Convert Path Into Number
 *
 * Additional State
 *
 * Decimal Number
 *
 * ----------------------------------------------------------------------
 *
 * Variant
 * -------
 *
 * Optimize Path
 *
 * Returned State
 *
 * Maximum Gain
 *
 * ----------------------------------------------------------------------
 *
 * Pattern Boundary
 * ----------------
 *
 * If information depends simultaneously on
 * parent and arbitrary descendants,
 * simple path-state DFS is insufficient.
 *
 * Tree DP or rerooting may be required.
 */

/**
 * **********************************************************************
 * 🧠 MASTERY CHECKLIST
 * **********************************************************************
 *
 * Before moving on, verify that you can answer every question
 * without looking at the implementations.
 *
 * ==========================================================
 * 1. PATH STATE
 * ==========================================================
 *
 * □ What does one recursive call represent?
 *
 * Answer:
 *
 * Exactly one root-to-current path.
 *
 * ----------------------------------------------------------
 *
 * □ What must every state variable summarize?
 *
 * Answer:
 *
 * Everything required about that path.
 *
 * Never less.
 *
 * Never more.
 *
 * ==========================================================
 * 2. TRANSITION
 * ==========================================================
 *
 * □ When is the state updated?
 *
 * Answer:
 *
 * Immediately after entering the node.
 *
 * Before visiting children.
 *
 * ----------------------------------------------------------
 *
 * □ Why?
 *
 * Answer:
 *
 * Children should inherit the complete path.
 *
 * ==========================================================
 * 3. TERMINATION
 * ==========================================================
 *
 * □ When is a leaf evaluated?
 *
 * Answer:
 *
 * After incorporating the current node.
 *
 * Never before.
 *
 * ==========================================================
 * 4. BACKTRACKING
 * ==========================================================
 *
 * □ Which problems require restoration?
 *
 * Answer:
 *
 * Mutable shared state.
 *
 * Example:
 *
 * currentPath
 *
 * prefix map
 *
 * ----------------------------------------------------------
 *
 * □ Which problems do not?
 *
 * Answer:
 *
 * Primitive values
 *
 * int
 *
 * long
 *
 * boolean
 *
 * because Java passes them by value.
 *
 * ==========================================================
 * 5. PREFIX SUM
 * ==========================================================
 *
 * □ Why initialize
 *
 * prefix[0]=1 ?
 *
 * Answer:
 *
 * So a path beginning at the root can be counted.
 *
 * ----------------------------------------------------------
 *
 * □ Why delete prefixes during backtracking?
 *
 * Answer:
 *
 * The HashMap must describe only the current recursion path.
 *
 * ==========================================================
 * 6. MAXIMUM PATH SUM
 * ==========================================================
 *
 * □ Why discard negative gains?
 *
 * Answer:
 *
 * A negative contribution can never improve a maximum.
 *
 * ----------------------------------------------------------
 *
 * □ Why return only one child?
 *
 * Answer:
 *
 * Paths cannot split while moving upward.
 *
 * ==========================================================
 * 7. DEBUGGING READINESS
 * ==========================================================
 *
 * If the answer is wrong,
 * check these first:
 *
 * □ Leaf checked too early?
 *
 * □ Forgot subtraction?
 *
 * □ Forgot multiplication by ten?
 *
 * □ Forgot removing current path?
 *
 * □ Forgot removing prefix?
 *
 * □ Returned left + right upward?
 *
 * □ Global variable reinitialized?
 *
 * □ Integer overflow?
 *
 * ==========================================================
 * 8. PATTERN BOUNDARY
 * ==========================================================
 *
 * This pattern applies when:
 *
 * information naturally accumulates along a path.
 *
 * It starts to break when:
 *
 * multiple independent subtrees must interact
 * in complicated ways.
 *
 * Then Tree DP,
 * rerooting,
 * centroid decomposition,
 * Euler tour,
 * Heavy-Light Decomposition,
 * etc.,
 * become more suitable.
 */

/**
 * **********************************************************************
 * ⚫ COMPARISON TABLE
 * **********************************************************************
 *
 * -----------------------------------------------------------------------
 * Problem                State              Answer
 * -----------------------------------------------------------------------
 *
 * Path Sum
 * remainingSum
 * boolean
 *
 * -----------------------------------------------------------------------
 *
 * Path Sum II
 * remainingSum + path
 * list
 *
 * -----------------------------------------------------------------------
 *
 * Path Sum III
 * remainingSum
 * count
 *
 * -----------------------------------------------------------------------
 *
 * Path Sum III Prefix
 * prefixSum
 * count
 *
 * -----------------------------------------------------------------------
 *
 * Sum Root To Leaf
 * decimalNumber
 * total sum
 *
 * -----------------------------------------------------------------------
 *
 * Maximum Path Sum
 * subtree gain
 * maximum
 *
 * **********************************************************************
 * TIME COMPLEXITIES
 * **********************************************************************
 *
 * Path Sum
 *
 * Time
 *
 * O(n)
 *
 * Space
 *
 * O(h)
 *
 * ----------------------------------------------------------
 *
 * Path Sum II
 *
 * Time
 *
 * O(n)
 *
 * Space
 *
 * O(h)
 *
 * (+ output)
 *
 * ----------------------------------------------------------
 *
 * Path Sum III Brute
 *
 * Time
 *
 * O(n²)
 *
 * Worst case
 *
 * O(n log n)
 *
 * Balanced
 *
 * ----------------------------------------------------------
 *
 * Path Sum III Prefix
 *
 * Time
 *
 * O(n)
 *
 * Space
 *
 * O(h)
 *
 * ----------------------------------------------------------
 *
 * Sum Root To Leaf
 *
 * Time
 *
 * O(n)
 *
 * Space
 *
 * O(h)
 *
 * ----------------------------------------------------------
 *
 * Maximum Path Sum
 *
 * Time
 *
 * O(n)
 *
 * Space
 *
 * O(h)
 */

/**
 * **********************************************************************
 * 🔍 FORENSIC DEBUGGING GUIDE
 * **********************************************************************
 *
 * These are the bugs most frequently seen during interviews.
 *
 * Instead of memorizing fixes, learn which invariant has been violated.
 *
 * ==========================================================
 * BUG 1
 * ==========================================================
 *
 * Symptom
 * -------
 *
 * Path Sum returns false for obvious valid cases.
 *
 * Typical Bug
 * -----------
 *
 * if (leaf)
 *     return remaining == 0;
 *
 * remaining -= node.val;
 *
 * Why Wrong?
 * ----------
 *
 * The current node has not yet contributed.
 *
 * Broken Invariant
 * ----------------
 *
 * State must always describe the current node.
 *
 * ==========================================================
 * BUG 2
 * ==========================================================
 *
 * Symptom
 * -------
 *
 * Path Sum II returns paths containing nodes
 * from different branches.
 *
 * Cause
 * -----
 *
 * Missing:
 *
 * path.remove(path.size()-1)
 *
 * Broken Invariant
 * ----------------
 *
 * path must equal the current recursion stack.
 *
 * ==========================================================
 * BUG 3
 * ==========================================================
 *
 * Symptom
 * -------
 *
 * Prefix Sum solution counts impossible paths.
 *
 * Cause
 * -----
 *
 * Prefix never removed.
 *
 * Broken Invariant
 * ----------------
 *
 * HashMap now represents multiple branches.
 *
 * ==========================================================
 * BUG 4
 * ==========================================================
 *
 * Symptom
 * -------
 *
 * Maximum Path Sum returns values
 * smaller than expected.
 *
 * Cause
 * -----
 *
 * Negative gains propagated upward.
 *
 * Broken Invariant
 * ----------------
 *
 * Returned contribution should never decrease
 * the parent's best path.
 *
 * ==========================================================
 * BUG 5
 * ==========================================================
 *
 * Symptom
 * -------
 *
 * Maximum Path Sum returns impossible values.
 *
 * Cause
 * -----
 *
 * Returning
 *
 * left + node + right
 *
 * to parent.
 *
 * Broken Invariant
 * ----------------
 *
 * Returned path cannot fork.
 *
 * ==========================================================
 * BUG 6
 * ==========================================================
 *
 * Symptom
 * -------
 *
 * Prefix Sum misses paths beginning at root.
 *
 * Cause
 * -----
 *
 * Missing
 *
 * prefix.put(0L,1)
 *
 * Broken Invariant
 * ----------------
 *
 * Empty prefix must exist before traversal starts.
 */

/**
 * **********************************************************************
 * ⚫ RECURSION STATE COMPARISON
 * **********************************************************************
 *
 * Every recursive algorithm has three questions.
 *
 * 1.
 * What information arrives?
 *
 * 2.
 * What information changes?
 *
 * 3.
 * What information leaves?
 *
 * ----------------------------------------------------------------------
 *
 * Problem 112
 *
 * Arrives:
 *
 * remainingSum
 *
 * Changes:
 *
 * subtract node
 *
 * Leaves:
 *
 * boolean
 *
 * ----------------------------------------------------------------------
 *
 * Problem 113
 *
 * Arrives:
 *
 * remainingSum
 *
 * path
 *
 * Changes:
 *
 * subtract
 *
 * push
 *
 * Leaves:
 *
 * nothing
 *
 * ----------------------------------------------------------------------
 *
 * Problem 437
 *
 * Arrives:
 *
 * prefix
 *
 * Changes:
 *
 * add node
 *
 * insert map
 *
 * Leaves:
 *
 * count
 *
 * ----------------------------------------------------------------------
 *
 * Problem 129
 *
 * Arrives:
 *
 * decimal number
 *
 * Changes:
 *
 * multiply
 *
 * add digit
 *
 * Leaves:
 *
 * subtree total
 *
 * ----------------------------------------------------------------------
 *
 * Problem 124
 *
 * Arrives:
 *
 * nothing
 *
 * Children return gain.
 *
 * Changes:
 *
 * compute best contribution.
 *
 * Leaves:
 *
 * maximum gain.
 */

/**
 * **********************************************************************
 * 🧩 IMPLEMENTATION RECONSTRUCTION DRILL
 * **********************************************************************
 *
 * Before writing any code,
 * ask yourself these questions.
 *
 * ----------------------------------------------------------------------
 *
 * Question 1
 *
 * What exactly is my recursion state?
 *
 * ----------------------------------------------------------------------
 *
 * Question 2
 *
 * Does the state move
 *
 * downward
 *
 * upward
 *
 * or both?
 *
 * ----------------------------------------------------------------------
 *
 * Question 3
 *
 * Does the answer live
 *
 * inside return value
 *
 * or
 *
 * global variable?
 *
 * ----------------------------------------------------------------------
 *
 * Question 4
 *
 * Is my state mutable?
 *
 * If yes,
 *
 * where is the undo operation?
 *
 * ----------------------------------------------------------------------
 *
 * Question 5
 *
 * Does parent receive
 *
 * one path
 *
 * or
 *
 * complete answer?
 *
 * ----------------------------------------------------------------------
 *
 * Correctly answering these five questions
 * usually determines the entire implementation.
 */

/**
 * **********************************************************************
 * 🧠 PATTERN RE-DERIVATION GUIDE
 * **********************************************************************
 *
 * The goal of this section is to reconstruct every solution from first
 * principles instead of memorizing code.
 *
 * ==========================================================
 * Problem 112
 * Path Sum
 * ==========================================================
 *
 * Ask:
 *
 * What information must every child know?
 *
 * Answer:
 *
 * Remaining sum after consuming every ancestor.
 *
 * Therefore:
 *
 * State
 *
 * remainingSum
 *
 * Transition
 *
 * remainingSum -= node.val
 *
 * Completion
 *
 * leaf && remainingSum == 0
 *
 * ==========================================================
 * Problem 113
 * Path Sum II
 * ==========================================================
 *
 * Same state.
 *
 * Additional requirement:
 *
 * Recover actual nodes.
 *
 * Therefore another state becomes necessary:
 *
 * currentPath
 *
 * Since currentPath is mutable,
 * restoration becomes mandatory.
 *
 * ==========================================================
 * Problem 437
 * ==========================================================
 *
 * Root restriction disappears.
 *
 * Therefore remainingSum alone is insufficient.
 *
 * We instead need:
 *
 * prefix(root,current)
 *
 * Every ancestor prefix becomes a possible starting point.
 *
 * Therefore:
 *
 * store frequencies.
 *
 * ==========================================================
 * Problem 129
 * ==========================================================
 *
 * Every edge extends a decimal number.
 *
 * Decimal append rule:
 *
 * abc
 *
 * +
 *
 * d
 *
 * =
 *
 * abc * 10 + d
 *
 * That alone determines the transition.
 *
 * ==========================================================
 * Problem 124
 * ==========================================================
 *
 * Parent asks:
 *
 * "How much can you contribute?"
 *
 * Child therefore returns
 *
 * one downward chain.
 *
 * Global answer separately evaluates
 *
 * left + node + right.
 */

/**
 * **********************************************************************
 * ⚫ STATE EVOLUTION EXAMPLES
 * **********************************************************************
 *
 * Example Tree
 *
 *              5
 *            /   \
 *           4     8
 *          /
 *        11
 *       /  \
 *      7    2
 *
 * ==========================================================
 * Remaining Sum
 * ==========================================================
 *
 * Target = 22
 *
 * Node
 *
 * 5
 *
 * remaining = 17
 *
 * ↓
 *
 * 4
 *
 * remaining = 13
 *
 * ↓
 *
 * 11
 *
 * remaining = 2
 *
 * ↓
 *
 * 2
 *
 * remaining = 0
 *
 * Valid path.
 *
 * ==========================================================
 * Decimal Number
 * ==========================================================
 *
 * Path
 *
 * 1
 *
 * 2
 *
 * 3
 *
 * evolves as
 *
 * 0
 *
 * ->
 *
 * 1
 *
 * ->
 *
 * 12
 *
 * ->
 *
 * 123
 *
 * ==========================================================
 * Prefix Sum
 * ==========================================================
 *
 * Prefixes
 *
 * 10
 *
 * 15
 *
 * 18
 *
 * 16
 *
 * ...
 *
 * Every node asks:
 *
 * "Has there already been a prefix equal to
 * currentPrefix - target?"
 *
 * If yes,
 * every occurrence corresponds to one valid path.
 *
 * ==========================================================
 * Maximum Gain
 * ==========================================================
 *
 * gain(node)
 *
 * =
 *
 * node
 *
 * +
 *
 * max(leftGain,rightGain)
 *
 * Candidate
 *
 * =
 *
 * leftGain
 *
 * +
 *
 * node
 *
 * +
 *
 * rightGain
 */

/**
 * **********************************************************************
 * 🔄 TRANSFER LEARNING
 * **********************************************************************
 *
 * Once this chapter is mastered,
 * the same reasoning transfers directly to:
 *
 * • Pseudo-Palindromic Paths
 *
 * • Smallest String Starting From Leaf
 *
 * • Longest ZigZag Path
 *
 * • Deepest Leaves Sum
 *
 * • Binary Tree Cameras
 *
 * • House Robber III
 *
 * • Diameter of Binary Tree
 *
 * • Longest Univalue Path
 *
 * • Path Sum IV
 *
 * • Even/Odd Tree
 *
 * because each problem differs primarily in:
 *
 * state,
 * transition,
 * answer update,
 * and return semantics,
 *
 * while preserving the same DFS framework.
 */

/**
 * **********************************************************************
 * ⚫ INTERVIEW DECISION TREE
 * **********************************************************************
 *
 * Instead of remembering problem numbers,
 * identify the required recursion state.
 *
 * ----------------------------------------------------------------------
 *
 * Does every child only need information from its ancestors?
 *
 * YES
 *
 * →
 *
 * Carry state downward.
 *
 * ----------------------------------------------------------------------
 *
 * Do we need the actual path?
 *
 * YES
 *
 * →
 *
 * Maintain a mutable path list.
 *
 * →
 *
 * Backtrack after recursion.
 *
 * ----------------------------------------------------------------------
 *
 * Can the path begin anywhere?
 *
 * YES
 *
 * →
 *
 * Prefix Sum.
 *
 * ----------------------------------------------------------------------
 *
 * Does the parent need information from children?
 *
 * YES
 *
 * →
 *
 * Return a value upward.
 *
 * ----------------------------------------------------------------------
 *
 * Can the answer fork through both children?
 *
 * YES
 *
 * →
 *
 * Global answer.
 *
 * Parent receives only one contribution.
 *
 * ----------------------------------------------------------------------
 *
 * Is the answer computed only at leaves?
 *
 * YES
 *
 * →
 *
 * Update only after leaf detection.
 *
 * ----------------------------------------------------------------------
 *
 * Does every node independently produce a candidate?
 *
 * YES
 *
 * →
 *
 * Evaluate candidate during postorder.
 */

/**
 * **********************************************************************
 * ⚫ COMMON PATTERN CONFUSIONS
 * **********************************************************************
 *
 * Path Sum
 *
 * vs
 *
 * Subarray Sum Equals K
 *
 * ------------------------------------------------------------
 *
 * Array
 *
 * prefix[i]
 *
 * Tree
 *
 * prefix(root,current)
 *
 * ------------------------------------------------------------
 *
 * Difference:
 *
 * Arrays never branch.
 *
 * Trees branch.
 *
 * Therefore trees require backtracking of prefix frequencies.
 *
 * **********************************************************************
 *
 * Maximum Path Sum
 *
 * vs
 *
 * Diameter of Binary Tree
 *
 * ------------------------------------------------------------
 *
 * Diameter
 *
 * Optimize number of edges.
 *
 * Maximum Path Sum
 *
 * Optimize sum of values.
 *
 * Same postorder framework.
 *
 * Different transition.
 *
 * **********************************************************************
 *
 * Path Sum II
 *
 * vs
 *
 * Combination Sum
 *
 * ------------------------------------------------------------
 *
 * Both require
 *
 * push
 *
 * recurse
 *
 * pop
 *
 * The underlying invariant is identical.
 *
 * The search space differs.
 */

/**
 * **********************************************************************
 * ⚫ MECHANICAL CODING ORDER
 * **********************************************************************
 *
 * This section is designed for whiteboard interviews.
 *
 * Never begin by writing recursion.
 *
 * Instead write in this exact order.
 *
 * ==========================================================
 * PATH SUM
 * ==========================================================
 *
 * Step 1
 *
 * Base case.
 *
 * Step 2
 *
 * Consume current node.
 *
 * Step 3
 *
 * Check leaf.
 *
 * Step 4
 *
 * DFS left.
 *
 * Step 5
 *
 * DFS right.
 *
 * ==========================================================
 * PATH SUM II
 * ==========================================================
 *
 * Step 1
 *
 * Add current node.
 *
 * Step 2
 *
 * Update remaining sum.
 *
 * Step 3
 *
 * Copy path if leaf.
 *
 * Step 4
 *
 * DFS children.
 *
 * Step 5
 *
 * Remove current node.
 *
 * ==========================================================
 * PREFIX SUM
 * ==========================================================
 *
 * Step 1
 *
 * Update prefix.
 *
 * Step 2
 *
 * Lookup answer.
 *
 * Step 3
 *
 * Insert prefix.
 *
 * Step 4
 *
 * DFS children.
 *
 * Step 5
 *
 * Remove prefix.
 *
 * ==========================================================
 * MAXIMUM PATH SUM
 * ==========================================================
 *
 * Step 1
 *
 * Compute left gain.
 *
 * Step 2
 *
 * Compute right gain.
 *
 * Step 3
 *
 * Ignore negatives.
 *
 * Step 4
 *
 * Update global answer.
 *
 * Step 5
 *
 * Return one-side gain.
 */

/**
 * **********************************************************************
 * ⚫ EDGE CASE CHECKLIST
 * **********************************************************************
 *
 * Every implementation should mentally verify:
 *
 * □ Empty tree
 *
 * □ Single node
 *
 * □ Only left children
 *
 * □ Only right children
 *
 * □ Negative values
 *
 * □ Target equals root
 *
 * □ Multiple identical values
 *
 * □ Large depth
 *
 * □ Large positive sums
 *
 * □ Large negative sums
 *
 * □ Duplicate prefix sums
 *
 * □ Multiple valid paths
 *
 * □ Root itself is leaf
 *
 * □ All negative tree (Problem 124)
 *
 * □ Prefix beginning at root
 *
 * □ Deep decimal accumulation
 */

/**
 * **********************************************************************
 * ⚫ INVARIANT REFERENCE TABLE
 * **********************************************************************
 *
 * This is the shortest possible summary of every core invariant.
 *
 * ------------------------------------------------------------------------
 * Problem
 * ------------------------------------------------------------------------
 *
 * Path Sum
 *
 * Invariant
 *
 * remainingSum always equals
 * target minus every node already visited.
 *
 * ------------------------------------------------------------------------
 *
 * Path Sum II
 *
 * Invariant
 *
 * currentPath always equals
 * the exact recursion stack.
 *
 * ------------------------------------------------------------------------
 *
 * Path Sum III
 *
 * Invariant
 *
 * prefixFrequency contains prefixes belonging only
 * to the active recursion path.
 *
 * ------------------------------------------------------------------------
 *
 * Sum Root To Leaf Numbers
 *
 * Invariant
 *
 * currentNumber always equals the decimal number
 * represented by the current path.
 *
 * ------------------------------------------------------------------------
 *
 * Maximum Path Sum
 *
 * Invariant
 *
 * gain(node) is the best downward path beginning
 * exactly at node.
 *
 * globalMaximum is the best complete path anywhere.
 */

/**
 * **********************************************************************
 * ⚫ STATE TRANSITION LIBRARY
 * **********************************************************************
 *
 * These five transition formulas are worth memorizing.
 *
 * ------------------------------------------------------------------------
 *
 * Remaining Sum
 *
 * remaining -= node.val
 *
 * ------------------------------------------------------------------------
 *
 * Prefix Sum
 *
 * prefix += node.val
 *
 * ------------------------------------------------------------------------
 *
 * Decimal Number
 *
 * number = number * 10 + node.val
 *
 * ------------------------------------------------------------------------
 *
 * Tree Gain
 *
 * gain =
 * node.val +
 * max(leftGain,rightGain)
 *
 * ------------------------------------------------------------------------
 *
 * Candidate Path
 *
 * node.val +
 * leftGain +
 * rightGain
 *
 * ------------------------------------------------------------------------
 *
 * These five transitions solve an enormous percentage
 * of binary-tree DFS interview questions.
 */

/**
 * **********************************************************************
 * ⚫ INTERVIEW NARRATION TEMPLATE
 * **********************************************************************
 *
 * If asked to explain while coding,
 * speak in terms of invariants rather than syntax.
 *
 * Example:
 *
 * "Each recursive call represents one root-to-current path.
 *
 * I update my state immediately after entering the node so
 * children inherit a correct path state.
 *
 * If the state is mutable, I restore it before returning.
 *
 * In Maximum Path Sum, the returned value is only the best
 * contribution upward, while the global answer considers
 * paths that terminate at the current node."
 *
 * This explanation communicates understanding rather than
 * memorization.
 */

/**
 * **********************************************************************
 * ⚫ IMPLEMENTATION MUSCLE MEMORY
 * **********************************************************************
 *
 * Read this until it becomes automatic.
 *
 * ------------------------------------------------------------
 *
 * Enter node.
 *
 * Update state.
 *
 * Evaluate completion.
 *
 * Explore children.
 *
 * Restore mutable state.
 *
 * Return contribution.
 *
 * ------------------------------------------------------------
 *
 * Every solution in this chapter can be reconstructed
 * from these six actions.
 */

/**
 * **********************************************************************
 * ⚫ MEMORY ANCHORS
 * **********************************************************************
 *
 * Problem 112
 *
 * "Carry Remaining."
 *
 * ------------------------------------------------------------
 *
 * Problem 113
 *
 * "Carry Remaining + Backpack."
 *
 * ------------------------------------------------------------
 *
 * Problem 437
 *
 * "Carry Prefix History."
 *
 * ------------------------------------------------------------
 *
 * Problem 129
 *
 * "Carry Decimal."
 *
 * ------------------------------------------------------------
 *
 * Problem 124
 *
 * "Return Gain."
 *
 * ------------------------------------------------------------
 *
 * Those five anchors are usually enough to reconstruct
 * the complete implementations.
 */

/**
 * **********************************************************************
 * 🧪 SELF-VERIFYING TESTS
 * **********************************************************************
 *
 * The remaining section contains:
 *
 * • Helper tree builders
 * • Assertion utilities
 * • Representative interview tests
 * • Boundary-condition tests
 * • main()
 *
 * Every assertion exists to verify one specific invariant.
 */

// ==============================================================
// TEST UTILITIES
// ==============================================================

private static TreeNode n(int value) {
    return new TreeNode(value);
}

    private static TreeNode n(int value, TreeNode left, TreeNode right) {
        return new TreeNode(value, left, right);
    }

    /**
     * Representative tree used by multiple tests.
     *
     *               5
     *             /   \
     *            4     8
     *           /     / \
     *         11    13   4
     *        /  \        / \
     *       7    2      5   1
     */
    private static TreeNode sampleTree() {

        return n(
                5,
                n(
                        4,
                        n(
                                11,
                                n(7),
                                n(2)),
                        null),
                n(
                        8,
                        n(13),
                        n(
                                4,
                                n(5),
                                n(1))));
    }

    /**
     * Tree used for Prefix Sum example.
     *
     *          10
     *         /  \
     *        5   -3
     *       / \    \
     *      3   2    11
     *     / \   \
     *    3 -2    1
     */
    private static TreeNode prefixTree() {

        return n(
                10,
                n(
                        5,
                        n(
                                3,
                                n(3),
                                n(-2)),
                        n(
                                2,
                                null,
                                n(1))),
                n(
                        -3,
                        null,
                        n(11)));
    }

    public static void main(String[] args) {

        // Enable with:
        //
        // java -ea BinaryTreePathProblems

        TreeNode root = sampleTree();

        // ==========================================================
        // Path Sum
        // ==========================================================

        // Classic positive example.
        assert PathSum.hasPathSum(root, 22);

        // Impossible target.
        assert !PathSum.hasPathSum(root, 100);

        // Empty tree.
        assert !PathSum.hasPathSum(null, 0);

        // Single node success.
        assert PathSum.hasPathSum(n(5), 5);

        // Single node failure.
        assert !PathSum.hasPathSum(n(5), 4);

        // ==========================================================
        // Path Sum II
        // ==========================================================

        List<List<Integer>> paths =
                PathSumII.pathSum(root, 22);

        // Two valid root-to-leaf paths.
        assert paths.size() == 2;

        assert paths.contains(
                Arrays.asList(5, 4, 11, 2));

        assert paths.contains(
                Arrays.asList(5, 8, 4, 5));

        // ==========================================================
        // Path Sum III
        // ==========================================================

        TreeNode prefixExample = prefixTree();

        assert PathSumIIIBruteForce.pathSum(
                prefixExample,
                8) == 3;

        assert PathSumIIIPrefixSum.pathSum(
                prefixExample,
                8) == 3;

        // Brute and optimal should always agree.
        assert PathSumIIIBruteForce.pathSum(
                prefixExample,
                18)
                ==
                PathSumIIIPrefixSum.pathSum(
                        prefixExample,
                        18);

        // ==========================================================
        // Sum Root To Leaf Numbers
        // ==========================================================

        TreeNode digits =
                n(
                        1,
                        n(2),
                        n(3));

        // 12 + 13
        assert SumRootToLeafNumbers.sumNumbers(digits) == 25;

        TreeNode digits2 =
                n(
                        4,
                        n(
                                9,
                                n(5),
                                n(1)),
                        n(0));

        // 495 + 491 + 40
        assert SumRootToLeafNumbers.sumNumbers(digits2) == 1026;

        // ==========================================================
        // Maximum Path Sum
        // ==========================================================

        BinaryTreeMaximumPathSum solver =
                new BinaryTreeMaximumPathSum();

        TreeNode tree1 =
                n(
                        1,
                        n(2),
                        n(3));

        // 2 -> 1 -> 3
        assert solver.maxPathSum(tree1) == 6;

        TreeNode tree2 =
                n(
                        -10,
                        n(9),
                        n(
                                20,
                                n(15),
                                n(7)));

        // 15 -> 20 -> 7
        assert solver.maxPathSum(tree2) == 42;

        // All negative values.
        TreeNode negatives =
                n(
                        -3,
                        n(-5),
                        n(-2));

        // Best single node.
        assert solver.maxPathSum(negatives) == -2;

        // ==========================================================
        // Boundary Conditions
        // ==========================================================

        TreeNode singleZero = n(0);

        assert PathSum.hasPathSum(singleZero, 0);

        assert SumRootToLeafNumbers.sumNumbers(singleZero) == 0;

        assert solver.maxPathSum(singleZero) == 0;

        // Prefix Sum empty tree.
        assert PathSumIIIPrefixSum.pathSum(null, 5) == 0;

        // Enumeration on empty tree.
        assert PathSumII.pathSum(null, 10).isEmpty();

        System.out.println("All assertions passed.");

        // ==========================================================
        // Consistency Checks
        // ==========================================================

        // Prefix Sum and Brute Force must agree on multiple targets.
        int[] targets = {-10, -3, 0, 3, 5, 8, 10, 18, 21, 100};

        for (int target : targets) {

            assert PathSumIIIBruteForce.pathSum(prefixExample, target)
                    == PathSumIIIPrefixSum.pathSum(prefixExample, target);
        }

        // Deep left chain.
        TreeNode chain =
                n(
                        1,
                        n(
                                2,
                                n(
                                        3,
                                        n(4),
                                        null),
                                null),
                        null);

        assert PathSum.hasPathSum(chain, 10);

        // Decimal accumulation:
        // 1234
        assert SumRootToLeafNumbers.sumNumbers(chain) == 1234;

        System.out.println("BinaryTreePathProblems chapter verified successfully.");
    }

    /**
     * **********************************************************************
     * FINAL RECALL
     * **********************************************************************
     *
     * If you forget every implementation,
     * reconstruct them using only these questions.
     *
     * ----------------------------------------------------------------------
     *
     * 1.
     *
     * What does one DFS call represent?
     *
     * Answer:
     *
     * One root-to-current path
     * or
     * one subtree (postorder problems).
     *
     * ----------------------------------------------------------------------
     *
     * 2.
     *
     * What information must children inherit?
     *
     * Remaining Sum?
     *
     * Prefix Sum?
     *
     * Decimal Number?
     *
     * Current Path?
     *
     * ----------------------------------------------------------------------
     *
     * 3.
     *
     * What information must parent receive?
     *
     * Boolean?
     *
     * Count?
     *
     * Maximum Gain?
     *
     * ----------------------------------------------------------------------
     *
     * 4.
     *
     * Is state mutable?
     *
     * If yes,
     * restore it.
     *
     * ----------------------------------------------------------------------
     *
     * 5.
     *
     * Does the answer belong
     * to this node
     * or
     * to the parent?
     *
     * ----------------------------------------------------------------------
     *
     * Answering these five questions is enough to derive every
     * implementation in this chapter from scratch.
     *
     * **********************************************************************
     * CHAPTER SUMMARY
     * **********************************************************************
     *
     * Master Pattern
     * --------------
     *
     * DFS Path Aggregation
     *
     * Core States
     * -----------
     *
     * • Remaining Sum
     * • Current Path
     * • Prefix Sum
     * • Decimal Number
     * • Maximum Gain
     *
     * Core Operations
     * ---------------
     *
     * • Update state
     * • Evaluate completion
     * • Recurse
     * • Restore mutable state
     * • Return contribution
     *
     * Transfer Learning
     * -----------------
     *
     * This single pattern generalizes to dozens of binary-tree interview
     * problems by changing only:
     *
     * • State
     * • Transition
     * • Completion condition
     * • Return semantics
     * • Answer update
     *
     * The traversal skeleton remains fundamentally unchanged.
     */

}