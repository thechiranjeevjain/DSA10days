package org.chijai.day3.session2;

/*
████████████████████████████████████████████████████████████████████████
████████████████████  COUNT NUMBER OF NICE SUBARRAYS  ██████████████████
████████████████████████████████████████████████████████████████████████

This file is a COMPLETE algorithm chapter.
It is intentionally long.
It is intentionally explicit.
It is designed to be sufficient months later without reopening LeetCode.
*/

public class NiceSubArrays {

    // =====================================================================
    // 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
    // =====================================================================

    /*
    Count Number of Nice Subarrays
    https://leetcode.com/problems/count-number-of-nice-subarrays/

    Given an array of integers nums and an integer k.
    A continuous subarray is called nice if there are k odd numbers on it.

    Return the number of nice sub-arrays.

    Example 1:
    Input: nums = [1,1,2,1,1], k = 3
    Output: 2
    Explanation: The only sub-arrays with 3 odd numbers are [1,1,2,1] and [1,2,1,1].

    Example 2:
    Input: nums = [2,4,6], k = 1
    Output: 0
    Explanation: There are no odd numbers in the array.

    Example 3:
    Input: nums = [2,2,2,1,2,2,1,2,2,2], k = 2
    Output: 16

    Constraints:
    1 <= nums.length <= 50000
    1 <= nums[i] <= 10^5
    1 <= k <= nums.length

    Seen this question in a real interview before?
    1/5

    Hint 1:
    After replacing each even by zero and every odd by one can we use prefix sum to find answer ?

    Hint 2:
    Can we use two pointers to count number of sub-arrays ?

    Hint 3:
    Can we store the indices of odd numbers and for each k indices count the number of sub-arrays that contains them ?
    */

    // =====================================================================
    // 3️⃣ 🔵 CORE PATTERN OVERVIEW
    // =====================================================================

    /*
    🔵 Pattern Name:
       "Subarrays with Exactly K Property" (Binary Reduction + Counting)

    🔵 Core Idea:
       Convert the array into a binary signal:
       odd → 1
       even → 0

       Then count subarrays whose sum is exactly k.

    🔵 Why It Works:
       Odd count becomes additive and monotonic.
       Every valid subarray is defined by:
       - choosing k odd positions
       - extending left and right over surrounding evens

    🔵 When To Use:
       - "exactly k occurrences"
       - contiguous range
       - binary or reducible-to-binary property

    🔵 Pattern Recognition Signals:
       - exactly k
       - continuous subarray
       - parity / condition count
       - constraints allow O(n)

    🔵 Difference from Sliding Window "≤ k":
       Exact-k requires counting multiple windows,
       not just maintaining feasibility.
    */

    // =====================================================================
    // 4️⃣ 🟢 MENTAL MODEL & INVARIANTS
    // =====================================================================

    /*
    🟢 Mental Model:
       A valid nice subarray is defined by:
       - k odd numbers fixed inside
       - any number of even numbers surrounding them

       Count combinations, not windows.

    🟢 Invariants:
       - Odd count is monotonic when expanding right
       - Exact-k windows have multiple left expansions
       - Each odd index acts as a pivot

    🟢 Variable Roles:
       prefixOddCount → number of odds till index
       frequency[prefix] → how many times we've seen this count
       result → total subarrays satisfying exact-k

    🟢 Termination Logic:
       Single pass through array
       Prefix counts only increase

    🟢 Forbidden Actions:
       ❌ Shrinking window greedily
       ❌ Using HashSet for window state
       ❌ Treating this as unique-window problem

    🟢 Why Alternatives Are Inferior:
       Sliding window alone cannot count all exact matches.
    */

    // =====================================================================
    // 5️⃣ 🔴 WHY YOUR NAIVE / WRONG SOLUTION FAILS
    // =====================================================================

    /*
    🔴 Your Attempt:
       - Used HashSet of odd indices
       - Tried to slide window while counting == k

    🔴 Why It Feels Correct:
       - Tracks odd positions
       - Moves left/right pointers

    🔴 Exact Invariant Violated:
       ❌ Assumes ONE window per right pointer
       ❌ Ignores multiple valid left extensions

    🔴 Counterexample:
       nums = [2,2,1,2,1,2], k = 2

       Valid subarrays overlap heavily.
       Your logic counts at most one per right.

    🔴 Interviewer Trap:
       They want to see if you understand:
       "Exact-k ≠ single window"

       This problem is about counting,
       not maintaining a valid range.
    */

    /*
    🟢 CONFUSION-KILLER WORKED EXAMPLE
    (READ THIS WHEN THE SOLUTION FEELS “MAGICAL”)

    This block exists to answer the following confusions explicitly:

    ❓ Why sliding window felt right but failed
    ❓ Why “oddCount == k” does NOT mean one subarray
    ❓ Where multiplication comes from
    ❓ Why HashSet / pointer juggling is the wrong mental model
    ❓ How this can be INVENTED in an interview (not memorized)

    ------------------------------------------------------------
    Example:
    nums = [1, 2, 2, 1]
    k = 1

    Index:   0  1  2  3
    Value:   1  2  2  1
    Parity:  O  E  E  O

    ------------------------------------------------------------
    STEP 1: Extract ONLY the odd positions (structure first)

    Odd indices are what DEFINE a nice subarray.
    Evens only EXTEND it.

    Add sentinels to avoid boundary special cases.

    oddPositions = [-1, 0, 3, 4]

    Meaning:
    - -1 → virtual odd before array (left boundary)
    -  0 → first real odd
    -  3 → second real odd
    -  4 → virtual odd after array (right boundary)

    This answers the confusion:
    ❓ “Why are we ignoring evens initially?”
    → Because evens never change the odd count.

    ------------------------------------------------------------
    STEP 2: Fix exactly ONE odd (k = 1) and COUNT combinations

    Key mental shift:
    We are NOT sliding a window.
    We are FIXING structure and COUNTING possibilities.

    ------------------------------------------------------------
    CASE 1: Fix odd at index 0

    We must include index 0.
    We must NOT include index 3.

    Valid start positions:
    From (previous odd + 1) to current odd
    = (-1 + 1) to 0
    → count = 0 - (-1) = 1

    Valid end positions:
    From current odd to (next odd - 1)
    = 0 to (3 - 1)
    → count = 3 - 0 = 3

    Total subarrays contributed:
    1 × 3 = 3

    They are:
    [1]
    [1,2]
    [1,2,2]

    ------------------------------------------------------------
    CASE 2: Fix odd at index 3

    Valid start positions:
    = 3 - 0 = 3

    Valid end positions:
    = 4 - 3 = 1

    Total subarrays contributed:
    3 × 1 = 3

    They are:
    [1]
    [2,1]
    [2,2,1]

    ------------------------------------------------------------
    FINAL ANSWER:
    Total nice subarrays = 3 + 3 = 6

    ------------------------------------------------------------
    CRITICAL CONFUSION CLARIFICATIONS

    ❓ “When oddCount == k, why isn’t that just ONE subarray?”
    → Because leading evens and trailing evens create MULTIPLE valid starts/ends.

    ❓ “Why multiplication?”
    → Left choices and right choices are independent.
    Each left choice pairs with every right choice.

    ❓ “Why sliding window alone fails?”
    → Sliding window finds A window.
    This problem asks to COUNT ALL windows.

    ❓ “Why HashSet of odd indices is wrong?”
    → HashSet loses ORDER and DISTANCE.
    Counting requires gaps between odds, not membership.

    ❓ “How could I INVENT this in interview?”
    → Natural progression:
       1. Try sliding window
       2. Notice multiple valid subarrays per window
       3. Ask “how many?”
       4. Realize odds define structure, evens define freedom
       5. Count combinations

    This is NOT a trick.
    This is STRUCTURE → COUNTING.

    If this block makes sense,
    you do NOT need to reread the full solution.
    */


    /*
    🟢 Worked Mental Model Example (DO NOT SKIP)

    nums = [1, 2, 2, 1]
    k = 1

    Index:  0  1  2  3
    Value:  1  2  2  1
    Parity: O  E  E  O

    Step 1: Extract odd positions and add sentinels
    oddPositions = [-1, 0, 3, 4]

    Explanation:
    - -1  → virtual odd before array (left boundary)
    -  0  → first odd
    -  3  → second odd
    -  4  → virtual odd after array (right boundary)

    Step 2: Fix one odd at a time (k = 1)

    Case 1: odd at index 0
    leftChoices  = 0 - (-1) = 1
    rightChoices = 3 - 0     = 3
    Contribution = 1 × 3 = 3 subarrays

    They are:
    [1]
    [1,2]
    [1,2,2]

    Case 2: odd at index 3
    leftChoices  = 3 - 0 = 3
    rightChoices = 4 - 3 = 1
    Contribution = 3 × 1 = 3 subarrays

    They are:
    [1]
    [2,1]
    [2,2,1]

    Total nice subarrays = 3 + 3 = 6
    */


    // =====================================================================
    // 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES
    // =====================================================================

    // --------------------------------------------------
    // 🟥 Brute Force
    // --------------------------------------------------

    static class BruteForce {
        /*
        Core idea:
        Check all subarrays and count odds.

        Time: O(n^2)
        Space: O(1)
        Interview: ❌ Not acceptable
        */

        static int numberOfSubarrays(int[] nums, int k) {
            int count = 0;
            for (int start = 0; start < nums.length; start++) {
                int oddCount = 0;
                for (int end = start; end < nums.length; end++) {
                    if ((nums[end] & 1) == 1) oddCount++;
                    if (oddCount == k) count++;
                }
            }
            return count;
        }
    }

    // --------------------------------------------------
    // 🟡 Improved — Prefix Sum
    // --------------------------------------------------

    static class PrefixSum {
        /*
        Core idea:
        Reduce to prefix sum with hashmap.

        Time: O(n)
        Space: O(n)
        Interview: ✔️ Acceptable
        */

        static int numberOfSubarrays(int[] nums, int k) {
            // prefixOddFrequency[x] = how many prefixes seen with x odd numbers
            int[] prefixOddFrequency = new int[nums.length + 1];

            // Empty prefix has 0 odd numbers
            prefixOddFrequency[0] = 1;

            int currentOddCount = 0;
            int totalNiceSubarrays = 0;

            for (int value : nums) {

                // Update prefix odd count
                if ((value & 1) == 1) {
                    currentOddCount++;
                }

                // We want:
                // currentOddCount - previousOddCount = k
                int requiredPreviousOddCount = currentOddCount - k;

                if (requiredPreviousOddCount >= 0) {
                    totalNiceSubarrays +=
                            prefixOddFrequency[requiredPreviousOddCount];
                }

                // Record current prefix
                prefixOddFrequency[currentOddCount]++;
            }

            return totalNiceSubarrays;
        }
    }

    // --------------------------------------------------
    // 🟢 Optimal — Odd Indices Counting (Interview-Preferred)
    // --------------------------------------------------

    static class Optimal {
        /*
        Core idea:
        Store odd indices.
        Count choices of evens around k odds.

        Time: O(n)
        Space: O(n)
        Interview: ⭐⭐⭐⭐⭐
        */

        static int numberOfSubarrays(int[] nums, int k) {
            // oddIndex[i] = index of the i-th odd number
            // with sentinels at start and end
            int[] oddIndex = new int[nums.length + 2];

            int totalOddCount = 0;

            // Left boundary sentinel
            oddIndex[0] = -1;

            // Step 1: collect positions of all odd numbers
            for (int i = 0; i < nums.length; i++) {
                if ((nums[i] & 1) == 1) {
                    oddIndex[++totalOddCount] = i;
                }
            }

            // Right boundary sentinel
            oddIndex[totalOddCount + 1] = nums.length;

            int totalNiceSubarrays = 0;

            // Step 2: fix k consecutive odd numbers
            for (int startOdd = 1;
                 startOdd + k - 1 <= totalOddCount;
                 startOdd++) {

                int endOdd = startOdd + k - 1;

                // Number of ways to extend on the left using evens
                int leftChoices =
                        oddIndex[startOdd] - oddIndex[startOdd - 1];

                // Number of ways to extend on the right using evens
                int rightChoices =
                        oddIndex[endOdd + 1] - oddIndex[endOdd];

                // Combine independent choices
                totalNiceSubarrays += leftChoices * rightChoices;
            }

            return totalNiceSubarrays;
        }
    }

    // =====================================================================
    // 7️⃣ 🟣 INTERVIEW ARTICULATION
    // =====================================================================

    /*
    🟣 Why Optimal Works:
       Each group of k odds defines a block.
       Surrounding evens multiply choices.

    🟣 Correctness Invariant:
       Every valid subarray has exactly one such odd-group.

    🟣 What Breaks If Changed:
       Removing sentinels breaks boundary cases.

    🟣 In-place Feasible:
       Yes (prefix sum variant).

    🟣 Streaming Feasible:
       Prefix sum approach works streaming.

    🟣 When NOT To Use:
       When property is non-additive.
    */

    // =====================================================================
    // 8️⃣ 🔄 VARIATIONS & TWEAKS
    // =====================================================================

    /*
    🟢 Invariant-Preserving:
       Replace odd with any binary property.

    🟡 Reasoning-Only:
       Use atMost(k) - atMost(k-1).

    🔴 Pattern-Break:
       If subarray is non-contiguous.
    */

    // =====================================================================
    // 9️⃣ ⚫ REINFORCEMENT PROBLEMS
    // =====================================================================

    /*
    Reinforcement Problem 1:
    Binary Subarrays With Sum
    https://leetcode.com/problems/binary-subarrays-with-sum/
    Same exact pattern.
    */

    static class BinarySubarraysWithSum {
        static int numSubarraysWithSum(int[] nums, int goal) {
            int[] freq = new int[nums.length + 1];
            freq[0] = 1;

            int prefix = 0;
            int result = 0;

            for (int num : nums) {
                prefix += num;
                if (prefix - goal >= 0) {
                    result += freq[prefix - goal];
                }
                freq[prefix]++;
            }
            return result;
        }
    }

    // =====================================================================
    // 10️⃣ 🧩 RELATED PROBLEMS
    // =====================================================================

    /*
    Subarrays with K Different Integers
    Pattern breaks → requires two sliding windows.
    */

    // =====================================================================
    // 11️⃣ 🟢 LEARNING VERIFICATION
    // =====================================================================

    /*
    ✔ Can re-derive without code
    ✔ Knows invariant
    ✔ Can detect exact-k problems
    */

    // =====================================================================
    // 12️⃣ 🧪 main() METHOD + SELF-VERIFYING TESTS
    // =====================================================================

    public static void main(String[] args) {
        assertEquals(2,
                Optimal.numberOfSubarrays(
                        new int[]{1, 1, 2, 1, 1}, 3),
                "Example 1");

        assertEquals(0,
                Optimal.numberOfSubarrays(
                        new int[]{2, 4, 6}, 1),
                "Example 2");

        assertEquals(16,
                Optimal.numberOfSubarrays(
                        new int[]{2, 2, 2, 1, 2, 2, 1, 2, 2, 2}, 2),
                "Example 3");

        assertEquals(1,
                Optimal.numberOfSubarrays(
                        new int[]{1}, 1),
                "Single element");

        System.out.println("✅ ALL TESTS PASSED");
    }

    static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(
                    message + " | Expected: " + expected + ", Got: " + actual);
        }
    }

    // =====================================================================
    // 13️⃣ 🧠 CHAPTER COMPLETION CHECKLIST (WITH ANSWERS)
    // =====================================================================

    /*
    • Invariant clarity
      → Answer: Prefix odd count difference equals k

    • Search target clarity
      → Answer: Subarrays with exactly k odd numbers

    • Discard logic
      → Answer: Non-matching prefix sums contribute zero

    • Termination guarantee
      → Answer: Single forward pass

    • Failure awareness
      → Answer: Sliding window counts only one window

    • Edge-case confidence
      → Answer: Sentinels handle boundaries

    • Variant readiness
      → Answer: Replace odd with any binary property

    • Pattern boundary
      → Answer: Non-contiguous or non-additive properties
    */

    /*
    🧘 FINAL CLOSURE STATEMENT

    For this problem, the invariant is exact difference of odd counts.
    The answer represents combinations of valid boundaries.
    The search terminates because prefix grows monotonically.
    I can re-derive this solution under pressure.
    This chapter is complete.
    */
}
