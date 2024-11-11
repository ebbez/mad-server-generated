package nl.hanze.se4.automaat.service;

import java.util.Optional;
import nl.hanze.se4.automaat.domain.Inspection;
import nl.hanze.se4.automaat.repository.InspectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link nl.hanze.se4.automaat.domain.Inspection}.
 */
@Service
@Transactional
public class InspectionService {

    private static final Logger LOG = LoggerFactory.getLogger(InspectionService.class);

    private final InspectionRepository inspectionRepository;

    public InspectionService(InspectionRepository inspectionRepository) {
        this.inspectionRepository = inspectionRepository;
    }

    /**
     * Save a inspection.
     *
     * @param inspection the entity to save.
     * @return the persisted entity.
     */
    public Inspection save(Inspection inspection) {
        LOG.debug("Request to save Inspection : {}", inspection);
        return inspectionRepository.save(inspection);
    }

    /**
     * Update a inspection.
     *
     * @param inspection the entity to save.
     * @return the persisted entity.
     */
    public Inspection update(Inspection inspection) {
        LOG.debug("Request to update Inspection : {}", inspection);
        return inspectionRepository.save(inspection);
    }

    /**
     * Partially update a inspection.
     *
     * @param inspection the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Inspection> partialUpdate(Inspection inspection) {
        LOG.debug("Request to partially update Inspection : {}", inspection);

        return inspectionRepository
            .findById(inspection.getId())
            .map(existingInspection -> {
                if (inspection.getCode() != null) {
                    existingInspection.setCode(inspection.getCode());
                }
                if (inspection.getOdometer() != null) {
                    existingInspection.setOdometer(inspection.getOdometer());
                }
                if (inspection.getResult() != null) {
                    existingInspection.setResult(inspection.getResult());
                }
                if (inspection.getDescription() != null) {
                    existingInspection.setDescription(inspection.getDescription());
                }
                if (inspection.getPhoto() != null) {
                    existingInspection.setPhoto(inspection.getPhoto());
                }
                if (inspection.getPhotoContentType() != null) {
                    existingInspection.setPhotoContentType(inspection.getPhotoContentType());
                }
                if (inspection.getCompleted() != null) {
                    existingInspection.setCompleted(inspection.getCompleted());
                }

                return existingInspection;
            })
            .map(inspectionRepository::save);
    }

    /**
     * Get all the inspections with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Inspection> findAllWithEagerRelationships(Pageable pageable) {
        return inspectionRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one inspection by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Inspection> findOne(Long id) {
        LOG.debug("Request to get Inspection : {}", id);
        return inspectionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the inspection by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Inspection : {}", id);
        inspectionRepository.deleteById(id);
    }
}
