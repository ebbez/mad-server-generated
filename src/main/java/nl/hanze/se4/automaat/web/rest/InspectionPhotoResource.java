package nl.hanze.se4.automaat.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nl.hanze.se4.automaat.domain.InspectionPhoto;
import nl.hanze.se4.automaat.repository.InspectionPhotoRepository;
import nl.hanze.se4.automaat.service.InspectionPhotoService;
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
 * REST controller for managing {@link nl.hanze.se4.automaat.domain.InspectionPhoto}.
 */
@RestController
@RequestMapping("/api/inspection-photos")
public class InspectionPhotoResource {

    private static final Logger LOG = LoggerFactory.getLogger(InspectionPhotoResource.class);

    private static final String ENTITY_NAME = "inspectionPhoto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InspectionPhotoService inspectionPhotoService;

    private final InspectionPhotoRepository inspectionPhotoRepository;

    public InspectionPhotoResource(InspectionPhotoService inspectionPhotoService, InspectionPhotoRepository inspectionPhotoRepository) {
        this.inspectionPhotoService = inspectionPhotoService;
        this.inspectionPhotoRepository = inspectionPhotoRepository;
    }

    /**
     * {@code POST  /inspection-photos} : Create a new inspectionPhoto.
     *
     * @param inspectionPhoto the inspectionPhoto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inspectionPhoto, or with status {@code 400 (Bad Request)} if the inspectionPhoto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InspectionPhoto> createInspectionPhoto(@RequestBody InspectionPhoto inspectionPhoto) throws URISyntaxException {
        LOG.debug("REST request to save InspectionPhoto : {}", inspectionPhoto);
        if (inspectionPhoto.getId() != null) {
            throw new BadRequestAlertException("A new inspectionPhoto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        inspectionPhoto = inspectionPhotoService.save(inspectionPhoto);
        return ResponseEntity.created(new URI("/api/inspection-photos/" + inspectionPhoto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, inspectionPhoto.getId().toString()))
            .body(inspectionPhoto);
    }

    /**
     * {@code PUT  /inspection-photos/:id} : Updates an existing inspectionPhoto.
     *
     * @param id the id of the inspectionPhoto to save.
     * @param inspectionPhoto the inspectionPhoto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectionPhoto,
     * or with status {@code 400 (Bad Request)} if the inspectionPhoto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inspectionPhoto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InspectionPhoto> updateInspectionPhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InspectionPhoto inspectionPhoto
    ) throws URISyntaxException {
        LOG.debug("REST request to update InspectionPhoto : {}, {}", id, inspectionPhoto);
        if (inspectionPhoto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectionPhoto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspectionPhotoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        inspectionPhoto = inspectionPhotoService.update(inspectionPhoto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspectionPhoto.getId().toString()))
            .body(inspectionPhoto);
    }

    /**
     * {@code PATCH  /inspection-photos/:id} : Partial updates given fields of an existing inspectionPhoto, field will ignore if it is null
     *
     * @param id the id of the inspectionPhoto to save.
     * @param inspectionPhoto the inspectionPhoto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectionPhoto,
     * or with status {@code 400 (Bad Request)} if the inspectionPhoto is not valid,
     * or with status {@code 404 (Not Found)} if the inspectionPhoto is not found,
     * or with status {@code 500 (Internal Server Error)} if the inspectionPhoto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InspectionPhoto> partialUpdateInspectionPhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InspectionPhoto inspectionPhoto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InspectionPhoto partially : {}, {}", id, inspectionPhoto);
        if (inspectionPhoto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectionPhoto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspectionPhotoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InspectionPhoto> result = inspectionPhotoService.partialUpdate(inspectionPhoto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspectionPhoto.getId().toString())
        );
    }

    /**
     * {@code GET  /inspection-photos} : get all the inspectionPhotos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inspectionPhotos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InspectionPhoto>> getAllInspectionPhotos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of InspectionPhotos");
        Page<InspectionPhoto> page = inspectionPhotoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inspection-photos/:id} : get the "id" inspectionPhoto.
     *
     * @param id the id of the inspectionPhoto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inspectionPhoto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InspectionPhoto> getInspectionPhoto(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InspectionPhoto : {}", id);
        Optional<InspectionPhoto> inspectionPhoto = inspectionPhotoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inspectionPhoto);
    }

    /**
     * {@code DELETE  /inspection-photos/:id} : delete the "id" inspectionPhoto.
     *
     * @param id the id of the inspectionPhoto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInspectionPhoto(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InspectionPhoto : {}", id);
        inspectionPhotoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
