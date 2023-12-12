package com.example.bookmanagement.api.v1.response;

import lombok.Data;
import java.io.Serializable;

@Data
public class BookRs implements Serializable {

    private long id;

    private String bookTitle;

    private String nameAuthor;

    private String category;
}
