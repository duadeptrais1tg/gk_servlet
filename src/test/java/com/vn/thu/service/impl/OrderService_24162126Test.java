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
import com.vn.thu.model.OrderStatus_24162126;
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
    private void updateStatusInDatabase(long id, String status) {
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNativeQuery("UPDATE customer_orders SET status = :status WHERE id = :id")
                    .setParameter("status", status).setParameter("id", id).executeUpdate();
            em.getTransaction().commit();
        } finally { em.close(); }
    }

    @Test public void sqlStatusChangesMoveOrderBetweenAllEightFiltersAndRefreshDetail() {
        Order_24162126 order = place(cart(first, 1));
        OrderStatus_24162126 previous = null;
        for (OrderStatus_24162126 status : OrderStatus_24162126.values()) {
            updateStatusInDatabase(order.getId(), status.getCode());
            assertEquals(1, service.countHistory(null, owner, status));
            var history = service.findHistory(null, owner, status, 1, 10);
            assertEquals(1, history.size());
            assertEquals(order.getId(), history.get(0).getId());
            assertEquals(status.getLabel(), history.get(0).getStatusLabel());
            assertEquals(status.getCode(), service.findForOwner(order.getId(), owner).getStatus());
            if (previous != null) {
                assertEquals(0, service.countHistory(null, owner, previous));
                assertTrue(service.findHistory(null, owner, previous, 1, 10).isEmpty());
            }
            previous = status;
        }
        assertEquals(1, service.countHistory(null, owner, null));
        assertEquals(4, stock(first));
    }

    @Test public void accountHistorySurvivesNewSessionAndDoesNotLeakAcrossAccounts() {
        Order_24162126 accountOrder = service.placeCodOrder(UUID.randomUUID().toString(), owner, 101,
                shipping, cart(first, 1).getItems());
        Order_24162126 guestOrder = place(cart(first, 1));
        String newSession = UUID.randomUUID().toString();
        assertEquals(1, service.countHistory(101, newSession, null));
        assertEquals(accountOrder.getId(), service.findHistory(101, newSession, null, 1, 10).get(0).getId());
        assertNotNull(service.findForOwner(accountOrder.getId(), 101, newSession));
        assertNotNull(service.findForOwner(accountOrder.getId(), 101, null));
        assertEquals(2, service.countHistory(101, owner, null));
        assertEquals(1, service.countHistory(202, owner, null));
        assertEquals(guestOrder.getId(), service.findHistory(202, owner, null, 1, 10).get(0).getId());
        assertNull(service.findForOwner(accountOrder.getId(), 202, owner));
        assertNull(service.findForOwner(accountOrder.getId(), null, owner));
        assertEquals(0, service.countHistory(202, newSession, null));
    }

    @Test public void guestsOnlySeeOwnOrdersAndAnonymousRequestsSeeNothing() {
        Order_24162126 own = place(cart(first, 1));
        String stranger = UUID.randomUUID().toString();
        service.placeCodOrder(UUID.randomUUID().toString(), stranger, null, shipping, cart(first, 1).getItems());
        assertEquals(1, service.countHistory(null, owner, null));
        assertEquals(own.getId(), service.findHistory(null, owner, null, 1, 10).get(0).getId());
        assertNull(service.findForOwner(own.getId(), stranger));
        assertEquals(0, service.countHistory(null, null, null));
        assertTrue(service.findHistory(null, null, null, 1, 10).isEmpty());
    }

    @Test public void historyPaginationIsNewestFirstAndFilterIsAppliedBeforePagination() {
        Order_24162126 old = place(cart(first, 1));
        Order_24162126 middle = place(cart(first, 1));
        Order_24162126 newest = place(cart(first, 1));
        updateStatusInDatabase(old.getId(), "CONFIRMED");
        updateStatusInDatabase(newest.getId(), "CONFIRMED");
        var page1 = service.findHistory(null, owner, null, 1, 2);
        var page2 = service.findHistory(null, owner, null, 2, 2);
        assertEquals(List.of(newest.getId(), middle.getId()), page1.stream().map(Order_24162126::getId).toList());
        assertEquals(List.of(old.getId()), page2.stream().map(Order_24162126::getId).toList());
        assertEquals(2, service.countHistory(null, owner, OrderStatus_24162126.CONFIRMED));
        assertEquals(old.getId(), service.findHistory(null, owner, OrderStatus_24162126.CONFIRMED, 2, 1).get(0).getId());
        assertThrows(IllegalArgumentException.class, () -> service.findHistory(null, owner, null, 0, 10));
    }

    @Test public void unknownDatabaseStatusRemainsVisibleInAllOrdersWithoutBreakingDetail() {
        Order_24162126 order = place(cart(first, 1));
        updateStatusInDatabase(order.getId(), "INVALID_STATUS");
        assertEquals(1, service.countHistory(null, owner, null));
        assertEquals(0, service.countHistory(null, owner, OrderStatus_24162126.PENDING));
        assertEquals("Trạng thái không xác định", service.findForOwner(order.getId(), owner).getStatusLabel());
    }

    @Test public void paymentStatusComesFromDatabaseIndependentlyOfDeliveryStatus() {
        Order_24162126 order = place(cart(first, 1));
        updateStatusInDatabase(order.getId(), "DELIVERED");
        assertEquals("Chưa thanh toán", service.findForOwner(order.getId(), owner).getPaymentStatusLabel());
        EntityManager em = factory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNativeQuery("UPDATE customer_orders SET payment_status = 'PAID' WHERE id = :id")
                    .setParameter("id", order.getId()).executeUpdate();
            em.getTransaction().commit();
        } finally { em.close(); }
        assertEquals("Đã thanh toán", service.findForOwner(order.getId(), owner).getPaymentStatusLabel());
    }
}
