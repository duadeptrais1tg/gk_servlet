package com.vn.thu.dao;

import java.util.List;

import com.vn.thu.entity.Book_24162126;

public interface IBookDao_24162126 {

	/** page bat dau tu 1 */
	List<Book_24162126> findPage(int page, int pageSize);

	long count();

	Book_24162126 findById(int id);

	/** Them moi sach kem danh sach tac gia */
	void insert(Book_24162126 book, List<Integer> authorIds);

	/** Cap nhat sach kem danh sach tac gia */
	void update(Book_24162126 book, List<Integer> authorIds);

	/** Xoa sach (xoa luon review va lien ket tac gia) */
	void delete(int id);
}
