package com.volunteer.api;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.volunteer.api.data.model.TaskStatus;
import com.volunteer.api.data.model.api.GenericCollectionDtoV1;
import com.volunteer.api.data.model.api.GenericPageDtoV1;
import com.volunteer.api.data.model.api.IntegerIdsDtoV1;
import com.volunteer.api.data.model.api.TaskBatchDtoV1;
import com.volunteer.api.data.model.api.TaskDetalizationDtoV1;
import com.volunteer.api.data.model.api.TaskDtoV1;
import com.volunteer.api.data.model.api.TaskSearchDtoV1;
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

    // Test get and batch get of non-existing tasks
    post(token1, "/tasks/batchget", IntegerIdsDtoV1.builder().ids(Arrays.asList(1, 2, 3)).build())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.isA(List.class)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/tasks/1").header("Authorization", "Bearer " + token1))
        .andExpect(MockMvcResultMatchers.status().isNotFound());

    // Test create task
    TaskDtoV1 response = createTask(token1);
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

    TaskDtoV1 getResponse = objectMapper.readValue(responseBodyGetTask, TaskDtoV1.class);
    Assert.assertEquals(userRepo.findByUserName("op").getId(), getResponse.getCreatedByUserId());
    Assert.assertTrue(
        (getResponse.getCreatedAt().longValue() - ZonedDateTime.now().toEpochSecond()) < 180L);

    // Create more and bulk get
    List<TaskDtoV1> batchCreatedTasks =
        batchCreateTasks(token2).stream().collect(Collectors.toList());
    Assert.assertEquals(2, batchCreatedTasks.size());
    Assert.assertEquals(3, taskRepository.count());
    int taskId2 = batchCreatedTasks.get(0).getId();
    int taskId3 = batchCreatedTasks.get(1).getId();

    post(token1, "/tasks/batchget",
        IntegerIdsDtoV1.builder().ids(Arrays.asList(taskId, taskId2, taskId3)).build())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.isA(List.class)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(3)));

    // Complete on new unverified task should be rejected
    post(token1, "/tasks/" + taskId + "/complete", null)
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    Assert.assertEquals(TaskStatus.NEW, taskService.getTaskById(taskId).get().getStatus());

    // Verify task
    post(token3, "/tasks/" + taskId + "/verify", null)
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.VERIFIED, taskService.getTaskById(taskId).get().getStatus());

    // Complete task
    post(token1, "/tasks/" + taskId + "/complete", null)
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.COMPLETED, taskService.getTaskById(taskId).get().getStatus());

    // Reject task
    TaskDtoV1 task4 = createTask(token1);
    post(token1, "/tasks/" + task4.getId() + "/reject", null)
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.REJECTED,
        taskService.getTaskById(task4.getId()).get().getStatus());

    // Batch complete on new unverified task should be rejected
    post(token1, "/tasks/batch/complete",
        IntegerIdsDtoV1.builder().ids(List.of(taskId2, taskId3)).build())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    Assert.assertEquals(TaskStatus.NEW, taskService.getTaskById(taskId2).get().getStatus());
    Assert.assertEquals(TaskStatus.NEW, taskService.getTaskById(taskId3).get().getStatus());

    // Batch verify tasks
    post(token2, "/tasks/batch/verify",
        IntegerIdsDtoV1.builder().ids(List.of(taskId2, taskId3)).build())
            .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.VERIFIED, taskService.getTaskById(taskId2).get().getStatus());
    Assert.assertEquals(TaskStatus.VERIFIED, taskService.getTaskById(taskId3).get().getStatus());

    // Batch complete tasks
    post(token3, "/tasks/batch/complete",
        IntegerIdsDtoV1.builder().ids(List.of(taskId2, taskId3)).build())
            .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.COMPLETED, taskService.getTaskById(taskId2).get().getStatus());
    Assert.assertEquals(TaskStatus.COMPLETED, taskService.getTaskById(taskId3).get().getStatus());

    // Batch reject tasks
    List<TaskDtoV1> batchCreatedTasks2 =
        batchCreateTasks(token1).stream().collect(Collectors.toList());
    post(token1, "/tasks/" + batchCreatedTasks2.get(0).getId() + "/verify", null)
        .andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.VERIFIED,
        taskService.getTaskById(batchCreatedTasks2.get(0).getId()).get().getStatus());

    post(token2, "/tasks/batch/reject",
        IntegerIdsDtoV1.builder()
            .ids(batchCreatedTasks2.stream().map(TaskDtoV1::getId).collect(Collectors.toList()))
            .build()).andExpect(MockMvcResultMatchers.status().isOk());
    Assert.assertEquals(TaskStatus.REJECTED,
        taskService.getTaskById(batchCreatedTasks2.get(0).getId()).get().getStatus());
    Assert.assertEquals(TaskStatus.REJECTED,
        taskService.getTaskById(batchCreatedTasks2.get(1).getId()).get().getStatus());

    // Test search by user ID
    {
      Set<Integer> tasksCreatedByOp2 =
          search(token1, TaskSearchDtoV1.builder().createdByUserId(userIdOp2).build()).getItems()
              .stream().map(TaskDtoV1::getId).collect(Collectors.toSet());
      Assert.assertEquals(2, tasksCreatedByOp2.size());
      Assert.assertTrue(tasksCreatedByOp2.contains(taskId2));
      Assert.assertTrue(tasksCreatedByOp2.contains(taskId3));
    }

    {
      Set<Integer> tasksVerifiedByOp3 =
          search(token1, TaskSearchDtoV1.builder().verifiedByUserId(userIdOp3).build()).getItems()
              .stream().map(TaskDtoV1::getId).collect(Collectors.toSet());
      Assert.assertEquals(1, tasksVerifiedByOp3.size());
      Assert.assertTrue(tasksVerifiedByOp3.contains(taskId));
    }

    {
      Set<Integer> tasksClosedByOp3 =
          search(token1, TaskSearchDtoV1.builder().closedByUserId(userIdOp3).build()).getItems()
              .stream().map(TaskDtoV1::getId).collect(Collectors.toSet());
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

  protected TaskDtoV1 createTask(String token) throws Exception {
    return getResponseAs(mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
        .header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsBytes(TaskDtoV1.builder().customer("test")
            .customerStoreId(1).volunteerStoreId(1).productMeasure("units").quantity(BigDecimal.TEN)
            .priority(1).deadlineDate(ZonedDateTime.now().plusDays(365).toEpochSecond())
            .productId(product.getId()).build())))
        .andExpect(MockMvcResultMatchers.status().isOk()), TaskDtoV1.class);
  }

  protected Collection<TaskDtoV1> batchCreateTasks(String token) throws Exception {
    TaskDtoV1 blueprint = TaskDtoV1.builder().customer("test").customerStoreId(store.getId())
        .volunteerStoreId(store.getId()).productMeasure("units").quantity(BigDecimal.TEN)
        .priority(1).deadlineDate(ZonedDateTime.now().plusDays(365).toEpochSecond()).productId(0)
        .build();

    int productId = productRepository.findAll().iterator().next().getId();
    return getResponseAs(
        mockMvc
            .perform(MockMvcRequestBuilders.post("/tasks/batch")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(
                    objectMapper.writeValueAsBytes(TaskBatchDtoV1.builder().blueprint(blueprint)
                        .details(Arrays.asList(
                            TaskDetalizationDtoV1.builder().productId(productId).quantity(20)
                                .unitOfMeasure("units").build(),
                            TaskDetalizationDtoV1.builder().productId(productId).quantity(30)
                                .unitOfMeasure("units").build()))
                        .build())))
            .andExpect(MockMvcResultMatchers.status().isOk()),
        new TypeReference<GenericCollectionDtoV1<TaskDtoV1>>() {}).getItems();
  }

  protected GenericPageDtoV1<TaskDtoV1> search(String token, TaskSearchDtoV1 query)
      throws Exception {
    return getResponseAs(
        post(token, "/tasks/search", query).andExpect(MockMvcResultMatchers.status().isOk()),
        new TypeReference<GenericPageDtoV1<TaskDtoV1>>() {});
  }
}
