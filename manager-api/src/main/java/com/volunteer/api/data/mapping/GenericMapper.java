package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.persistence.Product;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.repository.ProductRepository;
import com.volunteer.api.data.repository.UserRepository;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class GenericMapper {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ProductRepository productRepository;

  public Long mapZonedDateTimeToUnixtime(ZonedDateTime value) {
    return Objects.isNull(value) ? null : value.toEpochSecond();
  }

  public ZonedDateTime mapUnixtimeToZonedDateTime(Long value) {
    return Objects.isNull(value) ? null
        : ZonedDateTime.ofInstant(Instant.ofEpochSecond(value), ZoneOffset.UTC);
  }

  public VPUser mapVPUser(Integer userId) {
    return Objects.isNull(userId) ? null : userRepository.getById(userId);
  }

  public Product mapProduct(Integer productId) {
    return Objects.isNull(productId) ? null : productRepository.getById(productId);
  }

}
