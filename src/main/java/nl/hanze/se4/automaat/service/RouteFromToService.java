package nl.hanze.se4.automaat.service;

import java.util.Optional;
import nl.hanze.se4.automaat.domain.RouteFromTo;
import nl.hanze.se4.automaat.repository.RouteFromToRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link nl.hanze.se4.automaat.domain.RouteFromTo}.
 */
@Service
@Transactional
public class RouteFromToService {

    private static final Logger LOG = LoggerFactory.getLogger(RouteFromToService.class);

    private final RouteFromToRepository routeFromToRepository;

    public RouteFromToService(RouteFromToRepository routeFromToRepository) {
        this.routeFromToRepository = routeFromToRepository;
    }

    /**
     * Save a routeFromTo.
     *
     * @param routeFromTo the entity to save.
     * @return the persisted entity.
     */
    public RouteFromTo save(RouteFromTo routeFromTo) {
        LOG.debug("Request to save RouteFromTo : {}", routeFromTo);
        return routeFromToRepository.save(routeFromTo);
    }

    /**
     * Update a routeFromTo.
     *
     * @param routeFromTo the entity to save.
     * @return the persisted entity.
     */
    public RouteFromTo update(RouteFromTo routeFromTo) {
        LOG.debug("Request to update RouteFromTo : {}", routeFromTo);
        return routeFromToRepository.save(routeFromTo);
    }

    /**
     * Partially update a routeFromTo.
     *
     * @param routeFromTo the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RouteFromTo> partialUpdate(RouteFromTo routeFromTo) {
        LOG.debug("Request to partially update RouteFromTo : {}", routeFromTo);

        return routeFromToRepository
            .findById(routeFromTo.getId())
            .map(existingRouteFromTo -> {
                if (routeFromTo.getCode() != null) {
                    existingRouteFromTo.setCode(routeFromTo.getCode());
                }
                if (routeFromTo.getDescription() != null) {
                    existingRouteFromTo.setDescription(routeFromTo.getDescription());
                }
                if (routeFromTo.getDate() != null) {
                    existingRouteFromTo.setDate(routeFromTo.getDate());
                }

                return existingRouteFromTo;
            })
            .map(routeFromToRepository::save);
    }

    /**
     * Get all the routeFromTos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RouteFromTo> findAll(Pageable pageable) {
        LOG.debug("Request to get all RouteFromTos");
        return routeFromToRepository.findAll(pageable);
    }

    /**
     * Get all the routeFromTos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<RouteFromTo> findAllWithEagerRelationships(Pageable pageable) {
        return routeFromToRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one routeFromTo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RouteFromTo> findOne(Long id) {
        LOG.debug("Request to get RouteFromTo : {}", id);
        return routeFromToRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the routeFromTo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RouteFromTo : {}", id);
        routeFromToRepository.deleteById(id);
    }
}
