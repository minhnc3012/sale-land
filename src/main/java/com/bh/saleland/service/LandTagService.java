package com.bh.saleland.service;

import com.bh.saleland.domain.LandTag;
import com.bh.saleland.repository.LandTagRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LandTag}.
 */
@Service
@Transactional
public class LandTagService {

    private final Logger log = LoggerFactory.getLogger(LandTagService.class);

    private final LandTagRepository landTagRepository;

    public LandTagService(LandTagRepository landTagRepository) {
        this.landTagRepository = landTagRepository;
    }

    /**
     * Save a landTag.
     *
     * @param landTag the entity to save.
     * @return the persisted entity.
     */
    public LandTag save(LandTag landTag) {
        log.debug("Request to save LandTag : {}", landTag);
        return landTagRepository.save(landTag);
    }

    /**
     * Partially update a landTag.
     *
     * @param landTag the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LandTag> partialUpdate(LandTag landTag) {
        log.debug("Request to partially update LandTag : {}", landTag);

        return landTagRepository
            .findById(landTag.getId())
            .map(existingLandTag -> {
                if (landTag.getTag() != null) {
                    existingLandTag.setTag(landTag.getTag());
                }

                return existingLandTag;
            })
            .map(landTagRepository::save);
    }

    /**
     * Get all the landTags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LandTag> findAll(Pageable pageable) {
        log.debug("Request to get all LandTags");
        return landTagRepository.findAll(pageable);
    }

    /**
     * Get one landTag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LandTag> findOne(Long id) {
        log.debug("Request to get LandTag : {}", id);
        return landTagRepository.findById(id);
    }

    /**
     * Delete the landTag by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LandTag : {}", id);
        landTagRepository.deleteById(id);
    }
}
