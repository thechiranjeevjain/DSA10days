package org.chijai.day7.session1;

import java.util.*;

/**
 * ============================================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================================
 *
 * Title:
 * Award Top K Hotels
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * HashMap
 * HashSet
 * String Processing
 * Heap (Priority Queue)
 * Top K
 * Custom Comparator
 *
 * Problem Description
 * -------------------
 * A travel company wants to identify the highest-rated hotels based on customer
 * reviews.
 *
 * Every review belongs to exactly one hotel.
 *
 * Two dictionaries are provided:
 *
 * Positive keywords
 * Negative keywords
 *
 * Scoring Rules
 * -------------
 * Every occurrence of a positive keyword contributes +3.
 *
 * Every occurrence of a negative keyword contributes -1.
 *
 * Words are compared case-insensitively.
 *
 * Reviews belonging to the same hotel contribute to the same cumulative score.
 *
 * Return the IDs of the Top-K hotels sorted by:
 *
 * 1. Higher score first.
 * 2. If scores are equal,
 *    smaller hotel id should appear first.
 *
 * Constraints
 * -----------
 * • Number of reviews >= 1
 * • k >= 1
 * • hotelIds.size() == reviews.size()
 * • Same hotel may have many reviews.
 * • Keywords contain distinct words.
 * • Reviews may contain punctuation.
 *
 * Representative Example
 * ----------------------
 *
 * Positive:
 * "breakfast beach citycenter location metro view staff price"
 *
 * Negative:
 * "not"
 *
 * Hotel IDs:
 * [1,2,1,1,2]
 *
 * Reviews:
 * [
 * "This hotel has a nice view of the citycenter.",
 * "The breakfast is ok.",
 * "Location is excellent, 5 minutes from citycenter.",
 * "There is no breakfast and the staff is not friendly.",
 * "Very friendly staff."
 * ]
 *
 * Score(1)
 * --------
 * review1 : view + citycenter = +6
 * review3 : location + citycenter = +6
 * review4 : breakfast + staff - not = +5
 *
 * Total = 17
 *
 * Score(2)
 * --------
 * review2 : breakfast = +3
 * review5 : staff = +3
 *
 * Total = 6
 *
 * Answer:
 *
 * [1,2]
 *
 * Official References
 * -------------------
 * https://pragmaticdeveloper.info/award-top-k-hotels/
 *
 * https://leetcode.com/discuss/interview-question/1431676/Booking.com-Award-Top-K-Hotels-OA-HackerRank
 */
public class AwardTopKHotels {

    /**
     * =========================================================================
     * 3. 🔵 CORE PATTERN OVERVIEW
     * =========================================================================
     *
     * Pattern
     * -------
     * Aggregate Scores + Fixed-Size Min Heap
     *
     * Archetype
     * ---------
     * Top-K after aggregation.
     *
     * Core Invariant
     * --------------
     * After processing every review,
     * each hotel has exactly one accumulated score.
     *
     * During heap construction,
     * the heap always contains the current best K hotels.
     *
     * The root is intentionally the worst hotel among those K.
     *
     * Therefore,
     * whenever heap size exceeds K,
     * removing the root never removes a hotel that belongs to
     * the final answer.
     *
     * Why It Works
     * ------------
     * Aggregation converts many review records into one score per hotel.
     *
     * Afterwards,
     * Top-K becomes the classical "maintain K best elements" problem.
     *
     * Recognition Signals
     * -------------------
     * ✓ Multiple records per entity
     * ✓ Need cumulative score
     * ✓ Top K only
     * ✓ No need for complete sorting
     * ✓ Custom ranking
     *
     * Use When
     * --------
     * • Ranking users
     * • Ranking hotels
     * • Ranking products
     * • Leaderboards
     * • Frequency based Top-K
     *
     * Do NOT Use
     * ----------
     * • When every element must be sorted.
     * • When K equals total element count.
     * • When ordering changes continuously after every update
     *   (balanced tree may be preferable).
     *
     * Comparison
     * ----------
     *
     * Full Sort
     * ----------
     * Time:
     * O(n log n)
     *
     * Heap
     * ----
     * O(n log k)
     *
     * QuickSelect
     * -----------
     * Finds kth element,
     * but does not naturally produce ordered Top-K with tie-breaking.
     */

    /**
     * =========================================================================
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     * =========================================================================
     *
     * Mental Model
     * ------------
     * Imagine every hotel owns a score board.
     *
     * Reviews simply modify the hotel's score.
     *
     * Once all reviews finish,
     * forget the reviews completely.
     *
     * Only one record per hotel survives.
     *
     * Now imagine interviewing only the best K hotels.
     *
     * The heap is the waiting room.
     *
     * Whenever the room exceeds capacity,
     * remove the weakest candidate.
     *
     * Eventually only the strongest K remain.
     *
     * -----------------------------
     * Invariant 1
     * -----------------------------
     *
     * hotelScore[id]
     *
     * always equals the cumulative score
     * of every processed review belonging
     * to that hotel.
     *
     * -----------------------------
     * Invariant 2
     * -----------------------------
     *
     * Positive score contribution:
     *
     * +3 × occurrences
     *
     * Negative contribution:
     *
     * -1 × occurrences
     *
     * Every occurrence matters.
     *
     * Duplicate words inside one review
     * contribute multiple times.
     *
     * -----------------------------
     * Invariant 3
     * -----------------------------
     *
     * Heap size never exceeds K.
     *
     * -----------------------------
     * Invariant 4
     * -----------------------------
     *
     * Heap root is the weakest hotel
     * among the retained hotels.
     *
     * Therefore,
     * poll() is always safe.
     *
     * -----------------------------
     * Variable Meanings
     * -----------------------------
     *
     * positiveWords
     *     Fast membership lookup.
     *
     * negativeWords
     *     Fast membership lookup.
     *
     * hotelScore
     *     Aggregated score.
     *
     * pq
     *     Current best K hotels.
     *
     * -----------------------------
     * Allowed State Transition
     * -----------------------------
     *
     * review
     *     →
     * tokenize
     *     →
     * score
     *     →
     * accumulate
     *
     * After aggregation:
     *
     * hotel
     *     →
     * heap insertion
     *     →
     * possible removal
     *
     * -----------------------------
     * Forbidden Transition
     * -----------------------------
     *
     * Ranking reviews directly.
     *
     * The ranking unit is hotel,
     * not review.
     *
     * -----------------------------
     * Termination
     * -----------------------------
     *
     * Every review processed exactly once.
     *
     * Every hotel inserted once.
     *
     * Heap extraction finishes after K removals.
     *
     * -----------------------------
     * Why Naive Solutions Fail
     * -----------------------------
     *
     * Sorting reviews instead of hotels
     * ignores cumulative score.
     *
     * Maintaining every review inside heap
     * duplicates hotels.
     *
     * Recomputing hotel score repeatedly
     * wastes time.
     */

    /**
     * =========================================================================
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     * =========================================================================
     *
     * Mistake 1
     * ---------
     * Sort reviews instead of hotels.
     *
     * Why it looks correct:
     * Reviews already contain scores.
     *
     * Violated invariant:
     * Ranking unit must be hotel.
     *
     * ------------------------------
     *
     * Mistake 2
     * ---------
     * Ignore repeated reviews
     * for same hotel.
     *
     * Violated invariant:
     * hotelScore stores cumulative score.
     *
     * ------------------------------
     *
     * Mistake 3
     * ---------
     * Comparator uses larger id
     * before smaller id.
     *
     * Tie-breaking becomes incorrect.
     *
     * ------------------------------
     *
     * Mistake 4
     * ---------
     * Remove largest element from heap.
     *
     * Violated invariant:
     * Root must always represent
     * the weakest retained hotel.
     *
     * ------------------------------
     *
     * Mistake 5
     * ---------
     * Forget punctuation normalization.
     *
     * "location,"
     * becomes different from
     * "location".
     *
     * Counterexample
     * --------------
     *
     * Positive:
     *
     * location
     *
     * Review:
     *
     * "Great location."
     *
     * Without punctuation stripping,
     * score becomes zero.
     */

    /**
     * =========================================================================
     * ⚙️ IMPLEMENTATION BLUEPRINT
     * =========================================================================
     *
     * Typing Order
     * ------------
     *
     * 1.
     * Build positive HashSet.
     *
     * 2.
     * Build negative HashSet.
     *
     * 3.
     * Create hotelScore map.
     *
     * 4.
     * Iterate reviews.
     *
     * 5.
     * Normalize every token.
     *
     * 6.
     * Count review score.
     *
     * 7.
     * Accumulate hotel score.
     *
     * 8.
     * Create fixed-size min heap.
     *
     * 9.
     * Insert every hotel.
     *
     * 10.
     * Remove root if size>K.
     *
     * 11.
     * Extract in reverse order.
     *
     * Function Skeleton
     * -----------------
     *
     * build dictionaries
     *
     * aggregate scores
     *
     * build heap
     *
     * extract answer
     *
     * return
     */

    /**
     * =========================================================================
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     * =========================================================================
     *
     * build sets
     *
     * for every review
     *     score review
     *     accumulate hotel
     *
     * for every hotel
     *     push heap
     *     if heap>K
     *         pop
     *
     * extract reverse
     *
     * return
     */

    /**
     * =========================================================================
     * 6. SOLUTION CLASSES
     * =========================================================================
     */

    /**
     * -------------------------------------------------------------------------
     * Brute Force
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     * Aggregate scores.
     * Sort every hotel.
     *
     * Invariant
     * ---------
     * Entire ranking always available.
     *
     * Limitation
     * ----------
     * Pays sorting cost for every hotel.
     *
     * Complexity
     * ----------
     * Time:
     * O(R + H log H)
     *
     * Space:
     * O(H)
     *
     * Interview Usefulness
     * --------------------
     * Good starting solution.
     */
    static class BruteForce {

        static List<Integer> awardTopKHotels(
                String positiveKeywords,
                String negativeKeywords,
                List<Integer> hotelIds,
                List<String> reviews,
                int k) {

            Set<String> positive = buildDictionary(positiveKeywords);
            Set<String> negative = buildDictionary(negativeKeywords);

            Map<Integer, Integer> scoreMap =
                    aggregateScores(positive, negative, hotelIds, reviews);

            List<Map.Entry<Integer, Integer>> hotels =
                    new ArrayList<>(scoreMap.entrySet());

            hotels.sort((a, b) -> {
                if (!a.getValue().equals(b.getValue())) {
                    return Integer.compare(b.getValue(), a.getValue());
                }
                return Integer.compare(a.getKey(), b.getKey());
            });

            List<Integer> answer = new ArrayList<>();

            for (int i = 0; i < Math.min(k, hotels.size()); i++) {
                answer.add(hotels.get(i).getKey());
            }

            return answer;
        }
    }

    /**
     * -------------------------------------------------------------------------
     * Improved
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     * Aggregate scores once.
     *
     * Maintain a fixed-size min heap containing only the current best K hotels.
     *
     * 🟢 Invariant
     * ------------
     * After processing every hotel,
     * the heap contains exactly the best K hotels seen so far.
     *
     * The heap root is intentionally the weakest among those retained hotels.
     *
     * Improvement
     * -----------
     * Avoids sorting all hotels.
     *
     * Complexity
     * ----------
     * Let:
     *
     * R = number of reviews
     * T = total number of words
     * H = number of distinct hotels
     *
     * Time:
     * O(T + H log K)
     *
     * Space:
     * O(H)
     *
     * Interview Usefulness
     * --------------------
     * This is the expected production-quality solution.
     */
    static class Improved {

        static List<Integer> awardTopKHotels(
                String positiveKeywords,
                String negativeKeywords,
                List<Integer> hotelIds,
                List<String> reviews,
                int k) {

            Set<String> positive = buildDictionary(positiveKeywords);
            Set<String> negative = buildDictionary(negativeKeywords);

            Map<Integer, Integer> scoreMap =
                    aggregateScores(positive, negative, hotelIds, reviews);

            PriorityQueue<Map.Entry<Integer, Integer>> minHeap =
                    createRankingHeap();

            for (Map.Entry<Integer, Integer> entry : scoreMap.entrySet()) {

                // 🟢 Invariant:
                // Heap always stores only the best candidates.
                minHeap.offer(entry);

                if (minHeap.size() > k) {

                    // Discard Rule:
                    // Remove the weakest retained hotel.
                    minHeap.poll();
                }
            }

            LinkedList<Integer> answer = new LinkedList<>();

            while (!minHeap.isEmpty()) {

                // Reverse extraction because heap pops weakest first.
                answer.addFirst(minHeap.poll().getKey());
            }

            return answer;
        }
    }

    /**
     * -------------------------------------------------------------------------
     * Optimal (Interview Preferred)
     * -------------------------------------------------------------------------
     *
     * Idea
     * ----
     * Exactly the same asymptotic complexity as the improved solution,
     * but factored into reusable helper methods for easier implementation,
     * debugging, and interview reconstruction.
     *
     * 🟢 Invariant
     * ------------
     * Score aggregation and Top-K maintenance are independent phases.
     *
     * Correctness
     * -----------
     * Phase 1 guarantees every hotel has its final cumulative score.
     *
     * Phase 2 guarantees only the strongest K survive.
     *
     * Complexity
     * ----------
     * Time:
     * O(T + H log K)
     *
     * Space:
     * O(H)
     *
     * Interview Usefulness
     * --------------------
     * Recommended implementation.
     */
    static class Optimal {

        static List<Integer> awardTopKHotels(
                String positiveKeywords,
                String negativeKeywords,
                List<Integer> hotelIds,
                List<String> reviews,
                int k) {

            Set<String> positiveWords = buildDictionary(positiveKeywords);

            Set<String> negativeWords = buildDictionary(negativeKeywords);

            Map<Integer, Integer> hotelScores =
                    aggregateScores(
                            positiveWords,
                            negativeWords,
                            hotelIds,
                            reviews);

            PriorityQueue<Map.Entry<Integer, Integer>> heap =
                    createRankingHeap();

            for (Map.Entry<Integer, Integer> entry : hotelScores.entrySet()) {

                // 🟢 Invariant:
                // Heap contains the strongest hotels processed so far.
                heap.offer(entry);

                if (heap.size() > k) {

                    // 🟢 Root is intentionally weakest.
                    heap.poll();
                }
            }

            LinkedList<Integer> answer = new LinkedList<>();

            while (!heap.isEmpty()) {

                // Correct ordering is strongest → weakest.
                answer.addFirst(heap.poll().getKey());
            }

            return answer;
        }
    }

    /**
     * =========================================================================
     * Shared Helper Methods
     * =========================================================================
     */

    private static Set<String> buildDictionary(String keywords) {

        Set<String> dictionary = new HashSet<>();

        if (keywords == null || keywords.isBlank()) {
            return dictionary;
        }

        for (String word : keywords.split("\\s+")) {

            if (!word.isEmpty()) {
                dictionary.add(word.toLowerCase());
            }
        }

        return dictionary;
    }

    private static Map<Integer, Integer> aggregateScores(
            Set<String> positiveWords,
            Set<String> negativeWords,
            List<Integer> hotelIds,
            List<String> reviews) {

        Map<Integer, Integer> hotelScore = new HashMap<>();

        for (int reviewIndex = 0; reviewIndex < reviews.size(); reviewIndex++) {

            int hotelId = hotelIds.get(reviewIndex);

            int reviewScore =
                    scoreReview(
                            reviews.get(reviewIndex),
                            positiveWords,
                            negativeWords);

            // 🟢 Invariant:
            // Stored value equals cumulative score of processed reviews.
            hotelScore.merge(hotelId, reviewScore, Integer::sum);
        }

        return hotelScore;
    }

    private static int scoreReview(
            String review,
            Set<String> positiveWords,
            Set<String> negativeWords) {

        int score = 0;

        for (String rawToken : review.split("\\s+")) {

            String token = normalize(rawToken);

            if (token.isEmpty()) {
                continue;
            }

            if (positiveWords.contains(token)) {

                // Every positive occurrence contributes independently.
                score += 3;
            }

            if (negativeWords.contains(token)) {

                // Every negative occurrence contributes independently.
                score -= 1;
            }
        }

        return score;
    }

    private static String normalize(String token) {

        int left = 0;
        int right = token.length() - 1;

        while (left <= right &&
                !Character.isLetterOrDigit(token.charAt(left))) {
            left++;
        }

        while (right >= left &&
                !Character.isLetterOrDigit(token.charAt(right))) {
            right--;
        }

        if (left > right) {
            return "";
        }

        return token.substring(left, right + 1).toLowerCase();
    }

    private static PriorityQueue<Map.Entry<Integer, Integer>> createRankingHeap() {

        return new PriorityQueue<>((a, b) -> {

            int scoreComparison =
                    Integer.compare(a.getValue(), b.getValue());

            if (scoreComparison != 0) {
                return scoreComparison;
            }

            /*
             * 🟢 Comparator Invariant
             * -----------------------
             *
             * Smaller hotel id wins final ranking.
             *
             * Therefore,
             * inside the MIN heap,
             * larger hotel id must become weaker
             * so it gets removed first.
             */
            return Integer.compare(b.getKey(), a.getKey());
        });
    }

/**
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * Invariant
 * ---------
 * First aggregate every hotel's complete score.
 * Never rank individual reviews.
 *
 * Search Space
 * ------------
 * Distinct hotels.
 *
 * Discard Rule
 * ------------
 * Whenever heap exceeds K,
 * discard the weakest retained hotel.
 *
 * Correctness
 * -----------
 * Because the heap root is always the weakest candidate,
 * removing it can never eliminate a hotel that belongs
 * to the final Top-K.
 *
 * Termination
 * -----------
 * Every review is processed once.
 * Every hotel enters the heap once.
 *
 * In-place Feasibility
 * --------------------
 * No.
 *
 * Aggregated scores require auxiliary storage.
 *
 * Streaming Feasibility
 * ---------------------
 * Partially.
 *
 * Reviews may arrive as a stream while updating scores.
 *
 * Final ranking still requires maintaining the heap.
 *
 * When NOT to Use
 * ---------------
 * If complete ordering of all hotels is required,
 * simply sorting may be clearer.
 */

/**
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================================
 *
 * Trigger
 * -------
 * Aggregate entity scores then return Top-K.
 *
 * Pattern
 * -------
 * HashMap + Fixed-Size Min Heap.
 *
 * Invariant
 * ---------
 * Heap root is weakest retained hotel.
 *
 * Search Target
 * -------------
 * Distinct hotels.
 *
 * Discard Rule
 * ------------
 * Heap size > K → remove root.
 *
 * Common Trap
 * -----------
 * Wrong tie-breaking comparator.
 *
 * Edge Cases
 * ----------
 * Empty keyword list.
 * Multiple reviews.
 * Duplicate keywords in review.
 * Punctuation.
 * Case differences.
 * K > number of hotels.
 *
 * One-Liner
 * ---------
 * Aggregate first, rank second.
 *
 * Re-Derivation Cue
 * -----------------
 * One score per hotel.
 * One heap for winners.
 */

    /**
     * =========================================================================
     * 🔄 VARIATIONS & TWEAKS
     * =========================================================================
     *
     * Variation 1
     * -----------
     * Different positive / negative weights.
     *
     * Reasoning Change
     * ----------------
     * Only scoreReview() changes.
     *
     * Pattern remains identical.
     *
     * -------------------------------------------------------------
     *
     * Variation 2
     * -----------
     * Return complete ranking.
     *
     * Pattern Change
     * --------------
     * Replace fixed-size heap with full sorting.
     *
     * -------------------------------------------------------------
     *
     * Variation 3
     * -----------
     * Top-K products
     * Top-K restaurants
     * Top-K sellers
     * Top-K users
     *
     * Same Pattern
     * ------------
     * Aggregate by entity.
     * Maintain Top-K.
     *
     * -------------------------------------------------------------
     *
     * Variation 4
     * -----------
     * Millions of reviews.
     *
     * Still Works
     * -----------
     * Reviews are processed once.
     *
     * Memory depends primarily on the number
     * of distinct hotels rather than reviews.
     *
     * -------------------------------------------------------------
     *
     * Variation 5
     * -----------
     * Reviews streamed continuously.
     *
     * Pattern
     * -------
     * Keep updating scoreMap.
     *
     * Periodically rebuild or maintain the Top-K heap.
     *
     * =========================================================================
     * 🧠 MASTERY CHECKLIST
     * =========================================================================
     *
     * □ I know the Pattern.
     *
     * □ I know the aggregation invariant.
     *
     * □ I know why reviews are never ranked directly.
     *
     * □ I know why the heap root must be the weakest retained hotel.
     *
     * □ I can derive the comparator.
     *
     * □ I know why larger hotel id is considered weaker on score ties.
     *
     * □ I know the discard rule.
     *
     * □ I know termination.
     *
     * □ I know why naive sorting of reviews fails.
     *
     * □ I know punctuation must be normalized.
     *
     * □ I know duplicate keyword occurrences contribute repeatedly.
     *
     * □ I can explain correctness without code.
     *
     * □ I can reconstruct the implementation from the invariants alone.
     */

    /**
     * =========================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * =========================================================================
     */

    public static void main(String[] args) {

        testRepresentativeExample();

        testTieBreaking();

        testCaseInsensitive();

        testDuplicateOccurrences();

        testMultipleReviewsPerHotel();

        testPunctuationHandling();

        testEmptyKeywordLists();

        testKGreaterThanHotels();

        System.out.println("All assertions passed.");
    }

    private static void testRepresentativeExample() {

        String positive =
                "breakfast beach citycenter location metro view staff price";

        String negative = "not";

        List<Integer> hotelIds =
                Arrays.asList(1, 2, 1, 1, 2);

        List<String> reviews =
                Arrays.asList(
                        "This hotel has a nice view of the citycenter.",
                        "The breakfast is ok.",
                        "Location is excellent, 5 minutes from citycenter.",
                        "There is breakfast and the staff is not friendly.",
                        "Very friendly staff.");

        List<Integer> expected = Arrays.asList(1, 2);

        List<Integer> actual =
                Optimal.awardTopKHotels(
                        positive,
                        negative,
                        hotelIds,
                        reviews,
                        2);

        // Representative example from the problem.
        assert expected.equals(actual);
    }

    private static void testTieBreaking() {

        String positive = "good";

        String negative = "";

        List<Integer> hotelIds =
                Arrays.asList(20, 10);

        List<String> reviews =
                Arrays.asList(
                        "good",
                        "good");

        List<Integer> expected =
                Collections.singletonList(10);

        List<Integer> actual =
                Optimal.awardTopKHotels(
                        positive,
                        negative,
                        hotelIds,
                        reviews,
                        1);

        // Equal scores -> smaller hotel id wins.
        assert expected.equals(actual);
    }

    private static void testCaseInsensitive() {

        String positive = "excellent";

        String negative = "";

        List<Integer> hotelIds =
                Collections.singletonList(5);

        List<String> reviews =
                Collections.singletonList(
                        "EXCELLENT Excellent excellent");

        List<Integer> expected =
                Collections.singletonList(5);

        List<Integer> actual =
                Optimal.awardTopKHotels(
                        positive,
                        negative,
                        hotelIds,
                        reviews,
                        1);

        // Different cases should all match.
        assert expected.equals(actual);
    }

    private static void testDuplicateOccurrences() {

        String positive = "great";

        String negative = "";

        List<Integer> hotelIds =
                Collections.singletonList(7);

        List<String> reviews =
                Collections.singletonList(
                        "great great great");

        Map<Integer, Integer> scores =
                aggregateScores(
                        buildDictionary(positive),
                        buildDictionary(negative),
                        hotelIds,
                        reviews);

        // Three occurrences => 9 points.
        assert scores.get(7) == 9;
    }

    private static void testMultipleReviewsPerHotel() {

        String positive = "clean";

        String negative = "";

        List<Integer> hotelIds =
                Arrays.asList(1, 1, 1);

        List<String> reviews =
                Arrays.asList(
                        "clean",
                        "clean",
                        "clean");

        Map<Integer, Integer> scores =
                aggregateScores(
                        buildDictionary(positive),
                        buildDictionary(negative),
                        hotelIds,
                        reviews);

        // Scores accumulate across reviews.
        assert scores.get(1) == 9;
    }

    private static void testPunctuationHandling() {

        String positive = "location";

        String negative = "";

        List<Integer> hotelIds =
                Collections.singletonList(9);

        List<String> reviews =
                Collections.singletonList(
                        "location, location.");

        Map<Integer, Integer> scores =
                aggregateScores(
                        buildDictionary(positive),
                        buildDictionary(negative),
                        hotelIds,
                        reviews);

        // Trailing punctuation must not affect matching.
        assert scores.get(9) == 6;
    }

    private static void testEmptyKeywordLists() {

        List<Integer> hotelIds =
                Collections.singletonList(1);

        List<String> reviews =
                Collections.singletonList(
                        "anything goes here");

        Map<Integer, Integer> scores =
                aggregateScores(
                        buildDictionary(""),
                        buildDictionary(""),
                        hotelIds,
                        reviews);

        // No keywords => zero score.
        assert scores.get(1) == 0;
    }

    private static void testKGreaterThanHotels() {

        String positive = "good";

        String negative = "";

        List<Integer> hotelIds =
                Arrays.asList(1, 2);

        List<String> reviews =
                Arrays.asList(
                        "good",
                        "good");

        List<Integer> expected =
                Arrays.asList(1, 2);

        List<Integer> actual =
                Optimal.awardTopKHotels(
                        positive,
                        negative,
                        hotelIds,
                        reviews,
                        10);

        // Asking for more than available hotels
        // should simply return all ranked hotels.
        assert expected.equals(actual);
    }
}