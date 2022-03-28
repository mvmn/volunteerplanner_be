package com.volunteer.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.volunteer.api.data.model.TaskStatus;
import com.volunteer.api.data.model.api.GenericCollectionDtoV1;
import com.volunteer.api.data.model.api.GenericPageDtoV1;
import com.volunteer.api.data.model.api.TaskDtoV1;
import com.volunteer.api.data.model.api.TaskSearchDtoV1;
import com.volunteer.api.data.model.api.TaskViewDtoV1;
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
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class TaskManagementTest extends AbstractMockMvcTest {

  private static Store store;
  private static Product product;

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

    store = storeService.create(Store.builder().name("Test").city(addressService.getCityById(12))
        .address("address").build());

    product = productRepository.save(Product.builder().name("Test")
        .category(categoryService.create(Category.builder().name("Test").build())).build());

    taskRepository.deleteAll();
  }

  @Test
  public void testTaskCrud() throws Exception {
    int userIdOp2 = createUser("op2", "pass", "operator").getId();
    int userIdOp3 = createUser("op3", "pass", "operator").getId();

    Assert.assertEquals(0, taskRepository.count());
    String token1 = login("op", "pass").getRefreshToken();
    String token2 = login("op2", "pass").getRefreshToken();
    String token3 = login("op3", "pass").getRefreshToken();

    // Test get and search by ids of non-existing tasks
    mockMvc
        .perform(MockMvcRequestBuilders.get("/tasks/search?ids=1,2,3")
            .header("Authorization", "Bearer " + token1))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.isA(List.class)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/tasks/1").header("Authorization", "Bearer " + token1))
        .andExpect(MockMvcResultMatchers.status().isNotFound());

    // Test create task
    TaskViewDtoV1 response = createTask(token1);
    Assert.assertNotNull(response.getId());
    Assert.assertEquals(TaskStatus.NEW, response.getStatus());
    Assert.assertEquals(1, taskRepository.count());

    // Get existing task
    int taskId = response.getId();
    byte[] responseBodyGetTask = mockMvc
        .perform(MockMvcRequestBuilders.get("/tasks/" + taskId).header("Authorization",
            "Bearer " + token1))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue())).andReturn()
        .getResponse().getContentAsByteArray();

    TaskViewDtoV1 getResponse = objectMapper.readValue(responseBodyGetTask, TaskViewDtoV1.class);
    Assert.assertEquals(userRepo.findByUserName("op").getId(), getResponse.getCreatedBy().getId());
    Assert.assertTrue(
        (getResponse.getCreatedAt().longValue() - ZonedDateTime.now().toEpochSecond()) < 180L);

    // Create more and bulk get
    List<TaskViewDtoV1> batchCreatedTasks =
        batchCreateTasks(token2).stream().collect(Collectors.toList());
    Assert.assertEquals(2, batchCreatedTasks.size());
    Assert.assertEquals(3, taskRepository.count());
    int taskId2 = batchCreatedTasks.get(0).getId();
    int taskId3 = batchCreatedTasks.get(1).getId();

    mockMvc
        .perform(MockMvcRequestBuilders.get("/tasks/search?ids=" +
                Stream.of(taskId, taskId2, taskId3)
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")))
            .header("Authorization", "Bearer " + token1))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.isA(List.class)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(3)));

    // Complete on new unverified task should be rejected
    post(token1, "/tasks/" + taskId + "/complete", null)
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    Assert.assertEquals(TaskStatus.NEW, taskService.get(taskId).getStatus());

    // Verify task
    post(token3, "/tasks/" + taskId + "/verify", null)
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.VERIFIED, taskService.get(taskId).getStatus());

    // Complete task
    post(token1, "/tasks/" + taskId + "/complete", null)
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.COMPLETED, taskService.get(taskId).getStatus());

    // Reject task
    TaskViewDtoV1 task4 = createTask(token1);
    post(token1, "/tasks/" + task4.getId() + "/reject", null)
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.REJECTED,
        taskService.get(task4.getId()).getStatus());

    // Batch complete on new unverified task should be rejected
    post(token1, "/tasks/batch/complete",
        GenericCollectionDtoV1.<Integer>builder().items(List.of(taskId2, taskId3)).build())
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    Assert.assertEquals(TaskStatus.NEW, taskService.get(taskId2).getStatus());
    Assert.assertEquals(TaskStatus.NEW, taskService.get(taskId3).getStatus());

    // Batch verify tasks
    post(token2, "/tasks/batch/verify",
        GenericCollectionDtoV1.<Integer>builder().items(List.of(taskId2, taskId3)).build())
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.VERIFIED, taskService.get(taskId2).getStatus());
    Assert.assertEquals(TaskStatus.VERIFIED, taskService.get(taskId3).getStatus());

    // Batch complete tasks
    post(token3, "/tasks/batch/complete",
        GenericCollectionDtoV1.<Integer>builder().items(List.of(taskId2, taskId3)).build())
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.COMPLETED, taskService.get(taskId2).getStatus());
    Assert.assertEquals(TaskStatus.COMPLETED, taskService.get(taskId3).getStatus());

    // Batch reject tasks
    List<TaskViewDtoV1> batchCreatedTasks2 =
        batchCreateTasks(token1).stream().collect(Collectors.toList());
    post(token1, "/tasks/" + batchCreatedTasks2.get(0).getId() + "/verify", null)
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.VERIFIED,
        taskService.get(batchCreatedTasks2.get(0).getId()).getStatus());

    post(token2, "/tasks/batch/reject",
        GenericCollectionDtoV1.<Integer>builder().items(batchCreatedTasks2.stream()
            .map(TaskViewDtoV1::getId).collect(Collectors.toList())).build())
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.REJECTED,
        taskService.get(batchCreatedTasks2.get(0).getId()).getStatus());
    Assert.assertEquals(TaskStatus.REJECTED,
        taskService.get(batchCreatedTasks2.get(1).getId()).getStatus());

    // Test search by user ID
    {
      Set<Integer> tasksCreatedByOp2 =
          search(token1, TaskSearchDtoV1.builder().createdByUserId(userIdOp2).build()).getItems()
              .stream().map(TaskViewDtoV1::getId).collect(Collectors.toSet());
      Assert.assertEquals(2, tasksCreatedByOp2.size());
      Assert.assertTrue(tasksCreatedByOp2.contains(taskId2));
      Assert.assertTrue(tasksCreatedByOp2.contains(taskId3));
    }

    {
      Set<Integer> tasksVerifiedByOp3 =
          search(token1, TaskSearchDtoV1.builder().verifiedByUserId(userIdOp3).build()).getItems()
              .stream().map(TaskViewDtoV1::getId).collect(Collectors.toSet());
      Assert.assertEquals(1, tasksVerifiedByOp3.size());
      Assert.assertTrue(tasksVerifiedByOp3.contains(taskId));
    }

    {
      Set<Integer> tasksClosedByOp3 =
          search(token1, TaskSearchDtoV1.builder().closedByUserId(userIdOp3).build()).getItems()
              .stream().map(TaskViewDtoV1::getId).collect(Collectors.toSet());
      Assert.assertEquals(2, tasksClosedByOp3.size());
      Assert.assertTrue(tasksClosedByOp3.contains(taskId2));
      Assert.assertTrue(tasksClosedByOp3.contains(taskId3));
    }
  }

  protected ResultActions post(String token, String url, Object body) throws Exception {
    MockHttpServletRequestBuilder reqBuilder =
        MockMvcRequestBuilders.post(url).header("Authorization", "Bearer " + token);
    if (body != null) {
      reqBuilder = reqBuilder.contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(objectMapper.writeValueAsString(body));
    }
    return mockMvc.perform(reqBuilder);
  }

  protected TaskViewDtoV1 createTask(String token) throws Exception {
    return getResponseAs(mockMvc
        .perform(MockMvcRequestBuilders.post("/tasks").header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsBytes(TaskDtoV1.builder()
                .customerStoreId(store.getId()).volunteerStoreId(store.getId())
                .productMeasure("units").quantity(BigDecimal.TEN)
                .deadlineDate(ZonedDateTime.now().plusDays(365).toEpochSecond())
                .productId(product.getId()).build())))
        .andExpect(MockMvcResultMatchers.status().isCreated()), TaskViewDtoV1.class);
  }

  protected Collection<TaskViewDtoV1> batchCreateTasks(String token) throws Exception {
    int productId = productRepository.findAll().iterator().next().getId();

    final TaskDtoV1 task1 = TaskDtoV1.builder().customerStoreId(store.getId())
        .volunteerStoreId(store.getId()).productMeasure("units").quantity(BigDecimal.TEN)
        .deadlineDate(ZonedDateTime.now().plusDays(365).toEpochSecond())
        .productId(productId).quantity(BigDecimal.valueOf(20)).productMeasure("units").build();

    final TaskDtoV1 task2 = TaskDtoV1.builder().customerStoreId(store.getId())
        .volunteerStoreId(store.getId()).productMeasure("units").quantity(BigDecimal.TEN)
        .deadlineDate(ZonedDateTime.now().plusDays(365).toEpochSecond())
        .productId(productId).quantity(BigDecimal.valueOf(30)).productMeasure("units").build();

    return getResponseAs(
        mockMvc
            .perform(MockMvcRequestBuilders.post("/tasks/batch")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(
                    objectMapper.writeValueAsBytes(GenericCollectionDtoV1.builder()
                        .items(List.of(task1, task2)).build())))
            .andExpect(MockMvcResultMatchers.status().isCreated()),
        new TypeReference<GenericCollectionDtoV1<TaskViewDtoV1>>() {
        }).getItems();
  }

  protected GenericPageDtoV1<TaskViewDtoV1> search(String token, TaskSearchDtoV1 query)
      throws Exception {
    return getResponseAs(
        post(token, "/tasks/search", query).andExpect(MockMvcResultMatchers.status().isOk()),
        new TypeReference<GenericPageDtoV1<TaskViewDtoV1>>() {
        });
  }


}
