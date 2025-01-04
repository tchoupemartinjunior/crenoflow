package com.tchoupe.crenoflow.repository;

import com.tchoupe.crenoflow.domain.Course;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Course entity.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    default Optional<Course> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Course> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Course> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select course from Course course left join fetch course.subject left join fetch course.cycle",
        countQuery = "select count(course) from Course course"
    )
    Page<Course> findAllWithToOneRelationships(Pageable pageable);

    @Query("select course from Course course left join fetch course.subject left join fetch course.cycle")
    List<Course> findAllWithToOneRelationships();

    @Query("select course from Course course left join fetch course.subject left join fetch course.cycle where course.id =:id")
    Optional<Course> findOneWithToOneRelationships(@Param("id") Long id);
}
