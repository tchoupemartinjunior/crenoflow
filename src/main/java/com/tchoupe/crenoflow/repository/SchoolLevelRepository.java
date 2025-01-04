package com.tchoupe.crenoflow.repository;

import com.tchoupe.crenoflow.domain.SchoolLevel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SchoolLevel entity.
 */
@Repository
public interface SchoolLevelRepository extends JpaRepository<SchoolLevel, Long>, JpaSpecificationExecutor<SchoolLevel> {
    default Optional<SchoolLevel> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SchoolLevel> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SchoolLevel> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select schoolLevel from SchoolLevel schoolLevel left join fetch schoolLevel.cycle",
        countQuery = "select count(schoolLevel) from SchoolLevel schoolLevel"
    )
    Page<SchoolLevel> findAllWithToOneRelationships(Pageable pageable);

    @Query("select schoolLevel from SchoolLevel schoolLevel left join fetch schoolLevel.cycle")
    List<SchoolLevel> findAllWithToOneRelationships();

    @Query("select schoolLevel from SchoolLevel schoolLevel left join fetch schoolLevel.cycle where schoolLevel.id =:id")
    Optional<SchoolLevel> findOneWithToOneRelationships(@Param("id") Long id);
}
