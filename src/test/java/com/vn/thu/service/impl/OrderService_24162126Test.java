package com.vn.thu.service.impl;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import com.vn.thu.entity.Book_24162126;
import com.vn.thu.entity.Order_24162126;
import com.vn.thu.model.Cart_24162126;
import com.vn.thu.model.CheckoutDraft_24162126;
import com.vn.thu.model.ShippingDetails_24162126;
import jakarta.persistence.*;
import org.junit.*;

public class OrderService_24162126Test {
    private static EntityManagerFactory factory;
    private OrderService_24162126 service;
    private Book_24162126 first, second;
    private final String owner = UUID.randomUUID().toString();
    private final ShippingDetails_24162126 shipping = new ShippingDetails_24162126(
            " Nguyễn Văn A ", "0901234567", "12 Nguyễn Trãi, TP Hồ Chí Minh", "Giao giờ hành chính");

    @BeforeClass public static void openDatabase() {
        factory = Persistence.createEntityManagerFactory("bookstore", Map.of(
                "jakarta.persistence.jdbc.driver", "org.h2.Driver",
                "jakarta.persistence.jdbc.url", "jdbc:h2:mem:cod_test;MODE=MySQL;DB_CLOSE_DELAY=-1;LOCK_TIMEOUT=5000",
                "jakarta.persistence.jdbc.user", "sa",
                "jakarta.persistence.jdbc.password", "",
                "hibernate.dialect", "org.hibernate.dialect.H2Dialect",
                "hibernate.hbm2ddl.auto", "create-drop",
                "hibernate.show_sql", "false"));
    }
    @AfterClass public static void closeDatabase() { if (factory != null) factory.close(); }
    @Before public void seed() {
        service = new OrderService_24162126(factory::createEntityManager);
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM OrderItem_24162126").executeUpdate();
            em.createQuery("DELETE FROM Order_24162126").executeUpdate();
            em.createQuery("DELETE FROM Book_24162126").executeUpdate();
            first = book("Sách A", 5, "12.50");
            second = book("Sách B", 1, "0.10");
            em.persist(first);
            em.persist(second);
            em.getTransaction().commit();
        } finally { em.close(); }
    }
    private Book_24162126 book(String title, int stock, String price) {
        Book_24162126 book = new Book_24162126();
        book.setTitle(title);
        book.setQuantity(stock);
        book.setPrice(new BigDecimal(price));
        return book;
    }
    private Cart_24162126 cart(Book_24162126 book, int quantity) {
        Cart_24162126 cart = new Cart_24162126();
        cart.add(book, quantity);
        return cart;
    }
    private Order_24162126 place(Cart_24162126 cart) {
        return service.placeCodOrder(UUID.randomUUID().toString(), owner, null, shipping, cart.getItems());
    }
    private int stock(Book_24162126 book) {
        EntityManager em = factory.createEntityManager();
        try { return em.find(Book_24162126.class, book.getBookid()).getQuantity(); }
        finally { em.close(); }
    }
    private long orderCount() {
        EntityManager em = factory.createEntityManager();
        try { return em.createQuery("SELECT COUNT(o) FROM Order_24162126 o", Long.class).getSingleResult(); }
        finally { em.close(); }
    }
    private void changeStockOrPrice(Book_24162126 book, Integer stock, String price) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            Book_24162126 current = em.find(Book_24162126.class, book.getBookid());
            if (stock != null) current.setQuantity(stock);
            if (price != null) current.setPrice(new BigDecimal(price));
            em.getTransaction().commit();
        } finally { em.close(); }
    }

    @Test public void persistsCodOrderAndDeductsStockWithExactTotals() {
        Cart_24162126 cart = cart(first, 2);
        cart.add(second, 1);
        Order_24162126 order = place(cart);
        Order_24162126 stored = service.findForOwner(order.getId(), owner);
        assertEquals("COD", stored.getPaymentMethod());
        assertEquals("UNPAID", stored.getPaymentStatus());
        assertEquals("PENDING", stored.getStatus());
        assertEquals("Nguyễn Văn A", stored.getRecipientName());
        assertEquals("0901234567", stored.getPhone());
        assertEquals(new BigDecimal("25.10"), stored.getTotal());
        assertEquals(2, stored.getItems().size());
        assertEquals(3, stock(first));
        assertEquals(0, stock(second));
    }
    @Test public void insufficientStockRollsBackAllEarlierDeductions() {
        Cart_24162126 cart = cart(first, 2);
        cart.add(second, 1);
        changeStockOrPrice(second, 0, null);
        assertThrows(IllegalArgumentException.class, () -> place(cart));
        assertEquals(5, stock(first));
        assertEquals(0, orderCount());
    }
    @Test public void priceChangeRequiresReviewAndDoesNotCreateOrder() {
        Cart_24162126 cart = cart(first, 2);
        cart.add(second, 1);
        changeStockOrPrice(second, null, "0.20");
        assertThrows(IllegalArgumentException.class, () -> place(cart));
        assertEquals(5, stock(first));
        assertEquals(1, stock(second));
        assertEquals(0, orderCount());
    }
    @Test public void retryReturnsSameOrderWithoutDeductingStockAgain() {
        String token = UUID.randomUUID().toString();
        Cart_24162126 cart = cart(first, 2);
        Order_24162126 firstOrder = service.placeCodOrder(token, owner, null, shipping, cart.getItems());
        Order_24162126 retry = service.placeCodOrder(token, owner, null, shipping, List.of());
        assertEquals(firstOrder.getId(), retry.getId());
        assertEquals(1, orderCount());
        assertEquals(3, stock(first));
    }
    @Test public void emptyCartCannotBeOrdered() {
        assertThrows(IllegalArgumentException.class, () -> place(new Cart_24162126()));
        assertEquals(0, orderCount());
    }
    @Test public void invalidShippingDoesNotModifyDatabase() {
        for (ShippingDetails_24162126 invalid : List.of(
                new ShippingDetails_24162126("", "0901234567", shipping.getAddress(), ""),
                new ShippingDetails_24162126("An", "abc", shipping.getAddress(), ""),
                new ShippingDetails_24162126("An", "0901234567", "", ""),
                new ShippingDetails_24162126("An", "0901234567", shipping.getAddress(), "x".repeat(1001)))) {
            assertThrows(IllegalArgumentException.class, () -> service.placeCodOrder(
                    UUID.randomUUID().toString(), owner, null, invalid, cart(first, 1).getItems()));
        }
        assertEquals(5, stock(first));
        assertEquals(0, orderCount());
    }
    @Test public void onlyOwningSessionCanReadOrderAndSnapshotSurvivesCatalogDeletion() {
        Order_24162126 order = place(cart(first, 1));
        assertNull(service.findForOwner(order.getId(), UUID.randomUUID().toString()));
        assertNull(service.findForOwner(order.getId(), null));
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.find(Book_24162126.class, first.getBookid()));
            em.getTransaction().commit();
        } finally { em.close(); }
        Order_24162126 stored = service.findForOwner(order.getId(), owner);
        assertEquals("Sách A", stored.getItems().get(0).getTitle());
        assertEquals(new BigDecimal("12.50"), stored.getTotal());
    }
    @Test(timeout = 15000) public void concurrentBuyersCannotOversellLastBook() throws Exception {
        Cart_24162126 cart = cart(second, 1);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Callable<Boolean> buyer = () -> {
            start.await();
            try { place(cart); return true; }
            catch (IllegalArgumentException unavailable) { return false; }
        };
        try {
            Future<Boolean> a = pool.submit(buyer), b = pool.submit(buyer);
            start.countDown();
            int successes = (a.get(10, TimeUnit.SECONDS) ? 1 : 0) + (b.get(10, TimeUnit.SECONDS) ? 1 : 0);
            assertEquals(1, successes);
            assertEquals(0, stock(second));
            assertEquals(1, orderCount());
        } finally { pool.shutdownNow(); }
    }
    @Test public void draftRejectsCartChangesInAnotherTab() {
        Cart_24162126 cart = cart(first, 1);
        CheckoutDraft_24162126 draft = new CheckoutDraft_24162126(cart);
        assertTrue(draft.matches(cart));
        cart.update(first, 2);
        assertFalse(draft.matches(cart));
        cart.update(first, 1);
        first.setPrice(new BigDecimal("15.00"));
        cart.update(first, 1);
        assertFalse(draft.matches(cart));
    }
}
