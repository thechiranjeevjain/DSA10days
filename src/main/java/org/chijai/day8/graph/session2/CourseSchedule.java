package org.chijai.day8.graph.session2;

import java.util.*;

/**
 * ====================================================================================================
 * COURSE SCHEDULE / TOPOLOGICAL SORT FAMILY — INTERVIEW MASTER FILE
 * ====================================================================================================
 *
 * PRIMARY ANCHOR:
 * LeetCode 210 — Course Schedule II
 *
 * CANONICAL PATTERN:
 * Topological Sort using BFS (Kahn's Algorithm)
 *
 * ----------------------------------------------------------------------------------------------------
 * WHY THIS FILE IS STRUCTURED THIS WAY
 * ----------------------------------------------------------------------------------------------------
 *
 * This is an INTERVIEW-RECALL file, not a DRY production-code library.
 *
 * Therefore:
 *
 *     graph construction + indegree construction
 *
 * are intentionally repeated inside the important variants.
 *
 * Why?
 *
 *     controlled repetition
 *          ->
 *     same skeleton repeatedly visible
 *          ->
 *     easier reconstruction under pressure
 *
 * We intentionally DO NOT hide the core logic behind:
 *
 *     Graph g = buildGraph(...)
 *
 * because the most important thing to remember is exactly how:
 *
 *     [course, prerequisite]
 *
 * becomes:
 *
 *     prerequisite -> course
 *     indegree[course]++
 *
 * ----------------------------------------------------------------------------------------------------
 * MASTER MENTAL MODEL
 * ----------------------------------------------------------------------------------------------------
 *
 * prerequisites[i] = [course, prerequisite]
 *
 * Example:
 *
 *     [1, 0]
 *
 * means:
 *
 *     take 0 before 1
 *
 * therefore directed edge:
 *
 *     0 ----------> 1
 *     prereq       course
 *
 *
 * indegree[x]
 *
 *     = number of prerequisites of x
 *       that are STILL unresolved.
 *
 * Therefore:
 *
 *     indegree == 0
 *
 * means:
 *
 *     "THIS NODE IS AVAILABLE TO PROCESS RIGHT NOW."
 *
 *
 *                              DEPENDENCIES
 *                                   |
 *                                   v
 *                           DIRECTED GRAPH
 *                                   |
 *                                   v
 *                    TOPOLOGICAL SORT — KAHN BFS
 *                                   |
 *                                   v
 *                     prerequisite -> dependent
 *                                   |
 *                                   v
 *                     indegree = unresolved deps
 *                                   |
 *                                   v
 *                     all indegree 0 -> queue
 *                                   |
 *                                   v
 *                                process
 *                                   |
 *                                   v
 *                        release outgoing edges
 *                          indegree[neighbor]--
 *                                   |
 *                                   v
 *                        newly 0 -> queue
 *                                   |
 *                                   v
 *                         queue eventually empty
 *                                   |
 *                         +---------+---------+
 *                         |                   |
 *                    processed == V      processed < V
 *                         |                   |
 *                       VALID               CYCLE
 *
 *
 * ----------------------------------------------------------------------------------------------------
 * 30-SECOND RECALL
 * ----------------------------------------------------------------------------------------------------
 *
 * Trigger words:
 *
 *     prerequisite
 *     dependency
 *     before / after
 *     build order
 *     task ordering
 *     execution ordering
 *
 * Immediate thought:
 *
 *     dependency ordering
 *          ->
 *     directed graph
 *          ->
 *     topological sort
 *          ->
 *     Kahn BFS + indegree
 *
 * Core invariant:
 *
 *     queue contains only nodes whose dependencies are fully satisfied.
 *
 * Generic engine:
 *
 *     build graph
 *     build indegree
 *     enqueue all indegree 0
 *
 *     while queue not empty:
 *         pop
 *         process
 *
 *         for each dependent:
 *             indegree--
 *
 *             if indegree == 0:
 *                 enqueue
 *
 *     processed == V ? success : cycle
 *
 *
 * ----------------------------------------------------------------------------------------------------
 * FAMILY MAP — SAME ENGINE, SMALL MUTATIONS
 * ----------------------------------------------------------------------------------------------------
 *
 * KAHN ENGINE
 *     |
 *     +-- Can everything finish?
 *     |      -> count processed
 *     |      -> Course Schedule I / LC 207
 *     |
 *     +-- Return one valid ordering?
 *     |      -> store popped nodes
 *     |      -> Course Schedule II / LC 210
 *     |
 *     +-- Smallest valid ordering?
 *     |      -> Queue -> PriorityQueue
 *     |
 *     +-- Is the ordering unique?
 *     |      -> queue.size() must always be exactly 1
 *     |      -> Sequence Reconstruction idea / LC 444
 *     |
 *     +-- Minimum number of rounds / semesters?
 *     |      -> BFS level-by-level
 *     |      -> Parallel Courses / LC 1136
 *     |
 *     +-- Tasks have durations?
 *     |      -> topo traversal + DAG DP
 *     |      -> wait for the slowest prerequisite chain
 *     |
 *     +-- Constraints must be inferred first?
 *     |      -> infer edges, then same Kahn engine
 *     |      -> Alien Dictionary / LC 269
 *     |
 *     +-- Nodes are safe if every path eventually terminates?
 *     |      -> reverse graph + OUTDEGREE elimination
 *     |      -> Eventual Safe States / LC 802
 *     |
 *     +-- Need ALL valid orders?
 *            -> every currently-zero-indegree node is a backtracking choice
 *
 *
 * ----------------------------------------------------------------------------------------------------
 * IMPORTANT PRECISION
 * ----------------------------------------------------------------------------------------------------
 *
 * DFS is NOT "wrong" for topological sorting.
 *
 * Correct DFS topological sorting requires:
 *
 *     3-state cycle detection
 *     + postorder insertion
 *     + reversal
 *
 * What is wrong is:
 *
 *     "plain DFS traversal order is automatically a valid dependency order."
 *
 * Kahn BFS is preferred here because:
 *
 *     - iterative
 *     - explicit readiness invariant
 *     - natural cycle detection
 *     - variants are easy to derive
 *
 *
 * ----------------------------------------------------------------------------------------------------
 * COMPLEXITY OF THE CANONICAL KAHN ENGINE
 * ----------------------------------------------------------------------------------------------------
 *
 * V = vertices / courses
 * E = dependency edges
 *
 * Time:  O(V + E)
 * Space: O(V + E)
 *
 * ====================================================================================================
 */
public class CourseSchedule {

    // =================================================================================================
    // 1. COURSE SCHEDULE I — CAN EVERYTHING FINISH?
    // LeetCode 207
    //
    // SAME KAHN ENGINE.
    // Mutation:
    //     do not store the ordering;
    //     only count how many nodes were processed.
    // =================================================================================================

    static class CourseScheduleI {

        public boolean canFinish(int numCourses, int[][] prerequisites) {

            List<List<Integer>> graph = new ArrayList<>();

            for (int i = 0; i < numCourses; i++) {
                graph.add(new ArrayList<>());
            }

            int[] indegree = new int[numCourses];

            // [course, prereq] => prereq -> course
            for (int[] p : prerequisites) {
                int course = p[0];
                int prereq = p[1];

                graph.get(prereq).add(course);
                indegree[course]++;
            }

            Queue<Integer> q = new ArrayDeque<>();

            for (int course = 0; course < numCourses; course++) {
                if (indegree[course] == 0) {
                    q.offer(course);
                }
            }

            int processed = 0;

            while (!q.isEmpty()) {

                int course = q.poll();
                processed++;

                for (int next : graph.get(course)) {

                    indegree[next]--;

                    if (indegree[next] == 0) {
                        q.offer(next);
                    }
                }
            }

            return processed == numCourses;
        }
    }


    // =================================================================================================
    // 2. COURSE SCHEDULE II — RETURN ONE VALID ORDER
    // LeetCode 210
    //
    // THIS IS THE PRIMARY CANONICAL SOLUTION TO RETAIN.
    //
    // Core invariant:
    //     every node inside q currently has all prerequisites satisfied.
    // =================================================================================================

    static class CourseScheduleII {

        public int[] findOrder(int numCourses, int[][] prerequisites) {

            List<List<Integer>> graph = new ArrayList<>();

            for (int i = 0; i < numCourses; i++) {
                graph.add(new ArrayList<>());
            }

            int[] indegree = new int[numCourses];

            // IMPORTANT:
            // There is NO inner j-loop here.
            // Each prerequisite pair represents ONE directed edge.
            for (int[] p : prerequisites) {
                int course = p[0];
                int prereq = p[1];

                graph.get(prereq).add(course);
                indegree[course]++;
            }

            Queue<Integer> q = new ArrayDeque<>();

            for (int course = 0; course < numCourses; course++) {
                if (indegree[course] == 0) {
                    q.offer(course);
                }
            }

            int[] order = new int[numCourses];
            int index = 0;

            while (!q.isEmpty()) {

                int course = q.poll();

                // Safe to finalize because indegree == 0.
                order[index++] = course;

                for (int next : graph.get(course)) {

                    // Current prerequisite has now been satisfied.
                    indegree[next]--;

                    // Newly unlocked node.
                    if (indegree[next] == 0) {
                        q.offer(next);
                    }
                }
            }

            // Leftover nodes => cycle => no complete topological order.
            return index == numCourses
                    ? order
                    : new int[0];
        }
    }


    // =================================================================================================
    // 3. LEXICOGRAPHICALLY SMALLEST TOPOLOGICAL ORDER
    //
    // Mutation:
    //
    //     Queue
    //       ->
    //     PriorityQueue
    //
    // We still process ONLY indegree-0 nodes.
    // Among currently valid choices, choose the smallest numbered node.
    //
    // Time:
    //     O((V + E) log V)
    // =================================================================================================

    static class LexicographicallySmallestOrder {

        public int[] findOrder(int numCourses, int[][] prerequisites) {

            List<List<Integer>> graph = new ArrayList<>();

            for (int i = 0; i < numCourses; i++) {
                graph.add(new ArrayList<>());
            }

            int[] indegree = new int[numCourses];

            for (int[] p : prerequisites) {
                int course = p[0];
                int prereq = p[1];

                graph.get(prereq).add(course);
                indegree[course]++;
            }

            PriorityQueue<Integer> q = new PriorityQueue<>();

            for (int course = 0; course < numCourses; course++) {
                if (indegree[course] == 0) {
                    q.offer(course);
                }
            }

            int[] order = new int[numCourses];
            int index = 0;

            while (!q.isEmpty()) {

                int course = q.poll();
                order[index++] = course;

                for (int next : graph.get(course)) {

                    indegree[next]--;

                    if (indegree[next] == 0) {
                        q.offer(next);
                    }
                }
            }

            return index == numCourses
                    ? order
                    : new int[0];
        }
    }


    // =================================================================================================
    // 4. IS THE TOPOLOGICAL ORDER UNIQUE?
    //
    // Key observation:
    //
    //     q.size() > 1
    //
    // means:
    //
    //     there are at least two currently-valid next choices,
    //
    // therefore:
    //
    //     more than one topological ordering exists.
    //
    // For uniqueness:
    //
    //     queue size must be EXACTLY 1 at every processing step.
    // =================================================================================================

    static class UniqueTopologicalOrder {

        public boolean hasUniqueOrder(int numCourses, int[][] prerequisites) {

            List<List<Integer>> graph = new ArrayList<>();

            for (int i = 0; i < numCourses; i++) {
                graph.add(new ArrayList<>());
            }

            int[] indegree = new int[numCourses];

            for (int[] p : prerequisites) {
                int course = p[0];
                int prereq = p[1];

                graph.get(prereq).add(course);
                indegree[course]++;
            }

            Queue<Integer> q = new ArrayDeque<>();

            for (int course = 0; course < numCourses; course++) {
                if (indegree[course] == 0) {
                    q.offer(course);
                }
            }

            int processed = 0;

            while (!q.isEmpty()) {

                if (q.size() != 1) {
                    return false;
                }

                int course = q.poll();
                processed++;

                for (int next : graph.get(course)) {

                    indegree[next]--;

                    if (indegree[next] == 0) {
                        q.offer(next);
                    }
                }
            }

            return processed == numCourses;
        }
    }


    // =================================================================================================
    // 5. PARALLEL COURSES — MINIMUM SEMESTERS
    // LeetCode 1136
    //
    // relations[i] = [prerequisite, nextCourse]
    //
    // IMPORTANT:
    // The input orientation here is different from LC 207/210.
    //
    // Mutation:
    //
    //     normal Kahn BFS
    //          +
    //     process one queue LEVEL at a time
    //
    // Every level = one semester because all currently-unlocked courses can run in parallel.
    // =================================================================================================

    static class ParallelCourses {

        public int minimumSemesters(int n, int[][] relations) {

            List<List<Integer>> graph = new ArrayList<>();

            // Courses are labeled 1..n.
            for (int i = 0; i <= n; i++) {
                graph.add(new ArrayList<>());
            }

            int[] indegree = new int[n + 1];

            for (int[] relation : relations) {
                int prereq = relation[0];
                int course = relation[1];

                graph.get(prereq).add(course);
                indegree[course]++;
            }

            Queue<Integer> q = new ArrayDeque<>();

            for (int course = 1; course <= n; course++) {
                if (indegree[course] == 0) {
                    q.offer(course);
                }
            }

            int semesters = 0;
            int completed = 0;

            while (!q.isEmpty()) {

                int size = q.size();
                semesters++;

                while (size-- > 0) {

                    int course = q.poll();
                    completed++;

                    for (int next : graph.get(course)) {

                        indegree[next]--;

                        if (indegree[next] == 0) {
                            q.offer(next);
                        }
                    }
                }
            }

            return completed == n
                    ? semesters
                    : -1;
        }
    }


    // =================================================================================================
    // 6. TASKS / COURSES HAVE DURATIONS — DAG DP / CRITICAL PATH
    //
    // Input:
    //
    //     prerequisites[i] = [course, prerequisite]
    //     duration[course]  = time required by that course
    //
    // Mutation:
    //
    //     Kahn traversal
    //          +
    //     earliestFinish DP
    //
    // For edge:
    //
    //     course -> next
    //
    // update:
    //
    //     earliestFinish[next]
    //         = max(
    //               earliestFinish[next],
    //               earliestFinish[course] + duration[next]
    //           )
    //
    // WHY max?
    //
    // A node with multiple prerequisites must wait for the SLOWEST prerequisite chain.
    // =================================================================================================

    static class MinimumCompletionTime {

        public int minimumTime(int numCourses,
                               int[][] prerequisites,
                               int[] duration) {

            List<List<Integer>> graph = new ArrayList<>();

            for (int i = 0; i < numCourses; i++) {
                graph.add(new ArrayList<>());
            }

            int[] indegree = new int[numCourses];

            for (int[] p : prerequisites) {
                int course = p[0];
                int prereq = p[1];

                graph.get(prereq).add(course);
                indegree[course]++;
            }

            Queue<Integer> q = new ArrayDeque<>();

            int[] earliestFinish = duration.clone();

            for (int course = 0; course < numCourses; course++) {
                if (indegree[course] == 0) {
                    q.offer(course);
                }
            }

            int processed = 0;
            int answer = 0;

            while (!q.isEmpty()) {

                int course = q.poll();
                processed++;

                answer = Math.max(answer, earliestFinish[course]);

                for (int next : graph.get(course)) {

                    earliestFinish[next] = Math.max(
                            earliestFinish[next],
                            earliestFinish[course] + duration[next]
                    );

                    indegree[next]--;

                    if (indegree[next] == 0) {
                        q.offer(next);
                    }
                }
            }

            return processed == numCourses
                    ? answer
                    : -1;
        }
    }


    // =================================================================================================
    // 7. ALIEN DICTIONARY
    // LeetCode 269
    //
    // New difficulty:
    //
    //     edges are NOT directly given.
    //
    // We first infer ordering constraints from adjacent sorted words.
    //
    // Then:
    //
    //     SAME KAHN ENGINE.
    //
    // Critical rule:
    //
    //     Only the FIRST differing character between adjacent words creates an ordering constraint.
    //
    // Prefix invalidity:
    //
    //     ["abc", "ab"]
    //
    // is impossible because a longer word cannot appear before its exact prefix.
    // =================================================================================================

    static class AlienDictionary {

        public String alienOrder(String[] words) {

            Map<Character, Set<Character>> graph = new HashMap<>();
            Map<Character, Integer> indegree = new HashMap<>();

            // Include every character, even isolated ones.
            for (String word : words) {
                for (char ch : word.toCharArray()) {
                    graph.putIfAbsent(ch, new HashSet<>());
                    indegree.putIfAbsent(ch, 0);
                }
            }

            for (int i = 0; i < words.length - 1; i++) {

                String first = words[i];
                String second = words[i + 1];

                if (first.length() > second.length()
                        && first.startsWith(second)) {
                    return "";
                }

                int length = Math.min(first.length(), second.length());

                for (int j = 0; j < length; j++) {

                    char from = first.charAt(j);
                    char to = second.charAt(j);

                    if (from != to) {

                        // Avoid duplicate edge => avoid double indegree count.
                        if (graph.get(from).add(to)) {
                            indegree.put(to, indegree.get(to) + 1);
                        }

                        break;
                    }
                }
            }

            Queue<Character> q = new ArrayDeque<>();

            for (char ch : indegree.keySet()) {
                if (indegree.get(ch) == 0) {
                    q.offer(ch);
                }
            }

            StringBuilder order = new StringBuilder();

            while (!q.isEmpty()) {

                char current = q.poll();
                order.append(current);

                for (char next : graph.get(current)) {

                    indegree.put(next, indegree.get(next) - 1);

                    if (indegree.get(next) == 0) {
                        q.offer(next);
                    }
                }
            }

            return order.length() == indegree.size()
                    ? order.toString()
                    : "";
        }
    }


    // =================================================================================================
    // 8. SEQUENCE RECONSTRUCTION — UNIQUE ORDER MUST MATCH TARGET
    // LeetCode 444
    //
    // Mutation:
    //
    //     Kahn uniqueness check
    //          +
    //     popped node must equal nums[index]
    //
    // Important:
    //
    //     every required value must actually appear in the supplied sequences.
    //
    // Otherwise a target can look "reconstructible" even though the evidence never mentioned a node.
    // =================================================================================================

    static class SequenceReconstruction {

        public boolean sequenceReconstruction(int[] nums,
                                              List<List<Integer>> sequences) {

            Map<Integer, Set<Integer>> graph = new HashMap<>();
            Map<Integer, Integer> indegree = new HashMap<>();

            for (int num : nums) {
                graph.put(num, new HashSet<>());
                indegree.put(num, 0);
            }

            Set<Integer> seen = new HashSet<>();

            for (List<Integer> seq : sequences) {

                for (int num : seq) {

                    if (!graph.containsKey(num)) {
                        return false;
                    }

                    seen.add(num);
                }

                for (int i = 1; i < seq.size(); i++) {

                    int from = seq.get(i - 1);
                    int to = seq.get(i);

                    if (graph.get(from).add(to)) {
                        indegree.put(to, indegree.get(to) + 1);
                    }
                }
            }

            if (seen.size() != nums.length) {
                return false;
            }

            Queue<Integer> q = new ArrayDeque<>();

            for (int node : nums) {
                if (indegree.get(node) == 0) {
                    q.offer(node);
                }
            }

            int index = 0;

            while (!q.isEmpty()) {

                // More than one valid next node => target is not uniquely determined.
                if (q.size() != 1) {
                    return false;
                }

                int current = q.poll();

                if (index >= nums.length || nums[index] != current) {
                    return false;
                }

                index++;

                for (int next : graph.get(current)) {

                    indegree.put(next, indegree.get(next) - 1);

                    if (indegree.get(next) == 0) {
                        q.offer(next);
                    }
                }
            }

            return index == nums.length;
        }
    }


    // =================================================================================================
    // 9. EVENTUAL SAFE STATES
    // LeetCode 802
    //
    // Related elimination pattern:
    //
    // Original graph:
    //
    //     node -> next
    //
    // A terminal node has:
    //
    //     outdegree == 0
    //
    // Terminal nodes are obviously safe.
    //
    // Reverse the graph:
    //
    //     next -> previous
    //
    // Then repeatedly remove safe outgoing dependencies.
    //
    // This is the SAME "dependency elimination" idea, but using:
    //
    //     OUTDEGREE
    //
    // instead of indegree.
    // =================================================================================================

    static class EventualSafeStates {

        public List<Integer> eventualSafeNodes(int[][] graph) {

            int n = graph.length;

            List<List<Integer>> reverse = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                reverse.add(new ArrayList<>());
            }

            int[] outdegree = new int[n];

            for (int node = 0; node < n; node++) {

                outdegree[node] = graph[node].length;

                for (int next : graph[node]) {
                    reverse.get(next).add(node);
                }
            }

            Queue<Integer> q = new ArrayDeque<>();

            for (int node = 0; node < n; node++) {
                if (outdegree[node] == 0) {
                    q.offer(node);
                }
            }

            List<Integer> safe = new ArrayList<>();

            while (!q.isEmpty()) {

                int current = q.poll();
                safe.add(current);

                for (int previous : reverse.get(current)) {

                    outdegree[previous]--;

                    if (outdegree[previous] == 0) {
                        q.offer(previous);
                    }
                }
            }

            Collections.sort(safe);

            return safe;
        }
    }


    // =================================================================================================
    // 10. COURSE SCHEDULE IV — REACHABILITY BOUNDARY CASE
    // LeetCode 1462
    //
    // Question:
    //
    //     Is A a prerequisite of B, directly OR indirectly?
    //
    // This is not primarily:
    //
    //     "produce a topological order"
    //
    // It is:
    //
    //     "answer reachability queries"
    //
    // So do NOT force every dependency problem into Kahn.
    //
    // For moderate V, transitive closure is simple and robust:
    //
    //     reachable[a][b] = whether a can eventually reach b
    //
    // Time:  O(V^3)
    // Space: O(V^2)
    // =================================================================================================

    static class CourseScheduleIV {

        public List<Boolean> checkIfPrerequisite(int numCourses,
                                                 int[][] prerequisites,
                                                 int[][] queries) {

            boolean[][] reachable = new boolean[numCourses][numCourses];

            // Here prerequisite pair is [prereq, course].
            for (int[] p : prerequisites) {
                int prereq = p[0];
                int course = p[1];

                reachable[prereq][course] = true;
            }

            for (int via = 0; via < numCourses; via++) {
                for (int from = 0; from < numCourses; from++) {
                    for (int to = 0; to < numCourses; to++) {

                        reachable[from][to] =
                                reachable[from][to]
                                        || (reachable[from][via] && reachable[via][to]);
                    }
                }
            }

            List<Boolean> answer = new ArrayList<>();

            for (int[] query : queries) {
                answer.add(reachable[query[0]][query[1]]);
            }

            return answer;
        }
    }


    // =================================================================================================
    // 11. ALL VALID TOPOLOGICAL ORDERS
    //
    // Normal Kahn:
    //
    //     choose ANY zero-indegree node.
    //
    // If interviewer asks:
    //
    //     "return ALL possible valid orderings"
    //
    // then every current zero-indegree node becomes a BACKTRACKING CHOICE.
    //
    // Pattern:
    //
    //     choose
    //       ->
    //     decrement neighbors
    //       ->
    //     explore
    //       ->
    //     restore neighbors
    //       ->
    //     undo choice
    //
    // Potentially exponential.
    //
    // This is NOT the default solution.
    // =================================================================================================

    static class AllTopologicalOrders {

        public List<List<Integer>> findAllOrders(int numCourses,
                                                 int[][] prerequisites) {

            List<List<Integer>> graph = new ArrayList<>();

            for (int i = 0; i < numCourses; i++) {
                graph.add(new ArrayList<>());
            }

            int[] indegree = new int[numCourses];

            for (int[] p : prerequisites) {
                int course = p[0];
                int prereq = p[1];

                graph.get(prereq).add(course);
                indegree[course]++;
            }

            List<List<Integer>> result = new ArrayList<>();

            backtrack(
                    graph,
                    indegree,
                    new boolean[numCourses],
                    new ArrayList<>(),
                    result
            );

            return result;
        }

        private void backtrack(List<List<Integer>> graph,
                               int[] indegree,
                               boolean[] used,
                               List<Integer> path,
                               List<List<Integer>> result) {

            if (path.size() == graph.size()) {
                result.add(new ArrayList<>(path));
                return;
            }

            for (int node = 0; node < graph.size(); node++) {

                if (used[node] || indegree[node] != 0) {
                    continue;
                }

                // CHOOSE
                used[node] = true;
                path.add(node);

                for (int next : graph.get(node)) {
                    indegree[next]--;
                }

                // EXPLORE
                backtrack(graph, indegree, used, path, result);

                // UNDO
                for (int next : graph.get(node)) {
                    indegree[next]++;
                }

                path.remove(path.size() - 1);
                used[node] = false;
            }
        }
    }


    // =================================================================================================
    // IMPORTANT BOUNDARY / DO-NOT-OVERGENERALIZE NOTES
    // =================================================================================================
    //
    // 1. DFS topological sort is a valid alternative.
    //
    //    Keep it as SECONDARY knowledge:
    //
    //        state 0 = unvisited
    //        state 1 = visiting
    //        state 2 = visited
    //
    //        visiting -> visiting edge => cycle
    //
    //        add node in postorder
    //        reverse postorder
    //
    //    For this study family, Kahn is the primary reusable anchor.
    //
    //
    // 2. LeetCode 1203 — Sort Items by Groups Respecting Dependencies
    //
    //    This is NOT solved by a single generic topoSort helper.
    //
    //    It requires two-level dependency reasoning:
    //
    //        item graph
    //        +
    //        group graph
    //
    //    Treat it as an advanced extension, not a basic Course Schedule variant.
    //
    //
    // 3. Weighted shortest-path problems are NOT solved merely because a DAG/topological order exists.
    //    Topological order may be part of the solution, but the optimization state is separate.
    //
    //
    // 4. "Traversal order" is not automatically "dependency-safe order".
    //
    // =================================================================================================


    // =================================================================================================
    // SELF-VERIFYING TESTS
    //
    // IMPORTANT:
    //
    // Do NOT rely on Java's plain:
    //
    //     assert condition;
    //
    // because Java assertions are disabled unless the JVM is launched with -ea.
    //
    // check(...) below ALWAYS executes.
    // =================================================================================================

    public static void main(String[] args) {

        testCourseScheduleI();
        testCourseScheduleII();
        testLexicographicallySmallestOrder();
        testUniqueTopologicalOrder();
        testParallelCourses();
        testMinimumCompletionTime();
        testAlienDictionary();
        testSequenceReconstruction();
        testEventualSafeStates();
        testCourseScheduleIV();
        testAllTopologicalOrders();

        System.out.println();
        System.out.println("ALL COURSE-SCHEDULE-FAMILY TESTS PASSED");
    }


    private static void testCourseScheduleI() {

        CourseScheduleI solution = new CourseScheduleI();

        check(
                solution.canFinish(
                        4,
                        new int[][]{
                                {1, 0},
                                {2, 0},
                                {3, 1},
                                {3, 2}
                        }
                ),
                "Course Schedule I — DAG should finish"
        );

        check(
                !solution.canFinish(
                        2,
                        new int[][]{
                                {1, 0},
                                {0, 1}
                        }
                ),
                "Course Schedule I — cycle should fail"
        );

        check(
                solution.canFinish(
                        3,
                        new int[][]{}
                ),
                "Course Schedule I — isolated courses"
        );
    }


    private static void testCourseScheduleII() {

        CourseScheduleII solution = new CourseScheduleII();

        {
            int numCourses = 4;

            int[][] prerequisites = {
                    {1, 0},
                    {2, 0},
                    {3, 1},
                    {3, 2}
            };

            int[] order = solution.findOrder(numCourses, prerequisites);

            check(
                    isValidTopologicalOrder(numCourses, prerequisites, order),
                    "Course Schedule II — diamond"
            );
        }

        {
            int[] order = solution.findOrder(
                    2,
                    new int[][]{
                            {1, 0},
                            {0, 1}
                    }
            );

            check(
                    order.length == 0,
                    "Course Schedule II — cycle returns empty"
            );
        }

        {
            int[] order = solution.findOrder(
                    1,
                    new int[][]{}
            );

            check(
                    Arrays.equals(order, new int[]{0}),
                    "Course Schedule II — single course"
            );
        }

        {
            int numCourses = 6;

            int[][] prerequisites = {
                    {1, 0},
                    {3, 2}
            };

            int[] order = solution.findOrder(numCourses, prerequisites);

            check(
                    isValidTopologicalOrder(numCourses, prerequisites, order),
                    "Course Schedule II — disconnected graph"
            );
        }
    }


    private static void testLexicographicallySmallestOrder() {

        LexicographicallySmallestOrder solution =
                new LexicographicallySmallestOrder();

        int[] order = solution.findOrder(
                4,
                new int[][]{
                        {1, 0},
                        {2, 0},
                        {3, 1},
                        {3, 2}
                }
        );

        check(
                Arrays.equals(order, new int[]{0, 1, 2, 3}),
                "Lexicographically smallest order"
        );
    }


    private static void testUniqueTopologicalOrder() {

        UniqueTopologicalOrder solution =
                new UniqueTopologicalOrder();

        check(
                solution.hasUniqueOrder(
                        4,
                        new int[][]{
                                {1, 0},
                                {2, 1},
                                {3, 2}
                        }
                ),
                "Unique order — chain"
        );

        check(
                !solution.hasUniqueOrder(
                        4,
                        new int[][]{
                                {1, 0},
                                {2, 0},
                                {3, 1},
                                {3, 2}
                        }
                ),
                "Unique order — diamond is not unique"
        );

        check(
                !solution.hasUniqueOrder(
                        2,
                        new int[][]{
                                {1, 0},
                                {0, 1}
                        }
                ),
                "Unique order — cycle"
        );
    }


    private static void testParallelCourses() {

        ParallelCourses solution = new ParallelCourses();

        check(
                solution.minimumSemesters(
                        4,
                        new int[][]{
                                {1, 2},
                                {1, 3},
                                {2, 4},
                                {3, 4}
                        }
                ) == 3,
                "Parallel Courses — 3 semesters"
        );

        check(
                solution.minimumSemesters(
                        2,
                        new int[][]{
                                {1, 2},
                                {2, 1}
                        }
                ) == -1,
                "Parallel Courses — cycle"
        );
    }


    private static void testMinimumCompletionTime() {

        MinimumCompletionTime solution =
                new MinimumCompletionTime();

        int answer = solution.minimumTime(
                4,
                new int[][]{
                        {1, 0},
                        {2, 0},
                        {3, 1},
                        {3, 2}
                },
                new int[]{3, 2, 4, 5}
        );

        // Critical path:
        // 0 -> 2 -> 3
        // 3 + 4 + 5 = 12
        check(
                answer == 12,
                "Minimum completion time — critical path"
        );

        check(
                solution.minimumTime(
                        2,
                        new int[][]{
                                {1, 0},
                                {0, 1}
                        },
                        new int[]{2, 3}
                ) == -1,
                "Minimum completion time — cycle"
        );
    }


    private static void testAlienDictionary() {

        AlienDictionary solution = new AlienDictionary();

        String order = solution.alienOrder(
                new String[]{
                        "wrt",
                        "wrf",
                        "er",
                        "ett",
                        "rftt"
                }
        );

        check(
                isValidAlienOrder(
                        new String[]{
                                "wrt",
                                "wrf",
                                "er",
                                "ett",
                                "rftt"
                        },
                        order
                ),
                "Alien Dictionary — valid inferred ordering"
        );

        check(
                solution.alienOrder(
                        new String[]{
                                "abc",
                                "ab"
                        }
                ).isEmpty(),
                "Alien Dictionary — invalid prefix"
        );
    }


    private static void testSequenceReconstruction() {

        SequenceReconstruction solution =
                new SequenceReconstruction();

        check(
                solution.sequenceReconstruction(
                        new int[]{1, 2, 3},
                        Arrays.asList(
                                Arrays.asList(1, 2),
                                Arrays.asList(2, 3)
                        )
                ),
                "Sequence Reconstruction — uniquely reconstructible"
        );

        check(
                !solution.sequenceReconstruction(
                        new int[]{1, 2, 3},
                        Arrays.asList(
                                Arrays.asList(1, 2),
                                Arrays.asList(1, 3)
                        )
                ),
                "Sequence Reconstruction — multiple valid next choices"
        );

        check(
                !solution.sequenceReconstruction(
                        new int[]{1},
                        Collections.emptyList()
                ),
                "Sequence Reconstruction — required node never appears"
        );
    }


    private static void testEventualSafeStates() {

        EventualSafeStates solution =
                new EventualSafeStates();

        int[][] graph = {
                {1, 2},
                {2, 3},
                {5},
                {0},
                {5},
                {},
                {}
        };

        check(
                solution.eventualSafeNodes(graph)
                        .equals(Arrays.asList(2, 4, 5, 6)),
                "Eventual Safe States"
        );
    }


    private static void testCourseScheduleIV() {

        CourseScheduleIV solution =
                new CourseScheduleIV();

        List<Boolean> answer =
                solution.checkIfPrerequisite(
                        4,
                        new int[][]{
                                {0, 1},
                                {1, 2},
                                {2, 3}
                        },
                        new int[][]{
                                {0, 3},
                                {1, 3},
                                {3, 0},
                                {0, 2}
                        }
                );

        check(
                answer.equals(
                        Arrays.asList(
                                true,
                                true,
                                false,
                                true
                        )
                ),
                "Course Schedule IV — transitive prerequisite queries"
        );
    }


    private static void testAllTopologicalOrders() {

        AllTopologicalOrders solution =
                new AllTopologicalOrders();

        List<List<Integer>> orders =
                solution.findAllOrders(
                        4,
                        new int[][]{
                                {1, 0},
                                {2, 0},
                                {3, 1},
                                {3, 2}
                        }
                );

        check(
                orders.size() == 2,
                "All topological orders — diamond has exactly 2"
        );

        check(
                orders.contains(Arrays.asList(0, 1, 2, 3)),
                "All topological orders — contains 0,1,2,3"
        );

        check(
                orders.contains(Arrays.asList(0, 2, 1, 3)),
                "All topological orders — contains 0,2,1,3"
        );
    }


    // =================================================================================================
    // TEST HELPERS
    // =================================================================================================

    private static boolean isValidTopologicalOrder(int numCourses,
                                                   int[][] prerequisites,
                                                   int[] order) {

        if (order.length != numCourses) {
            return false;
        }

        boolean[] seen = new boolean[numCourses];
        int[] position = new int[numCourses];

        for (int i = 0; i < order.length; i++) {

            int course = order[i];

            if (course < 0
                    || course >= numCourses
                    || seen[course]) {
                return false;
            }

            seen[course] = true;
            position[course] = i;
        }

        for (int[] p : prerequisites) {

            int course = p[0];
            int prereq = p[1];

            if (position[prereq] > position[course]) {
                return false;
            }
        }

        return true;
    }


    private static boolean isValidAlienOrder(String[] words,
                                             String order) {

        if (order.isEmpty()) {
            return false;
        }

        Map<Character, Integer> position = new HashMap<>();

        for (int i = 0; i < order.length(); i++) {
            position.put(order.charAt(i), i);
        }

        Set<Character> allChars = new HashSet<>();

        for (String word : words) {
            for (char ch : word.toCharArray()) {
                allChars.add(ch);
            }
        }

        if (position.size() != allChars.size()) {
            return false;
        }

        for (int i = 0; i < words.length - 1; i++) {

            String first = words[i];
            String second = words[i + 1];

            if (first.length() > second.length()
                    && first.startsWith(second)) {
                return false;
            }

            int length = Math.min(first.length(), second.length());
            boolean foundDifference = false;

            for (int j = 0; j < length; j++) {

                char a = first.charAt(j);
                char b = second.charAt(j);

                if (a != b) {

                    if (position.get(a) > position.get(b)) {
                        return false;
                    }

                    foundDifference = true;
                    break;
                }
            }

            if (!foundDifference
                    && first.length() > second.length()) {
                return false;
            }
        }

        return true;
    }


    private static void check(boolean condition,
                              String testName) {

        if (!condition) {
            throw new AssertionError(
                    "FAILED: " + testName
            );
        }

        System.out.println(
                "PASSED: " + testName
        );
    }
}
