package com.example.bookmanagement.repository;

import com.example.bookmanagement.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findByBookTitleAndNameAuthor(String bookTitle, String nameAuthor);

    Long countByCategoryId(long id);

    List<BookEntity> findAllByCategoryId(long id);

}