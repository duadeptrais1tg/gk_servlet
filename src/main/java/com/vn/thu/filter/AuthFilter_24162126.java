package com.vn.thu.filter;

import java.io.IOException;

import com.vn.thu.config.Constant_24162126;
import com.vn.thu.entity.User_24162126;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** Chi admin (da dang nhap, is_admin = 1) moi vao duoc /admin/* */
@WebFilter(urlPatterns = { "/admin/*" })
public class AuthFilter_24162126 implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		HttpSession session = req.getSession(false);
		User_24162126 account = session == null ? null
				: (User_24162126) session.getAttribute(Constant_24162126.SESSION_ACCOUNT);
		if (account == null || !account.isAdminRole()) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		chain.doFilter(request, response);
	}
}
