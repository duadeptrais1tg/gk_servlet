package com.vn.thu.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book_24162126 implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bookid")
	private Integer bookid;

	@Column(name = "isbn")
	private Integer isbn;

	@Column(name = "title", length = 200)
	private String title;

	@Column(name = "publisher", length = 100)
	private String publisher;

	@Column(name = "price", precision = 6, scale = 2)
	private BigDecimal price;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "publish_date")
	private LocalDate publishDate;

	@Column(name = "cover_image", length = 100)
	private String coverImage;

	@Column(name = "quantity")
	private Integer quantity;

	/** Bang trung gian book_author */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "book_author",
			joinColumns = @JoinColumn(name = "bookid"),
			inverseJoinColumns = @JoinColumn(name = "author_id"))
	private List<Author_24162126> authors = new ArrayList<>();

	@OneToMany(mappedBy = "book")
	private List<Rating_24162126> ratings = new ArrayList<>();

	public Book_24162126() {
	}

	/** Ten cac tac gia noi bang dau phay - dung cho JSP */
	public String getAuthorNames() {
		return authors.stream().map(Author_24162126::getAuthorName).collect(Collectors.joining(", "));
	}

	/** Danh sach id tac gia - dung de check san checkbox trong form */
	public List<Integer> getAuthorIds() {
		return authors.stream().map(Author_24162126::getAuthorId).toList();
	}

	public Integer getBookid() {
		return bookid;
	}

	public void setBookid(Integer bookid) {
		this.bookid = bookid;
	}

	public Integer getIsbn() {
		return isbn;
	}

	public void setIsbn(Integer isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(LocalDate publishDate) {
		this.publishDate = publishDate;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public List<Author_24162126> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author_24162126> authors) {
		this.authors = authors;
	}

	public List<Rating_24162126> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating_24162126> ratings) {
		this.ratings = ratings;
	}
}
