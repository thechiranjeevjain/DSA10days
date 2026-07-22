package org.chijai.day3.session2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
===============================================================================
📘 PRIMARY PROBLEM
===============================================================================

LeetCode:
https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/

Title:
Count Unique Characters of All Substrings of a Given String

Difficulty:
Hard

Tags:
Contribution Technique
Last Occurrence
Previous/Next Occurrence
Combinatorics
String
Dynamic Contribution
Index Mathematics

-------------------------------------------------------------------------------
Problem
-------------------------------------------------------------------------------

For every substring, count how many characters appear exactly once inside that
substring.

Return the sum over ALL substrings.

Example:

Input:
ABC

Substrings:

A
B
C
AB
BC
ABC

Every character inside every substring is unique.

Answer:
1+1+1+2+2+3 = 10


Example:

Input:
ABA

Substrings:

A
B
A
AB
BA
ABA

Unique counts:

1
1
1
2
2
1

Answer = 8


Example:

Input:
LEETCODE

Output:
92

-------------------------------------------------------------------------------
Constraints
-------------------------------------------------------------------------------

1 <= n <= 100000

Only uppercase English letters.

The answer fits inside signed 32-bit integer.

===============================================================================
🔵 CORE PATTERN OVERVIEW
===============================================================================

Pattern Name

Contribution Technique using Previous/Next Occurrence

Problem Archetype

Instead of enumerating every substring,
enumerate every occurrence of every character
and compute exactly how many substrings consider THIS occurrence unique.

Core Invariant

Each character occurrence contributes independently.

If an occurrence is the ONLY occurrence of that character inside a substring,
then that substring receives +1 from this occurrence.

Therefore:

Total Answer

=
sum(contribution of every occurrence)

Why It Works

Every substring's unique characters are counted exactly once.

Instead of asking

"Which characters are unique inside this substring?"

we reverse the viewpoint:

"For this occurrence,
how many substrings make it unique?"

This converts O(n²) substring enumeration
into O(n).

When To Use

Whenever:

• every occurrence contributes independently
• answer is additive
• uniqueness depends on nearest equal elements
• previous/next occurrence completely determine validity

Recognition Signals

✔ "sum over all substrings"

✔ "unique occurrence"

✔ "exactly once"

✔ "count contribution"

✔ "previous occurrence"

✔ "next occurrence"

Difference From Prefix Sum

Prefix Sum

accumulates ranges.

Contribution Technique

lets every element compute its own total influence.

Difference From Sliding Window

Sliding Window maintains one active interval.

Contribution counts ALL intervals simultaneously.

===============================================================================
🟢 MENTAL MODEL & INVARIANTS
===============================================================================

Mental Model

Imagine every occurrence standing at its index.

Ask:

"How far may I expand left?"

"How far may I expand right?"

while ensuring no identical character enters.

Previous equal occurrence blocks expansion on the left.

Next equal occurrence blocks expansion on the right.

Therefore every occurrence owns one rectangle of valid substrings.

Example

A B A

index

0 1 2

Occurrence:

A at index 0

previous = -1

next = 2

Left choices

0

= index-prev

=1

Right choices

1

= next-index

=2

Contribution

1×2=2

Those substrings:

A
AB

Exactly the substrings where first A stays unique.

-------------------------------------------------------------------------------
Invariant 1
-------------------------------------------------------------------------------

Previous occurrence is exclusive.

Expansion may never cross previous equal character.

-------------------------------------------------------------------------------
Invariant 2
-------------------------------------------------------------------------------

Next occurrence is exclusive.

Expansion may never cross next equal character.

-------------------------------------------------------------------------------
Invariant 3
-------------------------------------------------------------------------------

Every valid substring chooses

one left boundary

and

one right boundary.

These choices are independent.

-------------------------------------------------------------------------------
Invariant 4
-------------------------------------------------------------------------------

Contribution

=

(left choices)

×

(right choices)

-------------------------------------------------------------------------------
Meaning Of Variables
-------------------------------------------------------------------------------

prev

nearest same character strictly left

next

nearest same character strictly right

leftChoices

index-prev

rightChoices

next-index

answer

sum of all contributions

-------------------------------------------------------------------------------
Allowed Moves
-------------------------------------------------------------------------------

Expand left until previous equal character.

Expand right until next equal character.

-------------------------------------------------------------------------------
Forbidden Moves
-------------------------------------------------------------------------------

Cross previous equal occurrence.

Cross next equal occurrence.

Doing so introduces another identical character,
destroying uniqueness.

-------------------------------------------------------------------------------
Termination
-------------------------------------------------------------------------------

Every occurrence processed once.

Total O(n).

-------------------------------------------------------------------------------
Why Naive Enumeration Fails
-------------------------------------------------------------------------------

There are

O(n²)

substrings.

Checking uniqueness costs

O(n)

Worst case

O(n³)

Impossible for n=100000.

Even using frequency arrays,

O(n²)

still fails.

===============================================================================
🔴 WHY WRONG SOLUTIONS FAIL
===============================================================================

Wrong Idea 1

Generate every substring.

Why it seems reasonable

The definition is about substrings.

Failure

Quadratic explosion.

-------------------------------------------------------------------------------

Wrong Idea 2

Maintain frequency for every start.

Complexity

O(n²)

Still too slow.

-------------------------------------------------------------------------------

Wrong Idea 3

Count every character once globally.

Counterexample

ABA

The first A contributes differently from second A.

Occurrences matter,
not characters.

-------------------------------------------------------------------------------

Wrong Idea 4

Use only previous occurrence.

Counterexample

ABCA

Need future occurrence too.

Otherwise we over-expand right.

-------------------------------------------------------------------------------

Interview Trap

People derive

(index-prev)

correctly

but forget

(next-index)

Both boundaries are required.

===============================================================================
⚙️ HOW TO PHYSICALLY ASSEMBLE THE CODE
===============================================================================

🛠 IMPLEMENTATION BLUEPRINT

Step 1

Create answer.

Step 2

Create previous array.

Step 3

Scan left→right.

Store previous occurrence.

Update last seen.

Step 4

Create next array.

Scan right→left.

Store next occurrence.

Update last seen.

Step 5

Scan every index.

leftChoices

=

i-prev[i]

rightChoices

=

next[i]-i

Contribution

=

leftChoices*rightChoices

Add to answer.

Step 6

Return answer.

===============================================================================
🧾 ULTRA-COMPACT PSEUDOCODE
===============================================================================

previous scan

next scan

answer = 0

for every index

left = i-prev

right = next-i

answer += left*right

return answer

===============================================================================
PRIMARY PROBLEM — SOLUTION CLASSES
===============================================================================
*/
public class CountUniqueChars {

    /*
    ===========================================================================
    Brute Force
    ===========================================================================

    Core Idea

    Enumerate every substring.
    Count frequencies.
    Count characters having frequency exactly one.

    Invariant

    Frequency map always represents current substring.

    Limitation Fixed

    None.

    Time

    O(n³)

    Space

    O(26)

    Interview Preference

    Only for discussion.
    */
    static class BruteForce {

        public int uniqueLetterString(String s) {

            int n = s.length();
            int answer = 0;

            for (int start = 0; start < n; start++) {

                for (int end = start; end < n; end++) {

                    int[] frequency = new int[26];

                    for (int index = start; index <= end; index++) {
                        frequency[s.charAt(index) - 'A']++;
                    }

                    for (int value : frequency) {
                        if (value == 1) {
                            answer++;
                        }
                    }
                }
            }

            return answer;
        }
    }

    /*
    ===========================================================================
    Improved
    ===========================================================================

    Core Idea

    Extend every start position.

    Update frequency incrementally.

    Recompute unique count after every extension.

    Time

    O(n²)

    Space

    O(26)

    Interview Preference

    Better than brute force,
    still not acceptable for constraints.
    */
    static class Improved {

        public int uniqueLetterString(String s) {

            int n = s.length();
            int answer = 0;

            for (int start = 0; start < n; start++) {

                int[] frequency = new int[26];

                for (int end = start; end < n; end++) {

                    frequency[s.charAt(end) - 'A']++;

                    int unique = 0;

                    for (int value : frequency) {
                        if (value == 1) {
                            unique++;
                        }
                    }

                    answer += unique;
                }
            }

            return answer;
        }
    }

    /*
    ===========================================================================
    Optimal (Interview Preferred)

    Core Idea

    Every occurrence computes its own contribution.

    Contribution

    (distance to previous equal occurrence)

    ×

    (distance to next equal occurrence)

    Invariant

    Every counted substring contains exactly one copy
    of this occurrence's character.

    Time

    O(n)

    Space

    O(n)

    Interview Preference

    Strongly preferred.
    */
    static class Optimal {

        public int uniqueLetterString(String s) {

            int n = s.length();

            int[] previous = new int[n];
            int[] next = new int[n];

            Arrays.fill(previous, -1);

            int[] lastSeen = new int[26];
            Arrays.fill(lastSeen, -1);

            // First pass:
            // previous[i] stores nearest identical character on the left.
            for (int index = 0; index < n; index++) {

                int character = s.charAt(index) - 'A';

                previous[index] = lastSeen[character];

                lastSeen[character] = index;
            }

            Arrays.fill(lastSeen, n);

            // Second pass:
            // next[i] stores nearest identical character on the right.
            for (int index = n - 1; index >= 0; index--) {

                int character = s.charAt(index) - 'A';

                next[index] = lastSeen[character];

                lastSeen[character] = index;
            }

            int answer = 0;

            for (int index = 0; index < n; index++) {

                // Invariant:
                // Left boundary cannot cross previous identical character.
                int leftChoices = index - previous[index];

                // Invariant:
                // Right boundary cannot cross next identical character.
                int rightChoices = next[index] - index;

                // Every left choice combines independently
                // with every right choice.
                answer += leftChoices * rightChoices;
            }

            return answer;
        }
    }

    /*
===============================================================================
🟣 INTERVIEW ARTICULATION (NO CODE)
===============================================================================

How would I explain the optimal idea?

Instead of iterating over every substring, I reverse the perspective.

I let every occurrence of every character ask:

    "In how many substrings am I the unique occurrence?"

The nearest identical character on the left prevents expanding farther left.

The nearest identical character on the right prevents expanding farther right.

Therefore,

    leftChoices = index - previousSame

    rightChoices = nextSame - index

Every left choice can pair with every right choice independently.

So contribution becomes

    leftChoices × rightChoices

Summing every occurrence gives the answer.

------------------------------------------------------------------------------

Correctness Guarantee

Every valid substring containing this occurrence exactly once is counted once.

No invalid substring is counted because crossing either identical occurrence
immediately violates uniqueness.

------------------------------------------------------------------------------

What breaks if we change the invariant?

If previous is not the nearest identical occurrence,

we allow substrings containing another copy.

Over-count.

If next is not nearest,

same problem.

Nearest occurrences are sufficient and necessary.

------------------------------------------------------------------------------

In-place feasibility

Yes.

Previous and next arrays may be avoided by storing only the last two
occurrences per character.

That version is slightly harder to derive during interviews.

The previous/next array solution is usually preferred.

------------------------------------------------------------------------------

Streaming feasibility

Not directly.

Future occurrences are required.

Unless contribution is delayed until future characters arrive,
a one-pass streaming solution cannot know the right boundary.

------------------------------------------------------------------------------

When NOT to use this pattern

Do NOT use contribution counting when

• answer is not additive

• occurrences interact globally

• removing one occurrence changes contributions non-locally

• nearest occurrences are insufficient

===============================================================================
🎯 INTERVIEW RECALL SHEET (30-SECOND RECALL)
===============================================================================

Pattern Trigger

✔ Sum over all substrings

✔ Exactly once

✔ Contribution counting

✔ Previous / Next occurrence

------------------------------------------------------------------------------

Core Invariant

Every occurrence contributes independently.

------------------------------------------------------------------------------

Search Target

Previous identical character.

Next identical character.

------------------------------------------------------------------------------

Discard Rule

Cannot cross nearest equal occurrence.

------------------------------------------------------------------------------

Formula

contribution

=

(i-prev)

×

(next-i)

------------------------------------------------------------------------------

Common Trap

Counting characters instead of occurrences.

------------------------------------------------------------------------------

Edge Cases

Single letter

All same letters

All distinct letters

First occurrence

Last occurrence

------------------------------------------------------------------------------

Interview One-Liner

Reverse substring enumeration into occurrence contribution.

------------------------------------------------------------------------------

Re-Derivation Cue

Ask

"How many substrings make THIS occurrence unique?"

===============================================================================
🔄 VARIATIONS & TWEAKS
===============================================================================

------------------------------------------------------------------------------
Variation 1
------------------------------------------------------------------------------

Lowercase letters

Simply change alphabet size from 26 to 26 lowercase.

Invariant unchanged.

------------------------------------------------------------------------------
Variation 2
------------------------------------------------------------------------------

ASCII

Alphabet becomes 128.

Formula unchanged.

------------------------------------------------------------------------------
Variation 3
------------------------------------------------------------------------------

Unicode

Replace fixed array with HashMap<Character,Integer>.

Pattern unchanged.

------------------------------------------------------------------------------
Variation 4
------------------------------------------------------------------------------

Count substrings where character appears exactly K times.

Pattern breaks.

Nearest occurrences are insufficient.

Need prefix-frequency style reasoning.

------------------------------------------------------------------------------
Variation 5
------------------------------------------------------------------------------

Longest substring with unique characters.

Entire pattern changes.

Sliding Window.

Not contribution counting.

===============================================================================
⚫ REINFORCEMENT PROBLEM 1
===============================================================================

Problem

LeetCode 828 (Alternative O(n) formulation)

Instead of explicitly building previous[] and next[],
maintain the previous TWO occurrences for every character.

Invariant Mapping

Every time a new occurrence arrives,

the middle occurrence now has a complete contribution because
both boundaries are known.

Edge Cases

First occurrence

Last occurrence

Consecutive duplicates

Interview Trap

Forgetting to flush remaining contributions after finishing the scan.
*/
    static class ReinforcementRollingContribution {

        public int uniqueLetterString(String s) {

            int[][] occurrence = new int[26][2];

            for (int i = 0; i < 26; i++) {
                occurrence[i][0] = -1;
                occurrence[i][1] = -1;
            }

            int answer = 0;

            for (int index = 0; index < s.length(); index++) {

                int character = s.charAt(index) - 'A';

                int previousPrevious = occurrence[character][0];
                int previous = occurrence[character][1];

                // Previous occurrence has now discovered
                // its next occurrence.
                answer += (previous - previousPrevious) * (index - previous);

                occurrence[character][0] = previous;
                occurrence[character][1] = index;
            }

            for (int character = 0; character < 26; character++) {

                int previousPrevious = occurrence[character][0];
                int previous = occurrence[character][1];

                answer += (previous - previousPrevious)
                        * (s.length() - previous);
            }

            return answer;
        }
    }

    /*
    Interview Articulation

    Instead of storing next[],
    I postpone contribution until I finally discover
    the next occurrence.

    Every occurrence is finalized exactly once.

    ===============================================================================
    ⚫ REINFORCEMENT PROBLEM 2
    ===============================================================================

    Problem

    Sum of Beauty of All Substrings (conceptual reinforcement)

    Similarity

    Iterate over substrings.

    Difference

    Contribution trick no longer works because
    beauty depends on maximum and minimum frequencies simultaneously.

    Pattern Mapping

    Contribution invariant is broken.

    Complete Optimal Java Solution
    */
    static class BeautySumOfAllSubstrings {

        public int beautySum(String s) {

            int n = s.length();
            int answer = 0;

            for (int start = 0; start < n; start++) {

                int[] frequency = new int[26];

                for (int end = start; end < n; end++) {

                    frequency[s.charAt(end) - 'a']++;

                    int maximum = 0;
                    int minimum = Integer.MAX_VALUE;

                    for (int value : frequency) {

                        if (value == 0) {
                            continue;
                        }

                        maximum = Math.max(maximum, value);
                        minimum = Math.min(minimum, value);
                    }

                    answer += maximum - minimum;
                }
            }

            return answer;
        }
    }

    /*
    Edge Cases

    One character

    All same

    All distinct

    Interview Trap

    Ignoring zero frequencies while computing minimum.

    Interview Articulation

    Although the problem also asks for all substrings,
    contribution counting fails because
    frequencies interact globally.

    ===============================================================================
    ⚫ REINFORCEMENT PROBLEM 3
    ===============================================================================

    Problem

    Count Distinct Characters of All Substrings

    Summary

    Instead of counting characters appearing exactly once,

    count distinct characters.

    Invariant

    Every FIRST occurrence inside a substring contributes.

    Modified contribution reasoning.

    Complete Optimal Java Solution
    */
    static class CountDistinctCharactersAllSubstrings {

        public long countDistinct(String s) {

            int n = s.length();

            int[] previous = new int[256];
            Arrays.fill(previous, -1);

            long answer = 0;

            for (int index = 0; index < n; index++) {

                int character = s.charAt(index);

                answer += (long) (index - previous[character]) * (n - index);

                previous[character] = index;
            }

            return answer;
        }
    }


    /*
Edge Cases

Empty (if allowed)

Single character

Repeated character

Entire string identical

Interview Trap

People incorrectly try to use both previous and next arrays.
For counting distinct characters, only the previous occurrence matters because
each occurrence is responsible for being the FIRST occurrence inside a
substring.

Interview Articulation

The contribution invariant changes.

In the original problem, an occurrence must be the ONLY copy.

Here, it only needs to be the FIRST copy.

Therefore only the previous occurrence determines validity.

===============================================================================
🧩 RELATED PROBLEM 1
===============================================================================

LeetCode 3

Longest Substring Without Repeating Characters

Pattern

Sliding Window

Relationship

Broken invariant.

We maintain one valid interval instead of counting contributions across every
interval.

Complete Optimal Java Solution
*/
    static class LongestSubstringWithoutRepeatingCharacters {

        public int lengthOfLongestSubstring(String s) {

            int[] lastSeen = new int[256];
            Arrays.fill(lastSeen, -1);

            int left = 0;
            int answer = 0;

            for (int right = 0; right < s.length(); right++) {

                char current = s.charAt(right);

                if (lastSeen[current] >= left) {
                    left = lastSeen[current] + 1;
                }

                answer = Math.max(answer, right - left + 1);

                lastSeen[current] = right;
            }

            return answer;
        }
    }

    /*
    Edge Case

    "abba"

    Interview Note

    This is NOT a contribution problem.

    It is an interval-maintenance problem.

    ===============================================================================
    🧩 RELATED PROBLEM 2
    ===============================================================================

    LeetCode 940

    Distinct Subsequences II

    Pattern

    Dynamic Programming

    Relationship

    Previous occurrence is still important,
    but substrings become subsequences.

    The contiguous invariant disappears.

    Complete Optimal Java Solution
    */
    static class DistinctSubsequencesII {

        private static final int MOD = 1_000_000_007;

        public int distinctSubseqII(String s) {

            long total = 0;

            long[] contribution = new long[26];

            for (char character : s.toCharArray()) {

                int index = character - 'a';

                long newContribution = (total + 1) % MOD;

                total = (total + newContribution - contribution[index] + MOD)
                        % MOD;

                contribution[index] = newContribution;
            }

            return (int) total;
        }
    }

    /*
    Edge Case

    "aaa"

    Interview Note

    Same idea of removing duplicate influence,
    different invariant.

    ===============================================================================
    🧩 RELATED PROBLEM 3
    ===============================================================================

    LeetCode 763

    Partition Labels

    Pattern

    Last occurrence

    Relationship

    Uses future occurrence information,
    but instead of contribution counting,
    it greedily determines partition boundaries.

    Complete Optimal Java Solution
    */
    static class PartitionLabels {

        public List<Integer> partitionLabels(String s) {

            int[] last = new int[26];

            for (int index = 0; index < s.length(); index++) {
                last[s.charAt(index) - 'a'] = index;
            }

            List<Integer> answer = new ArrayList<>();

            int start = 0;
            int end = 0;

            for (int index = 0; index < s.length(); index++) {

                end = Math.max(end, last[s.charAt(index) - 'a']);

                if (index == end) {

                    answer.add(end - start + 1);

                    start = index + 1;
                }
            }

            return answer;
        }
    }

    /*
    Edge Case

    Entire string forms one partition.

    Interview Note

    Future occurrence information is shared,
    but the invariant is greedy coverage,
    not contribution.

    ===============================================================================
    🧠 MASTERY CHECKLIST
    ===============================================================================

    □ Can I state the invariant without mentioning code?

        Every occurrence independently counts the substrings
        in which it is the unique occurrence of its character.

    ------------------------------------------------------------------------------

    □ What is the search target?

        Previous identical occurrence.

        Next identical occurrence.

    ------------------------------------------------------------------------------

    □ What is the discard rule?

        Never allow expansion across either nearest identical occurrence.

    ------------------------------------------------------------------------------

    □ Why does multiplication work?

        Left and right boundary choices are independent.

    ------------------------------------------------------------------------------

    □ Why is the answer additive?

        Every valid substring contributes exactly one unit
        for each unique occurrence it contains.

    ------------------------------------------------------------------------------

    □ Why does O(n²) fail?

        There are O(n²) substrings.

    ------------------------------------------------------------------------------

    □ Why does contribution become O(n)?

        Every occurrence is processed exactly once.

    ------------------------------------------------------------------------------

    □ Edge Cases

        □ Single character

        □ All identical

        □ All distinct

        □ Alternating duplicates

        □ Character appearing only once

    ------------------------------------------------------------------------------

    □ Debugging Readiness

        Verify previous[].

        Verify next[].

        Print

            index
            character
            previous
            next
            leftChoices
            rightChoices
            contribution

        The incorrect index almost always exposes the bug.

    ------------------------------------------------------------------------------

    □ Variant Readiness

        Can I explain why

        Longest Substring Without Repeating Characters

        does NOT use this pattern?

        Can I explain why

        Distinct Characters

        changes the invariant?

    ------------------------------------------------------------------------------

    □ Pattern Boundary

        Use contribution counting when

        • answer is additive

        • occurrences contribute independently

        • nearest equal occurrences completely determine validity

        Otherwise,

        choose another pattern.

    ===============================================================================
    🧪 SELF-VERIFY HELPERS
    ===============================================================================
    */

    private static void assertEquals(int expected,
                                     int actual,
                                     String message) {

        if (expected != actual) {

            throw new AssertionError(
                    message
                            + " Expected = "
                            + expected
                            + " Actual = "
                            + actual);
        }
    }

    private static void assertEquals(long expected,
                                     long actual,
                                     String message) {

        if (expected != actual) {

            throw new AssertionError(
                    message
                            + " Expected = "
                            + expected
                            + " Actual = "
                            + actual);
        }
    }

    /*
    ===============================================================================
    🧪 TEST DATA HELPERS
    ===============================================================================
    */

    private static int bruteReference(String s) {

        return new BruteForce().uniqueLetterString(s);
    }

    private static int optimalReference(String s) {

        return new Optimal().uniqueLetterString(s);
    }

        /*
    ===============================================================================
    main() + SELF-VERIFYING TESTS
    ===============================================================================
    */

    public static void main(String[] args) {

        /*
        --------------------------------------------------------------------------
        Happy Path
        --------------------------------------------------------------------------

        Every substring contains only unique characters.
        */

        assertEquals(
                10,
                optimalReference("ABC"),
                "ABC should contribute the sum of all substring lengths.");

        /*
        --------------------------------------------------------------------------
        Official Example

        Duplicate in the middle.
        */

        assertEquals(
                8,
                optimalReference("ABA"),
                "ABA validates contribution boundaries.");

        /*
        --------------------------------------------------------------------------
        Official Hard Example
        */

        assertEquals(
                92,
                optimalReference("LEETCODE"),
                "Official LeetCode sample.");

        /*
        --------------------------------------------------------------------------
        Boundary

        Smallest possible input.
        */

        assertEquals(
                1,
                optimalReference("A"),
                "Single character has one unique substring.");

        /*
        --------------------------------------------------------------------------
        Boundary

        All characters identical.
        */

        assertEquals(
                3,
                optimalReference("AAA"),
                "Only single-character substrings contribute.");

        /*
        --------------------------------------------------------------------------
        Boundary

        Two identical characters.
        */

        assertEquals(
                2,
                optimalReference("AA"),
                "Only the two length-1 substrings are unique.");

        /*
        --------------------------------------------------------------------------
        Boundary

        All distinct.
        */

        assertEquals(
                20,
                optimalReference("ABCD"),
                "All substrings contribute their full length.");

        /*
        --------------------------------------------------------------------------
        Interview Trap

        Duplicate at both ends.
        */

        assertEquals(
                bruteReference("ABCA"),
                optimalReference("ABCA"),
                "Contribution must equal brute force.");

        /*
        --------------------------------------------------------------------------
        Alternating duplicates.
        */

        assertEquals(
                bruteReference("ABABA"),
                optimalReference("ABABA"),
                "Alternating duplicates.");

        /*
        --------------------------------------------------------------------------
        Clustered duplicates.
        */

        assertEquals(
                bruteReference("AABBA"),
                optimalReference("AABBA"),
                "Clustered duplicate handling.");

        /*
        --------------------------------------------------------------------------
        Every character identical.
        */

        assertEquals(
                bruteReference("BBBBB"),
                optimalReference("BBBBB"),
                "Every occurrence except single-length substrings should fail.");

        /*
        --------------------------------------------------------------------------
        Random sanity tests against brute force.

        Since brute force is O(n³),
        keep strings intentionally small.
        */

        Random random = new Random(42);

        for (int test = 0; test < 300; test++) {

            int length = 1 + random.nextInt(7);

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < length; i++) {

                builder.append((char) ('A' + random.nextInt(4)));
            }

            String sample = builder.toString();

            int expected = bruteReference(sample);

            int actual = optimalReference(sample);

            assertEquals(
                    expected,
                    actual,
                    "Random verification failed for: " + sample);
        }

        /*
        --------------------------------------------------------------------------
        Verify rolling-contribution implementation
        against previous/next implementation.
        */

        ReinforcementRollingContribution rolling =
                new ReinforcementRollingContribution();

        String[] regression = {
                "A",
                "AA",
                "AAA",
                "ABC",
                "ABA",
                "ABCA",
                "LEETCODE",
                "ABCDE",
                "AABBA",
                "ABABAB",
                "ZZZZZ",
                "ABCDEA"
        };

        for (String sample : regression) {

            assertEquals(
                    optimalReference(sample),
                    rolling.uniqueLetterString(sample),
                    "Rolling contribution mismatch for: " + sample);
        }

        /*
        --------------------------------------------------------------------------
        Verify Count Distinct Characters of All Substrings.

        ABC

        Contributions

        A -> 3
        B -> 4
        C -> 3

        Total = 10
        */

        CountDistinctCharactersAllSubstrings distinct =
                new CountDistinctCharactersAllSubstrings();

        assertEquals(
                10L,
                distinct.countDistinct("ABC"),
                "Distinct-character contribution check.");

        /*
        --------------------------------------------------------------------------
        Verify Longest Substring Without Repeating Characters.
        */

        LongestSubstringWithoutRepeatingCharacters longest =
                new LongestSubstringWithoutRepeatingCharacters();

        assertEquals(
                3,
                longest.lengthOfLongestSubstring("abcabcbb"),
                "Classic sliding-window sample.");

        assertEquals(
                1,
                longest.lengthOfLongestSubstring("bbbbb"),
                "All identical.");

        assertEquals(
                3,
                longest.lengthOfLongestSubstring("pwwkew"),
                "Interview favorite.");

        /*
        --------------------------------------------------------------------------
        Verify Partition Labels.
        */

        PartitionLabels partition =
                new PartitionLabels();

        List<Integer> partitions =
                partition.partitionLabels("ababcbacadefegdehijhklij");

        List<Integer> expectedPartitions =
                Arrays.asList(9, 7, 8);

        if (!expectedPartitions.equals(partitions)) {

            throw new AssertionError(
                    "Partition Labels verification failed.");
        }

        /*
        --------------------------------------------------------------------------
        Verify Distinct Subsequences II.
        */

        DistinctSubsequencesII subsequences =
                new DistinctSubsequencesII();

        assertEquals(
                7,
                subsequences.distinctSubseqII("abc"),
                "Distinct subsequences.");

        assertEquals(
                3,
                subsequences.distinctSubseqII("aaa"),
                "Repeated subsequences.");

        System.out.println("========================================");
        System.out.println("All tests passed successfully.");
        System.out.println("Optimal solution verified.");
        System.out.println("Contribution invariant verified.");
        System.out.println("========================================");
    }
}

/*
===============================================================================
🧘 FINAL CLOSURE STATEMENT
===============================================================================

I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/

