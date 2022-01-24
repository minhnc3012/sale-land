package com.bh.saleland.web.rest;

import com.bh.saleland.domain.LandTag;
import com.bh.saleland.repository.LandTagRepository;
import com.bh.saleland.service.LandTagService;
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
 * REST controller for managing {@link com.bh.saleland.domain.LandTag}.
 */
@RestController
@RequestMapping("/api")
public class LandTagResource {

    private final Logger log = LoggerFactory.getLogger(LandTagResource.class);

    private static final String ENTITY_NAME = "landTag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LandTagService landTagService;

    private final LandTagRepository landTagRepository;

    public LandTagResource(LandTagService landTagService, LandTagRepository landTagRepository) {
        this.landTagService = landTagService;
        this.landTagRepository = landTagRepository;
    }

    /**
     * {@code POST  /land-tags} : Create a new landTag.
     *
     * @param landTag the landTag to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new landTag, or with status {@code 400 (Bad Request)} if the landTag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/land-tags")
    public ResponseEntity<LandTag> createLandTag(@RequestBody LandTag landTag) throws URISyntaxException {
        log.debug("REST request to save LandTag : {}", landTag);
        if (landTag.getId() != null) {
            throw new BadRequestAlertException("A new landTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LandTag result = landTagService.save(landTag);
        return ResponseEntity
            .created(new URI("/api/land-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /land-tags/:id} : Updates an existing landTag.
     *
     * @param id the id of the landTag to save.
     * @param landTag the landTag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated landTag,
     * or with status {@code 400 (Bad Request)} if the landTag is not valid,
     * or with status {@code 500 (Internal Server Error)} if the landTag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/land-tags/{id}")
    public ResponseEntity<LandTag> updateLandTag(@PathVariable(value = "id", required = false) final Long id, @RequestBody LandTag landTag)
        throws URISyntaxException {
        log.debug("REST request to update LandTag : {}, {}", id, landTag);
        if (landTag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, landTag.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!landTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LandTag result = landTagService.save(landTag);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, landTag.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /land-tags/:id} : Partial updates given fields of an existing landTag, field will ignore if it is null
     *
     * @param id the id of the landTag to save.
     * @param landTag the landTag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated landTag,
     * or with status {@code 400 (Bad Request)} if the landTag is not valid,
     * or with status {@code 404 (Not Found)} if the landTag is not found,
     * or with status {@code 500 (Internal Server Error)} if the landTag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/land-tags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LandTag> partialUpdateLandTag(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LandTag landTag
    ) throws URISyntaxException {
        log.debug("REST request to partial update LandTag partially : {}, {}", id, landTag);
        if (landTag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, landTag.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!landTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LandTag> result = landTagService.partialUpdate(landTag);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, landTag.getId().toString())
        );
    }

    /**
     * {@code GET  /land-tags} : get all the landTags.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of landTags in body.
     */
    @GetMapping("/land-tags")
    public ResponseEntity<List<LandTag>> getAllLandTags(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LandTags");
        Page<LandTag> page = landTagService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /land-tags/:id} : get the "id" landTag.
     *
     * @param id the id of the landTag to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the landTag, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/land-tags/{id}")
    public ResponseEntity<LandTag> getLandTag(@PathVariable Long id) {
        log.debug("REST request to get LandTag : {}", id);
        Optional<LandTag> landTag = landTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(landTag);
    }

    /**
     * {@code DELETE  /land-tags/:id} : delete the "id" landTag.
     *
     * @param id the id of the landTag to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/land-tags/{id}")
    public ResponseEntity<Void> deleteLandTag(@PathVariable Long id) {
        log.debug("REST request to delete LandTag : {}", id);
        landTagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
