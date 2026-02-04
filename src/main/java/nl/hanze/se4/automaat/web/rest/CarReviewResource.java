package nl.hanze.se4.automaat.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nl.hanze.se4.automaat.domain.CarReview;
import nl.hanze.se4.automaat.repository.CarReviewRepository;
import nl.hanze.se4.automaat.service.CarReviewService;
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
 * REST controller for managing {@link nl.hanze.se4.automaat.domain.CarReview}.
 */
@RestController
@RequestMapping("/api/car-reviews")
public class CarReviewResource {

    private static final Logger LOG = LoggerFactory.getLogger(CarReviewResource.class);

    private static final String ENTITY_NAME = "carReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarReviewService carReviewService;

    private final CarReviewRepository carReviewRepository;

    public CarReviewResource(CarReviewService carReviewService, CarReviewRepository carReviewRepository) {
        this.carReviewService = carReviewService;
        this.carReviewRepository = carReviewRepository;
    }

    /**
     * {@code POST  /car-reviews} : Create a new carReview.
     *
     * @param carReview the carReview to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carReview, or with status {@code 400 (Bad Request)} if the carReview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CarReview> createCarReview(@RequestBody CarReview carReview) throws URISyntaxException {
        LOG.debug("REST request to save CarReview : {}", carReview);
        if (carReview.getId() != null) {
            throw new BadRequestAlertException("A new carReview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        carReview = carReviewService.save(carReview);
        return ResponseEntity.created(new URI("/api/car-reviews/" + carReview.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, carReview.getId().toString()))
            .body(carReview);
    }

    /**
     * {@code PUT  /car-reviews/:id} : Updates an existing carReview.
     *
     * @param id the id of the carReview to save.
     * @param carReview the carReview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carReview,
     * or with status {@code 400 (Bad Request)} if the carReview is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carReview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarReview> updateCarReview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CarReview carReview
    ) throws URISyntaxException {
        LOG.debug("REST request to update CarReview : {}, {}", id, carReview);
        if (carReview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carReview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        carReview = carReviewService.update(carReview);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carReview.getId().toString()))
            .body(carReview);
    }

    /**
     * {@code PATCH  /car-reviews/:id} : Partial updates given fields of an existing carReview, field will ignore if it is null
     *
     * @param id the id of the carReview to save.
     * @param carReview the carReview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carReview,
     * or with status {@code 400 (Bad Request)} if the carReview is not valid,
     * or with status {@code 404 (Not Found)} if the carReview is not found,
     * or with status {@code 500 (Internal Server Error)} if the carReview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarReview> partialUpdateCarReview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CarReview carReview
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CarReview partially : {}, {}", id, carReview);
        if (carReview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carReview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarReview> result = carReviewService.partialUpdate(carReview);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carReview.getId().toString())
        );
    }

    /**
     * {@code GET  /car-reviews} : get all the carReviews.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carReviews in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CarReview>> getAllCarReviews(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of CarReviews");
        Page<CarReview> page;
        if (eagerload) {
            page = carReviewService.findAllWithEagerRelationships(pageable);
        } else {
            page = carReviewService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /car-reviews/:id} : get the "id" carReview.
     *
     * @param id the id of the carReview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carReview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarReview> getCarReview(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CarReview : {}", id);
        Optional<CarReview> carReview = carReviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carReview);
    }

    /**
     * {@code DELETE  /car-reviews/:id} : delete the "id" carReview.
     *
     * @param id the id of the carReview to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarReview(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CarReview : {}", id);
        carReviewService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
