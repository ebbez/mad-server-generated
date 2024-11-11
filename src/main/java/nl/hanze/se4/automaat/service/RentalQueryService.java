package nl.hanze.se4.automaat.service;

import jakarta.persistence.criteria.JoinType;
import nl.hanze.se4.automaat.domain.*; // for static metamodels
import nl.hanze.se4.automaat.domain.Rental;
import nl.hanze.se4.automaat.repository.RentalRepository;
import nl.hanze.se4.automaat.service.criteria.RentalCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Rental} entities in the database.
 * The main input is a {@link RentalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Rental} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RentalQueryService extends QueryService<Rental> {

    private static final Logger LOG = LoggerFactory.getLogger(RentalQueryService.class);

    private final RentalRepository rentalRepository;

    public RentalQueryService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    /**
     * Return a {@link Page} of {@link Rental} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Rental> findByCriteria(RentalCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Rental> specification = createSpecification(criteria);
        return rentalRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RentalCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Rental> specification = createSpecification(criteria);
        return rentalRepository.count(specification);
    }

    /**
     * Function to convert {@link RentalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Rental> createSpecification(RentalCriteria criteria) {
        Specification<Rental> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Rental_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Rental_.code));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), Rental_.longitude));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), Rental_.latitude));
            }
            if (criteria.getFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFromDate(), Rental_.fromDate));
            }
            if (criteria.getToDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getToDate(), Rental_.toDate));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), Rental_.state));
            }
            if (criteria.getInspectionId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getInspectionId(), root -> root.join(Rental_.inspections, JoinType.LEFT).get(Inspection_.id)
                    )
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(Rental_.customer, JoinType.LEFT).get(Customer_.id))
                );
            }
            if (criteria.getCarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCarId(), root -> root.join(Rental_.car, JoinType.LEFT).get(Car_.id))
                );
            }
        }
        return specification;
    }
}
