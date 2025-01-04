package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.*; // for static metamodels
import com.tchoupe.crenoflow.domain.Availability;
import com.tchoupe.crenoflow.repository.AvailabilityRepository;
import com.tchoupe.crenoflow.service.criteria.AvailabilityCriteria;
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
 * Service for executing complex queries for {@link Availability} entities in the database.
 * The main input is a {@link AvailabilityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Availability} or a {@link Page} of {@link Availability} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AvailabilityQueryService extends QueryService<Availability> {

    private final Logger log = LoggerFactory.getLogger(AvailabilityQueryService.class);

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityQueryService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    /**
     * Return a {@link List} of {@link Availability} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Availability> findByCriteria(AvailabilityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Availability> specification = createSpecification(criteria);
        return availabilityRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Availability} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Availability> findByCriteria(AvailabilityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Availability> specification = createSpecification(criteria);
        return availabilityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AvailabilityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Availability> specification = createSpecification(criteria);
        return availabilityRepository.count(specification);
    }

    /**
     * Function to convert {@link AvailabilityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Availability> createSpecification(AvailabilityCriteria criteria) {
        Specification<Availability> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Availability_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Availability_.date));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStartTime(), Availability_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEndTime(), Availability_.endTime));
            }
            if (criteria.getFormat() != null) {
                specification = specification.and(buildSpecification(criteria.getFormat(), Availability_.format));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), Availability_.comment));
            }
            if (criteria.getVideoLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVideoLink(), Availability_.videoLink));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Availability_.address));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Availability_.status));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Availability_.creationDate));
            }
        }
        return specification;
    }
}
