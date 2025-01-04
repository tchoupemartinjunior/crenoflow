package com.tchoupe.crenoflow.web.rest;

import com.tchoupe.crenoflow.domain.CourseLocation;
import com.tchoupe.crenoflow.repository.CourseLocationRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tchoupe.crenoflow.domain.CourseLocation}.
 */
@RestController
@RequestMapping("/api/course-locations")
@Transactional
public class CourseLocationResource {

    private final Logger log = LoggerFactory.getLogger(CourseLocationResource.class);

    private static final String ENTITY_NAME = "courseLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseLocationRepository courseLocationRepository;

    public CourseLocationResource(CourseLocationRepository courseLocationRepository) {
        this.courseLocationRepository = courseLocationRepository;
    }

    /**
     * {@code POST  /course-locations} : Create a new courseLocation.
     *
     * @param courseLocation the courseLocation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseLocation, or with status {@code 400 (Bad Request)} if the courseLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CourseLocation> createCourseLocation(@Valid @RequestBody CourseLocation courseLocation)
        throws URISyntaxException {
        log.debug("REST request to save CourseLocation : {}", courseLocation);
        if (courseLocation.getId() != null) {
            throw new BadRequestAlertException("A new courseLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseLocation result = courseLocationRepository.save(courseLocation);
        return ResponseEntity
            .created(new URI("/api/course-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-locations/:id} : Updates an existing courseLocation.
     *
     * @param id the id of the courseLocation to save.
     * @param courseLocation the courseLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseLocation,
     * or with status {@code 400 (Bad Request)} if the courseLocation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CourseLocation> updateCourseLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CourseLocation courseLocation
    ) throws URISyntaxException {
        log.debug("REST request to update CourseLocation : {}, {}", id, courseLocation);
        if (courseLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseLocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourseLocation result = courseLocationRepository.save(courseLocation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseLocation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /course-locations/:id} : Partial updates given fields of an existing courseLocation, field will ignore if it is null
     *
     * @param id the id of the courseLocation to save.
     * @param courseLocation the courseLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseLocation,
     * or with status {@code 400 (Bad Request)} if the courseLocation is not valid,
     * or with status {@code 404 (Not Found)} if the courseLocation is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CourseLocation> partialUpdateCourseLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CourseLocation courseLocation
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourseLocation partially : {}, {}", id, courseLocation);
        if (courseLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseLocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourseLocation> result = courseLocationRepository
            .findById(courseLocation.getId())
            .map(existingCourseLocation -> {
                if (courseLocation.getAddress() != null) {
                    existingCourseLocation.setAddress(courseLocation.getAddress());
                }
                if (courseLocation.getName() != null) {
                    existingCourseLocation.setName(courseLocation.getName());
                }

                return existingCourseLocation;
            })
            .map(courseLocationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseLocation.getId().toString())
        );
    }

    /**
     * {@code GET  /course-locations} : get all the courseLocations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseLocations in body.
     */
    @GetMapping("")
    public List<CourseLocation> getAllCourseLocations() {
        log.debug("REST request to get all CourseLocations");
        return courseLocationRepository.findAll();
    }

    /**
     * {@code GET  /course-locations/:id} : get the "id" courseLocation.
     *
     * @param id the id of the courseLocation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseLocation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseLocation> getCourseLocation(@PathVariable Long id) {
        log.debug("REST request to get CourseLocation : {}", id);
        Optional<CourseLocation> courseLocation = courseLocationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(courseLocation);
    }

    /**
     * {@code DELETE  /course-locations/:id} : delete the "id" courseLocation.
     *
     * @param id the id of the courseLocation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseLocation(@PathVariable Long id) {
        log.debug("REST request to delete CourseLocation : {}", id);
        courseLocationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
