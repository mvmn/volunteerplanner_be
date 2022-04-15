package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.CityDtoV1;
import com.volunteer.api.data.model.api.CityDtoV1.CityDtoV1Builder;
import com.volunteer.api.data.model.api.RegionDtoV1;
import com.volunteer.api.data.model.api.RegionDtoV1.RegionDtoV1Builder;
import com.volunteer.api.data.model.api.StoreDtoV1;
import com.volunteer.api.data.model.api.StoreDtoV1.StoreDtoV1Builder;
import com.volunteer.api.data.model.persistence.City;
import com.volunteer.api.data.model.persistence.City.CityBuilder;
import com.volunteer.api.data.model.persistence.Region;
import com.volunteer.api.data.model.persistence.Region.RegionBuilder;
import com.volunteer.api.data.model.persistence.Store;
import com.volunteer.api.data.model.persistence.Store.StoreBuilder;
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
public class StoreDtoMapperImpl implements StoreDtoMapper {

    @Override
    public Collection<StoreDtoV1> map(Collection<Store> stores) {
        if ( stores == null ) {
            return null;
        }

        Collection<StoreDtoV1> collection = new ArrayList<StoreDtoV1>( stores.size() );
        for ( Store store : stores ) {
            collection.add( map( store ) );
        }

        return collection;
    }

    @Override
    public StoreDtoV1 map(Store store) {
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

    @Override
    public Store map(StoreDtoV1 storeDto) {
        if ( storeDto == null ) {
            return null;
        }

        StoreBuilder store = Store.builder();

        store.id( storeDto.getId() );
        store.name( storeDto.getName() );
        store.city( cityDtoV1ToCity( storeDto.getCity() ) );
        store.address( storeDto.getAddress() );
        if ( storeDto.getConfidential() != null ) {
            store.confidential( storeDto.getConfidential() );
        }
        store.note( storeDto.getNote() );

        return store.build();
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

    protected Region regionDtoV1ToRegion(RegionDtoV1 regionDtoV1) {
        if ( regionDtoV1 == null ) {
            return null;
        }

        RegionBuilder region = Region.builder();

        region.id( regionDtoV1.getId() );
        region.name( regionDtoV1.getName() );

        return region.build();
    }

    protected City cityDtoV1ToCity(CityDtoV1 cityDtoV1) {
        if ( cityDtoV1 == null ) {
            return null;
        }

        CityBuilder city = City.builder();

        city.id( cityDtoV1.getId() );
        city.region( regionDtoV1ToRegion( cityDtoV1.getRegion() ) );
        city.name( cityDtoV1.getName() );

        return city.build();
    }
}
