package nl.hanze.se4.automaat.web.rest;

import static nl.hanze.se4.automaat.domain.RouteFromToAsserts.*;
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
import nl.hanze.se4.automaat.domain.RouteFromTo;
import nl.hanze.se4.automaat.repository.RouteFromToRepository;
import nl.hanze.se4.automaat.service.RouteFromToService;
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
 * Integration tests for the {@link RouteFromToResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RouteFromToResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/route-from-tos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RouteFromToRepository routeFromToRepository;

    @Mock
    private RouteFromToRepository routeFromToRepositoryMock;

    @Mock
    private RouteFromToService routeFromToServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRouteFromToMockMvc;

    private RouteFromTo routeFromTo;

    private RouteFromTo insertedRouteFromTo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouteFromTo createEntity() {
        return new RouteFromTo().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION).date(DEFAULT_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RouteFromTo createUpdatedEntity() {
        return new RouteFromTo().code(UPDATED_CODE).description(UPDATED_DESCRIPTION).date(UPDATED_DATE);
    }

    @BeforeEach
    public void initTest() {
        routeFromTo = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRouteFromTo != null) {
            routeFromToRepository.delete(insertedRouteFromTo);
            insertedRouteFromTo = null;
        }
    }

    @Test
    @Transactional
    void createRouteFromTo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RouteFromTo
        var returnedRouteFromTo = om.readValue(
            restRouteFromToMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(routeFromTo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RouteFromTo.class
        );

        // Validate the RouteFromTo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRouteFromToUpdatableFieldsEquals(returnedRouteFromTo, getPersistedRouteFromTo(returnedRouteFromTo));

        insertedRouteFromTo = returnedRouteFromTo;
    }

    @Test
    @Transactional
    void createRouteFromToWithExistingId() throws Exception {
        // Create the RouteFromTo with an existing ID
        routeFromTo.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRouteFromToMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(routeFromTo)))
            .andExpect(status().isBadRequest());

        // Validate the RouteFromTo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRouteFromTos() throws Exception {
        // Initialize the database
        insertedRouteFromTo = routeFromToRepository.saveAndFlush(routeFromTo);

        // Get all the routeFromToList
        restRouteFromToMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(routeFromTo.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRouteFromTosWithEagerRelationshipsIsEnabled() throws Exception {
        when(routeFromToServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRouteFromToMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(routeFromToServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRouteFromTosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(routeFromToServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRouteFromToMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(routeFromToRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRouteFromTo() throws Exception {
        // Initialize the database
        insertedRouteFromTo = routeFromToRepository.saveAndFlush(routeFromTo);

        // Get the routeFromTo
        restRouteFromToMockMvc
            .perform(get(ENTITY_API_URL_ID, routeFromTo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(routeFromTo.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRouteFromTo() throws Exception {
        // Get the routeFromTo
        restRouteFromToMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRouteFromTo() throws Exception {
        // Initialize the database
        insertedRouteFromTo = routeFromToRepository.saveAndFlush(routeFromTo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the routeFromTo
        RouteFromTo updatedRouteFromTo = routeFromToRepository.findById(routeFromTo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRouteFromTo are not directly saved in db
        em.detach(updatedRouteFromTo);
        updatedRouteFromTo.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).date(UPDATED_DATE);

        restRouteFromToMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRouteFromTo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRouteFromTo))
            )
            .andExpect(status().isOk());

        // Validate the RouteFromTo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRouteFromToToMatchAllProperties(updatedRouteFromTo);
    }

    @Test
    @Transactional
    void putNonExistingRouteFromTo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        routeFromTo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouteFromToMockMvc
            .perform(
                put(ENTITY_API_URL_ID, routeFromTo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(routeFromTo))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouteFromTo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRouteFromTo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        routeFromTo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouteFromToMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(routeFromTo))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouteFromTo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRouteFromTo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        routeFromTo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouteFromToMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(routeFromTo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouteFromTo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRouteFromToWithPatch() throws Exception {
        // Initialize the database
        insertedRouteFromTo = routeFromToRepository.saveAndFlush(routeFromTo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the routeFromTo using partial update
        RouteFromTo partialUpdatedRouteFromTo = new RouteFromTo();
        partialUpdatedRouteFromTo.setId(routeFromTo.getId());

        partialUpdatedRouteFromTo.description(UPDATED_DESCRIPTION).date(UPDATED_DATE);

        restRouteFromToMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouteFromTo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRouteFromTo))
            )
            .andExpect(status().isOk());

        // Validate the RouteFromTo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRouteFromToUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRouteFromTo, routeFromTo),
            getPersistedRouteFromTo(routeFromTo)
        );
    }

    @Test
    @Transactional
    void fullUpdateRouteFromToWithPatch() throws Exception {
        // Initialize the database
        insertedRouteFromTo = routeFromToRepository.saveAndFlush(routeFromTo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the routeFromTo using partial update
        RouteFromTo partialUpdatedRouteFromTo = new RouteFromTo();
        partialUpdatedRouteFromTo.setId(routeFromTo.getId());

        partialUpdatedRouteFromTo.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).date(UPDATED_DATE);

        restRouteFromToMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRouteFromTo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRouteFromTo))
            )
            .andExpect(status().isOk());

        // Validate the RouteFromTo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRouteFromToUpdatableFieldsEquals(partialUpdatedRouteFromTo, getPersistedRouteFromTo(partialUpdatedRouteFromTo));
    }

    @Test
    @Transactional
    void patchNonExistingRouteFromTo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        routeFromTo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRouteFromToMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, routeFromTo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(routeFromTo))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouteFromTo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRouteFromTo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        routeFromTo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouteFromToMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(routeFromTo))
            )
            .andExpect(status().isBadRequest());

        // Validate the RouteFromTo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRouteFromTo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        routeFromTo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRouteFromToMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(routeFromTo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RouteFromTo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRouteFromTo() throws Exception {
        // Initialize the database
        insertedRouteFromTo = routeFromToRepository.saveAndFlush(routeFromTo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the routeFromTo
        restRouteFromToMockMvc
            .perform(delete(ENTITY_API_URL_ID, routeFromTo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return routeFromToRepository.count();
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

    protected RouteFromTo getPersistedRouteFromTo(RouteFromTo routeFromTo) {
        return routeFromToRepository.findById(routeFromTo.getId()).orElseThrow();
    }

    protected void assertPersistedRouteFromToToMatchAllProperties(RouteFromTo expectedRouteFromTo) {
        assertRouteFromToAllPropertiesEquals(expectedRouteFromTo, getPersistedRouteFromTo(expectedRouteFromTo));
    }

    protected void assertPersistedRouteFromToToMatchUpdatableProperties(RouteFromTo expectedRouteFromTo) {
        assertRouteFromToAllUpdatablePropertiesEquals(expectedRouteFromTo, getPersistedRouteFromTo(expectedRouteFromTo));
    }
}
