package com.volunteer.api.data.user;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.volunteer.api.data.user.mapping.UserV1Mapper;
import com.volunteer.api.data.user.model.api.UserDtoV1;
import com.volunteer.api.data.user.model.persistence.VPUser;
import com.volunteer.api.data.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserControllerV1 {

  private final UserService service;
  private final UserV1Mapper userV1Mapper;

  @PreAuthorize("hasAuthority('root')")
  @GetMapping
  public ResponseEntity<Collection<UserDtoV1>> getAll() {
    final Collection<UserDtoV1> result = userV1Mapper.map(service.getAll());
    return ResponseEntity.ok(result);
  }

  @GetMapping(path = "/{user-id}")
  public ResponseEntity<UserDtoV1> getById(@PathVariable("user-id") final Integer userId) {
    final UserDtoV1 result = userV1Mapper.map(service.get(userId)
        .orElseThrow(() -> new IllegalArgumentException(String.format(
            "User with id '%d' does not exist", userId))));

    return ResponseEntity.ok(result);
  }

  @PostMapping
  public ResponseEntity<UserDtoV1> create(@RequestBody final UserDtoV1 source) {
    final UserDtoV1 result = userV1Mapper.map(service.create(userV1Mapper.map(source)));
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @PutMapping(path = "/{user-id}")
  public ResponseEntity<UserDtoV1> update(@PathVariable("user-id") final Integer userId,
      @RequestBody final UserDtoV1 source) {
    final VPUser userDto = userV1Mapper.map(source);
    source.setId(userId);

    final UserDtoV1 result = userV1Mapper.map(service.create(userDto));
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

}
