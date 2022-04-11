package com.volunteer.api.controller;

import com.volunteer.api.data.mapping.GenericPageDtoMapper;
import com.volunteer.api.data.mapping.UserDtoV1Mapper;
import com.volunteer.api.data.model.api.GenericPageDtoV1;
import com.volunteer.api.data.model.api.PasswordChangeDtoV1;
import com.volunteer.api.data.model.api.PhoneVerificationDtoV1;
import com.volunteer.api.data.model.api.UserDtoV1;
import com.volunteer.api.data.model.api.search.SearchDto;
import com.volunteer.api.data.model.api.search.filter.FilterDto;
import com.volunteer.api.data.model.persistence.VPUser;
import com.volunteer.api.data.repository.search.impl.UserQueryBuilder;
import com.volunteer.api.service.UserService;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(path = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserControllerV1 {

  private final UserService service;
  private final UserDtoV1Mapper userMapper;

  private final ObjectFactory<UserQueryBuilder> queryBuilderFactory;

  // access for anybody
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserDtoV1 create(@Valid @RequestBody final UserDtoV1 source) {
    return userMapper.map(service.create(userMapper.map(source)));
  }

  @PutMapping("/password/reset")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void passwordReset(@NotBlank @RequestParam(name = "userName") final String username) {
    service.passwordReset(username);
  }

  @PreAuthorize("hasAuthority('USERS_VIEW')")
  @PostMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public GenericPageDtoV1<UserDtoV1> search(@RequestBody @Valid final SearchDto<FilterDto> body) {
    final Page<VPUser> result = service.getAll(queryBuilderFactory.getObject()
        .withPageNum(body.getPage())
        .withPageSize(body.getPageSize())
        .withFilter(body.getFilter())
        .withSort(body.getSort())
    );

    return GenericPageDtoMapper.map(body.getPage(), body.getPageSize(), result, userMapper::map);
  }

  @PreAuthorize("hasAuthority('USERS_VIEW')")
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 getById(@PathVariable("id") final Integer userId) {
    return userMapper.map(service.get(userId));
  }

  @PreAuthorize("hasAuthority('USERS_RATING_RESET')")
  @PutMapping("/{id}/rating/reset")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 ratingReset(@PathVariable("id") final Integer userId) {
    return userMapper.map(service.ratingReset(userId));
  }

  @PreAuthorize("hasAuthority('USERS_VERIFY')")
  @PutMapping("/{id}/verify/phone")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 verifyPhone(@PathVariable("id") final Integer userId) {
    return userMapper.map(service.verifyPhoneNumber(userId));
  }

  @PreAuthorize("hasAuthority('USERS_VERIFY')")
  @PutMapping("/{id}/verify/user")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 verifyUser(@PathVariable("id") final Integer userId) {
    return userMapper.map(service.verifyUser(userId));
  }

  @PreAuthorize("hasAuthority('USERS_LOCK')")
  @PutMapping("/{id}/lock")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 lock(@PathVariable("id") final Integer userId) {
    return userMapper.map(service.lock(userId));
  }

  @PreAuthorize("hasAuthority('USERS_LOCK')")
  @PutMapping("/{id}/unlock")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 unlock(@PathVariable("id") final Integer userId) {
    return userMapper.map(service.unlock(userId));
  }

  // access for current user ONLY
  @GetMapping("/current")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 getCurrent() {
    return userMapper.map(service.getCurrentUser());
  }

  @PutMapping("/current")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 update(@Valid @RequestBody final UserDtoV1 source) {
    return userMapper.map(service.update(userMapper.map(source)));
  }

  @PutMapping("/current/password/change")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void passwordChange(@Valid @RequestBody final PasswordChangeDtoV1 source) {
    service.passwordChange(source.getOldPassword(), source.getNewPassword());
  }

  @GetMapping("/current/sms")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void verifyPhoneNumberStart() {
    service.verifyPhoneNumberStart();
  }

  @PostMapping("/current/sms")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 verifyPhoneNumberComplete(
      @Valid @RequestBody final PhoneVerificationDtoV1 source) {
    return userMapper.map(service.verifyPhoneNumberComplete(source.getCode()));
  }

}
