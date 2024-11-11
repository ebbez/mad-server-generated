package nl.hanze.se4.automaat.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nl.hanze.se4.automaat.domain.RouteFromTo;
import nl.hanze.se4.automaat.repository.RouteFromToRepository;
import nl.hanze.se4.automaat.service.RouteFromToService;
import nl.hanze.se4.automaat.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link nl.hanze.se4.automaat.domain.RouteFromTo}.
 */
@RestController
@RequestMapping("/api/route-from-tos")
public class RouteFromToResource {

    private static final Logger LOG = LoggerFactory.getLogger(RouteFromToResource.class);

    private static final String ENTITY_NAME = "routeFromTo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RouteFromToService routeFromToService;

    private final RouteFromToRepository routeFromToRepository;

    public RouteFromToResource(RouteFromToService routeFromToService, RouteFromToRepository routeFromToRepository) {
        this.routeFromToService = routeFromToService;
        this.routeFromToRepository = routeFromToRepository;
    }

    /**
     * {@code POST  /route-from-tos} : Create a new routeFromTo.
     *
     * @param routeFromTo the routeFromTo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new routeFromTo, or with status {@code 400 (Bad Request)} if the routeFromTo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RouteFromTo> createRouteFromTo(@RequestBody RouteFromTo routeFromTo) throws URISyntaxException {
        LOG.debug("REST request to save RouteFromTo : {}", routeFromTo);
        if (routeFromTo.getId() != null) {
            throw new BadRequestAlertException("A new routeFromTo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        routeFromTo = routeFromToService.save(routeFromTo);
        return ResponseEntity.created(new URI("/api/route-from-tos/" + routeFromTo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, routeFromTo.getId().toString()))
            .body(routeFromTo);
    }

    /**
     * {@code PUT  /route-from-tos/:id} : Updates an existing routeFromTo.
     *
     * @param id the id of the routeFromTo to save.
     * @param routeFromTo the routeFromTo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated routeFromTo,
     * or with status {@code 400 (Bad Request)} if the routeFromTo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the routeFromTo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RouteFromTo> updateRouteFromTo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RouteFromTo routeFromTo
    ) throws URISyntaxException {
        LOG.debug("REST request to update RouteFromTo : {}, {}", id, routeFromTo);
        if (routeFromTo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, routeFromTo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!routeFromToRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        routeFromTo = routeFromToService.update(routeFromTo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, routeFromTo.getId().toString()))
            .body(routeFromTo);
    }

    /**
     * {@code PATCH  /route-from-tos/:id} : Partial updates given fields of an existing routeFromTo, field will ignore if it is null
     *
     * @param id the id of the routeFromTo to save.
     * @param routeFromTo the routeFromTo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated routeFromTo,
     * or with status {@code 400 (Bad Request)} if the routeFromTo is not valid,
     * or with status {@code 404 (Not Found)} if the routeFromTo is not found,
     * or with status {@code 500 (Internal Server Error)} if the routeFromTo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RouteFromTo> partialUpdateRouteFromTo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RouteFromTo routeFromTo
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RouteFromTo partially : {}, {}", id, routeFromTo);
        if (routeFromTo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, routeFromTo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!routeFromToRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RouteFromTo> result = routeFromToService.partialUpdate(routeFromTo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, routeFromTo.getId().toString())
        );
    }

    /**
     * {@code GET  /route-from-tos} : get all the routeFromTos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of routeFromTos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RouteFromTo>> getAllRouteFromTos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of RouteFromTos");
        Page<RouteFromTo> page;
        if (eagerload) {
            page = routeFromToService.findAllWithEagerRelationships(pageable);
        } else {
            page = routeFromToService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /route-from-tos/:id} : get the "id" routeFromTo.
     *
     * @param id the id of the routeFromTo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the routeFromTo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RouteFromTo> getRouteFromTo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RouteFromTo : {}", id);
        Optional<RouteFromTo> routeFromTo = routeFromToService.findOne(id);
        return ResponseUtil.wrapOrNotFound(routeFromTo);
    }

    /**
     * {@code DELETE  /route-from-tos/:id} : delete the "id" routeFromTo.
     *
     * @param id the id of the routeFromTo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRouteFromTo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RouteFromTo : {}", id);
        routeFromToService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
