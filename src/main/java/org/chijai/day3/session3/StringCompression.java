package org.chijai.day3.session3;

import java.util.Arrays;

/**
 * LeetCode 443 — String Compression
 * https://leetcode.com/problems/string-compression/
 */
public class StringCompression {

    /**
     * =================================================================================
     * 1. DETAILED PROBLEM STATEMENT
     * =================================================================================
     *
     * Given a char array chars, compress each MAXIMAL CONSECUTIVE group in-place.
     *
     * For a group:
     *
     *     length == 1
     *         write only the character
     *
     *     length > 1
     *         write the character followed by every decimal digit of its length
     *
     * Example:
     *
     *     12 -> '1', '2'
     *
     * The compressed result must occupy the SAME chars array.
     * Return the length k of the valid compressed prefix:
     *
     *     chars[0 .. k - 1]
     *
     * Anything after index k - 1 is irrelevant.
     *
     * Required extra space:
     *
     *     O(1)
     *
     *
     * Example 1
     * ---------
     *
     *     Input:
     *         [a, a, b, b, c, c, c]
     *
     *     Groups:
     *         aa | bb | ccc
     *
     *     Compression:
     *         a2 | b2 | c3
     *
     *     Valid prefix:
     *         [a, 2, b, 2, c, 3]
     *
     *     Return:
     *         6
     *
     *
     * Example 2 — singleton
     * ---------------------
     *
     *     Input:
     *         [a]
     *
     *     Output:
     *         [a]
     *
     *     Return:
     *         1
     *
     * Important:
     *
     *     a -> a
     *
     * not:
     *
     *     a -> a1
     *
     *
     * Example 3 — multi-digit count
     * -----------------------------
     *
     *     Input:
     *         [a, b, b, b, b, b, b, b, b, b, b, b, b]
     *
     *     Groups:
     *         a | bbbbbbbbbbbb
     *
     *     Compression:
     *         a | b12
     *
     *     Valid prefix:
     *         [a, b, 1, 2]
     *
     *     Return:
     *         4
     *
     *
     * Example 4 — groups are consecutive
     * ----------------------------------
     *
     *     Input:
     *         [a, a, b, a, a]
     *
     *     Groups:
     *         aa | b | aa
     *
     *     Compression:
     *         a2ba2
     *
     * The four 'a' characters are NOT one group because b separates them.
     *
     *
     * What the statement is really asking
     * -----------------------------------
     *
     *     discover one consecutive group
     *         ->
     *     summarize it
     *         ->
     *     write that summary back into already-consumed space
     */

    /**
     * =================================================================================
     * 2. FIRST-PRINCIPLES INVENTION PATH
     * =================================================================================
     *
     * What is the problem really asking?
     *
     *     Read one consecutive group.
     *     Count how many characters it contains.
     *     Write its compressed form back into the same array.
     *
     * Because we read and write in the same array:
     *
     *     read
     *         reads original input
     *
     *     write
     *         writes compressed output
     *
     * For each group:
     *
     *     1. remember the character
     *     2. move read until the group ends
     *     3. groupLength = read - groupStart
     *     4. write the character
     *     5. if groupLength > 1:
     *
     *            12 -> "12" -> '1', '2'
     *
     *            write those digit characters
     *
     * Mental model:
     *
     *     READ GROUP
     *         ->
     *     COUNT IT
     *         ->
     *     WRITE CHAR
     *         ->
     *     WRITE COUNT IF > 1
     *
     * No manual digit mathematics is needed in the primary solution.
     */

    /**
     * =================================================================================
     * 3. PRIMARY INTERVIEW SOLUTION
     * =================================================================================
     *
     * Pattern:
     *
     *     Two Pointers -> Read / Write -> In-place Transformation
     *
     * Sub-pattern:
     *
     *     Consecutive Group Scan / Run-Length Encoding
     *
     * Invariant:
     *
     *     chars[0 .. write - 1]
     *         is already the final compressed output for processed groups
     *
     *     read
     *         points to the first unprocessed character
     *
     *     write <= read
     *
     * Recall:
     *
     *     READ GROUP -> WRITE CHAR -> WRITE COUNT IF > 1.
     */

    public int compress(char[] chars) {

        int read = 0;
        int write = 0;

        while (read < chars.length) {

            char groupChar = chars[read];
            int groupStart = read;

            while (read < chars.length
                    && chars[read] == groupChar) {
                read++;
            }

            int groupLength =
                    read - groupStart;

            chars[write++] =
                    groupChar;

            if (groupLength > 1) {

                String count =
                        String.valueOf(groupLength);

                for (char digit : count.toCharArray()) {
                    chars[write++] = digit;
                }
            }
        }

        return write;
    }


    /**
     * Optional stricter-space follow-up
     * ---------------------------------
     *
     * The primary version above intentionally favors easy interview reconstruction.
     *
     * If an interviewer explicitly forbids even the tiny temporary String created by
     * String.valueOf(groupLength), digits can be written with manual arithmetic.
     *
     * Do not memorize that version by default.
     * It solves a stricter implementation constraint, not the core problem.
     */


    /**
     * =================================================================================
     * 5. CORRECTNESS + COMPLEXITY + TRAPS
     * =================================================================================
     *
     * Why correct
     * -----------
     * 1. The inner loop stops exactly at a character change or array end.
     *    Therefore [groupStart, read) is one complete maximal group.
     *
     * 2. That group is encoded exactly by:
     *
     *        character
     *        +
     *        count digits iff count > 1
     *
     * 3. read moves only forward, so every input character belongs to exactly one group.
     *
     * 4. Each encoded group is no longer than the group consumed.
     *    Therefore write never destroys unread input.
     *
     * Complexity
     * ----------
     *     Time      O(n)
     *
     *     Primary implementation uses only a tiny temporary String for the
     *     current group's count; it does not build a second output buffer.
     *
     * Traps
     * -----
     * • singleton:
     *
     *       a -> a
     *
     *   not a1
     *
     * • count 12 means:
     *
     *       '1', '2'
     *
     *   not one char holding integer 12
     *
     * • do not build a second compressed output buffer; only the current
     *   group's count is temporarily converted to a String
     *
     * • final group needs no special cleanup because array-end is a normal group boundary
     *
     * • this is grouped scanning, not sliding window
     */

    /**
     * =================================================================================
     * 6. INTERVIEW ARTICULATION + BLANK-BRAIN RECONSTRUCTION
     * =================================================================================
     *
     * Say before coding
     * -----------------
     *
     * "I'll use separate read and write pointers. read consumes one maximal run of equal
     * characters, and write stores that run's compressed form in the same array. For a
     * repeated group, I'll convert its count to a String and write those digit characters.
     * The core idea is one-pass grouped scanning with in-place read/write pointers."
     *
     * Blank-brain reconstruction
     * --------------------------
     *
     * In-place transform?
     *     read + write
     *
     * What does read consume?
     *     one complete equal-character group
     *
     * Count?
     *     read - groupStart
     *
     * Always write?
     *     group character
     *
     * Write count?
     *     only if > 1
     *
     * Multi-digit count?
     *     String.valueOf(count), then write each digit
     *
     * Safety?
     *     encoded group length <= original group length
     *
     * One-line recall:
     *
     *     READ GROUP -> WRITE CHAR -> WRITE COUNT IF > 1.
     *
     * Related problems:
     *     see the dedicated transfer map below.
     */


    /**
     * =================================================================================
     * 7. RELATED / SIMILAR PROBLEMS
     * =================================================================================
     *
     * Highest transfer value — SAME read/write family
     * ------------------------------------------------
     *
     * LC 27 — Remove Element
     *
     *     READ every value
     *     WRITE only survivors
     *
     * LC 26 — Remove Duplicates from Sorted Array
     *
     *     READ sorted values
     *     WRITE one accepted representative
     *
     * LC 80 — Remove Duplicates from Sorted Array II
     *
     *     READ values
     *     WRITE according to a bounded-duplicates rule
     *
     * LC 283 — Move Zeroes
     *
     *     READ useful values
     *     WRITE them forward
     *     fill the remainder with zeroes
     *
     * Shared abstraction:
     *
     *     READ ORIGINAL STREAM
     *         ->
     *     DECIDE WHAT THIS VALUE / GROUP CONTRIBUTES
     *         ->
     *     WRITE COMPACTED OUTPUT BEHIND READ
     *
     *
     * Same consecutive-group thinking
     * -------------------------------
     *
     * LC 485 — Max Consecutive Ones
     *
     *     scan runs and measure their lengths
     *
     * LC 1446 — Consecutive Characters
     *
     *     detect maximal equal-character runs
     *
     * LC 1869 — Longer Contiguous Segments of Ones than Zeros
     *
     *     compare lengths of contiguous runs
     *
     *
     * Similar name, DIFFERENT algorithmic family
     * ------------------------------------------
     *
     * LC 1531 — String Compression II
     *
     *     You may delete characters before compressing.
     *
     *     Therefore local deterministic group processing is insufficient.
     *
     *     Pattern:
     *         Dynamic Programming
     *
     * Distinction worth remembering:
     *
     *     LC 443
     *         deterministic in-place transformation
     *
     *     LC 1531
     *         optimize compression after deletions -> DP
     *
     *
     * Suggested practice order
     * ------------------------
     *
     *     Remove Element
     *         ->
     *     Remove Duplicates
     *         ->
     *     Remove Duplicates II
     *         ->
     *     Move Zeroes
     *         ->
     *     String Compression
     */


    /**
     * =================================================================================
     * 8. RELATED PROBLEMS — COMPACT TRANSFER SOLUTIONS
     * =================================================================================
     *
     * Purpose of this section
     * -----------------------
     * These are NOT eight more full study notes.
     *
     * Each implementation exists only to expose what transfers from LC443:
     *
     *     READ
     *         ->
     *     DECIDE
     *         ->
     *     WRITE / MEASURE
     *
     * Keep LC443 as the canonical explanation of read/write in-place transformation.
     */


    /**
     * LC 27 — Remove Element
     *
     * Transfer:
     *     READ everything -> WRITE only survivors.
     *
     * Invariant:
     *     nums[0 .. write-1] contains exactly the kept values seen so far.
     *
     * Time O(n), Extra O(1)
     */
    static class RemoveElement {

        public int removeElement(
                int[] nums,
                int val
        ) {

            int write = 0;

            for (int read = 0;
                 read < nums.length;
                 read++) {

                if (nums[read] != val) {
                    nums[write++] = nums[read];
                }
            }

            return write;
        }
    }


    /**
     * LC 26 — Remove Duplicates from Sorted Array
     *
     * Sorted order makes duplicates adjacent.
     *
     * Transfer:
     *     READ candidates -> WRITE only a new distinct value.
     *
     * Invariant:
     *     nums[0 .. write-1] is the unique prefix.
     *
     * Time O(n), Extra O(1)
     */
    static class RemoveDuplicates {

        public int removeDuplicates(int[] nums) {

            if (nums.length == 0) {
                return 0;
            }

            int write = 1;

            for (int read = 1;
                 read < nums.length;
                 read++) {

                if (nums[read] != nums[write - 1]) {
                    nums[write++] = nums[read];
                }
            }

            return write;
        }
    }


    /**
     * LC 80 — Remove Duplicates from Sorted Array II
     *
     * Keep each value at most twice.
     *
     * Key test:
     *
     *     write < 2
     *         always safe
     *
     *     otherwise
     *         candidate may be written iff it differs from nums[write - 2]
     *
     * Why nums[write - 2]?
     *     If candidate equals it, accepting candidate would create three copies.
     *
     * Time O(n), Extra O(1)
     */
    static class RemoveDuplicatesII {

        public int removeDuplicates(int[] nums) {

            int write = 0;

            for (int read = 0;
                 read < nums.length;
                 read++) {

                if (write < 2
                        || nums[read] != nums[write - 2]) {

                    nums[write++] = nums[read];
                }
            }

            return write;
        }
    }


    /**
     * LC 283 — Move Zeroes
     *
     * Phase 1:
     *     READ all values -> WRITE non-zero values forward.
     *
     * Phase 2:
     *     fill remaining output positions with zeroes.
     *
     * Relative order of non-zero values is preserved.
     *
     * Time O(n), Extra O(1)
     */
    static class MoveZeroes {

        public void moveZeroes(int[] nums) {

            int write = 0;

            for (int read = 0;
                 read < nums.length;
                 read++) {

                if (nums[read] != 0) {
                    nums[write++] = nums[read];
                }
            }

            while (write < nums.length) {
                nums[write++] = 0;
            }
        }
    }


    /**
     * LC 485 — Max Consecutive Ones
     *
     * Same run-scanning idea as LC443, but no output array is written.
     *
     * current
     *     length of the run ending here
     *
     * best
     *     largest run seen anywhere
     *
     * Time O(n), Extra O(1)
     */
    static class MaxConsecutiveOnes {

        public int findMaxConsecutiveOnes(int[] nums) {

            int current = 0;
            int best = 0;

            for (int value : nums) {

                if (value == 1) {
                    current++;
                    best = Math.max(best, current);
                } else {
                    current = 0;
                }
            }

            return best;
        }
    }


    /**
     * LC 1446 — Consecutive Characters
     *
     * Same maximal-equal-run recognition as LC443.
     *
     * Difference:
     *     measure the longest run instead of compressing every run.
     *
     * Time O(n), Extra O(1)
     */
    static class ConsecutiveCharacters {

        public int maxPower(String s) {

            int current = 1;
            int best = 1;

            for (int i = 1;
                 i < s.length();
                 i++) {

                if (s.charAt(i) == s.charAt(i - 1)) {
                    current++;
                } else {
                    current = 1;
                }

                best = Math.max(best, current);
            }

            return best;
        }
    }


    /**
     * LC 1869 — Longer Contiguous Segments of Ones than Zeros
     *
     * Scan one current run.
     *
     * Depending on the run's character, update:
     *
     *     longestOne
     * or
     *     longestZero
     *
     * Time O(n), Extra O(1)
     */
    static class LongerOnesThanZeroes {

        public boolean checkZeroOnes(String s) {

            int current = 1;
            int longestOne = 0;
            int longestZero = 0;

            for (int i = 0;
                 i < s.length();
                 i++) {

                if (i > 0
                        && s.charAt(i) == s.charAt(i - 1)) {
                    current++;
                } else {
                    current = 1;
                }

                if (s.charAt(i) == '1') {
                    longestOne =
                            Math.max(longestOne, current);
                } else {
                    longestZero =
                            Math.max(longestZero, current);
                }
            }

            return longestOne > longestZero;
        }
    }


    /**
     * LC 1531 — String Compression II
     *
     * IMPORTANT:
     *     Similar compression vocabulary, DIFFERENT algorithmic family.
     *
     * We may delete up to k characters before compressing.
     *
     * Local greedy group processing no longer works because deleting a character
     * can merge two separated runs and change future compression cost.
     *
     * DP state:
     *
     *     solve(index, deletesLeft)
     *
     * At index:
     *
     *     Choice A
     *         delete s[index]
     *
     *     Choice B
     *         keep s[index] as the character of the next compressed run
     *         and scan forward
     *
     * During that scan:
     *
     *     same
     *         number of copies of s[index] kept in this run
     *
     *     different
     *         intervening characters that must be deleted
     *
     * Candidate cost:
     *
     *     encodedLength(same)
     *         +
     *     solve(nextIndex, remainingDeletes)
     *
     * Time:
     *     O(n^2 * k)
     *
     * Space:
     *     O(n * k) memo + recursion stack
     */
    static class StringCompressionII {

        private static final int INF = 1_000_000;

        public int getLengthOfOptimalCompression(
                String s,
                int k
        ) {

            int[][] memo =
                    new int[s.length()][k + 1];

            for (int[] row : memo) {
                Arrays.fill(row, -1);
            }

            return solve(
                    s,
                    0,
                    k,
                    memo
            );
        }

        private int solve(
                String s,
                int index,
                int deletesLeft,
                int[][] memo
        ) {

            if (index == s.length()
                    || s.length() - index <= deletesLeft) {
                return 0;
            }

            if (memo[index][deletesLeft] != -1) {
                return memo[index][deletesLeft];
            }

            int best = INF;

            // Choice A: delete s[index].
            if (deletesLeft > 0) {
                best = solve(
                        s,
                        index + 1,
                        deletesLeft - 1,
                        memo
                );
            }

            // Choice B: keep s[index] and build its next compressed run.
            int same = 0;
            int different = 0;

            for (int end = index;
                 end < s.length();
                 end++) {

                if (s.charAt(end) == s.charAt(index)) {
                    same++;
                } else {
                    different++;
                }

                if (different > deletesLeft) {
                    break;
                }

                int candidate =
                        encodedRunLength(same)
                                + solve(
                                        s,
                                        end + 1,
                                        deletesLeft - different,
                                        memo
                                );

                best = Math.min(best, candidate);
            }

            memo[index][deletesLeft] = best;
            return best;
        }

        private int encodedRunLength(int count) {

            if (count == 1) {
                return 1;
            }

            if (count < 10) {
                return 2;
            }

            if (count < 100) {
                return 3;
            }

            return 4;
        }
    }


    /**
     * =================================================================================
     * 9. SELF-VERIFYING TESTS
     * =================================================================================
     */
    public static void main(String[] args) {

        testPrimary();
        testRelatedProblems();

        System.out.println("All assertions passed.");
    }

    private static void testPrimary() {

        StringCompression solution = new StringCompression();

        verify(
                solution,
                new char[]{'a', 'a', 'b', 'b', 'c', 'c', 'c'},
                "a2b2c3"
        );

        verify(
                solution,
                new char[]{'a'},
                "a"
        );

        verify(
                solution,
                new char[]{
                        'a',
                        'b', 'b', 'b', 'b', 'b', 'b',
                        'b', 'b', 'b', 'b', 'b', 'b'
                },
                "ab12"
        );

        verify(
                solution,
                new char[]{'a', 'a'},
                "a2"
        );

        verify(
                solution,
                new char[]{'a', 'b', 'c'},
                "abc"
        );
    }

    private static void testRelatedProblems() {

        int[] removeElementInput = {3, 2, 2, 3};
        int removeElementLength =
                new RemoveElement()
                        .removeElement(removeElementInput, 3);

        assert removeElementLength == 2;
        assert Arrays.equals(
                Arrays.copyOf(removeElementInput, removeElementLength),
                new int[]{2, 2}
        );


        int[] uniqueInput = {1, 1, 2};
        int uniqueLength =
                new RemoveDuplicates()
                        .removeDuplicates(uniqueInput);

        assert Arrays.equals(
                Arrays.copyOf(uniqueInput, uniqueLength),
                new int[]{1, 2}
        );


        int[] twiceInput = {0, 0, 1, 1, 1, 1, 2, 3, 3};
        int twiceLength =
                new RemoveDuplicatesII()
                        .removeDuplicates(twiceInput);

        assert Arrays.equals(
                Arrays.copyOf(twiceInput, twiceLength),
                new int[]{0, 0, 1, 1, 2, 3, 3}
        );


        int[] moveZeroesInput = {0, 1, 0, 3, 12};

        new MoveZeroes()
                .moveZeroes(moveZeroesInput);

        assert Arrays.equals(
                moveZeroesInput,
                new int[]{1, 3, 12, 0, 0}
        );


        assert new MaxConsecutiveOnes()
                .findMaxConsecutiveOnes(
                        new int[]{1, 1, 0, 1, 1, 1}
                ) == 3;


        assert new ConsecutiveCharacters()
                .maxPower("leetcode") == 2;


        assert new LongerOnesThanZeroes()
                .checkZeroOnes("1101");


        StringCompressionII compressionII =
                new StringCompressionII();

        assert compressionII
                .getLengthOfOptimalCompression(
                        "aaabcccd",
                        2
                ) == 4;

        assert compressionII
                .getLengthOfOptimalCompression(
                        "aabbaa",
                        2
                ) == 2;
    }

    private static void verify(
            StringCompression solution,
            char[] chars,
            String expected
    ) {

        int length = solution.compress(chars);

        String actual =
                new String(
                        Arrays.copyOf(chars, length)
                );

        assert expected.equals(actual)
                : "Expected " + expected + " but got " + actual;
    }
}
