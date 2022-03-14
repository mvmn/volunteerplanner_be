package com.volunteer.api.data.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.volunteer.api.data.model.SubtaskStatus;
import com.volunteer.api.data.model.persistence.*;
import com.volunteer.api.data.repository.ProductRepository;
import com.volunteer.api.error.InvalidStatusException;
import com.volunteer.api.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@ActiveProfiles("test")
public class SubtaskServiceImplTest {
  @Autowired private SubtaskService subtaskService;
  @Autowired private AddressService addressService;
  @Autowired private StoreService storeService;
  @Autowired private CategoryService categoryService;
  @Autowired private TaskService taskService;
  @Autowired private UserService userService;
  @Autowired private RoleService roleService;
  @Autowired private ProductRepository productRepository;

  @Test
  public void crudOperationsTest() {
    // Prepare
    Address address =
        addressService.getOrCreate(Address.builder().city("c1").region("r1").address("a1").build());
    Store volunteerStore =
        storeService.create(Store.builder().name("vol store").address(address).build());
    Store customerStore =
        storeService.create(Store.builder().name("cust name").address(address).build());
    Category category = categoryService.create(Category.builder().name("cat1").build());

    Product product = new Product();
    product.setCategory(category);
    product.setName("prod 1");
    product = productRepository.save(product);

    VPUser user = new VPUser();
    user.setPhoneNumber("1234567");
    user.setRole(roleService.get("operator"));
    user.setAddress(address);
    user.setUserName("u1");
    user.setPassword("pwd");
    user = userService.create(user);

    Task task = new Task();
    task.setCustomer("cust 1");
    task.setProduct(product);
    task.setQuantity(10);
    task.setPriority(1);
    task.setProductMeasure("kg");
    task.setDeadlineDate(ZonedDateTime.now().plusDays(1));
    task.setCreatedAt(ZonedDateTime.now());
    task.setVolunteerStore(volunteerStore);
    task.setCustomerStore(customerStore);
    SecurityContextHolder.getContext()
        .setAuthentication(new TestingAuthenticationToken(user.getUserName(), null));

    task = taskService.createTask(task);

    // Create
    Subtask subtask1 =
        Subtask.builder()
            .product(product)
            .quantity(3)
            .note("st note")
            .volunteer(user)
            .task(task)
            .build();
    Subtask createdSubtask = subtaskService.createSubtask(subtask1);
    assertThat(createdSubtask.getId()).isGreaterThan(0);
    assertThat(createdSubtask.getProduct().getId()).isEqualTo(subtask1.getProduct().getId());
    assertThat(createdSubtask.getQuantity()).isEqualTo(subtask1.getQuantity());
    assertThat(createdSubtask.getStatus()).isEqualTo(SubtaskStatus.IN_PROGRESS);
    assertThat(createdSubtask.getTask().getId()).isEqualTo(subtask1.getTask().getId());
    assertThat(createdSubtask.getVolunteer().getId()).isEqualTo(subtask1.getVolunteer().getId());

    Optional<Task> updatedTask = taskService.getTaskById(task.getId());
    assertTrue(updatedTask.isPresent());
    assertThat(updatedTask.get().getQuantityLeft()).isEqualTo(7);

    // Complete
    subtaskService.complete(subtask1.getId());
    final Subtask completedSubtask = subtaskService.findBySubtaskId(subtask1.getId());
    assertThat(completedSubtask.getStatus()).isEqualTo(SubtaskStatus.COMPLETED);

    updatedTask = taskService.getTaskById(task.getId());
    assertTrue(updatedTask.isPresent());
    assertThat(updatedTask.get().getQuantityLeft()).isEqualTo(7);

    assertThrows(
        InvalidStatusException.class, () -> subtaskService.complete(completedSubtask.getId()));

    // Reject
    Subtask subtask2 =
        Subtask.builder()
            .product(product)
            .quantity(4)
            .note("st note 2")
            .volunteer(user)
            .task(task)
            .build();
    subtask2 = subtaskService.createSubtask(subtask2);

    subtaskService.reject(subtask2.getId());
    final Subtask rejectedSubtask = subtaskService.findBySubtaskId(subtask2.getId());

    assertThat(rejectedSubtask.getStatus()).isEqualTo(SubtaskStatus.REJECTED);
    updatedTask = taskService.getTaskById(task.getId());
    assertTrue(updatedTask.isPresent());
    assertThat(updatedTask.get().getQuantityLeft()).isEqualTo(7);

    assertThrows(
        InvalidStatusException.class, () -> subtaskService.reject(rejectedSubtask.getId()));

    // Find
    Collection<Subtask> foundSubtasks = subtaskService.findByProductId(product.getId());
    assertFalse(foundSubtasks.isEmpty());

    foundSubtasks = subtaskService.findByTaskId(task.getId());
    assertFalse(foundSubtasks.isEmpty());

    foundSubtasks = subtaskService.findByVolunteerId(user.getId());
    assertFalse(foundSubtasks.isEmpty());

    foundSubtasks = subtaskService.findByVolunteerPrincipal(user.getUserName());
    assertFalse(foundSubtasks.isEmpty());
  }
}
