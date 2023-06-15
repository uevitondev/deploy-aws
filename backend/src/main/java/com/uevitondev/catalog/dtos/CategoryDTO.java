package com.uevitondev.catalog.dtos;

import com.uevitondev.catalog.entities.Category;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class CategoryDTO implements Serializable {

    private Long id;

    @NotBlank(message = "campo obrigatório!")
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
