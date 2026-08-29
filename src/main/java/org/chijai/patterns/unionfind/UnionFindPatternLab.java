package org.chijai.patterns.unionfind;

import org.chijai.patterns.PatternChapter;

public final class UnionFindPatternLab {
    private UnionFindPatternLab() {
    }

    public static PatternChapter chapter() {
        return PatternChapter.of(
                "Union Find / DSU",
                "Connectivity / Component Merging",
                "Parent Links",
                "Union Fails When Already Connected",
                "Number Of Provinces"
        );
    }

    public static int countComponents(int nodes, int[][] edges) {
        Dsu dsu = new Dsu(nodes);
        int components = nodes;
        for (int[] edge : edges) {
            if (dsu.union(edge[0], edge[1])) {
                components--;
            }
        }
        return components;
    }

    public static int[] redundantConnection(int[][] edges) {
        Dsu dsu = new Dsu(edges.length + 1);
        for (int[] edge : edges) {
            if (!dsu.union(edge[0], edge[1])) {
                return edge;
            }
        }
        return new int[0];
    }

    private static final class Dsu {
        private final int[] parent;
        private final int[] rank;

        private Dsu(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        private int find(int node) {
            if (parent[node] != node) {
                parent[node] = find(parent[node]);
            }
            return parent[node];
        }

        private boolean union(int left, int right) {
            int rootLeft = find(left);
            int rootRight = find(right);
            if (rootLeft == rootRight) {
                return false;
            }
            if (rank[rootLeft] < rank[rootRight]) {
                parent[rootLeft] = rootRight;
            } else if (rank[rootLeft] > rank[rootRight]) {
                parent[rootRight] = rootLeft;
            } else {
                parent[rootRight] = rootLeft;
                rank[rootLeft]++;
            }
            return true;
        }
    }
}
