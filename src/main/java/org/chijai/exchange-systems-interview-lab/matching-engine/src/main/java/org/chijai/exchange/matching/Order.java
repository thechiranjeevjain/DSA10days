package org.chijai.exchange;

public final class Order {

    private final long id;
    private final String trader;
    private final Side side;
    private final long price;
    private long remainingQuantity;

    public Order(
            long id,
            String trader,
            Side side,
            long price,
            long quantity
    ) {
        if (price < 0) {
            throw new IllegalArgumentException("price must be >= 0");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }

        this.id = id;
        this.trader = trader;
        this.side = side;
        this.price = price;
        this.remainingQuantity = quantity;
    }

    public long id() {
        return id;
    }

    public String trader() {
        return trader;
    }

    public Side side() {
        return side;
    }

    public long price() {
        return price;
    }

    public long remainingQuantity() {
        return remainingQuantity;
    }

    public boolean isMarket() {
        return price == 0;
    }

    void fill(long quantity) {
        remainingQuantity -= quantity;
    }
}
