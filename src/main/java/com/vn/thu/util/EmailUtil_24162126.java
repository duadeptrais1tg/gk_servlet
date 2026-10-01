package com.vn.thu.util;

import java.security.SecureRandom;
import java.util.Properties;

import com.vn.thu.config.Constant_24162126;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/** Gui ma OTP qua Gmail SMTP */
public final class EmailUtil_24162126 {

	private static final SecureRandom RANDOM = new SecureRandom();

	private EmailUtil_24162126() {
	}

	/** Sinh ma OTP 6 chu so */
	public static String generateOtp() {
		return String.format("%06d", RANDOM.nextInt(1_000_000));
	}

	/** Gui OTP, tra ve true neu gui thanh cong */
	public static boolean sendOtp(String toEmail, String otp) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Constant_24162126.MAIL_USERNAME, Constant_24162126.MAIL_APP_PASSWORD);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Constant_24162126.MAIL_USERNAME));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject("BookStore - Mã OTP kích hoạt tài khoản", "UTF-8");
			message.setText("Mã OTP của bạn là: " + otp + "\nMã có hiệu lực trong 5 phút.", "UTF-8");
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			// Chua cau hinh Gmail -> in OTP ra console de van test duoc
			System.out.println("[OTP] Khong gui duoc mail toi " + toEmail + " (" + e.getMessage() + "). OTP = " + otp);
			return false;
		}
	}
}
