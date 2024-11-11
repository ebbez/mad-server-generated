package nl.hanze.se4.automaat.web.rest;

import static nl.hanze.se4.automaat.domain.InspectionAsserts.*;
import static nl.hanze.se4.automaat.web.rest.TestUtil.createUpdateProxyForBean;
import static nl.hanze.se4.automaat.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import nl.hanze.se4.automaat.IntegrationTest;
import nl.hanze.se4.automaat.domain.Car;
import nl.hanze.se4.automaat.domain.Employee;
import nl.hanze.se4.automaat.domain.Inspection;
import nl.hanze.se4.automaat.domain.Rental;
import nl.hanze.se4.automaat.repository.InspectionRepository;
import nl.hanze.se4.automaat.service.InspectionService;
import org.junit.jupiter.api.AfterEach;
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
 * Integration tests for the {@link InspectionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InspectionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_ODOMETER = 1L;
    private static final Long UPDATED_ODOMETER = 2L;
    private static final Long SMALLER_ODOMETER = 1L - 1L;

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_COMPLETED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_COMPLETED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_COMPLETED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/inspections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InspectionRepository inspectionRepository;

    @Mock
    private InspectionRepository inspectionRepositoryMock;

    @Mock
    private InspectionService inspectionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInspectionMockMvc;

    private Inspection inspection;

    private Inspection insertedInspection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspection createEntity() {
        return new Inspection()
            .code(DEFAULT_CODE)
            .odometer(DEFAULT_ODOMETER)
            .result(DEFAULT_RESULT)
            .description(DEFAULT_DESCRIPTION)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .completed(DEFAULT_COMPLETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspection createUpdatedEntity() {
        return new Inspection()
            .code(UPDATED_CODE)
            .odometer(UPDATED_ODOMETER)
            .result(UPDATED_RESULT)
            .description(UPDATED_DESCRIPTION)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .completed(UPDATED_COMPLETED);
    }

    @BeforeEach
    public void initTest() {
        inspection = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInspection != null) {
            inspectionRepository.delete(insertedInspection);
            insertedInspection = null;
        }
    }

    @Test
    @Transactional
    void createInspection() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Inspection
        var returnedInspection = om.readValue(
            restInspectionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inspection)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Inspection.class
        );

        // Validate the Inspection in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInspectionUpdatableFieldsEquals(returnedInspection, getPersistedInspection(returnedInspection));

        insertedInspection = returnedInspection;
    }

    @Test
    @Transactional
    void createInspectionWithExistingId() throws Exception {
        // Create the Inspection with an existing ID
        inspection.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInspectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inspection)))
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInspections() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList
        restInspectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inspection.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].odometer").value(hasItem(DEFAULT_ODOMETER.intValue())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(sameInstant(DEFAULT_COMPLETED))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInspectionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(inspectionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInspectionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inspectionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInspectionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inspectionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInspectionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(inspectionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInspection() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get the inspection
        restInspectionMockMvc
            .perform(get(ENTITY_API_URL_ID, inspection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inspection.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.odometer").value(DEFAULT_ODOMETER.intValue()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64.getEncoder().encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.completed").value(sameInstant(DEFAULT_COMPLETED)));
    }

    @Test
    @Transactional
    void getInspectionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        Long id = inspection.getId();

        defaultInspectionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInspectionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInspectionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInspectionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where code equals to
        defaultInspectionFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllInspectionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where code in
        defaultInspectionFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllInspectionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where code is not null
        defaultInspectionFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllInspectionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where code contains
        defaultInspectionFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllInspectionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where code does not contain
        defaultInspectionFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllInspectionsByOdometerIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where odometer equals to
        defaultInspectionFiltering("odometer.equals=" + DEFAULT_ODOMETER, "odometer.equals=" + UPDATED_ODOMETER);
    }

    @Test
    @Transactional
    void getAllInspectionsByOdometerIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where odometer in
        defaultInspectionFiltering("odometer.in=" + DEFAULT_ODOMETER + "," + UPDATED_ODOMETER, "odometer.in=" + UPDATED_ODOMETER);
    }

    @Test
    @Transactional
    void getAllInspectionsByOdometerIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where odometer is not null
        defaultInspectionFiltering("odometer.specified=true", "odometer.specified=false");
    }

    @Test
    @Transactional
    void getAllInspectionsByOdometerIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where odometer is greater than or equal to
        defaultInspectionFiltering("odometer.greaterThanOrEqual=" + DEFAULT_ODOMETER, "odometer.greaterThanOrEqual=" + UPDATED_ODOMETER);
    }

    @Test
    @Transactional
    void getAllInspectionsByOdometerIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where odometer is less than or equal to
        defaultInspectionFiltering("odometer.lessThanOrEqual=" + DEFAULT_ODOMETER, "odometer.lessThanOrEqual=" + SMALLER_ODOMETER);
    }

    @Test
    @Transactional
    void getAllInspectionsByOdometerIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where odometer is less than
        defaultInspectionFiltering("odometer.lessThan=" + UPDATED_ODOMETER, "odometer.lessThan=" + DEFAULT_ODOMETER);
    }

    @Test
    @Transactional
    void getAllInspectionsByOdometerIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where odometer is greater than
        defaultInspectionFiltering("odometer.greaterThan=" + SMALLER_ODOMETER, "odometer.greaterThan=" + DEFAULT_ODOMETER);
    }

    @Test
    @Transactional
    void getAllInspectionsByResultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where result equals to
        defaultInspectionFiltering("result.equals=" + DEFAULT_RESULT, "result.equals=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllInspectionsByResultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where result in
        defaultInspectionFiltering("result.in=" + DEFAULT_RESULT + "," + UPDATED_RESULT, "result.in=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllInspectionsByResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where result is not null
        defaultInspectionFiltering("result.specified=true", "result.specified=false");
    }

    @Test
    @Transactional
    void getAllInspectionsByResultContainsSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where result contains
        defaultInspectionFiltering("result.contains=" + DEFAULT_RESULT, "result.contains=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllInspectionsByResultNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where result does not contain
        defaultInspectionFiltering("result.doesNotContain=" + UPDATED_RESULT, "result.doesNotContain=" + DEFAULT_RESULT);
    }

    @Test
    @Transactional
    void getAllInspectionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where description equals to
        defaultInspectionFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInspectionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where description in
        defaultInspectionFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInspectionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where description is not null
        defaultInspectionFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllInspectionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where description contains
        defaultInspectionFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInspectionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where description does not contain
        defaultInspectionFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInspectionsByCompletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where completed equals to
        defaultInspectionFiltering("completed.equals=" + DEFAULT_COMPLETED, "completed.equals=" + UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    void getAllInspectionsByCompletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where completed in
        defaultInspectionFiltering("completed.in=" + DEFAULT_COMPLETED + "," + UPDATED_COMPLETED, "completed.in=" + UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    void getAllInspectionsByCompletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where completed is not null
        defaultInspectionFiltering("completed.specified=true", "completed.specified=false");
    }

    @Test
    @Transactional
    void getAllInspectionsByCompletedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where completed is greater than or equal to
        defaultInspectionFiltering(
            "completed.greaterThanOrEqual=" + DEFAULT_COMPLETED,
            "completed.greaterThanOrEqual=" + UPDATED_COMPLETED
        );
    }

    @Test
    @Transactional
    void getAllInspectionsByCompletedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where completed is less than or equal to
        defaultInspectionFiltering("completed.lessThanOrEqual=" + DEFAULT_COMPLETED, "completed.lessThanOrEqual=" + SMALLER_COMPLETED);
    }

    @Test
    @Transactional
    void getAllInspectionsByCompletedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where completed is less than
        defaultInspectionFiltering("completed.lessThan=" + UPDATED_COMPLETED, "completed.lessThan=" + DEFAULT_COMPLETED);
    }

    @Test
    @Transactional
    void getAllInspectionsByCompletedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        // Get all the inspectionList where completed is greater than
        defaultInspectionFiltering("completed.greaterThan=" + SMALLER_COMPLETED, "completed.greaterThan=" + DEFAULT_COMPLETED);
    }

    @Test
    @Transactional
    void getAllInspectionsByCarIsEqualToSomething() throws Exception {
        Car car;
        if (TestUtil.findAll(em, Car.class).isEmpty()) {
            inspectionRepository.saveAndFlush(inspection);
            car = CarResourceIT.createEntity();
        } else {
            car = TestUtil.findAll(em, Car.class).get(0);
        }
        em.persist(car);
        em.flush();
        inspection.setCar(car);
        inspectionRepository.saveAndFlush(inspection);
        Long carId = car.getId();
        // Get all the inspectionList where car equals to carId
        defaultInspectionShouldBeFound("carId.equals=" + carId);

        // Get all the inspectionList where car equals to (carId + 1)
        defaultInspectionShouldNotBeFound("carId.equals=" + (carId + 1));
    }

    @Test
    @Transactional
    void getAllInspectionsByEmployeeIsEqualToSomething() throws Exception {
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            inspectionRepository.saveAndFlush(inspection);
            employee = EmployeeResourceIT.createEntity();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        inspection.setEmployee(employee);
        inspectionRepository.saveAndFlush(inspection);
        Long employeeId = employee.getId();
        // Get all the inspectionList where employee equals to employeeId
        defaultInspectionShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the inspectionList where employee equals to (employeeId + 1)
        defaultInspectionShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    @Test
    @Transactional
    void getAllInspectionsByRentalIsEqualToSomething() throws Exception {
        Rental rental;
        if (TestUtil.findAll(em, Rental.class).isEmpty()) {
            inspectionRepository.saveAndFlush(inspection);
            rental = RentalResourceIT.createEntity();
        } else {
            rental = TestUtil.findAll(em, Rental.class).get(0);
        }
        em.persist(rental);
        em.flush();
        inspection.setRental(rental);
        inspectionRepository.saveAndFlush(inspection);
        Long rentalId = rental.getId();
        // Get all the inspectionList where rental equals to rentalId
        defaultInspectionShouldBeFound("rentalId.equals=" + rentalId);

        // Get all the inspectionList where rental equals to (rentalId + 1)
        defaultInspectionShouldNotBeFound("rentalId.equals=" + (rentalId + 1));
    }

    private void defaultInspectionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInspectionShouldBeFound(shouldBeFound);
        defaultInspectionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInspectionShouldBeFound(String filter) throws Exception {
        restInspectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inspection.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].odometer").value(hasItem(DEFAULT_ODOMETER.intValue())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(sameInstant(DEFAULT_COMPLETED))));

        // Check, that the count call also returns 1
        restInspectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInspectionShouldNotBeFound(String filter) throws Exception {
        restInspectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInspectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInspection() throws Exception {
        // Get the inspection
        restInspectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInspection() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inspection
        Inspection updatedInspection = inspectionRepository.findById(inspection.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInspection are not directly saved in db
        em.detach(updatedInspection);
        updatedInspection
            .code(UPDATED_CODE)
            .odometer(UPDATED_ODOMETER)
            .result(UPDATED_RESULT)
            .description(UPDATED_DESCRIPTION)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .completed(UPDATED_COMPLETED);

        restInspectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInspection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInspection))
            )
            .andExpect(status().isOk());

        // Validate the Inspection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInspectionToMatchAllProperties(updatedInspection);
    }

    @Test
    @Transactional
    void putNonExistingInspection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inspection.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inspection.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inspection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInspection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inspection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inspection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInspection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inspection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inspection)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inspection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInspectionWithPatch() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inspection using partial update
        Inspection partialUpdatedInspection = new Inspection();
        partialUpdatedInspection.setId(inspection.getId());

        partialUpdatedInspection.odometer(UPDATED_ODOMETER).description(UPDATED_DESCRIPTION).completed(UPDATED_COMPLETED);

        restInspectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInspection))
            )
            .andExpect(status().isOk());

        // Validate the Inspection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInspectionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInspection, inspection),
            getPersistedInspection(inspection)
        );
    }

    @Test
    @Transactional
    void fullUpdateInspectionWithPatch() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inspection using partial update
        Inspection partialUpdatedInspection = new Inspection();
        partialUpdatedInspection.setId(inspection.getId());

        partialUpdatedInspection
            .code(UPDATED_CODE)
            .odometer(UPDATED_ODOMETER)
            .result(UPDATED_RESULT)
            .description(UPDATED_DESCRIPTION)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .completed(UPDATED_COMPLETED);

        restInspectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInspection))
            )
            .andExpect(status().isOk());

        // Validate the Inspection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInspectionUpdatableFieldsEquals(partialUpdatedInspection, getPersistedInspection(partialUpdatedInspection));
    }

    @Test
    @Transactional
    void patchNonExistingInspection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inspection.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inspection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inspection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInspection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inspection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inspection))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInspection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inspection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inspection)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inspection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInspection() throws Exception {
        // Initialize the database
        insertedInspection = inspectionRepository.saveAndFlush(inspection);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inspection
        restInspectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, inspection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inspectionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Inspection getPersistedInspection(Inspection inspection) {
        return inspectionRepository.findById(inspection.getId()).orElseThrow();
    }

    protected void assertPersistedInspectionToMatchAllProperties(Inspection expectedInspection) {
        assertInspectionAllPropertiesEquals(expectedInspection, getPersistedInspection(expectedInspection));
    }

    protected void assertPersistedInspectionToMatchUpdatableProperties(Inspection expectedInspection) {
        assertInspectionAllUpdatablePropertiesEquals(expectedInspection, getPersistedInspection(expectedInspection));
    }
}
