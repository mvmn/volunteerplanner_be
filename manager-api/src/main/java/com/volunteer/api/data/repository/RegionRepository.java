package com.volunteer.api.data.repository;

import com.volunteer.api.data.model.persistence.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Integer> {

}
