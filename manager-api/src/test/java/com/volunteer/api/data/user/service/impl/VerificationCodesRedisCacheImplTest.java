package com.volunteer.api.data.user.service.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.user.service.impl.VerificationCodesRedisCacheImplTest.EmbeddedRedisConfig;
import com.volunteer.api.service.impl.VerificationCodesRedisCacheImpl;
import redis.embedded.RedisServer;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Testcontainers
@ActiveProfiles("test")
@Import(EmbeddedRedisConfig.class)
@TestPropertySource(properties = "vp.cachetype=redis")
public class VerificationCodesRedisCacheImplTest extends AbstractVerificationCodesCacheImplTest {

  @Autowired
  private VerificationCodesRedisCacheImpl unit;

  @TestConfiguration
  public static class EmbeddedRedisConfig {
    @Bean
    public RedisServer redisServer() {
      return RedisServer.builder().port(6379).setting("maxmemory 128M").build();
    }
  }

  @BeforeAll
  public static void init(@Autowired RedisServer redisServer) {
    redisServer.start();
  }

  @AfterAll
  public static void destroy(@Autowired RedisServer redisServer) {
    redisServer.stop();
  }

  @Test
  public void testFunctionality() {
    VPUser user = new VPUser();
    user.setId(1);
    testCacheFunctionality(user, unit);
  }
}
