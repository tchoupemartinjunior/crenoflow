package com.tchoupe.crenoflow.repository;

import com.tchoupe.crenoflow.domain.EducationLevel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EducationLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EducationLevelRepository extends JpaRepository<EducationLevel, Long>, JpaSpecificationExecutor<EducationLevel> {}
