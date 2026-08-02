package org.chijai.day8.session1;

import java.util.*;

/**
 * 01 Matrix
 *
 * An implementation-oriented chapter focused on Multi-Source BFS.
 *
 * Java 17
 */
public class Matrix01 {

    /*==============================================================
     *
     * 2. 📘 PRIMARY PROBLEM
     *
     *==============================================================*/

    static final String TITLE = """
        01 Matrix

        Difficulty:
        Medium

        Tags:
        Breadth First Search
        Multi-Source BFS
        Matrix
        Graph
        Dynamic Programming (Alternative)

        Problem

        You are given an m x n binary matrix.

        Every cell contains either:

        0
        or
        1

        For every cell, compute the distance to the nearest 0.

        Distance is measured using 4-direction movement:

        Up
        Down
        Left
        Right

        Every move costs exactly one.

        Return a matrix where every position stores the shortest
        distance to any zero.

        Constraints

        1 <= m, n <= 10^4
        1 <= m * n <= 10^4

        Every element is either 0 or 1.

        There is at least one 0.

        Representative Example

        Input

        [
          [0,0,0],
          [0,1,0],
          [1,1,1]
        ]

        Output

        [
          [0,0,0],
          [0,1,0],
          [1,2,1]
        ]

        Explanation

        Bottom-left 1 is one step from a zero.

        Middle-bottom 1 is two steps away.

        Bottom-right 1 is one step away.

        Official LeetCode

        https://leetcode.com/problems/01-matrix/
        """;

    /*==============================================================
     *
     * 3. 🔵 CORE PATTERN OVERVIEW
     *
     *==============================================================*/

    static final String CORE_PATTERN = """
        Pattern

        Multi-Source Breadth First Search

        Archetype

        Shortest distance from every node
        to the nearest source.

        Sources

        Every zero.

        Search Space

        Entire matrix.

        State

        A matrix cell.

        Transition

        Move in four directions.

        Core Invariant

        The first time BFS reaches a cell,
        that distance is guaranteed to be the shortest.

        Why It Works

        BFS expands strictly layer by layer.

        Layer 0

        All zeros.

        Layer 1

        Every cell at distance one.

        Layer 2

        Every cell at distance two.

        Since layers are processed in increasing order,
        the first assignment is optimal.

        Recognition Signals

        • Multiple starting positions
        • Equal edge weights
        • Nearest source
        • Shortest number of moves
        • Grid graph
        • Distance transform

        When To Use

        • Nearest hospital
        • Nearest gate
        • Fire spread
        • Rotten oranges
        • Police stations
        • Multi-origin shortest path

        When NOT To Use

        Weighted edges

        Use Dijkstra.

        Negative edges

        Use Bellman-Ford.

        Variable movement costs

        BFS no longer preserves shortest distance.

        Comparison

        Single-source BFS

        One starting node.

        Multi-source BFS

        Many starting nodes inserted initially.

        DFS

        Cannot guarantee shortest path.

        Dynamic Programming

        Possible for this problem using two passes,
        but much harder to derive correctly in interviews.
        """;

    /*==============================================================
     *
     * 4. 🟢 MENTAL MODEL & INVARIANTS
     *
     *==============================================================*/

    static final String MENTAL_MODEL = """
        Mental Model

        Imagine every zero starts emitting a wave
        at exactly the same time.

        Every second,
        the wave expands by one cell.

        Whichever wave reaches a cell first
        determines its shortest distance.

        BFS naturally simulates these waves.

        Never think:

        "For each 1, search for a 0."

        Instead think:

        "All 0s search outward together."

        That single viewpoint changes an O((mn)^2)
        brute-force search into O(mn).

        --------------------------------------------------

        Primary Invariant

        Every queued cell already has its
        final shortest distance.

        Therefore,

        when expanding a queued cell,

        every newly discovered neighbor receives

        parent distance + 1

        exactly once.

        --------------------------------------------------

        Queue Invariant

        Queue contents always belong to
        one current layer
        followed by future layers.

        Distances inside the queue
        never decrease.

        --------------------------------------------------

        Visit Invariant

        Every cell is assigned exactly once.

        Why?

        The first visit is already optimal.

        Any later visit must be longer
        or equal.

        Therefore later visits are ignored.

        --------------------------------------------------

        Distance Invariant

        If

        dist[current] = d

        then every newly discovered neighbor
        receives

        d + 1.

        --------------------------------------------------

        Variable Meanings

        queue

        Frontier whose distances
        are finalized.

        row
        col

        Current state.

        directions

        Four legal transitions.

        matrix[r][c] == -1

        Unvisited.

        matrix[r][c] >= 0

        Already finalized.

        --------------------------------------------------

        Allowed Moves

        Pop one finalized cell.

        Inspect four neighbors.

        Assign distance exactly once.

        Push newly finalized neighbor.

        --------------------------------------------------

        Forbidden Moves

        Revisiting finalized cells.

        Updating a finalized distance.

        Starting BFS separately
        from every one.

        Processing DFS first.

        --------------------------------------------------

        Termination

        Queue becomes empty.

        At that moment,
        every reachable cell
        has been finalized.

        Since at least one zero exists,
        every cell is reachable
        through the grid.

        --------------------------------------------------

        Correctness Intuition

        Suppose a cell first receives
        distance 5.

        Could a shorter distance 4 exist?

        No.

        Distance 4 would belong to
        an earlier BFS layer.

        That layer is processed completely
        before layer 5 begins.

        Therefore the shorter path
        would already have visited it.

        Contradiction.

        Hence the first assignment
        is optimal.

        --------------------------------------------------

        Why Naive Solutions Fail

        Naive Thinking

        For every 1

        run BFS until a zero.

        Complexity

        O((mn)^2)

        Massive repeated exploration.

        Example

        A huge block of ones.

        Every search repeats almost
        identical work.

        Multi-source BFS performs
        one global traversal instead.
        """;

    /*==============================================================
     *
     * 5. 🔴 WHY WRONG SOLUTIONS FAIL
     *
     *==============================================================*/

    static final String WRONG_SOLUTIONS = """
        Mistake 1

        Start BFS independently
        from every one.

        Looks reasonable because BFS
        computes shortest paths.

        Violated Invariant

        Search spaces overlap heavily.

        Same regions are explored repeatedly.

        --------------------------------------------------

        Mistake 2

        Mark visited only after polling.

        Looks harmless.

        Actually,

        one cell may enter the queue
        many times.

        Queue size explodes.

        Correct Rule

        Mark immediately before enqueue.

        --------------------------------------------------

        Mistake 3

        Forget to enqueue every zero.

        Then BFS becomes
        single-source.

        Distances become incorrect.

        --------------------------------------------------

        Mistake 4

        Enqueue a neighbor
        without assigning its distance.

        Later,

        debugging becomes impossible
        because the parent relationship
        is lost.

        Assign first.

        Enqueue second.

        --------------------------------------------------

        Mistake 5

        Update an already assigned cell.

        Violated Invariant

        First visit is shortest.

        Later updates only increase work
        and may corrupt correctness.

        --------------------------------------------------

        Interview Trap

        Interviewer:

        Why do we enqueue after assigning
        the distance?

        Correct Answer

        Assignment finalizes the cell.

        Enqueueing allows that finalized
        cell to become a parent
        for the next BFS layer.

        Without enqueueing,

        the wave cannot continue expanding.

        This is exactly why

        matrix[r][c] = distance;

        queue.offer(...);

        must always occur together.
        """;

    /*==============================================================
     *
     * ⚙️ IMPLEMENTATION BLUEPRINT
     *
     *==============================================================*/

    static final String IMPLEMENTATION_BLUEPRINT = """
        Typing Order

        1.
        Create answer structure.

        2.
        Create queue.

        3.
        Scan matrix.

           If value is 0

               enqueue.

           Else

               mark as -1 (unvisited).

        4.

        While queue not empty

            process one BFS layer

            increase current distance

            expand four neighbors

        5.

        For every neighbor

            boundary check

            skip visited

            assign distance

            enqueue

        6.

        Return matrix.

        Mechanical Skeleton

        initialize queue

        initialize all sources

        currentDistance = 0

        while queue not empty

            levelSize

            currentDistance++

            repeat levelSize

                poll

                explore neighbors

        return matrix
        """;

    /*==============================================================
     *
     * 🧾 ULTRA-COMPACT PSEUDOCODE
     *
     *==============================================================*/

    static final String PSEUDOCODE = """
        enqueue all zeros

        mark ones unvisited

        distance = 0

        while queue not empty

            distance++

            process one level

            assign neighbor = distance

            enqueue neighbor

        return matrix
        """;

    /*==============================================================
     *
     * 6. SOLUTION CLASSES
     *
     *==============================================================*/

    /**
     * ------------------------------------------------------------
     * Brute Force
     * ------------------------------------------------------------
     *
     * Idea
     *
     * Every 1 independently performs BFS
     * until finding a 0.
     *
     * Invariant
     *
     * Individual BFS is correct,
     * but work is massively duplicated.
     *
     * Limitation
     *
     * Repeated exploration.
     *
     * Complexity
     *
     * Time:
     * O((mn)^2)
     *
     * Space:
     * O(mn)
     *
     * Interview Usefulness
     *
     * Good starting discussion,
     * never acceptable final answer.
     */
    static class BruteForce {

        public int[][] updateMatrix(int[][] mat) {

            int rows = mat.length;
            int cols = mat[0].length;

            int[][] answer = new int[rows][cols];

            int[][] directions = {
                    {1, 0},
                    {-1, 0},
                    {0, 1},
                    {0, -1}
            };

            for (int startRow = 0; startRow < rows; startRow++) {

                for (int startCol = 0; startCol < cols; startCol++) {

                    if (mat[startRow][startCol] == 0) {
                        answer[startRow][startCol] = 0;
                        continue;
                    }

                    boolean[][] visited = new boolean[rows][cols];

                    Queue<int[]> queue = new ArrayDeque<>();

                    queue.offer(new int[]{startRow, startCol});

                    visited[startRow][startCol] = true;

                    int distance = 0;

                    boolean found = false;

                    while (!queue.isEmpty() && !found) {

                        int size = queue.size();

                        while (size-- > 0) {

                            int[] current = queue.poll();

                            if (mat[current[0]][current[1]] == 0) {

                                answer[startRow][startCol] = distance;

                                found = true;

                                break;
                            }

                            for (int[] direction : directions) {

                                int nextRow = current[0] + direction[0];
                                int nextCol = current[1] + direction[1];

                                if (nextRow < 0
                                        || nextCol < 0
                                        || nextRow >= rows
                                        || nextCol >= cols
                                        || visited[nextRow][nextCol]) {
                                    continue;
                                }

                                visited[nextRow][nextCol] = true;

                                queue.offer(new int[]{nextRow, nextCol});
                            }
                        }

                        distance++;
                    }
                }
            }

            return answer;
        }
    }

    /**
     * ------------------------------------------------------------
     * Improved
     * ------------------------------------------------------------
     *
     * Idea
     *
     * Dynamic Programming.
     *
     * Two directional passes propagate the nearest
     * known zero distance.
     *
     * Pass 1
     *
     * Top-left → Bottom-right
     *
     * Pass 2
     *
     * Bottom-right → Top-left
     *
     * Invariant
     *
     * After the first pass,
     * every cell knows the best answer obtainable
     * from its top and left neighbors.
     *
     * After the second pass,
     * information from bottom and right neighbors
     * completes the shortest distance.
     *
     * Improvement
     *
     * Removes repeated BFS.
     *
     * Complexity
     *
     * Time
     * O(mn)
     *
     * Space
     * O(1)
     * (excluding output updates performed in-place)
     *
     * Interview Usefulness
     *
     * Valuable alternative.
     *
     * However,
     * Multi-Source BFS is usually easier to derive
     * and explain under interview pressure.
     */
    static class Improved {

        public int[][] updateMatrix(int[][] mat) {

            int rows = mat.length;
            int cols = mat[0].length;

            int infinity = rows + cols + 5;

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (mat[row][col] == 0) {
                        continue;
                    }

                    int best = infinity;

                    if (row > 0) {
                        best = Math.min(best, mat[row - 1][col] + 1);
                    }

                    if (col > 0) {
                        best = Math.min(best, mat[row][col - 1] + 1);
                    }

                    mat[row][col] = best;
                }
            }

            for (int row = rows - 1; row >= 0; row--) {

                for (int col = cols - 1; col >= 0; col--) {

                    if (row + 1 < rows) {
                        mat[row][col] =
                                Math.min(mat[row][col],
                                        mat[row + 1][col] + 1);
                    }

                    if (col + 1 < cols) {
                        mat[row][col] =
                                Math.min(mat[row][col],
                                        mat[row][col + 1] + 1);
                    }
                }
            }

            return mat;
        }
    }

    /**
     * ------------------------------------------------------------
     * Optimal (Interview Preferred)
     * ------------------------------------------------------------
     *
     * Idea
     *
     * Treat every zero as a BFS source.
     *
     * Instead of asking
     *
     * "Where is the nearest zero for this one?"
     *
     * ask
     *
     * "How far can every zero expand?"
     *
     * Every expansion layer corresponds exactly
     * to one additional unit of distance.
     *
     * ------------------------------------------------------------
     *
     * Invariant
     *
     * Every dequeued cell already stores its
     * final shortest distance.
     *
     * Therefore every newly discovered neighbor
     * receives
     *
     * parentDistance + 1
     *
     * exactly once.
     *
     * ------------------------------------------------------------
     *
     * Correctness
     *
     * BFS explores in increasing distance order.
     *
     * Because all zeros begin simultaneously,
     * whichever wave reaches a cell first
     * must be the globally shortest path.
     *
     * ------------------------------------------------------------
     *
     * Complexity
     *
     * Time
     * O(mn)
     *
     * Space
     * O(mn)
     * (queue in worst case)
     *
     * ------------------------------------------------------------
     *
     * Interview Usefulness
     *
     * Excellent.
     *
     * Classic Multi-Source BFS template used in
     *
     * Walls and Gates
     * Rotten Oranges
     * Fire Spread
     * Nearest Exit
     * Distance Transform
     */
    static class Optimal {

        private static final int[][] DIRECTIONS = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        public int[][] updateMatrix(int[][] matrix) {

            int rows = matrix.length;
            int cols = matrix[0].length;

            Queue<int[]> queue = new ArrayDeque<>();

            // Invariant:
            // Queue initially contains every source.
            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < cols; col++) {

                    if (matrix[row][col] == 0) {

                        queue.offer(new int[]{row, col});

                    } else {

                        // Invariant:
                        // -1 means distance not finalized yet.
                        matrix[row][col] = -1;
                    }
                }
            }

            int currentDistance = 0;

            while (!queue.isEmpty()) {

                int levelSize = queue.size();

                currentDistance++;

                while (levelSize-- > 0) {

                    int[] current = queue.poll();

                    int currentRow = current[0];
                    int currentCol = current[1];

                    for (int[] direction : DIRECTIONS) {

                        int nextRow = currentRow + direction[0];
                        int nextCol = currentCol + direction[1];

                        // Ignore invalid positions.
                        if (nextRow < 0
                                || nextCol < 0
                                || nextRow >= rows
                                || nextCol >= cols) {
                            continue;
                        }

                        // Invariant:
                        // Already finalized.
                        if (matrix[nextRow][nextCol] != -1) {
                            continue;
                        }

                        // First assignment is optimal because
                        // BFS visits by increasing distance.
                        matrix[nextRow][nextCol] = currentDistance;

                        // This finalized node becomes the parent
                        // for the next BFS layer.
                        queue.offer(new int[]{
                                nextRow,
                                nextCol
                        });
                    }
                }
            }

            return matrix;
        }
    }

    /*==============================================================
     *
     * 🟣 INTERVIEW ARTICULATION
     *
     *==============================================================*/

    static final String INTERVIEW_ARTICULATION = """
        If I were explaining this in an interview:

        We have multiple starting points,
        not multiple destinations.

        Every zero is a source.

        Therefore I initialize the queue
        with every zero before BFS begins.

        The invariant is that every cell removed
        from the queue already stores its final
        shortest distance.

        Because BFS expands layer by layer,
        the first time a cell is reached
        cannot be improved later.

        Therefore I assign its distance only once.

        The discard rule is simple.

        Ignore

        • outside the matrix

        • already finalized cells

        because revisiting cannot improve
        the shortest distance.

        Termination occurs naturally when
        every reachable cell has been finalized
        and the queue becomes empty.

        The algorithm is not in-place in the
        strict algorithmic sense because
        the matrix is reused as both

        storage

        and

        visited state.

        Streaming is impossible because
        later rows depend on earlier BFS waves
        that have not yet propagated.
        """;

    /*==============================================================
     *
     * 🎯 INTERVIEW RECALL SHEET
     *
     *==============================================================*/

    static final String RECALL_SHEET = """
        Trigger

        Nearest source.

        Pattern

        Multi-Source BFS.

        Sources

        Every zero.

        State

        Matrix cell.

        Search Target

        Final shortest distance.

        Invariant

        First visit is optimal.

        Discard Rule

        Skip finalized cells.

        Common Trap

        Starting BFS from every one.

        Edge Cases

        Single row.

        Single column.

        All zeros.

        One isolated zero.

        One-Liner

        Let every zero expand simultaneously.

        Re-Derivation Cue

        Replace many searches

        with

        one global wave.
        """;
    /*==============================================================
     *
     * 🔄 VARIATIONS & TWEAKS
     *
     *==============================================================*/

    static final String VARIATIONS = """
        --------------------------------------------------
        Variation
        Walls and Gates
        --------------------------------------------------

        Pattern

        Multi-Source BFS

        Sources

        Every gate.

        State

        Empty room.

        Invariant

        First arrival from any gate is optimal.

        Reasoning Change

        Walls are never inserted into the queue.

        Pattern survives unchanged.

        --------------------------------------------------
        Variation
        Rotten Oranges
        --------------------------------------------------

        Pattern

        Multi-Source BFS

        Sources

        Rotten oranges.

        State

        Fresh oranges.

        Invariant

        Every minute corresponds to one BFS layer.

        Difference

        Final answer is the maximum BFS depth.

        --------------------------------------------------
        Variation
        Fire Spread
        --------------------------------------------------

        Pattern

        Multi-Source BFS

        Sources

        Initial fire cells.

        Invariant

        Fire expands simultaneously.

        Same reasoning.

        --------------------------------------------------
        Variation
        Nearest Hospital
        --------------------------------------------------

        Pattern

        Multi-Source BFS

        Sources

        Hospitals.

        Goal

        Nearest hospital distance.

        Exactly identical formulation.

        --------------------------------------------------
        Variation
        Distance To Nearest Exit
        --------------------------------------------------

        Sources

        Every exit.

        BFS wave expands inward.

        --------------------------------------------------
        Pattern Break

        Suppose movement cost differs.

        Example

        Grass costs 2.

        Road costs 1.

        BFS assumes every edge has identical cost.

        The invariant

        "first visit is shortest"

        immediately breaks.

        Required Algorithm

        Dijkstra.

        --------------------------------------------------
        Pattern Break

        Allow diagonal movement.

        Multi-source BFS still works.

        Only transition rules change.

        The invariant remains identical.

        --------------------------------------------------
        Pattern Break

        Weighted diagonal movement.

        Equal-layer expansion disappears.

        Need weighted shortest path algorithm.

        --------------------------------------------------
        Pattern Break

        Dynamic obstacles appearing while BFS runs.

        Previous finalized distances
        may become invalid.

        BFS correctness no longer holds.

        Need recomputation or dynamic graph methods.
        """;

    /*==============================================================
     *
     * 🧠 MASTERY CHECKLIST
     *
     *==============================================================*/

    static final String MASTERY_CHECKLIST = """
        Can I explain the invariant?

        Yes.

        Every dequeued cell already stores
        its final shortest distance.

        --------------------------------------------------

        Can I define the search target?

        Yes.

        Distance to the nearest zero.

        --------------------------------------------------

        Can I explain the discard rule?

        Yes.

        Ignore cells already assigned.

        First assignment is optimal.

        --------------------------------------------------

        Can I explain termination?

        Yes.

        Queue becomes empty after every
        reachable cell has been finalized.

        --------------------------------------------------

        Can I explain why the naive approach fails?

        Yes.

        It repeatedly searches identical regions.

        --------------------------------------------------

        Can I list edge cases?

        Yes.

        Single row.

        Single column.

        All zeros.

        One zero.

        Large connected block of ones.

        --------------------------------------------------

        Can I debug this algorithm?

        Yes.

        Verify

        Queue initialization.

        Layer boundaries.

        Immediate visitation marking.

        Boundary checks.

        Exactly-once assignment.

        --------------------------------------------------

        Can I derive variants?

        Yes.

        Replace sources.

        Preserve equal edge weights.

        Preserve first-arrival invariant.

        --------------------------------------------------

        Do I know the pattern boundary?

        Yes.

        Equal edge weights

        -> BFS.

        Unequal edge weights

        -> Dijkstra.

        Negative weights

        -> Bellman-Ford.
        """;

    /*==============================================================
     *
     * 🔍 DEBUGGING PLAYBOOK
     *
     *==============================================================*/

    static final String DEBUGGING_PLAYBOOK = """
        Symptom

        Distances too large.

        Check

        Did every zero enter the queue initially?

        --------------------------------------------------

        Symptom

        Queue grows unexpectedly.

        Check

        Are cells marked visited before enqueue?

        --------------------------------------------------

        Symptom

        Infinite processing.

        Check

        Is an already finalized cell
        being inserted again?

        --------------------------------------------------

        Symptom

        Neighbor never receives distance.

        Check

        Boundary condition.

        Check

        Assignment occurs before enqueue.

        --------------------------------------------------

        Symptom

        Entire region remains -1.

        Check

        Did BFS ever reach that region?

        Is every direction present?

        --------------------------------------------------

        Symptom

        Distance off by one.

        Check

        Layer counter placement.

        It must advance exactly once
        for each BFS level.

        --------------------------------------------------

        Symptom

        Some distances overwritten.

        Check

        Never update any cell
        after its first assignment.
        """;

    /*==============================================================
     *
     * ⚫ PATTERN MAPPING
     *
     *==============================================================*/

    static final String PATTERN_MAPPING = """
        Matrix Traversal
                    │
                    │
                    ▼
             Is shortest path needed?
                    │
            ┌───────┴────────┐
            │                │
            No               Yes
            │                │
            DFS         Equal edge cost?
                             │
                   ┌─────────┴─────────┐
                   │                   │
                  No                  Yes
                   │                   │
              Dijkstra        Multiple sources?
                                       │
                            ┌──────────┴──────────┐
                            │                     │
                           No                    Yes
                            │                     │
                    Single-source BFS     Multi-source BFS
        """;

    /*==============================================================
     *
     * ⚫ COMMON INTERVIEW QUESTIONS
     *
     *==============================================================*/

    static final String INTERVIEW_QA = """
        Q.

        Why start from zeros instead of ones?

        A.

        Every one wants the nearest zero.

        Reversing the search lets all zeros
        answer every one simultaneously.

        --------------------------------------------------

        Q.

        Why is the first visit optimal?

        A.

        BFS processes increasing distance layers.

        Earlier layer always represents
        a shorter path.

        --------------------------------------------------

        Q.

        Why not DFS?

        A.

        DFS explores depth first.

        It does not preserve shortest distance.

        --------------------------------------------------

        Q.

        Why store -1?

        A.

        It represents

        "distance not finalized."

        It combines

        visited array

        and

        answer array

        into one structure.

        --------------------------------------------------

        Q.

        Why enqueue after assigning?

        A.

        Assignment finalizes this node.

        Enqueue allows this finalized node
        to become a parent for the next layer.

        Without enqueueing,

        propagation stops.

        --------------------------------------------------

        Q.

        Could we revisit a cell?

        A.

        No.

        Any revisit would belong to an equal
        or later BFS layer.

        Therefore it cannot improve
        the shortest distance.

        --------------------------------------------------

        Q.

        What if there were no zeros?

        A.

        The original LeetCode constraints
        guarantee at least one zero.

        Otherwise,

        distances would be undefined and
        an additional specification would
        be required.
        """;

    /*==============================================================
     *
     * ⚫ RE-DERIVATION GUIDE
     *
     *==============================================================*/

    static final String REDERIVATION = """
        Forget the code.

        Remember only these ideas.

        Step 1

        Every zero is already at distance zero.

        Step 2

        Put every zero into one queue.

        Step 3

        Mark every one as unvisited.

        Step 4

        Expand level by level.

        Step 5

        First arrival finalizes distance.

        Step 6

        Newly finalized cells become
        parents for the next wave.

        Step 7

        Queue empty

        means

        every distance is finalized.

        From these seven statements,
        the entire implementation can
        be reconstructed mechanically.
        """;

    /*==============================================================
     *
     * ⚫ IMPLEMENTATION FORENSICS
     *
     *==============================================================*/

    static final String IMPLEMENTATION_FORENSICS = """
        Line

        queue.offer(zero)

        Why

        Every zero is a simultaneous BFS source.

        Forgetting even one zero changes
        the shortest distances.

        --------------------------------------------------

        Line

        matrix[row][col] = -1

        Why

        Unvisited sentinel.

        Avoids allocating a separate
        visited array.

        --------------------------------------------------

        Line

        int levelSize = queue.size()

        Why

        Captures exactly one BFS layer.

        Every node removed during this
        iteration has identical distance.

        --------------------------------------------------

        Line

        currentDistance++

        Why

        The next layer is exactly one step
        farther than the previous layer.

        --------------------------------------------------

        Line

        if (matrix[nextRow][nextCol] != -1)

        Why

        Already finalized.

        Never revisit.

        --------------------------------------------------

        Line

        matrix[nextRow][nextCol] = currentDistance

        Why

        First arrival is shortest.

        This assignment is permanent.

        --------------------------------------------------

        Line

        queue.offer(neighbor)

        Why

        Newly finalized cells must expand
        during the next BFS layer.

        Assignment without enqueue
        breaks propagation.

        --------------------------------------------------

        Queue Lifecycle

        Initialization

        All zeros.

        Expansion

        Current frontier.

        Completion

        Empty queue.

        Every reachable state finalized.
        """;

    /*==============================================================
     *
     * ⚫ COMPLEXITY ANALYSIS
     *
     *==============================================================*/

    static final String COMPLEXITY = """
        Time Complexity

        O(rows × cols)

        Why?

        Every cell enters the queue
        at most once.

        Every edge is inspected
        a constant number of times.

        --------------------------------------------------

        Space Complexity

        Queue

        Worst case

        O(rows × cols)

        Matrix

        Reused for visitation state.

        No additional visited array.

        --------------------------------------------------

        Tight Bound

        Theta(rows × cols)

        because every cell must be examined.
        """;

    /*==============================================================
     *
     * ⚫ EDGE CASE CATALOG
     *
     *==============================================================*/

    static final String EDGE_CASES = """
        Edge Case

        Single Cell

        [0]

        Output

        [0]

        --------------------------------------------------

        Single Row

        [1 1 0 1]

        Verify propagation only
        horizontally.

        --------------------------------------------------

        Single Column

        Verify propagation only
        vertically.

        --------------------------------------------------

        Entire Matrix Zero

        Queue initially contains
        every cell.

        BFS finishes immediately.

        --------------------------------------------------

        One Zero

        Longest propagation.

        Good stress test.

        --------------------------------------------------

        Checkerboard Pattern

        Every one has answer one.

        Useful for verifying
        unnecessary revisits.

        --------------------------------------------------

        Large Island of Ones

        Ensures layered propagation
        works correctly.

        --------------------------------------------------

        Zero On Boundary

        Confirms boundary checks.

        --------------------------------------------------

        Maximum Constraints

        Confirms linear complexity.
        """;

    /*==============================================================
     *
     * ⚫ BFS LAYER VISUALIZATION
     *
     *==============================================================*/

    static final String LAYER_VISUALIZATION = """
        Example

        0 1 1
        1 1 1
        1 1 1

        Initial Queue

        (0,0)

        Layer 0

        0 . .
        . . .
        . . .

        Layer 1

        0 1 .
        1 . .
        . . .

        Layer 2

        0 1 2
        1 2 .
        2 . .

        Layer 3

        0 1 2
        1 2 3
        2 3 .

        Layer 4

        0 1 2
        1 2 3
        2 3 4

        Observe

        Every layer corresponds to
        one additional unit of distance.
        """;

    /*==============================================================
     *
     * ⚫ TRANSFER LEARNING
     *
     *==============================================================*/

    static final String TRANSFER_LEARNING = """
        Whenever you see

        nearest X

        from many Y

        immediately ask

        Can Y become simultaneous sources?

        If every move has equal cost,

        Multi-Source BFS is usually
        the correct pattern.

        Examples

        Nearest Gate

        Nearest Police Station

        Nearest Charging Station

        Fire Spread

        Virus Spread

        Rotten Oranges

        Escape Time

        Flood Fill with Time

        Distance Transform

        This single abstraction
        transfers across dozens
        of interview problems.
        """;

    /*==============================================================
     *
     * ⚫ BFS TEMPLATE
     *
     *==============================================================*/

    static final String BFS_TEMPLATE = """
        Queue<State> queue

        Initialize every source

        Mark remaining states unvisited

        distance = 0

        while queue not empty

            size = queue size

            distance++

            repeat size times

                current = poll

                for every transition

                    skip invalid

                    skip finalized

                    finalize

                    enqueue

        return answer
        """;

    /*==============================================================
     *
     * ⚫ MEMORY PEGS
     *
     *==============================================================*/

    static final String MEMORY_PEGS = """
        Peg 1

        Reverse the search.

        --------------------------------------------------

        Peg 2

        All sources together.

        --------------------------------------------------

        Peg 3

        BFS expands like water.

        --------------------------------------------------

        Peg 4

        First touch wins.

        --------------------------------------------------

        Peg 5

        Finalize then enqueue.

        --------------------------------------------------

        Peg 6

        Queue layer equals distance.

        --------------------------------------------------

        Peg 7

        Never revisit finalized cells.
        """;

    /*==============================================================
     *
     * ⚫ IMPLEMENTATION CHECKLIST
     *
     *==============================================================*/

    static final String IMPLEMENTATION_CHECKLIST = """
        □ Queue created.

        □ Four directions prepared.

        □ Every zero enqueued.

        □ Every one marked unvisited.

        □ Layer size captured.

        □ Distance increased once per layer.

        □ Boundary checks correct.

        □ Already-finalized cells skipped.

        □ Distance assigned before enqueue.

        □ Queue eventually empty.

        □ Matrix returned.
        """;

    /*==============================================================
     *
     * ⚫ QUICK REFERENCE
     *
     *==============================================================*/

    static final String QUICK_REFERENCE = """
        Pattern

        Multi-Source BFS

        Sources

        Every zero

        State

        Cell

        Transition

        Four directions

        Invariant

        First visit is shortest

        Search Space

        Entire matrix

        Complexity

        O(mn)

        Termination

        Empty queue
        """;

    /*==============================================================
     *
     * 🧪 MAIN + SELF-VERIFYING TESTS
     *
     *==============================================================*/

    private static void assertMatrixEquals(int[][] expected, int[][] actual) {

        assert expected.length == actual.length : "Row count mismatch";

        for (int row = 0; row < expected.length; row++) {

            assert Arrays.equals(expected[row], actual[row])
                    : "Mismatch at row " + row
                    + " expected=" + Arrays.toString(expected[row])
                    + " actual=" + Arrays.toString(actual[row]);
        }
    }

    private static int[][] copy(int[][] matrix) {

        int[][] result = new int[matrix.length][];

        for (int row = 0; row < matrix.length; row++) {
            result[row] = matrix[row].clone();
        }

        return result;
    }

    public static void main(String[] args) {

        Optimal solver = new Optimal();

        /*
         * Happy Path
         *
         * Official example.
         */
        {
            int[][] input = {
                    {0, 0, 0},
                    {0, 1, 0},
                    {1, 1, 1}
            };

            int[][] expected = {
                    {0, 0, 0},
                    {0, 1, 0},
                    {1, 2, 1}
            };

            assertMatrixEquals(expected,
                    solver.updateMatrix(copy(input)));
        }

        /*
         * Single Cell.
         */
        {
            int[][] input = {
                    {0}
            };

            int[][] expected = {
                    {0}
            };

            assertMatrixEquals(expected,
                    solver.updateMatrix(copy(input)));
        }

        /*
         * Single Row.
         */
        {
            int[][] input = {
                    {1, 1, 0, 1}
            };

            int[][] expected = {
                    {2, 1, 0, 1}
            };

            assertMatrixEquals(expected,
                    solver.updateMatrix(copy(input)));
        }

        /*
         * Single Column.
         */
        {
            int[][] input = {
                    {1},
                    {1},
                    {0},
                    {1}
            };

            int[][] expected = {
                    {2},
                    {1},
                    {0},
                    {1}
            };

            assertMatrixEquals(expected,
                    solver.updateMatrix(copy(input)));
        }

        /*
         * Entire matrix already zero.
         */
        {
            int[][] input = {
                    {0, 0},
                    {0, 0}
            };

            int[][] expected = {
                    {0, 0},
                    {0, 0}
            };

            assertMatrixEquals(expected,
                    solver.updateMatrix(copy(input)));
        }

        /*
         * One zero in the corner.
         */
        {
            int[][] input = {
                    {0, 1, 1},
                    {1, 1, 1},
                    {1, 1, 1}
            };

            int[][] expected = {
                    {0, 1, 2},
                    {1, 2, 3},
                    {2, 3, 4}
            };

            assertMatrixEquals(expected,
                    solver.updateMatrix(copy(input)));
        }

        /*
         * Checkerboard.
         *
         * Every one should become one.
         */
        {
            int[][] input = {
                    {0, 1, 0},
                    {1, 0, 1},
                    {0, 1, 0}
            };

            int[][] expected = {
                    {0, 1, 0},
                    {1, 0, 1},
                    {0, 1, 0}
            };

            assertMatrixEquals(expected,
                    solver.updateMatrix(copy(input)));
        }

        /*
         * Long propagation.
         */
        {
            int[][] input = {
                    {1, 1, 1, 1, 0}
            };

            int[][] expected = {
                    {4, 3, 2, 1, 0}
            };

            assertMatrixEquals(expected,
                    solver.updateMatrix(copy(input)));
        }

        /*
         * Boundary propagation.
         */
        {
            int[][] input = {
                    {1, 0},
                    {1, 1}
            };

            int[][] expected = {
                    {1, 0},
                    {2, 1}
            };

            assertMatrixEquals(expected,
                    solver.updateMatrix(copy(input)));
        }

        /*
         * Multiple independent sources.
         */
        {
            int[][] input = {
                    {0, 1, 1},
                    {1, 1, 1},
                    {1, 1, 0}
            };

            int[][] expected = {
                    {0, 1, 2},
                    {1, 2, 1},
                    {2, 1, 0}
            };

            assertMatrixEquals(expected,
                    solver.updateMatrix(copy(input)));
        }

        System.out.println("All assertions passed.");
    }
}
