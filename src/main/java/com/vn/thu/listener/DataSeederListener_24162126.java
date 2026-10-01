package com.vn.thu.listener;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.vn.thu.config.Constant_24162126;
import com.vn.thu.config.JpaConfig_24162126;
import com.vn.thu.entity.Author_24162126;
import com.vn.thu.entity.Book_24162126;
import com.vn.thu.entity.RatingId_24162126;
import com.vn.thu.entity.Rating_24162126;
import com.vn.thu.entity.User_24162126;
import com.vn.thu.util.PasswordUtil_24162126;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/** Khi khoi dong: tao bang (hbm2ddl=update) va them du lieu mau neu database con trong */
@WebListener
public class DataSeederListener_24162126 implements ServletContextListener {

	private static final String[] TITLES = { "Toi thay hoa vang tren co xanh", "Mat biec", "De Men phieu luu ky",
			"Nha gia kim", "Dac nhan tam", "Clean Code", "Clean Architecture", "Cho toi xin mot ve di tuoi tho",
			"Ve nha", "Quang ganh lo di ma vui song", "Veronika quyet chet", "The Clean Coder" };
	private static final String[] PUBLISHERS = { "NXB Tre", "NXB Kim Dong", "NXB Van Hoc", "Prentice Hall" };
	/** Chi so tac gia cua tung cuon sach */
	private static final int[][] BOOK_AUTHORS = { { 0 }, { 0 }, { 1 }, { 2 }, { 3 }, { 4 }, { 4 }, { 0 }, { 1, 0 },
			{ 3 }, { 2 }, { 4 } };
	private static final String[] COLORS = { "#1e88e5", "#43a047", "#e53935", "#8e24aa", "#fb8c00", "#00897b" };

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			writeCovers();
			seed();
		} catch (Exception e) {
			System.out.println("[Seeder] Loi tao du lieu mau: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		JpaConfig_24162126.close();
	}

	private void seed() {
		EntityManager em = JpaConfig_24162126.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			if (em.createQuery("SELECT COUNT(u) FROM User_24162126 u", Long.class).getSingleResult() > 0) {
				return;
			}
			tx.begin();
			User_24162126 admin = newUser("admin@gmail.com", "Quan tri vien", true);
			User_24162126 user = newUser("user@gmail.com", "Nguoi dung", false);
			em.persist(admin);
			em.persist(user);

			List<Author_24162126> authors = List.of(
					new Author_24162126("Nguyen Nhat Anh", LocalDate.of(1955, 5, 7)),
					new Author_24162126("To Hoai", LocalDate.of(1920, 9, 27)),
					new Author_24162126("Paulo Coelho", LocalDate.of(1947, 8, 24)),
					new Author_24162126("Dale Carnegie", LocalDate.of(1888, 11, 24)),
					new Author_24162126("Robert C. Martin", LocalDate.of(1952, 12, 5)));
			authors.forEach(em::persist);

			List<Book_24162126> books = new ArrayList<>();
			for (int i = 0; i < TITLES.length; i++) {
				Book_24162126 b = new Book_24162126();
				b.setIsbn(100000 + i * 137);
				b.setTitle(TITLES[i]);
				b.setPublisher(PUBLISHERS[i % PUBLISHERS.length]);
				b.setPrice(BigDecimal.valueOf(50 + i * 10L));
				b.setDescription("Mo ta ngan cho cuon sach " + TITLES[i] + ".");
				b.setPublishDate(LocalDate.of(2010 + i, (i % 12) + 1, 10));
				b.setQuantity(10 + i * 3);
				b.setCoverImage("book" + (i + 1) + ".svg");
				for (int a : BOOK_AUTHORS[i]) {
					b.getAuthors().add(authors.get(a));
				}
				em.persist(b);
				books.add(b);
			}

			for (int i = 0; i < books.size(); i += 2) {
				em.persist(newRating(user, books.get(i), 4, "Sach rat hay, nen doc!"));
				em.persist(newRating(admin, books.get(i), 5, "Noi dung tot, trinh bay dep."));
			}
			tx.commit();
			System.out.println("[Seeder] Da them du lieu mau");
		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	private User_24162126 newUser(String email, String fullname, boolean isAdmin) {
		User_24162126 u = new User_24162126();
		u.setEmail(email);
		u.setFullname(fullname);
		u.setPhone(901234567);
		u.setPasswd(PasswordUtil_24162126.md5("123456"));
		u.setSignupDate(LocalDateTime.now());
		u.setIsAdmin(isAdmin);
		return u;
	}

	private Rating_24162126 newRating(User_24162126 user, Book_24162126 book, int stars, String text) {
		Rating_24162126 r = new Rating_24162126();
		r.setId(new RatingId_24162126(user.getId(), book.getBookid()));
		r.setUser(user);
		r.setBook(book);
		r.setRating(stars);
		r.setReviewText(text);
		return r;
	}

	/** Tao anh bia SVG cho sach mau (neu chua co) */
	private void writeCovers() throws IOException {
		Path dir = Paths.get(Constant_24162126.UPLOAD_DIR);
		Files.createDirectories(dir);
		for (int i = 0; i < TITLES.length; i++) {
			Path file = dir.resolve("book" + (i + 1) + ".svg");
			if (Files.exists(file)) {
				continue;
			}
			String svg = "<svg xmlns='http://www.w3.org/2000/svg' width='300' height='400'>"
					+ "<rect width='300' height='400' fill='" + COLORS[(i + 1) % COLORS.length] + "'/>"
					+ "<rect x='15' y='15' width='270' height='370' fill='none' stroke='white' stroke-width='3'/>"
					+ "<text x='150' y='190' font-size='20' fill='white' text-anchor='middle' font-family='Arial'>"
					+ TITLES[i] + "</text>"
					+ "<text x='150' y='360' font-size='14' fill='white' text-anchor='middle' font-family='Arial'>BookStore</text>"
					+ "</svg>";
			Files.writeString(file, svg, StandardCharsets.UTF_8);
		}
	}
}
