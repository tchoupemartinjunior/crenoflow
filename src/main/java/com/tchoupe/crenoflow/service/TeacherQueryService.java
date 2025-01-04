package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.*; // for static metamodels
import com.tchoupe.crenoflow.domain.Teacher;
import com.tchoupe.crenoflow.repository.TeacherRepository;
import com.tchoupe.crenoflow.service.criteria.TeacherCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Teacher} entities in the database.
 * The main input is a {@link TeacherCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Teacher} or a {@link Page} of {@link Teacher} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TeacherQueryService extends QueryService<Teacher> {

    private final Logger log = LoggerFactory.getLogger(TeacherQueryService.class);

    private final TeacherRepository teacherRepository;

    public TeacherQueryService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    /**
     * Return a {@link List} of {@link Teacher} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Teacher> findByCriteria(TeacherCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Teacher> specification = createSpecification(criteria);
        return teacherRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Teacher} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Teacher> findByCriteria(TeacherCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Teacher> specification = createSpecification(criteria);
        return teacherRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TeacherCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Teacher> specification = createSpecification(criteria);
        return teacherRepository.count(specification);
    }

    /**
     * Function to convert {@link TeacherCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Teacher> createSpecification(TeacherCriteria criteria) {
        Specification<Teacher> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Teacher_.id));
            }
            if (criteria.getIntroduction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIntroduction(), Teacher_.introduction));
            }
            if (criteria.getPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPersonId(), root -> root.join(Teacher_.person, JoinType.LEFT).get(Person_.id))
                    );
            }
            if (criteria.getEducationLevelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEducationLevelId(),
                            root -> root.join(Teacher_.educationLevel, JoinType.LEFT).get(EducationLevel_.id)
                        )
                    );
            }
            if (criteria.getProfessionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProfessionId(),
                            root -> root.join(Teacher_.profession, JoinType.LEFT).get(Profession_.id)
                        )
                    );
            }
            if (criteria.getSpecialityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSpecialityId(),
                            root -> root.join(Teacher_.speciality, JoinType.LEFT).get(Speciality_.id)
                        )
                    );
            }
            if (criteria.getSubjectCycleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubjectCycleId(),
                            root -> root.join(Teacher_.subjectCycles, JoinType.LEFT).get(SubjectCycle_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
