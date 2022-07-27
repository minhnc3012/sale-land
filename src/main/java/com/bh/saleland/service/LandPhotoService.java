package com.bh.saleland.service;

import com.bh.saleland.domain.LandPhoto;
import com.bh.saleland.repository.LandPhotoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LandPhoto}.
 */
@Service
@Transactional
public class LandPhotoService {

    private final Logger log = LoggerFactory.getLogger(LandPhotoService.class);

    private final LandPhotoRepository landPhotoRepository;

    public LandPhotoService(LandPhotoRepository landPhotoRepository) {
        this.landPhotoRepository = landPhotoRepository;
    }

    /**
     * Save a landPhoto.
     *
     * @param landPhoto the entity to save.
     * @return the persisted entity.
     */
    public LandPhoto save(LandPhoto landPhoto) {
        log.debug("Request to save LandPhoto : {}", landPhoto);
        return landPhotoRepository.save(landPhoto);
    }

    /**
     * Update a landPhoto.
     *
     * @param landPhoto the entity to save.
     * @return the persisted entity.
     */
    public LandPhoto update(LandPhoto landPhoto) {
        log.debug("Request to save LandPhoto : {}", landPhoto);
        return landPhotoRepository.save(landPhoto);
    }

    /**
     * Partially update a landPhoto.
     *
     * @param landPhoto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LandPhoto> partialUpdate(LandPhoto landPhoto) {
        log.debug("Request to partially update LandPhoto : {}", landPhoto);

        return landPhotoRepository
            .findById(landPhoto.getId())
            .map(existingLandPhoto -> {
                if (landPhoto.getImageUrl() != null) {
                    existingLandPhoto.setImageUrl(landPhoto.getImageUrl());
                }

                return existingLandPhoto;
            })
            .map(landPhotoRepository::save);
    }

    /**
     * Get all the landPhotos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LandPhoto> findAll(Pageable pageable) {
        log.debug("Request to get all LandPhotos");
        return landPhotoRepository.findAll(pageable);
    }

    /**
     * Get one landPhoto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LandPhoto> findOne(Long id) {
        log.debug("Request to get LandPhoto : {}", id);
        return landPhotoRepository.findById(id);
    }

    /**
     * Delete the landPhoto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LandPhoto : {}", id);
        landPhotoRepository.deleteById(id);
    }
}
