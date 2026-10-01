package com.vn.thu.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.vn.thu.config.JpaConfig_24162126;
import com.vn.thu.dao.IBookDao_24162126;
import com.vn.thu.entity.Author_24162126;
import com.vn.thu.entity.Book_24162126;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class BookDaoImpl_24162126 implements IBookDao_24162126 {

	@Override
	public List<Book_24162126> findPage(int page, int pageSize) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		try {
			return em.createQuery("SELECT b FROM Book_24162126 b ORDER BY b.bookid", Book_24162126.class)
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
			return em.createQuery("SELECT COUNT(b) FROM Book_24162126 b", Long.class).getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public Book_24162126 findById(int id) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		try {
			return em.find(Book_24162126.class, id);
		} finally {
			em.close();
		}
	}

	@Override
	public void insert(Book_24162126 book, List<Integer> authorIds) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			book.setAuthors(findAuthors(em, authorIds));
			em.persist(book);
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
	public void update(Book_24162126 book, List<Integer> authorIds) {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Book_24162126 old = em.find(Book_24162126.class, book.getBookid());
			old.setIsbn(book.getIsbn());
			old.setTitle(book.getTitle());
			old.setPublisher(book.getPublisher());
			old.setPrice(book.getPrice());
			old.setDescription(book.getDescription());
			old.setPublishDate(book.getPublishDate());
			old.setQuantity(book.getQuantity());
			if (book.getCoverImage() != null) {
				old.setCoverImage(book.getCoverImage());
			}
			old.setAuthors(findAuthors(em, authorIds));
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
			// Xoa review cua sach truoc (khoa ngoai rating.bookid)
			em.createQuery("DELETE FROM Rating_24162126 r WHERE r.book.bookid = :id")
					.setParameter("id", id)
					.executeUpdate();
			Book_24162126 book = em.find(Book_24162126.class, id);
			if (book != null) {
				book.getAuthors().clear(); // xoa dong trong book_author
				em.remove(book);
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

	private List<Author_24162126> findAuthors(EntityManager em, List<Integer> authorIds) {
		if (authorIds == null || authorIds.isEmpty()) {
			return new ArrayList<>();
		}
		return new ArrayList<>(em
				.createQuery("SELECT a FROM Author_24162126 a WHERE a.authorId IN :ids", Author_24162126.class)
				.setParameter("ids", authorIds)
				.getResultList());
	}
}
