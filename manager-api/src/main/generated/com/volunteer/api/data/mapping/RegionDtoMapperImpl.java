package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.RegionDtoV1;
import com.volunteer.api.data.model.api.RegionDtoV1.RegionDtoV1Builder;
import com.volunteer.api.data.model.persistence.Region;
import com.volunteer.api.data.model.persistence.Region.RegionBuilder;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-15T17:27:08+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.1 (Oracle Corporation)"
)
@Component
public class RegionDtoMapperImpl implements RegionDtoMapper {

    @Override
    public RegionDtoV1 map(Region region) {
        if ( region == null ) {
            return null;
        }

        RegionDtoV1Builder regionDtoV1 = RegionDtoV1.builder();

        regionDtoV1.id( region.getId() );
        regionDtoV1.name( region.getName() );

        return regionDtoV1.build();
    }

    @Override
    public Region map(RegionDtoV1 regionDto) {
        if ( regionDto == null ) {
            return null;
        }

        RegionBuilder region = Region.builder();

        region.id( regionDto.getId() );
        region.name( regionDto.getName() );

        return region.build();
    }

    @Override
    public Collection<RegionDtoV1> map(Collection<Region> regions) {
        if ( regions == null ) {
            return null;
        }

        Collection<RegionDtoV1> collection = new ArrayList<RegionDtoV1>( regions.size() );
        for ( Region region : regions ) {
            collection.add( map( region ) );
        }

        return collection;
    }
}
