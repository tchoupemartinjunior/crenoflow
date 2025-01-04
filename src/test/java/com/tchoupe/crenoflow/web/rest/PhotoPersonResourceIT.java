package com.tchoupe.crenoflow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tchoupe.crenoflow.IntegrationTest;
import com.tchoupe.crenoflow.domain.Person;
import com.tchoupe.crenoflow.domain.PhotoPerson;
import com.tchoupe.crenoflow.repository.PhotoPersonRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link PhotoPersonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhotoPersonResourceIT {

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/photo-people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PhotoPersonRepository photoPersonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhotoPersonMockMvc;

    private PhotoPerson photoPerson;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhotoPerson createEntity(EntityManager em) {
        PhotoPerson photoPerson = new PhotoPerson().photo(DEFAULT_PHOTO).photoContentType(DEFAULT_PHOTO_CONTENT_TYPE);
        return photoPerson;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhotoPerson createUpdatedEntity(EntityManager em) {
        PhotoPerson photoPerson = new PhotoPerson().photo(UPDATED_PHOTO).photoContentType(UPDATED_PHOTO_CONTENT_TYPE);
        return photoPerson;
    }

    @BeforeEach
    public void initTest() {
        photoPerson = createEntity(em);
    }

    @Test
    @Transactional
    void createPhotoPerson() throws Exception {
        int databaseSizeBeforeCreate = photoPersonRepository.findAll().size();
        // Create the PhotoPerson
        restPhotoPersonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photoPerson))
            )
            .andExpect(status().isCreated());

        // Validate the PhotoPerson in the database
        List<PhotoPerson> photoPersonList = photoPersonRepository.findAll();
        assertThat(photoPersonList).hasSize(databaseSizeBeforeCreate + 1);
        PhotoPerson testPhotoPerson = photoPersonList.get(photoPersonList.size() - 1);
        assertThat(testPhotoPerson.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testPhotoPerson.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createPhotoPersonWithExistingId() throws Exception {
        // Create the PhotoPerson with an existing ID
        photoPerson.setId(1L);

        int databaseSizeBeforeCreate = photoPersonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhotoPersonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photoPerson))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhotoPerson in the database
        List<PhotoPerson> photoPersonList = photoPersonRepository.findAll();
        assertThat(photoPersonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPhotoPeople() throws Exception {
        // Initialize the database
        photoPersonRepository.saveAndFlush(photoPerson);

        // Get all the photoPersonList
        restPhotoPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(photoPerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))));
    }

    @Test
    @Transactional
    void getPhotoPerson() throws Exception {
        // Initialize the database
        photoPersonRepository.saveAndFlush(photoPerson);

        // Get the photoPerson
        restPhotoPersonMockMvc
            .perform(get(ENTITY_API_URL_ID, photoPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(photoPerson.getId().intValue()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)));
    }

    @Test
    @Transactional
    void getPhotoPeopleByIdFiltering() throws Exception {
        // Initialize the database
        photoPersonRepository.saveAndFlush(photoPerson);

        Long id = photoPerson.getId();

        defaultPhotoPersonShouldBeFound("id.equals=" + id);
        defaultPhotoPersonShouldNotBeFound("id.notEquals=" + id);

        defaultPhotoPersonShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPhotoPersonShouldNotBeFound("id.greaterThan=" + id);

        defaultPhotoPersonShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPhotoPersonShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPhotoPeopleByPersonIsEqualToSomething() throws Exception {
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            photoPersonRepository.saveAndFlush(photoPerson);
            person = PersonResourceIT.createEntity(em);
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(person);
        em.flush();
        photoPerson.setPerson(person);
        photoPersonRepository.saveAndFlush(photoPerson);
        Long personId = person.getId();
        // Get all the photoPersonList where person equals to personId
        defaultPhotoPersonShouldBeFound("personId.equals=" + personId);

        // Get all the photoPersonList where person equals to (personId + 1)
        defaultPhotoPersonShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPhotoPersonShouldBeFound(String filter) throws Exception {
        restPhotoPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(photoPerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))));

        // Check, that the count call also returns 1
        restPhotoPersonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPhotoPersonShouldNotBeFound(String filter) throws Exception {
        restPhotoPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPhotoPersonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPhotoPerson() throws Exception {
        // Get the photoPerson
        restPhotoPersonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPhotoPerson() throws Exception {
        // Initialize the database
        photoPersonRepository.saveAndFlush(photoPerson);

        int databaseSizeBeforeUpdate = photoPersonRepository.findAll().size();

        // Update the photoPerson
        PhotoPerson updatedPhotoPerson = photoPersonRepository.findById(photoPerson.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPhotoPerson are not directly saved in db
        em.detach(updatedPhotoPerson);
        updatedPhotoPerson.photo(UPDATED_PHOTO).photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restPhotoPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPhotoPerson.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPhotoPerson))
            )
            .andExpect(status().isOk());

        // Validate the PhotoPerson in the database
        List<PhotoPerson> photoPersonList = photoPersonRepository.findAll();
        assertThat(photoPersonList).hasSize(databaseSizeBeforeUpdate);
        PhotoPerson testPhotoPerson = photoPersonList.get(photoPersonList.size() - 1);
        assertThat(testPhotoPerson.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testPhotoPerson.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingPhotoPerson() throws Exception {
        int databaseSizeBeforeUpdate = photoPersonRepository.findAll().size();
        photoPerson.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhotoPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, photoPerson.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photoPerson))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhotoPerson in the database
        List<PhotoPerson> photoPersonList = photoPersonRepository.findAll();
        assertThat(photoPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhotoPerson() throws Exception {
        int databaseSizeBeforeUpdate = photoPersonRepository.findAll().size();
        photoPerson.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photoPerson))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhotoPerson in the database
        List<PhotoPerson> photoPersonList = photoPersonRepository.findAll();
        assertThat(photoPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhotoPerson() throws Exception {
        int databaseSizeBeforeUpdate = photoPersonRepository.findAll().size();
        photoPerson.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoPersonMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photoPerson))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhotoPerson in the database
        List<PhotoPerson> photoPersonList = photoPersonRepository.findAll();
        assertThat(photoPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePhotoPersonWithPatch() throws Exception {
        // Initialize the database
        photoPersonRepository.saveAndFlush(photoPerson);

        int databaseSizeBeforeUpdate = photoPersonRepository.findAll().size();

        // Update the photoPerson using partial update
        PhotoPerson partialUpdatedPhotoPerson = new PhotoPerson();
        partialUpdatedPhotoPerson.setId(photoPerson.getId());

        restPhotoPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhotoPerson.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhotoPerson))
            )
            .andExpect(status().isOk());

        // Validate the PhotoPerson in the database
        List<PhotoPerson> photoPersonList = photoPersonRepository.findAll();
        assertThat(photoPersonList).hasSize(databaseSizeBeforeUpdate);
        PhotoPerson testPhotoPerson = photoPersonList.get(photoPersonList.size() - 1);
        assertThat(testPhotoPerson.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testPhotoPerson.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePhotoPersonWithPatch() throws Exception {
        // Initialize the database
        photoPersonRepository.saveAndFlush(photoPerson);

        int databaseSizeBeforeUpdate = photoPersonRepository.findAll().size();

        // Update the photoPerson using partial update
        PhotoPerson partialUpdatedPhotoPerson = new PhotoPerson();
        partialUpdatedPhotoPerson.setId(photoPerson.getId());

        partialUpdatedPhotoPerson.photo(UPDATED_PHOTO).photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restPhotoPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhotoPerson.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhotoPerson))
            )
            .andExpect(status().isOk());

        // Validate the PhotoPerson in the database
        List<PhotoPerson> photoPersonList = photoPersonRepository.findAll();
        assertThat(photoPersonList).hasSize(databaseSizeBeforeUpdate);
        PhotoPerson testPhotoPerson = photoPersonList.get(photoPersonList.size() - 1);
        assertThat(testPhotoPerson.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testPhotoPerson.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPhotoPerson() throws Exception {
        int databaseSizeBeforeUpdate = photoPersonRepository.findAll().size();
        photoPerson.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhotoPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, photoPerson.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(photoPerson))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhotoPerson in the database
        List<PhotoPerson> photoPersonList = photoPersonRepository.findAll();
        assertThat(photoPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhotoPerson() throws Exception {
        int databaseSizeBeforeUpdate = photoPersonRepository.findAll().size();
        photoPerson.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(photoPerson))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhotoPerson in the database
        List<PhotoPerson> photoPersonList = photoPersonRepository.findAll();
        assertThat(photoPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhotoPerson() throws Exception {
        int databaseSizeBeforeUpdate = photoPersonRepository.findAll().size();
        photoPerson.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoPersonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(photoPerson))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhotoPerson in the database
        List<PhotoPerson> photoPersonList = photoPersonRepository.findAll();
        assertThat(photoPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePhotoPerson() throws Exception {
        // Initialize the database
        photoPersonRepository.saveAndFlush(photoPerson);

        int databaseSizeBeforeDelete = photoPersonRepository.findAll().size();

        // Delete the photoPerson
        restPhotoPersonMockMvc
            .perform(delete(ENTITY_API_URL_ID, photoPerson.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PhotoPerson> photoPersonList = photoPersonRepository.findAll();
        assertThat(photoPersonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
