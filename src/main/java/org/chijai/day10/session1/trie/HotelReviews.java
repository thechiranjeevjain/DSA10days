package org.chijai.day10.session1.trie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 *  HOTEL REVIEWS
 * ============================================================================
 *
 * Difficulty:
 * Medium
 *
 * Primary Tags:
 * Trie, String, Hashing, Stable Sorting
 *
 * ----------------------------------------------------------------------------
 * Problem
 * ----------------------------------------------------------------------------
 *
 * You are given:
 *
 * 1. A string A containing all good words separated by '_'.
 *
 * Example:
 *
 *      "cool_ice_wifi"
 *
 * 2. A list of hotel reviews.
 *
 * Every review is also composed of words separated by '_'.
 *
 * Example:
 *
 *      "water_is_cool"
 *
 * Goodness Value of a review =
 * Number of words inside the review that belong to the good-word dictionary.
 *
 * Return the ORIGINAL INDICES of reviews after sorting by:
 *
 *      Higher Goodness Value first.
 *
 * Stable Sorting:
 *
 * If two reviews have identical goodness values,
 * their original order MUST remain unchanged.
 *
 * ----------------------------------------------------------------------------
 * Constraints
 * ----------------------------------------------------------------------------
 *
 * • Good words are lowercase.
 * • Review words are lowercase.
 * • Duplicate review words count multiple times.
 * • Stable ordering is mandatory.
 *
 * ----------------------------------------------------------------------------
 * Examples
 * ----------------------------------------------------------------------------
 *
 * Good Words
 *
 *      "cool_ice_wifi"
 *
 * Reviews
 *
 *      0 -> "water_is_cool"
 *      1 -> "cold_ice_drink"
 *      2 -> "cool_wifi_speed"
 *
 * Goodness:
 *
 *      Review0 = 1
 *      Review1 = 1
 *      Review2 = 2
 *
 * Stable descending order:
 *
 *      [2,0,1]
 *
 * ----------------------------------------------------------------------------
 * Official Link
 * ----------------------------------------------------------------------------
 *
 * https://www.interviewbit.com/problems/hotel-reviews/
 *
 * ============================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern
 * -------
 * Trie Dictionary Lookup
 *
 * Archetype
 * ---------
 * Build a searchable dictionary once.
 * Query many independent strings efficiently.
 *
 * Core Invariant
 * --------------
 * Every complete good word terminates at exactly one Trie node whose
 * terminal flag is true.
 *
 * Therefore,
 *
 *      search(word)
 *
 * is true
 *
 * iff
 *
 * the traversal finishes exactly on a terminal node.
 *
 * Why it Works
 * ------------
 * Every character chooses exactly one edge.
 *
 * Missing edge
 *      =>
 * impossible word.
 *
 * Existing path but non-terminal node
 *      =>
 * only a prefix.
 *
 * Existing path ending on terminal node
 *      =>
 * complete dictionary word.
 *
 * Recognition Signals
 * -------------------
 * Use Trie whenever:
 *
 * • Many dictionary words
 * • Many membership queries
 * • Prefix traversal is natural
 * • Characters are processed left to right
 *
 * When NOT to Use
 * ---------------
 * If only exact membership is required and interview explicitly allows hashing,
 * HashSet<String> is simpler.
 *
 * InterviewBit intentionally expects Trie for this problem.
 *
 * Comparison
 * ----------
 *
 * HashSet
 * --------
 * Easier implementation
 * O(length) hashing
 *
 * Trie
 * ----
 * Character-by-character deterministic lookup
 * Naturally extends to prefix problems
 * Common interview expectation
 *
 * ============================================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine every good word is inserted into a dictionary tree.
 *
 * Every review is broken into words.
 *
 * Each word independently walks through the tree.
 *
 * Successful walk ending on a terminal node contributes exactly one point.
 *
 * Stable sorting happens AFTER every review has already received its score.
 *
 * ---------------------------------------------------------------------------
 * Invariant 1
 * ---------------------------------------------------------------------------
 *
 * Trie Path Invariant
 *
 * After processing first k characters,
 * current node represents exactly that prefix.
 *
 * ---------------------------------------------------------------------------
 * Invariant 2
 * ---------------------------------------------------------------------------
 *
 * Terminal Invariant
 *
 * isWord == true
 *
 * means
 *
 * this node represents a COMPLETE dictionary word.
 *
 * Not merely a prefix.
 *
 * ---------------------------------------------------------------------------
 * Invariant 3
 * ---------------------------------------------------------------------------
 *
 * Lookup Invariant
 *
 * Missing child immediately proves
 *
 * word ∉ dictionary.
 *
 * No further exploration can recover.
 *
 * ---------------------------------------------------------------------------
 * Invariant 4
 * ---------------------------------------------------------------------------
 *
 * Counting Invariant
 *
 * score(review)
 *
 * equals
 *
 * Σ search(each review word)
 *
 * Every review word is independent.
 *
 * ---------------------------------------------------------------------------
 * Invariant 5
 * ---------------------------------------------------------------------------
 *
 * Stable Sorting Invariant
 *
 * Reviews having equal score
 * preserve original index order.
 *
 * This is achieved by using Java's stable sort.
 *
 * ---------------------------------------------------------------------------
 * Variable Meaning
 * ---------------------------------------------------------------------------
 *
 * root
 *      Trie root
 *
 * node
 *      Current traversal node
 *
 * word
 *      Current token
 *
 * score
 *      Number of successful searches
 *
 * index
 *      Original review position
 *
 * ---------------------------------------------------------------------------
 * Allowed State Transition
 * ---------------------------------------------------------------------------
 *
 * node
 *      ->
 * child(character)
 *
 * if child exists.
 *
 * ---------------------------------------------------------------------------
 * Forbidden Transition
 * ---------------------------------------------------------------------------
 *
 * Continue after missing edge.
 *
 * Once edge is absent,
 * search has already failed.
 *
 * ---------------------------------------------------------------------------
 * Termination
 * ---------------------------------------------------------------------------
 *
 * Search ends after consuming every character.
 *
 * Success requires terminal node.
 *
 * ---------------------------------------------------------------------------
 * Why Naive Solutions Fail
 * ---------------------------------------------------------------------------
 *
 * A naive nested comparison
 *
 * for every review
 *      for every good word
 *          compare strings
 *
 * performs unnecessary repeated work.
 *
 * Trie shares common prefixes once during construction.
 *
 * Membership checking becomes mechanical and scalable.
 *
 * ============================================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================================
 *
 * Mistake 1
 * ---------
 * Returning true after matching prefix.
 *
 * Example
 *
 * Dictionary:
 *
 *      cool
 *
 * Query:
 *
 *      coo
 *
 * Traversal exists,
 * but terminal flag is false.
 *
 * Violated Invariant:
 *
 * Terminal Invariant.
 *
 * ---------------------------------------------------------------------------
 * Mistake 2
 * ---------------------------------------------------------------------------
 *
 * Forgetting stable sorting.
 *
 * Example
 *
 * Scores:
 *
 *      2,2,2
 *
 * Incorrect sorting may shuffle equal reviews.
 *
 * InterviewBit requires original ordering.
 *
 * ---------------------------------------------------------------------------
 * Mistake 3
 * ---------------------------------------------------------------------------
 *
 * Stopping after first matched word.
 *
 * Entire review contributes multiple good words.
 *
 * Count ALL successful searches.
 *
 * ---------------------------------------------------------------------------
 * Mistake 4
 * ---------------------------------------------------------------------------
 *
 * Treating prefixes as words.
 *
 * Dictionary:
 *
 *      wifi
 *
 * Query:
 *
 *      wi
 *
 * Prefix ≠ complete word.
 *
 * ============================================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================================
 *
 * Typing Order
 * ------------
 *
 * 1. TrieNode
 *
 *      children
 *      isWord
 *
 * 2. insert(word)
 *
 * 3. contains(word)
 *
 * 4. score(review)
 *
 *      split review
 *      count successful searches
 *
 * 5. Build Trie
 *
 * 6. Compute (score,index)
 *
 * 7. Stable descending sort
 *
 * 8. Extract indices
 *
 * ============================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================================
 *
 * build trie
 *
 * for review
 *      score review
 *
 * stable sort by score descending
 *
 * output indices
 *
 * ============================================================================
 * 6. SOLUTION CLASSES
 * ============================================================================
 */

/**
 * Public chapter.
 */
public class HotelReviews {

    /**
     * Immutable score/index pair.
     *
     * Java's List.sort is stable, therefore equal scores automatically
     * preserve original ordering.
     */
    private static final class ReviewScore {
        final int index;
        final int score;

        ReviewScore(int index, int score) {
            this.index = index;
            this.score = score;
        }
    }

    /**
     * Trie node.
     *
     * HashMap keeps implementation compact while remaining interview friendly.
     */
    private static final class TrieNode {

        final Map<Character, TrieNode> children = new HashMap<>();

        boolean isWord;

    }

    /**
     * Trie implementation.
     */
    private static final class Trie {

        private final TrieNode root = new TrieNode();

        /**
         * Inserts one dictionary word.
         */
        void insert(String word) {

            TrieNode node = root;

            for (char ch : word.toCharArray()) {

                node = node.children.computeIfAbsent(ch, ignored -> new TrieNode());
            }

            // Invariant: terminal node represents one complete dictionary word.
            node.isWord = true;
        }

        /**
         * Exact dictionary lookup.
         */
        boolean contains(String word) {

            TrieNode node = root;

            for (char ch : word.toCharArray()) {

                node = node.children.get(ch);

                // Invariant: missing edge immediately disproves membership.
                if (node == null) {
                    return false;
                }
            }

            // Prefix alone is insufficient.
            return node.isWord;
        }

        /**
         * Computes the goodness score of one review.
         *
         * Every successfully matched dictionary word contributes one point.
         */
        int goodnessScore(String review) {

            if (review == null || review.isEmpty()) {
                return 0;
            }

            int score = 0;

            String[] words = review.split("_");

            for (String word : words) {

                // Invariant: every review word is evaluated independently.
                if (contains(word)) {
                    score++;
                }
            }

            return score;
        }
    }

    /**
     * =========================================================================
     * Brute Force
     * =========================================================================
     *
     * Idea
     * ----
     * Split the good-word string into an array.
     *
     * For every review word,
     * compare against every dictionary word.
     *
     * Invariant
     * ---------
     * Membership is discovered only after exhausting all dictionary words.
     *
     * Limitation
     * ----------
     * Massive repeated comparisons.
     *
     * Complexity
     * ----------
     *
     * Let
     *
     * G = number of good words
     * R = number of review words
     * L = average word length
     *
     * Time
     *
     *      O(R × G × L)
     *
     * Space
     *
     *      O(G)
     *
     * Interview Usefulness
     * --------------------
     * Good starting point before introducing Trie.
     */
    static final class BruteForce {

        List<Integer> solve(String goodWords, List<String> reviews) {

            String[] dictionary = goodWords.split("_");

            List<ReviewScore> scores = new ArrayList<>();

            for (int reviewIndex = 0; reviewIndex < reviews.size(); reviewIndex++) {

                int score = 0;

                String[] words = reviews.get(reviewIndex).split("_");

                for (String reviewWord : words) {

                    for (String dictionaryWord : dictionary) {

                        if (dictionaryWord.equals(reviewWord)) {
                            score++;
                            break;
                        }
                    }
                }

                scores.add(new ReviewScore(reviewIndex, score));
            }

            // Stable because Java List.sort uses TimSort.
            scores.sort((a, b) -> Integer.compare(b.score, a.score));

            List<Integer> answer = new ArrayList<>(scores.size());

            for (ReviewScore review : scores) {
                answer.add(review.index);
            }

            return answer;
        }
    }

    /**
     * =========================================================================
     * Improved
     * =========================================================================
     *
     * Idea
     * ----
     * Replace repeated linear search by HashSet membership.
     *
     * (InterviewBit still expects Trie, but this demonstrates the progression.)
     *
     * Invariant
     * ---------
     * Every dictionary word exists exactly once inside the hash table.
     *
     * Improvement
     * -----------
     * Membership becomes expected O(1).
     *
     * Complexity
     * ----------
     *
     * Build
     *
     *      O(G)
     *
     * Queries
     *
     *      O(R)
     *
     * Space
     *
     *      O(G)
     *
     * Interview Usefulness
     * --------------------
     * Demonstrates awareness of alternative data structures before explaining
     * why Trie is requested.
     */
    static final class Improved {

        List<Integer> solve(String goodWords, List<String> reviews) {

            java.util.HashSet<String> dictionary = new java.util.HashSet<>();

            for (String word : goodWords.split("_")) {
                dictionary.add(word);
            }

            List<ReviewScore> scores = new ArrayList<>();

            for (int reviewIndex = 0; reviewIndex < reviews.size(); reviewIndex++) {

                int score = 0;

                for (String word : reviews.get(reviewIndex).split("_")) {

                    if (dictionary.contains(word)) {
                        score++;
                    }
                }

                scores.add(new ReviewScore(reviewIndex, score));
            }

            scores.sort((a, b) -> Integer.compare(b.score, a.score));

            List<Integer> answer = new ArrayList<>(scores.size());

            for (ReviewScore review : scores) {
                answer.add(review.index);
            }

            return answer;
        }
    }

    /**
     * =========================================================================
     * Optimal (Interview Preferred)
     * =========================================================================
     *
     * Idea
     * ----
     * Build one Trie from the dictionary.
     *
     * Each review word performs deterministic character-by-character lookup.
     *
     * Pattern
     * -------
     * Trie Dictionary Search
     *
     * Primary Invariant
     * -----------------
     * Every successful lookup finishes on a terminal Trie node.
     *
     * Correctness
     * -----------
     * Every good word contributes exactly one point.
     * Every non-good word contributes zero.
     *
     * Stable ordering is delegated to Java's stable sort.
     *
     * Complexity
     * ----------
     *
     * Build Trie
     *
     *      O(total dictionary characters)
     *
     * Query
     *
     *      O(total review characters)
     *
     * Space
     *
     *      O(total dictionary characters)
     *
     * Interview Usefulness
     * --------------------
     * This is the implementation most interviewers expect for this problem.
     */
    static final class Optimal {

        List<Integer> solve(String goodWords, List<String> reviews) {

            Trie trie = new Trie();

            for (String word : goodWords.split("_")) {

                // Dictionary constructed exactly once.
                trie.insert(word);
            }

            List<ReviewScore> scores = new ArrayList<>(reviews.size());

            for (int reviewIndex = 0; reviewIndex < reviews.size(); reviewIndex++) {

                // Invariant: score depends only on successful dictionary lookups.
                int score = trie.goodnessScore(reviews.get(reviewIndex));

                scores.add(new ReviewScore(reviewIndex, score));
            }

            // Invariant:
            // Equal scores retain original insertion order because TimSort is
            // stable.
            scores.sort((a, b) -> Integer.compare(b.score, a.score));

            List<Integer> answer = new ArrayList<>(scores.size());

            for (ReviewScore review : scores) {

                // Extract only original indices after ordering.
                answer.add(review.index);
            }

            return answer;
        }
    }

/**
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * Explain the solution verbally:
 *
 * "I first build a Trie containing every good word.
 *
 * Every review is split into independent words.
 *
 * Each word walks through the Trie.
 *
 * Missing edge immediately proves the word is absent.
 *
 * Reaching a terminal node proves an exact dictionary match,
 * therefore I increment the score.
 *
 * After every review receives its score,
 * I perform a stable descending sort.
 *
 * Java's List.sort is stable,
 * so equal scores automatically preserve original review order."
 *
 * -------------------------------------------------------------------------
 *
 * Invariant
 *
 * Every successful lookup ends exactly on a terminal Trie node.
 *
 * -------------------------------------------------------------------------
 *
 * Discard Rule
 *
 * Missing child means the remaining characters can never recover the word.
 *
 * Stop immediately.
 *
 * -------------------------------------------------------------------------
 *
 * Correctness
 *
 * Every review word contributes exactly once.
 *
 * Every contribution is independent.
 *
 * Therefore total score equals the number of dictionary words present.
 *
 * -------------------------------------------------------------------------
 *
 * Termination
 *
 * Search consumes one character per iteration.
 *
 * Finite input guarantees completion.
 *
 * -------------------------------------------------------------------------
 *
 * In-place Feasibility
 *
 * No.
 *
 * Trie construction requires additional memory.
 *
 * -------------------------------------------------------------------------
 *
 * Streaming Feasibility
 *
 * Yes.
 *
 * Once the Trie is built,
 * reviews may be processed one by one.
 */

    /**
     * =========================================================================
     * 🎯 INTERVIEW RECALL SHEET
     * =========================================================================
     *
     * Trigger
     * -------
     * Large dictionary.
     * Many independent exact word lookups.
     * Interview explicitly mentions Trie.
     *
     * Pattern
     * -------
     * Trie Dictionary Lookup
     *
     * Search Target
     * -------------
     * Exact word membership.
     *
     * Invariant
     * ---------
     * Every successful lookup finishes on a terminal node.
     *
     * Discard Rule
     * ------------
     * Missing child immediately rejects the word.
     *
     * Common Trap
     * -----------
     * Returning true after matching only a prefix.
     *
     * Edge Cases
     * ----------
     * • Empty review
     * • Empty good-word list
     * • Duplicate good words
     * • Duplicate review words
     * • Equal scores (stable ordering)
     *
     * One-liner
     * ---------
     * Build Trie once, score each review independently, stable sort by score.
     *
     * Re-derivation Cue
     * -----------------
     * Dictionary → Trie
     * Review → Words
     * Word → Search
     * Search → Score
     * Score → Stable Sort
     */

    /**
     * =========================================================================
     * 🔄 VARIATIONS & TWEAKS
     * =========================================================================
     *
     * -------------------------------------------------------------------------
     * Variation 1
     * -------------------------------------------------------------------------
     *
     * HashSet instead of Trie.
     *
     * Reasoning
     * ---------
     * Exact membership only.
     *
     * Pattern Change
     * --------------
     * Trie is unnecessary unless interviewer specifically requests it or future
     * prefix queries are expected.
     *
     * -------------------------------------------------------------------------
     * Variation 2
     * -------------------------------------------------------------------------
     *
     * Prefix Matching
     *
     * Example
     *
     * Dictionary
     *      cool
     *
     * Query
     *      cooler
     *
     * Required Change
     * ---------------
     * Current solution intentionally rejects this.
     *
     * The search invariant changes from:
     *
     *      Exact Match
     *
     * to
     *
     *      Prefix Exists.
     *
     * -------------------------------------------------------------------------
     * Variation 3
     * -------------------------------------------------------------------------
     *
     * Weighted Good Words
     *
     * Example
     *
     * wifi = 5
     * ice = 2
     * cool = 8
     *
     * Modification
     * ------------
     * Store weight inside terminal Trie nodes.
     *
     * Score becomes:
     *
     *      sum(weights)
     *
     * Invariant remains identical.
     *
     * -------------------------------------------------------------------------
     * Variation 4
     * -------------------------------------------------------------------------
     *
     * Case-insensitive Reviews
     *
     * Normalize both dictionary and reviews before insertion/search.
     *
     * Trie logic remains unchanged.
     *
     * -------------------------------------------------------------------------
     * Variation 5
     * -------------------------------------------------------------------------
     *
     * Duplicate Good Words
     *
     * Example
     *
     * cool_cool_wifi
     *
     * Trie naturally merges duplicates.
     *
     * Terminal node simply remains true.
     *
     * -------------------------------------------------------------------------
     * Variation 6
     * -------------------------------------------------------------------------
     *
     * Online Processing
     *
     * Reviews arrive continuously.
     *
     * Build Trie once.
     *
     * Process each review independently.
     *
     * Pattern still works.
     *
     * -------------------------------------------------------------------------
     * Pattern Boundary
     * -------------------------------------------------------------------------
     *
     * Trie is NOT appropriate when:
     *
     * • Dictionary is tiny.
     * • Only one lookup exists.
     * • Character alphabet is extremely sparse and memory dominates.
     * • Approximate/fuzzy matching is required.
     */

    /**
     * =========================================================================
     * 🧠 MASTERY CHECKLIST
     * =========================================================================
     *
     * Can I explain the invariant?
     *
     * YES.
     *
     * Every successful lookup terminates at a terminal node.
     *
     * -------------------------------------------------------------------------
     *
     * Can I identify the search target?
     *
     * YES.
     *
     * Exact dictionary membership.
     *
     * -------------------------------------------------------------------------
     *
     * Can I explain the discard rule?
     *
     * YES.
     *
     * Missing edge immediately rejects the word.
     *
     * -------------------------------------------------------------------------
     *
     * Can I prove termination?
     *
     * YES.
     *
     * Exactly one character is consumed every iteration.
     *
     * -------------------------------------------------------------------------
     *
     * Can I explain why the naive solution fails?
     *
     * YES.
     *
     * It repeatedly compares identical prefixes across many dictionary words.
     *
     * -------------------------------------------------------------------------
     *
     * Can I identify edge cases?
     *
     * YES.
     *
     * Empty dictionary.
     * Empty review.
     * Empty review token.
     * Duplicate review words.
     * Stable ordering.
     *
     * -------------------------------------------------------------------------
     *
     * Can I debug failures?
     *
     * YES.
     *
     * Check:
     *
     * 1. Word splitting.
     * 2. Trie insertion.
     * 3. Terminal flag.
     * 4. Missing edge.
     * 5. Stable ordering.
     *
     * -------------------------------------------------------------------------
     *
     * Am I ready for variants?
     *
     * YES.
     *
     * Prefix queries.
     * Weighted words.
     * Streaming reviews.
     * Case normalization.
     *
     * -------------------------------------------------------------------------
     *
     * Do I know the pattern boundary?
     *
     * YES.
     *
     * Trie solves deterministic character lookup.
     * It is not a universal replacement for hashing.
     */

    /**
     * =========================================================================
     * Debug Helpers
     * =========================================================================
     */

    private static List<String> tokenize(String input) {

        if (input == null || input.isEmpty()) {
            return List.of();
        }

        return Arrays.asList(input.split("_"));
    }

    private static int manualScore(List<String> dictionary, String review) {

        int score = 0;

        for (String word : tokenize(review)) {

            if (dictionary.contains(word)) {
                score++;
            }
        }

        return score;
    }

    /**
     * =========================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * =========================================================================
     *
     * Run with assertions enabled:
     *
     *      java -ea HotelReviews
     */
    public static void main(String[] args) {

        Optimal solver = new Optimal();

        /*
         * Happy path from the problem statement.
         */
        {
            String goodWords = "cool_ice_wifi";

            List<String> reviews = Arrays.asList(
                    "water_is_cool",
                    "cold_ice_drink",
                    "cool_wifi_speed"
            );

            List<Integer> expected = Arrays.asList(2, 0, 1);

            assert solver.solve(goodWords, reviews).equals(expected)
                    : "Representative example failed.";
        }

        /*
         * Stable ordering:
         * Equal scores must preserve original indices.
         */
        {
            String goodWords = "good";

            List<String> reviews = Arrays.asList(
                    "good",
                    "good",
                    "good"
            );

            List<Integer> expected = Arrays.asList(0, 1, 2);

            assert solver.solve(goodWords, reviews).equals(expected)
                    : "Stable ordering violated.";
        }

        /*
         * No review contains a good word.
         */
        {
            String goodWords = "wifi";

            List<String> reviews = Arrays.asList(
                    "ice",
                    "cool",
                    "water"
            );

            List<Integer> expected = Arrays.asList(0, 1, 2);

            assert solver.solve(goodWords, reviews).equals(expected)
                    : "Zero-score ordering incorrect.";
        }

        /*
         * Duplicate review words each contribute independently.
         */
        {
            String goodWords = "cool";

            List<String> reviews = Arrays.asList(
                    "cool_cool",
                    "cool",
                    "water"
            );

            List<Integer> expected = Arrays.asList(0, 1, 2);

            assert solver.solve(goodWords, reviews).equals(expected)
                    : "Duplicate review words should all count.";
        }

        /*
         * Prefix must NOT count.
         */
        {
            Trie trie = new Trie();

            trie.insert("wifi");

            assert !trie.contains("wi")
                    : "Prefix incorrectly accepted.";

            assert trie.contains("wifi")
                    : "Exact word rejected.";

            assert !trie.contains("wifis")
                    : "Extended word incorrectly accepted.";
        }

        /*
         * Empty review.
         */
        {
            Trie trie = new Trie();

            trie.insert("cool");

            assert trie.goodnessScore("") == 0
                    : "Empty review should score zero.";
        }

        /*
         * Empty dictionary.
         */
        {
            Optimal emptyDictionarySolver = new Optimal();

            List<Integer> expected = Arrays.asList(0, 1);

            List<Integer> actual = emptyDictionarySolver.solve(
                    "",
                    Arrays.asList("cool", "wifi")
            );

            assert actual.equals(expected)
                    : "Empty dictionary should produce zero scores.";
        }

        /*
         * Manual verification against simple counting.
         */
        {
            List<String> dictionary = Arrays.asList("a", "b", "c");

            Trie trie = new Trie();

            for (String word : dictionary) {
                trie.insert(word);
            }

            String review = "a_d_x_b";

            assert trie.goodnessScore(review) == manualScore(dictionary, review)
                    : "Trie score differs from manual score.";
        }

        /*
         * Multiple different scores.
         */
        {
            String goodWords = "a_b_c";

            List<String> reviews = Arrays.asList(
                    "a",
                    "a_b",
                    "a_b_c",
                    "x"
            );

            List<Integer> expected = Arrays.asList(2, 1, 0, 3);

            assert solver.solve(goodWords, reviews).equals(expected)
                    : "Descending score ordering incorrect.";
        }

        /*
         * Single review.
         */
        {
            String goodWords = "java";

            List<Integer> expected = List.of(0);

            assert solver.solve(
                    goodWords,
                    List.of("java")
            ).equals(expected)
                    : "Single review failed.";
        }

        /*
         * Repeated insertion should remain valid.
         */
        {
            Trie trie = new Trie();

            trie.insert("cool");
            trie.insert("cool");
            trie.insert("cool");

            assert trie.contains("cool")
                    : "Repeated insertion corrupted Trie.";
        }

        /*
         * Character mismatch should fail immediately.
         */
        {
            Trie trie = new Trie();

            trie.insert("abc");

            assert !trie.contains("abd")
                    : "Character mismatch not detected.";
        }

        System.out.println("All assertions passed.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/
