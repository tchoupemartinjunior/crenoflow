package com.tchoupe.crenoflow.repository;

import com.tchoupe.crenoflow.domain.Teacher;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Teacher entity.
 *
 * When extending this class, extend TeacherRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TeacherRepository
    extends TeacherRepositoryWithBagRelationships, JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
    default Optional<Teacher> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Teacher> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Teacher> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select teacher from Teacher teacher left join fetch teacher.person left join fetch teacher.educationLevel left join fetch teacher.profession left join fetch teacher.speciality",
        countQuery = "select count(teacher) from Teacher teacher"
    )
    Page<Teacher> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select teacher from Teacher teacher left join fetch teacher.person left join fetch teacher.educationLevel left join fetch teacher.profession left join fetch teacher.speciality"
    )
    List<Teacher> findAllWithToOneRelationships();

    @Query(
        "select teacher from Teacher teacher left join fetch teacher.person left join fetch teacher.educationLevel left join fetch teacher.profession left join fetch teacher.speciality where teacher.id =:id"
    )
    Optional<Teacher> findOneWithToOneRelationships(@Param("id") Long id);
}
