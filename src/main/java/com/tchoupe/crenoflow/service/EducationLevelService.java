package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.EducationLevel;
import com.tchoupe.crenoflow.repository.EducationLevelRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.tchoupe.crenoflow.domain.EducationLevel}.
 */
@Service
@Transactional
public class EducationLevelService {

    private final Logger log = LoggerFactory.getLogger(EducationLevelService.class);

    private final EducationLevelRepository educationLevelRepository;

    public EducationLevelService(EducationLevelRepository educationLevelRepository) {
        this.educationLevelRepository = educationLevelRepository;
    }

    /**
     * Save a educationLevel.
     *
     * @param educationLevel the entity to save.
     * @return the persisted entity.
     */
    public EducationLevel save(EducationLevel educationLevel) {
        log.debug("Request to save EducationLevel : {}", educationLevel);
        return educationLevelRepository.save(educationLevel);
    }

    /**
     * Update a educationLevel.
     *
     * @param educationLevel the entity to save.
     * @return the persisted entity.
     */
    public EducationLevel update(EducationLevel educationLevel) {
        log.debug("Request to update EducationLevel : {}", educationLevel);
        return educationLevelRepository.save(educationLevel);
    }

    /**
     * Partially update a educationLevel.
     *
     * @param educationLevel the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EducationLevel> partialUpdate(EducationLevel educationLevel) {
        log.debug("Request to partially update EducationLevel : {}", educationLevel);

        return educationLevelRepository
            .findById(educationLevel.getId())
            .map(existingEducationLevel -> {
                if (educationLevel.getLabel() != null) {
                    existingEducationLevel.setLabel(educationLevel.getLabel());
                }

                return existingEducationLevel;
            })
            .map(educationLevelRepository::save);
    }

    /**
     * Get all the educationLevels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EducationLevel> findAll(Pageable pageable) {
        log.debug("Request to get all EducationLevels");
        return educationLevelRepository.findAll(pageable);
    }

    /**
     * Get one educationLevel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EducationLevel> findOne(Long id) {
        log.debug("Request to get EducationLevel : {}", id);
        return educationLevelRepository.findById(id);
    }

    /**
     * Delete the educationLevel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EducationLevel : {}", id);
        educationLevelRepository.deleteById(id);
    }
}
