# DSA 150 — Human Brain Mind Map for Fast Retrieval

## Goal

Do **not** memorize 150 problems as 150 isolated solutions.

Build a compressed mental system:

**Problem signal → Pattern → Invariant → Template → Variation → Solution**

The objective is to make retrieval fast enough that, when seeing an unfamiliar OA problem, the brain can quickly place it inside a known family.

---

# 1. The Master Mental Tree

```
                         ┌──────────────────────┐
                         │   UNKNOWN PROBLEM    │
                         └──────────┬───────────┘
                                    │
                     WHAT STRUCTURE DO I SEE?
                                    │
     ┌──────────────┬───────────────┼───────────────┬───────────────┐
     ▼              ▼               ▼               ▼               ▼
 ARRAY/STRING     LINKED          TREE            GRAPH          CHOICES/
                  STRUCTURE                                        STATES

```

---

# 2. Array / String

```
ARRAY / STRING
│
├── Contiguous region?
│      │
│      ├── Fixed/variable window
│      │      → SLIDING WINDOW
│      │
│      └── Aggregate/range information
│             → PREFIX SUM
│
├── Sorted / monotonic?
│      │
│      ├── Find boundary/value
│      │      → BINARY SEARCH
│      │
│      └── Opposite directional movement
│             → TWO POINTERS
│
├── Need fast lookup/count?
│      → HASH MAP / HASH SET
│
├── Next greater/smaller?
│      → MONOTONIC STACK
│
├── Top K / continuously best?
│      → HEAP / PRIORITY QUEUE
│
└── Intervals?
       → SORT + MERGE / SWEEP

```

---

# 3. Linked Structure

```
LINKED STRUCTURE
│
├── Cycle / middle?
│      → FAST + SLOW POINTER
│
├── Reverse?
│      → POINTER REWIRING
│
└── Merge / ordering?
       → DUMMY NODE + TWO POINTERS

```

---

# 4. Trees

```
TREE
│
├── Explore hierarchy?
│      → DFS
│
├── Level / nearest node?
│      → BFS
│
├── BST ordering?
│      → BST INVARIANT
│
└── Answer depends on children?
       → POSTORDER / TREE DP

```

---

# 5. Graphs

```
GRAPH
│
├── Reachability / components?
│      → DFS / BFS
│
├── Dependencies?
│      → TOPOLOGICAL SORT
│
├── Dynamic connectivity?
│      → UNION FIND
│
├── Shortest path — unweighted?
│      → BFS
│
└── Shortest path — weighted?
       → DIJKSTRA

```

---

# 6. Choices / State Problems

```
CHOICES / STATES
│
├── Enumerate possibilities?
│      → BACKTRACKING
│
├── Repeated states + optimization?
│      → DYNAMIC PROGRAMMING
│
├── Locally best choice can safely become permanent?
│      → GREEDY
│
└── Monotonic answer space?
       → BINARY SEARCH ON ANSWER

```

---

# 7. Compression Model

Instead of storing:

```
150 independent problems

```

Compress them into:

```
150 PROBLEMS
      ↓
~15–20 PATTERN FAMILIES
      ↓
~30–40 SUBPATTERNS
      ↓
3–10 CANONICAL PROBLEMS EACH
      ↓
ONE CORE INVARIANT PER SUBPATTERN

```

The brain should remember the **structure**, not merely the title of the question.

---

# 8. Example of Correct Storage

Do not store:

```
Longest Substring Without Repeating Characters

```

as an isolated fact.

Store it as:

```
ARRAY / STRING
   ↓
CONTIGUOUS
   ↓
VARIABLE WINDOW
   ↓
Constraint must remain valid
   ↓
LONGEST SUBSTRING WITHOUT REPEATING CHARACTERS

```

Nearby problems belong to the same family:

```
VARIABLE SLIDING WINDOW
│
├── Longest Substring Without Repeating Characters
├── Minimum Size Subarray Sum
├── Fruits Into Baskets
├── Max Consecutive Ones III
├── Longest Repeating Character Replacement
└── Minimum Window Substring

```

Now several problems occupy essentially one conceptual neighborhood.

---

# 9. The DSA Memory Palace

Associate major patterns with vivid locations.

```
🏠 WINDOW HOUSE
Two walls expand and contract
→ contiguous constraint

🔎 BINARY SEARCH TOWER
Building repeatedly cut in half
→ monotonic search space

🗺 GRAPH METRO
Stations and connections
→ reachability / shortest paths

🌳 TREE PARK
Branches spreading recursively
→ DFS / BFS / recursion

🥞 STACK RESTAURANT
Plates stacked vertically
→ next greater / matching / unresolved candidates

🏆 HEAP PODIUM
Best candidates continuously changing
→ top K / min-max

🏝 UNION-FIND ISLANDS
Separate islands merging
→ connectivity / components

🧮 DP GRID
Previously solved cells glowing
→ state + transition + reuse

🚪 BACKTRACKING MAZE
Choose → Explore → Undo
→ search through possibilities

```

The visual is only a retrieval hook.

Do not spend excessive time making the map beautiful.

**Solving and recalling strengthen the useful neural pathway.**

---

# 10. What to Remember for Every Problem

For each problem, memorize only five things.

## 1. Trigger

What wording or structural clues should activate this problem family?

## 2. Pattern

Which algorithm family solves it?

## 3. Invariant

What must remain true while the algorithm executes?

## 4. Key Move

What operation progresses the solution?

## 5. Trap

What is the most likely implementation mistake?

---

# 11. Problem Memory Card Template

```
PROBLEM:
________________________________________

TRIGGER:
________________________________________

PATTERN:
________________________________________

INVARIANT:
________________________________________

KEY MOVE:
________________________________________

TRAP:
________________________________________

```

---

# 12. Example Memory Card

## Longest Substring Without Repeating Characters

```
TRIGGER:
Longest contiguous substring satisfying a constraint

PATTERN:
Variable Sliding Window

INVARIANT:
Current window contains no duplicate characters

KEY MOVE:
Expand right.
If invalid, move left until valid again.

TRAP:
Shrinking only once instead of shrinking until validity is restored.

```

---

# 13. Pattern Invariant Cheat Sheet

```
SLIDING WINDOW
→ Maintain validity while boundaries move

TWO POINTERS
→ Each movement eliminates impossible candidates

PREFIX SUM
→ Reuse cumulative work instead of recomputing ranges

BINARY SEARCH
→ Maintain a monotonic search predicate

MONOTONIC STACK
→ Keep unresolved candidates in useful order

HEAP
→ Maintain the currently most important K elements

DFS
→ Fully explore one branch before returning

BFS
→ Expand states layer by layer

UNION FIND
→ Every component has a representative

TOPOLOGICAL SORT
→ Process nodes only after dependencies are resolved

BACKTRACKING
→ Choose → Explore → Undo

GREEDY
→ Make a locally optimal choice that never needs reversal

DYNAMIC PROGRAMMING
→ State + Transition + Reuse

BINARY SEARCH ON ANSWER
→ Guess answer → test feasibility → eliminate half

```

---

# 14. OA Retrieval Flow

When a random OA problem appears:

```
PROBLEM
   ↓
What exactly is being asked?
   ↓
What data structure dominates?
   ↓
What structural signal exists?
   ↓
Contiguous?
Sorted?
Connectivity?
Dependency?
Optimization?
Enumeration?
Top K?
Range?
   ↓
Candidate pattern
   ↓
What invariant would make this pattern valid?
   ↓
Which canonical problem is structurally similar?
   ↓
Adapt known template
   ↓
CODE

```

The eventual internal reaction should become:

> This problem looks unfamiliar, but structurally it is just a variation of X.

That is real pattern mastery.

---

# 15. Pattern Recognition Decision Tree

```
START
│
├── Contiguous array/string?
│      ├── fixed size
│      │      → FIXED WINDOW
│      │
│      ├── longest/shortest satisfying condition
│      │      → VARIABLE WINDOW
│      │
│      └── range aggregate
│             → PREFIX SUM
│
├── Sorted data?
│      ├── pair/triplet
│      │      → TWO POINTERS
│      │
│      └── target/boundary
│             → BINARY SEARCH
│
├── Need repeated fast lookup?
│      → HASH MAP / SET
│
├── Need best K / smallest / largest?
│      → HEAP
│
├── Need next greater / smaller?
│      → MONOTONIC STACK
│
├── Tree?
│      ├── levels/minimum distance
│      │      → BFS
│      │
│      └── subtree/path/recursive structure
│             → DFS
│
├── Graph?
│      ├── reachability
│      │      → DFS / BFS
│      │
│      ├── dependency
│      │      → TOPOLOGICAL SORT
│      │
│      ├── connectivity
│      │      → UNION FIND
│      │
│      └── weighted shortest path
│             → DIJKSTRA
│
├── Generate all possibilities?
│      → BACKTRACKING
│
├── Optimization + overlapping states?
│      → DP
│
├── Safe irreversible choice?
│      → GREEDY
│
└── Answer itself is monotonic?
       → BINARY SEARCH ON ANSWER

```

---

# 16. The Two Retrieval Directions

Train both.

## Direction A

```
PATTERN
   ↓
SUBPATTERN
   ↓
CANONICAL PROBLEMS

```

Example:

```
Sliding Window
   ↓
Variable Window
   ↓
Longest Substring
Minimum Window
Fruits Into Baskets
Character Replacement

```

## Direction B — More Important

```
UNKNOWN PROBLEM
   ↓
SIGNALS
   ↓
PATTERN
   ↓
INVARIANT

```

Direction B is what interviews and OAs actually test.

---

# 17. Daily Neural-Pathway Training

## First Exposure

For every new problem:

```
1. Solve / understand it
2. Classify the pattern
3. Identify the invariant
4. Attach it to the tree
5. Write the five-part memory card
6. Re-code without looking

```

---

# 18. Spaced Retrieval Schedule

```
DAY 0
Learn

DAY 1
Recall without notes

DAY 3
Solve again

DAY 7
Mixed recall

DAY 14
Random variant

DAY 30
OA-style unseen problem

```

Do not merely reread.

Always attempt retrieval before looking.

---

# 19. Blank-Sheet Exercise

Once every few days:

Take a blank sheet and reconstruct:

```
DSA
│
├── Arrays / Strings
├── Linked Structures
├── Trees
├── Graphs
├── Heaps
├── Stacks
├── Backtracking
├── Greedy
├── DP
└── Advanced Patterns

```

Then expand each branch from memory.

Finally write representative problems under each branch.

Anything you cannot reconstruct is a weak neural pathway.

---

# 20. Pattern → Problems Drill

Example:

```
VARIABLE SLIDING WINDOW

```

Immediately recall:

```
Longest Substring Without Repeating Characters
Minimum Size Subarray Sum
Fruits Into Baskets
Character Replacement
Max Consecutive Ones III
Minimum Window Substring

```

Then state the shared invariant.

---

# 21. Problem → Pattern Drill

Pick 20 problems randomly.

For each, spend no more than roughly 30–60 seconds answering:

```
1. Pattern?
2. Why?
3. Invariant?
4. Expected complexity?
5. Closest canonical problem?

```

Do not code.

This directly trains recognition speed.

---

# 22. Canonical Problems Matter More Than All Problems Equally

Inside every pattern family, choose a few anchor problems.

Example:

```
SLIDING WINDOW

Anchor 1:
Longest Substring Without Repeating Characters

Anchor 2:
Minimum Window Substring

Anchor 3:
Fixed Window Maximum Sum

```

All other sliding-window questions should mentally attach to one of those anchors.

---

# 23. Create Problem Neighborhoods

Example:

```
                         SLIDING WINDOW
                              │
             ┌────────────────┴────────────────┐
             │                                 │
         FIXED SIZE                       VARIABLE SIZE
             │                                 │
     ┌───────┴────────┐              ┌────────┼───────────┐
     │                │              │        │           │
 Maximum Sum      Anagrams       Longest   Shortest    Counting
                               Valid       Valid

```

Then attach problem names to the leaves.

This is much easier to retrieve than one enormous flat list.

---

# 24. Code Templates Are the Final Layer

Memory hierarchy:

```
SIGNAL
   ↓
PATTERN
   ↓
INVARIANT
   ↓
TEMPLATE
   ↓
PROBLEM-SPECIFIC MODIFICATION

```

Never reverse it into:

```
Problem name
   ↓
memorized code

```

That produces brittle interview performance.

---

# 25. Example Template — Sliding Window

```
int left = 0;

for (int right = 0; right < n; right++) {

    // Add right element to state

    while (windowIsInvalid()) {
        // Remove left element from state
        left++;
    }

    // Current window is valid
    updateAnswer(left, right);
}

```

Mental invariant:

```
After the while-loop finishes,
[left ... right] satisfies the required condition.

```

---

# 26. Example Template — BFS

```
Queue<Node> queue = new ArrayDeque<>();
queue.offer(start);

while (!queue.isEmpty()) {

    Node current = queue.poll();

    for (Node next : neighbors(current)) {
        if (!visited.contains(next)) {
            visited.add(next);
            queue.offer(next);
        }
    }
}

```

Mental invariant:

```
Nodes are explored in increasing distance / level order.

```

---

# 27. Example Template — Backtracking

```
void search(State state) {

    if (isComplete(state)) {
        record(state);
        return;
    }

    for (Choice choice : choices(state)) {

        apply(choice);

        search(state);

        undo(choice);
    }
}

```

Mental invariant:

```
Before exploring each sibling branch,
the state must be restored to exactly what it was before the previous choice.

```

---

# 28. Example Template — Dynamic Programming

Think:

```
STATE
What information uniquely defines the remaining problem?

TRANSITION
How does this state depend on smaller states?

BASE CASE
What states are already known?

ORDER
In what order must states be computed?

ANSWER
Which state represents the final result?

```

---

# 29. Failure Classification

Whenever you fail a problem, label the failure.

```
[P] Pattern recognition failure
[I] Invariant misunderstanding
[T] Template recall failure
[M] Mathematical reasoning failure
[C] Coding / implementation failure
[E] Edge-case failure
[D] Debugging failure
[S] Speed failure

```

Example:

```
Problem: Minimum Window Substring
Failure: [I]
Reason: Did not correctly define when the window was valid.

```

Do not respond to every failure by doing more random questions.

Repair the exact weakness.

---

# 30. Mastery Levels

## Level 1 — Recognition

```
I understand the solution when I see it.

```

Weak.

## Level 2 — Recall

```
I can reproduce the solution later.

```

Better.

## Level 3 — Pattern Recognition

```
I can classify a new variation.

```

Strong.

## Level 4 — Reconstruction

```
I can derive the implementation from the invariant.

```

Very strong.

## Level 5 — Transfer

```
I can solve an unseen problem using the same structural idea.

```

OA-ready.

---

# 31. Your Actual Target

Do not optimize for:

```
"I remember all 150 solutions."

```

Optimize for:

```
"I can see the structural family of an unfamiliar problem."

```

Ideal response to an unseen Medium:

```
Within ~2–5 minutes:

1. Identify likely pattern family
2. State the invariant
3. Explain brute force
4. Derive optimized approach
5. Estimate complexity
6. Start implementation

```

---

# 32. 80:20 Training Allocation

```
20%
Organize
Read
Build mind map
Review notes

80%
Active recall
Blank-sheet reconstruction
Re-solving
Timed mixed questions
Unseen problems
OA simulations

```

The map organizes knowledge.

**Retrieval practice creates accessibility.**

---

# 33. Weekly Loop

```
MON–THU

Learn / repair patterns
+
Re-solve weak problems
+
Mixed retrieval


FRIDAY

Pattern recognition drill
20–30 random questions
No coding initially


SATURDAY

Timed contest / OA simulation


SUNDAY

Failure analysis
Update mental tree
Reconstruct tree from memory
Re-solve failures

```

---

# 34. The Final Mental Model

```
                         DSA
                          │
          ┌───────────────┼───────────────┐
          │               │               │
       LINEAR         HIERARCHICAL      NETWORK
          │               │               │
    ARRAY/STRING         TREE            GRAPH
          │               │               │
   ┌──────┼──────┐     DFS/BFS       DFS/BFS/UF
   │      │      │
WINDOW   BS     HASH
   │
SUBPATTERN
   │
INVARIANT
   │
CANONICAL PROBLEM
   │
VARIATIONS

```

The brain should navigate downward automatically:

```
SIGNAL
→ FAMILY
→ SUBPATTERN
→ INVARIANT
→ TEMPLATE
→ SOLUTION

```

---

# 35. One-Line Pattern Backbone

Memorize this entire section until it becomes automatic:

```
Sliding Window
→ Maintain validity while boundaries move.

Two Pointers
→ Movement systematically eliminates candidates.

Prefix Sum
→ Cache cumulative work.

Binary Search
→ Exploit monotonicity to eliminate half.

Hash Map
→ Trade memory for constant-time lookup.

Monotonic Stack
→ Store unresolved candidates in ordered form.

Heap
→ Continuously maintain the most relevant elements.

DFS
→ Exhaust one branch before returning.

BFS
→ Expand by distance layers.

Union Find
→ Maintain component representatives.

Topological Sort
→ Respect dependency ordering.

Backtracking
→ Choose, explore, undo.

Greedy
→ Make a locally safe irreversible choice.

Dynamic Programming
→ Define state, transition, and reuse.

Binary Search on Answer
→ Guess, test feasibility, eliminate half.

```

---

# 36. Ultimate Rule

```
DO NOT MEMORIZE 150 SOLUTIONS.

COMPRESS THEM INTO PATTERNS.

ATTACH PATTERNS TO INVARIANTS.

ATTACH PROBLEMS TO PATTERNS.

RETRIEVE WITHOUT LOOKING.

SOLVE MIXED UNSEEN PROBLEMS.

REPEAT UNTIL:

UNKNOWN PROBLEM
      ↓
FAMILIAR STRUCTURE
      ↓
KNOWN INVARIANT
      ↓
RECONSTRUCTED SOLUTION

```

## Desired End State

> I do not need to remember the exact solution.
> I remember the structure clearly enough to reconstruct it.