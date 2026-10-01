package com.vn.thu.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import com.vn.thu.entity.User_24162126;
import com.vn.thu.service.IUserService_24162126;
import com.vn.thu.service.impl.UserServiceImpl_24162126;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** Xac thuc OTP de kich hoat tai khoan (va gui lai OTP) */
@WebServlet(urlPatterns = { "/verify-otp", "/resend-otp" })
public class VerifyOtpController_24162126 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final IUserService_24162126 userService = new UserServiceImpl_24162126();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getSession().getAttribute(RegisterController_24162126.SESSION_PENDING_USER) == null) {
			resp.sendRedirect(req.getContextPath() + "/register");
			return;
		}
		req.getRequestDispatcher("/WEB-INF/views/auth/verify-otp.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		User_24162126 pending = (User_24162126) session.getAttribute(RegisterController_24162126.SESSION_PENDING_USER);
		if (pending == null) {
			resp.sendRedirect(req.getContextPath() + "/register");
			return;
		}

		// Gui lai ma OTP
		if ("/resend-otp".equals(req.getServletPath())) {
			RegisterController_24162126.sendNewOtp(req, session, pending.getEmail());
			req.getRequestDispatcher("/WEB-INF/views/auth/verify-otp.jsp").forward(req, resp);
			return;
		}

		String otp = req.getParameter("otp") == null ? "" : req.getParameter("otp").trim();
		LocalDateTime expire = (LocalDateTime) session.getAttribute(RegisterController_24162126.SESSION_OTP_EXPIRE);
		String error = null;
		if (expire == null || LocalDateTime.now().isAfter(expire)) {
			error = "Mã OTP đã hết hạn, vui lòng bấm gửi lại mã";
		} else if (!otp.equals(session.getAttribute(RegisterController_24162126.SESSION_OTP))) {
			error = "Mã OTP không đúng";
		}
		if (error != null) {
			req.setAttribute("error", error);
			req.getRequestDispatcher("/WEB-INF/views/auth/verify-otp.jsp").forward(req, resp);
			return;
		}

		userService.register(pending, (String) session.getAttribute(RegisterController_24162126.SESSION_PENDING_PASS));
		session.removeAttribute(RegisterController_24162126.SESSION_PENDING_USER);
		session.removeAttribute(RegisterController_24162126.SESSION_PENDING_PASS);
		session.removeAttribute(RegisterController_24162126.SESSION_OTP);
		session.removeAttribute(RegisterController_24162126.SESSION_OTP_EXPIRE);
		session.setAttribute("message", "Kích hoạt tài khoản thành công, mời bạn đăng nhập");
		resp.sendRedirect(req.getContextPath() + "/login");
	}
}
