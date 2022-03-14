package com.volunteer.api.controller;

import com.volunteer.api.data.mapping.SubtaskDtoMapper;
import com.volunteer.api.data.model.api.SubtaskDtoV1;
import com.volunteer.api.data.model.api.SubtaskGetDtoV1;
import com.volunteer.api.service.SubtaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/subtasks", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SubtaskControllerV1 {
    private final SubtaskService subtaskService;
    private final SubtaskDtoMapper subtaskDtoMapper;

    @GetMapping("{subtaskId}")
    public SubtaskGetDtoV1 getSubtaskById(@PathVariable("subtaskId") Integer subtaskId) {
        return subtaskDtoMapper.mapGet(subtaskService.findBySubtaskId(subtaskId));
    }

    @GetMapping("task/{taskId}")
    public Collection<SubtaskGetDtoV1> getSubtaskByTaskId(@PathVariable("taskId") Integer taskId) {
        return subtaskDtoMapper.mapGet(subtaskService.findByTaskId(taskId));
    }

    @GetMapping("product/{productId}")
    public Collection<SubtaskGetDtoV1> getSubtaskByProductId(@PathVariable("productId") Integer productId) {
        return subtaskDtoMapper.mapGet(subtaskService.findByProductId(productId));
    }

    @GetMapping("volunteer/{volunteerId}")
    public Collection<SubtaskGetDtoV1> getSubtaskByVolunteerId(@PathVariable("volunteerId") Integer volunteerId) {
        return subtaskDtoMapper.mapGet(subtaskService.findByVolunteerId(volunteerId));
    }

    @GetMapping("volunteer/me")
    public Collection<SubtaskGetDtoV1> getMySubtask(Authentication authentication) {
        return subtaskDtoMapper.mapGet(subtaskService.findByVolunteerPrincipal(authentication.getName()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubtaskDtoV1 create(@RequestBody @Valid SubtaskDtoV1 subtask) {
        return subtaskDtoMapper.map(subtaskService.createSubtask(subtaskDtoMapper.map(subtask)));
    }

    @PostMapping("{subtaskId}/complete")
    @ResponseStatus(HttpStatus.OK)
    public void complete(@PathVariable("subtaskId") Integer subtaskId) {
        subtaskService.complete(subtaskId);
    }

    @PostMapping("{subtaskId}/reject")
    @ResponseStatus(HttpStatus.OK)
    public void reject(@PathVariable("subtaskId") Integer subtaskId) {
        subtaskService.reject(subtaskId);
    }
}
