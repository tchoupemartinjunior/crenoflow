package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.*; // for static metamodels
import com.tchoupe.crenoflow.domain.EducationLevel;
import com.tchoupe.crenoflow.repository.EducationLevelRepository;
import com.tchoupe.crenoflow.service.criteria.EducationLevelCriteria;
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
 * Service for executing complex queries for {@link EducationLevel} entities in the database.
 * The main input is a {@link EducationLevelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EducationLevel} or a {@link Page} of {@link EducationLevel} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EducationLevelQueryService extends QueryService<EducationLevel> {

    private final Logger log = LoggerFactory.getLogger(EducationLevelQueryService.class);

    private final EducationLevelRepository educationLevelRepository;

    public EducationLevelQueryService(EducationLevelRepository educationLevelRepository) {
        this.educationLevelRepository = educationLevelRepository;
    }

    /**
     * Return a {@link List} of {@link EducationLevel} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EducationLevel> findByCriteria(EducationLevelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EducationLevel> specification = createSpecification(criteria);
        return educationLevelRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EducationLevel} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EducationLevel> findByCriteria(EducationLevelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EducationLevel> specification = createSpecification(criteria);
        return educationLevelRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EducationLevelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EducationLevel> specification = createSpecification(criteria);
        return educationLevelRepository.count(specification);
    }

    /**
     * Function to convert {@link EducationLevelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EducationLevel> createSpecification(EducationLevelCriteria criteria) {
        Specification<EducationLevel> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EducationLevel_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), EducationLevel_.label));
            }
        }
        return specification;
    }
}
