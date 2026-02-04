package nl.hanze.se4.automaat.web.rest;

import static nl.hanze.se4.automaat.domain.CarAsserts.*;
import static nl.hanze.se4.automaat.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import nl.hanze.se4.automaat.IntegrationTest;
import nl.hanze.se4.automaat.domain.Car;
import nl.hanze.se4.automaat.domain.enumeration.Body;
import nl.hanze.se4.automaat.domain.enumeration.Fuel;
import nl.hanze.se4.automaat.repository.CarRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarResourceIT {

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Fuel DEFAULT_FUEL = Fuel.GASOLINE;
    private static final Fuel UPDATED_FUEL = Fuel.DIESEL;

    private static final String DEFAULT_OPTIONS = "AAAAAAAAAA";
    private static final String UPDATED_OPTIONS = "BBBBBBBBBB";

    private static final String DEFAULT_LICENSE_PLATE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_PLATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ENGINE_SIZE = 1;
    private static final Integer UPDATED_ENGINE_SIZE = 2;
    private static final Integer SMALLER_ENGINE_SIZE = 1 - 1;

    private static final Integer DEFAULT_MODEL_YEAR = 1;
    private static final Integer UPDATED_MODEL_YEAR = 2;
    private static final Integer SMALLER_MODEL_YEAR = 1 - 1;

    private static final LocalDate DEFAULT_SINCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SINCE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SINCE = LocalDate.ofEpochDay(-1L);

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final Integer DEFAULT_NR_OF_SEATS = 1;
    private static final Integer UPDATED_NR_OF_SEATS = 2;
    private static final Integer SMALLER_NR_OF_SEATS = 1 - 1;

    private static final Body DEFAULT_BODY = Body.STATIONWAGON;
    private static final Body UPDATED_BODY = Body.SEDAN;

    private static final Float DEFAULT_LONGITUDE = 1F;
    private static final Float UPDATED_LONGITUDE = 2F;
    private static final Float SMALLER_LONGITUDE = 1F - 1F;

    private static final Float DEFAULT_LATITUDE = 1F;
    private static final Float UPDATED_LATITUDE = 2F;
    private static final Float SMALLER_LATITUDE = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/cars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarMockMvc;

    private Car car;

    private Car insertedCar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity() {
        return new Car()
            .brand(DEFAULT_BRAND)
            .model(DEFAULT_MODEL)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .fuel(DEFAULT_FUEL)
            .options(DEFAULT_OPTIONS)
            .licensePlate(DEFAULT_LICENSE_PLATE)
            .engineSize(DEFAULT_ENGINE_SIZE)
            .modelYear(DEFAULT_MODEL_YEAR)
            .since(DEFAULT_SINCE)
            .price(DEFAULT_PRICE)
            .nrOfSeats(DEFAULT_NR_OF_SEATS)
            .body(DEFAULT_BODY)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createUpdatedEntity() {
        return new Car()
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .fuel(UPDATED_FUEL)
            .options(UPDATED_OPTIONS)
            .licensePlate(UPDATED_LICENSE_PLATE)
            .engineSize(UPDATED_ENGINE_SIZE)
            .modelYear(UPDATED_MODEL_YEAR)
            .since(UPDATED_SINCE)
            .price(UPDATED_PRICE)
            .nrOfSeats(UPDATED_NR_OF_SEATS)
            .body(UPDATED_BODY)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE);
    }

    @BeforeEach
    void initTest() {
        car = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCar != null) {
            carRepository.delete(insertedCar);
            insertedCar = null;
        }
    }

    @Test
    @Transactional
    void createCar() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Car
        var returnedCar = om.readValue(
            restCarMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(car)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Car.class
        );

        // Validate the Car in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCarUpdatableFieldsEquals(returnedCar, getPersistedCar(returnedCar));

        insertedCar = returnedCar;
    }

    @Test
    @Transactional
    void createCarWithExistingId() throws Exception {
        // Create the Car with an existing ID
        car.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(car)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCars() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].fuel").value(hasItem(DEFAULT_FUEL.toString())))
            .andExpect(jsonPath("$.[*].options").value(hasItem(DEFAULT_OPTIONS)))
            .andExpect(jsonPath("$.[*].licensePlate").value(hasItem(DEFAULT_LICENSE_PLATE)))
            .andExpect(jsonPath("$.[*].engineSize").value(hasItem(DEFAULT_ENGINE_SIZE)))
            .andExpect(jsonPath("$.[*].modelYear").value(hasItem(DEFAULT_MODEL_YEAR)))
            .andExpect(jsonPath("$.[*].since").value(hasItem(DEFAULT_SINCE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].nrOfSeats").value(hasItem(DEFAULT_NR_OF_SEATS)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())));
    }

    @Test
    @Transactional
    void getCar() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc
            .perform(get(ENTITY_API_URL_ID, car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64.getEncoder().encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.fuel").value(DEFAULT_FUEL.toString()))
            .andExpect(jsonPath("$.options").value(DEFAULT_OPTIONS))
            .andExpect(jsonPath("$.licensePlate").value(DEFAULT_LICENSE_PLATE))
            .andExpect(jsonPath("$.engineSize").value(DEFAULT_ENGINE_SIZE))
            .andExpect(jsonPath("$.modelYear").value(DEFAULT_MODEL_YEAR))
            .andExpect(jsonPath("$.since").value(DEFAULT_SINCE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.nrOfSeats").value(DEFAULT_NR_OF_SEATS))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()));
    }

    @Test
    @Transactional
    void getCarsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        Long id = car.getId();

        defaultCarFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCarFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCarFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCarsByBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where brand equals to
        defaultCarFiltering("brand.equals=" + DEFAULT_BRAND, "brand.equals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllCarsByBrandIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where brand in
        defaultCarFiltering("brand.in=" + DEFAULT_BRAND + "," + UPDATED_BRAND, "brand.in=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllCarsByBrandIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where brand is not null
        defaultCarFiltering("brand.specified=true", "brand.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByBrandContainsSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where brand contains
        defaultCarFiltering("brand.contains=" + DEFAULT_BRAND, "brand.contains=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllCarsByBrandNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where brand does not contain
        defaultCarFiltering("brand.doesNotContain=" + UPDATED_BRAND, "brand.doesNotContain=" + DEFAULT_BRAND);
    }

    @Test
    @Transactional
    void getAllCarsByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where model equals to
        defaultCarFiltering("model.equals=" + DEFAULT_MODEL, "model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByModelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where model in
        defaultCarFiltering("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL, "model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where model is not null
        defaultCarFiltering("model.specified=true", "model.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByModelContainsSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where model contains
        defaultCarFiltering("model.contains=" + DEFAULT_MODEL, "model.contains=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByModelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where model does not contain
        defaultCarFiltering("model.doesNotContain=" + UPDATED_MODEL, "model.doesNotContain=" + DEFAULT_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByFuelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where fuel equals to
        defaultCarFiltering("fuel.equals=" + DEFAULT_FUEL, "fuel.equals=" + UPDATED_FUEL);
    }

    @Test
    @Transactional
    void getAllCarsByFuelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where fuel in
        defaultCarFiltering("fuel.in=" + DEFAULT_FUEL + "," + UPDATED_FUEL, "fuel.in=" + UPDATED_FUEL);
    }

    @Test
    @Transactional
    void getAllCarsByFuelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where fuel is not null
        defaultCarFiltering("fuel.specified=true", "fuel.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByOptionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where options equals to
        defaultCarFiltering("options.equals=" + DEFAULT_OPTIONS, "options.equals=" + UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void getAllCarsByOptionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where options in
        defaultCarFiltering("options.in=" + DEFAULT_OPTIONS + "," + UPDATED_OPTIONS, "options.in=" + UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void getAllCarsByOptionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where options is not null
        defaultCarFiltering("options.specified=true", "options.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByOptionsContainsSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where options contains
        defaultCarFiltering("options.contains=" + DEFAULT_OPTIONS, "options.contains=" + UPDATED_OPTIONS);
    }

    @Test
    @Transactional
    void getAllCarsByOptionsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where options does not contain
        defaultCarFiltering("options.doesNotContain=" + UPDATED_OPTIONS, "options.doesNotContain=" + DEFAULT_OPTIONS);
    }

    @Test
    @Transactional
    void getAllCarsByLicensePlateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where licensePlate equals to
        defaultCarFiltering("licensePlate.equals=" + DEFAULT_LICENSE_PLATE, "licensePlate.equals=" + UPDATED_LICENSE_PLATE);
    }

    @Test
    @Transactional
    void getAllCarsByLicensePlateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where licensePlate in
        defaultCarFiltering(
            "licensePlate.in=" + DEFAULT_LICENSE_PLATE + "," + UPDATED_LICENSE_PLATE,
            "licensePlate.in=" + UPDATED_LICENSE_PLATE
        );
    }

    @Test
    @Transactional
    void getAllCarsByLicensePlateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where licensePlate is not null
        defaultCarFiltering("licensePlate.specified=true", "licensePlate.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByLicensePlateContainsSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where licensePlate contains
        defaultCarFiltering("licensePlate.contains=" + DEFAULT_LICENSE_PLATE, "licensePlate.contains=" + UPDATED_LICENSE_PLATE);
    }

    @Test
    @Transactional
    void getAllCarsByLicensePlateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where licensePlate does not contain
        defaultCarFiltering("licensePlate.doesNotContain=" + UPDATED_LICENSE_PLATE, "licensePlate.doesNotContain=" + DEFAULT_LICENSE_PLATE);
    }

    @Test
    @Transactional
    void getAllCarsByEngineSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where engineSize equals to
        defaultCarFiltering("engineSize.equals=" + DEFAULT_ENGINE_SIZE, "engineSize.equals=" + UPDATED_ENGINE_SIZE);
    }

    @Test
    @Transactional
    void getAllCarsByEngineSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where engineSize in
        defaultCarFiltering("engineSize.in=" + DEFAULT_ENGINE_SIZE + "," + UPDATED_ENGINE_SIZE, "engineSize.in=" + UPDATED_ENGINE_SIZE);
    }

    @Test
    @Transactional
    void getAllCarsByEngineSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where engineSize is not null
        defaultCarFiltering("engineSize.specified=true", "engineSize.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByEngineSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where engineSize is greater than or equal to
        defaultCarFiltering("engineSize.greaterThanOrEqual=" + DEFAULT_ENGINE_SIZE, "engineSize.greaterThanOrEqual=" + UPDATED_ENGINE_SIZE);
    }

    @Test
    @Transactional
    void getAllCarsByEngineSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where engineSize is less than or equal to
        defaultCarFiltering("engineSize.lessThanOrEqual=" + DEFAULT_ENGINE_SIZE, "engineSize.lessThanOrEqual=" + SMALLER_ENGINE_SIZE);
    }

    @Test
    @Transactional
    void getAllCarsByEngineSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where engineSize is less than
        defaultCarFiltering("engineSize.lessThan=" + UPDATED_ENGINE_SIZE, "engineSize.lessThan=" + DEFAULT_ENGINE_SIZE);
    }

    @Test
    @Transactional
    void getAllCarsByEngineSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where engineSize is greater than
        defaultCarFiltering("engineSize.greaterThan=" + SMALLER_ENGINE_SIZE, "engineSize.greaterThan=" + DEFAULT_ENGINE_SIZE);
    }

    @Test
    @Transactional
    void getAllCarsByModelYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where modelYear equals to
        defaultCarFiltering("modelYear.equals=" + DEFAULT_MODEL_YEAR, "modelYear.equals=" + UPDATED_MODEL_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByModelYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where modelYear in
        defaultCarFiltering("modelYear.in=" + DEFAULT_MODEL_YEAR + "," + UPDATED_MODEL_YEAR, "modelYear.in=" + UPDATED_MODEL_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByModelYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where modelYear is not null
        defaultCarFiltering("modelYear.specified=true", "modelYear.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByModelYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where modelYear is greater than or equal to
        defaultCarFiltering("modelYear.greaterThanOrEqual=" + DEFAULT_MODEL_YEAR, "modelYear.greaterThanOrEqual=" + UPDATED_MODEL_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByModelYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where modelYear is less than or equal to
        defaultCarFiltering("modelYear.lessThanOrEqual=" + DEFAULT_MODEL_YEAR, "modelYear.lessThanOrEqual=" + SMALLER_MODEL_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByModelYearIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where modelYear is less than
        defaultCarFiltering("modelYear.lessThan=" + UPDATED_MODEL_YEAR, "modelYear.lessThan=" + DEFAULT_MODEL_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsByModelYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where modelYear is greater than
        defaultCarFiltering("modelYear.greaterThan=" + SMALLER_MODEL_YEAR, "modelYear.greaterThan=" + DEFAULT_MODEL_YEAR);
    }

    @Test
    @Transactional
    void getAllCarsBySinceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where since equals to
        defaultCarFiltering("since.equals=" + DEFAULT_SINCE, "since.equals=" + UPDATED_SINCE);
    }

    @Test
    @Transactional
    void getAllCarsBySinceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where since in
        defaultCarFiltering("since.in=" + DEFAULT_SINCE + "," + UPDATED_SINCE, "since.in=" + UPDATED_SINCE);
    }

    @Test
    @Transactional
    void getAllCarsBySinceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where since is not null
        defaultCarFiltering("since.specified=true", "since.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsBySinceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where since is greater than or equal to
        defaultCarFiltering("since.greaterThanOrEqual=" + DEFAULT_SINCE, "since.greaterThanOrEqual=" + UPDATED_SINCE);
    }

    @Test
    @Transactional
    void getAllCarsBySinceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where since is less than or equal to
        defaultCarFiltering("since.lessThanOrEqual=" + DEFAULT_SINCE, "since.lessThanOrEqual=" + SMALLER_SINCE);
    }

    @Test
    @Transactional
    void getAllCarsBySinceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where since is less than
        defaultCarFiltering("since.lessThan=" + UPDATED_SINCE, "since.lessThan=" + DEFAULT_SINCE);
    }

    @Test
    @Transactional
    void getAllCarsBySinceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where since is greater than
        defaultCarFiltering("since.greaterThan=" + SMALLER_SINCE, "since.greaterThan=" + DEFAULT_SINCE);
    }

    @Test
    @Transactional
    void getAllCarsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where price equals to
        defaultCarFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCarsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where price in
        defaultCarFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCarsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where price is not null
        defaultCarFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where price is greater than or equal to
        defaultCarFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCarsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where price is less than or equal to
        defaultCarFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllCarsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where price is less than
        defaultCarFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllCarsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where price is greater than
        defaultCarFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllCarsByNrOfSeatsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where nrOfSeats equals to
        defaultCarFiltering("nrOfSeats.equals=" + DEFAULT_NR_OF_SEATS, "nrOfSeats.equals=" + UPDATED_NR_OF_SEATS);
    }

    @Test
    @Transactional
    void getAllCarsByNrOfSeatsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where nrOfSeats in
        defaultCarFiltering("nrOfSeats.in=" + DEFAULT_NR_OF_SEATS + "," + UPDATED_NR_OF_SEATS, "nrOfSeats.in=" + UPDATED_NR_OF_SEATS);
    }

    @Test
    @Transactional
    void getAllCarsByNrOfSeatsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where nrOfSeats is not null
        defaultCarFiltering("nrOfSeats.specified=true", "nrOfSeats.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByNrOfSeatsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where nrOfSeats is greater than or equal to
        defaultCarFiltering("nrOfSeats.greaterThanOrEqual=" + DEFAULT_NR_OF_SEATS, "nrOfSeats.greaterThanOrEqual=" + UPDATED_NR_OF_SEATS);
    }

    @Test
    @Transactional
    void getAllCarsByNrOfSeatsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where nrOfSeats is less than or equal to
        defaultCarFiltering("nrOfSeats.lessThanOrEqual=" + DEFAULT_NR_OF_SEATS, "nrOfSeats.lessThanOrEqual=" + SMALLER_NR_OF_SEATS);
    }

    @Test
    @Transactional
    void getAllCarsByNrOfSeatsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where nrOfSeats is less than
        defaultCarFiltering("nrOfSeats.lessThan=" + UPDATED_NR_OF_SEATS, "nrOfSeats.lessThan=" + DEFAULT_NR_OF_SEATS);
    }

    @Test
    @Transactional
    void getAllCarsByNrOfSeatsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where nrOfSeats is greater than
        defaultCarFiltering("nrOfSeats.greaterThan=" + SMALLER_NR_OF_SEATS, "nrOfSeats.greaterThan=" + DEFAULT_NR_OF_SEATS);
    }

    @Test
    @Transactional
    void getAllCarsByBodyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where body equals to
        defaultCarFiltering("body.equals=" + DEFAULT_BODY, "body.equals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllCarsByBodyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where body in
        defaultCarFiltering("body.in=" + DEFAULT_BODY + "," + UPDATED_BODY, "body.in=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllCarsByBodyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where body is not null
        defaultCarFiltering("body.specified=true", "body.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where longitude equals to
        defaultCarFiltering("longitude.equals=" + DEFAULT_LONGITUDE, "longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCarsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where longitude in
        defaultCarFiltering("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE, "longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCarsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where longitude is not null
        defaultCarFiltering("longitude.specified=true", "longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where longitude is greater than or equal to
        defaultCarFiltering("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCarsByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where longitude is less than or equal to
        defaultCarFiltering("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCarsByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where longitude is less than
        defaultCarFiltering("longitude.lessThan=" + UPDATED_LONGITUDE, "longitude.lessThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCarsByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where longitude is greater than
        defaultCarFiltering("longitude.greaterThan=" + SMALLER_LONGITUDE, "longitude.greaterThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCarsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where latitude equals to
        defaultCarFiltering("latitude.equals=" + DEFAULT_LATITUDE, "latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCarsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where latitude in
        defaultCarFiltering("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE, "latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCarsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where latitude is not null
        defaultCarFiltering("latitude.specified=true", "latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where latitude is greater than or equal to
        defaultCarFiltering("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE, "latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCarsByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where latitude is less than or equal to
        defaultCarFiltering("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE, "latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCarsByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where latitude is less than
        defaultCarFiltering("latitude.lessThan=" + UPDATED_LATITUDE, "latitude.lessThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCarsByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        // Get all the carList where latitude is greater than
        defaultCarFiltering("latitude.greaterThan=" + SMALLER_LATITUDE, "latitude.greaterThan=" + DEFAULT_LATITUDE);
    }

    private void defaultCarFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCarShouldBeFound(shouldBeFound);
        defaultCarShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCarShouldBeFound(String filter) throws Exception {
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].fuel").value(hasItem(DEFAULT_FUEL.toString())))
            .andExpect(jsonPath("$.[*].options").value(hasItem(DEFAULT_OPTIONS)))
            .andExpect(jsonPath("$.[*].licensePlate").value(hasItem(DEFAULT_LICENSE_PLATE)))
            .andExpect(jsonPath("$.[*].engineSize").value(hasItem(DEFAULT_ENGINE_SIZE)))
            .andExpect(jsonPath("$.[*].modelYear").value(hasItem(DEFAULT_MODEL_YEAR)))
            .andExpect(jsonPath("$.[*].since").value(hasItem(DEFAULT_SINCE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].nrOfSeats").value(hasItem(DEFAULT_NR_OF_SEATS)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())));

        // Check, that the count call also returns 1
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCarShouldNotBeFound(String filter) throws Exception {
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCar() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car
        Car updatedCar = carRepository.findById(car.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCar are not directly saved in db
        em.detach(updatedCar);
        updatedCar
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .fuel(UPDATED_FUEL)
            .options(UPDATED_OPTIONS)
            .licensePlate(UPDATED_LICENSE_PLATE)
            .engineSize(UPDATED_ENGINE_SIZE)
            .modelYear(UPDATED_MODEL_YEAR)
            .since(UPDATED_SINCE)
            .price(UPDATED_PRICE)
            .nrOfSeats(UPDATED_NR_OF_SEATS)
            .body(UPDATED_BODY)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE);

        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCar.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCarToMatchAllProperties(updatedCar);
    }

    @Test
    @Transactional
    void putNonExistingCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(put(ENTITY_API_URL_ID, car.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(car)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(car)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarWithPatch() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar
            .options(UPDATED_OPTIONS)
            .licensePlate(UPDATED_LICENSE_PLATE)
            .modelYear(UPDATED_MODEL_YEAR)
            .nrOfSeats(UPDATED_NR_OF_SEATS)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCar, car), getPersistedCar(car));
    }

    @Test
    @Transactional
    void fullUpdateCarWithPatch() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .fuel(UPDATED_FUEL)
            .options(UPDATED_OPTIONS)
            .licensePlate(UPDATED_LICENSE_PLATE)
            .engineSize(UPDATED_ENGINE_SIZE)
            .modelYear(UPDATED_MODEL_YEAR)
            .since(UPDATED_SINCE)
            .price(UPDATED_PRICE)
            .nrOfSeats(UPDATED_NR_OF_SEATS)
            .body(UPDATED_BODY)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarUpdatableFieldsEquals(partialUpdatedCar, getPersistedCar(partialUpdatedCar));
    }

    @Test
    @Transactional
    void patchNonExistingCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(patch(ENTITY_API_URL_ID, car.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(car)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCar() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        car.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(car)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCar() throws Exception {
        // Initialize the database
        insertedCar = carRepository.saveAndFlush(car);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the car
        restCarMockMvc.perform(delete(ENTITY_API_URL_ID, car.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return carRepository.count();
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

    protected Car getPersistedCar(Car car) {
        return carRepository.findById(car.getId()).orElseThrow();
    }

    protected void assertPersistedCarToMatchAllProperties(Car expectedCar) {
        assertCarAllPropertiesEquals(expectedCar, getPersistedCar(expectedCar));
    }

    protected void assertPersistedCarToMatchUpdatableProperties(Car expectedCar) {
        assertCarAllUpdatablePropertiesEquals(expectedCar, getPersistedCar(expectedCar));
    }
}
