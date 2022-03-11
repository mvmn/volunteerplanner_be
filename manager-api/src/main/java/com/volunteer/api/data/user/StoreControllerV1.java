package com.volunteer.api.data.user;

import com.volunteer.api.data.user.mapping.StoreDtoMapper;
import com.volunteer.api.data.user.model.api.StoreDtoV1;
import com.volunteer.api.data.user.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

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
  public ResponseEntity<StoreDtoV1> getById(@PathVariable("id") final Integer id) {
    return storeService.getById(id)
        .map(s -> ResponseEntity.ok(storeDtoMapper.map(s)))
        .orElse(ResponseEntity.notFound().build());
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

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public StoreDtoV1 update(@RequestBody final StoreDtoV1 store) {
    return storeDtoMapper.map(storeService.update(storeDtoMapper.map(store)));
  }
}
