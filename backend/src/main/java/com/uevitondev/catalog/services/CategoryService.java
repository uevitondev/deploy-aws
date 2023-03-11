package com.uevitondev.catalog.services;

import com.uevitondev.catalog.dto.CategoryDTO;
import com.uevitondev.catalog.entities.Category;
import com.uevitondev.catalog.repository.CategoryRepository;
import com.uevitondev.catalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new EntityNotFoundException("Entity not found! id: " + id));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO insertCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category = categoryRepository.save(category);
        return new CategoryDTO(category);
    }

}
