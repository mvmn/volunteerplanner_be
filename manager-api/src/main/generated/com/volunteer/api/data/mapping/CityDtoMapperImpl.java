package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.CityDtoV1;
import com.volunteer.api.data.model.api.CityDtoV1.CityDtoV1Builder;
import com.volunteer.api.data.model.api.RegionDtoV1;
import com.volunteer.api.data.model.api.RegionDtoV1.RegionDtoV1Builder;
import com.volunteer.api.data.model.persistence.City;
import com.volunteer.api.data.model.persistence.City.CityBuilder;
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
public class CityDtoMapperImpl implements CityDtoMapper {

    @Override
    public CityDtoV1 map(City city) {
        if ( city == null ) {
            return null;
        }

        CityDtoV1Builder cityDtoV1 = CityDtoV1.builder();

        cityDtoV1.id( city.getId() );
        cityDtoV1.region( regionToRegionDtoV1( city.getRegion() ) );
        cityDtoV1.name( city.getName() );

        return cityDtoV1.build();
    }

    @Override
    public City map(CityDtoV1 cityDto) {
        if ( cityDto == null ) {
            return null;
        }

        CityBuilder city = City.builder();

        city.id( cityDto.getId() );
        city.region( regionDtoV1ToRegion( cityDto.getRegion() ) );
        city.name( cityDto.getName() );

        return city.build();
    }

    @Override
    public Collection<CityDtoV1> map(Collection<City> cities) {
        if ( cities == null ) {
            return null;
        }

        Collection<CityDtoV1> collection = new ArrayList<CityDtoV1>( cities.size() );
        for ( City city : cities ) {
            collection.add( map( city ) );
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

    protected Region regionDtoV1ToRegion(RegionDtoV1 regionDtoV1) {
        if ( regionDtoV1 == null ) {
            return null;
        }

        RegionBuilder region = Region.builder();

        region.id( regionDtoV1.getId() );
        region.name( regionDtoV1.getName() );

        return region.build();
    }
}
