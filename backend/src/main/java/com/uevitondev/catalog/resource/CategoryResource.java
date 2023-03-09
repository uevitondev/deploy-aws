package com.uevitondev.catalog.resource;

import com.uevitondev.catalog.dto.CategoryDTO;
import com.uevitondev.catalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> findAllCategories() {
        return ResponseEntity.ok().body(categoryService.findAllCategories());
    }

}
