package com.vn.thu.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import com.vn.thu.entity.Book_24162126;

public class Cart_24162126 implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<Integer, CartItem_24162126> items = new LinkedHashMap<>();

    public synchronized void add(Book_24162126 book, int quantity) {
        requireAvailable(book);
        CartItem_24162126 current = items.get(book.getBookid());
        long total = (current == null ? 0L : current.getQuantity()) + quantity;
        if (quantity < 1 || total > book.getQuantity()) {
            throw new IllegalArgumentException("Số lượng phải từ 1 đến " + book.getQuantity()
                    + " và tổng số lượng trong giỏ không được vượt tồn kho.");
        }
        items.put(book.getBookid(), new CartItem_24162126(book, (int) total));
    }

    public synchronized void update(Book_24162126 book, int quantity) {
        requireAvailable(book);
        if (!items.containsKey(book.getBookid())) {
            throw new IllegalArgumentException("Sách không có trong giỏ hàng.");
        }
        if (quantity < 1 || quantity > book.getQuantity()) {
            throw new IllegalArgumentException("Số lượng phải từ 1 đến " + book.getQuantity() + ".");
        }
        items.put(book.getBookid(), new CartItem_24162126(book, quantity));
    }

    public synchronized void remove(int bookId) { items.remove(bookId); }

    /** Reconcile with current catalog before displaying the cart; does not reserve stock. */
    public synchronized boolean refresh(IntFunction<Book_24162126> findBook) {
        boolean adjusted = false;
        Map<Integer, CartItem_24162126> refreshed = new LinkedHashMap<>();
        for (CartItem_24162126 item : items.values()) {
            Book_24162126 book = findBook.apply(item.getBookId());
            if (!isAvailable(book)) {
                adjusted = true;
                continue;
            }
            int quantity = Math.min(item.getQuantity(), book.getQuantity());
            adjusted |= quantity != item.getQuantity() || item.getPrice().compareTo(book.getPrice()) != 0;
            refreshed.put(book.getBookid(), new CartItem_24162126(book, quantity));
        }
        items.clear();
        items.putAll(refreshed);
        return adjusted;
    }

    private static boolean isAvailable(Book_24162126 book) {
        return book != null && book.getBookid() != null && book.getQuantity() != null
                && book.getQuantity() > 0 && book.getPrice() != null && book.getPrice().signum() >= 0;
    }

    private static void requireAvailable(Book_24162126 book) {
        if (!isAvailable(book)) {
            throw new IllegalArgumentException("Sách không tồn tại, đã hết hàng hoặc chưa có giá hợp lệ.");
        }
    }

    public synchronized List<CartItem_24162126> getItems() { return List.copyOf(items.values()); }
    public synchronized long getTotalQuantity() {
        return items.values().stream().mapToLong(CartItem_24162126::getQuantity).sum();
    }
    public synchronized BigDecimal getTotal() {
        return items.values().stream().map(CartItem_24162126::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
