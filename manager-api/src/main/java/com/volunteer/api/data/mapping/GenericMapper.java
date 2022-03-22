package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.persistence.Product;
import com.volunteer.api.data.model.persistence.Store;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.service.ProductService;
import com.volunteer.api.service.StoreService;
import com.volunteer.api.service.UserService;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class GenericMapper {

  @Autowired
  private UserService userService;
  @Autowired
  private ProductService productService;
  @Autowired
  private StoreService storeService;

  public Long mapZonedDateTimeToUnixtime(ZonedDateTime value) {
    return Objects.isNull(value) ? null : value.toEpochSecond();
  }

  public ZonedDateTime mapUnixtimeToZonedDateTime(Long value) {
    return Objects.isNull(value) ? null
        : ZonedDateTime.ofInstant(Instant.ofEpochSecond(value), ZoneOffset.UTC);
  }

  public VPUser mapVPUser(Integer userId) {
    return Objects.isNull(userId) ? null : userService.get(userId);
  }

  public Product mapProduct(Integer productId) {
    return Objects.isNull(productId) ? null : productService.get(productId);
  }

  public Store mapStore(Integer storeId) {
    return Objects.isNull(storeId) ? null : storeService.get(storeId, true);
  }

}
