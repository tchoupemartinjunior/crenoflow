package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.Speciality;
import com.tchoupe.crenoflow.repository.SpecialityRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.tchoupe.crenoflow.domain.Speciality}.
 */
@Service
@Transactional
public class SpecialityService {

    private final Logger log = LoggerFactory.getLogger(SpecialityService.class);

    private final SpecialityRepository specialityRepository;

    public SpecialityService(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }

    /**
     * Save a speciality.
     *
     * @param speciality the entity to save.
     * @return the persisted entity.
     */
    public Speciality save(Speciality speciality) {
        log.debug("Request to save Speciality : {}", speciality);
        return specialityRepository.save(speciality);
    }

    /**
     * Update a speciality.
     *
     * @param speciality the entity to save.
     * @return the persisted entity.
     */
    public Speciality update(Speciality speciality) {
        log.debug("Request to update Speciality : {}", speciality);
        return specialityRepository.save(speciality);
    }

    /**
     * Partially update a speciality.
     *
     * @param speciality the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Speciality> partialUpdate(Speciality speciality) {
        log.debug("Request to partially update Speciality : {}", speciality);

        return specialityRepository
            .findById(speciality.getId())
            .map(existingSpeciality -> {
                if (speciality.getLabel() != null) {
                    existingSpeciality.setLabel(speciality.getLabel());
                }

                return existingSpeciality;
            })
            .map(specialityRepository::save);
    }

    /**
     * Get all the specialities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Speciality> findAll(Pageable pageable) {
        log.debug("Request to get all Specialities");
        return specialityRepository.findAll(pageable);
    }

    /**
     * Get one speciality by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Speciality> findOne(Long id) {
        log.debug("Request to get Speciality : {}", id);
        return specialityRepository.findById(id);
    }

    /**
     * Delete the speciality by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Speciality : {}", id);
        specialityRepository.deleteById(id);
    }
}
