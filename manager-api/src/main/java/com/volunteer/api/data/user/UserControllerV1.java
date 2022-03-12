package com.volunteer.api.data.user;

import com.volunteer.api.data.user.mapping.UserV1Mapper;
import com.volunteer.api.data.user.model.api.PasswordChangeDtoV1;
import com.volunteer.api.data.user.model.api.PhoneVerificationDtoV1;
import com.volunteer.api.data.user.model.api.UserDtoV1;
import com.volunteer.api.data.user.service.UserService;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
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

  @PreAuthorize("hasAuthority('root') or hasAuthority('operator')")
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Collection<UserDtoV1> getAll() {
    return userV1Mapper.map(service.getAll());
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserDtoV1 getById(@PathVariable("id") final Integer userId) {
    return userV1Mapper.map(service.get(userId));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  // permits access for not authorized users
  public UserDtoV1 create(@Valid @RequestBody final UserDtoV1 source) {
    return userV1Mapper.map(service.create(userV1Mapper.map(source)));
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  // permits access for authorized users. profile update is allowed by oneself ONLY
  public UserDtoV1 update(@Valid @RequestBody final UserDtoV1 source) {
    return userV1Mapper.map(service.update(userV1Mapper.map(source)));
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

  @PutMapping("/password/change")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  // for logged in user only
  public void passwordChange(@Valid @RequestBody final PasswordChangeDtoV1 source) {
    service.passwordChange(source.getOldPassword(), source.getNewPassword());
  }

  @PutMapping("/password/reset")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void passwordReset(@NotBlank @RequestParam(name = "userName") final String username) {
    service.passwordReset(username);
  }

  @GetMapping("/sms")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  // for logged in user only
  public void verifyPhoneNumberStart() {
    service.verifyPhoneNumberStart();
  }

  @PostMapping("/sms")
  @ResponseStatus(HttpStatus.OK)
  // for logged in user only
  public UserDtoV1 verifyPhoneNumberComplete(
      @Valid @RequestBody final PhoneVerificationDtoV1 source) {
    return userV1Mapper.map(service.verifyPhoneNumberComplete(source.getCode()));
  }

}
