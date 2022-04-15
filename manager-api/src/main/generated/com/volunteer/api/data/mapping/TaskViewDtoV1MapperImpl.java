package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.CityDtoV1;
import com.volunteer.api.data.model.api.CityDtoV1.CityDtoV1Builder;
import com.volunteer.api.data.model.api.RegionDtoV1;
import com.volunteer.api.data.model.api.RegionDtoV1.RegionDtoV1Builder;
import com.volunteer.api.data.model.api.StoreDtoV1;
import com.volunteer.api.data.model.api.StoreDtoV1.StoreDtoV1Builder;
import com.volunteer.api.data.model.api.TaskViewDtoV1;
import com.volunteer.api.data.model.api.TaskViewDtoV1.TaskViewDtoV1Builder;
import com.volunteer.api.data.model.persistence.City;
import com.volunteer.api.data.model.persistence.Region;
import com.volunteer.api.data.model.persistence.Store;
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
public class TaskViewDtoV1MapperImpl extends TaskViewDtoV1Mapper {

    @Autowired
    private GenericMapper genericMapper;
    @Autowired
    private UserViewDtoV1Mapper userViewDtoV1Mapper;
    @Autowired
    private ProductDtoMapper productDtoMapper;

    @Override
    public TaskViewDtoV1 map(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskViewDtoV1Builder taskViewDtoV1 = TaskViewDtoV1.builder();

        taskViewDtoV1.id( task.getId() );
        taskViewDtoV1.volunteerStore( storeToStoreDtoV1( task.getVolunteerStore() ) );
        taskViewDtoV1.customerStore( storeToStoreDtoV1( task.getCustomerStore() ) );
        taskViewDtoV1.product( productDtoMapper.map( task.getProduct() ) );
        taskViewDtoV1.quantity( task.getQuantity() );
        taskViewDtoV1.quantityLeft( task.getQuantityLeft() );
        taskViewDtoV1.productMeasure( task.getProductMeasure() );
        taskViewDtoV1.priority( task.getPriority() );
        taskViewDtoV1.deadlineDate( genericMapper.mapZonedDateTimeToUnixtime( task.getDeadlineDate() ) );
        taskViewDtoV1.note( task.getNote() );
        taskViewDtoV1.status( task.getStatus() );
        taskViewDtoV1.subtaskCount( task.getSubtaskCount() );
        taskViewDtoV1.createdBy( userViewDtoV1Mapper.map( task.getCreatedBy() ) );
        taskViewDtoV1.createdAt( genericMapper.mapZonedDateTimeToUnixtime( task.getCreatedAt() ) );
        taskViewDtoV1.verifiedBy( userViewDtoV1Mapper.map( task.getVerifiedBy() ) );
        taskViewDtoV1.verifiedAt( genericMapper.mapZonedDateTimeToUnixtime( task.getVerifiedAt() ) );
        taskViewDtoV1.verificationComment( task.getVerificationComment() );
        taskViewDtoV1.closedBy( userViewDtoV1Mapper.map( task.getClosedBy() ) );
        taskViewDtoV1.closedAt( genericMapper.mapZonedDateTimeToUnixtime( task.getClosedAt() ) );
        taskViewDtoV1.closeComment( task.getCloseComment() );

        return taskViewDtoV1.build();
    }

    @Override
    public Collection<TaskViewDtoV1> map(Collection<Task> tasks) {
        if ( tasks == null ) {
            return null;
        }

        Collection<TaskViewDtoV1> collection = new ArrayList<TaskViewDtoV1>( tasks.size() );
        for ( Task task : tasks ) {
            collection.add( map( task ) );
        }

        return collection;
    }

    protected RegionDtoV1 regionToRegionDtoV1(Region region) {
        if ( region == null ) {
            return null;
        }

        RegionDtoV1Builder regionDtoV1 = RegionDtoV1.builder();

        regionDtoV1.id( region.getId() );
        regionDtoV1.name( region.getName() );

        return regionDtoV1.build();
    }

    protected CityDtoV1 cityToCityDtoV1(City city) {
        if ( city == null ) {
            return null;
        }

        CityDtoV1Builder cityDtoV1 = CityDtoV1.builder();

        cityDtoV1.id( city.getId() );
        cityDtoV1.region( regionToRegionDtoV1( city.getRegion() ) );
        cityDtoV1.name( city.getName() );

        return cityDtoV1.build();
    }

    protected StoreDtoV1 storeToStoreDtoV1(Store store) {
        if ( store == null ) {
            return null;
        }

        StoreDtoV1Builder storeDtoV1 = StoreDtoV1.builder();

        storeDtoV1.id( store.getId() );
        storeDtoV1.name( store.getName() );
        storeDtoV1.city( cityToCityDtoV1( store.getCity() ) );
        storeDtoV1.address( store.getAddress() );
        storeDtoV1.confidential( store.isConfidential() );
        storeDtoV1.note( store.getNote() );

        return storeDtoV1.build();
    }
}
