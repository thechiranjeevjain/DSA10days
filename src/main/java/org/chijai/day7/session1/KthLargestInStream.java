package org.chijai.day7.session1;


import java.util.*;

/*
===============================================================
1️⃣ TOP-LEVEL PUBLIC CLASS DECLARATION
===============================================================
*/

public class KthLargestInStream {

/*
===============================================================
2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
===============================================================

🔗 Link:
https://leetcode.com/problems/kth-largest-element-in-a-stream/

Difficulty: Easy

Tags:
Heap (Priority Queue), Design, Data Stream

---------------------------------------------------------------
Problem Description:

You are part of a university admissions office and need to keep
track of the kth highest test score from applicants in real-time.
This helps to determine cut-off marks for interviews and admissions
dynamically as new applicants submit their scores.

You are tasked to implement a class which, for a given integer k,
maintains a stream of test scores and continuously returns the kth
highest test score after a new score has been submitted.

More specifically, we are looking for the kth highest score in the
sorted list of all scores.

---------------------------------------------------------------
Implement the KthLargest class:

KthLargest(int k, int[] nums)
- Initializes the object with the integer k and the stream of test
  scores nums.

int add(int val)
- Adds a new test score val to the stream and returns the element
  representing the kth largest element in the pool of test scores
  so far.

---------------------------------------------------------------
Example 1:

Input:
["KthLargest", "add", "add", "add", "add", "add"]
[[3, [4, 5, 8, 2]], [3], [5], [10], [9], [4]]

Output:
[null, 4, 5, 5, 8, 8]

Explanation:
KthLargest kthLargest = new KthLargest(3, [4, 5, 8, 2]);
kthLargest.add(3);   // return 4
kthLargest.add(5);   // return 5
kthLargest.add(10);  // return 5
kthLargest.add(9);   // return 8
kthLargest.add(4);   // return 8

---------------------------------------------------------------
Example 2:

Input:
["KthLargest", "add", "add", "add", "add"]
[[4, [7, 7, 7, 7, 8, 3]], [2], [10], [9], [9]]

Output:
[null, 7, 7, 7, 8]

Explanation:
KthLargest kthLargest = new KthLargest(4, [7, 7, 7, 7, 8, 3]);
kthLargest.add(2);   // return 7
kthLargest.add(10);  // return 7
kthLargest.add(9);   // return 7
kthLargest.add(9);   // return 8

---------------------------------------------------------------
Constraints:

0 <= nums.length <= 10^4
1 <= k <= nums.length + 1
-10^4 <= nums[i] <= 10^4
-10^4 <= val <= 10^4
At most 10^4 calls will be made to add.

*/

/*
===============================================================
3️⃣ 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
===============================================================

🔵 Pattern Name:
Min Heap of Size K (Top-K Streaming)

🔵 Problem Archetype:
"Maintain kth largest element in a dynamic stream"

🟢 Core Invariant:
The heap ALWAYS contains the K largest elements seen so far,
and the root is the Kth largest.

🟡 Why this invariant works:
If we keep only K largest elements:
- Anything smaller than current kth largest is irrelevant
- The smallest among these K is exactly the kth largest

🟡 When this pattern applies:
- Streaming data
- Top-K problems
- Continuous updates
- Cannot sort repeatedly

🟡 Pattern Recognition Signals:
- "kth largest/smallest"
- "stream"
- "real-time updates"
- "after each insertion"

🔴 Difference from similar patterns:

| Pattern             | Difference |
|--------------------|------------|
| Sorting            | O(n log n) per update (too slow) |
| Max Heap           | Keeps all elements → unnecessary |
| QuickSelect        | Not suitable for streaming |
| Balanced BST       | Works but heavier than heap |

⚫ Pattern Mapping:
Keep only what matters → discard rest → maintain invariant
*/

/*
===============================================================
4️⃣ 🟢 MENTAL MODEL & INVARIANTS
===============================================================

🟢 Mental Model:
Think like a strict admissions filter:
- You only care about TOP K candidates
- Anyone below current cutoff → discard immediately

🟢 Invariants:

1. Heap size ≤ K at all times
2. Heap contains K largest elements seen so far
3. Root = smallest among those K = kth largest overall

🟢 State Meaning:

heap → stores top K elements
heap.peek() → kth largest element

🟢 Allowed Moves:
- Add element
- Remove smallest if size > K

🟢 Forbidden Moves:
- Keeping all elements (wastes memory)
- Removing arbitrary elements

🟢 Termination:
At any moment, answer = heap.peek()

🟡 Why alternatives fail:
- Sorting each time → too slow
- Keeping all elements → unnecessary
- Max heap → wrong structure for kth tracking
*/

/*
===============================================================
5️⃣ 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS
===============================================================

🔴 Wrong Approach 1: Sort after each insertion

Why it seems correct:
- Sorting gives exact kth largest

Why it fails:
- O(n log n) per add
- Violates streaming efficiency

Counterexample:
10^4 operations → too slow

---------------------------------------------------------------

🔴 Wrong Approach 2: Store all elements in Max Heap

Why it seems correct:
- Largest always accessible

Why it fails:
- To find kth → need to pop k times → O(k log n)
- Repeated operations inefficient

---------------------------------------------------------------

🔴 Wrong Approach 3: Keep array and scan

Why it seems correct:
- Just find kth largest manually

Why it fails:
- O(n) per operation
- Inefficient at scale

---------------------------------------------------------------

🔴 Interview Trap:
"If you store all elements, you're not filtering signal from noise."

Correct mindset:
You must DISCARD irrelevant elements EARLY.
*/

/*
===============================================================
6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES
===============================================================
*/

    /*
    ---------------------------------------------------------------
    🧱 6.1 BRUTE FORCE SOLUTION
    ---------------------------------------------------------------
    */
    static class BruteForce {

        private List<Integer> list;
        private int k;

    /*
    🟡 Core Idea:
    Store all elements → sort every time

    🟢 Invariant:
    None efficiently maintained (recomputed each time)

    Time: O(n log n) per add
    Space: O(n)

    🔴 Not interview preferred
    */

        public BruteForce(int k, int[] nums) {
            this.k = k;
            list = new ArrayList<>();
            for (int num : nums) {
                list.add(num);
            }
        }

        public int add(int val) {
            list.add(val);

            // 🔴 Sorting every time → expensive
            Collections.sort(list);

            // kth largest = (n - k) index
            return list.get(list.size() - k);
        }
    }

    /*
    ---------------------------------------------------------------
    🧱 6.2 IMPROVED (PARTIAL HEAP BUT WRONG STRUCTURE)
    ---------------------------------------------------------------
    */
    static class Improved {

        private PriorityQueue<Integer> maxHeap;
        private int k;

    /*
    🟡 Core Idea:
    Use max heap but still store all elements

    🟢 Invariant:
    Largest always on top (NOT kth)

    Time: O(log n) insert + O(k log n) query
    Space: O(n)

    🔴 Still inefficient
    */

        public Improved(int k, int[] nums) {
            this.k = k;
            maxHeap = new PriorityQueue<>(Collections.reverseOrder());

            for (int num : nums) {
                maxHeap.offer(num);
            }
        }

        public int add(int val) {
            maxHeap.offer(val);

            // 🔴 Need to pop k times → inefficient
            PriorityQueue<Integer> temp = new PriorityQueue<>(maxHeap);

            int kth = -1;
            for (int i = 0; i < k; i++) {
                kth = temp.poll();
            }

            return kth;
        }
    }

    /*
    ---------------------------------------------------------------
    🧱 6.3 OPTIMAL (INTERVIEW-PREFERRED)
    ---------------------------------------------------------------
    */
    static class Optimal {

        private PriorityQueue<Integer> minHeap;
        private int k;

    /*
    🟡 Core Idea:
    Maintain a MIN HEAP of size K

    🟢 Invariant:
    Heap always contains top K elements
    Root = kth largest

    🟢 This is the KEY invariant

    Time: O(log k) per add
    Space: O(k)

    🟣 Interview Preferred
    */

        public Optimal(int k, int[] nums) {
            this.k = k;
            minHeap = new PriorityQueue<>();

            for (int num : nums) {
                add(num); // reuse logic
            }
        }

        public int add(int val) {

            // Step 1: Add element
            minHeap.offer(val);

            // Step 2: Maintain size K
            if (minHeap.size() > k) {
                minHeap.poll(); // remove smallest
            }

            // Step 3: Root is kth largest
            return minHeap.peek();
        }

    }
    /*
===============================================================
7️⃣ 🟣 INTERVIEW ARTICULATION (INVARIANT-LED · NO CODE)
===============================================================

🟣 State the invariant:
We maintain a min-heap of size K that always contains the K largest
elements seen so far. The root of the heap is the kth largest element.

🟣 Explain discard logic:
Whenever a new element comes:
- Add it to heap
- If heap size exceeds K → remove the smallest

This ensures:
All elements smaller than current kth largest are discarded.

🟣 Why correctness is guaranteed:
Because:
- Heap holds exactly K largest elements
- Smallest among them = kth largest globally

🟣 What breaks if changed:
If we:
- Don’t remove when size > K → heap grows → invariant breaks
- Use max heap → root becomes largest, not kth
- Remove random element → ordering breaks

🟣 In-place / streaming feasibility:
Fully streaming compatible
No need to store full dataset

🟣 When NOT to use this pattern:
- When full sorted order is required
- When K ≈ N (then sorting may be comparable)
- When data is static (QuickSelect is better)
*/

/*
===============================================================
8️⃣ 🔄 VARIATIONS & TWEAKS (INVARIANT-BASED)
===============================================================

🟡 Variation 1: Kth Smallest Element in Stream
- Use MAX heap of size K
- Invariant flips:
  Heap contains K smallest elements

---------------------------------------------------------------

🟡 Variation 2: Top K Frequent Elements
- Heap based on frequency
- Invariant:
  Keep K most frequent items

---------------------------------------------------------------

🟡 Variation 3: Median in Stream
- Two heaps:
  MaxHeap (left) + MinHeap (right)
- Invariant:
  Balanced halves

---------------------------------------------------------------

🟡 Pattern-Preserving Changes:
- Change comparator → changes meaning of "top"
- Change K → dynamic threshold

---------------------------------------------------------------

🔴 Pattern-Break Signals:
- Need full ordering → heap insufficient
- Need random access rank → use BST
- Need offline computation → QuickSelect better
*/


/*
===============================================================
9️⃣ ⚫ REINFORCEMENT PROBLEMS (FULL SUB-CHAPTERS)
===============================================================
*/

    /*
    ---------------------------------------------------------------
    ⚫ PROBLEM 1: Kth Largest Element in an Array
    ---------------------------------------------------------------

    🔗 https://leetcode.com/problems/kth-largest-element-in-an-array/

    Difficulty: Medium

    ---------------------------------------------------------------
    Problem Statement:

    Given an integer array nums and an integer k, return the kth largest
    element in the array.

    Note:
    It is the kth largest element in sorted order, not the kth distinct.

    ---------------------------------------------------------------
    Example:

    Input: nums = [3,2,1,5,6,4], k = 2
    Output: 5

    ---------------------------------------------------------------
    🟢 Invariant Mapping:

    Maintain min heap of size K:
    - Heap contains K largest elements
    - Root = kth largest

    ---------------------------------------------------------------
    🧠 Solution:
    */
    static class Reinforcement1 {

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
🟡 Edge Cases:
- k = 1 → max element
- k = n → min element
- duplicates allowed

🟣 Interview Articulation:
Same invariant as stream problem, but single pass.
*/

    /*
    ---------------------------------------------------------------
    ⚫ PROBLEM 2: Top K Frequent Elements
    ---------------------------------------------------------------

    🔗 https://leetcode.com/problems/top-k-frequent-elements/

    Difficulty: Medium

    ---------------------------------------------------------------
    Problem Statement:

    Given an integer array nums and an integer k, return the k most
    frequent elements.

    ---------------------------------------------------------------
    Example:

    Input: nums = [1,1,1,2,2,3], k = 2
    Output: [1,2]

    ---------------------------------------------------------------
    🟢 Invariant Mapping:

    Heap stores top K frequent elements based on frequency

    ---------------------------------------------------------------
    🧠 Solution:
    */
    static class Reinforcement2 {

        public int[] topKFrequent(int[] nums, int k) {

            Map<Integer, Integer> freqMap = new HashMap<>();

            for (int num : nums) {
                freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
            }

            PriorityQueue<int[]> minHeap = new PriorityQueue<>(
                    (a, b) -> a[1] - b[1]
            );

            for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
                minHeap.offer(new int[]{entry.getKey(), entry.getValue()});

                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }

            int[] result = new int[k];
            int i = 0;

            for (int[] pair : minHeap) {
                result[i++] = pair[0];
            }

            return result;
        }
    }

/*
🟡 Edge Cases:
- all elements same
- k = unique elements
- negative numbers

🟣 Interview Articulation:
We adapt invariant:
Keep top K by frequency instead of value.
*/

    /*
    ---------------------------------------------------------------
    ⚫ PROBLEM 3: K Closest Points to Origin
    ---------------------------------------------------------------

    🔗 https://leetcode.com/problems/k-closest-points-to-origin/

    Difficulty: Medium

    ---------------------------------------------------------------
    Problem Statement:

    Given an array of points where points[i] = [xi, yi], return the K
    closest points to the origin (0,0).

    ---------------------------------------------------------------
    Example:

    Input: points = [[1,3],[-2,2]], k = 1
    Output: [[-2,2]]

    ---------------------------------------------------------------
    🟢 Invariant Mapping:

    Keep K closest points using MAX heap (distance-based)

    ---------------------------------------------------------------
    🧠 Solution:
    */
    static class Reinforcement3 {

        public int[][] kClosest(int[][] points, int k) {

            PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
                    (a, b) -> distance(b) - distance(a)
            );

            for (int[] point : points) {
                maxHeap.offer(point);

                if (maxHeap.size() > k) {
                    maxHeap.poll();
                }
            }

            int[][] result = new int[k][2];
            int i = 0;

            for (int[] p : maxHeap) {
                result[i++] = p;
            }

            return result;
        }

        private int distance(int[] p) {
            return p[0] * p[0] + p[1] * p[1];
        }
    }

/*
🟡 Edge Cases:
- k = n
- negative coordinates
- duplicate distances

🟣 Interview Articulation:
Invert invariant:
Keep K closest → remove farthest
*/

/*
===============================================================
🔟 🧩 RELATED PROBLEMS (MINI INVARIANT CHAPTERS)
===============================================================
*/

/*
---------------------------------------------------------------
🧩 PROBLEM 1: Find Median from Data Stream
---------------------------------------------------------------

🔗 https://leetcode.com/problems/find-median-from-data-stream/

🟢 Invariant:

Two heaps:
- MaxHeap (left half)
- MinHeap (right half)

Balance:
|size(left) - size(right)| ≤ 1

Median:
- If equal → avg of tops
- Else → top of larger heap

---------------------------------------------------------------
🧠 Key Idea:
Split stream into two balanced halves
*/

/*
---------------------------------------------------------------
🧩 PROBLEM 2: Sliding Window Maximum
---------------------------------------------------------------

🔗 https://leetcode.com/problems/sliding-window-maximum/

🟢 Invariant:

Monotonic Deque:
- Decreasing order
- Front = max of window

---------------------------------------------------------------
🧠 Key Idea:
Maintain valid candidates only
*/

/*
---------------------------------------------------------------
🧩 PROBLEM 3: Merge K Sorted Lists
---------------------------------------------------------------

🔗 https://leetcode.com/problems/merge-k-sorted-lists/

🟢 Invariant:

Min Heap:
- Always extract smallest among K lists

---------------------------------------------------------------
🧠 Key Idea:
K-way merge using heap
*/

/*
===============================================================
1️⃣1️⃣ 🟢 LEARNING VERIFICATION
===============================================================

🟢 Invariant Recall:
Heap of size K → contains K largest elements → root = kth largest

---------------------------------------------------------------

🟢 Can you explain naive failure?
- Sorting → too slow
- Max heap → inefficient kth extraction
- Full storage → unnecessary

---------------------------------------------------------------

🟢 Debugging Readiness:
If answer wrong:
- Check heap size constraint
- Check removal condition
- Check comparator

---------------------------------------------------------------

🟢 Pattern Recognition Signals:
- kth largest/smallest
- streaming
- real-time updates
- top-K

---------------------------------------------------------------

🟢 If stuck, ask:
"What can I safely discard?"
*/

/*
===============================================================
1️⃣2️⃣ 🧪 MAIN METHOD + SELF-VERIFYING TESTS
===============================================================
*/

    public static void main(String[] args) {

        System.out.println("Running Kth Largest Element in Stream Tests...");

        // ----------------------------------------------------------
        // 🧪 Test 1: Basic Example
        // ----------------------------------------------------------
        Optimal kth1 = new Optimal(3, new int[]{4, 5, 8, 2});

        assert kth1.add(3) == 4 : "Test1 Failed";
        assert kth1.add(5) == 5 : "Test1 Failed";
        assert kth1.add(10) == 5 : "Test1 Failed";
        assert kth1.add(9) == 8 : "Test1 Failed";
        assert kth1.add(4) == 8 : "Test1 Failed";

        System.out.println("✅ Test 1 Passed");

        // ----------------------------------------------------------
        // 🧪 Test 2: Duplicates
        // ----------------------------------------------------------
        Optimal kth2 = new Optimal(4, new int[]{7, 7, 7, 7, 8, 3});

        assert kth2.add(2) == 7 : "Test2 Failed";
        assert kth2.add(10) == 7 : "Test2 Failed";
        assert kth2.add(9) == 7 : "Test2 Failed";
        assert kth2.add(9) == 8 : "Test2 Failed";

        System.out.println("✅ Test 2 Passed");

        // ----------------------------------------------------------
        // 🧪 Test 3: Edge Case (k = 1 → max element)
        // ----------------------------------------------------------
        Optimal kth3 = new Optimal(1, new int[]{});

        assert kth3.add(5) == 5 : "Test3 Failed";
        assert kth3.add(10) == 10 : "Test3 Failed";
        assert kth3.add(3) == 10 : "Test3 Failed";

        System.out.println("✅ Test 3 Passed");

        // ----------------------------------------------------------
        // 🧪 Test 4: Negative Values
        // ----------------------------------------------------------
        Optimal kth4 = new Optimal(2, new int[]{-1, -2, -3});

        assert kth4.add(-4) == -2 : "Test4 Failed";
        assert kth4.add(0) == -1 : "Test4 Failed";

        System.out.println("✅ Test 4 Passed");

        // ----------------------------------------------------------
        // 🧪 Test 5: Increasing Stream
        // ----------------------------------------------------------
        Optimal kth5 = new Optimal(3, new int[]{1, 2, 3});

        assert kth5.add(4) == 2 : "Test5 Failed";
        assert kth5.add(5) == 3 : "Test5 Failed";

        System.out.println("✅ Test 5 Passed");

        System.out.println("🎉 All Tests Passed Successfully!");
    }
}

/*
===============================================================
1️⃣3️⃣ ✅ COMPLETION CHECKLIST
===============================================================

🟢 Invariant:
Min heap of size K containing K largest elements

🟢 Search target:
kth largest element

🟢 Discard rule:
Remove smallest when size > K

🟢 Termination:
heap.peek()

🟢 Naive failure:
Sorting / full storage inefficient

🟢 Edge cases:
k=1, duplicates, negatives, empty initial array

🟢 Variant readiness:
Top K, closest K, frequency-based K

🟢 Pattern boundary:
Fails when full ordering required

---------------------------------------------------------------
🧘 FINAL CLOSURE STATEMENT

I understand the invariant.
I can re-derive the solution.
This chapter is complete.
*/


