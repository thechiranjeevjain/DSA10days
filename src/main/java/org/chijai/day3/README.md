# Day 3 — Sliding Windows, Prefix/Suffix and Strings

| Session | Pattern focus | Solutions |
|---|---|---|
| Session 1 | Fixed/variable sliding windows, need/have counts and monotonic deque | [At Most K Distinct](session1/AtMostKDistinct.java), [Longest Substring](session1/LongestSubString.java), [Sliding Window Variations](session1/LongestSubstringVariations.java), [Minimum Window Substring](session1/MinimumWindowSubstring.java), [Sliding Window Maximum](session1/SlidingWindowMaximum.java) |
| Session 2 | Prefix/suffix, exact-K counting and greedy arrays | [Product Except Self](session2/prefix/suffix/ProductOfArrayExceptSelf.java), [Nice Subarrays](session2/prefix/suffix/NiceSubArrays.java), [Gas Station](session2/prefix/suffix/GasStation.java), [Minimum Platforms](session2/prefix/suffix/MinimumPlatforms.java), [Count Unique Chars](session2/prefix/suffix/CountUniqueChars.java) |
| Session 3 | Frequency windows, palindrome, parsing and two-pointer strings | [Find All Anagrams](session3/FindAllAnagramsInAString.java), [Valid Anagram](session3/ValidAnagram.java), [Valid Palindrome](session3/ValidPalindrome.java), [Longest Palindromic Substring](session3/LongestPalindromicSubstring.java), [Longest Palindrome](session3/LongestPalindrome.java), [String to Integer](session3/StringToIntegerAtoi.java) |

Recognition cues: contiguous constraint → window; repeated maximum in a moving window → monotonic deque; exclude current index → prefix × suffix; symmetric substring → expand around center; same multiset → frequency counts.
