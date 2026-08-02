package org.chijai.day8.session3;

import java.util.*;

/**
 * ============================================================================
 *  AccountsMerge
 * ============================================================================
 *
 *  LeetCode:
 *  https://leetcode.com/problems/accounts-merge/
 *
 * ============================================================================
 *  📘 PRIMARY PROBLEM
 * ============================================================================
 *
 * Title:
 * Accounts Merge
 *
 * Difficulty:
 * Hard
 *
 * Tags:
 * Graph
 * Disjoint Set Union (Union Find)
 * Connected Components
 * HashMap
 * Set
 * Sorting
 *
 * ----------------------------------------------------------------------------
 * Problem
 * ----------------------------------------------------------------------------
 *
 * A person may own multiple accounts.
 *
 * Each account is represented as:
 *
 *      [name, email1, email2, email3...]
 *
 * Two accounts belong to the same person if they share at least one email.
 *
 * The name itself is NOT sufficient because different people may have the same
 * name.
 *
 * Merge all accounts belonging to the same person.
 *
 * Output:
 *
 * One merged account per connected person.
 *
 * Every merged account contains:
 *
 *      name
 *      all unique emails sorted lexicographically
 *
 * Order of merged accounts does not matter.
 *
 * ----------------------------------------------------------------------------
 * Constraints
 * ----------------------------------------------------------------------------
 *
 * 1 <= accounts.length <= 1000
 *
 * 2 <= accounts[i].length <= 10
 *
 * 1 <= total emails <= 10000
 *
 * Email strings are unique inside one account.
 *
 * ----------------------------------------------------------------------------
 * Example
 * ----------------------------------------------------------------------------
 *
 * Input
 *
 * [
 *   ["John","johnsmith@mail.com","john_newyork@mail.com"],
 *   ["John","johnsmith@mail.com","john00@mail.com"],
 *   ["Mary","mary@mail.com"],
 *   ["John","johnnybravo@mail.com"]
 * ]
 *
 * Output
 *
 * [
 *   ["John",
 *    "john00@mail.com",
 *    "john_newyork@mail.com",
 *    "johnsmith@mail.com"],
 *
 *   ["Mary","mary@mail.com"],
 *
 *   ["John","johnnybravo@mail.com"]
 * ]
 *
 * Explanation
 *
 * Account 0 and Account 1 share an email.
 *
 * Therefore they are the same connected component.
 *
 * Account 2 is isolated.
 *
 * Account 3 is isolated.
 *
 * ============================================================================
 *  🔵 CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern
 * -------
 * Disjoint Set Union (Union Find)
 *
 * Archetype
 * ---------
 * Dynamic Connected Components
 *
 * Core Invariant
 * --------------
 * Every account inside one connected component has exactly one representative
 * (root).
 *
 * If two accounts share even one email, they MUST eventually obtain the same
 * representative.
 *
 * Why It Works
 * ------------
 * Email overlap defines graph edges.
 *
 * Accounts are graph nodes.
 *
 * Shared email means:
 *
 *      accountA ----- accountB
 *
 * We never actually build the graph.
 *
 * Union Find maintains connected components incrementally while scanning
 * emails.
 *
 * Recognition Signals
 * -------------------
 *
 * ✓ Merge groups
 *
 * ✓ Connectivity
 *
 * ✓ Transitive relation
 *
 * ✓ "belongs to same person"
 *
 * ✓ Common identifier joins objects
 *
 * ✓ Components discovered through repeated merging
 *
 * When To Use
 * -----------
 *
 * • Merge accounts
 *
 * • Merge islands
 *
 * • Friend circles
 *
 * • Network connectivity
 *
 * • Similar strings
 *
 * • Kruskal MST
 *
 * When NOT To Use
 * ---------------
 *
 * Do not use DSU if:
 *
 * • shortest path required
 *
 * • traversal order matters
 *
 * • parent-child hierarchy matters
 *
 * • graph changes require deletions
 *
 * Pattern Comparison
 * ------------------
 *
 * DFS/BFS
 * --------
 * Requires explicit graph construction.
 *
 * DSU
 * ---
 * Builds connectivity online while reading edges.
 *
 * Graph Coloring
 * --------------
 * Answers reachability.
 *
 * DSU
 * ---
 * Answers connected component membership.
 *
 * ============================================================================
 *  🟢 MENTAL MODEL & INVARIANTS
 * ============================================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine every account starts as its own island.
 *
 * Every time an email appears again,
 * we discover a bridge between two islands.
 *
 * DSU permanently joins those islands.
 *
 * Eventually every connected island represents one person.
 *
 * ---------------------------------------------------------------------------
 * 🟢 Invariant 1
 * ---------------------------------------------------------------------------
 *
 * parent[root] == root
 *
 * Every connected component owns exactly one representative.
 *
 * ---------------------------------------------------------------------------
 * 🟢 Invariant 2
 * ---------------------------------------------------------------------------
 *
 * find(x)
 *
 * always returns the representative of x's component.
 *
 * After path compression,
 * future finds become almost constant time.
 *
 * ---------------------------------------------------------------------------
 * 🟢 Invariant 3
 * ---------------------------------------------------------------------------
 *
 * emailToAccount
 *
 * always stores the FIRST account that introduced an email.
 *
 * When another account contains the same email,
 * those two accounts must be unioned.
 *
 * Notice:
 *
 * We never need to remember every account containing an email.
 *
 * One representative account is sufficient because DSU preserves transitivity.
 *
 * Example
 *
 * email E
 *
 * first seen in account 2
 *
 * later appears in
 *
 * account 7
 *
 * union(2,7)
 *
 * later appears again
 *
 * account 15
 *
 * union(2,15)
 *
 * Since 2 and 7 are already connected,
 * all three become connected.
 *
 * ---------------------------------------------------------------------------
 * 🟢 Invariant 4
 * ---------------------------------------------------------------------------
 *
 * During grouping,
 *
 * every account contributes ALL its emails into exactly one root bucket.
 *
 * Bucket =
 * connected component.
 *
 * ---------------------------------------------------------------------------
 * 🟢 Variable Meanings
 * ---------------------------------------------------------------------------
 *
 * parent[]
 *
 * DSU forest
 *
 * emailToFirstAccount
 *
 * First occurrence of every email.
 *
 * rootToEmails
 *
 * Emails belonging to one connected component.
 *
 * root
 *
 * Representative of one merged person.
 *
 * ---------------------------------------------------------------------------
 * Allowed State Transitions
 * ---------------------------------------------------------------------------
 *
 * unseen email
 *
 *      ->
 *
 * record owner
 *
 *
 * repeated email
 *
 *      ->
 *
 * union owners
 *
 *
 * finished scanning
 *
 *      ->
 *
 * group by root
 *
 * ---------------------------------------------------------------------------
 * Forbidden Moves
 * ---------------------------------------------------------------------------
 *
 * ❌ Merge by person name.
 *
 * Different people may share identical names.
 *
 * ❌ Assume adjacent accounts are related.
 *
 * Connectivity depends ONLY on shared emails.
 *
 * ❌ Output before grouping by representative.
 *
 * Intermediate parents are not guaranteed to be roots.
 *
 * ---------------------------------------------------------------------------
 * Why Naive Solutions Fail
 * ---------------------------------------------------------------------------
 *
 * Consider
 *
 * A shares with B
 *
 * B shares with C
 *
 * A never directly shares with C.
 *
 * Pairwise merging misses transitive closure.
 *
 * DSU automatically preserves transitivity.
 *
 * ---------------------------------------------------------------------------
 * Termination
 * ---------------------------------------------------------------------------
 *
 * Every account scanned exactly once.
 *
 * Every email processed exactly once.
 *
 * Every account assigned to exactly one root.
 *
 * ============================================================================
 *  🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================================
 *
 * Mistake 1
 * ---------
 * Merge by names.
 *
 * Looks reasonable because output starts with names.
 *
 * Violated Invariant
 * ------------------
 * Connectivity is defined only by shared emails.
 *
 * Counterexample
 *
 * John
 *
 * john1@mail
 *
 * John
 *
 * john2@mail
 *
 * Different people.
 *
 * ---------------------------------------------------------------------------
 * Mistake 2
 * ---------
 * Store every email owner in a list.
 *
 * Looks harmless.
 *
 * Actually unnecessary.
 *
 * One representative account is enough because unions are transitive.
 *
 * ---------------------------------------------------------------------------
 * Mistake 3
 * ---------
 * Forget path compression.
 *
 * Correctness survives.
 *
 * Performance degrades badly.
 *
 * ---------------------------------------------------------------------------
 * Mistake 4
 * ---------
 * Build answer before calling find().
 *
 * Parent pointers may still point to intermediate nodes.
 *
 * Multiple buckets appear for one component.
 *
 * ============================================================================
 *  ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================================
 *
 * Typing Order
 * ------------
 *
 * 1.
 * Build DSU.
 *
 * 2.
 * Create email -> first account map.
 *
 * 3.
 * Scan every account.
 *
 * 4.
 * First email occurrence?
 *      store account.
 *
 * Otherwise
 *      union(previous,current).
 *
 * 5.
 * Scan accounts again.
 *
 * 6.
 * Find root.
 *
 * 7.
 * Add emails into root bucket.
 *
 * 8.
 * Sort emails.
 *
 * 9.
 * Produce answer.
 *
 * Function Skeleton
 * -----------------
 *
 * accountsMerge(...)
 *
 * create DSU
 *
 * first pass
 *
 * second pass
 *
 * build answer
 *
 * return
 *
 * ============================================================================
 *  🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================================
 *
 * init DSU
 *
 * for every account
 *      union repeated emails
 *
 * for every account
 *      root=find(account)
 *      collect emails
 *
 * format answer
 *
 * return
 *
 * ============================================================================
 *  6. SOLUTION CLASSES
 * ============================================================================
 *
 * ---------------------------------------------------------------------------
 * Brute Force
 * ---------------------------------------------------------------------------
 *
 * Idea
 * ----
 * Keep repeatedly comparing every pair of accounts until no merges remain.
 *
 * Invariant
 * ---------
 * Every iteration reduces the number of components.
 *
 * Limitation
 * ----------
 * Extremely expensive because every merge changes future comparisons.
 *
 * Complexity
 * ----------
 * Roughly O(N² × E)
 *
 * Interview Usefulness
 * --------------------
 * Good only as a starting discussion.
 *
 * ---------------------------------------------------------------------------
 * Improved
 * ---------------------------------------------------------------------------
 *
 * Idea
 * ----
 * Build explicit graph:
 *
 * account -> account
 *
 * using shared emails,
 * then run DFS over connected components.
 *
 * Invariant
 * ---------
 * One DFS visits exactly one connected component.
 *
 * Improvement
 * -----------
 * Avoid repeated merging.
 *
 * Complexity
 * ----------
 * O(E log E) for sorting + graph construction.
 *
 * Interview Usefulness
 * --------------------
 * Acceptable but graph construction is unnecessary.
 *
 * ---------------------------------------------------------------------------
 * Optimal (Interview Preferred)
 * ---------------------------------------------------------------------------
 *
 * Idea
 * ----
 * Union accounts while reading emails.
 *
 * Never explicitly construct graph edges.
 *
 * Invariant
 * ---------
 * DSU representative uniquely identifies one merged person.
 *
 * Correctness
 * -----------
 * Every shared email creates an edge.
 * DSU computes connected components induced by those edges.
 *
 * Complexity
 * ----------
 * Time:
 * O(T α(N) + T log T)
 *
 * T = total emails.
 *
 * Space:
 * O(T)
 *
 * Interview Usefulness
 * --------------------
 * Canonical DSU problem.
 */

public class AccountsMerge {

    /**
     * Stores one merged account before formatting.
     */
    static class MergedAccount {

        final String name;

        final TreeSet<String> emails = new TreeSet<>();

        MergedAccount(String name) {
            this.name = name;
        }

        List<String> toList() {
            List<String> result = new ArrayList<>();
            result.add(name);
            result.addAll(emails);
            return result;
        }
    }

    /**
     * Classic Union Find with
     * path compression
     * union by size.
     */
    static class UnionFind {

        private final int[] parent;

        private final int[] size;

        UnionFind(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int node) {

            // 🟢 Invariant:
            // Returned node is always the component representative.
            if (parent[node] == node) {
                return node;
            }

            parent[node] = find(parent[node]);

            return parent[node];
        }

        void union(int a, int b) {

            int rootA = find(a);
            int rootB = find(b);

            // Already one connected component.
            if (rootA == rootB) {
                return;
            }

            // Keep larger tree as representative.
            if (size[rootA] < size[rootB]) {
                int temp = rootA;
                rootA = rootB;
                rootB = temp;
            }

            parent[rootB] = rootA;
            size[rootA] += size[rootB];
        }
    }

    /**
     * ------------------------------------------------------------------------
     * Optimal Interview Solution
     * ------------------------------------------------------------------------
     */
    static class OptimalSolution {

        public List<List<String>> accountsMerge(List<List<String>> accounts) {

            if (accounts == null || accounts.isEmpty()) {
                return Collections.emptyList();
            }

            UnionFind unionFind = new UnionFind(accounts.size());

            // Email -> first account index that introduced this email.
            Map<String, Integer> emailToFirstAccount = new HashMap<>();

            // -----------------------------------------------------------------
            // First Pass
            //
            // Build connected components online.
            // -----------------------------------------------------------------
            for (int accountIndex = 0; accountIndex < accounts.size(); accountIndex++) {

                List<String> account = accounts.get(accountIndex);

                for (int emailIndex = 1; emailIndex < account.size(); emailIndex++) {

                    String email = account.get(emailIndex);

                    Integer previousOwner = emailToFirstAccount.get(email);

                    if (previousOwner == null) {

                        // First occurrence establishes the representative owner.
                        emailToFirstAccount.put(email, accountIndex);

                    } else {

                        // Shared email means both accounts belong to one component.
                        unionFind.union(previousOwner, accountIndex);
                    }
                }
            }

            // -----------------------------------------------------------------
            // Second Pass
            //
            // Gather every email under the true DSU representative.
            // -----------------------------------------------------------------
            Map<Integer, MergedAccount> rootToAccount = new HashMap<>();

            for (int accountIndex = 0; accountIndex < accounts.size(); accountIndex++) {

                int root = unionFind.find(accountIndex);

                MergedAccount merged =
                        rootToAccount.computeIfAbsent(
                                root,
                                ignored -> new MergedAccount(accounts.get(root).get(0))
                        );

                List<String> account = accounts.get(accountIndex);

                for (int emailIndex = 1; emailIndex < account.size(); emailIndex++) {

                    // TreeSet guarantees uniqueness and sorted order.
                    merged.emails.add(account.get(emailIndex));
                }
            }

            List<List<String>> answer = new ArrayList<>();

            for (MergedAccount account : rootToAccount.values()) {
                answer.add(account.toList());
            }

            return answer;
        }
    }

    /**
     * ------------------------------------------------------------------------
     * Alternative Optimal Solution
     *
     * Group using the unique email map instead of iterating every account twice.
     * ------------------------------------------------------------------------
     */
    static class EmailCentricSolution {

        public List<List<String>> accountsMerge(List<List<String>> accounts) {

            if (accounts == null || accounts.isEmpty()) {
                return Collections.emptyList();
            }

            UnionFind unionFind = new UnionFind(accounts.size());

            Map<String, Integer> emailToFirstAccount = new HashMap<>();

            // -------------------------------------------------------------
            // Step 1
            //
            // Connect accounts whenever an email repeats.
            // -------------------------------------------------------------
            for (int accountIndex = 0; accountIndex < accounts.size(); accountIndex++) {

                List<String> account = accounts.get(accountIndex);

                for (int emailIndex = 1; emailIndex < account.size(); emailIndex++) {

                    String email = account.get(emailIndex);

                    Integer previous = emailToFirstAccount.putIfAbsent(email, accountIndex);

                    if (previous != null) {

                        // Shared identifier discovered.
                        unionFind.union(previous, accountIndex);
                    }
                }
            }

            // -------------------------------------------------------------
            // Step 2
            //
            // Every unique email is processed exactly once.
            // -------------------------------------------------------------
            Map<Integer, TreeSet<String>> rootToEmails = new HashMap<>();

            for (Map.Entry<String, Integer> entry : emailToFirstAccount.entrySet()) {

                int root = unionFind.find(entry.getValue());

                rootToEmails
                        .computeIfAbsent(root, ignored -> new TreeSet<>())
                        .add(entry.getKey());
            }

            List<List<String>> answer = new ArrayList<>();

            for (Map.Entry<Integer, TreeSet<String>> entry : rootToEmails.entrySet()) {

                LinkedList<String> merged = new LinkedList<>();

                merged.add(accounts.get(entry.getKey()).get(0));

                merged.addAll(entry.getValue());

                answer.add(merged);
            }

            return answer;
        }
    }

/**
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * What is the invariant?
 * ----------------------
 *
 * Every connected person is represented by exactly one DSU root.
 *
 * Every account belonging to that person eventually compresses to the same
 * representative.
 *
 * -------------------------------------------------------------------------
 * Why is the discard rule safe?
 * -------------------------------------------------------------------------
 *
 * We never discard information.
 *
 * Whenever a repeated email is discovered,
 * we merge the two connected components.
 *
 * Since connectivity is transitive,
 * all future accounts automatically inherit that relationship.
 *
 * -------------------------------------------------------------------------
 * Why is the solution correct?
 * -------------------------------------------------------------------------
 *
 * Every shared email introduces one graph edge.
 *
 * DSU computes connected components over all such edges.
 *
 * Every connected component corresponds to exactly one real person.
 *
 * Collecting emails by DSU representative therefore merges precisely the
 * correct accounts.
 *
 * -------------------------------------------------------------------------
 * Why does the algorithm terminate?
 * -------------------------------------------------------------------------
 *
 * Every account is scanned once.
 *
 * Every email is processed once while building unions.
 *
 * Every account contributes once while grouping.
 *
 * No loop revisits unfinished work indefinitely.
 *
 * -------------------------------------------------------------------------
 * Can this be done in-place?
 * -------------------------------------------------------------------------
 *
 * No.
 *
 * New structures are fundamentally required:
 *
 * • email -> account map
 * • DSU arrays
 * • merged email sets
 *
 * -------------------------------------------------------------------------
 * Is streaming possible?
 * -------------------------------------------------------------------------
 *
 * Partially.
 *
 * Online union operations are naturally streaming.
 *
 * Final output cannot be streamed because lexicographically sorted emails
 * require all emails of each component before emission.
 *
 * -------------------------------------------------------------------------
 * When should DSU NOT be chosen?
 * -------------------------------------------------------------------------
 *
 * When relationships are directional.
 *
 * When shortest paths are required.
 *
 * When deletions must dynamically split components.
 *
 * When tree ancestry rather than connectivity is the objective.
 *
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================================
 *
 * Trigger
 * -------
 * Merge objects connected through shared identifiers.
 *
 * Pattern
 * -------
 * Union Find.
 *
 * Search Space
 * ------------
 * Accounts.
 *
 * State
 * -----
 * Connected component representative.
 *
 * Transition
 * ----------
 * Repeated email
 * ->
 * union(previous,current)
 *
 * Discard Rule
 * ------------
 * None.
 *
 * Connectivity only expands.
 *
 * Common Trap
 * -----------
 * Merge by names instead of emails.
 *
 * Edge Cases
 * ----------
 * Single account.
 *
 * Duplicate names.
 *
 * One huge connected component.
 *
 * Accounts containing only one email.
 *
 * One-Liner
 * ---------
 * Shared email means shared DSU representative.
 *
 * Re-Derivation Cue
 * -----------------
 * Email is the edge.
 *
 * DSU computes connected components.
 *
 * Group by root.
 *
 * =========================================================================
 * 🔄 VARIATIONS & TWEAKS
 * =========================================================================
 *
 * Variation 1
 * -----------
 * Email nodes instead of account nodes.
 *
 * Invariant
 * ---------
 * Every connected email belongs to one owner.
 *
 * Works?
 * ------
 * Yes.
 *
 * Frequently used in graph formulations.
 *
 * Variation 2
 * -----------
 * DFS over explicit graph.
 *
 * Invariant
 * ---------
 * One DFS visits one connected component.
 *
 * Works?
 * ------
 * Yes.
 *
 * Higher graph construction overhead.
 *
 * Variation 3
 * -----------
 * Union by email string directly.
 *
 * Works?
 * ------
 * Yes.
 *
 * Requires mapping every email to an integer id.
 *
 * Often used when accounts are extremely large.
 *
 * Variation 4
 * -----------
 * Remove path compression.
 *
 * Correct?
 * --------
 * Yes.
 *
 * Efficient?
 * ----------
 * No.
 *
 * Trees may become tall.
 *
 * Continue with the remaining sections and tests.
 */

    /**
     * =========================================================================
     * 🧠 MASTERY CHECKLIST
     * =========================================================================
     *
     * Q. What is the invariant?
     * -------------------------
     * Every connected person has exactly one DSU representative.
     *
     * -------------------------------------------------------------------------
     * Q. What is the search space?
     * -------------------------------------------------------------------------
     * Account indices.
     *
     * -------------------------------------------------------------------------
     * Q. What defines a transition?
     * -------------------------------------------------------------------------
     * Encountering an already-seen email.
     *
     * -------------------------------------------------------------------------
     * Q. What is the merge rule?
     * -------------------------------------------------------------------------
     * union(currentAccount, firstAccountOwningEmail)
     *
     * -------------------------------------------------------------------------
     * Q. Why is one previous owner sufficient?
     * -------------------------------------------------------------------------
     * DSU preserves transitive connectivity.
     *
     * If
     *
     * A ↔ B
     * B ↔ C
     *
     * then
     *
     * A, B and C eventually obtain the same representative.
     *
     * -------------------------------------------------------------------------
     * Q. Why doesn't merging by names work?
     * -------------------------------------------------------------------------
     * Names are not unique identifiers.
     *
     * Emails are.
     *
     * -------------------------------------------------------------------------
     * Q. Why group after every union?
     * -------------------------------------------------------------------------
     * Parent pointers may still be intermediate.
     *
     * Calling find() guarantees the true representative.
     *
     * -------------------------------------------------------------------------
     * Q. Which operation dominates complexity?
     * -------------------------------------------------------------------------
     * Sorting emails inside each merged component.
     *
     * DSU operations are almost constant:
     *
     * O(α(N))
     *
     * -------------------------------------------------------------------------
     * Q. Edge cases?
     * -------------------------------------------------------------------------
     *
     * ✓ Empty input
     *
     * ✓ One account
     *
     * ✓ Duplicate names
     *
     * ✓ One email per account
     *
     * ✓ Entire input forms one component
     *
     * ✓ Completely disconnected accounts
     *
     * -------------------------------------------------------------------------
     * Q. Debugging checklist
     * -------------------------------------------------------------------------
     *
     * □ Did every repeated email call union()?
     *
     * □ Did grouping use find() instead of parent[] directly?
     *
     * □ Is email uniqueness preserved?
     *
     * □ Are emails sorted?
     *
     * □ Is account name taken from the representative?
     *
     * □ Are duplicate emails eliminated?
     *
     * -------------------------------------------------------------------------
     * Q. Pattern boundary
     * -------------------------------------------------------------------------
     *
     * Use DSU whenever:
     *
     *      Connectivity evolves while reading relationships.
     *
     * Avoid DSU whenever:
     *
     *      Graph traversal order matters.
     *
     * =========================================================================
     * ⚫ PATTERN MAPPING
     * =========================================================================
     *
     * Similar Problems
     * ----------------
     *
     * Number of Provinces
     *
     * Redundant Connection
     *
     * Graph Valid Tree
     *
     * Kruskal Minimum Spanning Tree
     *
     * Smallest String With Swaps
     *
     * Similar String Groups
     *
     * Satisfiability of Equality Equations
     *
     * Most Stones Removed
     *
     * =========================================================================
     * FORENSIC DEBUGGING GUIDE
     * =========================================================================
     *
     * Symptom
     * -------
     * Same person appears twice.
     *
     * Likely Cause
     * ------------
     * Grouping performed before find().
     *
     * -------------------------------------------------------------------------
     *
     * Symptom
     * -------
     * Emails duplicated.
     *
     * Likely Cause
     * ------------
     * HashSet/TreeSet not used.
     *
     * -------------------------------------------------------------------------
     *
     * Symptom
     * -------
     * Emails unsorted.
     *
     * Likely Cause
     * ------------
     * TreeSet omitted.
     *
     * -------------------------------------------------------------------------
     *
     * Symptom
     * -------
     * Wrong merges.
     *
     * Likely Cause
     * ------------
     * Compared account names instead of emails.
     *
     * -------------------------------------------------------------------------
     *
     * Symptom
     * -------
     * Time unexpectedly high.
     *
     * Likely Cause
     * ------------
     * Missing path compression or union-by-size.
     *
     * =========================================================================
     * IMPLEMENTATION RECONSTRUCTION
     * =========================================================================
     *
     * Remember only these nine mechanical steps:
     *
     * 1.
     * Create DSU.
     *
     * 2.
     * Create email -> firstAccount map.
     *
     * 3.
     * Scan every account.
     *
     * 4.
     * First occurrence?
     *
     *      store.
     *
     * 5.
     * Repeated occurrence?
     *
     *      union.
     *
     * 6.
     * Scan accounts again.
     *
     * 7.
     * Find representative.
     *
     * 8.
     * Insert emails into TreeSet.
     *
     * 9.
     * Convert buckets into answer.
     *
     * =========================================================================
     * COMPLEXITY ANALYSIS
     * =========================================================================
     *
     * Let
     *
     * A = number of accounts
     *
     * E = total number of unique emails
     *
     * T = total email occurrences
     *
     * Union-Find
     * ----------
     *
     * Every union/find:
     *
     * O(α(A))
     *
     * Total:
     *
     * O(T α(A))
     *
     * Grouping
     * --------
     *
     * O(T)
     *
     * Sorting
     * -------
     *
     * TreeSet insertion:
     *
     * O(log E)
     *
     * Overall
     * -------
     *
     * Time:
     *
     * O(T α(A) + T log E)
     *
     * Space:
     *
     * O(E + A)
     *
     * =========================================================================
     * COMMON INTERVIEW FOLLOW-UPS
     * =========================================================================
     *
     * Q.
     * Could DFS replace DSU?
     *
     * A.
     * Yes.
     *
     * Explicit graph construction is required first.
     *
     * -------------------------------------------------------------------------
     *
     * Q.
     * Why not merge immediately into one answer list?
     *
     * A.
     * The representative is not known until all unions finish.
     *
     * -------------------------------------------------------------------------
     *
     * Q.
     * Why TreeSet?
     *
     * A.
     *
     * It simultaneously guarantees:
     *
     * • uniqueness
     *
     * • lexicographic ordering
     *
     * -------------------------------------------------------------------------
     *
     * Q.
     * Is union-by-rank mandatory?
     *
     * A.
     *
     * Correctness:
     * No.
     *
     * Performance:
     * Strongly recommended.
     *
     * -------------------------------------------------------------------------
     *
     * Q.
     * Why is path compression safe?
     *
     * A.
     *
     * It changes only internal parent pointers.
     *
     * Connected components remain identical.
     *
     * Therefore correctness is preserved.
     *
     * =========================================================================
     * MEMORY PEG
     * =========================================================================
     *
     * Shared Email
     *      ↓
     * Union
     *      ↓
     * Representative
     *      ↓
     * Group
     *      ↓
     * Sort
     *      ↓
     * Answer
     */

    public static void main(String[] args) {
        OptimalSolution solver = new OptimalSolution();

        // ---------------------------------------------------------------------
        // Happy Path
        //
        // Standard example from the problem statement.
        // ---------------------------------------------------------------------
        List<List<String>> accounts1 = List.of(
                List.of("John", "johnsmith@mail.com", "john_newyork@mail.com"),
                List.of("John", "johnsmith@mail.com", "john00@mail.com"),
                List.of("Mary", "mary@mail.com"),
                List.of("John", "johnnybravo@mail.com")
        );

        List<List<String>> result1 = solver.accountsMerge(accounts1);

        // Three connected components should remain.
        assert result1.size() == 3;

        boolean foundMergedJohn = false;

        for (List<String> account : result1) {

            if (account.contains("john00@mail.com")) {

                foundMergedJohn = true;

                // Representative should contain all connected emails.
                assert account.size() == 4;

                assert account.get(0).equals("John");

                assert account.contains("johnsmith@mail.com");
                assert account.contains("john_newyork@mail.com");
                assert account.contains("john00@mail.com");
            }
        }

        assert foundMergedJohn;

        // ---------------------------------------------------------------------
        // Edge Case
        //
        // Single isolated account.
        // ---------------------------------------------------------------------
        List<List<String>> accounts2 = List.of(
                List.of("Alice", "alice@mail.com")
        );

        List<List<String>> result2 = solver.accountsMerge(accounts2);

        assert result2.size() == 1;
        assert result2.get(0).get(0).equals("Alice");
        assert result2.get(0).get(1).equals("alice@mail.com");

        // ---------------------------------------------------------------------
        // Duplicate names but different people.
        // ---------------------------------------------------------------------
        List<List<String>> accounts3 = List.of(
                List.of("Bob", "bob1@mail.com"),
                List.of("Bob", "bob2@mail.com")
        );

        List<List<String>> result3 = solver.accountsMerge(accounts3);

        // Must NOT merge based on name.
        assert result3.size() == 2;

        // ---------------------------------------------------------------------
        // Entire input becomes one connected component.
        // ---------------------------------------------------------------------
        List<List<String>> accounts4 = List.of(
                List.of("A", "1", "2"),
                List.of("A", "2", "3"),
                List.of("A", "3", "4"),
                List.of("A", "4", "5")
        );

        List<List<String>> result4 = solver.accountsMerge(accounts4);

        assert result4.size() == 1;

        List<String> merged = result4.get(0);

        assert merged.size() == 6;

        // ---------------------------------------------------------------------
        // No shared emails anywhere.
        // ---------------------------------------------------------------------
        List<List<String>> accounts5 = List.of(
                List.of("A", "a"),
                List.of("B", "b"),
                List.of("C", "c")
        );

        List<List<String>> result5 = solver.accountsMerge(accounts5);

        assert result5.size() == 3;

        // ---------------------------------------------------------------------
        // Transitive connectivity.
        //
        // A-B
        // B-C
        // therefore
        // A-C
        // ---------------------------------------------------------------------
        List<List<String>> accounts6 = List.of(
                List.of("User", "a", "b"),
                List.of("User", "b", "c"),
                List.of("User", "c", "d")
        );

        List<List<String>> result6 = solver.accountsMerge(accounts6);

        assert result6.size() == 1;

        List<String> chain = result6.get(0);

        assert chain.contains("a");
        assert chain.contains("b");
        assert chain.contains("c");
        assert chain.contains("d");

        // ---------------------------------------------------------------------
        // Lexicographic ordering verification.
        // ---------------------------------------------------------------------
        List<List<String>> accounts7 = List.of(
                List.of("Lex", "z@mail", "b@mail", "a@mail")
        );

        List<List<String>> result7 = solver.accountsMerge(accounts7);

        List<String> ordered = result7.get(0);

        assert ordered.get(1).equals("a@mail");
        assert ordered.get(2).equals("b@mail");
        assert ordered.get(3).equals("z@mail");

        // ---------------------------------------------------------------------
        // Duplicate email inside merged component.
        //
        // TreeSet should eliminate duplicates.
        // ---------------------------------------------------------------------
        List<List<String>> accounts8 = List.of(
                List.of("P", "x", "y"),
                List.of("P", "y", "z"),
                List.of("P", "x")
        );

        List<List<String>> result8 = solver.accountsMerge(accounts8);

        assert result8.size() == 1;

        List<String> deduplicated = result8.get(0);

        assert deduplicated.size() == 4;

        // ---------------------------------------------------------------------
        // Empty input.
        // ---------------------------------------------------------------------
        List<List<String>> result9 =
                solver.accountsMerge(Collections.emptyList());

        assert result9.isEmpty();

        // ---------------------------------------------------------------------
        // Verify alternative implementation produces equivalent component count.
        // ---------------------------------------------------------------------
        EmailCentricSolution alternative = new EmailCentricSolution();

        List<List<String>> alt =
                alternative.accountsMerge(accounts1);

        assert alt.size() == result1.size();

        System.out.println("All self-verifying assertions passed.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/