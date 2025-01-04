package com.tchoupe.crenoflow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tchoupe.crenoflow.IntegrationTest;
import com.tchoupe.crenoflow.domain.CourseLocation;
import com.tchoupe.crenoflow.repository.CourseLocationRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CourseLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseLocationResourceIT {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/course-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseLocationRepository courseLocationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseLocationMockMvc;

    private CourseLocation courseLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseLocation createEntity(EntityManager em) {
        CourseLocation courseLocation = new CourseLocation().address(DEFAULT_ADDRESS).name(DEFAULT_NAME);
        return courseLocation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseLocation createUpdatedEntity(EntityManager em) {
        CourseLocation courseLocation = new CourseLocation().address(UPDATED_ADDRESS).name(UPDATED_NAME);
        return courseLocation;
    }

    @BeforeEach
    public void initTest() {
        courseLocation = createEntity(em);
    }

    @Test
    @Transactional
    void createCourseLocation() throws Exception {
        int databaseSizeBeforeCreate = courseLocationRepository.findAll().size();
        // Create the CourseLocation
        restCourseLocationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseLocation))
            )
            .andExpect(status().isCreated());

        // Validate the CourseLocation in the database
        List<CourseLocation> courseLocationList = courseLocationRepository.findAll();
        assertThat(courseLocationList).hasSize(databaseSizeBeforeCreate + 1);
        CourseLocation testCourseLocation = courseLocationList.get(courseLocationList.size() - 1);
        assertThat(testCourseLocation.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCourseLocation.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createCourseLocationWithExistingId() throws Exception {
        // Create the CourseLocation with an existing ID
        courseLocation.setId(1L);

        int databaseSizeBeforeCreate = courseLocationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseLocationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseLocation in the database
        List<CourseLocation> courseLocationList = courseLocationRepository.findAll();
        assertThat(courseLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseLocationRepository.findAll().size();
        // set the field null
        courseLocation.setName(null);

        // Create the CourseLocation, which fails.

        restCourseLocationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseLocation))
            )
            .andExpect(status().isBadRequest());

        List<CourseLocation> courseLocationList = courseLocationRepository.findAll();
        assertThat(courseLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourseLocations() throws Exception {
        // Initialize the database
        courseLocationRepository.saveAndFlush(courseLocation);

        // Get all the courseLocationList
        restCourseLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCourseLocation() throws Exception {
        // Initialize the database
        courseLocationRepository.saveAndFlush(courseLocation);

        // Get the courseLocation
        restCourseLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, courseLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseLocation.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCourseLocation() throws Exception {
        // Get the courseLocation
        restCourseLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCourseLocation() throws Exception {
        // Initialize the database
        courseLocationRepository.saveAndFlush(courseLocation);

        int databaseSizeBeforeUpdate = courseLocationRepository.findAll().size();

        // Update the courseLocation
        CourseLocation updatedCourseLocation = courseLocationRepository.findById(courseLocation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCourseLocation are not directly saved in db
        em.detach(updatedCourseLocation);
        updatedCourseLocation.address(UPDATED_ADDRESS).name(UPDATED_NAME);

        restCourseLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourseLocation.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourseLocation))
            )
            .andExpect(status().isOk());

        // Validate the CourseLocation in the database
        List<CourseLocation> courseLocationList = courseLocationRepository.findAll();
        assertThat(courseLocationList).hasSize(databaseSizeBeforeUpdate);
        CourseLocation testCourseLocation = courseLocationList.get(courseLocationList.size() - 1);
        assertThat(testCourseLocation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCourseLocation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCourseLocation() throws Exception {
        int databaseSizeBeforeUpdate = courseLocationRepository.findAll().size();
        courseLocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseLocation.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseLocation in the database
        List<CourseLocation> courseLocationList = courseLocationRepository.findAll();
        assertThat(courseLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseLocation() throws Exception {
        int databaseSizeBeforeUpdate = courseLocationRepository.findAll().size();
        courseLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseLocation in the database
        List<CourseLocation> courseLocationList = courseLocationRepository.findAll();
        assertThat(courseLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseLocation() throws Exception {
        int databaseSizeBeforeUpdate = courseLocationRepository.findAll().size();
        courseLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseLocationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseLocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseLocation in the database
        List<CourseLocation> courseLocationList = courseLocationRepository.findAll();
        assertThat(courseLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseLocationWithPatch() throws Exception {
        // Initialize the database
        courseLocationRepository.saveAndFlush(courseLocation);

        int databaseSizeBeforeUpdate = courseLocationRepository.findAll().size();

        // Update the courseLocation using partial update
        CourseLocation partialUpdatedCourseLocation = new CourseLocation();
        partialUpdatedCourseLocation.setId(courseLocation.getId());

        restCourseLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseLocation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseLocation))
            )
            .andExpect(status().isOk());

        // Validate the CourseLocation in the database
        List<CourseLocation> courseLocationList = courseLocationRepository.findAll();
        assertThat(courseLocationList).hasSize(databaseSizeBeforeUpdate);
        CourseLocation testCourseLocation = courseLocationList.get(courseLocationList.size() - 1);
        assertThat(testCourseLocation.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCourseLocation.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCourseLocationWithPatch() throws Exception {
        // Initialize the database
        courseLocationRepository.saveAndFlush(courseLocation);

        int databaseSizeBeforeUpdate = courseLocationRepository.findAll().size();

        // Update the courseLocation using partial update
        CourseLocation partialUpdatedCourseLocation = new CourseLocation();
        partialUpdatedCourseLocation.setId(courseLocation.getId());

        partialUpdatedCourseLocation.address(UPDATED_ADDRESS).name(UPDATED_NAME);

        restCourseLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseLocation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseLocation))
            )
            .andExpect(status().isOk());

        // Validate the CourseLocation in the database
        List<CourseLocation> courseLocationList = courseLocationRepository.findAll();
        assertThat(courseLocationList).hasSize(databaseSizeBeforeUpdate);
        CourseLocation testCourseLocation = courseLocationList.get(courseLocationList.size() - 1);
        assertThat(testCourseLocation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCourseLocation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCourseLocation() throws Exception {
        int databaseSizeBeforeUpdate = courseLocationRepository.findAll().size();
        courseLocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseLocation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseLocation in the database
        List<CourseLocation> courseLocationList = courseLocationRepository.findAll();
        assertThat(courseLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseLocation() throws Exception {
        int databaseSizeBeforeUpdate = courseLocationRepository.findAll().size();
        courseLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseLocation in the database
        List<CourseLocation> courseLocationList = courseLocationRepository.findAll();
        assertThat(courseLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseLocation() throws Exception {
        int databaseSizeBeforeUpdate = courseLocationRepository.findAll().size();
        courseLocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseLocationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseLocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseLocation in the database
        List<CourseLocation> courseLocationList = courseLocationRepository.findAll();
        assertThat(courseLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseLocation() throws Exception {
        // Initialize the database
        courseLocationRepository.saveAndFlush(courseLocation);

        int databaseSizeBeforeDelete = courseLocationRepository.findAll().size();

        // Delete the courseLocation
        restCourseLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseLocation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseLocation> courseLocationList = courseLocationRepository.findAll();
        assertThat(courseLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
