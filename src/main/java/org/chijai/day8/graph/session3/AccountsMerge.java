package org.chijai.day8.graph.session3;

import java.util.*;

/**
 * =============================================================================
 * AccountsMerge
 * JAVA GOLD — FINAL CANONICAL
 * =============================================================================
 *
 * PRIMARY GOAL OF THIS VERSION
 * ----------------------------
 * Learn Union Find once.
 * Reuse the same engine and the same visible solution skeleton.
 * For each related problem, store only the DELTA.
 *
 *      SAME BOILERPLATE
 *
 *          UnionFind unionFind = new UnionFind(numberOfNodes);
 *
 *          connectRelationships(..., unionFind);   // DELTA #1
 *
 *          return buildResult(..., unionFind);     // DELTA #2
 *
 * PHOTOGRAPHIC MEMORY RULE:
 * Keep two uninterrupted optimal snapshots immediately after the problem statement:
 *
 *      2A. SHORTEST / CRISPEST interview reconstruction
 *      2B. REUSABLE DSUNode version for learn-once / store-only-the-DELTA
 *
 * Put recognition, invariants, dry runs, trade-offs, and all long explanation AFTER.
 */
public class AccountsMerge {

    /**
     * =========================================================================
     * 1. PROBLEM STATEMENT
     * =========================================================================
     *
     * Each account is represented as:
     *
     *      [name, email1, email2, ...]
     *
     * Two accounts belong to the same person if they share at least one email.
     * The relation is transitive.
     *
     * Example:
     *
     *      Account 0 -> [a, b]
     *      Account 1 -> [b, c]
     *      Account 2 -> [c, d]
     *
     * Hidden graph:
     *
     *          b           c
     *      0 ------- 1 ------- 2
     *
     * Even though 0 and 2 share no email directly, all three accounts belong to
     * the same connected component.
     *
     * Return one merged account per person containing:
     *
     *      name
     *      all unique emails sorted lexicographically
     */


    /**
     * =========================================================================
     * 2A. SHORTEST / CRISPEST OPTIMAL SOLUTION — FAST INTERVIEW SNAPSHOT
     * =========================================================================
     *
     * Same DSU algorithm, minimum abstraction surface.
     * Use this when you want the whole answer in one quick photographic snapshot.
     * Detailed explanation still comes after BOTH code versions.
     */
    static class ShortestSolution {

        static class UnionFind {

            private final int[] parent;
            private final int[] size;

            UnionFind(int n) {
                parent = new int[n];
                size = new int[n];

                for (int node = 0; node < n; node++) {
                    parent[node] = node;
                    size[node] = 1;
                }
            }

            int find(int node) {
                if (parent[node] == node) {
                    return node;
                }

                parent[node] = find(parent[node]);
                return parent[node];
            }

            void union(int a, int b) {
                int rootA = find(a);
                int rootB = find(b);

                if (rootA == rootB) {
                    return;
                }

                if (size[rootA] < size[rootB]) {
                    int temp = rootA;
                    rootA = rootB;
                    rootB = temp;
                }

                parent[rootB] = rootA;
                size[rootA] += size[rootB];
            }
        }

        public List<List<String>> accountsMerge(List<List<String>> accounts) {

            if (accounts == null || accounts.isEmpty()) {
                return Collections.emptyList();
            }

            UnionFind unionFind = new UnionFind(accounts.size());
            Map<String, Integer> emailToFirstAccount = new HashMap<>();

            for (int accountIndex = 0; accountIndex < accounts.size(); accountIndex++) {
                List<String> account = accounts.get(accountIndex);

                for (int emailIndex = 1; emailIndex < account.size(); emailIndex++) {
                    String email = account.get(emailIndex);
                    Integer previousAccount = emailToFirstAccount.get(email);

                    if (previousAccount == null) {
                        emailToFirstAccount.put(email, accountIndex);
                    } else {
                        unionFind.union(previousAccount, accountIndex);
                    }
                }
            }

            Map<Integer, TreeSet<String>> rootToEmails = new HashMap<>();

            for (Map.Entry<String, Integer> entry : emailToFirstAccount.entrySet()) {
                int root = unionFind.find(entry.getValue());
                TreeSet<String> emails = rootToEmails.get(root);

                if (emails == null) {
                    emails = new TreeSet<>();
                    rootToEmails.put(root, emails);
                }

                emails.add(entry.getKey());
            }

            List<List<String>> answer = new ArrayList<>();

            for (Map.Entry<Integer, TreeSet<String>> entry : rootToEmails.entrySet()) {
                int root = entry.getKey();
                List<String> mergedAccount = new ArrayList<>();

                mergedAccount.add(accounts.get(root).get(0));
                mergedAccount.addAll(entry.getValue());
                answer.add(mergedAccount);
            }

            return answer;
        }
    }


    /**
     * =========================================================================
     * 2B. REUSABLE DSUNODE SOLUTION — ONE PHOTOGRAPHIC CODE SNAPSHOT
     * =========================================================================
     *
     * Keep the richer learn-once / reuse-many version together:
     *
     *      DSUNode
     *      UnionFind
     *      AccountEntry
     *      MergedAccount
     *      OptimalSolution
     *
     * Read / remember this entire block as ONE reusable solution image.
     * The shorter 2A version is for instant reconstruction; this 2B version
     * makes the reusable DSU structure and problem DELTAs explicit.
     */
    static class DSUNode {

        final int id;
        DSUNode parent;
        int size;

        DSUNode(int id) {
            this.id = id;
            this.parent = this;
            this.size = 1;
        }
    }

    static class UnionFind {

        private final DSUNode[] nodes;
        private int components;

        UnionFind(int n) {

            nodes = new DSUNode[n];
            components = n;

            for (int node = 0; node < n; node++) {
                nodes[node] = new DSUNode(node);
            }
        }

        DSUNode find(int node) {

            DSUNode current =
                    nodes[node];

            if (current.parent == current) {
                return current;
            }

            current.parent =
                    find(current.parent.id);

            return current.parent;
        }

        boolean union(int a, int b) {

            DSUNode rootA = find(a);
            DSUNode rootB = find(b);

            if (rootA == rootB) {
                return false;
            }

            if (rootA.size < rootB.size) {
                DSUNode temp = rootA;
                rootA = rootB;
                rootB = temp;
            }

            rootB.parent = rootA;
            rootA.size += rootB.size;

            components--;

            return true;
        }

        int components() {
            return components;
        }
    }


    record AccountEntry(
            String name,
            List<String> emails) {
    }

    static class MergedAccount {

        final String name;
        final TreeSet<String> emails = new TreeSet<>();

        MergedAccount(String name) {
            this.name = name;
        }

        void addEmails(List<String> newEmails) {
            emails.addAll(newEmails);
        }

        List<String> toList() {

            List<String> result = new ArrayList<>();
            result.add(name);
            result.addAll(emails);

            return result;
        }
    }


    static class OptimalSolution {

        public List<List<String>> accountsMerge(List<List<String>> accounts) {

            if (accounts == null || accounts.isEmpty()) {
                return Collections.emptyList();
            }

            List<AccountEntry> accountEntries =
                    toAccountEntries(accounts);

            UnionFind unionFind =
                    new UnionFind(accountEntries.size());

            connectAccountsBySharedEmail(
                    accountEntries,
                    unionFind);

            return buildMergedAccounts(
                    accountEntries,
                    unionFind);
        }

        private List<AccountEntry> toAccountEntries(
                List<List<String>> accounts) {

            List<AccountEntry> accountEntries =
                    new ArrayList<>();

            for (List<String> rawAccount : accounts) {

                String name =
                        rawAccount.get(0);

                List<String> emails =
                        rawAccount.subList(1, rawAccount.size());

                accountEntries.add(
                        new AccountEntry(name, emails));
            }

            return accountEntries;
        }

        private void connectAccountsBySharedEmail(
                List<AccountEntry> accountEntries,
                UnionFind unionFind) {

            Map<String, Integer> emailToFirstAccount =
                    new HashMap<>();

            for (int accountIndex = 0;
                 accountIndex < accountEntries.size();
                 accountIndex++) {

                AccountEntry accountEntry =
                        accountEntries.get(accountIndex);

                for (String email : accountEntry.emails()) {

                    Integer previousAccountIndex =
                            emailToFirstAccount.get(email);

                    if (previousAccountIndex == null) {

                        emailToFirstAccount.put(
                                email,
                                accountIndex);

                    } else {

                        unionFind.union(
                                previousAccountIndex,
                                accountIndex);
                    }
                }
            }
        }

        private List<List<String>> buildMergedAccounts(
                List<AccountEntry> accountEntries,
                UnionFind unionFind) {

            Map<Integer, MergedAccount> rootToMergedAccount =
                    new HashMap<>();

            for (int accountIndex = 0;
                 accountIndex < accountEntries.size();
                 accountIndex++) {

                AccountEntry accountEntry =
                        accountEntries.get(accountIndex);

                int rootId =
                        unionFind.find(accountIndex).id;

                MergedAccount mergedAccount =
                        rootToMergedAccount.get(rootId);

                if (mergedAccount == null) {

                    mergedAccount =
                            new MergedAccount(accountEntry.name());

                    rootToMergedAccount.put(
                            rootId,
                            mergedAccount);
                }

                mergedAccount.addEmails(
                        accountEntry.emails());
            }

            List<List<String>> answer =
                    new ArrayList<>();

            for (MergedAccount mergedAccount :
                    rootToMergedAccount.values()) {

                answer.add(
                        mergedAccount.toList());
            }

            return answer;
        }
    }


    /**
     * =========================================================================
     * 3. RECOGNITION + FIRST-PRINCIPLES INVENTION PATH
     * =========================================================================
     *
     * account       = node
     * shared email  = relationship / edge
     * person        = connected component
     *
     * We do not need shortest paths or traversal order.
     * We only need to know which component each account belongs to.
     *
     * Therefore:
     *
     *      repeated email
     *          -> union(previousAccount, currentAccount)
     *
     *      after all relationships are processed
     *          -> find(accountIndex)
     *          -> group by root
     *
     * Re-derivation cue:
     *
     *      shared identifier -> union -> root -> group -> answer
     */


    /**
     * =========================================================================
     * 4. UNION FIND EXPLANATION
     * =========================================================================
     *
     * CANONICAL STRUCTURAL PRIMITIVE
     * ------------------------------
     *
     *      Linked List -> ListNode
     *      Binary Tree -> TreeNode
     *      Trie        -> TrieNode
     *      Union Find  -> DSUNode
     *
     * A DSU is a forest. Every node points upward to a parent, and every
     * component has one representative root whose parent is itself.
     *
     *      node.parent -> parent / representative link
     *      node.size   -> component size, meaningful at the root
     *
     * WHY DSUNode IS A CLASS, NOT A RECORD
     * ------------------------------------
     *
     * DSUNode is mutable algorithm state:
     *
     *      find()  changes parent during path compression
     *      union() changes parent and size
     *
     * A record is a better fit for value-like input data such as AccountEntry.
     *
     * CORE REPRESENTATION INVARIANT
     * -----------------------------
     *
     *      nodes[i] is the one canonical DSUNode for logical node i
     *      nodes[i].id == i
     *
     * Every parent reference points to one of these canonical nodes.
     * Therefore recursive find can move from the parent object back to the
     * integer-indexed DSU with:
     *
     *      find(current.parent.id)
     *
     * DENSE-ID ASSUMPTION
     * -------------------
     *
     * Canonical UnionFind assumes ids:
     *
     *      0 .. n - 1
     *
     * If the domain starts with arbitrary labels such as emails or strings,
     * map those labels to dense integer ids outside UnionFind.
     *
     * ONE CANONICAL find()
     * --------------------
     *
     *      DSUNode find(int node)
     *
     * There is no second findRoot(), find(DSUNode), or root() method.
     *
     *      input  = integer node id
     *      output = representative DSUNode
     *
     * If business logic needs a grouping key:
     *
     *      int rootId = unionFind.find(node).id;
     *
     * If it only needs to know whether two nodes share a component:
     *
     *      unionFind.find(a) == unionFind.find(b)
     *
     * This identity comparison is intentional because find() returns the one
     * canonical representative object for the component.
     *
     * RECURSIVE FIND + PATH COMPRESSION
     * ---------------------------------
     *
     *      if I am my own parent
     *          -> I am the root
     *
     *      otherwise
     *          -> find my parent's root
     *          -> make that root my direct parent
     *          -> return it
     *
     *      before                 after find(x)
     *
     *         A                       A
     *         |                    /  |  \
     *         B                   B   C   x
     *         |
     *         C
     *         |
     *         x
     *
     * UNION BY SIZE
     * -------------
     *
     * Keep rootA as the larger component. If it is smaller, swap the roots.
     * Then one attachment line always means:
     *
     *      smaller rootB -> larger rootA
     *
     *      rootB.parent = rootA;
     *      rootA.size += rootB.size;
     *
     * Memory peg:
     *
     *      union by size    -> avoid creating tall trees
     *      path compression -> flatten trees during find()
     *
     * union() returns false when both nodes already have the same root.
     * That becomes the answer directly in problems such as Redundant Connection.
     *
     * components starts at n and decreases after every successful merge:
     *
     *      {A} + {B} -> {A,B}
     *      components--
     *
     * COMPACT ARRAY ENCODING
     * ----------------------
     *
     * The equally standard implementation is:
     *
     *      node.parent <=> parent[node]
     *      node.size   <=> size[node]
     *
     * int[] parent + int[] size is more compact and avoids object allocation.
     * The algorithm and invariants are identical. This Gold version keeps the
     * explicit DSUNode because the forest structure is easier to retain visually.
     */


    /**
     * 5. DOMAIN MODEL NOTES
     * ------------------
     *
     * LeetCode gives:
     *
     *      [name, email1, email2, ...]
     *
     * AccountEntry removes that positional knowledge from the algorithm:
     *
     *      accountEntry.name()
     *      accountEntry.emails()
     *
     * AccountEntry is a semantic input record.
     *
     * MergedAccount is mutable because it accumulates all unique sorted emails
     * belonging to one connected component. It avoids exposing plumbing such as:
     *
     *      Map.Entry<Integer, TreeSet<String>>
     *
     * throughout the primary algorithm.
     */


    /**
     * =========================================================================
     * 6. PRIMARY SOLUTION EXPLANATION
     * =========================================================================
     *
     * REUSABLE DSU SKELETON
     * ---------------------
     *
     *      create UnionFind
     *      connectRelationships(...)       <- DELTA #1
     *      buildResultFromComponents(...)  <- DELTA #2
     *
     * The public method stays tiny so this skeleton is visible immediately.
     *
     * BOUNDARY CONVERSION
     * -------------------
     *
     *      raw [name, email1, email2, ...]
     *          -> AccountEntry(name, emails)
     *
     * After this conversion, the algorithm no longer remembers get(0) vs get(1..).
     *
     * DELTA #1 — WHAT CREATES A RELATIONSHIP?
     * ---------------------------------------
     *
     *      same email
     *
     * The first owner of an email is stored in emailToFirstAccount.
     * Seeing the same email again means:
     *
     *      union(previousAccountIndex, accountIndex)
     *
     * DELTA #2 — WHAT DOES ONE COMPONENT MEAN?
     * -----------------------------------------
     *
     *      one MergedAccount
     *      = one name + all unique sorted emails
     *
     * VISUAL MEANING OF ROOT
     * ----------------------
     *
     * Suppose shared emails create:
     *
     *      0 ------- 1 ------- 2
     *
     *      3
     *
     * DSU may produce:
     *
     *      find(0).id = 0
     *      find(1).id = 0
     *      find(2).id = 0
     *      find(3).id = 3
     *
     * Keep these separate:
     *
     *      root node = DSUNode representative returned by find()
     *      root id   = find(node).id, used as an integer grouping key
     *
     *      0 --\
     *      1 ----> root id 0 -> one MergedAccount
     *      2 --/
     *
     *      3 ------> root id 3 -> another MergedAccount
     *
     * The representative is not a special business account. Its id is merely
     * the stable key for the component bucket.
     */

    /**
     * =========================================================================
     * 7. TRANSFER MAP — LEARN THE ENGINE ONCE, STORE ONLY THE DELTA
     * =========================================================================
     *
     * STABLE DSU ENGINE:
     *
     *      UnionFind(numberOfNodes)
     *      find(node)
     *      union(a, b)
     *      components()
     *
     * Problem                 Relationship DELTA        Result DELTA
     * -------------------------------------------------------------------------
     * Accounts Merge          shared email              root id -> emails
     * Redundant Connection    explicit edge             failed union -> edge
     * Equality Equations      a == b                    a != b + same root -> false
     * Network Connected       cable edge                components - 1
     * Smallest String Swaps   allowed index pair        root id -> characters
     * Graph Valid Tree        explicit edge             failed union -> cycle
     *
     * CROSSOVER PROBLEMS:
     *
     *      Number of Islands   -> DFS/BFS is simpler for the static grid
     *      Number of Provinces -> DFS/BFS is simpler for the static matrix
     *
     * DSU is still a valid transfer solution for both, but do not force the
     * engine when direct traversal is easier to reconstruct.
     *
     * MEMORY RULE:
     *
     *      SAME = DSU engine
     *      DELTA #1 = what creates a relationship?
     *      DELTA #2 = what does the resulting component mean?
     */

    /**
     * =========================================================================
     * 8. PRIMARY DRY RUN
     * =========================================================================
     *
     * Input:
     *
     *      0 -> [a, b]
     *      1 -> [b, c]
     *      2 -> [c, d]
     *
     * emailToFirstAccount:
     *
     *      a -> 0
     *      b -> 0
     *
     * Account 1 sees b again:
     *
     *      union(0, 1)
     *
     * Account 1 introduces c:
     *
     *      c -> 1
     *
     * Account 2 sees c again:
     *
     *      union(1, 2)
     *
     * Final:
     *
     *      find(0) == find(1) == find(2)
     *
     * Their returned DSUNode representative is the same object.
     * Its id is the grouping key used to build one MergedAccount.
     */

    /**
     * =========================================================================
     * 9. COMPLEXITY — DERIVED
     * =========================================================================
     *
     * Let:
     *
     *      A = number of accounts
     *      T = total email occurrences
     *      E = number of unique emails
     *
     * Connectivity pass:
     * Every email occurrence is processed once and repeated emails trigger union.
     *
     *      O(T * alpha(A))
     *
     * Grouping pass:
     * Every email occurrence is inserted into a TreeSet.
     *
     *      O(T log E)
     *
     * Overall:
     *
     *      Time  = O(T * alpha(A) + T log E)
     *      Space = O(A + E)
     */

    /**
     * =========================================================================
     * 10. GRAPH / DFS ALTERNATIVE — PATTERN BOUNDARY
     * =========================================================================
     *
     * DFS can also solve Accounts Merge after an explicit graph is built.
     *
     *      DFS -> explore an existing component
     *      DSU -> merge/maintain component membership as relationships appear
     *
     * Visual component:
     *
     *          b           c
     *      0 ------- 1 ------- 2
     *
     * DFS physically walks 0 -> 1 -> 2.
     * DSU discovers b and c and performs union(0,1), union(1,2).
     */
    static class GraphAlternative {

        public List<List<String>> accountsMerge(List<List<String>> accounts) {

            if (accounts == null || accounts.isEmpty()) {
                return Collections.emptyList();
            }

            Map<String, List<String>> graph = new HashMap<>();
            Map<String, String> emailToName = new HashMap<>();

            for (List<String> rawAccount : accounts) {

                String name = rawAccount.get(0);
                String firstEmail = rawAccount.get(1);

                graph.putIfAbsent(firstEmail, new ArrayList<>());

                for (int emailIndex = 1; emailIndex < rawAccount.size(); emailIndex++) {

                    String email = rawAccount.get(emailIndex);
                    emailToName.put(email, name);
                    graph.putIfAbsent(email, new ArrayList<>());

                    if (!email.equals(firstEmail)) {
                        graph.get(firstEmail).add(email);
                        graph.get(email).add(firstEmail);
                    }
                }
            }

            Set<String> visited = new HashSet<>();
            List<List<String>> answer = new ArrayList<>();

            for (String email : graph.keySet()) {

                if (visited.contains(email)) {
                    continue;
                }

                List<String> componentEmails = new ArrayList<>();
                dfs(email, graph, visited, componentEmails);
                Collections.sort(componentEmails);

                List<String> mergedAccount = new ArrayList<>();
                mergedAccount.add(emailToName.get(email));
                mergedAccount.addAll(componentEmails);
                answer.add(mergedAccount);
            }

            return answer;
        }

        private void dfs(
                String email,
                Map<String, List<String>> graph,
                Set<String> visited,
                List<String> componentEmails) {

            visited.add(email);
            componentEmails.add(email);

            for (String neighbor : graph.get(email)) {
                if (!visited.contains(neighbor)) {
                    dfs(neighbor, graph, visited, componentEmails);
                }
            }
        }
    }

    /**
     * =========================================================================
     * 11. GRAPH / DSU FAMILY — SAME FOUNDATION, STORE ONLY THE DELTA
     * =========================================================================
     *
     * IMPORTANT:
     *
     *      Do NOT force DSU onto every connected-components problem.
     *
     * Reuse happens at two levels:
     *
     *      LEVEL 1 — Graph foundation
     *          node
     *          relationship / neighbor
     *          connected component
     *
     *      LEVEL 2 — Algorithm engine
     *          DFS/BFS traversal OR Union Find
     *
     * If two problems genuinely use the same engine, reuse the boilerplate.
     * If the natural engine changes, keep the mapping but store the delta.
     */

    /**
     * -------------------------------------------------------------------------
     * CROSSOVER 1 — NUMBER OF ISLANDS
     * -------------------------------------------------------------------------
     *
     * PRIMARY:
     *      DFS/BFS Connected Components on a Grid
     *
     * IMPORTANT TERMINOLOGY:
     *
     *      "Flood fill" is NOT a separate algorithm to memorize.
     *
     *      It is simply the common name for DFS/BFS when traversal spreads
     *      through neighboring cells in a grid/image.
     *
     * SAME BASE TEMPLATE AS NUMBER OF PROVINCES:
     *
     *      for every node:
     *          if unvisited:
     *              components++
     *              dfs(node)
     *
     * ONLY THE NEIGHBOR GENERATOR CHANGES:
     *
     *      Number of Islands
     *          node      = grid cell
     *          neighbors = up / down / left / right LAND cells
     *
     *      Number of Provinces
     *          node      = city
     *          neighbors = every city j where isConnected[city][j] == 1
     *
     * DFS COMPLEXITY:
     *
     *      Time  = O(R * C)
     *
     *      There are R*C cells and every cell has at most four neighbors.
     *
     *      Space = O(R * C) worst-case recursion stack
     *
     *      If we mutate land '1' -> water '0', the grid itself stores visited
     *      state, so no separate visited[][] is required.
     *
     * DSU ALTERNATIVE:
     *
     *      Time  = O(R * C * alpha(R*C))
     *      Space = O(R * C)
     *
     * TRADE-OFF:
     *
     *      DFS/BFS
     *          + simplest reconstruction for the static grid
     *          + truly linear traversal
     *          + same connected-components template as Provinces
     *          + no Union Find / DSU machinery
     *
     *      DSU
     *          + useful if land/connectivity changes dynamically
     *          + reusable for Number of Islands II
     *          - more machinery for the static problem
     *
     * GOLD CHOICE:
     *
     *      Static Number of Islands -> DFS/BFS primary.
     *      Dynamic Islands          -> DSU becomes much more attractive.
     */

    static class NumberOfIslandsDFS {

        int numIslands(char[][] grid) {

            int islands = 0;

            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {

                    if (grid[row][col] == '1') {
                        islands++;
                        dfs(grid, row, col);
                    }
                }
            }

            return islands;
        }

        private void dfs(
                char[][] grid,
                int row,
                int col) {

            if (row < 0 || row >= grid.length ||
                col < 0 || col >= grid[0].length ||
                grid[row][col] != '1') {

                return;
            }

            grid[row][col] = '0';

            dfs(grid, row - 1, col);
            dfs(grid, row + 1, col);
            dfs(grid, row, col - 1);
            dfs(grid, row, col + 1);
        }
    }

    static class NumberOfIslandsDSU {

        int numIslands(char[][] grid) {

            int rows = grid.length;
            int cols = grid[0].length;

            UnionFind unionFind = new UnionFind(rows * cols);

            int landComponents = 0;

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {

                    if (grid[row][col] != '1') {
                        continue;
                    }

                    landComponents++;

                    int current = row * cols + col;

                    /*
                     * Only connect UP and LEFT.
                     *
                     * DOWN and RIGHT will be handled when those cells become
                     * the current cell, so this avoids duplicate work.
                     */
                    if (row > 0 && grid[row - 1][col] == '1') {

                        int up = (row - 1) * cols + col;

                        if (unionFind.union(current, up)) {
                            landComponents--;
                        }
                    }

                    if (col > 0 && grid[row][col - 1] == '1') {

                        int left = row * cols + (col - 1);

                        if (unionFind.union(current, left)) {
                            landComponents--;
                        }
                    }
                }
            }

            return landComponents;
        }
    }

    /**
     * -------------------------------------------------------------------------
     * CROSSOVER 2 — NUMBER OF PROVINCES
     * -------------------------------------------------------------------------
     *
     * PRIMARY:
     *      DFS/BFS Connected Components
     *
     * WHY PRIMARY?
     *      The adjacency matrix already exposes every city's neighbors.
     *      The question is directly:
     *
     *          "How many connected components exist?"
     *
     * DFS:
     *
     *      Time  = O(N^2)
     *
     *      Why?
     *      In the worst case we scan one full matrix row for every city.
     *
     *      Space = O(N)
     *
     *      visited[] + at most O(N) recursion depth.
     *
     * DSU:
     *
     *      Time  = O(N^2 * alpha(N))
     *              approximately O(N^2) in practice
     *
     *      Space = O(N)
     *
     * TRADE-OFF:
     *
     *      DFS/BFS
     *          + simpler
     *          + directly matches "consume one component"
     *
     *      DSU
     *          + same reusable union/find engine
     *          + useful if connectivity relationships arrive incrementally
     *          - slightly more machinery for this static matrix
     *
     * GOLD CHOICE:
     *
     *      DFS/BFS primary.
     *      DSU retained as the transfer solution.
     */

    static class NumberOfProvincesDFS {

        int findCircleNum(int[][] isConnected) {

            boolean[] visited = new boolean[isConnected.length];
            int provinces = 0;

            for (int city = 0; city < isConnected.length; city++) {

                if (!visited[city]) {
                    provinces++;
                    dfs(city, isConnected, visited);
                }
            }

            return provinces;
        }

        private void dfs(
                int city,
                int[][] isConnected,
                boolean[] visited) {

            visited[city] = true;

            for (int otherCity = 0;
                 otherCity < isConnected.length;
                 otherCity++) {

                if (isConnected[city][otherCity] == 1 &&
                    !visited[otherCity]) {

                    dfs(otherCity, isConnected, visited);
                }
            }
        }
    }

    static class NumberOfProvincesDSU {

        int findCircleNum(int[][] isConnected) {

            UnionFind unionFind = new UnionFind(isConnected.length);

            connectCities(isConnected, unionFind);

            return unionFind.components();
        }

        private void connectCities(
                int[][] isConnected,
                UnionFind unionFind) {

            for (int city = 0; city < isConnected.length; city++) {
                for (int otherCity = city + 1;
                     otherCity < isConnected.length;
                     otherCity++) {

                    if (isConnected[city][otherCity] == 1) {
                        unionFind.union(city, otherCity);
                    }
                }
            }
        }
    }

    /**
     * -------------------------------------------------------------------------
     * PROBLEM 2 — REDUNDANT CONNECTION
     * -------------------------------------------------------------------------
     *
     * SAME:
     *      create DSU
     *      process relationships
     *
     * DELTA:
     *      relationship = explicit edge [a,b]
     *      result condition = union(a,b) returns false
     *
     * Why false?
     * The endpoints were already in the same component, so this edge closes a
     * cycle and is redundant.
     */
    static class RedundantConnection {

        int[] findRedundantConnection(int[][] edges) {

            UnionFind unionFind = new UnionFind(edges.length + 1);

            return findFirstEdgeThatCannotMerge(edges, unionFind);
        }

        private int[] findFirstEdgeThatCannotMerge(
                int[][] edges,
                UnionFind unionFind) {

            for (int[] edge : edges) {

                int nodeA = edge[0];
                int nodeB = edge[1];

                if (!unionFind.union(nodeA, nodeB)) {
                    return edge;
                }
            }

            return new int[0];
        }
    }

    /**
     * -------------------------------------------------------------------------
     * PROBLEM 3 — SATISFIABILITY OF EQUALITY EQUATIONS
     * -------------------------------------------------------------------------
     *
     * SAME:
     *      create DSU
     *      connect relationships
     *      inspect roots
     *
     * DELTA #1:
     *      relationship = a == b
     *
     * DELTA #2:
     *      contradiction = a != b but find(a) == find(b)
     *
     * find() returns the canonical DSUNode representative, so == here compares
     * representative identity intentionally.
     */
    static class EqualityEquations {

        boolean equationsPossible(String[] equations) {

            UnionFind unionFind = new UnionFind(26);

            connectEqualVariables(equations, unionFind);

            return noInequalityContradiction(equations, unionFind);
        }

        private void connectEqualVariables(
                String[] equations,
                UnionFind unionFind) {

            for (String equation : equations) {

                if (equation.charAt(1) == '=') {
                    int variableA = equation.charAt(0) - 'a';
                    int variableB = equation.charAt(3) - 'a';

                    unionFind.union(variableA, variableB);
                }
            }
        }

        private boolean noInequalityContradiction(
                String[] equations,
                UnionFind unionFind) {

            for (String equation : equations) {

                if (equation.charAt(1) == '!') {
                    int variableA = equation.charAt(0) - 'a';
                    int variableB = equation.charAt(3) - 'a';

                    if (unionFind.find(variableA) == unionFind.find(variableB)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    /**
     * -------------------------------------------------------------------------
     * PROBLEM 4 — NUMBER OF OPERATIONS TO MAKE NETWORK CONNECTED
     * -------------------------------------------------------------------------
     *
     * SAME:
     *      create DSU
     *      union explicit relationships
     *      use final component count
     *
     * DELTA #1:
     *      relationship = cable [computerA, computerB]
     *
     * DELTA #2:
     *      if fewer than n - 1 cables exist, impossible
     *      otherwise answer = components - 1
     *
     * DSU:
     *      Time  = O(E * alpha(N))
     *      Space = O(N)
     *
     * DFS/BFS alternative:
     *      Time  = O(N + E)
     *      Space = O(N + E) because an adjacency list must be built
     *
     * GOLD CHOICE:
     *      DSU is a very strong primary here because the input is already an
     *      edge list and the final answer is directly component-count based.
     */
    static class NetworkConnected {

        int makeConnected(int n, int[][] connections) {

            if (connections.length < n - 1) {
                return -1;
            }

            UnionFind unionFind = new UnionFind(n);

            connectComputers(connections, unionFind);

            return unionFind.components() - 1;
        }

        private void connectComputers(
                int[][] connections,
                UnionFind unionFind) {

            for (int[] connection : connections) {
                unionFind.union(connection[0], connection[1]);
            }
        }
    }

    /**
     * -------------------------------------------------------------------------
     * PROBLEM 5 — SMALLEST STRING WITH SWAPS
     * -------------------------------------------------------------------------
     *
     * SAME:
     *      create DSU
     *      union related nodes
     *      group by root
     *
     * DELTA #1:
     *      relationship = allowed swap pair [indexA, indexB]
     *
     * DELTA #2:
     *      one component means its characters can be rearranged among all indices
     *      in that component; assign the smallest available characters first.
     */
    static class SmallestStringWithSwaps {

        String smallestStringWithSwaps(
                String s,
                List<List<Integer>> pairs) {

            UnionFind unionFind = new UnionFind(s.length());

            connectSwapPairs(pairs, unionFind);

            return buildSmallestString(s, unionFind);
        }

        private void connectSwapPairs(
                List<List<Integer>> pairs,
                UnionFind unionFind) {

            for (List<Integer> pair : pairs) {
                int indexA = pair.get(0);
                int indexB = pair.get(1);

                unionFind.union(indexA, indexB);
            }
        }

        private String buildSmallestString(
                String s,
                UnionFind unionFind) {

            Map<Integer, PriorityQueue<Character>> rootToCharacters =
                    new HashMap<>();

            for (int index = 0; index < s.length(); index++) {

                int rootId = unionFind.find(index).id;
                PriorityQueue<Character> characters = rootToCharacters.get(rootId);

                if (characters == null) {
                    characters = new PriorityQueue<>();
                    rootToCharacters.put(rootId, characters);
                }

                characters.offer(s.charAt(index));
            }

            StringBuilder answer = new StringBuilder();

            for (int index = 0; index < s.length(); index++) {
                int rootId = unionFind.find(index).id;
                answer.append(rootToCharacters.get(rootId).poll());
            }

            return answer.toString();
        }
    }

    /**
     * -------------------------------------------------------------------------
     * CROSSOVER 3 — GRAPH VALID TREE
     * -------------------------------------------------------------------------
     *
     * Both DFS and DSU are strong solutions here.
     *
     * A valid tree requires:
     *
     *      1. exactly n - 1 edges
     *      2. no cycle
     *
     * DFS:
     *
     *      Time  = O(N + E)
     *      Space = O(N + E)
     *
     *      We build an adjacency list, then traverse while tracking the parent.
     *
     * DSU:
     *
     *      Time  = O(E * alpha(N))
     *      Space = O(N)
     *
     *      No adjacency list is required because the input already gives edges.
     *
     * TRADE-OFF:
     *
     *      DFS
     *          + directly expresses graph traversal and cycle detection
     *          - must build adjacency list
     *
     *      DSU
     *          + extremely compact for edge-list input
     *          + lower extra space here
     *          + failed union directly means cycle
     *
     * GOLD CHOICE:
     *
     *      Both are interview-primary quality.
     *      DSU is arguably cleaner when the input is already an edge list.
     */

    static class GraphValidTreeDFS {

        boolean validTree(int n, int[][] edges) {

            if (edges.length != n - 1) {
                return false;
            }

            List<List<Integer>> graph = new ArrayList<>();

            for (int node = 0; node < n; node++) {
                graph.add(new ArrayList<>());
            }

            for (int[] edge : edges) {
                graph.get(edge[0]).add(edge[1]);
                graph.get(edge[1]).add(edge[0]);
            }

            boolean[] visited = new boolean[n];

            if (hasCycle(0, -1, graph, visited)) {
                return false;
            }

            for (boolean nodeVisited : visited) {
                if (!nodeVisited) {
                    return false;
                }
            }

            return true;
        }

        private boolean hasCycle(
                int node,
                int parent,
                List<List<Integer>> graph,
                boolean[] visited) {

            visited[node] = true;

            for (int neighbor : graph.get(node)) {

                if (neighbor == parent) {
                    continue;
                }

                if (visited[neighbor]) {
                    return true;
                }

                if (hasCycle(neighbor, node, graph, visited)) {
                    return true;
                }
            }

            return false;
        }
    }

    static class GraphValidTreeDSU {

        boolean validTree(int n, int[][] edges) {

            if (edges.length != n - 1) {
                return false;
            }

            UnionFind unionFind = new UnionFind(n);

            for (int[] edge : edges) {

                if (!unionFind.union(edge[0], edge[1])) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * =========================================================================
     * 12. GRAPH FAMILY BOUNDARY — DO NOT FORCE DSU EVERYWHERE
     * =========================================================================
     *
     * Number of Islands
     *      base algorithm = DFS/BFS connected components
     *      graph representation = grid
     *      delta = neighbors come from up/down/left/right LAND cells
     *      "flood fill" = only a nickname for this grid DFS/BFS
     *
     * Number of Provinces
     *      base algorithm = DFS/BFS connected components
     *      graph representation = adjacency matrix
     *      delta = neighbors come from isConnected[city][otherCity] == 1
     *      DSU is also clean, but traversal is the simpler primary
     *
     * Accounts Merge
     *      base idea = connected components
     *      delta = relationships discovered through shared identifiers
     *      primary = DSU
     *
     * Pacific Atlantic
     *      NOT component grouping
     *      base machinery = DFS/BFS
     *      delta = reverse reachability from two source sets + intersection
     *
     * Bipartite
     *      NOT component grouping
     *      base machinery = DFS/BFS
     *      delta = visited becomes RED/BLUE coloring + conflict checking
     *
     * SHARED DFS TEMPLATE:
     *
     *      Number of Islands
     *      Number of Provinces
     *
     *      both reduce to:
     *
     *          for every node:
     *              if unvisited:
     *                  components++
     *                  dfs(node)
     *
     *      Only neighbor(node) changes.
     *
     * PACIFIC ATLANTIC IS DIFFERENT:
     *
     *      We are NOT consuming disjoint components.
     *      We are asking which cells are REACHABLE from two source sets.
     *
     *      Pacific borders  -> reverse DFS/BFS uphill -> pacificReachable
     *      Atlantic borders -> reverse DFS/BFS uphill -> atlanticReachable
     *
     *      answer = intersection of the two reachable sets
     *
     * Fast recognition:
     *
     *      "How many groups/components?"
     *          -> connected-components DFS/BFS or DSU
     *
     *      "Can this node reach destination X?"
     *          -> reachability
     *
     *      "Can this node reach A AND B?"
     *          -> two reachability sets + intersection
     */

    /**
     * =========================================================================
     * 13. INTERVIEW RECALL SHEET
     * =========================================================================
     *
     * BASE DSU TEMPLATE
     * -----------------
     *
     *      UnionFind unionFind = new UnionFind(numberOfNodes);
     *
     *      // DELTA #1
     *      discover relationship (a, b)
     *      unionFind.union(a, b);
     *
     *      // DELTA #2
     *      use find()/components()/union-return-value according to the problem.
     *
     * CLEAN-DESIGN LESSON WITHOUT LLD OVERHEAD
     * ----------------------------------------
     *
     *      AccountEntry   = semantic input record
     *      MergedAccount = mutable component accumulator
     *      UnionFind     = stable reusable engine
     *
     *      connectAccountsBySharedEmail() = DELTA #1
     *      buildMergedAccounts()           = DELTA #2
     *
     * Stable core + isolated delta resembles Open/Closed thinking:
     * reuse the engine unchanged and extend only problem-specific behavior.
     * No interfaces/factories/frameworks are needed for a DSA interview.
     *
     * Accounts Merge memory peg:
     *
     *      shared email
     *          -> union owners
     *          -> root
     *          -> MergedAccount
     *          -> sorted emails
     *
     * Union Find memory peg:
     *
     *      find      = representative DSUNode
     *      find().id = integer component key when business logic needs one
     *      union     = merge components
     *      false     = already same component
     *      components= how many groups remain?
     */

    /**
     * =========================================================================
     * 14. MAIN + SELF-VERIFYING TESTS
     * =========================================================================
     */
    public static void main(String[] args) {

        // ---------------------------------------------------------------------
        // Union Find primitive — verify the reusable engine itself
        // ---------------------------------------------------------------------
        UnionFind unionFind = new UnionFind(4);

        assert unionFind.components() == 4;
        assert unionFind.union(0, 1);
        assert unionFind.union(1, 2);
        assert !unionFind.union(0, 2);
        assert unionFind.find(0) == unionFind.find(2);
        assert unionFind.find(0).id == unionFind.find(2).id;
        assert unionFind.components() == 2;

        // ---------------------------------------------------------------------
        // Accounts Merge
        // ---------------------------------------------------------------------
        ShortestSolution shortestAccountsSolver = new ShortestSolution();
        OptimalSolution accountsSolver = new OptimalSolution();

        List<List<String>> accounts = List.of(
                List.of("John", "johnsmith@mail.com", "john_newyork@mail.com"),
                List.of("John", "johnsmith@mail.com", "john00@mail.com"),
                List.of("Mary", "mary@mail.com"),
                List.of("John", "johnnybravo@mail.com")
        );

        List<List<String>> shortestMergedAccounts =
                shortestAccountsSolver.accountsMerge(accounts);

        List<List<String>> mergedAccounts =
                accountsSolver.accountsMerge(accounts);

        assert shortestMergedAccounts.size() == 3;
        assert mergedAccounts.size() == 3;

        List<String> shortestMergedJohn = findAccountContaining(
                shortestMergedAccounts,
                "john00@mail.com");

        assert shortestMergedJohn != null;
        assert shortestMergedJohn.equals(List.of(
                "John",
                "john00@mail.com",
                "john_newyork@mail.com",
                "johnsmith@mail.com"
        ));

        List<String> mergedJohn = findAccountContaining(
                mergedAccounts,
                "john00@mail.com");

        assert mergedJohn != null;
        assert mergedJohn.equals(List.of(
                "John",
                "john00@mail.com",
                "john_newyork@mail.com",
                "johnsmith@mail.com"
        ));

        // Transitive connectivity.
        List<List<String>> transitiveAccounts = List.of(
                List.of("User", "a", "b"),
                List.of("User", "b", "c"),
                List.of("User", "c", "d")
        );

        assert shortestAccountsSolver.accountsMerge(transitiveAccounts).equals(
                List.of(List.of("User", "a", "b", "c", "d"))
        );

        assert accountsSolver.accountsMerge(transitiveAccounts).equals(
                List.of(List.of("User", "a", "b", "c", "d"))
        );

        // ---------------------------------------------------------------------
        // Number of Islands — primary DFS connected components vs DSU alternative
        // ---------------------------------------------------------------------
        NumberOfIslandsDFS islandsDFS =
                new NumberOfIslandsDFS();

        NumberOfIslandsDSU islandsDSU =
                new NumberOfIslandsDSU();

        char[][] islandGrid1 = {
                {'1', '1', '0', '0'},
                {'1', '0', '0', '1'},
                {'0', '0', '1', '1'}
        };

        char[][] islandGrid2 = {
                {'1', '1', '0', '0'},
                {'1', '0', '0', '1'},
                {'0', '0', '1', '1'}
        };

        assert islandsDFS.numIslands(islandGrid1) == 2;
        assert islandsDSU.numIslands(islandGrid2) == 2;

        // ---------------------------------------------------------------------
        // Number of Provinces — primary DFS vs DSU transfer solution
        // ---------------------------------------------------------------------
        NumberOfProvincesDFS provincesDFS =
                new NumberOfProvincesDFS();

        NumberOfProvincesDSU provincesDSU =
                new NumberOfProvincesDSU();

        int[][] provinceGraph = {
                {1, 1, 0},
                {1, 1, 0},
                {0, 0, 1}
        };

        assert provincesDFS.findCircleNum(provinceGraph) == 2;
        assert provincesDSU.findCircleNum(provinceGraph) == 2;

        // ---------------------------------------------------------------------
        // Redundant Connection
        // ---------------------------------------------------------------------
        RedundantConnection redundantSolver = new RedundantConnection();

        assert Arrays.equals(
                redundantSolver.findRedundantConnection(new int[][]{
                        {1, 2},
                        {1, 3},
                        {2, 3}
                }),
                new int[]{2, 3}
        );

        // ---------------------------------------------------------------------
        // Equality Equations
        // ---------------------------------------------------------------------
        EqualityEquations equationsSolver = new EqualityEquations();

        assert equationsSolver.equationsPossible(
                new String[]{"a==b", "b==c", "a==c"}
        );

        assert !equationsSolver.equationsPossible(
                new String[]{"a==b", "b!=a"}
        );

        // ---------------------------------------------------------------------
        // Network Connected
        // ---------------------------------------------------------------------
        NetworkConnected networkSolver = new NetworkConnected();

        assert networkSolver.makeConnected(
                4,
                new int[][]{
                        {0, 1},
                        {0, 2},
                        {1, 2}
                }
        ) == 1;

        // ---------------------------------------------------------------------
        // Smallest String With Swaps
        // ---------------------------------------------------------------------
        SmallestStringWithSwaps swapsSolver = new SmallestStringWithSwaps();

        assert swapsSolver.smallestStringWithSwaps(
                "dcab",
                List.of(
                        List.of(0, 3),
                        List.of(1, 2)
                )
        ).equals("bacd");

        // ---------------------------------------------------------------------
        // Graph Valid Tree — DFS and DSU are both strong primary solutions
        // ---------------------------------------------------------------------
        GraphValidTreeDFS treeDFS =
                new GraphValidTreeDFS();

        GraphValidTreeDSU treeDSU =
                new GraphValidTreeDSU();

        int[][] validTreeEdges = {
                {0, 1},
                {0, 2},
                {0, 3},
                {1, 4}
        };

        int[][] cyclicTreeEdges = {
                {0, 1},
                {1, 2},
                {2, 3},
                {1, 3},
                {1, 4}
        };

        assert treeDFS.validTree(5, validTreeEdges);
        assert treeDSU.validTree(5, validTreeEdges);

        assert !treeDFS.validTree(5, cyclicTreeEdges);
        assert !treeDSU.validTree(5, cyclicTreeEdges);

        System.out.println("All Java Gold FINAL assertions passed.");
    }

    private static List<String> findAccountContaining(
            List<List<String>> accounts,
            String email) {

        for (List<String> rawAccount : accounts) {
            if (rawAccount.contains(email)) {
                return rawAccount;
            }
        }

        return null;
    }
}

/*
I know the DSU engine once.
I can point to the DELTA in each related problem.
I can reconstruct the boilerplate without memorizing each problem separately.
*/
