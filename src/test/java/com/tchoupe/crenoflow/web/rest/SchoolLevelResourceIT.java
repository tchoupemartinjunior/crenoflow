package com.tchoupe.crenoflow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tchoupe.crenoflow.IntegrationTest;
import com.tchoupe.crenoflow.domain.Cycle;
import com.tchoupe.crenoflow.domain.SchoolLevel;
import com.tchoupe.crenoflow.repository.SchoolLevelRepository;
import com.tchoupe.crenoflow.service.SchoolLevelService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SchoolLevelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SchoolLevelResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/school-levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchoolLevelRepository schoolLevelRepository;

    @Mock
    private SchoolLevelRepository schoolLevelRepositoryMock;

    @Mock
    private SchoolLevelService schoolLevelServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolLevelMockMvc;

    private SchoolLevel schoolLevel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolLevel createEntity(EntityManager em) {
        SchoolLevel schoolLevel = new SchoolLevel().label(DEFAULT_LABEL);
        return schoolLevel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolLevel createUpdatedEntity(EntityManager em) {
        SchoolLevel schoolLevel = new SchoolLevel().label(UPDATED_LABEL);
        return schoolLevel;
    }

    @BeforeEach
    public void initTest() {
        schoolLevel = createEntity(em);
    }

    @Test
    @Transactional
    void createSchoolLevel() throws Exception {
        int databaseSizeBeforeCreate = schoolLevelRepository.findAll().size();
        // Create the SchoolLevel
        restSchoolLevelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolLevel))
            )
            .andExpect(status().isCreated());

        // Validate the SchoolLevel in the database
        List<SchoolLevel> schoolLevelList = schoolLevelRepository.findAll();
        assertThat(schoolLevelList).hasSize(databaseSizeBeforeCreate + 1);
        SchoolLevel testSchoolLevel = schoolLevelList.get(schoolLevelList.size() - 1);
        assertThat(testSchoolLevel.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void createSchoolLevelWithExistingId() throws Exception {
        // Create the SchoolLevel with an existing ID
        schoolLevel.setId(1L);

        int databaseSizeBeforeCreate = schoolLevelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolLevelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolLevel in the database
        List<SchoolLevel> schoolLevelList = schoolLevelRepository.findAll();
        assertThat(schoolLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = schoolLevelRepository.findAll().size();
        // set the field null
        schoolLevel.setLabel(null);

        // Create the SchoolLevel, which fails.

        restSchoolLevelMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolLevel))
            )
            .andExpect(status().isBadRequest());

        List<SchoolLevel> schoolLevelList = schoolLevelRepository.findAll();
        assertThat(schoolLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchoolLevels() throws Exception {
        // Initialize the database
        schoolLevelRepository.saveAndFlush(schoolLevel);

        // Get all the schoolLevelList
        restSchoolLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolLevelsWithEagerRelationshipsIsEnabled() throws Exception {
        when(schoolLevelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolLevelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(schoolLevelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchoolLevelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(schoolLevelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSchoolLevelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(schoolLevelRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSchoolLevel() throws Exception {
        // Initialize the database
        schoolLevelRepository.saveAndFlush(schoolLevel);

        // Get the schoolLevel
        restSchoolLevelMockMvc
            .perform(get(ENTITY_API_URL_ID, schoolLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schoolLevel.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }

    @Test
    @Transactional
    void getSchoolLevelsByIdFiltering() throws Exception {
        // Initialize the database
        schoolLevelRepository.saveAndFlush(schoolLevel);

        Long id = schoolLevel.getId();

        defaultSchoolLevelShouldBeFound("id.equals=" + id);
        defaultSchoolLevelShouldNotBeFound("id.notEquals=" + id);

        defaultSchoolLevelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSchoolLevelShouldNotBeFound("id.greaterThan=" + id);

        defaultSchoolLevelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSchoolLevelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchoolLevelsByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        schoolLevelRepository.saveAndFlush(schoolLevel);

        // Get all the schoolLevelList where label equals to DEFAULT_LABEL
        defaultSchoolLevelShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the schoolLevelList where label equals to UPDATED_LABEL
        defaultSchoolLevelShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllSchoolLevelsByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        schoolLevelRepository.saveAndFlush(schoolLevel);

        // Get all the schoolLevelList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultSchoolLevelShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the schoolLevelList where label equals to UPDATED_LABEL
        defaultSchoolLevelShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllSchoolLevelsByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        schoolLevelRepository.saveAndFlush(schoolLevel);

        // Get all the schoolLevelList where label is not null
        defaultSchoolLevelShouldBeFound("label.specified=true");

        // Get all the schoolLevelList where label is null
        defaultSchoolLevelShouldNotBeFound("label.specified=false");
    }

    @Test
    @Transactional
    void getAllSchoolLevelsByLabelContainsSomething() throws Exception {
        // Initialize the database
        schoolLevelRepository.saveAndFlush(schoolLevel);

        // Get all the schoolLevelList where label contains DEFAULT_LABEL
        defaultSchoolLevelShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the schoolLevelList where label contains UPDATED_LABEL
        defaultSchoolLevelShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllSchoolLevelsByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        schoolLevelRepository.saveAndFlush(schoolLevel);

        // Get all the schoolLevelList where label does not contain DEFAULT_LABEL
        defaultSchoolLevelShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the schoolLevelList where label does not contain UPDATED_LABEL
        defaultSchoolLevelShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllSchoolLevelsByCycleIsEqualToSomething() throws Exception {
        Cycle cycle;
        if (TestUtil.findAll(em, Cycle.class).isEmpty()) {
            schoolLevelRepository.saveAndFlush(schoolLevel);
            cycle = CycleResourceIT.createEntity(em);
        } else {
            cycle = TestUtil.findAll(em, Cycle.class).get(0);
        }
        em.persist(cycle);
        em.flush();
        schoolLevel.setCycle(cycle);
        schoolLevelRepository.saveAndFlush(schoolLevel);
        Long cycleId = cycle.getId();
        // Get all the schoolLevelList where cycle equals to cycleId
        defaultSchoolLevelShouldBeFound("cycleId.equals=" + cycleId);

        // Get all the schoolLevelList where cycle equals to (cycleId + 1)
        defaultSchoolLevelShouldNotBeFound("cycleId.equals=" + (cycleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSchoolLevelShouldBeFound(String filter) throws Exception {
        restSchoolLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));

        // Check, that the count call also returns 1
        restSchoolLevelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSchoolLevelShouldNotBeFound(String filter) throws Exception {
        restSchoolLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSchoolLevelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchoolLevel() throws Exception {
        // Get the schoolLevel
        restSchoolLevelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSchoolLevel() throws Exception {
        // Initialize the database
        schoolLevelRepository.saveAndFlush(schoolLevel);

        int databaseSizeBeforeUpdate = schoolLevelRepository.findAll().size();

        // Update the schoolLevel
        SchoolLevel updatedSchoolLevel = schoolLevelRepository.findById(schoolLevel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSchoolLevel are not directly saved in db
        em.detach(updatedSchoolLevel);
        updatedSchoolLevel.label(UPDATED_LABEL);

        restSchoolLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSchoolLevel.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSchoolLevel))
            )
            .andExpect(status().isOk());

        // Validate the SchoolLevel in the database
        List<SchoolLevel> schoolLevelList = schoolLevelRepository.findAll();
        assertThat(schoolLevelList).hasSize(databaseSizeBeforeUpdate);
        SchoolLevel testSchoolLevel = schoolLevelList.get(schoolLevelList.size() - 1);
        assertThat(testSchoolLevel.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void putNonExistingSchoolLevel() throws Exception {
        int databaseSizeBeforeUpdate = schoolLevelRepository.findAll().size();
        schoolLevel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolLevel.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolLevel in the database
        List<SchoolLevel> schoolLevelList = schoolLevelRepository.findAll();
        assertThat(schoolLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchoolLevel() throws Exception {
        int databaseSizeBeforeUpdate = schoolLevelRepository.findAll().size();
        schoolLevel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolLevel in the database
        List<SchoolLevel> schoolLevelList = schoolLevelRepository.findAll();
        assertThat(schoolLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchoolLevel() throws Exception {
        int databaseSizeBeforeUpdate = schoolLevelRepository.findAll().size();
        schoolLevel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolLevelMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schoolLevel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolLevel in the database
        List<SchoolLevel> schoolLevelList = schoolLevelRepository.findAll();
        assertThat(schoolLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchoolLevelWithPatch() throws Exception {
        // Initialize the database
        schoolLevelRepository.saveAndFlush(schoolLevel);

        int databaseSizeBeforeUpdate = schoolLevelRepository.findAll().size();

        // Update the schoolLevel using partial update
        SchoolLevel partialUpdatedSchoolLevel = new SchoolLevel();
        partialUpdatedSchoolLevel.setId(schoolLevel.getId());

        partialUpdatedSchoolLevel.label(UPDATED_LABEL);

        restSchoolLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolLevel.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolLevel))
            )
            .andExpect(status().isOk());

        // Validate the SchoolLevel in the database
        List<SchoolLevel> schoolLevelList = schoolLevelRepository.findAll();
        assertThat(schoolLevelList).hasSize(databaseSizeBeforeUpdate);
        SchoolLevel testSchoolLevel = schoolLevelList.get(schoolLevelList.size() - 1);
        assertThat(testSchoolLevel.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void fullUpdateSchoolLevelWithPatch() throws Exception {
        // Initialize the database
        schoolLevelRepository.saveAndFlush(schoolLevel);

        int databaseSizeBeforeUpdate = schoolLevelRepository.findAll().size();

        // Update the schoolLevel using partial update
        SchoolLevel partialUpdatedSchoolLevel = new SchoolLevel();
        partialUpdatedSchoolLevel.setId(schoolLevel.getId());

        partialUpdatedSchoolLevel.label(UPDATED_LABEL);

        restSchoolLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolLevel.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchoolLevel))
            )
            .andExpect(status().isOk());

        // Validate the SchoolLevel in the database
        List<SchoolLevel> schoolLevelList = schoolLevelRepository.findAll();
        assertThat(schoolLevelList).hasSize(databaseSizeBeforeUpdate);
        SchoolLevel testSchoolLevel = schoolLevelList.get(schoolLevelList.size() - 1);
        assertThat(testSchoolLevel.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void patchNonExistingSchoolLevel() throws Exception {
        int databaseSizeBeforeUpdate = schoolLevelRepository.findAll().size();
        schoolLevel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolLevel.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolLevel in the database
        List<SchoolLevel> schoolLevelList = schoolLevelRepository.findAll();
        assertThat(schoolLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchoolLevel() throws Exception {
        int databaseSizeBeforeUpdate = schoolLevelRepository.findAll().size();
        schoolLevel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolLevel in the database
        List<SchoolLevel> schoolLevelList = schoolLevelRepository.findAll();
        assertThat(schoolLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchoolLevel() throws Exception {
        int databaseSizeBeforeUpdate = schoolLevelRepository.findAll().size();
        schoolLevel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolLevelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schoolLevel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolLevel in the database
        List<SchoolLevel> schoolLevelList = schoolLevelRepository.findAll();
        assertThat(schoolLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchoolLevel() throws Exception {
        // Initialize the database
        schoolLevelRepository.saveAndFlush(schoolLevel);

        int databaseSizeBeforeDelete = schoolLevelRepository.findAll().size();

        // Delete the schoolLevel
        restSchoolLevelMockMvc
            .perform(delete(ENTITY_API_URL_ID, schoolLevel.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchoolLevel> schoolLevelList = schoolLevelRepository.findAll();
        assertThat(schoolLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
