package com.volunteer.api.config;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;
import redis.embedded.core.RedisServerBuilder;

@ConditionalOnProperty(name = "cache.type", havingValue = "redis")
@TestConfiguration
public class CacheTestConfigurationRedis {

  private final RedisServer redisServer;

  public CacheTestConfigurationRedis(final RedisProperties redisProperties) throws IOException {
    this.redisServer = new RedisServerBuilder()
        .port(redisProperties.getPort())
        .setting("maxmemory 128M")
        .build();

  }

  @PostConstruct
  public void setup() throws IOException {
    redisServer.start();
  }

  @PreDestroy
  public void tearDown() throws IOException {
    redisServer.stop();
  }
}
