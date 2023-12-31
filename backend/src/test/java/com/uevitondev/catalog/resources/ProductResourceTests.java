package com.uevitondev.catalog.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uevitondev.catalog.dtos.ProductDTO;
import com.uevitondev.catalog.entities.Product;
import com.uevitondev.catalog.services.ProductService;
import com.uevitondev.catalog.services.exceptions.DatabaseException;
import com.uevitondev.catalog.services.exceptions.ResourceNotFoundException;
import com.uevitondev.catalog.tests.Factory;
import com.uevitondev.catalog.tests.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTests {

    private PageImpl<ProductDTO> page;
    private Product product;
    private ProductDTO productDTO;
    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TokenUtil tokenUtil;
    private String username;
    private String password;


    @BeforeEach
    public void setUp() throws Exception {

        product = Factory.createProduct();
        productDTO = Factory.createProductDto();
        page = new PageImpl<>(List.of(productDTO));
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        username = "maria@gmail.com";
        password = "123456";

        Mockito.when(productService.findAllProductsPaged(any(), any(), any())).thenReturn(page);
        Mockito.when(productService.findProductById(existingId)).thenReturn(productDTO);
        Mockito.when(productService.findProductById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(productService.updateProduct(eq(existingId), any())).thenReturn(productDTO);
        Mockito.when(productService.updateProduct(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(productService).deleteProduct(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(productService).deleteProduct(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(productService).deleteProduct(dependentId);

        Mockito.when(productService.insertProduct(any())).thenReturn(productDTO);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {

        //mockMvc.perform(get("/products")).andExpect(status().isOk());
        ResultActions resultActions = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {

        //mockMvc.perform(get("/products/{id}", existingId)).andExpect(status().isOk());
        ResultActions resultActions = mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        //mockMvc.perform(get("/products/{id}", nonExistingId)).andExpect(status().isOk());
        ResultActions resultActions = mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void updateProductShouldReturnProductDtoWhenIdExists() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        ResultActions resultActions = mockMvc.perform(put("/products/{id}", existingId)
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateProductShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        ResultActions resultActions = mockMvc.perform(put("/products/{id}", nonExistingId)
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void insertProductShouldReturnStatusCreatedAndProductDto() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        ResultActions resultActions = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void deleteProductShouldReturnStatusNoContentWhenIdExists() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        ResultActions resultActions = mockMvc.perform(delete("/products/{id}", existingId)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void deleteProductShouldReturnStatusNotFoundWhenIdDoesNotExist() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
        ResultActions resultActions = mockMvc.perform(delete("/products/{id}", nonExistingId)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }


}
