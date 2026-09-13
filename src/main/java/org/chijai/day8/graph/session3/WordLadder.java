package org.chijai.day8.graph.session3;

import java.util.*;

/**
 * Word Ladder
 *
 * LeetCode:
 * https://leetcode.com/problems/word-ladder/
 *
 * Difficulty:
 * Hard
 *
 * Java Gold V18
 *
 * Primary learning goal:
 *
 * Decompose an unfamiliar-looking problem into familiar reusable pieces:
 *
 * orchestration
 * +
 * problem-specific getNeighbors()
 * +
 * reusable graph materialization
 * +
 * reusable BFS
 */
public class WordLadder {

    /*
     * =============================================================================
     * 1. 📘 PROBLEM STATEMENT
     * =============================================================================
     *
     * A transformation sequence from beginWord to endWord is:
     *
     * beginWord -> s1 -> s2 -> ... -> sk
     *
     * such that:
     *
     * 1. Every adjacent pair differs by exactly one character.
     * 2. Every transformed word must belong to wordList.
     * 3. beginWord does not have to exist in wordList.
     * 4. endWord must be reached.
     *
     * Return the number of WORDS in the shortest transformation sequence.
     *
     * If no valid transformation exists, return 0.
     *
     * Example:
     *
     * beginWord = "hit"
     * endWord   = "cog"
     *
     * wordList =
     * ["hot", "dot", "dog", "lot", "log", "cog"]
     *
     * hit -> hot -> dot -> dog -> cog
     *
     * Answer = 5
     */


    /*
     * =============================================================================
     * 2. 🧠 MASTER REUSABLE MAP — MEMORIZE THIS ONCE
     * =============================================================================
     *
     *        SHORTEST NUMBER OF MOVES
     *                 +
     *        EVERY MOVE COSTS THE SAME
     *                 ↓
     *                BFS
     *
     *
     * CLEAN ARCHITECTURE
     * ==================
     *
     * EACH PROBLEM STANDS ALONE:
     *
     * public problem method
     *          ↓
     * buildGraph()
     *          ↓
     * getNeighbors()
     *          ↓
     * bfs()
     *
     *
     * WHAT CHANGES?
     * =============
     *
     * Word Ladder
     * ->
     * getNeighbors = mutate one letter
     *
     * Genetic Mutation
     * ->
     * getNeighbors = mutate one gene
     *
     * Open Lock
     * ->
     * getNeighbors = rotate one wheel
     * ->
     * generate lazily during BFS
     *
     *
     * WHAT STAYS CONCEPTUALLY?
     * =========================
     *
     * buildGraph()
     * bfs()
     * visited
     * queue
     * levelSize
     *
     * The COUNTER keeps its domain meaning:
     *
     * Word Ladder -> sequenceLength
     * Genetic Mutation -> mutations
     * Open Lock -> turns
     *
     *
     * IMPORTANT:
     *
     * We intentionally REPEAT that boilerplate inside each standalone delta
     * problem instead of extracting a generic GraphBfs framework.
     *
     * Why?
     *
     * Each delta should be copy-pastable, independently reconstructable, and
     * visually comparable beside the primary solution.
     *
     *
     * MEMORY:
     *
     * ACTUAL PROBLEM LOGIC
     * =
     * getNeighbors()
     *
     * Familiar boilerplate repeats around it.
     */


    /*
     * =============================================================================
     * 3. ⭐ PRIMARY — WORD LADDER
     * =============================================================================
     *
     * Pattern:
     * Graph Materialization + BFS
     *
     * Time:
     * O(N * L²)
     *
     * Space:
     * O(N * L + E * L) conservatively for stored generated String neighbors
     *
     * N = number of words
     * L = word length
     * E = number of directed adjacency entries
     *
     *
     * SIX-MONTH RECONSTRUCTION:
     *
     * Word Ladder
     * ->
     * define legal neighboring words
     * ->
     * build normal graph
     * ->
     * run normal BFS
     */

    static class Optimal {

        private static final String CHOICES =
                "abcdefghijklmnopqrstuvwxyz";


        public int ladderLength(String beginWord,
                                String endWord,
                                List<String> wordList) {

            Set<String> validWords =
                    new HashSet<>(wordList);

            if (!validWords.contains(endWord)) {
                return 0;
            }

            validWords.add(beginWord);

            Map<String, List<String>> graph =
                    buildGraph(validWords);

            return bfs(
                    beginWord,
                    endWord,
                    graph
            );
        }


        /*
         * FAMILIAR GRAPH-MATERIALIZATION BOILERPLATE
         *
         * buildGraph() does NOT care which replacement characters are legal.
         *
         * Its responsibility is only:
         *
         * current
         * ->
         * getNeighbors(current)
         * ->
         * adjacency list
         */
        private Map<String, List<String>> buildGraph(
                Set<String> validValues) {

            Map<String, List<String>> graph =
                    new HashMap<>();

            for (String current : validValues) {

                graph.put(
                        current,
                        getNeighbors(
                                current,
                                validValues
                        )
                );
            }

            return graph;
        }


        /*
         * ACTUAL TRANSITION ENGINE
         *
         * SAVE
         * ->
         * CHOOSE
         * ->
         * EXPLORE
         * ->
         * RESTORE
         *
         *
         * WORD-LADDER POLICY:
         *
         * CHOICES =
         * "abcdefghijklmnopqrstuvwxyz"
         */
        private List<String> getNeighbors(
                String current,
                Set<String> validValues) {

            List<String> neighbors =
                    new ArrayList<>();

            char[] characters =
                    current.toCharArray();

            for (int position = 0;
                 position < characters.length;
                 position++) {

                char original =
                        characters[position];

                for (int i = 0;
                     i < CHOICES.length();
                     i++) {

                    char choice =
                            CHOICES.charAt(i);

                    if (choice == original) {
                        continue;
                    }

                    // CHOOSE
                    characters[position] =
                            choice;

                    // EXPLORE
                    String candidate =
                            new String(characters);

                    if (validValues.contains(candidate)) {
                        neighbors.add(candidate);
                    }

                    // RESTORE
                    characters[position] =
                            original;
                }
            }

            return neighbors;
        }


        /*
         * WORD-LADDER BFS
         *
         * sequenceLength
         * =
         * number of WORDS in the transformation sequence
         */
        private int bfs(
                String beginWord,
                String endWord,
                Map<String, List<String>> graph) {

            Queue<String> queue =
                    new ArrayDeque<>();

            Set<String> visited =
                    new HashSet<>();

            queue.offer(beginWord);
            visited.add(beginWord);

            int sequenceLength = 1;

            while (!queue.isEmpty()) {

                int levelSize =
                        queue.size();

                for (int i = 0;
                     i < levelSize;
                     i++) {

                    String current =
                            queue.poll();

                    for (String neighbor :
                            graph.get(current)) {

                        if (neighbor.equals(endWord)) {
                            return sequenceLength + 1;
                        }

                        if (visited.add(neighbor)) {
                            queue.offer(neighbor);
                        }
                    }
                }

                sequenceLength++;
            }

            return 0;
        }
    }


    /*
     * =============================================================================
     * 4. 🔄 DELTA 1 — MINIMUM GENETIC MUTATION
     * =============================================================================
     *
     * LeetCode 433
     *
     * SAME ARCHITECTURE:
     *
     * minMutation()
     * ->
     * getNeighbors()
     * ->
     * GraphBfs.buildGraph()
     * ->
     * GraphBfs.shortestDistance()
     *
     *
     * ONLY IMPORTANT DELTA:
     *
     * Word Ladder alphabet:
     *
     * a..z
     *
     * becomes:
     *
     * A / C / G / T
     *
     *
     * State:
     * gene string
     *
     * Valid:
     * gene exists in bank
     *
     * Target:
     * endGene
     */

    static class MinimumGeneticMutation {

        private static final String CHOICES =
                "ACGT";


        public int minMutation(String startGene,
                               String endGene,
                               String[] bank) {

            Set<String> validGenes =
                    new HashSet<>(
                            Arrays.asList(bank)
                    );

            if (!validGenes.contains(endGene)) {
                return -1;
            }

            validGenes.add(startGene);

            Map<String, List<String>> graph =
                    buildGraph(validGenes);

            return bfs(
                    startGene,
                    endGene,
                    graph
            );
        }


        /*
         * SAME GRAPH-MATERIALIZATION SHAPE AS WORD LADDER.
         *
         * No transition-policy parameter is passed through this method.
         */
        private Map<String, List<String>> buildGraph(
                Set<String> validValues) {

            Map<String, List<String>> graph =
                    new HashMap<>();

            for (String current : validValues) {

                graph.put(
                        current,
                        getNeighbors(
                                current,
                                validValues
                        )
                );
            }

            return graph;
        }


        /*
         * SAME getNeighbors() ENGINE AS WORD LADDER.
         *
         * GENETIC-MUTATION POLICY:
         *
         * CHOICES =
         * "ACGT"
         */
        private List<String> getNeighbors(
                String current,
                Set<String> validValues) {

            List<String> neighbors =
                    new ArrayList<>();

            char[] characters =
                    current.toCharArray();

            for (int position = 0;
                 position < characters.length;
                 position++) {

                char original =
                        characters[position];

                for (int i = 0;
                     i < CHOICES.length();
                     i++) {

                    char choice =
                            CHOICES.charAt(i);

                    if (choice == original) {
                        continue;
                    }

                    // CHOOSE
                    characters[position] =
                            choice;

                    // EXPLORE
                    String candidate =
                            new String(characters);

                    if (validValues.contains(candidate)) {
                        neighbors.add(candidate);
                    }

                    // RESTORE
                    characters[position] =
                            original;
                }
            }

            return neighbors;
        }


        /*
         * GENETIC-MUTATION BFS
         *
         * mutations
         * =
         * number of mutation edges taken
         */
        private int bfs(
                String startGene,
                String endGene,
                Map<String, List<String>> graph) {

            Queue<String> queue =
                    new ArrayDeque<>();

            Set<String> visited =
                    new HashSet<>();

            queue.offer(startGene);
            visited.add(startGene);

            int mutations = 0;

            while (!queue.isEmpty()) {

                int levelSize =
                        queue.size();

                for (int i = 0;
                     i < levelSize;
                     i++) {

                    String current =
                            queue.poll();

                    if (current.equals(endGene)) {
                        return mutations;
                    }

                    for (String neighbor :
                            graph.get(current)) {

                        if (visited.add(neighbor)) {
                            queue.offer(neighbor);
                        }
                    }
                }

                mutations++;
            }

            return -1;
        }
    }


    /*
     * =============================================================================
     * 5. 🔄 DELTA 2 — OPEN THE LOCK
     * =============================================================================
     *
     * LeetCode 752
     *
     * SAME BFS FAMILY, DIFFERENT GRAPH EXPOSURE:
     *
     * openLock()
     * ->
     * bfs()
     * ->
     * getNeighbors(current)
     *
     *
     * No buildGraph() here.
     *
     * Open Lock's neighbors are cheap and naturally generated on demand.
     *
     *
     * ONLY IMPORTANT TRANSITION DELTA:
     *
     * instead of mutating letters:
     *
     * rotate one wheel
     *
     * forward
     * or
     * backward
     *
     *
     * State:
     * 4-digit lock string
     *
     * Valid:
     * not a deadend
     *
     * Target:
     * requested lock state
     */

    static class OpenTheLock {

        private static final String START =
                "0000";


        public int openLock(String[] deadends,
                            String target) {

            Set<String> blocked =
                    new HashSet<>(
                            Arrays.asList(deadends)
                    );

            if (blocked.contains(START)) {
                return -1;
            }

            return bfs(
                    START,
                    target,
                    blocked
            );
        }


        /*
         * OPEN-LOCK-SPECIFIC TRANSITION LOGIC
         *
         * For every wheel:
         *
         * SAVE
         * ->
         * rotate forward
         * ->
         * EXPLORE
         * ->
         * RESTORE
         *
         * then:
         *
         * rotate backward
         * ->
         * EXPLORE
         * ->
         * RESTORE
         *
         *
         * Unlike Word Ladder / Genetic Mutation:
         *
         * there is no supplied dictionary of all legal states that we need to
         * materialize first.
         *
         * The next states are cheap to generate directly from the current lock.
         */
        private List<String> getNeighbors(
                String current) {

            List<String> neighbors =
                    new ArrayList<>();

            char[] digits =
                    current.toCharArray();

            for (int position = 0;
                 position < digits.length;
                 position++) {

                char original =
                        digits[position];


                /*
                 * CHOOSE FORWARD
                 */
                digits[position] =
                        original == '9'
                                ? '0'
                                : (char) (original + 1);

                /*
                 * EXPLORE
                 */
                neighbors.add(
                        new String(digits)
                );

                /*
                 * RESTORE
                 */
                digits[position] =
                        original;


                /*
                 * CHOOSE BACKWARD
                 */
                digits[position] =
                        original == '0'
                                ? '9'
                                : (char) (original - 1);

                /*
                 * EXPLORE
                 */
                neighbors.add(
                        new String(digits)
                );

                /*
                 * RESTORE
                 */
                digits[position] =
                        original;
            }

            return neighbors;
        }


        /*
         * LAZY IMPLICIT-GRAPH BFS
         *
         * We do NOT build:
         *
         * 0000 ... 9999
         *
         * and we do NOT precompute every adjacency list.
         *
         * Instead:
         *
         * BFS reaches current
         * ->
         * getNeighbors(current)
         * ->
         * inspect only those 8 next lock states
         *
         *
         * turns
         * =
         * number of wheel turns
         */
        private int bfs(
                String start,
                String target,
                Set<String> blocked) {

            Queue<String> queue =
                    new ArrayDeque<>();

            Set<String> visited =
                    new HashSet<>();

            queue.offer(start);
            visited.add(start);

            int turns = 0;

            while (!queue.isEmpty()) {

                int levelSize =
                        queue.size();

                for (int i = 0;
                     i < levelSize;
                     i++) {

                    String current =
                            queue.poll();

                    if (current.equals(target)) {
                        return turns;
                    }

                    for (String neighbor :
                            getNeighbors(current)) {

                        if (!blocked.contains(neighbor)
                                && visited.add(neighbor)) {

                            queue.offer(neighbor);
                        }
                    }
                }

                turns++;
            }

            return -1;
        }
    }


    /*
     * =============================================================================
     * 6. 👀 READ THESE THREE SOLUTIONS SIDE BY SIDE
     * =============================================================================
     *
     * WORD LADDER
     * ===========
     *
     * choices =
     * "abcdefghijklmnopqrstuvwxyz"
     *
     *
     * GENETIC MUTATION
     * ================
     *
     * choices =
     * "ACGT"
     *
     *
     * EVERYTHING BELOW THAT LINE IS INTENTIONALLY THE SAME SHAPE:
     *
     * buildGraph(validValues)
     *
     * getNeighbors(current, validValues)
     *
     * bfs(...)
     *
     *
     * CHOICES belongs only to getNeighbors().
     *
     * buildGraph() should not receive transition-policy details it does not use.
     *
     *
     * OPEN LOCK
     * =========
     *
     * getNeighbors(current)
     *
     * position
     * ×
     * forward/backward rotation
     *
     * blocked / visited filtering happens inside BFS.
     *
     *
     * EACH CLASS STILL CONTAINS ITS OWN:
     *
     * buildGraph()
     * bfs()
     *
     * so every delta is standalone and copy-pastable.
     *
     *
     * WHAT SHOULD YOUR EYE NOTICE?
     *
     * Word Ladder and Genetic Mutation are nearly the SAME standalone solution.
     *
     * buildGraph() is the same shape.
     * getNeighbors() is the same shape.
     * bfs() is the same shape.
     *
     * The main transition delta is:
     *
     * choices.
     *
     *
     * Open Lock keeps the same higher-level mutation idea:
     *
     * SAVE -> CHOOSE -> EXPLORE -> RESTORE
     *
     * but it does NOT force the same buildGraph() structure.
     *
     * Its state space is naturally implicit and cheap to expand:
     *
     * current lock
     * ->
     * 8 neighbors
     *
     * so it uses lazy getNeighbors() directly inside BFS.
     *
     *
     * THAT IS THE LEARNING GOAL:
     *
     * NEW PROBLEM
     *      ↓
     * RECOGNIZE FAMILIAR BOILERPLATE
     *      ↓
     * IDENTIFY THE NEW getNeighbors()
     *      ↓
     * RECONSTRUCT THE WHOLE STANDALONE SOLUTION.
     */


    /*
     * =============================================================================
     * 7. 🌳 CODE ↔ BFS TREE VISUAL MAPPING
     * =============================================================================
     *
     * Word Ladder graph:
     *
     *                           hit
     *                            |
     *                           hot
     *                         /     \
     *                       dot     lot
     *                        |       |
     *                       dog     log
     *                         \     /
     *                           cog
     *
     *
     * levelSize = queue.size()
     * ->
     * freeze one horizontal tree row
     *
     *
     * queue.poll()
     * ->
     * move horizontally across that row
     *
     *
     * graph.get(current)
     * ->
     * look downward at prebuilt children
     *
     *
     * queue.offer(neighbor)
     * ->
     * build the next row
     *
     *
     * moves++
     * ->
     * move the horizontal ruler down one row
     *
     *
     * Example:
     *
     * current queue:
     *
     * [dot, lot]
     *
     * levelSize = 2
     *
     * poll dot
     * ->
     * enqueue dog
     *
     * queue:
     *
     * [lot, dog]
     *
     * lot
     * =
     * still current row
     *
     * dog
     * =
     * already next row
     *
     * levelSize keeps those rows separate.
     */


    /*
     * =============================================================================
     * 8. 🟢 FIRST-PRINCIPLES INVENTION PATH
     * =============================================================================
     *
     * Need:
     *
     * minimum transformations
     *
     * Every transformation:
     *
     * costs exactly one
     *
     * Therefore:
     *
     * shortest equal-cost path
     * ->
     * BFS
     *
     *
     * But input does not explicitly give edges.
     *
     * So ask:
     *
     * "For one state, how do I derive its legal neighbors?"
     *
     * Word Ladder:
     *
     * mutate one character
     * +
     * dictionary membership
     *
     *
     * Once that transition rule exists:
     *
     * states + getNeighbors()
     * ->
     * buildGraph()
     * ->
     * ordinary BFS
     *
     *
     * This is the reconstruction-first decomposition.
     */


    /*
     * =============================================================================
     * 9. 🧠 EXPLICIT vs IMPLICIT GRAPH DECISION RULE
     * =============================================================================
     *
     * EXPLICIT EDGES GIVEN
     * ====================
     *
     * Example:
     * Course Schedule
     *
     * prerequisites already describe edges
     *
     * ->
     * directly build adjacency list
     *
     *
     * IMPLICIT EDGES DEFINED BY RULE
     * ==============================
     *
     * Example:
     * Word Ladder
     *
     * words are given
     * edge rule is:
     *
     * "differ by exactly one character"
     *
     * ->
     * derive neighbors
     *
     *
     * Then choose:
     *
     * LAZY
     * ->
     * derive neighbors during traversal
     *
     * or
     *
     * MATERIALIZED
     * ->
     * derive all neighbors first
     * then run ordinary graph traversal
     *
     *
     * CURRENT FILE:
     *
     * Word Ladder / Genetic Mutation
     * ->
     * materialized graph for reconstruction familiarity
     *
     * Open Lock
     * ->
     * lazy implicit graph because each current state has only 8 immediately
     * derivable neighbors and prebuilding all 10,000 lock states adds clutter
     *
     *
     * Java Gold preference:
     *
     * If required time complexity remains acceptable and graph materialization
     * makes the solution dramatically easier to reconstruct using familiar
     * templates, it is a valid primary choice.
     *
     * Document the memory trade-off.
     */


    /*
     * =============================================================================
     * 10. 🔁 MUTATION ↔ BACKTRACKING CONNECTION
     * =============================================================================
     *
     * getNeighbors() is not itself a full recursive backtracking algorithm.
     *
     * But it uses the same reusable state-restoration micro-pattern:
     *
     * SAVE
     *   ↓
     * CHOOSE
     *   ↓
     * EXPLORE
     *   ↓
     * RESTORE
     *
     *
     * Word Ladder:
     *
     * save original char
     * ->
     * replace char
     * ->
     * inspect candidate
     * ->
     * restore original char
     *
     *
     * Word Search:
     *
     * save board cell
     * ->
     * mark used
     * ->
     * recurse
     * ->
     * restore board cell
     *
     *
     * Permutations:
     *
     * add choice
     * ->
     * recurse
     * ->
     * remove choice
     *
     *
     * Reusable rule:
     *
     * If shared mutable state is temporarily changed for one sibling choice,
     * restore it before exploring the next sibling.
     */


    /*
     * =============================================================================
     * 11. 🔀 WORD LADDER vs WORD SEARCH
     * =============================================================================
     *
     * Both belong to:
     *
     * STATE-SPACE SEARCH
     *
     *
     * WORD LADDER
     * ===========
     *
     * State:
     * complete word
     *
     * Neighbor:
     * one-character mutation
     *
     * Objective:
     * shortest equal-cost path
     *
     * Engine:
     * BFS
     *
     * Visited:
     * global
     *
     *
     * WORD SEARCH
     * ===========
     *
     * State:
     * row + col + word index
     *
     * Neighbor:
     * adjacent board cell
     *
     * Objective:
     * does one valid path exist?
     *
     * Engine:
     * DFS / backtracking
     *
     * Visited:
     * path-local
     *
     *
     * DECISION:
     *
     * Need shortest equal-cost path?
     * ->
     * BFS
     *
     * Need to explore one path deeply and undo choices?
     * ->
     * DFS / backtracking
     */


    /*
     * =============================================================================
     * 12. ⏱ PRIMARY COMPLEXITY — DERIVATION
     * =============================================================================
     *
     * Let:
     *
     * N = number of words
     * L = word length
     * E = number of directed adjacency entries
     *
     *
     * -------------------------------------------------------------------------
     * Word Ladder graph construction
     * -------------------------------------------------------------------------
     *
     * For each of N words:
     *
     * L positions
     * ×
     * 26 replacement letters
     *
     * Each:
     *
     * new String(letters)
     *
     * materializes O(L) characters.
     *
     * Therefore:
     *
     * O(N * L * 26 * L)
     *
     * 26 is constant:
     *
     * O(N * L²)
     *
     *
     * -------------------------------------------------------------------------
     * BFS
     * -------------------------------------------------------------------------
     *
     * Each node:
     * processed at most once
     *
     * Each stored edge:
     * scanned at most once
     *
     * O(N + E)
     *
     *
     * A lowercase word can have at most:
     *
     * 25 * L
     *
     * one-character neighbors.
     *
     * Therefore:
     *
     * E = O(N * L)
     *
     *
     * Overall:
     *
     * O(N * L² + N + E)
     *
     * =
     *
     * O(N * L²)
     *
     *
     * Space:
     *
     * valid word storage
     * +
     * adjacency lists
     * +
     * generated neighbor strings
     *
     * conservatively:
     *
     * O(N * L + E * L)
     *
     *
     * TRADE-OFF:
     *
     * Lazy neighbor generation uses less graph memory.
     *
     * This primary chooses graph materialization because it makes:
     *
     * buildGraph()
     * +
     * bfs()
     *
     * completely familiar reusable templates.
     */


    /*
     * =============================================================================
     * 13. 🔁 DISTINCT ALTERNATIVE — WILDCARD INDEX
     * =============================================================================
     *
     * Another genuinely different way to expose adjacency:
     *
     * hot
     * ->
     * *ot
     * h*t
     * ho*
     *
     * dot
     * ->
     * *ot
     * d*t
     * do*
     *
     *
     * Shared:
     *
     * *ot
     *
     * means:
     *
     * hot and dot are neighbors.
     *
     *
     * Reusable idea:
     *
     * Instead of directly materializing:
     *
     * node -> neighbors
     *
     * build an INDEX that groups compatible states:
     *
     * pattern -> states
     *
     *
     * Keep as alternative because it teaches indexing/preprocessing,
     * not because primary BFS needs it.
     */


    /*
     * =============================================================================
     * 14. 🎯 30-SECOND RECALL
     * =============================================================================
     *
     * Trigger
     * ->
     * shortest moves + equal move cost
     *
     * Engine
     * ->
     * BFS
     *
     * Architecture
     * ->
     * getNeighbors()
     * -> buildGraph()
     * -> BFS
     *
     * Actual Word Ladder delta
     * ->
     * choices = "abcdefghijklmnopqrstuvwxyz"
     *
     * Mutation template
     * ->
     * SAVE → CHOOSE → EXPLORE → RESTORE
     *
     * Time
     * ->
     * O(N * L²)
     *
     * Main trade-off
     * ->
     * materialize when it simplifies reconstruction;
     * stay lazy when materialization adds unnecessary work/code
     *
     * Biggest learning
     * ->
     * keep the engine boring;
     * push variability into getNeighbors()
     */


    /*
     * =============================================================================
     * 15. 🧠 MASTERY CHECKLIST
     * =============================================================================
     *
     * [ ] Can I derive BFS from shortest + equal-cost moves?
     *
     * [ ] Can I distinguish explicit and implicit graphs?
     *
     * [ ] Can I write a standalone buildGraph() from memory?
     *
     * [ ] Can I write a standalone bfs() from memory?
     *
     * [ ] Can I write the generic-named getNeighbors() template from memory?
     *
     * [ ] Can I see that Genetic Mutation mainly changes the CHOICES constant?
     *
     * [ ] Can I explain why Open Lock stays lazy instead of building all states?
     *
     * [ ] Can I explain SAVE → CHOOSE → EXPLORE → RESTORE?
     *
     * [ ] Can I map levelSize directly to one BFS-tree row?
     *
     * [ ] Can I derive O(N * L²)?
     *
     * [ ] Can I explain the graph-materialization memory trade-off?
     */


    /*
     * =============================================================================
     * 16. 🧪 SELF-VERIFYING TESTS
     * =============================================================================
     */

    public static void main(String[] args) {

        Optimal wordLadder =
                new Optimal();

        assert wordLadder.ladderLength(
                "hit",
                "cog",
                Arrays.asList(
                        "hot",
                        "dot",
                        "dog",
                        "lot",
                        "log",
                        "cog"
                )
        ) == 5;

        assert wordLadder.ladderLength(
                "hit",
                "cog",
                Arrays.asList(
                        "hot",
                        "dot",
                        "dog",
                        "lot",
                        "log"
                )
        ) == 0;

        assert wordLadder.ladderLength(
                "a",
                "c",
                Arrays.asList(
                        "a",
                        "b",
                        "c"
                )
        ) == 2;


        MinimumGeneticMutation mutation =
                new MinimumGeneticMutation();

        assert mutation.minMutation(
                "AACCGGTT",
                "AACCGGTA",
                new String[]{
                        "AACCGGTA"
                }
        ) == 1;

        assert mutation.minMutation(
                "AACCGGTT",
                "AAACGGTA",
                new String[]{
                        "AACCGGTA",
                        "AACCGCTA",
                        "AAACGGTA"
                }
        ) == 2;


        OpenTheLock lock =
                new OpenTheLock();

        assert lock.openLock(
                new String[]{
                        "0201",
                        "0101",
                        "0102",
                        "1212",
                        "2002"
                },
                "0202"
        ) == 6;

        assert lock.openLock(
                new String[]{
                        "8888"
                },
                "0009"
        ) == 1;

        assert lock.openLock(
                new String[]{
                        "0000"
                },
                "8888"
        ) == -1;


        System.out.println(
                "All assertions passed."
        );
    }
}
