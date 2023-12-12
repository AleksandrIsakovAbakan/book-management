package com.example.bookmanagement.api.v1.request;

import lombok.Data;

@Data
public class BookRq {

    private long id;

    private String bookTitle;

    private String nameAuthor;

    private String category;
}
