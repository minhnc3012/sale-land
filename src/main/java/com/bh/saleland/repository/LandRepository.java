package com.bh.saleland.repository;

import com.bh.saleland.domain.Land;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Land entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LandRepository extends JpaRepository<Land, Long> {}
