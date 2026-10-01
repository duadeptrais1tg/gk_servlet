package com.vn.thu.service.impl;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import com.vn.thu.config.JpaConfig_24162126;
import com.vn.thu.entity.Book_24162126;
import com.vn.thu.entity.Order_24162126;
import com.vn.thu.model.CartItem_24162126;
import com.vn.thu.model.ShippingDetails_24162126;
import jakarta.persistence.*;

public class OrderService_24162126 {
    private final Supplier<EntityManager> entityManagers;
    public OrderService_24162126() { this(JpaConfig_24162126::getEntityManager); }
    public OrderService_24162126(Supplier<EntityManager> entityManagers) { this.entityManagers = entityManagers; }

    public Order_24162126 placeCodOrder(String token, String ownerKey, Integer userId,
            ShippingDetails_24162126 shipping, List<CartItem_24162126> items) {
        UUID.fromString(token);
        UUID.fromString(ownerKey);
        shipping.validate();
        EntityManager em = entityManagers.get();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Order_24162126 existing = findByToken(em, token, ownerKey);
            if (existing != null) {
                tx.commit();
                return existing;
            }
            if (items == null || items.isEmpty()) throw new IllegalArgumentException("Giỏ hàng đang trống.");
            Order_24162126 order = new Order_24162126(token, ownerKey, userId, shipping);
            var bookIds = new HashSet<Integer>();
            // Consistent lock order avoids deadlocks between carts containing the same books.
            for (CartItem_24162126 item : items.stream().sorted(Comparator.comparingInt(CartItem_24162126::getBookId)).toList()) {
                if (item.getQuantity() < 1 || !bookIds.add(item.getBookId())) {
                    throw new IllegalArgumentException("Giỏ hàng không hợp lệ. Vui lòng kiểm tra lại.");
                }
                Book_24162126 book = em.find(Book_24162126.class, item.getBookId(), LockModeType.PESSIMISTIC_WRITE);
                if (book == null || book.getQuantity() == null || book.getQuantity() < item.getQuantity()) {
                    throw new IllegalArgumentException("Sách trong giỏ không còn đủ tồn kho. Vui lòng quay lại giỏ hàng để kiểm tra.");
                }
                if (book.getPrice() == null || book.getPrice().signum() < 0
                        || book.getPrice().compareTo(item.getPrice()) != 0) {
                    throw new IllegalArgumentException("Giá sách đã thay đổi. Vui lòng quay lại giỏ hàng để xem giá mới trước khi đặt.");
                }
                order.addItem(book, item.getQuantity());
                book.setQuantity(book.getQuantity() - item.getQuantity());
            }
            em.persist(order);
            tx.commit();
            return order;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            // A concurrent retry may have already committed the same unique checkout token.
            if (ex instanceof PersistenceException) {
                EntityManager retry = entityManagers.get();
                try {
                    Order_24162126 existing = findByToken(retry, token, ownerKey);
                    if (existing != null) return existing;
                } finally { retry.close(); }
            }
            throw ex;
        } finally { em.close(); }
    }

    private Order_24162126 findByToken(EntityManager em, String token, String ownerKey) {
        return em.createQuery("SELECT DISTINCT o FROM Order_24162126 o LEFT JOIN FETCH o.items "
                + "WHERE o.checkoutToken = :token AND o.ownerKey = :owner", Order_24162126.class)
                .setParameter("token", token).setParameter("owner", ownerKey)
                .getResultStream().findFirst().orElse(null);
    }

    public Order_24162126 findForOwner(long id, String ownerKey) {
        if (ownerKey == null) return null;
        EntityManager em = entityManagers.get();
        try {
            return em.createQuery("SELECT DISTINCT o FROM Order_24162126 o LEFT JOIN FETCH o.items "
                    + "WHERE o.id = :id AND o.ownerKey = :owner", Order_24162126.class)
                    .setParameter("id", id).setParameter("owner", ownerKey)
                    .getResultStream().findFirst().orElse(null);
        } finally { em.close(); }
    }
}
