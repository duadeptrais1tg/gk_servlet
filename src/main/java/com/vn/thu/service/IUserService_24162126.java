package com.vn.thu.service;

import com.vn.thu.entity.User_24162126;

public interface IUserService_24162126 {

	boolean emailExists(String email);

	/** Luu user moi sau khi da xac thuc OTP */
	void register(User_24162126 user, String rawPassword);

	/** Tra ve user neu dung email + mat khau, nguoc lai null */
	User_24162126 login(String email, String rawPassword);
}
