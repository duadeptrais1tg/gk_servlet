package com.vn.thu.controller;

import java.io.IOException;
import com.vn.thu.entity.Book_24162126;
import com.vn.thu.model.Cart_24162126;
import com.vn.thu.service.IBookService_24162126;
import com.vn.thu.service.impl.BookServiceImpl_24162126;
import com.vn.thu.util.CartSession_24162126;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/cart" })
public class CartController_24162126 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IBookService_24162126 bookService = new BookServiceImpl_24162126();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        CartSession_24162126.ensureToken(session);
        synchronized (session) {
            Cart_24162126 cart = CartSession_24162126.getCart(session);
            if (cart.refresh(bookService::findById)) {
                req.setAttribute("cartWarning", "Giỏ hàng đã được điều chỉnh theo giá và tồn kho hiện tại; sách không còn bán đã được xóa.");
            }
            req.setAttribute("cartItems", cart.getItems());
            req.setAttribute("cartTotal", cart.getTotal());
            req.setAttribute("cartMessage", session.getAttribute("cartMessage"));
            req.setAttribute("cartError", session.getAttribute("cartError"));
            session.removeAttribute("cartMessage");
            session.removeAttribute("cartError");
        }
        resp.setHeader("Cache-Control", "no-store");
        req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("cartToken") == null
                || !session.getAttribute("cartToken").equals(req.getParameter("cartToken"))) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Phiên thao tác không hợp lệ. Vui lòng tải lại trang.");
            return;
        }
        synchronized (session) {
            session.removeAttribute("cartMessage");
            session.removeAttribute("cartError");
            try {
                String action = req.getParameter("action");
                if (!"add".equals(action) && !"update".equals(action) && !"remove".equals(action)) {
                    throw new IllegalArgumentException("Thao tác giỏ hàng không hợp lệ.");
                }
                int bookId = positiveInteger(req.getParameter("bookId"), "Mã sách");
                Cart_24162126 cart = CartSession_24162126.getCart(session);
                if ("remove".equals(action)) {
                    cart.remove(bookId);
                    session.setAttribute("cartMessage", "Đã xóa sách khỏi giỏ hàng.");
                } else {
                    int quantity = positiveInteger(req.getParameter("quantity"), "Số lượng");
                    Book_24162126 book = bookService.findById(bookId);
                    if ("add".equals(action)) {
                        cart.add(book, quantity);
                        session.setAttribute("cartMessage", "Đã thêm sách vào giỏ hàng.");
                    } else {
                        cart.update(book, quantity);
                        session.setAttribute("cartMessage", "Đã cập nhật số lượng.");
                    }
                }
            } catch (IllegalArgumentException ex) {
                session.setAttribute("cartError", ex.getMessage());
            }
        }
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private static int positiveInteger(String value, String label) {
        try {
            int result = Integer.parseInt(value);
            if (result > 0) { return result; }
        } catch (NumberFormatException ignored) { }
        throw new IllegalArgumentException(label + " phải là số nguyên dương hợp lệ.");
    }
}
