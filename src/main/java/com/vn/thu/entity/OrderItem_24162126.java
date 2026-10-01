package com.vn.thu.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem_24162126 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order_24162126 order;
    // Historical snapshot remains valid if the catalog book is edited or deleted.
    @Column(name = "book_id", nullable = false)
    private Integer bookId;
    @Column(length = 200)
    private String title;
    @Column(nullable = false, precision = 24, scale = 2)
    private BigDecimal price;
    @Column(nullable = false)
    private int quantity;

    protected OrderItem_24162126() { }
    OrderItem_24162126(Order_24162126 order, Book_24162126 book, int quantity) {
        this.order = order;
        this.bookId = book.getBookid();
        this.title = book.getTitle();
        this.price = book.getPrice();
        this.quantity = quantity;
    }
    public Integer getBookId() { return bookId; }
    public String getTitle() { return title; }
    public BigDecimal getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public BigDecimal getSubtotal() { return price.multiply(BigDecimal.valueOf(quantity)); }
}
