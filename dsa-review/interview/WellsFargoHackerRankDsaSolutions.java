package org.chijai.wellsfargo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Blank-screen Java reference for every DSA problem named in the Wells Fargo
 * HackerRank/interview companion. Each method is intentionally self-contained.
 */
public final class WellsFargoHackerRankDsaSolutions {
    private static final long MOD = 1_000_000_007L;
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    private WellsFargoHackerRankDsaSolutions() {
    }

    // LeetCode 2904: O(n^2) time, O(1) auxiliary space apart from substrings.
    public static String shortestBeautifulSubstring(String s, int k) {
        if (k == 0) {
            return "";
        }
        String best = null;
        for (int left = 0; left < s.length(); left++) {
            int ones = 0;
            for (int right = left; right < s.length(); right++) {
                ones += s.charAt(right) == '1' ? 1 : 0;
                if (ones == k) {
                    String candidate = s.substring(left, right + 1);
                    if (best == null || candidate.length() < best.length()
                            || candidate.length() == best.length() && candidate.compareTo(best) < 0) {
                        best = candidate;
                    }
                    break;
                }
            }
        }
        return best == null ? "" : best;
    }

    // LeetCode 796: O(n) expected substring search, O(n) concatenation space.
    public static boolean rotateString(String s, String goal) {
        return s.length() == goal.length() && (s + s).contains(goal);
    }

    // LeetCode 1768: O(n + m) time and output space.
    public static String mergeAlternately(String first, String second) {
        StringBuilder answer = new StringBuilder(first.length() + second.length());
        for (int i = 0; i < Math.max(first.length(), second.length()); i++) {
            if (i < first.length()) {
                answer.append(first.charAt(i));
            }
            if (i < second.length()) {
                answer.append(second.charAt(i));
            }
        }
        return answer.toString();
    }

    // LeetCode 703: constructor O(n log k), add O(log k), space O(k).
    public static final class KthLargest {
        private final int k;
        private final PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        public KthLargest(int k, int[] values) {
            if (k <= 0) {
                throw new IllegalArgumentException("k must be positive");
            }
            this.k = k;
            for (int value : values) {
                add(value);
            }
        }

        public int add(int value) {
            minHeap.offer(value);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
            return minHeap.element();
        }
    }

    // LeetCode 2696: remove AB or CD. O(n) time and O(n) space.
    public static int minimumLengthAfterRemovingSubstrings(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char current : s.toCharArray()) {
            if (!stack.isEmpty()
                    && (stack.peekLast() == 'A' && current == 'B'
                    || stack.peekLast() == 'C' && current == 'D')) {
                stack.removeLast();
            } else {
                stack.addLast(current);
            }
        }
        return stack.size();
    }

    // LeetCode 200: O(rows * columns) time; queue can hold O(rows * columns).
    public static int numberOfIslands(char[][] grid) {
        int islands = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[0].length; column++) {
                if (grid[row][column] != '1') {
                    continue;
                }
                islands++;
                floodFill(grid, row, column);
            }
        }
        return islands;
    }

    private static void floodFill(char[][] grid, int startRow, int startColumn) {
        Deque<int[]> queue = new ArrayDeque<>();
        queue.addLast(new int[]{startRow, startColumn});
        grid[startRow][startColumn] = '0';
        while (!queue.isEmpty()) {
            int[] current = queue.removeFirst();
            for (int[] direction : DIRECTIONS) {
                int row = current[0] + direction[0];
                int column = current[1] + direction[1];
                if (row >= 0 && row < grid.length && column >= 0 && column < grid[0].length
                        && grid[row][column] == '1') {
                    grid[row][column] = '0';
                    queue.addLast(new int[]{row, column});
                }
            }
        }
    }

    // LeetCode 2062: O(n) time using atMost(all vowels) - atMost(at most four distinct vowels).
    public static int countVowelSubstrings(String word) {
        return countVowelWindowsAtMostDistinct(word, 5) - countVowelWindowsAtMostDistinct(word, 4);
    }

    private static int countVowelWindowsAtMostDistinct(String word, int limit) {
        Map<Character, Integer> frequencies = new HashMap<>();
        int answer = 0;
        int left = 0;
        for (int right = 0; right < word.length(); right++) {
            char current = word.charAt(right);
            if (!isVowel(current)) {
                frequencies.clear();
                left = right + 1;
                continue;
            }
            frequencies.merge(current, 1, Integer::sum);
            while (frequencies.size() > limit) {
                char removed = word.charAt(left++);
                frequencies.compute(removed, (ignored, count) -> count == 1 ? null : count - 1);
            }
            answer += right - left + 1;
        }
        return answer;
    }

    private static boolean isVowel(char value) {
        return value == 'a' || value == 'e' || value == 'i' || value == 'o' || value == 'u';
    }

    // LeetCode 460: average O(1) get/put using frequency buckets with LRU tie-breaking.
    public static final class LFUCache {
        private final int capacity;
        private final Map<Integer, Integer> values = new HashMap<>();
        private final Map<Integer, Integer> frequencies = new HashMap<>();
        private final Map<Integer, LinkedHashSet<Integer>> keysByFrequency = new HashMap<>();
        private int minimumFrequency;

        public LFUCache(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            if (!values.containsKey(key)) {
                return -1;
            }
            promote(key);
            return values.get(key);
        }

        public void put(int key, int value) {
            if (capacity == 0) {
                return;
            }
            if (values.containsKey(key)) {
                values.put(key, value);
                promote(key);
                return;
            }
            if (values.size() == capacity) {
                LinkedHashSet<Integer> leastFrequent = keysByFrequency.get(minimumFrequency);
                int evicted = leastFrequent.iterator().next();
                leastFrequent.remove(evicted);
                values.remove(evicted);
                frequencies.remove(evicted);
            }
            values.put(key, value);
            frequencies.put(key, 1);
            keysByFrequency.computeIfAbsent(1, ignored -> new LinkedHashSet<>()).add(key);
            minimumFrequency = 1;
        }

        private void promote(int key) {
            int frequency = frequencies.get(key);
            LinkedHashSet<Integer> oldBucket = keysByFrequency.get(frequency);
            oldBucket.remove(key);
            if (frequency == minimumFrequency && oldBucket.isEmpty()) {
                minimumFrequency++;
            }
            int next = frequency + 1;
            frequencies.put(key, next);
            keysByFrequency.computeIfAbsent(next, ignored -> new LinkedHashSet<>()).add(key);
        }
    }

    // LeetCode 253: O(n log n) time and O(n) heap space.
    public static int minimumMeetingRooms(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(interval -> interval[0]));
        PriorityQueue<Integer> endTimes = new PriorityQueue<>();
        for (int[] interval : intervals) {
            if (!endTimes.isEmpty() && endTimes.peek() <= interval[0]) {
                endTimes.poll();
            }
            endTimes.offer(interval[1]);
        }
        return endTimes.size();
    }

    // LeetCode 844: O(n + m) time and O(1) auxiliary space.
    public static boolean backspaceCompare(String first, String second) {
        int i = first.length() - 1;
        int j = second.length() - 1;
        while (i >= 0 || j >= 0) {
            i = previousVisibleIndex(first, i);
            j = previousVisibleIndex(second, j);
            if (i < 0 || j < 0) {
                return i == j;
            }
            if (first.charAt(i--) != second.charAt(j--)) {
                return false;
            }
        }
        return true;
    }

    private static int previousVisibleIndex(String value, int index) {
        int skipped = 0;
        while (index >= 0) {
            if (value.charAt(index) == '#') {
                skipped++;
                index--;
            } else if (skipped > 0) {
                skipped--;
                index--;
            } else {
                break;
            }
        }
        return index;
    }

    public static final class ListNode {
        public final int value;
        public ListNode next;

        public ListNode(int value) {
            this.value = value;
        }
    }

    // LeetCode 160: O(n + m) time and O(1) space; compares node identity.
    public static ListNode intersection(ListNode first, ListNode second) {
        ListNode left = first;
        ListNode right = second;
        while (left != right) {
            left = left == null ? second : left.next;
            right = right == null ? first : right.next;
        }
        return left;
    }

    // LeetCode 20: O(n) time and O(n) stack space.
    public static boolean validParentheses(String s) {
        Deque<Character> expected = new ArrayDeque<>();
        for (char current : s.toCharArray()) {
            if (current == '(') {
                expected.addLast(')');
            } else if (current == '[') {
                expected.addLast(']');
            } else if (current == '{') {
                expected.addLast('}');
            } else if (expected.isEmpty() || expected.removeLast() != current) {
                return false;
            }
        }
        return expected.isEmpty();
    }

    // LeetCode 146: O(1) get/put using a map plus a doubly linked list.
    public static final class LRUCache {
        private static final class Node {
            private final int key;
            private int value;
            private Node previous;
            private Node next;

            private Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }

        private final int capacity;
        private final Map<Integer, Node> nodes = new HashMap<>();
        private final Node leastRecent = new Node(0, 0);
        private final Node mostRecent = new Node(0, 0);

        public LRUCache(int capacity) {
            this.capacity = capacity;
            leastRecent.next = mostRecent;
            mostRecent.previous = leastRecent;
        }

        public int get(int key) {
            Node node = nodes.get(key);
            if (node == null) {
                return -1;
            }
            moveToMostRecent(node);
            return node.value;
        }

        public void put(int key, int value) {
            if (capacity == 0) {
                return;
            }
            Node existing = nodes.get(key);
            if (existing != null) {
                existing.value = value;
                moveToMostRecent(existing);
                return;
            }
            if (nodes.size() == capacity) {
                Node evicted = leastRecent.next;
                unlink(evicted);
                nodes.remove(evicted.key);
            }
            Node added = new Node(key, value);
            nodes.put(key, added);
            linkBeforeMostRecent(added);
        }

        private void moveToMostRecent(Node node) {
            unlink(node);
            linkBeforeMostRecent(node);
        }

        private void unlink(Node node) {
            node.previous.next = node.next;
            node.next.previous = node.previous;
        }

        private void linkBeforeMostRecent(Node node) {
            node.previous = mostRecent.previous;
            node.next = mostRecent;
            mostRecent.previous.next = node;
            mostRecent.previous = node;
        }
    }

    // LeetCode 2008: O(rides log rides) time and O(rides) DP space.
    public static long maximumTaxiEarnings(int destination, int[][] rides) {
        Arrays.sort(rides, Comparator.comparingInt(ride -> ride[1]));
        int[] end = new int[rides.length];
        long[] best = new long[rides.length + 1];
        for (int i = 0; i < rides.length; i++) {
            end[i] = rides[i][1];
            int compatibleCount = upperBound(end, i, rides[i][0]);
            long earning = (long) rides[i][1] - rides[i][0] + rides[i][2];
            best[i + 1] = Math.max(best[i], best[compatibleCount] + earning);
        }
        return best[rides.length];
    }

    private static int upperBound(int[] values, int length, int target) {
        int left = 0;
        int right = length;
        while (left < right) {
            int middle = left + (right - left) / 2;
            if (values[middle] <= target) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return left;
    }

    // LeetCode 696: O(n) time and O(1) auxiliary space.
    public static int countBinarySubstrings(String s) {
        int previousRun = 0;
        int currentRun = 1;
        int answer = 0;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1)) {
                currentRun++;
            } else {
                answer += Math.min(previousRun, currentRun);
                previousRun = currentRun;
                currentRun = 1;
            }
        }
        return answer + Math.min(previousRun, currentRun);
    }

    // LeetCode 93: at most 3^4 candidates after pruning; output dominates space.
    public static List<String> restoreIpAddresses(String digits) {
        List<String> answer = new ArrayList<>();
        restoreIp(digits, 0, new ArrayList<>(), answer);
        return answer;
    }

    private static void restoreIp(String digits, int index, List<String> parts, List<String> answer) {
        int partsRemaining = 4 - parts.size();
        int charactersRemaining = digits.length() - index;
        if (charactersRemaining < partsRemaining || charactersRemaining > 3 * partsRemaining) {
            return;
        }
        if (parts.size() == 4) {
            answer.add(String.join(".", parts));
            return;
        }
        for (int length = 1; length <= 3 && index + length <= digits.length(); length++) {
            if (length > 1 && digits.charAt(index) == '0') {
                break;
            }
            String part = digits.substring(index, index + length);
            if (Integer.parseInt(part) > 255) {
                break;
            }
            parts.add(part);
            restoreIp(digits, index + length, parts, answer);
            parts.remove(parts.size() - 1);
        }
    }

    // LeetCode 56: O(n log n) time and O(n) output space.
    public static int[][] mergeIntervals(int[][] intervals) {
        if (intervals.length == 0) {
            return new int[0][];
        }
        Arrays.sort(intervals, Comparator.comparingInt(interval -> interval[0]));
        List<int[]> answer = new ArrayList<>();
        int[] current = intervals[0].clone();
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= current[1]) {
                current[1] = Math.max(current[1], intervals[i][1]);
            } else {
                answer.add(current);
                current = intervals[i].clone();
            }
        }
        answer.add(current);
        return answer.toArray(int[][]::new);
    }

    // LeetCode 713 assumes positive integers. O(n) time and O(1) space.
    public static int subarrayProductLessThanK(int[] values, int k) {
        if (k <= 1) {
            return 0;
        }
        long product = 1;
        int left = 0;
        int answer = 0;
        for (int right = 0; right < values.length; right++) {
            product *= values[right];
            while (product >= k) {
                product /= values[left++];
            }
            answer += right - left + 1;
        }
        return answer;
    }

    // LeetCode 121: O(n) time and O(1) space.
    public static int maximumStockProfit(int[] prices) {
        int minimum = Integer.MAX_VALUE;
        int answer = 0;
        for (int price : prices) {
            minimum = Math.min(minimum, price);
            answer = Math.max(answer, price - minimum);
        }
        return answer;
    }

    // LeetCode 2812: multi-source BFS plus widest-path Dijkstra. O(n^2 log n).
    public static int maximumSafenessFactor(List<List<Integer>> grid) {
        int size = grid.size();
        int[][] distance = new int[size][size];
        for (int[] row : distance) {
            Arrays.fill(row, -1);
        }
        Deque<int[]> queue = new ArrayDeque<>();
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (grid.get(row).get(column) == 1) {
                    distance[row][column] = 0;
                    queue.addLast(new int[]{row, column});
                }
            }
        }
        while (!queue.isEmpty()) {
            int[] current = queue.removeFirst();
            for (int[] direction : DIRECTIONS) {
                int row = current[0] + direction[0];
                int column = current[1] + direction[1];
                if (row >= 0 && row < size && column >= 0 && column < size && distance[row][column] == -1) {
                    distance[row][column] = distance[current[0]][current[1]] + 1;
                    queue.addLast(new int[]{row, column});
                }
            }
        }
        int[][] best = new int[size][size];
        for (int[] row : best) {
            Arrays.fill(row, -1);
        }
        PriorityQueue<int[]> widestFirst = new PriorityQueue<>((left, right) -> Integer.compare(right[0], left[0]));
        best[0][0] = distance[0][0];
        widestFirst.offer(new int[]{best[0][0], 0, 0});
        while (!widestFirst.isEmpty()) {
            int[] current = widestFirst.poll();
            int safeness = current[0];
            int currentRow = current[1];
            int currentColumn = current[2];
            if (safeness != best[currentRow][currentColumn]) {
                continue;
            }
            if (currentRow == size - 1 && currentColumn == size - 1) {
                return safeness;
            }
            for (int[] direction : DIRECTIONS) {
                int row = currentRow + direction[0];
                int column = currentColumn + direction[1];
                if (row < 0 || row >= size || column < 0 || column >= size) {
                    continue;
                }
                int candidate = Math.min(safeness, distance[row][column]);
                if (candidate > best[row][column]) {
                    best[row][column] = candidate;
                    widestFirst.offer(new int[]{candidate, row, column});
                }
            }
        }
        return 0;
    }

    // Reported-style problem: every character may occur at most k times. O(n).
    public static String longestSubstringWithCharacterLimit(String s, int k) {
        if (k <= 0 || s.isEmpty()) {
            return "";
        }
        Map<Character, Integer> frequencies = new HashMap<>();
        int left = 0;
        int bestStart = 0;
        int bestLength = 0;
        for (int right = 0; right < s.length(); right++) {
            char added = s.charAt(right);
            frequencies.merge(added, 1, Integer::sum);
            while (frequencies.get(added) > k) {
                char removed = s.charAt(left++);
                frequencies.compute(removed, (ignored, count) -> count == 1 ? null : count - 1);
            }
            int length = right - left + 1;
            if (length > bestLength) {
                bestLength = length;
                bestStart = left;
            }
        }
        return s.substring(bestStart, bestStart + bestLength);
    }

    // Reported-style problem: LCM of permutation cycle lengths modulo 1e9+7.
    public static long permutationCycleLcmModulo(int[] permutation) {
        boolean[] visited = new boolean[permutation.length];
        Map<Integer, Integer> maximumPrimeExponent = new HashMap<>();
        for (int start = 0; start < permutation.length; start++) {
            if (visited[start]) {
                continue;
            }
            int length = 0;
            int current = start;
            while (!visited[current]) {
                visited[current] = true;
                int next = permutation[current];
                if (next < 1 || next > permutation.length) {
                    throw new IllegalArgumentException("permutation values must be in [1,n]");
                }
                current = next - 1;
                length++;
            }
            factorIntoMaximumExponents(length, maximumPrimeExponent);
        }
        long answer = 1;
        for (Map.Entry<Integer, Integer> factor : maximumPrimeExponent.entrySet()) {
            answer = answer * modularPower(factor.getKey(), factor.getValue()) % MOD;
        }
        return answer;
    }

    private static void factorIntoMaximumExponents(int value, Map<Integer, Integer> maximumPrimeExponent) {
        for (int prime = 2; prime * prime <= value; prime++) {
            int exponent = 0;
            while (value % prime == 0) {
                value /= prime;
                exponent++;
            }
            if (exponent > 0) {
                maximumPrimeExponent.merge(prime, exponent, Math::max);
            }
        }
        if (value > 1) {
            maximumPrimeExponent.merge(value, 1, Math::max);
        }
    }

    private static long modularPower(long base, int exponent) {
        long answer = 1;
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                answer = answer * base % MOD;
            }
            base = base * base % MOD;
            exponent >>= 1;
        }
        return answer;
    }

    // Binary-search code-review answer: inclusive bounds, overflow-safe midpoint.
    public static int binarySearch(int[] sorted, int target) {
        int left = 0;
        int right = sorted.length - 1;
        while (left <= right) {
            int middle = left + (right - left) / 2;
            if (sorted[middle] == target) {
                return middle;
            }
            if (sorted[middle] < target) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
        return -1;
    }

    // LeetCode 14: O(total compared characters) time.
    public static String longestCommonPrefix(String[] values) {
        if (values.length == 0) {
            return "";
        }
        String prefix = values[0];
        for (int i = 1; i < values.length; i++) {
            while (!values[i].startsWith(prefix)) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) {
                    return "";
                }
            }
        }
        return prefix;
    }

    // LeetCode 268: O(n) time and O(1) space; XOR avoids sum overflow.
    public static int missingNumber(int[] values) {
        int answer = values.length;
        for (int i = 0; i < values.length; i++) {
            answer ^= i ^ values[i];
        }
        return answer;
    }

    // LeetCode 53: Kadane's algorithm, O(n) time and O(1) space.
    public static int maximumSubarray(int[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("values must not be empty");
        }
        int endingHere = values[0];
        int answer = values[0];
        for (int i = 1; i < values.length; i++) {
            endingHere = Math.max(values[i], endingHere + values[i]);
            answer = Math.max(answer, endingHere);
        }
        return answer;
    }

    public static void main(String[] args) {
        require("11001".equals(shortestBeautifulSubstring("100011001", 3)), "beautiful substring");
        require(rotateString("abcde", "cdeab"), "rotate string");
        require("apbqcr".equals(mergeAlternately("abc", "pqr")), "merge alternately");
        KthLargest kthLargest = new KthLargest(3, new int[]{4, 5, 8, 2});
        require(kthLargest.add(3) == 4 && kthLargest.add(10) == 5, "kth largest");
        require(minimumLengthAfterRemovingSubstrings("ABFCACDB") == 2, "minimum length");
        char[][] islands = {{'1', '1', '0'}, {'0', '1', '0'}, {'1', '0', '1'}};
        require(numberOfIslands(islands) == 3, "number of islands");
        require(countVowelSubstrings("aeiouu") == 2, "vowel substrings");
        LFUCache lfu = new LFUCache(2);
        lfu.put(1, 1);
        lfu.put(2, 2);
        require(lfu.get(1) == 1, "LFU get");
        lfu.put(3, 3);
        require(lfu.get(2) == -1 && lfu.get(3) == 3, "LFU eviction");
        require(minimumMeetingRooms(new int[][]{{0, 30}, {5, 10}, {15, 20}}) == 2, "meeting rooms");
        require(backspaceCompare("ab#c", "ad#c"), "backspace compare");
        ListNode shared = new ListNode(8);
        ListNode first = new ListNode(1);
        ListNode second = new ListNode(2);
        first.next = shared;
        second.next = new ListNode(3);
        second.next.next = shared;
        require(intersection(first, second) == shared, "linked-list intersection");
        require(validParentheses("([]{})"), "parentheses");
        LRUCache lru = new LRUCache(2);
        lru.put(1, 1);
        lru.put(2, 2);
        require(lru.get(1) == 1, "LRU get");
        lru.put(3, 3);
        require(lru.get(2) == -1 && lru.get(3) == 3, "LRU eviction");
        require(maximumTaxiEarnings(5, new int[][]{{2, 5, 4}, {1, 5, 1}}) == 7, "taxi earnings");
        require(countBinarySubstrings("00110011") == 6, "binary substrings");
        require(restoreIpAddresses("25525511135").size() == 2, "restore IP");
        int[][] merged = mergeIntervals(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}});
        require(Arrays.deepEquals(merged, new int[][]{{1, 6}, {8, 10}, {15, 18}}), "merge intervals");
        require(subarrayProductLessThanK(new int[]{10, 5, 2, 6}, 100) == 8, "product window");
        require(maximumStockProfit(new int[]{7, 1, 5, 3, 6, 4}) == 5, "stock profit");
        List<List<Integer>> safenessGrid = List.of(
                List.of(0, 0, 1),
                List.of(0, 0, 0),
                List.of(0, 0, 0));
        require(maximumSafenessFactor(safenessGrid) == 2, "maximum safeness");
        require("aabb".equals(longestSubstringWithCharacterLimit("aaabb", 2)), "character limit");
        require(permutationCycleLcmModulo(new int[]{2, 1, 4, 5, 3}) == 6, "cycle LCM");
        require(binarySearch(new int[]{1, 3, 5, 7}, 5) == 2, "binary search");
        require("fl".equals(longestCommonPrefix(new String[]{"flower", "flow", "flight"})), "prefix");
        require(missingNumber(new int[]{3, 0, 1}) == 2, "missing number");
        require(maximumSubarray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}) == 6, "maximum subarray");
        System.out.println("Wells Fargo HackerRank DSA solution smoke tests passed.");
    }

    private static void require(boolean condition, String name) {
        if (!condition) {
            throw new AssertionError(name);
        }
    }
}
