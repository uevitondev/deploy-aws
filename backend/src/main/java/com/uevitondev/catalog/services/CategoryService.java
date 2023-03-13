package com.uevitondev.catalog.services;

import com.uevitondev.catalog.dto.CategoryDTO;
import com.uevitondev.catalog.entities.Category;
import com.uevitondev.catalog.repository.CategoryRepository;
import com.uevitondev.catalog.services.exceptions.DatabaseException;
import com.uevitondev.catalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found! id: " + id));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO insertCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category = categoryRepository.save(category);
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        try {
            Category category = categoryRepository.getReferenceById(id);
            category.setName(categoryDTO.getName());
            category = categoryRepository.save(category);
            return new CategoryDTO(category);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found! id: " + id);
        }
    }

    public void deleteCategory(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found! id: " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation!");
        }
    }
}
