package com.tchoupe.crenoflow.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tchoupe.crenoflow.IntegrationTest;
import com.tchoupe.crenoflow.domain.Availability;
import com.tchoupe.crenoflow.domain.enumeration.CourseFormat;
import com.tchoupe.crenoflow.domain.enumeration.SlotStatus;
import com.tchoupe.crenoflow.repository.AvailabilityRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AvailabilityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AvailabilityResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_START_TIME = "22:22";
    private static final String UPDATED_START_TIME = "02:02";

    private static final String DEFAULT_END_TIME = "10:13";
    private static final String UPDATED_END_TIME = "06:40";

    private static final CourseFormat DEFAULT_FORMAT = CourseFormat.IN_PERSON;
    private static final CourseFormat UPDATED_FORMAT = CourseFormat.ONLINE;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_VIDEO_LINK = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final SlotStatus DEFAULT_STATUS = SlotStatus.AVAILABLE;
    private static final SlotStatus UPDATED_STATUS = SlotStatus.UNAVAILABLE;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/availabilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAvailabilityMockMvc;

    private Availability availability;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Availability createEntity(EntityManager em) {
        Availability availability = new Availability()
            .date(DEFAULT_DATE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .format(DEFAULT_FORMAT)
            .comment(DEFAULT_COMMENT)
            .videoLink(DEFAULT_VIDEO_LINK)
            .address(DEFAULT_ADDRESS)
            .status(DEFAULT_STATUS)
            .creationDate(DEFAULT_CREATION_DATE);
        return availability;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Availability createUpdatedEntity(EntityManager em) {
        Availability availability = new Availability()
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .format(UPDATED_FORMAT)
            .comment(UPDATED_COMMENT)
            .videoLink(UPDATED_VIDEO_LINK)
            .address(UPDATED_ADDRESS)
            .status(UPDATED_STATUS)
            .creationDate(UPDATED_CREATION_DATE);
        return availability;
    }

    @BeforeEach
    public void initTest() {
        availability = createEntity(em);
    }

    @Test
    @Transactional
    void createAvailability() throws Exception {
        int databaseSizeBeforeCreate = availabilityRepository.findAll().size();
        // Create the Availability
        restAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isCreated());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeCreate + 1);
        Availability testAvailability = availabilityList.get(availabilityList.size() - 1);
        assertThat(testAvailability.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAvailability.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testAvailability.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testAvailability.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testAvailability.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testAvailability.getVideoLink()).isEqualTo(DEFAULT_VIDEO_LINK);
        assertThat(testAvailability.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testAvailability.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAvailability.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void createAvailabilityWithExistingId() throws Exception {
        // Create the Availability with an existing ID
        availability.setId(1L);

        int databaseSizeBeforeCreate = availabilityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = availabilityRepository.findAll().size();
        // set the field null
        availability.setDate(null);

        // Create the Availability, which fails.

        restAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = availabilityRepository.findAll().size();
        // set the field null
        availability.setStartTime(null);

        // Create the Availability, which fails.

        restAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = availabilityRepository.findAll().size();
        // set the field null
        availability.setEndTime(null);

        // Create the Availability, which fails.

        restAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFormatIsRequired() throws Exception {
        int databaseSizeBeforeTest = availabilityRepository.findAll().size();
        // set the field null
        availability.setFormat(null);

        // Create the Availability, which fails.

        restAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = availabilityRepository.findAll().size();
        // set the field null
        availability.setStatus(null);

        // Create the Availability, which fails.

        restAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = availabilityRepository.findAll().size();
        // set the field null
        availability.setCreationDate(null);

        // Create the Availability, which fails.

        restAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAvailabilities() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList
        restAvailabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(availability.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].videoLink").value(hasItem(DEFAULT_VIDEO_LINK)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getAvailability() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get the availability
        restAvailabilityMockMvc
            .perform(get(ENTITY_API_URL_ID, availability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(availability.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.videoLink").value(DEFAULT_VIDEO_LINK))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getAvailabilitiesByIdFiltering() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        Long id = availability.getId();

        defaultAvailabilityShouldBeFound("id.equals=" + id);
        defaultAvailabilityShouldNotBeFound("id.notEquals=" + id);

        defaultAvailabilityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAvailabilityShouldNotBeFound("id.greaterThan=" + id);

        defaultAvailabilityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAvailabilityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where date equals to DEFAULT_DATE
        defaultAvailabilityShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the availabilityList where date equals to UPDATED_DATE
        defaultAvailabilityShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where date in DEFAULT_DATE or UPDATED_DATE
        defaultAvailabilityShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the availabilityList where date equals to UPDATED_DATE
        defaultAvailabilityShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where date is not null
        defaultAvailabilityShouldBeFound("date.specified=true");

        // Get all the availabilityList where date is null
        defaultAvailabilityShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where date is greater than or equal to DEFAULT_DATE
        defaultAvailabilityShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the availabilityList where date is greater than or equal to UPDATED_DATE
        defaultAvailabilityShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where date is less than or equal to DEFAULT_DATE
        defaultAvailabilityShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the availabilityList where date is less than or equal to SMALLER_DATE
        defaultAvailabilityShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where date is less than DEFAULT_DATE
        defaultAvailabilityShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the availabilityList where date is less than UPDATED_DATE
        defaultAvailabilityShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where date is greater than DEFAULT_DATE
        defaultAvailabilityShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the availabilityList where date is greater than SMALLER_DATE
        defaultAvailabilityShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where startTime equals to DEFAULT_START_TIME
        defaultAvailabilityShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the availabilityList where startTime equals to UPDATED_START_TIME
        defaultAvailabilityShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultAvailabilityShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the availabilityList where startTime equals to UPDATED_START_TIME
        defaultAvailabilityShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where startTime is not null
        defaultAvailabilityShouldBeFound("startTime.specified=true");

        // Get all the availabilityList where startTime is null
        defaultAvailabilityShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByStartTimeContainsSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where startTime contains DEFAULT_START_TIME
        defaultAvailabilityShouldBeFound("startTime.contains=" + DEFAULT_START_TIME);

        // Get all the availabilityList where startTime contains UPDATED_START_TIME
        defaultAvailabilityShouldNotBeFound("startTime.contains=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByStartTimeNotContainsSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where startTime does not contain DEFAULT_START_TIME
        defaultAvailabilityShouldNotBeFound("startTime.doesNotContain=" + DEFAULT_START_TIME);

        // Get all the availabilityList where startTime does not contain UPDATED_START_TIME
        defaultAvailabilityShouldBeFound("startTime.doesNotContain=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where endTime equals to DEFAULT_END_TIME
        defaultAvailabilityShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the availabilityList where endTime equals to UPDATED_END_TIME
        defaultAvailabilityShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultAvailabilityShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the availabilityList where endTime equals to UPDATED_END_TIME
        defaultAvailabilityShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where endTime is not null
        defaultAvailabilityShouldBeFound("endTime.specified=true");

        // Get all the availabilityList where endTime is null
        defaultAvailabilityShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByEndTimeContainsSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where endTime contains DEFAULT_END_TIME
        defaultAvailabilityShouldBeFound("endTime.contains=" + DEFAULT_END_TIME);

        // Get all the availabilityList where endTime contains UPDATED_END_TIME
        defaultAvailabilityShouldNotBeFound("endTime.contains=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByEndTimeNotContainsSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where endTime does not contain DEFAULT_END_TIME
        defaultAvailabilityShouldNotBeFound("endTime.doesNotContain=" + DEFAULT_END_TIME);

        // Get all the availabilityList where endTime does not contain UPDATED_END_TIME
        defaultAvailabilityShouldBeFound("endTime.doesNotContain=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where format equals to DEFAULT_FORMAT
        defaultAvailabilityShouldBeFound("format.equals=" + DEFAULT_FORMAT);

        // Get all the availabilityList where format equals to UPDATED_FORMAT
        defaultAvailabilityShouldNotBeFound("format.equals=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByFormatIsInShouldWork() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where format in DEFAULT_FORMAT or UPDATED_FORMAT
        defaultAvailabilityShouldBeFound("format.in=" + DEFAULT_FORMAT + "," + UPDATED_FORMAT);

        // Get all the availabilityList where format equals to UPDATED_FORMAT
        defaultAvailabilityShouldNotBeFound("format.in=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where format is not null
        defaultAvailabilityShouldBeFound("format.specified=true");

        // Get all the availabilityList where format is null
        defaultAvailabilityShouldNotBeFound("format.specified=false");
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where comment equals to DEFAULT_COMMENT
        defaultAvailabilityShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the availabilityList where comment equals to UPDATED_COMMENT
        defaultAvailabilityShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultAvailabilityShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the availabilityList where comment equals to UPDATED_COMMENT
        defaultAvailabilityShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where comment is not null
        defaultAvailabilityShouldBeFound("comment.specified=true");

        // Get all the availabilityList where comment is null
        defaultAvailabilityShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByCommentContainsSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where comment contains DEFAULT_COMMENT
        defaultAvailabilityShouldBeFound("comment.contains=" + DEFAULT_COMMENT);

        // Get all the availabilityList where comment contains UPDATED_COMMENT
        defaultAvailabilityShouldNotBeFound("comment.contains=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByCommentNotContainsSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where comment does not contain DEFAULT_COMMENT
        defaultAvailabilityShouldNotBeFound("comment.doesNotContain=" + DEFAULT_COMMENT);

        // Get all the availabilityList where comment does not contain UPDATED_COMMENT
        defaultAvailabilityShouldBeFound("comment.doesNotContain=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByVideoLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where videoLink equals to DEFAULT_VIDEO_LINK
        defaultAvailabilityShouldBeFound("videoLink.equals=" + DEFAULT_VIDEO_LINK);

        // Get all the availabilityList where videoLink equals to UPDATED_VIDEO_LINK
        defaultAvailabilityShouldNotBeFound("videoLink.equals=" + UPDATED_VIDEO_LINK);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByVideoLinkIsInShouldWork() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where videoLink in DEFAULT_VIDEO_LINK or UPDATED_VIDEO_LINK
        defaultAvailabilityShouldBeFound("videoLink.in=" + DEFAULT_VIDEO_LINK + "," + UPDATED_VIDEO_LINK);

        // Get all the availabilityList where videoLink equals to UPDATED_VIDEO_LINK
        defaultAvailabilityShouldNotBeFound("videoLink.in=" + UPDATED_VIDEO_LINK);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByVideoLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where videoLink is not null
        defaultAvailabilityShouldBeFound("videoLink.specified=true");

        // Get all the availabilityList where videoLink is null
        defaultAvailabilityShouldNotBeFound("videoLink.specified=false");
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByVideoLinkContainsSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where videoLink contains DEFAULT_VIDEO_LINK
        defaultAvailabilityShouldBeFound("videoLink.contains=" + DEFAULT_VIDEO_LINK);

        // Get all the availabilityList where videoLink contains UPDATED_VIDEO_LINK
        defaultAvailabilityShouldNotBeFound("videoLink.contains=" + UPDATED_VIDEO_LINK);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByVideoLinkNotContainsSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where videoLink does not contain DEFAULT_VIDEO_LINK
        defaultAvailabilityShouldNotBeFound("videoLink.doesNotContain=" + DEFAULT_VIDEO_LINK);

        // Get all the availabilityList where videoLink does not contain UPDATED_VIDEO_LINK
        defaultAvailabilityShouldBeFound("videoLink.doesNotContain=" + UPDATED_VIDEO_LINK);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where address equals to DEFAULT_ADDRESS
        defaultAvailabilityShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the availabilityList where address equals to UPDATED_ADDRESS
        defaultAvailabilityShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultAvailabilityShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the availabilityList where address equals to UPDATED_ADDRESS
        defaultAvailabilityShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where address is not null
        defaultAvailabilityShouldBeFound("address.specified=true");

        // Get all the availabilityList where address is null
        defaultAvailabilityShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByAddressContainsSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where address contains DEFAULT_ADDRESS
        defaultAvailabilityShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the availabilityList where address contains UPDATED_ADDRESS
        defaultAvailabilityShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where address does not contain DEFAULT_ADDRESS
        defaultAvailabilityShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the availabilityList where address does not contain UPDATED_ADDRESS
        defaultAvailabilityShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where status equals to DEFAULT_STATUS
        defaultAvailabilityShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the availabilityList where status equals to UPDATED_STATUS
        defaultAvailabilityShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultAvailabilityShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the availabilityList where status equals to UPDATED_STATUS
        defaultAvailabilityShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where status is not null
        defaultAvailabilityShouldBeFound("status.specified=true");

        // Get all the availabilityList where status is null
        defaultAvailabilityShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where creationDate equals to DEFAULT_CREATION_DATE
        defaultAvailabilityShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the availabilityList where creationDate equals to UPDATED_CREATION_DATE
        defaultAvailabilityShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultAvailabilityShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the availabilityList where creationDate equals to UPDATED_CREATION_DATE
        defaultAvailabilityShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllAvailabilitiesByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList where creationDate is not null
        defaultAvailabilityShouldBeFound("creationDate.specified=true");

        // Get all the availabilityList where creationDate is null
        defaultAvailabilityShouldNotBeFound("creationDate.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAvailabilityShouldBeFound(String filter) throws Exception {
        restAvailabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(availability.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].videoLink").value(hasItem(DEFAULT_VIDEO_LINK)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));

        // Check, that the count call also returns 1
        restAvailabilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAvailabilityShouldNotBeFound(String filter) throws Exception {
        restAvailabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAvailabilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAvailability() throws Exception {
        // Get the availability
        restAvailabilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAvailability() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();

        // Update the availability
        Availability updatedAvailability = availabilityRepository.findById(availability.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAvailability are not directly saved in db
        em.detach(updatedAvailability);
        updatedAvailability
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .format(UPDATED_FORMAT)
            .comment(UPDATED_COMMENT)
            .videoLink(UPDATED_VIDEO_LINK)
            .address(UPDATED_ADDRESS)
            .status(UPDATED_STATUS)
            .creationDate(UPDATED_CREATION_DATE);

        restAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAvailability.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAvailability))
            )
            .andExpect(status().isOk());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
        Availability testAvailability = availabilityList.get(availabilityList.size() - 1);
        assertThat(testAvailability.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAvailability.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testAvailability.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testAvailability.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testAvailability.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testAvailability.getVideoLink()).isEqualTo(UPDATED_VIDEO_LINK);
        assertThat(testAvailability.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAvailability.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAvailability.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAvailability() throws Exception {
        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();
        availability.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, availability.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAvailability() throws Exception {
        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();
        availability.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAvailability() throws Exception {
        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();
        availability.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAvailabilityWithPatch() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();

        // Update the availability using partial update
        Availability partialUpdatedAvailability = new Availability();
        partialUpdatedAvailability.setId(availability.getId());

        partialUpdatedAvailability
            .date(UPDATED_DATE)
            .endTime(UPDATED_END_TIME)
            .format(UPDATED_FORMAT)
            .comment(UPDATED_COMMENT)
            .status(UPDATED_STATUS)
            .creationDate(UPDATED_CREATION_DATE);

        restAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvailability.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvailability))
            )
            .andExpect(status().isOk());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
        Availability testAvailability = availabilityList.get(availabilityList.size() - 1);
        assertThat(testAvailability.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAvailability.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testAvailability.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testAvailability.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testAvailability.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testAvailability.getVideoLink()).isEqualTo(DEFAULT_VIDEO_LINK);
        assertThat(testAvailability.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testAvailability.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAvailability.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAvailabilityWithPatch() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();

        // Update the availability using partial update
        Availability partialUpdatedAvailability = new Availability();
        partialUpdatedAvailability.setId(availability.getId());

        partialUpdatedAvailability
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .format(UPDATED_FORMAT)
            .comment(UPDATED_COMMENT)
            .videoLink(UPDATED_VIDEO_LINK)
            .address(UPDATED_ADDRESS)
            .status(UPDATED_STATUS)
            .creationDate(UPDATED_CREATION_DATE);

        restAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvailability.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvailability))
            )
            .andExpect(status().isOk());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
        Availability testAvailability = availabilityList.get(availabilityList.size() - 1);
        assertThat(testAvailability.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAvailability.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testAvailability.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testAvailability.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testAvailability.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testAvailability.getVideoLink()).isEqualTo(UPDATED_VIDEO_LINK);
        assertThat(testAvailability.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAvailability.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAvailability.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAvailability() throws Exception {
        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();
        availability.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, availability.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAvailability() throws Exception {
        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();
        availability.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAvailability() throws Exception {
        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();
        availability.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAvailability() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        int databaseSizeBeforeDelete = availabilityRepository.findAll().size();

        // Delete the availability
        restAvailabilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, availability.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
