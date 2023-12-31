package com.uevitondev.catalog.resources;

import com.uevitondev.catalog.dtos.ProductDTO;
import com.uevitondev.catalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAllProductsPaged(@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
                                                                 @RequestParam(value = "name", defaultValue = "") String name,
                                                                 Pageable pageable) {

        return ResponseEntity.ok().body(productService.findAllProductsPaged(categoryId, name.trim(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findProductById(@PathVariable Long id) {
        return ResponseEntity.ok().body(productService.findProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insertProduct(@RequestBody @Valid ProductDTO productDTO) {
        productDTO = productService.insertProduct(productDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(productDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDTO productDTO) {
        productDTO = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok().body(productDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
