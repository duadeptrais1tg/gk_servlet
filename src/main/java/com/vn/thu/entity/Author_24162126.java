package com.vn.thu.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "author")
public class Author_24162126 implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "author_id")
	private Integer authorId;

	@Column(name = "author_name", length = 100)
	private String authorName;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@ManyToMany(mappedBy = "authors")
	private List<Book_24162126> books = new ArrayList<>();

	public Author_24162126() {
	}

	public Author_24162126(String authorName, LocalDate dateOfBirth) {
		this.authorName = authorName;
		this.dateOfBirth = dateOfBirth;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public List<Book_24162126> getBooks() {
		return books;
	}

	public void setBooks(List<Book_24162126> books) {
		this.books = books;
	}
}
