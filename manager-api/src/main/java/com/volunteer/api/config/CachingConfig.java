package com.volunteer.api.config;

import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
@EnableCaching
public class CachingConfig {

  @Value("${vp.verificationcodes.ttlmin:10}")
  private Integer verificationCodesCacheTtlMin = 10;

  @Bean
  public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
    return (builder) -> builder.withCacheConfiguration("verificationCodesCache",
        RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(verificationCodesCacheTtlMin)));
  }

  @Bean
  public RedisCacheConfiguration cacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ZERO)
        .disableCachingNullValues().serializeValuesWith(
            SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
  }

  @Bean
  public CacheKeyPrefix cacheKeyPrefix() {
    return new CacheKeyPrefix() {

      private final Map<String, String> CACHE_PREFIXES = Map.of("verificationCodesCache", "vcc");

      @Override
      public String compute(String cacheName) {
        return CACHE_PREFIXES.getOrDefault(cacheName, cacheName);
      }
    };
  }
}
