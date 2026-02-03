package org.chijai.day5.session1;

// =====================================================================================
// NEXT GREATER ELEMENT II — INVARIANT-FIRST ALGORITHM CHAPTER
// =====================================================================================
//
// ⚠️ SINGLE FILE · INTELLIJ-READY · SELF-CONTAINED · NO EXTERNAL LIBRARIES
//
// =====================================================================================

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Stack;

public class NextGreaterElement {

    // =================================================================================
    // 2️⃣ 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
    // =================================================================================
    /*
     * 🔗 Link: https://leetcode.com/problems/next-greater-element-ii/
     * 🧩 Difficulty: Medium
     * 🏷️ Tags: Array, Stack, Monotonic Stack
     *
     * -------------------------------------------------------------------------
     * Given a circular integer array nums (i.e., the next element of nums[nums.length - 1]
     * is nums[0]), return the next greater number for every element in nums.
     *
     * The next greater number of a number x is the first greater number to its
     * traversing-order next in the array, which means you could search circularly
     * to find its next greater number. If it doesn't exist, return -1 for this number.
     *
     * -------------------------------------------------------------------------
     * Example 1:
     *
     * Input: nums = [1,2,1]
     * Output: [2,-1,2]
     *
     * Explanation:
     * The first 1's next greater number is 2;
     * The number 2 can't find next greater number.
     * The second 1's next greater number needs to search circularly, which is also 2.
     *
     * -------------------------------------------------------------------------
     * Example 2:
     *
     * Input: nums = [1,2,3,4,3]
     * Output: [2,3,4,-1,4]
     *
     * -------------------------------------------------------------------------
     * Constraints:
     *
     * 1 <= nums.length <= 10^4
     * -10^9 <= nums[i] <= 10^9
     */
    // =================================================================================


    // =================================================================================
    // 3️⃣ 🔵 CORE PATTERN OVERVIEW (INVARIANT-FIRST)
    // =================================================================================
    /*
     * 🔵 Pattern Name:
     * Monotonic Decreasing Stack (Next Greater Element Pattern)
     *
     * 🔵 Problem Archetype:
     * For each index, find the first future element (in traversal order)
     * that is strictly greater.
     *
     * 🟢 Core Invariant (MANDATORY):
     * The stack always stores indices of elements in strictly decreasing
     * value order, and none of them has yet found a valid next greater element.
     *
     * 🟡 Why this invariant works:
     * If a new element is greater than the stack top, it is the FIRST
     * greater element encountered — anything in between was smaller.
     *
     * 🔵 When this pattern applies:
     * - "Next greater / smaller"
     * - One-directional discovery
     * - Local future dominance
     *
     * 🧭 Pattern Recognition Signals:
     * - Circular or linear scan
     * - First greater to the right
     * - Brute force is O(n²)
     *
     * 🔵 Difference from similar patterns:
     * - Sliding window → window invariant, not dominance
     * - Two pointers → positional narrowing, not ordering
     */
    // =================================================================================


    // =================================================================================
    // 4️⃣ 🟢 MENTAL MODEL & INVARIANTS (CANONICAL)
    // =================================================================================
    /*
     * 🧠 Mental Model:
     * Walk forward while carrying unresolved elements behind you.
     * When a bigger number appears, it resolves all smaller waiting ones.
     *
     * 🟢 Invariants:
     * 1. Stack indices map to values in strictly decreasing order.
     * 2. Every index in the stack has NOT yet found its next greater element.
     * 3. Once popped, an index is permanently resolved.
     *
     * 🟢 State Representation:
     * - stack: unresolved indices
     * - answer[i]: next greater element for i or -1
     *
     * 🟢 Allowed Moves:
     * - Push current index if unresolved
     * - Pop while current value > stack top value
     *
     * 🔴 Forbidden Moves:
     * - Pushing without popping smaller values
     * - Resolving an index twice
     *
     * 🟡 Termination Logic:
     * Each index is pushed once and popped once → finite.
     *
     * 🔴 Why alternatives fail:
     * Brute force ignores dominance invariant → redundant scanning.
     */
    // =================================================================================


    // =================================================================================
    // 5️⃣ 🔴 WHY THE NAIVE / WRONG SOLUTION FAILS
    // =================================================================================
    /*
     * ❌ Naive Approach:
     * For each index, scan forward (circularly) until a greater element is found.
     *
     * Why it seems correct:
     * - Direct translation of the problem statement
     *
     * Why it fails:
     * - Violates dominance invariant
     * - Re-scans same elements repeatedly
     *
     * 🔴 Invariant Violation:
     * No memory of unresolved elements.
     *
     * 🧪 Minimal Counterexample:
     * nums = [5,4,3,2,1]
     * → Each element scans entire array → O(n²)
     *
     * 🎯 Interview Trap:
     * Circular array distracts candidates into modulo-heavy brute force.
     */
    // =================================================================================


    // =================================================================================
    // 6️⃣ PRIMARY PROBLEM — SOLUTION CLASSES
    // =================================================================================

    // -------------------------------------------------------------------------
    // 🔴 BRUTE FORCE
    // -------------------------------------------------------------------------
    static class BruteForce {
        /*
         * Core Idea:
         * For each index, scan forward up to n elements circularly.
         *
         * Invariant Enforced:
         * None globally.
         *
         * Time: O(n²)
         * Space: O(1)
         * Interview Preference: ❌
         */
        static int[] nextGreaterElements(int[] nums) {
            int n = nums.length;
            int[] answer = new int[n];

            for (int i = 0; i < n; i++) {
                answer[i] = -1;
                for (int step = 1; step < n; step++) {
                    int nextIndex = (i + step) % n;
                    if (nums[nextIndex] > nums[i]) {
                        answer[i] = nums[nextIndex];
                        break;
                    }
                }
            }
            return answer;
        }
    }

    // -------------------------------------------------------------------------
    // 🟡 IMPROVED (DOUBLE PASS, STILL STACK-LESS)
    // -------------------------------------------------------------------------
    static class Improved {
        /*
         * Core Idea:
         * Duplicate traversal range to simulate circularity.
         *
         * Invariant:
         * Partial ordering but no dominance memory.
         *
         * Time: O(n²)
         * Space: O(1)
         * Interview Preference: ❌
         */
        static int[] nextGreaterElements(int[] nums) {
            int n = nums.length;
            int[] answer = new int[n];

            for (int i = 0; i < n; i++) {
                answer[i] = -1;
                for (int j = i + 1; j < i + n; j++) {
                    if (nums[j % n] > nums[i]) {
                        answer[i] = nums[j % n];
                        break;
                    }
                }
            }
            return answer;
        }
    }

    // -------------------------------------------------------------------------
    // 🟢 OPTIMAL (MONOTONIC STACK · INTERVIEW-PREFERRED)
    // -------------------------------------------------------------------------
    static class Optimal {
        /*
         * Core Idea:
         * Use monotonic decreasing stack over 2n traversal.
         *
         * Enforced Invariant:
         * Stack holds unresolved indices in decreasing value order.
         *
         * Time: O(n)
         * Space: O(n)
         * Interview Preference: ✅
         */
        static int[] nextGreaterElements(int[] nums) {
            int n = nums.length;
            int[] result = new int[n];
            Arrays.fill(result, -1);

            Stack<Integer> stack = new Stack<>(); // stores indices

            // Traverse twice to simulate circular array
            for (int i = 0; i < 2 * n; i++) {

                int circularIndex = i % n;

                while (!stack.isEmpty()
                        && nums[circularIndex] > nums[stack.peek()]) {

                    int unresolvedIndex = stack.pop();
                    result[unresolvedIndex] = nums[circularIndex];
                }

                stack.push(circularIndex);
            }

            return result;
        }
    }

    // =================================================================================
    // 7️⃣ 🟣 INTERVIEW ARTICULATION (INVARIANT-LED)
    // =================================================================================
    /*
     * I maintain a stack of indices whose next greater element is unknown.
     * The stack is strictly decreasing in value.
     *
     * When I see a larger number, it resolves all smaller waiting elements.
     * Circularity is handled by scanning twice.
     *
     * Correctness:
     * First greater encountered is guaranteed by monotonicity.
     *
     * If logic changes:
     * - Removing monotonicity → wrong resolutions
     *
     * In-place: ❌ (needs stack)
     * Streaming: ❌ (needs future knowledge)
     *
     * Not suitable when:
     * - You need k-th greater
     * - Bidirectional constraints exist
     */
    // =================================================================================


    // =================================================================================
    // 8️⃣ 🔄 VARIATIONS & TWEAKS
    // =================================================================================
    /*
     * 🟢 Invariant-preserving:
     * - Next smaller element → flip comparison
     *
     * 🟡 Reasoning-only:
     * - Use values instead of indices if no duplicates
     *
     * 🔴 Pattern-break:
     * - Distance to next greater → invariant insufficient alone
     */
    // =================================================================================


    // =================================================================================
    // 9️⃣ ⚫ REINFORCEMENT PROBLEMS (SAME INVARIANT)
    // =================================================================================

    // -------------------------------------------------------------------------
    // ⚫ Reinforcement 1: Next Greater Element I
    // -------------------------------------------------------------------------
    static class NextGreaterElementI {
        /*
         * Same invariant:
         * Monotonic decreasing stack.
         * boths nums1 and nums2 are non-circular and unique
         */
        static int[] nextGreaterElement(int[] nums1, int[] nums2) {
            java.util.Map<Integer, Integer> nextMap = new java.util.HashMap<>();
            java.util.Deque<Integer> stack = new java.util.ArrayDeque<>();

            for (int value : nums2) {
                while (!stack.isEmpty() && value > stack.peek()) {
                    nextMap.put(stack.pop(), value);
                }
                stack.push(value);
            }

            int[] answer = new int[nums1.length];
            for (int i = 0; i < nums1.length; i++) {
                answer[i] = nextMap.getOrDefault(nums1[i], -1);
            }
            return answer;
        }
    }

    // =================================================================================
    // 10️⃣ 🧩 RELATED PROBLEMS (MINI CHAPTER)
    // =================================================================================
    /*
     * Daily Temperatures:
     * Modified invariant (distance tracking)
     *
     * Trapping Rain Water:
     * Invariant insufficient → needs two-sided boundary
     */
    // =================================================================================


    // =================================================================================
    // 11️⃣ 🟢 LEARNING VERIFICATION
    // =================================================================================
    /*
     * Must recall:
     * - Stack is decreasing
     * - Pop resolves answer
     *
     * Debug skills:
     * - Missing second pass
     * - Wrong push condition
     *
     * Detection:
     * "first greater to the right"
     */
    // =================================================================================


    // =================================================================================
    // 12️⃣ 🧪 main() METHOD + SELF-VERIFYING TESTS
    // =================================================================================
    public static void main(String[] args) {
        test(new int[]{1, 2, 1}, new int[]{2, -1, 2});
        test(new int[]{1, 2, 3, 4, 3}, new int[]{2, 3, 4, -1, 4});
        test(new int[]{5, 4, 3, 2, 1}, new int[]{-1,5,5,5,5});
        test(new int[]{2, 2, 2}, new int[]{-1, -1, -1});
    }

    private static void test(int[] input, int[] expected) {
        int[] actual = Optimal.nextGreaterElements(input);
        if (!java.util.Arrays.equals(actual, expected)) {
            throw new AssertionError(
                    "Expected " + java.util.Arrays.toString(expected)
                            + " but got " + java.util.Arrays.toString(actual));
        }
    }

    // =================================================================================
    // 13️⃣ 🧠 CHAPTER COMPLETION CHECKLIST (WITH ANSWERS)
    // =================================================================================
    /*
     * Invariant clarity → Stack holds unresolved decreasing values
     * Search target clarity → First greater to the right (circular)
     * Discard logic → Pop smaller values
     * Termination guarantee → Each index pushed/popped once
     * Failure awareness → Brute force O(n²)
     * Edge-case confidence → All decreasing, duplicates
     * Variant readiness → Next smaller, non-circular
     * Pattern boundary → Multi-directional constraints
     */

    // 🧘 FINAL CLOSURE STATEMENT
    /*
     * For this problem, the invariant is a decreasing stack of unresolved indices.
     * The answer represents the first future greater value.
     * The search terminates because each index is resolved once.
     * I can re-derive this solution under pressure.
     * This chapter is complete.
     */
}
