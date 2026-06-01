package org.chijai.day8.session2;
import java.util.*;

/**
 * ====================================================================================================
 * 📘 PRIMARY PROBLEM — FULL OFFICIAL LEETCODE STATEMENT
 * ====================================================================================================
 *
 * 210. Course Schedule II
 *
 * Difficulty: Medium
 *
 * Tags:
 * Graph, Breadth-First Search, Depth-First Search, Topological Sort
 *
 * Official Link:
 * https://leetcode.com/problems/course-schedule-ii/
 *
 * Problem Statement:
 *
 * There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1.
 * You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that
 * you must take course bi first if you want to take course ai.
 *
 * For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
 *
 * Return the ordering of courses you should take to finish all courses.
 * If there are many valid answers, return any of them.
 * If it is impossible to finish all courses, return an empty array.
 *
 * Example 1:
 *
 * Input: numCourses = 2, prerequisites = [[1,0]]
 * Output: [0,1]
 * Explanation:
 * There are a total of 2 courses to take.
 * To take course 1 you should have finished course 0.
 * So the correct course order is [0,1].
 *
 * Example 2:
 *
 * Input: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
 * Output: [0,2,1,3]
 * Explanation:
 * There are a total of 4 courses to take.
 * To take course 3 you should have finished both courses 1 and 2.
 * Both courses 1 and 2 should be taken after you finished course 0.
 * So one correct course order is [0,1,2,3].
 * Another correct ordering is [0,2,1,3].
 *
 * Example 3:
 *
 * Input: numCourses = 1, prerequisites = []
 * Output: [0]
 *
 * Constraints:
 *
 * 1 <= numCourses <= 2000
 * 0 <= prerequisites.length <= numCourses * (numCourses - 1)
 * prerequisites[i].length == 2
 * 0 <= ai, bi < numCourses
 * ai != bi
 * All the pairs [ai, bi] are distinct.
 *
 * ====================================================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * ====================================================================================================
 *
 * Pattern Name:
 * Topological Sorting using BFS (Kahn's Algorithm)
 *
 * Problem Archetype:
 * Dependency Resolution in Directed Acyclic Graph (DAG)
 *
 * CORE INVARIANT:
 *
 * A node can enter the ordering ONLY when its indegree becomes 0.
 *
 * Meaning:
 * Every prerequisite for that node has already been processed.
 *
 * Why It Works:
 *
 * Indegree represents remaining unmet dependencies.
 * Once indegree becomes 0:
 * - all prerequisites are satisfied
 * - node is now safe to process
 *
 * We repeatedly:
 * - process currently unlocked nodes
 * - remove their outgoing edges
 * - unlock new nodes
 *
 * If all nodes are processed:
 * - graph had no cycle
 *
 * If some nodes remain locked forever:
 * - cycle exists
 * - impossible ordering
 *
 * When To Use:
 *
 * Use this pattern when:
 * - dependencies exist
 * - ordering matters
 * - prerequisite relationships exist
 * - tasks must happen before other tasks
 * - directed graph + cycle detection + ordering
 *
 * Recognition Signals:
 *
 * Keywords:
 * - before
 * - after
 * - prerequisite
 * - dependency
 * - build order
 * - scheduling
 * - task pipeline
 *
 * Output asks:
 * - valid ordering
 * - execution order
 * - feasibility
 *
 * Differences vs Similar Patterns:
 *
 * DFS Cycle Detection:
 * - good for detecting cycles
 * - harder to reason about ordering incrementally
 *
 * Kahn BFS:
 * - naturally produces topological order
 * - very interview friendly
 * - easier invariant
 * - excellent for dependency unlock simulation
 *
 * ====================================================================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * ====================================================================================================
 *
 * Mental Model:
 *
 * Imagine courses locked behind prerequisite chains.
 *
 * indegree[x] =
 * number of remaining locks on course x.
 *
 * Queue contains:
 * courses currently unlocked.
 *
 * Processing a course:
 * means we completed it.
 *
 * Completing a course removes one dependency from neighbors.
 *
 * If neighbor loses all locks:
 * it becomes available.
 *
 * --------------------------------------------------------------------------------
 * ALL INVARIANTS
 * --------------------------------------------------------------------------------
 *
 * INVARIANT 1:
 * Queue always contains ONLY nodes with indegree == 0.
 *
 * INVARIANT 2:
 * Every node removed from queue is safe to place in final ordering.
 *
 * INVARIANT 3:
 * Once a node is processed, it never needs reconsideration.
 *
 * INVARIANT 4:
 * indegree[v] always equals remaining unmet prerequisites.
 *
 * INVARIANT 5:
 * If cycle exists, some nodes never reach indegree 0.
 *
 * --------------------------------------------------------------------------------
 * Meaning of Variables
 * --------------------------------------------------------------------------------
 *
 * graph[u]:
 * all nodes dependent on u
 *
 * indegree[v]:
 * remaining prerequisites for v
 *
 * queue:
 * currently executable courses
 *
 * order:
 * valid topological ordering constructed incrementally
 *
 * processedCount:
 * number of finalized nodes
 *
 * --------------------------------------------------------------------------------
 * Allowed Moves
 * --------------------------------------------------------------------------------
 *
 * ✔ Remove node with indegree 0
 * ✔ Reduce neighbor indegrees
 * ✔ Add neighbor when indegree becomes 0
 *
 * --------------------------------------------------------------------------------
 * Forbidden Moves
 * --------------------------------------------------------------------------------
 *
 * ✘ Process node with indegree > 0
 * ✘ Ignore cycle leftovers
 * ✘ Reverse edge direction accidentally
 *
 * --------------------------------------------------------------------------------
 * Termination Logic
 * --------------------------------------------------------------------------------
 *
 * BFS stops when queue becomes empty.
 *
 * Then:
 *
 * If processedCount == numCourses:
 *     success
 *
 * Else:
 *     cycle exists
 *
 * --------------------------------------------------------------------------------
 * Why Naive Approaches Fail
 * --------------------------------------------------------------------------------
 *
 * Naive mistake:
 * "Just do BFS/DFS traversal."
 *
 * Failure:
 * Traversal order ≠ dependency-safe order.
 *
 * Example:
 *
 * 1 depends on 0
 *
 * If traversal starts at 1:
 * invalid ordering produced.
 *
 * Dependency satisfaction must be enforced explicitly.
 *
 * ====================================================================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * ====================================================================================================
 *
 * --------------------------------------------------------------------------------
 * Wrong Approach 1:
 * Simple DFS Traversal
 * --------------------------------------------------------------------------------
 *
 * Why it seems correct:
 * DFS explores deeply.
 *
 * Invariant violation:
 * Node may be processed before prerequisites complete.
 *
 * Counterexample:
 *
 * 0 -> 1
 *
 * Starting DFS from 1:
 * ordering becomes [1,0]
 *
 * invalid.
 *
 * --------------------------------------------------------------------------------
 * Wrong Approach 2:
 * Process Any Unvisited Node
 * --------------------------------------------------------------------------------
 *
 * Why it seems correct:
 * "Eventually everything gets visited."
 *
 * Invariant violation:
 * ignores prerequisite readiness.
 *
 * Ordering correctness destroyed.
 *
 * --------------------------------------------------------------------------------
 * Wrong Approach 3:
 * Forget Cycle Validation
 * --------------------------------------------------------------------------------
 *
 * Why it seems correct:
 * queue became empty.
 *
 * Hidden bug:
 * queue may empty because cycle deadlocked graph.
 *
 * Example:
 *
 * 0 -> 1
 * 1 -> 0
 *
 * No node reaches indegree 0.
 *
 * --------------------------------------------------------------------------------
 * Wrong Approach 4:
 * Reverse Edge Direction
 * --------------------------------------------------------------------------------
 *
 * Common interview trap.
 *
 * Correct:
 * prerequisite -> course
 *
 * If reversed:
 * indegrees become meaningless.
 *
 * ====================================================================================================
 * ⚙️ HOW TO PHYSICALLY ASSEMBLE THE CODE
 * ====================================================================================================
 *
 * 🛠️ IMPLEMENTATION BLUEPRINT
 *
 * STEP 1:
 * Create adjacency list.
 *
 * STEP 2:
 * Create indegree array.
 *
 * STEP 3:
 * Build graph:
 *
 * for each [a,b]:
 *     b -> a
 *     indegree[a]++
 *
 * STEP 4:
 * Initialize queue with all indegree 0 nodes.
 *
 * STEP 5:
 * Create answer array.
 *
 * STEP 6:
 * BFS loop:
 *
 * while queue not empty:
 *     pop node
 *     add to answer
 *
 *     for neighbors:
 *         reduce indegree
 *
 *         if indegree becomes 0:
 *             enqueue
 *
 * STEP 7:
 * Validate:
 *
 * if processed != numCourses:
 *     return empty array
 *
 * STEP 8:
 * return answer
 *
 * --------------------------------------------------------------------------------
 * 🧾 ULTRA-COMPACT PSEUDOCODE (MEMORY SCAFFOLD)
 * --------------------------------------------------------------------------------
 *
 * build graph
 * build indegree
 *
 * push indegree 0 nodes
 *
 * while queue not empty:
 *     node = pop
 *     add to answer
 *
 *     for neighbor:
 *         indegree--
 *
 *         if indegree == 0:
 *             push
 *
 * if processed != n:
 *     return empty
 *
 * return answer
 *
 * ====================================================================================================
 * 6. PRIMARY PROBLEM — SOLUTION CLASSES
 * ====================================================================================================
 */
public class CourseSchedule {

    /**
     * ================================================================================================
     * Brute Force Solution
     * ================================================================================================
     *
     * Core Idea:
     * Repeatedly scan all courses.
     * Pick courses whose prerequisites are already completed.
     *
     * Invariant Enforced:
     * Only executable courses are selected.
     *
     * Limitation:
     * Extremely inefficient repeated rescanning.
     *
     * Time Complexity:
     * O(V * (V + E))
     *
     * Space Complexity:
     * O(V)
     *
     * Interview Preference:
     * Low
     * Only useful as reasoning bridge toward Kahn's algorithm.
     */
    static class BruteForceSolution {

        public int[] findOrder(int numCourses, int[][] prerequisites) {

            boolean[] completed = new boolean[numCourses];
            int[] order = new int[numCourses];

            int completedCount = 0;

            while (completedCount < numCourses) {

                boolean progressMade = false;

                // Try every course repeatedly.
                for (int course = 0; course < numCourses; course++) {

                    if (completed[course]) {
                        continue;
                    }

                    boolean canTake = true;

                    // Check whether all prerequisites are done.
                    for (int[] edge : prerequisites) {

                        int next = edge[0];
                        int prereq = edge[1];

                        if (next == course && !completed[prereq]) {
                            canTake = false;
                            break;
                        }
                    }

                    if (canTake) {

                        completed[course] = true;

                        order[completedCount++] = course;

                        progressMade = true;
                    }
                }

                // No progress means cycle deadlock.
                if (!progressMade) {
                    return new int[0];
                }
            }

            return order;
        }
    }

    /**
     * ================================================================================================
     * Improved Solution — DFS Topological Sort
     * ================================================================================================
     *
     * Core Idea:
     * DFS postorder.
     *
     * Invariant:
     * Node enters answer AFTER all descendants processed.
     *
     * Limitation Fixed:
     * Avoid repeated rescanning.
     *
     * Time Complexity:
     * O(V + E)
     *
     * Space Complexity:
     * O(V + E)
     *
     * Interview Preference:
     * Medium
     *
     * Harder than Kahn BFS because:
     * - recursion states
     * - cycle handling
     * - postorder reversal
     */
    static class DFSTopologicalSortSolution {

        private static final int UNVISITED = 0;
        private static final int VISITING = 1;
        private static final int VISITED = 2;

        public int[] findOrder(int numCourses, int[][] prerequisites) {

            List<List<Integer>> graph = buildGraph(numCourses, prerequisites);

            int[] state = new int[numCourses];

            List<Integer> topo = new ArrayList<>();

            for (int node = 0; node < numCourses; node++) {

                if (state[node] == UNVISITED) {

                    if (hasCycle(node, graph, state, topo)) {
                        return new int[0];
                    }
                }
            }

            Collections.reverse(topo);

            int[] answer = new int[numCourses];

            for (int i = 0; i < numCourses; i++) {
                answer[i] = topo.get(i);
            }

            return answer;
        }

        private boolean hasCycle(int node,
                                 List<List<Integer>> graph,
                                 int[] state,
                                 List<Integer> topo) {

            // Back-edge detected.
            if (state[node] == VISITING) {
                return true;
            }

            // Already processed safely.
            if (state[node] == VISITED) {
                return false;
            }

            state[node] = VISITING;

            for (int neighbor : graph.get(node)) {

                if (hasCycle(neighbor, graph, state, topo)) {
                    return true;
                }
            }

            state[node] = VISITED;

            // Postorder insertion.
            topo.add(node);

            return false;
        }

        private List<List<Integer>> buildGraph(int n, int[][] prerequisites) {

            List<List<Integer>> graph = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }

            for (int[] edge : prerequisites) {

                int next = edge[0];
                int prereq = edge[1];

                graph.get(prereq).add(next);
            }

            return graph;
        }
    }

    /**
     * ================================================================================================
     * Optimal Solution — Interview Preferred
     * Kahn's Algorithm (BFS Topological Sort)
     * ================================================================================================
     *
     * Core Idea:
     * Process only indegree 0 nodes.
     *
     * Core Invariant:
     * Every node inside queue currently has ALL prerequisites satisfied.
     *
     * Limitation Fixed:
     * Natural ordering generation + explicit cycle detection.
     *
     * Time Complexity:
     * O(V + E)
     *
     * Space Complexity:
     * O(V + E)
     *
     * Interview Preference:
     * HIGH
     *
     * Why Interviewers Like It:
     * - clear invariant
     * - iterative
     * - deterministic reasoning
     * - dependency modeling
     * - easy debugging
     */
    static class OptimalKahnBFSSolution {

        public int[] findOrder(int numCourses, int[][] prerequisites) {

            // Handle minimal graph cleanly.
            if (numCourses == 0) {
                return new int[0];
            }

            // Adjacency list:
            // prerequisite -> dependent courses
            List<List<Integer>> graph = new ArrayList<>();

            for (int i = 0; i < numCourses; i++) {
                graph.add(new ArrayList<>());
            }

            // indegree[x] =
            // remaining unmet prerequisites for x
            int[] indegree = new int[numCourses];

            // Build graph carefully.
            for (int[] edge : prerequisites) {

                int nextCourse = edge[0];
                int prerequisiteCourse = edge[1];

                graph.get(prerequisiteCourse).add(nextCourse);

                indegree[nextCourse]++;
            }

            // Queue stores only executable courses.
            Queue<Integer> queue = new ArrayDeque<>();

            // Initially unlocked courses.
            for (int course = 0; course < numCourses; course++) {

                if (indegree[course] == 0) {
                    queue.offer(course);
                }
            }

            int[] order = new int[numCourses];

            int index = 0;

            // Invariant:
            // queue always contains dependency-safe nodes.
            while (!queue.isEmpty()) {

                int currentCourse = queue.poll();

                // Safe to finalize now.
                order[index++] = currentCourse;

                // Completing currentCourse removes one dependency
                // from all dependent courses.
                for (int neighbor : graph.get(currentCourse)) {

                    indegree[neighbor]--;

                    // Newly unlocked course.
                    if (indegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            // If some nodes never processed:
            // cycle exists.
            if (index != numCourses) {
                return new int[0];
            }

            return order;
        }
    }

    /**
     * ====================================================================================================
     * 🟣 INTERVIEW ARTICULATION (NO CODE)
     * ====================================================================================================
     *
     * Verbal Explanation:
     *
     * "I model prerequisites as a directed graph.
     *
     * Edge:
     * prerequisite -> course
     *
     * indegree[x] means:
     * how many prerequisites are still unmet for course x.
     *
     * My invariant is:
     * queue always contains only courses whose prerequisites are fully satisfied.
     *
     * Every time I process a course:
     * I remove its outgoing edges by decrementing neighbor indegrees.
     *
     * When neighbor indegree becomes zero:
     * it becomes executable and enters queue.
     *
     * If I process all nodes:
     * graph was acyclic.
     *
     * Otherwise:
     * some nodes remained dependency-locked forever,
     * meaning a cycle exists."
     *
     * --------------------------------------------------------------------------------
     * Discard Logic
     * --------------------------------------------------------------------------------
     *
     * Once course processed:
     * it never needs reconsideration.
     *
     * --------------------------------------------------------------------------------
     * Correctness Guarantee
     * --------------------------------------------------------------------------------
     *
     * Node enters answer only after all prerequisites processed.
     *
     * --------------------------------------------------------------------------------
     * What Breaks If Changed
     * --------------------------------------------------------------------------------
     *
     * If we enqueue indegree > 0 nodes:
     * ordering may violate prerequisites.
     *
     * --------------------------------------------------------------------------------
     * In-place Feasibility
     * --------------------------------------------------------------------------------
     *
     * Not naturally.
     *
     * Need:
     * graph + indegree tracking.
     *
     * --------------------------------------------------------------------------------
     * Streaming Feasibility
     * --------------------------------------------------------------------------------
     *
     * Partially.
     *
     * If edges dynamically arrive:
     * indegrees must be updated incrementally.
     *
     * --------------------------------------------------------------------------------
     * When NOT To Use
     * --------------------------------------------------------------------------------
     *
     * Do not use if:
     * - graph undirected
     * - dependencies absent
     * - shortest path problem
     * - weighted optimization problem
     *
     * ====================================================================================================
     * 🎯 INTERVIEW RECALL SHEET (30-SECOND RECALL)
     * ====================================================================================================
     *
     * Pattern Trigger:
     * dependency ordering
     *
     * Core Invariant:
     * queue contains only indegree 0 nodes
     *
     * Search Target:
     * valid topological ordering
     *
     * Discard Rule:
     * processed node permanently finalized
     *
     * Common Trap:
     * reversing edge direction
     *
     * Edge Cases:
     * - cycle
     * - isolated nodes
     * - no prerequisites
     * - disconnected graph
     *
     * Interview One-Liner:
     *
     * "Indegree counts remaining prerequisites.
     * Process only fully unlocked courses."
     *
     * Re-derivation Cue:
     *
     * "Dependency unlock simulation."
     *
     * ====================================================================================================
     * 🔄 VARIATIONS & TWEAKS
     * ====================================================================================================
     *
     * --------------------------------------------------------------------------------
     * Variation 1:
     * Return Only Feasibility
     * --------------------------------------------------------------------------------
     *
     * Same invariant.
     *
     * Instead of storing order:
     * just compare processed count.
     *
     * Example:
     * LeetCode 207 — Course Schedule
     *
     * --------------------------------------------------------------------------------
     * Variation 2:
     * Lexicographically Smallest Ordering
     * --------------------------------------------------------------------------------
     *
     * Replace queue with min-heap.
     *
     * Invariant preserved:
     * still process only indegree 0 nodes.
     *
     * --------------------------------------------------------------------------------
     * Variation 3:
     * Dynamic Dependency Updates
     * --------------------------------------------------------------------------------
     *
     * Pattern becomes harder.
     *
     * Need incremental graph maintenance.
     *
     * --------------------------------------------------------------------------------
     * Pattern Break Signals
     * --------------------------------------------------------------------------------
     *
     * If graph weighted:
     * topological sort alone insufficient.
     *
     * If cycles allowed intentionally:
     * ordering impossible.
     *
     * ====================================================================================================
     * ⚫ REINFORCEMENT PROBLEMS
     * ====================================================================================================
     */

    /**
     * --------------------------------------------------------------------------------
     * Reinforcement Problem 1
     * LeetCode 207 — Course Schedule
     * --------------------------------------------------------------------------------
     *
     * Summary:
     * Determine whether all courses can be completed.
     *
     * Invariant Mapping:
     * Same exact indegree invariant.
     *
     * Key Difference:
     * Boolean result only.
     *
     * Edge Cases:
     * - cycle
     * - disconnected graph
     *
     * Interview Trap:
     * forgetting isolated nodes
     */
    static class CourseScheduleCanFinish {

        public boolean canFinish(int numCourses, int[][] prerequisites) {

            List<List<Integer>> graph = new ArrayList<>();

            for (int i = 0; i < numCourses; i++) {
                graph.add(new ArrayList<>());
            }

            int[] indegree = new int[numCourses];

            for (int[] edge : prerequisites) {

                int next = edge[0];
                int prereq = edge[1];

                graph.get(prereq).add(next);

                indegree[next]++;
            }

            Queue<Integer> queue = new ArrayDeque<>();

            for (int i = 0; i < numCourses; i++) {

                if (indegree[i] == 0) {
                    queue.offer(i);
                }
            }

            int processed = 0;

            while (!queue.isEmpty()) {

                int node = queue.poll();

                processed++;

                for (int neighbor : graph.get(node)) {

                    indegree[neighbor]--;

                    if (indegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return processed == numCourses;
        }
    }

    /**
     * --------------------------------------------------------------------------------
     * Reinforcement Problem 2
     * LeetCode 269 — Alien Dictionary
     * --------------------------------------------------------------------------------
     *
     * Summary:
     * Derive valid character ordering from sorted alien words.
     *
     * Invariant Mapping:
     * Character enters ordering only when all preceding constraints satisfied.
     *
     * Edge Case:
     * prefix invalidity
     *
     * Interview Trap:
     * forgetting isolated characters
     */
    static class AlienDictionarySolution {

        public String alienOrder(String[] words) {

            Map<Character, Set<Character>> graph = new HashMap<>();

            Map<Character, Integer> indegree = new HashMap<>();

            // Initialize all characters.
            for (String word : words) {

                for (char ch : word.toCharArray()) {

                    graph.putIfAbsent(ch, new HashSet<>());

                    indegree.putIfAbsent(ch, 0);
                }
            }

            for (int i = 0; i < words.length - 1; i++) {

                String first = words[i];
                String second = words[i + 1];

                // Invalid prefix case.
                if (first.length() > second.length()
                        && first.startsWith(second)) {

                    return "";
                }

                int minLength = Math.min(first.length(), second.length());

                for (int j = 0; j < minLength; j++) {

                    char u = first.charAt(j);
                    char v = second.charAt(j);

                    if (u != v) {

                        if (!graph.get(u).contains(v)) {

                            graph.get(u).add(v);

                            indegree.put(v, indegree.get(v) + 1);
                        }

                        break;
                    }
                }
            }

            Queue<Character> queue = new ArrayDeque<>();

            for (char ch : indegree.keySet()) {

                if (indegree.get(ch) == 0) {
                    queue.offer(ch);
                }
            }

            StringBuilder answer = new StringBuilder();

            while (!queue.isEmpty()) {

                char current = queue.poll();

                answer.append(current);

                for (char neighbor : graph.get(current)) {

                    indegree.put(neighbor, indegree.get(neighbor) - 1);

                    if (indegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return answer.length() == indegree.size()
                    ? answer.toString()
                    : "";
        }
    }

    /**
     * --------------------------------------------------------------------------------
     * Reinforcement Problem 3
     * LeetCode 1136 — Parallel Courses
     * --------------------------------------------------------------------------------
     *
     * Summary:
     * Minimum semesters required.
     *
     * Invariant Mapping:
     * Semester queue contains currently executable courses.
     *
     * Interview Trap:
     * forgetting level-by-level BFS meaning.
     */
    static class ParallelCoursesSolution {

        public int minimumSemesters(int n, int[][] relations) {

            List<List<Integer>> graph = new ArrayList<>();

            for (int i = 0; i <= n; i++) {
                graph.add(new ArrayList<>());
            }

            int[] indegree = new int[n + 1];

            for (int[] edge : relations) {

                int prereq = edge[0];
                int next = edge[1];

                graph.get(prereq).add(next);

                indegree[next]++;
            }

            Queue<Integer> queue = new ArrayDeque<>();

            for (int course = 1; course <= n; course++) {

                if (indegree[course] == 0) {
                    queue.offer(course);
                }
            }

            int semesters = 0;
            int completed = 0;

            while (!queue.isEmpty()) {

                int size = queue.size();

                semesters++;

                for (int i = 0; i < size; i++) {

                    int current = queue.poll();

                    completed++;

                    for (int neighbor : graph.get(current)) {

                        indegree[neighbor]--;

                        if (indegree[neighbor] == 0) {
                            queue.offer(neighbor);
                        }
                    }
                }
            }

            return completed == n ? semesters : -1;
        }
    }

    /**
     * ====================================================================================================
     * 🧩 RELATED PROBLEMS
     * ====================================================================================================
     */

    /**
     * --------------------------------------------------------------------------------
     * Related Problem 1
     * LeetCode 444 — Sequence Reconstruction
     * --------------------------------------------------------------------------------
     *
     * Same / Modified / Broken Invariant:
     * Same invariant +
     * uniqueness constraint.
     *
     * Edge Case:
     * multiple valid nodes simultaneously.
     *
     * Interview Note:
     * Queue size > 1 means ordering not unique.
     */
    static class SequenceReconstructionSolution {

        public boolean sequenceReconstruction(int[] nums, List<List<Integer>> sequences) {

            Map<Integer, Set<Integer>> graph = new HashMap<>();
            Map<Integer, Integer> indegree = new HashMap<>();

            for (int num : nums) {
                graph.put(num, new HashSet<>());
                indegree.put(num, 0);
            }

            for (List<Integer> seq : sequences) {

                for (int num : seq) {

                    if (!graph.containsKey(num)) {
                        return false;
                    }
                }

                for (int i = 1; i < seq.size(); i++) {

                    int u = seq.get(i - 1);
                    int v = seq.get(i);

                    if (graph.get(u).add(v)) {
                        indegree.put(v, indegree.get(v) + 1);
                    }
                }
            }

            Queue<Integer> queue = new ArrayDeque<>();

            for (int node : indegree.keySet()) {

                if (indegree.get(node) == 0) {
                    queue.offer(node);
                }
            }

            int index = 0;

            while (!queue.isEmpty()) {

                // Multiple choices => non-unique ordering.
                if (queue.size() > 1) {
                    return false;
                }

                int current = queue.poll();

                if (nums[index++] != current) {
                    return false;
                }

                for (int neighbor : graph.get(current)) {

                    indegree.put(neighbor, indegree.get(neighbor) - 1);

                    if (indegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return index == nums.length;
        }
    }

    /**
     * --------------------------------------------------------------------------------
     * Related Problem 2
     * LeetCode 802 — Find Eventual Safe States
     * --------------------------------------------------------------------------------
     *
     * Modified Invariant:
     * reverse graph + outdegree reduction.
     *
     * Edge Case:
     * cycles create unsafe nodes.
     *
     * Interview Note:
     * same dependency elimination principle.
     */
    static class EventualSafeStatesSolution {

        public List<Integer> eventualSafeNodes(int[][] graph) {

            int n = graph.length;

            List<List<Integer>> reverse = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                reverse.add(new ArrayList<>());
            }

            int[] outdegree = new int[n];

            for (int node = 0; node < n; node++) {

                outdegree[node] = graph[node].length;

                for (int neighbor : graph[node]) {
                    reverse.get(neighbor).add(node);
                }
            }

            Queue<Integer> queue = new ArrayDeque<>();

            for (int node = 0; node < n; node++) {

                if (outdegree[node] == 0) {
                    queue.offer(node);
                }
            }

            List<Integer> safe = new ArrayList<>();

            while (!queue.isEmpty()) {

                int current = queue.poll();

                safe.add(current);

                for (int prev : reverse.get(current)) {

                    outdegree[prev]--;

                    if (outdegree[prev] == 0) {
                        queue.offer(prev);
                    }
                }
            }

            Collections.sort(safe);

            return safe;
        }
    }

    /**
     * --------------------------------------------------------------------------------
     * Related Problem 3
     * LeetCode 1203 — Sort Items by Groups Respecting Dependencies
     * --------------------------------------------------------------------------------
     *
     * Modified Invariant:
     * two-level topological sort.
     *
     * Edge Case:
     * inter-group dependencies.
     *
     * Interview Note:
     * layered DAG reasoning.
     *
     * Concise simplified implementation shown.
     */
    static class SimpleTopologicalUtility {

        public List<Integer> topoSort(int nodes,
                                      List<List<Integer>> graph,
                                      int[] indegree) {

            Queue<Integer> queue = new ArrayDeque<>();

            for (int i = 0; i < nodes; i++) {

                if (indegree[i] == 0) {
                    queue.offer(i);
                }
            }

            List<Integer> order = new ArrayList<>();

            while (!queue.isEmpty()) {

                int current = queue.poll();

                order.add(current);

                for (int neighbor : graph.get(current)) {

                    indegree[neighbor]--;

                    if (indegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return order.size() == nodes
                    ? order
                    : new ArrayList<>();
        }
    }

    /**
     * ====================================================================================================
     * 🧠 MASTERY CHECKLIST
     * ====================================================================================================
     *
     * ✔ Invariant
     *
     * Queue contains only indegree 0 nodes.
     *
     * ✔ Search Target
     *
     * Valid dependency-safe ordering.
     *
     * ✔ Discard Rule
     *
     * Processed node permanently finalized.
     *
     * ✔ Termination Logic
     *
     * Queue empty.
     *
     * Then compare processed count vs total nodes.
     *
     * ✔ Naive Failure
     *
     * Traversal order does not guarantee dependency correctness.
     *
     * ✔ Edge Cases
     *
     * - cycle
     * - disconnected graph
     * - isolated nodes
     * - no prerequisites
     * - multiple valid orders
     *
     * ✔ Debugging Readiness
     *
     * Debug checkpoints:
     * - graph direction
     * - indegree correctness
     * - queue initialization
     * - processed count
     *
     * ✔ Variant Readiness
     *
     * Can adapt to:
     * - feasibility
     * - uniqueness
     * - semester batching
     * - dependency unlock systems
     *
     * ✔ Pattern Boundary
     *
     * Not for:
     * - weighted shortest path
     * - undirected graphs
     * - cyclic execution systems
     *
     * ====================================================================================================
     * 🧪 main() + SELF-VERIFYING TESTS
     * ====================================================================================================
     */

    public static void main(String[] args) {

        OptimalKahnBFSSolution solution = new OptimalKahnBFSSolution();

        // --------------------------------------------------------------------------------
        // Test 1:
        // Basic linear dependency
        // 0 -> 1
        // Why:
        // simplest valid topological order
        // --------------------------------------------------------------------------------
        {
            int numCourses = 2;
            int[][] prerequisites = {
                    {1, 0}
            };

            int[] result = solution.findOrder(numCourses, prerequisites);

            assert isValidTopologicalOrder(numCourses, prerequisites, result);

            System.out.println("Test 1 Passed");
        }

        // --------------------------------------------------------------------------------
        // Test 2:
        // Diamond dependency
        // Why:
        // multiple valid orderings
        // --------------------------------------------------------------------------------
        {
            int numCourses = 4;

            int[][] prerequisites = {
                    {1, 0},
                    {2, 0},
                    {3, 1},
                    {3, 2}
            };

            int[] result = solution.findOrder(numCourses, prerequisites);

            assert isValidTopologicalOrder(numCourses, prerequisites, result);

            System.out.println("Test 2 Passed");
        }

        // --------------------------------------------------------------------------------
        // Test 3:
        // No prerequisites
        // Why:
        // isolated nodes
        // --------------------------------------------------------------------------------
        {
            int numCourses = 3;

            int[][] prerequisites = {};

            int[] result = solution.findOrder(numCourses, prerequisites);

            assert result.length == 3;

            assert isValidTopologicalOrder(numCourses, prerequisites, result);

            System.out.println("Test 3 Passed");
        }

        // --------------------------------------------------------------------------------
        // Test 4:
        // Cycle detection
        // Why:
        // must return empty
        // --------------------------------------------------------------------------------
        {
            int numCourses = 2;

            int[][] prerequisites = {
                    {0, 1},
                    {1, 0}
            };

            int[] result = solution.findOrder(numCourses, prerequisites);

            assert result.length == 0;

            System.out.println("Test 4 Passed");
        }

        // --------------------------------------------------------------------------------
        // Test 5:
        // Disconnected graph
        // Why:
        // ensure all components handled
        // --------------------------------------------------------------------------------
        {
            int numCourses = 6;

            int[][] prerequisites = {
                    {1, 0},
                    {3, 2}
            };

            int[] result = solution.findOrder(numCourses, prerequisites);

            assert isValidTopologicalOrder(numCourses, prerequisites, result);

            System.out.println("Test 5 Passed");
        }

        // --------------------------------------------------------------------------------
        // Test 6:
        // Single node
        // Why:
        // boundary case
        // --------------------------------------------------------------------------------
        {
            int numCourses = 1;

            int[][] prerequisites = {};

            int[] result = solution.findOrder(numCourses, prerequisites);

            assert result.length == 1;
            assert result[0] == 0;

            System.out.println("Test 6 Passed");
        }

        // --------------------------------------------------------------------------------
        // Test 7:
        // Long chain
        // Why:
        // ordering strictness
        // --------------------------------------------------------------------------------
        {
            int numCourses = 5;

            int[][] prerequisites = {
                    {1, 0},
                    {2, 1},
                    {3, 2},
                    {4, 3}
            };

            int[] result = solution.findOrder(numCourses, prerequisites);

            assert isValidTopologicalOrder(numCourses, prerequisites, result);

            System.out.println("Test 7 Passed");
        }

        System.out.println();
        System.out.println("All tests passed successfully.");

        System.out.println();
        System.out.println("I understand the invariant.");
        System.out.println("I can re-derive the solution.");
        System.out.println("I can physically reconstruct the implementation under pressure.");
        System.out.println("This chapter is complete.");
    }

    /**
     * Helper verifier:
     * Ensures prerequisite ordering validity.
     */
    private static boolean isValidTopologicalOrder(int numCourses,
                                                   int[][] prerequisites,
                                                   int[] order) {

        if (order.length != numCourses) {
            return false;
        }

        int[] position = new int[numCourses];

        for (int i = 0; i < order.length; i++) {
            position[order[i]] = i;
        }

        for (int[] edge : prerequisites) {

            int next = edge[0];
            int prereq = edge[1];

            // prerequisite must appear earlier
            if (position[prereq] > position[next]) {
                return false;
            }
        }

        return true;
    }
}


