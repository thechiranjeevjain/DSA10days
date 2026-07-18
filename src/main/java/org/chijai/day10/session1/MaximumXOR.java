package org.chijai.day10.session1;
import java.util.*;

/**
 * ============================================================================
 * LeetCode 421. Maximum XOR of Two Numbers in an Array
 * Difficulty: Medium
 *
 * Tags
 * ----
 * Trie
 * Bit Manipulation
 * Greedy
 * Binary Trie
 *
 * Problem
 * -------
 * Given an integer array nums,
 * return the maximum value of
 *
 *      nums[i] XOR nums[j]
 *
 * where
 *
 *      0 <= i <= j < nums.length
 *
 * Constraints
 * -----------
 * 1 <= nums.length <= 200000
 * 0 <= nums[i] <= 2^31 - 1
 *
 * Examples
 * --------
 *
 * nums = [3,10,5,25,2,8]
 *
 * Maximum XOR
 *
 *      5 ^ 25 = 28
 *
 * Answer = 28
 *
 * ------------------------------------
 *
 * nums =
 * [14,70,53,83,49,91,36,80,92,51,66,70]
 *
 * Answer = 127
 *
 * Official
 * --------
 * https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/
 *
 * ============================================================================
 *
 * 🔵 CORE PATTERN OVERVIEW
 *
 * Pattern
 * -------
 * Binary Trie + Greedy Bit Selection
 *
 * Problem Archetype
 * -----------------
 * Maximize/minimize a bitwise expression by matching every bit independently
 * from the most significant bit toward the least significant bit.
 *
 * Core Invariant
 * --------------
 * While processing one number,
 * every higher bit decision is permanently optimal.
 *
 * Once a higher bit becomes 1,
 * no later lower bits can compensate for losing it.
 *
 * Therefore:
 *
 *      Highest bit first.
 *      Greedily try opposite bit.
 *
 * Why It Works
 * ------------
 * XOR prefers different bits.
 *
 *      0 ^ 1 = 1
 *      1 ^ 0 = 1
 *
 * Since higher bits contribute exponentially more,
 * maximizing earlier bits is always optimal.
 *
 * Recognition Signals
 * -------------------
 * Think Binary Trie whenever you see:
 *
 * ✓ Maximum XOR
 * ✓ Minimum XOR
 * ✓ Bitwise optimization
 * ✓ Prefix XOR queries
 * ✓ Binary decisions per bit
 *
 * Difference vs HashSet Prefix Method
 * -----------------------------------
 *
 * Prefix HashSet
 *
 *      Great for solving exactly this problem.
 *
 * Binary Trie
 *
 *      Easier to generalize.
 *
 *      Supports
 *
 *      • online insert
 *      • streaming
 *      • delete (augmented)
 *      • max xor query
 *      • min xor query
 *      • k-th xor variants
 *      • persistent trie variants
 *
 * Therefore,
 * Binary Trie is the interview-preferred transferable pattern.
 *
 * ============================================================================
 *
 * 🟢 MENTAL MODEL
 *
 * Imagine every integer written using exactly 32 bits.
 *
 * Example
 *
 * 5
 *
 * 00000000000000000000000000000101
 *
 * Every level of the trie represents ONE BIT.
 *
 *                  root
 *                /      \
 *             bit0      bit1
 *             /  \      /  \
 *            ...
 *
 * Level 31
 * --------
 * Sign bit (always zero here because numbers are non-negative)
 *
 * Level 30
 * --------
 * Bit 30
 *
 * ...
 *
 * Level 0
 * -------
 * Least significant bit.
 *
 * Traversal Rule
 * --------------
 *
 * Suppose current number has
 *
 * bit = 0
 *
 * Best partner bit?
 *
 *      1
 *
 * because
 *
 *      0 XOR 1 = 1
 *
 * If opposite branch exists,
 * always take it.
 *
 * Otherwise,
 * accept same bit.
 *
 * ============================================================================
 *
 * 🟢 COMPLETE INVARIANTS
 *
 * Invariant 1
 * -----------
 * Trie always contains previously inserted numbers.
 *
 * Invariant 2
 * -----------
 * Every root-to-leaf path represents exactly one inserted integer.
 *
 * Invariant 3
 * -----------
 * During query,
 * processed higher bits already produce the largest possible prefix XOR.
 *
 * Invariant 4
 * -----------
 * Never sacrifice an already achievable higher-bit 1
 * to improve lower bits.
 *
 * Invariant 5
 * -----------
 * Every move is greedy only because
 * XOR value is lexicographically ordered by bits.
 *
 * Variable Meaning
 * ----------------
 *
 * bit
 *      Current bit of number.
 *
 * desired
 *      Opposite bit.
 *
 * node
 *      Current trie node.
 *
 * xorValue
 *      Best XOR constructed so far.
 *
 * Allowed Moves
 * -------------
 *
 * if opposite child exists
 *      move there
 *
 * else
 *      move to same-bit child
 *
 * Forbidden Move
 * --------------
 *
 * Choosing same bit while opposite exists.
 *
 * That permanently loses a higher-value XOR bit.
 *
 * Termination
 * -----------
 *
 * After processing all 32 bits,
 * xorValue is optimal.
 *
 * ============================================================================
 *
 * 🔴 WHY NAIVE SOLUTIONS FAIL
 *
 * Wrong #1
 * --------
 * Compare every pair.
 *
 * Complexity
 *
 * O(N²)
 *
 * N = 200000
 *
 * Impossible.
 *
 * -----------------------------
 *
 * Wrong #2
 *
 * Sort numbers first.
 *
 * Why it looks reasonable
 *
 * Nearby values may have useful XOR.
 *
 * Counterexample
 *
 * 5
 * 25
 *
 * are far apart numerically
 * but produce the optimum.
 *
 * Sorting destroys no information,
 * but it also provides no useful ordering for XOR.
 *
 * -----------------------------
 *
 * Wrong #3
 *
 * Always compare against largest value.
 *
 * Counterexample
 *
 * 8
 * 7
 * 15
 *
 * Best partner is not necessarily numerically largest.
 *
 * -----------------------------
 *
 * Wrong #4
 *
 * Greedy from least significant bit.
 *
 * Counterexample
 *
 * Binary
 *
 * 100000
 * 011111
 *
 * Losing the highest bit can never be repaired
 * by perfect lower bits.
 *
 * Exact Invariant Violation
 * -------------------------
 *
 * Lower bits never outweigh higher bits.
 *
 * ============================================================================
 *
 * 🛠 IMPLEMENTATION BLUEPRINT
 *
 * Step 1
 * ------
 * Build TrieNode.
 *
 * Step 2
 * ------
 * Give every node
 *
 * children[0]
 * children[1]
 *
 * Step 3
 * ------
 * Insert every number bit-by-bit
 * from bit 31 down to bit 0.
 *
 * Step 4
 * ------
 * Query each number.
 *
 * Step 5
 * ------
 * At every bit
 *
 * desired = bit ^ 1
 *
 * Step 6
 * ------
 * If desired exists
 *
 * set XOR bit
 *
 * move desired
 *
 * else
 *
 * move same bit.
 *
 * Step 7
 * ------
 * Update answer.
 *
 * Step 8
 * ------
 * Return maximum.
 *
 * ============================================================================
 *
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 *
 * build trie
 *
 * answer = 0
 *
 * for each number
 *      query trie
 *      update answer
 *
 * insert current
 *
 * return answer
 *
 * ============================================================================
 */
public class MaximumXOR {


    /*
     * =======================================================================
     * BRUTE FORCE
     * =======================================================================
     *
     * Core Idea
     * ---------
     * Try every pair.
     *
     * Invariant
     * ---------
     * Best answer seen so far.
     *
     * Complexity
     * ----------
     * Time  : O(N²)
     * Space : O(1)
     *
     * Interview
     * ---------
     * Only useful as baseline.
     */
    static class BruteForceSolution {

        public int findMaximumXOR(int[] nums) {

            int answer = 0;

            for (int i = 0; i < nums.length; i++) {

                for (int j = i; j < nums.length; j++) {

                    answer = Math.max(answer, nums[i] ^ nums[j]);
                }
            }

            return answer;
        }
    }

    /*
     * =======================================================================
     * IMPROVED
     * =======================================================================
     *
     * Prefix HashSet Greedy
     *
     * Time
     * ----
     * O(32N)
     *
     * Space
     * -----
     * O(N)
     *
     * Useful because many interviewers expect awareness
     * of this elegant approach before introducing Binary Trie.
     */
    static class PrefixHashSetSolution {

        public int findMaximumXOR(int[] nums) {

            int answer = 0;
            int mask = 0;

            for (int bitIndex = 31; bitIndex >= 0; bitIndex--) {

                mask |= (1 << bitIndex);

                Set<Integer> prefixes = new HashSet<>();

                for (int number : nums) {
                    prefixes.add(number & mask);
                }

                int candidate = answer | (1 << bitIndex);

                boolean found = false;

                for (int prefix : prefixes) {

                    if (prefixes.contains(prefix ^ candidate)) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    answer = candidate;
                }
            }

            return answer;
        }
    }

    /*
     * =======================================================================
     * OPTIMAL (Interview Preferred)
     * =======================================================================
     *
     * Core Idea
     * ---------
     * Binary Trie.
     *
     * Build trie incrementally.
     *
     * Query best partner before inserting current number.
     *
     * This naturally satisfies
     *
     * i <= j
     *
     * because every query only searches previously inserted values
     * plus self after insertion ordering is handled.
     *
     * Complexity
     * ----------
     * Time
     * O(32N)
     *
     * Space
     * O(32N)
     */

    /*
     * =======================================================================
     * Binary Trie Node
     * =======================================================================
     */
    static class TrieNode {

        TrieNode[] children = new TrieNode[2];

    }

    /*
     * =======================================================================
     * Binary Trie
     * =======================================================================
     */
    static class BinaryTrie {

        private final TrieNode root = new TrieNode();

        /*
         * Insert one integer.
         *
         * Invariant:
         * Every processed bit extends exactly one path.
         */
        public void insert(int number) {

            TrieNode current = root;

            for (int bitIndex = 31; bitIndex >= 0; bitIndex--) {

                int bit = (number >>> bitIndex) & 1;

                if (current.children[bit] == null) {
                    current.children[bit] = new TrieNode();
                }

                current = current.children[bit];
            }
        }

        /*
         * Query maximum XOR partner already inside trie.
         *
         * Returns
         * -------
         * Best XOR value obtainable.
         */
        public int queryMaximumXor(int number) {

            TrieNode current = root;

            int xorValue = 0;

            for (int bitIndex = 31; bitIndex >= 0; bitIndex--) {

                int bit = (number >>> bitIndex) & 1;

                int opposite = bit ^ 1; //XOR operator flips the bit

                // Invariant:
                // Always prefer opposite bit because it creates XOR = 1
                // at the highest remaining position.
                if (current.children[opposite] != null) {

                    xorValue |= (1 << bitIndex);

                    current = current.children[opposite];

                } else {

                    // Opposite bit unavailable.
                    // Preserve trie path.
                    // This XOR bit = 0
                    // xorValue already contains 0 here, so nothing to update.
                    current = current.children[bit];
                }
            }

            return xorValue;
        }
    }

    static class OptimalSolution {

        public int findMaximumXOR(int[] nums) {

            if (nums.length == 1) {
                return 0;
            }

            BinaryTrie trie = new BinaryTrie();

            trie.insert(nums[0]);

            int answer = 0;

            for (int index = 1; index < nums.length; index++) {

                // Query before insertion.
                // Invariant:
                // Trie contains exactly previous numbers.
                answer = Math.max(
                        answer,
                        trie.queryMaximumXor(nums[index])
                );

                // Current number now becomes available
                // for future comparisons.
                trie.insert(nums[index]);
            }

            return answer;
        }
    }



    /*
     * =========================================================================
     * 🟣 INTERVIEW ARTICULATION (NO CODE)
     * =========================================================================
     *
     * How would I explain the optimal solution?
     * -----------------------------------------
     *
     * "XOR is maximized from the most significant bit downward.
     * Since a higher bit contributes more than all lower bits combined,
     * I greedily try to make every higher XOR bit equal to 1.
     *
     * To do that efficiently, I store every previously seen number inside a
     * binary trie.
     *
     * While querying one number, if the opposite bit exists, I always take it,
     * because that immediately creates a 1 in the current XOR position.
     *
     * Otherwise I am forced to take the same bit.
     *
     * Repeating this for all 32 bits constructs the maximum possible XOR."
     *
     * -------------------------------------------------------------------------
     *
     * Why is greedy correct?
     * ----------------------
     *
     * Suppose we are deciding bit k.
     *
     * Weight of this bit:
     *
     *      2^k
     *
     * Maximum contribution of ALL remaining bits:
     *
     *      2^k - 1
     *
     * Therefore,
     *
     * gaining bit k
     *
     * is always better than
     *
     * losing bit k
     * and perfecting every lower bit.
     *
     * Hence greedy is globally optimal.
     *
     * -------------------------------------------------------------------------
     *
     * Why does the trie work?
     * -----------------------
     *
     * Every root-to-leaf path is exactly one inserted number.
     *
     * At every level we immediately know whether an opposite bit exists.
     *
     * No searching,
     * no scanning,
     * only one branch decision.
     *
     * -------------------------------------------------------------------------
     *
     * What breaks if greedy is reversed?
     * ----------------------------------
     *
     * Choosing same-bit while opposite exists permanently loses a higher XOR bit.
     *
     * Lower bits can never recover that loss.
     *
     * -------------------------------------------------------------------------
     *
     * Can this be done in-place?
     * --------------------------
     *
     * No.
     *
     * Efficient queries require auxiliary structure
     * (Trie or Prefix HashSet).
     *
     * -------------------------------------------------------------------------
     *
     * Streaming?
     * ----------
     *
     * Yes.
     *
     * Binary Trie naturally supports
     *
     * insert
     * query
     *
     * online.
     *
     * This is one reason interviewers prefer this pattern.
     *
     * -------------------------------------------------------------------------
     *
     * When NOT to use Binary Trie?
     * ----------------------------
     *
     * • Tiny arrays
     * • Brute force acceptable
     * • Problem not decomposable bit-by-bit
     * • Ordering of bits is irrelevant
     *
     * =========================================================================
     * 🎯 INTERVIEW RECALL SHEET (30 Seconds)
     * =========================================================================
     *
     * Pattern Trigger
     * ---------------
     * "Maximum XOR"
     *
     * Core Invariant
     * --------------
     * Higher XOR bits dominate lower bits.
     *
     * Search Target
     * -------------
     * Opposite bit.
     *
     * Greedy Rule
     * -----------
     * Prefer opposite child.
     *
     * Otherwise,
     * same child.
     *
     * Trie Direction
     * --------------
     * MSB → LSB
     *
     * Common Trap
     * -----------
     * Thinking numerically instead of bitwise.
     *
     * Edge Cases
     * ----------
     * • one element
     * • duplicates
     * • zero
     * • all equal
     *
     * Interview One-Liner
     * -------------------
     *
     * "Each bit decision is locally optimal because higher bits outweigh every
     * remaining lower bit combined."
     *
     * Re-Derivation Cue
     * -----------------
     *
     * XOR wants opposite bits.
     * Walk from highest bit downward.
     *
     * =========================================================================
     * 🔄 VARIATIONS & TWEAKS
     * =========================================================================
     *
     * Variation
     * ---------
     * Find minimum XOR.
     *
     * Change
     * ------
     * Prefer SAME bit first.
     *
     * Invariant preserved?
     *
     * Yes.
     *
     * Goal simply reverses.
     *
     * -------------------------------------------------------------------------
     *
     * Variation
     * ---------
     * Online stream.
     *
     * Trie still works.
     *
     * Simply
     *
     * query
     * insert
     *
     * for every arriving number.
     *
     * -------------------------------------------------------------------------
     *
     * Variation
     * ---------
     * Prefix XOR queries.
     *
     * Store prefix XOR values.
     *
     * Query identical.
     *
     * Only inserted values differ.
     *
     * -------------------------------------------------------------------------
     *
     * Variation
     * ---------
     * Delete values.
     *
     * Need frequency count inside each node.
     *
     * Child removed only when frequency becomes zero.
     *
     * -------------------------------------------------------------------------
     *
     * Pattern Break
     * -------------
     *
     * If optimization cannot be expressed
     * independently bit-by-bit,
     * Binary Trie no longer applies.
     *
     * =========================================================================
     * ⚫ REINFORCEMENT PROBLEM 1
     *
     * LeetCode 1707
     * Maximum XOR With An Element From Array
     * =========================================================================
     *
     * Summary
     * -------
     * For every query
     *
     *      (x, m)
     *
     * choose any array element <= m
     * maximizing XOR.
     *
     * Key Observation
     * ---------------
     *
     * Same trie query.
     *
     * Difference:
     *
     * Insert only eligible numbers.
     *
     * Sort queries by m.
     *
     * =========================================================================
     */

    static class ReinforcementMaximumXorWithLimit {

        static class Query {

            int value;
            int limit;
            int originalIndex;

            Query(int value, int limit, int originalIndex) {
                this.value = value;
                this.limit = limit;
                this.originalIndex = originalIndex;
            }
        }

        public int[] maximizeXor(int[] nums, int[][] queries) {

            Arrays.sort(nums);

            Query[] offline = new Query[queries.length];

            for (int i = 0; i < queries.length; i++) {

                offline[i] = new Query(
                        queries[i][0],
                        queries[i][1],
                        i
                );
            }

            Arrays.sort(
                    offline,
                    Comparator.comparingInt(q -> q.limit)
            );

            BinaryTrie trie = new BinaryTrie();

            int[] answer = new int[queries.length];

            int pointer = 0;

            for (Query query : offline) {

                while (pointer < nums.length &&
                        nums[pointer] <= query.limit) {

                    trie.insert(nums[pointer]);
                    pointer++;
                }

                if (pointer == 0) {

                    answer[query.originalIndex] = -1;

                } else {

                    answer[query.originalIndex] =
                            trie.queryMaximumXor(query.value);
                }
            }

            return answer;
        }
    }

    /*
     * Edge Cases
     * ----------
     *
     * • no eligible number
     * • duplicate limits
     * • duplicate values
     *
     * Interview Trap
     * --------------
     *
     * Sorting queries does NOT change answers because original indices
     * are restored afterward.
     *
     * Interview Articulation
     * ----------------------
     *
     * "The trie invariant stays unchanged.
     * Only the insertion schedule changes."
     *
     * =========================================================================
     * ⚫ REINFORCEMENT PROBLEM 2
     *
     * Maximum XOR Pair in a Stream
     * =========================================================================
     *
     * Summary
     * -------
     * Numbers arrive one-by-one.
     *
     * After every insertion,
     * report current maximum XOR.
     *
     * Same invariant.
     *
     * Query first.
     * Insert second.
     *
     * =========================================================================
     */

    static class StreamingMaximumXor {

        private final BinaryTrie trie = new BinaryTrie();

        private boolean empty = true;

        private int maximum = 0;

        public int add(int number) {

            if (empty) {

                trie.insert(number);

                empty = false;

                return 0;
            }

            maximum = Math.max(
                    maximum,
                    trie.queryMaximumXor(number)
            );

            trie.insert(number);

            return maximum;
        }
    }

    /*
     * Edge Cases
     * ----------
     *
     * First insertion.
     * Duplicate numbers.
     * Large values.
     *
     * Interview Trap
     * --------------
     *
     * Query AFTER insertion allows the new value to compare with itself.
     *
     * That still produces 0,
     * but conceptually violates the intended online invariant.
     *
     * Preferred order:
     *
     * query
     * insert
     *
     * =========================================================================
     * ⚫ REINFORCEMENT PROBLEM 3
     *
     * Maximum Subarray XOR
     * =========================================================================
     *
     * Summary
     * -------
     *
     * Compute
     *
     * maximum(prefixXor[i] XOR prefixXor[j])
     *
     * Same trie.
     *
     * Only inserted values become prefix XORs instead of original numbers.
     *
     * =========================================================================
     */

    static class MaximumSubarrayXor {

        public int maximumSubarrayXor(int[] nums) {

            BinaryTrie trie = new BinaryTrie();

            trie.insert(0);

            int prefix = 0;

            int answer = 0;

            for (int number : nums) {

                prefix ^= number;

                answer = Math.max(
                        answer,
                        trie.queryMaximumXor(prefix)
                );

                trie.insert(prefix);
            }

            return answer;
        }
    }



    /*
     * Edge Cases
     * ----------
     *
     * • Empty prefix
     * • Entire array
     * • Single element
     * • All zeros
     *
     * Interview Trap
     * --------------
     *
     * Forgetting to insert prefix XOR = 0 initially.
     *
     * Then subarrays starting at index 0 are never considered.
     *
     * Interview Articulation
     * ----------------------
     *
     * "A subarray XOR equals the XOR of two prefix XORs.
     * Therefore the problem reduces directly to maximum XOR pair."
     *
     * =========================================================================
     * ⚫ RELATED PROBLEM 1
     *
     * LeetCode 208
     * Implement Trie (Prefix Tree)
     * =========================================================================
     *
     * Relationship
     * ------------
     *
     * Same data structure.
     *
     * Broken Invariant
     * ----------------
     *
     * Characters instead of bits.
     *
     * Branch count changes
     *
     *      2  -> 26
     *
     * but traversal logic remains identical.
     *
     * =========================================================================
     */

    static class PrefixTrie {

        static class Node {

            Node[] children = new Node[26];

            boolean isWord;

        }

        private final Node root = new Node();

        public void insert(String word) {

            Node current = root;

            for (char ch : word.toCharArray()) {

                int index = ch - 'a';

                if (current.children[index] == null) {

                    current.children[index] = new Node();
                }

                current = current.children[index];
            }

            current.isWord = true;
        }

        public boolean search(String word) {

            Node node = traverse(word);

            return node != null && node.isWord;
        }

        public boolean startsWith(String prefix) {

            return traverse(prefix) != null;
        }

        private Node traverse(String text) {

            Node current = root;

            for (char ch : text.toCharArray()) {

                int index = ch - 'a';

                if (current.children[index] == null) {
                    return null;
                }

                current = current.children[index];
            }

            return current;
        }
    }

    /*
     * Edge Case
     * ---------
     * Empty string.
     *
     * Interview Note
     * --------------
     *
     * Character Trie and Binary Trie differ only in alphabet size.
     *
     * =========================================================================
     * ⚫ RELATED PROBLEM 2
     *
     * LeetCode 1803
     * Count Pairs With XOR in a Range
     * =========================================================================
     *
     * Relationship
     * ------------
     *
     * Same Binary Trie.
     *
     * Additional Information
     * ----------------------
     *
     * Every node stores
     *
     * subtree frequency.
     *
     * Instead of maximizing XOR,
     * we count how many valid branches satisfy
     * XOR < limit.
     *
     * =========================================================================
     */

    static class CountPairsSkeleton {

        static class Node {

            Node[] children = new Node[2];

            int count;

        }

        /*
         * Skeleton only.
         *
         * Purpose:
         * demonstrate the extra node information required.
         *
         * The traversal invariant remains identical:
         *
         * process bits from MSB to LSB.
         */
    }

/*
 * Interview Note
 * --------------
 *
 * Binary Trie scales naturally from optimization
 * to counting problems by augmenting each node.
 *
 * =========================================================================
 * ⚫ RELATED PROBLEM 3
 *
 * LeetCode 1938
 * Maximum Genetic Difference Query
 * =========================================================================
 *
 * Relationship
 * ------------
 *
 * Same Binary Trie.
 *
 * Difference
 * ----------
 *
 * Trie changes dynamically while DFS enters
 * and leaves tree nodes.
 *
 * Needed Feature
 * --------------
 *
 * Insert.
 * Delete.
 * Query.
 *
 * Interview Note
 * --------------
 *
 * Add frequency counters to support deletion.
 *
 * =========================================================================
 * 🧠 MASTERY CHECKLIST
 * =========================================================================
 *
 * □ Can I explain why XOR prefers opposite bits?
 *
 * □ Can I explain why MSB is processed first?
 *
 * □ Can I derive the greedy rule without memorizing?
 *
 * □ Can I reconstruct BinaryTrie from memory?
 *
 * □ Can I implement insert()?
 *
 * □ Can I implement queryMaximumXor()?
 *
 * □ Do I know why opposite branch is always preferred?
 *
 * □ Can I prove correctness?
 *
 * □ Can I identify when Trie is NOT appropriate?
 *
 * □ Can I adapt the Trie for:
 *
 *      • online queries
 *      • prefix XOR
 *      • counting
 *      • deletion
 *
 * =========================================================================
 * DEBUGGING PLAYBOOK
 * =========================================================================
 *
 * Symptom
 * -------
 * Wrong answer on every test.
 *
 * Check
 * -----
 *
 * Processing bits from
 *
 *      0 -> 31
 *
 * instead of
 *
 *      31 -> 0.
 *
 * -------------------------------------------------------------------------
 *
 * Symptom
 * -------
 * Always returning zero.
 *
 * Check
 * -----
 *
 * Forgetting
 *
 * xorValue |= (1 << bitIndex)
 *
 * after taking opposite branch.
 *
 * -------------------------------------------------------------------------
 *
 * Symptom
 * -------
 * NullPointerException.
 *
 * Check
 * -----
 *
 * Child creation during insertion.
 *
 * -------------------------------------------------------------------------
 *
 * Symptom
 * -------
 * Misses first element.
 *
 * Check
 * -----
 *
 * Initial insertion order.
 *
 * -------------------------------------------------------------------------
 *
 * Symptom
 * -------
 * Wrong on streaming variant.
 *
 * Check
 * -----
 *
 * Query before insert.
 *
 * =========================================================================
 * IMPLEMENTATION MUSCLE MEMORY
 * =========================================================================
 *
 * Build Trie
 *
 * root
 *
 * for bit = 31 ... 0
 *
 *      create child
 *      move
 *
 * ------------------------------------
 *
 * Query Trie
 *
 * xor = 0
 *
 * for bit = 31 ... 0
 *
 *      desired = bit ^ 1
 *
 *      if desired exists
 *
 *          set XOR bit
 *          move desired
 *
 *      else
 *
 *          move same
 *
 * return xor
 *
 * ------------------------------------
 *
 * Solve
 *
 * insert first
 *
 * for remaining
 *
 *      query
 *      update answer
 *      insert
 *
 * =========================================================================
 * COMMON INTERVIEW QUESTIONS
 * =========================================================================
 *
 * Q:
 * Why 32 iterations?
 *
 * A:
 * Java int contains 32 bits.
 *
 * -------------------------------------------------------------------------
 *
 * Q:
 * Why unsigned right shift (>>>)?
 *
 * A:
 * It safely extracts bits without sign extension.
 *
 * Even though this problem has only non-negative integers,
 * >>> is the conventional bit-extraction operator.
 *
 * -------------------------------------------------------------------------
 *
 * Q:
 * Why not store characters/bits explicitly?
 *
 * A:
 * The edge taken already represents the bit.
 *
 * child[0]
 * means bit 0.
 *
 * child[1]
 * means bit 1.
 *
 * No additional field is necessary.
 *
 * -------------------------------------------------------------------------
 *
 * Q:
 * Why only two children?
 *
 * A:
 * Binary representation contains only
 *
 * 0
 * 1
 *
 * therefore branching factor equals two.
 *
 * =========================================================================
 * BIT VISUALIZATION
 * =========================================================================
 *
 * Example
 *
 * number = 5
 *
 * Binary
 *
 * 00000000000000000000000000000101
 *
 * Query wants
 *
 * 11111111111111111111111111111010
 *
 * Of course that exact value probably
 * does not exist.
 *
 * Therefore,
 * at every level we greedily choose
 * the opposite bit whenever available.
 *
 * This constructs the lexicographically
 * largest XOR bit string.
     *

     *
 */


/*
 * =========================================================================
 * FULL TRIE VISUALIZATION
 * =========================================================================
 *
 * Every node stores ONLY pointers.
 *
 * The edge index itself represents the bit.
 *
 * TrieNode
 *
 *      children[0]
 *      children[1]
 *
 * There is NO field:
 *
 *      bit
 *
 * because the index already tells us
 * which branch was taken.
 *
 * Example
 *
 * Insert
 *
 *      5
 *      10
 *
 * Binary (showing only lowest 4 bits)
 *
 *      5  = 0101
 *      10 = 1010
 *
 *                     root
 *                   /      \
 *                (0)        (1)
 *                /            \
 *             level2        level2
 *            /     \        /     \
 *         (0)     (1)    (0)     (1)
 *          |        |      |        |
 *        level3   level3 level3   level3
 *          |        |      |        |
 *         ...
 *
 * Each edge label
 *
 *      (0)
 *      (1)
 *
 * is simply the child index.
 *
 * Nothing else is stored.
 *
 * =========================================================================
 * WHY EDGE INDEX IS ENOUGH
 * =========================================================================
 *
 * During insertion:
 *
 * bit = (number >>> bitIndex) & 1;
 *
 * current = current.children[bit];
 *
 * If we move to
 *
 * children[0]
 *
 * we already know
 *
 * current bit = 0.
 *
 * Likewise
 *
 * children[1]
 *
 * means
 *
 * current bit = 1.
 *
 * Therefore
 *
 * storing another variable
 *
 *      int bit;
 *
 * is completely redundant.
 *
 * =========================================================================
 * WHY GREEDY NEVER BACKTRACKS
 * =========================================================================
 *
 * Suppose we are deciding
 *
 * bit 30.
 *
 * Choice A
 *
 * produces
 *
 *      XOR bit = 1
 *
 * Choice B
 *
 * produces
 *
 *      XOR bit = 0
 *
 * Maximum gain from all remaining
 * lower bits is
 *
 *      2^30 - 1
 *
 * But gaining bit30 already contributes
 *
 *      2^30
 *
 * Therefore
 *
 * once opposite child exists,
 * there is absolutely no future sequence
 * that can compensate for rejecting it.
 *
 * Hence
 *
 * no recursion
 * no DP
 * no backtracking
 * no reconsideration
 *
 * =========================================================================
 * FREQUENT IMPLEMENTATION MISTAKES
 * =========================================================================
 *
 * Mistake #1
 *
 * Using
 *
 *      >>
 *
 * instead of
 *
 *      >>>
 *
 * Works here because inputs are non-negative,
 * but >>> is the safer interview habit.
 *
 * -------------------------------------------------------------------------
 *
 * Mistake #2
 *
 * Looping
 *
 *      bit = 0 -> 31
 *
 * instead of
 *
 *      31 -> 0.
 *
 * Breaks greedy correctness.
 *
 * -------------------------------------------------------------------------
 *
 * Mistake #3
 *
 * Forgetting
 *
 * xorValue |= (1 << bitIndex)
 *
 * after taking opposite branch.
 *
 * -------------------------------------------------------------------------
 *
 * Mistake #4
 *
 * Updating answer during insertion.
 *
 * Cleaner approach:
 *
 * Query
 * Update answer
 * Insert
 *
 * -------------------------------------------------------------------------
 *
 * Mistake #5
 *
 * Thinking trie stores integers.
 *
 * Reality:
 *
 * Trie stores paths.
 *
 * Integers emerge only by walking
 * from root to leaf.
 *
 * =========================================================================
 * TIME & SPACE ANALYSIS
 * =========================================================================
 *
 * Let
 *
 * N = number of integers
 *
 * B = number of bits = 32
 *
 * Insert
 *
 * O(B)
 *
 * Query
 *
 * O(B)
 *
 * Total
 *
 * O(32N)
 *
 * which simplifies to
 *
 * O(N)
 *
 * -------------------------------------------------------------------------
 *
 * Maximum Nodes
 *
 * Each inserted integer creates
 * at most 32 new nodes.
 *
 * Therefore
 *
 * O(32N)
 *
 * memory.
 *
 * =========================================================================
 * PATTERN BOUNDARY
 * =========================================================================
 *
 * Use Binary Trie when:
 *
 * ✓ optimization depends on bits
 * ✓ greedy by MSB is valid
 * ✓ repeated XOR queries
 * ✓ online processing
 *
 * Avoid Binary Trie when:
 *
 * ✗ ordering is arithmetic rather than bitwise
 * ✗ objective is unrelated to bit representation
 * ✗ comparison depends on multiple numbers simultaneously
 *
 * =========================================================================
 * MINI RE-DERIVATION EXERCISE
 * =========================================================================
 *
 * Without memorizing code,
 * reconstruct the algorithm:
 *
 * 1. XOR likes opposite bits.
 *
 * 2. Higher bits dominate.
 *
 * 3. Therefore process MSB first.
 *
 * 4. Need fast lookup of opposite bit.
 *
 * 5. Binary Trie provides exactly that.
 *
 * 6. Query every number.
 *
 * 7. Update answer.
 *
 * 8. Insert current number.
 *
 * Algorithm rediscovered.
 *
 * =========================================================================
 * SELF-QUIZ
 * =========================================================================
 *
 * Q1.
 * Why not DFS every trie path?
 *
 * A.
 * Greedy already proves one branch is always optimal.
 *
 * -------------------------------------------------------------------------
 *
 * Q2.
 * Why exactly two children?
 *
 * A.
 * Binary digits.
 *
 * -------------------------------------------------------------------------
 *
 * Q3.
 * Why not sort first?
 *
 * A.
 * Numeric proximity has no relationship
 * with XOR optimality.
 *
 * -------------------------------------------------------------------------
 *
 * Q4.
 * Why query before insertion?
 *
 * A.
 * Maintains the invariant that the trie
 * contains only previously processed numbers.
 *
 * -------------------------------------------------------------------------
 *
 * Q5.
 * Can duplicates break correctness?
 *
 * A.
 * No.
 *
 * Duplicate paths simply overlap.
 *
 * =========================================================================
 * READY-TO-REMEMBER IMPLEMENTATION SKELETON
 * =========================================================================
 *
 * TrieNode
 *     children[2]
 *
 * insert(x)
 *     for 31 -> 0
 *         create child
 *         move
 *
 * query(x)
 *     xor = 0
 *     for 31 -> 0
 *         opposite = bit ^ 1
 *         if exists
 *             set xor bit
 *             move opposite
 *         else
 *             move same
 *     return xor
 *
 * solve()
 *     insert first
 *     for remaining
 *         answer = max(answer, query())
 *         insert()
 *
 * =========================================================================
 */



    /*
     * =========================================================================
     * 🧪 SELF-VERIFYING TESTS
     * =========================================================================
     */

    private static void assertEquals(int expected, int actual, String message) {

        if (expected != actual) {
            throw new AssertionError(
                    message +
                            "\nExpected : " + expected +
                            "\nActual   : " + actual
            );
        }
    }

    private static void runBruteForceTests() {

        BruteForceSolution solution = new BruteForceSolution();

        // Happy path from problem statement.
        assertEquals(
                28,
                solution.findMaximumXOR(new int[]{3, 10, 5, 25, 2, 8}),
                "Brute Force Example 1"
        );

        // Second official example.
        assertEquals(
                127,
                solution.findMaximumXOR(
                        new int[]{14, 70, 53, 83, 49, 91, 36, 80, 92, 51, 66, 70}
                ),
                "Brute Force Example 2"
        );

        // Single element.
        assertEquals(
                0,
                solution.findMaximumXOR(new int[]{7}),
                "Brute Force Single Element"
        );

        // Duplicate numbers.
        assertEquals(
                0,
                solution.findMaximumXOR(new int[]{9, 9, 9}),
                "Brute Force Duplicates"
        );

        // Zero.
        assertEquals(
                0,
                solution.findMaximumXOR(new int[]{0, 0}),
                "Brute Force Zero"
        );
    }

    private static void runPrefixHashSetTests() {

        PrefixHashSetSolution solution = new PrefixHashSetSolution();

        assertEquals(
                28,
                solution.findMaximumXOR(new int[]{3, 10, 5, 25, 2, 8}),
                "Prefix HashSet Example 1"
        );

        assertEquals(
                127,
                solution.findMaximumXOR(
                        new int[]{14, 70, 53, 83, 49, 91, 36, 80, 92, 51, 66, 70}
                ),
                "Prefix HashSet Example 2"
        );

        assertEquals(
                0,
                solution.findMaximumXOR(new int[]{1}),
                "Prefix HashSet Single Element"
        );

        assertEquals(
                15,
                solution.findMaximumXOR(new int[]{0, 15}),
                "Prefix HashSet Boundary"
        );
    }

    private static void runOptimalTests() {

        OptimalSolution solution = new OptimalSolution();

        // Official example.
        assertEquals(
                28,
                solution.findMaximumXOR(new int[]{3, 10, 5, 25, 2, 8}),
                "Optimal Example 1"
        );

        // Official example.
        assertEquals(
                127,
                solution.findMaximumXOR(
                        new int[]{14, 70, 53, 83, 49, 91, 36, 80, 92, 51, 66, 70}
                ),
                "Optimal Example 2"
        );

        // Only one number.
        assertEquals(
                0,
                solution.findMaximumXOR(new int[]{42}),
                "Optimal Single Element"
        );

        // Duplicate values.
        assertEquals(
                0,
                solution.findMaximumXOR(new int[]{100, 100}),
                "Optimal Duplicate Values"
        );

        // Opposite bits.
        assertEquals(
                15,
                solution.findMaximumXOR(new int[]{0, 15}),
                "Optimal Opposite Bits"
        );

        // Large value boundary.
        assertEquals(
                Integer.MAX_VALUE,
                solution.findMaximumXOR(
                        new int[]{0, Integer.MAX_VALUE}
                ),
                "Optimal Integer Boundary"
        );
    }

    private static void runStreamingTests() {

        StreamingMaximumXor stream = new StreamingMaximumXor();

        assertEquals(
                0,
                stream.add(3),
                "Streaming First Insert"
        );

        assertEquals(
                9,
                stream.add(10),
                "Streaming Second Insert"
        );

        assertEquals(
                15,
                stream.add(5),
                "Streaming Third Insert"
        );

        assertEquals(
                28,
                stream.add(25),
                "Streaming Fourth Insert"
        );
    }

    private static void runMaximumSubarrayTests() {

        MaximumSubarrayXor solution = new MaximumSubarrayXor();

        // Entire array.
        assertEquals(
                15,
                solution.maximumSubarrayXor(new int[]{8, 1, 2, 4}),
                "Maximum Subarray XOR"
        );

        // All zeros.
        assertEquals(
                0,
                solution.maximumSubarrayXor(new int[]{0, 0, 0}),
                "Maximum Subarray All Zero"
        );
    }

    /*
     * =========================================================================
     * MAIN
     * =========================================================================
     */

    public static void main(String[] args) {

        runBruteForceTests();

        runPrefixHashSetTests();

        runOptimalTests();

        runStreamingTests();

        runMaximumSubarrayTests();

        System.out.println("All tests passed.");

        /*
         * =====================================================================
         * 🧘 FINAL CLOSURE STATEMENT
         * =====================================================================
         *
         * I understand the invariant.
         *
         * I can re-derive the solution.
         *
         * I can physically reconstruct the implementation under pressure.
         *
         * This chapter is complete.
         *
         * =====================================================================
         */
    }
}