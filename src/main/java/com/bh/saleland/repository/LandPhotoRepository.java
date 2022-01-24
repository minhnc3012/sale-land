package com.bh.saleland.repository;

import com.bh.saleland.domain.LandPhoto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LandPhoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LandPhotoRepository extends JpaRepository<LandPhoto, Long> {}
