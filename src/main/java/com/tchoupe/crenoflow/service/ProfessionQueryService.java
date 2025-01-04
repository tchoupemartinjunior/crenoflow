package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.*; // for static metamodels
import com.tchoupe.crenoflow.domain.Profession;
import com.tchoupe.crenoflow.repository.ProfessionRepository;
import com.tchoupe.crenoflow.service.criteria.ProfessionCriteria;
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
 * Service for executing complex queries for {@link Profession} entities in the database.
 * The main input is a {@link ProfessionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Profession} or a {@link Page} of {@link Profession} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfessionQueryService extends QueryService<Profession> {

    private final Logger log = LoggerFactory.getLogger(ProfessionQueryService.class);

    private final ProfessionRepository professionRepository;

    public ProfessionQueryService(ProfessionRepository professionRepository) {
        this.professionRepository = professionRepository;
    }

    /**
     * Return a {@link List} of {@link Profession} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Profession> findByCriteria(ProfessionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Profession> specification = createSpecification(criteria);
        return professionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Profession} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Profession> findByCriteria(ProfessionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Profession> specification = createSpecification(criteria);
        return professionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProfessionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Profession> specification = createSpecification(criteria);
        return professionRepository.count(specification);
    }

    /**
     * Function to convert {@link ProfessionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Profession> createSpecification(ProfessionCriteria criteria) {
        Specification<Profession> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Profession_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), Profession_.label));
            }
        }
        return specification;
    }
}
