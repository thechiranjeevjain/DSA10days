package org.chijai.day4.LinkedList.session2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CopyListWithRandomPointer {

    /*
     * ============================================================
     * PRIMARY PROBLEM — COPY LIST WITH RANDOM POINTER
     * ============================================================
     *
     * Deep-copy a linked list where every node has:
     *
     *      next
     *      random
     *
     * random may point to any node in the list or null.
     *
     * Deep copy requires:
     *
     *      1. every original node gets exactly one NEW clone
     *      2. next relationships are preserved among clones
     *      3. random relationships are preserved among clones
     *      4. no cloned pointer references an original node
     *
     * Pattern:
     * Original Node -> Clone Node Identity Mapping
     *
     * Preferred Interview Solution:
     * HashMap + Two Passes
     *
     * Time  : O(n)
     * Space : O(n)
     */

    static final class Node {
        int val;
        Node next;
        Node random;

        Node(int val) {
            this.val = val;
        }
    }

    /*
     * ============================================================
     * PREFERRED INTERVIEW SOLUTION
     * ============================================================
     */

    static final class Preferred {

        Node copyRandomList(Node head) {

            if (head == null) {
                return null;
            }

            Map<Node, Node> originalToClone = new HashMap<>();

            Node current = head;

            while (current != null) {
                originalToClone.put(current, new Node(current.val));
                current = current.next;
            }

            current = head;

            while (current != null) {

                Node clone = originalToClone.get(current);

                clone.next = originalToClone.get(current.next);
                clone.random = originalToClone.get(current.random);

                current = current.next;
            }

            return originalToClone.get(head);
        }
    }

    /*
     * ============================================================
     * WHY 1 — WHAT IS THE REAL PROBLEM?
     * ============================================================
     *
     * Copying values is easy.
     *
     * The real problem is preserving IDENTITY.
     *
     * If:
     *
     *      original A.random -> original C
     *
     * then:
     *
     *      clone A.random -> clone C
     *
     * NOT:
     *
     *      clone A.random -> original C
     *
     * Core invariant:
     *
     *      every original node maps to exactly one clone.
     */

    /*
     * ============================================================
     * WHY 2 — WHY A HASHMAP?
     * ============================================================
     *
     * random can point:
     *
     *      backward
     *      forward
     *      to itself
     *      to a shared target
     *      to null
     *
     * Therefore we need a reliable answer to:
     *
     *      "Given this original node,
     *       where is its corresponding clone?"
     *
     * HashMap stores exactly that:
     *
     *      original -> clone
     */

    /*
     * ============================================================
     * WHY 3 — WHY TWO PASSES?
     * ============================================================
     *
     * PASS 1 — CREATE ALL CLONES
     *
     *      original -> clone
     *
     * PASS 2 — WIRE ALL EDGES
     *
     *      clone.next   = map.get(original.next)
     *      clone.random = map.get(original.random)
     *
     * This gives a reusable discipline:
     *
     *      CREATE IDENTITY FIRST.
     *      WIRE RELATIONSHIPS SECOND.
     *
     * It avoids asking whether a random target has already been cloned.
     */

    /*
     * ============================================================
     * WHY 4 — WHY DOES map.get(null) WORK CLEANLY?
     * ============================================================
     *
     * No null key is inserted.
     *
     * Therefore:
     *
     *      map.get(null) -> null
     *
     * So both lines naturally preserve null edges:
     *
     *      clone.next   = map.get(current.next);
     *      clone.random = map.get(current.random);
     */

    /*
     * ============================================================
     * WHY 5 — WHY IS THIS THE SOLUTION TO OWN?
     * ============================================================
     *
     * The HashMap solution captures the REUSABLE pattern:
     *
     *      original identity -> cloned identity
     *
     * This transfers directly to:
     *
     *      Clone Graph
     *      tree with random pointers
     *      arbitrary object graphs
     *      multiple cross-references
     *
     * The O(1)-space interleaving solution is clever,
     * but it is a structure-specific optimization.
     *
     * STUDY PRIORITY:
     *
     *      MUST OWN      -> HashMap identity mapping
     *      SHOULD KNOW   -> interleaving idea
     *      OPTIONAL DRILL-> perfect interleaving recall
     */

    /*
     * ============================================================
     * 30-SECOND RECALL CARD
     * ============================================================
     *
     * TRIGGER
     * -------
     * Deep copy + arbitrary references.
     *
     * PATTERN
     * -------
     * Original -> Clone map.
     *
     * INVARIANT
     * ---------
     * One original = one clone.
     * Every cloned edge points to a clone.
     *
     * TEMPLATE
     * --------
     *
     * Pass 1:
     *      while (current != null)
     *          map.put(current, new clone)
     *          current = current.next
     *
     * Pass 2:
     *      while (current != null)
     *          clone.next   = map.get(current.next)
     *          clone.random = map.get(current.random)
     *          current = current.next
     *
     * return map.get(head)
     *
     * MEMORY:
     *
     *      CLONE FIRST.
     *      WIRE SECOND.
     */

    /*
     * ============================================================
     * INTERVIEW ARTICULATION
     * ============================================================
     *
     * "I treat this as an identity-mapping problem.
     *
     * Every original node must correspond to exactly one cloned node,
     * because next and random references may point to the same object.
     *
     * In the first pass, I create every clone and store
     * original -> clone in a HashMap.
     *
     * In the second pass, I translate each original edge through
     * that map:
     *
     * clone.next is the clone of original.next,
     * and clone.random is the clone of original.random.
     *
     * Because every pointer assignment goes through the mapping,
     * the cloned list preserves the same topology without pointing
     * back into the original list.
     *
     * Time is O(n), space is O(n)."
     *
     * FOLLOW-UP IF ASKED FOR O(1) EXTRA SPACE:
     *
     * "I can remove the HashMap by temporarily placing each clone
     * directly after its original node, so original.next itself
     * becomes the original-to-clone mapping."
     */

    /*
     * ============================================================
     * REUSABLE MASTER TEMPLATE — DEEP COPY / CLONE
     * ============================================================
     *
     * 1. Establish identity:
     *
     *      original -> clone
     *
     * 2. Ensure one clone per original.
     *
     * 3. Translate every edge:
     *
     *      clone.edge = cloneOf(original.edge)
     *
     * 4. For recursive graph-like structures:
     *
     *      REGISTER CLONE BEFORE EXPLORING EDGES.
     *
     * Master invariant:
     *
     *      CLONE ONCE.
     *      REUSE EVERYWHERE.
     *      NEVER POINT BACK TO ORIGINAL.
     */

    /*
     * ============================================================
     * CODING STYLE — WHY WHILE LOOPS HERE?
     * ============================================================
     *
     * Linked lists expose sequential pointer movement directly.
     *
     * Prefer:
     *
     *      Node current = head;
     *
     *      while (current != null) {
     *          ...
     *          current = current.next;
     *      }
     *
     * This keeps the state transition visible:
     *
     *      CURRENT NODE -> PROCESS -> ADVANCE
     *
     * It also matches how linked-list code is usually reconstructed
     * under interview pressure.
     *
     * V3 RULE:
     * Use while loops for traversal in this study file.
     */

    /*
     * ============================================================
     * APPROACH PROGRESSION
     * ============================================================
     *
     * 1. HashMap + Two Passes
     *
     *      Time  : O(n)
     *      Space : O(n)
     *
     *      Preferred interview answer.
     *      Simplest proof.
     *      Most reusable pattern.
     *
     * 2. Interleaving / Weaving
     *
     *      Time  : O(n)
     *      Space : O(1) auxiliary
     *
     *      Follow-up optimization.
     *      Temporarily mutates the original list.
     *      More pointer manipulation and bug surface.
     *
     * Decision:
     *
     *      O(n) space acceptable?
     *          -> HashMap.
     *
     *      Interviewer explicitly asks O(1) extra space?
     *          -> Interleaving.
     */

    /*
     * ============================================================
     * FOLLOW-UP — O(1) EXTRA SPACE INTERLEAVING
     * ============================================================
     *
     * DO I NEED TO LEARN THIS?
     * ------------------------
     *
     * Yes, but as a SECONDARY optimization.
     *
     * Learn the idea:
     *
     *      WEAVE
     *      WIRE RANDOM
     *      UNWEAVE
     *
     * Do not spend equal effort memorizing it.
     *
     * ------------------------------------------------------------
     * STEP 1 — WEAVE
     * ------------------------------------------------------------
     *
     * Original:
     *
     *      A -> B -> C
     *
     * After inserting each clone after its original:
     *
     *      A -> A' -> B -> B' -> C -> C'
     *
     * Now:
     *
     *      original.next = its clone
     *
     * The list itself temporarily replaces the HashMap.
     *
     * ------------------------------------------------------------
     * STEP 2 — WIRE RANDOM
     * ------------------------------------------------------------
     *
     * Suppose:
     *
     *      A.random = C
     *
     * Then:
     *
     *      clone(A) = A.next
     *      clone(C) = C.next
     *
     * Therefore:
     *
     *      A.next.random = A.random.next
     *
     * ------------------------------------------------------------
     * STEP 3 — UNWEAVE
     * ------------------------------------------------------------
     *
     * Restore:
     *
     *      A -> B -> C
     *
     * Extract:
     *
     *      A' -> B' -> C'
     *
     * ------------------------------------------------------------
     * INTERVIEW ROI
     * ------------------------------------------------------------
     *
     * HIGH:
     *      understand why weaving replaces the map
     *
     * MEDIUM:
     *      be able to derive the three passes
     *
     * LOW-MEDIUM:
     *      memorize every pointer reassignment exactly
     *
     * Prefer derivation over memorization.
     */

    static final class InterleavingFollowUp {

        Node copyRandomList(Node head) {

            if (head == null) {
                return null;
            }

            // 1. WEAVE.
            Node current = head;

            while (current != null) {

                Node clone = new Node(current.val);

                clone.next = current.next;
                current.next = clone;

                current = clone.next;
            }

            // 2. WIRE RANDOM.
            current = head;

            while (current != null) {

                if (current.random != null) {
                    current.next.random = current.random.next;
                }

                current = current.next.next;
            }

            // 3. UNWEAVE.
            Node cloneHead = head.next;
            current = head;

            while (current != null) {

                Node clone = current.next;
                Node nextOriginal = clone.next;

                current.next = nextOriginal;

                clone.next =
                        nextOriginal == null
                                ? null
                                : nextOriginal.next;

                current = nextOriginal;
            }

            return cloneHead;
        }
    }

    /*
     * ============================================================
     * INTERLEAVING — INTERVIEW ARTICULATION
     * ============================================================
     *
     * "The HashMap is only being used to answer:
     *  where is the clone of this original?
     *
     * I can encode that mapping physically by inserting every clone
     * immediately after its original.
     *
     * Then original.next is its clone, and original.random.next
     * is the clone of its random target.
     *
     * After assigning random pointers, I separate the interleaved
     * structure into the restored original list and the cloned list.
     *
     * This keeps O(n) time while reducing auxiliary space to O(1),
     * at the cost of temporarily modifying the input."
     */

    /*
     * ============================================================
     * WHEN NOT TO USE INTERLEAVING
     * ============================================================
     *
     * Avoid / do not force it when:
     *
     *      original structure must never be mutated
     *      concurrent readers may observe the list
     *      O(n) extra space is acceptable
     *      generalizing to graph/object cloning
     *
     * Important:
     *
     * Interleaving is NOT the master pattern.
     *
     * Identity mapping IS.
     */

    /*
     * ============================================================
     * RELATED / REINFORCEMENT — CLONE GRAPH
     * ============================================================
     *
     * SAME invariant:
     *
     *      every original node gets exactly one clone.
     *
     * Difference:
     *
     *      list -> linear traversal
     *      graph -> DFS/BFS traversal
     *
     * Critical rule:
     *
     *      REGISTER BEFORE RECURSION.
     *
     * Why?
     *
     * Cycles may revisit the same original node before the first
     * recursive path finishes.
     */

    static final class GraphNode {

        int val;
        List<GraphNode> neighbors = new ArrayList<>();

        GraphNode(int val) {
            this.val = val;
        }
    }

    static final class CloneGraph {

        GraphNode cloneGraph(GraphNode node) {
            return clone(node, new HashMap<>());
        }

        private GraphNode clone(
                GraphNode original,
                Map<GraphNode, GraphNode> originalToClone
        ) {

            if (original == null) {
                return null;
            }

            if (originalToClone.containsKey(original)) {
                return originalToClone.get(original);
            }

            GraphNode copy = new GraphNode(original.val);

            // Register BEFORE exploring cyclic edges.
            originalToClone.put(original, copy);

            int index = 0;

            while (index < original.neighbors.size()) {

                GraphNode neighbor = original.neighbors.get(index);

                copy.neighbors.add(
                        clone(neighbor, originalToClone)
                );

                index++;
            }

            return copy;
        }
    }

    /*
     * ============================================================
     * RELATED / REINFORCEMENT PROBLEMS
     * ============================================================
     *
     * 1. Clone Graph
     *    Same original -> clone identity map.
     *    Traversal becomes DFS/BFS.
     *
     * 2. Binary Tree with Random Pointer
     *    random links make the reachable structure graph-like.
     *    Same identity map.
     *
     * 3. Copy List with Multiple Random Pointers
     *    Same mapping; translate every random edge.
     *
     * 4. Arbitrary Object Graph Deep Copy
     *    Same master rule:
     *
     *      clone once
     *      register
     *      wire/recurse
     *
     * Transfer lesson:
     *
     *      STRUCTURE MAY CHANGE.
     *      IDENTITY INVARIANT DOES NOT.
     */

    /*
     * ============================================================
     * COMMON FAILURE MODES
     * ============================================================
     *
     * 1. clone.random = current.random
     *
     *      -> points into original list
     *      -> shallow copy bug
     *
     * 2. Create a fresh clone every time a node is referenced
     *
     *      -> one original gets multiple clones
     *      -> identity broken
     *
     * 3. Graph clone: recurse before registering clone
     *
     *      -> cycle may recurse forever
     *
     * 4. Interleaving: forget to restore original next pointers
     *
     *      -> input remains corrupted
     *
     * 5. Interleaving:
     *
     *      clone.random = current.random
     *
     *      WRONG.
     *
     * Correct:
     *
     *      clone.random = current.random.next
     *
     * because current.random.next is the RANDOM TARGET'S CLONE.
     */

    /*
     * ============================================================
     * SELF-VERIFYING TESTS
     * ============================================================
     *
     * Run with assertions enabled:
     *
     *      java -ea CopyListWithRandomPointerV3
     */

    public static void main(String[] args) {

        Preferred preferred = new Preferred();
        InterleavingFollowUp interleaving = new InterleavingFollowUp();

        // Representative case.
        {
            Node head = buildSample();

            Node copy = preferred.copyRandomList(head);

            assertDeepCopy(head, copy);
        }

        // Same case using O(1)-space follow-up.
        {
            Node head = buildSample();

            Node originalSecond = head.next;
            Node originalThird = head.next.next;

            Node copy = interleaving.copyRandomList(head);

            // Original list must be restored.
            assert head.next == originalSecond;
            assert originalSecond.next == originalThird;
            assert originalThird.next == null;

            assertDeepCopy(head, copy);
        }

        // Null.
        assert preferred.copyRandomList(null) == null;
        assert interleaving.copyRandomList(null) == null;

        // Self-random.
        {
            Node node = new Node(7);
            node.random = node;

            Node copy = preferred.copyRandomList(node);

            assert copy != node;
            assert copy.random == copy;
        }

        // Shared random target.
        {
            Node a = new Node(1);
            Node b = new Node(2);
            Node c = new Node(3);

            a.next = b;
            b.next = c;

            a.random = c;
            b.random = c;

            Node copy = preferred.copyRandomList(a);

            assert copy.random == copy.next.random;
            assert copy.random == copy.next.next;
            assert copy.random != c;
        }

        // Clone Graph transfer test.
        {
            GraphNode a = new GraphNode(1);
            GraphNode b = new GraphNode(2);

            a.neighbors.add(b);
            b.neighbors.add(a);

            GraphNode copy = new CloneGraph().cloneGraph(a);

            assert copy != a;
            assert copy.neighbors.get(0) != b;
            assert copy.neighbors.get(0).neighbors.get(0) == copy;
        }

        System.out.println("All assertions passed.");
    }

    private static Node buildSample() {

        Node one = new Node(1);
        Node two = new Node(2);
        Node three = new Node(3);

        one.next = two;
        two.next = three;

        one.random = three;
        two.random = one;
        three.random = two;

        return one;
    }

    private static void assertDeepCopy(Node original, Node copy) {

        Node o1 = original;
        Node o2 = original.next;
        Node o3 = original.next.next;

        Node c1 = copy;
        Node c2 = copy.next;
        Node c3 = copy.next.next;

        assert c1 != o1;
        assert c2 != o2;
        assert c3 != o3;

        assert c1.val == o1.val;
        assert c2.val == o2.val;
        assert c3.val == o3.val;

        assert c1.random == c3;
        assert c2.random == c1;
        assert c3.random == c2;

        assert c1.random != o3;
        assert c2.random != o1;
        assert c3.random != o2;

        assert c3.next == null;
    }
}
