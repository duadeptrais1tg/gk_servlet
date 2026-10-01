package com.vn.thu.controller.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.vn.thu.config.Constant_24162126;
import com.vn.thu.entity.Book_24162126;
import com.vn.thu.service.IAuthorService_24162126;
import com.vn.thu.service.IBookService_24162126;
import com.vn.thu.service.impl.AuthorServiceImpl_24162126;
import com.vn.thu.service.impl.BookServiceImpl_24162126;
import com.vn.thu.util.FileUtil_24162126;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** CRUD bang Books (co phan trang) */
@WebServlet(urlPatterns = { "/admin/books", "/admin/books/view", "/admin/books/new", "/admin/books/edit",
		"/admin/books/save", "/admin/books/delete" })
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
public class AdminBookController_24162126 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final IBookService_24162126 bookService = new BookServiceImpl_24162126();
	private final IAuthorService_24162126 authorService = new AuthorServiceImpl_24162126();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath();
		switch (path) {
		case "/admin/books/new" -> { // CREATE - form them moi
			req.setAttribute("book", new Book_24162126());
			req.setAttribute("authors", authorService.findAll());
			req.getRequestDispatcher("/WEB-INF/views/admin/book-form.jsp").forward(req, resp);
		}
		case "/admin/books/edit", "/admin/books/view" -> { // UPDATE - form sua / READ - xem chi tiet
			Book_24162126 book = findBook(req);
			if (book == null) {
				resp.sendRedirect(req.getContextPath() + "/admin/books");
				return;
			}
			req.setAttribute("book", book);
			if (path.endsWith("/edit")) {
				req.setAttribute("authors", authorService.findAll());
				req.getRequestDispatcher("/WEB-INF/views/admin/book-form.jsp").forward(req, resp);
			} else {
				req.setAttribute("reviewCount", bookService.countReviews(book.getBookid()));
				req.getRequestDispatcher("/WEB-INF/views/admin/book-view.jsp").forward(req, resp);
			}
		}
		default -> { // READ - danh sach co phan trang
			int pageSize = Constant_24162126.ADMIN_PAGE_SIZE;
			int totalPages = bookService.totalPages(pageSize);
			int page = parsePage(req.getParameter("page"), totalPages);
			req.setAttribute("books", bookService.findPage(page, pageSize));
			req.setAttribute("currentPage", page);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("totalItems", bookService.count());
			req.getRequestDispatcher("/WEB-INF/views/admin/book-list.jsp").forward(req, resp);
		}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath();
		if ("/admin/books/delete".equals(path)) { // DELETE
			int id = Integer.parseInt(req.getParameter("id"));
			bookService.delete(id);
			req.getSession().setAttribute("message", "Đã xóa sách #" + id);
			resp.sendRedirect(req.getContextPath() + "/admin/books?page=" + req.getParameter("page"));
			return;
		}
		if ("/admin/books/save".equals(path)) { // CREATE + UPDATE
			Book_24162126 book = new Book_24162126();
			String id = req.getParameter("bookid");
			book.setBookid(isBlank(id) ? null : Integer.valueOf(id));
			book.setTitle(req.getParameter("title"));
			book.setIsbn(parseInt(req.getParameter("isbn")));
			book.setPublisher(req.getParameter("publisher"));
			book.setPrice(isBlank(req.getParameter("price")) ? null : new BigDecimal(req.getParameter("price")));
			book.setPublishDate(isBlank(req.getParameter("publishDate")) ? null
					: LocalDate.parse(req.getParameter("publishDate")));
			book.setQuantity(parseInt(req.getParameter("quantity")));
			book.setDescription(req.getParameter("description"));
			book.setCoverImage(FileUtil_24162126.saveCover(req.getPart("coverFile"))); // null = giu anh cu

			List<Integer> authorIds = new ArrayList<>();
			String[] values = req.getParameterValues("authorIdList");
			if (values != null) {
				for (String v : values) {
					authorIds.add(Integer.valueOf(v));
				}
			}

			boolean isNew = book.getBookid() == null;
			bookService.save(book, authorIds);
			req.getSession().setAttribute("message", isNew ? "Thêm sách thành công" : "Cập nhật sách thành công");
		}
		resp.sendRedirect(req.getContextPath() + "/admin/books");
	}

	private Book_24162126 findBook(HttpServletRequest req) {
		Integer id = parseInt(req.getParameter("id"));
		return id == null ? null : bookService.findById(id);
	}

	static int parsePage(String value, int totalPages) {
		Integer page = parseInt(value);
		return Math.max(1, Math.min(page == null ? 1 : page, totalPages));
	}

	static Integer parseInt(String value) {
		try {
			return Integer.valueOf(value.trim());
		} catch (Exception e) {
			return null;
		}
	}

	static boolean isBlank(String s) {
		return s == null || s.isBlank();
	}
}
