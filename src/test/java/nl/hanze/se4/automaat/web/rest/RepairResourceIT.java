package nl.hanze.se4.automaat.web.rest;

import static nl.hanze.se4.automaat.domain.RepairAsserts.*;
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
import nl.hanze.se4.automaat.domain.Repair;
import nl.hanze.se4.automaat.domain.enumeration.RepairStatus;
import nl.hanze.se4.automaat.repository.RepairRepository;
import nl.hanze.se4.automaat.service.RepairService;
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
 * Integration tests for the {@link RepairResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RepairResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final RepairStatus DEFAULT_REPAIR_STATUS = RepairStatus.PLANNED;
    private static final RepairStatus UPDATED_REPAIR_STATUS = RepairStatus.DOING;

    private static final LocalDate DEFAULT_DATE_COMPLETED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_COMPLETED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/repairs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RepairRepository repairRepository;

    @Mock
    private RepairRepository repairRepositoryMock;

    @Mock
    private RepairService repairServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRepairMockMvc;

    private Repair repair;

    private Repair insertedRepair;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repair createEntity() {
        return new Repair().description(DEFAULT_DESCRIPTION).repairStatus(DEFAULT_REPAIR_STATUS).dateCompleted(DEFAULT_DATE_COMPLETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repair createUpdatedEntity() {
        return new Repair().description(UPDATED_DESCRIPTION).repairStatus(UPDATED_REPAIR_STATUS).dateCompleted(UPDATED_DATE_COMPLETED);
    }

    @BeforeEach
    public void initTest() {
        repair = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRepair != null) {
            repairRepository.delete(insertedRepair);
            insertedRepair = null;
        }
    }

    @Test
    @Transactional
    void createRepair() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Repair
        var returnedRepair = om.readValue(
            restRepairMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repair)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Repair.class
        );

        // Validate the Repair in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRepairUpdatableFieldsEquals(returnedRepair, getPersistedRepair(returnedRepair));

        insertedRepair = returnedRepair;
    }

    @Test
    @Transactional
    void createRepairWithExistingId() throws Exception {
        // Create the Repair with an existing ID
        repair.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepairMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repair)))
            .andExpect(status().isBadRequest());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRepairs() throws Exception {
        // Initialize the database
        insertedRepair = repairRepository.saveAndFlush(repair);

        // Get all the repairList
        restRepairMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repair.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].repairStatus").value(hasItem(DEFAULT_REPAIR_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateCompleted").value(hasItem(DEFAULT_DATE_COMPLETED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRepairsWithEagerRelationshipsIsEnabled() throws Exception {
        when(repairServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRepairMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(repairServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRepairsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(repairServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRepairMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(repairRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRepair() throws Exception {
        // Initialize the database
        insertedRepair = repairRepository.saveAndFlush(repair);

        // Get the repair
        restRepairMockMvc
            .perform(get(ENTITY_API_URL_ID, repair.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(repair.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.repairStatus").value(DEFAULT_REPAIR_STATUS.toString()))
            .andExpect(jsonPath("$.dateCompleted").value(DEFAULT_DATE_COMPLETED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRepair() throws Exception {
        // Get the repair
        restRepairMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRepair() throws Exception {
        // Initialize the database
        insertedRepair = repairRepository.saveAndFlush(repair);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the repair
        Repair updatedRepair = repairRepository.findById(repair.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRepair are not directly saved in db
        em.detach(updatedRepair);
        updatedRepair.description(UPDATED_DESCRIPTION).repairStatus(UPDATED_REPAIR_STATUS).dateCompleted(UPDATED_DATE_COMPLETED);

        restRepairMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRepair.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRepair))
            )
            .andExpect(status().isOk());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRepairToMatchAllProperties(updatedRepair);
    }

    @Test
    @Transactional
    void putNonExistingRepair() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repair.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepairMockMvc
            .perform(put(ENTITY_API_URL_ID, repair.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repair)))
            .andExpect(status().isBadRequest());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRepair() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repair.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(repair))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRepair() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repair.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(repair)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRepairWithPatch() throws Exception {
        // Initialize the database
        insertedRepair = repairRepository.saveAndFlush(repair);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the repair using partial update
        Repair partialUpdatedRepair = new Repair();
        partialUpdatedRepair.setId(repair.getId());

        restRepairMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepair.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRepair))
            )
            .andExpect(status().isOk());

        // Validate the Repair in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRepairUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRepair, repair), getPersistedRepair(repair));
    }

    @Test
    @Transactional
    void fullUpdateRepairWithPatch() throws Exception {
        // Initialize the database
        insertedRepair = repairRepository.saveAndFlush(repair);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the repair using partial update
        Repair partialUpdatedRepair = new Repair();
        partialUpdatedRepair.setId(repair.getId());

        partialUpdatedRepair.description(UPDATED_DESCRIPTION).repairStatus(UPDATED_REPAIR_STATUS).dateCompleted(UPDATED_DATE_COMPLETED);

        restRepairMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepair.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRepair))
            )
            .andExpect(status().isOk());

        // Validate the Repair in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRepairUpdatableFieldsEquals(partialUpdatedRepair, getPersistedRepair(partialUpdatedRepair));
    }

    @Test
    @Transactional
    void patchNonExistingRepair() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repair.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepairMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, repair.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(repair))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRepair() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repair.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(repair))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRepair() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        repair.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepairMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(repair)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Repair in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRepair() throws Exception {
        // Initialize the database
        insertedRepair = repairRepository.saveAndFlush(repair);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the repair
        restRepairMockMvc
            .perform(delete(ENTITY_API_URL_ID, repair.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return repairRepository.count();
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

    protected Repair getPersistedRepair(Repair repair) {
        return repairRepository.findById(repair.getId()).orElseThrow();
    }

    protected void assertPersistedRepairToMatchAllProperties(Repair expectedRepair) {
        assertRepairAllPropertiesEquals(expectedRepair, getPersistedRepair(expectedRepair));
    }

    protected void assertPersistedRepairToMatchUpdatableProperties(Repair expectedRepair) {
        assertRepairAllUpdatablePropertiesEquals(expectedRepair, getPersistedRepair(expectedRepair));
    }
}
