package org.chijai.day7.session1;
import java.util.*;

/*
================================================================================
📘 PRIMARY PROBLEM — FULL OFFICIAL STATEMENT
================================================================================

🔗 Link:
https://www.geeksforgeeks.org/heap-sort/

Difficulty: Medium
Tags: Heap, Sorting, Array, Divide and Conquer

--------------------------------------------------------------------------------
PROBLEM DESCRIPTION:

Heap Sort is a comparison-based sorting technique based on a Binary Heap data structure.

It is similar to selection sort where we first find the maximum element and place the
maximum element at the end. We repeat the same process for the remaining elements.

--------------------------------------------------------------------------------
HOW HEAP SORT WORKS:

1. Build a Max Heap from the input data.
2. At this point, the largest element is stored at the root of the heap.
3. Replace it with the last element of the heap followed by reducing the size of heap by 1.
4. Heapify the root of the tree.
5. Repeat steps 2–4 while heap size is greater than 1.

--------------------------------------------------------------------------------
INPUT:
An array of integers

OUTPUT:
Sorted array in ascending order

--------------------------------------------------------------------------------
EXAMPLE 1:

Input:
arr = [4, 10, 3, 5, 1]

Output:
[1, 3, 4, 5, 10]

Explanation:
Heap is built → largest extracted repeatedly

--------------------------------------------------------------------------------
EXAMPLE 2:

Input:
arr = [12, 11, 13, 5, 6, 7]

Output:
[5, 6, 7, 11, 12, 13]

--------------------------------------------------------------------------------
CONSTRAINTS:

1 ≤ N ≤ 10^5
-10^9 ≤ arr[i] ≤ 10^9

--------------------------------------------------------------------------------
NOTES:

• Heap Sort is in-place
• Not stable
• Time Complexity: O(N log N)
• Space Complexity: O(1)

================================================================================
🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
================================================================================

🔵 Pattern Name:
Heap-based Selection (Priority-driven ordering)

🔵 Problem Archetype:
Repeatedly selecting the extreme (max/min) efficiently

🟢 Core Invariant (MANDATORY):
At any step, the root of the heap is the maximum element among all elements in the current heap.

🟡 Why this invariant works:
Because the heap structure enforces partial order:
parent ≥ children → guarantees global maximum at root

🔵 When this pattern applies:
• Need repeated max/min extraction
• Cannot afford O(N^2)
• Need in-place sorting

🔵 Pattern recognition signals:
• “Find largest repeatedly”
• “Sort with priority”
• “Binary tree structure”
• “In-place without extra space”

🔵 Difference from similar patterns:

| Pattern        | Difference |
|---------------|-----------|
| Quick Sort    | Partition-based, not invariant root max |
| Merge Sort    | Divide & merge, requires extra space |
| Selection Sort| O(N^2), no structural invariant |

================================================================================
🟢 MENTAL MODEL & INVARIANTS (CANONICAL SOURCE OF TRUTH)
================================================================================

🔵 Mental Model:

Think of the array as a COMPLETE BINARY TREE.

We enforce:
Every parent ≥ its children

Thus:
Root always holds MAX element

Then:
We REMOVE root → place at end → shrink heap

Repeat.

--------------------------------------------------------------------------------
🟢 ALL INVARIANTS:

1. Heap Property:
   arr[parent] ≥ arr[left_child], arr[right_child]

2. Max Element Invariant:
   Root always holds the maximum element in current heap

3. Shrinking Boundary:
   Elements after index 'heapSize' are already sorted

4. Subtree Validity:
   After heapify, subtree rooted at index satisfies heap property

--------------------------------------------------------------------------------
🟢 State Meaning:

arr[]       → underlying heap
n           → total size
heapSize    → active heap boundary
i           → current node index

--------------------------------------------------------------------------------
🟢 Allowed Moves:

• Swap root with last element
• Heapify (push down)
• Build heap bottom-up

--------------------------------------------------------------------------------
🔴 Forbidden Moves:

• Breaking heap property
• Ignoring subtree violations
• Heapifying top-down initially (inefficient)

--------------------------------------------------------------------------------
🟢 Termination Logic:

When heapSize = 1 → array is sorted

--------------------------------------------------------------------------------
🔴 Why common alternatives fail:

• Sorting each time → O(N^2)
• Not heapifying properly → breaks invariant
• Using min-heap incorrectly → reversed order issues

================================================================================
🔴 WHY THE NAIVE / WRONG SOLUTION FAILS (FORENSIC ANALYSIS)
================================================================================

🔴 WRONG APPROACH 1:
Repeatedly scan array for max

Why it seems correct:
You are selecting max each time

Why it fails:
O(N^2)

Invariant violated:
No structure preserving max efficiently

Counterexample:
Large N = 10^5 → TLE

--------------------------------------------------------------------------------
🔴 WRONG APPROACH 2:
Partial heap without heapify

Why it seems correct:
You “roughly” maintain order

Why it fails:
Heap property breaks → incorrect sorting

Invariant violated:
Parent ≥ children

Counterexample:
[3, 1, 2] → incorrect swaps

--------------------------------------------------------------------------------
🔴 WRONG APPROACH 3:
Heapify only root once

Why it seems correct:
Root is max initially

Why it fails:
After swap, subtree becomes invalid

Invariant violated:
Subtree validity

--------------------------------------------------------------------------------
🟣 Interview Trap:

“Why not just sort after building heap?”

Because:
Sorting already costs O(N log N) — defeats purpose

================================================================================
PRIMARY PROBLEM — SOLUTION CLASSES
================================================================================
*/

public class HeapSort {

    /*
    ----------------------------------------------------------------------------
    1️⃣ BRUTE FORCE (Selection Sort Equivalent)
    ----------------------------------------------------------------------------
    */

    static class BruteForce {
        /*
        🔵 Core Idea:
        Repeatedly find max by scanning entire array

        🟢 Invariant:
        Last k elements are sorted

        🔴 Limitation:
        O(N^2)

        Time: O(N^2)
        Space: O(1)
        Interview: ❌ Not preferred
        */

        public void sort(int[] arr) {
            int n = arr.length;

            for (int i = n - 1; i > 0; i--) {
                int maxIdx = 0;

                for (int j = 1; j <= i; j++) {
                    if (arr[j] > arr[maxIdx]) {
                        maxIdx = j;
                    }
                }

                swap(arr, maxIdx, i);
            }
        }

        private void swap(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    /*
    ----------------------------------------------------------------------------
    2️⃣ IMPROVED (Using Priority Queue)
    ----------------------------------------------------------------------------
    */

    static class Improved {
        /*
        🔵 Core Idea:
        Use Max Heap (PriorityQueue)

        🟢 Invariant:
        PQ always gives max element

        🟡 Fixes:
        Avoids repeated scanning

        🔴 Limitation:
        Extra space

        Time: O(N log N)
        Space: O(N)
        Interview: ⚠️ Acceptable but not optimal
        */

        public void sort(int[] arr) {
            PriorityQueue<Integer> maxHeap =
                    new PriorityQueue<>(Collections.reverseOrder());

            for (int num : arr) {
                maxHeap.offer(num);
            }

            int i = 0;
            while (!maxHeap.isEmpty()) {
                arr[i++] = maxHeap.poll();
            }
        }
    }

    /*
    ----------------------------------------------------------------------------
    3️⃣ OPTIMAL (Heap Sort — IN-PLACE)
    ----------------------------------------------------------------------------
    */

    static class Optimal {
        /*
        🔵 Core Idea:
        Build max heap → extract max repeatedly

        🟢 Enforced Invariant:
        Root is max element in heap

        🟡 Fixes:
        Removes extra space

        Time: O(N log N)
        Space: O(1)
        Interview: ✅ Preferred
        */

        public void sort(int[] arr) {
            int n = arr.length;

            // 🔵 STEP 1: Build Max Heap
            for (int i = n / 2 - 1; i >= 0; i--) {
                heapify(arr, n, i);
            }

            // 🔵 STEP 2: Extract elements
            for (int i = n - 1; i > 0; i--) {

                // Move current root to end
                swap(arr, 0, i);

                // Restore heap property
                heapify(arr, i, 0);
            }
        }

        /*
        🟢 Heapify ensures:
        Subtree rooted at i satisfies heap property
        */
        private void heapify(int[] arr, int n, int i) {
            int largest = i;

            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < n && arr[left] > arr[largest]) {
                largest = left;
            }

            if (right < n && arr[right] > arr[largest]) {
                largest = right;
            }

            if (largest != i) {
                swap(arr, i, largest);

                // Recursively heapify affected subtree
                heapify(arr, n, largest);
            }
        }

        private void swap(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

        /*
    ================================================================================
    🟣 INTERVIEW ARTICULATION (INVARIANT-LED · NO CODE)
    ================================================================================
    */

    /*
    🟣 How to explain in interview:

    1. 🟢 State the invariant:
       "I maintain a max-heap such that the root always contains
        the maximum element of the current unsorted portion."

    2. 🟡 Discard logic:
       "I swap the root (max element) with the last element,
        effectively placing it in its final sorted position.
        Then I reduce heap size."

    3. 🟢 Why correctness is guaranteed:
       Because:
       • Heap invariant ensures root is always max
       • Each extraction places correct element at correct index
       • Remaining structure is restored via heapify

    4. 🔴 What breaks if changed:
       • If heap property is violated → wrong max extraction
       • If heapify skipped → subtree becomes invalid
       • If we don't shrink heap → infinite loop / incorrect order

    5. 🟡 In-place feasibility:
       Yes, uses constant extra space

    6. 🟡 Streaming feasibility:
       No, because we need full structure to maintain heap

    7. 🔴 When NOT to use:
       • When stability is required
       • When cache locality matters (QuickSort often faster in practice)

    --------------------------------------------------------------------------------
    🔥 One-line articulation:
    "Build a max heap, repeatedly extract the maximum to the end,
     and restore heap property — guaranteeing sorted order."
    */

    /*
    ================================================================================
    🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
    ================================================================================
    */

    /*
    🔵 Invariant-Preserving Changes:

    1. Min Heap Version:
       • Root = minimum
       • Produces descending order

    2. K Largest Elements:
       • Maintain heap of size K
       • Extract when size exceeds K

    3. Partial Sorting:
       • Stop after K extractions

    --------------------------------------------------------------------------------
    🟡 Reasoning-only changes:

    • Switching comparison sign flips sorting order
    • Changing heap type changes extraction behavior

    --------------------------------------------------------------------------------
    🔴 Pattern-break signals:

    • Need stable sorting → HeapSort fails
    • Need linear time → use Counting Sort / Radix Sort
    • Data not comparable → heap invalid

    */

    /*
    ================================================================================
    ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS)
    ================================================================================
    */

    /*
    ----------------------------------------------------------------------------
    PROBLEM 1: Kth Largest Element in an Array
    ----------------------------------------------------------------------------

    🔗 https://leetcode.com/problems/kth-largest-element-in-an-array/
    Difficulty: Medium

    PROBLEM STATEMENT:

    Given an integer array nums and an integer k, return the kth largest element.

    Example:
    Input: nums = [3,2,1,5,6,4], k = 2
    Output: 5

    ----------------------------------------------------------------------------
    🟢 Invariant Mapping:

    Maintain a MIN HEAP of size k such that:
    The root is the kth largest element.

    ----------------------------------------------------------------------------
    🟡 Reasoning:

    • Keep largest k elements
    • Smallest among them = answer

    ----------------------------------------------------------------------------
    ✅ Java Solution:
    */

    static class KthLargest {
        public int findKthLargest(int[] nums, int k) {
            PriorityQueue<Integer> minHeap = new PriorityQueue<>();

            for (int num : nums) {
                minHeap.offer(num);

                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }

            return minHeap.peek();
        }
    }

    /*
    ----------------------------------------------------------------------------
    🔴 Edge Cases:

    • k = 1 → max element
    • k = n → min element

    ----------------------------------------------------------------------------
    🟣 Interview Articulation:

    "Maintain a min heap of size k so that the smallest element in the heap
     represents the kth largest in the full array."
    */

    /*
    ----------------------------------------------------------------------------
    PROBLEM 2: Top K Frequent Elements
    ----------------------------------------------------------------------------

    🔗 https://leetcode.com/problems/top-k-frequent-elements/
    Difficulty: Medium

    PROBLEM STATEMENT:

    Given an integer array nums and an integer k, return the k most frequent elements.

    Example:
    Input: nums = [1,1,1,2,2,3], k = 2
    Output: [1,2]

    ----------------------------------------------------------------------------
    🟢 Invariant Mapping:

    Heap stores k elements with highest frequency

    ----------------------------------------------------------------------------
    ✅ Java Solution:
    */

    static class TopKFrequent {
        public int[] topKFrequent(int[] nums, int k) {
            Map<Integer, Integer> freq = new HashMap<>();

            for (int num : nums) {
                freq.put(num, freq.getOrDefault(num, 0) + 1);
            }

            PriorityQueue<int[]> minHeap =
                    new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

            for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
                minHeap.offer(new int[]{entry.getKey(), entry.getValue()});

                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }

            int[] result = new int[k];
            int i = 0;

            while (!minHeap.isEmpty()) {
                result[i++] = minHeap.poll()[0];
            }

            return result;
        }
    }

    /*
    ----------------------------------------------------------------------------
    🔴 Edge Cases:

    • All elements same
    • k = number of unique elements

    ----------------------------------------------------------------------------
    🟣 Interview Articulation:

    "Use a min heap of size k to track top frequencies efficiently."
    */

    /*
    ----------------------------------------------------------------------------
    PROBLEM 3: Merge K Sorted Arrays
    ----------------------------------------------------------------------------

    🔗 https://www.geeksforgeeks.org/merge-k-sorted-arrays/
    Difficulty: Medium

    PROBLEM STATEMENT:

    Merge k sorted arrays into a single sorted array.

    ----------------------------------------------------------------------------
    🟢 Invariant Mapping:

    Min heap always gives next smallest element

    ----------------------------------------------------------------------------
    ✅ Java Solution:
    */

    static class MergeKSortedArrays {
        static class Node {
            int value, arrIdx, elemIdx;

            Node(int v, int a, int e) {
                value = v;
                arrIdx = a;
                elemIdx = e;
            }
        }

        public List<Integer> merge(int[][] arrays) {
            PriorityQueue<Node> minHeap =
                    new PriorityQueue<>(Comparator.comparingInt(a -> a.value));

            List<Integer> result = new ArrayList<>();

            for (int i = 0; i < arrays.length; i++) {
                if (arrays[i].length > 0) {
                    minHeap.offer(new Node(arrays[i][0], i, 0));
                }
            }

            while (!minHeap.isEmpty()) {
                Node node = minHeap.poll();
                result.add(node.value);

                if (node.elemIdx + 1 < arrays[node.arrIdx].length) {
                    minHeap.offer(new Node(
                            arrays[node.arrIdx][node.elemIdx + 1],
                            node.arrIdx,
                            node.elemIdx + 1
                    ));
                }
            }

            return result;
        }
    }

    /*
    ----------------------------------------------------------------------------
    🔴 Edge Cases:

    • Empty arrays
    • Single array

    ----------------------------------------------------------------------------
    🟣 Interview Articulation:

    "Use a min heap to always pick the smallest next element across arrays."
    */

    /*
    ================================================================================
    🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
    ================================================================================
    */

    /*
    ----------------------------------------------------------------------------
    PROBLEM 1: Sort Characters By Frequency
    ----------------------------------------------------------------------------

    🔗 https://leetcode.com/problems/sort-characters-by-frequency/
    Difficulty: Medium

    PROBLEM STATEMENT:

    Given a string s, sort it in decreasing order based on frequency of characters.

    Example:
    Input: "tree"
    Output: "eert"

    ----------------------------------------------------------------------------
    🟢 Invariant:

    Max heap ensures most frequent character is always extracted first

    ----------------------------------------------------------------------------
    ✅ Java Solution:
    */

    static class SortByFrequency {
        public String frequencySort(String s) {
            Map<Character, Integer> freq = new HashMap<>();

            for (char c : s.toCharArray()) {
                freq.put(c, freq.getOrDefault(c, 0) + 1);
            }

            PriorityQueue<Character> maxHeap =
                    new PriorityQueue<>((a, b) -> freq.get(b) - freq.get(a));

            maxHeap.addAll(freq.keySet());

            StringBuilder sb = new StringBuilder();

            while (!maxHeap.isEmpty()) {
                char c = maxHeap.poll();
                int count = freq.get(c);

                for (int i = 0; i < count; i++) {
                    sb.append(c);
                }
            }

            return sb.toString();
        }
    }

    /*
    ----------------------------------------------------------------------------
    🔴 Edge Cases:

    • Single character
    • All same frequency

    ----------------------------------------------------------------------------
    🟣 Interview Articulation:

    "Use max heap to always extract highest frequency character."
    */

    /*
    ----------------------------------------------------------------------------
    PROBLEM 2: Find Median from Data Stream
    ----------------------------------------------------------------------------

    🔗 https://leetcode.com/problems/find-median-from-data-stream/
    Difficulty: Hard

    PROBLEM STATEMENT:

    Continuously add numbers and find median.

    ----------------------------------------------------------------------------
    🟢 Invariant:

    Two heaps:
    • MaxHeap (left half)
    • MinHeap (right half)

    Balance ensures median extraction

    ----------------------------------------------------------------------------
    ✅ Java Solution:
    */

    static class MedianFinder {
        PriorityQueue<Integer> maxHeap; // left
        PriorityQueue<Integer> minHeap; // right

        public MedianFinder() {
            maxHeap = new PriorityQueue<>(Collections.reverseOrder());
            minHeap = new PriorityQueue<>();
        }

        public void addNum(int num) {
            maxHeap.offer(num);
            minHeap.offer(maxHeap.poll());

            if (minHeap.size() > maxHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
        }

        public double findMedian() {
            if (maxHeap.size() > minHeap.size()) {
                return maxHeap.peek();
            }
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        }
    }

    /*
    ----------------------------------------------------------------------------
    🔴 Edge Cases:

    • Even number of elements
    • Single element

    ----------------------------------------------------------------------------
    🟣 Interview Articulation:

    "Maintain two balanced heaps so median is always at top(s)."
    */

    /*
    ================================================================================
    🟢 LEARNING VERIFICATION
    ================================================================================
    */

    /*
    🟢 Can you recall without code?

    1. What is the invariant?
       → Root is always max in heap

    2. Why naive fails?
       → O(N^2), no structure

    3. Debugging readiness:
       → Check heapify correctness
       → Check boundaries

    4. Pattern signals:
       → Repeated max/min extraction

    */

    /*
    ================================================================================
    🧪 main() METHOD + SELF-VERIFYING TESTS
    ================================================================================
    */

    public static void main(String[] args) {

        Optimal optimal = new Optimal();

        // ✅ Test 1: Basic case
        int[] arr1 = {4, 10, 3, 5, 1};
        optimal.sort(arr1);
        assert isSorted(arr1) : "Test 1 Failed";

        // ✅ Test 2: Already sorted
        int[] arr2 = {1, 2, 3, 4, 5};
        optimal.sort(arr2);
        assert isSorted(arr2) : "Test 2 Failed";

        // ✅ Test 3: Reverse sorted
        int[] arr3 = {5, 4, 3, 2, 1};
        optimal.sort(arr3);
        assert isSorted(arr3) : "Test 3 Failed";

        // ✅ Test 4: Single element
        int[] arr4 = {42};
        optimal.sort(arr4);
        assert isSorted(arr4) : "Test 4 Failed";

        // ✅ Test 5: Duplicates
        int[] arr5 = {2, 3, 2, 1, 1};
        optimal.sort(arr5);
        assert isSorted(arr5) : "Test 5 Failed";

        System.out.println("All tests passed ✅");
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) return false;
        }
        return true;
    }

    /*
    ================================================================================
    ✅ COMPLETION CHECKLIST
    ================================================================================
    */

    /*
    • Invariant:
      Root is max element of heap

    • Search target:
      Maximum element

    • Discard rule:
      Swap root with last → shrink heap

    • Termination:
      Heap size becomes 1

    • Naive failure:
      O(N^2) scanning

    • Edge cases:
      Single element, duplicates

    • Variant readiness:
      Kth largest, median, frequency problems

    • Pattern boundary:
      Not stable, not for streaming

    */

    /*
    🧘 FINAL CLOSURE STATEMENT

    I understand the invariant.
    I can re-derive the solution.
    This chapter is complete.
    */
}