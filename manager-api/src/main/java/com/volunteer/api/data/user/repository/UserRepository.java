package com.volunteer.api.data.user.repository;

import com.volunteer.api.data.user.model.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDto, Integer> {

  UserDto findByUserName(final String userName);

}
