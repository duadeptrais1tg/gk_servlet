package com.vn.thu.entity;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "rating")
public class Rating_24162126 implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RatingId_24162126 id = new RatingId_24162126();

	@ManyToOne
	@MapsId("userid")
	@JoinColumn(name = "userid")
	private User_24162126 user;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("bookid")
	@JoinColumn(name = "bookid")
	private Book_24162126 book;

	@Column(name = "rating", columnDefinition = "TINYINT")
	private Integer rating;

	@Column(name = "review_text", columnDefinition = "TEXT")
	private String reviewText;

	public Rating_24162126() {
	}

	public RatingId_24162126 getId() {
		return id;
	}

	public void setId(RatingId_24162126 id) {
		this.id = id;
	}

	public User_24162126 getUser() {
		return user;
	}

	public void setUser(User_24162126 user) {
		this.user = user;
	}

	public Book_24162126 getBook() {
		return book;
	}

	public void setBook(Book_24162126 book) {
		this.book = book;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}
}
