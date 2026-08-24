package org.chijai.day7.session1.heap;

import java.util.*;

/**
 * LeetCode 347 - Top K Frequent Elements.
 *
 * <h2>Problem</h2>
 * <p>Given an integer array {@code nums} and an integer {@code k}, return the
 * {@code k} values that occur most frequently. The answer order does not
 * matter. LeetCode guarantees that the answer set is unique.</p>
 *
 * <h2>Examples</h2>
 * <pre>
 * nums = [1,1,1,2,2,3], k = 2  -> [1,2]
 * nums = [1],             k = 1  -> [1]
 * nums = [4,4,-1,-1,-1,2], k = 2 -> [-1,4]
 * </pre>
 *
 * <h2>Derivation from first principles</h2>
 * <ol>
 *   <li>We cannot rank values until we know how often each value occurs.</li>
 *   <li>A frequency map converts the input into {@code value -> count}.</li>
 *   <li>We need only the best {@code k} entries, not a complete ordering.</li>
 *   <li>Keep a min-heap of at most {@code k} entries. Its root is the weakest
 *       candidate currently accepted.</li>
 *   <li>After adding a candidate, remove the root when the heap exceeds
 *       {@code k}. The heap therefore retains the strongest {@code k} entries.</li>
 * </ol>
 *
 * <h2>Invariant</h2>
 * <p>After processing any number of distinct values, the heap contains the
 * {@code min(k, processedDistinctValues)} highest-frequency entries among
 * those processed values. The root is the easiest retained entry to replace.</p>
 *
 * <h2>Implementation traps</h2>
 * <ul>
 *   <li>Count every value before selecting the top {@code k}.</li>
 *   <li>Use a min-heap, because the weakest retained candidate must leave.</li>
 *   <li>Limit the heap to {@code k}; otherwise this becomes full sorting.</li>
 *   <li>Do not promise output order—the problem allows any order.</li>
 *   <li>Compare frequencies safely with {@link Integer#compare(int, int)}.</li>
 * </ul>
 *
 * <h2>Interview defense</h2>
 * <p>Let {@code u} be the number of distinct values. Counting costs O(n).
 * Every distinct value performs one O(log k) heap insertion and possibly one
 * O(log k) removal, giving O(n + u log k) time and O(u + k) space.</p>
 */
public class TopKFrequentElements {

    /** LeetCode-compatible entry point using the preferred size-k min-heap. */
    public int[] topKFrequent(int[] nums, int k) {
        return new MinHeapSolution().topKFrequent(nums, k);
    }

    /** Baseline: count and sort every distinct value by descending frequency. */
    static class SortingBaseline {

        public int[] topKFrequent(int[] nums, int k) {
            Map<Integer, Integer> frequencies = countFrequencies(nums);
            List<Integer> values = new ArrayList<>(frequencies.keySet());
            values.sort(
                    Comparator.comparingInt(frequencies::get)
                            .reversed()
            );
            int[] result = new int[k];
            for (int index = 0; index < k; index++) {
                result[index] = values.get(index);
            }
            return result;
        }
    }

    /** Interview-preferred solution: retain only the current best k entries. */
    static class MinHeapSolution {

        public int[] topKFrequent(int[] nums, int k) {
            Map<Integer, Integer> frequencies = countFrequencies(nums);
            PriorityQueue<Integer> topK =
                    new PriorityQueue<>(Comparator.comparingInt(frequencies::get));
            for (int value : frequencies.keySet()) {
                topK.offer(value);
                if (topK.size() > k) {
                    topK.poll();
                }
            }

            int[] result = new int[k];
            for (int index = k - 1; index >= 0; index--) {
                result[index] = topK.poll();
            }
            return result;
        }
    }

    private static Map<Integer, Integer> countFrequencies(int[] nums) {
        Map<Integer, Integer> frequencies = new HashMap<>();
        for (int value : nums) {
            frequencies.merge(value, 1, Integer::sum);
        }
        return frequencies;
    }
}
