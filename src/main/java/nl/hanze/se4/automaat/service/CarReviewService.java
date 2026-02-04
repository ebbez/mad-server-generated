package nl.hanze.se4.automaat.service;

import java.util.Optional;
import nl.hanze.se4.automaat.domain.CarReview;
import nl.hanze.se4.automaat.repository.CarReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link nl.hanze.se4.automaat.domain.CarReview}.
 */
@Service
@Transactional
public class CarReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(CarReviewService.class);

    private final CarReviewRepository carReviewRepository;

    public CarReviewService(CarReviewRepository carReviewRepository) {
        this.carReviewRepository = carReviewRepository;
    }

    /**
     * Save a carReview.
     *
     * @param carReview the entity to save.
     * @return the persisted entity.
     */
    public CarReview save(CarReview carReview) {
        LOG.debug("Request to save CarReview : {}", carReview);
        return carReviewRepository.save(carReview);
    }

    /**
     * Update a carReview.
     *
     * @param carReview the entity to save.
     * @return the persisted entity.
     */
    public CarReview update(CarReview carReview) {
        LOG.debug("Request to update CarReview : {}", carReview);
        return carReviewRepository.save(carReview);
    }

    /**
     * Partially update a carReview.
     *
     * @param carReview the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CarReview> partialUpdate(CarReview carReview) {
        LOG.debug("Request to partially update CarReview : {}", carReview);

        return carReviewRepository
            .findById(carReview.getId())
            .map(existingCarReview -> {
                if (carReview.getReview() != null) {
                    existingCarReview.setReview(carReview.getReview());
                }

                return existingCarReview;
            })
            .map(carReviewRepository::save);
    }

    /**
     * Get all the carReviews.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CarReview> findAll(Pageable pageable) {
        LOG.debug("Request to get all CarReviews");
        return carReviewRepository.findAll(pageable);
    }

    /**
     * Get all the carReviews with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CarReview> findAllWithEagerRelationships(Pageable pageable) {
        return carReviewRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one carReview by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CarReview> findOne(Long id) {
        LOG.debug("Request to get CarReview : {}", id);
        return carReviewRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the carReview by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CarReview : {}", id);
        carReviewRepository.deleteById(id);
    }
}
