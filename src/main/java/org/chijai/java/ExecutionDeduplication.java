package org.chijai.java;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExecutionDeduplication {

    static class ExecutionEvent {
        private final String executionId;
        private final String accountId;
        private final double quantity;
        private final double price;

        ExecutionEvent(String executionId,
                       String accountId,
                       double quantity,
                       double price) {
            this.executionId = executionId;
            this.accountId = accountId;
            this.quantity = quantity;
            this.price = price;
        }

        public String getExecutionId() {
            return executionId;
        }

        public String getAccountId() {
            return accountId;
        }

        public double getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "ExecutionEvent{" +
                    "executionId='" + executionId + '\'' +
                    ", accountId='" + accountId + '\'' +
                    ", quantity=" + quantity +
                    ", price=" + price +
                    '}';
        }
    }

    static class PositionState {
        private double totalQuantity;
        private double totalNotional;

        public void apply(ExecutionEvent event) {
            totalQuantity += event.getQuantity();
            totalNotional += event.getQuantity() * event.getPrice();
        }

        public double getTotalQuantity() {
            return totalQuantity;
        }

        public double getTotalNotional() {
            return totalNotional;
        }

        @Override
        public String toString() {
            return "PositionState{" +
                    "totalQuantity=" + totalQuantity +
                    ", totalNotional=" + totalNotional +
                    '}';
        }
    }

    static class ExecutionProcessor {

        /*
         * Invariant:
         *
         * If an executionId exists in processedExecutionIds,
         * its state mutation has already been applied.
         */
        private final Set<String> processedExecutionIds = new HashSet<>();

        private final PositionState state = new PositionState();

        public void process(ExecutionEvent event) {

            /*
             * HashSet.add() returns:
             *
             * true  -> ID was NOT present
             * false -> ID was already present
             */
            if (!processedExecutionIds.add(event.getExecutionId())) {
                System.out.println(
                        "DUPLICATE IGNORED: " + event.getExecutionId()
                );
                return;
            }

            // Apply state mutation exactly once.
            state.apply(event);

            System.out.println(
                    "APPLIED: " + event.getExecutionId()
                            + " -> " + state
            );
        }

        public PositionState getState() {
            return state;
        }

        public int getProcessedExecutionCount() {
            return processedExecutionIds.size();
        }
    }

    public static void main(String[] args) {

        ExecutionProcessor processor = new ExecutionProcessor();

        List<ExecutionEvent> events = List.of(
                new ExecutionEvent("EXEC-1001", "ACC-1", 100, 10.00),
                new ExecutionEvent("EXEC-1002", "ACC-1", 50, 20.00),

                // Duplicate: must NOT affect state again.
                new ExecutionEvent("EXEC-1001", "ACC-1", 100, 10.00),

                new ExecutionEvent("EXEC-1003", "ACC-1", -20, 15.00),

                // Another duplicate.
                new ExecutionEvent("EXEC-1002", "ACC-1", 50, 20.00)
        );

        for (ExecutionEvent event : events) {
            processor.process(event);
        }

        System.out.println("\n========== FINAL STATE ==========");
        System.out.println(processor.getState());
        System.out.println(
                "Unique executions processed: "
                        + processor.getProcessedExecutionCount()
        );
        System.out.println("=================================");
    }
}

/*
==========================================================
EXECUTION DEDUPLICATION — INTERVIEW RECALL
==========================================================

PROBLEM:

A stream may deliver the same execution multiple times.

Example:

EXEC-1
EXEC-2
EXEC-1     <- duplicate

If EXEC-1 changes:

position += 100

then applying it twice would incorrectly produce:

position += 200


GOAL:

Each logical execution must mutate state EXACTLY ONCE.


----------------------------------------------------------
CORE PATTERN
----------------------------------------------------------

Set<ExecutionId> processedIds


On event:

if (!processedIds.add(id))
    duplicate -> ignore

else
    apply state


----------------------------------------------------------
MUG-UP CODE
----------------------------------------------------------

if (!seen.add(event.getExecutionId())) {
    return;
}

apply(event);


----------------------------------------------------------
WHY HashSet.add() IS PERFECT HERE
----------------------------------------------------------

add(id) returns:

true
    -> first time seeing ID

false
    -> ID already exists


So:

if (!seen.add(id))
    return;


gives:

CHECK + INSERT

in one operation.


----------------------------------------------------------
CORE INVARIANT
----------------------------------------------------------

Every executionId in processedExecutionIds
has already affected state exactly once.


----------------------------------------------------------
EXAMPLE
----------------------------------------------------------

Events:

E1 = +100
E2 = +50
E1 = +100 duplicate


Start:

position = 0


E1:

seen = {E1}
position = 100


E2:

seen = {E1, E2}
position = 150


E1 again:

E1 already exists

IGNORE


Final:

position = 150

NOT 250.


----------------------------------------------------------
COMPLEXITY
----------------------------------------------------------

HashSet lookup/insertion:

O(1) average


Per execution:

O(1) average


Space:

O(n)

because execution IDs are remembered.


----------------------------------------------------------
IMPORTANT INTERVIEW CATCH
----------------------------------------------------------

This implementation is correct for:

SINGLE PROCESS
+
SINGLE THREAD
+
IN-MEMORY STATE


But "exactly once" becomes much harder in a real distributed system.


----------------------------------------------------------
THREAD-SAFETY PROBLEM
----------------------------------------------------------

This is unsafe:

if (!seen.contains(id)) {

    seen.add(id);

    updateState();
}


Two threads can both execute:

contains(id) == false

and both update state.


NEVER separate:

CHECK
+
INSERT

when concurrency matters.


----------------------------------------------------------
CONCURRENT VERSION
----------------------------------------------------------

Use:

ConcurrentHashMap.newKeySet()


Then:

if (!seen.add(id)) {
    return;
}

apply(event);


add() is atomic for the set operation.


----------------------------------------------------------
BUT THERE IS STILL A SUBTLE FAILURE
----------------------------------------------------------

Consider:

1. add execution ID to set
2. process crashes
3. state update never happens


After restart:

ID appears processed

but state was never updated.


Reverse ordering is also dangerous:

1. update state
2. crash
3. ID not persisted

After restart:

event arrives again

state gets updated TWICE.


Therefore:

memory-only deduplication does NOT provide true
durable exactly-once semantics.


----------------------------------------------------------
REAL PRODUCTION SOLUTION
----------------------------------------------------------

Deduplication record
+
business-state update

must generally happen ATOMICALLY.


Example database transaction:

BEGIN

INSERT execution_id into processed_executions

UPDATE position

COMMIT


With:

execution_id UNIQUE


If duplicate event arrives:

INSERT violates unique constraint

=> execution is not applied again.


----------------------------------------------------------
DATABASE PATTERN
----------------------------------------------------------

processed_execution

execution_id VARCHAR PRIMARY KEY


Transaction:

INSERT INTO processed_execution(execution_id)
VALUES (?);

UPDATE position
SET quantity = quantity + ?
WHERE account_id = ?;


COMMIT;


The unique execution ID becomes the
IDEMPOTENCY KEY.


----------------------------------------------------------
KEY TERM
----------------------------------------------------------

IDEMPOTENCY

Processing the same logical request multiple times
has the same final effect as processing it once.


Execution ID:

EXEC-12345

acts as the:

IDEMPOTENCY KEY


----------------------------------------------------------
DELIVERY SEMANTICS
----------------------------------------------------------

AT-MOST-ONCE

Message may be lost.
Never intentionally retried.

Risk:

missing executions.


AT-LEAST-ONCE

Messages may be retried.

Risk:

duplicates.


AT-LEAST-ONCE
+
IDEMPOTENT CONSUMER

is a very common practical design.


----------------------------------------------------------
"EXACTLY ONCE" MENTAL MODEL
----------------------------------------------------------

Do NOT think:

"Broker promises exactly once."


Think:

UNIQUE EVENT ID
+
DURABLE DEDUP STORE
+
ATOMIC STATE MUTATION


----------------------------------------------------------
TRADING SYSTEM EXAMPLE
----------------------------------------------------------

Exchange sends execution:

ExecID = ABC123
Qty    = 100


Gateway receives it.

Position:

100


Connection drops.

Exchange retransmits:

ABC123


Without deduplication:

Position = 200       WRONG


With deduplication:

ABC123 already processed

IGNORE


Position = 100       CORRECT


----------------------------------------------------------
LOW-LATENCY TRADE-OFF
----------------------------------------------------------

Keeping every execution ID forever:

O(n) memory

which is usually undesirable.


Possible production strategies:

1. bounded retention window

2. session-level execution IDs

3. sequence-number-based deduplication

4. persistent store with expiration

5. partition-local dedup caches

6. exchange-provided execution/session semantics


----------------------------------------------------------
INTERVIEW GOLDEN ANSWER
----------------------------------------------------------

"For the coding version, I maintain a set of processed
execution IDs and atomically add the ID before applying
the state mutation.

In production, an in-memory set alone cannot guarantee
durable exactly-once processing because a crash can occur
between deduplication and state mutation.

I would use the execution ID as an idempotency key and
persist the deduplication marker and business-state update
in the same transaction, usually protected by a unique
constraint."


----------------------------------------------------------
RETRIEVAL TRIGGER
----------------------------------------------------------

"Duplicate events must not mutate state twice"

        ↓

IDEMPOTENCY KEY
+
DEDUP SET
+
ATOMIC CHECK-AND-INSERT
+
APPLY ONCE


----------------------------------------------------------
ONE-LINE CORE
----------------------------------------------------------

if (seen.add(executionId)) apply(event);


==========================================================
*/