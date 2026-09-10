package org.chijai.exchange;

public record RiskDecision(boolean accepted, String reason) {

    public static RiskDecision accept() {
        return new RiskDecision(true, "OK");
    }

    public static RiskDecision reject(String reason) {
        return new RiskDecision(false, reason);
    }
}
