package com.tchoupe.crenoflow.repository;

import com.tchoupe.crenoflow.domain.PhotoPerson;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PhotoPerson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhotoPersonRepository extends JpaRepository<PhotoPerson, Long>, JpaSpecificationExecutor<PhotoPerson> {}
