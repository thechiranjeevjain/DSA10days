package org.chijai.design.lld;

import java.util.concurrent.locks.ReentrantLock;

/**
 * DesignTokenBucketRateLimiter
 *
 * ============================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================
 *
 * Title:
 * Design Rate Limiter — Token Bucket
 *
 * Difficulty:
 * Medium
 *
 * Tags:
 * Design
 * Concurrency
 * Rate Limiting
 * System Design
 * Thread Safety
 *
 * Problem Description
 * -------------------
 * Design a thread-safe Token Bucket Rate Limiter.
 *
 * The limiter contains:
 *
 * - capacity
 *      Maximum number of tokens that may exist.
 *
 * - refill rate
 *      Tokens generated every second.
 *
 * - current tokens
 *      Number of usable permits currently available.
 *
 * Whenever a request arrives:
 *
 *      allow()
 *
 * returns
 *
 *      true
 *
 * if at least one token is available.
 *
 * Otherwise
 *
 *      false
 *
 * A consumed token disappears immediately.
 *
 * Tokens are regenerated continuously according to elapsed time.
 *
 * The bucket must never contain more than capacity tokens.
 *
 * Thread safety is required.
 *
 * Constraints
 * -----------
 *
 * • capacity > 0
 * • refillRatePerSecond > 0
 * • millions of requests possible
 * • long idle periods possible
 * • multiple concurrent callers
 * • O(1) work per request
 *
 * Representative Example
 * ----------------------
 *
 * capacity = 5
 * refill = 2 tokens/sec
 *
 * Initial:
 *
 * tokens = 5
 *
 * allow()
 * -> true
 *
 * tokens = 4
 *
 * wait 500 ms
 *
 * regenerated = 1 token
 *
 * tokens = 5
 *
 * consume
 *
 * tokens = 4
 *
 * Example 2
 * ---------
 *
 * capacity = 2
 *
 * Requests:
 *
 * T
 * T
 * F
 *
 * wait 1 second
 *
 * refill = 2
 *
 * T
 * T
 *
 * Official Reference
 * ------------------
 * https://leetcode.com/discuss/interview-question/system-design/124558/design-rate-limiter
 *
 *
 * ============================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ============================================================
 *
 * Pattern
 * -------
 * Time-driven State Reconstruction
 *
 * Archetype
 * ---------
 * Lazy State Update
 *
 * Instead of continuously refilling the bucket,
 * reconstruct its state only when an operation arrives.
 *
 * Core Invariant
 * --------------
 * Before every decision,
 * the bucket state represents exactly the amount of tokens
 * that should exist according to elapsed time.
 *
 * Why It Works
 * ------------
 * Time itself becomes the driver of state transitions.
 *
 * No background thread is necessary.
 *
 * State is reconstructed on demand.
 *
 * Recognition Signals
 * -------------------
 *
 * - requests consume permits
 * - permits regenerate over time
 * - regeneration is deterministic
 * - capacity upper bound exists
 * - idle periods must not waste CPU
 *
 * When To Use
 * -----------
 *
 * ✔ API gateways
 * ✔ Payment services
 * ✔ Login throttling
 * ✔ Messaging systems
 * ✔ Distributed services
 * ✔ Reverse proxies
 *
 * When NOT To Use
 * ---------------
 *
 * ✘ Exact request scheduling
 *
 * ✘ Fair queue ordering
 *
 * ✘ Sliding window analytics
 *
 * ✘ Per-user history queries
 *
 * Comparison
 * ----------
 *
 * Token Bucket
 * ------------
 * allows bursts
 * accumulates credits
 *
 * Leaky Bucket
 * ------------
 * fixed output rate
 * smoother traffic
 *
 * Fixed Window
 * ------------
 * simple
 * suffers boundary spikes
 *
 * Sliding Window
 * --------------
 * more accurate
 * more bookkeeping
 *
 *
 * ============================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine a water bucket.
 *
 * Water drips into it continuously.
 *
 * Every request drinks exactly one unit.
 *
 * The bucket:
 *
 * - never overflows
 * - never becomes negative
 *
 * We never continuously watch the bucket.
 *
 * Instead,
 * whenever someone asks for water,
 * we first calculate how much water should have accumulated
 * since the previous visit.
 *
 * Then we answer.
 *
 *
 * ------------------------------------------------------------
 * Primary Invariant
 * ------------------------------------------------------------
 *
 * Immediately before deciding allow() or deny(),
 *
 * currentTokens
 *
 * equals
 *
 * previousTokens
 * + generatedTokens
 *
 * clamped by capacity.
 *
 *
 * ------------------------------------------------------------
 * Time Invariant
 * ------------------------------------------------------------
 *
 * lastRefillTime always represents
 * the moment up to which token generation
 * has already been accounted for.
 *
 *
 * ------------------------------------------------------------
 * Capacity Invariant
 * ------------------------------------------------------------
 *
 * 0 <= currentTokens <= capacity
 *
 *
 * ------------------------------------------------------------
 * Consumption Invariant
 * ------------------------------------------------------------
 *
 * Token consumption happens only after reconstruction.
 *
 * Never consume before refill.
 *
 *
 * ------------------------------------------------------------
 * Variable Meaning
 * ------------------------------------------------------------
 *
 * capacity
 *
 * immutable upper bound.
 *
 * refillRatePerSecond
 *
 * generation speed.
 *
 * currentTokens
 *
 * usable permits.
 *
 * lastRefillNanos
 *
 * accounting boundary.
 *
 *
 * ------------------------------------------------------------
 * Allowed State Transition
 * ------------------------------------------------------------
 *
 * reconstruct
 * ->
 * clamp
 * ->
 * consume
 * ->
 * store timestamp
 *
 *
 * Forbidden Transition
 * --------------------
 *
 * consume
 * ->
 * refill
 *
 * This loses tokens.
 *
 *
 * ------------------------------------------------------------
 * Correctness Intuition
 * ------------------------------------------------------------
 *
 * Since every operation reconstructs the exact bucket state
 * before making a decision,
 * each request behaves exactly as if a background refill thread
 * had been continuously updating the bucket.
 *
 * Lazy reconstruction and continuous refill are mathematically
 * equivalent.
 *
 *
 * ------------------------------------------------------------
 * Termination
 * ------------------------------------------------------------
 *
 * Every operation performs:
 *
 * one timestamp read
 * one refill computation
 * one decision
 *
 * Therefore O(1).
 *
 *
 * ------------------------------------------------------------
 * Why Naive Solutions Fail
 * ------------------------------------------------------------
 *
 * Naive Idea 1
 *
 * Refill exactly one token every second.
 *
 * Wrong because:
 *
 * refill rate may be fractional.
 *
 * Example:
 *
 * 100 tokens/sec
 *
 * waiting one second destroys timing precision.
 *
 *
 * Naive Idea 2
 *
 * Background refill thread.
 *
 * Problems:
 *
 * unnecessary CPU
 * synchronization complexity
 * timer drift
 * scheduling delays
 *
 *
 * Naive Idea 3
 *
 * Store integer seconds.
 *
 * Wrong.
 *
 * Sub-second accuracy disappears.
 *
 *
 * ============================================================
 * 5. 🔴 WHY WRONG SOLUTIONS FAIL
 * ============================================================
 *
 * Mistake 1
 * ---------
 *
 * Forgetting capacity clamp.
 *
 * Looks harmless.
 *
 * Eventually bucket grows infinitely.
 *
 * Violated Invariant
 *
 * currentTokens <= capacity
 *
 *
 * Mistake 2
 * ---------
 *
 * Update timestamp after consuming only.
 *
 * Idle periods become double-counted.
 *
 *
 * Mistake 3
 * ---------
 *
 * Integer arithmetic.
 *
 * Example
 *
 * refill = 3/sec
 *
 * elapsed = 200 ms
 *
 * integer math creates zero tokens forever.
 *
 *
 * Mistake 4
 * ---------
 *
 * Consume first.
 *
 * Then refill.
 *
 * This rejects requests that should succeed.
 *
 *
 * Interview Trap
 * --------------
 *
 * "Why don't we need a scheduler?"
 *
 * Because elapsed time completely determines
 * the missing state.
 *
 *
 * ============================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================
 *
 * Typing Order
 * ------------
 *
 * 1.
 * Declare immutable configuration.
 *
 * 2.
 * Declare mutable state.
 *
 * 3.
 * Create refill().
 *
 * 4.
 * Call refill() first inside allow().
 *
 * 5.
 * Check token count.
 *
 * 6.
 * Consume.
 *
 * 7.
 * Return decision.
 *
 *
 * Function Skeleton
 * -----------------
 *
 * allow()
 *      lock
 *      refill
 *      if token exists
 *          consume
 *          return true
 *      return false
 *
 *
 * Variables
 * ---------
 *
 * capacity
 * refillRatePerNano
 * currentTokens
 * lastRefillNanos
 *
 *
 * Transition
 * ----------
 *
 * elapsed
 * ->
 * generated
 * ->
 * clamp
 * ->
 * consume
 *
 *
 * Return
 * ------
 *
 * true if token available
 * false otherwise
 *
 *
 * ============================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================
 *
 * refill
 *
 * if token
 *     consume
 *     success
 *
 * deny
 *
 *
 * ============================================================
 * 6. SOLUTION CLASSES
 * ============================================================
 *
 * ------------------------------------------------------------
 * Brute Force
 * ------------------------------------------------------------
 *
 * Idea
 * ----
 *
 * Background timer periodically inserts tokens.
 *
 * Invariant
 * ---------
 *
 * Timer owns token generation.
 *
 * Limitation
 * ----------
 *
 * Requires scheduler.
 *
 * Synchronization grows complicated.
 *
 * Complexity
 * ----------
 *
 * O(1) request
 *
 * plus background work.
 *
 * Interview Usefulness
 * --------------------
 *
 * Good discussion.
 *
 * Rarely preferred implementation.
 *
 *
 * ------------------------------------------------------------
 * Improved
 * ------------------------------------------------------------
 *
 * Idea
 * ----
 *
 * Lazy refill on every request.
 *
 * Invariant
 * ---------
 *
 * State reconstructed before decision.
 *
 * Improvement
 * -----------
 *
 * Eliminates scheduler.
 *
 * Complexity
 * ----------
 *
 * O(1)
 *
 * Interview Usefulness
 * --------------------
 *
 * Excellent stepping stone.
 *
 *
 * ------------------------------------------------------------
 * Optimal (Interview Preferred)
 * ------------------------------------------------------------
 */
public class DesignTokenBucketRateLimiter {

    /**
     * Thread-safe lazy token bucket.
     */
    static final class TokenBucket {

        private final long capacity;

        private final double refillRatePerNano;

        private double currentTokens;

        private long lastRefillNanos;

        private final ReentrantLock lock = new ReentrantLock();

        TokenBucket(long capacity, double refillRatePerSecond) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("capacity must be positive");
            }

            if (refillRatePerSecond <= 0) {
                throw new IllegalArgumentException("refill rate must be positive");
            }

            this.capacity = capacity;
            this.refillRatePerNano = refillRatePerSecond / 1_000_000_000.0;
            this.currentTokens = capacity;
            this.lastRefillNanos = System.nanoTime();
        }

        /**
         * Attempts to consume exactly one token.
         */
        boolean allow() {

            lock.lock();

            try {

                refill();

                // Invariant: currentTokens reflects all elapsed time.

                if (currentTokens < 1.0) {
                    return false;
                }

                // Consume only after reconstruction.
                currentTokens--;

                return true;

            } finally {
                lock.unlock();
            }
        }

        /**
         * Restores bucket state from elapsed time.
         */
        private void refill() {

            long now = System.nanoTime();

            long elapsed = now - lastRefillNanos;

            if (elapsed <= 0) {
                return;
            }

            double generated = elapsed * refillRatePerNano;

            if (generated <= 0.0) {
                return;
            }

            // Invariant: bucket never exceeds capacity.
            currentTokens = Math.min(capacity, currentTokens + generated);

            lastRefillNanos = now;
        }

        /**
         * Attempts to consume multiple tokens atomically.
         */
        boolean allow(long permits) {

            if (permits <= 0) {
                throw new IllegalArgumentException("permits must be positive");
            }

            if (permits > capacity) {
                return false;
            }

            lock.lock();

            try {

                refill();

                // Invariant: state already reflects elapsed time.
                if (currentTokens < permits) {
                    return false;
                }

                currentTokens -= permits;

                return true;

            } finally {
                lock.unlock();
            }
        }

        /**
         * Returns current token estimate after reconstruction.
         */
        double availableTokens() {

            lock.lock();

            try {

                refill();

                return currentTokens;

            } finally {
                lock.unlock();
            }
        }

        /**
         * Time until a single permit becomes available.
         *
         * Returns:
         *
         * 0 if immediately available.
         */
        long nanosUntilNextPermit() {

            lock.lock();

            try {

                refill();

                if (currentTokens >= 1.0) {
                    return 0L;
                }

                double missing = 1.0 - currentTokens;

                return (long) Math.ceil(missing / refillRatePerNano);

            } finally {
                lock.unlock();
            }
        }

        long capacity() {
            return capacity;
        }
    }

/*
 * ============================================================
 * 🟣 INTERVIEW ARTICULATION
 * ============================================================
 *
 * Explain the Invariant
 * ---------------------
 *
 * Before every admission decision, I first reconstruct the
 * bucket using elapsed time.
 *
 * Therefore currentTokens always represents the exact number
 * of permits that should exist at this instant.
 *
 *
 * Explain the Discard Rule
 * ------------------------
 *
 * Unlike binary search there is no search space discard.
 *
 * The important transition is:
 *
 * reconstruct
 * →
 * clamp
 * →
 * consume
 *
 * Every request follows this identical state transition.
 *
 *
 * Correctness
 * -----------
 *
 * Since elapsed time uniquely determines how many tokens should
 * have appeared, reconstructing before every request produces
 * the same observable behavior as continuously generating tokens.
 *
 *
 * Termination
 * -----------
 *
 * Each request performs:
 *
 * • one timestamp read
 * • one arithmetic computation
 * • one clamp
 * • one comparison
 *
 * Hence O(1).
 *
 *
 * In-place Feasibility
 * --------------------
 *
 * Yes.
 *
 * Only constant additional state is maintained.
 *
 *
 * Streaming Feasibility
 * ---------------------
 *
 * Excellent.
 *
 * Requests are processed independently.
 *
 * No historical request list is required.
 *
 *
 * When NOT To Use
 * ---------------
 *
 * • strict FIFO fairness
 * • delayed scheduling
 * • exact execution ordering
 * • historical analytics
 *
 *
 * ============================================================
 * 🎯 INTERVIEW RECALL SHEET
 * ============================================================
 *
 * Trigger
 * -------
 *
 * "Allow bursts while maintaining average throughput."
 *
 *
 * Pattern
 * -------
 *
 * Lazy Time Reconstruction
 *
 *
 * Invariant
 * ---------
 *
 * Bucket state is reconstructed before every decision.
 *
 *
 * Search Target
 * -------------
 *
 * Not applicable.
 *
 * Maintain valid bucket state.
 *
 *
 * Discard Rule
 * ------------
 *
 * Clamp tokens to capacity.
 *
 *
 * Common Trap
 * -----------
 *
 * Forgetting to refill before checking availability.
 *
 *
 * Edge Cases
 * ----------
 *
 * • long idle duration
 * • refill exceeding capacity
 * • fractional refill
 * • zero elapsed time
 * • concurrent requests
 *
 *
 * One-Liner
 * ---------
 *
 * Rebuild state from elapsed time, clamp, then consume.
 *
 *
 * Re-derivation Cue
 * -----------------
 *
 * Ask:
 *
 * "If a background refill thread never existed,
 * what should the bucket contain right now?"
 *
 *
 * ============================================================
 * 🔄 VARIATIONS & TWEAKS
 * ============================================================
 *
 * Variant 1
 * ---------
 *
 * Variable request cost.
 *
 * Consume N tokens instead of one.
 *
 * Invariant unchanged.
 *
 *
 * Variant 2
 * ---------
 *
 * Per-user limiter.
 *
 * Store one bucket per key.
 *
 * Bucket invariant unchanged.
 *
 *
 * Variant 3
 * ---------
 *
 * Distributed bucket.
 *
 * Persist state inside Redis.
 *
 * Atomic update becomes the new invariant boundary.
 *
 *
 * Variant 4
 * ---------
 *
 * Hierarchical limiter.
 *
 * Global bucket
 * +
 * Tenant bucket
 * +
 * User bucket.
 *
 * Request succeeds only if every bucket succeeds.
 *
 *
 * Variant 5
 * ---------
 *
 * Fractional permits.
 *
 * Consume decimal weights.
 *
 * Double precision remains sufficient.
 *
 *
 * Pattern Break
 * -------------
 *
 * If regeneration depends on external events instead of elapsed
 * time, lazy reconstruction is no longer sufficient because time
 * alone cannot determine the missing state.
 *
 *
 * Why It Still Works
 * ------------------
 *
 * Every successful variation preserves exactly one invariant:
 *
 * state is reconstructed before consumption.
 *
 *
 * Why It Fails
 * ------------
 *
 * If requests modify state without reconstruction,
 * bucket correctness immediately diverges from elapsed time.
 *
 *
 * ============================================================
 * 🧠 MASTERY CHECKLIST
 * ============================================================
 *
 * Can you state the invariant?
 *
 * ✓ Bucket reconstructed before every decision.
 *
 * Can you explain the state?
 *
 * ✓ capacity
 * ✓ currentTokens
 * ✓ refillRate
 * ✓ lastRefillTime
 *
 * Can you explain the transition?
 *
 * ✓ elapsed
 * →
 * generated
 * →
 * clamp
 * →
 * consume
 *
 * Can you explain termination?
 *
 * ✓ Constant work.
 *
 * Can you explain naive failure?
 *
 * ✓ Scheduler unnecessary.
 *
 * ✓ Integer arithmetic loses precision.
 *
 * ✓ Missing clamp overfills bucket.
 *
 * Can you debug quickly?
 *
 * ✓ Check timestamp update.
 *
 * ✓ Check clamp.
 *
 * ✓ Check refill-before-consume ordering.
 *
 * ✓ Check concurrent access.
 *
 * Are you variant ready?
 *
 * ✓ Multiple permits.
 *
 * ✓ Per-user buckets.
 *
 * ✓ Distributed buckets.
 *
 * ✓ Weighted requests.
 *
 * Pattern Boundary
 * ----------------
 *
 * Token Bucket controls average throughput while allowing bursts.
 *
 * It is not a request scheduler and does not guarantee fairness.
 */

/**
 * ============================================================
 * ⚫ Pattern Mapping
 * ============================================================
 *
 * Problem
 * --------------------------------------------
 * API Gateway Rate Limiting
 * Pattern
 * Token Bucket
 * Reason
 * Burst tolerance with average rate guarantee.
 *
 * --------------------------------------------
 * Login Attempt Throttling
 * Pattern
 * Token Bucket
 * Reason
 * Legitimate retries remain smooth while attacks are slowed.
 *
 * --------------------------------------------
 * Messaging Systems
 * Pattern
 * Token Bucket
 * Reason
 * Temporary bursts are acceptable.
 *
 * --------------------------------------------
 * Streaming APIs
 * Pattern
 * Token Bucket
 * Reason
 * Natural burst handling without continuous background work.
 *
 * --------------------------------------------
 * CPU Scheduling
 * Pattern
 * Often Token Bucket
 * Reason
 * Resource credits regenerate over time.
 *
 *
 * ============================================================
 * 🔍 Debugging Guide
 * ============================================================
 *
 * Symptom
 * -------
 * Requests are rejected after a long idle period.
 *
 * Verify
 * ------
 * refill() is executed before every availability check.
 *
 *
 * Symptom
 * -------
 * Token count exceeds capacity.
 *
 * Verify
 * ------
 * Math.min(capacity, ...)
 * is always applied after generation.
 *
 *
 * Symptom
 * -------
 * Rate limiter feels slower than configured.
 *
 * Verify
 * ------
 * Time unit conversion.
 *
 * Seconds vs milliseconds vs nanoseconds.
 *
 *
 * Symptom
 * -------
 * Random negative token values.
 *
 * Verify
 * ------
 * Consume only after successful availability check.
 *
 *
 * Symptom
 * -------
 * Inconsistent concurrent behavior.
 *
 * Verify
 * ------
 * Entire reconstruction and consumption execute under the same
 * lock.
 *
 *
 * ============================================================
 * Frequently Asked Interview Questions
 * ============================================================
 *
 * Q.
 * Why use System.nanoTime() instead of currentTimeMillis()?
 *
 * A.
 * nanoTime() is monotonic and unaffected by wall clock changes.
 *
 *
 * Q.
 * Why store currentTokens as double?
 *
 * A.
 * Fractional regeneration prevents losing precision for high
 * refill rates or short elapsed durations.
 *
 *
 * Q.
 * Can this implementation allow bursts?
 *
 * A.
 * Yes.
 *
 * Idle time accumulates credits until capacity.
 *
 *
 * Q.
 * Why no scheduled executor?
 *
 * A.
 * Time completely determines reconstruction.
 *
 * No active maintenance thread is required.
 *
 *
 * Q.
 * Complexity?
 *
 * A.
 *
 * Time:
 * O(1)
 *
 * Space:
 * O(1)
 *
 *
 * ============================================================
 * Production Notes
 * ============================================================
 *
 * 1.
 * Replace the single JVM bucket with Redis for distributed
 * deployments.
 *
 * 2.
 * Use Lua scripting (or equivalent atomic primitive) so
 * reconstruction and consumption remain atomic.
 *
 * 3.
 * Prefer monotonic clocks whenever available.
 *
 * 4.
 * Emit metrics:
 *
 * - allowed requests
 * - rejected requests
 * - average available tokens
 * - refill latency
 *
 * 5.
 * Keep bucket state minimal:
 *
 * currentTokens
 * lastRefillTime
 *
 * Everything else is immutable configuration.
 *
 *
 * ============================================================
 * Complexity Summary
 * ============================================================
 *
 * Operation                Time     Space
 * ---------------------------------------
 * allow()                 O(1)      O(1)
 * allow(k)                O(1)      O(1)
 * availableTokens()       O(1)      O(1)
 * nanosUntilNextPermit()  O(1)      O(1)
 *
 *
 * ============================================================
 * Key Implementation Memory Hooks
 * ============================================================
 *
 * 1.
 * Read current monotonic time.
 *
 * 2.
 * Compute elapsed duration.
 *
 * 3.
 * Generate fractional tokens.
 *
 * 4.
 * Clamp to capacity.
 *
 * 5.
 * Advance accounting timestamp.
 *
 * 6.
 * Check availability.
 *
 * 7.
 * Consume.
 *
 * 8.
 * Return decision.
 *
 * If those eight steps are remembered,
 * the entire implementation can be reconstructed mechanically.
 */


public static void main(String[] args) {

    /*
     * Happy Path
     *
     * Bucket starts full.
     */
    TokenBucket bucket = new TokenBucket(5, 5);

    assert bucket.allow() : "Initial token should be available.";
    assert bucket.allow() : "Second token should be available.";
    assert bucket.allow() : "Third token should be available.";
    assert bucket.allow() : "Fourth token should be available.";
    assert bucket.allow() : "Fifth token should be available.";

    /*
     * Boundary:
     * Bucket is empty.
     */
    assert !bucket.allow() : "Sixth request must be rejected.";

    /*
     * Wait for refill.
     */
    sleepMillis(250);

    assert bucket.allow() : "A regenerated token should allow one request.";

    /*
     * Boundary:
     * Capacity clamp after long idle period.
     */
    sleepMillis(2_000);

    double available = bucket.availableTokens();

    assert available <= bucket.capacity()
            : "Bucket must never exceed capacity.";

    /*
     * Multiple permit success.
     */
    TokenBucket bucket2 = new TokenBucket(10, 10);

    assert bucket2.allow(4)
            : "Enough permits exist initially.";

    /*
     * Multiple permit rejection.
     */
    TokenBucket bucket3 = new TokenBucket(2, 1);

    assert bucket3.allow();
    assert bucket3.allow();

    assert !bucket3.allow()
            : "Bucket should now be empty.";

    assert !bucket3.allow(2)
            : "Cannot consume unavailable permits.";

    /*
     * Invalid request.
     */
    boolean threw = false;

    try {
        bucket.allow(0);
    } catch (IllegalArgumentException ex) {
        threw = true;
    }

    assert threw
            : "Non-positive permit requests should fail.";

    /*
     * Waiting time should never be negative.
     */
    long nanos = bucket3.nanosUntilNextPermit();

    assert nanos >= 0
            : "Wait duration cannot be negative.";

    /*
     * Idle reconstruction.
     */
    sleepMillis(1_500);

    assert bucket3.allow()
            : "Elapsed time should regenerate permits.";

    /*
     * Long idle should restore bucket to capacity.
     */
    TokenBucket bucket4 = new TokenBucket(3, 100);

    sleepMillis(500);

    assert bucket4.availableTokens() <= 3
            : "Capacity invariant must hold.";

    assert bucket4.availableTokens() >= 0
            : "Token count can never become negative.";

    /*
     * Fractional refill precision.
     */
    TokenBucket bucket5 = new TokenBucket(1, 2);

    assert bucket5.allow();

    sleepMillis(600);

    assert bucket5.allow()
            : "Fractional regeneration should eventually accumulate one token.";

    /*
     * Large request exceeding capacity.
     */
    TokenBucket bucket6 = new TokenBucket(5, 5);

    assert !bucket6.allow(6)
            : "A request larger than capacity is impossible.";

    /*
     * Immediate availability.
     */
    TokenBucket bucket7 = new TokenBucket(2, 2);

    assert bucket7.nanosUntilNextPermit() == 0
            : "Full bucket should require no waiting.";

    System.out.println("All assertions passed.");
}

    private static void sleepMillis(long millis) {

        try {

            Thread.sleep(millis);

        } catch (InterruptedException ex) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(ex);
        }
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/
