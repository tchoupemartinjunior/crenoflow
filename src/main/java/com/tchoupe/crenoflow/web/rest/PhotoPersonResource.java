package com.tchoupe.crenoflow.web.rest;

import com.tchoupe.crenoflow.domain.PhotoPerson;
import com.tchoupe.crenoflow.repository.PhotoPersonRepository;
import com.tchoupe.crenoflow.service.PhotoPersonQueryService;
import com.tchoupe.crenoflow.service.PhotoPersonService;
import com.tchoupe.crenoflow.service.criteria.PhotoPersonCriteria;
import com.tchoupe.crenoflow.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tchoupe.crenoflow.domain.PhotoPerson}.
 */
@RestController
@RequestMapping("/api/photo-people")
public class PhotoPersonResource {

    private final Logger log = LoggerFactory.getLogger(PhotoPersonResource.class);

    private static final String ENTITY_NAME = "photoPerson";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhotoPersonService photoPersonService;

    private final PhotoPersonRepository photoPersonRepository;

    private final PhotoPersonQueryService photoPersonQueryService;

    public PhotoPersonResource(
        PhotoPersonService photoPersonService,
        PhotoPersonRepository photoPersonRepository,
        PhotoPersonQueryService photoPersonQueryService
    ) {
        this.photoPersonService = photoPersonService;
        this.photoPersonRepository = photoPersonRepository;
        this.photoPersonQueryService = photoPersonQueryService;
    }

    /**
     * {@code POST  /photo-people} : Create a new photoPerson.
     *
     * @param photoPerson the photoPerson to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new photoPerson, or with status {@code 400 (Bad Request)} if the photoPerson has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PhotoPerson> createPhotoPerson(@RequestBody PhotoPerson photoPerson) throws URISyntaxException {
        log.debug("REST request to save PhotoPerson : {}", photoPerson);
        if (photoPerson.getId() != null) {
            throw new BadRequestAlertException("A new photoPerson cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PhotoPerson result = photoPersonService.save(photoPerson);
        return ResponseEntity
            .created(new URI("/api/photo-people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /photo-people/:id} : Updates an existing photoPerson.
     *
     * @param id the id of the photoPerson to save.
     * @param photoPerson the photoPerson to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated photoPerson,
     * or with status {@code 400 (Bad Request)} if the photoPerson is not valid,
     * or with status {@code 500 (Internal Server Error)} if the photoPerson couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PhotoPerson> updatePhotoPerson(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PhotoPerson photoPerson
    ) throws URISyntaxException {
        log.debug("REST request to update PhotoPerson : {}, {}", id, photoPerson);
        if (photoPerson.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, photoPerson.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!photoPersonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PhotoPerson result = photoPersonService.update(photoPerson);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, photoPerson.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /photo-people/:id} : Partial updates given fields of an existing photoPerson, field will ignore if it is null
     *
     * @param id the id of the photoPerson to save.
     * @param photoPerson the photoPerson to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated photoPerson,
     * or with status {@code 400 (Bad Request)} if the photoPerson is not valid,
     * or with status {@code 404 (Not Found)} if the photoPerson is not found,
     * or with status {@code 500 (Internal Server Error)} if the photoPerson couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PhotoPerson> partialUpdatePhotoPerson(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PhotoPerson photoPerson
    ) throws URISyntaxException {
        log.debug("REST request to partial update PhotoPerson partially : {}, {}", id, photoPerson);
        if (photoPerson.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, photoPerson.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!photoPersonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PhotoPerson> result = photoPersonService.partialUpdate(photoPerson);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, photoPerson.getId().toString())
        );
    }

    /**
     * {@code GET  /photo-people} : get all the photoPeople.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of photoPeople in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PhotoPerson>> getAllPhotoPeople(
        PhotoPersonCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PhotoPeople by criteria: {}", criteria);

        Page<PhotoPerson> page = photoPersonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /photo-people/count} : count all the photoPeople.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPhotoPeople(PhotoPersonCriteria criteria) {
        log.debug("REST request to count PhotoPeople by criteria: {}", criteria);
        return ResponseEntity.ok().body(photoPersonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /photo-people/:id} : get the "id" photoPerson.
     *
     * @param id the id of the photoPerson to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the photoPerson, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PhotoPerson> getPhotoPerson(@PathVariable Long id) {
        log.debug("REST request to get PhotoPerson : {}", id);
        Optional<PhotoPerson> photoPerson = photoPersonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(photoPerson);
    }

    /**
     * {@code DELETE  /photo-people/:id} : delete the "id" photoPerson.
     *
     * @param id the id of the photoPerson to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhotoPerson(@PathVariable Long id) {
        log.debug("REST request to delete PhotoPerson : {}", id);
        photoPersonService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
