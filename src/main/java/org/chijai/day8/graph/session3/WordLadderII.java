package org.chijai.day8.graph.session3;

import java.util.*;

/**
 * =============================================================================
 * Word Ladder II — Java Gold
 * =============================================================================
 *
 * LeetCode 126
 *
 * Core classification:
 *
 *     Graph / State-Space Search
 *     Shortest Unweighted Paths
 *     BFS + Backtracking
 *     Multiple Parents / Shortest-Path DAG
 *
 * Big idea:
 *
 *     Word Ladder I
 *     -> shortest LENGTH
 *     -> BFS is enough
 *
 *     Word Ladder II
 *     -> ALL shortest SEQUENCES
 *     -> BFS discovers shortest-path structure
 *     -> Backtracking reconstructs every shortest path
 */
public class WordLadderII {

    /*
     * =============================================================================
     * 1. PROBLEM STATEMENT
     * =============================================================================
     *
     * A transformation sequence from beginWord to endWord is:
     *
     *     beginWord -> s1 -> s2 -> ... -> sk
     *
     * such that:
     *
     * 1. Every adjacent pair differs by exactly one character.
     * 2. Every transformed word must belong to wordList.
     * 3. beginWord does not have to exist in wordList.
     * 4. endWord must be reached.
     *
     * Return ALL transformation sequences having the minimum possible length.
     *
     * If no transformation sequence exists, return an empty list.
     *
     * Example:
     *
     *     beginWord = "hit"
     *     endWord   = "cog"
     *
     *     wordList =
     *     ["hot", "dot", "dog", "lot", "log", "cog"]
     *
     * Shortest answers:
     *
     *     hit -> hot -> dot -> dog -> cog
     *
     *     hit -> hot -> lot -> log -> cog
     *
     * Both contain 5 words.
     *
     * A longer valid sequence must NOT be returned.
     */


    /*
     * =============================================================================
     * 2. FIRST-PRINCIPLES INVENTION PATH
     * =============================================================================
     *
     * STEP 1 — Recognize the graph.
     *
     *     word = node
     *
     *     edge = two valid words differ by exactly one character
     *
     * Every edge costs exactly one transformation.
     *
     *
     * STEP 2 — The word SHORTEST forces BFS.
     *
     *     shortest path
     *     +
     *     equal edge cost
     *     ->
     *     BFS
     *
     *
     * STEP 3 — But BFS alone is not enough anymore.
     *
     * Word Ladder I asks only:
     *
     *     "What is the shortest length?"
     *
     * Word Ladder II asks:
     *
     *     "What are ALL shortest paths?"
     *
     * Therefore BFS must preserve enough information to reconstruct every
     * shortest path afterward.
     *
     *
     * STEP 4 — What information do we preserve?
     *
     * For every word:
     *
     *     distance[word]
     *         = shortest number of edges from beginWord
     *
     *     parents[word]
     *         = every previous word that reaches this word using that
     *           shortest distance
     *
     * Example:
     *
     *     dog <- dot
     *
     *     log <- lot
     *
     *     cog <- dog
     *     cog <- log
     *
     * cog intentionally has TWO parents.
     *
     *
     * STEP 5 — BFS creates a shortest-path DAG.
     *
     *     beginWord
     *         ↓
     *     shortest-level relationships
     *         ↓
     *     endWord
     *
     * We do NOT retain longer-path parent relationships.
     *
     *
     * STEP 6 — Backtrack from endWord to beginWord.
     *
     *     endWord
     *         ↓ choose one parent
     *     parent
     *         ↓ choose one parent
     *     ...
     *         ↓
     *     beginWord
     *
     * Every completed reverse path is reversed and added to the answer.
     *
     *
     * FINAL COMPOSITION:
     *
     *     BFS
     *     -> build shortest-path structure
     *
     *     Backtracking
     *     -> enumerate all paths inside that structure
     */


    /*
     * =============================================================================
     * 3. ⭐ PRIMARY SOLUTION — PHOTOGRAPHIC CODE
     * =============================================================================
     *
     * Keep this code visually together.
     * Explanation comes AFTER the complete solution.
     */
    static class Optimal {

        private static final String CHOICES =
                "abcdefghijklmnopqrstuvwxyz";

        public List<List<String>> findLadders(
                String beginWord,
                String endWord,
                List<String> wordList) {

            Set<String> validWords = new HashSet<>(wordList);

            if (!validWords.contains(endWord)) {
                return Collections.emptyList();
            }

            Map<String, List<String>> parents =
                    buildShortestParents(beginWord, validWords);

            if (!parents.containsKey(endWord)) {
                return Collections.emptyList();
            }

            List<List<String>> answer = new ArrayList<>();
            List<String> path = new ArrayList<>();

            path.add(endWord);

            backtrack(
                    beginWord,
                    endWord,
                    parents,
                    path,
                    answer
            );

            return answer;
        }

        private Map<String, List<String>> buildShortestParents(
                String beginWord,
                Set<String> validWords) {

            Map<String, Integer> distance = new HashMap<>();
            Map<String, List<String>> parents = new HashMap<>();
            Queue<String> queue = new ArrayDeque<>();

            queue.offer(beginWord);
            distance.put(beginWord, 0);

            while (!queue.isEmpty()) {

                String current = queue.poll();
                int nextDistance = distance.get(current) + 1;

                for (String neighbor : getNeighbors(current, validWords)) {

                    Integer knownDistance = distance.get(neighbor);

                    if (knownDistance == null) {

                        distance.put(neighbor, nextDistance);

                        List<String> neighborParents = new ArrayList<>();
                        neighborParents.add(current);
                        parents.put(neighbor, neighborParents);

                        queue.offer(neighbor);

                    } else if (knownDistance == nextDistance) {

                        parents.get(neighbor).add(current);
                    }
                }
            }

            return parents;
        }

        private List<String> getNeighbors(
                String current,
                Set<String> validWords) {

            List<String> neighbors = new ArrayList<>();
            char[] characters = current.toCharArray();

            for (int position = 0;
                 position < characters.length;
                 position++) {

                char original = characters[position];

                for (int i = 0; i < CHOICES.length(); i++) {

                    char choice = CHOICES.charAt(i);

                    if (choice == original) {
                        continue;
                    }

                    characters[position] = choice;

                    String candidate = new String(characters);

                    if (validWords.contains(candidate)) {
                        neighbors.add(candidate);
                    }

                    characters[position] = original;
                }
            }

            return neighbors;
        }

        private void backtrack(
                String beginWord,
                String current,
                Map<String, List<String>> parents,
                List<String> path,
                List<List<String>> answer) {

            if (current.equals(beginWord)) {

                List<String> completePath = new ArrayList<>(path);
                Collections.reverse(completePath);
                answer.add(completePath);

                return;
            }

            for (String parent : parents.get(current)) {

                path.add(parent);

                backtrack(
                        beginWord,
                        parent,
                        parents,
                        path,
                        answer
                );

                path.remove(path.size() - 1);
            }
        }
    }


    /*
     * =============================================================================
     * 4. PRIMARY SOLUTION EXPLANATION
     * =============================================================================
     *
     * -----------------------------------------------------------------------------
     * A. THE TOP-LEVEL STORY
     * -----------------------------------------------------------------------------
     *
     * The public method has only two real phases:
     *
     *     BFS
     *     -> buildShortestParents()
     *
     *     Backtracking
     *     -> backtrack()
     *
     * Mental skeleton:
     *
     *     build shortest structure
     *     -> reconstruct every shortest answer
     *
     *
     * -----------------------------------------------------------------------------
     * B. WHY distance IS NEEDED
     * -----------------------------------------------------------------------------
     *
     * Suppose:
     *
     *     hit -> ... -> cog
     *
     * One path reaches some word X in 3 transformations.
     * Another reaches X in 5 transformations.
     *
     * Only the distance-3 arrival can participate in a shortest path through X.
     *
     * Therefore:
     *
     *     distance[X]
     *
     * means:
     *
     *     shortest distance discovered from beginWord to X
     *
     * A later longer arrival is ignored.
     *
     *
     * -----------------------------------------------------------------------------
     * C. WHY parents IS List<String>, NOT ONE String
     * -----------------------------------------------------------------------------
     *
     * This is the heart of Word Ladder II.
     *
     * Example:
     *
     *                     dog
     *                    /   \
     *                  dot    cog
     *                    \   /
     *                     log
     *
     * More concretely:
     *
     *     cog can be reached optimally from dog
     *     cog can also be reached optimally from log
     *
     * Therefore:
     *
     *     parents[cog] = [dog, log]
     *
     * If we stored only one parent, we would lose one valid shortest sequence.
     *
     *
     * -----------------------------------------------------------------------------
     * D. THE MOST IMPORTANT BFS CONDITION
     * -----------------------------------------------------------------------------
     *
     * For neighbor:
     *
     *     nextDistance = distance[current] + 1
     *
     * CASE 1:
     *
     *     neighbor has never been seen
     *
     * Then this BFS discovery is necessarily shortest:
     *
     *     distance[neighbor] = nextDistance
     *     parents[neighbor] = [current]
     *     queue.offer(neighbor)
     *
     *
     * CASE 2:
     *
     *     distance[neighbor] == nextDistance
     *
     * Then another parent reaches neighbor using the SAME shortest distance:
     *
     *     parents[neighbor].add(current)
     *
     * Do NOT enqueue neighbor again.
     * Its outgoing edges already need to be processed only once.
     *
     *
     * CASE 3:
     *
     *     distance[neighbor] < nextDistance
     *
     * We found a longer path.
     * Ignore it completely.
     *
     *
     * -----------------------------------------------------------------------------
     * E. WHY ordinary boolean visited IS NOT ENOUGH
     * -----------------------------------------------------------------------------
     *
     * In Word Ladder I, once a word is seen, we mostly just need:
     *
     *     visited = true
     *
     * because we only need one shortest length.
     *
     * In Word Ladder II, this would be dangerous if it caused us to reject all
     * later same-level arrivals.
     *
     * Example:
     *
     *     dog -> cog
     *     log -> cog
     *
     * dog may discover cog first.
     *
     * When log later reaches cog at the SAME shortest distance, we still need to
     * remember:
     *
     *     log is also a parent of cog
     *
     * Therefore the state we really need is:
     *
     *     shortest distance
     *     +
     *     all parents achieving that distance
     *
     *
     * -----------------------------------------------------------------------------
     * F. WHY BACKTRACK FROM endWord
     * -----------------------------------------------------------------------------
     *
     * BFS stored edges in this convenient direction:
     *
     *     child -> all shortest parents
     *
     * Therefore answer construction naturally starts at endWord:
     *
     *     cog
     *     -> dog
     *     -> dot
     *     -> hot
     *     -> hit
     *
     * This produces the path backward.
     *
     * At beginWord:
     *
     *     copy path
     *     reverse copy
     *     add to answer
     *
     *
     * -----------------------------------------------------------------------------
     * G. WHY CHOOSE -> EXPLORE -> UNDO APPEARS HERE
     * -----------------------------------------------------------------------------
     *
     * During backtrack():
     *
     *     path.add(parent);                // CHOOSE
     *
     *     backtrack(...);                 // EXPLORE
     *
     *     path.remove(path.size() - 1);    // UNDO
     *
     * This is true recursive backtracking.
     *
     * Contrast with getNeighbors():
     *
     *     temporarily mutate one character
     *     inspect candidate
     *     restore character
     *
     * That uses the same restoration micro-pattern, but backtrack() is the
     * actual recursive backtracking phase.
     */


    /*
     * =============================================================================
     * 5. WORD LADDER I -> WORD LADDER II DELTA
     * =============================================================================
     *
     * SAME:
     *
     *     state = word
     *
     *     legal move = change exactly one character
     *
     *     transformed word must exist in dictionary
     *
     *     every move costs 1
     *
     *     shortest-path engine = BFS
     *
     *     getNeighbors() = mutate one position through a..z
     *
     *
     * DELTA 1 — OUTPUT
     *
     * Word Ladder I:
     *
     *     int shortestLength
     *
     * Word Ladder II:
     *
     *     List<List<String>> allShortestPaths
     *
     *
     * DELTA 2 — STATE RETAINED BY BFS
     *
     * Word Ladder I:
     *
     *     visited
     *     + current level
     *
     * Word Ladder II:
     *
     *     distance[word]
     *     + parents[word]
     *
     *
     * DELTA 3 — BFS CANNOT RETURN IMMEDIATELY WITH THE ANSWER
     *
     * Word Ladder I:
     *
     *     once endWord is reached at the first BFS level
     *     -> shortest length is known
     *     -> return
     *
     * Word Ladder II:
     *
     *     reaching endWord tells us the shortest distance,
     *     but not yet all actual sequences
     *
     *     -> preserve all shortest parents
     *     -> reconstruct paths afterward
     *
     *
     * DELTA 4 — SECOND ENGINE
     *
     * Word Ladder I:
     *
     *     BFS
     *
     * Word Ladder II:
     *
     *     BFS
     *     +
     *     backtracking
     *
     *
     * MEMORY:
     *
     *     shortest LENGTH
     *     -> BFS
     *
     *     ALL shortest paths
     *     -> BFS + backtracking
     */


    /*
     * =============================================================================
     * 6. VISUAL DRY RUN
     * =============================================================================
     *
     * Input:
     *
     *     beginWord = hit
     *     endWord   = cog
     *
     *     hot dot dog lot log cog
     *
     *
     * BFS DISTANCES:
     *
     *     distance[hit] = 0
     *
     *     distance[hot] = 1
     *
     *     distance[dot] = 2
     *     distance[lot] = 2
     *
     *     distance[dog] = 3
     *     distance[log] = 3
     *
     *     distance[cog] = 4
     *
     *
     * SHORTEST PARENTS:
     *
     *     hot <- hit
     *
     *     dot <- hot
     *     lot <- hot
     *
     *     dog <- dot
     *     log <- lot
     *
     *     cog <- dog
     *     cog <- log
     *
     *
     * SHORTEST-PATH DAG:
     *
     *                         hit
     *                          |
     *                         hot
     *                       /     \
     *                     dot     lot
     *                      |       |
     *                     dog     log
     *                       \     /
     *                         cog
     *
     *
     * BACKTRACK FROM cog:
     *
     *     cog
     *     ├── dog
     *     │   └── dot
     *     │       └── hot
     *     │           └── hit
     *     │
     *     └── log
     *         └── lot
     *             └── hot
     *                 └── hit
     *
     * Reverse each completed path:
     *
     *     hit -> hot -> dot -> dog -> cog
     *
     *     hit -> hot -> lot -> log -> cog
     */


    /*
     * =============================================================================
     * 7. WHY WE DO NOT STOP BFS THE FIRST TIME endWord IS DISCOVERED
     * =============================================================================
     *
     * This is a major trap.
     *
     * Suppose dog discovers cog first:
     *
     *     dog -> cog
     *
     * If we immediately terminate the entire BFS, we may never process log from
     * the same BFS depth:
     *
     *     log -> cog
     *
     * Then we lose one shortest path.
     *
     * Our primary implementation avoids this problem completely by storing
     * distances and allowing every same-shortest-distance parent to be recorded.
     *
     * It continues BFS through the reachable state space rather than introducing
     * extra early-stop bookkeeping.
     *
     * That is slightly more work than the most aggressively pruned version, but
     * it is extremely easy to reason about and reconstruct correctly.
     */


    /*
     * =============================================================================
     * 8. WHY THIS IS A SHORTEST-PATH DAG
     * =============================================================================
     *
     * Every stored parent edge satisfies:
     *
     *     distance[parent] + 1 == distance[child]
     *
     * Therefore distance strictly decreases when we follow parents backward:
     *
     *     4 -> 3 -> 2 -> 1 -> 0
     *
     * A cycle is impossible inside the retained parent structure.
     *
     * So even though the original word graph may contain cycles, the structure
     * we backtrack through is acyclic.
     *
     * That is why it is useful to think:
     *
     *     original graph
     *         ↓ BFS filtering
     *     shortest-path DAG
     *         ↓ backtracking
     *     all shortest paths
     */


    /*
     * =============================================================================
     * 9. COMPLEXITY DERIVATION
     * =============================================================================
     *
     * Let:
     *
     *     N = number of valid words
     *     L = word length
     *     P = total number of words across all returned shortest paths
     *         counting repetitions between different returned paths
     *
     *
     * -----------------------------------------------------------------------------
     * NEIGHBOR GENERATION DURING BFS
     * -----------------------------------------------------------------------------
     *
     * For one processed word:
     *
     *     L positions
     *     ×
     *     26 replacement characters
     *
     * Each candidate creates a String of length L:
     *
     *     O(L)
     *
     * Therefore one word costs:
     *
     *     O(26 * L * L)
     *     = O(L²)
     *
     * Across N words:
     *
     *     O(N * L²)
     *
     *
     * -----------------------------------------------------------------------------
     * BACKTRACKING
     * -----------------------------------------------------------------------------
     *
     * We must physically construct every returned sequence.
     *
     * Therefore output-sensitive work is unavoidable:
     *
     *     O(P)
     *
     *
     * -----------------------------------------------------------------------------
     * TOTAL TIME
     * -----------------------------------------------------------------------------
     *
     *     O(N * L² + P)
     *
     *
     * -----------------------------------------------------------------------------
     * SPACE
     * -----------------------------------------------------------------------------
     *
     * validWords:
     *
     *     O(N * L)
     *
     * distance:
     *
     *     O(N)
     *
     * parents:
     *
     *     O(E_shortest)
     *
     * where E_shortest is the number of retained shortest-parent relationships.
     *
     * queue:
     *
     *     O(N)
     *
     * current backtracking path:
     *
     *     O(N) worst case
     *
     * output itself:
     *
     *     O(P)
     *
     * Excluding returned output, the structural auxiliary space is roughly:
     *
     *     O(N * L + E_shortest)
     */


    /*
     * =============================================================================
     * 10. REUSABLE PATTERN — BFS + BACKTRACKING
     * =============================================================================
     *
     * Recognition cue:
     *
     *     "Return ALL SHORTEST ..."
     *
     * Think:
     *
     *     PHASE 1
     *     BFS
     *     -> determine minimum distance / shortest layers
     *     -> retain all optimal parent relationships
     *
     *     PHASE 2
     *     DFS / backtracking
     *     -> enumerate all paths allowed by those relationships
     *
     *
     * Reusable shape:
     *
     *     shortest-distance engine
     *     +
     *     answer-enumeration engine
     *
     *
     * Other problems can have the same architecture whenever they ask for:
     *
     *     all shortest paths
     *     all minimum-move sequences
     *     all optimal predecessor chains
     *
     * The transition rule changes.
     * The composition stays recognizable.
     */


    /*
     * =============================================================================
     * 11. BFS vs BACKTRACKING — RESPONSIBILITY SPLIT
     * =============================================================================
     *
     * BFS answers:
     *
     *     What is the shortest structural route?
     *
     * Specifically:
     *
     *     Which parent relationships belong to shortest paths?
     *
     *
     * Backtracking answers:
     *
     *     What are all concrete sequences represented by that structure?
     *
     *
     * Do not mix the responsibilities mentally:
     *
     *     BFS = optimization / shortestness
     *
     *     Backtracking = enumeration / reconstruction
     */


    /*
     * =============================================================================
     * 12. COMMON TRAPS
     * =============================================================================
     *
     * TRAP 1
     * ------
     * Store only one parent.
     *
     * Wrong because multiple shortest paths may converge on the same word.
     *
     *
     * TRAP 2
     * ------
     * Reject every later visit just because the word was already seen.
     *
     * Wrong because a later visit at the SAME shortest distance must contribute
     * another parent.
     *
     *
     * TRAP 3
     * ------
     * Accept a parent arriving at a longer distance.
     *
     * Wrong because that creates non-shortest answer paths.
     *
     *
     * TRAP 4
     * ------
     * Stop the whole BFS immediately on the first discovery of endWord.
     *
     * Wrong unless the implementation still guarantees that every node in the
     * same BFS level can contribute its endWord parent relationship.
     *
     *
     * TRAP 5
     * ------
     * Forget to undo path state during backtracking.
     *
     * Correct pattern:
     *
     *     add
     *     recurse
     *     remove
     *
     *
     * TRAP 6
     * ------
     * Generate neighbors that are not in wordList.
     *
     * Every transformed intermediate word must be valid.
     */


    /*
     * =============================================================================
     * 13. 30-SECOND RECALL
     * =============================================================================
     *
     * Trigger:
     *
     *     ALL shortest transformation sequences
     *
     * Engine:
     *
     *     BFS + backtracking
     *
     * BFS stores:
     *
     *     distance[word]
     *     parents[word]
     *
     * Critical BFS rule:
     *
     *     unseen neighbor
     *     -> set distance
     *     -> first parent
     *     -> enqueue
     *
     *     same shortest distance
     *     -> add another parent
     *
     *     longer distance
     *     -> ignore
     *
     * Backtracking:
     *
     *     start at endWord
     *     follow every parent
     *     CHOOSE -> EXPLORE -> UNDO
     *     reverse completed path
     *
     * One-liner:
     *
     *     BFS builds the shortest-path DAG;
     *     backtracking enumerates every path through it.
     */


    /*
     * =============================================================================
     * 14. MASTERY CHECKLIST
     * =============================================================================
     *
     * [ ] Can I explain why Word Ladder II needs more than ordinary BFS?
     *
     * [ ] Can I explain why one child may need multiple parents?
     *
     * [ ] Can I derive the three neighbor cases using distance?
     *
     * [ ] Can I explain why same-distance revisits are useful?
     *
     * [ ] Can I explain why longer-distance revisits are ignored?
     *
     * [ ] Can I explain why the retained parent graph is acyclic?
     *
     * [ ] Can I write CHOOSE -> EXPLORE -> UNDO for backtrack()?
     *
     * [ ] Can I distinguish BFS responsibility from backtracking responsibility?
     *
     * [ ] Can I derive O(N * L² + output)?
     */


    /*
     * =============================================================================
     * 15. SELF-VERIFYING TESTS
     * =============================================================================
     */
    public static void main(String[] args) {

        Optimal solver =
                new Optimal();


        List<List<String>> result1 =
                solver.findLadders(
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
                );

        Set<List<String>> expected1 =
                new HashSet<>(
                        Arrays.asList(
                                Arrays.asList(
                                        "hit",
                                        "hot",
                                        "dot",
                                        "dog",
                                        "cog"
                                ),
                                Arrays.asList(
                                        "hit",
                                        "hot",
                                        "lot",
                                        "log",
                                        "cog"
                                )
                        )
                );

        assert new HashSet<>(result1).equals(expected1);


        List<List<String>> result2 =
                solver.findLadders(
                        "hit",
                        "cog",
                        Arrays.asList(
                                "hot",
                                "dot",
                                "dog",
                                "lot",
                                "log"
                        )
                );

        assert result2.isEmpty();


        List<List<String>> result3 =
                solver.findLadders(
                        "a",
                        "c",
                        Arrays.asList(
                                "a",
                                "b",
                                "c"
                        )
                );

        Set<List<String>> expected3 =
                new HashSet<>(
                        Collections.singletonList(
                                Arrays.asList(
                                        "a",
                                        "c"
                                )
                        )
                );

        assert new HashSet<>(result3).equals(expected3);


        List<List<String>> result4 =
                solver.findLadders(
                        "red",
                        "tax",
                        Arrays.asList(
                                "ted",
                                "tex",
                                "red",
                                "tax",
                                "tad",
                                "den",
                                "rex",
                                "pee"
                        )
                );

        Set<List<String>> expected4 =
                new HashSet<>(
                        Arrays.asList(
                                Arrays.asList(
                                        "red",
                                        "ted",
                                        "tad",
                                        "tax"
                                ),
                                Arrays.asList(
                                        "red",
                                        "ted",
                                        "tex",
                                        "tax"
                                ),
                                Arrays.asList(
                                        "red",
                                        "rex",
                                        "tex",
                                        "tax"
                                )
                        )
                );

        assert new HashSet<>(result4).equals(expected4);


        System.out.println(
                "All Word Ladder II Java Gold assertions passed."
        );
    }
}
