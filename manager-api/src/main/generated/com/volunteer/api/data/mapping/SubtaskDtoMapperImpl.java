package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.SubtaskDtoV1;
import com.volunteer.api.data.model.api.SubtaskDtoV1.SubtaskDtoV1Builder;
import com.volunteer.api.data.model.persistence.Subtask;
import com.volunteer.api.data.model.persistence.Subtask.SubtaskBuilder;
import com.volunteer.api.data.model.persistence.Task;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-15T17:27:08+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.1 (Oracle Corporation)"
)
@Component
public class SubtaskDtoMapperImpl implements SubtaskDtoMapper {

    @Autowired
    private GenericMapper genericMapper;
    @Autowired
    private UserViewDtoV1Mapper userViewDtoV1Mapper;

    @Override
    public SubtaskDtoV1 map(Subtask subtask) {
        if ( subtask == null ) {
            return null;
        }

        SubtaskDtoV1Builder subtaskDtoV1 = SubtaskDtoV1.builder();

        subtaskDtoV1.id( subtask.getId() );
        subtaskDtoV1.taskId( subtask.getTaskId() );
        subtaskDtoV1.quantity( subtask.getQuantity() );
        subtaskDtoV1.status( subtask.getStatus() );
        subtaskDtoV1.note( subtask.getNote() );
        subtaskDtoV1.transportRequired( subtask.isTransportRequired() );
        subtaskDtoV1.createdBy( userViewDtoV1Mapper.map( subtask.getCreatedBy() ) );
        subtaskDtoV1.createdAt( genericMapper.mapZonedDateTimeToUnixtime( subtask.getCreatedAt() ) );
        subtaskDtoV1.closedBy( userViewDtoV1Mapper.map( subtask.getClosedBy() ) );
        subtaskDtoV1.closedAt( genericMapper.mapZonedDateTimeToUnixtime( subtask.getClosedAt() ) );

        return subtaskDtoV1.build();
    }

    @Override
    public Subtask map(SubtaskDtoV1 dto) {
        if ( dto == null ) {
            return null;
        }

        SubtaskBuilder subtask = Subtask.builder();

        subtask.task( subtaskDtoV1ToTask( dto ) );
        subtask.id( dto.getId() );
        subtask.taskId( dto.getTaskId() );
        subtask.quantity( dto.getQuantity() );
        subtask.status( dto.getStatus() );
        subtask.note( dto.getNote() );
        if ( dto.getTransportRequired() != null ) {
            subtask.transportRequired( dto.getTransportRequired() );
        }
        subtask.createdAt( genericMapper.mapUnixtimeToZonedDateTime( dto.getCreatedAt() ) );
        subtask.closedAt( genericMapper.mapUnixtimeToZonedDateTime( dto.getClosedAt() ) );

        return subtask.build();
    }

    @Override
    public Collection<SubtaskDtoV1> map(Collection<Subtask> subtasks) {
        if ( subtasks == null ) {
            return null;
        }

        Collection<SubtaskDtoV1> collection = new ArrayList<SubtaskDtoV1>( subtasks.size() );
        for ( Subtask subtask : subtasks ) {
            collection.add( map( subtask ) );
        }

        return collection;
    }

    protected Task subtaskDtoV1ToTask(SubtaskDtoV1 subtaskDtoV1) {
        if ( subtaskDtoV1 == null ) {
            return null;
        }

        Task task = new Task();

        task.setId( subtaskDtoV1.getTaskId() );

        return task;
    }
}
