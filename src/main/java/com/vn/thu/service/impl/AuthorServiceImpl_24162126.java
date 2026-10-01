package com.vn.thu.service.impl;

import java.util.List;
import java.util.Map;

import com.vn.thu.dao.IAuthorDao_24162126;
import com.vn.thu.dao.impl.AuthorDaoImpl_24162126;
import com.vn.thu.entity.Author_24162126;
import com.vn.thu.service.IAuthorService_24162126;

public class AuthorServiceImpl_24162126 implements IAuthorService_24162126 {

	private final IAuthorDao_24162126 authorDao = new AuthorDaoImpl_24162126();

	@Override
	public List<Author_24162126> findPage(int page, int pageSize) {
		return authorDao.findPage(page, pageSize);
	}

	@Override
	public int totalPages(int pageSize) {
		return Math.max(1, (int) Math.ceil((double) authorDao.count() / pageSize));
	}

	@Override
	public long count() {
		return authorDao.count();
	}

	@Override
	public List<Author_24162126> findAll() {
		return authorDao.findAll();
	}

	@Override
	public Author_24162126 findById(int id) {
		return authorDao.findById(id);
	}

	@Override
	public Author_24162126 findByIdWithBooks(int id) {
		return authorDao.findByIdWithBooks(id);
	}

	@Override
	public Map<Integer, Long> countBooks(List<Author_24162126> authors) {
		return authorDao.countBooks(authors.stream().map(Author_24162126::getAuthorId).toList());
	}

	@Override
	public void save(Author_24162126 author) {
		if (author.getAuthorId() == null) {
			authorDao.insert(author);
		} else {
			authorDao.update(author);
		}
	}

	@Override
	public void delete(int id) {
		authorDao.delete(id);
	}
}
