package org.chijai.day10.session2;

/**
 * LeetCode 268 - Missing Number
 *
 * ================================================================
 * 1. PROBLEM IN VERY SIMPLE WORDS
 * ================================================================
 *
 * Imagine numbered cards from 0 through n.
 *
 * If n = 3, the complete card collection is:
 *
 *     [0, 1, 2, 3]
 *
 * Someone removes exactly one card and gives us the remaining cards in any
 * order. We must find the removed card.
 *
 * Example:
 *
 *     nums = [3, 0, 1]
 *
 * The array contains 3 numbers, so n = 3. The complete range must therefore
 * be [0, 1, 2, 3]. Comparing the two collections shows that 2 is missing.
 *
 * Important guarantees from the problem:
 *
 * 1. nums contains n values.
 * 2. Every value is between 0 and n, inclusive.
 * 3. Values do not repeat.
 * 4. Exactly one value from 0 through n is missing.
 *
 * ================================================================
 * 2. FIRST-PRINCIPLES SOLUTIONS
 * ================================================================
 *
 * Start with the meaning of the problem. We have:
 *
 *     expected collection = every number from 0 through n
 *     actual collection   = every number stored in nums
 *
 * We need the value present in the expected collection but absent from the
 * actual collection.
 *
 * SIMPLE APPROACH A: HASH SET
 *
 * Put every value from nums into a set. Then check 0, 1, 2, ..., n and return
 * the first value that is not in the set.
 *
 *     Time:  O(n)
 *     Space: O(n)
 *
 * This is easy to invent and is a valid starting answer in an interview.
 *
 * SIMPLE APPROACH B: SUM DIFFERENCE
 *
 * Add every expected value and subtract every actual value.
 *
 * For nums = [3, 0, 1]:
 *
 *     expected sum = 0 + 1 + 2 + 3 = 6
 *     actual sum   = 3 + 0 + 1     = 4
 *     missing      = 6 - 4         = 2
 *
 * This gives O(n) time and O(1) extra space. In problems with larger numeric
 * limits, however, a sum can overflow its integer type. XOR gives us the same
 * cancellation idea without adding large totals.
 *
 * ================================================================
 * 3. XOR FROM FIRST PRINCIPLES
 * ================================================================
 *
 * XOR means "different bits produce 1; equal bits produce 0."
 *
 *     0 ^ 0 = 0
 *     0 ^ 1 = 1
 *     1 ^ 0 = 1
 *     1 ^ 1 = 0
 *
 * Apply that rule to every bit of an integer. Two important results follow:
 *
 *     x ^ x = 0      A number XOR itself cancels completely.
 *     x ^ 0 = x      XOR with zero leaves the number unchanged.
 *
 * Example with decimal 3, whose binary form is 011:
 *
 *       011    (3)
 *     ^ 011    (3)
 *     -----
 *       000    (0)
 *
 * XOR is also commutative and associative. This means order and grouping do
 * not matter:
 *
 *     a ^ b = b ^ a
 *     (a ^ b) ^ c = a ^ (b ^ c)
 *
 * Therefore, matching values can be mentally moved next to each other and
 * cancelled even when the input array is in a random order.
 *
 * ================================================================
 * 4. TURN THE PROBLEM INTO PAIR CANCELLATION
 * ================================================================
 *
 * XOR every expected number with every actual number.
 *
 * For nums = [3, 0, 1], n = 3:
 *
 *     expected = 0 ^ 1 ^ 2 ^ 3
 *     actual   = 3 ^ 0 ^ 1
 *
 * Combine them:
 *
 *     0 ^ 1 ^ 2 ^ 3 ^ 3 ^ 0 ^ 1
 *
 * Regroup equal values because XOR order does not matter:
 *
 *     (0 ^ 0) ^ (1 ^ 1) ^ (3 ^ 3) ^ 2
 *          0  ^      0  ^      0  ^ 2
 *
 * Everything that appears in both collections cancels. The missing number 2
 * appears only in the expected collection, so it is the only value left.
 *
 * Think of matching socks: every matching pair is removed. The one unpaired
 * sock is the answer.
 *
 * ================================================================
 * 5. WHY DOES THE CODE START WITH nums.length?
 * ================================================================
 *
 * If nums contains n values, the expected values are:
 *
 *     0, 1, 2, ..., n - 1, n
 *
 * The loop index naturally produces only:
 *
 *     0, 1, 2, ..., n - 1
 *
 * The loop never reaches index n. We therefore put n into the accumulator
 * before the loop:
 *
 *     int missing = nums.length;
 *
 * Now the starting value n plus all loop indices 0 through n - 1 represent
 * the complete expected range 0 through n.
 *
 * ================================================================
 * 6. LINE-BY-LINE DRY RUN
 * ================================================================
 *
 * Input:
 *
 *     nums = [3, 0, 1]
 *     n = nums.length = 3
 *
 * Start:
 *
 *     missing = 3
 *
 * index = 0, nums[0] = 3:
 *
 *     missing = 3 ^ 0 ^ 3 = 0
 *
 * index = 1, nums[1] = 0:
 *
 *     missing = 0 ^ 1 ^ 0 = 1
 *
 * index = 2, nums[2] = 1:
 *
 *     missing = 1 ^ 2 ^ 1 = 2
 *
 * Return 2.
 *
 * The intermediate values are not meaningful partial answers. The invariant
 * is that missing contains the XOR of everything processed so far. Only after
 * all expected and actual values are included have all matching pairs had a
 * chance to cancel.
 *
 * ================================================================
 * 7. MORE EDGE-CASE EXAMPLES
 * ================================================================
 *
 * Missing n:
 *
 *     nums = [0, 1]
 *     expected range = [0, 1, 2]
 *     answer = 2
 *
 * Missing zero:
 *
 *     nums = [1]
 *     expected range = [0, 1]
 *     answer = 0
 *
 * One-element range with n missing:
 *
 *     nums = [0]
 *     expected range = [0, 1]
 *     answer = 1
 *
 * ================================================================
 * 8. INTERVIEW DERIVATION AND DEFENSE
 * ================================================================
 *
 * Do not pretend that XOR appeared magically. Explain the progression:
 *
 * 1. "I can use a HashSet and scan 0 through n. That is O(n) extra space."
 * 2. "I can remove the extra space using expected sum minus actual sum."
 * 3. "The deeper operation is pair cancellation. XOR also cancels equal
 *    values, uses O(1) space, and does not build a potentially large sum."
 *
 * Interview explanation:
 *
 * "The expected set is 0 through n, and nums contains every expected value
 * except one. I XOR the expected values with the actual values. Every present
 * value occurs twice and cancels because x XOR x is zero. The missing value
 * occurs only once and remains. I initialize with n because loop indices
 * contribute only 0 through n minus 1. This is O(n) time and O(1) space."
 *
 * ================================================================
 * 9. COMPLEXITY
 * ================================================================
 *
 * Time: O(n)
 *     We visit every array element exactly once.
 *
 * Extra space: O(1)
 *     We keep only the accumulator and loop index, regardless of input size.
 */
public class MissingNumber {

    public int missingNumber(int[] nums) {
        // Include n, because loop indices will contribute only 0 through n - 1.
        int missing = nums.length;

        for (int index = 0; index < nums.length; index++) {
            // Add one expected value from the range: index.
            missing ^= index;

            // Add one actual array value. Matching expected/actual values cancel.
            missing ^= nums[index];
        }

        // Every matching pair is now cancelled; only the missing value remains.
        return missing;
    }
}
