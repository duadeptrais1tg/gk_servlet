package com.vn.thu.service;

import java.util.List;
import java.util.Map;

import com.vn.thu.entity.Author_24162126;

public interface IAuthorService_24162126 {

	/** page bat dau tu 1 */
	List<Author_24162126> findPage(int page, int pageSize);

	/** Tong so trang (it nhat 1) */
	int totalPages(int pageSize);

	long count();

	List<Author_24162126> findAll();

	Author_24162126 findById(int id);

	Author_24162126 findByIdWithBooks(int id);

	/** So sach cua tung tac gia: authorId -> so sach */
	Map<Integer, Long> countBooks(List<Author_24162126> authors);

	/** Them moi (authorId null) hoac cap nhat tac gia */
	void save(Author_24162126 author);

	void delete(int id);
}
