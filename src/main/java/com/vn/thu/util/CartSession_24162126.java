package com.vn.thu.util;

import java.util.UUID;
import com.vn.thu.model.Cart_24162126;
import jakarta.servlet.http.HttpSession;

public final class CartSession_24162126 {
    private CartSession_24162126() { }

    public static void ensureToken(HttpSession session) {
        synchronized (session) {
            if (session.getAttribute("cartToken") == null) {
                session.setAttribute("cartToken", UUID.randomUUID().toString());
            }
        }
    }

    public static Cart_24162126 getCart(HttpSession session) {
        synchronized (session) {
            Cart_24162126 cart = (Cart_24162126) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart_24162126();
                session.setAttribute("cart", cart);
            }
            return cart;
        }
    }
}
