package com.example.bookmanagement.service;

import com.example.bookmanagement.entity.CategoryEntity;
import com.example.bookmanagement.exception.EntityNotFoundException;
import com.example.bookmanagement.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.bookmanagement.service.BookService.ENTITY_NOT_FOUND;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    CategoryEntity getCategoryEntityNameCategory(String nameCategory){
        Optional<CategoryEntity> categoryEntity = categoryRepository.findByNameCategory(nameCategory);
        return categoryEntity.orElse(null);
    }

    public CategoryEntity getCategoryEntityId(Long id) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(id);
        if (categoryEntity.isPresent()){
            return categoryEntity.get();
        } else {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND + " category " + id);
        }
    }

    CategoryEntity saveCategoryEntity(String nameCategory){
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setNameCategory(nameCategory);
        categoryRepository.save(categoryEntity);
        return categoryEntity;
    }

    void deleteCategory(long id){
        categoryRepository.deleteById(id);
    }

}
