package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.repository.RoleRepository;
import com.volunteer.api.service.UserService;
import com.volunteer.api.service.impl.VerificationCodeDbCacheImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Testcontainers
@ActiveProfiles("test")
@TestPropertySource(properties = "cache.type=db")
public class VerificationCodeDbCacheImplTest extends AbstractVerificationCodeCacheImplTest {

  @Autowired
  private VerificationCodeDbCacheImpl unit;

  @Autowired
  private UserService userService;

  @Autowired
  private RoleRepository roleRepo;

  @Test
  public void testFunctionality() {
    final VPUser user = prepareUser("123456789");
    testCacheFunctionality(user, unit);
  }

  private VPUser prepareUser(final String phoneNumber) {
    final Optional<VPUser> current = userService.getByPrincipal(phoneNumber);
    if (current.isPresent()) {
      return current.get();
    }

    final VPUser result = new VPUser();
    result.setPhoneNumber(phoneNumber);
    result.setPassword("pass");
    result.setRole(roleRepo.findByName("operator"));
    result.setDisplayName("u-" + phoneNumber);

    return userService.create(result);
  }

}
