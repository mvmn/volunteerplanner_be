package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.TaskDtoV1;
import com.volunteer.api.data.model.persistence.Task;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-15T17:27:08+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.1 (Oracle Corporation)"
)
@Component
public class TaskDtoV1MapperImpl implements TaskDtoV1Mapper {

    @Autowired
    private GenericMapper genericMapper;

    @Override
    public Task map(TaskDtoV1 dto) {
        if ( dto == null ) {
            return null;
        }

        Task task = new Task();

        task.setVolunteerStore( genericMapper.mapStore( dto.getVolunteerStoreId() ) );
        task.setCustomerStore( genericMapper.mapStore( dto.getCustomerStoreId() ) );
        task.setProduct( genericMapper.mapProduct( dto.getProductId() ) );
        task.setQuantity( dto.getQuantity() );
        task.setProductMeasure( dto.getProductMeasure() );
        task.setPriority( dto.getPriority() );
        task.setDeadlineDate( genericMapper.mapUnixtimeToZonedDateTime( dto.getDeadlineDate() ) );
        task.setNote( dto.getNote() );
        task.setStatus( dto.getStatus() );

        return task;
    }
}
