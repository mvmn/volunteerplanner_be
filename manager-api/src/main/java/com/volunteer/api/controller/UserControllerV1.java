package com.volunteer.api.controller;

import com.volunteer.api.data.mapping.UserV1Mapper;
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
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserControllerV1 {

  private final UserService service;
  private final UserV1Mapper userV1Mapper;

  // access for anybody
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserDtoV1 create(@Valid @RequestBody final UserDtoV1 source) {
    return userV1Mapper.map(service.create(userV1Mapper.map(source)));
  }

  @PutMapping("/password/reset")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void passwordReset(@NotBlank @RequestParam(name = "userName") final String username) {
    service.passwordReset(username);
  }

  // access for root & operator only
  @PreAuthorize("hasAuthority('root') or hasAuthority('operator')")
  @PostMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public Page<UserDtoV1> search(@RequestBody @Valid final SearchDto<FilterDto> body) {
    final Page<VPUser> result = service.getAll(new UserQueryBuilder()
        .withPageNum(body.getPage())
        .withPageSize(body.getPageSize())
        .withFilter(body.getFilter())
        .withSort(body.getSort())
    );

    return result.map(userV1Mapper::map);
  }

  @PreAuthorize("hasAuthority('root') or hasAuthority('operator')")
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 getById(@PathVariable("id") final Integer userId) {
    return userV1Mapper.map(service.get(userId));
  }

  @PreAuthorize("hasAuthority('root') or hasAuthority('operator')")
  @PutMapping("/{id}/verify")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 verifyUser(@PathVariable("id") final Integer userId) {
    return userV1Mapper.map(service.verifyUser(userId));
  }

  @PreAuthorize("hasAuthority('root') or hasAuthority('operator')")
  @PutMapping("/{id}/lock")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 lock(@PathVariable("id") final Integer userId) {
    return userV1Mapper.map(service.lock(userId));
  }

  @PreAuthorize("hasAuthority('root') or hasAuthority('operator')")
  @PutMapping("/{id}/unlock")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 unlock(@PathVariable("id") final Integer userId) {
    return userV1Mapper.map(service.unlock(userId));
  }

  // access for current user ONLY
  @GetMapping("/current")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 getCurrent() {
    return userV1Mapper.map(service.getCurrentUser());
  }

  @PutMapping("/current")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 update(@Valid @RequestBody final UserDtoV1 source) {
    return userV1Mapper.map(service.update(userV1Mapper.map(source)));
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
    return userV1Mapper.map(service.verifyPhoneNumberComplete(source.getCode()));
  }

}
