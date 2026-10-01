package com.vn.thu.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.vn.thu.config.Constant_24162126;

import jakarta.servlet.http.Part;

/** Luu anh bia sach upload vao thu muc UPLOAD_DIR */
public final class FileUtil_24162126 {

	private FileUtil_24162126() {
	}

	/** Tra ve ten file da luu, hoac null neu khong co file upload */
	public static String saveCover(Part part) throws IOException {
		if (part == null || part.getSize() == 0 || part.getSubmittedFileName() == null
				|| part.getSubmittedFileName().isBlank()) {
			return null;
		}
		Path dir = Paths.get(Constant_24162126.UPLOAD_DIR);
		Files.createDirectories(dir);
		String original = Paths.get(part.getSubmittedFileName()).getFileName().toString();
		int dot = original.lastIndexOf('.');
		String name = System.currentTimeMillis() + (dot >= 0 ? original.substring(dot) : "");
		try (InputStream in = part.getInputStream()) {
			Files.copy(in, dir.resolve(name), StandardCopyOption.REPLACE_EXISTING);
		}
		return name;
	}
}
