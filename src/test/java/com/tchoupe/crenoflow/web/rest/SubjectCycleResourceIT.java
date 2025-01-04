package com.tchoupe.crenoflow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tchoupe.crenoflow.IntegrationTest;
import com.tchoupe.crenoflow.domain.Cycle;
import com.tchoupe.crenoflow.domain.Subject;
import com.tchoupe.crenoflow.domain.SubjectCycle;
import com.tchoupe.crenoflow.domain.Teacher;
import com.tchoupe.crenoflow.repository.SubjectCycleRepository;
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
 * Integration tests for the {@link SubjectCycleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubjectCycleResourceIT {

    private static final String ENTITY_API_URL = "/api/subject-cycles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubjectCycleRepository subjectCycleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubjectCycleMockMvc;

    private SubjectCycle subjectCycle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubjectCycle createEntity(EntityManager em) {
        SubjectCycle subjectCycle = new SubjectCycle();
        return subjectCycle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubjectCycle createUpdatedEntity(EntityManager em) {
        SubjectCycle subjectCycle = new SubjectCycle();
        return subjectCycle;
    }

    @BeforeEach
    public void initTest() {
        subjectCycle = createEntity(em);
    }

    @Test
    @Transactional
    void createSubjectCycle() throws Exception {
        int databaseSizeBeforeCreate = subjectCycleRepository.findAll().size();
        // Create the SubjectCycle
        restSubjectCycleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subjectCycle))
            )
            .andExpect(status().isCreated());

        // Validate the SubjectCycle in the database
        List<SubjectCycle> subjectCycleList = subjectCycleRepository.findAll();
        assertThat(subjectCycleList).hasSize(databaseSizeBeforeCreate + 1);
        SubjectCycle testSubjectCycle = subjectCycleList.get(subjectCycleList.size() - 1);
    }

    @Test
    @Transactional
    void createSubjectCycleWithExistingId() throws Exception {
        // Create the SubjectCycle with an existing ID
        subjectCycle.setId(1L);

        int databaseSizeBeforeCreate = subjectCycleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubjectCycleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subjectCycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectCycle in the database
        List<SubjectCycle> subjectCycleList = subjectCycleRepository.findAll();
        assertThat(subjectCycleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubjectCycles() throws Exception {
        // Initialize the database
        subjectCycleRepository.saveAndFlush(subjectCycle);

        // Get all the subjectCycleList
        restSubjectCycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subjectCycle.getId().intValue())));
    }

    @Test
    @Transactional
    void getSubjectCycle() throws Exception {
        // Initialize the database
        subjectCycleRepository.saveAndFlush(subjectCycle);

        // Get the subjectCycle
        restSubjectCycleMockMvc
            .perform(get(ENTITY_API_URL_ID, subjectCycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subjectCycle.getId().intValue()));
    }

    @Test
    @Transactional
    void getSubjectCyclesByIdFiltering() throws Exception {
        // Initialize the database
        subjectCycleRepository.saveAndFlush(subjectCycle);

        Long id = subjectCycle.getId();

        defaultSubjectCycleShouldBeFound("id.equals=" + id);
        defaultSubjectCycleShouldNotBeFound("id.notEquals=" + id);

        defaultSubjectCycleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubjectCycleShouldNotBeFound("id.greaterThan=" + id);

        defaultSubjectCycleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubjectCycleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubjectCyclesByTeacherIsEqualToSomething() throws Exception {
        Teacher teacher;
        if (TestUtil.findAll(em, Teacher.class).isEmpty()) {
            subjectCycleRepository.saveAndFlush(subjectCycle);
            teacher = TeacherResourceIT.createEntity(em);
        } else {
            teacher = TestUtil.findAll(em, Teacher.class).get(0);
        }
        em.persist(teacher);
        em.flush();
        subjectCycle.addTeacher(teacher);
        subjectCycleRepository.saveAndFlush(subjectCycle);
        Long teacherId = teacher.getId();
        // Get all the subjectCycleList where teacher equals to teacherId
        defaultSubjectCycleShouldBeFound("teacherId.equals=" + teacherId);

        // Get all the subjectCycleList where teacher equals to (teacherId + 1)
        defaultSubjectCycleShouldNotBeFound("teacherId.equals=" + (teacherId + 1));
    }

    @Test
    @Transactional
    void getAllSubjectCyclesBySubjectIsEqualToSomething() throws Exception {
        Subject subject;
        if (TestUtil.findAll(em, Subject.class).isEmpty()) {
            subjectCycleRepository.saveAndFlush(subjectCycle);
            subject = SubjectResourceIT.createEntity(em);
        } else {
            subject = TestUtil.findAll(em, Subject.class).get(0);
        }
        em.persist(subject);
        em.flush();
        subjectCycle.addSubject(subject);
        subjectCycleRepository.saveAndFlush(subjectCycle);
        Long subjectId = subject.getId();
        // Get all the subjectCycleList where subject equals to subjectId
        defaultSubjectCycleShouldBeFound("subjectId.equals=" + subjectId);

        // Get all the subjectCycleList where subject equals to (subjectId + 1)
        defaultSubjectCycleShouldNotBeFound("subjectId.equals=" + (subjectId + 1));
    }

    @Test
    @Transactional
    void getAllSubjectCyclesByCycleIsEqualToSomething() throws Exception {
        Cycle cycle;
        if (TestUtil.findAll(em, Cycle.class).isEmpty()) {
            subjectCycleRepository.saveAndFlush(subjectCycle);
            cycle = CycleResourceIT.createEntity(em);
        } else {
            cycle = TestUtil.findAll(em, Cycle.class).get(0);
        }
        em.persist(cycle);
        em.flush();
        subjectCycle.addCycle(cycle);
        subjectCycleRepository.saveAndFlush(subjectCycle);
        Long cycleId = cycle.getId();
        // Get all the subjectCycleList where cycle equals to cycleId
        defaultSubjectCycleShouldBeFound("cycleId.equals=" + cycleId);

        // Get all the subjectCycleList where cycle equals to (cycleId + 1)
        defaultSubjectCycleShouldNotBeFound("cycleId.equals=" + (cycleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubjectCycleShouldBeFound(String filter) throws Exception {
        restSubjectCycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subjectCycle.getId().intValue())));

        // Check, that the count call also returns 1
        restSubjectCycleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubjectCycleShouldNotBeFound(String filter) throws Exception {
        restSubjectCycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubjectCycleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubjectCycle() throws Exception {
        // Get the subjectCycle
        restSubjectCycleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubjectCycle() throws Exception {
        // Initialize the database
        subjectCycleRepository.saveAndFlush(subjectCycle);

        int databaseSizeBeforeUpdate = subjectCycleRepository.findAll().size();

        // Update the subjectCycle
        SubjectCycle updatedSubjectCycle = subjectCycleRepository.findById(subjectCycle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubjectCycle are not directly saved in db
        em.detach(updatedSubjectCycle);

        restSubjectCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubjectCycle.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSubjectCycle))
            )
            .andExpect(status().isOk());

        // Validate the SubjectCycle in the database
        List<SubjectCycle> subjectCycleList = subjectCycleRepository.findAll();
        assertThat(subjectCycleList).hasSize(databaseSizeBeforeUpdate);
        SubjectCycle testSubjectCycle = subjectCycleList.get(subjectCycleList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingSubjectCycle() throws Exception {
        int databaseSizeBeforeUpdate = subjectCycleRepository.findAll().size();
        subjectCycle.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubjectCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subjectCycle.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subjectCycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectCycle in the database
        List<SubjectCycle> subjectCycleList = subjectCycleRepository.findAll();
        assertThat(subjectCycleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubjectCycle() throws Exception {
        int databaseSizeBeforeUpdate = subjectCycleRepository.findAll().size();
        subjectCycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subjectCycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectCycle in the database
        List<SubjectCycle> subjectCycleList = subjectCycleRepository.findAll();
        assertThat(subjectCycleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubjectCycle() throws Exception {
        int databaseSizeBeforeUpdate = subjectCycleRepository.findAll().size();
        subjectCycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectCycleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subjectCycle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubjectCycle in the database
        List<SubjectCycle> subjectCycleList = subjectCycleRepository.findAll();
        assertThat(subjectCycleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubjectCycleWithPatch() throws Exception {
        // Initialize the database
        subjectCycleRepository.saveAndFlush(subjectCycle);

        int databaseSizeBeforeUpdate = subjectCycleRepository.findAll().size();

        // Update the subjectCycle using partial update
        SubjectCycle partialUpdatedSubjectCycle = new SubjectCycle();
        partialUpdatedSubjectCycle.setId(subjectCycle.getId());

        restSubjectCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubjectCycle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubjectCycle))
            )
            .andExpect(status().isOk());

        // Validate the SubjectCycle in the database
        List<SubjectCycle> subjectCycleList = subjectCycleRepository.findAll();
        assertThat(subjectCycleList).hasSize(databaseSizeBeforeUpdate);
        SubjectCycle testSubjectCycle = subjectCycleList.get(subjectCycleList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateSubjectCycleWithPatch() throws Exception {
        // Initialize the database
        subjectCycleRepository.saveAndFlush(subjectCycle);

        int databaseSizeBeforeUpdate = subjectCycleRepository.findAll().size();

        // Update the subjectCycle using partial update
        SubjectCycle partialUpdatedSubjectCycle = new SubjectCycle();
        partialUpdatedSubjectCycle.setId(subjectCycle.getId());

        restSubjectCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubjectCycle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubjectCycle))
            )
            .andExpect(status().isOk());

        // Validate the SubjectCycle in the database
        List<SubjectCycle> subjectCycleList = subjectCycleRepository.findAll();
        assertThat(subjectCycleList).hasSize(databaseSizeBeforeUpdate);
        SubjectCycle testSubjectCycle = subjectCycleList.get(subjectCycleList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingSubjectCycle() throws Exception {
        int databaseSizeBeforeUpdate = subjectCycleRepository.findAll().size();
        subjectCycle.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubjectCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subjectCycle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subjectCycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectCycle in the database
        List<SubjectCycle> subjectCycleList = subjectCycleRepository.findAll();
        assertThat(subjectCycleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubjectCycle() throws Exception {
        int databaseSizeBeforeUpdate = subjectCycleRepository.findAll().size();
        subjectCycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subjectCycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubjectCycle in the database
        List<SubjectCycle> subjectCycleList = subjectCycleRepository.findAll();
        assertThat(subjectCycleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubjectCycle() throws Exception {
        int databaseSizeBeforeUpdate = subjectCycleRepository.findAll().size();
        subjectCycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectCycleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subjectCycle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubjectCycle in the database
        List<SubjectCycle> subjectCycleList = subjectCycleRepository.findAll();
        assertThat(subjectCycleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubjectCycle() throws Exception {
        // Initialize the database
        subjectCycleRepository.saveAndFlush(subjectCycle);

        int databaseSizeBeforeDelete = subjectCycleRepository.findAll().size();

        // Delete the subjectCycle
        restSubjectCycleMockMvc
            .perform(delete(ENTITY_API_URL_ID, subjectCycle.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubjectCycle> subjectCycleList = subjectCycleRepository.findAll();
        assertThat(subjectCycleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
