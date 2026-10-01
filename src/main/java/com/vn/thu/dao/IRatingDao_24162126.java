package com.vn.thu.dao;

import java.util.List;
import java.util.Map;

import com.vn.thu.entity.Rating_24162126;

public interface IRatingDao_24162126 {

	/** Danh sach review cua 1 sach (kem thong tin user) */
	List<Rating_24162126> findByBookId(int bookId);

	long countByBookId(int bookId);

	/** So review cua nhieu sach: bookId -> so review */
	Map<Integer, Long> countByBookIds(List<Integer> bookIds);

	/** Them moi hoac cap nhat review (moi user 1 review / sach) */
	void save(int userId, int bookId, int rating, String reviewText);
}
