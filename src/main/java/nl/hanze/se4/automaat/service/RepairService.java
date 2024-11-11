package nl.hanze.se4.automaat.service;

import java.util.Optional;
import nl.hanze.se4.automaat.domain.Repair;
import nl.hanze.se4.automaat.repository.RepairRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link nl.hanze.se4.automaat.domain.Repair}.
 */
@Service
@Transactional
public class RepairService {

    private static final Logger LOG = LoggerFactory.getLogger(RepairService.class);

    private final RepairRepository repairRepository;

    public RepairService(RepairRepository repairRepository) {
        this.repairRepository = repairRepository;
    }

    /**
     * Save a repair.
     *
     * @param repair the entity to save.
     * @return the persisted entity.
     */
    public Repair save(Repair repair) {
        LOG.debug("Request to save Repair : {}", repair);
        return repairRepository.save(repair);
    }

    /**
     * Update a repair.
     *
     * @param repair the entity to save.
     * @return the persisted entity.
     */
    public Repair update(Repair repair) {
        LOG.debug("Request to update Repair : {}", repair);
        return repairRepository.save(repair);
    }

    /**
     * Partially update a repair.
     *
     * @param repair the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Repair> partialUpdate(Repair repair) {
        LOG.debug("Request to partially update Repair : {}", repair);

        return repairRepository
            .findById(repair.getId())
            .map(existingRepair -> {
                if (repair.getDescription() != null) {
                    existingRepair.setDescription(repair.getDescription());
                }
                if (repair.getRepairStatus() != null) {
                    existingRepair.setRepairStatus(repair.getRepairStatus());
                }
                if (repair.getDateCompleted() != null) {
                    existingRepair.setDateCompleted(repair.getDateCompleted());
                }

                return existingRepair;
            })
            .map(repairRepository::save);
    }

    /**
     * Get all the repairs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Repair> findAll(Pageable pageable) {
        LOG.debug("Request to get all Repairs");
        return repairRepository.findAll(pageable);
    }

    /**
     * Get all the repairs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Repair> findAllWithEagerRelationships(Pageable pageable) {
        return repairRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one repair by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Repair> findOne(Long id) {
        LOG.debug("Request to get Repair : {}", id);
        return repairRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the repair by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Repair : {}", id);
        repairRepository.deleteById(id);
    }
}
