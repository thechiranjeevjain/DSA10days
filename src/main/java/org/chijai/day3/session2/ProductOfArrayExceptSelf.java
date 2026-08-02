package org.chijai.day3.session2;

import java.util.Arrays;

public class ProductOfArrayExceptSelf {

    /*
     * ============================================================
     * 2. 📘 PRIMARY PROBLEM
     * ============================================================
     *
     * Title:
     * Product of Array Except Self
     *
     * Difficulty:
     * Medium
     *
     * Tags:
     * Array
     * Prefix Product
     * Suffix Product
     * Prefix/Suffix Pattern
     * Space Optimization
     *
     * Problem:
     *
     * Given an integer array nums, return an array answer such that:
     *
     * answer[i] =
     * product of every element of nums except nums[i].
     *
     * Requirements:
     *
     * • O(n) time
     * • No division operation
     *
     * The product of every prefix or suffix is guaranteed to fit inside
     * a signed 32-bit integer.
     *
     * Constraints:
     *
     * 2 <= nums.length <= 100000
     * -30 <= nums[i] <= 30
     *
     * Examples:
     *
     * nums = [1,2,3,4]
     * answer = [24,12,8,6]
     *
     * nums = [-1,1,0,-3,3]
     * answer = [0,0,9,0,0]
     *
     * Important observations:
     *
     * • Division is forbidden.
     * • Array may contain zero(s).
     * • We need every index independently.
     *
     * Official:
     * https://leetcode.com/problems/product-of-array-except-self/
     */


    /*
     * ============================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * ============================================================
     *
     * Pattern:
     * Prefix Product + Suffix Product
     *
     * Archetype:
     * Two directional accumulation.
     *
     * Core Invariant:
     *
     * At every index i,
     *
     * answer[i]
     * =
     * (product of everything left of i)
     * ×
     * (product of everything right of i)
     *
     * We never actually need the current element.
     *
     * Why it works:
     *
     * Every element contributes to every other answer.
     *
     * Instead of asking:
     *
     * "What should be excluded?"
     *
     * ask:
     *
     * "What definitely belongs to the left?"
     *
     * and
     *
     * "What definitely belongs to the right?"
     *
     * Multiplying those two independent products automatically excludes
     * nums[i].
     *
     * Recognition signals:
     *
     * • Need information excluding current position.
     * • Division forbidden.
     * • Entire array contributes.
     * • Associative operation (multiplication).
     * • O(n²) brute force is obvious.
     *
     * When to use:
     *
     * • Product except self
     * • Prefix contribution
     * • Suffix contribution
     * • Left/right accumulation
     * • Bidirectional preprocessing
     *
     * When NOT to use:
     *
     * • Dynamic window problems
     * • Non-associative operations
     * • Order-dependent transitions
     * • Problems needing arbitrary interval updates
     *
     * Comparison:
     *
     * Prefix Sum
     * --------------------------
     * combines by addition.
     *
     * Prefix Product
     * --------------------------
     * combines by multiplication.
     *
     * Sliding Window
     * --------------------------
     * maintains one moving interval.
     *
     * Prefix/Suffix
     * --------------------------
     * computes independent cumulative information.
     */


    /*
     * ============================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * ============================================================
     *
     * Mental Model
     * ------------
     *
     * Imagine cutting the array at index i.
     *
     *                 i
     *                 ↓
     *
     * [ left part ] nums[i] [ right part ]
     *
     * Ignore the middle.
     *
     * Multiply:
     *
     * left product
     * ×
     * right product
     *
     * That is exactly the required answer.
     *
     * ------------------------------------------------------------
     * Invariant 1
     * ------------------------------------------------------------
     *
     * Prefix[i]
     *
     * always stores
     *
     * product of every element BEFORE i.
     *
     * Never including nums[i].
     *
     * Example:
     *
     * nums
     *
     * 1 2 3 4
     *
     * prefix
     *
     * 1
     * 1
     * 2
     * 6
     *
     * prefix[3]
     *
     * =
     *
     * 1×2×3
     *
     * ------------------------------------------------------------
     * Invariant 2
     * ------------------------------------------------------------
     *
     * Suffix[i]
     *
     * stores
     *
     * product of every element AFTER i.
     *
     * Never including nums[i].
     *
     * Example:
     *
     * nums
     *
     * 1 2 3 4
     *
     * suffix
     *
     * 24
     * 12
     * 4
     * 1
     *
     * ------------------------------------------------------------
     * Invariant 3
     * ------------------------------------------------------------
     *
     * answer[i]
     *
     * =
     *
     * prefix[i]
     * ×
     * suffix[i]
     *
     * Since neither contains nums[i],
     * the exclusion happens automatically.
     *
     * ------------------------------------------------------------
     * Variable meanings
     * ------------------------------------------------------------
     *
     * prefix[i]
     *
     * Left product.
     *
     * suffix[i]
     *
     * Right product.
     *
     * suffixRunning
     *
     * Product accumulated while scanning
     * from right to left.
     *
     * answer
     *
     * Final output.
     *
     * ------------------------------------------------------------
     * Allowed transitions
     * ------------------------------------------------------------
     *
     * Left pass:
     *
     * prefix[i]
     * =
     * prefix[i-1] × nums[i-1]
     *
     * Right pass:
     *
     * suffix[i]
     * =
     * suffix[i+1] × nums[i+1]
     *
     * ------------------------------------------------------------
     * Forbidden transition
     * ------------------------------------------------------------
     *
     * Never multiply nums[i]
     * into its own answer.
     *
     * That violates the invariant immediately.
     *
     * ------------------------------------------------------------
     * Termination
     * ------------------------------------------------------------
     *
     * Left scan reaches n.
     *
     * Right scan reaches -1.
     *
     * Every answer is finalized exactly once.
     *
     * ------------------------------------------------------------
     * Why the naïve solution fails
     * ------------------------------------------------------------
     *
     * Brute force recomputes almost identical products.
     *
     * Example:
     *
     * For index 5,
     * product of first five numbers is recomputed.
     *
     * For index 6,
     * the same prefix is recomputed again.
     *
     * Massive repeated work.
     *
     * Prefix/suffix accumulation removes this redundancy.
     */


    /*
     * ============================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * ============================================================
     *
     * Mistake 1
     * ---------
     * Use totalProduct / nums[i]
     *
     * Looks correct because mathematically:
     *
     * total / current
     *
     * equals remaining product.
     *
     * Fails because:
     *
     * • division forbidden
     * • zeros break correctness
     *
     * Counterexample:
     *
     * [1,2,0,4]
     *
     * total product = 0
     *
     * Every division answer becomes meaningless.
     *
     * ------------------------------------------------------------
     * Mistake 2
     * ------------------------------------------------------------
     *
     * Build prefix including current element.
     *
     * Example:
     *
     * prefix[i]
     * =
     * prefix[i-1] * nums[i]
     *
     * Then multiply suffix.
     *
     * Current element appears once.
     *
     * Violated invariant:
     *
     * Prefix must exclude current index.
     *
     * ------------------------------------------------------------
     * Mistake 3
     * ------------------------------------------------------------
     *
     * Incorrect initialization.
     *
     * prefix[0] = 0
     *
     * instead of
     *
     * prefix[0] = 1
     *
     * Product identity is one.
     *
     * Zero destroys every multiplication afterwards.
     *
     * ------------------------------------------------------------
     * Mistake 4
     * ------------------------------------------------------------
     *
     * Forget that first element has
     * no left product.
     *
     * Forget that last element has
     * no right product.
     *
     * Identity element:
     *
     * 1
     *
     * not
     * 0
     *
     * ------------------------------------------------------------
     * Interview trap
     * ------------------------------------------------------------
     *
     * After obtaining O(n) time,
     * interviewer asks:
     *
     * "Can you reduce extra space?"
     *
     * Expected improvement:
     *
     * Reuse answer array as prefix storage,
     * then multiply by one running suffix product.
     */


    /*
     * ============================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * ============================================================
     *
     * Mechanical typing order (Optimal)
     *
     * 1.
     * Handle size.
     *
     * 2.
     * Allocate answer.
     *
     * 3.
     * answer[0] = 1.
     *
     * 4.
     * Left pass:
     *
     * answer[i]
     * =
     * answer[i-1]
     * ×
     * nums[i-1]
     *
     * 5.
     * suffixRunning = 1.
     *
     * 6.
     * Traverse from right.
     *
     * answer[i]
     * *= suffixRunning
     *
     * 7.
     * Update suffixRunning
     * using nums[i].
     *
     * 8.
     * Return answer.
     *
     * Nothing else is required.
     */


    /*
     * ============================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * ============================================================
     *
     * prefix[0] = 1
     *
     * build left products
     *
     * suffix = 1
     *
     * traverse right
     *
     * answer *= suffix
     *
     * update suffix
     *
     * return answer
     */


    /*
     * ============================================================
     * 6. SOLUTION CLASSES
     * ============================================================
     */


    static class BruteForce {

        /*
         * Idea
         * ----
         *
         * For every index,
         * multiply every other element.
         *
         * Invariant
         * ---------
         *
         * Current index is skipped.
         *
         * Limitation
         * ----------
         *
         * Recomputes nearly identical products.
         *
         * Complexity
         * ----------
         *
         * Time : O(n²)
         * Space: O(1)
         *
         * Interview usefulness
         * --------------------
         *
         * Good starting point before optimization.
         */

        static int[] productExceptSelf(int[] nums) {

            int n = nums.length;

            int[] answer = new int[n];

            for (int i = 0; i < n; i++) {

                int product = 1;

                for (int j = 0; j < n; j++) {

                    if (i != j) {
                        product *= nums[j];
                    }
                }

                answer[i] = product;
            }

            return answer;
        }
    }


    static class Improved {        /*
     * Idea
     * ----
     *
     * Precompute:
     *
     * • prefix product before every index
     * • suffix product after every index
     *
     * Then combine them.
     *
     * Invariant
     * ---------
     *
     * prefixLeftProduct[i]
     * contains the product strictly before i.
     *
     * suffixRightProduct[i]
     * contains the product strictly after i.
     *
     * Therefore:
     *
     * answer[i]
     * =
     * prefixLeftProduct[i]
     * ×
     * suffixRightProduct[i]
     *
     * Improvement
     * -----------
     *
     * Removes repeated multiplication by storing reusable state.
     *
     * Complexity
     * ----------
     *
     * Time : O(n)
     * Space: O(n)
     *
     * Interview usefulness
     * --------------------
     *
     * Natural stepping stone toward the optimal O(1) extra-space
     * solution.
     */

        static int[] productExceptSelf(int[] nums) {

            final int n = nums.length;

            int[] prefixLeftProduct = new int[n];
            int[] suffixRightProduct = new int[n];
            int[] answer = new int[n];

            prefixLeftProduct[0] = 1;

            for (int i = 1; i < n; i++) {

                // 🟢 Invariant:
                // Product of every element strictly before i.
                prefixLeftProduct[i] =
                        prefixLeftProduct[i - 1] * nums[i - 1];
            }

            suffixRightProduct[n - 1] = 1;

            for (int i = n - 2; i >= 0; i--) {

                // 🟢 Invariant:
                // Product of every element strictly after i.
                suffixRightProduct[i] =
                        suffixRightProduct[i + 1] * nums[i + 1];
            }

            for (int i = 0; i < n; i++) {

                // 🟢 Left contribution × Right contribution.
                answer[i] =
                        prefixLeftProduct[i] * suffixRightProduct[i];
            }

            return answer;
        }
    }


    static class Optimal {

        /*
         * Idea
         * ----
         *
         * Instead of storing both prefix and suffix arrays,
         * reuse the answer array.
         *
         * Pass 1
         * ------
         *
         * answer[i]
         * stores the left product.
         *
         * Pass 2
         * ------
         *
         * Maintain one running suffix product.
         *
         * Multiply it directly into answer[i].
         *
         * Invariant
         * ---------
         *
         * Before entering index i during the right-to-left scan:
         *
         * answer[i]
         * already equals
         *
         * left product.
         *
         * suffixRunning
         * already equals
         *
         * right product.
         *
         * Therefore:
         *
         * answer[i]
         * *=
         * suffixRunning
         *
         * immediately becomes the final answer.
         *
         * Correctness
         * -----------
         *
         * Every element contributes exactly once
         * from the left
         * and exactly once
         * from the right.
         *
         * Current element never contributes
         * to its own answer.
         *
         * Complexity
         * ----------
         *
         * Time : O(n)
         *
         * Extra Space : O(1)
         *
         * (Output array excluded.)
         *
         * Interview usefulness
         * --------------------
         *
         * This is the expected interview solution.
         */

        static int[] productExceptSelf(int[] nums) {

            final int n = nums.length;

            int[] answer = new int[n];

            // 🟢 Identity for multiplication.
            answer[0] = 1;

            for (int i = 1; i < n; i++) {

                // 🟢 Invariant:
                // answer[i] stores product before i.
                answer[i] =
                        answer[i - 1] * nums[i - 1];
            }

            int suffixRunning = 1;

            for (int i = n - 1; i >= 0; i--) {

                // 🟢 Left product already exists.
                // suffixRunning is the right product.
                answer[i] *= suffixRunning;

                // 🟢 Extend suffix for the next index.
                suffixRunning *= nums[i];
            }

            return answer;
        }
    }


/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Invariant
 * ---------
 *
 * Every answer consists of exactly two independent parts:
 *
 * left product
 * ×
 * right product.
 *
 * The current element belongs to neither part.
 *
 * ------------------------------------------------------------
 * Discard Rule
 * ------------------------------------------------------------
 *
 * We never explicitly remove nums[i].
 *
 * Instead,
 * we never insert it into either accumulated product.
 *
 * Exclusion is achieved by construction,
 * not subtraction.
 *
 * ------------------------------------------------------------
 * Correctness
 * ------------------------------------------------------------
 *
 * Left scan guarantees every earlier element contributes.
 *
 * Right scan guarantees every later element contributes.
 *
 * No element contributes twice.
 *
 * No element is omitted except the current one.
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * Two linear scans.
 *
 * Every index is finalized exactly once.
 *
 * ------------------------------------------------------------
 * In-place feasibility
 * ------------------------------------------------------------
 *
 * Yes.
 *
 * The output array doubles as the prefix array.
 *
 * Only one additional running suffix product is required.
 *
 * ------------------------------------------------------------
 * Streaming feasibility
 * ------------------------------------------------------------
 *
 * No.
 *
 * A single left-to-right stream never knows
 * future right-side products.
 *
 * At least one reverse traversal or equivalent
 * stored information is necessary.
 *
 * ------------------------------------------------------------
 * When NOT to use
 * ------------------------------------------------------------
 *
 * If the operation is not associative,
 * or if left/right decomposition is impossible,
 * this pattern breaks down.
 */
/*
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 *
 * "Product of everything except current element."
 *
 * Invariant
 * ---------
 *
 * answer[i]
 * =
 * left product
 * ×
 * right product
 *
 * Search Target
 * -------------
 *
 * Build left contribution once.
 *
 * Build right contribution once.
 *
 * Combine.
 *
 * Discard Rule
 * ------------
 *
 * Never insert nums[i]
 * into either accumulated product.
 *
 * Common Trap
 * -----------
 *
 * Prefix accidentally includes nums[i].
 *
 * Edge Cases
 * ----------
 *
 * • Two elements
 * • One zero
 * • Multiple zeros
 * • Negative values
 * • All ones
 * • Large prefix products
 *
 * One-liner
 * ---------
 *
 * Prefix before me
 * ×
 * Suffix after me.
 *
 * Re-derivation Cue
 * -----------------
 *
 * Cut the array at i.
 *
 * Ignore the middle.
 *
 * Multiply both sides.
 */


/*
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * ------------------------------------------------------------
 * Variation 1
 * ------------------------------------------------------------
 *
 * O(n²) Brute Force
 *
 * Pattern:
 * None.
 *
 * Preserves correctness.
 *
 * Fails efficiency.
 *
 * ------------------------------------------------------------
 * Variation 2
 * ------------------------------------------------------------
 *
 * Prefix + Suffix arrays.
 *
 * Preserves the invariant exactly.
 *
 * Uses O(n) auxiliary space.
 *
 * Easier to derive.
 *
 * ------------------------------------------------------------
 * Variation 3
 * ------------------------------------------------------------
 *
 * Prefix inside answer array.
 *
 * Running suffix variable.
 *
 * Same invariant.
 *
 * Better space complexity.
 *
 * ------------------------------------------------------------
 * Variation 4
 * ------------------------------------------------------------
 *
 * Division-based solution.
 *
 * Looks attractive.
 *
 * Pattern breaks because:
 *
 * • division forbidden
 * • zeros invalidate logic
 *
 * ------------------------------------------------------------
 * Variation 5
 * ------------------------------------------------------------
 *
 * Sum Except Self
 *
 * Instead of multiplication,
 * use addition.
 *
 * Identity changes:
 *
 * 0 instead of 1.
 *
 * ------------------------------------------------------------
 * Variation 6
 * ------------------------------------------------------------
 *
 * Prefix XOR / Prefix AND
 *
 * Works only if the operation
 * is associative and admits
 * left/right decomposition.
 *
 * ------------------------------------------------------------
 * Pattern Boundary
 * ------------------------------------------------------------
 *
 * This pattern assumes:
 *
 * State(i)
 * =
 * LeftState(i)
 * ⊗
 * RightState(i)
 *
 * where ⊗ is associative.
 *
 * If state depends on ordering,
 * history,
 * or arbitrary interval updates,
 * another pattern should be chosen.
 */


/*
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * Can I state the invariant?
 *
 * YES.
 *
 * Every answer equals:
 *
 * left product
 * ×
 * right product.
 *
 * ------------------------------------------------------------
 * Can I identify the search target?
 *
 * YES.
 *
 * Compute reusable contributions
 * instead of recomputing products.
 *
 * ------------------------------------------------------------
 * Can I explain the discard rule?
 *
 * YES.
 *
 * The current element is never added
 * to either cumulative product.
 *
 * ------------------------------------------------------------
 * Can I justify termination?
 *
 * YES.
 *
 * Two complete linear traversals.
 *
 * ------------------------------------------------------------
 * Can I explain why brute force fails?
 *
 * YES.
 *
 * It repeatedly recomputes
 * identical prefix and suffix products.
 *
 * ------------------------------------------------------------
 * Can I list important edge cases?
 *
 * YES.
 *
 * • one zero
 * • two zeros
 * • negatives
 * • two elements
 * • repeated values
 *
 * ------------------------------------------------------------
 * Am I debugging ready?
 *
 * YES.
 *
 * Verify:
 *
 * prefix starts with 1.
 *
 * suffix starts with 1.
 *
 * Prefix excludes current.
 *
 * Suffix excludes current.
 *
 * Right scan updates
 * answer BEFORE suffixRunning.
 *
 * ------------------------------------------------------------
 * Am I variant ready?
 *
 * YES.
 *
 * Can derive:
 *
 * • brute force
 * • prefix/suffix arrays
 * • O(1) extra-space version
 *
 * ------------------------------------------------------------
 * Do I know the pattern boundary?
 *
 * YES.
 *
 * This works because
 * multiplication is associative
 * and decomposes into
 * independent left/right contributions.
 */


/*
 * ============================================================
 * ⚫ PATTERN MAPPING
 * ============================================================
 *
 * Similar Problems
 * ----------------
 *
 * • Trapping Rain Water
 *   (left max + right max)
 *
 * • Best Time to Buy and Sell Stock
 *   (running minimum)
 *
 * • Prefix Sum
 *
 * • Range Sum Query
 *
 * • Maximum Difference
 *
 * • Equilibrium Index
 *
 * Shared Theme
 * ------------
 *
 * Precompute reusable state
 * instead of recomputing
 * local answers independently.
 */


/*
 * ============================================================
 * ⚫ DEBUGGING GUIDE
 * ============================================================
 *
 * Symptom
 * -------
 *
 * Every answer becomes zero.
 *
 * Check
 * -----
 *
 * Prefix or suffix initialized
 * with zero instead of one.
 *
 * ------------------------------------------------------------
 * Symptom
 * -------
 *
 * Every answer contains itself.
 *
 * Check
 * -----
 *
 * Prefix transition should use:
 *
 * nums[i - 1]
 *
 * not
 *
 * nums[i]
 *
 * ------------------------------------------------------------
 * Symptom
 * -------
 *
 * Rightmost answer incorrect.
 *
 * Check
 * -----
 *
 * suffixRunning
 * must initially equal one.
 *
 * ------------------------------------------------------------
 * Symptom
 * -------
 *
 * Entire array shifted.
 *
 * Check
 * -----
 *
 * Prefix assignment is off by one.
 *
 * ------------------------------------------------------------
 * Symptom
 * -------
 *
 * Last pass produces incorrect values.
 *
 * Check
 * -----
 *
 * Correct order:
 *
 * answer[i] *= suffixRunning;
 *
 * suffixRunning *= nums[i];
 *
 * Never reverse these statements.
 */
    /*
     * ============================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * ============================================================
     */

    public static void main(String[] args) {

        // Representative example.
        assert Arrays.equals(
                Optimal.productExceptSelf(
                        new int[]{1, 2, 3, 4}),
                new int[]{24, 12, 8, 6});

        // Example containing one zero.
        assert Arrays.equals(
                Optimal.productExceptSelf(
                        new int[]{-1, 1, 0, -3, 3}),
                new int[]{0, 0, 9, 0, 0});

        // Smallest valid array.
        assert Arrays.equals(
                Optimal.productExceptSelf(
                        new int[]{2, 3}),
                new int[]{3, 2});

        // All elements identical.
        assert Arrays.equals(
                Optimal.productExceptSelf(
                        new int[]{5, 5, 5, 5}),
                new int[]{125, 125, 125, 125});

        // Single zero at beginning.
        assert Arrays.equals(
                Optimal.productExceptSelf(
                        new int[]{0, 2, 3, 4}),
                new int[]{24, 0, 0, 0});

        // Single zero in middle.
        assert Arrays.equals(
                Optimal.productExceptSelf(
                        new int[]{1, 2, 0, 4}),
                new int[]{0, 0, 8, 0});

        // Two zeros.
        assert Arrays.equals(
                Optimal.productExceptSelf(
                        new int[]{0, 2, 0, 4}),
                new int[]{0, 0, 0, 0});

        // Negative values.
        assert Arrays.equals(
                Optimal.productExceptSelf(
                        new int[]{-1, -2, -3, -4}),
                new int[]{-24, -12, -8, -6});

        // Mixed positive and negative.
        assert Arrays.equals(
                Optimal.productExceptSelf(
                        new int[]{2, -3, 4, -5}),
                new int[]{60, -40, 30, -24});

        // All ones.
        assert Arrays.equals(
                Optimal.productExceptSelf(
                        new int[]{1, 1, 1, 1}),
                new int[]{1, 1, 1, 1});

        // Repeated values.
        assert Arrays.equals(
                Optimal.productExceptSelf(
                        new int[]{3, 3, 3}),
                new int[]{9, 9, 9});

        // Increasing sequence.
        assert Arrays.equals(
                Optimal.productExceptSelf(
                        new int[]{2, 3, 4, 5}),
                new int[]{60, 40, 30, 24});

        // Verify improved and optimal implementations agree.
        int[] sample = {7, 1, 5, 2};

        assert Arrays.equals(
                Improved.productExceptSelf(sample),
                Optimal.productExceptSelf(sample));

        // Verify brute force matches optimal.
        assert Arrays.equals(
                BruteForce.productExceptSelf(sample),
                Optimal.productExceptSelf(sample));

        // Boundary with alternating signs.
        assert Arrays.equals(
                Optimal.productExceptSelf(
                        new int[]{-2, 3}),
                new int[]{3, -2});

        // Product identity behavior.
        assert Arrays.equals(
                Optimal.productExceptSelf(
                        new int[]{1, 7}),
                new int[]{7, 1});

        System.out.println("All assertions passed.");
    }

}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/
