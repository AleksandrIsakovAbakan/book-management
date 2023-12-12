package com.example.bookmanagement.service;

import com.example.bookmanagement.api.v1.request.BookRq;
import com.example.bookmanagement.api.v1.response.BookRs;
import com.example.bookmanagement.entity.BookEntity;
import com.example.bookmanagement.entity.CategoryEntity;
import com.example.bookmanagement.exception.EntityNotFoundException;
import com.example.bookmanagement.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableCaching
public class BookService {

    private final BookRepository bookRepository;

    private final CategoryService categoryService;

    static final String ENTITY_NOT_FOUND = "Entity not found ";

    @Cacheable(value = "getBookByTitleAndAuthorCache", key = "#bookTitle+#nameAuthor")
    public BookRs getBookByTitleAndAuthor(String bookTitle, String nameAuthor) {

        Optional<BookEntity> bookEntity = bookRepository.findByBookTitleAndNameAuthor(bookTitle, nameAuthor);
        if (bookEntity.isPresent()){
            CategoryEntity categoryEntity = categoryService.getCategoryEntityId(bookEntity.get().getCategoryId());
            return bookEntityToBookRs(bookEntity.get(), categoryEntity);
        }
        throw new EntityNotFoundException(ENTITY_NOT_FOUND + bookTitle + " " + nameAuthor);
    }

    @Cacheable(value = "getBookByCategoryCache", key = "#nameCategory")
    public List<BookRs> getBookByCategory(String nameCategory) {

        CategoryEntity categoryEntity = categoryService.getCategoryEntityNameCategory(nameCategory);
        if (categoryEntity != null){
            List<BookRs> bookRsList = new ArrayList<>();
            List<BookEntity> bookEntityList = bookRepository.findAllByCategoryId(categoryEntity.getId());
            for (BookEntity bookEntity : bookEntityList){
                BookRs bookRs = bookEntityToBookRs(bookEntity, categoryEntity);
                bookRsList.add(bookRs);
            }
            return bookRsList;
        }
        return new ArrayList<>();
    }
    @Caching(evict = {
            @CacheEvict(value = "getBookByCategoryCache", key = "#categoryOld"),
            @CacheEvict(value = "getBookByTitleAndAuthorCache", key = "#bookTitleOld+#nameAuthorOld")
    })
    @CachePut(value = "getBookByTitleAndAuthorCache", key = "#bookRq.bookTitle+#bookRq.nameAuthor")
    public BookRs putIdBook(BookRq bookRq, String bookTitleOld, String nameAuthorOld, String categoryOld) {
        Optional<BookEntity> bookEntity = bookRepository.findById(bookRq.getId());
        if (bookEntity.isPresent()){
            BookEntity bookEntity1;
            CategoryEntity categoryEntity1;
            CategoryEntity categoryEntity = categoryService.getCategoryEntityNameCategory(bookRq.getCategory());
            if (!bookEntity.get().getBookTitle().equals(bookRq.getBookTitle())
                    || !bookEntity.get().getNameAuthor().equals(bookRq.getNameAuthor())){
                bookEntity1 = bookUpdate(bookRq, bookEntity.get());
            } else {
                bookEntity1 = bookEntity.get();
            }
            if (categoryEntity != null) {
                if (bookEntity.get().getCategoryId() != categoryEntity.getId()) {
                    categoryEntity1 = categoryUpdate(bookEntity.get(), categoryEntity);
                    bookEntity1.setCategoryId(categoryEntity1.getId());
                } else {
                    categoryEntity1 = categoryEntity;
                }
            } else {
                categoryEntity1 = categoryService.saveCategoryEntity(bookRq.getCategory());
                bookEntity1.setCategoryId(categoryEntity1.getId());
                bookRepository.save(bookEntity1);
                Long byCategoryId = bookRepository.countByCategoryId(bookEntity.get().getId());
                if (byCategoryId <= 1){
                    categoryService.deleteCategory(bookEntity.get().getCategoryEntity().getId());
                }
            }
            return bookEntityToBookRs(bookEntity1, categoryEntity1);
        } else {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND + bookRq.getId());
        }
    }

    public CategoryEntity categoryUpdate(BookEntity bookEntity, CategoryEntity categoryEntity){

        if (bookEntity.getCategoryId() == categoryEntity.getId()){
            return categoryEntity;
        } else {
            Long byCategoryId = bookRepository.countByCategoryId(bookEntity.getId());
            if (byCategoryId <= 1){
                categoryService.deleteCategory(categoryEntity.getId());
            }
            return categoryEntity;
        }
    }

    @CacheEvict(value = "getBookByCategoryCache", key = "#bookRq.category")
    @CachePut(value = "getBookByTitleAndAuthorCache", key = "#bookRq.bookTitle+#bookRq.nameAuthor")
    public BookRs addBook(BookRq bookRq) {
        BookEntity bookEntity1;
        CategoryEntity categoryEntity = null;
        Optional<BookEntity> bookEntity = bookRepository.findByBookTitleAndNameAuthor(bookRq.getBookTitle(),
                bookRq.getNameAuthor());
        if (bookEntity.isEmpty()){
            CategoryEntity byNameCategory = categoryService.getCategoryEntityNameCategory(bookRq.getCategory());
            if (byNameCategory == null){
                categoryEntity = categoryService.saveCategoryEntity(bookRq.getCategory());
            } else {
                categoryEntity = byNameCategory;
            }
            bookEntity1 = bookSave(bookRq, categoryEntity);
            return bookEntityToBookRs(bookEntity1, categoryEntity);
        } else {
            throw new ArrayIndexOutOfBoundsException("Entity already exists " + bookRq.getBookTitle() + " "
                    + bookRq.getNameAuthor());
        }
    }

    @Caching(evict = {
        @CacheEvict(value = "getBookByCategoryCache", key = "#categoryEntityDel.nameCategory"),
        @CacheEvict(value = "getBookByTitleAndAuthorCache", key = "#bookEntityDel.bookTitle+#bookEntityDel.nameAuthor")
    })
    public BookRs deleteIdBook(long id, BookEntity bookEntityDel, CategoryEntity categoryEntityDel) {
        Optional<BookEntity> bookEntity = bookRepository.findById(id);
        if (bookEntity.isPresent()){
            BookEntity bookEntity1 = bookEntity.get();
            CategoryEntity categoryEntity = categoryService.getCategoryEntityId(bookEntity1.getCategoryId());
            Long countByCategoryId = bookRepository.countByCategoryId(categoryEntity.getId());
            if (countByCategoryId <= 1) {
                categoryService.deleteCategory(categoryEntity.getId());
            }
            bookRepository.delete(bookEntity.get());
            return bookEntityToBookRs(bookEntity.get(), categoryEntity);
        } else {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND + id);
        }
    }

    private BookRs bookEntityToBookRs(BookEntity bookEntity, CategoryEntity categoryEntity){

        BookRs bookRs = new BookRs();
        bookRs.setId(bookEntity.getId());
        bookRs.setBookTitle(bookEntity.getBookTitle());
        bookRs.setNameAuthor(bookEntity.getNameAuthor());
        bookRs.setCategory(categoryEntity.getNameCategory());
        return bookRs;
    }

    public BookEntity bookSave(BookRq bookRq, CategoryEntity categoryEntity){

        BookEntity bookEntity = new BookEntity();
        bookEntity.setBookTitle(bookRq.getBookTitle());
        bookEntity.setNameAuthor(bookRq.getNameAuthor());
        bookEntity.setCategoryId(categoryEntity.getId());
        return bookRepository.save(bookEntity);
    }

    public BookEntity bookUpdate(BookRq bookRq, BookEntity bookEntity){

        bookEntity.setBookTitle(bookRq.getBookTitle());
        bookEntity.setNameAuthor(bookRq.getNameAuthor());
        return bookRepository.save(bookEntity);
    }

    public BookEntity getBookById(long id) {
        Optional<BookEntity> bookEntity = bookRepository.findById(id);
        if (bookEntity.isPresent()){
            return bookEntity.get();
        } else {
            throw new EntityNotFoundException("Book not found id=" + id);
        }
    }
}
