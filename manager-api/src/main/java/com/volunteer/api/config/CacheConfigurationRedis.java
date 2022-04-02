package com.volunteer.api.config;

import com.volunteer.api.service.VerificationCodeCache;
import com.volunteer.api.service.impl.VerificationCodesRedisCacheImpl;
import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@ConditionalOnProperty(name = "cache.type", havingValue = "redis")
@Configuration
@EnableCaching
public class CacheConfigurationRedis {

  public static final String VERIFICATION_CODES_CACHE_NAME = "verificationCodesCache";

  @Value("${cache.verification-code-ttl:10m}")
  private Duration verificationCodeTtl;

  @Bean
  public VerificationCodeCache verificationCodeCache(final CacheManager cacheManager) {
    return new VerificationCodesRedisCacheImpl(
        cacheManager.getCache(VERIFICATION_CODES_CACHE_NAME)
    );
  }

  @Bean
  public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(
      CacheKeyPrefix keyPrefix) {
    return (builder) -> builder.withCacheConfiguration(VERIFICATION_CODES_CACHE_NAME,
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
            .computePrefixWith(keyPrefix)
            .entryTtl(verificationCodeTtl));
  }

  @Bean
  public RedisCacheConfiguration cacheConfiguration(
      CacheKeyPrefix keyPrefix) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ZERO)
        .computePrefixWith(keyPrefix).disableCachingNullValues().serializeValuesWith(
            SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
  }

  @Bean
  public CacheKeyPrefix cacheKeyPrefix() {
    return new CacheKeyPrefix() {

      private final Map<String, String> CACHE_PREFIXES =
          Map.of(VERIFICATION_CODES_CACHE_NAME, "vcc");

      @Override
      public String compute(String cacheName) {
        return CACHE_PREFIXES.getOrDefault(cacheName, cacheName) + "::";
      }
    };
  }
}
