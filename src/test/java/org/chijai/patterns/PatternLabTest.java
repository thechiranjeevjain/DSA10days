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
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatternLabTest {
    @Test
    void hashmapSkeletonsCacheProcessedState() {
        assertArrayEquals(new int[]{0, 1}, HashMapPatternLab.twoSum(new int[]{2, 7, 11, 15}, 9));
        assertTrue(HashMapPatternLab.isAnagram("listen", "silent"));
        assertFalse(HashMapPatternLab.isAnagram("rat", "car"));
        assertEquals(7, HashMapPatternLab.longestPalindromeLength("abccccdd"));
    }

    @Test
    void binarySearchSkeletonsPreserveBoundaryInvariants() {
        assertEquals(2, BinarySearchPatternLab.lowerBound(new int[]{1, 3, 5, 7}, 4));
        assertEquals(2, BinarySearchPatternLab.firstTrue(0, 5, value -> value * value >= 4));
        assertEquals(4, BinarySearchPatternLab.minimumFeasible(1, 10, speed -> speed >= 4));
    }

    @Test
    void slidingWindowSkeletonsMaintainCurrentWindowState() {
        assertEquals(3, SlidingWindowPatternLab.longestAtMostKDistinct("eceba", 2));
        assertEquals(2, SlidingWindowPatternLab.minLengthSubarrayAtLeastTarget(7, new int[]{2, 3, 1, 2, 4, 3}));
        assertEquals(2, SlidingWindowPatternLab.countFixedWindowAnagrams("cbaebabacd", "abc"));
    }

    @Test
    void prefixSuffixSkeletonsReuseRangeState() {
        assertArrayEquals(new int[]{24, 12, 8, 6}, PrefixSuffixPatternLab.productExceptSelf(new int[]{1, 2, 3, 4}));
        int[] prefix = PrefixSuffixPatternLab.prefixSums(new int[]{2, 4, 6, 8});
        assertEquals(10, PrefixSuffixPatternLab.rangeSum(prefix, 1, 2));
        assertEquals(2, PrefixSuffixPatternLab.subarraySumEqualsK(new int[]{1, 1, 1}, 2));
    }

    @Test
    void linkedListSkeletonsKeepPointerOrderVisible() {
        LinkedListPatternLab.Node one = new LinkedListPatternLab.Node(1);
        LinkedListPatternLab.Node two = new LinkedListPatternLab.Node(2);
        LinkedListPatternLab.Node three = new LinkedListPatternLab.Node(3);
        one.next = two;
        two.next = three;

        LinkedListPatternLab.Node reversed = LinkedListPatternLab.reverse(one);
        assertEquals(3, reversed.value);
        assertEquals(2, reversed.next.value);
        assertEquals(1, reversed.next.next.value);

        LinkedListPatternLab.Node cycleA = new LinkedListPatternLab.Node(1);
        LinkedListPatternLab.Node cycleB = new LinkedListPatternLab.Node(2);
        cycleA.next = cycleB;
        cycleB.next = cycleA;
        assertTrue(LinkedListPatternLab.hasCycle(cycleA));

        LinkedListPatternLab.Node left = new LinkedListPatternLab.Node(1);
        left.next = new LinkedListPatternLab.Node(4);
        LinkedListPatternLab.Node right = new LinkedListPatternLab.Node(2);
        right.next = new LinkedListPatternLab.Node(3);
        assertEquals(1, LinkedListPatternLab.mergeSorted(left, right).value);
    }

    @Test
    void twoPointerSkeletonsShrinkOrCompactSearchSpace() {
        assertArrayEquals(new int[]{1, 3}, TwoPointersPatternLab.twoSumSortedZeroBased(new int[]{1, 2, 4, 6}, 8));
        assertTrue(TwoPointersPatternLab.isPalindromeIgnoringNonAlphanumeric("A man, a plan, a canal: Panama"));
        assertArrayEquals(new int[]{1, 3, 12, 0, 0}, TwoPointersPatternLab.moveZeroesStableCopy(new int[]{0, 1, 0, 3, 12}));
    }

    @Test
    void treeBfsSkeletonsPreserveLevels() {
        TreeBfsPatternLab.TreeNode root = new TreeBfsPatternLab.TreeNode(1);
        root.left = new TreeBfsPatternLab.TreeNode(2);
        root.right = new TreeBfsPatternLab.TreeNode(3);
        root.right.left = new TreeBfsPatternLab.TreeNode(4);

        assertEquals(List.of(List.of(1), List.of(2, 3), List.of(4)), TreeBfsPatternLab.levelOrder(root));
        assertEquals(List.of(1, 3, 4), TreeBfsPatternLab.rightSideView(root));
    }

    @Test
    void treeDfsSkeletonsDefineReturnContract() {
        TreeDfsPatternLab.TreeNode root = new TreeDfsPatternLab.TreeNode(5);
        root.left = new TreeDfsPatternLab.TreeNode(3);
        root.right = new TreeDfsPatternLab.TreeNode(7);
        root.left.left = new TreeDfsPatternLab.TreeNode(2);
        root.left.right = new TreeDfsPatternLab.TreeNode(4);

        assertEquals(3, TreeDfsPatternLab.maxDepth(root));
        assertTrue(TreeDfsPatternLab.isValidBst(root));
        assertSame(root.left, TreeDfsPatternLab.lowestCommonAncestor(root, root.left.left, root.left.right));
    }

    @Test
    void graphBfsSkeletonsUseFirstDiscoveryForMinimumSteps() {
        List<List<Integer>> graph = List.of(List.of(1, 2), List.of(3), List.of(3), List.of());
        assertEquals(2, GraphBfsPatternLab.shortestPathLength(graph, 0, 3));
        int[][] grid = {{2, 1, 1}, {1, 1, 0}, {0, 1, 1}};
        assertEquals(4, GraphBfsPatternLab.orangesRotting(grid));
    }

    @Test
    void graphDfsSkeletonsOwnComponents() {
        List<List<Integer>> graph = List.of(List.of(1), List.of(0), List.of(3), List.of(2));
        assertEquals(2, GraphDfsPatternLab.countComponents(graph));
        char[][] grid = {{'1', '1', '0'}, {'0', '1', '0'}, {'1', '0', '1'}};
        assertEquals(3, GraphDfsPatternLab.numIslands(grid));
    }

    @Test
    void topologicalSortSkeletonProcessesUnlockedNodes() {
        assertArrayEquals(new int[]{0, 1, 2}, TopologicalSortPatternLab.topologicalOrder(3, new int[][]{{0, 1}, {1, 2}}));
        assertEquals(0, TopologicalSortPatternLab.topologicalOrder(2, new int[][]{{0, 1}, {1, 0}}).length);
    }

    @Test
    void dynamicProgrammingSkeletonsNameStateBeforeTransition() {
        assertEquals(8, DynamicProgrammingPatternLab.climbStairs(5));
        assertEquals(12, DynamicProgrammingPatternLab.houseRobber(new int[]{2, 7, 9, 3, 1}));
        assertEquals(3, DynamicProgrammingPatternLab.coinChangeMinCoins(new int[]{1, 2, 5}, 11));
    }

    @Test
    void backtrackingSkeletonsChooseRecurseUndo() {
        assertEquals(4, BacktrackingPatternLab.subsets(new int[]{1, 2}).size());
        assertEquals(List.of("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"), BacktrackingPatternLab.letterCombinations("23"));
    }

    @Test
    void stackSkeletonsKeepUnresolvedCandidates() {
        assertTrue(StackPatternLab.isValidParentheses("([]){}"));
        assertFalse(StackPatternLab.isValidParentheses("([)]"));
        assertArrayEquals(new int[]{4, 2, 4, -1}, StackPatternLab.nextGreaterToRight(new int[]{2, 1, 2, 4}));
        assertEquals(10, StackPatternLab.largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}));
    }

    @Test
    void heapSkeletonsMaintainOnlyNeededFrontier() {
        assertEquals(5, HeapPatternLab.kthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));
        assertArrayEquals(new int[]{1, 2}, HeapPatternLab.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2));
    }

    @Test
    void intervalsAndGreedySkeletonsMakeConflictsLocal() {
        assertArrayEquals(new int[][]{{1, 6}, {8, 10}, {15, 18}}, IntervalsGreedyPatternLab.merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}}));
        assertEquals(1, IntervalsGreedyPatternLab.eraseOverlapIntervals(new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 3}}));
        assertEquals(List.of(9, 7, 8), IntervalsGreedyPatternLab.partitionLabels("ababcbacadefegdehijhklij"));
    }

    @Test
    void trieSkeletonsSharePrefixes() {
        TriePatternLab trie = new TriePatternLab();
        trie.insert("apple");
        assertTrue(trie.search("apple"));
        assertFalse(trie.search("app"));
        assertTrue(trie.startsWith("app"));
    }

    @Test
    void unionFindSkeletonsMaintainComponents() {
        assertEquals(2, UnionFindPatternLab.countComponents(5, new int[][]{{0, 1}, {1, 2}, {3, 4}}));
        assertArrayEquals(new int[]{2, 3}, UnionFindPatternLab.redundantConnection(new int[][]{{1, 2}, {1, 3}, {2, 3}}));
    }

    @Test
    void mathBitStringSkeletonsExposeInvariant() {
        assertEquals("100", MathBitStringPatternLab.addBinary("11", "1"));
        assertEquals(2, MathBitStringPatternLab.missingNumber(new int[]{3, 0, 1}));
        assertEquals(List.of(2, 3, 5, 7), MathBitStringPatternLab.primesBelow(10));
    }

    @Test
    void coreBasicsSkeletonsMakeSimulationStateExplicit() {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}};
        assertEquals(List.of(1, 2, 3, 6, 5, 4), CoreBasicsPatternLab.spiralOrder(matrix));
        assertEquals(-42, CoreBasicsPatternLab.atoiClamped("   -42abc"));
        assertEquals(Integer.MAX_VALUE, CoreBasicsPatternLab.atoiClamped("91283472332"));
    }

    @Test
    void designLldSkeletonsStartFromOperationContracts() {
        DesignLldPatternLab.TinyUrlCodec codec = new DesignLldPatternLab.TinyUrlCodec();
        String key = codec.encode("https://example.com/a");
        assertEquals("https://example.com/a", codec.decode(key));

        DesignLldPatternLab.TokenBucket bucket = new DesignLldPatternLab.TokenBucket(2, 1);
        assertTrue(bucket.allow(0));
        assertTrue(bucket.allow(0));
        assertFalse(bucket.allow(0));
        assertTrue(bucket.allow(1));
    }

    @Test
    void chapterTaxonomyIsVisibleFromPatternLabs() {
        assertEquals("Sliding Window", SlidingWindowPatternLab.chapter().topic());
        assertTrue(BinarySearchPatternLab.chapter().primaryHome().contains("First True Predicate"));
        assertTrue(DynamicProgrammingPatternLab.chapter().chapterFlow().contains("DEFEND"));
        assertEquals("Trie", TriePatternLab.chapter().topic());
        assertEquals("Design Data Structures", DesignLldPatternLab.chapter().topic());
        assertEquals(21, PatternLabCatalog.chapters().size());
        assertEquals("HashMap / Frequency / Set", PatternLabCatalog.chapters().get(0).topic());
        assertEquals("Design Data Structures", PatternLabCatalog.chapters().get(20).topic());
    }
}
