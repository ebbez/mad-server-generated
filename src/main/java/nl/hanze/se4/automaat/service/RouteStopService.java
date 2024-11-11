package nl.hanze.se4.automaat.service;

import java.util.Optional;
import nl.hanze.se4.automaat.domain.RouteStop;
import nl.hanze.se4.automaat.repository.RouteStopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link nl.hanze.se4.automaat.domain.RouteStop}.
 */
@Service
@Transactional
public class RouteStopService {

    private static final Logger LOG = LoggerFactory.getLogger(RouteStopService.class);

    private final RouteStopRepository routeStopRepository;

    public RouteStopService(RouteStopRepository routeStopRepository) {
        this.routeStopRepository = routeStopRepository;
    }

    /**
     * Save a routeStop.
     *
     * @param routeStop the entity to save.
     * @return the persisted entity.
     */
    public RouteStop save(RouteStop routeStop) {
        LOG.debug("Request to save RouteStop : {}", routeStop);
        return routeStopRepository.save(routeStop);
    }

    /**
     * Update a routeStop.
     *
     * @param routeStop the entity to save.
     * @return the persisted entity.
     */
    public RouteStop update(RouteStop routeStop) {
        LOG.debug("Request to update RouteStop : {}", routeStop);
        return routeStopRepository.save(routeStop);
    }

    /**
     * Partially update a routeStop.
     *
     * @param routeStop the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RouteStop> partialUpdate(RouteStop routeStop) {
        LOG.debug("Request to partially update RouteStop : {}", routeStop);

        return routeStopRepository
            .findById(routeStop.getId())
            .map(existingRouteStop -> {
                if (routeStop.getNr() != null) {
                    existingRouteStop.setNr(routeStop.getNr());
                }

                return existingRouteStop;
            })
            .map(routeStopRepository::save);
    }

    /**
     * Get all the routeStops.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RouteStop> findAll(Pageable pageable) {
        LOG.debug("Request to get all RouteStops");
        return routeStopRepository.findAll(pageable);
    }

    /**
     * Get one routeStop by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RouteStop> findOne(Long id) {
        LOG.debug("Request to get RouteStop : {}", id);
        return routeStopRepository.findById(id);
    }

    /**
     * Delete the routeStop by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RouteStop : {}", id);
        routeStopRepository.deleteById(id);
    }
}
