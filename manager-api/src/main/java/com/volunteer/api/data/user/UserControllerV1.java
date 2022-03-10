package com.volunteer.api.data.user;

import com.volunteer.api.data.user.mapping.UserV1Mapper;
import com.volunteer.api.data.user.model.api.UserV1;
import com.volunteer.api.data.user.model.dto.UserDto;
import com.volunteer.api.data.user.service.UserService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserControllerV1 {

  private final UserService service;

  public UserControllerV1(@Autowired final UserService service) {
    this.service = service;
  }

  @PreAuthorize("hasAuthority('root')")
  @GetMapping
  public ResponseEntity<Collection<UserV1>> getAll() {
    final Collection<UserV1> result = UserV1Mapper.map(service.getAll());
    return ResponseEntity.ok(result);
  }

  @GetMapping(path = "/{user-id}")
  public ResponseEntity<UserV1> getById(@PathVariable("user-id") final Integer userId) {
    final UserV1 result = UserV1Mapper.map(service.get(userId)
        .orElseThrow(() -> new IllegalArgumentException(String.format(
            "User with id '%d' does not exist", userId))));

    return ResponseEntity.ok(result);
  }

  @PostMapping
  public ResponseEntity<UserV1> create(@RequestBody final UserV1 source) {
    final UserV1 result = UserV1Mapper.map(service.create(UserV1Mapper.map(source)));
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @PutMapping(path = "/{user-id}")
  public ResponseEntity<UserV1> update(@PathVariable("user-id") final Integer userId,
      @RequestBody final UserV1 source) {
    final UserDto userDto = UserV1Mapper.map(source);
    source.setId(userId);

    final UserV1 result = UserV1Mapper.map(service.create(userDto));
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

}
