package org.chijai.day5.stack.session3;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * =====================================================================================
 * EVALUATE REVERSE POLISH NOTATION — EXPRESSION STACK
 * =====================================================================================
 *
 * Goal:
 * See why postfix evaluation naturally becomes:
 *
 *      operand  -> push
 *      operator -> pop right, pop left, combine, push result
 *
 * Governing invariant:
 *
 *      Every stack value is one completely evaluated sub-expression.
 *
 * =====================================================================================
 */
public class EvalRPN {

    /*
     * =================================================================================
     * 1️⃣ PROBLEM STATEMENT
     * =================================================================================
     *
     * LeetCode 150 — Evaluate Reverse Polish Notation
     *
     * Given an array of tokens representing an arithmetic expression in
     * Reverse Polish Notation (postfix notation), evaluate the expression.
     *
     * Each token is either:
     *
     *   integer
     *   "+"
     *   "-"
     *   "*"
     *   "/"
     *
     * Division truncates toward zero.
     * The expression is valid.
     *
     * Example:
     *
     *   ["2","1","+","3","*"]
     *
     *   2 1 +  -> 3
     *   3 3 *  -> 9
     *
     *   answer = 9
     *
     * Another:
     *
     *   ["4","13","5","/","+"]
     *
     *   13 / 5 = 2
     *   4 + 2  = 6
     *
     *   answer = 6
     *
     * Constraints:
     *
     *   1 <= tokens.length <= 10^4
     *   valid expression
     *   no division by zero
     *   answer fits in 32-bit signed integer
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 2️⃣ HOW THE BRAIN SHOULD SEE IT
     * =================================================================================
     *
     * Do not begin with:
     *
     *   "This is a stack problem."
     *
     * Begin with the evaluation dependency.
     *
     * In postfix notation:
     *
     *   operands appear BEFORE the operator that needs them.
     *
     * Example:
     *
     *   2 1 +
     *
     * By the time "+" arrives:
     *
     *   2 is already complete
     *   1 is already complete
     *
     * The operator needs the TWO MOST RECENT completed expressions.
     *
     * That phrase is the structural signal:
     *
     *   TWO MOST RECENT
     *          ↓
     *        LIFO
     *          ↓
     *        STACK
     *
     * After combining them:
     *
     *   two completed expressions disappear
     *   one new completed expression replaces them
     *
     * So the expression repeatedly COLLAPSES:
     *
     *   completed pieces
     *         ↓
     *   consume latest two
     *         ↓
     *   produce one completed piece
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 3️⃣ UNSEEN-PROBLEM DECODER — WHEN SHOULD A STACK APPEAR?
     * =================================================================================
     *
     * For a random problem, ask:
     *
     *   1. WHAT arrives one by one?
     *
     *      Here:
     *          tokens
     *
     *   2. WHAT information from the past does current need?
     *
     *      Here:
     *          an operator needs the two newest completed expressions
     *
     *   3. WHICH past item is needed first?
     *
     *      Here:
     *          most recent one
     *
     *      most recent first -> LIFO -> stack
     *
     *   4. WHAT does consuming stack state mean?
     *
     *      Here:
     *          two completed expressions are merged permanently
     *
     *   5. WHAT gets pushed back?
     *
     *      Here:
     *          the newly completed expression
     *
     *
     * Mental movie:
     *
     *                TOKEN ARRIVES
     *                     |
     *              +------+------+
     *              |             |
     *          OPERATOR         NUMBER
     *              |             |
     *              v             v
     *         pop RIGHT         PUSH
     *         pop LEFT
     *              |
     *              v
     *         COMBINE THEM
     *              |
     *              v
     *         PUSH RESULT
     *
     *
     * Generic recognition sentence:
     *
     *   "Current input consumes the most recently completed states
     *    and replaces them with one new completed state."
     *
     * That is a strong stack signal.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 4️⃣ PATTERN RECOGNITION + BOUNDARY
     * =================================================================================
     *
     * Pattern:
     *
     *   Expression Stack / Reduction Stack
     *
     * Why it fits:
     *
     *   postfix notation already encodes evaluation order
     *   each operator consumes the newest completed expressions
     *   no precedence search is required
     *
     * Recognition signals:
     *
     *   postfix
     *   Reverse Polish Notation
     *   operands before operator
     *   operator consumes previous values
     *
     * Boundary:
     *
     *   RPN / postfix
     *      -> one left-to-right stack
     *
     *   Prefix
     *      -> same reduction idea, traverse right-to-left
     *
     *   Infix
     *      -> precedence is NOT already encoded
     *      -> needs parsing / precedence handling
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 5️⃣ MENTAL MODEL + CORE INVARIANT
     * =================================================================================
     *
     * Mental model:
     *
     *   Stack = shelf of COMPLETED sub-expressions.
     *
     * Every number is already a complete expression by itself.
     *
     * Every operator:
     *
     *   removes two completed expressions
     *   combines them
     *   returns one completed expression to the shelf
     *
     *
     * Core invariant:
     *
     *   After every processed token,
     *   every stack element represents one fully evaluated sub-expression.
     *
     *
     * Ordering consequence:
     *
     *   first pop  = RIGHT operand
     *   second pop = LEFT operand
     *
     * because the right operand appears later in postfix notation,
     * so it is closer to the top.
     *
     *
     * Size transition:
     *
     *   operand:
     *       +1 stack element
     *
     *   operator:
     *       pop 2, push 1
     *       net -1
     *
     * =================================================================================
     */


    /**
     * =================================================================================
     * 6️⃣ REUSABLE REDUCTION SKELETON
     * =================================================================================
     *
     * Stack<State> stack = new Stack<>();
     *
     * for (each input) {
     *
     *     if (input is an operator) {
     *
     *         State right = stack.pop();
     *         State left  = stack.pop();
     *
     *         stack.push(combine(left, right, input));
     *
     *     } else {
     *
     *         stack.push(state);
     *     }
     * }
     *
     * return stack.pop();
     *
     *
     * Customize only:
     *
     *   1. What creates a completed state?
     *   2. How many previous states does an operation consume?
     *   3. In what order are they consumed?
     *   4. How are they combined?
     *
     *
     * THIS PROBLEM:
     *
     *   completed state -> integer value
     *   operator consumes -> 2 values
     *   first pop -> right
     *   second pop -> left
     *   combine -> arithmetic operation
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 7️⃣ PRIMARY IMPLEMENTATION
     * =================================================================================
     */
    static class Optimal {

        public int evalRPN(String[] tokens) {

            Deque<Integer> stack = new ArrayDeque<>();

            for (String token : tokens) {

                if ("+-*/".contains(token)) {

                    int right = stack.pop();
                    int left = stack.pop();

                    switch (token) {
                        case "+" -> stack.push(left + right);
                        case "-" -> stack.push(left - right);
                        case "*" -> stack.push(left * right);
                        case "/" -> stack.push(left / right);
                    }

                } else {

                    stack.push(Integer.parseInt(token));
                }
            }

            return stack.pop();
        }
    }


    /*
     * =================================================================================
     * 8️⃣ FULL STATE-EVOLUTION DRY RUN
     * =================================================================================
     *
     * tokens:
     *
     *   ["2","1","+","3","*"]
     *
     * stack shown bottom -> top.
     *
     * ========================================================================
     * TOKEN | ACTION                           | STACK
     * ========================================================================
     * "2"   | push 2                           | [2]
     * "1"   | push 1                           | [2,1]
     * "+"   | right=1, left=2, push 2+1=3     | [3]
     * "3"   | push 3                           | [3,3]
     * "*"   | right=3, left=3, push 3*3=9     | [9]
     * ========================================================================
     *
     * Final stack contains exactly one completed expression:
     *
     *   9
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 9️⃣ FOCUSED HARD-PART TRACE — WHY RIGHT IS POPPED FIRST
     * =================================================================================
     *
     * Example:
     *
     *   5 2 -
     *
     * Before "-":
     *
     *   bottom -> [5,2] <- top
     *
     * The token closest to the operator is 2.
     *
     * Therefore:
     *
     *   first pop  = 2 = right
     *   second pop = 5 = left
     *
     * Correct:
     *
     *   left - right
     *   5 - 2
     *   = 3
     *
     * Wrong:
     *
     *   right - left
     *   2 - 5
     *   = -3
     *
     * Same issue for division.
     *
     * Addition and multiplication hide this bug because they are commutative.
     *
     * RECALL:
     *
     *   FIRST POP  = RIGHT
     *   SECOND POP = LEFT
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 🔟 HIGH-ROI NUANCES
     * =================================================================================
     *
     * 1. Operator detection stays literal:
     *
     *    check whether token is one of the four operator symbols.
     *
     *    So "-11" is not mistaken for the minus operator.
     *    It falls into the number branch and parses normally.
     *
     *
     * 2. Java integer division already truncates toward zero.
     *
     *        7 / -3 == -2
     *
     *
     * 3. No precedence handling is needed.
     *
     *    RPN already encodes evaluation order.
     *
     *
     * 4. Java `assert` is disabled unless the JVM runs with -ea.
     *
     *    Therefore the tests below use explicit checks instead of `assert`,
     *    so they really verify the implementation on a normal run.
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣1️⃣ CORRECTNESS + COMPLEXITY
     * =================================================================================
     *
     * Correctness:
     *
     * Base:
     *
     *   A number is a fully evaluated expression, so pushing it preserves
     *   the invariant.
     *
     * Operator step:
     *
     *   RPN guarantees both operands have already appeared.
     *
     *   By the invariant, the stack contains completed sub-expressions.
     *   The two newest completed expressions are exactly the operator's
     *   right and left operands.
     *
     *   Combining them creates another completed expression, so pushing
     *   the result preserves the invariant.
     *
     * End:
     *
     *   A valid complete expression collapses to one stack value.
     *   That value is the result of the entire expression.
     *
     *
     * Complexity:
     *
     *   each token is processed once
     *   each stack push/pop is O(1)
     *
     *   Time  = O(n)
     *   Space = O(n)
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣2️⃣ SAME-FAMILY VARIANTS
     * =================================================================================
     *
     * Prefix evaluation:
     *
     *   same reduction invariant
     *   traverse right -> left
     *
     *
     * Floating-point RPN:
     *
     *   same invariant
     *   Integer -> Double
     *
     *
     * Custom operators:
     *
     *   same invariant
     *   extend combination logic
     *
     *
     * Operator with different arity:
     *
     *   same reduction idea
     *   number of consumed stack states changes
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣3️⃣ ±Δ — WHEN THE PATTERN SURVIVES / BREAKS
     * =================================================================================
     *
     * +Δ SAME CORE
     *
     * Postfix with "^"
     *
     *   only combination rule changes
     *
     *
     * Prefix notation
     *
     *   traversal direction changes
     *   reduction invariant survives
     *
     *
     * -------------------------------------------------------------------------
     * -Δ PATTERN BREAKS
     * -------------------------------------------------------------------------
     *
     * Infix:
     *
     *   "2 + 3 * 4"
     *
     * Current operator cannot always execute immediately.
     * Precedence must first be resolved.
     *
     * Therefore the simple:
     *
     *   operand -> push
     *   operator -> pop two immediately
     *
     * invariant is insufficient.
     *
     *
     * Expression tree already given:
     *
     *   structure already stores dependency
     *   recursive/tree traversal may be more natural than token stack simulation
     *
     * =================================================================================
     */


    /*
     * =================================================================================
     * 1️⃣4️⃣ 30-SECOND RECONSTRUCTION + INTERVIEW ARTICULATION
     * =================================================================================
     *
     * Reconstruction:
     *
     *   postfix
     *      ↓
     *   operator sees operands already completed
     *      ↓
     *   needs two MOST RECENT completed values
     *      ↓
     *   stack
     *
     *   operator?
     *       right = pop
     *       left  = pop
     *       apply
     *       push
     *
     *   else:
     *       parse number
     *       push
     *
     *   return final value
     *
     *
     * Interview sentence:
     *
     *   "I keep a stack where every value represents one fully evaluated
     *    sub-expression. If the token is an operator, I pop right, then left,
     *    apply the operator, and push the result. Otherwise I parse the number
     *    and push it. Each token is processed once, so the solution is O(n)."
     *
     * =================================================================================
     */


    // =================================================================================
    // 1️⃣5️⃣ SELF-VERIFYING TESTS
    // =================================================================================

    public static void main(String[] args) {

        Optimal solver = new Optimal();

        assertEquals(
                9,
                solver.evalRPN(new String[]{"2", "1", "+", "3", "*"}),
                "representative example"
        );

        assertEquals(
                6,
                solver.evalRPN(new String[]{"4", "13", "5", "/", "+"}),
                "division before addition"
        );

        assertEquals(
                22,
                solver.evalRPN(new String[]{
                        "10", "6", "9", "3", "+", "-11", "*",
                        "/", "*", "17", "+", "5", "+"
                }),
                "official complex example"
        );

        assertEquals(
                42,
                solver.evalRPN(new String[]{"42"}),
                "single operand"
        );

        assertEquals(
                3,
                solver.evalRPN(new String[]{"5", "2", "-"}),
                "subtraction operand order"
        );

        assertEquals(
                4,
                solver.evalRPN(new String[]{"8", "2", "/"}),
                "division operand order"
        );

        assertEquals(
                -2,
                solver.evalRPN(new String[]{"7", "-3", "/"}),
                "division truncates toward zero"
        );

        assertEquals(
                -8,
                solver.evalRPN(new String[]{"-2", "4", "*"}),
                "negative operand"
        );

        assertEquals(
                14,
                solver.evalRPN(new String[]{
                        "5", "1", "2", "+", "4", "*", "+", "3", "-"
                }),
                "mixed operators"
        );

        System.out.println("ALL TESTS PASSED");
    }

    private static void assertEquals(
            int expected,
            int actual,
            String name) {

        if (expected != actual) {
            throw new AssertionError(
                    name
                            + " expected=" + expected
                            + " actual=" + actual
            );
        }
    }
}
