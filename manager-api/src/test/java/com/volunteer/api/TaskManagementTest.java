package com.volunteer.api;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.volunteer.api.data.model.TaskStatus;
import com.volunteer.api.data.model.api.GenericCollectionDtoV1;
import com.volunteer.api.data.model.api.TaskBatchDtoV1;
import com.volunteer.api.data.model.api.TaskDetalizationDtoV1;
import com.volunteer.api.data.model.api.TaskDtoV1;
import com.volunteer.api.data.model.persistence.Address;
import com.volunteer.api.data.model.persistence.Category;
import com.volunteer.api.data.model.persistence.Product;
import com.volunteer.api.data.model.persistence.Store;
import com.volunteer.api.data.repository.ProductRepository;
import com.volunteer.api.data.repository.TaskRepository;
import com.volunteer.api.data.repository.UserRepository;
import com.volunteer.api.service.AddressService;
import com.volunteer.api.service.CategoryService;
import com.volunteer.api.service.StoreService;
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

  @Autowired
  private TaskRepository taskRepository;

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
    Assert.assertEquals(0, taskRepository.count());
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
    Assert.assertEquals(1, taskRepository.count());

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
    List<TaskDtoV1> batchCreatedTasks =
        batchCreateTasks(token).stream().collect(Collectors.toList());
    Assert.assertEquals(2, batchCreatedTasks.size());
    Assert.assertEquals(3, taskRepository.count());
    int taskId2 = batchCreatedTasks.get(0).getId();
    int taskId3 = batchCreatedTasks.get(1).getId();

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

    // Reject task
    TaskDtoV1 task4 = createTask(token);
    mockMvc
        .perform(MockMvcRequestBuilders.post("/tasks/" + task4.getId() + "/reject")
            .header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.REJECTED,
        taskService.getTaskById(task4.getId()).get().getStatus());

    // Batch complete on new unverified task should be rejected
    mockMvc
        .perform(MockMvcRequestBuilders.post("/tasks/batch/complete")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(GenericCollectionDtoV1.<Integer>builder()
                .items(List.of(taskId2, taskId3)).build())))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    Assert.assertEquals(TaskStatus.NEW, taskService.getTaskById(taskId2).get().getStatus());
    Assert.assertEquals(TaskStatus.NEW, taskService.getTaskById(taskId3).get().getStatus());

    // Batch verify tasks
    mockMvc
        .perform(MockMvcRequestBuilders.post("/tasks/batch/verify")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(GenericCollectionDtoV1.<Integer>builder()
                .items(List.of(taskId2, taskId3)).build())))
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.VERIFIED, taskService.getTaskById(taskId2).get().getStatus());
    Assert.assertEquals(TaskStatus.VERIFIED, taskService.getTaskById(taskId3).get().getStatus());

    // Batch complete tasks
    mockMvc
        .perform(MockMvcRequestBuilders.post("/tasks/batch/complete")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(GenericCollectionDtoV1.<Integer>builder()
                .items(List.of(taskId2, taskId3)).build())))
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.COMPLETED, taskService.getTaskById(taskId2).get().getStatus());
    Assert.assertEquals(TaskStatus.COMPLETED, taskService.getTaskById(taskId3).get().getStatus());

    // Batch reject tasks
    List<TaskDtoV1> batchCreatedTasks2 =
        batchCreateTasks(token).stream().collect(Collectors.toList());
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/tasks/" + batchCreatedTasks2.get(0).getId() + "/verify")
                .header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.VERIFIED,
        taskService.getTaskById(batchCreatedTasks2.get(0).getId()).get().getStatus());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/tasks/batch/reject")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(GenericCollectionDtoV1.<Integer>builder()
                    .items(batchCreatedTasks2.stream().map(TaskDtoV1::getId)
                        .collect(Collectors.toList()))
                    .build())))
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.REJECTED,
        taskService.getTaskById(batchCreatedTasks2.get(0).getId()).get().getStatus());
    Assert.assertEquals(TaskStatus.REJECTED,
        taskService.getTaskById(batchCreatedTasks2.get(1).getId()).get().getStatus());
  }

  protected TaskDtoV1 createTask(String token) throws Exception {
    byte[] responseBody = mockMvc
        .perform(MockMvcRequestBuilders.post("/tasks").header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsBytes(TaskDtoV1.builder().customer("test")
                .customerStoreId(storeService.getByName("Test").iterator().next().getId())
                .volunteerStoreId(storeService.getByName("Test").iterator().next().getId())
                .productMeasure("units").quantity(BigDecimal.TEN).priority(1)
                .deadlineDate(ZonedDateTime.now().plusDays(365).toEpochSecond())
                .productId(productRepository.findAll().iterator().next().getId()).build())))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse()
        .getContentAsByteArray();
    TaskDtoV1 response = objectMapper.readValue(responseBody, TaskDtoV1.class);
    return response;
  }

  protected Collection<TaskDtoV1> batchCreateTasks(String token) throws Exception {
    TaskDtoV1 blueprint = TaskDtoV1.builder().customer("test")
        .customerStoreId(storeService.getByName("Test").iterator().next().getId())
        .volunteerStoreId(storeService.getByName("Test").iterator().next().getId())
        .productMeasure("units").quantity(BigDecimal.TEN).priority(1)
        .deadlineDate(ZonedDateTime.now().plusDays(365).toEpochSecond()).productId(0).build();

    int productId = productRepository.findAll().iterator().next().getId();
    byte[] responseBody = mockMvc
        .perform(MockMvcRequestBuilders.post("/tasks/batch")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsBytes(TaskBatchDtoV1.builder().blueprint(blueprint)
                .details(Arrays.asList(
                    TaskDetalizationDtoV1.builder().productId(productId).quantity(20)
                        .unitOfMeasure("units").build(),
                    TaskDetalizationDtoV1.builder().productId(productId).quantity(30)
                        .unitOfMeasure("units").build()))
                .build())))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse()
        .getContentAsByteArray();
    GenericCollectionDtoV1<TaskDtoV1> response = objectMapper.readValue(responseBody,
        new TypeReference<GenericCollectionDtoV1<TaskDtoV1>>() {});
    return response.getItems();
  }
}
