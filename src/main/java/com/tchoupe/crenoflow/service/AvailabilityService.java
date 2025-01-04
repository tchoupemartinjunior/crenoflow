package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.Availability;
import com.tchoupe.crenoflow.repository.AvailabilityRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.tchoupe.crenoflow.domain.Availability}.
 */
@Service
@Transactional
public class AvailabilityService {

    private final Logger log = LoggerFactory.getLogger(AvailabilityService.class);

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    /**
     * Save a availability.
     *
     * @param availability the entity to save.
     * @return the persisted entity.
     */
    public Availability save(Availability availability) {
        log.debug("Request to save Availability : {}", availability);
        return availabilityRepository.save(availability);
    }

    /**
     * Update a availability.
     *
     * @param availability the entity to save.
     * @return the persisted entity.
     */
    public Availability update(Availability availability) {
        log.debug("Request to update Availability : {}", availability);
        return availabilityRepository.save(availability);
    }

    /**
     * Partially update a availability.
     *
     * @param availability the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Availability> partialUpdate(Availability availability) {
        log.debug("Request to partially update Availability : {}", availability);

        return availabilityRepository
            .findById(availability.getId())
            .map(existingAvailability -> {
                if (availability.getDate() != null) {
                    existingAvailability.setDate(availability.getDate());
                }
                if (availability.getStartTime() != null) {
                    existingAvailability.setStartTime(availability.getStartTime());
                }
                if (availability.getEndTime() != null) {
                    existingAvailability.setEndTime(availability.getEndTime());
                }
                if (availability.getFormat() != null) {
                    existingAvailability.setFormat(availability.getFormat());
                }
                if (availability.getComment() != null) {
                    existingAvailability.setComment(availability.getComment());
                }
                if (availability.getVideoLink() != null) {
                    existingAvailability.setVideoLink(availability.getVideoLink());
                }
                if (availability.getAddress() != null) {
                    existingAvailability.setAddress(availability.getAddress());
                }
                if (availability.getStatus() != null) {
                    existingAvailability.setStatus(availability.getStatus());
                }
                if (availability.getCreationDate() != null) {
                    existingAvailability.setCreationDate(availability.getCreationDate());
                }

                return existingAvailability;
            })
            .map(availabilityRepository::save);
    }

    /**
     * Get all the availabilities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Availability> findAll(Pageable pageable) {
        log.debug("Request to get all Availabilities");
        return availabilityRepository.findAll(pageable);
    }

    /**
     * Get one availability by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Availability> findOne(Long id) {
        log.debug("Request to get Availability : {}", id);
        return availabilityRepository.findById(id);
    }

    /**
     * Delete the availability by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Availability : {}", id);
        availabilityRepository.deleteById(id);
    }
}
