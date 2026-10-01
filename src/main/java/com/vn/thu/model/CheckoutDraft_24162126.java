package com.vn.thu.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/** The exact quantities and prices reviewed on the checkout page. */
public class CheckoutDraft_24162126 implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String token = UUID.randomUUID().toString();
    private final List<CartItem_24162126> items;

    public CheckoutDraft_24162126(Cart_24162126 cart) { items = cart.getItems(); }
    public String getToken() { return token; }
    public List<CartItem_24162126> getItems() { return items; }
    public BigDecimal getTotal() {
        return items.stream().map(CartItem_24162126::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public boolean matches(Cart_24162126 cart) {
        List<CartItem_24162126> current = cart.getItems();
        if (current.size() != items.size()) return false;
        for (int i = 0; i < items.size(); i++) {
            CartItem_24162126 before = items.get(i), now = current.get(i);
            if (before.getBookId() != now.getBookId() || before.getQuantity() != now.getQuantity()
                    || before.getPrice().compareTo(now.getPrice()) != 0) return false;
        }
        return true;
    }
}
