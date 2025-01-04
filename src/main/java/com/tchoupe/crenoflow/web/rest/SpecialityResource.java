package com.tchoupe.crenoflow.web.rest;

import com.tchoupe.crenoflow.domain.Speciality;
import com.tchoupe.crenoflow.repository.SpecialityRepository;
import com.tchoupe.crenoflow.service.SpecialityQueryService;
import com.tchoupe.crenoflow.service.SpecialityService;
import com.tchoupe.crenoflow.service.criteria.SpecialityCriteria;
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
 * REST controller for managing {@link com.tchoupe.crenoflow.domain.Speciality}.
 */
@RestController
@RequestMapping("/api/specialities")
public class SpecialityResource {

    private final Logger log = LoggerFactory.getLogger(SpecialityResource.class);

    private static final String ENTITY_NAME = "speciality";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialityService specialityService;

    private final SpecialityRepository specialityRepository;

    private final SpecialityQueryService specialityQueryService;

    public SpecialityResource(
        SpecialityService specialityService,
        SpecialityRepository specialityRepository,
        SpecialityQueryService specialityQueryService
    ) {
        this.specialityService = specialityService;
        this.specialityRepository = specialityRepository;
        this.specialityQueryService = specialityQueryService;
    }

    /**
     * {@code POST  /specialities} : Create a new speciality.
     *
     * @param speciality the speciality to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new speciality, or with status {@code 400 (Bad Request)} if the speciality has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Speciality> createSpeciality(@RequestBody Speciality speciality) throws URISyntaxException {
        log.debug("REST request to save Speciality : {}", speciality);
        if (speciality.getId() != null) {
            throw new BadRequestAlertException("A new speciality cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Speciality result = specialityService.save(speciality);
        return ResponseEntity
            .created(new URI("/api/specialities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /specialities/:id} : Updates an existing speciality.
     *
     * @param id the id of the speciality to save.
     * @param speciality the speciality to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated speciality,
     * or with status {@code 400 (Bad Request)} if the speciality is not valid,
     * or with status {@code 500 (Internal Server Error)} if the speciality couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Speciality> updateSpeciality(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Speciality speciality
    ) throws URISyntaxException {
        log.debug("REST request to update Speciality : {}, {}", id, speciality);
        if (speciality.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, speciality.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Speciality result = specialityService.update(speciality);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, speciality.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /specialities/:id} : Partial updates given fields of an existing speciality, field will ignore if it is null
     *
     * @param id the id of the speciality to save.
     * @param speciality the speciality to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated speciality,
     * or with status {@code 400 (Bad Request)} if the speciality is not valid,
     * or with status {@code 404 (Not Found)} if the speciality is not found,
     * or with status {@code 500 (Internal Server Error)} if the speciality couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Speciality> partialUpdateSpeciality(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Speciality speciality
    ) throws URISyntaxException {
        log.debug("REST request to partial update Speciality partially : {}, {}", id, speciality);
        if (speciality.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, speciality.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Speciality> result = specialityService.partialUpdate(speciality);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, speciality.getId().toString())
        );
    }

    /**
     * {@code GET  /specialities} : get all the specialities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Speciality>> getAllSpecialities(
        SpecialityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Specialities by criteria: {}", criteria);

        Page<Speciality> page = specialityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /specialities/count} : count all the specialities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSpecialities(SpecialityCriteria criteria) {
        log.debug("REST request to count Specialities by criteria: {}", criteria);
        return ResponseEntity.ok().body(specialityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /specialities/:id} : get the "id" speciality.
     *
     * @param id the id of the speciality to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the speciality, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Speciality> getSpeciality(@PathVariable Long id) {
        log.debug("REST request to get Speciality : {}", id);
        Optional<Speciality> speciality = specialityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(speciality);
    }

    /**
     * {@code DELETE  /specialities/:id} : delete the "id" speciality.
     *
     * @param id the id of the speciality to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpeciality(@PathVariable Long id) {
        log.debug("REST request to delete Speciality : {}", id);
        specialityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
