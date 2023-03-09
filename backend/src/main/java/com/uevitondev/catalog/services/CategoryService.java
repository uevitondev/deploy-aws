package com.uevitondev.catalog.services;

import com.uevitondev.catalog.entities.Category;
import com.uevitondev.catalog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

}
