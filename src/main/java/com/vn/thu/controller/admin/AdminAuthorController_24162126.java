package com.vn.thu.controller.admin;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.vn.thu.config.Constant_24162126;
import com.vn.thu.entity.Author_24162126;
import com.vn.thu.service.IAuthorService_24162126;
import com.vn.thu.service.impl.AuthorServiceImpl_24162126;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** CRUD bang Author (co phan trang) */
@WebServlet(urlPatterns = { "/admin/authors", "/admin/authors/view", "/admin/authors/new", "/admin/authors/edit",
		"/admin/authors/save", "/admin/authors/delete" })
public class AdminAuthorController_24162126 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final IAuthorService_24162126 authorService = new AuthorServiceImpl_24162126();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath();
		switch (path) {
		case "/admin/authors/new" -> { // CREATE - form them moi
			req.setAttribute("author", new Author_24162126());
			req.getRequestDispatcher("/WEB-INF/views/admin/author-form.jsp").forward(req, resp);
		}
		case "/admin/authors/edit" -> { // UPDATE - form sua
			Integer id = AdminBookController_24162126.parseInt(req.getParameter("id"));
			Author_24162126 author = id == null ? null : authorService.findById(id);
			if (author == null) {
				resp.sendRedirect(req.getContextPath() + "/admin/authors");
				return;
			}
			req.setAttribute("author", author);
			req.getRequestDispatcher("/WEB-INF/views/admin/author-form.jsp").forward(req, resp);
		}
		case "/admin/authors/view" -> { // READ - xem chi tiet kem sach cua tac gia
			Integer id = AdminBookController_24162126.parseInt(req.getParameter("id"));
			Author_24162126 author = id == null ? null : authorService.findByIdWithBooks(id);
			if (author == null) {
				resp.sendRedirect(req.getContextPath() + "/admin/authors");
				return;
			}
			req.setAttribute("author", author);
			req.getRequestDispatcher("/WEB-INF/views/admin/author-view.jsp").forward(req, resp);
		}
		default -> { // READ - danh sach co phan trang
			int pageSize = Constant_24162126.ADMIN_PAGE_SIZE;
			int totalPages = authorService.totalPages(pageSize);
			int page = AdminBookController_24162126.parsePage(req.getParameter("page"), totalPages);
			List<Author_24162126> authors = authorService.findPage(page, pageSize);
			req.setAttribute("authors", authors);
			req.setAttribute("bookCounts", authorService.countBooks(authors));
			req.setAttribute("currentPage", page);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("totalItems", authorService.count());
			req.getRequestDispatcher("/WEB-INF/views/admin/author-list.jsp").forward(req, resp);
		}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath();
		if ("/admin/authors/delete".equals(path)) { // DELETE
			int id = Integer.parseInt(req.getParameter("id"));
			authorService.delete(id);
			req.getSession().setAttribute("message", "Đã xóa tác giả #" + id);
			resp.sendRedirect(req.getContextPath() + "/admin/authors?page=" + req.getParameter("page"));
			return;
		}
		if ("/admin/authors/save".equals(path)) { // CREATE + UPDATE
			Author_24162126 author = new Author_24162126();
			author.setAuthorId(AdminBookController_24162126.parseInt(req.getParameter("authorId")));
			author.setAuthorName(req.getParameter("authorName"));
			String dob = req.getParameter("dateOfBirth");
			author.setDateOfBirth(AdminBookController_24162126.isBlank(dob) ? null : LocalDate.parse(dob));

			boolean isNew = author.getAuthorId() == null;
			authorService.save(author);
			req.getSession().setAttribute("message", isNew ? "Thêm tác giả thành công" : "Cập nhật tác giả thành công");
		}
		resp.sendRedirect(req.getContextPath() + "/admin/authors");
	}
}
