package com.vn.thu.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import com.vn.thu.entity.User_24162126;
import com.vn.thu.service.IUserService_24162126;
import com.vn.thu.service.impl.UserServiceImpl_24162126;
import com.vn.thu.util.EmailUtil_24162126;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Dang ky: luu tam thong tin + OTP vao Session, gui OTP qua mail.
 * Chi luu vao DB khi nhap dung OTP (VerifyOtpController).
 */
@WebServlet(urlPatterns = { "/register" })
public class RegisterController_24162126 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String SESSION_PENDING_USER = "pendingUser";
	public static final String SESSION_PENDING_PASS = "pendingPass";
	public static final String SESSION_OTP = "otp";
	public static final String SESSION_OTP_EXPIRE = "otpExpire";

	private final IUserService_24162126 userService = new UserServiceImpl_24162126();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = trim(req.getParameter("email"));
		String fullname = trim(req.getParameter("fullname"));
		String phone = trim(req.getParameter("phone"));
		String password = req.getParameter("password");
		String confirm = req.getParameter("confirmPassword");

		req.setAttribute("email", email);
		req.setAttribute("fullname", fullname);
		req.setAttribute("phone", phone);

		String error = null;
		Integer phoneNumber = null;
		if (email.isEmpty() || password == null || password.length() < 6) {
			error = "Vui lòng nhập email và mật khẩu (ít nhất 6 ký tự)";
		} else if (!password.equals(confirm)) {
			error = "Mật khẩu nhập lại không khớp";
		} else if (userService.emailExists(email)) {
			error = "Email đã được sử dụng";
		} else if (!phone.isEmpty()) {
			try {
				phoneNumber = Integer.valueOf(phone);
			} catch (NumberFormatException e) {
				error = "Số điện thoại không hợp lệ";
			}
		}
		if (error != null) {
			req.setAttribute("error", error);
			req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
			return;
		}

		User_24162126 user = new User_24162126();
		user.setEmail(email);
		user.setFullname(fullname);
		user.setPhone(phoneNumber);

		HttpSession session = req.getSession();
		session.setAttribute(SESSION_PENDING_USER, user);
		session.setAttribute(SESSION_PENDING_PASS, password);
		sendNewOtp(req, session, email);
		req.getRequestDispatcher("/WEB-INF/views/auth/verify-otp.jsp").forward(req, resp);
	}

	/** Sinh OTP moi (hieu luc 5 phut), luu vao Session va gui mail */
	static void sendNewOtp(HttpServletRequest req, HttpSession session, String email) {
		String otp = EmailUtil_24162126.generateOtp();
		session.setAttribute(SESSION_OTP, otp);
		session.setAttribute(SESSION_OTP_EXPIRE, LocalDateTime.now().plusMinutes(5));
		boolean sent = EmailUtil_24162126.sendOtp(email, otp);
		req.setAttribute("message", sent ? "Mã OTP đã được gửi tới email " + email
				: "Không gửi được mail (chưa cấu hình Gmail) - xem mã OTP trong console của Tomcat");
	}

	private static String trim(String s) {
		return s == null ? "" : s.trim();
	}
}
