package com.tchoupe.crenoflow.web.rest;

import com.tchoupe.crenoflow.domain.SchoolLevel;
import com.tchoupe.crenoflow.repository.SchoolLevelRepository;
import com.tchoupe.crenoflow.service.SchoolLevelQueryService;
import com.tchoupe.crenoflow.service.SchoolLevelService;
import com.tchoupe.crenoflow.service.criteria.SchoolLevelCriteria;
import com.tchoupe.crenoflow.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.tchoupe.crenoflow.domain.SchoolLevel}.
 */
@RestController
@RequestMapping("/api/school-levels")
public class SchoolLevelResource {

    private final Logger log = LoggerFactory.getLogger(SchoolLevelResource.class);

    private static final String ENTITY_NAME = "schoolLevel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchoolLevelService schoolLevelService;

    private final SchoolLevelRepository schoolLevelRepository;

    private final SchoolLevelQueryService schoolLevelQueryService;

    public SchoolLevelResource(
        SchoolLevelService schoolLevelService,
        SchoolLevelRepository schoolLevelRepository,
        SchoolLevelQueryService schoolLevelQueryService
    ) {
        this.schoolLevelService = schoolLevelService;
        this.schoolLevelRepository = schoolLevelRepository;
        this.schoolLevelQueryService = schoolLevelQueryService;
    }

    /**
     * {@code POST  /school-levels} : Create a new schoolLevel.
     *
     * @param schoolLevel the schoolLevel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schoolLevel, or with status {@code 400 (Bad Request)} if the schoolLevel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SchoolLevel> createSchoolLevel(@Valid @RequestBody SchoolLevel schoolLevel) throws URISyntaxException {
        log.debug("REST request to save SchoolLevel : {}", schoolLevel);
        if (schoolLevel.getId() != null) {
            throw new BadRequestAlertException("A new schoolLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchoolLevel result = schoolLevelService.save(schoolLevel);
        return ResponseEntity
            .created(new URI("/api/school-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /school-levels/:id} : Updates an existing schoolLevel.
     *
     * @param id the id of the schoolLevel to save.
     * @param schoolLevel the schoolLevel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schoolLevel,
     * or with status {@code 400 (Bad Request)} if the schoolLevel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schoolLevel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SchoolLevel> updateSchoolLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SchoolLevel schoolLevel
    ) throws URISyntaxException {
        log.debug("REST request to update SchoolLevel : {}, {}", id, schoolLevel);
        if (schoolLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schoolLevel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schoolLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SchoolLevel result = schoolLevelService.update(schoolLevel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schoolLevel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /school-levels/:id} : Partial updates given fields of an existing schoolLevel, field will ignore if it is null
     *
     * @param id the id of the schoolLevel to save.
     * @param schoolLevel the schoolLevel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schoolLevel,
     * or with status {@code 400 (Bad Request)} if the schoolLevel is not valid,
     * or with status {@code 404 (Not Found)} if the schoolLevel is not found,
     * or with status {@code 500 (Internal Server Error)} if the schoolLevel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SchoolLevel> partialUpdateSchoolLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SchoolLevel schoolLevel
    ) throws URISyntaxException {
        log.debug("REST request to partial update SchoolLevel partially : {}, {}", id, schoolLevel);
        if (schoolLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schoolLevel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schoolLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SchoolLevel> result = schoolLevelService.partialUpdate(schoolLevel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schoolLevel.getId().toString())
        );
    }

    /**
     * {@code GET  /school-levels} : get all the schoolLevels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schoolLevels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SchoolLevel>> getAllSchoolLevels(
        SchoolLevelCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SchoolLevels by criteria: {}", criteria);

        Page<SchoolLevel> page = schoolLevelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /school-levels/count} : count all the schoolLevels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSchoolLevels(SchoolLevelCriteria criteria) {
        log.debug("REST request to count SchoolLevels by criteria: {}", criteria);
        return ResponseEntity.ok().body(schoolLevelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /school-levels/:id} : get the "id" schoolLevel.
     *
     * @param id the id of the schoolLevel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schoolLevel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SchoolLevel> getSchoolLevel(@PathVariable Long id) {
        log.debug("REST request to get SchoolLevel : {}", id);
        Optional<SchoolLevel> schoolLevel = schoolLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(schoolLevel);
    }

    /**
     * {@code DELETE  /school-levels/:id} : delete the "id" schoolLevel.
     *
     * @param id the id of the schoolLevel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchoolLevel(@PathVariable Long id) {
        log.debug("REST request to delete SchoolLevel : {}", id);
        schoolLevelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
