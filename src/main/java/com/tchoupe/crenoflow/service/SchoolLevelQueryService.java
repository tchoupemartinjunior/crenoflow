package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.*; // for static metamodels
import com.tchoupe.crenoflow.domain.SchoolLevel;
import com.tchoupe.crenoflow.repository.SchoolLevelRepository;
import com.tchoupe.crenoflow.service.criteria.SchoolLevelCriteria;
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
 * Service for executing complex queries for {@link SchoolLevel} entities in the database.
 * The main input is a {@link SchoolLevelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SchoolLevel} or a {@link Page} of {@link SchoolLevel} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SchoolLevelQueryService extends QueryService<SchoolLevel> {

    private final Logger log = LoggerFactory.getLogger(SchoolLevelQueryService.class);

    private final SchoolLevelRepository schoolLevelRepository;

    public SchoolLevelQueryService(SchoolLevelRepository schoolLevelRepository) {
        this.schoolLevelRepository = schoolLevelRepository;
    }

    /**
     * Return a {@link List} of {@link SchoolLevel} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SchoolLevel> findByCriteria(SchoolLevelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SchoolLevel> specification = createSpecification(criteria);
        return schoolLevelRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SchoolLevel} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolLevel> findByCriteria(SchoolLevelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SchoolLevel> specification = createSpecification(criteria);
        return schoolLevelRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SchoolLevelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SchoolLevel> specification = createSpecification(criteria);
        return schoolLevelRepository.count(specification);
    }

    /**
     * Function to convert {@link SchoolLevelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SchoolLevel> createSpecification(SchoolLevelCriteria criteria) {
        Specification<SchoolLevel> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SchoolLevel_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), SchoolLevel_.label));
            }
            if (criteria.getCycleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCycleId(), root -> root.join(SchoolLevel_.cycle, JoinType.LEFT).get(Cycle_.id))
                    );
            }
        }
        return specification;
    }
}
