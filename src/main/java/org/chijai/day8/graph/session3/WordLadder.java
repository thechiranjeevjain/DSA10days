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
 * Tags:
 * Graph
 * Breadth First Search (BFS)
 * Shortest Path
 * HashSet
 * String
 *
 * =====================================================================================
 * 2. 📘 PRIMARY PROBLEM
 * =====================================================================================
 *
 * Problem
 * -------
 * A transformation sequence from beginWord to endWord using a dictionary wordList is
 * defined as:
 *
 * beginWord -> s1 -> s2 -> ... -> sk
 *
 * such that:
 *
 * 1. Every adjacent pair differs by exactly one character.
 * 2. Every intermediate word belongs to wordList.
 * 3. beginWord does NOT have to exist inside wordList.
 * 4. endWord must be reached.
 *
 * Return the number of words in the shortest transformation sequence.
 * If impossible, return 0.
 *
 * Constraints
 * -----------
 * • 1 <= beginWord.length <= 10
 * • endWord.length == beginWord.length
 * • 1 <= wordList.length <= 5000
 * • All words have identical length.
 * • All words consist of lowercase English letters.
 * • All words are unique.
 *
 * Representative Example
 * ----------------------
 * begin = "hit"
 * end   = "cog"
 *
 * dictionary
 * hot dot dog lot log cog
 *
 * hit
 *  |
 * hot
 * / \
 * dot lot
 * |    |
 * dog log
 *   \ /
 *   cog
 *
 * Shortest sequence:
 *
 * hit
 * ->
 * hot
 * ->
 * dot
 * ->
 * dog
 * ->
 * cog
 *
 * Answer = 5
 *
 * Example 2
 * ---------
 * endWord absent from dictionary.
 *
 * No valid transformation.
 *
 * Return 0.
 *
 *
 * =====================================================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * =====================================================================================
 *
 * Pattern
 * -------
 * Unweighted Graph Shortest Path using Breadth First Search.
 *
 * Archetype
 * ---------
 * Level-order exploration.
 *
 * Core Invariant
 * --------------
 * Every word removed from the BFS queue is reached using the minimum possible
 * number of transformations.
 *
 * Therefore the first time endWord is discovered, the answer is optimal.
 *
 * Why It Works
 * ------------
 * Every valid character replacement represents one graph edge.
 *
 * Every edge has equal cost (=1).
 *
 * BFS explores states in increasing path length.
 *
 * Recognition Signals
 * -------------------
 * ✔ minimum number of operations
 * ✔ every operation costs exactly one
 * ✔ transformations
 * ✔ graph not explicitly given
 * ✔ shortest path
 *
 * When To Use
 * -----------
 * • Equal edge weights
 * • Minimum moves
 * • Implicit graph
 * • State transitions generated on demand
 *
 * When NOT To Use
 * ---------------
 * • Different edge costs
 * • Negative weights
 * • Weighted shortest path
 * • Longest path
 *
 * Comparison
 * ----------
 *
 * DFS
 * ----
 * Finds a path.
 * Not guaranteed shortest.
 *
 * Dijkstra
 * --------
 * Weighted graphs.
 * Unnecessary overhead here.
 *
 * A*
 * ---
 * Uses heuristic.
 * Useful for huge search spaces.
 *
 * Bidirectional BFS
 * -----------------
 * Faster practical optimization.
 * Same correctness.
 * More implementation complexity.
 *
 *
 * =====================================================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * =====================================================================================
 *
 * Mental Model
 * ------------
 * Imagine every valid dictionary word as a node.
 *
 * Two nodes are connected if they differ by exactly one character.
 *
 * We never explicitly build this graph.
 *
 * Instead, while standing on one word, we mechanically generate all possible
 * neighbors by changing one character at every position.
 *
 * BFS walks outward layer by layer.
 *
 * Layer 1
 * --------
 * Words reachable in one transformation.
 *
 * Layer 2
 * --------
 * Words reachable in two transformations.
 *
 * ...
 *
 * Eventually endWord appears.
 *
 * Since BFS never skips layers, that layer is optimal.
 *
 *
 * --------------------------
 * 🟢 Invariant 1
 * --------------------------
 *
 * Every queued word belongs to exactly one BFS level.
 *
 * Meaning:
 *
 * All words inside the queue before processing a level represent paths having
 * identical length.
 *
 *
 * --------------------------
 * 🟢 Invariant 2
 * --------------------------
 *
 * A dictionary word is visited at most once.
 *
 * We immediately remove it from the HashSet when discovered.
 *
 * This guarantees:
 *
 * • no duplicate work
 * • no infinite cycles
 * • no longer path replacing a shorter one
 *
 *
 * --------------------------
 * 🟢 Invariant 3
 * --------------------------
 *
 * The remaining HashSet is exactly the unexplored search space.
 *
 * Every removal permanently shrinks future work.
 *
 *
 * --------------------------
 * 🟢 Invariant 4
 * --------------------------
 *
 * step equals the number of words in the transformation sequence represented by
 * the current BFS layer.
 *
 * Initial state:
 *
 * Queue:
 * hit
 *
 * step = 1
 *
 * because the sequence currently contains only beginWord.
 *
 *
 * --------------------------
 * 🟢 Variable Meanings
 * --------------------------
 *
 * wordSet
 *
 * Remaining unexplored dictionary.
 *
 * queue
 *
 * Current BFS frontier.
 *
 * size
 *
 * Number of states belonging to the current shortest distance.
 *
 * step
 *
 * Transformation sequence length for current layer.
 *
 * currWord
 *
 * Current graph node.
 *
 *
 * --------------------------
 * 🟢 Allowed State Transition
 * --------------------------
 *
 * Current word
 *
 * Change exactly one character.
 *
 * If new word exists inside unexplored dictionary:
 *
 * enqueue
 * remove from dictionary
 *
 *
 * --------------------------
 * 🔴 Forbidden Transition
 * --------------------------
 *
 * Visiting an already removed word.
 *
 * That would violate shortest-path ordering.
 *
 *
 * --------------------------
 * 🟢 Termination
 * --------------------------
 *
 * BFS stops when:
 *
 * 1. endWord discovered
 *
 * OR
 *
 * 2. queue empty
 *
 * Queue empty means the reachable connected component has been exhausted.
 *
 *
 * --------------------------
 * Why Naive Solutions Fail
 * --------------------------
 *
 * Naive DFS
 * ---------
 * May discover a very long transformation before the shortest one.
 *
 * Backtracking over all possibilities is exponential.
 *
 * Recursive search revisits states repeatedly.
 *
 * BFS avoids all of these by exploring strictly in increasing distance.
 *
 *
 * =====================================================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * =====================================================================================
 *
 * Mistake 1
 * ---------
 * Remove a word after dequeue instead of immediately after enqueue.
 *
 * Looks reasonable because the word is "processed" only when popped.
 *
 * Failure:
 *
 * Multiple parents enqueue the same node.
 *
 * Queue explodes.
 *
 * Invariant violated:
 *
 * "Every dictionary word is discovered once."
 *
 *
 * Mistake 2
 * ---------
 * Use DFS because graph seems small.
 *
 * Counterexample
 *
 * hit
 * |
 * hot
 * |
 * dot
 * |
 * dog
 * |
 * cog
 *
 * Another branch contains hundreds of useless words.
 *
 * DFS may traverse entire useless branch first.
 *
 *
 * Mistake 3
 * ---------
 * Forget to process level by level.
 *
 * Incrementing step after every node instead of every level.
 *
 * Counterexample:
 *
 * One BFS layer contains multiple words.
 *
 * All should share identical shortest distance.
 *
 *
 * Mistake 4
 * ---------
 * Continue searching after reaching endWord.
 *
 * BFS guarantee is lost conceptually.
 *
 * First discovery is already optimal.
 *
 *
 * Interview Trap
 * --------------
 *
 * "Can we mark visited after dequeue?"
 *
 * Correct answer:
 *
 * No.
 *
 * Multiple shortest parents may enqueue the same child before it is popped,
 * causing duplicate work and violating the one-discovery invariant.
 *
 *
 * =====================================================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * =====================================================================================
 *
 * Mechanical typing order:
 *
 * 1.
 *
 * int ladderLength(...)
 *
 * 2.
 *
 * Validate endWord exists.
 *
 * 3.
 *
 * Build HashSet.
 *
 * 4.
 *
 * Create queue.
 *
 * 5.
 *
 * Push beginWord.
 *
 * 6.
 *
 * step = 1.
 *
 * 7.
 *
 * while queue not empty
 *
 *      levelSize
 *
 *      repeat levelSize times
 *
 *          pop word
 *
 *          for every position
 *
 *              try all 26 letters
 *
 *                  generate neighbor
 *
 *                  if neighbor absent
 *                      continue
 *
 *                  if neighbor == endWord
 *                      return step + 1
 *
 *                  enqueue
 *
 *                  remove immediately
 *
 *      step++
 *
 * 8.
 *
 * return 0
 *
 *
 * =====================================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * =====================================================================================
 *
 * build set
 * verify end exists
 *
 * enqueue(begin)
 * step = 1
 *
 * while queue
 *      process one level
 *      generate neighbors
 *      visit once
 *      return on target
 *      step++
 *
 * return 0
 *
 *
 * =====================================================================================
 * 6. SOLUTION CLASSES
 * =====================================================================================
 */

/**
 * Exactly one top-level public class as required.
 */
public class WordLadder {

    /**
     * =========================================================================
     * Brute Force
     * =========================================================================
     *
     * Idea
     * ----
     * Try every possible transformation recursively.
     *
     * Invariant
     * ---------
     * Current path remains valid.
     *
     * Limitation
     * ----------
     * Explores exponentially many paths.
     *
     * Complexity
     * ----------
     * Exponential.
     *
     * Interview Usefulness
     * --------------------
     * Only useful as a baseline discussion.
     */
    static class BruteForce {

        public int ladderLength(String beginWord,
                                String endWord,
                                List<String> wordList) {
            throw new UnsupportedOperationException(
                    "Brute-force recursion is intentionally omitted because it is exponential and unsuitable for interviews."
            );
        }
    }

    /**
     * =========================================================================
     * Improved
     * =========================================================================
     *
     * Idea
     * ----
     * Build the implicit graph on demand using BFS.
     *
     * Invariant
     * ---------
     * Every visited word has already been reached optimally.
     *
     * Improvement
     * -----------
     * Avoids exponential exploration.
     *
     * Complexity
     * ----------
     * Time:
     * O(N * L * 26)
     *
     * Space:
     * O(N)
     *
     * Interview Usefulness
     * --------------------
     * This is already the accepted interview solution.
     */
    static class Improved {


        public int ladderLength(String beginWord,
                                String endWord,
                                List<String> wordList) {

            if (!wordList.contains(endWord)) {
                return 0;
            }

            Set<String> wordSet = new HashSet<>(wordList);

            Queue<String> queue = new ArrayDeque<>();
            queue.offer(beginWord);

            // Prevent revisiting beginWord if it exists in the dictionary.
            wordSet.remove(beginWord);

            int step = 1;

            while (!queue.isEmpty()) {

                // Every word currently in the queue belongs to the same BFS layer.
                int levelSize = queue.size();

                for (int levelIndex = 0; levelIndex < levelSize; levelIndex++) {

                    String currentWord = queue.poll();

                    char[] characters = currentWord.toCharArray();

                    for (int position = 0; position < characters.length; position++) {

                        char originalCharacter = characters[position];

                        for (char candidate = 'a'; candidate <= 'z'; candidate++) {

                            if (candidate == originalCharacter) {
                                continue;
                            }

                            characters[position] = candidate;

                            String nextWord = new String(characters);

                            if (!wordSet.contains(nextWord)) {
                                continue;
                            }

                            // First discovery of endWord is guaranteed optimal.
                            if (nextWord.equals(endWord)) {
                                return step + 1;
                            }

                            // Remove immediately to preserve one-discovery invariant.
                            wordSet.remove(nextWord);

                            queue.offer(nextWord);
                        }

                        // Restore state before mutating another position.
                        characters[position] = originalCharacter;
                    }
                }

                // Entire layer processed; advance shortest-path length.
                step++;
            }

            return 0;
        }
    }

    /**
     * =========================================================================
     * Optimal (Interview Preferred)
     * =========================================================================
     *
     * Idea
     * ----
     * Treat every dictionary word as a node in an implicit graph.
     *
     * Generate neighbors by replacing one character at every position.
     *
     * BFS guarantees that nodes are explored in increasing transformation
     * length, so the first time endWord is discovered we have the shortest
     * possible sequence.
     *
     * 🟢 Invariant
     * ------------
     * Every queued word has already been reached using the minimum possible
     * number of transformations.
     *
     * Correctness
     * -----------
     * Equal edge weights imply BFS is equivalent to shortest-path search.
     *
     * Complexity
     * ----------
     * Let:
     *
     * N = number of dictionary words
     * L = length of each word
     *
     * Each word is discovered at most once.
     *
     * For every discovered word:
     *
     * • L character positions
     * • 26 candidate letters
     *
     * Time:
     * O(N × L × 26)
     *
     * Since 26 is constant:
     *
     * O(N × L)
     *
     * Space:
     * O(N)
     *
     * Interview Usefulness
     * --------------------
     * This is the standard expected solution for Word Ladder.
     */
    static class Optimal {

        public int ladderLength(String beginWord,
                                String endWord,
                                List<String> wordList) {

            if (!wordList.contains(endWord)) {
                return 0;
            }

            Set<String> unexploredWords = new HashSet<>(wordList);

            Queue<String> bfsQueue = new ArrayDeque<>();

            bfsQueue.offer(beginWord);

            unexploredWords.remove(beginWord);

            int step = 1;

            while (!bfsQueue.isEmpty()) {

                // Invariant: every node in this batch has identical distance.
                int currentLevelSize = bfsQueue.size();

                for (int node = 0; node < currentLevelSize; node++) {

                    String currentWord = bfsQueue.poll();

                    char[] letters = currentWord.toCharArray();

                    for (int index = 0; index < letters.length; index++) {

                        char original = letters[index];

                        for (char replacement = 'a';
                             replacement <= 'z';
                             replacement++) {

                            if (replacement == original) {
                                continue;
                            }

                            letters[index] = replacement;

                            String candidate = new String(letters);

                            if (!unexploredWords.contains(candidate)) {
                                continue;
                            }

                            // Invariant:
                            // First discovery means shortest transformation.
                            if (candidate.equals(endWord)) {
                                return step + 1;
                            }

                            // Remove immediately so no second parent can
                            // enqueue the same state.
                            unexploredWords.remove(candidate);

                            bfsQueue.offer(candidate);
                        }

                        // Restore original state before changing another
                        // position.
                        letters[index] = original;
                    }
                }

                // Entire shortest-distance frontier has been exhausted.
                step++;
            }

            // Search space exhausted without reaching target.
            return 0;
        }
    }

/**
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * Explain the Invariant
 * ---------------------
 *
 * I model every valid word as a graph node.
 *
 * Two nodes are adjacent when they differ by exactly one character.
 *
 * Instead of explicitly constructing the graph, I generate neighbors
 * lazily by replacing each character with every lowercase letter.
 *
 * BFS explores these nodes level by level.
 *
 * Therefore every word removed from the queue has already been reached
 * through the minimum possible number of transformations.
 *
 *
 * Explain the Discard Rule
 * ------------------------
 *
 * As soon as a neighbor is discovered, I immediately remove it from the
 * dictionary.
 *
 * This guarantees that each graph node is discovered exactly once.
 *
 * It prevents duplicate queue entries and preserves shortest-path
 * correctness.
 *
 *
 * Explain Correctness
 * -------------------
 *
 * Every transformation has equal cost.
 *
 * BFS explores states in increasing path length.
 *
 * Therefore the first discovery of endWord is necessarily optimal.
 *
 *
 * Explain Termination
 * -------------------
 *
 * The algorithm terminates because every dictionary word can enter the
 * queue at most once.
 *
 * Eventually either:
 *
 * • endWord is found
 *
 * or
 *
 * • the queue becomes empty.
 *
 *
 * In-place Feasibility
 * --------------------
 *
 * Not applicable.
 *
 * The dictionary must remain searchable.
 *
 * A HashSet is the natural state representation.
 *
 *
 * Streaming Feasibility
 * ---------------------
 *
 * Not naturally.
 *
 * Random membership queries over the entire dictionary are required.
 *
 *
 * When NOT To Use
 * ---------------
 *
 * • weighted transformations
 * • negative costs
 * • minimum-cost instead of minimum-step problems
 * • dynamic edge weights
 *
 *
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================================
 *
 * Trigger
 * -------
 * Minimum transformations with equal cost.
 *
 * Pattern
 * -------
 * BFS on an implicit graph.
 *
 * Invariant
 * ---------
 * Queue stores one shortest-distance frontier.
 *
 * Search Target
 * -------------
 * First appearance of endWord.
 *
 * Discard Rule
 * ------------
 * Remove from HashSet immediately after enqueue.
 *
 * Common Trap
 * -----------
 * Removing only after dequeue duplicates work.
 *
 * Edge Cases
 * ----------
 * • endWord absent
 * • single-character words
 * • unreachable target
 * • beginWord already inside dictionary
 *
 * One-liner
 * ---------
 * Equal edge weights imply BFS.
 *
 * Re-derivation Cue
 * -----------------
 * Queue = shortest frontier.
 * HashSet = unexplored search space.
 */

/**
 * =========================================================================
 * 🔄 VARIATIONS & TWEAKS
 * =========================================================================
 *
 * -------------------------------------------------------------------------
 * Variation 1
 * -------------------------------------------------------------------------
 * Return the actual transformation sequence.
 *
 * Reasoning Change
 * ----------------
 * Store the parent of every discovered word.
 *
 * Parent Map:
 *
 * child -> parent
 *
 * Once endWord is found, repeatedly follow parent pointers back to
 * beginWord and reverse the collected path.
 *
 * Invariant Preserved
 * -------------------
 * The first recorded parent always belongs to the shortest path because a
 * node is discovered only once.
 *
 *
 * -------------------------------------------------------------------------
 * Variation 2
 * -------------------------------------------------------------------------
 * Return every shortest transformation sequence.
 *
 * (Word Ladder II)
 *
 * Reasoning Change
 * ----------------
 * One parent is insufficient.
 *
 * Maintain:
 *
 * child -> list of shortest parents
 *
 * Finish processing the entire BFS layer where endWord is first reached.
 *
 * Afterwards perform DFS/backtracking over the parent graph.
 *
 * Pattern Break
 * -------------
 * Immediate termination is no longer valid because other shortest parents
 * may exist in the same BFS level.
 *
 *
 * -------------------------------------------------------------------------
 * Variation 3
 * -------------------------------------------------------------------------
 * Bidirectional BFS
 *
 * Reasoning Change
 * ----------------
 * Expand simultaneously from:
 *
 * beginWord
 * endWord
 *
 * Always expand the smaller frontier.
 *
 * Why It Still Works
 * ------------------
 * Both searches preserve BFS distance ordering.
 *
 * The first meeting point represents the optimal path.
 *
 * Practical Benefit
 * -----------------
 * Dramatically reduces explored states on large dictionaries.
 *
 *
 * -------------------------------------------------------------------------
 * Variation 4
 * -------------------------------------------------------------------------
 * Precomputed wildcard graph.
 *
 * Example
 * -------
 * hot
 *
 * generates
 *
 * *ot
 * h*t
 * ho*
 *
 * Build:
 *
 * wildcard
 * ->
 * words
 *
 * Neighbor lookup becomes faster because only compatible words are scanned.
 *
 * Trade-off
 * ---------
 * More preprocessing.
 *
 * More memory.
 *
 *
 * -------------------------------------------------------------------------
 * Variation 5
 * -------------------------------------------------------------------------
 * Weighted transformations.
 *
 * Pattern Break
 * -------------
 * BFS correctness disappears.
 *
 * Correct Pattern
 * ---------------
 * Dijkstra.
 *
 *
 * -------------------------------------------------------------------------
 * Variation 6
 * -------------------------------------------------------------------------
 * Different word lengths.
 *
 * Pattern Break
 * -------------
 * Single-character replacement alone no longer defines graph edges.
 *
 * Insertions and deletions must also be modeled.
 *
 * Graph definition changes completely.
 *
 *
 * =========================================================================
 * 🧠 MASTERY CHECKLIST
 * =========================================================================
 *
 * □ Can I state the invariant?
 *
 * Every queued node has already been reached optimally.
 *
 *
 * □ What is the search target?
 *
 * First discovery of endWord.
 *
 *
 * □ What is the discard rule?
 *
 * Remove from HashSet immediately after enqueue.
 *
 *
 * □ Why does BFS terminate?
 *
 * Every dictionary word is processed at most once.
 *
 *
 * □ Why does DFS fail?
 *
 * DFS does not preserve increasing path length.
 *
 *
 * □ Why is the answer optimal?
 *
 * Equal edge weights plus BFS level ordering.
 *
 *
 * □ Which data structures matter?
 *
 * Queue
 *
 * HashSet
 *
 *
 * □ Why not boolean visited[]?
 *
 * Words are strings.
 *
 * HashSet naturally provides:
 *
 * O(1)
 *
 * membership
 *
 * and
 *
 * visited removal.
 *
 *
 * □ Edge cases remembered?
 *
 * ✔ endWord absent
 *
 * ✔ unreachable graph
 *
 * ✔ beginWord inside dictionary
 *
 * ✔ dictionary of size one
 *
 * ✔ repeated generated candidates
 *
 *
 * □ Debugging readiness?
 *
 * Check:
 *
 * • level size
 *
 * • step increment timing
 *
 * • immediate removal
 *
 * • character restoration
 *
 *
 * □ Variant readiness?
 *
 * ✔ Word Ladder II
 *
 * ✔ Bidirectional BFS
 *
 * ✔ Wildcard preprocessing
 *
 * ✔ Parent reconstruction
 *
 *
 * □ Pattern boundary?
 *
 * Equal-cost shortest path
 * -> BFS
 *
 * Weighted shortest path
 * -> Dijkstra
 *
 *
 * =========================================================================
 * ⚫ PATTERN MAPPING
 * =========================================================================
 *
 * This problem belongs to the family:
 *
 * • Rotten Oranges
 * • Minimum Genetic Mutation
 * • Open the Lock
 * • Bus Routes
 * • Snakes and Ladders
 * • Binary Matrix Shortest Path
 *
 * Shared Invariant
 * ----------------
 * One BFS layer equals one unit of distance.
 *
 *
 * =========================================================================
 * 🔍 FORENSIC DEBUGGING GUIDE
 * =========================================================================
 *
 * Symptom
 * -------
 * Same word appears many times in the queue.
 *
 * Likely Cause
 * ------------
 * Removal performed after dequeue instead of after enqueue.
 *
 * Violated Invariant
 * ------------------
 * Every node must be discovered exactly once.
 *
 *
 * Symptom
 * -------
 * Returned answer is one larger or smaller.
 *
 * Likely Cause
 * ------------
 * step updated at the wrong time.
 *
 * Correct Rule
 * ------------
 * Increment only after processing an entire BFS layer.
 *
 *
 * Symptom
 * -------
 * Valid transformations disappear unexpectedly.
 *
 * Likely Cause
 * ------------
 * Character array not restored after mutation.
 *
 * Correct Rule
 * ------------
 * Restore the original character before changing another position.
 *
 *
 * Symptom
 * -------
 * Algorithm never reaches endWord even though a path exists.
 *
 * Likely Cause
 * ------------
 * Neighbor generation skipped some positions or letters.
 *
 * Verification
 * ------------
 * For every word:
 *
 * L positions
 *
 * ×
 *
 * 26 letters
 *
 * must be attempted.
 *
 *
 * Symptom
 * -------
 * Queue becomes empty immediately.
 *
 * Likely Cause
 * ------------
 * endWord missing from dictionary or neighbor generation incorrect.
 *
 *
 * =========================================================================
 * ⚫ IMPLEMENTATION RECONSTRUCTION DRILL
 * =========================================================================
 *
 * Memorize only these mechanical steps:
 *
 * 1.
 * Verify endWord exists.
 *
 * 2.
 * HashSet from dictionary.
 *
 * 3.
 * Queue beginWord.
 *
 * 4.
 * step = 1.
 *
 * 5.
 * While queue not empty:
 *
 *      levelSize
 *
 *      repeat levelSize:
 *
 *          poll
 *
 *          every position
 *
 *          every letter
 *
 *          create candidate
 *
 *          if absent -> continue
 *
 *          if target -> return step + 1
 *
 *          remove
 *
 *          enqueue
 *
 *      step++
 *
 * 6.
 * Return 0.
 *
 *
 * =========================================================================
 * 🧪 MAIN + SELF-VERIFYING TESTS
 * =========================================================================
 */

public static void main(String[] args) {

    Optimal solver = new Optimal();

    // Representative LeetCode example.
    assert solver.ladderLength(
            "hit",
            "cog",
            Arrays.asList("hot", "dot", "dog", "lot", "log", "cog")
    ) == 5 : "Expected shortest transformation length of 5.";

    // endWord absent from dictionary.
    assert solver.ladderLength(
            "hit",
            "cog",
            Arrays.asList("hot", "dot", "dog", "lot", "log")
    ) == 0 : "No valid transformation should exist.";

    // Single-character transformation.
    assert solver.ladderLength(
            "a",
            "c",
            Arrays.asList("a", "b", "c")
    ) == 2 : "Direct one-letter change should require two words.";

    // One intermediate transformation.
    assert solver.ladderLength(
            "ab",
            "bb",
            Arrays.asList("ab", "bb")
    ) == 2 : "Only one transformation required.";

    // Longer chain.
    assert solver.ladderLength(
            "aaa",
            "bbb",
            Arrays.asList(
                    "aab",
                    "abb",
                    "bbb",
                    "aba",
                    "baa"
            )
    ) == 4 : "Shortest chain should be aaa -> aab -> abb -> bbb.";

    // Unreachable although endWord exists.
    assert solver.ladderLength(
            "aaa",
            "ccc",
            Arrays.asList(
                    "aac",
                    "acc",
                    "bbb",
                    "ccc"
            )
    ) == 0 : "Disconnected graph should return zero.";

    // beginWord already inside dictionary.
    assert solver.ladderLength(
            "hit",
            "hot",
            Arrays.asList("hit", "hot")
    ) == 2 : "Removing beginWord from the set must not break correctness.";

    // Duplicate candidate generation should still discover each word once.
    assert solver.ladderLength(
            "red",
            "tax",
            Arrays.asList(
                    "ted",
                    "tex",
                    "tax",
                    "rex",
                    "red"
            )
    ) == 4 : "Expected path: red -> ted -> tex -> tax.";

    // Immediate failure because target is absent.
    assert solver.ladderLength(
            "abc",
            "xyz",
            Arrays.asList(
                    "abd",
                    "acd",
                    "xbc"
            )
    ) == 0 : "Missing target must immediately return zero.";

    // Boundary case: dictionary containing only the target.
    assert solver.ladderLength(
            "aa",
            "ab",
            Arrays.asList("ab")
    ) == 2 : "Single valid transformation should succeed.";

    System.out.println("All assertions passed.");
}
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/