package com.vn.thu.dao.impl;

import java.util.List;

import com.vn.thu.config.JpaConfig_24162126;
import com.vn.thu.dao.IUserDao_24162126;
import com.vn.thu.entity.User_24162126;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class UserDaoImpl_24162126 implements IUserDao_24162126 {

	@Override
	public User_24162126 findByEmail(String email) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		try {
			List<User_24162126> list = em
					.createQuery("SELECT u FROM User_24162126 u WHERE u.email = :email", User_24162126.class)
					.setParameter("email", email)
					.getResultList();
			return list.isEmpty() ? null : list.get(0);
		} finally {
			em.close();
		}
	}

	@Override
	public boolean existsByEmail(String email) {
		return findByEmail(email) != null;
	}

	@Override
	public void insert(User_24162126 user) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(user);
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	@Override
	public void update(User_24162126 user) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.merge(user);
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	@Override
	public long count() {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		try {
			return em.createQuery("SELECT COUNT(u) FROM User_24162126 u", Long.class).getSingleResult();
		} finally {
			em.close();
		}
	}
}
