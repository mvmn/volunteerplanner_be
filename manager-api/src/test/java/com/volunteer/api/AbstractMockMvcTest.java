package com.volunteer.api;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volunteer.api.data.model.persistence.Address;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.security.model.AuthenticationRequest;
import com.volunteer.api.security.model.AuthenticationResponse;
import com.volunteer.api.service.AddressService;
import com.volunteer.api.service.RoleService;
import com.volunteer.api.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AbstractMockMvcTest {

  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;

  @BeforeAll
  public static void init(@Autowired UserService userService, @Autowired RoleService roleService,
      @Autowired AddressService addressService) {
    VPUser user = new VPUser();
    user.setUserName("op");
    user.setFullName("op");
    user.setPhoneNumber("12345");
    user.setUserVerified(true);
    user.setPassword("pass");
    user.setRole(roleService.get("operator"));
    user.setAddress(addressService.getOrCreate(new Address(null, "test", "test", "test")));
    userService.create(user);
  }

  public AuthenticationResponse loginAsOperator() throws Exception {
    byte[] authResponseBytes = mockMvc
        .perform(MockMvcRequestBuilders.post("/authenticate")
            .content(objectMapper.writeValueAsBytes(
                AuthenticationRequest.builder().userName("op").password("pass").build()))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse()
        .getContentAsByteArray();

    AuthenticationResponse authResponse =
        objectMapper.readValue(authResponseBytes, AuthenticationResponse.class);
    return authResponse;
  }
}
