package com.volunteer.api;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volunteer.api.AbstractMockMvcTest.EmbeddedRedisConfig;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.security.model.AuthenticationRequest;
import com.volunteer.api.security.model.AuthenticationResponse;
import com.volunteer.api.service.AddressService;
import com.volunteer.api.service.RoleService;
import com.volunteer.api.service.UserService;
import redis.embedded.RedisServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(EmbeddedRedisConfig.class)
public class AbstractMockMvcTest extends AbstractTestWithPersistence {

  @TestConfiguration
  public static class EmbeddedRedisConfig {

    @Bean
    public RedisServer redisServer() {
      return RedisServer.builder().port(6370).setting("maxmemory 128M").build();
    }
  }

  private static final AtomicInteger PHONE_NUM = new AtomicInteger();

  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected UserService userService;
  @Autowired
  protected RoleService roleService;
  @Autowired
  protected AddressService addressService;

  @BeforeAll
  public static void init(@Autowired UserService userService, @Autowired RoleService roleService,
      @Autowired AddressService addressService, @Autowired RedisServer redisServer) {
    redisServer.start();
    createUser("op", "pass", "operator", userService, roleService, addressService);
  }

  @AfterAll
  public static void destroy(@Autowired RedisServer redisServer) {
    redisServer.stop();
  }

  protected static VPUser createUser(String userName, String password, String role,
      UserService userService, RoleService roleService, AddressService addressService) {
    Optional<VPUser> existingUser = userService.get(userName);
    if (existingUser.isPresent()) {
      return existingUser.get();
    }
    VPUser user = new VPUser();
    user.setUserName(userName);
    user.setFullName(userName);
    user.setPhoneNumber(String.format("%09d", PHONE_NUM.incrementAndGet()));
    user.setUserVerified(true);
    user.setPhoneNumberVerified(true);
    user.setPassword(password);
    user.setRole(roleService.get(role));
    user.setCity(addressService.getCityById(10));
    return userService.create(user);
  }

  protected VPUser createUser(String userName, String password, String role) {
    return createUser(userName, password, role, userService, roleService, addressService);
  }

  public AuthenticationResponse login(String userName, String password) throws Exception {
    byte[] authResponseBytes = mockMvc
        .perform(MockMvcRequestBuilders.post("/authenticate")
            .content(objectMapper.writeValueAsBytes(
                AuthenticationRequest.builder().userName(userName).password(password).build()))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse()
        .getContentAsByteArray();

    AuthenticationResponse authResponse =
        objectMapper.readValue(authResponseBytes, AuthenticationResponse.class);
    return authResponse;
  }

  public <T> T getResponseAs(ResultActions result, TypeReference<T> type) {
    byte[] bytes = result.andReturn().getResponse().getContentAsByteArray();
    try {
      return objectMapper.readValue(bytes, type);
    } catch (IOException e) {
      throw new RuntimeException("Failed to map response to " + type, e);
    }
  }

  public <T> T getResponseAs(ResultActions result, Class<T> clazz) {
    byte[] bytes = result.andReturn().getResponse().getContentAsByteArray();
    try {
      return objectMapper.readValue(bytes, clazz);
    } catch (IOException e) {
      throw new RuntimeException("Failed to map response to " + clazz.getCanonicalName(), e);
    }
  }
}
