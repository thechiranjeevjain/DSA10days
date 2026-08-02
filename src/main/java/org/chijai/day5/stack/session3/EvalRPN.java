package org.chijai.day5.stack.session3;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Evaluate Reverse Polish Notation
 *
 * ============================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================
 *
 * Title:
 * Evaluate Reverse Polish Notation
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * Stack, Simulation, Expression Evaluation
 *
 * Problem Description:
 *
 * Given an array of strings representing an arithmetic expression
 * in Reverse Polish Notation (RPN), evaluate the expression.
 *
 * Every token is either:
 *
 * • an integer
 * • "+"
 * • "-"
 * • "*"
 * • "/"
 *
 * Division truncates toward zero.
 *
 * Every operator always has exactly two operands.
 *
 * The input expression is guaranteed to be valid.
 *
 * Constraints:
 *
 * • 1 <= tokens.length <= 10^4
 * • tokens[i] is an operator or integer
 * • Expression is always valid
 * • No division by zero
 * • Result fits in 32-bit signed integer
 *
 * Representative Example 1
 *
 * Input:
 * ["2","1","+","3","*"]
 *
 * Evaluation:
 *
 * 2 1 +  -> 3
 * 3 3 *  -> 9
 *
 * Output:
 * 9
 *
 * ------------------------------------------------------------
 *
 * Representative Example 2
 *
 * Input:
 *
 * ["4","13","5","/","+"]
 *
 * Evaluation:
 *
 * 13 / 5 = 2
 * 4 + 2 = 6
 *
 * Output:
 * 6
 *
 * ------------------------------------------------------------
 *
 * Representative Example 3
 *
 * Input:
 *
 * ["10","6","9","3","+","-11","*","/","*","17","+","5","+"]
 *
 * Output:
 * 22
 *
 * Official LeetCode:
 *
 * https://leetcode.com/problems/evaluate-reverse-polish-notation/
 *
 *
 * ============================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ============================================================
 *
 * Pattern:
 * Expression Stack
 *
 * Archetype:
 * Last-In-First-Out state reconstruction
 *
 * Core Invariant:
 *
 * After processing every token,
 * the stack stores exactly the values of all completely evaluated
 * sub-expressions encountered so far.
 *
 * The top of the stack is always the most recently completed
 * sub-expression.
 *
 * Why It Works:
 *
 * Reverse Polish Notation postpones operators until both operands
 * have already appeared.
 *
 * Therefore when an operator arrives:
 *
 * • the required operands are guaranteed to be the two newest
 *   completed sub-expressions.
 *
 * Since the stack stores completed sub-expressions in evaluation
 * order, the correct operands are always on top.
 *
 * Recognition Signals:
 *
 * • postfix notation
 * • Reverse Polish Notation
 * • expression evaluation
 * • operands before operator
 * • every operator consumes previous values
 *
 * When To Use:
 *
 * • postfix evaluation
 * • compiler parsing
 * • calculator implementation
 * • expression interpreters
 * • stack machine simulation
 *
 * When NOT To Use:
 *
 * • infix parsing without preprocessing
 * • precedence parsing
 * • recursive expression trees already available
 *
 * Comparison:
 *
 * ------------------------------------------------------------
 * Infix Evaluation
 * ------------------------------------------------------------
 * Requires operator precedence.
 * Usually needs two stacks.
 *
 * ------------------------------------------------------------
 * Prefix Evaluation
 * ------------------------------------------------------------
 * Traverse from right to left.
 *
 * ------------------------------------------------------------
 * Reverse Polish Evaluation
 * ------------------------------------------------------------
 * Traverse once from left to right.
 * One stack is sufficient.
 *
 *
 * ============================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================
 *
 * Mental Model
 *
 * Imagine assembling larger expressions from already completed
 * smaller expressions.
 *
 * Every number starts as a complete expression.
 *
 * Every operator merges the latest two completed expressions into
 * one new completed expression.
 *
 * That newly formed expression becomes available for future
 * operators.
 *
 * The stack therefore represents the frontier between:
 *
 * already solved
 *
 * and
 *
 * not yet combined.
 *
 *
 * ------------------------------------------------------------
 * 🟢 Primary Invariant
 * ------------------------------------------------------------
 *
 * After processing token i,
 * every stack element represents one fully evaluated
 * sub-expression.
 *
 *
 * ------------------------------------------------------------
 * 🟢 Ordering Invariant
 * ------------------------------------------------------------
 *
 * The top of stack corresponds to the most recently completed
 * sub-expression.
 *
 * Therefore:
 *
 * first pop  = right operand
 *
 * second pop = left operand
 *
 * This ordering is essential.
 *
 *
 * ------------------------------------------------------------
 * 🟢 Size Invariant
 * ------------------------------------------------------------
 *
 * Reading an operand:
 *
 * stack size increases by one.
 *
 * Reading an operator:
 *
 * stack size decreases by one.
 *
 * because:
 *
 * two expressions become one.
 *
 *
 * ------------------------------------------------------------
 * 🟢 Operand Invariant
 * ------------------------------------------------------------
 *
 * Before every operator,
 * at least two values already exist.
 *
 * Guaranteed by the problem.
 *
 *
 * ------------------------------------------------------------
 * Variable Meaning
 * ------------------------------------------------------------
 *
 * stack
 *
 * Stores evaluated sub-expressions.
 *
 * right
 *
 * First value popped.
 *
 * left
 *
 * Second value popped.
 *
 * result
 *
 * Combination of left and right.
 *
 *
 * ------------------------------------------------------------
 * Allowed State Transition
 * ------------------------------------------------------------
 *
 * Operand:
 *
 * value
 * ->
 * push(value)
 *
 *
 * Operator:
 *
 * left
 * right
 *
 * ->
 *
 * evaluate(left,right)
 *
 * ->
 *
 * push(result)
 *
 *
 * ------------------------------------------------------------
 * Forbidden Transition
 * ------------------------------------------------------------
 *
 * Never reverse subtraction operands.
 *
 * Incorrect:
 *
 * right - left
 *
 * Correct:
 *
 * left - right
 *
 * Same rule for division.
 *
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * Every operator reduces the number of unfinished expressions.
 *
 * Eventually exactly one expression remains.
 *
 * That value is the answer.
 *
 *
 * ------------------------------------------------------------
 * Why Naive Solutions Fail
 * ------------------------------------------------------------
 *
 * Building an expression string first appears tempting.
 *
 * Problems:
 *
 * • precedence reconstruction
 * • parentheses management
 * • unnecessary parsing
 * • slower implementation
 *
 * RPN already encodes evaluation order.
 *
 * The stack directly executes that order.
 *
 *
 * ============================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================
 *
 * Mistake 1
 *
 * Swapping subtraction operands.
 *
 * Example:
 *
 * 5 2 -
 *
 * Correct:
 *
 * 5-2=3
 *
 * Wrong:
 *
 * 2-5=-3
 *
 * Violated Invariant:
 *
 * First pop is always the right operand.
 *
 * ------------------------------------------------------------
 *
 * Mistake 2
 *
 * Swapping division operands.
 *
 * Example:
 *
 * 8 2 /
 *
 * Correct:
 *
 * 8/2=4
 *
 * Wrong:
 *
 * 2/8=0
 *
 * Violated Invariant:
 *
 * Operand ordering.
 *
 * ------------------------------------------------------------
 *
 * Mistake 3
 *
 * Evaluating operators before enough operands.
 *
 * Impossible for valid inputs,
 * but common during manual implementations if stack operations
 * are written incorrectly.
 *
 * ------------------------------------------------------------
 *
 * Mistake 4
 *
 * Forgetting that integer division truncates toward zero.
 *
 * Java already satisfies this requirement.
 *
 *
 * ------------------------------------------------------------
 * Interview Trap
 * ------------------------------------------------------------
 *
 * Interviewer often asks:
 *
 * "Why are subtraction and division different?"
 *
 * Answer:
 *
 * Because addition and multiplication are commutative.
 *
 * Subtraction and division preserve operand order,
 * therefore first pop is right operand,
 * second pop is left operand.
 *
 *
 * ============================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================
 *
 * Typing Order
 *
 * 1.
 *
 * public int evalRPN(String[] tokens)
 *
 * 2.
 *
 * create stack
 *
 * 3.
 *
 * iterate through tokens
 *
 * 4.
 *
 * determine whether current token is operator
 *
 * 5.
 *
 * if operand
 *
 *      parse integer
 *      push
 *
 * 6.
 *
 * if operator
 *
 *      right = pop
 *      left = pop
 *
 *      compute
 *
 *      push result
 *
 * 7.
 *
 * return final stack value
 *
 *
 * Loop Skeleton
 *
 * for every token
 *
 *      operand ?
 *          push
 *      else
 *          pop right
 *          pop left
 *          compute
 *          push
 *
 *
 * ============================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================
 *
 * create stack
 *
 * for token
 *
 *      if number
 *          push
 *      else
 *          right=pop
 *          left=pop
 *          push(operation)
 *
 * return pop
 *
 *
 * ============================================================
 * 6. SOLUTION CLASSES
 * ============================================================
 */

/**
 * Exactly one public class as required.
 */
public class EvalRPN {

    /**
     * --------------------------------------------------------
     * Brute Force
     * --------------------------------------------------------
     *
     * Idea:
     *
     * Continuously scan the token list until an operator whose
     * operands are both numbers is found.
     *
     * Replace those three tokens with the computed value.
     *
     * Repeat until only one token remains.
     *
     * Invariant:
     *
     * Every replacement shortens the remaining expression.
     *
     * Limitation:
     *
     * Multiple rescans.
     *
     * Complexity:
     *
     * Time:
     * O(n²)
     *
     * Space:
     * O(n)
     *
     * Interview Usefulness:
     *
     * Good starting discussion only.
     */
    static class BruteForce {

        public int evalRPN(String[] tokens) {

            java.util.List<String> list = new java.util.ArrayList<>();

            for (String token : tokens) {
                list.add(token);
            }

            while (list.size() > 1) {

                for (int i = 0; i < list.size(); i++) {

                    String token = list.get(i);

                    if (!isOperator(token)) {
                        continue;
                    }

                    int left = Integer.parseInt(list.get(i - 2));
                    int right = Integer.parseInt(list.get(i - 1));

                    int value = apply(left, right, token);

                    list.remove(i);
                    list.remove(i - 1);
                    list.remove(i - 2);

                    list.add(i - 2, String.valueOf(value));

                    break;
                }
            }

            return Integer.parseInt(list.get(0));
        }        private static boolean isOperator(String token) {

            return token.equals("+")
                    || token.equals("-")
                    || token.equals("*")
                    || token.equals("/");
        }

        private static int apply(int left, int right, String operator) {

            return switch (operator) {

                case "+" -> left + right;

                case "-" -> left - right;

                case "*" -> left * right;

                default -> left / right;
            };
        }
    }

    /**
     * --------------------------------------------------------
     * Improved
     * --------------------------------------------------------
     *
     * Idea:
     *
     * Maintain a stack containing evaluated sub-expressions.
     *
     * Every operand is pushed exactly once.
     *
     * Every operator consumes two completed sub-expressions and
     * produces one new completed sub-expression.
     *
     * Invariant:
     *
     * The stack always stores only fully evaluated sub-expressions.
     *
     * Improvement:
     *
     * Single left-to-right traversal.
     *
     * No rescanning.
     *
     * Complexity:
     *
     * Time:
     * O(n)
     *
     * Space:
     * O(n)
     *
     * Interview Usefulness:
     *
     * Introduces the correct invariant before discussing
     * implementation refinements.
     */
    static class Improved {

        public int evalRPN(String[] tokens) {

            Deque<Integer> stack = new ArrayDeque<>();

            for (String token : tokens) {

                if (!isOperator(token)) {

                    stack.push(Integer.parseInt(token));

                    continue;
                }

                int right = stack.pop();

                int left = stack.pop();

                stack.push(apply(left, right, token));
            }

            return stack.pop();
        }

        private static boolean isOperator(String token) {

            return token.length() == 1
                    && "+-*/".indexOf(token.charAt(0)) >= 0;
        }

        private static int apply(int left,
                                 int right,
                                 String operator) {

            return switch (operator.charAt(0)) {

                case '+' -> left + right;

                case '-' -> left - right;

                case '*' -> left * right;

                default -> left / right;
            };
        }
    }

    /**
     * --------------------------------------------------------
     * Optimal (Interview Preferred)
     * --------------------------------------------------------
     *
     * Idea:
     *
     * Traverse once.
     *
     * Treat every stack value as one completely evaluated
     * sub-expression.
     *
     * When an operator appears:
     *
     * • remove the two newest completed expressions
     * • combine them
     * • push the new completed expression
     *
     * Invariant:
     *
     * After every processed token,
     * every stack element is a valid completed sub-expression.
     *
     * Correctness:
     *
     * Reverse Polish Notation guarantees that whenever an operator
     * appears, its operands are precisely the two most recently
     * completed sub-expressions.
     *
     * Therefore popping twice always retrieves the correct operands.
     *
     * Complexity:
     *
     * Time:
     * O(n)
     *
     * Space:
     * O(n)
     *
     * Interview Usefulness:
     *
     * Preferred solution.
     *
     * Small.
     *
     * Deterministic.
     *
     * Easy to derive from the invariant.
     */
    static class Optimal {

        public int evalRPN(String[] tokens) {

            // 🟢 Invariant:
            // Stack contains only completed sub-expressions.
            Deque<Integer> stack = new ArrayDeque<>();

            for (String token : tokens) {

                // Operand starts a new completed expression.
                if (!isOperator(token)) {

                    stack.push(Integer.parseInt(token));

                    continue;
                }

                // First pop is always the right operand.
                int right = stack.pop();

                // Second pop is always the left operand.
                int left = stack.pop();

                int result;

                switch (token) {

                    case "+" ->

                        // Addition is order independent.
                            result = left + right;

                    case "-" ->

                        // Preserve operand order.
                            result = left - right;

                    case "*" ->

                        // Multiplication is order independent.
                            result = left * right;

                    default ->

                        // Java truncates toward zero.
                            result = left / right;
                }

                // Newly completed expression replaces two smaller ones.
                stack.push(result);
            }

            // Entire expression has collapsed into one value.
            return stack.pop();
        }

        private static boolean isOperator(String token) {

            return token.length() == 1
                    && "+-*/".indexOf(token.charAt(0)) >= 0;
        }
    }

/**
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Invariant
 *
 * The stack never stores partial expressions.
 *
 * Every value is already completely evaluated.
 *
 * ------------------------------------------------------------
 *
 * Search Space
 *
 * Remaining unfinished expression.
 *
 * Every operator reduces that search space by merging two
 * completed expressions into one.
 *
 * ------------------------------------------------------------
 *
 * Discard Rule
 *
 * Once two operands are combined into one value,
 * the original operands will never be needed again.
 *
 * Therefore they can safely disappear from the stack.
 *
 * ------------------------------------------------------------
 *
 * Correctness
 *
 * Because postfix notation guarantees operands appear before
 * operators,
 * the top two stack elements are exactly the required operands.
 *
 * ------------------------------------------------------------
 *
 * Termination
 *
 * Every operator decreases stack size by one.
 *
 * Eventually only one completed expression remains.
 *
 * ------------------------------------------------------------
 *
 * In-place Feasibility
 *
 * Not naturally.
 *
 * A stack is required because future operators may reference
 * previously computed values in LIFO order.
 *
 * ------------------------------------------------------------
 *
 * Streaming Feasibility
 *
 * Yes.
 *
 * Tokens can be processed one by one without storing the entire
 * input beyond the evaluation stack.
 *
 * ------------------------------------------------------------
 *
 * When NOT To Use
 *
 * Infix expressions with precedence rules.
 *
 * Those require parsing or operator precedence handling.
 *
 *
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 *
 * Postfix expression.
 *
 * ------------------------------------------------------------
 *
 * Pattern
 *
 * Expression Stack.
 *
 * ------------------------------------------------------------
 *
 * Invariant
 *
 * Stack stores only completed sub-expressions.
 *
 * ------------------------------------------------------------
 *
 * Search Target
 *
 * Final single expression.
 *
 * ------------------------------------------------------------
 *
 * Discard Rule
 *
 * Two completed expressions merge into one.
 *
 * ------------------------------------------------------------
 *
 * Common Trap
 *
 * Reverse subtraction/division operands.
 *
 * ------------------------------------------------------------
 *
 * Edge Cases
 *
 * • one operand
 * • negative numbers
 * • zero
 * • truncating division
 *
 * ------------------------------------------------------------
 *
 * One-Liner
 *
 * Push operands, pop two for every operator,
 * evaluate, push result.
 *
 * ------------------------------------------------------------
 *
 * Re-derivation Cue
 *
 * Every stack value represents one finished expression.
 *
 *
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * Variant:
 * Prefix Evaluation
 *
 * Change:
 *
 * Traverse from right to left.
 *
 * Invariant remains identical.
 *
 * ------------------------------------------------------------
 *
 * Variant:
 *
 * Floating Point Evaluation
 *
 * Replace integer stack with double stack.
 *
 * Invariant remains unchanged.
 *
 * ------------------------------------------------------------
 *
 * Variant:
 *
 * Custom Operators
 *
 * Extend operator dispatch.
 *
 * Stack invariant does not change.
 *
 * ------------------------------------------------------------
 *
 * Pattern Break
 *
 * Infix notation.
 *
 * Why?
 *
 * Operator precedence is no longer encoded.
 *
 * One stack is insufficient by itself.
 *

 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * Can you answer these without looking at the code?
 *
 * □ What is the invariant?
 *
 *   Every stack element is a fully evaluated sub-expression.
 *
 * □ What is the search target?
 *
 *   Reduce all sub-expressions into exactly one final value.
 *
 * □ What is the discard rule?
 *
 *   Two completed sub-expressions are replaced by one newly
 *   completed sub-expression.
 *
 * □ Why does termination happen?
 *
 *   Every operator reduces stack size by one.
 *
 * □ Why does the naive approach fail?
 *
 *   It repeatedly rescans and reconstructs expressions instead
 *   of exploiting the evaluation order already encoded by RPN.
 *
 * □ Which edge cases matter?
 *
 *   • Single operand
 *   • Negative numbers
 *   • Zero
 *   • Division truncation toward zero
 *
 * □ What is the easiest debugging checkpoint?
 *
 *   After every processed token, verify every stack value
 *   represents a complete sub-expression.
 *
 * □ Are subtraction and division handled correctly?
 *
 *   First pop is right operand.
 *   Second pop is left operand.
 *
 * □ Can this invariant extend to prefix notation?
 *
 *   Yes.
 *   Only the traversal direction changes.
 *
 * □ Where does this pattern stop working?
 *
 *   Infix expressions without precedence handling.
 */

    /**
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */
    public static void main(String[] args) {

        Optimal solver = new Optimal();

        // Representative example.
        assert solver.evalRPN(
                new String[]{"2", "1", "+", "3", "*"}) == 9;

        // Division before addition.
        assert solver.evalRPN(
                new String[]{"4", "13", "5", "/", "+"}) == 6;

        // Official complex example.
        assert solver.evalRPN(
                new String[]{
                        "10",
                        "6",
                        "9",
                        "3",
                        "+",
                        "-11",
                        "*",
                        "/",
                        "*",
                        "17",
                        "+",
                        "5",
                        "+"
                }) == 22;

        // Single operand should return itself.
        assert solver.evalRPN(
                new String[]{"42"}) == 42;

        // Verify subtraction preserves operand order.
        assert solver.evalRPN(
                new String[]{"5", "2", "-"}) == 3;

        // Verify division preserves operand order.
        assert solver.evalRPN(
                new String[]{"8", "2", "/"}) == 4;

        // Division truncates toward zero.
        assert solver.evalRPN(
                new String[]{"7", "-3", "/"}) == -2;

        // Negative operand multiplication.
        assert solver.evalRPN(
                new String[]{"-2", "4", "*"}) == -8;

        // Nested expression.
        assert solver.evalRPN(
                new String[]{
                        "3",
                        "4",
                        "+",
                        "2",
                        "*",
                        "7",
                        "/"
                }) == 2;

        // Mixed operators.
        assert solver.evalRPN(
                new String[]{
                        "5",
                        "1",
                        "2",
                        "+",
                        "4",
                        "*",
                        "+",
                        "3",
                        "-"
                }) == 14;

        // Cross-check all implementations.

        BruteForce brute = new BruteForce();
        Improved improved = new Improved();

        String[][] suites = {

                {"2", "1", "+", "3", "*"},

                {"4", "13", "5", "/", "+"},

                {"5", "2", "-"},

                {"8", "2", "/"},

                {"-2", "4", "*"},

                {"42"},

                {
                        "10",
                        "6",
                        "9",
                        "3",
                        "+",
                        "-11",
                        "*",
                        "/",
                        "*",
                        "17",
                        "+",
                        "5",
                        "+"
                }
        };

        for (String[] test : suites) {

            int expected = brute.evalRPN(test);

            assert improved.evalRPN(test) == expected;

            assert solver.evalRPN(test) == expected;
        }

        System.out.println("All assertions passed.");
    }
}