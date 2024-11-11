package nl.hanze.se4.automaat.service;

import java.util.Optional;
import nl.hanze.se4.automaat.domain.InspectionPhoto;
import nl.hanze.se4.automaat.repository.InspectionPhotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link nl.hanze.se4.automaat.domain.InspectionPhoto}.
 */
@Service
@Transactional
public class InspectionPhotoService {

    private static final Logger LOG = LoggerFactory.getLogger(InspectionPhotoService.class);

    private final InspectionPhotoRepository inspectionPhotoRepository;

    public InspectionPhotoService(InspectionPhotoRepository inspectionPhotoRepository) {
        this.inspectionPhotoRepository = inspectionPhotoRepository;
    }

    /**
     * Save a inspectionPhoto.
     *
     * @param inspectionPhoto the entity to save.
     * @return the persisted entity.
     */
    public InspectionPhoto save(InspectionPhoto inspectionPhoto) {
        LOG.debug("Request to save InspectionPhoto : {}", inspectionPhoto);
        return inspectionPhotoRepository.save(inspectionPhoto);
    }

    /**
     * Update a inspectionPhoto.
     *
     * @param inspectionPhoto the entity to save.
     * @return the persisted entity.
     */
    public InspectionPhoto update(InspectionPhoto inspectionPhoto) {
        LOG.debug("Request to update InspectionPhoto : {}", inspectionPhoto);
        return inspectionPhotoRepository.save(inspectionPhoto);
    }

    /**
     * Partially update a inspectionPhoto.
     *
     * @param inspectionPhoto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InspectionPhoto> partialUpdate(InspectionPhoto inspectionPhoto) {
        LOG.debug("Request to partially update InspectionPhoto : {}", inspectionPhoto);

        return inspectionPhotoRepository
            .findById(inspectionPhoto.getId())
            .map(existingInspectionPhoto -> {
                if (inspectionPhoto.getPhoto() != null) {
                    existingInspectionPhoto.setPhoto(inspectionPhoto.getPhoto());
                }
                if (inspectionPhoto.getPhotoContentType() != null) {
                    existingInspectionPhoto.setPhotoContentType(inspectionPhoto.getPhotoContentType());
                }

                return existingInspectionPhoto;
            })
            .map(inspectionPhotoRepository::save);
    }

    /**
     * Get all the inspectionPhotos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InspectionPhoto> findAll(Pageable pageable) {
        LOG.debug("Request to get all InspectionPhotos");
        return inspectionPhotoRepository.findAll(pageable);
    }

    /**
     * Get one inspectionPhoto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InspectionPhoto> findOne(Long id) {
        LOG.debug("Request to get InspectionPhoto : {}", id);
        return inspectionPhotoRepository.findById(id);
    }

    /**
     * Delete the inspectionPhoto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InspectionPhoto : {}", id);
        inspectionPhotoRepository.deleteById(id);
    }
}
