package com.tchoupe.crenoflow.web.rest;

import com.tchoupe.crenoflow.domain.SubjectCycle;
import com.tchoupe.crenoflow.repository.SubjectCycleRepository;
import com.tchoupe.crenoflow.service.SubjectCycleQueryService;
import com.tchoupe.crenoflow.service.SubjectCycleService;
import com.tchoupe.crenoflow.service.criteria.SubjectCycleCriteria;
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
 * REST controller for managing {@link com.tchoupe.crenoflow.domain.SubjectCycle}.
 */
@RestController
@RequestMapping("/api/subject-cycles")
public class SubjectCycleResource {

    private final Logger log = LoggerFactory.getLogger(SubjectCycleResource.class);

    private static final String ENTITY_NAME = "subjectCycle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubjectCycleService subjectCycleService;

    private final SubjectCycleRepository subjectCycleRepository;

    private final SubjectCycleQueryService subjectCycleQueryService;

    public SubjectCycleResource(
        SubjectCycleService subjectCycleService,
        SubjectCycleRepository subjectCycleRepository,
        SubjectCycleQueryService subjectCycleQueryService
    ) {
        this.subjectCycleService = subjectCycleService;
        this.subjectCycleRepository = subjectCycleRepository;
        this.subjectCycleQueryService = subjectCycleQueryService;
    }

    /**
     * {@code POST  /subject-cycles} : Create a new subjectCycle.
     *
     * @param subjectCycle the subjectCycle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subjectCycle, or with status {@code 400 (Bad Request)} if the subjectCycle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubjectCycle> createSubjectCycle(@RequestBody SubjectCycle subjectCycle) throws URISyntaxException {
        log.debug("REST request to save SubjectCycle : {}", subjectCycle);
        if (subjectCycle.getId() != null) {
            throw new BadRequestAlertException("A new subjectCycle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubjectCycle result = subjectCycleService.save(subjectCycle);
        return ResponseEntity
            .created(new URI("/api/subject-cycles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subject-cycles/:id} : Updates an existing subjectCycle.
     *
     * @param id the id of the subjectCycle to save.
     * @param subjectCycle the subjectCycle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subjectCycle,
     * or with status {@code 400 (Bad Request)} if the subjectCycle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subjectCycle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubjectCycle> updateSubjectCycle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubjectCycle subjectCycle
    ) throws URISyntaxException {
        log.debug("REST request to update SubjectCycle : {}, {}", id, subjectCycle);
        if (subjectCycle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subjectCycle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subjectCycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubjectCycle result = subjectCycleService.update(subjectCycle);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subjectCycle.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /subject-cycles/:id} : Partial updates given fields of an existing subjectCycle, field will ignore if it is null
     *
     * @param id the id of the subjectCycle to save.
     * @param subjectCycle the subjectCycle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subjectCycle,
     * or with status {@code 400 (Bad Request)} if the subjectCycle is not valid,
     * or with status {@code 404 (Not Found)} if the subjectCycle is not found,
     * or with status {@code 500 (Internal Server Error)} if the subjectCycle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubjectCycle> partialUpdateSubjectCycle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubjectCycle subjectCycle
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubjectCycle partially : {}, {}", id, subjectCycle);
        if (subjectCycle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subjectCycle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subjectCycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubjectCycle> result = subjectCycleService.partialUpdate(subjectCycle);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subjectCycle.getId().toString())
        );
    }

    /**
     * {@code GET  /subject-cycles} : get all the subjectCycles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subjectCycles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubjectCycle>> getAllSubjectCycles(
        SubjectCycleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SubjectCycles by criteria: {}", criteria);

        Page<SubjectCycle> page = subjectCycleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subject-cycles/count} : count all the subjectCycles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSubjectCycles(SubjectCycleCriteria criteria) {
        log.debug("REST request to count SubjectCycles by criteria: {}", criteria);
        return ResponseEntity.ok().body(subjectCycleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /subject-cycles/:id} : get the "id" subjectCycle.
     *
     * @param id the id of the subjectCycle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subjectCycle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubjectCycle> getSubjectCycle(@PathVariable Long id) {
        log.debug("REST request to get SubjectCycle : {}", id);
        Optional<SubjectCycle> subjectCycle = subjectCycleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subjectCycle);
    }

    /**
     * {@code DELETE  /subject-cycles/:id} : delete the "id" subjectCycle.
     *
     * @param id the id of the subjectCycle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubjectCycle(@PathVariable Long id) {
        log.debug("REST request to delete SubjectCycle : {}", id);
        subjectCycleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
