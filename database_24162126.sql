-- Database BookStore - MSSV 24162126 (MySQL)
-- (Ung dung cung tu tao bang bang JPA ddl-auto=update va tu them du lieu mau)
CREATE DATABASE IF NOT EXISTS bookstore_24162126 CHARACTER SET utf8mb4;
USE bookstore_24162126;

CREATE TABLE IF NOT EXISTS books (
    bookid       INT AUTO_INCREMENT PRIMARY KEY,
    isbn         INT NULL,
    title        VARCHAR(200) NULL,
    publisher    VARCHAR(100) NULL,
    price        DECIMAL(6,2) NULL,
    description  TEXT NULL,
    publish_date DATE NULL,
    cover_image  VARCHAR(100) NULL,
    quantity     INT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    email       VARCHAR(50) NOT NULL,
    fullname    NVARCHAR(50) NULL,
    phone       INT NULL,
    passwd      VARCHAR(32) NOT NULL,
    signup_date DATETIME NULL,
    last_login  DATETIME NULL,
    is_admin    BIT NULL
);

CREATE TABLE IF NOT EXISTS author (
    author_id     INT AUTO_INCREMENT PRIMARY KEY,
    author_name   VARCHAR(100) NULL,
    date_of_birth DATE NULL
);

CREATE TABLE IF NOT EXISTS book_author (
    bookid    INT NOT NULL,
    author_id INT NOT NULL,
    PRIMARY KEY (bookid, author_id),
    FOREIGN KEY (bookid) REFERENCES books(bookid),
    FOREIGN KEY (author_id) REFERENCES author(author_id)
);

CREATE TABLE IF NOT EXISTS rating (
    userid      INT NOT NULL,
    bookid      INT NOT NULL,
    rating      TINYINT NULL,
    review_text TEXT NULL,
    PRIMARY KEY (userid, bookid),
    FOREIGN KEY (userid) REFERENCES users(id),
    FOREIGN KEY (bookid) REFERENCES books(bookid)
);

-- COD orders: snapshot prices/titles survive catalog changes.
CREATE TABLE IF NOT EXISTS customer_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    checkout_token VARCHAR(36) NOT NULL UNIQUE,
    owner_key VARCHAR(36) NOT NULL,
    user_id INT NULL,
    recipient_name VARCHAR(100) NOT NULL,
    phone VARCHAR(16) NOT NULL,
    address VARCHAR(500) NOT NULL,
    note VARCHAR(1000) NULL,
    created_at DATETIME(6) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    payment_status VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    total DECIMAL(24,2) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    book_id INT NOT NULL,
    title VARCHAR(200) NULL,
    price DECIMAL(24,2) NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES customer_orders(id)
) ENGINE=InnoDB;
