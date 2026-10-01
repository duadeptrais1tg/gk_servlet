package com.vn.thu.controller;

import java.io.IOException;
import java.util.List;

import com.vn.thu.config.Constant_24162126;
import com.vn.thu.entity.Book_24162126;
import com.vn.thu.service.IBookService_24162126;
import com.vn.thu.service.impl.BookServiceImpl_24162126;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Trang chu: hien thi tat ca sach, phan trang 6 sach / trang */
@WebServlet(urlPatterns = { "", "/home", "/books" })
public class HomeController_24162126 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final IBookService_24162126 bookService = new BookServiceImpl_24162126();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int pageSize = Constant_24162126.HOME_PAGE_SIZE;
		int totalPages = bookService.totalPages(pageSize);
		int page = parsePage(req.getParameter("page"), totalPages);

		List<Book_24162126> books = bookService.findPage(page, pageSize);
		req.setAttribute("books", books);
		req.setAttribute("reviewCounts", bookService.countReviews(books));
		req.setAttribute("currentPage", page);
		req.setAttribute("totalPages", totalPages);
		req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
	}

	static int parsePage(String value, int totalPages) {
		int page = 1;
		try {
			page = Integer.parseInt(value);
		} catch (Exception ignored) {
		}
		return Math.max(1, Math.min(page, totalPages));
	}
}
