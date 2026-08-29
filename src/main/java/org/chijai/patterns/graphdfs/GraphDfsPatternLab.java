package org.chijai.patterns.graphdfs;

import org.chijai.patterns.PatternChapter;

import java.util.List;

public final class GraphDfsPatternLab {
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    private GraphDfsPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Graph DFS / Components",
                "Explore / Path / Component",
                "Visited Ownership",
                "One Traversal Per Component",
                "Number Of Islands"
        );
    }

    public static int countComponents(List<List<Integer>> graph) {
        boolean[] visited = new boolean[graph.size()];
        int components = 0;
        for (int node = 0; node < graph.size(); node++) {
            if (!visited[node]) {
                components++;
                dfs(node, graph, visited);
            }
        }
        return components;
    }

    private static void dfs(int node, List<List<Integer>> graph, boolean[] visited) {
        visited[node] = true;
        for (int next : graph.get(node)) {
            if (!visited[next]) {
                dfs(next, graph, visited);
            }
        }
    }

    public static int numIslands(char[][] grid) {
        int islands = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] == '1') {
                    islands++;
                    sink(grid, row, col);
                }
            }
        }
        return islands;
    }

    private static void sink(char[][] grid, int row, int col) {
        if (row < 0 || col < 0 || row >= grid.length || col >= grid[0].length || grid[row][col] != '1') {
            return;
        }
        grid[row][col] = '0';
        for (int[] direction : DIRECTIONS) {
            sink(grid, row + direction[0], col + direction[1]);
        }
    }
}
