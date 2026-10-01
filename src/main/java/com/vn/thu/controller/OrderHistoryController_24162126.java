package com.vn.thu.controller;

import java.io.IOException;
import java.util.List;
import com.vn.thu.config.Constant_24162126;
import com.vn.thu.entity.User_24162126;
import com.vn.thu.model.OrderStatus_24162126;
import com.vn.thu.service.impl.OrderService_24162126;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(urlPatterns = { "/orders" })
public class OrderHistoryController_24162126 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 10;
    private final OrderService_24162126 orders = new OrderService_24162126();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Cache-Control", "no-store");
        String code = req.getParameter("status");
        OrderStatus_24162126 status = OrderStatus_24162126.fromCode(code);
        if (code != null && !code.isEmpty() && status == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Trạng thái đơn hàng không hợp lệ.");
            return;
        }
        HttpSession session = req.getSession(false);
        User_24162126 account = session == null ? null
                : (User_24162126) session.getAttribute(Constant_24162126.SESSION_ACCOUNT);
        Integer userId = account == null ? null : account.getId();
        String ownerKey = session == null ? null : (String) session.getAttribute("orderOwnerKey");
        long total = orders.countHistory(userId, ownerKey, status);
        int totalPages = (int) Math.max(1L, (total + PAGE_SIZE - 1) / PAGE_SIZE);
        int page = 1;
        try { page = Integer.parseInt(req.getParameter("page")); }
        catch (NumberFormatException ignored) { }
        page = Math.max(1, Math.min(page, totalPages));
        req.setAttribute("orders", total == 0 ? List.of() : orders.findHistory(userId, ownerKey, status, page, PAGE_SIZE));
        req.setAttribute("statuses", OrderStatus_24162126.values());
        req.setAttribute("selectedStatus", status == null ? "" : status.getCode());
        req.setAttribute("totalOrders", total);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("/WEB-INF/views/order-history.jsp").forward(req, resp);
    }
}
