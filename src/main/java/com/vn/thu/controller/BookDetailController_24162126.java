package com.vn.thu.controller;

import java.io.IOException;

import com.vn.thu.entity.Book_24162126;
import com.vn.thu.service.IBookService_24162126;
import com.vn.thu.service.impl.BookServiceImpl_24162126;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Trang chi tiet 1 cuon sach + danh sach review + form them review */
@WebServlet(urlPatterns = { "/book" })
public class BookDetailController_24162126 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final IBookService_24162126 bookService = new BookServiceImpl_24162126();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Book_24162126 book = null;
		try {
			book = bookService.findById(Integer.parseInt(req.getParameter("id")));
		} catch (NumberFormatException ignored) {
		}
		if (book == null) {
			resp.sendRedirect(req.getContextPath() + "/");
			return;
		}
		req.setAttribute("book", book);
		req.setAttribute("reviews", bookService.findReviews(book.getBookid()));
		req.setAttribute("reviewCount", bookService.countReviews(book.getBookid()));
		req.getRequestDispatcher("/WEB-INF/views/book-detail.jsp").forward(req, resp);
	}
}
