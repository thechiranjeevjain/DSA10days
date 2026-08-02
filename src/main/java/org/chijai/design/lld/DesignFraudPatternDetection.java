package org.chijai.design.lld;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * =============================================================================
 * Design Fraud Pattern Detection
 * =============================================================================
 *
 * Difficulty:
 * Hard
 *
 * Tags:
 * Design, HashMap, Sliding Window, Queue, Streaming, Time Series,
 * Rate Limiting, Fraud Detection, System Design Foundations
 *
 * Problem Description
 * -------------------
 * Design an in-memory fraud pattern detection engine capable of processing a
 * continuous stream of transactions.
 *
 * Every incoming transaction contains:
 *
 *  - transactionId
 *  - userId
 *  - amount
 *  - merchantId
 *  - timestamp
 *
 * The detector should support multiple fraud rules.
 *
 * Initially implement:
 *
 * Rule 1:
 * More than K transactions within W minutes.
 *
 * Rule 2:
 * More than X amount spent within W minutes.
 *
 * Rule 3:
 * Same merchant repeated N consecutive times.
 *
 * Rule 4:
 * Duplicate transaction id.
 *
 * The detector should be designed so that future rules can be added without
 * modifying existing rule implementations.
 *
 * Return every triggered fraud alert.
 *
 * Constraints
 * -----------
 * - Transactions arrive one by one.
 * - Processing should be online.
 * - Memory should not grow forever.
 * - Old state must naturally expire.
 * - Each rule maintains only the minimum state required.
 *
 * Representative Example
 * ----------------------
 *
 * K = 3
 * Window = 5 minutes
 *
 * t1
 * t2
 * t3
 * t4
 *
 * Fourth transaction inside same window triggers:
 *
 * TOO_MANY_TRANSACTIONS
 *
 * Example 2
 *
 * Amount window:
 *
 * 100
 * 200
 * 500
 * 800
 *
 * Threshold = 1200
 *
 * Last transaction causes total = 1600
 *
 * Trigger:
 *
 * HIGH_SPENDING
 *
 * Example 3
 *
 * Amazon
 * Amazon
 * Amazon
 *
 * N = 3
 *
 * Trigger:
 *
 * SAME_MERCHANT_REPEATED
 *
 * Example 4
 *
 * tx-100
 * tx-101
 * tx-100
 *
 * Trigger:
 *
 * DUPLICATE_TRANSACTION
 *
 * Official Reference
 * ------------------
 * Inspired by common fraud detection interview questions.
 *
 * https://leetcode.com/discuss/interview-question/system-design
 *
 * =============================================================================
 * 🔵 CORE PATTERN OVERVIEW
 * =============================================================================
 *
 * Pattern
 * -------
 * Streaming Stateful Rule Evaluation
 *
 * Archetype
 * ---------
 * Each incoming event updates a tiny state machine.
 *
 * Core Invariant
 * --------------
 * Every rule owns exactly the state required to decide whether the newest
 * transaction violates that rule.
 *
 * No rule depends on another rule.
 *
 * Why It Works
 * ------------
 * Instead of repeatedly scanning historical transactions, each rule preserves
 * just enough information so every new event can be evaluated incrementally.
 *
 * Recognition Signals
 * -------------------
 * - Infinite event stream
 * - Online processing
 * - Low latency
 * - Sliding time windows
 * - Pattern recognition
 * - Event-by-event updates
 *
 * When To Use
 * -----------
 * - Fraud detection
 * - Monitoring
 * - Alerting
 * - Streaming analytics
 * - Rate limiting
 * - Security pipelines
 *
 * When NOT To Use
 * ---------------
 * - Batch analytics
 * - Full historical reporting
 * - Offline ML feature engineering
 *
 * Comparison
 * ----------
 *
 * Sliding Window
 *     focuses on one window statistic.
 *
 * State Machine
 *     focuses on transitions.
 *
 * Fraud Detection
 *     combines several state machines and sliding windows simultaneously.
 *
 * =============================================================================
 * 🟢 MENTAL MODEL & INVARIANTS
 * =============================================================================
 *
 * Mental Model
 * ------------
 *
 * Imagine each fraud rule owns a tiny notebook.
 *
 * Every arriving transaction updates only that notebook.
 *
 * The notebook never stores unnecessary history.
 *
 * It continuously forgets expired information.
 *
 * Therefore every rule remains bounded in memory.
 *
 * -------------------------------------------------------------------------
 * Invariant 1
 * -------------------------------------------------------------------------
 *
 * Every rule owns its own isolated state.
 *
 * Violating this causes coupling between rules.
 *
 * -------------------------------------------------------------------------
 * Invariant 2
 * -------------------------------------------------------------------------
 *
 * Sliding-window rules never retain expired events.
 *
 * Before evaluating a new transaction:
 *
 *     remove expired
 *     evaluate
 *     insert newest
 *
 * Never evaluate using stale history.
 *
 * -------------------------------------------------------------------------
 * Invariant 3
 * -------------------------------------------------------------------------
 *
 * Duplicate detection stores only identifiers already observed.
 *
 * Membership check remains O(1).
 *
 * -------------------------------------------------------------------------
 * Invariant 4
 * -------------------------------------------------------------------------
 *
 * Consecutive merchant detection only needs:
 *
 * current merchant
 * current streak
 *
 * Entire history is unnecessary.
 *
 * -------------------------------------------------------------------------
 * Variable Meanings
 * -------------------------------------------------------------------------
 *
 * window
 *      active transactions
 *
 * runningAmount
 *      sum(window)
 *
 * streak
 *      current consecutive merchant count
 *
 * seenTransactions
 *      all unique ids observed
 *
 * -------------------------------------------------------------------------
 * Allowed State Transitions
 * -------------------------------------------------------------------------
 *
 * Receive transaction
 *        ↓
 *
 * Expire stale state
 *        ↓
 *
 * Update rule state
 *        ↓
 *
 * Evaluate invariant
 *        ↓
 *
 * Emit alerts
 *
 * -------------------------------------------------------------------------
 * Forbidden Moves
 * -------------------------------------------------------------------------
 *
 * Scanning every historical transaction.
 *
 * Recomputing sums from scratch.
 *
 * Sharing mutable state across rules.
 *
 * Keeping expired transactions forever.
 *
 * -------------------------------------------------------------------------
 * Termination
 * -------------------------------------------------------------------------
 *
 * Every transaction is processed exactly once.
 *
 * Every window element is inserted once.
 *
 * Every window element is removed once.
 *
 * Hence amortized linear movement.
 *
 * -------------------------------------------------------------------------
 * Correctness Intuition
 * -------------------------------------------------------------------------
 *
 * Since each rule maintains exactly the minimum sufficient state and removes
 * obsolete information before evaluation, every decision is made using the
 * correct active search space.
 *
 * -------------------------------------------------------------------------
 * Why Naive Solutions Fail
 * -------------------------------------------------------------------------
 *
 * Naive solution:
 *
 * For every transaction:
 *
 *     iterate over entire history
 *
 * Complexity quickly becomes O(N²).
 *
 * Memory also grows indefinitely.
 *
 * =============================================================================
 * 🔴 WHY WRONG SOLUTIONS FAIL
 * =============================================================================
 *
 * Mistake 1
 * ---------
 * Never removing expired transactions.
 *
 * Why It Looks Correct
 * --------------------
 * Counts always increase.
 *
 * Violated Invariant
 * ------------------
 * Sliding window contains expired events.
 *
 * Counterexample
 * --------------
 *
 * Window = 5 minutes
 *
 * Yesterday's transaction still contributes today.
 *
 * ---------------------------------------------------------
 *
 * Mistake 2
 *
 * Recomputing window sums every arrival.
 *
 * Looks simple.
 *
 * Violated Invariant
 *
 * Running aggregate already exists.
 *
 * Complexity becomes O(N²).
 *
 * ---------------------------------------------------------
 *
 * Mistake 3
 *
 * One giant if-else for every fraud rule.
 *
 * Initially appears manageable.
 *
 * Violated Invariant
 *
 * Rules become tightly coupled.
 *
 * Adding one rule risks breaking others.
 *
 * ---------------------------------------------------------
 *
 * Mistake 4
 *
 * Store entire transaction history forever.
 *
 * Violated Invariant
 *
 * State should be bounded whenever possible.
 *
 * =============================================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * =============================================================================
 *
 * Mechanical typing order
 * -----------------------
 *
 * 1. Transaction model
 *
 * 2. Alert model
 *
 * 3. FraudRule interface
 *
 * 4. Detection engine
 *
 * 5. Individual rule implementations
 *
 *      initialize state
 *
 *      expire old entries
 *
 *      update state
 *
 *      evaluate invariant
 *
 *      emit alerts
 *
 * 6. Tests
 *
 * =============================================================================
 * ULTRA-COMPACT PSEUDOCODE
 * =============================================================================
 *
 * initialize rules
 *
 * for transaction
 *
 *      for each rule
 *
 *          expire
 *
 *          update
 *
 *          evaluate
 *
 * return alerts
 *
 * =============================================================================
 * 🟣 SOLUTION CLASSES
 * =============================================================================
 */
public class DesignFraudPatternDetection {

    /**
     * =========================================================================
     * Brute Force
     * =========================================================================
     *
     * Idea
     * ----
     * Store every transaction forever.
     *
     * Every new transaction scans complete history.
     *
     * Invariant
     * ---------
     * None.
     *
     * Limitation
     * ----------
     * O(N²)
     *
     * Interview Usefulness
     * --------------------
     * Only useful for explaining why incremental state is required.
     */

    static class BruteForceDescription {
    }

    /**
     * =========================================================================
     * Improved
     * =========================================================================
     *
     * Maintain sliding windows independently.
     *
     * Each rule owns its own data.
     */

    static class ImprovedDescription {
    }

    /**
     * =========================================================================
     * Optimal (Interview Preferred)
     * =========================================================================
     *
     * Idea
     * ----
     * Strategy Pattern.
     *
     * Every fraud rule is an independent state machine.
     *
     * Engine simply forwards transactions.
     *
     * New rules can be added without changing engine logic.
     */

    enum FraudType {
        TOO_MANY_TRANSACTIONS,
        HIGH_SPENDING,
        SAME_MERCHANT_REPEATED,
        DUPLICATE_TRANSACTION
    }

    static class Transaction {

        final String transactionId;
        final String userId;
        final double amount;
        final String merchantId;
        final Instant timestamp;

        Transaction(
                String transactionId,
                String userId,
                double amount,
                String merchantId,
                Instant timestamp
        ) {
            this.transactionId = transactionId;
            this.userId = userId;
            this.amount = amount;
            this.merchantId = merchantId;
            this.timestamp = timestamp;
        }
    }

    static class FraudAlert {

        final FraudType type;
        final String message;
        final Transaction transaction;

        FraudAlert(FraudType type,
                   String message,
                   Transaction transaction) {

            this.type = type;
            this.message = message;
            this.transaction = transaction;
        }

        @Override
        public String toString() {
            return type + " : " + message;
        }
    }

    interface FraudRule {

        List<FraudAlert> evaluate(Transaction transaction);

    }

    static class FraudDetectionEngine {

        private final List<FraudRule> rules = new ArrayList<>();

        FraudDetectionEngine(List<FraudRule> rules) {
            this.rules.addAll(rules);
        }

        List<FraudAlert> process(Transaction transaction) {

            List<FraudAlert> alerts = new ArrayList<>();

            for (FraudRule rule : rules) {

                // Invariant:
                // Every rule evaluates independently.
                alerts.addAll(rule.evaluate(transaction));
            }

            return alerts;
        }
    }

    static class TooManyTransactionsRule implements FraudRule {

        private final int limit;

        private final Duration window;

        private final Map<String, Deque<Transaction>> history = new HashMap<>();

        TooManyTransactionsRule(int limit,
                                Duration window) {

            this.limit = limit;
            this.window = window;
        }

        @Override
        public List<FraudAlert> evaluate(Transaction transaction) {

            List<FraudAlert> alerts = new ArrayList<>();

            Deque<Transaction> queue =
                    history.computeIfAbsent(
                            transaction.userId,
                            k -> new ArrayDeque<>()
                    );

            Instant cutoff =
                    transaction.timestamp.minus(window);

            // Invariant:
            // Queue contains only active window.

            while (!queue.isEmpty()
                    && queue.peekFirst().timestamp.isBefore(cutoff)) {

                queue.removeFirst();
            }

            queue.addLast(transaction);

            if (queue.size() > limit) {

                alerts.add(
                        new FraudAlert(
                                FraudType.TOO_MANY_TRANSACTIONS,
                                "Too many recent transactions.",
                                transaction
                        )
                );
            }

            return alerts;
        }
    }

    static class HighSpendingRule implements FraudRule {

        private final double threshold;

        private final Duration window;

        private final Map<String, Deque<Transaction>> history = new HashMap<>();

        private final Map<String, Double> runningTotals = new HashMap<>();

        HighSpendingRule(double threshold,
                         Duration window) {

            this.threshold = threshold;
            this.window = window;
        }

        @Override
        public List<FraudAlert> evaluate(Transaction transaction) {

            List<FraudAlert> alerts = new ArrayList<>();

            Deque<Transaction> queue =
                    history.computeIfAbsent(
                            transaction.userId,
                            k -> new ArrayDeque<>()
                    );

            runningTotals.putIfAbsent(transaction.userId, 0.0);

            Instant cutoff = transaction.timestamp.minus(window);

            // Invariant:
            // Queue and running total always describe the same active window.

            while (!queue.isEmpty()
                    && queue.peekFirst().timestamp.isBefore(cutoff)) {

                Transaction expired = queue.removeFirst();

                runningTotals.put(
                        transaction.userId,
                        runningTotals.get(transaction.userId) - expired.amount
                );
            }

            queue.addLast(transaction);

            runningTotals.put(
                    transaction.userId,
                    runningTotals.get(transaction.userId) + transaction.amount
            );

            double total = runningTotals.get(transaction.userId);

            if (total > threshold) {

                alerts.add(
                        new FraudAlert(
                                FraudType.HIGH_SPENDING,
                                String.format(
                                        Locale.US,
                                        "Window spending %.2f exceeds %.2f",
                                        total,
                                        threshold
                                ),
                                transaction
                        )
                );
            }

            return alerts;
        }
    }

    static class SameMerchantRepeatedRule implements FraudRule {

        private static class MerchantState {

            String merchantId;

            int streak;
        }

        private final int repeatThreshold;

        private final Map<String, MerchantState> states = new HashMap<>();

        SameMerchantRepeatedRule(int repeatThreshold) {

            this.repeatThreshold = repeatThreshold;
        }

        @Override
        public List<FraudAlert> evaluate(Transaction transaction) {

            List<FraudAlert> alerts = new ArrayList<>();

            MerchantState state =
                    states.computeIfAbsent(
                            transaction.userId,
                            k -> new MerchantState()
                    );

            // Invariant:
            // Streak represents consecutive transactions for exactly one
            // merchant.

            if (Objects.equals(state.merchantId, transaction.merchantId)) {

                state.streak++;

            } else {

                state.merchantId = transaction.merchantId;
                state.streak = 1;
            }

            if (state.streak >= repeatThreshold) {

                alerts.add(
                        new FraudAlert(
                                FraudType.SAME_MERCHANT_REPEATED,
                                "Repeated merchant pattern detected.",
                                transaction
                        )
                );
            }

            return alerts;
        }
    }

    static class DuplicateTransactionRule implements FraudRule {

        private final Set<String> seenTransactionIds = new HashSet<>();

        @Override
        public List<FraudAlert> evaluate(Transaction transaction) {

            List<FraudAlert> alerts = new ArrayList<>();

            // Invariant:
            // Every accepted identifier appears only once.

            if (!seenTransactionIds.add(transaction.transactionId)) {

                alerts.add(
                        new FraudAlert(
                                FraudType.DUPLICATE_TRANSACTION,
                                "Duplicate transaction id detected.",
                                transaction
                        )
                );
            }

            return alerts;
        }
    }

/**
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * Explain the design verbally
 * ---------------------------
 *
 * "This problem is fundamentally a streaming state-management problem
 * rather than a search problem.
 *
 * I model each fraud pattern as an independent strategy implementing the
 * same FraudRule interface.
 *
 * Every rule owns only the minimum state needed to preserve its invariant.
 *
 * Sliding-window rules maintain queues that always contain only active
 * transactions.
 *
 * Aggregate statistics are updated incrementally instead of recomputed.
 *
 * Duplicate detection requires only membership state.
 *
 * Consecutive-pattern detection requires only the previous merchant and the
 * current streak.
 *
 * The engine itself never knows any fraud logic.
 *
 * Therefore adding another fraud rule follows the Open/Closed Principle:
 * create another FraudRule implementation and register it."
 *
 * -------------------------------------------------------------------------
 *
 * Invariant
 * ---------
 *
 * Every rule owns complete responsibility for preserving exactly one
 * correctness invariant.
 *
 * -------------------------------------------------------------------------
 *
 * Discard Rule
 * ------------
 *
 * Before evaluating a sliding-window rule, all expired events are removed.
 *
 * -------------------------------------------------------------------------
 *
 * Correctness
 * -----------
 *
 * Because obsolete state is removed before evaluation and every update is
 * incremental, each decision reflects exactly the active search space.
 *
 * -------------------------------------------------------------------------
 *
 * Termination
 * -----------
 *
 * Each transaction enters and leaves every queue at most once.
 *
 * Hence amortized O(1) update per event.
 *
 * -------------------------------------------------------------------------
 *
 * In-place Feasibility
 * --------------------
 *
 * Not applicable.
 *
 * Stateful streaming inherently requires persistent auxiliary state.
 *
 * -------------------------------------------------------------------------
 *
 * Streaming Feasibility
 * ---------------------
 *
 * This design is naturally streaming.
 *
 * Historical rescans are never required.
 *
 * -------------------------------------------------------------------------
 *
 * When NOT To Use
 * ---------------
 *
 * If fraud logic depends on global historical analytics, graph mining,
 * offline feature engineering, or ML inference over months of history,
 * this lightweight in-memory architecture is insufficient.
 *
 * Those workloads require distributed storage and analytical pipelines.
 */

/**
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================================
 *
 * Trigger
 * -------
 * Online fraud detection.
 *
 * Pattern
 * -------
 * Independent streaming state machines.
 *
 * Search Space
 * ------------
 * Only active state owned by each rule.
 *
 * Invariant
 * ---------
 * Every rule stores only sufficient state.
 *
 * Discard Rule
 * ------------
 * Expire stale events before evaluation.
 *
 * Common Trap
 * -----------
 * Never recompute entire history.
 *
 * Edge Cases
 * ----------
 * Empty stream.
 * Duplicate identifiers.
 * Exact threshold.
 * Window expiration boundary.
 *
 * One-liner
 * ---------
 * "Incremental state beats historical rescanning."
 *
 * Re-derivation Cue
 * -----------------
 * Ask:
 *
 * "What is the smallest state required to answer the next event?"
 */

/**
 * =========================================================================
 * 🔄 VARIATIONS & TWEAKS
 * =========================================================================
 *
 * Every variation below preserves the same architectural invariant:
 *
 *      Incoming Event
 *             ↓
 *     Update Local State
 *             ↓
 *      Evaluate Rule
 *             ↓
 *        Emit Alert
 *
 * Only the maintained state changes.
 *
 * -------------------------------------------------------------------------
 * Variation 1
 * Velocity Between Countries
 * -------------------------------------------------------------------------
 *
 * Fraud Pattern
 * -------------
 * Same user appears in two geographically impossible locations within a
 * short time.
 *
 * Required State
 * --------------
 *
 * lastCountry
 * lastTimestamp
 *
 * Transition
 * ----------
 *
 * Compute travel time.
 *
 * If impossible:
 *
 *      emit alert.
 *
 * Pattern Still Works
 * -------------------
 *
 * Only previous observation is needed.
 *
 * O(1) memory.
 *
 * -------------------------------------------------------------------------
 * Variation 2
 * Multiple Cards Sharing Device
 * -------------------------------------------------------------------------
 *
 * Maintain:
 *
 * deviceId
 * →
 * distinct cards
 *
 * Alert once threshold exceeded.
 *
 * -------------------------------------------------------------------------
 * Variation 3
 * Many Merchants In Very Short Time
 * -------------------------------------------------------------------------
 *
 * Sliding window.
 *
 * Store:
 *
 * queue
 * +
 * merchant frequency map.
 *
 * Expire oldest entries.
 *
 * Maintain distinct merchant count incrementally.
 *
 * -------------------------------------------------------------------------
 * Variation 4
 * Impossible Spending Velocity
 * -------------------------------------------------------------------------
 *
 * Sliding amount window.
 *
 * Same invariant as HighSpendingRule.
 *
 * Only alert condition changes.
 *
 * -------------------------------------------------------------------------
 * Variation 5
 * Login Followed By Payment
 * -------------------------------------------------------------------------
 *
 * Maintain latest login timestamp.
 *
 * Reject payment before successful login.
 *
 * Simple finite state machine.
 *
 * -------------------------------------------------------------------------
 * Variation 6
 * Merchant Blacklist
 * -------------------------------------------------------------------------
 *
 * Maintain:
 *
 * Set<String> blacklistedMerchants
 *
 * Membership lookup:
 *
 * O(1)
 *
 * -------------------------------------------------------------------------
 * Variation 7
 * Daily Spending Limit
 * -------------------------------------------------------------------------
 *
 * Key:
 *
 * userId + LocalDate
 *
 * Maintain running total.
 *
 * Reset automatically next day.
 *
 * -------------------------------------------------------------------------
 * Variation 8
 * New Device Detection
 * -------------------------------------------------------------------------
 *
 * Maintain:
 *
 * user →
 * known devices
 *
 * Unknown device:
 *
 * Raise risk score.
 *
 * -------------------------------------------------------------------------
 * Variation 9
 * Risk Scoring Instead Of Binary Rules
 * -------------------------------------------------------------------------
 *
 * Instead of emitting immediately:
 *
 * Every rule contributes points.
 *
 * Example
 *
 * Duplicate
 * +80
 *
 * High spending
 * +40
 *
 * New country
 * +30
 *
 * Final score > threshold
 *
 * Emit alert.
 *
 * Invariant
 * ---------
 *
 * Independent rule evaluation remains unchanged.
 *
 * Aggregation layer changes only.
 *
 * -------------------------------------------------------------------------
 * Variation 10
 * Distributed Deployment
 * -------------------------------------------------------------------------
 *
 * Replace local HashMaps with:
 *
 * Redis
 *
 * Hazelcast
 *
 * Aerospike
 *
 * DynamoDB
 *
 * Invariant remains identical.
 *
 * Only storage changes.
 */

/**
 * =========================================================================
 * 🧠 MASTERY CHECKLIST
 * =========================================================================
 *
 * □ What is the Pattern?
 *
 * Streaming state machines.
 *
 * ---------------------------------------------------------
 *
 * □ What is the invariant?
 *
 * Every rule owns exactly the minimum sufficient state.
 *
 * ---------------------------------------------------------
 *
 * □ What is the Search Space?
 *
 * Active state maintained by each rule.
 *
 * ---------------------------------------------------------
 *
 * □ What is discarded?
 *
 * Expired events.
 *
 * ---------------------------------------------------------
 *
 * □ Why does brute force fail?
 *
 * Repeated historical scans.
 *
 * ---------------------------------------------------------
 *
 * □ How do we terminate?
 *
 * Every event processed once.
 *
 * ---------------------------------------------------------
 *
 * □ Complexity?
 *
 * Average:
 *
 * O(number of rules)
 *
 * per transaction.
 *
 * ---------------------------------------------------------
 *
 * □ Debugging Question
 *
 * Does every state variable represent only active information?
 *
 * ---------------------------------------------------------
 *
 * □ Pattern Boundary
 *
 * Use for streaming.
 *
 * Not for offline analytics.
 *
 * ---------------------------------------------------------
 *
 * □ Variant Ready?
 *
 * Can a new fraud rule be added without modifying the engine?
 *
 * If yes,
 *
 * architecture remains Open/Closed compliant.
 */

/**
 * =========================================================================
 * ⚫ PATTERN MAPPING
 * =========================================================================
 *
 * Interview Problem
 * ----------------------------------------
 * Rate Limiter
 * Pattern
 * Sliding Window
 *
 * ----------------------------------------
 * API Abuse Detection
 * Pattern
 * Stateful Streaming
 *
 * ----------------------------------------
 * Login Monitoring
 * Pattern
 * Finite State Machine
 *
 * ----------------------------------------
 * Session Timeout
 * Pattern
 * Expiring State
 *
 * ----------------------------------------
 * Fraud Detection
 * Pattern
 * Independent Rule Strategies
 *
 * ----------------------------------------
 * CEP (Complex Event Processing)
 * Pattern
 * Event Pipelines
 */

    /**
     * =========================================================================
     * 🔍 DEBUGGING PLAYBOOK
     * =========================================================================
     *
     * When a fraud alert is incorrect, inspect the invariant rather than the
     * symptom.
     *
     * -------------------------------------------------------------------------
     * Case 1
     * -------------------------------------------------------------------------
     *
     * Symptom
     * -------
     * TooManyTransactionsRule fires too often.
     *
     * Checklist
     * ---------
     *
     * □ Are expired transactions removed before insertion?
     *
     * □ Is the cutoff timestamp computed correctly?
     *
     * □ Is the comparison strictly before() or should equality expire too?
     *
     * □ Is the queue user-specific?
     *
     * -------------------------------------------------------------------------
     * Case 2
     * -------------------------------------------------------------------------
     *
     * High spending is never detected.
     *
     * Checklist
     * ---------
     *
     * □ Is runningTotal updated after expiration?
     *
     * □ Is every removed transaction subtracted exactly once?
     *
     * □ Is every inserted transaction added exactly once?
     *
     * □ Does runningTotal equal the queue sum?
     *
     * -------------------------------------------------------------------------
     * Case 3
     * -------------------------------------------------------------------------
     *
     * Merchant streak resets unexpectedly.
     *
     * Checklist
     * ---------
     *
     * □ Is state keyed by user?
     *
     * □ Is merchant comparison using Objects.equals()?
     *
     * □ Is streak initialized to one?
     *
     * -------------------------------------------------------------------------
     * Case 4
     * -------------------------------------------------------------------------
     *
     * Duplicate detection misses duplicates.
     *
     * Checklist
     * ---------
     *
     * □ Is transactionId globally unique?
     *
     * □ Is HashSet shared across transactions?
     *
     * □ Is add() result interpreted correctly?
     *
     * -------------------------------------------------------------------------
     * Universal Debugging Invariant
     * -----------------------------
     *
     * At every moment,
     *
     * queue
     *
     * runningTotal
     *
     * counters
     *
     * streak
     *
     * and identifiers
     *
     * must all describe exactly the same logical state.
     */

    /**
     * =========================================================================
     * ⚫ COMPLEXITY SUMMARY
     * =========================================================================
     *
     * TooManyTransactionsRule
     *
     * Time
     * ----
     * Amortized O(1)
     *
     * Space
     * -----
     * O(active window)
     *
     * -------------------------------------------------------------------------
     *
     * HighSpendingRule
     *
     * Time
     * ----
     * Amortized O(1)
     *
     * Space
     * -----
     * O(active window)
     *
     * -------------------------------------------------------------------------
     *
     * SameMerchantRepeatedRule
     *
     * Time
     * ----
     * O(1)
     *
     * Space
     * -----
     * O(number of users)
     *
     * -------------------------------------------------------------------------
     *
     * DuplicateTransactionRule
     *
     * Time
     * ----
     * O(1) average
     *
     * Space
     * -----
     * O(number of unique transaction ids)
     *
     * -------------------------------------------------------------------------
     *
     * Entire Engine
     *
     * Time
     * ----
     * O(R)
     *
     * where R = number of registered rules.
     *
     * Since R is typically very small, latency remains nearly constant.
     */

    /**
     * =========================================================================
     * ⚫ EXTENSIBILITY GUIDE
     * =========================================================================
     *
     * To implement a new fraud rule:
     *
     * Step 1
     * ------
     *
     * Decide the minimum sufficient state.
     *
     * Step 2
     * ------
     *
     * Define the invariant.
     *
     * Step 3
     * ------
     *
     * Implement FraudRule.
     *
     * Step 4
     * ------
     *
     * Update state incrementally.
     *
     * Step 5
     * ------
     *
     * Emit alerts only when the invariant is violated.
     *
     * Example Skeleton
     *
     * class NewRule implements FraudRule {
     *
     *     state...
     *
     *     evaluate(transaction)
     *
     *          expire old state
     *
     *          update state
     *
     *          check invariant
     *
     *          return alerts
     * }
     *
     * The engine never changes.
     */

    private static Transaction tx(
            String id,
            String user,
            double amount,
            String merchant,
            Instant time
    ) {

        return new Transaction(
                id,
                user,
                amount,
                merchant,
                time
        );
    }

    public static void main(String[] args) {

        FraudDetectionEngine engine = new FraudDetectionEngine(
                List.of(
                        new TooManyTransactionsRule(
                                3,
                                Duration.ofMinutes(5)
                        ),
                        new HighSpendingRule(
                                1_200.0,
                                Duration.ofMinutes(5)
                        ),
                        new SameMerchantRepeatedRule(3),
                        new DuplicateTransactionRule()
                )
        );

        Instant now = Instant.now();

        /*
         * Happy Path
         *
         * No fraud expected.
         */
        List<FraudAlert> alerts = engine.process(
                tx(
                        "TX-1",
                        "alice",
                        100,
                        "Amazon",
                        now
                )
        );

        assert alerts.isEmpty()
                : "Single transaction should not trigger fraud.";

        /*
         * Boundary:
         * Fourth transaction inside window.
         */
        engine.process(tx("TX-2", "bob", 100, "Flipkart", now.plusSeconds(10)));
        engine.process(tx("TX-3", "bob", 100, "Flipkart", now.plusSeconds(20)));

        alerts = engine.process(
                tx(
                        "TX-4",
                        "bob",
                        100,
                        "Flipkart",
                        now.plusSeconds(30)
                )
        );

        assert alerts.stream().anyMatch(
                a -> a.type == FraudType.TOO_MANY_TRANSACTIONS
        ) : "Expected transaction-count alert.";

        /*
         * Spending threshold.
         */
        engine.process(tx("TX-10", "charlie", 500, "Apple", now));
        engine.process(tx("TX-11", "charlie", 400, "Apple", now.plusSeconds(20)));

        alerts = engine.process(
                tx(
                        "TX-12",
                        "charlie",
                        400,
                        "Apple",
                        now.plusSeconds(30)
                )
        );

        assert alerts.stream().anyMatch(
                a -> a.type == FraudType.HIGH_SPENDING
        ) : "Expected high-spending alert.";

        /*
         * Consecutive merchant pattern.
         */
        engine.process(tx("TX-20", "david", 50, "Steam", now));
        engine.process(tx("TX-21", "david", 60, "Steam", now.plusSeconds(5)));

        alerts = engine.process(
                tx(
                        "TX-22",
                        "david",
                        70,
                        "Steam",
                        now.plusSeconds(10)
                )
        );

        assert alerts.stream().anyMatch(
                a -> a.type == FraudType.SAME_MERCHANT_REPEATED
        ) : "Expected repeated merchant alert.";

        /*
         * Duplicate transaction identifier.
         */
        engine.process(tx("TX-30", "eve", 10, "Uber", now));

        alerts = engine.process(
                tx(
                        "TX-30",
                        "eve",
                        10,
                        "Uber",
                        now.plusSeconds(1)
                )
        );

        assert alerts.stream().anyMatch(
                a -> a.type == FraudType.DUPLICATE_TRANSACTION
        ) : "Expected duplicate transaction alert.";

        /*
         * Window expiration.
         *
         * Earlier transactions should naturally expire.
         */
        FraudDetectionEngine expirationEngine = new FraudDetectionEngine(
                List.of(
                        new TooManyTransactionsRule(
                                2,
                                Duration.ofMinutes(1)
                        )
                )
        );

        expirationEngine.process(
                tx(
                        "A1",
                        "user",
                        10,
                        "M",
                        now
                )
        );

        expirationEngine.process(
                tx(
                        "A2",
                        "user",
                        10,
                        "M",
                        now.plusSeconds(20)
                )
        );

        alerts = expirationEngine.process(
                tx(
                        "A3",
                        "user",
                        10,
                        "M",
                        now.plusSeconds(121)
                )
        );

        assert alerts.isEmpty()
                : "Expired window must not contribute to active count.";

        /*
         * Different users maintain isolated state.
         */
        FraudDetectionEngine isolationEngine = new FraudDetectionEngine(
                List.of(
                        new TooManyTransactionsRule(
                                2,
                                Duration.ofMinutes(5)
                        )
                )
        );

        isolationEngine.process(tx("U1", "u1", 5, "M", now));
        isolationEngine.process(tx("U2", "u2", 5, "M", now));
        isolationEngine.process(tx("U3", "u1", 5, "M", now.plusSeconds(5)));

        alerts = isolationEngine.process(
                tx(
                        "U4",
                        "u2",
                        5,
                        "M",
                        now.plusSeconds(5)
                )
        );

        assert alerts.isEmpty()
                : "User state must remain isolated.";

        /*
         * Exact threshold should not trigger because implementation
         * intentionally checks 'greater than'.
         */
        FraudDetectionEngine thresholdEngine = new FraudDetectionEngine(
                List.of(
                        new HighSpendingRule(
                                100.0,
                                Duration.ofMinutes(5)
                        )
                )
        );

        alerts = thresholdEngine.process(
                tx(
                        "S1",
                        "spender",
                        100.0,
                        "Store",
                        now
                )
        );

        assert alerts.isEmpty()
                : "Exact threshold should not trigger.";

        System.out.println("All assertions passed.");
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/

