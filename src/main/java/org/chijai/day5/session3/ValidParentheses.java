package org.chijai.day5.session3;


import java.util.Stack;

public class ValidParentheses {

/*
 * ================================================================
 * 2. 📘 PRIMARY PROBLEM
 * ================================================================
 *
 * Title:
 * Valid Parentheses
 *
 * Difficulty:
 * Easy
 *
 * Tags:
 * Stack
 * String
 *
 * Problem Description
 * -------------------
 * Given a string consisting only of:
 *
 *      (
 *      )
 *      {
 *      }
 *      [
 *      ]
 *
 * determine whether the entire string is a valid parentheses
 * sequence.
 *
 * A string is valid if:
 *
 * 1. Every opening bracket has a matching closing bracket.
 *
 * 2. The matching bracket is of the same type.
 *
 * 3. Brackets close in the correct nesting order.
 *
 * Constraints
 * -----------
 * 1 <= s.length <= 10^4
 * s contains only:
 *
 *      ()
 *      {}
 *      []
 *
 * Representative Examples
 * -----------------------
 *
 * Example 1
 *
 * Input:
 * "()"
 *
 * Output:
 * true
 *
 * -----------------------
 *
 * Example 2
 *
 * Input:
 * "()[]{}"
 *
 * Output:
 * true
 *
 * -----------------------
 *
 * Example 3
 *
 * Input:
 * "(]"
 *
 * Output:
 * false
 *
 * -----------------------
 *
 * Example 4
 *
 * Input:
 * "([)]"
 *
 * Output:
 * false
 *
 * -----------------------
 *
 * Example 5
 *
 * Input:
 * "{[]}"
 *
 * Output:
 * true
 *
 * Official LeetCode
 * -----------------
 * https://leetcode.com/problems/valid-parentheses/
 */

/*
 * ================================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ================================================================
 *
 * Pattern
 * -------
 * Stack Simulation
 *
 * Archetype
 * ---------
 * Last Opened -> First Closed
 *
 * This is the canonical LIFO (Last In First Out) pattern.
 *
 * Core Invariant
 * --------------
 * The stack always stores the brackets that are still waiting to
 * be matched.
 *
 * Even better, we store the EXPECTED closing bracket instead of the
 * opening bracket.
 *
 * Therefore:
 *
 * top of stack
 * =
 * the next legal closing character.
 *
 * Why It Works
 * ------------
 * Parentheses form nested structures.
 *
 * Example
 *
 * ({
 *
 * The '{' must close before '('.
 *
 * The most recently opened bracket must therefore be matched first.
 *
 * That is exactly the property provided by a stack.
 *
 * Recognition Signals
 * -------------------
 * Think Stack whenever you see:
 *
 * • nested structure
 * • balanced symbols
 * • latest item must finish first
 * • parsing expressions
 * • matching begin/end tokens
 * • compiler-like validation
 *
 * When To Use
 * -----------
 * ✓ Parentheses
 * ✓ XML tags
 * ✓ HTML tags
 * ✓ Expression parsing
 * ✓ Undo history
 * ✓ DFS simulation
 *
 * When NOT To Use
 * ---------------
 * If matching does not depend on the latest unmatched element.
 *
 * Example:
 * counting frequencies
 * sorting
 * prefix sums
 * sliding window
 *
 * Comparison With Similar Patterns
 * --------------------------------
 *
 * Queue
 * -----
 * First opened leaves first.
 *
 * Wrong for nested structures.
 *
 * Counter
 * -------
 * Works only for ONE bracket type:
 *
 * (())
 *
 * Cannot distinguish:
 *
 * ([)]
 *
 * because counts remain equal.
 *
 * Stack
 * -----
 * Preserves nesting order.
 * Preserves bracket type.
 * Preserves matching sequence.
 */

/*
 * ================================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ================================================================
 *
 * Mental Model
 * ------------
 * Imagine walking through the string from left to right.
 *
 * Every opening bracket creates unfinished work.
 *
 * Every closing bracket must complete the MOST RECENT unfinished
 * work.
 *
 * The stack is simply the list of unfinished work.
 *
 * Better Representation
 * ---------------------
 * Instead of pushing:
 *
 * '('
 *
 * push:
 *
 * ')'
 *
 * Instead of pushing:
 *
 * '{'
 *
 * push:
 *
 * '}'
 *
 * Instead of pushing:
 *
 * '['
 *
 * push:
 *
 * ']'
 *
 * Now every future closing bracket only needs one comparison.
 *
 * Stack Meaning
 * -------------
 * Stack contains:
 *
 * expected future closing brackets
 *
 * NOT
 *
 * previously seen opening brackets.
 *
 * Primary Invariant
 * -----------------
 * After processing the first i characters,
 * the stack contains exactly the unmatched expected closing brackets
 * in reverse completion order.
 *
 * Variable Meaning
 * ----------------
 * ch
 * Current input symbol.
 *
 * stack
 * Remaining expected closing symbols.
 *
 * stack.peek()
 * The ONLY legal closing bracket at this moment.
 *
 * Allowed State Transitions
 * -------------------------
 *
 * Opening bracket
 *
 * Transition:
 *
 * Push expected closing bracket.
 *
 * -------------------------
 *
 * Closing bracket
 *
 * Transition:
 *
 * Compare with stack top.
 *
 * If equal:
 *
 * remove expectation.
 *
 * Else:
 *
 * invalid immediately.
 *
 * Forbidden States
 * ----------------
 * Closing bracket while stack empty.
 *
 * Means:
 *
 * there is no opening bracket available.
 *
 * -------------------------
 *
 * Closing bracket different from stack top.
 *
 * Means:
 *
 * nesting order has been violated.
 *
 * Example
 *
 * ([)]
 *
 * Expected:
 *
 * ]
 *
 * Received:
 *
 * )
 *
 * Invariant broken.
 *
 * Termination
 * -----------
 * After scanning every character:
 *
 * Valid iff stack becomes empty.
 *
 * Remaining elements indicate unmatched openings.
 *
 * Why Naive Solutions Fail
 * ------------------------
 * Naive Idea:
 *
 * Count each bracket type.
 *
 * Example:
 *
 * ([)]
 *
 * Counts:
 *
 * ( = )
 * [ = ]
 *
 * Yet answer is false.
 *
 * Counts ignore ordering.
 *
 * The Stack invariant simultaneously preserves:
 *
 * • order
 * • nesting
 * • bracket type
 */

/*
 * ================================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * ================================================================
 *
 * Mistake 1
 * ---------
 * Count opening and closing brackets.
 *
 * Looks reasonable because totals match.
 *
 * Violated Invariant
 * ------------------
 * Ordering is lost.
 *
 * Counterexample
 *
 * ([)]
 *
 * Counts succeed.
 * Nesting fails.
 *
 * ------------------------------------------------
 *
 * Mistake 2
 * ---------
 * Push opening brackets and later search inside the stack.
 *
 * Looks flexible.
 *
 * Violated Invariant
 * ------------------
 * Only the most recent opening bracket may close.
 *
 * Searching deeper skips unfinished work.
 *
 * ------------------------------------------------
 *
 * Mistake 3
 * ---------
 * Ignore empty stack checks.
 *
 * Counterexample
 *
 * ")"
 *
 * Causes runtime error instead of returning false.
 *
 * ------------------------------------------------
 *
 * Mistake 4
 * ---------
 * Forget final stack.isEmpty().
 *
 * Counterexample
 *
 * "((("
 *
 * Loop finishes successfully.
 *
 * But unfinished work still exists.
 *
 * ------------------------------------------------
 *
 * Interview Trap
 * --------------
 * Many candidates push opening brackets.
 *
 * Better implementation:
 *
 * Push expected closing brackets.
 *
 * Benefits:
 *
 * • one comparison
 * • simpler logic
 * • fewer mapping mistakes
 * • easier debugging
 */

/*
 * ================================================================
 * ⚙️ IMPLEMENTATION BLUEPRINT
 * ================================================================
 *
 * Mechanical Typing Order
 * -----------------------
 *
 * 1.
 * Create function.
 *
 * 2.
 * Create empty stack.
 *
 * 3.
 * Iterate characters.
 *
 * 4.
 * Opening bracket?
 *
 * Push expected closing bracket.
 *
 * 5.
 * Otherwise:
 *
 * stack empty?
 * -> false
 *
 * 6.
 * Pop expectation.
 *
 * 7.
 * Compare with current character.
 *
 * Different?
 *
 * -> false
 *
 * 8.
 * End loop.
 *
 * 9.
 * Return stack.isEmpty().
 *
 * Deterministic Skeleton
 * ----------------------
 *
 * initialize stack
 *
 * for every character
 *
 *      opening ?
 *          push expected closing
 *
 *      else
 *          validate stack
 *          compare top
 *
 * return stack empty
 */

/*
 * ================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ================================================================
 *
 * create stack
 *
 * for each character
 *
 *     opening -> push expected closing
 *
 *     closing -> validate and pop
 *
 * return stack empty
 */    /*
     * ================================================================
     * 6. SOLUTION CLASSES
     * ================================================================
     */

    /*
     * ------------------------------------------------
     * Brute Force
     * ------------------------------------------------
     *
     * Idea
     * ----
     * Repeatedly locate adjacent matching pairs:
     *
     *      ()
     *      {}
     *      []
     *
     * Remove them until either:
     *
     * • the string becomes empty
     * • no more removals are possible
     *
     * Invariant
     * ---------
     * Every removal preserves the validity of the remaining sequence.
     *
     * Limitation
     * ----------
     * Every removal creates a new string.
     * Characters are repeatedly shifted.
     *
     * Complexity
     * ----------
     * Time:
     * O(n²)
     *
     * Space:
     * O(n)
     *
     * Interview Usefulness
     * --------------------
     * Demonstrates understanding of the nesting property but is not
     * efficient enough for large inputs.
     */
    static class BruteForce {

        static boolean isValid(String s) {

            String current = s;

            boolean changed = true;

            while (changed) {

                changed = false;

                String reduced = current
                        .replace("()", "")
                        .replace("[]", "")
                        .replace("{}", "");

                if (!reduced.equals(current)) {
                    changed = true;
                    current = reduced;
                }
            }

            return current.isEmpty();
        }
    }

    /*
     * ------------------------------------------------
     * Improved
     * ------------------------------------------------
     *
     * Idea
     * ----
     * Push opening brackets.
     *
     * Whenever a closing bracket arrives,
     * compare it with the latest unmatched opening bracket.
     *
     * Invariant
     * ---------
     * The stack always contains unmatched opening brackets.
     *
     * Improvement
     * -----------
     * Eliminates repeated rescanning.
     *
     * Complexity
     * ----------
     * Time:
     * O(n)
     *
     * Space:
     * O(n)
     *
     * Interview Usefulness
     * --------------------
     * Perfectly acceptable interview solution.
     * The optimal solution below is simply a cleaner realization of the
     * same Stack pattern.
     */
    static class Improved {

        static boolean isValid(String s) {

            Stack<Character> stack = new Stack<>();

            for (char ch : s.toCharArray()) {

                if (ch == '(' || ch == '[' || ch == '{') {

                    stack.push(ch);

                    continue;
                }

                if (stack.isEmpty()) {
                    return false;
                }

                char open = stack.pop();

                if (ch == ')' && open != '(') {
                    return false;
                }

                if (ch == ']' && open != '[') {
                    return false;
                }

                if (ch == '}' && open != '{') {
                    return false;
                }
            }

            return stack.isEmpty();
        }
    }

    /*
     * ------------------------------------------------
     * Optimal (Interview Preferred)
     * ------------------------------------------------
     *
     * Idea
     * ----
     * Instead of storing opening brackets,
     * immediately store the closing bracket that will be required later.
     *
     * Example
     *
     * Read '('
     *
     * Push ')'
     *
     * -----------------------
     *
     * Read '['
     *
     * Push ']'
     *
     * -----------------------
     *
     * Stack now represents future expectations rather than history.
     *
     * Invariant
     * ---------
     * The stack contains every unmatched expected closing bracket.
     *
     * The top of the stack is the ONLY valid closing character that may
     * legally appear next.
     *
     * Correctness
     * -----------
     * Opening brackets add one future expectation.
     *
     * Correct closing brackets satisfy exactly one expectation.
     *
     * Wrong closing brackets violate the invariant immediately.
     *
     * Remaining expectations after the scan correspond exactly to
     * unmatched opening brackets.
     *
     * Complexity
     * ----------
     * Time:
     * O(n)
     *
     * Space:
     * O(n)
     *
     * Interview Usefulness
     * --------------------
     * Preferred because:
     *
     * • fewer comparisons
     * • cleaner invariant
     * • fewer mapping bugs
     * • easy to reconstruct from memory
     */
    static class Optimal {

        static boolean isValid(String s) {

            Stack<Character> stack = new Stack<>();

            for (char ch : s.toCharArray()) {

                if (ch == '(') {

                    // 🟢 Invariant:
                    // Record the exact closing bracket now expected.
                    stack.push(')');
                }
                else if (ch == '{') {

                    // 🟢 Future expectation grows by one.
                    stack.push('}');
                }
                else if (ch == '[') {

                    // 🟢 Top always stores the next legal closer.
                    stack.push(']');
                }
                else {

                    // 🔴 No unfinished opening bracket exists.
                    if (stack.isEmpty()) {
                        return false;
                    }

                    char expected = stack.pop();

                    // 🟢 Current closing bracket must satisfy the latest
                    // unfinished expectation.
                    if (expected != ch) {
                        return false;
                    }
                }
            }

            // 🟢 Every expectation must have been satisfied.
            return stack.isEmpty();
        }
    }

/*
 * ================================================================
 * 🟣 INTERVIEW ARTICULATION
 * ================================================================
 *
 * Pattern
 * -------
 * Stack Simulation.
 *
 * Invariant
 * ---------
 * The stack stores every unmatched expected closing bracket.
 *
 * Therefore the top of the stack is always the only valid closing
 * symbol that may legally appear next.
 *
 * Search Space
 * ------------
 * We scan the string exactly once from left to right.
 *
 * Each character changes only the current Stack state.
 *
 * Discard Rule
 * ------------
 * If the current closing bracket does not equal the expected closing
 * bracket on top of the stack, the entire string is already invalid.
 *
 * No future character can repair this mismatch because nesting order
 * has already been violated.
 *
 * Correctness
 * -----------
 * Every opening bracket creates one expectation.
 *
 * Every correct closing bracket removes exactly one expectation.
 *
 * Therefore:
 *
 * Empty stack after the scan
 * ⇔
 * Every expectation has been satisfied exactly once.
 *
 * Termination
 * -----------
 * The loop processes each character once.
 *
 * Afterwards the remaining Stack state completely determines the
 * answer.
 *
 * In-place Feasibility
 * --------------------
 * No.
 *
 * Nested structures require remembering unfinished work.
 *
 * Streaming Feasibility
 * ---------------------
 * Yes.
 *
 * Characters may arrive one at a time while maintaining only the
 * Stack state.
 *
 * When NOT To Use
 * ---------------
 * If relationships are not LIFO.
 *
 * Examples:
 *
 * frequency counting
 * sorting
 * prefix accumulation
 * sliding windows
 */

/*
 * ================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ================================================================
 *
 * Trigger
 * -------
 * Balanced nested symbols.
 *
 * Invariant
 * ---------
 * Stack stores expected closing brackets.
 *
 * Search Target
 * -------------
 * Verify every closer satisfies the latest expectation.
 *
 * Discard Rule
 * ------------
 * Empty stack or wrong closing bracket
 * =>
 * immediately false.
 *
 * Common Trap
 * -----------
 * Counting brackets instead of preserving nesting.
 *
 * Edge Cases
 * ----------
 * ""
 * "("
 * ")"
 * "([)]"
 * "{[]}"
 *
 * One-Liner
 * ---------
 * Push expectations.
 * Pop validations.
 *
 * Re-derivation Cue
 * -----------------
 * Ask:
 *
 * "What closing bracket must legally appear next?"
 */    /*
     * ================================================================
     * 🔄 VARIATIONS & TWEAKS
     * ================================================================
     *
     * Variation 1
     * -----------
     * Multiple Bracket Types
     *
     * Pattern
     * -------
     * Same Stack invariant.
     *
     * Only the mapping changes.
     *
     * -------------------------------
     *
     * Variation 2
     * -----------
     * Only '(' and ')'
     *
     * Pattern
     * -------
     * Stack still works.
     *
     * A simple counter also works because there is only one bracket type
     * and ordering cannot be confused.
     *
     * -------------------------------
     *
     * Variation 3
     * -----------
     * Long Expressions
     *
     * Example:
     *
     * a + (b * (c + d))
     *
     * Ignore non-bracket characters.
     * Maintain the identical Stack invariant.
     *
     * -------------------------------
     *
     * Variation 4
     * -----------
     * HTML / XML Tag Validation
     *
     * Push expected closing tags.
     *
     * Pop and compare as closing tags arrive.
     *
     * Same invariant.
     *
     * -------------------------------
     *
     * Variation 5
     * -----------
     * Minimum Remove to Make Valid
     *
     * Pattern changes.
     *
     * We are no longer validating.
     *
     * We are repairing.
     *
     * Extra bookkeeping becomes necessary.
     *
     * -------------------------------
     *
     * Pattern Boundary
     * ----------------
     * This Stack pattern succeeds whenever unfinished work must be
     * completed in reverse order.
     *
     * It fails whenever matching is not LIFO.
     */

    /*
     * ================================================================
     * 🧠 MASTERY CHECKLIST
     * ================================================================
     *
     * Q. What is the invariant?
     *
     * A.
     * Stack stores every unmatched expected closing bracket.
     *
     * ------------------------------------------------
     *
     * Q. What is the search target?
     *
     * A.
     * Verify every closing bracket matches the current expectation.
     *
     * ------------------------------------------------
     *
     * Q. What is the discard rule?
     *
     * A.
     * Empty stack or mismatched closing bracket immediately proves the
     * sequence cannot become valid.
     *
     * ------------------------------------------------
     *
     * Q. Why does termination work?
     *
     * A.
     * Every character is processed exactly once.
     * Remaining expectations determine the final answer.
     *
     * ------------------------------------------------
     *
     * Q. Why does the naive solution fail?
     *
     * A.
     * Counts preserve quantity but lose nesting order.
     *
     * ------------------------------------------------
     *
     * Q. Which edge cases must always be checked?
     *
     * A.
     * Empty input.
     * Single opening bracket.
     * Single closing bracket.
     * Crossed nesting.
     * Deep nesting.
     *
     * ------------------------------------------------
     *
     * Q. Can you debug quickly?
     *
     * A.
     * Yes.
     *
     * Observe:
     *
     * • current character
     * • expected character
     * • stack contents
     *
     * The first mismatch identifies the failure.
     *
     * ------------------------------------------------
     *
     * Q. Are you ready for variants?
     *
     * A.
     * Yes.
     *
     * Preserve the Stack invariant.
     * Change only the mapping or token representation.
     *
     * ------------------------------------------------
     *
     * Q. Where does this pattern stop applying?
     *
     * A.
     * Whenever matching is not based on the latest unfinished work.
     */

    /*
     * ================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ================================================================
     */

    public static void main(String[] args) {

        // Representative example.
        assert Optimal.isValid("()");

        // Multiple independent pairs.
        assert Optimal.isValid("()[]{}");

        // Wrong bracket type.
        assert !Optimal.isValid("(]");

        // Correct nested structure.
        assert Optimal.isValid("{[]}");

        // Crossed nesting.
        assert !Optimal.isValid("([)]");

        // Deep nesting.
        assert Optimal.isValid("((({{{[[[]]]}}})))");

        // Single opening bracket.
        assert !Optimal.isValid("(");

        // Single closing bracket.
        assert !Optimal.isValid(")");

        // Missing final closing bracket.
        assert !Optimal.isValid("(()");

        // Extra closing bracket.
        assert !Optimal.isValid("())");

        // Empty string is valid.
        assert Optimal.isValid("");

        // Long alternating sequence.
        assert Optimal.isValid("(){}[]({[]})");

        // Verify brute force implementation.
        assert BruteForce.isValid("{[()]}");

        // Brute force detects invalid nesting.
        assert !BruteForce.isValid("([)]");

        // Improved implementation correctness.
        assert Improved.isValid("{{[[(())]]}}");

        // Improved implementation detects mismatch.
        assert !Improved.isValid("{[(])}");

        // All three implementations agree.
        String[] cases = {
                "",
                "()",
                "()[]{}",
                "{[]}",
                "((()))",
                "([)]",
                "(]",
                "(()",
                "())",
                "{{{{",
                "[]{}()",
                "({[]})",
                "(((((())))))",
                "[(])",
                "{[()()]}"
        };

        for (String test : cases) {
            boolean b1 = BruteForce.isValid(test);
            boolean b2 = Improved.isValid(test);
            boolean b3 = Optimal.isValid(test);

            assert b1 == b2;
            assert b2 == b3;
        }

        System.out.println("All assertions passed.");
    }

}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/