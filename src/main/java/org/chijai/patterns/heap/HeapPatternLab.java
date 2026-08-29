package org.chijai.patterns.heap;

import org.chijai.patterns.PatternChapter;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public final class HeapPatternLab {
    private HeapPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Heap / Priority Queue",
                "Top K / Next Best",
                "Bounded Frontier",
                "Root Is Current Answer",
                "Top K Frequent Elements"
        );
    }

    public static int kthLargest(int[] nums, int k) {
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        for (int num : nums) {
            heap.add(num);
            if (heap.size() > k) {
                heap.remove();
            }
        }
        return heap.peek();
    }

    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> frequency = new HashMap<>();
        for (int num : nums) {
            frequency.merge(num, 1, Integer::sum);
        }
        PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> frequency.get(a) - frequency.get(b));
        for (int num : frequency.keySet()) {
            heap.add(num);
            if (heap.size() > k) {
                heap.remove();
            }
        }
        int[] result = new int[heap.size()];
        for (int i = result.length - 1; i >= 0; i--) {
            result[i] = heap.remove();
        }
        return result;
    }
}
