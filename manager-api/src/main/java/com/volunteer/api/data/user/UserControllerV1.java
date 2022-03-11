package com.volunteer.api.data.user;

import com.volunteer.api.data.user.mapping.UserV1Mapper;
import com.volunteer.api.data.user.model.api.UserV1;
import com.volunteer.api.data.user.model.dto.UserDto;
import com.volunteer.api.data.user.service.UserService;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserControllerV1 {

  private final UserService service;
  private final UserV1Mapper userV1Mapper;

  @PreAuthorize("hasAuthority('root')")
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Collection<UserV1> getAll() {
    return userV1Mapper.map(service.getAll());
  }

  @GetMapping(path = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserV1 getById(@PathVariable("id") final Integer userId) {
    return userV1Mapper.map(service.get(userId));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserV1 create(@RequestBody final UserV1 source) {
    return userV1Mapper.map(service.create(userV1Mapper.map(source)));
  }

  @PutMapping(path = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserV1 update(@PathVariable("id") final Integer userId, @RequestBody final UserV1 source) {
    final UserDto userDto = userV1Mapper.map(source);
    source.setId(userId);

    return userV1Mapper.map(service.create(userDto));
  }

}
