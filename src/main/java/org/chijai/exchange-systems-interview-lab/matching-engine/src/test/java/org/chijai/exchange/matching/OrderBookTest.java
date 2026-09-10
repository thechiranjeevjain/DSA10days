package org.chijai.exchange;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderBookTest {

    @Test
    void aggressiveBuyExecutesAtRestingAskPrice() {
        OrderBook book = new OrderBook();

        book.enter(new Order(1, "Seller", Side.SELL, 100, 50));

        List<Trade> trades =
                book.enter(new Order(2, "Buyer", Side.BUY, 105, 20));

        assertEquals(1, trades.size());
        assertEquals(100, trades.get(0).price());
        assertEquals(20, trades.get(0).quantity());
    }

    @Test
    void aggressiveSellExecutesAtRestingBidPrice() {
        OrderBook book = new OrderBook();

        book.enter(new Order(1, "Buyer", Side.BUY, 100, 50));

        List<Trade> trades =
                book.enter(new Order(2, "Seller", Side.SELL, 95, 20));

        assertEquals(100, trades.get(0).price());
    }

    @Test
    void fifoWithinSamePriceLevel() {
        OrderBook book = new OrderBook();

        book.enter(new Order(1, "First", Side.SELL, 100, 10));
        book.enter(new Order(2, "Second", Side.SELL, 100, 10));

        List<Trade> trades =
                book.enter(new Order(3, "Buyer", Side.BUY, 100, 15));

        assertEquals(2, trades.size());
        assertEquals(1, trades.get(0).sellOrderId());
        assertEquals(2, trades.get(1).sellOrderId());
        assertEquals(10, trades.get(0).quantity());
        assertEquals(5, trades.get(1).quantity());
    }

    @Test
    void marketOrderRemainderDoesNotRest() {
        OrderBook book = new OrderBook();

        book.enter(new Order(1, "Seller", Side.SELL, 100, 10));

        book.enter(new Order(2, "Buyer", Side.BUY, 0, 50));

        assertTrue(book.bestBid().isEmpty());
        assertTrue(book.bestAsk().isEmpty());
        assertEquals(10, book.trades().get(0).quantity());
    }
}
