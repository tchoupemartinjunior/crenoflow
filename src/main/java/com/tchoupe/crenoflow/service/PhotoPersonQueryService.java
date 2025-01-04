package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.*; // for static metamodels
import com.tchoupe.crenoflow.domain.PhotoPerson;
import com.tchoupe.crenoflow.repository.PhotoPersonRepository;
import com.tchoupe.crenoflow.service.criteria.PhotoPersonCriteria;
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
 * Service for executing complex queries for {@link PhotoPerson} entities in the database.
 * The main input is a {@link PhotoPersonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PhotoPerson} or a {@link Page} of {@link PhotoPerson} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PhotoPersonQueryService extends QueryService<PhotoPerson> {

    private final Logger log = LoggerFactory.getLogger(PhotoPersonQueryService.class);

    private final PhotoPersonRepository photoPersonRepository;

    public PhotoPersonQueryService(PhotoPersonRepository photoPersonRepository) {
        this.photoPersonRepository = photoPersonRepository;
    }

    /**
     * Return a {@link List} of {@link PhotoPerson} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PhotoPerson> findByCriteria(PhotoPersonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PhotoPerson> specification = createSpecification(criteria);
        return photoPersonRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PhotoPerson} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PhotoPerson> findByCriteria(PhotoPersonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PhotoPerson> specification = createSpecification(criteria);
        return photoPersonRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PhotoPersonCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PhotoPerson> specification = createSpecification(criteria);
        return photoPersonRepository.count(specification);
    }

    /**
     * Function to convert {@link PhotoPersonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PhotoPerson> createSpecification(PhotoPersonCriteria criteria) {
        Specification<PhotoPerson> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PhotoPerson_.id));
            }
            if (criteria.getPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPersonId(), root -> root.join(PhotoPerson_.person, JoinType.LEFT).get(Person_.id))
                    );
            }
        }
        return specification;
    }
}
