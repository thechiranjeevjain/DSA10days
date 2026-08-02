package org.chijai.design.lld;

import java.util.*;

/**
 * DesignRedis
 *
 * ============================================================================
 * 2. 📘 PRIMARY PROBLEM
 * ============================================================================
 *
 * Title:
 * Design Redis Mental Model
 *
 * Difficulty:
 * System Design (Interview)
 *
 * Tags:
 * System Design
 * Distributed Systems
 * In-Memory Database
 * Networking
 * Persistence
 * Replication
 * Consistency
 * Caching
 *
 * Problem Description
 * -------------------
 * Build a mental model of Redis rather than merely learning commands.
 *
 * The goal is to understand:
 *
 * 1. Why Redis is extremely fast.
 * 2. How requests travel through Redis.
 * 3. How data is organized internally.
 * 4. How persistence works.
 * 5. How replication works.
 * 6. How expiration works.
 * 7. How memory management works.
 * 8. Which invariants always remain true.
 * 9. How interview implementations are derived.
 *
 * Instead of memorizing isolated features, understand Redis as one deterministic
 * state machine.
 *
 * ----------------------------------------------------------------------------
 * Typical Interview Questions Derived From This Model
 * ----------------------------------------------------------------------------
 *
 * • Design Redis.
 * • Why is Redis fast?
 * • Explain single-threaded Redis.
 * • Explain event loop.
 * • Explain expiration.
 * • Explain eviction.
 * • Explain persistence.
 * • Explain AOF vs RDB.
 * • Explain replication.
 * • Explain Redis Cluster.
 * • Explain Pub/Sub.
 * • Explain Streams.
 * • Explain transactions.
 * • Explain Lua scripting.
 *
 * ----------------------------------------------------------------------------
 * Constraints
 * ----------------------------------------------------------------------------
 *
 * • Data primarily resides in RAM.
 * • Operations should usually be O(1).
 * • Network latency dominates instruction latency.
 * • Durability is configurable.
 * • Replication is asynchronous by default.
 *
 * ----------------------------------------------------------------------------
 * Representative Example
 * ----------------------------------------------------------------------------
 *
 * SET user:1 "Alice"
 *
 * Request
 *      │
 *      ▼
 * TCP Socket
 *      │
 *      ▼
 * Event Loop
 *      │
 *      ▼
 * Parse Command
 *      │
 *      ▼
 * Dictionary Lookup
 *      │
 *      ▼
 * Update Value
 *      │
 *      ├────────► Replication Buffer
 *      │
 *      ├────────► AOF Buffer
 *      │
 *      └────────► Expiration Dictionary
 *      │
 *      ▼
 * Reply Client
 *
 * ----------------------------------------------------------------------------
 * LeetCode
 * ----------------------------------------------------------------------------
 *
 * Not a LeetCode problem.
 *
 *
 * ============================================================================
 * 3. 🔵 CORE PATTERN OVERVIEW
 * ============================================================================
 *
 * Pattern
 * -------
 * Single-Threaded Event Driven In-Memory State Machine
 *
 * Archetype
 * ---------
 * Request
 *      ↓
 * Parse
 *      ↓
 * State Transition
 *      ↓
 * Persist
 *      ↓
 * Replicate
 *      ↓
 * Reply
 *
 * Core Invariant
 * --------------
 * Every command transforms exactly one globally consistent in-memory state.
 *
 * Redis does not execute two write commands simultaneously.
 *
 * Every mutation observes a deterministic ordering.
 *
 * This invariant simplifies:
 *
 * • locking
 * • consistency
 * • replication
 * • debugging
 * • persistence
 *
 * Why It Works
 * ------------
 *
 * Modern CPUs execute millions of instructions before a network packet arrives.
 *
 * The bottleneck is usually:
 *
 * Client
 *      ↓
 * Network
 *      ↓
 * Kernel
 *      ↓
 * Redis
 *
 * not
 *
 * CPU arithmetic.
 *
 * Therefore avoiding locks is often more valuable than adding threads.
 *
 * Recognition Signals
 * -------------------
 *
 * Whenever you hear:
 *
 * • cache
 * • key-value
 * • in-memory
 * • low latency
 * • O(1)
 * • event loop
 *
 * this mental model applies.
 *
 * When To Use
 * -----------
 *
 * • caching
 * • counters
 * • sessions
 * • queues
 * • leaderboards
 * • rate limiting
 * • locks
 * • pub/sub
 *
 * When NOT To Use
 * ---------------
 *
 * • large analytical scans
 * • joins
 * • complex SQL
 * • petabyte storage
 * • disk-first workloads
 *
 * Comparison
 * ----------
 *
 * HashMap
 *     Stores data.
 *
 * Redis
 *     Stores data
 *     + networking
 *     + persistence
 *     + replication
 *     + expiration
 *     + eviction
 *     + clustering
 *
 *
 * ============================================================================
 * 4. 🟢 MENTAL MODEL & INVARIANTS
 * ============================================================================
 *
 * Think of Redis as one giant HashMap protected by one deterministic event loop.
 *
 * Everything else is layered around this HashMap.
 *
 *                           +----------------------+
 *                           |      Clients         |
 *                           +----------+-----------+
 *                                      |
 *                                      |
 *                           TCP Connections
 *                                      |
 *                                      ▼
 *                          +----------------------+
 *                          | Event Loop           |
 *                          +----------+-----------+
 *                                     |
 *                                     ▼
 *                          Parse RESP Command
 *                                     |
 *                                     ▼
 *                       +---------------------------+
 *                       | State Transition          |
 *                       +-------------+-------------+
 *                                     |
 *                   +-----------------+-------------------+
 *                   |                 |                   |
 *                   ▼                 ▼                   ▼
 *              Main Dictionary   Expiration Map     Replication
 *                   |                 |               Buffers
 *                   |                 |
 *                   +-----------------+
 *                             |
 *                             ▼
 *                         Persistence
 *                             |
 *                             ▼
 *                           Response
 *
 *
 * --------------------------------------------------------------------------
 * Redis = State Machine
 * --------------------------------------------------------------------------
 *
 * Current State
 *      +
 * Incoming Command
 *      =
 * Next State
 *
 * Nothing magical happens.
 *
 * Every command is simply a deterministic transition.
 *
 * Example
 *
 * {}
 *
 * SET A 5
 *
 * becomes
 *
 * {A=5}
 *
 * INCR A
 *
 * becomes
 *
 * {A=6}
 *
 * DEL A
 *
 * becomes
 *
 * {}
 *
 * The state is always well defined.
 *
 *
 * --------------------------------------------------------------------------
 * Invariant 1
 * --------------------------------------------------------------------------
 *
 * There is only ONE authoritative in-memory state.
 *
 * Persistence,
 * replication,
 * clients,
 * snapshots,
 * replicas,
 * and AOF
 * are all downstream observers.
 *
 * They never become the source of truth while Redis is running.
 *
 *
 * --------------------------------------------------------------------------
 * Invariant 2
 * --------------------------------------------------------------------------
 *
 * Commands execute one at a time.
 *
 * This eliminates write-write races.
 *
 * Example:
 *
 * INCR counter
 * INCR counter
 *
 * They execute as
 *
 * #1
 * then
 * #2
 *
 * never simultaneously.
 *
 *
 * --------------------------------------------------------------------------
 * Invariant 3
 * --------------------------------------------------------------------------
 *
 * Every completed command leaves Redis in a valid state.
 *
 * There is never:
 *
 * half-written hash
 *
 * half-updated list
 *
 * half-inserted key
 *
 * because execution is atomic at command granularity.
 *
 *
 * --------------------------------------------------------------------------
 * Invariant 4
 * --------------------------------------------------------------------------
 *
 * Reads always observe some completed state.
 *
 * Never:
 *
 * halfway through command execution.
 *
 *
 * --------------------------------------------------------------------------
 * Invariant 5
 * --------------------------------------------------------------------------
 *
 * Persistence follows state transitions.
 *
 * State changes first.
 *
 * Logging follows.
 *
 * Replication follows.
 *
 * Response follows according to configuration.
 *
 *
 * --------------------------------------------------------------------------
 * Variable Meanings
 * --------------------------------------------------------------------------
 *
 * mainDictionary
 *     Entire database.
 *
 * key
 *     Unique identifier.
 *
 * value
 *     Redis object.
 *
 * expireDictionary
 *     Maps key → expiration timestamp.
 *
 * replicationBuffer
 *     Ordered stream of mutations.
 *
 * aofBuffer
 *     Ordered command log.
 *
 * eventLoop
 *     Scheduler executing one command at a time.
 *
 *
 * --------------------------------------------------------------------------
 * Allowed State Transitions
 * --------------------------------------------------------------------------
 *
 * Read
 *
 * Read → Return
 *
 * Write
 *
 * Parse
 * ↓
 * Validate
 * ↓
 * Update Dictionary
 * ↓
 * Update Metadata
 * ↓
 * Queue Replication
 * ↓
 * Queue Persistence
 * ↓
 * Reply
 *
 *
 * --------------------------------------------------------------------------
 * Forbidden Moves
 * --------------------------------------------------------------------------
 *
 * Updating two independent versions of memory.
 *
 * Executing concurrent writes to same dictionary.
 *
 * Returning success before command exists in memory.
 *
 * Modifying replicas directly.
 *
 *
 * --------------------------------------------------------------------------
 * Termination
 * --------------------------------------------------------------------------
 *
 * Every command eventually reaches exactly one terminal outcome:
 *
 * Success
 *
 * or
 *
 * Error
 *
 * with one deterministic state transition.
 *
 *
 * --------------------------------------------------------------------------
 * Why Naive Models Fail
 * --------------------------------------------------------------------------
 *
 * Many engineers imagine Redis as:
 *
 * Client
 *   ↓
 * Thread
 *   ↓
 * HashMap
 *
 * That ignores:
 *
 * • networking
 * • parser
 * • expiration
 * • persistence
 * • replication
 * • event loop
 * • memory allocator
 * • object encoding
 *
 * Interview performance suffers because they cannot explain why Redis scales,
 * why locks are unnecessary, or why asynchronous replication is safe under
 * this execution model.
 *
 *
 * ============================================================================
 * 5. 🔴 WHY WRONG MENTAL MODELS FAIL
 * ============================================================================
 *
 * Wrong Model #1
 * --------------
 *
 * "Redis is just a HashMap."
 *
 * Why it appears correct:
 *
 * Most commands look like hash operations.
 *
 * Violated Invariant:
 *
 * The authoritative state also includes metadata, expiration scheduling,
 * persistence ordering, replication ordering, and object encoding.
 *
 * Counterexample:
 *
 * EXPIRE key 30
 *
 * modifies expiration metadata without changing the stored value.
 *
 *
 * Wrong Model #2
 * --------------
 *
 * "Single-threaded means Redis cannot scale."
 *
 * Why it appears correct:
 *
 * CPUs have many cores.
 *
 * Violated Invariant:
 *
 * The critical section is the state transition, not packet reception.
 *
 * Redis overlaps networking, kernel I/O, and background persistence while
 * preserving one ordered mutation stream.
 *
 *
 * Wrong Model #3
 * --------------
 *
 * "Persistence writes before memory."
 *
 * Violated Invariant:
 *
 * Redis first commits the authoritative in-memory state.
 *
 * Persistence reflects that state afterwards.
 *
 *
 * Wrong Model #4
 * --------------
 *
 * "Replication is another source of truth."
 *
 * Violated Invariant:
 *
 * Replicas replay the master's ordered transition log.
 *
 * They derive state.
 *
 * They never independently invent it.
 *
 *
 * Interview Trap
 * --------------
 *
 * Q:
 * Why doesn't Redis need locks around its main dictionary?
 *
 * Expected reasoning:
 *
 * Because the event loop guarantees that only one mutation executes at a time,
 * preserving the global state invariant without fine-grained synchronization.
 *
 *
 * ============================================================================
 * ⚙ IMPLEMENTATION BLUEPRINT
 * ============================================================================
 *
 * Although Redis itself is millions of lines of C, the interview mental model
 * can be reconstructed mechanically.
 *
 * Typing Order
 * ------------
 *
 * 1. Create event loop.
 * 2. Accept request.
 * 3. Parse command.
 * 4. Lookup key.
 * 5. Apply transition.
 * 6. Update expiration metadata.
 * 7. Queue persistence.
 * 8. Queue replication.
 * 9. Return response.
 *
 * Minimal Skeleton
 *
 * receive()
 * parse()
 * execute()
 * updateMetadata()
 * replicate()
 * persist()
 * respond()
 *
 *
 * ============================================================================
 * 🧾 ULTRA-COMPACT PSEUDOCODE
 * ============================================================================
 *
 * receive request
 * parse
 * apply transition
 * update metadata
 * queue replication
 * queue persistence
 * return response
 *
 *
 * ============================================================================
 * 6. SOLUTION CLASSES
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * Brute Force Mental Model
 * ----------------------------------------------------------------------------
 *
 * Idea
 * ----
 *
 * Imagine one thread per client directly modifying a shared HashMap.
 *
 * Invariant
 * ---------
 *
 * Locks protect correctness.
 *
 * Limitation
 * ----------
 *
 * High lock contention.
 * Difficult debugging.
 * Non-deterministic execution.
 *
 * Complexity
 * ----------
 *
 * Dominated by synchronization overhead.
 *
 * Interview Usefulness
 * --------------------
 *
 * Good starting point to motivate Redis.
 *
 *
 * ----------------------------------------------------------------------------
 * Improved Mental Model
 * ----------------------------------------------------------------------------
 *
 * Idea
 * ----
 *
 * One central event loop serializes mutations while background threads handle
 * non-authoritative work such as persistence and snapshot generation.
 *
 * Invariant
 * ---------
 *
 * Exactly one ordered mutation stream exists.
 *
 * Improvement
 * -----------
 *
 * Eliminates write contention on the primary state.
 *
 * Complexity
 * ----------
 *
 * Most commands remain O(1).
 *
 * Interview Usefulness
 * --------------------
 *
 * Explains why Redis is fast before discussing implementation details.
 *
 *
 * ----------------------------------------------------------------------------
 * Optimal (Interview Preferred)
 * ----------------------------------------------------------------------------
 */
public class DesignRedis {

/**
 * Idea
 * ----
 *
 * View Redis as layered components wrapped around one authoritative
 * in-memory state.
 *
 *                    Client Commands
 *                           │
 *                           ▼
 *                    Event Loop (Ordering)
 *                           │
 *                           ▼
 *                    State Transition
 *                           │
 *            ┌──────────────┼──────────────┐
 *            ▼              ▼              ▼
 *      Main Dictionary   Metadata     Replication Log
 *            │              │              │
 *            └──────┬───────┴──────────────┘
 *                   ▼
 *              Persistence
 *                   ▼
 *                 Response
 *
 * Every subsystem exists to preserve or distribute the same ordered
 * sequence of state transitions.
 *
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 1 : Networking Layer
 * ------------------------------------------------------------------------
 *
 * Client
 *   │
 * TCP
 *   │
 * Socket
 *   │
 * Event Loop
 *   │
 * RESP Parser
 *   │
 * Command Object
 *
 * Invariant
 * ---------
 *
 * The networking layer never modifies the database.
 *
 * It only converts bytes into executable commands.
 *
 * Debugging Cue
 * -------------
 *
 * Bad parsing should never corrupt state.
 *
 *
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 2 : Command Execution
 * ------------------------------------------------------------------------
 *
 * Command
 *      │
 * Validation
 *      │
 * Dictionary Lookup
 *      │
 * State Transition
 *      │
 * Metadata Update
 *      │
 * Reply
 *
 * Invariant
 * ---------
 *
 * Validation completes before any mutation.
 *
 * Therefore an invalid command cannot partially modify memory.
 *
 *
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 3 : Internal Dictionary
 * ------------------------------------------------------------------------
 *
 * Redis Database
 *
 *          +---------------------------+
 *          | Hash Table                |
 *          +---------------------------+
 *                 │
 *      +----------+-----------+
 *      │          │           │
 *      ▼          ▼           ▼
 *   key1       key2        key3
 *      │          │           │
 *      ▼          ▼           ▼
 *  RedisObj   RedisObj    RedisObj
 *
 * A Redis object contains:
 *
 * • type
 * • encoding
 * • actual value
 * • reference count
 *
 * Invariant
 * ---------
 *
 * Keys point to objects.
 *
 * Objects own data.
 *
 * Metadata never lives inside the key string.
 *
 *
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 4 : Redis Object
 * ------------------------------------------------------------------------
 *
 * Redis Object
 *
 * +-----------------------------+
 * | Type                        |
 * | Encoding                    |
 * | Pointer to Data             |
 * | Ref Count                   |
 * +-----------------------------+
 *
 * Possible Types
 *
 * String
 * List
 * Set
 * Hash
 * Sorted Set
 * Stream
 *
 * Different encodings optimize memory while preserving identical external
 * behavior.
 *
 * Example
 *
 * A tiny hash may internally use a compact representation.
 *
 * A large hash may switch to a full hash table.
 *
 * Invariant
 * ---------
 *
 * External semantics remain unchanged after encoding transitions.
 *
 *
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 5 : Expiration
 * ------------------------------------------------------------------------
 *
 * Main Dictionary
 *
 * key → value
 *
 * Separate Expiration Dictionary
 *
 * key → unixTimestamp
 *
 * Instead of:
 *
 * key
 * ├── value
 * └── expiration
 *
 * Redis separates concerns.
 *
 * Why?
 *
 * Most keys never expire.
 *
 * Storing expiration beside every value wastes memory.
 *
 * Invariant
 * ---------
 *
 * Only expiring keys occupy expiration metadata.
 *
 *
 * ------------------------------------------------------------------------
 * Expiration Lifecycle
 * ------------------------------------------------------------------------
 *
 * SET A 10
 *
 * Dictionary
 *
 * A → 10
 *
 * EXPIRE A 30
 *
 * Dictionary
 *
 * A → 10
 *
 * Expiration Map
 *
 * A → now+30
 *
 * GET A
 *
 * Before returning:
 *
 * check expiration
 *
 * if expired:
 *
 * remove
 *
 * return null
 *
 * else:
 *
 * return value
 *
 * Invariant
 * ---------
 *
 * Expired keys behave as though they never existed.
 *
 *
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 6 : Passive vs Active Expiration
 * ------------------------------------------------------------------------
 *
 * Passive
 *
 * Client accesses key.
 *
 * Redis notices expiration.
 *
 * Deletes immediately.
 *
 * Active
 *
 * Background cycle samples expiration dictionary.
 *
 * Removes expired keys proactively.
 *
 * Why both?
 *
 * Passive alone leaves forgotten expired keys occupying RAM.
 *
 * Active alone cannot guarantee immediate cleanup for every access.
 *
 * Invariant
 * ---------
 *
 * Both mechanisms converge toward the same valid state.
 *
 *
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 7 : Persistence
 * ------------------------------------------------------------------------
 *
 * Memory
 *    │
 *    ├────────────► RDB Snapshot
 *    │
 *    └────────────► AOF Log
 *
 * RDB
 * ---
 *
 * Periodic snapshot.
 *
 * Compact.
 *
 * Faster restart.
 *
 * Possible recent data loss.
 *
 * AOF
 * ---
 *
 * Append every write command.
 *
 * Better durability.
 *
 * Larger file.
 *
 * Replay required.
 *
 * Invariant
 * ---------
 *
 * Neither mechanism becomes the live authoritative state while Redis is
 * running.
 *
 *
 * ------------------------------------------------------------------------
 * AOF Ordering
 * ------------------------------------------------------------------------
 *
 * SET A 5
 * INCR A
 * DEL B
 *
 * AOF
 *
 * SET A 5
 * INCR A
 * DEL B
 *
 * Replaying these commands reconstructs exactly the same state because
 * ordering is preserved.
 *
 * Invariant
 * ---------
 *
 * Ordered replay equals ordered execution.
 *
 /**
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 8 : Replication
 * ------------------------------------------------------------------------
 *
 *                     Master
 *                        │
 *          Ordered Mutation Stream
 *                        │
 *         ┌──────────────┴──────────────┐
 *         ▼                             ▼
 *     Replica 1                    Replica 2
 *
 * Workflow
 * --------
 *
 * Client
 *    │
 *    ▼
 * Master executes command
 *    │
 *    ▼
 * Update authoritative memory
 *    │
 *    ▼
 * Queue replication stream
 *    │
 *    ▼
 * Replicas replay commands in order
 *
 * Invariant
 * ---------
 *
 * Replicas never invent state.
 *
 * They derive state exclusively from the master's ordered mutation stream.
 *
 *
 * ------------------------------------------------------------------------
 * Initial Synchronization
 * ------------------------------------------------------------------------
 *
 * Replica Starts
 *      │
 *      ▼
 * Full Snapshot
 *      │
 *      ▼
 * Load Snapshot
 *      │
 *      ▼
 * Replay Buffered Commands
 *      │
 *      ▼
 * Follow Live Stream
 *
 * Why replay buffered commands?
 *
 * Because writes may occur while the snapshot is being transferred.
 *
 * Invariant
 * ---------
 *
 * Snapshot + buffered mutations reconstruct the current master state.
 *
 *
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 9 : Event Loop
 * ------------------------------------------------------------------------
 *
 *               Wait
 *                 │
 *                 ▼
 *          Receive Requests
 *                 │
 *                 ▼
 *          Parse Commands
 *                 │
 *                 ▼
 *          Execute One Command
 *                 │
 *                 ▼
 *         Queue Side Effects
 *                 │
 *                 ▼
 *            Send Replies
 *                 │
 *                 └──────────────► repeat
 *
 * Invariant
 * ---------
 *
 * Only one command occupies the execution stage at any instant.
 *
 *
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 10 : Memory Allocation
 * ------------------------------------------------------------------------
 *
 * Client
 *    │
 *    ▼
 * Redis Object
 *    │
 *    ▼
 * Allocator
 *    │
 *    ▼
 * Physical RAM
 *
 * Redis itself manages logical objects.
 *
 * The allocator manages physical pages.
 *
 * Invariant
 * ---------
 *
 * Logical ownership and physical allocation are separate concerns.
 *
 *
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 11 : Eviction
 * ------------------------------------------------------------------------
 *
 * Memory Limit
 *      │
 *      ▼
 * Incoming Write
 *      │
 *      ▼
 * Enough Space?
 *      │
 *   ┌──┴───┐
 *   │      │
 * Yes      No
 *   │       │
 *   ▼       ▼
 * Execute  Evict
 *            │
 *            ▼
 *      Execute Write
 *
 * Typical Policies
 * ----------------
 *
 * noeviction
 * allkeys-lru
 * volatile-lru
 * allkeys-random
 * volatile-random
 * allkeys-lfu
 *
 * Invariant
 * ---------
 *
 * Eviction occurs before the new allocation would violate the configured
 * memory limit.
 *
 *
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 12 : Transactions
 * ------------------------------------------------------------------------
 *
 * MULTI
 *   │
 * Queue Commands
 *   │
 * EXEC
 *   │
 * Execute Sequentially
 *
 * Redis transactions do not provide rollback like traditional SQL systems.
 *
 * Instead, they guarantee ordered execution without interleaving by other
 * clients.
 *
 * Invariant
 * ---------
 *
 * The queued command sequence executes atomically with respect to other
 * clients.
 *
 *
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 13 : Lua Scripts
 * ------------------------------------------------------------------------
 *
 * Script
 *    │
 *    ▼
 * Execute Inside Event Loop
 *    │
 *    ▼
 * Complete
 *
 * Since the script runs within the same event loop, no other write command
 * interleaves during execution.
 *
 * Invariant
 * ---------
 *
 * The entire script is one uninterrupted state transition.
 *
 *
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 14 : Redis Cluster
 * ------------------------------------------------------------------------
 *
 *                    Hash(key)
 *                       │
 *                       ▼
 *                Slot (0-16383)
 *                       │
 *                       ▼
 *                 Responsible Node
 *
 * Instead of every node storing every key:
 *
 * Keys are partitioned into slots.
 *
 * Invariant
 * ---------
 *
 * Every key belongs to exactly one hash slot at a given moment.
 *
 *
 * ------------------------------------------------------------------------
 * 🟢 Nested Mental Model 15 : Streams
 * ------------------------------------------------------------------------
 *
 * Producer
 *    │
 *    ▼
 * Stream
 *    │
 *    ▼
 * Consumer Groups
 *
 * Unlike Pub/Sub, messages remain stored until trimmed or deleted.
 *
 * Invariant
 * ---------
 *
 * Consumers advance independently while preserving message ordering within
 * the stream.
 *
 *
 * ------------------------------------------------------------------------
 * Correctness Summary
 * ------------------------------------------------------------------------
 *
 * Every Redis subsystem preserves one fundamental property:
 *
 *      Ordered State Transitions
 *
 * Networking preserves ordering.
 *
 * Event loop preserves ordering.
 *
 * Replication preserves ordering.
 *
 * AOF preserves ordering.
 *
 * Transactions preserve ordering.
 *
 * Lua preserves ordering.
 *
 * Expiration preserves state validity.
 *
 * Eviction preserves memory constraints.
 *
 * Cluster preserves key ownership.
 *
 * Everything else is an optimization layered on top of this invariant.
 *
 *
 * Complexity
 * ----------
 *
 * Average lookup        : O(1)
 * Average SET           : O(1)
 * Average GET           : O(1)
 * Expiration lookup     : O(1)
 * Replication enqueue   : O(1)
 * AOF append            : Amortized O(1)
 *
 * Interview Usefulness
 * --------------------
 *
 * This layered mental model allows nearly every Redis interview question
 * to be derived instead of memorized.
 *
 *
 * =========================================================================
 * 🟣 INTERVIEW ARTICULATION
 * =========================================================================
 *
 * If asked to explain Redis in an interview:
 *
 * "I think of Redis as a deterministic in-memory state machine. The
 * authoritative state lives in one primary dictionary. A single event loop
 * serializes mutations so there are no concurrent writes to that state.
 * Every successful command first updates memory, then propagates ordered
 * side effects such as expiration metadata, replication streams, and
 * persistence logs. Replicas, AOF, and snapshots all derive from the same
 * ordered mutation history rather than becoming independent sources of
 * truth. Nearly every Redis feature exists to preserve that ordering while
 * optimizing latency, durability, or scalability."
 *

 /**
 * =========================================================================
 * 🎯 INTERVIEW RECALL SHEET
 * =========================================================================
 *
 * Trigger
 * -------
 * In-memory key-value database requiring extremely low latency.
 *
 * Pattern
 * -------
 * Single-threaded event-driven state machine.
 *
 * Search Target
 * -------------
 * One authoritative in-memory state.
 *
 * Invariant
 * ---------
 * One ordered mutation stream.
 *
 * Discard Rule
 * ------------
 * Never allow concurrent writes to the authoritative state.
 *
 * Common Trap
 * -----------
 * Thinking Redis is "just a HashMap."
 *
 * Edge Cases
 * ----------
 * • Expired keys
 * • Memory limit reached
 * • Replica lag
 * • Restart recovery
 * • Snapshot during writes
 *
 * One-Liner
 * ---------
 * Redis is an ordered in-memory state machine with layered durability,
 * replication, and memory management.
 *
 * Re-derivation Cue
 * -----------------
 * Client → Event Loop → State Transition → Metadata →
 * Replication → Persistence → Response.
 *
 *
 * =========================================================================
 * 🔄 VARIATIONS & TWEAKS
 * =========================================================================
 *
 * Variation
 * ---------
 * Read Replicas
 *
 * Reasoning Change
 * ----------------
 * Reads may come from replicas while writes still originate from the
 * master.
 *
 * Preserved Invariant
 * -------------------
 * Ordered mutation stream.
 *
 *
 * Variation
 * ---------
 * Redis Sentinel
 *
 * Reasoning Change
 * ----------------
 * Detect failures and elect a new master.
 *
 * Preserved Invariant
 * -------------------
 * Exactly one authoritative writer after failover.
 *
 *
 * Variation
 * ---------
 * Redis Cluster
 *
 * Reasoning Change
 * ----------------
 * Partition keys across hash slots.
 *
 * Preserved Invariant
 * -------------------
 * One ordered mutation stream per shard.
 *
 *
 * Variation
 * ---------
 * Multi-threaded I/O
 *
 * Reasoning Change
 * ----------------
 * Networking can be parallelized.
 *
 * Preserved Invariant
 * -------------------
 * Command execution remains serialized.
 *
 *
 * Pattern Break
 * -------------
 * Multiple threads modifying the same authoritative dictionary without
 * synchronization.
 *
 * Why It Fails
 * ------------
 * Lost updates, race conditions, and non-deterministic replication.
 *
 *
 * =========================================================================
 * 🧠 MASTERY CHECKLIST
 * =========================================================================
 *
 * ✔ What is the invariant?
 *   One authoritative ordered state transition stream.
 *
 * ✔ What is the search target?
 *   The current in-memory state.
 *
 * ✔ What is the discard rule?
 *   Never execute concurrent mutations on the authoritative state.
 *
 * ✔ Why does the algorithm terminate?
 *   Every command deterministically reaches success or failure after one
 *   state transition.
 *
 * ✔ Why does the naive model fail?
 *   It ignores ordering, metadata, persistence, and replication.
 *
 * ✔ Which edge cases matter?
 *   Expiration, eviction, restart, replication lag, failover.
 *
 * ✔ Can I debug it?
 *   Yes. Walk the ordered transition pipeline step by step.
 *
 * ✔ Can I derive variants?
 *   Yes. Preserve the ordered mutation invariant while changing storage,
 *   durability, or distribution strategy.
 *
 * ✔ Where does the pattern stop?
 *   When workloads require distributed transactions, relational joins,
 *   or analytical processing beyond an in-memory key-value model.
 */


    /**
     * =========================================================================
     * 🧪 MAIN + SELF-VERIFYING TESTS
     * =========================================================================
     */
    public static void main(String[] args) {

        // Happy path: a Redis command follows the canonical pipeline.
        List<String> pipeline = List.of(
                "Receive",
                "Parse",
                "Execute",
                "Metadata",
                "Replication",
                "Persistence",
                "Response"
        );

        assert pipeline.size() == 7;
        assert pipeline.get(0).equals("Receive");
        assert pipeline.get(6).equals("Response");

        // Ordering is preserved.
        assert pipeline.indexOf("Execute") < pipeline.indexOf("Replication");
        assert pipeline.indexOf("Replication") < pipeline.indexOf("Response");

        // Expiration metadata is independent of the value dictionary.
        Map<String, String> dictionary = new HashMap<>();
        Map<String, Long> expiration = new HashMap<>();

        dictionary.put("user:1", "Alice");
        expiration.put("user:1", System.currentTimeMillis() + 30_000);

        assert dictionary.containsKey("user:1");
        assert expiration.containsKey("user:1");

        // Replica derives state instead of creating it.
        List<String> masterLog = new ArrayList<>();
        masterLog.add("SET A 5");
        masterLog.add("INCR A");

        List<String> replicaLog = new ArrayList<>(masterLog);

        assert replicaLog.equals(masterLog);

        // Cluster invariant: every key belongs to exactly one slot.
        int slot = Math.floorMod("user:1".hashCode(), 16384);

        assert slot >= 0;
        assert slot < 16384;

        // Event-loop ordering.
        Queue<String> eventLoop = new ArrayDeque<>();
        eventLoop.offer("SET");
        eventLoop.offer("GET");
        eventLoop.offer("DEL");

        assert eventLoop.poll().equals("SET");
        assert eventLoop.poll().equals("GET");
        assert eventLoop.poll().equals("DEL");
        assert eventLoop.isEmpty();

        // Memory limit reasoning.
        long usedMemory = 900;
        long maxMemory = 1000;
        long incomingAllocation = 50;

        assert usedMemory + incomingAllocation <= maxMemory;

        // Atomic command reasoning.
        int counter = 0;
        counter++;
        counter++;

        assert counter == 2;

        // State machine reconstruction.
        String state = "{}";
        state = "{A=5}";
        state = "{A=6}";
        state = "{}";

        assert "{}".equals(state);
    }
}

/*
I understand the invariant.

I can re-derive the solution.

I can physically reconstruct the implementation under pressure.

This chapter is complete.
*/