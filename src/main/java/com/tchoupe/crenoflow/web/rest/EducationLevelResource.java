package com.tchoupe.crenoflow.web.rest;

import com.tchoupe.crenoflow.domain.EducationLevel;
import com.tchoupe.crenoflow.repository.EducationLevelRepository;
import com.tchoupe.crenoflow.service.EducationLevelQueryService;
import com.tchoupe.crenoflow.service.EducationLevelService;
import com.tchoupe.crenoflow.service.criteria.EducationLevelCriteria;
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
 * REST controller for managing {@link com.tchoupe.crenoflow.domain.EducationLevel}.
 */
@RestController
@RequestMapping("/api/education-levels")
public class EducationLevelResource {

    private final Logger log = LoggerFactory.getLogger(EducationLevelResource.class);

    private static final String ENTITY_NAME = "educationLevel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EducationLevelService educationLevelService;

    private final EducationLevelRepository educationLevelRepository;

    private final EducationLevelQueryService educationLevelQueryService;

    public EducationLevelResource(
        EducationLevelService educationLevelService,
        EducationLevelRepository educationLevelRepository,
        EducationLevelQueryService educationLevelQueryService
    ) {
        this.educationLevelService = educationLevelService;
        this.educationLevelRepository = educationLevelRepository;
        this.educationLevelQueryService = educationLevelQueryService;
    }

    /**
     * {@code POST  /education-levels} : Create a new educationLevel.
     *
     * @param educationLevel the educationLevel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new educationLevel, or with status {@code 400 (Bad Request)} if the educationLevel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EducationLevel> createEducationLevel(@RequestBody EducationLevel educationLevel) throws URISyntaxException {
        log.debug("REST request to save EducationLevel : {}", educationLevel);
        if (educationLevel.getId() != null) {
            throw new BadRequestAlertException("A new educationLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EducationLevel result = educationLevelService.save(educationLevel);
        return ResponseEntity
            .created(new URI("/api/education-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /education-levels/:id} : Updates an existing educationLevel.
     *
     * @param id the id of the educationLevel to save.
     * @param educationLevel the educationLevel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated educationLevel,
     * or with status {@code 400 (Bad Request)} if the educationLevel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the educationLevel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EducationLevel> updateEducationLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EducationLevel educationLevel
    ) throws URISyntaxException {
        log.debug("REST request to update EducationLevel : {}, {}", id, educationLevel);
        if (educationLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, educationLevel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!educationLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EducationLevel result = educationLevelService.update(educationLevel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, educationLevel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /education-levels/:id} : Partial updates given fields of an existing educationLevel, field will ignore if it is null
     *
     * @param id the id of the educationLevel to save.
     * @param educationLevel the educationLevel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated educationLevel,
     * or with status {@code 400 (Bad Request)} if the educationLevel is not valid,
     * or with status {@code 404 (Not Found)} if the educationLevel is not found,
     * or with status {@code 500 (Internal Server Error)} if the educationLevel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EducationLevel> partialUpdateEducationLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EducationLevel educationLevel
    ) throws URISyntaxException {
        log.debug("REST request to partial update EducationLevel partially : {}, {}", id, educationLevel);
        if (educationLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, educationLevel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!educationLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EducationLevel> result = educationLevelService.partialUpdate(educationLevel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, educationLevel.getId().toString())
        );
    }

    /**
     * {@code GET  /education-levels} : get all the educationLevels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of educationLevels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EducationLevel>> getAllEducationLevels(
        EducationLevelCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EducationLevels by criteria: {}", criteria);

        Page<EducationLevel> page = educationLevelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /education-levels/count} : count all the educationLevels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEducationLevels(EducationLevelCriteria criteria) {
        log.debug("REST request to count EducationLevels by criteria: {}", criteria);
        return ResponseEntity.ok().body(educationLevelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /education-levels/:id} : get the "id" educationLevel.
     *
     * @param id the id of the educationLevel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the educationLevel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EducationLevel> getEducationLevel(@PathVariable Long id) {
        log.debug("REST request to get EducationLevel : {}", id);
        Optional<EducationLevel> educationLevel = educationLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(educationLevel);
    }

    /**
     * {@code DELETE  /education-levels/:id} : delete the "id" educationLevel.
     *
     * @param id the id of the educationLevel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationLevel(@PathVariable Long id) {
        log.debug("REST request to delete EducationLevel : {}", id);
        educationLevelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
