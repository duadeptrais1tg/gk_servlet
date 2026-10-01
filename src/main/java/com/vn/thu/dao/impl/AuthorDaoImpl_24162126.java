package com.vn.thu.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vn.thu.config.JpaConfig_24162126;
import com.vn.thu.dao.IAuthorDao_24162126;
import com.vn.thu.entity.Author_24162126;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class AuthorDaoImpl_24162126 implements IAuthorDao_24162126 {

	@Override
	public List<Author_24162126> findPage(int page, int pageSize) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		try {
			return em.createQuery("SELECT a FROM Author_24162126 a ORDER BY a.authorId", Author_24162126.class)
					.setFirstResult((page - 1) * pageSize)
					.setMaxResults(pageSize)
					.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public long count() {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		try {
			return em.createQuery("SELECT COUNT(a) FROM Author_24162126 a", Long.class).getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Author_24162126> findAll() {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		try {
			return em.createQuery("SELECT a FROM Author_24162126 a ORDER BY a.authorName", Author_24162126.class)
					.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public Author_24162126 findById(int id) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		try {
			return em.find(Author_24162126.class, id);
		} finally {
			em.close();
		}
	}

	@Override
	public Author_24162126 findByIdWithBooks(int id) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		try {
			List<Author_24162126> list = em.createQuery(
					"SELECT DISTINCT a FROM Author_24162126 a LEFT JOIN FETCH a.books WHERE a.authorId = :id",
					Author_24162126.class)
					.setParameter("id", id)
					.getResultList();
			return list.isEmpty() ? null : list.get(0);
		} finally {
			em.close();
		}
	}

	@Override
	public Map<Integer, Long> countBooks(List<Integer> authorIds) {
		Map<Integer, Long> result = new HashMap<>();
		if (authorIds.isEmpty()) {
			return result;
		}
		EntityManager em = JpaConfig_24162126.getEntityManager();
		try {
			List<Object[]> rows = em.createQuery(
					"SELECT a.authorId, COUNT(b) FROM Author_24162126 a JOIN a.books b WHERE a.authorId IN :ids GROUP BY a.authorId",
					Object[].class)
					.setParameter("ids", authorIds)
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
	public void insert(Author_24162126 author) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(author);
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
	public void update(Author_24162126 author) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Author_24162126 old = em.find(Author_24162126.class, author.getAuthorId());
			old.setAuthorName(author.getAuthorName());
			old.setDateOfBirth(author.getDateOfBirth());
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
	public void delete(int id) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			// Xoa lien ket sach - tac gia truoc (khoa ngoai book_author.author_id)
			em.createNativeQuery("DELETE FROM book_author WHERE author_id = ?1")
					.setParameter(1, id)
					.executeUpdate();
			Author_24162126 author = em.find(Author_24162126.class, id);
			if (author != null) {
				em.remove(author);
			}
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
