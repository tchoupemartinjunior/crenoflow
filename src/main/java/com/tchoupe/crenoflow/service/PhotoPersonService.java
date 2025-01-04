package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.PhotoPerson;
import com.tchoupe.crenoflow.repository.PhotoPersonRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.tchoupe.crenoflow.domain.PhotoPerson}.
 */
@Service
@Transactional
public class PhotoPersonService {

    private final Logger log = LoggerFactory.getLogger(PhotoPersonService.class);

    private final PhotoPersonRepository photoPersonRepository;

    public PhotoPersonService(PhotoPersonRepository photoPersonRepository) {
        this.photoPersonRepository = photoPersonRepository;
    }

    /**
     * Save a photoPerson.
     *
     * @param photoPerson the entity to save.
     * @return the persisted entity.
     */
    public PhotoPerson save(PhotoPerson photoPerson) {
        log.debug("Request to save PhotoPerson : {}", photoPerson);
        return photoPersonRepository.save(photoPerson);
    }

    /**
     * Update a photoPerson.
     *
     * @param photoPerson the entity to save.
     * @return the persisted entity.
     */
    public PhotoPerson update(PhotoPerson photoPerson) {
        log.debug("Request to update PhotoPerson : {}", photoPerson);
        return photoPersonRepository.save(photoPerson);
    }

    /**
     * Partially update a photoPerson.
     *
     * @param photoPerson the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PhotoPerson> partialUpdate(PhotoPerson photoPerson) {
        log.debug("Request to partially update PhotoPerson : {}", photoPerson);

        return photoPersonRepository
            .findById(photoPerson.getId())
            .map(existingPhotoPerson -> {
                if (photoPerson.getPhoto() != null) {
                    existingPhotoPerson.setPhoto(photoPerson.getPhoto());
                }
                if (photoPerson.getPhotoContentType() != null) {
                    existingPhotoPerson.setPhotoContentType(photoPerson.getPhotoContentType());
                }

                return existingPhotoPerson;
            })
            .map(photoPersonRepository::save);
    }

    /**
     * Get all the photoPeople.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PhotoPerson> findAll(Pageable pageable) {
        log.debug("Request to get all PhotoPeople");
        return photoPersonRepository.findAll(pageable);
    }

    /**
     * Get one photoPerson by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PhotoPerson> findOne(Long id) {
        log.debug("Request to get PhotoPerson : {}", id);
        return photoPersonRepository.findById(id);
    }

    /**
     * Delete the photoPerson by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PhotoPerson : {}", id);
        photoPersonRepository.deleteById(id);
    }
}
