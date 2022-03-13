package com.volunteer.api.data;

import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.volunteer.api.data.mapping.StoreDtoMapper;
import com.volunteer.api.data.model.api.StoreDtoV1;
import com.volunteer.api.data.model.persistence.Store;
import com.volunteer.api.data.service.StoreService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/stores", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class StoreControllerV1 {
  private final StoreService storeService;
  private final StoreDtoMapper storeDtoMapper;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Collection<StoreDtoV1> getAll() {
    return storeDtoMapper.map(storeService.getAll());
  }

  @GetMapping(path = "/{id}")
  public StoreDtoV1 getById(@PathVariable("id") final Integer id) {
    return storeDtoMapper.map(storeService.getById(id));
  }

  @GetMapping(path = "/search/{name}")
  @ResponseStatus(HttpStatus.OK)
  public Collection<StoreDtoV1> searchByName(@PathVariable("name") final String name) {
    return storeDtoMapper.map(storeService.getByName(name));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public StoreDtoV1 create(@RequestBody final StoreDtoV1 store) {
    return storeDtoMapper.map(storeService.create(storeDtoMapper.map(store)));
  }

  @PutMapping(path = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public StoreDtoV1 update(@PathVariable("id") final Integer id,
                           @RequestBody final StoreDtoV1 store) {
    Store entity = storeDtoMapper.map(store);
    entity.setId(id);
    return storeDtoMapper.map(storeService.update(entity));
  }
}
