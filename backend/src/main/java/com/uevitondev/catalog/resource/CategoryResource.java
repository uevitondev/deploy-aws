package com.uevitondev.catalog.resource;

import com.uevitondev.catalog.entities.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryResource {

    @GetMapping
    public ResponseEntity<List<Category>> findAllCategories() {

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1L, "Auto"));
        categoryList.add(new Category(2L, "Books"));

        return ResponseEntity.ok().body(categoryList);
    }

}
