package com.vn.thu.controller;

import java.io.IOException;

import com.vn.thu.config.Constant_24162126;
import com.vn.thu.entity.User_24162126;
import com.vn.thu.service.IUserService_24162126;
import com.vn.thu.service.impl.UserServiceImpl_24162126;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/login" })
public class LoginController_24162126 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final IUserService_24162126 userService = new UserServiceImpl_24162126();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
		String password = req.getParameter("password");

		User_24162126 user = (email == null || password == null) ? null : userService.login(email.trim(), password);
		if (user == null) {
			// That bai -> quay lai trang dang nhap
			req.setAttribute("email", email);
			req.setAttribute("error", "Email hoặc mật khẩu không đúng");
			req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
			return;
		}

		HttpSession session = req.getSession();
		session.setAttribute(Constant_24162126.SESSION_ACCOUNT, user);
		// Admin -> trang quan tri, User -> trang chu cua User
		resp.sendRedirect(req.getContextPath() + (user.isAdminRole() ? "/admin" : "/"));
	}
}
