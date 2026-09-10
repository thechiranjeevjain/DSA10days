#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
OUT="${TMPDIR:-/tmp}/exchange-systems-interview-lab-out"

rm -rf "$OUT"
mkdir -p "$OUT/risk" "$OUT/matching" "$OUT/interview"

find "$ROOT/risk-engine/src/main/java" -name '*.java' -print0 |
  xargs -0 javac -d "$OUT/risk"

find "$ROOT/matching-engine/src/main/java" -name '*.java' -print0 |
  xargs -0 javac -d "$OUT/matching"

for file in "$ROOT"/risk-engine/interview-versions/*.java             "$ROOT"/matching-engine/interview-version/*.java; do
  javac "$file" -d "$OUT/interview"
done

java -cp "$OUT/interview" L0_RiskLimitEngineSingleOwner
java -cp "$OUT/interview" L1_RiskLimitEngineConcurrent
java -cp "$OUT/interview" L2_RiskLimitEngineOnBus
java -cp "$OUT/interview" L3_RiskLimitEnginePartitioned
java -cp "$OUT/interview" L4_RiskLimitEnginePartitionedOwner
java -cp "$OUT/interview" MatchingEngineInterview

echo "Verification complete."
