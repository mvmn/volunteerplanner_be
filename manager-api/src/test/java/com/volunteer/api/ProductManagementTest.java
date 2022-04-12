package com.volunteer.api;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.volunteer.api.data.mapping.CategoryDtoMapper;
import com.volunteer.api.data.model.api.CategoryDtoV1;
import com.volunteer.api.data.model.api.GenericPageDtoV1;
import com.volunteer.api.data.model.api.ProductDtoV1;
import com.volunteer.api.data.model.api.search.SearchDto;
import com.volunteer.api.data.model.api.search.filter.FilterDto;
import com.volunteer.api.data.model.api.search.filter.OperatorFilterDto;
import com.volunteer.api.data.model.api.search.filter.OperatorFilterDto.Operator;
import com.volunteer.api.data.model.api.search.filter.ValueNumberFilterDto;
import com.volunteer.api.data.model.api.search.filter.ValueTextFilterDto;
import com.volunteer.api.data.model.persistence.Category;
import com.volunteer.api.service.CategoryService;

public class ProductManagementTest extends AbstractMockMvcTest {

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
    String token = login().getRefreshToken();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/products/1").header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isNotFound());

    // Create
    CategoryDtoV1 category = categoryDtoMapper.map(
        categoryService.getByName("Test").iterator().next());
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
    GenericPageDtoV1<ProductDtoV1> productsGot = search(token,
        SearchDto.<FilterDto>builder().build());
    assertThat(productsGot).isNotNull();
    assertThat(productsGot.getTotalCount()).isGreaterThan(0);
    assertThat(productsGot.getItems().iterator().next()).isEqualTo(product);

    // Update
    product.setName("Name upd");
    product.setNote("Note upd");
    ProductDtoV1 productUpd = update(token, product);
    assertThat(productUpd).isNotNull();
    assertThat(productUpd).isEqualTo(product);

    // Search
    SearchDto<FilterDto> searchRequest = SearchDto.<FilterDto>builder()
        .filter(OperatorFilterDto.builder()
            .operator(Operator.AND)
            .operands(List.of(
                ValueTextFilterDto.builder()
                    .field("name")
                    .value(product.getName())
                    .build(),
                ValueNumberFilterDto.builder()
                    .field("category.id")
                    .value(product.getCategory().getId())
                    .build()
            ))
            .build())
        .build();

    productsGot = search(token, searchRequest);
    assertThat(productsGot).isNotNull();
    assertThat(productsGot.getTotalCount()).isEqualTo(1);

    searchRequest = SearchDto.<FilterDto>builder()
        .filter(ValueTextFilterDto.builder()
            .field("name")
            .value("wrong name")
            .build())
        .build();

    productsGot = search(token, searchRequest);
    assertThat(productsGot).isNotNull();
    assertThat(productsGot.getTotalCount()).isEqualTo(0);

    // Delete
    delete(token, product);

    productsGot = search(token, SearchDto.<FilterDto>builder().build());
    assertThat(productsGot.getTotalCount()).isEqualTo(0);
  }

  private ProductDtoV1 update(String token, ProductDtoV1 product) throws Exception {
    byte[] responseBody = mockMvc
        .perform(MockMvcRequestBuilders.put("/api/v1/products/" + product.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsBytes(product)))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse()
        .getContentAsByteArray();
    return objectMapper.readValue(responseBody, ProductDtoV1.class);
  }

  private void delete(String token, ProductDtoV1 product) throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.delete("/api/v1/products/" + product.getId())
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  private ProductDtoV1 getById(String token, ProductDtoV1 product) throws Exception {
    byte[] responseBody = mockMvc
        .perform(MockMvcRequestBuilders.get("/api/v1/products/" + product.getId())
            .header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn().getResponse()
        .getContentAsByteArray();
    return objectMapper.readValue(responseBody, ProductDtoV1.class);
  }

  private ProductDtoV1 create(String token, CategoryDtoV1 category) throws Exception {
    byte[] responseBody = mockMvc
        .perform(MockMvcRequestBuilders.post("/api/v1/products")
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

  private GenericPageDtoV1<ProductDtoV1> search(final String token,
      final SearchDto<FilterDto> productSearch) throws Exception {
    try {
      final byte[] responseBody = mockMvc
          .perform(MockMvcRequestBuilders.post("/api/v1/products/search")
              .header("Authorization", "Bearer " + token)
              .contentType(MediaType.APPLICATION_JSON_VALUE)
              .content(objectMapper.writerFor(new TypeReference<SearchDto<FilterDto>>() {
                  }).writeValueAsBytes(productSearch)))
          .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse()
          .getContentAsByteArray();

      return objectMapper.readValue(responseBody,
          new TypeReference<GenericPageDtoV1<ProductDtoV1>>() {
          });
    } catch (final Exception exception) {
      throw exception;
    }
  }
}
