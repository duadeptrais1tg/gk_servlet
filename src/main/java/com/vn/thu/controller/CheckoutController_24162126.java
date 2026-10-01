package com.vn.thu.controller;

import java.io.IOException;
import java.util.UUID;
import com.vn.thu.config.Constant_24162126;
import com.vn.thu.entity.Order_24162126;
import com.vn.thu.entity.User_24162126;
import com.vn.thu.model.Cart_24162126;
import com.vn.thu.model.CheckoutDraft_24162126;
import com.vn.thu.model.ShippingDetails_24162126;
import com.vn.thu.service.impl.BookServiceImpl_24162126;
import com.vn.thu.service.impl.OrderService_24162126;
import com.vn.thu.util.CartSession_24162126;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(urlPatterns = { "/checkout", "/order" })
public class CheckoutController_24162126 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderService_24162126 orders = new OrderService_24162126();
    private final BookServiceImpl_24162126 books = new BookServiceImpl_24162126();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Cache-Control", "no-store");
        if ("/order".equals(req.getServletPath())) {
            showOrder(req, resp);
            return;
        }
        HttpSession session = req.getSession();
        synchronized (session) {
            CartSession_24162126.ensureToken(session);
            Cart_24162126 cart = CartSession_24162126.getCart(session);
            if (cart.refresh(books::findById)) {
                req.setAttribute("checkoutWarning", "Giỏ hàng đã được điều chỉnh theo giá và tồn kho hiện tại. Vui lòng kiểm tra trước khi đặt.");
            }
            if (cart.getItems().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }
            if (session.getAttribute("orderOwnerKey") == null) {
                session.setAttribute("orderOwnerKey", UUID.randomUUID().toString());
            }
            CheckoutDraft_24162126 draft = new CheckoutDraft_24162126(cart);
            session.setAttribute("checkoutDraft", draft);
            User_24162126 account = (User_24162126) session.getAttribute(Constant_24162126.SESSION_ACCOUNT);
            render(req, resp, draft, new ShippingDetails_24162126(account == null ? "" : account.getFullname(), "", "", ""));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!"/checkout".equals(req.getServletPath())) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        req.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-store");
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Phiên đặt hàng đã hết hạn. Vui lòng mở lại giỏ hàng.");
            return;
        }
        synchronized (session) {
            if (session.getAttribute("cartToken") == null
                    || !session.getAttribute("cartToken").equals(req.getParameter("cartToken"))) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            String token = req.getParameter("checkoutToken");
            if (token != null && token.equals(session.getAttribute("completedCheckoutToken"))) {
                resp.sendRedirect(req.getContextPath() + "/order?id=" + session.getAttribute("completedOrderId"));
                return;
            }
            CheckoutDraft_24162126 draft = (CheckoutDraft_24162126) session.getAttribute("checkoutDraft");
            if (draft == null || !draft.getToken().equals(token)
                    || !draft.matches(CartSession_24162126.getCart(session))) {
                session.setAttribute("cartError", "Giỏ hàng hoặc trang thanh toán đã thay đổi. Vui lòng kiểm tra và thanh toán lại.");
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }
            ShippingDetails_24162126 shipping = new ShippingDetails_24162126(req.getParameter("recipientName"),
                    req.getParameter("phone"), req.getParameter("address"), req.getParameter("note"));
            try {
                User_24162126 account = (User_24162126) session.getAttribute(Constant_24162126.SESSION_ACCOUNT);
                Order_24162126 order = orders.placeCodOrder(token, (String) session.getAttribute("orderOwnerKey"),
                        account == null ? null : account.getId(), shipping, draft.getItems());
                session.setAttribute("completedCheckoutToken", token);
                session.setAttribute("completedOrderId", order.getId());
                session.setAttribute("orderSuccessId", order.getId());
                session.removeAttribute("checkoutDraft");
                session.removeAttribute("cart");
                session.removeAttribute("cartMessage");
                session.removeAttribute("cartError");
                resp.sendRedirect(req.getContextPath() + "/order?id=" + order.getId());
            } catch (IllegalArgumentException ex) {
                req.setAttribute("checkoutError", ex.getMessage());
                render(req, resp, draft, shipping);
            } catch (RuntimeException ex) {
                getServletContext().log("Không thể tạo đơn COD", ex);
                req.setAttribute("checkoutError", "Chưa thể xác nhận đơn hàng. Vui lòng thử lại; giỏ hàng của bạn vẫn được giữ nguyên.");
                render(req, resp, draft, shipping);
            }
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, CheckoutDraft_24162126 draft,
            ShippingDetails_24162126 shipping) throws ServletException, IOException {
        req.setAttribute("draft", draft);
        req.setAttribute("shipping", shipping);
        req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
    }

    private void showOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Order_24162126 order = null;
        if (session != null) {
            try {
                User_24162126 account = (User_24162126) session.getAttribute(Constant_24162126.SESSION_ACCOUNT);
                order = orders.findForOwner(Long.parseLong(req.getParameter("id")), account == null ? null : account.getId(),
                        (String) session.getAttribute("orderOwnerKey"));
            } catch (NumberFormatException ignored) { }
        }
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        synchronized (session) {
            if (order.getId().equals(session.getAttribute("orderSuccessId"))) {
                req.setAttribute("orderJustPlaced", true);
                session.removeAttribute("orderSuccessId");
            }
        }
        req.setAttribute("order", order);
        req.getRequestDispatcher("/WEB-INF/views/order-success.jsp").forward(req, resp);
    }
}
