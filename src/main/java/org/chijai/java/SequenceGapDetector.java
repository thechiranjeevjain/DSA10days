package org.chijai.java;

import java.util.*;

public class SequenceGapDetector {

    static class Message {
        private final long sequenceNumber;
        private final String payload;

        Message(long sequenceNumber, String payload) {
            this.sequenceNumber = sequenceNumber;
            this.payload = payload;
        }

        public long getSequenceNumber() {
            return sequenceNumber;
        }

        public String getPayload() {
            return payload;
        }

        @Override
        public String toString() {
            return "Message{seq=" + sequenceNumber + ", payload='" + payload + "'}";
        }
    }

    static class SequenceTracker {

        // Highest contiguous sequence number successfully received.
        private long highestContiguousSequence = 0;

        // Seen sequence numbers above highestContiguousSequence.
        // TreeSet keeps future/out-of-order messages sorted.
        private final TreeSet<Long> pending = new TreeSet<>();

        // All sequence numbers ever seen, for duplicate detection.
        private final Set<Long> seen = new HashSet<>();

        public void onMessage(Message message) {
            long seq = message.getSequenceNumber();

            System.out.println("\nReceived: " + message);

            // 1. Duplicate
            if (!seen.add(seq)) {
                System.out.println("DUPLICATE: sequence " + seq);
                return;
            }

            // 2. Old / out-of-order arrival
            if (seq <= highestContiguousSequence) {
                System.out.println("OUT-OF-ORDER OLD MESSAGE: sequence " + seq);
                return;
            }

            // 3. Exactly the next expected sequence
            if (seq == highestContiguousSequence + 1) {
                highestContiguousSequence = seq;

                // Advance through any previously received future messages.
                while (pending.remove(highestContiguousSequence + 1)) {
                    highestContiguousSequence++;
                }

                System.out.println(
                        "IN-ORDER. Highest contiguous sequence = "
                                + highestContiguousSequence
                );

                printMissingRange();
                return;
            }

            // 4. seq > expected => gap detected + out-of-order future message
            pending.add(seq);

            long expected = highestContiguousSequence + 1;

            System.out.println(
                    "OUT-OF-ORDER FUTURE MESSAGE: received " + seq
                            + ", expected " + expected
            );

            System.out.println(
                    "MISSING RANGE: "
                            + expected
                            + "-"
                            + (seq - 1)
            );
        }

        private void printMissingRange() {
            if (pending.isEmpty()) {
                System.out.println("NO CURRENT GAP");
                return;
            }

            long firstFutureSequence = pending.first();
            long missingStart = highestContiguousSequence + 1;

            if (firstFutureSequence > missingStart) {
                System.out.println(
                        "CURRENT MISSING RANGE: "
                                + missingStart
                                + "-"
                                + (firstFutureSequence - 1)
                );
            } else {
                System.out.println("NO CURRENT GAP");
            }
        }

        public void printState() {
            System.out.println("\n========== FINAL STATE ==========");
            System.out.println(
                    "Highest contiguous sequence: "
                            + highestContiguousSequence
            );

            System.out.println(
                    "Pending out-of-order sequences: "
                            + pending
            );

            if (pending.isEmpty()) {
                System.out.println("Missing ranges: none");
            } else {
                printAllMissingRanges();
            }

            System.out.println("=================================");
        }

        private void printAllMissingRanges() {
            System.out.println("Missing ranges:");

            long expected = highestContiguousSequence + 1;

            for (long seq : pending) {
                if (seq > expected) {
                    System.out.println(
                            "  " + expected + "-" + (seq - 1)
                    );
                }

                expected = seq + 1;
            }
        }
    }

    public static void main(String[] args) {

        SequenceTracker tracker = new SequenceTracker();

        List<Message> messages = List.of(
                new Message(1, "A"),
                new Message(2, "B"),

                // Missing 3, message 4 arrives first.
                new Message(4, "D"),

                // Missing message arrives later.
                new Message(3, "C"),

                // Duplicate.
                new Message(4, "D again"),

                // Big gap: missing 5,6.
                new Message(7, "G"),

                // Future message, still missing 5,6.
                new Message(8, "H"),

                // Fill part of the gap.
                new Message(5, "E"),

                // Fill remaining gap.
                // Tracker should now automatically advance through 7 and 8.
                new Message(6, "F"),

                // Duplicate / old arrival.
                new Message(2, "B again"),

                // New gap: missing 9.
                new Message(10, "J"),

                // Fill gap.
                new Message(9, "I")
        );

        for (Message message : messages) {
            tracker.onMessage(message);
        }

        tracker.printState();
    }
}

/*
==========================================================
SEQUENCE GAP DETECTOR — INTERVIEW RECALL
==========================================================

GOAL:

Given messages containing monotonically increasing sequence numbers,
detect:

1. DUPLICATES
2. MISSING SEQUENCES / RANGES
3. OUT-OF-ORDER ARRIVALS


----------------------------------------------------------
CORE STATE
----------------------------------------------------------

long highestContiguousSequence;

Set<Long> seen;

TreeSet<Long> pending;


MEANING:

highestContiguousSequence
    = everything <= this sequence has arrived.

pending
    = future messages that arrived before their gaps were filled.

seen
    = every sequence ever received.


----------------------------------------------------------
CORE INVARIANT
----------------------------------------------------------

ALL sequence numbers <= highestContiguousSequence
have already been received.

That is the key invariant.


----------------------------------------------------------
DECISION TREE
----------------------------------------------------------

Receive seq

        |
        v

already in seen?
        |
      YES
        |
        v
    DUPLICATE


Otherwise:

seq == highestContiguous + 1
        |
      YES
        |
        v
advance contiguous pointer
and consume pending consecutive numbers


seq > highestContiguous + 1
        |
        v
OUT OF ORDER
+
GAP DETECTED

missing:

[highestContiguous + 1, seq - 1]


----------------------------------------------------------
EXAMPLE
----------------------------------------------------------

Receive:

1, 2, 4

highestContiguous = 2

4 arrives

expected = 3

=> 4 is out of order
=> missing range = [3,3]

pending = {4}


Then 3 arrives:

highestContiguous becomes 3

pending contains 4

=> consume 4

highestContiguous becomes 4


----------------------------------------------------------
IMPORTANT DISTINCTION
----------------------------------------------------------

DUPLICATE:

1, 2, 2

Second 2 was already seen.


OUT OF ORDER:

1, 2, 4, 3

4 arrived before 3.


GAP:

1, 2, 5

Missing range:

3-4


----------------------------------------------------------
COMPLEXITY
----------------------------------------------------------

HashSet duplicate detection:

O(1) average


TreeSet insertion/removal:

O(log n)


Per message:

O(log n) worst case


Space:

O(k)

where k = number of remembered sequence numbers.


----------------------------------------------------------
LOW-LATENCY INTERVIEW TRADE-OFF
----------------------------------------------------------

TreeSet is simple and interview-friendly.

But for a high-throughput trading/feed-handler system,
you may instead use:

- ring buffer
- bitmap
- bounded boolean array
- specialized primitive collections

if the allowed sequence window is bounded.

This can reduce:

allocation
GC pressure
cache misses
log(n) operations


----------------------------------------------------------
REAL TRADING SYSTEM MENTAL MODEL
----------------------------------------------------------

Exchange sends:

100
101
103

Expected:

102

103 must usually NOT be processed as if the stream were complete.

System may:

1. mark gap 102
2. buffer 103
3. request retransmission/recovery
4. receive 102
5. release 102
6. then release buffered 103


This is common in:

market-data feeds
FIX sequencing
order gateways
event streams
replication logs


----------------------------------------------------------
MUG-UP VERSION
----------------------------------------------------------

highest contiguous + seen + pending

seq seen        -> duplicate
seq == next     -> advance + drain pending
seq > next      -> gap + out-of-order + buffer


----------------------------------------------------------
ONE-LINE RETRIEVAL TRIGGER
----------------------------------------------------------

"Sequenced stream?"

Think:

EXPECTED POINTER
+
DUPLICATE SET
+
OUT-OF-ORDER BUFFER
+
GAP RANGE

==========================================================
*/
