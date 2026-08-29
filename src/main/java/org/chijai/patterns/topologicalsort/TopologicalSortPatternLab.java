package org.chijai.patterns.topologicalsort;

import org.chijai.patterns.PatternChapter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public final class TopologicalSortPatternLab {
    private TopologicalSortPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Topological Sort",
                "Dependencies / Ordering",
                "Kahn Indegree",
                "Process Only Unlocked Nodes",
                "Course Schedule II"
        );
    }

    public static int[] topologicalOrder(int nodes, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < nodes; i++) {
            graph.add(new ArrayList<>());
        }
        int[] indegree = new int[nodes];
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            indegree[edge[1]]++;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < nodes; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        int[] order = new int[nodes];
        int index = 0;
        while (!queue.isEmpty()) {
            int node = queue.remove();
            order[index++] = node;
            for (int next : graph.get(node)) {
                if (--indegree[next] == 0) {
                    queue.add(next);
                }
            }
        }
        return index == nodes ? order : new int[0];
    }
}
