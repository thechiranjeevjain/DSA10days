package org.chijai.exchange;

public abstract class AbstractRiskCheck implements RiskCheck {

    protected final String name;
    protected final long limit;
    protected long consumption;

    private long previousConsumption;

    protected AbstractRiskCheck(String name, long limit) {
        this.name = name;
        this.limit = limit;
    }

    @Override
    public void begin() {
        previousConsumption = consumption;
    }

    @Override
    public void rollback() {
        consumption = previousConsumption;
    }

    public long consumption() {
        return consumption;
    }

    public long limit() {
        return limit;
    }

    @Override
    public String rejectionReason() {
        return name + " breach";
    }

    @Override
    public String toString() {
        return name + "[" + consumption + "/" + limit + "]";
    }
}
