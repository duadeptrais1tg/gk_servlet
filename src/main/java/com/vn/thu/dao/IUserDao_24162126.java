package com.vn.thu.dao;

import com.vn.thu.entity.User_24162126;

public interface IUserDao_24162126 {

	User_24162126 findByEmail(String email);

	boolean existsByEmail(String email);

	void insert(User_24162126 user);

	void update(User_24162126 user);

	long count();
}
