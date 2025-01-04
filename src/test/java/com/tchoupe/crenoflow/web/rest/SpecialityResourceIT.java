package com.tchoupe.crenoflow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tchoupe.crenoflow.IntegrationTest;
import com.tchoupe.crenoflow.domain.Speciality;
import com.tchoupe.crenoflow.repository.SpecialityRepository;
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
 * Integration tests for the {@link SpecialityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecialityResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/specialities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecialityRepository specialityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialityMockMvc;

    private Speciality speciality;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Speciality createEntity(EntityManager em) {
        Speciality speciality = new Speciality().label(DEFAULT_LABEL);
        return speciality;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Speciality createUpdatedEntity(EntityManager em) {
        Speciality speciality = new Speciality().label(UPDATED_LABEL);
        return speciality;
    }

    @BeforeEach
    public void initTest() {
        speciality = createEntity(em);
    }

    @Test
    @Transactional
    void createSpeciality() throws Exception {
        int databaseSizeBeforeCreate = specialityRepository.findAll().size();
        // Create the Speciality
        restSpecialityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(speciality))
            )
            .andExpect(status().isCreated());

        // Validate the Speciality in the database
        List<Speciality> specialityList = specialityRepository.findAll();
        assertThat(specialityList).hasSize(databaseSizeBeforeCreate + 1);
        Speciality testSpeciality = specialityList.get(specialityList.size() - 1);
        assertThat(testSpeciality.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void createSpecialityWithExistingId() throws Exception {
        // Create the Speciality with an existing ID
        speciality.setId(1L);

        int databaseSizeBeforeCreate = specialityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(speciality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Speciality in the database
        List<Speciality> specialityList = specialityRepository.findAll();
        assertThat(specialityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSpecialities() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

        // Get all the specialityList
        restSpecialityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(speciality.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }

    @Test
    @Transactional
    void getSpeciality() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

        // Get the speciality
        restSpecialityMockMvc
            .perform(get(ENTITY_API_URL_ID, speciality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(speciality.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }

    @Test
    @Transactional
    void getSpecialitiesByIdFiltering() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

        Long id = speciality.getId();

        defaultSpecialityShouldBeFound("id.equals=" + id);
        defaultSpecialityShouldNotBeFound("id.notEquals=" + id);

        defaultSpecialityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSpecialityShouldNotBeFound("id.greaterThan=" + id);

        defaultSpecialityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSpecialityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSpecialitiesByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

        // Get all the specialityList where label equals to DEFAULT_LABEL
        defaultSpecialityShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the specialityList where label equals to UPDATED_LABEL
        defaultSpecialityShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllSpecialitiesByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

        // Get all the specialityList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultSpecialityShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the specialityList where label equals to UPDATED_LABEL
        defaultSpecialityShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllSpecialitiesByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

        // Get all the specialityList where label is not null
        defaultSpecialityShouldBeFound("label.specified=true");

        // Get all the specialityList where label is null
        defaultSpecialityShouldNotBeFound("label.specified=false");
    }

    @Test
    @Transactional
    void getAllSpecialitiesByLabelContainsSomething() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

        // Get all the specialityList where label contains DEFAULT_LABEL
        defaultSpecialityShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the specialityList where label contains UPDATED_LABEL
        defaultSpecialityShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllSpecialitiesByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

        // Get all the specialityList where label does not contain DEFAULT_LABEL
        defaultSpecialityShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the specialityList where label does not contain UPDATED_LABEL
        defaultSpecialityShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSpecialityShouldBeFound(String filter) throws Exception {
        restSpecialityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(speciality.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));

        // Check, that the count call also returns 1
        restSpecialityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSpecialityShouldNotBeFound(String filter) throws Exception {
        restSpecialityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSpecialityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSpeciality() throws Exception {
        // Get the speciality
        restSpecialityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpeciality() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

        int databaseSizeBeforeUpdate = specialityRepository.findAll().size();

        // Update the speciality
        Speciality updatedSpeciality = specialityRepository.findById(speciality.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpeciality are not directly saved in db
        em.detach(updatedSpeciality);
        updatedSpeciality.label(UPDATED_LABEL);

        restSpecialityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpeciality.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSpeciality))
            )
            .andExpect(status().isOk());

        // Validate the Speciality in the database
        List<Speciality> specialityList = specialityRepository.findAll();
        assertThat(specialityList).hasSize(databaseSizeBeforeUpdate);
        Speciality testSpeciality = specialityList.get(specialityList.size() - 1);
        assertThat(testSpeciality.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void putNonExistingSpeciality() throws Exception {
        int databaseSizeBeforeUpdate = specialityRepository.findAll().size();
        speciality.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, speciality.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(speciality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Speciality in the database
        List<Speciality> specialityList = specialityRepository.findAll();
        assertThat(specialityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpeciality() throws Exception {
        int databaseSizeBeforeUpdate = specialityRepository.findAll().size();
        speciality.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(speciality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Speciality in the database
        List<Speciality> specialityList = specialityRepository.findAll();
        assertThat(specialityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpeciality() throws Exception {
        int databaseSizeBeforeUpdate = specialityRepository.findAll().size();
        speciality.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(speciality))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Speciality in the database
        List<Speciality> specialityList = specialityRepository.findAll();
        assertThat(specialityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialityWithPatch() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

        int databaseSizeBeforeUpdate = specialityRepository.findAll().size();

        // Update the speciality using partial update
        Speciality partialUpdatedSpeciality = new Speciality();
        partialUpdatedSpeciality.setId(speciality.getId());

        partialUpdatedSpeciality.label(UPDATED_LABEL);

        restSpecialityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpeciality.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpeciality))
            )
            .andExpect(status().isOk());

        // Validate the Speciality in the database
        List<Speciality> specialityList = specialityRepository.findAll();
        assertThat(specialityList).hasSize(databaseSizeBeforeUpdate);
        Speciality testSpeciality = specialityList.get(specialityList.size() - 1);
        assertThat(testSpeciality.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void fullUpdateSpecialityWithPatch() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

        int databaseSizeBeforeUpdate = specialityRepository.findAll().size();

        // Update the speciality using partial update
        Speciality partialUpdatedSpeciality = new Speciality();
        partialUpdatedSpeciality.setId(speciality.getId());

        partialUpdatedSpeciality.label(UPDATED_LABEL);

        restSpecialityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpeciality.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpeciality))
            )
            .andExpect(status().isOk());

        // Validate the Speciality in the database
        List<Speciality> specialityList = specialityRepository.findAll();
        assertThat(specialityList).hasSize(databaseSizeBeforeUpdate);
        Speciality testSpeciality = specialityList.get(specialityList.size() - 1);
        assertThat(testSpeciality.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void patchNonExistingSpeciality() throws Exception {
        int databaseSizeBeforeUpdate = specialityRepository.findAll().size();
        speciality.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, speciality.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(speciality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Speciality in the database
        List<Speciality> specialityList = specialityRepository.findAll();
        assertThat(specialityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpeciality() throws Exception {
        int databaseSizeBeforeUpdate = specialityRepository.findAll().size();
        speciality.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(speciality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Speciality in the database
        List<Speciality> specialityList = specialityRepository.findAll();
        assertThat(specialityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpeciality() throws Exception {
        int databaseSizeBeforeUpdate = specialityRepository.findAll().size();
        speciality.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(speciality))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Speciality in the database
        List<Speciality> specialityList = specialityRepository.findAll();
        assertThat(specialityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpeciality() throws Exception {
        // Initialize the database
        specialityRepository.saveAndFlush(speciality);

        int databaseSizeBeforeDelete = specialityRepository.findAll().size();

        // Delete the speciality
        restSpecialityMockMvc
            .perform(delete(ENTITY_API_URL_ID, speciality.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Speciality> specialityList = specialityRepository.findAll();
        assertThat(specialityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
