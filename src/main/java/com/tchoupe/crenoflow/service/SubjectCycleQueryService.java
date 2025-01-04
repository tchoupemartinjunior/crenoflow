package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.*; // for static metamodels
import com.tchoupe.crenoflow.domain.SubjectCycle;
import com.tchoupe.crenoflow.repository.SubjectCycleRepository;
import com.tchoupe.crenoflow.service.criteria.SubjectCycleCriteria;
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
 * Service for executing complex queries for {@link SubjectCycle} entities in the database.
 * The main input is a {@link SubjectCycleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubjectCycle} or a {@link Page} of {@link SubjectCycle} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubjectCycleQueryService extends QueryService<SubjectCycle> {

    private final Logger log = LoggerFactory.getLogger(SubjectCycleQueryService.class);

    private final SubjectCycleRepository subjectCycleRepository;

    public SubjectCycleQueryService(SubjectCycleRepository subjectCycleRepository) {
        this.subjectCycleRepository = subjectCycleRepository;
    }

    /**
     * Return a {@link List} of {@link SubjectCycle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubjectCycle> findByCriteria(SubjectCycleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SubjectCycle> specification = createSpecification(criteria);
        return subjectCycleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SubjectCycle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubjectCycle> findByCriteria(SubjectCycleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubjectCycle> specification = createSpecification(criteria);
        return subjectCycleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubjectCycleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SubjectCycle> specification = createSpecification(criteria);
        return subjectCycleRepository.count(specification);
    }

    /**
     * Function to convert {@link SubjectCycleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubjectCycle> createSpecification(SubjectCycleCriteria criteria) {
        Specification<SubjectCycle> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubjectCycle_.id));
            }
            if (criteria.getTeacherId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTeacherId(),
                            root -> root.join(SubjectCycle_.teachers, JoinType.LEFT).get(Teacher_.id)
                        )
                    );
            }
            if (criteria.getSubjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubjectId(),
                            root -> root.join(SubjectCycle_.subjects, JoinType.LEFT).get(Subject_.id)
                        )
                    );
            }
            if (criteria.getCycleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCycleId(), root -> root.join(SubjectCycle_.cycles, JoinType.LEFT).get(Cycle_.id))
                    );
            }
        }
        return specification;
    }
}
