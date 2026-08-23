package org.chijai.design.lld;

import java.util.*;

/**
 * LeetCode 1244 - Design A Leaderboard
 *
 * INTERVIEW PATTERN
 * -----------------
 * We need two different capabilities:
 *
 * 1) Fast lookup of a player's current score
 *      playerId -> score
 *      => HashMap
 *
 * 2) Scores kept globally ordered so top(K) is easy
 *      score -> number of players with that score
 *      => TreeMap in descending order
 *
 * Why score -> frequency?
 * Multiple players can have the same score.
 *
 * Core invariant:
 * - playerScore always stores every active player's latest score.
 * - scoreFrequency contains exactly the same active scores,
 *   aggregated by frequency.
 *
 * Complexity:
 * - addScore(): O(log N)
 * - reset():    O(log N)
 * - top(K):     O(D) where D is the number of distinct score levels
 *               visited before collecting K players.
 *               In the common interview shorthand: O(K) when scores are distinct.
 * - Space:      O(N)
 */
public class DesignALeaderboard {

    static class Leaderboard {

        // Direct lookup:
        // playerId -> current total score
        private final Map<Integer, Integer> playerScore = new HashMap<>();

        // Ordered multiset of scores:
        // score -> how many players currently have that score
        //
        // reverseOrder() means iteration starts from the highest score.
        private final TreeMap<Integer, Integer> scoreFrequency =
                new TreeMap<>(Collections.reverseOrder());

        /**
         * Add score to a player.
         *
         * If the player already exists:
         * 1. Remove the old score from the ordered score structure.
         * 2. Compute the new total score.
         * 3. Insert the new score into both structures.
         */
        public void addScore(int playerId, int score) {

            if (playerScore.containsKey(playerId)) {
                int oldScore = playerScore.get(playerId);
                int newScore = oldScore + score;

                removeScoreFromFrequencyMap(oldScore);

                playerScore.put(playerId, newScore);
                scoreFrequency.merge(newScore, 1, Integer::sum);

            } else {
                // First score for this player.
                playerScore.put(playerId, score);
                scoreFrequency.merge(score, 1, Integer::sum);
            }
        }

        /**
         * Return the sum of the top K player scores.
         *
         * TreeMap is already descending:
         *
         * highest score
         *      ↓
         * lower score
         *      ↓
         * ...
         *
         * Frequencies allow us to consume multiple players
         * having the same score in one step.
         */
        public int top(int k) {

            int sum = 0;
            int playersTaken = 0;

            for (Map.Entry<Integer, Integer> entry : scoreFrequency.entrySet()) {

                int score = entry.getKey();
                int frequency = entry.getValue();

                int remainingPlayersNeeded = k - playersTaken;

                // Example:
                // score = 100, frequency = 3, remaining = 2
                // We only take 2 of those 3 players.
                int take = Math.min(frequency, remainingPlayersNeeded);

                sum += score * take;
                playersTaken += take;

                if (playersTaken == k) {
                    break;
                }
            }

            return sum;
        }

        /**
         * Reset a player's score.
         *
         * LeetCode guarantees the player exists when reset() is called.
         */
        public void reset(int playerId) {

            int score = playerScore.remove(playerId);

            removeScoreFromFrequencyMap(score);
        }

        /**
         * Remove exactly one occurrence of a score
         * from the ordered multiset.
         */
        private void removeScoreFromFrequencyMap(int score) {

            int frequency = scoreFrequency.get(score);

            if (frequency == 1) {
                scoreFrequency.remove(score);
            } else {
                scoreFrequency.put(score, frequency - 1);
            }
        }
    }

    // ---------------------------------------------------------------------
    // TEST HARNESS
    // ---------------------------------------------------------------------

    private static void assertEquals(int expected, int actual, String testName) {
        if (expected != actual) {
            throw new AssertionError(
                    testName + " FAILED: expected=" + expected + ", actual=" + actual
            );
        }

        System.out.println("PASS: " + testName + " -> " + actual);
    }

    /**
     * LeetCode-style example:
     *
     * addScore(1,73)
     * addScore(2,56)
     * addScore(3,39)
     * addScore(4,51)
     * addScore(5,4)
     *
     * top(1) = 73
     *
     * reset(1)
     * reset(2)
     *
     * addScore(2,51)
     *
     * top(3) = 141
     */
    private static void testLeetCodeExample() {

        System.out.println("\n=== Test 1: LeetCode Example ===");

        Leaderboard leaderboard = new Leaderboard();

        leaderboard.addScore(1, 73);
        leaderboard.addScore(2, 56);
        leaderboard.addScore(3, 39);
        leaderboard.addScore(4, 51);
        leaderboard.addScore(5, 4);

        assertEquals(
                73,
                leaderboard.top(1),
                "Top 1"
        );

        leaderboard.reset(1);
        leaderboard.reset(2);

        leaderboard.addScore(2, 51);

        assertEquals(
                141,
                leaderboard.top(3),
                "Top 3 after reset/update"
        );
    }

    /**
     * Tests score accumulation for an existing player.
     */
    private static void testScoreAccumulation() {

        System.out.println("\n=== Test 2: Score Accumulation ===");

        Leaderboard leaderboard = new Leaderboard();

        leaderboard.addScore(10, 20);
        leaderboard.addScore(10, 30);

        assertEquals(
                50,
                leaderboard.top(1),
                "Existing player's score accumulates"
        );
    }

    /**
     * Tests duplicate scores.
     *
     * This is exactly why we use:
     *
     *     score -> frequency
     *
     * rather than:
     *
     *     score -> playerId
     */
    private static void testDuplicateScores() {

        System.out.println("\n=== Test 3: Duplicate Scores ===");

        Leaderboard leaderboard = new Leaderboard();

        leaderboard.addScore(1, 100);
        leaderboard.addScore(2, 100);
        leaderboard.addScore(3, 50);

        assertEquals(
                200,
                leaderboard.top(2),
                "Two players with same top score"
        );

        assertEquals(
                250,
                leaderboard.top(3),
                "All duplicate scores counted"
        );
    }

    /**
     * Tests that reset removes exactly one occurrence
     * when two players share the same score.
     */
    private static void testResetWithDuplicateScores() {

        System.out.println("\n=== Test 4: Reset With Duplicate Scores ===");

        Leaderboard leaderboard = new Leaderboard();

        leaderboard.addScore(1, 80);
        leaderboard.addScore(2, 80);
        leaderboard.addScore(3, 60);

        leaderboard.reset(1);

        assertEquals(
                140,
                leaderboard.top(2),
                "Reset removes only one duplicate occurrence"
        );
    }

    /**
     * Tests ranking after one player's score changes
     * enough to become the new leader.
     */
    private static void testRankingAfterUpdate() {

        System.out.println("\n=== Test 5: Ranking After Update ===");

        Leaderboard leaderboard = new Leaderboard();

        leaderboard.addScore(1, 20);
        leaderboard.addScore(2, 50);
        leaderboard.addScore(3, 40);

        assertEquals(
                90,
                leaderboard.top(2),
                "Initial top 2"
        );

        leaderboard.addScore(1, 50); // player 1: 20 -> 70

        assertEquals(
                120,
                leaderboard.top(2),
                "Top 2 after player becomes leader"
        );
    }

    public static void main(String[] args) {

        testLeetCodeExample();
        testScoreAccumulation();
        testDuplicateScores();
        testResetWithDuplicateScores();
        testRankingAfterUpdate();

        System.out.println("\nALL TESTS PASSED");
    }
}
