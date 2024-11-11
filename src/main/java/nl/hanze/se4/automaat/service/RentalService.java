package nl.hanze.se4.automaat.service;

import java.util.Optional;
import nl.hanze.se4.automaat.domain.Rental;
import nl.hanze.se4.automaat.repository.RentalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link nl.hanze.se4.automaat.domain.Rental}.
 */
@Service
@Transactional
public class RentalService {

    private static final Logger LOG = LoggerFactory.getLogger(RentalService.class);

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    /**
     * Save a rental.
     *
     * @param rental the entity to save.
     * @return the persisted entity.
     */
    public Rental save(Rental rental) {
        LOG.debug("Request to save Rental : {}", rental);
        return rentalRepository.save(rental);
    }

    /**
     * Update a rental.
     *
     * @param rental the entity to save.
     * @return the persisted entity.
     */
    public Rental update(Rental rental) {
        LOG.debug("Request to update Rental : {}", rental);
        return rentalRepository.save(rental);
    }

    /**
     * Partially update a rental.
     *
     * @param rental the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Rental> partialUpdate(Rental rental) {
        LOG.debug("Request to partially update Rental : {}", rental);

        return rentalRepository
            .findById(rental.getId())
            .map(existingRental -> {
                if (rental.getCode() != null) {
                    existingRental.setCode(rental.getCode());
                }
                if (rental.getLongitude() != null) {
                    existingRental.setLongitude(rental.getLongitude());
                }
                if (rental.getLatitude() != null) {
                    existingRental.setLatitude(rental.getLatitude());
                }
                if (rental.getFromDate() != null) {
                    existingRental.setFromDate(rental.getFromDate());
                }
                if (rental.getToDate() != null) {
                    existingRental.setToDate(rental.getToDate());
                }
                if (rental.getState() != null) {
                    existingRental.setState(rental.getState());
                }

                return existingRental;
            })
            .map(rentalRepository::save);
    }

    /**
     * Get all the rentals with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Rental> findAllWithEagerRelationships(Pageable pageable) {
        return rentalRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one rental by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Rental> findOne(Long id) {
        LOG.debug("Request to get Rental : {}", id);
        return rentalRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the rental by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Rental : {}", id);
        rentalRepository.deleteById(id);
    }
}
