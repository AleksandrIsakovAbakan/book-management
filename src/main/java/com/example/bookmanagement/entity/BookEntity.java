package com.example.bookmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Data
@Entity
@Table(name = "book")
public class BookEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "book_title")
    private String bookTitle;

    @Column(name = "name_author")
    private String nameAuthor;

    @Column(name = "category_id")
    private long categoryId;

    @ManyToOne
    @JoinColumn(name="category_id", nullable=false, insertable=false, updatable=false)
    private CategoryEntity categoryEntity;

}
