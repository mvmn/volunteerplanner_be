package com.volunteer.api.data.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.volunteer.api.data.model.persistence.Category;
import com.volunteer.api.service.CategoryService;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Testcontainers
@ActiveProfiles("test")
public class CategoryServiceImplTest {

  @Autowired
  private CategoryService service;

  @Test
  public void crudOperationsTest() {
    final Category expectedRoot = Category.builder()
        .name("root")
        .note("root-note")
        .build();

    Category actualRoot = service.create(expectedRoot);
    assertProperties(expectedRoot, actualRoot);

    expectedRoot.setId(actualRoot.getId());
    expectedRoot.setName("root-name");
    expectedRoot.setNote("root-note");

    actualRoot = service.update(expectedRoot);
    assertProperties(expectedRoot, actualRoot);

    final Category expectedChild1 = Category.builder()
        .parent(actualRoot)
        .name("root-child1")
        .note("root-child1-note")
        .build();

    Category actualChild1 = service.create(expectedChild1);
    assertProperties(expectedChild1, actualChild1);

    final List<Category> roots = service.getRoots();
    assertThat(roots.size()).isEqualTo(1);
    assertProperties(expectedRoot, roots.get(0));

    final List<Category> actualByName = service.getByName(expectedChild1.getName());
    assertThat(actualByName.size()).isEqualTo(1);
    assertProperties(expectedChild1, actualByName.get(0));
  }

  private void assertProperties(final Category expected, final Category actual) {
    assertTrue(Objects.nonNull(actual.getId()));

    assertThat(Objects.isNull(actual.getParent())).isEqualTo(Objects.isNull(expected.getParent()));
    if (Objects.nonNull(expected.getParent())) {
      assertThat(actual.getParent().getId()).isEqualTo(expected.getParent().getId());
    }

    assertThat(actual.getName()).isEqualTo(expected.getName());
    assertThat(actual.getNote()).isEqualTo(expected.getNote());

    if (Objects.isNull(expected.getParent())) {
      assertThat(actual.getPath()).isEqualTo(String.format("/%d/", actual.getId()));
    } else {
      assertThat(actual.getPath()).isEqualTo(actual.getParent().getPath() + actual.getId() + "/");
    }
  }

}
