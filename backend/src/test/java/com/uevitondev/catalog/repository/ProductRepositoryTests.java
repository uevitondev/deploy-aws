package com.uevitondev.catalog.repository;

import com.uevitondev.catalog.entities.Product;
import com.uevitondev.catalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private long existsId;
    private long countTotalProducts;
    private long optionalNotEmptyId;
    private long optionalEmptyId;

    @BeforeEach
    void setUp() throws Exception {
        existsId = 1L;
        countTotalProducts = 25L;
        optionalNotEmptyId = 1L;
        optionalEmptyId = 1000L;
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {

        Product product = Factory.createProduct();
        product.setId(null);
        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        productRepository.deleteById(existsId);
        Optional<Product> result = productRepository.findById(existsId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void returnOptionalProductNotEmptyWhenObjectIdIsPresent() {

        Optional<Product> product = productRepository.findById(optionalNotEmptyId);
        Assertions.assertTrue(product.isPresent());
    }

    @Test
    public void returnOptionalProductEmptyWhenObjectIdNoPresent() {

        Optional<Product> product = productRepository.findById(optionalEmptyId);
        Assertions.assertFalse(product.isPresent());
    }


}
