package com.bh.saleland.service;

import com.bh.saleland.domain.Land;
import com.bh.saleland.repository.LandRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Land}.
 */
@Service
@Transactional
public class LandService {

    private final Logger log = LoggerFactory.getLogger(LandService.class);

    private final LandRepository landRepository;

    public LandService(LandRepository landRepository) {
        this.landRepository = landRepository;
    }

    /**
     * Save a land.
     *
     * @param land the entity to save.
     * @return the persisted entity.
     */
    public Land save(Land land) {
        log.debug("Request to save Land : {}", land);
        return landRepository.save(land);
    }

    /**
     * Partially update a land.
     *
     * @param land the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Land> partialUpdate(Land land) {
        log.debug("Request to partially update Land : {}", land);

        return landRepository
            .findById(land.getId())
            .map(existingLand -> {
                if (land.getTitle() != null) {
                    existingLand.setTitle(land.getTitle());
                }
                if (land.getAddress() != null) {
                    existingLand.setAddress(land.getAddress());
                }
                if (land.getStatus() != null) {
                    existingLand.setStatus(land.getStatus());
                }
                if (land.getType() != null) {
                    existingLand.setType(land.getType());
                }
                if (land.getPrice() != null) {
                    existingLand.setPrice(land.getPrice());
                }
                if (land.getInit() != null) {
                    existingLand.setInit(land.getInit());
                }
                if (land.getPriceType() != null) {
                    existingLand.setPriceType(land.getPriceType());
                }
                if (land.getFeeType() != null) {
                    existingLand.setFeeType(land.getFeeType());
                }
                if (land.getDescription() != null) {
                    existingLand.setDescription(land.getDescription());
                }
                if (land.getWidth() != null) {
                    existingLand.setWidth(land.getWidth());
                }
                if (land.getHeight() != null) {
                    existingLand.setHeight(land.getHeight());
                }
                if (land.getArea() != null) {
                    existingLand.setArea(land.getArea());
                }
                if (land.getLatitude() != null) {
                    existingLand.setLatitude(land.getLatitude());
                }
                if (land.getLongitude() != null) {
                    existingLand.setLongitude(land.getLongitude());
                }

                return existingLand;
            })
            .map(landRepository::save);
    }

    /**
     * Get all the lands.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Land> findAll(Pageable pageable) {
        log.debug("Request to get all Lands");
        return landRepository.findAll(pageable);
    }

    /**
     * Get one land by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Land> findOne(Long id) {
        log.debug("Request to get Land : {}", id);
        return landRepository.findById(id);
    }

    /**
     * Delete the land by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Land : {}", id);
        landRepository.deleteById(id);
    }
}
