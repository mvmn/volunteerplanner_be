package com.volunteer.api.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

@ConditionalOnProperty(name = "cache.type", havingValue = "redis")
@TestConfiguration
public class CacheTestConfigurationRedis {

  private final RedisServer redisServer;

  public CacheTestConfigurationRedis(final RedisProperties redisProperties) {
    this.redisServer = RedisServer.builder()
        .port(redisProperties.getPort())
        .setting("maxmemory 128M")
        .build();

  }

  @PostConstruct
  public void setup() {
    redisServer.start();
  }

  @PreDestroy
  public void tearDown() {
    redisServer.stop();
  }

}
