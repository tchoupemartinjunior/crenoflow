package com.tchoupe.crenoflow.repository;

import com.tchoupe.crenoflow.domain.CourseLocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CourseLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseLocationRepository extends JpaRepository<CourseLocation, Long> {}
