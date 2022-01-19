package com.bh.saleland.repository;

import com.bh.saleland.domain.LandCoordinate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LandCoordinate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LandCoordinateRepository extends JpaRepository<LandCoordinate, Long> {}
