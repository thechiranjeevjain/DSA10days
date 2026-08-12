# Design/LLD

Focused pattern pass. Keep the global rank order inside this file; lower rank means higher interview ROI.

## Recognition Signal

State API contract, data structures, invariants, and complexity per operation.

## Interview Move

Start from operations and constraints, then pick data structures that preserve per-operation invariants.

## Problems

| Global Rank | Must Level | Problem | Pattern | Java | LeetCode | One-line recall | Crisp code idea |
|---:|---|---|---|---|---|---|---|
| 167 | If Time | Api Integration Example | LLD/API integration | [Java](../../../src/main/java/org/chijai/design/lld/ApiIntegrationExample.java) | - | State API contract, data structures, invariants, and complexity per operation. | Implement operations around maps, lists, queues, heaps, or tries with clear invariants. |
| 168 | If Time | Design Redis | LLD / data structures | [Java](../../../src/main/java/org/chijai/design/lld/DesignRedis.java) | - | State API contract, data structures, invariants, and complexity per operation. | Implement operations around maps, lists, queues, heaps, or tries with clear invariants. |
| 169 | If Time | Design Token Bucket Rate Limiter | LLD / rate limiting | [Java](../../../src/main/java/org/chijai/design/lld/DesignTokenBucketRateLimiter.java) | - | State API contract, data structures, invariants, and complexity per operation. | Implement operations around maps, lists, queues, heaps, or tries with clear invariants. |
| 170 | If Time | Encode And Decode Tinyurl | LLD / URL shortener | [Java](../../../src/main/java/org/chijai/design/lld/DesignUrlShortner.java) | [LC](https://leetcode.com/problems/encode-and-decode-tinyurl/) | State API contract, data structures, invariants, and complexity per operation. | Implement operations around maps, lists, queues, heaps, or tries with clear invariants. |
| 171 | If Time | Two Sum | LLD / URL shortener | [Java](../../../src/main/java/org/chijai/design/lld/DesignUrlShortner.java) | [LC](https://leetcode.com/problems/two-sum/) | State API contract, data structures, invariants, and complexity per operation. | Implement operations around maps, lists, queues, heaps, or tries with clear invariants. |

## Drill

1. Read only the problem title.
2. Say brute force, bottleneck, pattern, invariant, code idea, dry run.
3. Open Java only after the spoken answer is complete.
4. Code one missed problem from blank before moving to another pattern.