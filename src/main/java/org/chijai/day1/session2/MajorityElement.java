package org.chijai.day1.session2;

// ============================================================================
// 📘 MajorityElementII_TextbookChapter.java
// A complete, IntelliJ-ready, single-file DSA textbook chapter
// Problem: Majority Element II (elements appearing more than ⌊n/3⌋ times)
// Pattern: Extended Boyer–Moore Voting (k = 2 counters)
// ============================================================================

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MajorityElement {

    // ============================
    // 🔵 CORE PATTERN OVERVIEW
    // ============================
    /*
        🔵 Pattern Name:
        Boyer–Moore Voting Algorithm (Generalized to k counters)

        🔵 Core Idea:
        When searching for elements appearing more than ⌊n/(k+1)⌋ times,
        we only need k counters. All other elements can be "paired out"
        against these counters and safely discarded.

        🔵 Why It Works:
        Each "pair-out" operation removes (k+1) distinct elements.
        An element that truly appears more than ⌊n/(k+1)⌋ times
        cannot be completely eliminated by such cancellations.
        Therefore, it must survive as a candidate.

        🔵 When to Use:
        - Frequency threshold problems with fixed denominator (n/2, n/3, n/4, …)
        - When O(1) space is required
        - When streaming-friendly, single-pass candidate detection is needed

        🧭 Pattern Recognition Signals:
        - Problem asks for elements appearing "more than ⌊n/x⌋ times"
        - x is small and fixed
        - Output size is bounded (≤ x − 1)
        - HashMap solution is obvious but space-inefficient
    */

    // ----------------------------
    // 🟢 MENTAL MODEL & INVARIANTS
    // ----------------------------
    /*
        🟢 Mental Model (Think in cancellations, not counts):
        Imagine removing groups of (k+1) *distinct* elements repeatedly.
        What remains at the end are only possible heavy hitters.

        For Majority Element II:
        - Threshold = ⌊n/3⌋
        - Maximum possible answers = 2
        - Therefore, we maintain 2 candidates + 2 counters

        🟢 Invariants (MUST always hold):
        1. At any point, count1 and count2 represent relative dominance,
           not actual frequencies.
        2. candidate1 and candidate2 are the only possible survivors
           after all valid pair-outs.
        3. If both counters are non-zero and current number matches neither,
           one unit of dominance is removed from BOTH candidates.
        4. Order of checks preserves identity before replacement.

        🟢 Role of Every Variable:
        - candidate1, candidate2:
            Current surviving identities after cancellations
        - count1, count2:
            Relative dominance scores
        - num:
            Current stream element being processed

        🟢 Termination Logic:
        - First pass finds *potential* candidates only
        - Second pass verifies actual frequency against ⌊n/3⌋

        🟢 Forbidden Actions:
        - ❌ Using HashMap without justification
        - ❌ Skipping the verification pass
        - ❌ Replacing candidate before checking equality
        - ❌ Treating counts as final frequencies

        🟢 Why Common Alternatives Are Inferior:
        - Sorting: O(n log n), unnecessary
        - HashMap: O(n) space, violates constraint
        - Brute force: O(n²), not scalable
    */

    // ============================================================
    // PRIMARY PROBLEM — SOLUTION CLASSES
    // ============================================================

    // ------------------------------------------------------------
    // 🔹 Brute Force Approach
    // ------------------------------------------------------------
    static class BruteForceSolution {
        /*
            🔵 Core Idea:
            Count frequency of every element using nested loops.

            🔵 Limitation Fixed by Next Approach:
            Eliminates quadratic time.

            ⏱ Time Complexity: O(n²)
            🧠 Space Complexity: O(1)
            🎯 Interview-Preferred: ❌ No (too slow)
        */
        public List<Integer> majorityElement(int[] nums) {
            List<Integer> result = new ArrayList<>();
            int n = nums.length;

            for (int i = 0; i < n; i++) {
                int count = 0;
                for (int j = 0; j < n; j++) {
                    if (nums[j] == nums[i]) count++;
                }
                if (count > n / 3 && !result.contains(nums[i])) {
                    result.add(nums[i]);
                }
            }
            return result;
        }
    }

    // ------------------------------------------------------------
    // 🔹 Improved Approach (HashMap)
    // ------------------------------------------------------------
    static class HashMapSolution {
        /*
            🔵 Core Idea:
            Count frequencies using a map.

            🔵 Limitation Fixed by Next Approach:
            Reduces space from O(n) to O(1).

            ⏱ Time Complexity: O(n)
            🧠 Space Complexity: O(n)
            🎯 Interview-Preferred: ⚠️ Only if space allowed
        */
        public List<Integer> majorityElement(int[] nums) {
            java.util.Map<Integer, Integer> frequency = new java.util.HashMap<>();
            for (int num : nums) {
                frequency.put(num, frequency.getOrDefault(num, 0) + 1);
            }

            List<Integer> result = new ArrayList<>();
            for (var entry : frequency.entrySet()) {
                if (entry.getValue() > nums.length / 3) {
                    result.add(entry.getKey());
                }
            }
            return result;
        }
    }

    // ------------------------------------------------------------
    // 🔹 Optimal Approach (Extended Boyer–Moore)
    // ------------------------------------------------------------

    static class OptimalBoyerMooreSolution {
        /*
            🔵 Core Idea:
            Maintain two candidates and cancel out triples.

            🔵 What Limitation It Fixes:
            Removes extra space while keeping linear time.

            ⏱ Time Complexity: O(n)
            🧠 Space Complexity: O(1)
            🎯 Interview-Preferred: ✅ YES
        */
        public List<Integer> majorityElement(int[] nums) {
            int candidate1 = 0, candidate2 = 0;
            int count1 = 0, count2 = 0;

            for (int num : nums) {

                // 🟡 Why this order exists:
                // Equality must be checked BEFORE zero-count replacement
                // Otherwise, we risk overwriting a valid candidate.

                if (num == candidate1) {
                    count1++;
                } else if (num == candidate2) {
                    count2++;
                } else if (count1 == 0) {
                    candidate1 = num;
                    count1 = 1;
                } else if (count2 == 0) {
                    candidate2 = num;
                    count2 = 1;
                } else {
                    // ❌ INTERVIEW TRAP:
                    // Forgetting this step breaks cancellation invariant
                    count1--;
                    count2--;
                }
            }

            // Verification pass (MANDATORY)
            count1 = 0;
            count2 = 0;
            for (int num : nums) {
                if (num == candidate1) count1++;
                else if (num == candidate2) count2++;
            }

            List<Integer> result = new ArrayList<>();
            int threshold = nums.length / 3;

            if (count1 > threshold) result.add(candidate1);
            if (count2 > threshold) result.add(candidate2);

            return result;
        }
    }

    // ============================================================================
    // 🧠 CORE INTUITION — WHY BOYER–MOORE (n/3) WORKS
    // ============================================================================
    /*
    🔵 Key Fact:
    If an element appears more than ⌊n/3⌋ times, there can be at most TWO such elements.
    Three distinct elements each > n/3 would together exceed n.

    🔵 Cancellation / Pair-Out Model:
    Think of the array as a voting process.
    Whenever we see THREE distinct numbers, we can remove one vote from each.
    Such removal never eliminates a true > n/3 element completely.

    🔵 Algorithm Intuition:
    • Maintain TWO candidates and TWO counters
    • If number matches a candidate → reinforce it
    • If a counter is zero → claim that slot
    • If it matches neither and both slots are full → cancel all three

    This simulates repeatedly deleting groups of 3 distinct elements.
    Counters track RELATIVE dominance, not actual frequency.

    🔴 CRITICAL ORDER INVARIANT (COMMON BUG):
    Equality checks MUST come before checking count == 0.

    ❌ If count == 0 is checked first:
    → A valid surviving candidate may be overwritten
    → Cancellation invariant breaks

    🧠 Mental Rule:
    “Is this number already someone I’m tracking?”
    Only if the answer is NO may a candidate be replaced.
    */


    // ============================================================
    // 🟣 INTERVIEW ARTICULATION & FOLLOW-UPS
    // ============================================================
    /*
        🟣 Why the Optimal Approach Works:
        Any element appearing more than ⌊n/3⌋ times cannot be fully canceled.

        🟣 Which Invariant Guarantees Correctness:
        At most 2 elements can exceed ⌊n/3⌋ frequency.

        🟣 What Breaks If Order Changes:
        Checking count == 0 before equality can replace a valid candidate,
        destroying the cancellation guarantee.

        🟣 Can It Be Done In-Place?
        Yes. Only constant extra variables used.

        🟣 Can It Handle Streaming Input?
        Candidate detection: YES
        Verification: Requires second pass or stored data.

        🟣 When Should This Pattern NOT Be Used?
        - Variable thresholds
        - Large k values
        - When exact frequencies are required mid-stream

        🟣 How to Explain Without Code:
        "Repeatedly remove groups of three distinct numbers.
         Any number that appears too frequently cannot be fully removed."
    */

    // ============================================================
    // 🔄 VARIATIONS & TWEAKS — COMPLETE COVERAGE
    // ============================================================
    /*
        🟢 Invariant-Preserving Changes:
        - Extend to n/4 using 3 counters

        🟡 Reasoning-Only Changes:
        - Different explanation metaphors (voting, cancellation, balance)

        🔴 Pattern-Break Signals:
        - Asking for top-k frequent elements
        - Threshold depends on input dynamically
    */

    // ============================================================
    // ⚫ PATTERN REINFORCEMENT PROBLEMS
    // ============================================================

    // ------------------------------------------------------------
    // ⚫ Reinforcement 1 — Majority Element (n/2)
    // ------------------------------------------------------------
    static class MajorityElementNBy2 {
        /*
            🔵 Problem:
            Find element appearing more than ⌊n/2⌋ times.

            ⚫ SAME PATTERN AS PRIMARY PROBLEM BECAUSE:
            Threshold-based heavy hitter detection.

            🟢 Key Invariant:
            One element survives all pair-outs.

            🟡 What Changes:
            Only one candidate + one counter.
        */
        public int majorityElement(int[] nums) {
            int candidate = 0, count = 0;
            for (int num : nums) {
                if (count == 0) candidate = num;
                count += (num == candidate) ? 1 : -1;
            }
            return candidate;
        }
    }

    // ------------------------------------------------------------
    // ⚫ Reinforcement 2 — Elements Appearing More Than n/4 Times
    // ------------------------------------------------------------
    static class MajorityElementNBy4 {
        /*
            Uses 3 counters.
            Same cancellation logic.
        */
        public List<Integer> majorityElement(int[] nums) {
            int[] candidates = new int[3];
            int[] counts = new int[3];

            for (int num : nums) {
                boolean matched = false;
                for (int i = 0; i < 3; i++) {
                    if (counts[i] > 0 && candidates[i] == num) {
                        counts[i]++;
                        matched = true;
                        break;
                    }
                }
                if (matched) continue;

                for (int i = 0; i < 3; i++) {
                    if (counts[i] == 0) {
                        candidates[i] = num;
                        counts[i] = 1;
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    for (int i = 0; i < 3; i++) counts[i]--;
                }
            }

            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                int actualCount = 0;
                for (int num : nums) if (num == candidates[i]) actualCount++;
                if (actualCount > nums.length / 4) result.add(candidates[i]);
            }
            return result;
        }
    }

    // ============================================================
    // 🟢 LEARNING VERIFICATION
    // ============================================================
    /*
        ✅ You’ve mastered this if:
        - You can explain why only k counters are needed
        - You remember order of condition checks
        - You never forget verification pass

        🐞 Bugs to Debug Intentionally:
        - Swap equality and zero-count checks
        - Remove verification pass

        🔍 Pattern Recognition:
        "More than ⌊n/x⌋" + small x → Boyer–Moore generalized
    */

    // ============================================================
    // 🧪 main() METHOD + TESTS (MUST BE LAST)
    // ============================================================
    public static void main(String[] args) {
        OptimalBoyerMooreSolution solution = new OptimalBoyerMooreSolution();

        // 🟡 Core case
        System.out.println(solution.majorityElement(new int[]{3, 2, 3}));
        // Expected: [3]

        // 🟡 Single element (INTERVIEW TRAP)
        System.out.println(solution.majorityElement(new int[]{1}));
        // Expected: [1]

        // 🟡 Two elements
        System.out.println(solution.majorityElement(new int[]{1, 2}));
        // Expected: [1, 2]

        // 🟡 Duplicate-heavy input
        System.out.println(solution.majorityElement(new int[]{1,1,1,3,3,2,2,2}));
        // Expected: [1, 2]

        // 🟡 Edge: all identical
        System.out.println(solution.majorityElement(new int[]{5,5,5,5}));
        // Expected: [5]
    }
}
