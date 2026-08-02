package org.chijai.day5.stack.session3;

import java.util.ArrayDeque;
import java.util.Deque;

public class BasicCalculator {

/*
 * ============================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================
 *
 * Title:
 * Basic Calculator
 *
 * Difficulty:
 * Hard
 *
 * Tags:
 * Stack
 * Expression Parsing
 * Simulation
 * String
 *
 * Official LeetCode:
 * https://leetcode.com/problems/basic-calculator/
 *
 * ------------------------------------------------------------
 * Problem
 * ------------------------------------------------------------
 *
 * Given a valid arithmetic expression consisting of:
 *
 *  - digits
 *  - spaces
 *  - '+'
 *  - '-'
 *  - '('
 *  - ')'
 *
 * evaluate the expression.
 *
 * Division and multiplication do not exist.
 *
 * You may NOT use eval() or any parser library.
 *
 * ------------------------------------------------------------
 * Constraints
 * ------------------------------------------------------------
 *
 * • 1 <= s.length <= 3 * 10^5
 *
 * • s contains:
 *      digits
 *      spaces
 *      '+'
 *      '-'
 *      '('
 *      ')'
 *
 * • Expression is valid.
 *
 * • Unary minus may appear.
 *
 * ------------------------------------------------------------
 * Examples
 * ------------------------------------------------------------
 *
 * Example 1
 *
 * Input:
 * "1 + 1"
 *
 * Output:
 * 2
 *
 * ------------------------------------------------------------
 *
 * Example 2
 *
 * Input:
 * "2-1+2"
 *
 * Output:
 * 3
 *
 * ------------------------------------------------------------
 *
 * Example 3
 *
 * "(1+(4+5+2)-3)+(6+8)"
 *
 * Output:
 * 23
 *
 * ------------------------------------------------------------
 * Goal
 * ------------------------------------------------------------
 *
 * Evaluate the expression in one left-to-right scan.
 *
 * Time:
 * O(n)
 *
 * Space:
 * O(depth of parentheses)
 *
 * ============================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ============================================================
 *
 * Pattern
 * -------
 * Environment Stack
 *
 * Archetype
 * ---------
 * Context Propagation
 *
 * Unlike classical expression parsing where operators are stored,
 * here only the CURRENT SIGN ENVIRONMENT is stored.
 *
 * Every parenthesis introduces a new environment.
 *
 * The current environment determines whether '+' and '-'
 * should behave normally or be inverted.
 *
 * ------------------------------------------------------------
 * Core Invariant
 * ------------------------------------------------------------
 *
 * stack.peek()
 *
 * ALWAYS represents
 *
 * "What does '+' mean in the current parenthesis?"
 *
 * If
 *
 * stack.peek() = +1
 *
 * then
 *
 *      '+' means +
 *      '-' means -
 *
 * If
 *
 * stack.peek() = -1
 *
 * then
 *
 *      '+' means -
 *      '-' means +
 *
 * This single invariant completely removes the need to
 * recursively evaluate expressions.
 *
 * ------------------------------------------------------------
 * Recognition Signals
 * ------------------------------------------------------------
 *
 * Use this pattern whenever:
 *
 * ✓ parentheses only affect sign
 *
 * ✓ operators are only '+' and '-'
 *
 * ✓ nested inversions exist
 *
 * ✓ expression is evaluated left-to-right
 *
 * ------------------------------------------------------------
 * Why It Works
 * ------------------------------------------------------------
 *
 * Consider
 *
 * -(A+B-C)
 *
 * Everything inside gets multiplied by -1.
 *
 * Instead of revisiting all terms,
 * we simply remember:
 *
 * Current Environment = -1
 *
 * Every future sign automatically incorporates this.
 *
 * ------------------------------------------------------------
 * Search Space
 * ------------------------------------------------------------
 *
 * We never search numbers.
 *
 * We search
 *
 * "Which sign environment is currently active?"
 *
 * Every '(' pushes a new environment.
 *
 * Every ')' restores the previous environment.
 *
 * ------------------------------------------------------------
 * When NOT To Use
 * ------------------------------------------------------------
 *
 * This pattern breaks once
 *
 * '*'
 * '/'
 * '^'
 * precedence
 *
 * enter the language.
 *
 * Then operator precedence must also be stored.
 *
 * ------------------------------------------------------------
 * Comparison
 * ------------------------------------------------------------
 *
 * Classic Operator Stack
 *
 * Stores:
 * operators
 * operands
 *
 * -------------------------------
 *
 * Environment Stack
 *
 * Stores:
 * ONLY sign context
 *
 * Much smaller state.
 *
 * ============================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================
 *
 * Imagine carrying a polarity switch.
 *
 * Initially
 *
 * + means +
 * - means -
 *
 * Whenever we walk through
 *
 * -(...)
 *
 * the switch flips.
 *
 * Every future operator inside the parenthesis automatically
 * changes meaning.
 *
 * We never revisit previous numbers.
 *
 * We only update the meaning of future signs.
 *
 * ------------------------------------------------------------
 * Variables
 * ------------------------------------------------------------
 *
 * ans
 *
 * Sum of every completely processed number.
 *
 * -------------------------------
 *
 * num
 *
 * Current number being constructed.
 *
 * -------------------------------
 *
 * sign
 *
 * Effective sign of num.
 *
 * Already includes every surrounding parenthesis.
 *
 * -------------------------------
 *
 * stack.peek()
 *
 * Current parenthesis environment.
 *
 * ------------------------------------------------------------
 * Fundamental Invariant #1
 * ------------------------------------------------------------
 *
 * ans
 *
 * always equals
 *
 * evaluation of everything BEFORE num.
 *
 * Current num is intentionally excluded.
 *
 * ------------------------------------------------------------
 * Fundamental Invariant #2
 * ------------------------------------------------------------
 *
 * sign
 *
 * already contains
 *
 * every parenthesis effect.
 *
 * Therefore
 *
 * ans += sign * num;
 *
 * is always correct.
 *
 * ------------------------------------------------------------
 * Fundamental Invariant #3
 * ------------------------------------------------------------
 *
 * stack.peek()
 *
 * equals
 *
 * total sign inherited from all open parentheses.
 *
 * Example
 *
 * -( +( -( ... )))
 *
 * Multiplication of outer signs becomes
 *
 * (+1)
 * (-1)
 * (+1)
 * ...
 *
 * Exactly what the stack stores.
 *
 * ------------------------------------------------------------
 * Fundamental Invariant #4
 * ------------------------------------------------------------
 *
 * Encountering '('
 *
 * NEVER changes ans.
 *
 * NEVER changes num.
 *
 * It ONLY starts a new sign environment.
 *
 * ------------------------------------------------------------
 * Allowed Moves
 * ------------------------------------------------------------
 *
 * digit
 *      extend current number
 *
 * '+'/'-'
 *      commit previous number
 *      compute next effective sign
 *
 * '('
 *      push environment
 *
 * ')'
 *      pop environment
 *
 * ------------------------------------------------------------
 * Forbidden Moves
 * ------------------------------------------------------------
 *
 * Never delay updating ans after reading '+'/'-'.
 *
 * Otherwise
 * num leaks into later operators.
 *
 * Never compute sign without stack.peek().
 *
 * Otherwise nested negatives fail.
 *
 * Never reset sign after ')'.
 *
 * Pop already restores the previous environment.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * The final number has no following operator.
 *
 * Therefore after the scan finishes we must execute
 *
 * ans + sign * num
 *
 * exactly once.
 *
 * ------------------------------------------------------------
 * Why Naive Solutions Fail
 * ------------------------------------------------------------
 *
 * A common instinct:
 *
 * whenever ')' appears,
 * compute the entire parenthesis.
 *
 * This repeatedly rebuilds expressions,
 * complicates unary minus,
 * and creates unnecessary state.
 *
 * The environment-stack invariant avoids all recomputation.
 *
 * ============================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================
 *
 * Mistake 1
 * ---------
 *
 * Forgetting to multiply new sign by stack.peek().
 *
 * Example
 *
 * -(4+5)
 *
 * '+' inside becomes '-'.
 *
 * Missing multiplication gives wrong answer.
 *
 * ------------------------------------------------------------
 *
 * Mistake 2
 * ---------
 *
 * Forgetting final
 *
 * ans += sign * num
 *
 * Last number is silently lost.
 *
 * ------------------------------------------------------------
 *
 * Mistake 3
 * ---------
 *
 * Pushing '+' or '-'
 * instead of effective sign.
 *
 * Nested negatives become impossible to reconstruct.
 *
 * ------------------------------------------------------------
 *
 * Mistake 4
 * ---------
 *
 * Resetting sign after ')'.
 *
 * Parenthesis already restores environment.
 *
 * Manual reset corrupts later evaluation.
 *
 * ------------------------------------------------------------
 *
 * Interview Trap
 * --------------
 *
 * Explain WHY
 *
 * stack stores environment,
 * not operators.
 *
 * This distinction is the entire algorithm.
 *
 * ============================================================
 * ⚙️ IMPLEMENTATION BLUEPRINT
 * ============================================================
 *
 * Typing Order
 * ------------
 *
 * 1.
 *
 * int ans = 0;
 * int num = 0;
 * int sign = 1;
 *
 * 2.
 *
 * create stack
 *
 * push(+1)
 *
 * 3.
 *
 * iterate characters
 *
 * 4.
 *
 * digit
 *      build num
 *
 * 5.
 *
 * '('
 *      push(sign)
 *
 * 6.
 *
 * ')'
 *      pop()
 *
 * 7.
 *
 * '+' or '-'
 *
 * commit previous number
 *
 * sign =
 * currentOperator * stack.peek()
 *
 * reset num
 *
 * 8.
 *
 * return
 *
 * ans + sign * num
 *
 * ============================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================
 *
 * initialize
 *
 * for each character
 *
 * build number
 *
 * push environment
 *
 * pop environment
 *
 * commit previous number
 *
 * compute effective sign
 *
 * return final contribution
 */    /*
     * ============================================================
     * 🗣 LIVE CODING NARRATION
     * ============================================================
     *
     * While implementing under interview pressure, keep the narration
     * mechanical and invariant-driven.
     *
     * 1.
     * "ans contains every finished number."
     *
     * 2.
     * "num is still being built."
     *
     * 3.
     * "sign already includes every enclosing parenthesis."
     *
     * 4.
     * "stack.peek() tells me what '+' currently means."
     *
     * 5.
     * "Whenever I see '+' or '-', I first commit the previous number."
     *
     * 6.
     * "Then I compute the effective sign for the next number."
     *
     * 7.
     * "The final number has no following operator, so I add it once
     * after the loop."
     *
     * This narration naturally reconstructs the implementation without
     * memorizing code.
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
     * Repeatedly evaluate the innermost parenthesis.
     *
     * Replace it by its computed value.
     *
     * Continue until no parenthesis remains.
     *
     * ------------------------------------------------------------
     * Invariant
     * ------------------------------------------------------------
     *
     * Every iteration removes exactly one parenthesized expression.
     *
     * ------------------------------------------------------------
     * Limitation
     * ------------------------------------------------------------
     *
     * Requires repeatedly scanning the string.
     *
     * Parenthesis extraction also becomes expensive.
     *
     * Nested expressions cause repeated work.
     *
     * ------------------------------------------------------------
     * Complexity
     * ------------------------------------------------------------
     *
     * Time
     * O(n²)
     *
     * Space
     * O(n)
     *
     * ------------------------------------------------------------
     * Interview Usefulness
     * ------------------------------------------------------------
     *
     * Good only as a starting discussion.
     *
     * Not suitable for the required constraints.
     */
    static final class BruteForce {

        public int calculate(String s) {
            throw new UnsupportedOperationException(
                    "Conceptual baseline only. Use the optimal solution.");
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
     * Maintain operand and operator stacks.
     *
     * Evaluate when closing parentheses are encountered.
     *
     * ------------------------------------------------------------
     * Invariant
     * ------------------------------------------------------------
     *
     * Stacks together represent the partially parsed expression.
     *
     * ------------------------------------------------------------
     * Improvement
     * ------------------------------------------------------------
     *
     * Eliminates repeated rescanning.
     *
     * ------------------------------------------------------------
     * Limitation
     * ------------------------------------------------------------
     *
     * Stores significantly more state than necessary for this problem.
     *
     * Since only '+' and '-' exist,
     * operator precedence never changes.
     *
     * ------------------------------------------------------------
     * Complexity
     * ------------------------------------------------------------
     *
     * Time
     * O(n)
     *
     * Space
     * O(n)
     *
     * ------------------------------------------------------------
     * Interview Usefulness
     * ------------------------------------------------------------
     *
     * Reasonable,
     * but still more complicated than needed.
     */
    static final class Improved {

        public int calculate(String s) {
            throw new UnsupportedOperationException(
                    "Conceptual improvement only. Use the optimal solution.");
        }
    }

    /**
     * ============================================================
     * Optimal (Interview Preferred)
     * ============================================================
     *
     * Pattern
     * -------
     * Environment Stack
     *
     * ------------------------------------------------------------
     * Idea
     * ------------------------------------------------------------
     *
     * Instead of storing operators,
     * store only the current sign environment.
     *
     * Every '(' inherits the current effective sign.
     *
     * Every ')' restores the previous environment.
     *
     * ------------------------------------------------------------
     * Invariant
     * ------------------------------------------------------------
     *
     * stack.peek()
     *
     * equals the accumulated sign contributed by every currently
     * active parenthesis.
     *
     * sign
     *
     * equals the effective sign that should multiply the next number.
     *
     * ------------------------------------------------------------
     * Correctness
     * ------------------------------------------------------------
     *
     * Every completed number contributes exactly once.
     *
     * Every enclosing minus sign is already incorporated into sign.
     *
     * Therefore
     *
     * ans += sign * num
     *
     * is always correct.
     *
     * ------------------------------------------------------------
     * Complexity
     * ------------------------------------------------------------
     *
     * Time
     * O(n)
     *
     * Space
     * O(depth of parentheses)
     *
     * ------------------------------------------------------------
     * Interview Usefulness
     * ------------------------------------------------------------
     *
     * This is the expected optimal solution.
     */
    static final class Optimal {

        public int calculate(String s) {

            // 🔵 Empty expression contributes nothing.
            if (s == null || s.isEmpty()) {
                return 0;
            }

            int ans = 0;

            // 🟢 Number currently being constructed.
            int num = 0;

            // 🟢 Effective sign for the current number.
            int sign = 1;

            Deque<Integer> stack = new ArrayDeque<>();

            // 🟢 Initial environment:
            // '+' behaves as positive.
            stack.push(1);

            for (char c : s.toCharArray()) {

                if (Character.isDigit(c)) {

                    // 🟢 Invariant:
                    // num stores every digit read so far.
                    num = num * 10 + (c - '0');

                } else if (c == '(') {

                    // 🟢 Current effective sign becomes the
                    // environment for everything inside.
                    stack.push(sign);

                } else if (c == ')') {

                    // 🟢 Restore previous parenthesis environment.
                    stack.pop();

                } else if (c == '+' || c == '-') {

                    // 🟢 Commit exactly one completed number.
                    ans += sign * num;

                    // 🟢 Compute effective sign for the next number.
                    //
                    // Local operator
                    // multiplied by
                    // inherited environment.
                    sign = (c == '+' ? 1 : -1) * stack.peek();

                    // 🟢 Ready to build the next integer.
                    num = 0;

                } else {

                    // Ignore spaces.
                }
            }

            // 🟢 Final number has no following operator.
            return ans + sign * num;
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * If asked,
 *
 * "Explain your algorithm without code."
 *
 * Answer:
 *
 * The core invariant is that I never store operators.
 *
 * I only store the sign environment produced by enclosing
 * parentheses.
 *
 * Every number is committed exactly once when I encounter the
 * next operator.
 *
 * The effective sign of that number has already absorbed every
 * surrounding minus.
 *
 * Therefore nested expressions never need to be revisited.
 *
 * ------------------------------------------------------------
 * Discard Rule
 * ------------------------------------------------------------
 *
 * After committing a number,
 * its value is permanently absorbed into ans.
 *
 * It will never change again.
 *
 * ------------------------------------------------------------
 * Correctness
 * ------------------------------------------------------------
 *
 * Every surrounding parenthesis contributes exactly one
 * multiplicative sign.
 *
 * That sign is preserved on the stack.
 *
 * Therefore every number receives exactly the correct polarity.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * The scan is finite.
 *
 * Every character is processed once.
 *
 * The only deferred contribution is the final number,
 * which is added after the loop.


 * ------------------------------------------------------------
 * In-place Feasibility
 * ------------------------------------------------------------
 *
 * No.
 *
 * Parenthesis nesting creates state that must survive until the
 * matching ')' is encountered.
 *
 * A stack proportional to the nesting depth is therefore necessary.
 *
 * ------------------------------------------------------------
 * Streaming Feasibility
 * ------------------------------------------------------------
 *
 * Yes.
 *
 * Characters are consumed exactly once from left to right.
 *
 * The algorithm never needs to revisit earlier characters.
 *
 * ------------------------------------------------------------
 * When NOT To Use
 * ------------------------------------------------------------
 *
 * This technique is specialized for expressions whose precedence
 * is completely determined by parentheses and whose operators are
 * only '+' and '-'.
 *
 * If '*', '/', '%', '^', function calls, or arbitrary precedence
 * rules are introduced, operator precedence must also become part
 * of the maintained state.
 *
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 *
 * Parentheses only influence '+' and '-'.
 *
 * ------------------------------------------------------------
 * Pattern
 * ------------------------------------------------------------
 *
 * Environment Stack
 *
 * ------------------------------------------------------------
 * Invariant
 * ------------------------------------------------------------
 *
 * stack.peek()
 *
 * =
 *
 * meaning of '+' inside the current parenthesis.
 *
 * ------------------------------------------------------------
 * Search Target
 * ------------------------------------------------------------
 *
 * Maintain the current sign environment.
 *
 * ------------------------------------------------------------
 * Discard Rule
 * ------------------------------------------------------------
 *
 * Once
 *
 * ans += sign * num
 *
 * executes,
 *
 * that number is finalized forever.
 *
 * ------------------------------------------------------------
 * Common Trap
 * ------------------------------------------------------------
 *
 * Forgetting
 *
 * stack.peek()
 *
 * while computing the next sign.
 *
 * ------------------------------------------------------------
 * Edge Cases
 * ------------------------------------------------------------
 *
 * ✓ spaces
 *
 * ✓ multi-digit numbers
 *
 * ✓ nested negatives
 *
 * ✓ final number
 *
 * ✓ deeply nested parentheses
 *
 * ------------------------------------------------------------
 * One-Liner
 * ------------------------------------------------------------
 *
 * "Store environments, not operators."
 *
 * ------------------------------------------------------------
 * Re-Derivation Cue
 * ------------------------------------------------------------
 *
 * Ask:
 *
 * "If I enter one more parenthesis,
 * what changes?"
 *
 * Answer:
 *
 * Only the interpretation of future signs.
 *
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * Variation 1
 * -----------
 *
 * Unary Minus
 *
 * Example
 *
 * -(2+3)
 *
 * Still works because sign already includes the inherited
 * environment.
 *
 * ------------------------------------------------------------
 *
 * Variation 2
 * -----------
 *
 * Extremely Deep Nesting
 *
 * Still correct.
 *
 * Stack size grows only with nesting depth.
 *
 * ------------------------------------------------------------
 *
 * Variation 3
 * -----------
 *
 * Multi-digit Numbers
 *
 * Still preserved because digits are accumulated before
 * any contribution is committed.
 *
 * ------------------------------------------------------------
 *
 * Variation 4
 * -----------
 *
 * Large Amounts of Spaces
 *
 * Spaces represent no state transition.
 *
 * They are ignored.
 *
 * ------------------------------------------------------------
 *
 * Variation 5
 * -----------
 *
 * Add Multiplication
 *
 * Pattern Break.
 *
 * Why?
 *
 * The environment stack alone no longer captures operator
 * precedence.
 *
 * Multiplication must be evaluated before addition,
 * requiring additional state.
 *
 * ------------------------------------------------------------
 *
 * Variation 6
 * -----------
 *
 * Add Division
 *
 * Same failure.
 *
 * Operator precedence cannot be reconstructed solely from
 * inherited sign environments.
 *
 * ------------------------------------------------------------
 *
 * Variation 7
 * -----------
 *
 * Expression Without Parentheses
 *
 * The stack never grows beyond one element.
 *
 * The algorithm naturally degenerates into a simple running sum.
 *
 * ------------------------------------------------------------
 *
 * Variation 8
 * -----------
 *
 * Streaming Input
 *
 * Characters arriving over a network stream can be processed
 * incrementally as long as order is preserved.
 *
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * Q.
 * What is the invariant?
 *
 * A.
 * stack.peek() is the current sign environment.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * What is the maintained state?
 *
 * A.
 * ans
 * num
 * sign
 * stack
 *
 * ------------------------------------------------------------
 *
 * Q.
 * What is the search target?
 *
 * A.
 * Correct sign for the next completed number.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * What is the discard rule?
 *
 * A.
 * After
 *
 * ans += sign * num
 *
 * the number is permanently finalized.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Why does termination work?
 *
 * A.
 * Every character is processed once.
 *
 * The final deferred number is added after the scan.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Why does the naive approach fail?
 *
 * A.
 * It repeatedly rebuilds subexpressions instead of maintaining
 * a persistent sign environment.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Which edge cases must always be mentally checked?
 *
 * A.
 *
 * ✓ Empty-like spacing
 *
 * ✓ One number
 *
 * ✓ Nested negatives
 *
 * ✓ Consecutive digits
 *
 * ✓ Final number
 *
 * ✓ Deep nesting
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Debugging Readiness
 *
 * A.
 *
 * If an answer is incorrect:
 *
 * 1. Verify every operator commits the previous number.
 *
 * 2. Verify stack.push(sign), not operator.
 *
 * 3. Verify sign uses stack.peek().
 *
 * 4. Verify final contribution is added.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Variant Readiness
 *
 * A.
 *
 * If only '+' and '-' exist,
 * reuse this pattern.
 *
 * If precedence changes,
 * redesign the state.
 *
 * ------------------------------------------------------------
 *
 * Q.
 * Pattern Boundary
 *
 * A.
 *
 * This algorithm is complete exactly while parentheses only
 * propagate sign.
 *
 * Beyond that boundary, an operator-precedence parser becomes
 * necessary.
 *
 * ============================================================
 * 🧪 MAIN + SELF-VERIFYING TESTS
 * ============================================================
 */

    public static void main(String[] args) {

        Optimal solver = new Optimal();

        /*
         * ------------------------------------------------------------
         * Happy Path
         * ------------------------------------------------------------
         */

        // Basic addition.
        assert solver.calculate("1 + 1") == 2;

        // Mixed addition and subtraction.
        assert solver.calculate(" 2-1 + 2 ") == 3;

        // Representative LeetCode example.
        assert solver.calculate("(1+(4+5+2)-3)+(6+8)") == 23;

        /*
         * ------------------------------------------------------------
         * Single Number
         * ------------------------------------------------------------
         */

        // No operators.
        assert solver.calculate("42") == 42;

        /*
         * ------------------------------------------------------------
         * Multi-digit Numbers
         * ------------------------------------------------------------
         */

        // Ensures digit accumulation works correctly.
        assert solver.calculate("123+456") == 579;

        // Multiple multi-digit values.
        assert solver.calculate("1000-250+50") == 800;

        /*
         * ------------------------------------------------------------
         * Nested Parentheses
         * ------------------------------------------------------------
         */

        // Double nesting.
        assert solver.calculate("((7))") == 7;

        // Nested positive groups.
        assert solver.calculate("(2+(3+(4+5)))") == 14;

        // Nested sign inversion.
        assert solver.calculate("(1-(4+5-2)-3)+(48-6)") == 33;

        // Multiple alternating environments.
        assert solver.calculate("1-(2-(3-(4-(5-6))))") == -3;

        /*
         * ------------------------------------------------------------
         * Leading Unary Minus
         * ------------------------------------------------------------
         */

        // Unary minus before parentheses.
        assert solver.calculate("-(2+3)") == -5;

        // Unary minus before a number.
        assert solver.calculate("-7") == -7;

        /*
         * ------------------------------------------------------------
         * Whitespace Robustness
         * ------------------------------------------------------------
         */

        assert solver.calculate("   9   ") == 9;

        assert solver.calculate(" ( 10 + ( 20 - 5 ) ) ") == 25;

        /*
         * ------------------------------------------------------------
         * Zero Handling
         * ------------------------------------------------------------
         */

        assert solver.calculate("0") == 0;

        assert solver.calculate("(0-(0+0))") == 0;

        /*
         * ------------------------------------------------------------
         * Deep Sign Propagation
         * ------------------------------------------------------------
         */

        assert solver.calculate("-(-(-1))") == -1;

        assert solver.calculate("-(-(8-3))") == 5;

        /*
         * ------------------------------------------------------------
         * Long Sequential Operations
         * ------------------------------------------------------------
         */

        assert solver.calculate("1+2+3+4+5+6+7+8+9") == 45;

        assert solver.calculate("50-10-10-10-10") == 10;

        /*
         * ------------------------------------------------------------
         * Boundary-style Checks
         * ------------------------------------------------------------
         */

        assert solver.calculate("2147483647") == 2147483647;

        assert solver.calculate("(1000000-(999999))") == 1;

        /*
         * ------------------------------------------------------------
         * Interview Traps
         * ------------------------------------------------------------
         */

        // Final number must be committed after loop.
        assert solver.calculate("1+2") == 3;

        // Nested minus environments.
        assert solver.calculate("1-(2+3)") == -4;

        // Minus immediately before parenthesis.
        assert solver.calculate("5-(3-1)") == 3;

        // Multiple nested inversions.
        assert solver.calculate("10-(2-(3-(4)))") == 5;

        System.out.println("All assertions passed.");
    }

    /*
     * ============================================================
     * Final Notes
     * ============================================================
     *
     * Mechanical Reconstruction Checklist
     * ----------------------------------
     *
     * 1. ans = finished contribution.
     *
     * 2. num = number currently being built.
     *
     * 3. sign = effective sign of num.
     *
     * 4. stack.peek() = current sign environment.
     *
     * 5. On digit:
     *      extend num.
     *
     * 6. On '(':
     *      push(sign).
     *
     * 7. On ')':
     *      pop().
     *
     * 8. On '+' or '-':
     *      commit current number,
     *      compute next effective sign,
     *      clear num.
     *
     * 9. After loop:
     *      commit final number.
     *
     * If you can reproduce these nine mechanical steps, the complete
     * implementation follows naturally from the invariant without
     * memorizing the source code.
     */

}


