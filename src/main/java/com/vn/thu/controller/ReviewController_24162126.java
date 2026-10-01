package com.vn.thu.controller;

import java.io.IOException;

import com.vn.thu.config.Constant_24162126;
import com.vn.thu.entity.User_24162126;
import com.vn.thu.service.IBookService_24162126;
import com.vn.thu.service.impl.BookServiceImpl_24162126;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Them review cho sach (phai dang nhap). Moi user 1 review / sach, gui lai se cap nhat */
@WebServlet(urlPatterns = { "/book/review" })
public class ReviewController_24162126 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final IBookService_24162126 bookService = new BookServiceImpl_24162126();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User_24162126 account = (User_24162126) req.getSession().getAttribute(Constant_24162126.SESSION_ACCOUNT);
		if (account == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		int bookId;
		int rating;
		try {
			bookId = Integer.parseInt(req.getParameter("bookId"));
			rating = Integer.parseInt(req.getParameter("rating"));
		} catch (NumberFormatException e) {
			resp.sendRedirect(req.getContextPath() + "/");
			return;
		}
		String reviewText = req.getParameter("reviewText");
		if (bookService.findById(bookId) == null || reviewText == null || reviewText.isBlank()) {
			resp.sendRedirect(req.getContextPath() + "/book?id=" + bookId);
			return;
		}
		bookService.saveReview(account.getId(), bookId, rating, reviewText.trim());
		req.getSession().setAttribute("message", "Đã lưu review của bạn");
		resp.sendRedirect(req.getContextPath() + "/book?id=" + bookId);
	}
}
