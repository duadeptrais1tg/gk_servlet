package com.vn.thu.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/** Tao EntityManager tu persistence-unit "bookstore" (persistence.xml) */
public class JpaConfig_24162126 {

	private static EntityManagerFactory factory;

	public static synchronized EntityManager getEntityManager() {
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory("bookstore");
		}
		return factory.createEntityManager();
	}

	public static synchronized void close() {
		if (factory != null) {
			factory.close();
			factory = null;
		}
	}
}
