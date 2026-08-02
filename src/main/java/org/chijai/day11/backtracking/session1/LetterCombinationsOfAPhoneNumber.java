package org.chijai.day11.backtracking.session1;

import java.util.ArrayList;
import java.util.List;

/**
 * Letter Combinations of a Phone Number
 *
 * ============================================================
 * 📘 PRIMARY PROBLEM
 * ============================================================
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * Backtracking
 * DFS
 * Recursion
 * String
 * Combinatorics
 *
 * LeetCode:
 * https://leetcode.com/problems/letter-combinations-of-a-phone-number/
 *
 * ------------------------------------------------------------
 * Problem
 * ------------------------------------------------------------
 *
 * Given a string consisting of digits from '2' to '9',
 * return every possible letter combination represented by
 * those digits using the classic telephone keypad.
 *
 * Mapping:
 *
 * 2 -> abc
 * 3 -> def
 * 4 -> ghi
 * 5 -> jkl
 * 6 -> mno
 * 7 -> pqrs
 * 8 -> tuv
 * 9 -> wxyz
 *
 * Return the answer in any order.
 *
 * ------------------------------------------------------------
 * Constraints
 * ------------------------------------------------------------
 *
 * 0 <= digits.length <= 4
 * digits[i] ∈ ['2','9']
 *
 * ------------------------------------------------------------
 * Examples
 * ------------------------------------------------------------
 *
 * Example 1
 *
 * digits = "23"
 *
 * Output
 *
 * [
 *   "ad","ae","af",
 *   "bd","be","bf",
 *   "cd","ce","cf"
 * ]
 *
 * ------------------------------------------------------------
 *
 * Example 2
 *
 * digits = ""
 *
 * Output
 *
 * []
 *
 * ------------------------------------------------------------
 *
 * Example 3
 *
 * digits = "2"
 *
 * Output
 *
 * ["a","b","c"]
 *
 * ============================================================
 * 🔵 CORE PATTERN OVERVIEW
 * ============================================================
 *
 * Pattern
 * -------
 * Backtracking (Depth First Enumeration)
 *
 * Archetype
 * ---------
 * "Choose -> Explore -> Unchoose"
 *
 * Every level fixes one decision.
 * Every recursive call moves to the next decision.
 *
 * ------------------------------------------------------------
 * Core Invariant
 * ------------------------------------------------------------
 *
 * Before dfs(index):
 *
 * current.length() == index
 *
 * and
 *
 * current already represents a valid prefix for
 * digits[0 ... index-1].
 *
 * Nothing inside current is invalid.
 *
 * ------------------------------------------------------------
 * Why It Works
 * ------------------------------------------------------------
 *
 * Every digit independently contributes one character.
 *
 * Therefore,
 *
 * Search Space =
 *
 * choices(digit1)
 * ×
 * choices(digit2)
 * ×
 * ...
 *
 * DFS simply walks this decision tree.
 *
 * Every root-to-leaf path corresponds to exactly one answer.
 *
 * ------------------------------------------------------------
 * Recognition Signals
 * ------------------------------------------------------------
 *
 * Use this pattern when:
 *
 * • Need all possible combinations
 * • Every position has several choices
 * • Decisions are independent
 * • Output size dominates runtime
 * • Enumeration is required instead of optimization
 *
 * ------------------------------------------------------------
 * When NOT to Use
 * ------------------------------------------------------------
 *
 * Do NOT use backtracking when:
 *
 * • Only one optimal answer is needed
 * • Greedy invariant exists
 * • DP has overlapping states
 * • Binary search can discard half
 *
 * ------------------------------------------------------------
 * Comparison
 * ------------------------------------------------------------
 *
 * Subsets
 * --------
 * Choose or skip.
 *
 * Permutations
 * ------------
 * Choose unused element.
 *
 * Combination Sum
 * ---------------
 * Choose candidate while tracking remaining target.
 *
 * Phone Number
 * ------------
 * Exactly one character chosen for each digit.
 * No skipping.
 * No pruning.
 * Simply enumerate.
 *
 * ============================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================
 *
 * Imagine walking level by level through a tree.
 *
 * digits = "279"
 *
 *                 ""
 *            /   |   \
 *          a     b     c
 *         /|\   /|\   /|\
 *        p q r s ...
 *
 * Every tree level corresponds to one digit.
 *
 * Every outgoing edge corresponds to choosing one letter.
 *
 * Leaf = complete answer.
 *
 * ------------------------------------------------------------
 * State
 * ------------------------------------------------------------
 *
 * index
 *
 * Meaning:
 *
 * First undecided digit.
 *
 * current
 *
 * Meaning:
 *
 * Prefix already constructed.
 *
 * result
 *
 * Meaning:
 *
 * Completed combinations.
 *
 * ------------------------------------------------------------
 * Invariant 1
 * ------------------------------------------------------------
 *
 * current.length() == index
 *
 * Therefore,
 *
 * index alone determines exactly how much work
 * has already been completed.
 *
 * ------------------------------------------------------------
 * Invariant 2
 * ------------------------------------------------------------
 *
 * current is always valid.
 *
 * There is never an illegal prefix.
 *
 * Therefore recursion never needs repair.
 *
 * ------------------------------------------------------------
 * Invariant 3
 * ------------------------------------------------------------
 *
 * Characters beyond index are completely undecided.
 *
 * DFS only extends.
 *
 * It never edits earlier choices except while
 * backtracking.
 *
 * ------------------------------------------------------------
 * Invariant 4
 * ------------------------------------------------------------
 *
 * Every recursive call owns exactly one additional
 * character compared to its parent.
 *
 * Parent:
 *
 * "ad"
 *
 * Child:
 *
 * "adg"
 *
 * ------------------------------------------------------------
 * Invariant 5
 * ------------------------------------------------------------
 *
 * Returning from recursion restores the parent state
 * exactly.
 *
 * append(...)
 *
 * recurse(...)
 *
 * deleteLast()
 *
 * State restoration is perfect.
 *
 * ------------------------------------------------------------
 * Variable Meaning
 * ------------------------------------------------------------
 *
 * digits
 *     Original input.
 *
 * index
 *     Current digit being processed.
 *
 * options
 *     Letters mapped from current digit.
 *
 * current
 *     Current answer prefix.
 *
 * result
 *     All finished combinations.
 *
 * ------------------------------------------------------------
 * Allowed Moves
 * ------------------------------------------------------------
 *
 * ✔ Pick one mapped letter.
 *
 * ✔ Advance index.
 *
 * ✔ Undo exactly one character.
 *
 * ------------------------------------------------------------
 * Forbidden Moves
 * ------------------------------------------------------------
 *
 * ✘ Skip a digit.
 *
 * ✘ Append two letters for one digit.
 *
 * ✘ Forget to remove appended letter.
 *
 * ✘ Advance without choosing.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * Stop when
 *
 * index == digits.length()
 *
 * Every digit has contributed exactly one letter.
 *
 * Current prefix is complete.
 *
 * ------------------------------------------------------------
 * Why Naive Thinking Fails
 * ------------------------------------------------------------
 *
 * Beginners often think:
 *
 * "Generate every string then filter."
 *
 * That creates invalid strings.
 *
 * Backtracking instead constructs only valid prefixes,
 * so every recursive state remains useful.
 *
 * ============================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================
 *
 * Mistake 1
 * ---------
 * Forgetting deleteCharAt().
 *
 * Why it seems correct:
 *
 * Characters keep getting appended.
 *
 * Why wrong:
 *
 * Parent state is destroyed.
 *
 * Violated Invariant:
 *
 * Parent must be restored before exploring
 * the next sibling.
 *
 * Counterexample:
 *
 * digits = "23"
 *
 * Expected:
 *
 * ad
 * ae
 * af
 *
 * Wrong traversal:
 *
 * ad
 * ade
 * adef
 *
 * ------------------------------------------------------------
 * Mistake 2
 * ---------
 * Using index + 1 outside recursion.
 *
 * Causes repeated processing of one digit.
 *
 * ------------------------------------------------------------
 * Mistake 3
 * ---------
 * Using digits[index].
 *
 * Java String is NOT an array.
 *
 * Correct:
 *
 * digits.charAt(index)
 *
 * ------------------------------------------------------------
 * Mistake 4
 * ---------
 * Mapping using character directly.
 *
 * Wrong:
 *
 * keypad[digits.charAt(index)]
 *
 * Correct:
 *
 * keypad[digits.charAt(index) - '0']
 *
 * ------------------------------------------------------------
 * Mistake 5
 * ---------
 * Returning empty string instead of empty list
 * for empty input.
 *
 * The problem asks for zero combinations,
 * not one empty combination.
 *
 * ============================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================
 *
 * Typing Order
 * ------------
 *
 * 1. Handle empty input.
 *
 * 2. Create answer list.
 *
 * 3. Create reusable StringBuilder.
 *
 * 4. Call dfs(builder, 0).
 *
 * 5. In dfs:
 *
 *      if finished:
 *          save answer
 *
 *      lookup letters
 *
 *      for every letter
 *          choose
 *          recurse
 *          unchoose
 *
 * 6. Return answer.
 *
 * ------------------------------------------------------------
 * Function Skeleton
 * ------------------------------------------------------------
 *
 * letterCombinations(...)
 *
 * dfs(builder, index)
 *
 * ------------------------------------------------------------
 * Initialization
 * ------------------------------------------------------------
 *
 * result = []
 *
 * builder = ""
 *
 * ------------------------------------------------------------
 * Loop Skeleton
 * ------------------------------------------------------------
 *
 * for every mapped letter
 *
 *      append
 *
 *      recurse
 *
 *      delete last
 *
 * ------------------------------------------------------------
 * Transition
 * ------------------------------------------------------------
 *
 * index
 *
 * becomes
 *
 * index + 1
 *
 * ------------------------------------------------------------
 * Return
 * ------------------------------------------------------------
 *
 * Base case stores one finished combination.
 *
 * ============================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================
 *
 * if empty
 *      return []
 *
 * dfs(index)
 *
 * if finished
 *      save
 *
 * for every letter
 *      choose
 *      dfs(next)
 *      unchoose
 *
 * return answer
 *
 * ============================================================
 * 6. SOLUTION CLASSES
 * ============================================================
 */

/**
 * Top-level public class exactly matching the problem title.
 */
public class LetterCombinationsOfAPhoneNumber {

    private static final String[] KEYPAD = {
            "0",
            "1",
            "abc",
            "def",
            "ghi",
            "jkl",
            "mno",
            "pqrs",
            "tuv",
            "wxyz"
    };

    /**
     * ============================================================
     * Brute Force
     * ============================================================
     *
     * Idea
     * ----
     * Construct the complete decision tree recursively.
     *
     * Since the constraints are tiny, this is already practical.
     *
     * Invariant
     * ---------
     * Every recursive level fixes exactly one digit.
     *
     * Limitation
     * ----------
     * Still explores every valid combination.
     *
     * Complexity
     * ----------
     * Time:
     * O(n * 4^n)
     *
     * Space:
     * O(n)
     *
     * Interview Usefulness
     * --------------------
     * Good first solution.
     * Naturally leads to the optimal discussion because
     * output itself has size Θ(4^n).
     */
    static class BruteForce {

        public List<String> letterCombinations(String digits) {

            List<String> answer = new ArrayList<>();

            if (digits == null || digits.isEmpty()) {
                return answer;
            }

            dfs(
                    digits,
                    0,
                    new StringBuilder(),
                    answer
            );

            return answer;
        }

        private void dfs(
                String digits,
                int index,
                StringBuilder current,
                List<String> answer
        ) {

            // 🟢 Invariant:
            // current represents exactly the first 'index' digits.

            if (index == digits.length()) {
                answer.add(current.toString());
                return;
            }

            String options = KEYPAD[digits.charAt(index) - '0'];

            for (char letter : options.toCharArray()) {

                // Choose one letter for the current digit.
                current.append(letter);

                dfs(
                        digits,
                        index + 1,
                        current,
                        answer
                );

                // Restore parent state before exploring sibling.
                current.deleteCharAt(current.length() - 1);
            }
        }
    }    /**
     * ============================================================
     * Improved
     * ============================================================
     *
     * Idea
     * ----
     * The brute-force algorithm is already asymptotically optimal
     * because every valid combination must be produced.
     *
     * The only meaningful improvements are implementation-level:
     *
     * • Reuse one StringBuilder.
     * • Avoid repeated object creation.
     * • Keep keypad static.
     * • Restore state deterministically.
     *
     * Invariant
     * ---------
     * current.length() == index
     *
     * Improvement
     * -----------
     * Reduces temporary allocations while preserving the exact
     * same search space.
     *
     * Complexity
     * ----------
     * Time:
     * O(n × 4^n)
     *
     * Space:
     * O(n)
     *
     * (excluding output)
     *
     * Interview Usefulness
     * --------------------
     * Demonstrates awareness that asymptotic complexity cannot
     * improve once output size dominates.
     */
    static class Improved {

        public List<String> letterCombinations(String digits) {

            List<String> result = new ArrayList<>();

            if (digits == null || digits.isEmpty()) {
                return result;
            }

            StringBuilder current = new StringBuilder();

            dfs(current, 0, digits, result);

            return result;
        }

        private void dfs(
                StringBuilder current,
                int index,
                String digits,
                List<String> result
        ) {

            // 🟢 Every recursive frame owns one prefix only.

            if (index == digits.length()) {
                result.add(current.toString());
                return;
            }

            String letters = KEYPAD[digits.charAt(index) - '0'];

            for (int i = 0; i < letters.length(); i++) {

                current.append(letters.charAt(i));

                dfs(
                        current,
                        index + 1,
                        digits,
                        result
                );

                // 🟢 Restore invariant before next sibling.
                current.deleteCharAt(current.length() - 1);
            }
        }
    }

    /**
     * ============================================================
     * Optimal (Interview Preferred)
     * ============================================================
     *
     * Idea
     * ----
     * Perform a depth-first traversal over the implicit decision
     * tree.
     *
     * Each recursion level decides exactly one digit.
     *
     * There is no pruning because every leaf is a required answer.
     *
     * Why It Is Optimal
     * -----------------
     * Let
     *
     * L = number of generated combinations.
     *
     * Any algorithm must output every one of those strings.
     *
     * Therefore
     *
     * Ω(L)
     *
     * work is unavoidable.
     *
     * Since every generated string has length n,
     *
     * Time = Θ(n × L)
     *
     * which is optimal.
     *
     * Core Invariant
     * --------------
     * Before entering dfs(index):
     *
     * current contains exactly one chosen letter for each digit
     * before index.
     *
     * Digits from index onward remain undecided.
     *
     * Correctness
     * -----------
     * Every path:
     *
     * root
     *   →
     * level 1
     *   →
     * level 2
     *   →
     * ...
     *   →
     * leaf
     *
     * chooses exactly one character from every keypad entry.
     *
     * Thus every leaf corresponds to exactly one legal answer.
     *
     * No answer is skipped.
     *
     * No answer is duplicated.
     *
     * Complexity
     * ----------
     * Time:
     * O(n × 4^n)
     *
     * Space:
     * O(n)
     *
     * excluding output.
     *
     * Interview Usefulness
     * --------------------
     * This is the expected solution.
     */
    static class Optimal {

        public List<String> letterCombinations(String digits) {

            List<String> result = new ArrayList<>();

            // 🟢 Empty input has zero combinations.
            if (digits == null || digits.isEmpty()) {
                return result;
            }

            StringBuilder current = new StringBuilder();

            dfs(
                    current,
                    0,
                    digits,
                    result
            );

            return result;
        }

        private void dfs(
                StringBuilder current,
                int index,
                String digits,
                List<String> result
        ) {

            // 🟢 Invariant:
            // current stores one chosen character for every digit
            // before 'index'.

            if (index == digits.length()) {

                // Every digit has contributed exactly one letter.
                result.add(current.toString());
                return;
            }

            String options = KEYPAD[digits.charAt(index) - '0'];

            for (int i = 0; i < options.length(); i++) {

                char choice = options.charAt(i);

                // Choose.
                current.append(choice);

                // Explore remaining undecided digits.
                dfs(
                        current,
                        index + 1,
                        digits,
                        result
                );

                // Restore parent state exactly.
                current.deleteCharAt(current.length() - 1);
            }
        }
    }

/**
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Explain Verbally
 * ----------------
 *
 * Pattern
 * -------
 * Backtracking over a decision tree.
 *
 * Every recursion level represents exactly one digit.
 *
 * Every outgoing edge represents choosing one letter.
 *
 * ------------------------------------------------------------
 * Invariant
 * ------------------------------------------------------------
 *
 * Before dfs(index),
 *
 * current contains one valid letter for every processed digit.
 *
 * Therefore
 *
 * current.length() == index.
 *
 * ------------------------------------------------------------
 * Search Space
 * ------------------------------------------------------------
 *
 * Each digit contributes:
 *
 * 3 or 4 choices.
 *
 * Total search space:
 *
 * Cartesian product
 *
 * of all keypad mappings.
 *
 * ------------------------------------------------------------
 * Discard Rule
 * ------------------------------------------------------------
 *
 * None.
 *
 * Unlike binary search or pruning problems,
 * every branch corresponds to a required answer.
 *
 * Therefore every branch must be explored.
 *
 * ------------------------------------------------------------
 * Correctness
 * ------------------------------------------------------------
 *
 * We choose exactly one letter for every digit.
 *
 * Once every digit has been processed,
 * the constructed string is complete and valid.
 *
 * Because every possible choice is explored exactly once,
 * every valid combination appears exactly once.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * Stop when
 *
 * index == digits.length().
 *
 * Every decision has been made.
 *
 * ------------------------------------------------------------
 * In-place Feasibility
 * ------------------------------------------------------------
 *
 * Yes.
 *
 * The mutable StringBuilder is reused during DFS.
 *
 * ------------------------------------------------------------
 * Streaming Feasibility
 * ------------------------------------------------------------
 *
 * Yes.
 *
 * Instead of storing answers,
 * each completed string could be emitted immediately.
 *
 * ------------------------------------------------------------
 * When NOT To Use
 * ------------------------------------------------------------
 *
 * Do not use this pattern when:
 *
 * • only one optimal solution is required,
 * • overlapping subproblems exist,
 * • pruning eliminates large portions of the search space,
 * • greedy decisions are sufficient.
 *
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 * Generate every keypad combination.
 *
 * Pattern
 * -------
 * Backtracking.
 *
 * Invariant
 * ---------
 * current.length() == index.
 *
 * Search Target
 * -------------
 * Complete every root-to-leaf path.
 *
 * Discard Rule
 * ------------
 * None.
 *
 * Every branch is valid.
 *
 * Common Trap
 * -----------
 * Forgetting to delete the appended character.
 *
 * Edge Cases
 * ----------
 * • empty input
 * • one digit
 * • digit '7'
 * • digit '9'
 * • repeated digits
 *
 * One-liner
 * ---------
 * One recursion level decides one digit.
 *
 * Re-derivation Cue
 * -----------------
 * Choose →
 * Explore →
 * Unchoose.
 *
 * Restore parent state after every recursive call.
 *
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * Variation 1
 * -----------
 * Return only the count.
 *
 * Reasoning
 * ---------
 * Replace answer insertion with integer accumulation.
 *
 * Invariant remains unchanged.
 *
 * ------------------------------------------------------------
 * Variation 2
 * -----------
 * Stream answers to a callback.
 *
 * Reasoning
 * ---------
 * Emit completed strings instead of storing them.
 *
 * Search space is identical.
 *
 * ------------------------------------------------------------
 * Variation 3
 * -----------
 * Iterative BFS.
 *
 * Reasoning
 * ---------
 * Expand prefixes level by level using a queue.
 *
 * Pattern changes,
 * invariant becomes:
 *
 * Every queued string has equal length.     *
 *      * Why It Still Works
 *      * ------------------
 *      * Every expansion appends exactly one valid letter to every
 *      * existing prefix, so the queue always contains only valid
 *      * partial combinations of the same depth.
 *      *
 *      * ------------------------------------------------------------
 *      * Variation 4
 *      * -----------
 *      * Replace StringBuilder with char[].
 *      *
 *      * Reasoning
 *      * ---------
 *      * Allocate a fixed-size array of length digits.length().
 *      *
 *      * At recursion level index:
 *      *
 *      *     current[index] = chosenLetter
 *      *
 *      * No deletion is necessary because the next assignment
 *      * overwrites the previous value.
 *      *
 *      * Why It Still Works
 *      * ------------------
 *      * The invariant becomes:
 *      *
 *      * current[0...index-1]
 *      * are finalized.
 *      *
 *      * current[index...]
 *      * are don't-care values.
 *      *
 *      * ------------------------------------------------------------
 *      * Variation 5
 *      * -----------
 *      * Allow wildcard digits.
 *      *
 *      * Example:
 *      *
 *      * '?'
 *      *
 *      * may represent any keypad digit.
 *      *
 *      * Pattern Change
 *      * --------------
 *      * Every recursion level first expands possible digits,
 *      * then expands letters for each chosen digit.
 *      *
 *      * Search space increases dramatically.
 *      *
 *      * ------------------------------------------------------------
 *      * Pattern Boundary
 *      * ----------------
 *      *
 *      * This pattern breaks when:
 *      *
 *      * • states overlap (prefer DP),
 *      * • decisions depend on future optimization,
 *      * • pruning becomes the dominant operation,
 *      * • only existence rather than enumeration is required.
 *      *
 *      * ============================================================
 *      * 🧠 MASTERY CHECKLIST
 *      * ============================================================
 *      *
 *      * □ What is the invariant?
 *      *
 *      * current contains exactly one chosen letter for every
 *      * processed digit.
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * □ What is the search target?
 *      *
 *      * Every complete root-to-leaf path.
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * □ What is discarded?
 *      *
 *      * Nothing.
 *      *
 *      * Every branch corresponds to one required answer.
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * □ Why does recursion terminate?
 *      *
 *      * index eventually reaches digits.length().
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * □ Why does the naive implementation fail?
 *      *
 *      * Failure to restore state corrupts sibling branches.
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * □ Which edge cases matter?
 *      *
 *      * • ""
 *      * • one digit
 *      * • repeated digits
 *      * • all digits having four letters
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * □ Debugging readiness
 *      *
 *      * Verify after every recursive return:
 *      *
 *      * current.length() == index
 *      *
 *      * If false,
 *      * backtracking restoration is broken.
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * □ Variant readiness
 *      *
 *      * Can implement:
 *      *
 *      * • DFS
 *      * • BFS
 *      * • char[]
 *      * • callback streaming
 *      *
 *      * by preserving the same invariant.
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * □ Pattern Boundary
 *      *
 *      * Enumeration problem:
 *      * Yes.
 *      *
 *      * Optimization problem:
 *      * No.
 *      *
 *      * ============================================================
 *      * Additional Interview Notes
 *      * ============================================================
 *      *
 *      * Frequently Asked Follow-up
 *      * --------------------------
 *      *
 *      * Q:
 *      * Why use StringBuilder instead of String?
 *      *
 *      * A:
 *      * String is immutable.
 *      *
 *      * Every concatenation creates a new object.
 *      *
 *      * StringBuilder allows constant-time append and delete,
 *      * making the recursive implementation cleaner and reducing
 *      * temporary allocations.
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * Q:
 *      * Why subtract '0'?
 *      *
 *      * A:
 *      * digits.charAt(index) returns a character.
 *      *
 *      * Example:
 *      *
 *      * '7'
 *      *
 *      * ASCII value:
 *      *
 *      * 55
 *      *
 *      * Subtracting '0' converts it to the integer:
 *      *
 *      * 7
 *      *
 *      * allowing direct indexing into KEYPAD.
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * Q:
 *      * Why is there no visited array?
 *      *
 *      * A:
 *      * Each recursion level corresponds to a fixed digit.
 *      *
 *      * Digits are processed strictly from left to right.
 *      *
 *      * Therefore no state can ever be revisited.
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * Q:
 *      * Why isn't this permutation?
 *      *
 *      * A:
 *      * Positions are fixed.
 *      *
 *      * We never rearrange letters.
 *      *
 *      * We only choose one letter for each fixed position.
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * Q:
 *      * Can duplicate answers occur?
 *      *
 *      * A:
 *      * No.
 *      *
 *      * Every recursive path represents one unique sequence of
 *      * choices.
 *      *
 *      * Different paths differ at least at one level.
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * Debugging Checklist
 *      * -------------------
 *      *
 *      * If output is empty:
 *      *
 *      * ✓ Did you return early for empty input only?
 *      *
 *      * ✓ Is the base case correct?
 *      *
 *      * ✓ Is recursion being called?
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * If answers are too long:
 *      *
 *      * ✓ Did deleteCharAt execute?
 *      *
 *      * ✓ Is append paired with delete?
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * If answers are missing:
 *      *
 *      * ✓ Loop over every mapped letter.
 *      *
 *      * ✓ Increment index exactly once.
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * If IndexOutOfBoundsException occurs:
 *      *
 *      * ✓ Use
 *      *
 *      * digits.charAt(index)
 *      *
 *      * not
 *      *
 *      * digits.charAt(index + 1)
 *      *
 *      * before recursion.
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * If wrong keypad mapping:
 *      *
 *      * ✓ Ensure:
 *      *
 *      * KEYPAD[digits.charAt(index) - '0']
 *      *
 *      * not
 *      *
 *      * KEYPAD[index]
 *      *
 *      * ------------------------------------------------------------
 *      *
 *      * Memory Reconstruction
 *      * ---------------------
 *      *
 *      * Think only four lines:
 *      *
 *      * append
 *      *
 *      * recurse(next index)
 *      *
 *      * delete
 *      *
 *      * repeat
 *      *
 *      * Everything else follows from the invariant.
 *      *
 *      * ============================================================
 *      * End of theory.
 *      * Tests follow.
 *      * ============================================================
 *      */
public static void main(String[] args) {

    Optimal solver = new Optimal();

    // --------------------------------------------------------
    // Happy path from the problem statement.
    // --------------------------------------------------------
    List<String> result23 = solver.letterCombinations("23");

    assert result23.size() == 9
            : "2 has 3 letters and 3 has 3 letters -> 3 × 3 = 9.";

    assert result23.contains("ad")
            : "Representative combination must exist.";

    assert result23.contains("cf")
            : "Last representative combination must exist.";

    // --------------------------------------------------------
    // Empty input should produce zero combinations.
    // --------------------------------------------------------
    List<String> empty = solver.letterCombinations("");

    assert empty.isEmpty()
            : "Problem requires an empty list for empty input.";

    // --------------------------------------------------------
    // Single digit.
    // --------------------------------------------------------
    List<String> single = solver.letterCombinations("2");

    assert single.size() == 3
            : "Digit 2 maps to exactly three letters.";

    assert single.contains("a");
    assert single.contains("b");
    assert single.contains("c");

    // --------------------------------------------------------
    // Digit with four mapped letters.
    // --------------------------------------------------------
    List<String> seven = solver.letterCombinations("7");

    assert seven.size() == 4
            : "Digit 7 maps to pqrs.";

    assert seven.contains("p");
    assert seven.contains("q");
    assert seven.contains("r");
    assert seven.contains("s");

    // --------------------------------------------------------
    // Two digits where both have four choices.
    // --------------------------------------------------------
    List<String> seventyNine = solver.letterCombinations("79");

    assert seventyNine.size() == 16
            : "4 × 4 = 16 combinations.";

    assert seventyNine.contains("pw");
    assert seventyNine.contains("sz");

    // --------------------------------------------------------
    // Repeated digit.
    // --------------------------------------------------------
    List<String> repeated = solver.letterCombinations("22");

    assert repeated.size() == 9
            : "3 × 3 = 9 combinations.";

    assert repeated.contains("aa");
    assert repeated.contains("bc");
    assert repeated.contains("cb");
    assert repeated.contains("cc");

    // --------------------------------------------------------
    // Maximum length in constraints.
    // --------------------------------------------------------
    List<String> max = solver.letterCombinations("9999");

    assert max.size() == 256
            : "4^4 = 256 combinations.";

    assert max.contains("wwww");
    assert max.contains("zzzz");

    // --------------------------------------------------------
    // Verify every generated string has correct length.
    // --------------------------------------------------------
    for (String combination : result23) {
        assert combination.length() == 2
                : "Each answer must contain one letter per digit.";
    }

    for (String combination : max) {
        assert combination.length() == 4
                : "Every generated answer must match input length.";
    }

    // --------------------------------------------------------
    // Verify deterministic restoration of StringBuilder state.
    // Running twice should produce identical results.
    // --------------------------------------------------------
    List<String> firstRun = solver.letterCombinations("234");
    List<String> secondRun = solver.letterCombinations("234");

    assert firstRun.equals(secondRun)
            : "Backtracking must restore state perfectly.";

    // --------------------------------------------------------
    // Boundary: all outputs must be unique.
    // --------------------------------------------------------
    assert firstRun.size() == new java.util.HashSet<>(firstRun).size()
            : "Duplicate combinations indicate broken traversal.";

    System.out.println("All assertions passed. Run with -ea to enable assertions.");
}
}


