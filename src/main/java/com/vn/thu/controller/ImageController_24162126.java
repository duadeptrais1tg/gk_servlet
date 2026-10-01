package com.vn.thu.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.vn.thu.config.Constant_24162126;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Tra ve anh bia sach trong UPLOAD_DIR: /image?fname=book1.svg */
@WebServlet(urlPatterns = { "/image" })
public class ImageController_24162126 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fname = req.getParameter("fname");
		if (fname == null || fname.isBlank()) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		// Chi lay ten file, chan ../ ra ngoai thu muc upload
		Path file = Paths.get(Constant_24162126.UPLOAD_DIR).resolve(Paths.get(fname).getFileName());
		if (!Files.isRegularFile(file)) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String mime = getServletContext().getMimeType(file.getFileName().toString());
		resp.setContentType(mime != null ? mime : "application/octet-stream");
		resp.setContentLengthLong(Files.size(file));
		Files.copy(file, resp.getOutputStream());
	}
}
