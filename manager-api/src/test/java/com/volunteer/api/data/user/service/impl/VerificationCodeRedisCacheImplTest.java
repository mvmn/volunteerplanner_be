package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.config.CacheTestConfigurationRedis;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.service.impl.VerificationCodeRedisCacheImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Testcontainers
@ActiveProfiles("test")
@Import(CacheTestConfigurationRedis.class)
@TestPropertySource(properties = "cache.type=redis")
public class VerificationCodeRedisCacheImplTest extends AbstractVerificationCodeCacheImplTest {

  @Autowired
  private VerificationCodeRedisCacheImpl unit;

  @Test
  public void testFunctionality() {
    VPUser user = new VPUser();
    user.setId(1);
    testCacheFunctionality(user, unit);
  }
}
