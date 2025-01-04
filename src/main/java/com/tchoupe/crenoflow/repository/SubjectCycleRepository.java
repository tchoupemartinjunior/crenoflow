package com.tchoupe.crenoflow.repository;

import com.tchoupe.crenoflow.domain.SubjectCycle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubjectCycle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubjectCycleRepository extends JpaRepository<SubjectCycle, Long>, JpaSpecificationExecutor<SubjectCycle> {}
