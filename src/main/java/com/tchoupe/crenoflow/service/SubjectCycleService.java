package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.SubjectCycle;
import com.tchoupe.crenoflow.repository.SubjectCycleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.tchoupe.crenoflow.domain.SubjectCycle}.
 */
@Service
@Transactional
public class SubjectCycleService {

    private final Logger log = LoggerFactory.getLogger(SubjectCycleService.class);

    private final SubjectCycleRepository subjectCycleRepository;

    public SubjectCycleService(SubjectCycleRepository subjectCycleRepository) {
        this.subjectCycleRepository = subjectCycleRepository;
    }

    /**
     * Save a subjectCycle.
     *
     * @param subjectCycle the entity to save.
     * @return the persisted entity.
     */
    public SubjectCycle save(SubjectCycle subjectCycle) {
        log.debug("Request to save SubjectCycle : {}", subjectCycle);
        return subjectCycleRepository.save(subjectCycle);
    }

    /**
     * Update a subjectCycle.
     *
     * @param subjectCycle the entity to save.
     * @return the persisted entity.
     */
    public SubjectCycle update(SubjectCycle subjectCycle) {
        log.debug("Request to update SubjectCycle : {}", subjectCycle);
        // no save call needed as we have no fields that can be updated
        return subjectCycle;
    }

    /**
     * Partially update a subjectCycle.
     *
     * @param subjectCycle the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubjectCycle> partialUpdate(SubjectCycle subjectCycle) {
        log.debug("Request to partially update SubjectCycle : {}", subjectCycle);

        return subjectCycleRepository.findById(subjectCycle.getId()); // .map(subjectCycleRepository::save)
    }

    /**
     * Get all the subjectCycles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SubjectCycle> findAll(Pageable pageable) {
        log.debug("Request to get all SubjectCycles");
        return subjectCycleRepository.findAll(pageable);
    }

    /**
     * Get one subjectCycle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubjectCycle> findOne(Long id) {
        log.debug("Request to get SubjectCycle : {}", id);
        return subjectCycleRepository.findById(id);
    }

    /**
     * Delete the subjectCycle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SubjectCycle : {}", id);
        subjectCycleRepository.deleteById(id);
    }
}
