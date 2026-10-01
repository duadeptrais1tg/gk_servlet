package com.vn.thu.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/** Ma hoa mat khau MD5 -> 32 ky tu, vua voi cot passwd varchar(32) */
public final class PasswordUtil_24162126 {

	private PasswordUtil_24162126() {
	}

	public static String md5(String raw) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return HexFormat.of().formatHex(md.digest(raw.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}
}
