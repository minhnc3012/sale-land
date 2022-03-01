package com.bh.saleland.web.rest;

import com.bh.saleland.domain.LandPhoto;
import com.bh.saleland.repository.LandPhotoRepository;
import com.bh.saleland.service.LandPhotoService;
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
 * REST controller for managing {@link com.bh.saleland.domain.LandPhoto}.
 */
@RestController
@RequestMapping("/api")
public class LandPhotoResource {

    private final Logger log = LoggerFactory.getLogger(LandPhotoResource.class);

    private static final String ENTITY_NAME = "landPhoto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LandPhotoService landPhotoService;

    private final LandPhotoRepository landPhotoRepository;

    public LandPhotoResource(LandPhotoService landPhotoService, LandPhotoRepository landPhotoRepository) {
        this.landPhotoService = landPhotoService;
        this.landPhotoRepository = landPhotoRepository;
    }

    /**
     * {@code POST  /land-photos} : Create a new landPhoto.
     *
     * @param landPhoto the landPhoto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new landPhoto, or with status {@code 400 (Bad Request)} if the landPhoto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/land-photos")
    public ResponseEntity<LandPhoto> createLandPhoto(@RequestBody LandPhoto landPhoto) throws URISyntaxException {
        log.debug("REST request to save LandPhoto : {}", landPhoto);
        if (landPhoto.getId() != null) {
            throw new BadRequestAlertException("A new landPhoto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LandPhoto result = landPhotoService.save(landPhoto);
        return ResponseEntity
            .created(new URI("/api/land-photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /land-photos/:id} : Updates an existing landPhoto.
     *
     * @param id the id of the landPhoto to save.
     * @param landPhoto the landPhoto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated landPhoto,
     * or with status {@code 400 (Bad Request)} if the landPhoto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the landPhoto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/land-photos/{id}")
    public ResponseEntity<LandPhoto> updateLandPhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LandPhoto landPhoto
    ) throws URISyntaxException {
        log.debug("REST request to update LandPhoto : {}, {}", id, landPhoto);
        if (landPhoto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, landPhoto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!landPhotoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LandPhoto result = landPhotoService.save(landPhoto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, landPhoto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /land-photos/:id} : Partial updates given fields of an existing landPhoto, field will ignore if it is null
     *
     * @param id the id of the landPhoto to save.
     * @param landPhoto the landPhoto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated landPhoto,
     * or with status {@code 400 (Bad Request)} if the landPhoto is not valid,
     * or with status {@code 404 (Not Found)} if the landPhoto is not found,
     * or with status {@code 500 (Internal Server Error)} if the landPhoto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/land-photos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LandPhoto> partialUpdateLandPhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LandPhoto landPhoto
    ) throws URISyntaxException {
        log.debug("REST request to partial update LandPhoto partially : {}, {}", id, landPhoto);
        if (landPhoto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, landPhoto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!landPhotoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LandPhoto> result = landPhotoService.partialUpdate(landPhoto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, landPhoto.getId().toString())
        );
    }

    /**
     * {@code GET  /land-photos} : get all the landPhotos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of landPhotos in body.
     */
    @GetMapping("/land-photos")
    public ResponseEntity<List<LandPhoto>> getAllLandPhotos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LandPhotos");
        Page<LandPhoto> page = landPhotoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /land-photos/:id} : get the "id" landPhoto.
     *
     * @param id the id of the landPhoto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the landPhoto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/land-photos/{id}")
    public ResponseEntity<LandPhoto> getLandPhoto(@PathVariable Long id) {
        log.debug("REST request to get LandPhoto : {}", id);
        Optional<LandPhoto> landPhoto = landPhotoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(landPhoto);
    }

    /**
     * {@code DELETE  /land-photos/:id} : delete the "id" landPhoto.
     *
     * @param id the id of the landPhoto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/land-photos/{id}")
    public ResponseEntity<Void> deleteLandPhoto(@PathVariable Long id) {
        log.debug("REST request to delete LandPhoto : {}", id);
        landPhotoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
