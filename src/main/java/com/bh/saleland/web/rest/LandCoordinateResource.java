package com.bh.saleland.web.rest;

import com.bh.saleland.domain.LandCoordinate;
import com.bh.saleland.repository.LandCoordinateRepository;
import com.bh.saleland.service.LandCoordinateService;
import com.bh.saleland.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bh.saleland.domain.LandCoordinate}.
 */
@RestController
@RequestMapping("/api")
public class LandCoordinateResource {

    private final Logger log = LoggerFactory.getLogger(LandCoordinateResource.class);

    private static final String ENTITY_NAME = "landCoordinate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LandCoordinateService landCoordinateService;

    private final LandCoordinateRepository landCoordinateRepository;

    public LandCoordinateResource(LandCoordinateService landCoordinateService, LandCoordinateRepository landCoordinateRepository) {
        this.landCoordinateService = landCoordinateService;
        this.landCoordinateRepository = landCoordinateRepository;
    }

    /**
     * {@code POST  /land-coordinates} : Create a new landCoordinate.
     *
     * @param landCoordinate the landCoordinate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new landCoordinate, or with status {@code 400 (Bad Request)} if the landCoordinate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/land-coordinates")
    public ResponseEntity<LandCoordinate> createLandCoordinate(@RequestBody LandCoordinate landCoordinate) throws URISyntaxException {
        log.debug("REST request to save LandCoordinate : {}", landCoordinate);
        if (landCoordinate.getId() != null) {
            throw new BadRequestAlertException("A new landCoordinate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LandCoordinate result = landCoordinateService.save(landCoordinate);
        return ResponseEntity
            .created(new URI("/api/land-coordinates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /land-coordinates/:id} : Updates an existing landCoordinate.
     *
     * @param id the id of the landCoordinate to save.
     * @param landCoordinate the landCoordinate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated landCoordinate,
     * or with status {@code 400 (Bad Request)} if the landCoordinate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the landCoordinate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/land-coordinates/{id}")
    public ResponseEntity<LandCoordinate> updateLandCoordinate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LandCoordinate landCoordinate
    ) throws URISyntaxException {
        log.debug("REST request to update LandCoordinate : {}, {}", id, landCoordinate);
        if (landCoordinate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, landCoordinate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!landCoordinateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LandCoordinate result = landCoordinateService.update(landCoordinate);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, landCoordinate.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /land-coordinates/:id} : Partial updates given fields of an existing landCoordinate, field will ignore if it is null
     *
     * @param id the id of the landCoordinate to save.
     * @param landCoordinate the landCoordinate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated landCoordinate,
     * or with status {@code 400 (Bad Request)} if the landCoordinate is not valid,
     * or with status {@code 404 (Not Found)} if the landCoordinate is not found,
     * or with status {@code 500 (Internal Server Error)} if the landCoordinate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/land-coordinates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LandCoordinate> partialUpdateLandCoordinate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LandCoordinate landCoordinate
    ) throws URISyntaxException {
        log.debug("REST request to partial update LandCoordinate partially : {}, {}", id, landCoordinate);
        if (landCoordinate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, landCoordinate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!landCoordinateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LandCoordinate> result = landCoordinateService.partialUpdate(landCoordinate);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, landCoordinate.getId().toString())
        );
    }

    /**
     * {@code GET  /land-coordinates} : get all the landCoordinates.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of landCoordinates in body.
     */
    @GetMapping("/land-coordinates")
    public ResponseEntity<List<LandCoordinate>> getAllLandCoordinates(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LandCoordinates");
        Page<LandCoordinate> page = landCoordinateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /land-coordinates/:id} : get the "id" landCoordinate.
     *
     * @param id the id of the landCoordinate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the landCoordinate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/land-coordinates/{id}")
    public ResponseEntity<LandCoordinate> getLandCoordinate(@PathVariable Long id) {
        log.debug("REST request to get LandCoordinate : {}", id);
        Optional<LandCoordinate> landCoordinate = landCoordinateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(landCoordinate);
    }

    /**
     * {@code DELETE  /land-coordinates/:id} : delete the "id" landCoordinate.
     *
     * @param id the id of the landCoordinate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/land-coordinates/{id}")
    public ResponseEntity<Void> deleteLandCoordinate(@PathVariable Long id) {
        log.debug("REST request to delete LandCoordinate : {}", id);
        landCoordinateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
