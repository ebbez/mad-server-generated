package nl.hanze.se4.automaat.service;

import jakarta.persistence.criteria.JoinType;
import nl.hanze.se4.automaat.domain.*; // for static metamodels
import nl.hanze.se4.automaat.domain.Car;
import nl.hanze.se4.automaat.repository.CarRepository;
import nl.hanze.se4.automaat.service.criteria.CarCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Car} entities in the database.
 * The main input is a {@link CarCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Car} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CarQueryService extends QueryService<Car> {

    private static final Logger LOG = LoggerFactory.getLogger(CarQueryService.class);

    private final CarRepository carRepository;

    public CarQueryService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Return a {@link Page} of {@link Car} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Car> findByCriteria(CarCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Car> specification = createSpecification(criteria);
        return carRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CarCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Car> specification = createSpecification(criteria);
        return carRepository.count(specification);
    }

    /**
     * Function to convert {@link CarCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Car> createSpecification(CarCriteria criteria) {
        Specification<Car> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Car_.id),
                buildStringSpecification(criteria.getBrand(), Car_.brand),
                buildStringSpecification(criteria.getModel(), Car_.model),
                buildSpecification(criteria.getFuel(), Car_.fuel),
                buildStringSpecification(criteria.getOptions(), Car_.options),
                buildStringSpecification(criteria.getLicensePlate(), Car_.licensePlate),
                buildRangeSpecification(criteria.getEngineSize(), Car_.engineSize),
                buildRangeSpecification(criteria.getModelYear(), Car_.modelYear),
                buildRangeSpecification(criteria.getSince(), Car_.since),
                buildRangeSpecification(criteria.getPrice(), Car_.price),
                buildRangeSpecification(criteria.getNrOfSeats(), Car_.nrOfSeats),
                buildSpecification(criteria.getBody(), Car_.body),
                buildRangeSpecification(criteria.getLongitude(), Car_.longitude),
                buildRangeSpecification(criteria.getLatitude(), Car_.latitude),
                buildSpecification(criteria.getInspectionId(), root -> root.join(Car_.inspections, JoinType.LEFT).get(Inspection_.id)),
                buildSpecification(criteria.getReviewId(), root -> root.join(Car_.reviews, JoinType.LEFT).get(CarReview_.id)),
                buildSpecification(criteria.getRepairId(), root -> root.join(Car_.repairs, JoinType.LEFT).get(Repair_.id)),
                buildSpecification(criteria.getRentalId(), root -> root.join(Car_.rentals, JoinType.LEFT).get(Rental_.id))
            );
        }
        return specification;
    }
}
