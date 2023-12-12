package com.example.bookmanagement.controller;

import com.example.bookmanagement.api.v1.request.BookRq;
import com.example.bookmanagement.api.v1.response.BookRs;
import com.example.bookmanagement.entity.BookEntity;
import com.example.bookmanagement.entity.CategoryEntity;
import com.example.bookmanagement.service.BookService;
import com.example.bookmanagement.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/book", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

    private final BookService bookService;

    private final CategoryService categoryService;

    @GetMapping("/")
    public BookRs getBookByTitleAndAuthor(@RequestParam String title,
                                          @RequestParam String author)
    {
        return bookService.getBookByTitleAndAuthor(title, author);
    }

    @GetMapping("/category/{nameCategory}")
    public List<BookRs> getBookByCategory(@PathVariable String nameCategory)
    {
        return bookService.getBookByCategory(nameCategory);
    }

    @PutMapping("/")
    public BookRs editBook(@RequestBody BookRq bookRq)
    {
        BookEntity bookEntity = bookService.getBookById(bookRq.getId());
        CategoryEntity categoryEntity = categoryService.getCategoryEntityId(bookEntity.getCategoryId());
        return bookService.putIdBook(bookRq, bookEntity.getBookTitle(), bookEntity.getNameAuthor(),
                categoryEntity.getNameCategory());
    }

    @PostMapping("/")
    public BookRs addBook(@RequestBody BookRq bookRq)
    {
        return bookService.addBook(bookRq);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookRs> deleteIdBook(@PathVariable long id)
    {
        BookEntity bookEntity = bookService.getBookById(id);
        CategoryEntity categoryEntity = categoryService.getCategoryEntityId(bookEntity.getCategoryId());
        return new ResponseEntity<>(bookService.deleteIdBook(id, bookEntity, categoryEntity), HttpStatus.NO_CONTENT);
    }

}
