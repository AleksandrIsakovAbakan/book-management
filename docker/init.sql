CREATE SCHEMA IF NOT EXISTS postgres;

CREATE TABLE IF NOT EXISTS postgres.book
(
    id SERIAL PRIMARY KEY,
    book_title VARCHAR(255) NOT NULL,
    name_author VARCHAR(255) NOT NULL,
    category_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS postgres.category
(
    id SERIAL PRIMARY KEY,
    name_category VARCHAR(255) NOT NULL
);
