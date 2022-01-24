package com.bh.saleland.repository;

import com.bh.saleland.domain.LandTag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LandTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LandTagRepository extends JpaRepository<LandTag, Long> {}
