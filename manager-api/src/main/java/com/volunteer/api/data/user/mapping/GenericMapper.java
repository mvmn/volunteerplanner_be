package com.volunteer.api.data.user.mapping;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.volunteer.api.data.user.model.persistence.Address;
import com.volunteer.api.data.user.model.persistence.Product;
import com.volunteer.api.data.user.model.persistence.VPUser;
import com.volunteer.api.data.user.repository.AddressRepository;
import com.volunteer.api.data.user.repository.ProductRepository;
import com.volunteer.api.data.user.repository.UserRepository;

@Mapper(componentModel = "spring")
public abstract class GenericMapper {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private AddressRepository addressRepository;
  @Autowired
  private ProductRepository productRepository;

  public Long mapZonedDateTimeToUnixtime(ZonedDateTime value) {
    return value != null ? value.toEpochSecond() : null;
  }

  public ZonedDateTime mapUnixtimeToZonedDateTime(Long value) {
    return value != null ? ZonedDateTime.ofInstant(Instant.ofEpochSecond(value), ZoneOffset.UTC)
        : null;
  }

  public Address mapAddress(Integer addressId) {
    return addressId != null ? addressRepository.getById(addressId) : null;
  }

  public VPUser mapVPUser(Integer userId) {
    return userId != null ? userRepository.getById(userId) : null;
  }

  public Product mapProduct(Integer productId) {
    return productId != null ? productRepository.getById(productId) : null;
  }
}
