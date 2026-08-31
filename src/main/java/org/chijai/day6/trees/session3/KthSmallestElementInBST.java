package org.chijai.day6.trees.session3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Kth Smallest Element in a BST
 * LeetCode 230
 * https://leetcode.com/problems/kth-smallest-element-in-a-bst/
 *
 * Canonical interview-study file:
 *
 * 1. Preferred interview solution
 * 2. Ordered WHY blocks
 * 3. 30-second recall card
 * 4. Reusable master template
 * 5. Approach progression
 * 6. Follow-up / workload variations
 * 7. Related / reinforcement problems
 * 8. Self-verifying tests
 *
 * Core transfer idea:
 *
 *      BST inorder gives sorted order.
 *
 * Follow-up transfer idea:
 *
 *      STORE COUNTS -> SKIP WHOLE ORDERED REGIONS.
 */
public class KthSmallestElementInBST {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /*
     * =========================================================================
     * 1. PREFERRED INTERVIEW SOLUTION
     * =========================================================================
     *
     * Trigger:
     * BST + kth smallest / rank / sorted-order question.
     *
     * Pattern:
     * Iterative inorder traversal with early stopping.
     *
     * Time : O(H + k), O(n) worst case
     * Space: O(H)
     */
    static class Preferred {

        int kthSmallest(TreeNode root, int k) {

            TreeNode current = root;
            Deque<TreeNode> stack = new ArrayDeque<>();

            while (current != null || !stack.isEmpty()) {

                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }

                TreeNode node = stack.pop();
                k--;

                if (k == 0) {
                    return node.val;
                }

                current = node.right;
            }

            throw new IllegalArgumentException("k must be a valid 1-indexed rank.");
        }
    }

    /*
     * =========================================================================
     * 2. WHY? — MATCH THE CODE IN THE SAME ORDER
     * =========================================================================
     */

    /*
     * WHY 1 — Why inorder?
     *
     * BST property:
     *
     *      every LEFT value < NODE < every RIGHT value
     *
     * Therefore:
     *
     *      LEFT -> NODE -> RIGHT
     *
     * visits values in ascending order.
     *
     * So the kth smallest value is simply the kth inorder VISIT.
     */

    /*
     * WHY 2 — Why the inner "go left" loop?
     *
     * Before we may visit a node, every smaller value in its left subtree
     * must be processed.
     *
     * Pushing the entire left chain postpones ancestors until their
     * smaller candidates are exhausted.
     */

    /*
     * WHY 3 — Why is stack.pop() the next smallest?
     *
     * After current becomes null, there is no unprocessed node farther left.
     *
     * The stack top is therefore the smallest node whose:
     *
     *      left subtree is finished
     *      node itself is still unvisited
     *
     * KEY INVARIANT:
     *
     *      POP = NEXT SMALLEST UNVISITED BST NODE.
     */

    /*
     * WHY 4 — Why decrement k exactly after pop?
     *
     * Rank advances when a node is VISITED, not when it is discovered,
     * pushed, or when traversal enters its subtree.
     *
     *      pop node
     *      k--
     *
     * means:
     *
     *      "one more smallest element has now been consumed."
     */

    /*
     * WHY 5 — Why return when k == 0?
     *
     * k is 1-indexed.
     *
     * After exactly k inorder visits, the current node has sorted rank k.
     * Every future inorder node is larger, so no later work can change
     * the answer.
     */

    /*
     * WHY 6 — Why current = node.right?
     *
     * Inorder is:
     *
     *      LEFT -> NODE -> RIGHT
     *
     * After popping node:
     *
     *      LEFT is complete
     *      NODE is complete
     *
     * so the only unfinished part owned by node is RIGHT.
     *
     * The next loop again walks to the leftmost node inside that right subtree.
     */

    /*
     * WHY 7 — Why the outer OR condition?
     *
     *      current != null
     *
     * means there is a subtree we can descend into.
     *
     *      !stack.isEmpty()
     *
     * means there are deferred ancestors still waiting to be visited.
     *
     * Traversal is finished only when BOTH are empty.
     */

    /*
     * =========================================================================
     * 3. 30-SECOND RECALL CARD
     * =========================================================================
     *
     * TRIGGER
     * -------
     * BST + kth smallest / rank.
     *
     * PATTERN
     * -------
     * Inorder = sorted order.
     *
     * CORE INVARIANT
     * --------------
     * POP = next smallest unvisited node.
     *
     * MECHANICS
     * ---------
     * go LEFT
     * -> POP / VISIT
     * -> k--
     * -> k == 0 ? answer
     * -> go RIGHT
     *
     * COMPLEXITY
     * ----------
     * O(H + k) time, O(H) space.
     *
     * FOLLOW-UP CUE
     * -------------
     * Frequent rank queries?
     *
     * STORE COUNTS SO YOU CAN SKIP WHOLE REGIONS.
     */

    /*
     * =========================================================================
     * 4. REUSABLE MASTER TEMPLATE — ITERATIVE INORDER
     * =========================================================================
     *
     * Use this whenever you need sorted-order processing in a BST.
     *
     *      Deque<TreeNode> stack = new ArrayDeque<>();
     *      TreeNode current = root;
     *
     *      while (current != null || !stack.isEmpty()) {
     *
     *          while (current != null) {
     *              stack.push(current);
     *              current = current.left;
     *          }
     *
     *          TreeNode node = stack.pop();
     *
     *          // PROCESS node here
     *
     *          current = node.right;
     *      }
     *
     * Transfer problems:
     *
     * kth smallest       -> count PROCESS calls
     * kth largest        -> reverse left/right
     * BST iterator       -> pause between PROCESS calls
     * validate BST       -> compare current with previous
     * recover BST        -> detect inorder inversions
     * range reporting    -> process only wanted sorted values
     */

    /*
     * =========================================================================
     * 5. APPROACH PROGRESSION
     * =========================================================================
     *
     * A. Extract all values + sort
     * ---------------------------
     * Time : O(n log n)
     * Space: O(n)
     *
     * Works even if you forget the BST advantage,
     * but wastes the ordering already encoded by the tree.
     *
     * B. Full inorder list
     * --------------------
     * Time : O(n)
     * Space: O(n)
     *
     * Better:
     * BST gives sorted order without sorting.
     *
     * Still wasteful:
     * materializes every value even if k is tiny.
     *
     * C. Recursive inorder + early stop
     * ---------------------------------
     * Time : O(H + k), worst O(n)
     * Space: O(H) recursion
     *
     * D. Iterative inorder + early stop — PREFERRED
     * ------------------------------------------------
     * Time : O(H + k), worst O(n)
     * Space: O(H)
     *
     * Same asymptotics as recursive early-stop,
     * but explicit state and no recursion-depth concern.
     *
     * E. Morris inorder
     * -----------------
     * Auxiliary space: O(1)
     *
     * Important nuance:
     * Morris temporarily rewires tree pointers.
     *
     * A naive early return may leave temporary threads behind.
     * A safe implementation that guarantees restoration can continue
     * traversal after finding the answer, making it O(n) time.
     */

    static class FullInorderList {

        int kthSmallest(TreeNode root, int k) {
            List<Integer> values = new ArrayList<>();
            inorder(root, values);
            return values.get(k - 1);
        }

        private void inorder(TreeNode node, List<Integer> values) {
            if (node == null) {
                return;
            }

            inorder(node.left, values);
            values.add(node.val);
            inorder(node.right, values);
        }
    }

    static class RecursiveEarlyStop {

        private int remaining;
        private Integer answer;

        int kthSmallest(TreeNode root, int k) {
            remaining = k;
            answer = null;
            inorder(root);

            if (answer == null) {
                throw new IllegalArgumentException("k must be a valid 1-indexed rank.");
            }

            return answer;
        }

        private void inorder(TreeNode node) {
            if (node == null || answer != null) {
                return;
            }

            inorder(node.left);

            if (answer != null) {
                return;
            }

            remaining--;

            if (remaining == 0) {
                answer = node.val;
                return;
            }

            inorder(node.right);
        }
    }

    static class MorrisTraversalSafe {

        /*
         * O(1) auxiliary space.
         *
         * We deliberately DO NOT return immediately when kth is found,
         * because outstanding Morris threads may still exist.
         *
         * We remember the answer and finish traversal so every temporary
         * pointer is restored.
         *
         * Time : O(n)
         * Space: O(1)
         */
        int kthSmallest(TreeNode root, int k) {

            TreeNode current = root;
            Integer answer = null;

            while (current != null) {

                if (current.left == null) {

                    if (answer == null && --k == 0) {
                        answer = current.val;
                    }

                    current = current.right;
                    continue;
                }

                TreeNode predecessor = current.left;

                while (predecessor.right != null && predecessor.right != current) {
                    predecessor = predecessor.right;
                }

                if (predecessor.right == null) {
                    predecessor.right = current;
                    current = current.left;
                } else {
                    predecessor.right = null;

                    if (answer == null && --k == 0) {
                        answer = current.val;
                    }

                    current = current.right;
                }
            }

            if (answer == null) {
                throw new IllegalArgumentException("k must be a valid 1-indexed rank.");
            }

            return answer;
        }
    }

    /*
     * =========================================================================
     * 6. FOLLOW-UP MASTER IDEA
     * =========================================================================
     *
     * Original solution:
     *
     *      visit values sequentially until rank k
     *
     * Follow-up optimization:
     *
     *      know HOW MANY values live in a region
     *      -> skip that whole region in one decision
     *
     * This same idea appears in:
     *
     *      Order Statistic Tree -> subtree counts
     *      Fenwick Tree         -> prefix frequency counts
     *      Segment Tree         -> interval frequency counts
     *
     * UNIFYING PATTERN:
     *
     *      STORE COUNTS -> SKIP WHOLE ORDERED REGIONS.
     */

    /*
     * =========================================================================
     * 7. FOLLOW-UP VARIATION A — MANY QUERIES, STATIC TREE
     * =========================================================================
     *
     * Workload:
     *
     *      tree almost never changes
     *      kthSmallest is asked many times
     *
     * Best simple trade-off:
     *
     *      preprocess inorder once
     *      cache sorted values
     *
     * Build : O(n)
     * Query : O(1)
     * Space : O(n)
     *
     * Update:
     * expensive because cache becomes stale.
     *
     * Important lesson:
     *
     * If data is static, preprocessing can beat a more sophisticated
     * O(log n) dynamic data structure.
     */

    static class StaticKthIndex {

        private final List<Integer> sorted = new ArrayList<>();

        StaticKthIndex(TreeNode root) {
            build(root);
        }

        int kthSmallest(int k) {
            if (k < 1 || k > sorted.size()) {
                throw new IllegalArgumentException("k out of range.");
            }

            return sorted.get(k - 1);
        }

        private void build(TreeNode node) {
            if (node == null) {
                return;
            }

            build(node.left);
            sorted.add(node.val);
            build(node.right);
        }
    }

    /*
     * =========================================================================
     * 8. FOLLOW-UP VARIATION B — FREQUENT QUERIES + INSERT/DELETE
     * =========================================================================
     *
     * Standard interview answer:
     *
     *      augment each BST node with subtreeSize
     *
     * For current node:
     *
     *      leftSize = size(current.left)
     *
     * Then:
     *
     *      leftSize + 1 = current node's rank inside this subtree.
     *
     * Decision:
     *
     *      k <= leftSize
     *          -> answer is LEFT
     *
     *      k == leftSize + 1
     *          -> current node is answer
     *
     *      k > leftSize + 1
     *          -> skip LEFT + NODE
     *          -> k -= leftSize + 1
     *          -> go RIGHT
     *
     * This is binary-search-like rank navigation.
     *
     * Complexity:
     *
     *      Query  O(H)
     *      Insert O(H)
     *      Delete O(H)
     *
     * If the tree is balanced:
     *
     *      H = O(log n)
     *
     * so all become O(log n).
     *
     * CRITICAL NUANCE:
     *
     * Merely storing subtreeSize does NOT guarantee O(log n).
     * The tree must also stay balanced:
     *
     *      AVL
     *      Red-Black Tree
     *      Treap
     *      another balanced BST
     *
     * The implementation below demonstrates augmentation and maintenance,
     * but does not perform rotations itself.
     */

    static class OrderStatisticBST {

        static class Node {
            int val;
            int size = 1;
            Node left;
            Node right;

            Node(int val) {
                this.val = val;
            }
        }

        private Node root;

        void insert(int val) {
            root = insert(root, val);
        }

        void delete(int val) {
            root = delete(root, val);
        }

        int kthSmallest(int k) {
            if (k < 1 || k > size(root)) {
                throw new IllegalArgumentException("k out of range.");
            }

            Node current = root;

            while (current != null) {

                int leftSize = size(current.left);

                if (k == leftSize + 1) {
                    return current.val;
                }

                if (k <= leftSize) {
                    current = current.left;
                } else {
                    k -= leftSize + 1;
                    current = current.right;
                }
            }

            throw new IllegalStateException("Unreachable for valid k.");
        }

        int size() {
            return size(root);
        }

        private Node insert(Node node, int val) {

            if (node == null) {
                return new Node(val);
            }

            if (val < node.val) {
                node.left = insert(node.left, val);
            } else if (val > node.val) {
                node.right = insert(node.right, val);
            } else {
                throw new IllegalArgumentException("This demo BST expects unique values.");
            }

            refresh(node);
            return node;
        }

        private Node delete(Node node, int val) {

            if (node == null) {
                return null;
            }

            if (val < node.val) {
                node.left = delete(node.left, val);
            } else if (val > node.val) {
                node.right = delete(node.right, val);
            } else {

                if (node.left == null) {
                    return node.right;
                }

                if (node.right == null) {
                    return node.left;
                }

                Node successor = min(node.right);
                node.val = successor.val;
                node.right = delete(node.right, successor.val);
            }

            refresh(node);
            return node;
        }

        private Node min(Node node) {
            while (node.left != null) {
                node = node.left;
            }
            return node;
        }

        private void refresh(Node node) {
            node.size = 1 + size(node.left) + size(node.right);
        }

        private int size(Node node) {
            return node == null ? 0 : node.size;
        }
    }

    /*
     * =========================================================================
     * 9. FOLLOW-UP VARIATION C — VALUES COME FROM A SMALL / BOUNDED DOMAIN
     * =========================================================================
     *
     * Example:
     *
     *      0 <= value <= 10^4
     *
     * If the tree shape itself is irrelevant and we mainly need:
     *
     *      insert(value)
     *      delete(value)
     *      kthSmallest(k)
     *
     * store FREQUENCIES by value instead.
     *
     * Fenwick Tree supports:
     *
     *      point frequency update
     *      prefix count
     *      kth by cumulative frequency
     *
     * Complexity:
     *
     *      update      O(log M)
     *      kthSmallest O(log M)
     *      space       O(M)
     *
     * where M is the value domain size.
     *
     * This naturally supports duplicates because frequency may exceed 1.
     */

    static class FenwickOrderStatistic {

        private final int[] tree;
        private int count;

        FenwickOrderStatistic(int maxValue) {
            tree = new int[maxValue + 2];
        }

        void insert(int value) {
            add(value, 1);
            count++;
        }

        void delete(int value) {
            if (frequency(value) <= 0) {
                throw new IllegalArgumentException("Value does not exist.");
            }

            add(value, -1);
            count--;
        }

        int kthSmallest(int k) {

            if (k < 1 || k > count) {
                throw new IllegalArgumentException("k out of range.");
            }

            int index = 0;
            int step = Integer.highestOneBit(tree.length - 1);

            while (step != 0) {

                int next = index + step;

                if (next < tree.length && tree[next] < k) {
                    index = next;
                    k -= tree[next];
                }

                step >>= 1;
            }

            // Fenwick index = value + 1.
            // index is the final prefix position strictly before the target,
            // so the corresponding 0-based value is also index.
            return index;
        }

        int frequency(int value) {
            return prefix(value) - prefix(value - 1);
        }

        private void add(int value, int delta) {

            int index = value + 1;

            while (index < tree.length) {
                tree[index] += delta;
                index += index & -index;
            }
        }

        private int prefix(int value) {

            if (value < 0) {
                return 0;
            }

            int index = Math.min(value + 1, tree.length - 1);
            int sum = 0;

            while (index > 0) {
                sum += tree[index];
                index -= index & -index;
            }

            return sum;
        }
    }

    /*
     * =========================================================================
     * 10. FOLLOW-UP VARIATION D — NEED RANGE COUNTS TOO
     * =========================================================================
     *
     * Segment Tree stores:
     *
     *      count of values inside each value interval
     *
     * kth query:
     *
     *      if left interval has >= k values
     *          descend left
     *      else
     *          k -= leftCount
     *          descend right
     *
     * Same transfer pattern again:
     *
     *      COUNT -> SKIP.
     *
     * Prefer Segment Tree over Fenwick when you also want richer interval
     * information or range operations.
     *
     * Complexity:
     *
     *      point update O(log M)
     *      kth query    O(log M)
     *      range count  O(log M)
     */

    static class SegmentTreeOrderStatistic {

        private final int size;
        private final int[] tree;
        private int count;

        SegmentTreeOrderStatistic(int maxValue) {

            int powerOfTwo = 1;

            while (powerOfTwo <= maxValue) {
                powerOfTwo <<= 1;
            }

            size = powerOfTwo;
            tree = new int[size << 1];
        }

        void insert(int value) {
            update(value, 1);
            count++;
        }

        void delete(int value) {

            if (rangeCount(value, value) == 0) {
                throw new IllegalArgumentException("Value does not exist.");
            }

            update(value, -1);
            count--;
        }

        int kthSmallest(int k) {

            if (k < 1 || k > count) {
                throw new IllegalArgumentException("k out of range.");
            }

            int node = 1;

            while (node < size) {

                int left = node << 1;

                if (tree[left] >= k) {
                    node = left;
                } else {
                    k -= tree[left];
                    node = left + 1;
                }
            }

            return node - size;
        }

        int rangeCount(int leftValue, int rightValue) {

            int left = leftValue + size;
            int right = rightValue + size;
            int answer = 0;

            while (left <= right) {

                if ((left & 1) == 1) {
                    answer += tree[left++];
                }

                if ((right & 1) == 0) {
                    answer += tree[right--];
                }

                left >>= 1;
                right >>= 1;
            }

            return answer;
        }

        private void update(int value, int delta) {

            int index = value + size;
            tree[index] += delta;
            index >>= 1;

            while (index > 0) {
                tree[index] = tree[index << 1] + tree[(index << 1) + 1];
                index >>= 1;
            }
        }
    }

    /*
     * =========================================================================
     * 11. FOLLOW-UP VARIATION E — DUPLICATES
     * =========================================================================
     *
     * Standard LeetCode BST interpretation usually assumes strict ordering,
     * but real ordered multisets may contain duplicates.
     *
     * Instead of one node per occurrence, an augmented node can store:
     *
     *      value
     *      frequency
     *      subtreeCount
     *
     * where subtreeCount counts TOTAL ELEMENTS, not just distinct nodes.
     *
     * Rank logic becomes:
     *
     *      leftCount = total elements in left subtree
     *
     *      if k <= leftCount
     *          go left
     *
     *      else if k <= leftCount + frequency
     *          current value is answer
     *
     *      else
     *          k -= leftCount + frequency
     *          go right
     *
     * Same invariant.
     * Only "node contributes 1" changes to "node contributes frequency".
     */

    /*
     * =========================================================================
     * 12. FOLLOW-UP VARIATION F — INSERTIONS ONLY
     * =========================================================================
     *
     * Easier than full insert/delete maintenance.
     *
     * During insertion:
     *
     *      every ancestor on the insertion path gains one descendant
     *
     * so refresh subtreeSize while recursion unwinds.
     *
     * Still remember:
     *
     *      augmentation gives O(H), NOT automatically O(log n).
     *
     * A skewed ordinary BST can still have H = n.
     */

    /*
     * =========================================================================
     * 13. FOLLOW-UP VARIATION G — MORRIS VS ORDER-STATISTIC AUGMENTATION
     * =========================================================================
     *
     * These solve DIFFERENT bottlenecks.
     *
     * Morris:
     *
     *      objective -> reduce traversal SPACE
     *      result    -> O(1) auxiliary space
     *      still     -> sequential traversal
     *
     * Subtree size:
     *
     *      objective -> reduce QUERY TIME
     *      result    -> skip whole subtrees
     *
     * Do not confuse:
     *
     *      space optimization
     *
     * with
     *
     *      rank-query optimization.
     */

    /*
     * =========================================================================
     * 14. WORKLOAD DECISION CARD
     * =========================================================================
     *
     * ONE / OCCASIONAL kth QUERY
     * --------------------------
     * Iterative inorder.
     *
     *      O(H + k) time
     *      O(H) space
     *
     * MANY QUERIES + STATIC TREE
     * --------------------------
     * Cache full inorder array/list.
     *
     *      O(n) build
     *      O(1) query
     *
     * MANY QUERIES + FREQUENT BST INSERT/DELETE
     * -----------------------------------------
     * Balanced BST + subtree sizes.
     *
     *      O(log n) query/update
     *
     * SMALL / BOUNDED VALUE DOMAIN
     * ----------------------------
     * Fenwick Tree.
     *
     *      O(log M) query/update
     *
     * NEED RANGE COUNTS / RICHER INTERVAL OPERATIONS
     * -----------------------------------------------
     * Segment Tree.
     *
     *      O(log M) query/update/range count
     *
     * DUPLICATES
     * ----------
     * frequency + subtreeCount
     * or frequency structure such as Fenwick / Segment Tree.
     */

    /*
     * =========================================================================
     * 15. BINARY-SEARCH CONNECTION
     * =========================================================================
     *
     * Binary search:
     *
     *      compare target with middle
     *      -> discard half
     *
     * Order Statistic BST:
     *
     *      leftSize + 1 = current rank
     *      compare k with current rank
     *      -> discard a whole subtree
     *
     * The analogy becomes strongest when the BST is balanced:
     *
     *      height = O(log n)
     *
     * Mental model:
     *
     *      subtree size turns a BST into something
     *      you can "binary-search by rank".
     */

    /*
     * =========================================================================
     * 16. RELATED / REINFORCEMENT PROBLEMS
     * =========================================================================
     *
     * Keep these because they train the SAME invariant from different angles.
     *
     * 1. Kth Largest in BST
     * ---------------------
     * SAME:
     * ordered BST traversal + rank counting.
     *
     * DIFFERENCE:
     * reverse inorder:
     *
     *      RIGHT -> NODE -> LEFT
     *
     * 2. BST Iterator
     * ---------------
     * SAME:
     * stack top is next smallest.
     *
     * DIFFERENCE:
     * traversal is paused and resumed across next() calls.
     *
     * 3. Validate BST
     * ---------------
     * SAME:
     * inorder should be sorted.
     *
     * DIFFERENCE:
     * instead of counting visits, compare current with previous.
     *
     * 4. Recover BST
     * --------------
     * SAME:
     * inorder is expected to be sorted.
     *
     * DIFFERENCE:
     * detect inversions caused by two swapped nodes.
     *
     * 5. Rank of a Value / Count Smaller
     * ----------------------------------
     * SAME:
     * subtree counts encode order statistics.
     *
     * DIFFERENCE:
     * ask "what is this value's rank?" instead of "what value has rank k?"
     *
     * 6. Dynamic Median / Quantiles
     * -----------------------------
     * SAME:
     * median is just a rank query:
     *
     *      k ~= n / 2
     *
     * DIFFERENCE:
     * repeated updates make augmentation / frequency structures valuable.
     */

    static class KthLargest {

        int kthLargest(TreeNode root, int k) {

            TreeNode current = root;
            Deque<TreeNode> stack = new ArrayDeque<>();

            while (current != null || !stack.isEmpty()) {

                while (current != null) {
                    stack.push(current);
                    current = current.right;
                }

                TreeNode node = stack.pop();

                if (--k == 0) {
                    return node.val;
                }

                current = node.left;
            }

            throw new IllegalArgumentException("k must be valid.");
        }
    }

    static class BSTIterator {

        private final Deque<TreeNode> stack = new ArrayDeque<>();

        BSTIterator(TreeNode root) {
            pushLeft(root);
        }

        boolean hasNext() {
            return !stack.isEmpty();
        }

        int next() {

            TreeNode node = stack.pop();
            pushLeft(node.right);
            return node.val;
        }

        private void pushLeft(TreeNode node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }
    }

    static class ValidateBST {

        boolean isValidBST(TreeNode root) {

            Deque<TreeNode> stack = new ArrayDeque<>();
            TreeNode current = root;
            Long previous = null;

            while (current != null || !stack.isEmpty()) {

                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }

                TreeNode node = stack.pop();

                if (previous != null && node.val <= previous) {
                    return false;
                }

                previous = (long) node.val;
                current = node.right;
            }

            return true;
        }
    }

    /*
     * =========================================================================
     * 17. COMMON INTERVIEW TRAPS
     * =========================================================================
     *
     * TRAP 1
     * ------
     * Decrementing k while PUSHING.
     *
     * Wrong:
     * push order is not inorder visit order.
     *
     * Correct:
     * decrement exactly when node is popped / visited.
     *
     * TRAP 2
     * ------
     * Thinking inorder sorts every binary tree.
     *
     * False:
     * only BST ordering makes inorder sorted.
     *
     * TRAP 3
     * ------
     * Saying augmented BST is automatically O(log n).
     *
     * False:
     * query is O(H).
     * You need balancing to guarantee H = O(log n).
     *
     * TRAP 4
     * ------
     * Returning early from naive Morris traversal.
     *
     * Risk:
     * temporary predecessor threads may remain,
     * mutating/corrupting the original tree.
     *
     * TRAP 5
     * ------
     * Using TreeMap and claiming kth is O(log n).
     *
     * Java TreeMap keeps keys ordered,
     * but does not expose subtree sizes / rank selection.
     *
     * A plain TreeMap still needs iteration across keys/counts
     * unless you build additional rank metadata.
     *
     * TRAP 6
     * ------
     * Using a sophisticated dynamic structure for static data.
     *
     * If the tree never changes and queries are extremely frequent:
     *
     *      inorder cache -> O(1) query
     *
     * may be simpler and faster.
     */

    /*
     * =========================================================================
     * 18. FINAL MASTER INVARIANTS
     * =========================================================================
     *
     * BASE PROBLEM
     * ------------
     *
     *      INORDER = SORTED ORDER.
     *
     *      POP = NEXT SMALLEST.
     *
     * DYNAMIC FOLLOW-UP
     * -----------------
     *
     *      LEFT COUNT + NODE CONTRIBUTION = CURRENT RANK.
     *
     * GENERAL ORDER-STATISTIC TRANSFER
     * --------------------------------
     *
     *      STORE COUNTS -> SKIP WHOLE ORDERED REGIONS.
     *
     * WORKLOAD PRINCIPLE
     * ------------------
     *
     *      Static data:
     *          preprocess aggressively.
     *
     *      Dynamic data:
     *          maintain metadata incrementally.
     *
     *      Bounded value domain:
     *          index by value/frequency instead of tree shape.
     */

    private static TreeNode n(int value) {
        return new TreeNode(value);
    }

    private static TreeNode n(int value, TreeNode left, TreeNode right) {
        return new TreeNode(value, left, right);
    }

    private static TreeNode exampleOneTree() {
        return n(
                3,
                n(1, null, n(2)),
                n(4)
        );
    }

    private static TreeNode exampleTwoTree() {
        return n(
                5,
                n(
                        3,
                        n(2, n(1), null),
                        n(4)
                ),
                n(6)
        );
    }

    private static TreeNode invalidBST() {
        return n(
                5,
                n(1),
                n(4, n(3), n(6))
        );
    }

    /*
     * =========================================================================
     * 19. SELF-VERIFYING TESTS
     * =========================================================================
     *
     * Run with assertions enabled:
     *
     *      java -ea ...
     */
    public static void main(String[] args) {

        Preferred preferred = new Preferred();
        FullInorderList full = new FullInorderList();
        RecursiveEarlyStop recursive = new RecursiveEarlyStop();
        MorrisTraversalSafe morris = new MorrisTraversalSafe();

        TreeNode tree1 = exampleOneTree();

        assert preferred.kthSmallest(tree1, 1) == 1;
        assert preferred.kthSmallest(tree1, 2) == 2;
        assert preferred.kthSmallest(tree1, 3) == 3;
        assert preferred.kthSmallest(tree1, 4) == 4;

        TreeNode tree2 = exampleTwoTree();

        int[] expected = {1, 2, 3, 4, 5, 6};

        for (int k = 1; k <= expected.length; k++) {

            int answer = expected[k - 1];

            assert preferred.kthSmallest(tree2, k) == answer;
            assert full.kthSmallest(tree2, k) == answer;
            assert recursive.kthSmallest(tree2, k) == answer;
            assert morris.kthSmallest(tree2, k) == answer;
        }

        // Morris must leave the tree usable after every query.
        assert preferred.kthSmallest(tree2, 3) == 3;
        assert preferred.kthSmallest(tree2, 6) == 6;

        StaticKthIndex index = new StaticKthIndex(tree2);

        assert index.kthSmallest(1) == 1;
        assert index.kthSmallest(3) == 3;
        assert index.kthSmallest(6) == 6;

        OrderStatisticBST orderStatisticBST = new OrderStatisticBST();

        for (int value : new int[]{5, 3, 6, 2, 4, 1}) {
            orderStatisticBST.insert(value);
        }

        assert orderStatisticBST.size() == 6;
        assert orderStatisticBST.kthSmallest(1) == 1;
        assert orderStatisticBST.kthSmallest(3) == 3;
        assert orderStatisticBST.kthSmallest(6) == 6;

        orderStatisticBST.delete(3);

        assert orderStatisticBST.size() == 5;
        assert orderStatisticBST.kthSmallest(1) == 1;
        assert orderStatisticBST.kthSmallest(3) == 4;
        assert orderStatisticBST.kthSmallest(5) == 6;

        FenwickOrderStatistic fenwick = new FenwickOrderStatistic(10_000);

        for (int value : new int[]{5, 3, 6, 2, 4, 1, 3, 3}) {
            fenwick.insert(value);
        }

        // Sorted multiset: 1, 2, 3, 3, 3, 4, 5, 6
        assert fenwick.kthSmallest(1) == 1;
        assert fenwick.kthSmallest(3) == 3;
        assert fenwick.kthSmallest(5) == 3;
        assert fenwick.kthSmallest(8) == 6;

        fenwick.delete(3);

        // Now: 1, 2, 3, 3, 4, 5, 6
        assert fenwick.kthSmallest(5) == 4;

        SegmentTreeOrderStatistic segment = new SegmentTreeOrderStatistic(10_000);

        for (int value : new int[]{5, 3, 6, 2, 4, 1, 3, 3}) {
            segment.insert(value);
        }

        assert segment.kthSmallest(1) == 1;
        assert segment.kthSmallest(5) == 3;
        assert segment.kthSmallest(8) == 6;
        assert segment.rangeCount(2, 4) == 5;

        segment.delete(3);

        assert segment.kthSmallest(5) == 4;
        assert segment.rangeCount(2, 4) == 4;

        KthLargest kthLargest = new KthLargest();

        assert kthLargest.kthLargest(tree2, 1) == 6;
        assert kthLargest.kthLargest(tree2, 3) == 4;

        BSTIterator iterator = new BSTIterator(tree2);

        for (int value : expected) {
            assert iterator.hasNext();
            assert iterator.next() == value;
        }

        assert !iterator.hasNext();

        ValidateBST validator = new ValidateBST();

        assert validator.isValidBST(tree2);
        assert !validator.isValidBST(invalidBST());

        System.out.println("All assertions passed.");
    }
}

/*
 * =========================================================================
 * FINAL RE-DERIVATION
 * =========================================================================
 *
 * If I forget everything:
 *
 * 1. BST inorder is sorted.
 * 2. Therefore kth smallest = kth inorder visit.
 * 3. Iterative inorder:
 *      push left chain -> pop -> process -> go right.
 * 4. POP is the next smallest unvisited node.
 * 5. For frequent dynamic rank queries:
 *      store counts so whole ordered regions can be skipped.
 *
 * That one chain re-derives the entire chapter.
 */
