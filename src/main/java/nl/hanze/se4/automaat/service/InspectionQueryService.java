package nl.hanze.se4.automaat.service;

import jakarta.persistence.criteria.JoinType;
import nl.hanze.se4.automaat.domain.*; // for static metamodels
import nl.hanze.se4.automaat.domain.Inspection;
import nl.hanze.se4.automaat.repository.InspectionRepository;
import nl.hanze.se4.automaat.service.criteria.InspectionCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Inspection} entities in the database.
 * The main input is a {@link InspectionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Inspection} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InspectionQueryService extends QueryService<Inspection> {

    private static final Logger LOG = LoggerFactory.getLogger(InspectionQueryService.class);

    private final InspectionRepository inspectionRepository;

    public InspectionQueryService(InspectionRepository inspectionRepository) {
        this.inspectionRepository = inspectionRepository;
    }

    /**
     * Return a {@link Page} of {@link Inspection} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Inspection> findByCriteria(InspectionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Inspection> specification = createSpecification(criteria);
        return inspectionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InspectionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Inspection> specification = createSpecification(criteria);
        return inspectionRepository.count(specification);
    }

    /**
     * Function to convert {@link InspectionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Inspection> createSpecification(InspectionCriteria criteria) {
        Specification<Inspection> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Inspection_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Inspection_.code));
            }
            if (criteria.getOdometer() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOdometer(), Inspection_.odometer));
            }
            if (criteria.getResult() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResult(), Inspection_.result));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Inspection_.description));
            }
            if (criteria.getCompleted() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompleted(), Inspection_.completed));
            }
            if (criteria.getPhotoId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPhotoId(), root -> root.join(Inspection_.photos, JoinType.LEFT).get(InspectionPhoto_.id))
                );
            }
            if (criteria.getRepairId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRepairId(), root -> root.join(Inspection_.repairs, JoinType.LEFT).get(Repair_.id))
                );
            }
            if (criteria.getCarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCarId(), root -> root.join(Inspection_.car, JoinType.LEFT).get(Car_.id))
                );
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEmployeeId(), root -> root.join(Inspection_.employee, JoinType.LEFT).get(Employee_.id))
                );
            }
            if (criteria.getRentalId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRentalId(), root -> root.join(Inspection_.rental, JoinType.LEFT).get(Rental_.id))
                );
            }
        }
        return specification;
    }
}
