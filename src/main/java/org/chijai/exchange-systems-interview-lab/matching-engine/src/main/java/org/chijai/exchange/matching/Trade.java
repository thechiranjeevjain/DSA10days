package org.chijai.exchange;

public record Trade(
        long price,
        long quantity,
        long buyOrderId,
        long sellOrderId
) {
}
