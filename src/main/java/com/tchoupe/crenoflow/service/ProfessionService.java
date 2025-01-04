package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.Profession;
import com.tchoupe.crenoflow.repository.ProfessionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.tchoupe.crenoflow.domain.Profession}.
 */
@Service
@Transactional
public class ProfessionService {

    private final Logger log = LoggerFactory.getLogger(ProfessionService.class);

    private final ProfessionRepository professionRepository;

    public ProfessionService(ProfessionRepository professionRepository) {
        this.professionRepository = professionRepository;
    }

    /**
     * Save a profession.
     *
     * @param profession the entity to save.
     * @return the persisted entity.
     */
    public Profession save(Profession profession) {
        log.debug("Request to save Profession : {}", profession);
        return professionRepository.save(profession);
    }

    /**
     * Update a profession.
     *
     * @param profession the entity to save.
     * @return the persisted entity.
     */
    public Profession update(Profession profession) {
        log.debug("Request to update Profession : {}", profession);
        return professionRepository.save(profession);
    }

    /**
     * Partially update a profession.
     *
     * @param profession the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Profession> partialUpdate(Profession profession) {
        log.debug("Request to partially update Profession : {}", profession);

        return professionRepository
            .findById(profession.getId())
            .map(existingProfession -> {
                if (profession.getLabel() != null) {
                    existingProfession.setLabel(profession.getLabel());
                }

                return existingProfession;
            })
            .map(professionRepository::save);
    }

    /**
     * Get all the professions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Profession> findAll(Pageable pageable) {
        log.debug("Request to get all Professions");
        return professionRepository.findAll(pageable);
    }

    /**
     * Get one profession by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Profession> findOne(Long id) {
        log.debug("Request to get Profession : {}", id);
        return professionRepository.findById(id);
    }

    /**
     * Delete the profession by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Profession : {}", id);
        professionRepository.deleteById(id);
    }
}
