package com.volunteer.api.data.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.volunteer.api.AbstractTestWithPersistence;
import com.volunteer.api.data.model.SubtaskStatus;
import com.volunteer.api.data.model.TaskPriority;
import com.volunteer.api.data.model.TaskStatus;
import com.volunteer.api.data.model.persistence.Category;
import com.volunteer.api.data.model.persistence.Product;
import com.volunteer.api.data.model.persistence.Store;
import com.volunteer.api.data.model.persistence.Subtask;
import com.volunteer.api.data.model.persistence.Task;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.repository.ProductRepository;
import com.volunteer.api.error.InvalidStatusException;
import com.volunteer.api.service.AddressService;
import com.volunteer.api.service.CategoryService;
import com.volunteer.api.service.RoleService;
import com.volunteer.api.service.StoreService;
import com.volunteer.api.service.SubtaskService;
import com.volunteer.api.service.TaskService;
import com.volunteer.api.service.UserService;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@ActiveProfiles("test")
public class SubtaskServiceImplTest extends AbstractTestWithPersistence {

  @Autowired
  private SubtaskService subtaskService;
  @Autowired
  private AddressService addressService;
  @Autowired
  private StoreService storeService;
  @Autowired
  private CategoryService categoryService;
  @Autowired
  private TaskService taskService;
  @Autowired
  private UserService userService;
  @Autowired
  private RoleService roleService;
  @Autowired
  private ProductRepository productRepository;

  @Test
  public void crudOperationsTest() {
    // Prepare
    Store volunteerStore = storeService.create(Store.builder()
        .name("vol store")
        .city(addressService.getCityById(12))
        .address("address")
        .build());
    Store customerStore = storeService.create(Store.builder()
        .name("cust name")
        .city(addressService.getCityById(20))
        .address("address")
        .build());
    Category category = categoryService.create(Category.builder()
        .name("cat1")
        .build());

    Product product = new Product();
    product.setCategory(category);
    product.setName("prod 1");
    product = productRepository.save(product);

    VPUser user = new VPUser();
    user.setPhoneNumber("1234567");
    user.setRole(roleService.get("operator"));
    user.setUserName("u1");
    user.setPassword("pwd");
    user = userService.create(user);

    Task task = new Task();
    task.setProduct(product);
    task.setQuantity(BigDecimal.TEN);
    task.setProductMeasure("kg");
    task.setDeadlineDate(ZonedDateTime.now().plusDays(1));
    task.setCreatedAt(ZonedDateTime.now());
    task.setVolunteerStore(volunteerStore);
    task.setCustomerStore(customerStore);
    SecurityContextHolder.getContext()
        .setAuthentication(new TestingAuthenticationToken(user.getUserName(), null));

    task = taskService.create(task);
    taskService.changeStatus(task.getId(), TaskStatus.VERIFIED);

    // Create
    Subtask subtask1 =
        Subtask.builder()
            .task(task)
            .quantity(BigDecimal.valueOf(3L))
            .note("st note")
            .build();
    Subtask createdSubtask = subtaskService.create(subtask1);
    assertThat(createdSubtask.getId()).isGreaterThan(0);
    assertThat(createdSubtask.getQuantity()).isEqualTo(subtask1.getQuantity());
    assertThat(createdSubtask.getStatus()).isEqualTo(SubtaskStatus.IN_PROGRESS);
    assertThat(createdSubtask.getTask().getId()).isEqualTo(subtask1.getTask().getId());

    Task updatedTask = taskService.get(task.getId());
    assertNotNull(updatedTask);
    assertThat(updatedTask.getQuantityLeft().longValue()).isEqualTo(7L);

    // Complete
    subtaskService.complete(subtask1.getId());
    final Subtask completedSubtask = subtaskService.getById(subtask1.getId(), false);
    assertThat(completedSubtask.getStatus()).isEqualTo(SubtaskStatus.COMPLETED);

    updatedTask = taskService.get(task.getId());
    assertNotNull(updatedTask);
    assertThat(updatedTask.getQuantityLeft().longValue()).isEqualTo(7L);

    assertThrows(
        InvalidStatusException.class, () -> subtaskService.reject(completedSubtask.getId(), false));

    // Reject
    Subtask subtask2 =
        Subtask.builder()
            .quantity(BigDecimal.valueOf(4L))
            .note("st note 2")
            .task(task)
            .build();
    subtask2 = subtaskService.create(subtask2);

    subtaskService.reject(subtask2.getId(), false);
    final Subtask rejectedSubtask = subtaskService.getById(subtask2.getId(), false);

    assertThat(rejectedSubtask.getStatus()).isEqualTo(SubtaskStatus.REJECTED);
    updatedTask = taskService.get(task.getId());
    assertNotNull(updatedTask);
    assertThat(updatedTask.getQuantityLeft().longValue()).isEqualTo(7L);

    assertThrows(
        InvalidStatusException.class, () -> subtaskService.complete(rejectedSubtask.getId()));
  }
}
