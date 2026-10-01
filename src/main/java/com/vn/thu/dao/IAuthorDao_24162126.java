package com.vn.thu.dao;

import java.util.List;
import java.util.Map;

import com.vn.thu.entity.Author_24162126;

public interface IAuthorDao_24162126 {

	/** page bat dau tu 1 */
	List<Author_24162126> findPage(int page, int pageSize);

	long count();

	List<Author_24162126> findAll();

	Author_24162126 findById(int id);

	/** Lay tac gia kem danh sach sach cua tac gia */
	Author_24162126 findByIdWithBooks(int id);

	/** So sach cua tung tac gia: authorId -> so sach */
	Map<Integer, Long> countBooks(List<Integer> authorIds);

	void insert(Author_24162126 author);

	void update(Author_24162126 author);

	/** Xoa tac gia (xoa luon lien ket trong book_author) */
	void delete(int id);
}
