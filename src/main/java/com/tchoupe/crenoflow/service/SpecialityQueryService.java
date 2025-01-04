package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.*; // for static metamodels
import com.tchoupe.crenoflow.domain.Speciality;
import com.tchoupe.crenoflow.repository.SpecialityRepository;
import com.tchoupe.crenoflow.service.criteria.SpecialityCriteria;
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
 * Service for executing complex queries for {@link Speciality} entities in the database.
 * The main input is a {@link SpecialityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Speciality} or a {@link Page} of {@link Speciality} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SpecialityQueryService extends QueryService<Speciality> {

    private final Logger log = LoggerFactory.getLogger(SpecialityQueryService.class);

    private final SpecialityRepository specialityRepository;

    public SpecialityQueryService(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }

    /**
     * Return a {@link List} of {@link Speciality} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Speciality> findByCriteria(SpecialityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Speciality> specification = createSpecification(criteria);
        return specialityRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Speciality} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Speciality> findByCriteria(SpecialityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Speciality> specification = createSpecification(criteria);
        return specialityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SpecialityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Speciality> specification = createSpecification(criteria);
        return specialityRepository.count(specification);
    }

    /**
     * Function to convert {@link SpecialityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Speciality> createSpecification(SpecialityCriteria criteria) {
        Specification<Speciality> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Speciality_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), Speciality_.label));
            }
        }
        return specification;
    }
}
