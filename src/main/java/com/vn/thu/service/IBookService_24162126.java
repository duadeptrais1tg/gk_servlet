package com.vn.thu.service;

import java.util.List;
import java.util.Map;

import com.vn.thu.entity.Book_24162126;
import com.vn.thu.entity.Rating_24162126;

public interface IBookService_24162126 {

	/** page bat dau tu 1 */
	List<Book_24162126> findPage(int page, int pageSize);

	/** Tong so trang (it nhat 1) */
	int totalPages(int pageSize);

	long count();

	Book_24162126 findById(int id);

	/** So review cua tung sach: bookid -> count */
	Map<Integer, Long> countReviews(List<Book_24162126> books);

	long countReviews(int bookId);

	List<Rating_24162126> findReviews(int bookId);

	/** Them / cap nhat review cua user cho sach */
	void saveReview(int userId, int bookId, int rating, String reviewText);

	/** Them moi (bookid null) hoac cap nhat sach */
	void save(Book_24162126 book, List<Integer> authorIds);

	void delete(int id);
}
