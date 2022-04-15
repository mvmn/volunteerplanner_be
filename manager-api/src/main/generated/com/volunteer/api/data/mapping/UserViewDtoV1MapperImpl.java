package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.UserDtoV1;
import com.volunteer.api.data.model.persistence.VPUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-15T17:27:08+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.1 (Oracle Corporation)"
)
@Component
public class UserViewDtoV1MapperImpl implements UserViewDtoV1Mapper {

    @Override
    public UserDtoV1 map(VPUser source) {
        if ( source == null ) {
            return null;
        }

        UserDtoV1 userDtoV1 = new UserDtoV1();

        userDtoV1.setId( source.getId() );
        userDtoV1.setDisplayName( source.getDisplayName() );
        userDtoV1.setOrganization( source.getOrganization() );
        userDtoV1.setRating( source.getRating() );

        return userDtoV1;
    }
}
