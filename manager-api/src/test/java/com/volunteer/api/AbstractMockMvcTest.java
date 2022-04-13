package com.volunteer.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.security.model.AuthenticationRequest;
import com.volunteer.api.security.model.AuthenticationResponse;
import com.volunteer.api.service.RoleService;
import com.volunteer.api.service.UserService;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AbstractMockMvcTest extends AbstractTestWithPersistence {

  private static final AtomicInteger PHONE_NUM = new AtomicInteger();

  protected static VPUser SYSTEM_USER;

  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected UserService userService;
  @Autowired
  protected RoleService roleService;

  @BeforeAll
  public static void init(@Autowired UserService userService, @Autowired RoleService roleService) {
    SYSTEM_USER = createUser(generatePhoneNumber(), "pass", "operator", userService,
        roleService);
  }

  protected static String generatePhoneNumber() {
    return String.format("%12d", PHONE_NUM.incrementAndGet());
  }

  protected static VPUser createUser(String phoneNumber, String password, String role,
      UserService userService, RoleService roleService) {
    Optional<VPUser> existingUser = userService.getByPrincipal(phoneNumber);
    if (existingUser.isPresent()) {
      final VPUser result = existingUser.get();
      result.setPassword(password);

      return result;
    }

    VPUser user = new VPUser();
    user.setPhoneNumber(phoneNumber);
    user.setDisplayName("u-" + phoneNumber);
    user.setUserVerified(true);
    user.setPhoneNumberVerified(true);
    user.setPassword(password);
    user.setRole(roleService.get(role));

    final VPUser result = userService.create(user);
    result.setPassword(password);

    return result;
  }

  protected VPUser createUser(String phoneNumber, String password, String role) {
    return createUser(phoneNumber, password, role, userService, roleService);
  }

  public AuthenticationResponse login() throws Exception {
    return login(SYSTEM_USER.getPhoneNumber(), SYSTEM_USER.getPassword());
  }

  public AuthenticationResponse login(String principal, String password) throws Exception {
    byte[] authResponseBytes = mockMvc
        .perform(MockMvcRequestBuilders.post("/api/v1/authenticate")
            .content(objectMapper.writeValueAsBytes(
                AuthenticationRequest.builder().principal(principal).password(password).build()))
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
