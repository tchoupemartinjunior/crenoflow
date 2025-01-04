package com.tchoupe.crenoflow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tchoupe.crenoflow.IntegrationTest;
import com.tchoupe.crenoflow.domain.EducationLevel;
import com.tchoupe.crenoflow.repository.EducationLevelRepository;
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
 * Integration tests for the {@link EducationLevelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EducationLevelResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/education-levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EducationLevelRepository educationLevelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEducationLevelMockMvc;

    private EducationLevel educationLevel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EducationLevel createEntity(EntityManager em) {
        EducationLevel educationLevel = new EducationLevel().label(DEFAULT_LABEL);
        return educationLevel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EducationLevel createUpdatedEntity(EntityManager em) {
        EducationLevel educationLevel = new EducationLevel().label(UPDATED_LABEL);
        return educationLevel;
    }

    @BeforeEach
    public void initTest() {
        educationLevel = createEntity(em);
    }

    @Test
    @Transactional
    void createEducationLevel() throws Exception {
        int databaseSizeBeforeCreate = educationLevelRepository.findAll().size();
        // Create the EducationLevel
        restEducationLevelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationLevel))
            )
            .andExpect(status().isCreated());

        // Validate the EducationLevel in the database
        List<EducationLevel> educationLevelList = educationLevelRepository.findAll();
        assertThat(educationLevelList).hasSize(databaseSizeBeforeCreate + 1);
        EducationLevel testEducationLevel = educationLevelList.get(educationLevelList.size() - 1);
        assertThat(testEducationLevel.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void createEducationLevelWithExistingId() throws Exception {
        // Create the EducationLevel with an existing ID
        educationLevel.setId(1L);

        int databaseSizeBeforeCreate = educationLevelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEducationLevelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationLevel in the database
        List<EducationLevel> educationLevelList = educationLevelRepository.findAll();
        assertThat(educationLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEducationLevels() throws Exception {
        // Initialize the database
        educationLevelRepository.saveAndFlush(educationLevel);

        // Get all the educationLevelList
        restEducationLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(educationLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }

    @Test
    @Transactional
    void getEducationLevel() throws Exception {
        // Initialize the database
        educationLevelRepository.saveAndFlush(educationLevel);

        // Get the educationLevel
        restEducationLevelMockMvc
            .perform(get(ENTITY_API_URL_ID, educationLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(educationLevel.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }

    @Test
    @Transactional
    void getEducationLevelsByIdFiltering() throws Exception {
        // Initialize the database
        educationLevelRepository.saveAndFlush(educationLevel);

        Long id = educationLevel.getId();

        defaultEducationLevelShouldBeFound("id.equals=" + id);
        defaultEducationLevelShouldNotBeFound("id.notEquals=" + id);

        defaultEducationLevelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEducationLevelShouldNotBeFound("id.greaterThan=" + id);

        defaultEducationLevelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEducationLevelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEducationLevelsByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        educationLevelRepository.saveAndFlush(educationLevel);

        // Get all the educationLevelList where label equals to DEFAULT_LABEL
        defaultEducationLevelShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the educationLevelList where label equals to UPDATED_LABEL
        defaultEducationLevelShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllEducationLevelsByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        educationLevelRepository.saveAndFlush(educationLevel);

        // Get all the educationLevelList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultEducationLevelShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the educationLevelList where label equals to UPDATED_LABEL
        defaultEducationLevelShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllEducationLevelsByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        educationLevelRepository.saveAndFlush(educationLevel);

        // Get all the educationLevelList where label is not null
        defaultEducationLevelShouldBeFound("label.specified=true");

        // Get all the educationLevelList where label is null
        defaultEducationLevelShouldNotBeFound("label.specified=false");
    }

    @Test
    @Transactional
    void getAllEducationLevelsByLabelContainsSomething() throws Exception {
        // Initialize the database
        educationLevelRepository.saveAndFlush(educationLevel);

        // Get all the educationLevelList where label contains DEFAULT_LABEL
        defaultEducationLevelShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the educationLevelList where label contains UPDATED_LABEL
        defaultEducationLevelShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllEducationLevelsByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        educationLevelRepository.saveAndFlush(educationLevel);

        // Get all the educationLevelList where label does not contain DEFAULT_LABEL
        defaultEducationLevelShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the educationLevelList where label does not contain UPDATED_LABEL
        defaultEducationLevelShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEducationLevelShouldBeFound(String filter) throws Exception {
        restEducationLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(educationLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));

        // Check, that the count call also returns 1
        restEducationLevelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEducationLevelShouldNotBeFound(String filter) throws Exception {
        restEducationLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEducationLevelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEducationLevel() throws Exception {
        // Get the educationLevel
        restEducationLevelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEducationLevel() throws Exception {
        // Initialize the database
        educationLevelRepository.saveAndFlush(educationLevel);

        int databaseSizeBeforeUpdate = educationLevelRepository.findAll().size();

        // Update the educationLevel
        EducationLevel updatedEducationLevel = educationLevelRepository.findById(educationLevel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEducationLevel are not directly saved in db
        em.detach(updatedEducationLevel);
        updatedEducationLevel.label(UPDATED_LABEL);

        restEducationLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEducationLevel.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEducationLevel))
            )
            .andExpect(status().isOk());

        // Validate the EducationLevel in the database
        List<EducationLevel> educationLevelList = educationLevelRepository.findAll();
        assertThat(educationLevelList).hasSize(databaseSizeBeforeUpdate);
        EducationLevel testEducationLevel = educationLevelList.get(educationLevelList.size() - 1);
        assertThat(testEducationLevel.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void putNonExistingEducationLevel() throws Exception {
        int databaseSizeBeforeUpdate = educationLevelRepository.findAll().size();
        educationLevel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, educationLevel.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationLevel in the database
        List<EducationLevel> educationLevelList = educationLevelRepository.findAll();
        assertThat(educationLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEducationLevel() throws Exception {
        int databaseSizeBeforeUpdate = educationLevelRepository.findAll().size();
        educationLevel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationLevel in the database
        List<EducationLevel> educationLevelList = educationLevelRepository.findAll();
        assertThat(educationLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEducationLevel() throws Exception {
        int databaseSizeBeforeUpdate = educationLevelRepository.findAll().size();
        educationLevel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationLevelMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationLevel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EducationLevel in the database
        List<EducationLevel> educationLevelList = educationLevelRepository.findAll();
        assertThat(educationLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEducationLevelWithPatch() throws Exception {
        // Initialize the database
        educationLevelRepository.saveAndFlush(educationLevel);

        int databaseSizeBeforeUpdate = educationLevelRepository.findAll().size();

        // Update the educationLevel using partial update
        EducationLevel partialUpdatedEducationLevel = new EducationLevel();
        partialUpdatedEducationLevel.setId(educationLevel.getId());

        restEducationLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEducationLevel.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEducationLevel))
            )
            .andExpect(status().isOk());

        // Validate the EducationLevel in the database
        List<EducationLevel> educationLevelList = educationLevelRepository.findAll();
        assertThat(educationLevelList).hasSize(databaseSizeBeforeUpdate);
        EducationLevel testEducationLevel = educationLevelList.get(educationLevelList.size() - 1);
        assertThat(testEducationLevel.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void fullUpdateEducationLevelWithPatch() throws Exception {
        // Initialize the database
        educationLevelRepository.saveAndFlush(educationLevel);

        int databaseSizeBeforeUpdate = educationLevelRepository.findAll().size();

        // Update the educationLevel using partial update
        EducationLevel partialUpdatedEducationLevel = new EducationLevel();
        partialUpdatedEducationLevel.setId(educationLevel.getId());

        partialUpdatedEducationLevel.label(UPDATED_LABEL);

        restEducationLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEducationLevel.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEducationLevel))
            )
            .andExpect(status().isOk());

        // Validate the EducationLevel in the database
        List<EducationLevel> educationLevelList = educationLevelRepository.findAll();
        assertThat(educationLevelList).hasSize(databaseSizeBeforeUpdate);
        EducationLevel testEducationLevel = educationLevelList.get(educationLevelList.size() - 1);
        assertThat(testEducationLevel.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void patchNonExistingEducationLevel() throws Exception {
        int databaseSizeBeforeUpdate = educationLevelRepository.findAll().size();
        educationLevel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, educationLevel.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(educationLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationLevel in the database
        List<EducationLevel> educationLevelList = educationLevelRepository.findAll();
        assertThat(educationLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEducationLevel() throws Exception {
        int databaseSizeBeforeUpdate = educationLevelRepository.findAll().size();
        educationLevel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(educationLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationLevel in the database
        List<EducationLevel> educationLevelList = educationLevelRepository.findAll();
        assertThat(educationLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEducationLevel() throws Exception {
        int databaseSizeBeforeUpdate = educationLevelRepository.findAll().size();
        educationLevel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationLevelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(educationLevel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EducationLevel in the database
        List<EducationLevel> educationLevelList = educationLevelRepository.findAll();
        assertThat(educationLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEducationLevel() throws Exception {
        // Initialize the database
        educationLevelRepository.saveAndFlush(educationLevel);

        int databaseSizeBeforeDelete = educationLevelRepository.findAll().size();

        // Delete the educationLevel
        restEducationLevelMockMvc
            .perform(delete(ENTITY_API_URL_ID, educationLevel.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EducationLevel> educationLevelList = educationLevelRepository.findAll();
        assertThat(educationLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
