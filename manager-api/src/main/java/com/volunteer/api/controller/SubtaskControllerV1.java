package com.volunteer.api.controller;

import com.volunteer.api.data.mapping.GenericPageDtoMapper;
import com.volunteer.api.data.mapping.SubtaskDtoMapper;
import com.volunteer.api.data.model.UserAuthority;
import com.volunteer.api.data.model.api.GenericPageDtoV1;
import com.volunteer.api.data.model.api.SubtaskDtoV1;
import com.volunteer.api.data.model.api.search.SearchDto;
import com.volunteer.api.data.model.api.search.filter.FilterDto;
import com.volunteer.api.data.model.persistence.Subtask;
import com.volunteer.api.data.repository.search.impl.SubtaskQueryBuilder;
import com.volunteer.api.security.utils.AuthenticationUtils;
import com.volunteer.api.service.SubtaskService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/subtasks", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SubtaskControllerV1 {

  private final SubtaskService service;
  private final SubtaskDtoMapper subtaskDtoMapper;

  @PreAuthorize("hasAuthority('SUBTASKS_VIEW') or hasAuthority('SUBTASKS_VIEW_MINE')")
  @GetMapping("/{subtask-id}")
  @ResponseStatus(HttpStatus.OK)
  public SubtaskDtoV1 getById(@PathVariable("subtask-id") final Integer subtaskId,
      final Authentication authentication) {
    final boolean showAny = AuthenticationUtils.hasAuthority(
        UserAuthority.SUBTASKS_VIEW, authentication);

    return subtaskDtoMapper.map(service.getById(subtaskId, !showAny));
  }

  @PreAuthorize("hasAuthority('SUBTASKS_VIEW') or hasAuthority('SUBTASKS_VIEW_MINE')")
  @GetMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public GenericPageDtoV1<SubtaskDtoV1> search(@RequestBody @Valid final SearchDto<FilterDto> body,
      final Authentication authentication, final SubtaskQueryBuilder queryBuilder) {
    final boolean showAny = AuthenticationUtils.hasAuthority(
        UserAuthority.SUBTASKS_VIEW, authentication);

    final Page<Subtask> result = service.getAll(queryBuilder
        .withShowOnlyMine(!showAny)
        .withPageNum(body.getPage())
        .withPageSize(body.getPageSize())
        .withFilter(body.getFilter())
        .withSort(body.getSort())
    );

    return GenericPageDtoMapper.map(body.getPage(), body.getPageSize(), result,
        subtaskDtoMapper::map);
  }

  // move to task controller
//  @GetMapping("task/{taskId}")
//  public Collection<SubtaskGetDtoV1> getSubtaskByTaskId(@PathVariable("taskId") Integer taskId) {
//    return subtaskDtoMapper.mapGet(service.getByTaskId(taskId));
//  }

  @PreAuthorize("hasAuthority('SUBTASKS_MODIFY')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public SubtaskDtoV1 create(@RequestBody @Valid SubtaskDtoV1 subtask) {
    return subtaskDtoMapper.map(service.create(subtaskDtoMapper.map(subtask)));
  }

  @PreAuthorize("hasAuthority('SUBTASKS_MODIFY')")
  @PutMapping("/{subtask-id}")
  @ResponseStatus(HttpStatus.OK)
  public SubtaskDtoV1 update(@PathVariable("subtask-id") final Integer subtaskId,
      @RequestBody @Valid SubtaskDtoV1 subtask) {
    final SubtaskDtoV1 entity = subtask;
    subtask.setId(subtaskId);

    return subtaskDtoMapper.map(service.update(subtaskDtoMapper.map(entity)));
  }

  @PreAuthorize("hasAuthority('SUBTASKS_COMPLETE')")
  @PutMapping("/{subtask-id}/complete")
  @ResponseStatus(HttpStatus.OK)
  public SubtaskDtoV1 complete(@PathVariable("subtask-id") Integer subtaskId) {
    return subtaskDtoMapper.map(service.complete(subtaskId));
  }

  @PreAuthorize("hasAuthority('SUBTASKS_REJECT')")
  @PutMapping("/{subtask-id}/reject")
  @ResponseStatus(HttpStatus.OK)
  public SubtaskDtoV1 reject(@PathVariable("subtask-id") Integer subtaskId) {
    return subtaskDtoMapper.map(service.reject(subtaskId));
  }

}
