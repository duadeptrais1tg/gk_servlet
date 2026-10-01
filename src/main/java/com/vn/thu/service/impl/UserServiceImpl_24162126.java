package com.vn.thu.service.impl;

import java.time.LocalDateTime;

import com.vn.thu.dao.IUserDao_24162126;
import com.vn.thu.dao.impl.UserDaoImpl_24162126;
import com.vn.thu.entity.User_24162126;
import com.vn.thu.service.IUserService_24162126;
import com.vn.thu.util.PasswordUtil_24162126;

public class UserServiceImpl_24162126 implements IUserService_24162126 {

	private final IUserDao_24162126 userDao = new UserDaoImpl_24162126();

	@Override
	public boolean emailExists(String email) {
		return userDao.existsByEmail(email);
	}

	@Override
	public void register(User_24162126 user, String rawPassword) {
		user.setPasswd(PasswordUtil_24162126.md5(rawPassword));
		user.setSignupDate(LocalDateTime.now());
		user.setIsAdmin(false);
		userDao.insert(user);
	}

	@Override
	public User_24162126 login(String email, String rawPassword) {
		User_24162126 user = userDao.findByEmail(email);
		if (user == null || !user.getPasswd().equals(PasswordUtil_24162126.md5(rawPassword))) {
			return null;
		}
		user.setLastLogin(LocalDateTime.now());
		userDao.update(user);
		return user;
	}
}
