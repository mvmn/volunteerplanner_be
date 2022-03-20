package com.volunteer.api.data.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.volunteer.api.AbstractTestWithPersistence;
import com.volunteer.api.data.model.persistence.Store;
import com.volunteer.api.service.AddressService;
import com.volunteer.api.service.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@ActiveProfiles("test")
public class StoreServiceImpTest extends AbstractTestWithPersistence {

  @Autowired
  private StoreService storeService;
  @Autowired
  private AddressService addressService;

  @Test
  public void crudOperationsTest() {
    Store store =
        Store.builder()
            .name("st name")
            .city(addressService.getCityById(12))
            .address("address")
            .note("st note")
            .build();

    Store created = storeService.create(store);
    assertThat(created.getId()).isGreaterThan(0);
    assertProperties(store, created);

    // Update
    created.setName(created.getName() + " u1");
    created.setNote(created.getNote() + " u1");
    created.setAddress(created.getAddress() + " u1");

    Store updated = storeService.update(created);
    assertProperties(store, updated);

    Store byId = storeService.getById(updated.getId(), true);
    assertProperties(updated, byId);
  }

  private void assertProperties(Store expected, Store actual) {
    assertThat(actual.getName()).isEqualTo(expected.getName());
    assertThat(actual.getNote()).isEqualTo(expected.getNote());
    assertThat(actual.getAddress()).isEqualTo(expected.getAddress());
  }
}
