package com.volunteer.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.volunteer.api.data.mapping.CategoryDtoMapper;
import com.volunteer.api.data.model.api.CategoryDtoV1;
import com.volunteer.api.data.model.api.ProductDtoV1;
import com.volunteer.api.data.model.api.ProductSearchDtoV1;
import com.volunteer.api.data.model.persistence.Category;
import com.volunteer.api.service.CategoryService;
import com.volunteer.api.service.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collection;
import java.util.List;

public class ProductManagementTest extends AbstractMockMvcTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryDtoMapper categoryDtoMapper;

    @BeforeAll
    public static void initTestData(@Autowired CategoryService categoryService) {
        categoryService.create(Category.builder().name("Test").build());
    }

    @Test
    public void testProductCrud() throws Exception {
        assertTrue(productService.getAll().isEmpty());

        String token = login("op", "pass").getRefreshToken();

        // Check if no products present
        mockMvc
                .perform(MockMvcRequestBuilders.get("/products")
                        .header("Authorization","Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.isA(List.class)))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/products/1").header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Create
        CategoryDtoV1 category = categoryDtoMapper.map(categoryService.getByName("Test").iterator().next());
        ProductDtoV1 product = create(token, category);
        assertThat(product).isNotNull();
        assertThat(product.getId()).isGreaterThan(0);
        assertThat(product.getName()).isEqualTo("test");
        assertThat(product.getNote()).isEqualTo("Test note");
        assertThat(product.getCategory()).isNotNull();
        assertThat(product.getCategory().getId()).isEqualTo(category.getId());

        // Get by id
        ProductDtoV1 productGot = getById(token, product);
        assertThat(productGot).isNotNull();
        assertThat(productGot).isEqualTo(product);

        // Get all
        Collection<ProductDtoV1> productsGot = getAll(token);
        assertThat(productsGot).isNotNull();
        assertThat(productsGot).isNotEmpty();
        assertThat(productsGot.iterator().next()).isEqualTo(product);

        // Update
        product.setName("Name upd");
        product.setNote("Note upd");
        ProductDtoV1 productUpd = update(token, product);
        assertThat(productUpd).isNotNull();
        assertThat(productUpd).isEqualTo(product);

        // Search
        ProductSearchDtoV1 productSearch = ProductSearchDtoV1.builder()
                .name(product.getName())
                .catcgoryId(category.getId())
                .build();

        productsGot = search(token, productSearch);
        assertThat(productsGot).isNotNull();
        assertThat(productsGot.size()).isEqualTo(1);

        productSearch.setName("wrong name");
        productsGot = search(token, productSearch);
        assertThat(productsGot).isNotNull();
        assertThat(productsGot).isEmpty();

        // Delete
        delete(token, product);

        productsGot = getAll(token);
        assertThat(productsGot).isEmpty();
    }

    private ProductDtoV1 update(String token, ProductDtoV1 product) throws Exception {
        byte[] responseBody = mockMvc
                .perform(MockMvcRequestBuilders.put("/products/" + product.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(product)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(responseBody, ProductDtoV1.class);
    }

    private void delete(String token, ProductDtoV1 product) throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/products/" + product.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private ProductDtoV1 getById(String token, ProductDtoV1 product) throws Exception {
        byte[] responseBody = mockMvc
                .perform(MockMvcRequestBuilders.get("/products/" + product.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(responseBody, ProductDtoV1.class);
    }

    private ProductDtoV1 create(String token, CategoryDtoV1 category) throws Exception {
        byte[] responseBody = mockMvc
                .perform(MockMvcRequestBuilders.post("/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(ProductDtoV1.builder()
                                .name("test")
                                .category(category)
                                .note("Test note")
                                .build())))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(responseBody, ProductDtoV1.class);
    }

    private Collection<ProductDtoV1> getAll(String token) throws Exception {
        byte[] responseBody;
        responseBody = mockMvc
                .perform(MockMvcRequestBuilders.get("/products")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(responseBody,
                new TypeReference<Collection<ProductDtoV1>>(){});
    }

    private Collection<ProductDtoV1> search(String token, ProductSearchDtoV1 productSearch) throws Exception {
        byte[] responseBody;
        responseBody = mockMvc
                .perform(MockMvcRequestBuilders.post("/products/search")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(productSearch)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse()
                .getContentAsByteArray();
        return objectMapper.readValue(responseBody,
                new TypeReference<Collection<ProductDtoV1>>(){});
    }
}
