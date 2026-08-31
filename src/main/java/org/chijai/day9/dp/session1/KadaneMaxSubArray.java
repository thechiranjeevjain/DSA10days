package org.chijai.day9.dp.session1;

/**
 * 53. Maximum Subarray — Kadane Pattern V5 (AUDITED)
 *
 * Primary classification:
 *
 * arrays/
 *   dynamicProgramming/
 *     kadane/
 *       KadaneMaxSubArray.java
 *
 * Core reusable idea:
 *
 *     "For every index, compute the best answer that MUST end here."
 *
 * ONE TEMPLATE TO OWN:
 *
 *     restart at current
 *     OR
 *     extend previous ending-here state
 *
 * Reusability motto:
 *
 *     Learn one rolling-state transition.
 *     Reuse it across related contiguous-optimization problems.
 */
public class KadaneMaxSubArray {

    /*
     * ============================================================
     * 📘 PROBLEM
     * ============================================================
     *
     * Given an integer array nums, return the maximum sum of a
     * non-empty contiguous subarray.
     *
     * Example:
     *
     * nums = [-2,1,-3,4,-1,2,1,-5,4]
     *
     * answer = 6
     *
     * best subarray:
     *
     * [4,-1,2,1]
     *
     * Assumption:
     *
     *     nums is non-empty, matching the standard problem constraint.
     */

    /*
     * ============================================================
     * 🧭 EXACT CLASSIFICATION
     * ============================================================
     *
     * PRIMARY:
     *
     *     Dynamic Programming
     *
     * SUBTYPE:
     *
     *     1D DP / State Compression
     *
     * COMMON NAME:
     *
     *     Kadane's Algorithm
     *
     * ARCHETYPE:
     *
     *     Best subarray ENDING HERE
     *
     * ------------------------------------------------------------
     * WHY IT IS DP
     * ------------------------------------------------------------
     *
     * State:
     *
     *     bestEndingHere(i)
     *
     * Transition:
     *
     *     max(
     *         nums[i],                        // restart
     *         bestEndingHere(i - 1) + nums[i] // extend
     *     )
     *
     * Only the previous state is needed.
     *
     * So:
     *
     * O(n) DP array
     *     ↓ compress
     * O(1) rolling variable
     */

    /*
     * ============================================================
     * 🧠 CORE MENTAL MODEL
     * ============================================================
     *
     * Walk left -> right.
     *
     * At every index ask:
     *
     *     "Is my past helping me or hurting me?"
     *
     * Any subarray that MUST end at index i has exactly two choices:
     *
     * 1. EXTEND
     *
     *     previousBestEndingHere + nums[i]
     *
     * 2. RESTART
     *
     *     nums[i]
     *
     * There is no third possibility.
     *
     * ------------------------------------------------------------
     * CORE TRANSITION
     * ------------------------------------------------------------
     *
     * currentBestEndingHere =
     *
     *     max(
     *         nums[i],
     *         currentBestEndingHere + nums[i]
     *     )
     *
     * ------------------------------------------------------------
     * TWO DIFFERENT STATES
     * ------------------------------------------------------------
     *
     * currentBestEndingHere:
     *
     *     best subarray sum that MUST end at current index
     *
     * globalBestSoFar:
     *
     *     best subarray sum found anywhere so far
     *
     * ------------------------------------------------------------
     * ONE-LINER
     * ------------------------------------------------------------
     *
     *     "Extend if the past helps; restart if the past hurts."
     */

    /*
     * ============================================================
     * 🟢 ALLOWED / 🔴 FORBIDDEN THINKING
     * ============================================================
     *
     * ALLOWED:
     *
     * ✓ Extend previous subarray.
     * ✓ Restart at current element.
     *
     * ------------------------------------------------------------
     * DO NOT THINK:
     * ------------------------------------------------------------
     *
     * ✗ "extend OR do nothing"
     *
     * If you think only:
     *
     *     extend previous
     *     OR
     *     keep old answer
     *
     * then you never allow the current index to start a brand-new
     * candidate subarray.
     *
     * The local choice is:
     *
     *     EXTEND OR RESTART.
     *
     * ------------------------------------------------------------
     *
     * ✗ Do not blindly reset current sum to zero.
     *
     * Why?
     *
     * nums = [-5,-2,-8]
     *
     * Correct answer:
     *
     *     -2
     *
     * Reset-to-zero logic may incorrectly suggest:
     *
     *     0
     *
     * but the problem requires a NON-EMPTY subarray.
     *
     * ------------------------------------------------------------
     *
     * Negative history hurts the future.
     *
     * Zero history does not hurt.
     *
     * currentBestEndingHere means:
     *
     *     "the best subarray that MUST end at i."
     */

    /*
     * ============================================================
     * 📈 APPROACH PROGRESSION
     * ============================================================
     *
     * 1. BRUTE FORCE
     *
     * Enumerate every subarray.
     *
     * For each one, rescan to compute its sum.
     *
     * Time:
     *
     *     O(n^3)
     *
     * ------------------------------------------------------------
     *
     * 2. IMPROVED
     *
     * Fix start.
     *
     * Extend end one step at a time and keep runningSum.
     *
     * Time:
     *
     *     O(n^2)
     *
     * ------------------------------------------------------------
     *
     * 3. DP
     *
     * Ask:
     *
     *     "What is the best subarray that MUST end at i?"
     *
     * Transition:
     *
     *     restart OR extend
     *
     * Time:
     *
     *     O(n)
     *
     * Space:
     *
     *     O(n) if stored in dp[]
     *
     * ------------------------------------------------------------
     *
     * 4. STATE-COMPRESSED DP / KADANE
     *
     * Only dp[i - 1] is required.
     *
     * Replace dp[] with one variable.
     *
     * Time:
     *
     *     O(n)
     *
     * Space:
     *
     *     O(1)
     */

    /*
     * ============================================================
     * 🔴 SOLUTION 1 — BRUTE FORCE
     * ============================================================
     */

    static class BruteForce {

        public int maxSubArray(int[] nums) {

            int best = Integer.MIN_VALUE;

            for (int start = 0; start < nums.length; start++) {

                for (int end = start; end < nums.length; end++) {

                    int sum = 0;

                    for (int i = start; i <= end; i++) {
                        sum += nums[i];
                    }

                    best = Math.max(best, sum);
                }
            }

            return best;
        }
    }

    /*
     * ============================================================
     * 🟡 SOLUTION 2 — RUNNING SUM FOR EACH START
     * ============================================================
     */

    static class Improved {

        public int maxSubArray(int[] nums) {

            int best = Integer.MIN_VALUE;

            for (int start = 0; start < nums.length; start++) {

                int runningSum = 0;

                for (int end = start; end < nums.length; end++) {

                    runningSum += nums[end];

                    best = Math.max(best, runningSum);
                }
            }

            return best;
        }
    }

    /*
     * ============================================================
     * 🏆 SOLUTION 3 — KADANE
     * ============================================================
     *
     * THIS is the implementation to memorize.
     */

    static class Optimal {

        public int maxSubArray(int[] nums) {

            int currentBestEndingHere = nums[0];
            int globalBestSoFar = nums[0];

            for (int i = 1; i < nums.length; i++) {

                // Extend or restart:
                // carry baggage or fresh start.
                currentBestEndingHere =
                        Math.max(
                                nums[i],
                                currentBestEndingHere + nums[i]
                        );

                globalBestSoFar =
                        Math.max(
                                globalBestSoFar,
                                currentBestEndingHere
                        );
            }

            return globalBestSoFar;
        }
    }

    /*
     * ============================================================
     * 🎯 ONE TEMPLATE TO OWN
     * ============================================================
     *
     * Memorize THIS mental/code shape:
     *
     *     bestEndingHere =
     *         BEST(
     *             restart at current,
     *             extend previous ending-here state
     *         );
     *
     *     globalBest =
     *         BEST(globalBest, bestEndingHere);
     *
     * ------------------------------------------------------------
     * WHY THIS TEMPLATE HAS HIGH ROI
     * ------------------------------------------------------------
     *
     * It teaches the reusable DP question:
     *
     *     "What is the best state that MUST end here?"
     *
     * Then:
     *
     *     "Can I start fresh?"
     *
     *     "Can I extend the previous ending-here state?"
     *
     *     "Do I need one rolling state or a few?"
     *
     * This transfers beyond this exact sum formula.
     */

    /*
     * ============================================================
     * 🟢 KADANE WITH INDEX TRACKING
     * ============================================================
     *
     * Same transition.
     *
     * Extra state:
     *
     * startCandidate:
     *
     *     start of current ending-here subarray
     *
     * finalStart / finalEnd:
     *
     *     boundaries of global best
     */

    static class OptimalWithIndices {

        record Result(int maxSum, int startIndex, int endIndex) {}

        public Result maxSubArrayWithIndices(int[] nums) {

            int currentBestEndingHere = nums[0];
            int globalBestSoFar = nums[0];

            int startCandidate = 0;

            int finalStart = 0;
            int finalEnd = 0;

            for (int i = 1; i < nums.length; i++) {

                if (nums[i]
                        > currentBestEndingHere + nums[i]) {

                    // Restart.
                    currentBestEndingHere = nums[i];
                    startCandidate = i;

                } else {

                    // Extend.
                    currentBestEndingHere += nums[i];
                }

                if (currentBestEndingHere > globalBestSoFar) {

                    globalBestSoFar =
                            currentBestEndingHere;

                    finalStart = startCandidate;
                    finalEnd = i;
                }
            }

            return new Result(
                    globalBestSoFar,
                    finalStart,
                    finalEnd
            );
        }
    }

    /*
     * ============================================================
     * 🔍 ALTERNATIVE MATHEMATICAL VIEW — DO NOT MEMORIZE AS SECOND TEMPLATE
     * ============================================================
     *
     * Maximum Subarray can also be derived using prefix sums.
     *
     * For a subarray:
     *
     *     sum(left..right)
     *
     * =
     *
     *     prefix[right]
     *     -
     *     prefix[left - 1]
     *
     * So for each current prefix:
     *
     *     best ending here
     *
     * =
     *
     *     currentPrefix
     *     -
     *     minimum prefix seen BEFORE this current prefix
     *
     * This resembles Stock I:
     *
     *     currentPrice
     *     -
     *     minimumHistoricalPrice
     *
     * ------------------------------------------------------------
     * LEARNING DECISION
     * ------------------------------------------------------------
     *
     * UNDERSTAND:
     *
     *     prefixSum - minPrefixSeenBeforeCurrent
     *
     * Important implementation order:
     *
     *     1. update prefixSum
     *     2. compute answer using OLD minPrefix
     *     3. update minPrefix with current prefixSum
     *
     * Otherwise including the current prefix itself can accidentally
     * represent an empty subarray.
     *
     * MEMORIZE:
     *
     *     Kadane / best-ending-here
     *
     * Do NOT memorize both as separate implementations.
     *
     * Kadane has higher transfer value because it generalizes the
     * state-design idea rather than depending on subtraction algebra.
     */

    /*
     * ============================================================
     * 🔗 SAME STATE-DESIGN FAMILY 1 — MAXIMUM PRODUCT SUBARRAY
     * ============================================================
     *
     * Same question:
     *
     *     "What is the best product that MUST end here?"
     *
     * Difference:
     *
     * multiplication by a negative flips:
     *
     *     maximum <-> minimum
     *
     * Therefore track BOTH:
     *
     *     maxEndingHere
     *     minEndingHere
     *
     * At every index there are three possibilities:
     *
     *     current
     *     previousMax * current
     *     previousMin * current
     *
     * In sum problems:
     *
     *     negative history hurts.
     *
     * In product problems:
     *
     *     negative history may become best after another negative.
     */

    static class MaximumProductSubarray {

        public int maxProduct(int[] nums) {

            int maxEndingHere = nums[0];
            int minEndingHere = nums[0];

            int globalBest = nums[0];

            for (int i = 1; i < nums.length; i++) {

                int current = nums[i];

                int previousMax = maxEndingHere;
                int previousMin = minEndingHere;

                maxEndingHere =
                        Math.max(
                                current,
                                Math.max(
                                        previousMax * current,
                                        previousMin * current
                                )
                        );

                minEndingHere =
                        Math.min(
                                current,
                                Math.min(
                                        previousMax * current,
                                        previousMin * current
                                )
                        );

                globalBest =
                        Math.max(globalBest, maxEndingHere);
            }

            return globalBest;
        }
    }

    /*
     * ============================================================
     * 🔗 SAME STATE-DESIGN FAMILY 2 — MAXIMUM CIRCULAR SUBARRAY
     * ============================================================
     *
     * Two possibilities:
     *
     * 1. Best subarray does NOT wrap.
     *
     *     normal Kadane maximum
     *
     * 2. Best subarray DOES wrap.
     *
     *     totalSum - minimum subarray
     *
     * Why?
     *
     * To maximize wrapped portion,
     * remove the worst middle portion.
     *
     * ------------------------------------------------------------
     * ALL-NEGATIVE TRAP
     * ------------------------------------------------------------
     *
     * nums = [-3,-2,-5]
     *
     * minimum subarray = entire array
     *
     * totalSum - globalMin = 0
     *
     * That means:
     *
     *     remove entire array
     *
     * leaving an EMPTY subarray.
     *
     * Invalid.
     *
     * If globalMax < 0:
     *
     *     return globalMax
     */

    static class MaximumCircularSubarray {

        public int maxSubarraySumCircular(int[] nums) {

            int totalSum = nums[0];

            int maxEndingHere = nums[0];
            int minEndingHere = nums[0];

            int globalMax = nums[0];
            int globalMin = nums[0];

            for (int i = 1; i < nums.length; i++) {

                int value = nums[i];

                maxEndingHere =
                        Math.max(
                                value,
                                maxEndingHere + value
                        );

                minEndingHere =
                        Math.min(
                                value,
                                minEndingHere + value
                        );

                globalMax =
                        Math.max(globalMax, maxEndingHere);

                globalMin =
                        Math.min(globalMin, minEndingHere);

                totalSum += value;
            }

            if (globalMax < 0) {
                return globalMax;
            }

            return Math.max(
                    globalMax,
                    totalSum - globalMin
            );
        }
    }

    /*
     * ============================================================
     * 🔗 RELATED TRANSFORMATION — BEST TIME TO BUY/SELL STOCK I
     * ============================================================
     *
     * Prices:
     *
     *     [7,1,5,3,6,4]
     *
     * Daily differences:
     *
     *     [-6,4,-2,3,-2]
     *
     * For buy day b and later sell day s,
     * that profit equals the contiguous sum of day-to-day
     * differences from b + 1 through s.
     *
     * Therefore Stock I can be viewed as:
     *
     *     Maximum Subarray on daily differences.
     *
     * ------------------------------------------------------------
     * IMPORTANT
     * ------------------------------------------------------------
     *
     * For Stock I itself, the direct reusable solution is usually:
     *
     *     currentPrice - minimumHistoricalPrice
     *
     * So this connection is useful for understanding Kadane,
     * but you do NOT need to solve Stock I via Kadane in interviews.
     */

    static class BestTimeToBuySellStockViaKadane {

        public int maxProfit(int[] prices) {

            int bestEndingHere = 0;
            int globalBest = 0;

            for (int i = 1; i < prices.length; i++) {

                int dailyDiff =
                        prices[i] - prices[i - 1];

                bestEndingHere =
                        Math.max(
                                0,
                                bestEndingHere + dailyDiff
                        );

                globalBest =
                        Math.max(globalBest, bestEndingHere);
            }

            return globalBest;
        }
    }


    /*
     * ============================================================
     * ♻️ PATTERN REINFORCEMENT LADDER
     * ============================================================
     *
     * The goal is NOT to memorize six algorithms.
     *
     * Keep asking the SAME question:
     *
     *     "What is the best state that MUST end here?"
     *
     * Then add only the minimum extra state required by the variant.
     *
     * ------------------------------------------------------------
     * LEVEL 1 — EXACT MIRROR
     * ------------------------------------------------------------
     *
     * Minimum Subarray:
     *
     *     minEndingHere =
     *         min(
     *             current,                 // restart
     *             minEndingHere + current  // extend
     *         )
     *
     * Literally Kadane with max -> min.
     *
     * ------------------------------------------------------------
     * LEVEL 2 — TRACK BOTH DIRECTIONS
     * ------------------------------------------------------------
     *
     * Maximum Absolute Subarray Sum:
     *
     *     track maxEndingHere
     *     track minEndingHere
     *
     * answer:
     *
     *     max(
     *         abs(globalMax),
     *         abs(globalMin)
     *     )
     *
     * ------------------------------------------------------------
     * LEVEL 3 — ADD ONE "USED OPERATION" STATE
     * ------------------------------------------------------------
     *
     * Maximum Subarray Sum With One Deletion:
     *
     *     keepEndingHere
     *
     *         best ending here with NO deletion
     *
     *     deleteEndingHere
     *
     *         best ending here after using ONE deletion
     *
     * Same Kadane principle, but state now remembers whether the
     * special operation has already been consumed.
     *
     * ------------------------------------------------------------
     * LEVEL 4 — REUSE KADANE AS AN INNER ENGINE
     * ------------------------------------------------------------
     *
     * Maximum Sum Rectangle in a Matrix:
     *
     *     choose top row
     *     choose bottom row
     *     compress rows into column sums
     *     run ordinary 1D Kadane on the compressed array
     *
     * This shows that Kadane is not merely one array trick.
     * It can become a reusable optimization subroutine.
     */

    /*
     * ============================================================
     * 🔗 REINFORCEMENT 1 — MINIMUM SUBARRAY
     * ============================================================
     *
     * Exact mirror of Maximum Subarray.
     *
     * OWN THIS RELATIONSHIP:
     *
     *     maxEndingHere -> maximum version
     *     minEndingHere -> minimum version
     */

    static class MinimumSubarray {

        public int minSubArray(int[] nums) {

            int currentMinEndingHere = nums[0];
            int globalMinSoFar = nums[0];

            for (int i = 1; i < nums.length; i++) {

                currentMinEndingHere =
                        Math.min(
                                nums[i],
                                currentMinEndingHere + nums[i]
                        );

                globalMinSoFar =
                        Math.min(
                                globalMinSoFar,
                                currentMinEndingHere
                        );
            }

            return globalMinSoFar;
        }
    }

    /*
     * ============================================================
     * 🔗 REINFORCEMENT 2 — MAXIMUM ABSOLUTE SUBARRAY SUM
     * ============================================================
     *
     * Need the largest magnitude:
     *
     *     either a very positive subarray
     *     or a very negative subarray
     *
     * Therefore run max-Kadane and min-Kadane together.
     */

    static class MaximumAbsoluteSubarraySum {

        public int maxAbsoluteSum(int[] nums) {

            int maxEndingHere = nums[0];
            int minEndingHere = nums[0];

            int globalMax = nums[0];
            int globalMin = nums[0];

            for (int i = 1; i < nums.length; i++) {

                int value = nums[i];

                maxEndingHere =
                        Math.max(
                                value,
                                maxEndingHere + value
                        );

                minEndingHere =
                        Math.min(
                                value,
                                minEndingHere + value
                        );

                globalMax = Math.max(globalMax, maxEndingHere);
                globalMin = Math.min(globalMin, minEndingHere);
            }

            return Math.max(
                    Math.abs(globalMax),
                    Math.abs(globalMin)
            );
        }
    }

    /*
     * ============================================================
     * 🔗 REINFORCEMENT 3 — MAXIMUM SUBARRAY WITH ONE DELETION
     * ============================================================
     *
     * Same ending-here idea, now with TWO states.
     *
     * keepEndingHere:
     *
     *     best non-empty subarray ending here
     *     without deleting anything
     *
     * deleteUsedThroughHere:
     *
     *     best sum for a candidate span processed through i
     *     after using one deletion.
     *
     *     If current itself is deleted, the retained values end at i - 1.
     *
     * Transition:
     *
     * keep:
     *
     *     restart
     *     OR
     *     extend
     *
     * delete:
     *
     *     delete CURRENT element
     *         -> previous keep state
     *
     *     OR
     *
     *     deletion happened earlier, extend with current
     *         -> previous delete state + current
     *
     * This is the natural DP extension:
     *
     *     Kadane state
     *     +
     *     one boolean dimension: operation used or not.
     */

    static class MaximumSubarrayWithOneDeletion {

        public int maximumSum(int[] nums) {

            int keepEndingHere = nums[0];

            // Transitional state: delete nums[0].
            // It is not allowed to become the final answer by itself
            // because the final subarray must remain non-empty.
            int deleteUsedThroughHere = 0;

            int globalBest = nums[0];

            for (int i = 1; i < nums.length; i++) {

                int value = nums[i];

                int previousKeep = keepEndingHere;
                int previousDelete = deleteUsedThroughHere;

                keepEndingHere =
                        Math.max(
                                value,
                                previousKeep + value
                        );

                deleteUsedThroughHere =
                        Math.max(
                                previousKeep,          // delete current
                                previousDelete + value // deleted earlier
                        );

                globalBest =
                        Math.max(
                                globalBest,
                                Math.max(
                                        keepEndingHere,
                                        deleteUsedThroughHere
                                )
                        );
            }

            return globalBest;
        }
    }

    /*
     * ============================================================
     * 🔗 REINFORCEMENT 4 — MAXIMUM SUM RECTANGLE IN 2D
     * ============================================================
     *
     * Reuse ordinary Kadane as a subroutine.
     *
     * For each pair:
     *
     *     top row
     *     bottom row
     *
     * compress that row band into:
     *
     *     columnSums[col]
     *
     * Then solve:
     *
     *     maximum subarray(columnSums)
     *
     * with the SAME Optimal Kadane implementation.
     *
     * Complexity:
     *
     *     O(rows^2 * cols)
     *
     * if rows are the dimension we pair.
     */

    static class MaximumSumRectangle2D {

        public int maxSumRectangle(int[][] matrix) {

            if (matrix == null
                    || matrix.length == 0
                    || matrix[0].length == 0) {
                return 0;
            }

            int rows = matrix.length;
            int cols = matrix[0].length;

            int best = Integer.MIN_VALUE;

            Optimal kadane = new Optimal();

            for (int top = 0; top < rows; top++) {

                int[] columnSums = new int[cols];

                for (int bottom = top; bottom < rows; bottom++) {

                    for (int col = 0; col < cols; col++) {
                        columnSums[col] += matrix[bottom][col];
                    }

                    best =
                            Math.max(
                                    best,
                                    kadane.maxSubArray(columnSums)
                            );
                }
            }

            return best;
        }
    }

    /*
     * ============================================================
     * 🧭 REUSABILITY MAP
     * ============================================================
     *
     * MAXIMUM SUBARRAY
     *
     *     max ending here
     *
     * ------------------------------------------------------------
     *
     * MAXIMUM PRODUCT SUBARRAY
     *
     *     max ending here
     *     +
     *     min ending here
     *
     * ------------------------------------------------------------
     *
     * MAXIMUM CIRCULAR SUBARRAY
     *
     *     max ending here
     *     +
     *     min ending here
     *     +
     *     total sum
     *
     * ------------------------------------------------------------
     *
     * STOCK I
     *
     *     related through daily-difference transformation
     *
     * ------------------------------------------------------------
     *
     * MINIMUM SUBARRAY
     *
     *     exact mirror: max -> min
     *
     * ------------------------------------------------------------
     *
     * MAXIMUM ABSOLUTE SUBARRAY SUM
     *
     *     max ending here
     *     +
     *     min ending here
     *
     * ------------------------------------------------------------
     *
     * MAXIMUM SUBARRAY WITH ONE DELETION
     *
     *     ending-here state
     *     +
     *     deletion-used state
     *
     * ------------------------------------------------------------
     *
     * MAXIMUM SUM RECTANGLE IN 2D
     *
     *     compress rows
     *     +
     *     reuse ordinary Kadane
     *
     * ------------------------------------------------------------
     * RECOGNITION SIGNAL
     * ------------------------------------------------------------
     *
     * contiguous sequence
     * +
     * optimization objective
     * +
     * decision at i depends only on a small summary of state
     * ending at i - 1
     *
     *     -> Kadane / compressed 1D DP
     */

    /*
     * ============================================================
     * 🔴 PATTERN BOUNDARIES
     * ============================================================
     *
     * Kadane works when:
     *
     *     answer is contiguous
     *     +
     *     ending-here state summarizes all useful history
     *
     * Kadane does NOT directly apply when:
     *
     *     elements may be arbitrarily reordered
     *
     *     multiple non-contiguous selections are allowed
     *
     *     future decision needs richer history than a small
     *     rolling ending-here state
     *
     * ------------------------------------------------------------
     * DO NOT CONFUSE WITH SLIDING WINDOW
     * ------------------------------------------------------------
     *
     * Sliding Window:
     *
     *     maintains an explicit contiguous [left, right] window
     *     and adjusts boundaries to satisfy a predicate
     *
     * Kadane:
     *
     *     maintains the BEST VALUE of a subarray ending here
     *
     *     no left pointer is required for sum-only version
     */

    /*
     * ============================================================
     * ⚡ RECONSTRUCTION DRILL
     * ============================================================
     *
     * If you forget the code, rebuild it:
     *
     * 1. What MUST my local state mean?
     *
     *     best subarray that ends at i
     *
     * 2. What choices can such a subarray have?
     *
     *     restart at i
     *     OR
     *     extend best ending at i - 1
     *
     * 3. Take the better one.
     *
     * 4. Track the global best.
     *
     * That mechanically recreates Kadane.
     */

    /*
     * ============================================================
     * 🧠 4-LINE MENTAL MAP
     * ============================================================
     *
     * Extend or restart.
     * Track ending-here.
     * Lock global best.
     * Handle negatives.
     */

    /*
     * ============================================================
     * 🎯 INTERVIEW RECALL SHEET
     * ============================================================
     *
     * TRIGGER:
     *
     *     maximum/minimum contiguous subarray-style optimization
     *
     * STATE:
     *
     *     bestEndingHere
     *
     * TRANSITION:
     *
     *     restart here
     *     OR
     *     extend previous
     *
     * GLOBAL:
     *
     *     best answer anywhere so far
     *
     * ONE-LINER:
     *
     *     "Is my past helping me or hurting me?"
     *
     * COMMON BUG:
     *
     *     resetting to zero and breaking all-negative arrays
     *
     * COMPLEXITY:
     *
     *     O(n) time
     *     O(1) extra space
     *
     * STREAMING:
     *
     *     yes — only rolling state is needed
     *
     * LEARNING DECISION:
     *
     *     OWN Kadane.
     *     RECOGNIZE prefix-min.
     *     DO NOT memorize both implementations.
     */

    /*
     * ============================================================
     * 🧪 SELF-VERIFYING TESTS
     * ============================================================
     */

    private static void assertEquals(int expected,
                                     int actual,
                                     String reason) {

        if (expected != actual) {
            throw new AssertionError(
                    reason
                    + "\nExpected: " + expected
                    + "\nActual:   " + actual
            );
        }
    }

    private static void assertSameAsBrute(int[] nums) {

        int brute =
                new BruteForce()
                        .maxSubArray(nums);

        int optimal =
                new Optimal()
                        .maxSubArray(nums);

        if (brute != optimal) {
            throw new AssertionError(
                    "Brute/optimal mismatch"
            );
        }
    }

    public static void main(String[] args) {

        Optimal optimal = new Optimal();

        assertEquals(
                6,
                optimal.maxSubArray(
                        new int[]{
                                -2,1,-3,4,-1,2,1,-5,4
                        }
                ),
                "Classic maximum subarray"
        );

        assertEquals(
                -2,
                optimal.maxSubArray(
                        new int[]{-3,-2,-5}
                ),
                "All-negative array"
        );

        assertEquals(
                1,
                optimal.maxSubArray(
                        new int[]{1}
                ),
                "Single element"
        );

        OptimalWithIndices.Result result =
                new OptimalWithIndices()
                        .maxSubArrayWithIndices(
                                new int[]{
                                        -2,1,-3,4,-1,2,1,-5,4
                                }
                        );

        assertEquals(
                6,
                result.maxSum(),
                "Index tracking sum"
        );

        assertEquals(
                3,
                result.startIndex(),
                "Index tracking start"
        );

        assertEquals(
                6,
                result.endIndex(),
                "Index tracking end"
        );

        assertEquals(
                6,
                new MaximumProductSubarray()
                        .maxProduct(
                                new int[]{2,3,-2,4}
                        ),
                "Maximum Product Subarray"
        );

        assertEquals(
                10,
                new MaximumCircularSubarray()
                        .maxSubarraySumCircular(
                                new int[]{5,-3,5}
                        ),
                "Circular maximum"
        );

        assertEquals(
                -2,
                new MaximumCircularSubarray()
                        .maxSubarraySumCircular(
                                new int[]{-3,-2,-5}
                        ),
                "Circular all-negative"
        );

        assertEquals(
                5,
                new BestTimeToBuySellStockViaKadane()
                        .maxProfit(
                                new int[]{7,1,5,3,6,4}
                        ),
                "Stock via daily differences"
        );

        assertEquals(
                -6,
                new MinimumSubarray()
                        .minSubArray(
                                new int[]{3,-4,2,-3,-1,7,-5}
                        ),
                "Minimum Subarray"
        );

        assertEquals(
                5,
                new MaximumAbsoluteSubarraySum()
                        .maxAbsoluteSum(
                                new int[]{1,-3,2,3,-4}
                        ),
                "Maximum Absolute Subarray Sum"
        );

        assertEquals(
                4,
                new MaximumSubarrayWithOneDeletion()
                        .maximumSum(
                                new int[]{1,-2,0,3}
                        ),
                "Maximum Subarray With One Deletion"
        );

        assertEquals(
                29,
                new MaximumSumRectangle2D()
                        .maxSumRectangle(
                                new int[][]{
                                        {1, 2, -1, -4, -20},
                                        {-8, -3, 4, 2, 1},
                                        {3, 8, 10, 1, 3},
                                        {-4, -1, 1, 7, -6}
                                }
                        ),
                "Maximum Sum Rectangle 2D"
        );

        int[][] regression = {
                {-2,1,-3,4,-1,2,1,-5,4},
                {-3,-2,-5},
                {1},
                {1,2,3},
                {5,-10,6},
                {-1,0,-2},
                {8,-1,-1,8}
        };

        for (int[] nums : regression) {
            assertSameAsBrute(nums);
        }

        System.out.println(
                "All KadaneMaxSubArrayV5 tests passed."
        );
    }
}
