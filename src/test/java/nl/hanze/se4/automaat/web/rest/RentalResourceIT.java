package nl.hanze.se4.automaat.web.rest;

import static nl.hanze.se4.automaat.domain.RentalAsserts.*;
import static nl.hanze.se4.automaat.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import nl.hanze.se4.automaat.IntegrationTest;
import nl.hanze.se4.automaat.domain.Car;
import nl.hanze.se4.automaat.domain.Customer;
import nl.hanze.se4.automaat.domain.Rental;
import nl.hanze.se4.automaat.domain.enumeration.RentalState;
import nl.hanze.se4.automaat.repository.RentalRepository;
import nl.hanze.se4.automaat.service.RentalService;
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
 * Integration tests for the {@link RentalResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RentalResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Float DEFAULT_LONGITUDE = 1F;
    private static final Float UPDATED_LONGITUDE = 2F;
    private static final Float SMALLER_LONGITUDE = 1F - 1F;

    private static final Float DEFAULT_LATITUDE = 1F;
    private static final Float UPDATED_LATITUDE = 2F;
    private static final Float SMALLER_LATITUDE = 1F - 1F;

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FROM_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TO_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TO_DATE = LocalDate.ofEpochDay(-1L);

    private static final RentalState DEFAULT_STATE = RentalState.ACTIVE;
    private static final RentalState UPDATED_STATE = RentalState.RESERVED;

    private static final String ENTITY_API_URL = "/api/rentals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RentalRepository rentalRepository;

    @Mock
    private RentalRepository rentalRepositoryMock;

    @Mock
    private RentalService rentalServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRentalMockMvc;

    private Rental rental;

    private Rental insertedRental;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rental createEntity() {
        return new Rental()
            .code(DEFAULT_CODE)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .state(DEFAULT_STATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rental createUpdatedEntity() {
        return new Rental()
            .code(UPDATED_CODE)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .state(UPDATED_STATE);
    }

    @BeforeEach
    public void initTest() {
        rental = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRental != null) {
            rentalRepository.delete(insertedRental);
            insertedRental = null;
        }
    }

    @Test
    @Transactional
    void createRental() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Rental
        var returnedRental = om.readValue(
            restRentalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rental)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Rental.class
        );

        // Validate the Rental in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRentalUpdatableFieldsEquals(returnedRental, getPersistedRental(returnedRental));

        insertedRental = returnedRental;
    }

    @Test
    @Transactional
    void createRentalWithExistingId() throws Exception {
        // Create the Rental with an existing ID
        rental.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rental)))
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRentals() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList
        restRentalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rental.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRentalsWithEagerRelationshipsIsEnabled() throws Exception {
        when(rentalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRentalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(rentalServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRentalsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(rentalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRentalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(rentalRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRental() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get the rental
        restRentalMockMvc
            .perform(get(ENTITY_API_URL_ID, rental.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rental.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    void getRentalsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        Long id = rental.getId();

        defaultRentalFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRentalFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRentalFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRentalsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where code equals to
        defaultRentalFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllRentalsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where code in
        defaultRentalFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllRentalsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where code is not null
        defaultRentalFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllRentalsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where code contains
        defaultRentalFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllRentalsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where code does not contain
        defaultRentalFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllRentalsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where longitude equals to
        defaultRentalFiltering("longitude.equals=" + DEFAULT_LONGITUDE, "longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllRentalsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where longitude in
        defaultRentalFiltering("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE, "longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllRentalsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where longitude is not null
        defaultRentalFiltering("longitude.specified=true", "longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllRentalsByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where longitude is greater than or equal to
        defaultRentalFiltering("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllRentalsByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where longitude is less than or equal to
        defaultRentalFiltering("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllRentalsByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where longitude is less than
        defaultRentalFiltering("longitude.lessThan=" + UPDATED_LONGITUDE, "longitude.lessThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllRentalsByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where longitude is greater than
        defaultRentalFiltering("longitude.greaterThan=" + SMALLER_LONGITUDE, "longitude.greaterThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllRentalsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where latitude equals to
        defaultRentalFiltering("latitude.equals=" + DEFAULT_LATITUDE, "latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllRentalsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where latitude in
        defaultRentalFiltering("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE, "latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllRentalsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where latitude is not null
        defaultRentalFiltering("latitude.specified=true", "latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllRentalsByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where latitude is greater than or equal to
        defaultRentalFiltering("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE, "latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllRentalsByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where latitude is less than or equal to
        defaultRentalFiltering("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE, "latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllRentalsByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where latitude is less than
        defaultRentalFiltering("latitude.lessThan=" + UPDATED_LATITUDE, "latitude.lessThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllRentalsByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where latitude is greater than
        defaultRentalFiltering("latitude.greaterThan=" + SMALLER_LATITUDE, "latitude.greaterThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllRentalsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where fromDate equals to
        defaultRentalFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where fromDate in
        defaultRentalFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where fromDate is not null
        defaultRentalFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRentalsByFromDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where fromDate is greater than or equal to
        defaultRentalFiltering("fromDate.greaterThanOrEqual=" + DEFAULT_FROM_DATE, "fromDate.greaterThanOrEqual=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByFromDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where fromDate is less than or equal to
        defaultRentalFiltering("fromDate.lessThanOrEqual=" + DEFAULT_FROM_DATE, "fromDate.lessThanOrEqual=" + SMALLER_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByFromDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where fromDate is less than
        defaultRentalFiltering("fromDate.lessThan=" + UPDATED_FROM_DATE, "fromDate.lessThan=" + DEFAULT_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByFromDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where fromDate is greater than
        defaultRentalFiltering("fromDate.greaterThan=" + SMALLER_FROM_DATE, "fromDate.greaterThan=" + DEFAULT_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByToDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where toDate equals to
        defaultRentalFiltering("toDate.equals=" + DEFAULT_TO_DATE, "toDate.equals=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByToDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where toDate in
        defaultRentalFiltering("toDate.in=" + DEFAULT_TO_DATE + "," + UPDATED_TO_DATE, "toDate.in=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByToDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where toDate is not null
        defaultRentalFiltering("toDate.specified=true", "toDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRentalsByToDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where toDate is greater than or equal to
        defaultRentalFiltering("toDate.greaterThanOrEqual=" + DEFAULT_TO_DATE, "toDate.greaterThanOrEqual=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByToDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where toDate is less than or equal to
        defaultRentalFiltering("toDate.lessThanOrEqual=" + DEFAULT_TO_DATE, "toDate.lessThanOrEqual=" + SMALLER_TO_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByToDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where toDate is less than
        defaultRentalFiltering("toDate.lessThan=" + UPDATED_TO_DATE, "toDate.lessThan=" + DEFAULT_TO_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByToDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where toDate is greater than
        defaultRentalFiltering("toDate.greaterThan=" + SMALLER_TO_DATE, "toDate.greaterThan=" + DEFAULT_TO_DATE);
    }

    @Test
    @Transactional
    void getAllRentalsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where state equals to
        defaultRentalFiltering("state.equals=" + DEFAULT_STATE, "state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllRentalsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where state in
        defaultRentalFiltering("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE, "state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllRentalsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        // Get all the rentalList where state is not null
        defaultRentalFiltering("state.specified=true", "state.specified=false");
    }

    @Test
    @Transactional
    void getAllRentalsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            rentalRepository.saveAndFlush(rental);
            customer = CustomerResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        rental.setCustomer(customer);
        rentalRepository.saveAndFlush(rental);
        Long customerId = customer.getId();
        // Get all the rentalList where customer equals to customerId
        defaultRentalShouldBeFound("customerId.equals=" + customerId);

        // Get all the rentalList where customer equals to (customerId + 1)
        defaultRentalShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllRentalsByCarIsEqualToSomething() throws Exception {
        Car car;
        if (TestUtil.findAll(em, Car.class).isEmpty()) {
            rentalRepository.saveAndFlush(rental);
            car = CarResourceIT.createEntity();
        } else {
            car = TestUtil.findAll(em, Car.class).get(0);
        }
        em.persist(car);
        em.flush();
        rental.setCar(car);
        rentalRepository.saveAndFlush(rental);
        Long carId = car.getId();
        // Get all the rentalList where car equals to carId
        defaultRentalShouldBeFound("carId.equals=" + carId);

        // Get all the rentalList where car equals to (carId + 1)
        defaultRentalShouldNotBeFound("carId.equals=" + (carId + 1));
    }

    private void defaultRentalFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRentalShouldBeFound(shouldBeFound);
        defaultRentalShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRentalShouldBeFound(String filter) throws Exception {
        restRentalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rental.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));

        // Check, that the count call also returns 1
        restRentalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRentalShouldNotBeFound(String filter) throws Exception {
        restRentalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRentalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRental() throws Exception {
        // Get the rental
        restRentalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRental() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rental
        Rental updatedRental = rentalRepository.findById(rental.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRental are not directly saved in db
        em.detach(updatedRental);
        updatedRental
            .code(UPDATED_CODE)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .state(UPDATED_STATE);

        restRentalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRental.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRental))
            )
            .andExpect(status().isOk());

        // Validate the Rental in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRentalToMatchAllProperties(updatedRental);
    }

    @Test
    @Transactional
    void putNonExistingRental() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rental.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(put(ENTITY_API_URL_ID, rental.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rental)))
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRental() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rental.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rental))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRental() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rental.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rental)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rental in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRentalWithPatch() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rental using partial update
        Rental partialUpdatedRental = new Rental();
        partialUpdatedRental.setId(rental.getId());

        partialUpdatedRental
            .code(UPDATED_CODE)
            .latitude(UPDATED_LATITUDE)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .state(UPDATED_STATE);

        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRental.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRental))
            )
            .andExpect(status().isOk());

        // Validate the Rental in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRentalUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRental, rental), getPersistedRental(rental));
    }

    @Test
    @Transactional
    void fullUpdateRentalWithPatch() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rental using partial update
        Rental partialUpdatedRental = new Rental();
        partialUpdatedRental.setId(rental.getId());

        partialUpdatedRental
            .code(UPDATED_CODE)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .state(UPDATED_STATE);

        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRental.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRental))
            )
            .andExpect(status().isOk());

        // Validate the Rental in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRentalUpdatableFieldsEquals(partialUpdatedRental, getPersistedRental(partialUpdatedRental));
    }

    @Test
    @Transactional
    void patchNonExistingRental() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rental.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rental.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rental))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRental() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rental.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rental))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRental() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rental.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rental)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rental in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRental() throws Exception {
        // Initialize the database
        insertedRental = rentalRepository.saveAndFlush(rental);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rental
        restRentalMockMvc
            .perform(delete(ENTITY_API_URL_ID, rental.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rentalRepository.count();
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

    protected Rental getPersistedRental(Rental rental) {
        return rentalRepository.findById(rental.getId()).orElseThrow();
    }

    protected void assertPersistedRentalToMatchAllProperties(Rental expectedRental) {
        assertRentalAllPropertiesEquals(expectedRental, getPersistedRental(expectedRental));
    }

    protected void assertPersistedRentalToMatchUpdatableProperties(Rental expectedRental) {
        assertRentalAllUpdatablePropertiesEquals(expectedRental, getPersistedRental(expectedRental));
    }
}
