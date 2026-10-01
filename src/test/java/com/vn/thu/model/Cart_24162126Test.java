package com.vn.thu.model;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.Test;
import com.vn.thu.entity.Book_24162126;

public class Cart_24162126Test {
    private Book_24162126 book(int id, int stock, String price) {
        Book_24162126 book = new Book_24162126();
        book.setBookid(id);
        book.setTitle("Sách " + id);
        book.setQuantity(stock);
        book.setPrice(new BigDecimal(price));
        return book;
    }

    @Test public void repeatedAddMergesAndTotalsUseDecimalArithmetic() {
        Cart_24162126 cart = new Cart_24162126();
        cart.add(book(1, 5, "0.10"), 2);
        cart.add(book(1, 5, "0.10"), 3);
        cart.add(book(2, 8, "1.25"), 2);
        assertEquals(2, cart.getItems().size());
        assertEquals(7L, cart.getTotalQuantity());
        assertEquals(new BigDecimal("3.00"), cart.getTotal());
    }

    @Test public void cumulativeAddCannotExceedStockAndLeavesCartIntact() {
        Cart_24162126 cart = new Cart_24162126();
        Book_24162126 book = book(1, 5, "12.50");
        cart.add(book, 4);
        assertThrows(IllegalArgumentException.class, () -> cart.add(book, 2));
        assertEquals(4L, cart.getTotalQuantity());
    }

    @Test public void rejectsZeroNegativeAndOverStockQuantities() {
        Cart_24162126 cart = new Cart_24162126();
        Book_24162126 book = book(1, 5, "10.00");
        for (int quantity : new int[] { 0, -1, Integer.MIN_VALUE, 6 }) {
            assertThrows(IllegalArgumentException.class, () -> cart.add(book, quantity));
        }
        assertTrue(cart.getItems().isEmpty());
        cart.add(book, 2);
        for (int quantity : new int[] { 0, -1, Integer.MIN_VALUE, 6 }) {
            assertThrows(IllegalArgumentException.class, () -> cart.update(book, quantity));
        }
        assertEquals(2L, cart.getTotalQuantity());
    }

    @Test public void additionDoesNotOverflowInteger() {
        Cart_24162126 cart = new Cart_24162126();
        Book_24162126 book = book(1, Integer.MAX_VALUE, "1.00");
        cart.add(book, Integer.MAX_VALUE);
        assertThrows(IllegalArgumentException.class, () -> cart.add(book, 1));
        assertEquals((long) Integer.MAX_VALUE, cart.getTotalQuantity());
        cart.add(book(2, 1, "1.00"), 1);
        assertEquals(2147483648L, cart.getTotalQuantity());
    }

    @Test public void updateReplacesQuantityAtBothBoundaries() {
        Cart_24162126 cart = new Cart_24162126();
        Book_24162126 book = book(1, 5, "2.25");
        cart.add(book, 2);
        cart.update(book, 5);
        assertEquals(5L, cart.getTotalQuantity());
        cart.update(book, 1);
        assertEquals(new BigDecimal("2.25"), cart.getTotal());
        assertThrows(IllegalArgumentException.class, () -> cart.update(book(2, 5, "2.25"), 1));
    }

    @Test public void removeIsIdempotentAndEmptyCartTotalsAreZero() {
        Cart_24162126 cart = new Cart_24162126();
        cart.add(book(1, 5, "1.00"), 3);
        cart.remove(1);
        cart.remove(1);
        assertTrue(cart.getItems().isEmpty());
        assertEquals(0L, cart.getTotalQuantity());
        assertEquals(BigDecimal.ZERO, cart.getTotal());
    }

    @Test public void rejectsMissingUnavailableAndUnpricedBooks() {
        Cart_24162126 cart = new Cart_24162126();
        assertThrows(IllegalArgumentException.class, () -> cart.add(null, 1));
        assertThrows(IllegalArgumentException.class, () -> cart.add(book(1, 0, "1.00"), 1));
        assertThrows(IllegalArgumentException.class, () -> cart.add(book(1, -1, "1.00"), 1));
        assertThrows(IllegalArgumentException.class, () -> cart.add(book(1, 1, "-1.00"), 1));
        Book_24162126 book = book(1, 1, "1.00");
        book.setQuantity(null);
        assertThrows(IllegalArgumentException.class, () -> cart.add(book, 1));
        book.setQuantity(1);
        book.setPrice(null);
        assertThrows(IllegalArgumentException.class, () -> cart.add(book, 1));
    }

    @Test public void refreshClampsStockRemovesDeletedBooksAndUpdatesPrices() {
        Cart_24162126 cart = new Cart_24162126();
        for (int id = 1; id <= 3; id++) cart.add(book(id, 5, "1.00"), 4);
        Map<Integer, Book_24162126> catalog = Map.of(1, book(1, 2, "1.50"), 2, book(2, 0, "1.00"));
        assertTrue(cart.refresh(catalog::get));
        assertEquals(1, cart.getItems().size());
        assertEquals(2L, cart.getTotalQuantity());
        assertEquals(new BigDecimal("3.00"), cart.getTotal());
        assertFalse(cart.refresh(catalog::get));
    }

    @Test public void catalogChangesDoNotMutateSessionSnapshotUntilRefresh() {
        Cart_24162126 cart = new Cart_24162126();
        Book_24162126 book = book(1, 5, "2.00");
        cart.add(book, 2);
        book.setPrice(new BigDecimal("3.00"));
        assertEquals(new BigDecimal("4.00"), cart.getTotal());
        assertThrows(UnsupportedOperationException.class, () -> cart.getItems().clear());
        assertTrue(cart.refresh(id -> book));
        assertEquals(new BigDecimal("6.00"), cart.getTotal());
        assertEquals(Integer.valueOf(5), book.getQuantity());
    }
}
