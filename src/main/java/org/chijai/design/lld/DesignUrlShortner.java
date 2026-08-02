package org.chijai.design.lld;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ============================================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================================
 *
 * Title:
 * Design URL Shortener
 *
 * Difficulty:
 * Medium (LLD / Object-Oriented Design)
 *
 * Tags:
 * Design
 * HashMap
 * OOP
 * Encoding
 * ID Generation
 * Bidirectional Mapping
 * Database Design
 * Scalability
 *
 * Problem Description
 * -------------------
 *
 * Design a URL shortening service similar to TinyURL or Bit.ly.
 *
 * Support at least:
 *
 * 1. shorten(longUrl)
 *      Returns a deterministic short URL.
 *
 * 2. expand(shortUrl)
 *      Returns original long URL.
 *
 * Functional Requirements
 * -----------------------
 *
 * • Every long URL receives one short URL.
 * • Repeated shortening of the same URL returns the same short URL.
 * • Short URLs are unique.
 * • Expanding must return the exact original URL.
 * • Unknown short URLs return null.
 *
 * Nice-to-have Features
 * ---------------------
 *
 * • click counting
 * • expiration
 * • custom aliases
 * • analytics
 * • distributed ID generation
 *
 * Constraints
 * -----------
 *
 * • O(1) average lookup
 * • O(1) average insertion
 * • Millions to billions of URLs
 * • Collision-free identifier generation
 *
 * Representative Example
 * ----------------------
 *
 * shorten("https://leetcode.com/problems/two-sum")
 *
 * =>
 *
 * https://tiny.url/AAAAAB
 *
 *
 * expand("https://tiny.url/AAAAAB")
 *
 * =>
 *
 * https://leetcode.com/problems/two-sum
 *
 *
 * Repeated shorten()
 *
 * shorten(original)
 *
 * =>
 *
 * same short URL
 *
 *
 * LeetCode Reference
 * ------------------
 *
 * https://leetcode.com/problems/encode-and-decode-tinyurl/
 *
 * ============================================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern
 * -------
 *
 * Bidirectional Mapping + Unique Identifier Generation
 *
 * Archetype
 * ---------
 *
 * Object Design
 *
 * Core Invariant
 * --------------
 *
 * Every long URL maps to exactly one identifier.
 *
 * Every identifier maps back to exactly one long URL.
 *
 * Therefore:
 *
 * long -> id -> short
 *
 * and
 *
 * short -> id -> long
 *
 * remain perfectly reversible.
 *
 * Why It Works
 * ------------
 *
 * IDs are unique.
 *
 * Encoding is reversible.
 *
 * Hash maps preserve constant-time lookup.
 *
 * Recognition Signals
 * -------------------
 *
 * Whenever the problem says:
 *
 * "Encode and Decode"
 *
 * "Compress and Recover"
 *
 * "Generate Unique Handle"
 *
 * "Bidirectional Lookup"
 *
 * think:
 *
 * Unique ID
 * +
 * Two Maps
 *
 * When To Use
 * -----------
 *
 * • URL shortener
 * • File identifiers
 * • Session tokens
 * • Object handles
 * • Cache keys
 *
 * When NOT To Use
 * ---------------
 *
 * If identifiers are derived from mutable values.
 *
 * If collisions cannot be tolerated.
 *
 * If one-directional hashing is required.
 *
 * Comparison
 * ----------
 *
 * Hashing:
 *     irreversible
 *
 * Encryption:
 *     reversible using secret
 *
 * Encoding:
 *     reversible without secret
 *
 * URL Shortener:
 *     generated identifier
 *
 * ============================================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine assigning every URL a permanent employee ID.
 *
 * The employee name may be long.
 *
 * The employee ID is short.
 *
 * The ID never changes.
 *
 * The directory maintains:
 *
 * Name -> ID
 *
 * ID -> Name
 *
 * Nobody shares IDs.
 *
 * Nobody owns multiple IDs.
 *
 * Fundamental Invariants
 * ----------------------
 *
 * Invariant 1
 *
 * Every generated numeric ID is unique.
 *
 * Invariant 2
 *
 * Every encoded key represents exactly one numeric ID.
 *
 * Invariant 3
 *
 * longToShort and shortToLong remain perfectly synchronized.
 *
 * Invariant 4
 *
 * Existing URLs never receive another identifier.
 *
 * Invariant 5
 *
 * Unknown identifiers never fabricate URLs.
 *
 * Variable Meanings
 * -----------------
 *
 * nextId
 *
 * Next unused unique identifier.
 *
 * longToShort
 *
 * Guarantees deterministic shortening.
 *
 * shortToLong
 *
 * Enables decoding.
 *
 * baseUrl
 *
 * Prefix visible to users.
 *
 * Allowed State Transitions
 * -------------------------
 *
 * New URL
 *      Generate ID
 *      Encode
 *      Store both directions
 *
 * Existing URL
 *      Return existing mapping
 *
 * Existing Short URL
 *      Lookup
 *
 * Forbidden Transitions
 * ---------------------
 *
 * Existing URL
 *      ->
 *      Different identifier
 *
 * Identifier
 *      ->
 *      Different URL
 *
 * Partial insertion
 *
 *      One map updated
 *      Other map missing
 *
 * Correctness Intuition
 * ---------------------
 *
 * Every operation preserves the two-way correspondence.
 *
 * Therefore decoding is always deterministic.
 *
 * Termination
 * -----------
 *
 * All operations perform a constant number of hash lookups.
 *
 * No recursion.
 *
 * No iterative search.
 *
 * Why Naive Solutions Fail
 * ------------------------
 *
 * Using hashCode()
 *
 * Different URLs may collide.
 *
 * Using random values
 *
 * Duplicate generation becomes possible.
 *
 * Searching linearly
 *
 * O(N) decode.
 *
 * ============================================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================================
 *
 * Mistake
 * -------
 *
 * Random six-character strings
 *
 * Why It Looks Correct
 * --------------------
 *
 * Collisions seem unlikely.
 *
 * Violated Invariant
 * ------------------
 *
 * Identifier uniqueness.
 *
 * Counterexample
 * --------------
 *
 * Two identical random strings.
 *
 * Mistake
 * -------
 *
 * Only one map.
 *
 * Looks Correct
 * -------------
 *
 * Encoding works.
 *
 * Failure
 * -------
 *
 * Reverse lookup becomes O(N).
 *
 * Mistake
 * -------
 *
 * hashCode()
 *
 * Failure
 * -------
 *
 * Java hashCode is not unique.
 *
 * Interview Trap
 * --------------
 *
 * Candidate says:
 *
 * "I'll hash the URL."
 *
 * Interviewer asks:
 *
 * "How do you recover the original URL?"
 *
 * Impossible.
 *
 * ============================================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================================
 *
 * Typing Order
 * ------------
 *
 * 1.
 * Interface
 *
 * 2.
 * Maps
 *
 * 3.
 * AtomicLong
 *
 * 4.
 * shorten()
 *
 *      lookup existing
 *      generate id
 *      encode
 *      build short URL
 *      update both maps
 *
 * 5.
 * expand()
 *
 *      lookup
 *
 * 6.
 * helpers
 *
 *      encodeBase62
 *      decodeBase62
 *
 * ============================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================================
 *
 * if exists
 *      return existing
 *
 * id++
 *
 * encode(id)
 *
 * save both directions
 *
 * return short
 *
 * decode:
 *
 * lookup
 *
 * return long
 *
 * ============================================================================
 * 6. SOLUTION CLASSES
 * ============================================================================
 */
public class DesignUrlShortner {

    /**
     * =========================================================================
     * Brute Force
     * =========================================================================
     *
     * Idea
     * ----
     *
     * Store every URL in a list.
     *
     * Search linearly.
     *
     * Invariant
     * ---------
     *
     * List preserves insertion order.
     *
     * Limitation
     * ----------
     *
     * O(N) search.
     *
     * Complexity
     * ----------
     *
     * shorten
     * O(N)
     *
     * expand
     * O(N)
     *
     * Interview Usefulness
     * --------------------
     *
     * Baseline only.
     */
    static class BruteForceUrlShortener {

        private final Map<Integer, String> idToUrl = new HashMap<>();

        public String shorten(String url) {

            for (Map.Entry<Integer, String> entry : idToUrl.entrySet()) {
                if (Objects.equals(entry.getValue(), url)) {
                    return "https://tiny.url/" + entry.getKey();
                }
            }

            int id = idToUrl.size() + 1;
            idToUrl.put(id, url);
            return "https://tiny.url/" + id;
        }

        public String expand(String shortUrl) {

            int id = Integer.parseInt(shortUrl.substring(shortUrl.lastIndexOf('/') + 1));

            return idToUrl.get(id);
        }
    }

    /**
     * =========================================================================
     * Improved
     * =========================================================================
     *
     * Idea
     * ----
     *
     * Maintain both directions.
     *
     * Invariant
     * ---------
     *
     * Both maps remain synchronized.
     *
     * Improvement
     * -----------
     *
     * O(1) average lookup.
     *
     * Complexity
     * ----------
     *
     * O(1)
     *
     * Interview Usefulness
     * --------------------
     *
     * Good transition before scalable ID generation.
     */
    static class ImprovedUrlShortener {

        protected final Map<String, String> longToShort = new HashMap<>();
        protected final Map<String, String> shortToLong = new HashMap<>();

        protected long nextId = 1;

        protected final String domain = "https://tiny.url/";

        public String shorten(String longUrl) {

            if (longToShort.containsKey(longUrl)) {
                return longToShort.get(longUrl);
            }

            String shortUrl = domain + nextId++;

            longToShort.put(longUrl, shortUrl);
            shortToLong.put(shortUrl, longUrl);

            return shortUrl;
        }

        public String expand(String shortUrl) {
            return shortToLong.get(shortUrl);
        }
    }

    /**
     * =========================================================================
     * Optimal (Interview Preferred)
     * =========================================================================
     *
     * Idea
     * ----
     *
     * Generate monotonically increasing IDs.
     *
     * Encode into Base62.
     *
     * Preserve bidirectional maps.
     *
     * Invariant
     * ---------
     *
     * Every numeric identifier is unique.
     *
     * Correctness
     * -----------
     *
     * Unique IDs guarantee collision-free encoding.
     *
     * Complexity
     * ----------
     *
     * O(1)
     *
     * Interview Usefulness
     * --------------------
     *
     * Closest to production architecture.
     */
    static class OptimalUrlShortener {

        private static final String DOMAIN = "https://tiny.url/";

        private static final char[] ALPHABET =
                "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                        .toCharArray();

        private final AtomicLong nextId = new AtomicLong(1);

        private final Map<String, UrlRecord> shortToRecord = new HashMap<>();

        private final Map<String, String> longToShort = new HashMap<>();

        /**
         * 🔵 Invariant:
         * Every original URL owns exactly one short URL.
         */
        public String shorten(String longUrl) {

            Objects.requireNonNull(longUrl);

            if (longToShort.containsKey(longUrl)) {
                return longToShort.get(longUrl);
            }

            long id = nextId.getAndIncrement();

            // 🟢 Invariant: unique ID guarantees unique encoded key.
            String key = encodeBase62(id);

            String shortUrl = DOMAIN + key;

            UrlRecord record = new UrlRecord(
                    id,
                    longUrl,
                    shortUrl,
                    Instant.now(),
                    0
            );

            longToShort.put(longUrl, shortUrl);
            shortToRecord.put(shortUrl, record);

            return shortUrl;
        }

        /**
         * 🔵 Invariant:
         * Unknown identifiers never fabricate URLs.
         */
        public String expand(String shortUrl) {

            UrlRecord record = shortToRecord.get(shortUrl);

            if (record == null) {
                return null;
            }

            // 🟢 Analytics state changes without breaking mapping invariant.
            record.clickCount++;

            return record.longUrl;
        }

        public long getClickCount(String shortUrl) {

            UrlRecord record = shortToRecord.get(shortUrl);

            return record == null ? 0 : record.clickCount;
        }

        public boolean exists(String shortUrl) {
            return shortToRecord.containsKey(shortUrl);
        }

        /**
         * 🔵 Base62 Encoding
         *
         * Numeric ID
         *
         * ->
         *
         * Compact printable identifier.
         */
        private String encodeBase62(long value) {

            if (value == 0) {
                return "0";
            }

            StringBuilder builder = new StringBuilder();

            while (value > 0) {

                // 🟢 Invariant:
                // Lowest digit resolved first.
                int digit = (int) (value % 62);

                builder.append(ALPHABET[digit]);

                value /= 62;
            }

            return builder.reverse().toString();
        }

        /**
         * Useful during debugging.
         *
         * Production decode often isn't required because
         * we lookup directly from the map.
         */
        @SuppressWarnings("unused")
        private long decodeBase62(String key) {

            long value = 0;

            for (char c : key.toCharArray()) {

                value *= 62;

                if (c >= '0' && c <= '9') {
                    value += c - '0';
                } else if (c >= 'a' && c <= 'z') {
                    value += c - 'a' + 10;
                } else {
                    value += c - 'A' + 36;
                }
            }

            return value;
        }

        /**
         * Immutable identity.
         *
         * Mutable analytics.
         */
        static class UrlRecord {

            final long id;

            final String longUrl;

            final String shortUrl;

            final Instant createdAt;

            long clickCount;

            UrlRecord(
                    long id,
                    String longUrl,
                    String shortUrl,
                    Instant createdAt,
                    long clickCount) {

                this.id = id;
                this.longUrl = longUrl;
                this.shortUrl = shortUrl;
                this.createdAt = createdAt;
                this.clickCount = clickCount;
            }
        }
    }

/**
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * Pattern
 * -------
 *
 * Bidirectional Mapping.
 *
 * Invariant
 * ---------
 *
 * Every long URL owns one identifier.
 *
 * Every identifier owns one long URL.
 *
 * Search Space
 * ------------
 *
 * Hash maps.
 *
 * State
 * -----
 *
 * long -> short
 *
 * short -> metadata
 *
 * Transition
 * ----------
 *
 * Existing URL
 *      Return existing mapping.
 *
 * New URL
 *      Allocate next identifier.
 *
 * Discard Rule
 * ------------
 *
 * No searching.
 *
 * Direct lookup.
 *
 * Correctness
 * -----------
 *
 * Uniqueness of IDs guarantees uniqueness of short URLs.
 *
 * Bidirectional storage guarantees deterministic decoding.
 *
 * Termination
 * -----------
 *
 * Constant number of hash operations.
 *
 * In-place Feasibility
 * --------------------
 *
 * Not applicable.
 *
 * Extra storage is essential.
 *
 * Streaming Feasibility
 * ---------------------
 *
 * Yes.
 *
 * URLs arrive independently.
 *
 * When NOT To Use
 * ---------------
 *
 * If identifiers must be cryptographically unpredictable.
 *
 * Then sequential IDs should be replaced by distributed
 * random or Snowflake-style generators.
 *
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================================
 *
 * Trigger
 * -------
 *
 * Encode
 * Decode
 * Reversible identifier
 *
 * Pattern
 * -------
 *
 * Unique ID
 * +
 * Two Maps
 *
 * Invariant
 * ---------
 *
 * One URL
 * ↔
 * One Identifier
 *
 * Search Target
 * -------------
 *
 * Constant-time lookup.
 *
 * Discard Rule
 * ------------
 *
 * Never scan.
 *
 * Hash lookup only.
 *
 * Common Trap
 * -----------
 *
 * hashCode()
 *
 * is not unique.
 *
 * Edge Cases
 * ----------
 *
 * Duplicate URL.
 *
 * Unknown short URL.
 *
 * Empty string.
 *
 * Extremely long URL.
 *
 * One-Liner
 * ---------
 *
 * Allocate unique IDs, encode them, maintain both directions.
 *
 * Re-Derivation Cue
 * -----------------
 *
 * Think employee ID directory.
 *
 * Identity is permanent.
 * Lookup is symmetric.
 *
 * =========================================================================
 * 🔄 VARIATIONS & TWEAKS
 * =========================================================================
 *
 * Variant
 * -------
 *
 * Custom Alias
 *
 * Change
 * ------
 *
 * User supplies identifier.
 *
 * Invariant
 * ---------
 *
 * Alias must still remain globally unique.
 *
 * Variant
 * -------
 *
 * Expiration
 *
 * Change
 * ------
 *
 * Validate timestamp before returning URL.
 *
 * Invariant Preserved
 * -------------------
 *
 * Mapping exists only while active.
 *
 * Variant
 * -------
 *
 * Analytics
 *
 * Change
 * ------
 *
 * Metadata grows.
 *
 * Mapping remains unchanged.

 /**
 * =========================================================================
 * Continued: 🔄 VARIATIONS & TWEAKS
 * =========================================================================
 *
 * Variant
 * -------
 *
 * Distributed Deployment
 *
 * Change
 * ------
 *
 * Replace AtomicLong with a globally unique ID generator.
 *
 * Examples
 * --------
 *
 * • Snowflake IDs
 * • Database sequence
 * • ZooKeeper allocator
 * • UUID (longer URLs)
 *
 * Reasoning Change
 * ----------------
 *
 * Local uniqueness becomes global uniqueness.
 *
 * Pattern Still Holds
 * -------------------
 *
 * The identifier source changes.
 *
 * The bidirectional mapping invariant does not.
 *
 * Variant
 * -------
 *
 * Database Persistence
 *
 * Change
 * ------
 *
 * Replace HashMaps with persistent storage.
 *
 * Example Schema
 * --------------
 *
 * URL_TABLE
 *
 * id
 * short_key
 * long_url
 * created_at
 * expiry
 * click_count
 *
 * Cache Layer
 * -----------
 *
 * Frequently accessed mappings may be cached in Redis.
 *
 * Pattern Boundary
 * ----------------
 *
 * Caching improves latency.
 *
 * It must never become the source of truth.
 *
 * Variant
 * -------
 *
 * Collision Detection
 *
 * Needed?
 * -------
 *
 * No.
 *
 * Sequential IDs cannot collide.
 *
 * Random generators would require retry logic.
 *
 * Variant
 * -------
 *
 * Base64 Instead of Base62
 *
 * Why It Breaks
 * -------------
 *
 * '+' and '/'
 * are not URL-friendly.
 *
 * Additional escaping becomes necessary.
 *
 * Therefore Base62 is preferred.
 *
 * Variant
 * -------
 *
 * Multi-Region Service
 *
 * Additional Requirement
 * ----------------------
 *
 * Region-aware ID allocation.
 *
 * Global uniqueness remains the invariant.
 *
 * =========================================================================
 * 🧠 MASTERY CHECKLIST
 * =========================================================================
 *
 * □ What is the invariant?
 *
 * Every URL owns exactly one identifier.
 *
 * Every identifier owns exactly one URL.
 *
 * □ What is the search target?
 *
 * Constant-time lookup.
 *
 * □ What guarantees correctness?
 *
 * Unique identifier generation.
 *
 * Bidirectional storage.
 *
 * □ Why does decoding always succeed?
 *
 * Because every generated identifier is stored.
 *
 * □ Why does duplicate shortening return the same result?
 *
 * Existing mapping is reused.
 *
 * □ Why is scanning unnecessary?
 *
 * Hash lookup replaces searching.
 *
 * □ What breaks the invariant?
 *
 * Two identifiers for one URL.
 *
 * One identifier for two URLs.
 *
 * Updating only one map.
 *
 * Random collisions.
 *
 * □ Edge Cases
 *
 * Empty URL.
 *
 * Duplicate URL.
 *
 * Unknown short URL.
 *
 * Large identifier values.
 *
 * Millions of mappings.
 *
 * □ Debugging Readiness
 *
 * Verify:
 *
 * longToShort size
 * ==
 * shortToRecord size
 *
 * Verify every insertion updates both maps.
 *
 * Verify encoded key uniqueness.
 *
 * Verify click count changes only analytics state.
 *
 * □ Variant Readiness
 *
 * Can replace:
 *
 * ID generator
 *
 * Storage
 *
 * Cache
 *
 * Expiration policy
 *
 * without changing the core invariant.
 *
 * □ Pattern Boundary
 *
 * Pattern solves reversible identifier mapping.
 *
 * It does not solve:
 *
 * Authentication
 *
 * Authorization
 *
 * Abuse prevention
 *
 * Rate limiting
 *
 * Malware detection
 *
 * =========================================================================
 * Production Design Notes (Concise)
 * =========================================================================
 *
 * Components
 * ----------
 *
 * Client
 *    |
 * Load Balancer
 *    |
 * URL Shortener Service
 *    |
 * +---------------------------+
 * |                           |
 * Cache                  Database
 *
 * ID Generation
 * -------------
 *
 * Preferred:
 *
 * Snowflake IDs
 *
 * because they are:
 *
 * globally unique
 * sortable
 * scalable
 *
 * Database
 * --------
 *
 * Indexes:
 *
 * UNIQUE(short_key)
 *
 * UNIQUE(long_url)
 *
 * Cache Strategy
 * --------------
 *
 * Cache:
 *
 * short -> long
 *
 * Evict using LRU or TTL.
 *
 * Analytics
 * ---------
 *
 * Click counting should usually be asynchronous
 * to avoid increasing redirect latency.
 *
 * Redirect Flow
 * -------------
 *
 * Receive short URL.
 *
 * Lookup cache.
 *
 * Cache miss.
 *
 * Read database.
 *
 * Increment analytics.
 *
 * Redirect (HTTP 301/302).
 *
 * Failure Modes
 * -------------
 *
 * Lost cache:
 *
 * Recover from database.
 *
 * Lost database:
 *
 * Service unavailable.
 *
 * Duplicate ID generation:
 *
 * Prevent using globally coordinated allocator.


 /**
 * =========================================================================
 * 🧪 MAIN + SELF-VERIFYING TESTS
 * =========================================================================
 */
public static void main(String[] args) {

    OptimalUrlShortener shortener = new OptimalUrlShortener();

    // Happy path: shorten then expand.
    String google = "https://www.google.com/search?q=java";
    String googleShort = shortener.shorten(google);

    assert googleShort.startsWith("https://tiny.url/")
            : "Short URL should contain configured domain.";

    assert google.equals(shortener.expand(googleShort))
            : "Expansion must recover original URL.";

    // Duplicate URL should return identical short URL.
    String duplicate = shortener.shorten(google);

    assert googleShort.equals(duplicate)
            : "Duplicate shortening must reuse existing mapping.";

    // Different URLs must receive different identifiers.
    String github = "https://github.com/openai";
    String githubShort = shortener.shorten(github);

    assert !googleShort.equals(githubShort)
            : "Different URLs must not share identifiers.";

    assert github.equals(shortener.expand(githubShort))
            : "Second mapping should decode correctly.";

    // Unknown identifier.
    assert shortener.expand("https://tiny.url/doesNotExist") == null
            : "Unknown short URL should return null.";

    // Click counting.
    long before = shortener.getClickCount(googleShort);

    shortener.expand(googleShort);
    shortener.expand(googleShort);

    long after = shortener.getClickCount(googleShort);

    assert after == before + 2
            : "Expand should increment analytics count.";

    // Exists API.
    assert shortener.exists(googleShort)
            : "Previously generated URL should exist.";

    assert !shortener.exists("https://tiny.url/xyz123")
            : "Unknown short URL should not exist.";

    // Base62 growth.
    String previous = null;

    for (int i = 0; i < 200; i++) {

        String s = shortener.shorten("https://example.com/" + i);

        assert s != null;
        assert s.startsWith("https://tiny.url/");

        if (previous != null) {
            assert !previous.equals(s)
                    : "Every generated short URL must be unique.";
        }

        previous = s;
    }

    // Empty string is still a valid deterministic key.
    String empty = shortener.shorten("");

    assert "".equals(shortener.expand(empty))
            : "Empty URL should remain reversible.";

    // Large URL.
    StringBuilder builder = new StringBuilder();

    builder.append("https://example.com/");

    for (int i = 0; i < 5000; i++) {
        builder.append('a');
    }

    String large = builder.toString();

    String largeShort = shortener.shorten(large);

    assert large.equals(shortener.expand(largeShort))
            : "Very large URLs should decode correctly.";

    // Mapping consistency.
    String repeat = shortener.shorten(large);

    assert repeat.equals(largeShort)
            : "Repeated shortening must preserve invariant.";

    // Null handling.
    boolean threw = false;

    try {
        shortener.shorten(null);
    } catch (NullPointerException e) {
        threw = true;
    }

    assert threw
            : "Null input should fail fast.";

    System.out.println("All assertions passed.");
}
}

/*
==============================================================================
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
============================================================================

 */
