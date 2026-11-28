# Project deep dive and behavioural interview workbook

**Baseline:** Java 17+ (Java 21 features marked), Spring Boot 3.x, PostgreSQL where applicable. P0 = closed-book recall; P1 = explain and implement with documentation. Snippets are focused fragments, not standalone projects.

## P0 — Prepare three real stories
Use your actual Morgan Stanley and Nasdaq work; do not invent metrics or claim ownership you did not have. Candidate topics to validate: risk buffer implementation, NGINX rate-limiting POC, cross-team delivery/Scrum coordination. Choose one architecture story, one production/debugging story and one influence/conflict story. Replace placeholders with verified non-confidential details.

## Architecture story template
```text
Business problem / risk:
Users and constraints:
My responsibility vs team responsibility:
Before architecture:
After architecture:
Request and data flow:
Key invariant / failure mode:
Alternatives considered and why rejected:
Implementation and tests:
Deployment / monitoring / rollback:
Measured result (baseline, method, period) or qualitative evidence:
What I would improve now:
```
## P0 — STAR + trade-off
Situation (brief context), Task (your responsibility), Action (specific decisions and collaboration), Result (verified outcome), Reflection (lesson). Give a 90-second version and a 5-minute technical expansion. Use “I” for your contribution and “we” for team outcome. If no measured number, state that honestly and describe evidence.

## P0 — Interview question bank
- Tell me about your most complex system and its bottleneck.
- Why did you choose this architecture? What alternative did you reject?
- How did you validate financial/risk correctness under failure and concurrency?
- Describe a production incident: detection, mitigation, root cause, prevention.
- Describe a disagreement, missed deadline, mistake, ambiguous requirement.
- How did you influence without authority and improve team delivery?
- How would you redesign your system at 10× load?

## P1 — Risk and strategy differentiation
Frame risk-sensitive decisions as expected impact, likelihood, cost, reversibility and control coverage. Explain false positives vs false negatives, stale data, fail-open vs fail-closed, auditability and reconciliation. Distinguish a technical solution from business outcome. For rate limiting, explain burst vs sustained limits, identity key, proxy topology, distributed enforcement and load testing; do not assert unverified production adoption.

## Practice checklist
- [ ] Three truthful STAR stories with follow-up questions.
- [ ] One architecture diagram from memory, sanitized for confidentiality.
- [ ] One incident timeline and trade-off matrix.
- [ ] One example of mentoring or cross-team influence.
- [ ] Explain why next role fits your strengths and desired trajectory.
- [ ] Prepare questions about ownership, system constraints, expectations and success measures.
