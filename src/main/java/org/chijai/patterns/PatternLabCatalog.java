package org.chijai.patterns;

import org.chijai.patterns.backtracking.BacktrackingPatternLab;
import org.chijai.patterns.binarysearch.BinarySearchPatternLab;
import org.chijai.patterns.corebasics.CoreBasicsPatternLab;
import org.chijai.patterns.designlld.DesignLldPatternLab;
import org.chijai.patterns.dynamicprogramming.DynamicProgrammingPatternLab;
import org.chijai.patterns.graphbfs.GraphBfsPatternLab;
import org.chijai.patterns.graphdfs.GraphDfsPatternLab;
import org.chijai.patterns.hashmap.HashMapPatternLab;
import org.chijai.patterns.heap.HeapPatternLab;
import org.chijai.patterns.intervalsgreedy.IntervalsGreedyPatternLab;
import org.chijai.patterns.linkedlist.LinkedListPatternLab;
import org.chijai.patterns.mathbitstring.MathBitStringPatternLab;
import org.chijai.patterns.prefixsuffix.PrefixSuffixPatternLab;
import org.chijai.patterns.slidingwindow.SlidingWindowPatternLab;
import org.chijai.patterns.stack.StackPatternLab;
import org.chijai.patterns.topologicalsort.TopologicalSortPatternLab;
import org.chijai.patterns.treebfs.TreeBfsPatternLab;
import org.chijai.patterns.treedfs.TreeDfsPatternLab;
import org.chijai.patterns.trie.TriePatternLab;
import org.chijai.patterns.twopointers.TwoPointersPatternLab;
import org.chijai.patterns.unionfind.UnionFindPatternLab;

import java.util.List;

public final class PatternLabCatalog {
    private PatternLabCatalog() {
    }

    public static List<PatternChapter> chapters() {
        return List.of(
                HashMapPatternLab.chapter(),
                BinarySearchPatternLab.chapter(),
                SlidingWindowPatternLab.chapter(),
                PrefixSuffixPatternLab.chapter(),
                LinkedListPatternLab.chapter(),
                TwoPointersPatternLab.chapter(),
                TreeBfsPatternLab.chapter(),
                TreeDfsPatternLab.chapter(),
                GraphDfsPatternLab.chapter(),
                TopologicalSortPatternLab.chapter(),
                GraphBfsPatternLab.chapter(),
                DynamicProgrammingPatternLab.chapter(),
                BacktrackingPatternLab.chapter(),
                StackPatternLab.chapter(),
                HeapPatternLab.chapter(),
                IntervalsGreedyPatternLab.chapter(),
                TriePatternLab.chapter(),
                UnionFindPatternLab.chapter(),
                MathBitStringPatternLab.chapter(),
                CoreBasicsPatternLab.chapter(),
                DesignLldPatternLab.chapter()
        );
    }
}
