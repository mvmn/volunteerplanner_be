package com.volunteer.api;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.volunteer.api.data.model.TaskStatus;
import com.volunteer.api.data.model.api.TaskDtoV1;
import com.volunteer.api.data.model.persistence.Address;
import com.volunteer.api.data.model.persistence.Category;
import com.volunteer.api.data.model.persistence.Product;
import com.volunteer.api.data.model.persistence.Store;
import com.volunteer.api.data.repository.ProductRepository;
import com.volunteer.api.data.repository.TaskRepository;
import com.volunteer.api.data.repository.UserRepository;
import com.volunteer.api.data.service.StoreService;
import com.volunteer.api.service.AddressService;
import com.volunteer.api.service.CategoryService;
import com.volunteer.api.service.TaskService;

public class TaskManagementTest extends AbstractMockMvcTest {

  @Autowired
  private StoreService storeService;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private TaskService taskService;

  @BeforeAll
  public static void initTestData(@Autowired StoreService storeService,
      @Autowired AddressService addressService, @Autowired ProductRepository productRepository,
      @Autowired CategoryService categoryService, @Autowired TaskRepository taskRepository) {
    storeService.create(Store.builder().contactPerson("Test").name("Test")
        .address(addressService.getOrCreate(new Address(null, "test", "test", "test"))).build());
    Product product = new Product();
    product.setName("Test");
    product.setCategory(categoryService.create(Category.builder().name("Test").build()));
    productRepository.save(product);
    taskRepository.deleteAll();
  }

  @Test
  public void testTaskCrud() throws Exception {
    String token = loginAsOperator().getRefreshToken();

    // Test get and batch get of non-existing tasks
    mockMvc
        .perform(MockMvcRequestBuilders.get("/tasks/batch/1,2,3").header("Authorization",
            "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.isA(List.class)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/tasks/1").header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isNotFound());

    // Test create task
    TaskDtoV1 response = createTask(token);
    Assert.assertNotNull(response.getId());
    Assert.assertEquals(TaskStatus.NEW, response.getStatus());

    // Get existing task
    int taskId = response.getId();
    byte[] responseBodyGetTask = mockMvc
        .perform(MockMvcRequestBuilders.get("/tasks/" + taskId).header("Authorization",
            "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue())).andReturn()
        .getResponse().getContentAsByteArray();

    TaskDtoV1 getResponse = objectMapper.readValue(responseBodyGetTask, TaskDtoV1.class);
    Assert.assertEquals(userRepo.findByUserName("op").getId(), getResponse.getCreatedByUserId());
    Assert.assertTrue(
        (getResponse.getCreatedAt().longValue() - ZonedDateTime.now().toEpochSecond()) < 180L);

    // Create more and bulk get
    int taskId2 = createTask(token).getId();
    int taskId3 = createTask(token).getId();

    mockMvc
        .perform(MockMvcRequestBuilders.get("/tasks/batch/" + Stream.of(taskId, taskId2, taskId3)
            .map(v -> v.toString()).collect(Collectors.joining(",")))
            .header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.isA(List.class)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(3)));

    // Complete on new unverified task should be rejected
    mockMvc
        .perform(MockMvcRequestBuilders.post("/tasks/" + taskId + "/complete")
            .header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    Assert.assertEquals(TaskStatus.NEW, taskService.getTaskById(taskId).get().getStatus());

    // Verify task
    mockMvc.perform(MockMvcRequestBuilders.post("/tasks/" + taskId + "/verify")
        .header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.VERIFIED, taskService.getTaskById(taskId).get().getStatus());

    // Complete task
    mockMvc
        .perform(MockMvcRequestBuilders.post("/tasks/" + taskId + "/complete")
            .header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.COMPLETED, taskService.getTaskById(taskId).get().getStatus());
  }

  protected TaskDtoV1 createTask(String token) throws Exception {
    byte[] responseBody = mockMvc
        .perform(MockMvcRequestBuilders.post("/tasks").header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsBytes(TaskDtoV1.builder().customer("test")
                .customerStoreId(storeService.getByName("Test").iterator().next().getId())
                .volunteerStoreId(storeService.getByName("Test").iterator().next().getId())
                .productMeasure("units").quantity(10).priority(1)
                .deadlineDate(ZonedDateTime.now().plusDays(365).toEpochSecond())
                .productId(productRepository.findAll().iterator().next().getId()).build())))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse()
        .getContentAsByteArray();
    TaskDtoV1 response = objectMapper.readValue(responseBody, TaskDtoV1.class);
    return response;
  }
}
