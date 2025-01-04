package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.SchoolLevel;
import com.tchoupe.crenoflow.repository.SchoolLevelRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.tchoupe.crenoflow.domain.SchoolLevel}.
 */
@Service
@Transactional
public class SchoolLevelService {

    private final Logger log = LoggerFactory.getLogger(SchoolLevelService.class);

    private final SchoolLevelRepository schoolLevelRepository;

    public SchoolLevelService(SchoolLevelRepository schoolLevelRepository) {
        this.schoolLevelRepository = schoolLevelRepository;
    }

    /**
     * Save a schoolLevel.
     *
     * @param schoolLevel the entity to save.
     * @return the persisted entity.
     */
    public SchoolLevel save(SchoolLevel schoolLevel) {
        log.debug("Request to save SchoolLevel : {}", schoolLevel);
        return schoolLevelRepository.save(schoolLevel);
    }

    /**
     * Update a schoolLevel.
     *
     * @param schoolLevel the entity to save.
     * @return the persisted entity.
     */
    public SchoolLevel update(SchoolLevel schoolLevel) {
        log.debug("Request to update SchoolLevel : {}", schoolLevel);
        return schoolLevelRepository.save(schoolLevel);
    }

    /**
     * Partially update a schoolLevel.
     *
     * @param schoolLevel the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SchoolLevel> partialUpdate(SchoolLevel schoolLevel) {
        log.debug("Request to partially update SchoolLevel : {}", schoolLevel);

        return schoolLevelRepository
            .findById(schoolLevel.getId())
            .map(existingSchoolLevel -> {
                if (schoolLevel.getLabel() != null) {
                    existingSchoolLevel.setLabel(schoolLevel.getLabel());
                }

                return existingSchoolLevel;
            })
            .map(schoolLevelRepository::save);
    }

    /**
     * Get all the schoolLevels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SchoolLevel> findAll(Pageable pageable) {
        log.debug("Request to get all SchoolLevels");
        return schoolLevelRepository.findAll(pageable);
    }

    /**
     * Get all the schoolLevels with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SchoolLevel> findAllWithEagerRelationships(Pageable pageable) {
        return schoolLevelRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one schoolLevel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SchoolLevel> findOne(Long id) {
        log.debug("Request to get SchoolLevel : {}", id);
        return schoolLevelRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the schoolLevel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SchoolLevel : {}", id);
        schoolLevelRepository.deleteById(id);
    }
}
