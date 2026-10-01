package com.vn.thu.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.vn.thu.model.ShippingDetails_24162126;
import jakarta.persistence.*;

@Entity
@Table(name = "customer_orders")
public class Order_24162126 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "checkout_token", nullable = false, unique = true, length = 36)
    private String checkoutToken;
    @Column(name = "owner_key", nullable = false, length = 36)
    private String ownerKey;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;
    @Column(nullable = false, length = 16)
    private String phone;
    @Column(nullable = false, length = 500)
    private String address;
    @Column(length = 1000)
    private String note;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "payment_method", nullable = false, length = 20)
    private String paymentMethod = "COD";
    @Column(name = "payment_status", nullable = false, length = 20)
    private String paymentStatus = "UNPAID";
    @Column(nullable = false, length = 20)
    private String status = "PENDING";
    @Column(nullable = false, precision = 24, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<OrderItem_24162126> items = new ArrayList<>();

    protected Order_24162126() { }
    public Order_24162126(String token, String ownerKey, Integer userId, ShippingDetails_24162126 shipping) {
        this.checkoutToken = token;
        this.ownerKey = ownerKey;
        this.userId = userId;
        this.recipientName = shipping.getRecipientName();
        this.phone = shipping.getPhone();
        this.address = shipping.getAddress();
        this.note = shipping.getNote();
        this.createdAt = LocalDateTime.now();
    }
    public void addItem(Book_24162126 book, int quantity) {
        OrderItem_24162126 item = new OrderItem_24162126(this, book, quantity);
        items.add(item);
        total = total.add(item.getSubtotal());
    }
    public Long getId() { return id; }
    public String getCheckoutToken() { return checkoutToken; }
    public Integer getUserId() { return userId; }
    public String getRecipientName() { return recipientName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getNote() { return note; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getStatus() { return status; }
    public BigDecimal getTotal() { return total; }
    public List<OrderItem_24162126> getItems() { return List.copyOf(items); }
}
