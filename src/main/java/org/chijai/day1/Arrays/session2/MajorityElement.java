package org.chijai.day1.Arrays.session2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Majority Element Family — V3
 *
 * Learning order:
 *
 * 1. Obvious counting solution (HashMap)
 * 2. Mathematical observation: > n/k => at most k - 1 winners
 * 3. Boyer-Moore n/2 — simplest cancellation case
 * 4. Boyer-Moore n/3 — primary problem
 * 5. Generic n/k — Misra-Gries
 * 6. Proof + interview recall
 * 7. Self-verifying tests
 *
 * Motto:
 * Learn one invariant, one code shape, reuse everywhere.
 */
public class MajorityElement {

    /*
     * ============================================================
     * 📘 PRIMARY PROBLEM — MAJORITY ELEMENT II
     * ============================================================
     *
     * Given int[] nums, return all elements appearing more than
     * floor(n / 3) times.
     *
     * Example:
     *
     * nums = [1,1,1,3,3,2,2,2]
     *
     * answer = [1,2]
     *
     * Desired optimal complexity:
     *
     * Time  : O(n)
     * Space : O(1)
     */

    /*
     * ============================================================
     * 1. OBVIOUS BASELINE — HASHMAP
     * ============================================================
     *
     * First thought:
     *
     * "I need frequencies."
     *
     * So count everybody.
     *
     * This is already O(n) time.
     * Boyer-Moore improves SPACE, not asymptotic time.
     *
     * Running frequency is monotonic:
     *
     * threshold = 3
     *
     * count:
     * 1  2  3  4  5 ...
     * F  F  F  T  T ...
     *          ↑
     *      first crossing
     *
     * Therefore record exactly when:
     *
     *     count == threshold + 1
     *
     * Why not:
     *
     *     count > threshold
     *
     * Because once the predicate becomes true, it stays true:
     *
     * threshold = 3
     *
     * count:
     * 1  2  3  4  5  6
     * F  F  F  T  T  T
     *          ↑
     *      first true
     *
     * If we used count > threshold and added every time,
     * the same winner would be added repeatedly at counts
     * 4, 5, 6, ...
     *
     * threshold + 1 is the exact FALSE -> TRUE boundary.
     *
     * This is the same monotonic-boundary idea used in binary
     * search, except here we encounter the boundary naturally
     * while scanning, so no binary search is needed.
     *
     * No second map scan is required because freq[num] is the
     * ACTUAL frequency seen so far.
     */

    static class HashMapNBy3 {

        public List<Integer> majorityElement(int[] nums) {

            Map<Integer, Integer> freq = new HashMap<>();
            List<Integer> ans = new ArrayList<>();

            int threshold = nums.length / 3;

            for (int num : nums) {

                int count = freq.merge(num, 1, Integer::sum);

                // Add exactly once, at the false -> true boundary.
                if (count == threshold + 1) {
                    ans.add(num);
                }
            }

            return ans;
        }
    }

    /*
     * Same HashMap idea for > n/2.
     *
     * There can be only one answer, so return immediately
     * when a value crosses the threshold.
     */
    static class HashMapNBy2 {

        public int majorityElement(int[] nums) {

            Map<Integer, Integer> freq = new HashMap<>();

            int threshold = nums.length / 2;

            for (int num : nums) {

                int count = freq.merge(num, 1, Integer::sum);

                if (count == threshold + 1) {
                    return num;
                }
            }

            // LeetCode 169 guarantees a majority exists.
            throw new IllegalArgumentException("No majority element");
        }
    }

    /*
     * ============================================================
     * 2. THE MATHEMATICAL BRIDGE
     * ============================================================
     *
     * Why can we possibly replace a HashMap with only a few slots?
     *
     * Because the threshold limits how many winners can exist.
     *
     * ------------------------------------------------------------
     * > n/2
     * ------------------------------------------------------------
     *
     * At most 1 winner.
     *
     * Two values each occurring > n/2 would together require
     * more than n array positions.
     *
     * ------------------------------------------------------------
     * > n/3
     * ------------------------------------------------------------
     *
     * At most 2 winners.
     *
     * Three values each occurring > n/3 would require:
     *
     * > n/3 + > n/3 + > n/3
     * > n
     *
     * impossible.
     *
     * ------------------------------------------------------------
     * General rule
     * ------------------------------------------------------------
     *
     * > n/k
     *
     * => at most k - 1 winners.
     *
     * This tells us the maximum number of candidate slots needed.
     */

    /*
     * ============================================================
     * 3. BOYER-MOORE > n/2 — LEARN CANCELLATION HERE FIRST
     * ============================================================
     *
     * One possible winner
     * -> one candidate slot.
     *
     * Mental model:
     *
     * candidate + different value
     *             ↓
     *           CANCEL
     *
     * count is NOT actual frequency.
     *
     * count = uncancelled support.
     *
     * If a true majority occupies > half the array,
     * there are fewer non-majority elements than majority elements.
     *
     * Therefore all majority copies cannot be cancelled.
     *
     * Standard family-wide code order:
     *
     * MATCH EXISTING
     * -> FILL EMPTY
     * -> CANCEL
     */

    static class BoyerMooreNBy2 {

        public int majorityElement(int[] nums) {

            int candidate = 0;
            int count = 0;

            for (int num : nums) {

                if (num == candidate) {
                    count++;

                } else if (count == 0) {
                    candidate = num;
                    count = 1;

                } else {
                    count--;
                }
            }

            // LeetCode 169 guarantees a majority exists.
            return candidate;
        }
    }

    /*
     * ============================================================
     * 4. BOYER-MOORE > n/3 — PRIMARY OPTIMAL SOLUTION
     * ============================================================
     *
     * At most 2 winners
     * -> keep 2 candidate slots.
     *
     * Same exact thought process:
     *
     * 1. MATCH EXISTING
     * 2. FILL EMPTY SLOT
     * 3. OTHERWISE CANCEL
     *
     * When both slots are active and num matches neither:
     *
     * candidate1
     * candidate2
     * num
     *
     * are THREE distinct values.
     *
     * Conceptually delete one copy of all three.
     *
     * The incoming num is discarded implicitly,
     * while:
     *
     * count1--;
     * count2--;
     *
     * represent deleting one copy of the two stored candidates.
     */

    static class BoyerMooreNBy3 {

        public List<Integer> majorityElement(int[] nums) {

            int candidate1 = 0;
            int candidate2 = 0;

            int count1 = 0;
            int count2 = 0;

            // PASS 1 — find possible survivors.
            for (int num : nums) {

                // 1. MATCH EXISTING
                if (num == candidate1) {
                    count1++;

                } else if (num == candidate2) {
                    count2++;

                // 2. FILL EMPTY SLOT
                } else if (count1 == 0) {
                    candidate1 = num;
                    count1 = 1;

                } else if (count2 == 0) {
                    candidate2 = num;
                    count2 = 1;

                // 3. THIRD DISTINCT VALUE -> CANCEL
                } else {
                    count1--;
                    count2--;
                }
            }

            // PASS 2 — verify actual frequencies.
            count1 = 0;
            count2 = 0;

            for (int num : nums) {
                if (num == candidate1) {
                    count1++;
                } else if (num == candidate2) {
                    count2++;
                }
            }

            List<Integer> ans = new ArrayList<>();
            int threshold = nums.length / 3;

            if (count1 > threshold) {
                ans.add(candidate1);
            }

            if (count2 > threshold) {
                ans.add(candidate2);
            }

            return ans;
        }
    }

    /*
     * ============================================================
     * WHY ORDER MATTERS FOR n/3
     * ============================================================
     *
     * MATCH must happen before EMPTY-SLOT replacement.
     *
     * Example:
     *
     * candidate1 = 5, count1 = 0
     * candidate2 = 8, count2 = 3
     * num = 8
     *
     * Wrong order:
     *
     * if (count1 == 0) ...
     *
     * would set:
     *
     * candidate1 = 8
     *
     * giving:
     *
     * candidate1 = 8
     * candidate2 = 8
     *
     * Both slots now track the same value.
     *
     * Correct order first sees:
     *
     * num == candidate2
     *
     * and increments count2.
     *
     * ------------------------------------------------------------
     * n/2 nuance
     * ------------------------------------------------------------
     *
     * With only one slot, duplicate-slot corruption cannot happen.
     *
     * But using MATCH -> EMPTY -> CANCEL there too gives us
     * one reusable family-wide coding style.
     */

    /*
     * ============================================================
     * WHY BOYER-MOORE NEEDS PASS 2
     * ============================================================
     *
     * This is a crucial distinction:
     *
     * HashMap counters:
     *
     *     ACTUAL frequencies
     *
     * Boyer-Moore counters:
     *
     *     UNCANCELLED support after elimination
     *
     * ------------------------------------------------------------
     * PASS 1 ONLY FINDS SUSPECTS
     * ------------------------------------------------------------
     *
     * After Boyer-Moore pass 1, we know:
     *
     *     every real > n/3 winner
     *     MUST be among candidate1 / candidate2
     *
     * But we do NOT know:
     *
     *     every surviving candidate
     *     actually occurs > n/3 times
     *
     * Example:
     *
     * nums = [1, 2, 3, 4]
     *
     * n = 4
     * threshold = floor(4 / 3) = 1
     *
     * No value occurs more than once,
     * so the correct answer is [].
     *
     * Yet Boyer-Moore can still finish with surviving candidates.
     *
     * Therefore survivors must be counted again for real.
     *
     * ------------------------------------------------------------
     * WHY NOT KEEP ACTUAL COUNTS DURING PASS 1?
     * ------------------------------------------------------------
     *
     * Because candidates can change.
     *
     * Suppose value 7 appeared earlier while it was NOT one of
     * the active candidates.
     *
     * Later, 7 becomes a candidate.
     *
     * Its earlier occurrences are already forgotten.
     *
     * To preserve exact historical counts for every value,
     * we would need:
     *
     *     value -> frequency
     *
     * which is exactly a HashMap.
     *
     * So the trade-off is:
     *
     *     ONE PASS + exact frequencies
     *     -> HashMap
     *     -> O(n) extra space
     *
     *     O(1) EXTRA SPACE
     *     -> Boyer-Moore candidate discovery
     *     -> second verification pass
     *
     * Two passes are still:
     *
     *     O(n) + O(n) = O(n)
     *
     * ------------------------------------------------------------
     * WHY n/2 SOMETIMES NEEDS NO VERIFICATION
     * ------------------------------------------------------------
     *
     * LeetCode 169 guarantees that a > n/2 majority exists.
     *
     * Therefore:
     *
     *     true majority must survive
     *     +
     *     some majority is guaranteed to exist
     *
     * so the surviving candidate can be returned directly.
     *
     * If existence were NOT guaranteed,
     * n/2 Boyer-Moore would also need a verification pass.
     *
     * ------------------------------------------------------------
     * REUSABLE RULE
     * ------------------------------------------------------------
     *
     * Boyer-Moore pass 1:
     *
     *     CANDIDATE DISCOVERY
     *
     * Is existence guaranteed?
     *
     *     YES -> candidate may be returned directly
     *            if the problem guarantees uniqueness/existence
     *
     *     NO  -> VERIFY actual frequency in pass 2
     */

    /*
     * ============================================================
     * 5. GENERIC > n/k — MISRA-GRIES
     * ============================================================
     *
     * Generalized Boyer-Moore is usually called Misra-Gries.
     *
     * > n/k
     *
     * => at most k - 1 winners
     *
     * therefore maintain at most k - 1 candidate counters.
     *
     * For every incoming num:
     *
     * MATCH EXISTING
     * -> increment
     *
     * else EMPTY SLOT
     * -> insert with count 1
     *
     * else
     * -> num is the kth distinct active value
     * -> decrement every stored counter
     * -> remove zero counters
     *
     * Then verify the surviving candidates.
     *
     * Complexity:
     *
     * Time  : O(n * k) in this straightforward implementation
     * Space : O(k)
     *
     * For fixed small k, this is O(n) time and O(1) extra space.
     */

    static class MisraGriesNByK {

        public List<Integer> majorityElement(int[] nums, int k) {

            if (k < 2) {
                throw new IllegalArgumentException("k must be >= 2");
            }

            Map<Integer, Integer> candidates = new HashMap<>();

            // PASS 1 — cancellation.
            for (int num : nums) {

                // 1. MATCH EXISTING
                if (candidates.containsKey(num)) {
                    candidates.put(num, candidates.get(num) + 1);
                    continue;
                }

                // 2. FILL EMPTY SLOT
                if (candidates.size() < k - 1) {
                    candidates.put(num, 1);
                    continue;
                }

                // 3. kth DISTINCT VALUE -> CANCEL EVERY ACTIVE SLOT
                Iterator<Map.Entry<Integer, Integer>> it =
                        candidates.entrySet().iterator();

                while (it.hasNext()) {

                    Map.Entry<Integer, Integer> entry = it.next();
                    int newCount = entry.getValue() - 1;

                    if (newCount == 0) {
                        it.remove();
                    } else {
                        entry.setValue(newCount);
                    }
                }
            }

            // PASS 2 — actual frequencies of survivors only.
            Map<Integer, Integer> actual = new HashMap<>();

            for (int candidate : candidates.keySet()) {
                actual.put(candidate, 0);
            }

            for (int num : nums) {
                if (actual.containsKey(num)) {
                    actual.put(num, actual.get(num) + 1);
                }
            }

            List<Integer> ans = new ArrayList<>();
            int threshold = nums.length / k;

            for (Map.Entry<Integer, Integer> entry : actual.entrySet()) {
                if (entry.getValue() > threshold) {
                    ans.add(entry.getKey());
                }
            }

            return ans;
        }
    }

    /*
     * ============================================================
     * 6. PROOF — COMMON-SENSE INTERVIEW VERSION
     * ============================================================
     *
     * For n/3:
     *
     * We repeatedly cancel groups of 3 distinct values.
     *
     * Suppose X really occurs f > n/3 times.
     *
     * One cancellation can remove at most ONE X.
     *
     * Completely removing all f copies of X would require
     * at least f cancellation groups.
     *
     * Each group contains 3 elements.
     *
     * So that would require at least:
     *
     * 3f > n
     *
     * total elements.
     *
     * Impossible.
     *
     * Therefore X cannot disappear completely.
     * It must survive as candidate1 or candidate2.
     *
     * ------------------------------------------------------------
     * IMPORTANT LOGIC DIRECTION
     * ------------------------------------------------------------
     *
     * PASS 1 proves:
     *
     * TRUE WINNER -> MUST BE A SURVIVOR
     *
     * It does NOT prove:
     *
     * SURVIVOR -> MUST BE A TRUE WINNER
     *
     * Hence verification.
     */

    /*
     * ============================================================
     * HASHMAP VS BOYER-MOORE
     * ============================================================
     *
     * HashMap
     * -------
     *
     * Counts everyone.
     *
     * Time  : O(n)
     * Space : O(n)
     *
     * Easier to write and reason about.
     *
     * ------------------------------------------------------------
     *
     * Boyer-Moore / Misra-Gries
     * -------------------------
     *
     * Keeps only possible survivors.
     *
     * Time  : O(n) for fixed k
     * Space : O(1) for fixed k
     *
     * Useful when interviewer asks:
     *
     * "Can you reduce the extra space?"
     *
     * ------------------------------------------------------------
     *
     * Core optimization insight:
     *
     * We do not need to remember everybody.
     *
     * Prove what information can be safely cancelled / forgotten.
     */

    /*
     * ============================================================
     * 🎯 INTERVIEW RECALL
     * ============================================================
     *
     * Trigger:
     *
     * "more than n/k times"
     * + small fixed k
     * + O(1) / bounded extra space desired
     *
     * ------------------------------------------------------------
     *
     * FACT:
     *
     * > n/k -> at most k - 1 winners
     *
     * ------------------------------------------------------------
     *
     * CODE:
     *
     * MATCH EXISTING
     * -> FILL EMPTY
     * -> CANCEL
     *
     * ------------------------------------------------------------
     *
     * COUNTERS:
     *
     * uncancelled support,
     * NOT actual frequencies
     *
     * ------------------------------------------------------------
     *
     * VERIFY:
     *
     * mandatory unless the problem guarantees the surviving
     * candidate is a true majority, as LeetCode 169 does.
     *
     * ------------------------------------------------------------
     *
     * FAMILY:
     *
     * > n/2
     * 1 candidate
     * cancel 2 distinct
     *
     * > n/3
     * 2 candidates
     * cancel 3 distinct
     *
     * > n/k
     * k - 1 candidates
     * cancel k distinct
     *
     * ------------------------------------------------------------
     *
     * ONE-LINER:
     *
     * "The algorithm is not counting winners;
     *  it is eliminating losers."
     */

    /*
     * ============================================================
     * 🧪 SELF-VERIFYING TESTS
     * ============================================================
     */

    private static void assertSameElements(List<Integer> expected,
                                           List<Integer> actual,
                                           String reason) {

        if (expected.size() != actual.size()
                || !expected.containsAll(actual)
                || !actual.containsAll(expected)) {

            throw new AssertionError(
                    reason +
                    "\nExpected: " + expected +
                    "\nActual:   " + actual
            );
        }
    }

    private static void assertEquals(int expected,
                                     int actual,
                                     String reason) {

        if (expected != actual) {
            throw new AssertionError(
                    reason +
                    "\nExpected: " + expected +
                    "\nActual:   " + actual
            );
        }
    }

    private static void assertNBy3(List<Integer> expected, int[] nums) {

        assertSameElements(
                expected,
                new HashMapNBy3().majorityElement(nums),
                "HashMap n/3 failed"
        );

        assertSameElements(
                expected,
                new BoyerMooreNBy3().majorityElement(nums),
                "Boyer-Moore n/3 failed"
        );

        assertSameElements(
                expected,
                new MisraGriesNByK().majorityElement(nums, 3),
                "Generic Misra-Gries k=3 failed"
        );
    }

    public static void main(String[] args) {

        // --------------------------------------------------------
        // n/2
        // --------------------------------------------------------

        int[] majorityHalf = {2, 2, 1, 1, 1, 2, 2};

        assertEquals(
                2,
                new HashMapNBy2().majorityElement(majorityHalf),
                "HashMap n/2 failed"
        );

        assertEquals(
                2,
                new BoyerMooreNBy2().majorityElement(majorityHalf),
                "Boyer-Moore n/2 failed"
        );

        // --------------------------------------------------------
        // n/3
        // --------------------------------------------------------

        assertNBy3(
                List.of(3),
                new int[]{3, 2, 3}
        );

        assertNBy3(
                List.of(1),
                new int[]{1}
        );

        assertNBy3(
                List.of(1, 2),
                new int[]{1, 2}
        );

        assertNBy3(
                List.of(1, 2),
                new int[]{1, 1, 1, 3, 3, 2, 2, 2}
        );

        assertNBy3(
                List.of(),
                new int[]{1, 2, 3, 4}
        );

        // --------------------------------------------------------
        // generic n/4
        // --------------------------------------------------------

        assertSameElements(
                List.of(1, 2, 3),
                new MisraGriesNByK().majorityElement(
                        new int[]{1,1,1,1,2,2,2,2,3,3,3,3,4},
                        4
                ),
                "Generic Misra-Gries k=4 failed"
        );

        System.out.println("All MajorityElementV3 assertions passed.");
    }
}
