package com.vn.thu.service.impl;

import java.util.List;
import java.util.Map;

import com.vn.thu.dao.IBookDao_24162126;
import com.vn.thu.dao.IRatingDao_24162126;
import com.vn.thu.dao.impl.BookDaoImpl_24162126;
import com.vn.thu.dao.impl.RatingDaoImpl_24162126;
import com.vn.thu.entity.Book_24162126;
import com.vn.thu.entity.Rating_24162126;
import com.vn.thu.service.IBookService_24162126;

public class BookServiceImpl_24162126 implements IBookService_24162126 {

	private final IBookDao_24162126 bookDao = new BookDaoImpl_24162126();
	private final IRatingDao_24162126 ratingDao = new RatingDaoImpl_24162126();

	@Override
	public List<Book_24162126> findPage(int page, int pageSize) {
		return bookDao.findPage(page, pageSize);
	}

	@Override
	public int totalPages(int pageSize) {
		return Math.max(1, (int) Math.ceil((double) bookDao.count() / pageSize));
	}

	@Override
	public long count() {
		return bookDao.count();
	}

	@Override
	public Book_24162126 findById(int id) {
		return bookDao.findById(id);
	}

	@Override
	public Map<Integer, Long> countReviews(List<Book_24162126> books) {
		return ratingDao.countByBookIds(books.stream().map(Book_24162126::getBookid).toList());
	}

	@Override
	public long countReviews(int bookId) {
		return ratingDao.countByBookId(bookId);
	}

	@Override
	public List<Rating_24162126> findReviews(int bookId) {
		return ratingDao.findByBookId(bookId);
	}

	@Override
	public void saveReview(int userId, int bookId, int rating, String reviewText) {
		int stars = Math.max(1, Math.min(5, rating));
		ratingDao.save(userId, bookId, stars, reviewText);
	}

	@Override
	public void save(Book_24162126 book, List<Integer> authorIds) {
		if (book.getBookid() == null) {
			bookDao.insert(book, authorIds);
		} else {
			bookDao.update(book, authorIds);
		}
	}

	@Override
	public void delete(int id) {
		bookDao.delete(id);
	}
}
