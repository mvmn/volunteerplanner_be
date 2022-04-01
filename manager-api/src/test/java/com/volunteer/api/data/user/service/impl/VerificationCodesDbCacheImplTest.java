package com.volunteer.api.data.user.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.repository.RoleRepository;
import com.volunteer.api.data.repository.UserRepository;
import com.volunteer.api.service.impl.VerificationCodesDbCacheImpl;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Testcontainers
@ActiveProfiles("test")
@TestPropertySource(properties = "vp.cachetype=db")
public class VerificationCodesDbCacheImplTest extends AbstractVerificationCodesCacheImplTest {

  @Autowired
  private VerificationCodesDbCacheImpl unit;

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private RoleRepository roleRepo;

  @Test
  public void testFunctionality() {
    VPUser user = new VPUser();
    user.setUserName("test");
    user.setFullName("test");
    user.setPhoneNumber("12345");
    user.setUserVerified(true);
    user.setPhoneNumberVerified(true);
    user.setPassword("pass");
    user.setRole(roleRepo.findByName("operator"));

    user = userRepo.save(user);

    testCacheFunctionality(user, unit);
  }
}
