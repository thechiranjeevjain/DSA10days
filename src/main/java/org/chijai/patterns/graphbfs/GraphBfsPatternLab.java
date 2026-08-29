package org.chijai.patterns.graphbfs;

import org.chijai.patterns.PatternChapter;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public final class GraphBfsPatternLab {
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    private GraphBfsPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Graph BFS / Shortest Path",
                "Minimum Steps",
                "Queue Layer Expansion",
                "Mark On Enqueue",
                "Rotting Oranges"
        );
    }

    public static int shortestPathLength(List<List<Integer>> graph, int source, int target) {
        boolean[] visited = new boolean[graph.size()];
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{source, 0});
        visited[source] = true;
        while (!queue.isEmpty()) {
            int[] state = queue.remove();
            if (state[0] == target) {
                return state[1];
            }
            for (int next : graph.get(state[0])) {
                if (!visited[next]) {
                    visited[next] = true;
                    queue.add(new int[]{next, state[1] + 1});
                }
            }
        }
        return -1;
    }

    public static int orangesRotting(int[][] grid) {
        Queue<int[]> queue = new ArrayDeque<>();
        int fresh = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] == 2) {
                    queue.add(new int[]{row, col});
                } else if (grid[row][col] == 1) {
                    fresh++;
                }
            }
        }

        int minutes = 0;
        while (fresh > 0 && !queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cell = queue.remove();
                for (int[] direction : DIRECTIONS) {
                    int nextRow = cell[0] + direction[0];
                    int nextCol = cell[1] + direction[1];
                    if (nextRow < 0 || nextCol < 0 || nextRow >= grid.length || nextCol >= grid[0].length || grid[nextRow][nextCol] != 1) {
                        continue;
                    }
                    grid[nextRow][nextCol] = 2;
                    fresh--;
                    queue.add(new int[]{nextRow, nextCol});
                }
            }
            minutes++;
        }
        return fresh == 0 ? minutes : -1;
    }
}
