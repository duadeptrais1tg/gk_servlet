package com.vn.thu.model;

import java.io.Serializable;
import java.math.BigDecimal;
import com.vn.thu.entity.Book_24162126;

/** Immutable snapshot: no managed JPA entities are stored in the session. */
public class CartItem_24162126 implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int bookId;
    private final String title;
    private final BigDecimal price;
    private final int stock;
    private final int quantity;

    CartItem_24162126(Book_24162126 book, int quantity) {
        this.bookId = book.getBookid();
        this.title = book.getTitle();
        this.price = book.getPrice();
        this.stock = book.getQuantity();
        this.quantity = quantity;
    }

    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public int getQuantity() { return quantity; }
    public BigDecimal getSubtotal() { return price.multiply(BigDecimal.valueOf(quantity)); }
}
