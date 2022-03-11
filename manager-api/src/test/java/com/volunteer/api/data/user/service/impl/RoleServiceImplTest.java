package com.volunteer.api.data.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.volunteer.api.data.user.model.dto.RoleDto;
import com.volunteer.api.data.user.service.RoleService;
import com.volunteer.api.error.ObjectNotFoundException;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Testcontainers
public class RoleServiceImplTest {

  @Autowired
  private RoleService service;

  @Test
  public void getAllWorks() {
    final List<RoleDto> actual = service.getAll();
    assertTrue(CollectionUtils.isNotEmpty(actual));
  }

  @Test
  public void getByNameWorks() {
    final String name = "volunteer";
    final RoleDto actual = service.get(name);

    assertNotNull(actual);
    assertThat(actual.getName()).isEqualTo(name);
  }

  @Test
  public void getByNameThrowsExceptionIfNameMissed() {
    final String name = "phone";
    assertThrows(ObjectNotFoundException.class, () -> service.get(name));
  }

}
