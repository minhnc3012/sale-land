package com.bh.saleland.service;

import com.bh.saleland.domain.LandCoordinate;
import com.bh.saleland.repository.LandCoordinateRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LandCoordinate}.
 */
@Service
@Transactional
public class LandCoordinateService {

    private final Logger log = LoggerFactory.getLogger(LandCoordinateService.class);

    private final LandCoordinateRepository landCoordinateRepository;

    public LandCoordinateService(LandCoordinateRepository landCoordinateRepository) {
        this.landCoordinateRepository = landCoordinateRepository;
    }

    /**
     * Save a landCoordinate.
     *
     * @param landCoordinate the entity to save.
     * @return the persisted entity.
     */
    public LandCoordinate save(LandCoordinate landCoordinate) {
        log.debug("Request to save LandCoordinate : {}", landCoordinate);
        return landCoordinateRepository.save(landCoordinate);
    }

    /**
     * Update a landCoordinate.
     *
     * @param landCoordinate the entity to save.
     * @return the persisted entity.
     */
    public LandCoordinate update(LandCoordinate landCoordinate) {
        log.debug("Request to save LandCoordinate : {}", landCoordinate);
        return landCoordinateRepository.save(landCoordinate);
    }

    /**
     * Partially update a landCoordinate.
     *
     * @param landCoordinate the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LandCoordinate> partialUpdate(LandCoordinate landCoordinate) {
        log.debug("Request to partially update LandCoordinate : {}", landCoordinate);

        return landCoordinateRepository
            .findById(landCoordinate.getId())
            .map(existingLandCoordinate -> {
                if (landCoordinate.getLatitude() != null) {
                    existingLandCoordinate.setLatitude(landCoordinate.getLatitude());
                }
                if (landCoordinate.getLongitude() != null) {
                    existingLandCoordinate.setLongitude(landCoordinate.getLongitude());
                }

                return existingLandCoordinate;
            })
            .map(landCoordinateRepository::save);
    }

    /**
     * Get all the landCoordinates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LandCoordinate> findAll(Pageable pageable) {
        log.debug("Request to get all LandCoordinates");
        return landCoordinateRepository.findAll(pageable);
    }

    /**
     * Get one landCoordinate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LandCoordinate> findOne(Long id) {
        log.debug("Request to get LandCoordinate : {}", id);
        return landCoordinateRepository.findById(id);
    }

    /**
     * Delete the landCoordinate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LandCoordinate : {}", id);
        landCoordinateRepository.deleteById(id);
    }
}
