package org.chijai.day7.session1.heap;

import java.util.*;

/**
 * HEAP / BUCKET / QUICKSELECT — INTERVIEW PATTERN WORKBOOK
 *
 * ============================================================================
 * MASTER DECISION FLOW
 * ============================================================================
 *
 *                  TOP-K / KTH / RANK / FREQUENCY
 *                               |
 *                               v
 *                  What am I ranking by?
 *                               |
 *              +----------------+----------------+
 *              |                                 |
 *              v                                 v
 *      BOUNDED INTEGER SCORE?            LARGE / ARBITRARY SCORE?
 *      e.g. frequency [1..n]             value, distance, etc.
 *              |                                 |
 *             YES                                YES
 *              |                                 |
 *              v                                 v
 *        BUCKET / COUNTING                Is input STREAMING?
 *              |                                 |
 *              |                          +------+------+
 *              |                          |             |
 *              |                         YES            NO
 *              |                          |             |
 *              |                          v             v
 *              |                        HEAP       Need Top-K / kth?
 *              |                                        |
 *              |                                 +------+------+
 *              |                                 |             |
 *              |                                YES            NO
 *              |                                 |             |
 *              |                                 v             v
 *              |                           HEAP / QUICKSELECT  SORT
 *              |
 *              v
 *      Tie-break / secondary ordering?
 *              |
 *        +-----+-----+
 *        |           |
 *       NO          YES
 *        |           |
 *        v           v
 *     BUCKET      HEAP / SORT
 *                 often cleaner
 *
 * ============================================================================
 * SHORTEST MEMORY RULE
 * ============================================================================
 *
 * BUCKET
 *   = bounded SCORE
 *
 * HEAP
 *   = bounded NUMBER OF WINNERS
 *
 * QUICKSELECT
 *   = static Top-K / kth + arbitrary score
 *
 * STREAM
 *   = strongly favors HEAP
 *
 * TIE-BREAK
 *   = often favors Comparator + HEAP / SORT
 *
 * ============================================================================
 * PROBLEM MAP
 * ============================================================================
 *
 * 347  Top K Frequent Elements       -> Heap + Bucket
 * 692  Top K Frequent Words          -> Heap
 * 215  Kth Largest Element           -> Quickselect
 * 973  K Closest Points              -> Size-k Max Heap
 * 703  Kth Largest in a Stream       -> Size-k Min Heap
 * 451  Sort Characters by Frequency  -> Bucket
 * 274  H-Index                       -> Counting/Bucket
 */
public class TopKFrequentElements {

    // ========================================================================
    // 347. TOP K FREQUENT ELEMENTS
    // ========================================================================

    /**
     * PROBLEM
     * -------
     * Given nums[] and k, return the k most frequent values.
     * Output order does not matter.
     *
     * Example:
     *   nums = [1,1,1,2,2,3], k = 2
     *   -> [1,2]
     *
     * PATTERN JUDGEMENT
     * -----------------
     * First instinct:
     *   "Top K" -> size-k heap.
     *
     * Better observation:
     *   ranking score = frequency
     *   frequency is bounded by [1..n]
     *
     * Therefore:
     *   Heap   -> valid, O(n + u log k)
     *   Bucket -> optimal, O(n)
     *
     * WHY NOT ONLY HEAP?
     * ------------------
     * Heap solves "keep best k", but still pays log k comparisons.
     *
     * Bucket exploits stronger structure:
     *   frequency itself can become an array/list index.
     *
     * RECALL CUE
     * ----------
     *   value -> frequency
     *   frequency -> values
     *
     *   bounded score => bucket
     */
    static class LC347 {

        /**
         * APPROACH A — SIZE-K MIN HEAP
         *
         * Invariant:
         *   heap contains the best k values seen so far.
         *
         * Why MIN-heap?
         *   root = weakest accepted candidate.
         *
         * If size becomes k+1:
         *   remove weakest.
         *
         * Time:  O(n + u log k)
         * Space: O(u + k)
         */
        static int[] heap(int[] nums, int k) {

            Map<Integer, Integer> freq = count(nums);

            PriorityQueue<Integer> topK =
                    new PriorityQueue<>(
                            Comparator.comparingInt(freq::get)
                    );

            for (int value : freq.keySet()) {

                topK.offer(value);

                if (topK.size() > k) {
                    topK.poll();
                }
            }

            int[] result = new int[k];

            for (int i = k - 1; i >= 0; i--) {
                result[i] = topK.poll();
            }

            return result;
        }

        /**
         * APPROACH B — BUCKET SORT
         *
         * Invariant:
         *   buckets[f] contains every value occurring exactly f times.
         *
         * Key:
         *   max frequency = nums.length.
         *
         * Build:
         *   value -> frequency
         *   frequency -> values
         *
         * Scan:
         *   highest frequency -> lowest
         *   take first k values.
         *
         * Time:  O(n)
         * Space: O(n)
         */
        static int[] bucket(int[] nums, int k) {

            Map<Integer, Integer> freq = count(nums);

            List<List<Integer>> buckets = new ArrayList<>();

            //be careful this is equal to ,
            // not less than,
            // because we want to include the case where
            // the frequency is equal to nums.length
            for (int i = 0; i <= nums.length; i++) {
                buckets.add(new ArrayList<>());
            }

            for (int value : freq.keySet()) {
                buckets.get(freq.get(value)).add(value);
            }

            int[] result = new int[k];
            int resultIndex = 0;

            for (int f = nums.length;
                 f >= 1 && resultIndex < k;
                 f--) {

                List<Integer> numbers =
                        buckets.get(f);

                for (int i = 0; i < numbers.size(); i++) {

                    int num = numbers.get(i);

                    result[resultIndex++] = num;

                    if (resultIndex == k) {
                        break;
                    }
                }
            }

            return result;
        }
    }

    // ========================================================================
    // 692. TOP K FREQUENT WORDS
    // ========================================================================

    /**
     * PROBLEM
     * -------
     * Return the k most frequent words.
     *
     * Ordering:
     *   1. higher frequency first
     *   2. if tied, lexicographically smaller first
     *
     * Example:
     *   ["i","love","leetcode","i","love","coding"], k = 2
     *   -> ["i","love"]
     *
     * PATTERN JUDGEMENT
     * -----------------
     * Looks almost identical to LC347.
     *
     * Frequency is bounded, so bucket is tempting.
     *
     * BUT:
     *   ties require lexical ordering.
     *
     * This adds a second ranking rule.
     * Comparator + heap expresses that cleanly.
     *
     * WHY NOT PURE BUCKET?
     * --------------------
     * You could bucket by frequency, but each bucket would still need
     * lexical ordering.
     *
     * Once secondary ordering matters,
     * Comparator-based heap/sort is often simpler.
     *
     * INVARIANT
     * ---------
     * Keep only the best k words seen so far.
     *
     * Heap root = weakest accepted word:
     *   lower frequency = weaker
     *   same frequency + lexicographically larger = weaker
     *
     * RECALL CUE
     * ----------
     *   bounded frequency + tie-break
     *   => Comparator + Heap
     *
     * Time:  O(n + u log k)
     * Space: O(u + k)
     */
    static class LC692 {

        static List<String> solve(String[] words, int k) {

            Map<String, Integer> freq = new HashMap<>();

            for (String word : words) {
                freq.merge(word, 1, Integer::sum);
            }

            PriorityQueue<String> topK = new PriorityQueue<>(
                    (a, b) -> {

                        int byFrequency =
                                Integer.compare(freq.get(a), freq.get(b));

                        if (byFrequency != 0) {
                            return byFrequency;
                        }

                        // Larger lexical word = weaker candidate.
                        return b.compareTo(a);
                    }
            );

            for (String word : freq.keySet()) {

                topK.offer(word);

                if (topK.size() > k) {
                    topK.poll();
                }
            }

            LinkedList<String> result = new LinkedList<>();

            while (!topK.isEmpty()) {
                result.addFirst(topK.poll());
            }

            return result;
        }
    }

    // ========================================================================
    // 215. KTH LARGEST ELEMENT IN AN ARRAY
    // ========================================================================

    /**
     * PROBLEM
     * -------
     * Return the kth largest element in an unsorted array.
     *
     * Example:
     *   [3,2,1,5,6,4], k = 2
     *   -> 5
     *
     * PATTERN JUDGEMENT
     * -----------------
     * This is selection, not frequency ranking.
     *
     * Ranking score:
     *   nums[i] itself.
     *
     * Value range may be huge / arbitrary.
     * So bucket-by-value is not naturally safe.
     *
     * Input is STATIC.
     * We need only one order statistic.
     *
     * Therefore QUICKSELECT is a strong fit.
     *
     * WHY NOT BUCKET?
     * ---------------
     * A bucket indexed by raw value can waste huge memory
     * when value range is sparse or enormous.
     *
     * WHY NOT HEAP?
     * -------------
     * Heap is valid:
     *   size-k min-heap -> O(n log k)
     *
     * But static input lets Quickselect exploit partitioning:
     *   average O(n).
     *
     * INVARIANT
     * ---------
     * After partition:
     *
     *   left side  <= pivot
     *   pivot at final sorted position
     *   right side >= pivot
     *
     * kth largest corresponds to ascending index:
     *   n - k
     *
     * RECALL CUE
     * ----------
     *   static + kth + arbitrary values
     *   => Quickselect
     *
     * Average Time: O(n)
     * Worst Time:   O(n^2)
     * Space:        O(1) iterative
     */
    static class LC215 {

        static int solve(int[] nums, int k) {

            int target = nums.length - k;
            int left = 0;
            int right = nums.length - 1;

            while (left <= right) {

                int pivotIndex = partition(nums, left, right);

                if (pivotIndex == target) {
                    return nums[pivotIndex];
                }

                if (pivotIndex < target) {
                    left = pivotIndex + 1;
                } else {
                    right = pivotIndex - 1;
                }
            }

            throw new IllegalStateException("Unreachable");
        }

        private static int partition(int[] nums, int left, int right) {

            int pivot = nums[right];
            int boundary = left;

            for (int i = left; i < right; i++) {

                if (nums[i] <= pivot) {
                    swap(nums, i, boundary);
                    boundary++;
                }
            }

            swap(nums, boundary, right);

            return boundary;
        }
    }

    // ========================================================================
    // 973. K CLOSEST POINTS TO ORIGIN
    // ========================================================================

    /**
     * PROBLEM
     * -------
     * Return the k points closest to origin (0,0).
     *
     * Ranking score:
     *   distance^2 = x*x + y*y
     *
     * Example:
     *   [[1,3],[-2,2]], k = 1
     *   -> [[-2,2]]
     *
     * PATTERN JUDGEMENT
     * -----------------
     * Need only k winners.
     *
     * Ranking score = distance.
     * Distance range can be large / sparse.
     *
     * Therefore:
     *   Bucket is not attractive.
     *   Heap fits naturally.
     *
     * WHY MAX-HEAP?
     * -------------
     * We want the k SMALLEST distances.
     *
     * Among accepted k candidates,
     * the FARTHEST is the weakest.
     *
     * Therefore:
     *   root = farthest accepted point.
     *
     * If size > k:
     *   remove root.
     *
     * WHY NOT MIN-HEAP?
     * -----------------
     * A min-heap would expose the strongest candidate,
     * but we need fast access to the weakest accepted one to evict it.
     *
     * RECALL CUE
     * ----------
     *   keep k smallest
     *   => MAX-heap of size k
     *
     * Time:  O(n log k)
     * Space: O(k)
     */
    static class LC973 {

        static int[][] solve(int[][] points, int k) {

            PriorityQueue<int[]> heap = new PriorityQueue<>(
                    (a, b) -> Integer.compare(distance(b), distance(a))
            );

            for (int[] point : points) {

                heap.offer(point);

                if (heap.size() > k) {
                    heap.poll();
                }
            }

            int[][] result = new int[k][2];

            for (int i = 0; i < k; i++) {
                result[i] = heap.poll();
            }

            return result;
        }

        private static int distance(int[] point) {
            return point[0] * point[0] + point[1] * point[1];
        }
    }

    // ========================================================================
    // 703. KTH LARGEST ELEMENT IN A STREAM
    // ========================================================================

    /**
     * PROBLEM
     * -------
     * Numbers arrive one by one.
     * After each add(value), return kth largest seen so far.
     *
     * Example:
     *   k = 3, initial = [4,5,8,2]
     *
     *   add(3)  -> 4
     *   add(5)  -> 5
     *   add(10) -> 5
     *   add(9)  -> 8
     *
     * PATTERN JUDGEMENT
     * -----------------
     * Keyword:
     *   STREAM / ONLINE.
     *
     * Quickselect is unattractive because new values keep arriving.
     *
     * Maintain only the largest k values seen so far.
     *
     * WHY MIN-HEAP?
     * -------------
     * Of the largest k values:
     *
     *   smallest among them = kth largest overall.
     *
     * Therefore:
     *   root = kth largest.
     *
     * INVARIANT
     * ---------
     * Heap always stores at most the k largest values seen so far.
     *
     * add(value):
     *   offer
     *   if size > k -> poll smallest
     *   peek -> kth largest
     *
     * RECALL CUE
     * ----------
     *   stream + kth largest
     *   => size-k MIN-heap
     *
     * Each add: O(log k)
     * Space:    O(k)
     */
    static class LC703 {

        private final int k;
        private final PriorityQueue<Integer> topK =
                new PriorityQueue<>();

        LC703(int k, int[] nums) {

            this.k = k;

            for (int value : nums) {
                add(value);
            }
        }

        int add(int value) {

            topK.offer(value);

            if (topK.size() > k) {
                topK.poll();
            }

            return topK.peek();
        }
    }

    // ========================================================================
    // 451. SORT CHARACTERS BY FREQUENCY
    // ========================================================================

    /**
     * PROBLEM
     * -------
     * Sort characters in a string by decreasing frequency.
     *
     * Example:
     *   "tree"
     *   -> "eert" or "eetr"
     *
     * PATTERN JUDGEMENT
     * -----------------
     * Ranking score = frequency.
     *
     * Frequency is bounded:
     *   1..n
     *
     * Therefore frequency can be a bucket index.
     *
     * WHY NOT HEAP?
     * -------------
     * Heap would work, but it performs comparison work we do not need.
     *
     * Since frequency is already bounded,
     * buckets give direct grouping.
     *
     * INVARIANT
     * ---------
     * bucket[f] contains all chars occurring exactly f times.
     *
     * Scan from n down to 1.
     *
     * RECALL CUE
     * ----------
     *   rank by bounded frequency
     *   => bucket
     *
     * Time:  O(n)
     * Space: O(n)
     */
    static class LC451 {

        static String solve(String s) {

            Map<Character, Integer> freq = new HashMap<>();

            for (char ch : s.toCharArray()) {
                freq.merge(ch, 1, Integer::sum);
            }

            List<List<Character>> buckets = new ArrayList<>();

            for (int i = 0; i <= s.length(); i++) {
                buckets.add(new ArrayList<>());
            }

            for (char ch : freq.keySet()) {
                buckets.get(freq.get(ch)).add(ch);
            }

            StringBuilder result = new StringBuilder();

            for (int f = s.length(); f >= 1; f--) {

                for (char ch : buckets.get(f)) {

                    result.append(
                            String.valueOf(ch).repeat(f)
                    );
                }
            }

            return result.toString();
        }
    }

    // ========================================================================
    // 274. H-INDEX
    // ========================================================================

    /**
     * PROBLEM
     * -------
     * Return maximum h such that at least h papers
     * have at least h citations each.
     *
     * Example:
     *   [3,0,6,1,5]
     *   -> 3
     *
     * PATTERN JUDGEMENT
     * -----------------
     * At first this looks like sorting citations.
     *
     * But there are only n papers.
     *
     * For H-index:
     *   citation counts above n are equivalent.
     *
     * Example if n = 5:
     *
     *   5, 50, 500
     *
     * all behave the same for the maximum possible h.
     *
     * So compress:
     *
     *   bucket[min(citation, n)]++
     *
     * WHY BUCKET / COUNTING?
     * ----------------------
     * Effective score domain becomes:
     *
     *   0..n
     *
     * That is exactly what counting arrays exploit.
     *
     * INVARIANT
     * ---------
     * While scanning h from n downward:
     *
     *   papers = number of papers with at least h citations.
     *
     * First h where:
     *
     *   papers >= h
     *
     * is the answer.
     *
     * RECALL CUE
     * ----------
     *   huge raw values
     *   BUT compressible effective score [0..n]
     *   => counting / bucket
     *
     * Time:  O(n)
     * Space: O(n)
     */
    static class LC274 {

        static int solve(int[] citations) {

            int n = citations.length;
            int[] buckets = new int[n + 1];

            for (int citation : citations) {
                buckets[Math.min(citation, n)]++;
            }

            int papers = 0;

            for (int h = n; h >= 0; h--) {

                papers += buckets[h];

                if (papers >= h) {
                    return h;
                }
            }

            return 0;
        }
    }

    // ========================================================================
    // SHARED HELPERS
    // ========================================================================

    private static Map<Integer, Integer> count(int[] nums) {

        Map<Integer, Integer> freq = new HashMap<>();

        for (int value : nums) {
            freq.merge(value, 1, Integer::sum);
        }

        return freq;
    }

    private static void swap(int[] nums, int i, int j) {

        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    private static void assertInt(int expected, int actual) {

        if (expected != actual) {
            throw new AssertionError(
                    "expected=" + expected + ", actual=" + actual
            );
        }
    }

    private static void assertList(
            List<String> expected,
            List<String> actual) {

        if (!expected.equals(actual)) {
            throw new AssertionError(
                    "expected=" + expected + ", actual=" + actual
            );
        }
    }

    private static void assertAnyOrder(
            int[] expected,
            int[] actual) {

        int[] a = expected.clone();
        int[] b = actual.clone();

        Arrays.sort(a);
        Arrays.sort(b);

        if (!Arrays.equals(a, b)) {
            throw new AssertionError(
                    "expected=" + Arrays.toString(expected)
                            + ", actual=" + Arrays.toString(actual)
            );
        }
    }

    private static void assertPoints(
            int[][] expected,
            int[][] actual) {

        String[] a = new String[expected.length];
        String[] b = new String[actual.length];

        for (int i = 0; i < expected.length; i++) {
            a[i] = Arrays.toString(expected[i]);
        }

        for (int i = 0; i < actual.length; i++) {
            b[i] = Arrays.toString(actual[i]);
        }

        Arrays.sort(a);
        Arrays.sort(b);

        if (!Arrays.equals(a, b)) {
            throw new AssertionError(
                    "expected=" + Arrays.deepToString(expected)
                            + ", actual=" + Arrays.deepToString(actual)
            );
        }
    }

    private static boolean validFrequencySort(
            String input,
            String output) {

        if (input.length() != output.length()) {
            return false;
        }

        Map<Character, Integer> inputFreq = new HashMap<>();
        Map<Character, Integer> outputFreq = new HashMap<>();

        for (char ch : input.toCharArray()) {
            inputFreq.merge(ch, 1, Integer::sum);
        }

        for (char ch : output.toCharArray()) {
            outputFreq.merge(ch, 1, Integer::sum);
        }

        if (!inputFreq.equals(outputFreq)) {
            return false;
        }

        int previousFrequency = Integer.MAX_VALUE;

        for (int i = 0; i < output.length();) {

            char ch = output.charAt(i);
            int frequency = outputFreq.get(ch);

            if (frequency > previousFrequency) {
                return false;
            }

            for (int j = 0; j < frequency; j++) {

                if (i + j >= output.length()
                        || output.charAt(i + j) != ch) {
                    return false;
                }
            }

            previousFrequency = frequency;
            i += frequency;
        }

        return true;
    }

    // ========================================================================
    // MAIN — KEPT AT THE END
    // ========================================================================

    public static void main(String[] args) {

        // LC347
        int[] nums347 = {1, 1, 1, 2, 2, 3};

        assertAnyOrder(
                new int[]{1, 2},
                LC347.heap(nums347, 2)
        );

        assertAnyOrder(
                new int[]{1, 2},
                LC347.bucket(nums347, 2)
        );

        // LC692
        assertList(
                List.of("i", "love"),
                LC692.solve(
                        new String[]{
                                "i", "love", "leetcode",
                                "i", "love", "coding"
                        },
                        2
                )
        );

        // LC215
        assertInt(
                5,
                LC215.solve(
                        new int[]{3, 2, 1, 5, 6, 4},
                        2
                )
        );

        // LC973
        assertPoints(
                new int[][]{{-2, 2}},
                LC973.solve(
                        new int[][]{
                                {1, 3},
                                {-2, 2}
                        },
                        1
                )
        );

        // LC703
        LC703 stream =
                new LC703(
                        3,
                        new int[]{4, 5, 8, 2}
                );

        assertInt(4, stream.add(3));
        assertInt(5, stream.add(5));
        assertInt(5, stream.add(10));
        assertInt(8, stream.add(9));
        assertInt(8, stream.add(4));

        // LC451
        String sorted451 = LC451.solve("tree");

        if (!validFrequencySort("tree", sorted451)) {
            throw new AssertionError(
                    "LC451 failed: " + sorted451
            );
        }

        // LC274
        assertInt(
                3,
                LC274.solve(
                        new int[]{3, 0, 6, 1, 5}
                )
        );

        System.out.println(
                "All Heap/Bucket/Quickselect pattern tests passed."
        );
    }
}
