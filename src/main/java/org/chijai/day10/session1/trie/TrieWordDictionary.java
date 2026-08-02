package org.chijai.day10.session1.trie;



import java.util.*;

/*
================================================================================
📘 LEETCODE 211 — DESIGN ADD AND SEARCH WORDS DATA STRUCTURE
Difficulty : Medium

Tags
Trie
DFS
Backtracking
Design
String

LeetCode
https://leetcode.com/problems/design-add-and-search-words-data-structure/

--------------------------------------------------------------------------------
PROBLEM

Design a data structure supporting two operations.

addWord(word)
    Insert a lowercase English word.

search(word)
    Return whether a previously inserted word matches.

The search string may contain '.'

'.' matches ANY SINGLE lowercase letter.

Examples

addWord("bad")
addWord("dad")
addWord("mad")

search("pad") -> false
search("bad") -> true
search(".ad") -> true
search("b..") -> true

--------------------------------------------------------------------------------
Constraints

1 <= word.length <= 25

addWord()
    lowercase letters only

search()
    lowercase letters OR '.'

At most 10^4 total operations.

Search contains at most TWO dots.

================================================================================
🔵 CORE PATTERN OVERVIEW

Pattern
Trie + DFS State Search

Problem Archetype

Store many strings while supporting
prefix navigation plus wildcard expansion.

Whenever search reaches '.',
we branch into every existing child.

This is NOT brute force over all words.

It is DFS over ONLY feasible Trie paths.

--------------------------------------------------------------------------------
🟢 CORE INVARIANT

At every recursive call

(node, index)

represents

"We have successfully matched every character
before index and are currently positioned at
the Trie node representing exactly that prefix."

Nothing before index is ever reconsidered.

Every recursive branch preserves this invariant.

--------------------------------------------------------------------------------
Why it works

Trie stores common prefixes once.

Normal characters

follow exactly ONE edge.

Wildcard '.'

tries every possible child.

Because every recursive call preserves the matched
prefix invariant, correctness follows naturally.

--------------------------------------------------------------------------------
Recognition Signals

✓ Dictionary of words

✓ Multiple insertions

✓ Many search queries

✓ Prefix sharing

✓ Wildcard matching

✓ Alphabet is small (26)

Whenever you see

"many strings"

+

"wildcard"

+

"online insert"

Trie should become the default candidate.

--------------------------------------------------------------------------------
Similar Patterns

HashSet

Good:
Exact lookup

Bad:
Wildcard search

--------------------------------------------------

Balanced BST

Good:
Ordered traversal

Bad:
Wildcard expansion

--------------------------------------------------

Trie

Excellent:
Prefix queries
Wildcard search
Autocomplete
Dictionary matching

================================================================================
🟢 MENTAL MODEL

Imagine a road network.

Root

represents

empty string.

Each edge

adds ONE character.

Example

root
 |
 b
 |
 a
 |
 d

represents

"bad"

Now suppose search()

".ad"

First character is unknown.

Instead of choosing one road,

we temporarily explore every available road.

Every branch still represents

"matched prefix so far."

Only successful branches survive.

================================================================================
🟢 VARIABLES

root

Starting Trie node.

node

Current matched prefix.

index

Current character in search word.

children[26]

Possible next letters.

isWord

Marks complete inserted word.

================================================================================
🟢 INVARIANTS

Invariant 1

Current node represents exactly

word[0...index-1]

Invariant 2

Nothing before index changes.

Invariant 3

Every recursive call consumes exactly one character.

Invariant 4

Normal letters create exactly one recursive path.

Invariant 5

Wildcard creates independent recursive branches.

Invariant 6

Search succeeds only if

all characters consumed

AND

current node marks complete word.

================================================================================
🟢 ALLOWED MOVES

Letter

Move to corresponding child.

'.'

Visit every non-null child.

Return true immediately when one succeeds.

================================================================================
🔴 FORBIDDEN MOVES

Never skip characters.

Never consume two characters together.

Never return true merely because prefix exists.

Never ignore isWord.

Never revisit previous index.

================================================================================
🟡 TERMINATION

If index == word.length()

search succeeds iff

node.isWord == true

================================================================================
🔴 WHY NAIVE APPROACHES FAIL

------------------------------------------------------------------------------
Wrong Idea 1

Store all words inside HashSet.

Seems correct

Exact lookup works.

Fails

Wildcard requires generating all possibilities.

Example

".."

26^2 possibilities.

Impossible to scale.

Invariant violation

Search state is no longer represented by prefix.

------------------------------------------------------------------------------
Wrong Idea 2

Compare against every inserted word.

Works logically.

Time

O(number_of_words × length)

Too expensive.

Invariant violation

No prefix pruning.

------------------------------------------------------------------------------
Wrong Idea 3

Return true after matching every character,
ignoring isWord.

Counterexample

Inserted

badger

Searching

bad

Prefix exists.

Word does not.

Need terminal marker.

================================================================================
⚙️ HOW TO PHYSICALLY ASSEMBLE THE CODE

🛠 IMPLEMENTATION BLUEPRINT

STEP 1

Create TrieNode.

Fields

children[26]
isWord

--------------------------------------------------

STEP 2

Create root.

--------------------------------------------------

STEP 3

addWord()

Start at root.

Loop over characters.

Create missing child.

Move forward.

Mark final node.

--------------------------------------------------

STEP 4

search()

Delegate to DFS

(root, word, 0)

--------------------------------------------------

STEP 5

DFS

Base case

index reached end

Return node.isWord

--------------------------------------------------

STEP 6

Read current character.

--------------------------------------------------

STEP 7

If normal character

Follow one child.

--------------------------------------------------

STEP 8

If '.'

Loop over every child.

Return true immediately if any succeeds.

Else false.

================================================================================
🧾 ULTRA-COMPACT PSEUDOCODE

insert

start root

for char

create child

move

mark word

---------------------

search(node,index)

finished?

return terminal

letter?

follow one edge

dot?

try every child

return any success

================================================================================
PRIMARY SOLUTION CLASSES

1.
Brute Force

Store every inserted word.

Search compares against all compatible words.

Time

Add
O(1)

Search
O(N × L)

Useful only for understanding.

--------------------------------------------------------------------------------
2.
Improved

Trie

Exact search

O(L)

Wildcard

DFS over matching branches.

--------------------------------------------------------------------------------
3.
Optimal (Interview Preferred)

Trie

+

Recursive DFS

Only explores reachable prefixes.

================================================================================
*/
public class TrieWordDictionary {

    /*
    ============================================================================
    Trie Node

    Character is NOT stored.

    Child index itself implies character.

    0 -> a
    1 -> b
    ...
    25 -> z

    This reduces redundancy.

    Parent edge determines the character.
    ============================================================================
    */


    /*
    ============================================================================
    BRUTE FORCE SOLUTION

    Idea

    Store every inserted word.

    During search,

    compare against every stored word.

    Supports wildcard by checking each position.

    Good for reasoning.

    Poor scalability.
    ============================================================================
    */
    static class WordDictionaryBruteForce {

        private final List<String> words = new ArrayList<>();

        public void addWord(String word) {

            words.add(word);

        }

        public boolean search(String pattern) {

            for (String word : words) {

                if (word.length() != pattern.length()) {
                    continue;
                }

                boolean matches = true;

                for (int i = 0; i < word.length(); i++) {

                    char p = pattern.charAt(i);

                    if (p != '.' && p != word.charAt(i)) {

                        matches = false;
                        break;

                    }
                }

                if (matches) {
                    return true;
                }

            }

            return false;

        }

    }


    /*
    ============================================================================
    IMPROVED / OPTIMAL

    Trie + DFS

    All wildcard handling happens naturally by DFS.

    Shared prefixes are stored only once.

    ============================================================================
    */

    static class TrieNode {

        TrieNode[] children = new TrieNode[26];

        boolean isWord;

    }

    static class WordDictionary {

        private final TrieNode root = new TrieNode();

        /*
        ------------------------------------------------------------------------
        Core Idea

        Walk down the Trie.

        Missing node?

        Create it.

        Final node marks complete word.

        Invariant

        Current node always represents
        processed prefix.
        ------------------------------------------------------------------------
        */
        public void addWord(String word) {

            TrieNode current = root;

            for (char ch : word.toCharArray()) {

                int index = ch - 'a';

                if (current.children[index] == null) {

                    current.children[index] = new TrieNode();

                }

                current = current.children[index];

            }

            current.isWord = true;

        }

        /*
        ------------------------------------------------------------------------
        Interview narration

        // Search always starts from empty prefix.

        // Every recursive call preserves the matched prefix invariant.

        ------------------------------------------------------------------------
        */
        public boolean search(String word) {

            return dfs(root, word, 0);

        }

        /*
        ------------------------------------------------------------------------
        DFS State

        (node,index)

        Means

        Prefix before index has already matched exactly.

        Remaining work begins at index.

        ------------------------------------------------------------------------
        */
        private boolean dfs(TrieNode node, String word, int index) {

            // Dead path.

            if (node == null) {
                return false;
            }

            // Entire pattern consumed.

            // Only succeed if this is a complete word.

            if (index == word.length()) {

                return node.isWord;

            }

            char currentChar = word.charAt(index);

            // Normal character.
            // Follow exactly one edge.

            if (currentChar != '.') {

                int child = currentChar - 'a';

                // Invariant:
                // matched prefix grows by one character.

                return dfs(node.children[child], word, index + 1);

            }

            // Wildcard.

            // Explore every feasible continuation.
            //```


            for (TrieNode next : node.children) {

                // Ignore impossible branches immediately.

                if (next == null) {
                    continue;
                }

                // Invariant:
                // Every recursive branch independently represents
                // one possible matched prefix.

                if (dfs(next, word, index + 1)) {

                    // One successful branch is sufficient.

                    return true;

                }

            }

            // Every branch failed.

            return false;

        }

    }

    /*
=============================================================================
⚫ VARIATION

Return ALL Matching Words

Problem

Instead of

    boolean search(pattern)

suppose we need

    List<String> searchAll(pattern)

Example

Inserted

bad
dad
mad
bed

searchAll(".ad")

returns

[bad, dad, mad]

----------------------------------------------------------------------------

Core Observation

The Trie does NOT change.

The invariant does NOT change.

(node, index)

still means

"The prefix before index has already matched exactly."

Only the objective changes.

----------------------------------------------------------------------------

Original Problem

Goal

Does ANY matching word exist?

Therefore

return immediately after first successful branch.

----------------------------------------------------------------------------

New Problem

Goal

Return EVERY matching word.

Therefore

explore every valid branch.

No early return.

----------------------------------------------------------------------------

Why Backtracking Is Needed Here

Original search()

only tracked

(node, index)

Both are local variables.

Nothing was modified.

Therefore no backtracking was required.

Here we introduce

StringBuilder path

which is shared across recursive calls.

Before recursion

append character.

After recursion

remove character.

Every recursive call must leave

path

exactly as it found it.

----------------------------------------------------------------------------

Rule of Thumb

DFS without shared mutable state

    → No backtracking.

DFS with shared mutable state
(StringBuilder, List, visited array, board, etc.)

    → Backtracking is mandatory.

=============================================================================
*/
    static class WordDictionaryAllMatches {

        /*-----------------------------------------------------------------------
         Trie Node
         -----------------------------------------------------------------------*/
        static class TrieNode {

            TrieNode[] children = new TrieNode[26];

            boolean isWord;

        }

        private final TrieNode root = new TrieNode();

        /*-----------------------------------------------------------------------
         Insert a word into the Trie.
         -----------------------------------------------------------------------*/
        public void addWord(String word) {

            TrieNode current = root;

            for (char ch : word.toCharArray()) {

                int index = ch - 'a';

                if (current.children[index] == null) {
                    current.children[index] = new TrieNode();
                }

                current = current.children[index];
            }

            current.isWord = true;
        }

        /*-----------------------------------------------------------------------
         Return every word matching the pattern.
         '.' matches any single character.
         -----------------------------------------------------------------------*/
        public List<String> searchAll(String pattern) {

            List<String> matches = new ArrayList<>();

            dfs(root,
                    pattern,
                    0,
                    new StringBuilder(),
                    matches);

            return matches;
        }

        /*-----------------------------------------------------------------------
         DFS State

         (node, index)

         means

         Every character before index has already matched.

         path stores the currently matched word.

         -----------------------------------------------------------------------*/
        private void dfs(TrieNode node,
                         String pattern,
                         int index,
                         StringBuilder path,
                         List<String> matches) {

            if (node == null) {
                return;
            }

            // Entire pattern has been matched.
            if (index == pattern.length()) {

                if (node.isWord) {
                    matches.add(path.toString());
                }

                return;
            }

            char current = pattern.charAt(index);

            // Wildcard: explore every child.
            if (current == '.') {

                for (int i = 0; i < 26; i++) {

                    TrieNode child = node.children[i];

                    if (child == null) {
                        continue;
                    }

                    char letter = (char) ('a' + i);

                    visit(child,
                            letter,
                            pattern,
                            index + 1,
                            path,
                            matches);
                }

            }

            // Normal character: follow only one edge.
            else {

                TrieNode child = node.children[current - 'a'];

                visit(child,
                        current,
                        pattern,
                        index + 1,
                        path,
                        matches);
            }

        }

        /*-----------------------------------------------------------------------
         Visit one child.

         Choose
             Add current letter.

         Explore
             Continue DFS.

         Un-Choose (Backtrack)
             Restore path before returning.
         -----------------------------------------------------------------------*/
        private void visit(TrieNode child,
                           char letter,
                           String pattern,
                           int nextIndex,
                           StringBuilder path,
                           List<String> matches) {

            // Dead branch.
            if (child == null) {
                return;
            }

            // Choose
            path.append(letter);

            // Explore
            dfs(child,
                    pattern,
                    nextIndex,
                    path,
                    matches);

            // Un-Choose (Backtrack)
            path.deleteCharAt(path.length() - 1);
        }

    }

    /*
    =============================================================================
    🟣 INTERVIEW ARTICULATION (NO CODE)

    Q. What is the invariant?

    Every recursive state

        (node, index)

    means

    "All characters before index have already matched exactly,
    and node represents that matched prefix."

    This invariant never changes.

    -----------------------------------------------------------------------------

    Q. Why is DFS correct?

    There are only two possibilities.

    1.

    Current character is a letter.

    There is exactly ONE legal continuation.

    2.

    Current character is '.'

    Every child is a legal continuation.

    DFS simply enumerates all invariant-preserving continuations.

    -----------------------------------------------------------------------------

    Q. Why can we stop on first successful branch?

    Search asks

        "Does ANY matching word exist?"

    Therefore

    existential search

    ==

    OR over every recursive branch.

    -----------------------------------------------------------------------------

    Q. Why is isWord necessary?

    Trie node represents a prefix.

    Not every prefix is a complete word.

    Example

    inserted

        badger

    searching

        bad

    Prefix exists.

    Word does not.

    Terminal marker distinguishes both.

    -----------------------------------------------------------------------------

    Q. Why is recursion natural here?

    Every recursive call consumes exactly one character.

    State size continuously shrinks.

    Remaining suffix becomes the next subproblem.

    -----------------------------------------------------------------------------

    Q. Is this in-place?

    Not applicable.

    Trie is an external data structure.

    -----------------------------------------------------------------------------

    Q. Streaming feasibility?

    Insertion

    Yes.

    Online.

    Search

    Yes.

    Each query is independent.

    -----------------------------------------------------------------------------

    Q. When should Trie NOT be used?

    Few words.

    Rare searches.

    Huge alphabet.

    Memory-constrained environment.

    Exact lookup only.

    HashSet is simpler.

    =============================================================================
    🎯 INTERVIEW RECALL SHEET

    Pattern Trigger

    • Many strings
    • Prefix sharing
    • Wildcards
    • Online insertion

    ------------------------------------------------------------

    Core Invariant

    (node,index)

    ==
    matched prefix before index.

    ------------------------------------------------------------

    Search Target

    Reach end of pattern
    AND
    node.isWord

    ------------------------------------------------------------

    Wildcard Rule

    '.'

    Try every child.

    Any success
    =>
    success.

    ------------------------------------------------------------

    Letter Rule

    Follow exactly one edge.

    ------------------------------------------------------------

    Common Trap

    Forgetting terminal marker.

    ------------------------------------------------------------

    Edge Cases

    Empty child.

    Prefix only.

    Single letter.

    Multiple dots.

    Duplicate insertion.

    ------------------------------------------------------------

    Interview One-Liner

    Trie compresses common prefixes,
    while DFS explores only wildcard branches that remain feasible.

    ------------------------------------------------------------

    Re-derivation Cue

    "Current node always equals matched prefix."

    =============================================================================
    🔄 VARIATIONS & TWEAKS

    -----------------------------------------------------------------------------
    Variation

    Exact Dictionary

    Wildcards removed.

    DFS unnecessary.

    Trie traversal only.

    Time

    O(L)

    -----------------------------------------------------------------------------
    Variation

    Prefix Search

    Instead of checking isWord,

    simply return true after consuming prefix.

    Same invariant.

    Different termination condition.

    -----------------------------------------------------------------------------
    Variation

    Autocomplete

    Reach prefix node.

    DFS collects descendants.

    Trie unchanged.

    Search objective changes.

    -----------------------------------------------------------------------------
    Variation

    Count Matching Words

    Instead of boolean,

    sum successful branches.

    Invariant preserved.

    Aggregation changes.

    -----------------------------------------------------------------------------
    Variation

    Delete Word

    Unmark terminal.

    Remove unused nodes during unwind.

    Invariant preserved.

    -----------------------------------------------------------------------------
    Pattern Break Signal

    Alphabet becomes extremely large.

    Trie memory becomes excessive.

    Consider

    HashMap children

    instead of fixed array.

    -----------------------------------------------------------------------------
    Pattern Break Signal

    Approximate matching

    edit distance

    substitutions

    insertions

    deletions

    Simple Trie no longer sufficient.

    DP or Automaton required.

    =============================================================================
    ⚫ REINFORCEMENT PROBLEM 1

    IMPLEMENT TRIE (LEETCODE 208)

    Summary

    Design

    insert

    search

    startsWith

    No wildcard.

    Pattern

    Same Trie.

    Simpler traversal.

    =============================================================================
    */
    static class ImplementTrie {

        static class Node {

            Node[] children = new Node[26];

            boolean isWord;

        }

        private final Node root = new Node();

        public void insert(String word) {

            Node current = root;

            for (char c : word.toCharArray()) {

                int index = c - 'a';

                if (current.children[index] == null) {

                    current.children[index] = new Node();

                }

                current = current.children[index];

            }

            current.isWord = true;

        }

        public boolean search(String word) {

            Node node = walk(word);

            return node != null && node.isWord;

        }

        public boolean startsWith(String prefix) {

            return walk(prefix) != null;

        }

        private Node walk(String text) {

            Node current = root;

            for (char c : text.toCharArray()) {

                int index = c - 'a';

                if (current.children[index] == null) {

                    return null;

                }

                current = current.children[index];

            }

            return current;

        }

    }

    /*
    -----------------------------------------------------------------------------

    Key Example

    insert("apple")

    search("apple") -> true

    startsWith("app") -> true

    search("app") -> false

    insert("app")

    search("app") -> true

    -----------------------------------------------------------------------------

    Invariant Mapping

    Current node
    ==
    processed prefix.

    -----------------------------------------------------------------------------

    Edge Cases

    Duplicate insertion.

    Prefix not terminal.

    Missing edge.

    -----------------------------------------------------------------------------

    Interview Trap

    startsWith()

    does NOT require terminal node.

    =============================================================================
    ⚫ REINFORCEMENT PROBLEM 2

    MAP SUM PAIRS (LEETCODE 677)

    Summary

    Store

    key -> value

    Return sum of all values
    having given prefix.

    Same Trie.

    Extra aggregate stored.

    =============================================================================
    */
    static class MapSum {

        static class Node {

            Node[] children = new Node[26];

            int sum;

        }

        private final Node root = new Node();

        private final Map<String, Integer> values = new HashMap<>();

        public void insert(String key, int value) {

            int delta = value - values.getOrDefault(key, 0);

            values.put(key, value);

            Node current = root;

            current.sum += delta;

            for (char c : key.toCharArray()) {

                int index = c - 'a';

                if (current.children[index] == null) {

                    current.children[index] = new Node();

                }

                current = current.children[index];

                current.sum += delta;

            }

        }

        public int sum(String prefix) {

            Node current = root;

            for (char c : prefix.toCharArray()) {

                int index = c - 'a';

                if (current.children[index] == null) {

                    return 0;

                }

                current = current.children[index];

            }

            return current.sum;

        }

    }

    /*
    -----------------------------------------------------------------------------

    Invariant

    Every node stores

    sum of every key passing through it.

    Therefore

    Prefix answer

    becomes O(length).

    -----------------------------------------------------------------------------

    Edge Cases

    Updating existing key.

    Missing prefix.

    Empty Trie.

    -----------------------------------------------------------------------------```


        Interview Trap

    Forgetting to update previous value.

    The node stores cumulative sums.

    Therefore updates require

    delta = newValue - oldValue

    not simply adding the new value.

    =============================================================================
    ⚫ REINFORCEMENT PROBLEM 3

    REPLACE WORDS (LEETCODE 648)

    Summary

    Given a dictionary of roots,
    replace each word by its shortest matching root.

    Pattern

    Same Trie.

    Stop immediately when a terminal node is reached.

    =============================================================================
    */
    static class ReplaceWords {

        static class Node {

            Node[] children = new Node[26];

            boolean isWord;

        }

        private final Node root = new Node();

        public void insert(String word) {

            Node current = root;

            for (char c : word.toCharArray()) {

                int index = c - 'a';

                if (current.children[index] == null) {

                    current.children[index] = new Node();

                }

                current = current.children[index];

            }

            current.isWord = true;

        }

        public String replace(String word) {

            Node current = root;

            StringBuilder prefix = new StringBuilder();

            for (char c : word.toCharArray()) {

                int index = c - 'a';

                if (current.children[index] == null) {

                    return word;

                }

                current = current.children[index];

                prefix.append(c);

                if (current.isWord) {

                    return prefix.toString();

                }

            }

            return word;

        }

        public String replaceSentence(List<String> dictionary,
                                      String sentence) {

            for (String rootWord : dictionary) {

                insert(rootWord);

            }

            StringBuilder answer = new StringBuilder();

            String[] words = sentence.split(" ");

            for (int i = 0; i < words.length; i++) {

                if (i > 0) {

                    answer.append(' ');

                }

                answer.append(replace(words[i]));

            }

            return answer.toString();

        }

    }

    /*
    -----------------------------------------------------------------------------

    Key Example

    Dictionary

    cat
    bat
    rat

    Sentence

    cattle was rattled by battery

    Answer

    cat was rat by bat

    -----------------------------------------------------------------------------

    Invariant Mapping

    Current node
    ==
    processed prefix.

    First terminal node
    ==
    shortest valid root.

    -----------------------------------------------------------------------------

    Edge Cases

    No matching root.

    Entire word already a root.

    Multiple possible roots.

    Always choose shortest.

    -----------------------------------------------------------------------------

    Interview Trap

    Continue traversal after terminal.

    Wrong.

    First terminal is already the shortest root.

    =============================================================================
    🧩 RELATED PROBLEM 1

    WORD SEARCH II (LEETCODE 212)

    Summary

    Find every dictionary word inside a board.

    Pattern

    Trie
    +
    DFS on grid.

    Same Invariant?

    Modified.

    Trie still stores dictionary.

    DFS state now becomes

    (boardCell,
     trieNode)

    instead of

    (trieNode,
     stringIndex)

    =============================================================================
    */
    static class WordSearchII {

        static class Node {

            Node[] children = new Node[26];

            String word;

        }

        public List<String> findWords(char[][] board,
                                      String[] words) {

            Node root = build(words);

            List<String> answer = new ArrayList<>();

            int rows = board.length;
            int cols = board[0].length;

            for (int r = 0; r < rows; r++) {

                for (int c = 0; c < cols; c++) {

                    dfs(board, r, c, root, answer);

                }

            }

            return answer;

        }

        private Node build(String[] words) {

            Node root = new Node();

            for (String word : words) {

                Node current = root;

                for (char ch : word.toCharArray()) {

                    int index = ch - 'a';

                    if (current.children[index] == null) {

                        current.children[index] = new Node();

                    }

                    current = current.children[index];

                }

                current.word = word;

            }

            return root;

        }

        private void dfs(char[][] board,
                         int row,
                         int col,
                         Node node,
                         List<String> answer) {

            if (row < 0
                    || row >= board.length
                    || col < 0
                    || col >= board[0].length
                    || board[row][col] == '#') {

                return;

            }

            char ch = board[row][col];

            Node next = node.children[ch - 'a'];

            if (next == null) {

                return;

            }

            if (next.word != null) {

                answer.add(next.word);

                // Prevent duplicate reporting.

                next.word = null;

            }

            board[row][col] = '#';

            dfs(board, row + 1, col, next, answer);
            dfs(board, row - 1, col, next, answer);
            dfs(board, row, col + 1, next, answer);
            dfs(board, row, col - 1, next, answer);

            board[row][col] = ch;

        }

    }

    /*
    -----------------------------------------------------------------------------

    Same Invariant?

    Modified.

    Trie node
    ==
    matched dictionary prefix.

    Grid position
    ==
    current board path.

    -----------------------------------------------------------------------------

    Edge Case

    Duplicate discovery.

    Remove stored word after first match.

    -----------------------------------------------------------------------------

    Interview Note

    Trie prunes impossible paths immediately.

    =============================================================================
    🧩 RELATED PROBLEM 2

    LONGEST WORD IN DICTIONARY (LEETCODE 720)

    Summary

    Find longest word such that every prefix
    is also a valid word.

    Pattern

    Trie

    DFS

    Terminal-node validation.

    =============================================================================
    */
    static class LongestWordDictionary {

        static class Node {

            Node[] children = new Node[26];

            boolean isWord;

            String word;

        }

        public String longestWord(String[] words) {

            Node root = new Node();

            root.isWord = true;

            for (String word : words) {

                Node current = root;

                for (char c : word.toCharArray()) {

                    int index = c - 'a';

                    if (current.children[index] == null) {

                        current.children[index] = new Node();

                    }

                    current = current.children[index];

                }

                current.isWord = true;

                current.word = word;

            }

            String[] best = new String[]{""};

            dfs(root, best);

            return best[0];

        }

        private void dfs(Node node,
                         String[] best) {

            if (node == null || !node.isWord) {

                return;

            }

            if (node.word != null) {

                if (node.word.length() > best[0].length()
                        || (node.word.length() == best[0].length()
                        && node.word.compareTo(best[0]) < 0)) {

                    best[0] = node.word;

                }

            }

            for (Node child : node.children) {

                dfs(child, best);

            }

        }

    }

    /*
    -----------------------------------------------------------------------------

    Same Invariant?

    Modified.

    DFS is allowed to continue

    ONLY through terminal nodes.

    Every explored path therefore has
    all prefixes present.

    -----------------------------------------------------------------------------```

        Edge Cases

    Single valid word.

    Multiple answers with same length.

    Lexicographically smallest wins.

    -----------------------------------------------------------------------------

    Interview Note

    The Trie is identical.

    Only the traversal invariant changes.

    =============================================================================
    🧩 RELATED PROBLEM 3

    SEARCH SUGGESTIONS SYSTEM (LEETCODE 1268)

    Summary

    Given products and a search word,
    return at most three lexicographically smallest suggestions
    after each typed character.

    Pattern

    Trie

    +
    Prefix traversal

    +
    DFS collection.

    =============================================================================
    */
    static class SearchSuggestionsSystem {

        static class Node {

            Node[] children = new Node[26];

            boolean isWord;

        }

        private final Node root = new Node();

        public void insert(String word) {

            Node current = root;

            for (char c : word.toCharArray()) {

                int index = c - 'a';

                if (current.children[index] == null) {

                    current.children[index] = new Node();

                }

                current = current.children[index];

            }

            current.isWord = true;

        }

        public List<List<String>> suggestedProducts(String[] products,
                                                    String searchWord) {

            Arrays.sort(products);

            for (String product : products) {

                insert(product);

            }

            List<List<String>> answer = new ArrayList<>();

            Node current = root;

            StringBuilder prefix = new StringBuilder();

            for (char c : searchWord.toCharArray()) {

                prefix.append(c);

                if (current != null) {

                    current = current.children[c - 'a'];

                }

                List<String> suggestions = new ArrayList<>();

                if (current != null) {

                    collect(current,
                            prefix,
                            suggestions);

                }

                answer.add(suggestions);

            }

            return answer;

        }

        private void collect(Node node,
                             StringBuilder prefix,
                             List<String> answer) {

            if (answer.size() == 3) {

                return;

            }

            if (node.isWord) {

                answer.add(prefix.toString());

            }

            for (int i = 0; i < 26; i++) {

                if (node.children[i] == null) {

                    continue;

                }

                prefix.append((char) ('a' + i));

                collect(node.children[i],
                        prefix,
                        answer);

                prefix.deleteCharAt(prefix.length() - 1);

                if (answer.size() == 3) {

                    return;

                }

            }

        }

    }

    /*
    -----------------------------------------------------------------------------

    Same Invariant

    Current node

    ==
    typed prefix.

    DFS only extends legal continuations.

    -----------------------------------------------------------------------------

    Edge Cases

    Missing prefix.

    Fewer than three answers.

    Empty suggestion list.

    -----------------------------------------------------------------------------

    Interview Note

    Sorting once guarantees DFS naturally produces
    lexicographic answers.

    =============================================================================
    🧠 MASTERY CHECKLIST

    □ Can I explain why Trie is better than HashSet here?

    □ Can I define the recursive state exactly?

    □ Can I explain what node represents?

    □ Can I explain what index represents?

    □ Can I justify every recursive call?

    □ Can I explain why '.' branches?

    □ Can I explain why normal letters do not?

    □ Can I explain why node.isWord is necessary?

    □ Can I derive the recursion without memorizing?

    □ Can I implement addWord() from memory?

    □ Can I implement search() from only the invariant?

    □ Can I explain complexity with and without wildcards?

    □ Can I modify the Trie for prefix search?

    □ Can I adapt it for autocomplete?

    □ Can I debug a missing terminal marker?

    □ Can I debug incorrect wildcard branching?

    -----------------------------------------------------------------------------

    Explicit Answers

    -----------------------------------------------------------------------------

    Invariant

    (node,index)

    ==
    matched prefix before index.

    -----------------------------------------------------------------------------

    Search Target

    End of pattern

    AND

    terminal node.

    -----------------------------------------------------------------------------

    Discard Rule

    Letter

    Follow one edge.

    Dot

    Explore every child.

    -----------------------------------------------------------------------------

    Termination Logic

    index == pattern.length()

    return node.isWord

    -----------------------------------------------------------------------------

    Naive Failure

    HashSet

    Cannot efficiently expand wildcards.

    -----------------------------------------------------------------------------

    Critical Edge Cases

    Prefix only.

    Duplicate insert.

    Missing child.

    Multiple dots.

    Empty branch.

    -----------------------------------------------------------------------------

    Debugging Readiness

    If search unexpectedly returns true,

    verify

    node.isWord

    not merely prefix existence.

    If wildcard misses answers,

    verify

    every non-null child is explored.

    If insertion fails,

    verify

    child creation before movement.

    -----------------------------------------------------------------------------

    Variant Readiness

    Prefix search

    Autocomplete

    Replace words

    Word Search II

    Search Suggestions

    Map Sum

    Trie Delete

    -----------------------------------------------------------------------------

    Pattern Boundary

    Trie excels when

    many strings

    share prefixes

    and repeated queries exist.

    It becomes memory-expensive for

    huge sparse alphabets.

    =============================================================================
    🧪 SELF-VERIFYING TEST HELPERS

    Every test throws AssertionError on failure.

    A passing run prints nothing except the
    final success message.

    =============================================================================
    */

    private static void assertTrue(boolean value,
                                   String message) {

        if (!value) {

            throw new AssertionError(message);

        }

    }

    private static void assertFalse(boolean value,
                                    String message) {

        if (value) {

            throw new AssertionError(message);

        }

    }

    private static void assertEquals(Object expected,
                                     Object actual,
                                     String message) {

        if (!Objects.equals(expected, actual)) {

            throw new AssertionError(
                    message
                            + "\nExpected : "
                            + expected
                            + "\nActual   : "
                            + actual);

        }

    }

    /*
    =============================================================================
    TEST GROUP 1

    Official Example

    =============================================================================
    */

    private static void testOfficialExample() {

        WordDictionary dictionary = new WordDictionary();

        dictionary.addWord("bad");
        dictionary.addWord("dad");
        dictionary.addWord("mad");

        assertFalse(
                dictionary.search("pad"),
                "Unknown word should not exist."
        );

        assertTrue(
                dictionary.search("bad"),
                "Exact word should exist."
        );

        assertTrue(
                dictionary.search(".ad"),
                "Wildcard should match first letter."
        );

        assertTrue(
                dictionary.search("b.."),
                "Wildcard suffix should match."
        );

    }

    /*
    =============================================================================
    TEST GROUP 2

    Terminal Marker Validation

    =============================================================================
    */

    private static void testPrefixVsWord() {

        WordDictionary dictionary = new WordDictionary();

        dictionary.addWord("badger");

        assertFalse(
                dictionary.search("bad"),
                "Prefix alone is not a word."
        );

        assertTrue(
                dictionary.search("badger"),
                "Complete inserted word must match."
        );

    }

    /*
    =============================================================================
    TEST GROUP 3

    Duplicate Insertions

    =============================================================================
    */

    private static void testDuplicateInsertions() {

        WordDictionary dictionary = new WordDictionary();

        dictionary.addWord("apple");
        dictionary.addWord("apple");
        dictionary.addWord("apple");

        assertTrue(
                dictionary.search("apple"),
                "Duplicate insertions must remain valid."
        );

    }



        /*
    =============================================================================
    TEST GROUP 4

    Single Character Words

    =============================================================================
    */

    private static void testSingleCharacterWords() {

        WordDictionary dictionary = new WordDictionary();

        dictionary.addWord("a");
        dictionary.addWord("z");

        assertTrue(
                dictionary.search("a"),
                "Single character should match."
        );

        assertTrue(
                dictionary.search("."),
                "Wildcard should match any one-letter word."
        );

        assertFalse(
                dictionary.search("b"),
                "Non-existent one-letter word should fail."
        );

    }

    /*
    =============================================================================
    TEST GROUP 5

    Multiple Wildcards

    =============================================================================
    */

    private static void testMultipleWildcards() {

        WordDictionary dictionary = new WordDictionary();

        dictionary.addWord("code");
        dictionary.addWord("cope");
        dictionary.addWord("cake");

        assertTrue(
                dictionary.search("c..e"),
                "Multiple wildcard branches should succeed."
        );

        assertTrue(
                dictionary.search("...."),
                "All wildcard pattern should match length-4 words."
        );

        assertFalse(
                dictionary.search("....."),
                "Incorrect length must fail."
        );

    }

    /*
    =============================================================================
    TEST GROUP 6

    Missing Child Pruning

    =============================================================================
    */

    private static void testMissingChildPruning() {

        WordDictionary dictionary = new WordDictionary();

        dictionary.addWord("tree");

        assertFalse(
                dictionary.search("free"),
                "Traversal should terminate immediately on missing child."
        );

        assertFalse(
                dictionary.search("trie"),
                "Different path should fail."
        );

    }

    /*
    =============================================================================
    TEST GROUP 7

    Prefix Sharing

    =============================================================================
    */

    private static void testSharedPrefixes() {

        WordDictionary dictionary = new WordDictionary();

        dictionary.addWord("app");
        dictionary.addWord("apple");
        dictionary.addWord("application");

        assertTrue(
                dictionary.search("app"),
                "Shortest inserted prefix should match."
        );

        assertTrue(
                dictionary.search("apple"),
                "Longer shared-prefix word should match."
        );

        assertTrue(
                dictionary.search("appl."),
                "Wildcard at end should match."
        );

        assertFalse(
                dictionary.search("apply"),
                "Uninserted word should fail."
        );

    }

    /*
    =============================================================================
    TEST GROUP 8

    Wildcard Branch Selection

    Ensures DFS explores every feasible child.

    =============================================================================
    */

    private static void testWildcardBranchSelection() {

        WordDictionary dictionary = new WordDictionary();

        dictionary.addWord("bat");
        dictionary.addWord("cat");
        dictionary.addWord("rat");

        assertTrue(
                dictionary.search(".at"),
                "Wildcard should discover any matching branch."
        );

        assertTrue(
                dictionary.search("..t"),
                "Two wildcards should still succeed."
        );

        assertFalse(
                dictionary.search(".ax"),
                "Every explored branch should fail."
        );

    }

    /*
    =============================================================================
    TEST GROUP 9

    Deep Trie Paths

    =============================================================================
    */

    private static void testDeepTrie() {

        WordDictionary dictionary = new WordDictionary();

        dictionary.addWord("abcdefghijklmnopqrstuvwxy");

        assertTrue(
                dictionary.search("abcdefghijklmnopqrstuvwxy"),
                "Deep exact path should match."
        );

        assertTrue(
                dictionary.search(".bcdefghijklmnopqrstuvwxy"),
                "Wildcard at root should match."
        );

        assertTrue(
                dictionary.search("abcdefghijklmnopqrstuvwx."),
                "Wildcard at leaf should match."
        );

    }

    /*
    =============================================================================
    TEST GROUP 10

    Reinforcement Problem Verification

    =============================================================================
    */

    private static void testImplementTrie() {

        ImplementTrie trie = new ImplementTrie();

        trie.insert("apple");

        assertTrue(
                trie.search("apple"),
                "Inserted word should exist."
        );

        assertFalse(
                trie.search("app"),
                "Prefix should not automatically be a word."
        );

        assertTrue(
                trie.startsWith("app"),
                "Prefix lookup should succeed."
        );

        trie.insert("app");

        assertTrue(
                trie.search("app"),
                "Inserted prefix should now become a word."
        );

    }

    /*
    =============================================================================
    TEST GROUP 11

    Map Sum

    =============================================================================
    */

    private static void testMapSum() {

        MapSum map = new MapSum();

        map.insert("apple", 3);

        assertEquals(
                3,
                map.sum("ap"),
                "Prefix sum incorrect."
        );

        map.insert("app", 2);

        assertEquals(
                5,
                map.sum("ap"),
                "Both keys should contribute."
        );

        map.insert("apple", 5);

        assertEquals(
                7,
                map.sum("ap"),
                "Delta update should be applied."
        );

    }

    /*
    =============================================================================
    TEST GROUP 12

    Replace Words

    =============================================================================
    */

    private static void testReplaceWords() {

        ReplaceWords solution = new ReplaceWords();

        String answer = solution.replaceSentence(
                Arrays.asList("cat", "bat", "rat"),
                "the cattle was rattled by battery"
        );

        assertEquals(
                "the cat was rat by bat",
                answer,
                "Shortest root replacement failed."
        );

    }



        /*
    =============================================================================
    TEST GROUP 13

    Longest Word in Dictionary

    =============================================================================
    */

    private static void testLongestWordDictionary() {

        LongestWordDictionary solution = new LongestWordDictionary();

        String answer = solution.longestWord(
                new String[]{
                        "w",
                        "wo",
                        "wor",
                        "worl",
                        "world",
                        "banana"
                }
        );

        assertEquals(
                "world",
                answer,
                "Longest buildable word is incorrect."
        );

    }

    /*
    =============================================================================
    TEST GROUP 14

    Search Suggestions System

    =============================================================================
    */

    private static void testSearchSuggestionsSystem() {

        SearchSuggestionsSystem system = new SearchSuggestionsSystem();

        List<List<String>> answer =
                system.suggestedProducts(
                        new String[]{
                                "mobile",
                                "mouse",
                                "moneypot",
                                "monitor",
                                "mousepad"
                        },
                        "mouse"
                );

        assertEquals(
                Arrays.asList(
                        "mobile",
                        "moneypot",
                        "monitor"
                ),
                answer.get(0),
                "Suggestions for prefix 'm' are incorrect."
        );

        assertEquals(
                Arrays.asList(
                        "mouse",
                        "mousepad"
                ),
                answer.get(answer.size() - 1),
                "Suggestions for complete prefix are incorrect."
        );

    }

    /*
    =============================================================================
    TEST GROUP 15

    Word Search II

    =============================================================================
    */

    private static void testWordSearchII() {

        WordSearchII solution = new WordSearchII();

        char[][] board = {
                {'o', 'a', 'a', 'n'},
                {'e', 't', 'a', 'e'},
                {'i', 'h', 'k', 'r'},
                {'i', 'f', 'l', 'v'}
        };

        List<String> found = solution.findWords(
                board,
                new String[]{
                        "oath",
                        "pea",
                        "eat",
                        "rain"
                }
        );

        Collections.sort(found);

        assertEquals(
                Arrays.asList("eat", "oath"),
                found,
                "Board search produced incorrect words."
        );

    }

    /*
    =============================================================================
    MAIN

    Executes every verification test.

    Any AssertionError immediately identifies
    the failed invariant.

    =============================================================================
    */

    public static void main(String[] args) {

        testOfficialExample();

        testPrefixVsWord();

        testDuplicateInsertions();

        testSingleCharacterWords();

        testMultipleWildcards();

        testMissingChildPruning();

        testSharedPrefixes();

        testWildcardBranchSelection();

        testDeepTrie();

        testImplementTrie();

        testMapSum();

        testReplaceWords();

        testLongestWordDictionary();

        testSearchSuggestionsSystem();

        testWordSearchII();

        System.out.println("====================================================");
        System.out.println("All self-verifying tests passed.");
        System.out.println("Trie + DFS wildcard implementation verified.");
        System.out.println("====================================================");
        System.out.println();
        System.out.println("I understand the invariant.");
        System.out.println("I can re-derive the solution.");
        System.out.println("I can physically reconstruct the implementation under pressure.");
        System.out.println("This chapter is complete.");

    }

}


