package com.volunteer.api.controller;

import com.volunteer.api.data.mapping.GenericPageDtoMapper;
import com.volunteer.api.data.mapping.SubtaskDtoMapper;
import com.volunteer.api.data.model.UserAuthority;
import com.volunteer.api.data.model.api.GenericCollectionDtoV1;
import com.volunteer.api.data.model.api.GenericPageDtoV1;
import com.volunteer.api.data.model.api.SubtaskDtoV1;
import com.volunteer.api.data.model.api.search.SearchDto;
import com.volunteer.api.data.model.api.search.filter.FilterDto;
import com.volunteer.api.data.model.persistence.Subtask;
import com.volunteer.api.data.repository.search.impl.SubtaskQueryBuilder;
import com.volunteer.api.utils.AuthenticationUtils;
import com.volunteer.api.service.SubtaskService;
import java.util.Collection;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
@RequestMapping(path = "/api/v1/subtasks", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SubtaskControllerV1 {

  private final SubtaskService service;
  private final SubtaskDtoMapper mapper;

  private final ObjectFactory<SubtaskQueryBuilder> queryBuilderFactory;

  @PreAuthorize("hasAuthority('SUBTASKS_VIEW') or hasAuthority('SUBTASKS_VIEW_MINE')")
  @GetMapping("/{subtask-id}")
  @ResponseStatus(HttpStatus.OK)
  public SubtaskDtoV1 getById(@PathVariable("subtask-id") final Integer subtaskId,
      final Authentication authentication) {
    final boolean showAny = AuthenticationUtils.hasAuthority(
        UserAuthority.SUBTASKS_VIEW, authentication);

    return mapper.map(service.getById(subtaskId, !showAny));
  }

  @PreAuthorize("hasAuthority('SUBTASKS_VIEW') or hasAuthority('SUBTASKS_VIEW_MINE')")
  @GetMapping("/search")
  public GenericCollectionDtoV1<SubtaskDtoV1> search(
      @RequestParam("taskId") final Integer taskId, final Authentication authentication) {
    final boolean showAll = AuthenticationUtils.hasAuthority(
        UserAuthority.SUBTASKS_VIEW, authentication);

    final Collection<SubtaskDtoV1> result = mapper.map(service.getByTaskId(taskId,
        !showAll));

    return GenericCollectionDtoV1.<SubtaskDtoV1>builder()
        .items(result)
        .build();
  }

  @PreAuthorize("hasAuthority('SUBTASKS_VIEW') or hasAuthority('SUBTASKS_VIEW_MINE')")
  @PostMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public GenericPageDtoV1<SubtaskDtoV1> search(@RequestBody @Valid final SearchDto<FilterDto> body,
      final Authentication authentication) {
    final boolean showAny = AuthenticationUtils.hasAuthority(
        UserAuthority.SUBTASKS_VIEW, authentication);

    final Page<Subtask> result = service.getAll(queryBuilderFactory.getObject()
        .withShowOnlyMine(!showAny)
        .withPageNum(body.getPage())
        .withPageSize(body.getPageSize())
        .withFilter(body.getFilter())
        .withSort(body.getSort())
    );

    return GenericPageDtoMapper.map(body.getPage(), body.getPageSize(), result,
        mapper::map);
  }

  @PreAuthorize("hasAuthority('SUBTASKS_MODIFY')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public SubtaskDtoV1 create(@RequestBody @Valid SubtaskDtoV1 subtask) {
    return mapper.map(service.create(mapper.map(subtask)));
  }

  @PreAuthorize("hasAuthority('SUBTASKS_MODIFY') or hasAuthority('SUBTASKS_MODIFY_MINE')")
  @PutMapping("/{subtask-id}")
  @ResponseStatus(HttpStatus.OK)
  public SubtaskDtoV1 update(@PathVariable("subtask-id") final Integer subtaskId,
      @RequestBody @Valid SubtaskDtoV1 subtask, final Authentication authentication) {
    final SubtaskDtoV1 entity = subtask;
    subtask.setId(subtaskId);

    final boolean modifyAny =
        AuthenticationUtils.hasAuthority(UserAuthority.SUBTASKS_MODIFY, authentication);
    return mapper.map(service.update(mapper.map(entity), !modifyAny));
  }

  @PreAuthorize("hasAuthority('SUBTASKS_COMPLETE')")
  @PutMapping("/{subtask-id}/complete")
  @ResponseStatus(HttpStatus.OK)
  public SubtaskDtoV1 complete(@PathVariable("subtask-id") Integer subtaskId) {
    return mapper.map(service.complete(subtaskId));
  }
  
  @PreAuthorize("hasAuthority('SUBTASKS_REJECT') or hasAuthority('SUBTASKS_REJECT_MINE')")
  @PutMapping("/{subtask-id}/reject")
  @ResponseStatus(HttpStatus.OK)
  public SubtaskDtoV1 reject(@PathVariable("subtask-id") Integer subtaskId,
      final Authentication authentication) {
    final boolean rejectAny = AuthenticationUtils.hasAuthority(
        UserAuthority.SUBTASKS_REJECT, authentication);

    return mapper.map(service.reject(subtaskId, !rejectAny));
  }

}
