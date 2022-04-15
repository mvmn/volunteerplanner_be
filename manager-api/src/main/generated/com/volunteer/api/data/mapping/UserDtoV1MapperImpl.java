package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.UserDtoV1;
import com.volunteer.api.data.model.persistence.Role;
import com.volunteer.api.data.model.persistence.VPUser;
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
public class UserDtoV1MapperImpl extends UserDtoV1Mapper {

    @Autowired
    private GenericMapper genericMapper;

    @Override
    public UserDtoV1 map(VPUser dto) {
        if ( dto == null ) {
            return null;
        }

        UserDtoV1 userDtoV1 = new UserDtoV1();

        userDtoV1.setRole( dtoRoleName( dto ) );
        userDtoV1.setId( dto.getId() );
        userDtoV1.setPhoneNumber( dto.getPhoneNumber() );
        userDtoV1.setDisplayName( dto.getDisplayName() );
        userDtoV1.setOrganization( dto.getOrganization() );
        userDtoV1.setRating( dto.getRating() );
        userDtoV1.setPhoneNumberVerified( dto.isPhoneNumberVerified() );
        userDtoV1.setUserVerified( dto.isUserVerified() );
        userDtoV1.setUserVerifiedByUserId( dto.getUserVerifiedByUserId() );
        userDtoV1.setUserVerifiedAt( genericMapper.mapZonedDateTimeToUnixtime( dto.getUserVerifiedAt() ) );
        userDtoV1.setLocked( dto.isLocked() );
        userDtoV1.setLockedByUserId( dto.getLockedByUserId() );
        userDtoV1.setLockedAt( genericMapper.mapZonedDateTimeToUnixtime( dto.getLockedAt() ) );

        userDtoV1.setPassword( "******" );

        return userDtoV1;
    }

    @Override
    public VPUser map(UserDtoV1 user) {
        if ( user == null ) {
            return null;
        }

        VPUser vPUser = new VPUser();

        vPUser.setId( user.getId() );
        vPUser.setPhoneNumber( user.getPhoneNumber() );
        vPUser.setPassword( user.getPassword() );
        vPUser.setRole( map( user.getRole() ) );
        vPUser.setDisplayName( user.getDisplayName() );
        vPUser.setOrganization( user.getOrganization() );

        return vPUser;
    }

    @Override
    public Collection<UserDtoV1> map(Collection<VPUser> data) {
        if ( data == null ) {
            return null;
        }

        Collection<UserDtoV1> collection = new ArrayList<UserDtoV1>( data.size() );
        for ( VPUser vPUser : data ) {
            collection.add( map( vPUser ) );
        }

        return collection;
    }

    private String dtoRoleName(VPUser vPUser) {
        if ( vPUser == null ) {
            return null;
        }
        Role role = vPUser.getRole();
        if ( role == null ) {
            return null;
        }
        String name = role.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
