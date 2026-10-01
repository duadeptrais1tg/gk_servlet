package com.vn.thu.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vn.thu.config.JpaConfig_24162126;
import com.vn.thu.dao.IRatingDao_24162126;
import com.vn.thu.entity.Book_24162126;
import com.vn.thu.entity.RatingId_24162126;
import com.vn.thu.entity.Rating_24162126;
import com.vn.thu.entity.User_24162126;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class RatingDaoImpl_24162126 implements IRatingDao_24162126 {

	@Override
	public List<Rating_24162126> findByBookId(int bookId) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		try {
			return em.createQuery(
					"SELECT r FROM Rating_24162126 r JOIN FETCH r.user WHERE r.book.bookid = :id",
					Rating_24162126.class)
					.setParameter("id", bookId)
					.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public long countByBookId(int bookId) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		try {
			return em.createQuery("SELECT COUNT(r) FROM Rating_24162126 r WHERE r.book.bookid = :id", Long.class)
					.setParameter("id", bookId)
					.getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public Map<Integer, Long> countByBookIds(List<Integer> bookIds) {
		Map<Integer, Long> result = new HashMap<>();
		if (bookIds.isEmpty()) {
			return result;
		}
		EntityManager em = JpaConfig_24162126.getEntityManager();
		try {
			List<Object[]> rows = em.createQuery(
					"SELECT r.book.bookid, COUNT(r) FROM Rating_24162126 r WHERE r.book.bookid IN :ids GROUP BY r.book.bookid",
					Object[].class)
					.setParameter("ids", bookIds)
					.getResultList();
			for (Object[] row : rows) {
				result.put((Integer) row[0], (Long) row[1]);
			}
			return result;
		} finally {
			em.close();
		}
	}

	@Override
	public void save(int userId, int bookId, int rating, String reviewText) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			RatingId_24162126 id = new RatingId_24162126(userId, bookId);
			Rating_24162126 r = em.find(Rating_24162126.class, id);
			if (r == null) {
				r = new Rating_24162126();
				r.setId(id);
				r.setUser(em.getReference(User_24162126.class, userId));
				r.setBook(em.getReference(Book_24162126.class, bookId));
				em.persist(r);
			}
			r.setRating(rating);
			r.setReviewText(reviewText);
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
}
