package com.volunteer.api.data.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.volunteer.api.data.user.model.persistence.Address;
import com.volunteer.api.data.user.model.persistence.Store;
import com.volunteer.api.data.user.service.AddressService;
import com.volunteer.api.data.user.service.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
public class StoreServiceImpTest {

  @Autowired private StoreService storeService;
  @Autowired private AddressService addressService;

  @Test
  public void crudOperationsTest() {
    // Create
    Address address = Address.builder().city("c1").region("r1").address("a1").build();
    address = addressService.save(address);

    Store store =
        Store.builder()
            .name("st name")
            .address(address)
            .note("st note")
            .contactPerson("c pers")
            .build();

    Store created = storeService.create(store);
    assertThat(created.getId()).isGreaterThan(0);
    assertProperties(store, created);

    // Update
    created.setName(created.getName() + " u1");
    created.setNote(created.getNote() + " u1");
    created.setContactPerson(created.getContactPerson() + " u1");
    created.setAddress(Address.builder().id(address.getId()).build());

    Store updated = storeService.update(created);
    assertProperties(store, updated);

    Store byId = storeService.getById(updated.getId());
    assertProperties(updated, byId);

    Collection<Store> byName = storeService.getByName(updated.getName());
    assertThat(byName.size()).isEqualTo(1);
    assertProperties(updated, byName.iterator().next());

    byName = storeService.getByName(updated.getName() + "absent");
    assertTrue(byName.isEmpty());

    Collection<Store> all = storeService.getAll();
    assertThat(all.size()).isEqualTo(1);
    assertProperties(updated, all.iterator().next());
  }

  private void assertProperties(Store expected, Store actual) {
    assertThat(actual.getName()).isEqualTo(expected.getName());
    assertThat(actual.getNote()).isEqualTo(expected.getNote());
    assertThat(actual.getContactPerson()).isEqualTo(expected.getContactPerson());
    assertThat(actual.getAddress().getId()).isEqualTo(expected.getAddress().getId());
  }
}
