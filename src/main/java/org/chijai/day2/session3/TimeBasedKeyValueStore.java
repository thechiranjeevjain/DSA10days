package org.chijai.day2.session3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TimeBasedKeyValueStore {

    /*
     =====================================================================================
     2. 📘 PRIMARY PROBLEM
     =====================================================================================

     Title:
     Time Based Key Value Store

     Difficulty:
     Medium

     Tags:
     - Binary Search
     - HashMap
     - Ordered Data Structure
     - Design
     - TreeMap

     LeetCode:
     https://leetcode.com/problems/time-based-key-value-store/

     -------------------------------------------------------------------------------------

     Problem Description

     Design a data structure that supports storing multiple values for the same key,
     each associated with a timestamp.

     Operations:

     TimeMap()

         Creates an empty data structure.

     set(key, value, timestamp)

         Store value for key at timestamp.

         For every individual key, timestamps supplied to set() are strictly increasing.

     get(key, timestamp)

         Return the value associated with the largest timestamp that is

             timestampStored <= timestamp

         If no such timestamp exists, return an empty string.

     -------------------------------------------------------------------------------------

     Constraints

     • 1 <= key.length <= 100
     • 1 <= value.length <= 100
     • key/value contain lowercase letters and digits
     • 1 <= timestamp <= 10^7
     • timestamps for each key are strictly increasing
     • At most 2 * 10^5 total operations

     -------------------------------------------------------------------------------------

     Example

     TimeMap tm = new TimeMap();

     tm.set("foo","bar",1);

     tm.get("foo",1)
         -> "bar"

     tm.get("foo",3)
         -> "bar"

     tm.set("foo","bar2",4);

     tm.get("foo",4)
         -> "bar2"

     tm.get("foo",5)
         -> "bar2"

     -------------------------------------------------------------------------------------

     Important Observation

     The problem explicitly guarantees:

         timestamps are inserted in increasing order.

     This single guarantee completely changes the optimal solution.

     Because insertions are already sorted,

         we never need to sort later.

     Every key naturally owns a sorted timeline.

     The entire interview revolves around recognizing and exploiting this invariant.
     */


    /*
     =====================================================================================
     3. 🔵 CORE PATTERN OVERVIEW
     =====================================================================================

     Pattern

         HashMap + Binary Search over Sorted History

     Archetype

         Indexed history lookup.

     Search Space

         All historical versions of ONE key.

     State

         Sorted list of

             (timestamp, value)

     Transition

         Binary search discards half the timestamps each iteration.

     Search Target

         Largest timestamp

             <= queryTimestamp

     -------------------------------------------------------------------------------------

     Core Invariant

         Every key owns a strictly increasing timestamp sequence.

     Therefore

         append preserves sorted order forever.

     No insertion can invalidate previous ordering.

     Binary search is therefore always legal.

     -------------------------------------------------------------------------------------

     Why This Works

     HashMap solves

         key lookup.

     Binary search solves

         historical version lookup.

     They solve orthogonal problems.

         HashMap reduces

             O(number of keys)

         to

             O(1)

     Binary search reduces

             O(history size)

         to

             O(log history size)

     Combined complexity

         set()

             O(1)

         get()

             O(log N)

     -------------------------------------------------------------------------------------

     Recognition Signals

     Think of this pattern whenever you see

     ✓ history of updates

     ✓ immutable past entries

     ✓ append-only timeline

     ✓ monotonic timestamps

     ✓ latest version before X

     ✓ predecessor search

     ✓ versioned objects

     ✓ temporal database

     ✓ event sourcing

     -------------------------------------------------------------------------------------

     When To Use

     Use whenever

     - data only grows
     - ordering is guaranteed
     - predecessor queries dominate
     - updates never rearrange history

     -------------------------------------------------------------------------------------

     When NOT To Use

     Avoid this pattern when

     - timestamps arrive randomly
     - history must remain mutable
     - deletions occur frequently
     - ordering is not guaranteed

     Then

     TreeMap,

     balanced BST,

     skip list,

     or another ordered structure
     becomes necessary.

     -------------------------------------------------------------------------------------

     Comparison

     HashMap + Binary Search

         insert
             O(1)

         query
             O(log N)

         Requires increasing timestamps.

     TreeMap

         insert
             O(log N)

         query
             O(log N)

         Works even without monotonic insertion.

     Linear Scan

         insert
             O(1)

         query
             O(N)

         Too slow.

     HashMap only

         Cannot answer predecessor queries.

     Binary Search only

         Cannot locate the correct key efficiently.
     */


    /*
     =====================================================================================
     4. 🟢 MENTAL MODEL & INVARIANTS
     =====================================================================================

     Mental Model

     Imagine every key owns a personal timeline.

     Example

         apple

         time

         2 ---- 5 ---- 9 ---- 15

         value

         red   green yellow black

     Query

         timestamp = 11

     We are NOT searching values.

     We are searching history.

     We want

         the newest event
         that is still not after
         the requested time.

     Binary search repeatedly asks

         "Can every timestamp on the left still contain the answer?"

     If yes

         continue right.

     Otherwise

         discard right.

     -------------------------------------------------------------------------------------

     🟢 Invariant 1

     Every timeline is sorted.

     Why?

     Because set() guarantees increasing timestamps.

     Therefore

         append()

     preserves sorted order forever.

     -------------------------------------------------------------------------------------

     🟢 Invariant 2

     Answer always lies inside

         [left, right)

     during binary search.

     Anything removed can never become correct again.

     -------------------------------------------------------------------------------------

     🟢 Invariant 3

     When

         timestamp[mid] <= query

     mid is a VALID candidate.

     But maybe

         an even newer valid timestamp exists.

     Therefore

         keep searching right.

     Never return immediately.

     -------------------------------------------------------------------------------------

     🟢 Invariant 4

     When

         timestamp[mid] > query

     every element after mid is also invalid.

     Sorted order guarantees this.

     Entire right half can be discarded.

     -------------------------------------------------------------------------------------

     🟢 Invariant 5

     At termination

         left == right

     Both point to

         first timestamp > query.

     Therefore

         predecessor

             right - 1

     is automatically

         largest timestamp <= query.

     This is the entire correctness proof.

     -------------------------------------------------------------------------------------

     Variable Meaning

     map

         key
             ->
         sorted timeline

     timeline

         chronological history

     left

         first undecided candidate

     right

         first impossible position

     mid

         decision point

     -------------------------------------------------------------------------------------

     Allowed Moves

     timestamp[mid] <= query

         keep mid

         move left = mid + 1

     timestamp[mid] > query

         discard mid

         move right = mid

     -------------------------------------------------------------------------------------

     Forbidden Moves

     Returning immediately when

         timestamp[mid] <= query

     Why?

     There may exist

         a later timestamp

         still <= query.

     That later timestamp must win.

     This is the most common bug in interviews.
     */

    /*
     -------------------------------------------------------------------------------------

     Forbidden Move

         right = mid - 1

     This changes the search interval from

         [left, right)

     into

         [left, right]

     Mixing interval conventions almost always creates

         off-by-one bugs.

     Keep one convention throughout the implementation.

     We intentionally use

         left inclusive
         right exclusive

     because termination becomes extremely mechanical.

     -------------------------------------------------------------------------------------

     Termination

     Loop

         while (left < right)

     shrinks the search interval every iteration.

     Either

         left increases

     or

         right decreases.

     Therefore

         termination is guaranteed.

     -------------------------------------------------------------------------------------

     Correctness Intuition

     We never discard a possible answer.

     If timestamp[mid] <= query

         mid remains a legal candidate,
         so we search only to see whether an even newer legal timestamp exists.

     If timestamp[mid] > query

         sorted order guarantees everything to the right is also too large.

     Thus every discard is mathematically justified.

     -------------------------------------------------------------------------------------

     Why Naive Solutions Fail

     Linear scan from the beginning

         O(N)

     wastes the sorted property.

     Reverse scan

         still O(N)

     in the worst case.

     Sorting during every get()

         O(N log N)

     repeats unnecessary work.

     TreeMap works but ignores the important interview hint:

         timestamps are already increasing.

     The optimal solution exploits the guarantee instead of replacing it.
     */


    /*
     =====================================================================================
     5. 🔴 WHY WRONG SOLUTIONS FAIL
     =====================================================================================

     -------------------------------------------------------------------------------------
     Mistake 1

     Return immediately after finding

         timestamp[mid] <= query

     Looks reasonable because

         the timestamp is valid.

     Violated Invariant

         We need the LARGEST valid timestamp.

     Counterexample

         timeline

         1 4 8

         query = 6

     Returning 4 immediately may accidentally work.

     But

         timeline

         1 4 5

         query = 6

     Returning 4 is now incorrect.

     Correct answer is 5.

     -------------------------------------------------------------------------------------
     Mistake 2

     Using

         right = size - 1

     together with

         while(left < right)

     and

         right = mid

     mixes inclusive and exclusive intervals.

     Symptoms

     • infinite loops
     • missing final element
     • predecessor errors

     -------------------------------------------------------------------------------------
     Mistake 3

     Forgetting to handle

         key absent.

     Binary search on null history causes exceptions.

     Always eliminate impossible cases before searching.

     -------------------------------------------------------------------------------------
     Mistake 4

     Forgetting timestamps earlier than the first insertion.

     Example

         (5,"A")

     query = 3

     Binary search finishes with

         predecessor = -1

     Accessing it crashes.

     Always verify predecessor exists.

     -------------------------------------------------------------------------------------
     Mistake 5

     Sorting after every insertion.

     Interview Trap

         Candidate notices binary search
         but ignores increasing timestamp guarantee.

     Complexity becomes

         O(N log N)

     instead of

         O(1)

     append.

     -------------------------------------------------------------------------------------
     Mistake 6

     One global timeline.

     Different keys must own independent histories.

     Mixing histories destroys correctness immediately.

     -------------------------------------------------------------------------------------
     Interview Trap

     Interviewer may ask

         "Why not TreeMap?"

     Correct answer

         TreeMap is perfectly valid.

         However,

         the problem already guarantees increasing timestamps.

         Therefore

         an ArrayList stays sorted forever,

         making insertion O(1)
         instead of O(log N).

         Binary search still provides O(log N) retrieval.

         This is strictly better under the stated constraints.
     */


    /*
     =====================================================================================
     ⚙ IMPLEMENTATION BLUEPRINT
     =====================================================================================

     Goal

         Mechanically reconstruct the optimal implementation.

     -------------------------------------------------------------------------------------

     Step 1

         Create

             Map<String, List<Entry>>

     Every key maps to its own sorted timeline.

     -------------------------------------------------------------------------------------

     Step 2

         set()

     Locate timeline.

     If absent

         create one.

     Append

         (timestamp, value)

     Never insert in the middle.

     Never sort.

     -------------------------------------------------------------------------------------

     Step 3

         get()

     If key absent

         return "".

     Obtain timeline.

     -------------------------------------------------------------------------------------

     Step 4

         Initialize

         left = 0

         right = timeline.size()

     Remember

         right is exclusive.

     -------------------------------------------------------------------------------------

     Step 5

         Binary Search

         while(left < right)

             mid

             compare timestamp

             move boundary

     -------------------------------------------------------------------------------------

     Step 6

     If

         timestamp(mid) <= query

     move

         left = mid + 1

     because

         mid is valid,
         but maybe not the newest valid timestamp.

     -------------------------------------------------------------------------------------

     Step 7

     Else

         right = mid

     Entire right half is impossible.

     -------------------------------------------------------------------------------------

     Step 8

     Binary search finishes.

     Candidate index

         left - 1

     If

         left == 0

     there is no predecessor.

     Return empty string.

     Otherwise

         return timeline.get(left - 1).value

     Done.
     */


    /*
     =====================================================================================
     🧾 ULTRA-COMPACT PSEUDOCODE
     =====================================================================================

     create map

     set

         append(timestamp,value)

     get

         locate history

         binary search first greater timestamp

         predecessor = left - 1

         if none
             return ""

         return predecessor value
     */


    /*
     =====================================================================================
     6. SOLUTION CLASSES
     =====================================================================================
     */


    /*
     -------------------------------------------------------------------------------------
     Brute Force
     -------------------------------------------------------------------------------------

     Idea

         Store complete history.

         Scan backwards until
         first timestamp <= query.

     Invariant

         Reverse scan encounters newest timestamps first.

     Limitation

         Worst-case O(N).

     Complexity

         set()

             O(1)

         get()

             O(N)

     Interview Usefulness

         Good baseline before optimization.
     */
    static class BruteForceTimeMap {

        private static class Entry {
            final int timestamp;
            final String value;

            Entry(int timestamp, String value) {
                this.timestamp = timestamp;
                this.value = value;
            }
        }

        private final Map<String, List<Entry>> store = new HashMap<>();

        public void set(String key, String value, int timestamp) {
            store.computeIfAbsent(key, ignored -> new ArrayList<>())
                    .add(new Entry(timestamp, value));
        }

        public String get(String key, int timestamp) {

            List<Entry> history = store.get(key);

            if (history == null) {
                return "";
            }

            for (int i = history.size() - 1; i >= 0; i--) {

                Entry current = history.get(i);

                // Invariant: later timestamps were already checked.
                if (current.timestamp <= timestamp) {
                    return current.value;
                }
            }

            return "";
        }
    }


    /*
     -------------------------------------------------------------------------------------
     Improved
     -------------------------------------------------------------------------------------

     Idea

         Use TreeMap.

         floorKey() directly returns the predecessor timestamp.

     Invariant

         TreeMap always maintains sorted timestamps.

     Improvement

         Supports arbitrary insertion order.

     Complexity

         set()

             O(log N)

         get()

             O(log N)

     Interview Usefulness

         Excellent generic solution when monotonic insertion
         is NOT guaranteed.
     */
    static class ImprovedTreeMapTimeMap {

        private final Map<String, TreeMap<Integer, String>> store = new HashMap<>();

        public void set(String key, String value, int timestamp) {

            store.computeIfAbsent(key, ignored -> new TreeMap<>())
                    .put(timestamp, value);
        }

        public String get(String key, int timestamp) {

            TreeMap<Integer, String> timeline = store.get(key);

            if (timeline == null) {
                return "";
            }

            Integer predecessor = timeline.floorKey(timestamp);

            if (predecessor == null) {
                return "";
            }

            return timeline.get(predecessor);
        }
    }

    /*
 -------------------------------------------------------------------------------------
 Optimal (Interview Preferred)
 -------------------------------------------------------------------------------------

 Idea

     Exploit the problem guarantee that timestamps for every key are inserted
     in strictly increasing order.

     Therefore every history remains permanently sorted simply by appending.

     Query becomes a predecessor search using binary search.

 🟢 Invariant

     For every key,

         history[i].timestamp < history[i + 1].timestamp

     During binary search,

         answer always remains inside [left, right).

     After termination,

         left

     equals

         first timestamp > query.

     Therefore

         left - 1

     is the largest timestamp <= query.

 Correctness

     Binary search never discards a possible predecessor.

     Valid timestamps stay on the left.

     Invalid timestamps are removed from consideration.

 Complexity

     set()

         O(1)

     get()

         O(log N)

     Space

         O(total number of set operations)

 Interview Usefulness

     This is the expected optimal solution because it exploits the strongest
     guarantee given in the problem statement.
 */
    static class OptimalTimeMap {

        /*
         Every key owns an independent immutable history.
         Histories never need reordering because timestamps are monotonic.
         */
        private final Map<String, List<Entry>> store = new HashMap<>();

        /*
         Small immutable record describing one historical version.
         */
        private static class Entry {

            final int timestamp;
            final String value;

            Entry(int timestamp, String value) {
                this.timestamp = timestamp;
                this.value = value;
            }
        }

        public OptimalTimeMap() {
        }

        public void set(String key, String value, int timestamp) {

            // Invariant: history stays sorted forever because timestamps increase.
            store.computeIfAbsent(key, ignored -> new ArrayList<>())
                    .add(new Entry(timestamp, value));
        }

        public String get(String key, int timestamp) {

            // Empty input handled early.
            List<Entry> history = store.get(key);

            if (history == null) {
                return "";
            }

            int left = 0;
            int right = history.size();

            // Invariant: answer always lies inside [left, right).
            while (left < right) {

                int mid = left + (right - left) / 2;

                Entry current = history.get(mid);

                if (current.timestamp <= timestamp) {

                    // Current timestamp is valid.
                    // Search right for a newer valid timestamp.
                    left = mid + 1;

                } else {

                    // Discard right half including current.
                    right = mid;
                }
            }

            // No predecessor exists.
            if (left == 0) {
                return "";
            }

            // left is first timestamp greater than query.
            return history.get(left - 1).value;
        }
    }


    /*
     =====================================================================================
     🟣 INTERVIEW ARTICULATION
     =====================================================================================

     If asked

         "Explain your solution."

     A concise answer is:

     I maintain one history list for every key.

     Because the problem guarantees timestamps arrive in strictly increasing
     order, appending automatically keeps every history sorted.

     During get(), I perform a binary search over only that key's history.

     The search invariant is that the answer always remains inside the current
     search interval.

     Whenever I see

         timestamp <= query

     I keep searching right because I want the newest valid timestamp.

     Whenever I see

         timestamp > query

     I discard the entire right portion because sorted order guarantees every
     later timestamp is also invalid.

     The search ends at the first timestamp greater than the query.

     Therefore the predecessor immediately before it is the required answer.

     -------------------------------------------------------------------------------------

     Why Binary Search Works

     Sorted timestamps.

     Nothing more is required.

     -------------------------------------------------------------------------------------

     Discard Rule

     timestamp <= query

         keep searching right

     timestamp > query

         discard right half

     -------------------------------------------------------------------------------------

     Correctness

     Every discarded element is mathematically impossible to become the answer.

     Every retained element may still become the predecessor.

     -------------------------------------------------------------------------------------

     Termination

     Search interval strictly shrinks.

     Therefore

         left == right

     eventually.

     -------------------------------------------------------------------------------------

     In-place Feasibility

     Not applicable.

     We must preserve complete history.

     -------------------------------------------------------------------------------------

     Streaming Feasibility

     Excellent.

     Appending is O(1).

     Query remains O(log N).

     -------------------------------------------------------------------------------------

     When NOT To Use

     Do not use this approach if timestamps are not inserted in sorted order.

     In that case,

         TreeMap

     or another balanced ordered structure becomes the correct choice.
     */


    /*
     =====================================================================================
     🎯 INTERVIEW RECALL SHEET
     =====================================================================================

     Trigger

         Version history.

         Time travel.

         Historical lookup.

         Largest timestamp <= query.

     -------------------------------------------------------------------------------------

     Pattern

         HashMap

             +

         Binary Search

     -------------------------------------------------------------------------------------

     Search Target

         First timestamp greater than query.

     -------------------------------------------------------------------------------------

     Invariant

         History remains permanently sorted because insertion order is monotonic.

     -------------------------------------------------------------------------------------

     Discard Rule

         <= query

             move right

         > query

             discard right half

     -------------------------------------------------------------------------------------

     Answer

         predecessor

             left - 1

     -------------------------------------------------------------------------------------

     Common Trap

         Returning immediately after finding a valid timestamp.

     -------------------------------------------------------------------------------------

     Edge Cases

         Missing key

         Empty history

         Query before first timestamp

         Query after latest timestamp

         Single element history

     -------------------------------------------------------------------------------------

     One-Liner

         Binary search for the first timestamp greater than the query, then
         return the predecessor.

     -------------------------------------------------------------------------------------

     Re-Derivation Cue

         Think

             upper_bound()

         from C++ STL.
     */


    /*
     =====================================================================================
     🔄 VARIATIONS & TWEAKS
     =====================================================================================

     Variant

         Arbitrary timestamp insertion.

     Change

         Replace ArrayList with TreeMap.

     Reason

         Ordering must now be maintained dynamically.

     -------------------------------------------------------------------------------------

     Variant

         Millions of queries.

     Change

         Current solution remains optimal.

     Binary search scales well.

     -------------------------------------------------------------------------------------

     Variant

         Need deletion.

     Pattern Break

         ArrayList append-only invariant no longer holds.

         TreeMap becomes preferable.

     -------------------------------------------------------------------------------------

     Variant

         Need latest value only.

     Pattern Changes

         HashMap<String,String>

     is sufficient.

     No history required.

     -------------------------------------------------------------------------------------

     Variant

         Need timestamp range queries.

     Better Structure

         TreeMap

         because

             subMap()

         naturally supports intervals.

     -------------------------------------------------------------------------------------

     Variant

         Random insertion order.

     Why Current Solution Fails

         Binary search requires sorted history.

         Appending no longer preserves ordering.

         Core invariant is violated.
     */


    /*
     =====================================================================================
     🧠 MASTERY CHECKLIST
     =====================================================================================

     Can you explain the invariant?

         □ Every history stays sorted forever.

     Can you identify the search target?

         □ First timestamp greater than query.

     Can you justify the discard rule?

         □ Sorted order proves discarded timestamps cannot be answers.

     Can you prove termination?

         □ Search interval shrinks every iteration.

     Can you explain why linear scan is inferior?

         □ Ignores sorted property.

     Can you handle all edge cases?

         □ Missing key
         □ Before first timestamp
         □ After last timestamp
         □ One element
         □ Exact match

     Can you debug off-by-one errors?

         □ Understand left-inclusive right-exclusive interval.

     Can you switch to TreeMap when insertion order changes?

         □ Yes.

     Do you know the pattern boundary?

         □ Binary search requires sorted search space.
     */


    /*
     =====================================================================================
     🧪 MAIN + SELF-VERIFYING TESTS
     =====================================================================================
     */

    public static void main(String[] args) {

        OptimalTimeMap timeMap = new OptimalTimeMap();

        /*
         Happy path from the problem statement.
         */
        timeMap.set("foo", "bar", 1);

        assert "bar".equals(timeMap.get("foo", 1))
                : "Exact timestamp lookup should succeed.";

        assert "bar".equals(timeMap.get("foo", 3))
                : "Latest value before query should be returned.";

        timeMap.set("foo", "bar2", 4);

        assert "bar2".equals(timeMap.get("foo", 4))
                : "Exact match after second insertion.";

        assert "bar2".equals(timeMap.get("foo", 5))
                : "Newest predecessor should be returned.";

        /*
         Query before the first timestamp.
         */
        assert "".equals(timeMap.get("foo", 0))
                : "No predecessor exists before first insertion.";

        /*
         Missing key.
         */
        assert "".equals(timeMap.get("missing", 100))
                : "Unknown key should return empty string.";

        /*
         Single history element.
         */
        OptimalTimeMap single = new OptimalTimeMap();

        single.set("a", "x", 10);

        assert "".equals(single.get("a", 5))
                : "Earlier query should fail.";

        assert "x".equals(single.get("a", 10))
                : "Exact match should succeed.";

        assert "x".equals(single.get("a", 100))
                : "Latest value should persist.";

        /*
         Multiple predecessor checks.
         */
        OptimalTimeMap history = new OptimalTimeMap();

        history.set("k", "v1", 2);
        history.set("k", "v2", 5);
        history.set("k", "v3", 8);
        history.set("k", "v4", 20);

        assert "v1".equals(history.get("k", 2))
                : "Exact first timestamp.";

        assert "v1".equals(history.get("k", 4))
                : "Predecessor between first and second.";

        assert "v2".equals(history.get("k", 5))
                : "Exact middle timestamp.";

        assert "v2".equals(history.get("k", 7))
                : "Binary search should locate predecessor.";

        assert "v3".equals(history.get("k", 8))
                : "Exact third timestamp.";

        assert "v3".equals(history.get("k", 19))
                : "Largest timestamp below query.";

        assert "v4".equals(history.get("k", 20))
                : "Exact latest timestamp.";

        assert "v4".equals(history.get("k", 1000))
                : "Latest value should remain valid.";

        /*
         Independent histories.
         */
        OptimalTimeMap independent = new OptimalTimeMap();

        independent.set("alice", "A1", 1);
        independent.set("bob", "B1", 2);
        independent.set("alice", "A2", 3);

        assert "A2".equals(independent.get("alice", 100))
                : "Alice history must remain independent.";

        assert "B1".equals(independent.get("bob", 100))
                : "Bob history must remain independent.";

        /*
         Improved TreeMap implementation sanity check.
         */
        ImprovedTreeMapTimeMap tree = new ImprovedTreeMapTimeMap();

        tree.set("x", "one", 5);
        tree.set("x", "two", 10);

        assert "one".equals(tree.get("x", 8))
                : "TreeMap predecessor lookup.";

        assert "two".equals(tree.get("x", 10))
                : "TreeMap exact match.";

        /*
         Brute force implementation sanity check.
         */
        BruteForceTimeMap brute = new BruteForceTimeMap();

        brute.set("z", "first", 1);
        brute.set("z", "second", 9);

        assert "first".equals(brute.get("z", 4))
                : "Reverse scan predecessor.";

        assert "second".equals(brute.get("z", 100))
                : "Reverse scan latest value.";

        System.out.println("All assertions passed.");
    }

}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/