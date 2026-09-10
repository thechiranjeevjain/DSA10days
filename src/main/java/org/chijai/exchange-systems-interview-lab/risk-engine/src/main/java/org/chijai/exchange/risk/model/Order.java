package org.chijai.exchange;

import java.util.Objects;

public record Order(
        Account account,
        String ticker,
        Side side,
        long price,
        long quantity
) {
    public Order {
        Objects.requireNonNull(account);
        Objects.requireNonNull(ticker);
        Objects.requireNonNull(side);

        if (price < 0) {
            throw new IllegalArgumentException("price must be >= 0");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
    }

    public long signedQuantity() {
        return side == Side.BUY ? quantity : -quantity;
    }
}
