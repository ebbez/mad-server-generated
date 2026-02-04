package nl.hanze.se4.automaat.web.rest;

import static nl.hanze.se4.automaat.domain.CarReviewAsserts.*;
import static nl.hanze.se4.automaat.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import nl.hanze.se4.automaat.IntegrationTest;
import nl.hanze.se4.automaat.domain.CarReview;
import nl.hanze.se4.automaat.repository.CarReviewRepository;
import nl.hanze.se4.automaat.service.CarReviewService;
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
 * Integration tests for the {@link CarReviewResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CarReviewResourceIT {

    private static final String DEFAULT_REVIEW = "AAAAAAAAAA";
    private static final String UPDATED_REVIEW = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/car-reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CarReviewRepository carReviewRepository;

    @Mock
    private CarReviewRepository carReviewRepositoryMock;

    @Mock
    private CarReviewService carReviewServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarReviewMockMvc;

    private CarReview carReview;

    private CarReview insertedCarReview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarReview createEntity() {
        return new CarReview().review(DEFAULT_REVIEW);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarReview createUpdatedEntity() {
        return new CarReview().review(UPDATED_REVIEW);
    }

    @BeforeEach
    void initTest() {
        carReview = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCarReview != null) {
            carReviewRepository.delete(insertedCarReview);
            insertedCarReview = null;
        }
    }

    @Test
    @Transactional
    void createCarReview() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CarReview
        var returnedCarReview = om.readValue(
            restCarReviewMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carReview)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CarReview.class
        );

        // Validate the CarReview in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCarReviewUpdatableFieldsEquals(returnedCarReview, getPersistedCarReview(returnedCarReview));

        insertedCarReview = returnedCarReview;
    }

    @Test
    @Transactional
    void createCarReviewWithExistingId() throws Exception {
        // Create the CarReview with an existing ID
        carReview.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carReview)))
            .andExpect(status().isBadRequest());

        // Validate the CarReview in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCarReviews() throws Exception {
        // Initialize the database
        insertedCarReview = carReviewRepository.saveAndFlush(carReview);

        // Get all the carReviewList
        restCarReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].review").value(hasItem(DEFAULT_REVIEW)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarReviewsWithEagerRelationshipsIsEnabled() throws Exception {
        when(carReviewServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarReviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carReviewServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarReviewsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(carReviewServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarReviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(carReviewRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCarReview() throws Exception {
        // Initialize the database
        insertedCarReview = carReviewRepository.saveAndFlush(carReview);

        // Get the carReview
        restCarReviewMockMvc
            .perform(get(ENTITY_API_URL_ID, carReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carReview.getId().intValue()))
            .andExpect(jsonPath("$.review").value(DEFAULT_REVIEW));
    }

    @Test
    @Transactional
    void getNonExistingCarReview() throws Exception {
        // Get the carReview
        restCarReviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCarReview() throws Exception {
        // Initialize the database
        insertedCarReview = carReviewRepository.saveAndFlush(carReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carReview
        CarReview updatedCarReview = carReviewRepository.findById(carReview.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCarReview are not directly saved in db
        em.detach(updatedCarReview);
        updatedCarReview.review(UPDATED_REVIEW);

        restCarReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCarReview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCarReview))
            )
            .andExpect(status().isOk());

        // Validate the CarReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCarReviewToMatchAllProperties(updatedCarReview);
    }

    @Test
    @Transactional
    void putNonExistingCarReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carReview.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carReview.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carReview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carReview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarReviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carReview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarReviewWithPatch() throws Exception {
        // Initialize the database
        insertedCarReview = carReviewRepository.saveAndFlush(carReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carReview using partial update
        CarReview partialUpdatedCarReview = new CarReview();
        partialUpdatedCarReview.setId(carReview.getId());

        partialUpdatedCarReview.review(UPDATED_REVIEW);

        restCarReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarReview))
            )
            .andExpect(status().isOk());

        // Validate the CarReview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarReviewUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCarReview, carReview),
            getPersistedCarReview(carReview)
        );
    }

    @Test
    @Transactional
    void fullUpdateCarReviewWithPatch() throws Exception {
        // Initialize the database
        insertedCarReview = carReviewRepository.saveAndFlush(carReview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carReview using partial update
        CarReview partialUpdatedCarReview = new CarReview();
        partialUpdatedCarReview.setId(carReview.getId());

        partialUpdatedCarReview.review(UPDATED_REVIEW);

        restCarReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarReview))
            )
            .andExpect(status().isOk());

        // Validate the CarReview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarReviewUpdatableFieldsEquals(partialUpdatedCarReview, getPersistedCarReview(partialUpdatedCarReview));
    }

    @Test
    @Transactional
    void patchNonExistingCarReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carReview.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carReview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carReview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarReviewMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(carReview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarReview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarReview() throws Exception {
        // Initialize the database
        insertedCarReview = carReviewRepository.saveAndFlush(carReview);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the carReview
        restCarReviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, carReview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return carReviewRepository.count();
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

    protected CarReview getPersistedCarReview(CarReview carReview) {
        return carReviewRepository.findById(carReview.getId()).orElseThrow();
    }

    protected void assertPersistedCarReviewToMatchAllProperties(CarReview expectedCarReview) {
        assertCarReviewAllPropertiesEquals(expectedCarReview, getPersistedCarReview(expectedCarReview));
    }

    protected void assertPersistedCarReviewToMatchUpdatableProperties(CarReview expectedCarReview) {
        assertCarReviewAllUpdatablePropertiesEquals(expectedCarReview, getPersistedCarReview(expectedCarReview));
    }
}
