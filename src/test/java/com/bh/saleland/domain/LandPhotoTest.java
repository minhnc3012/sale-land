package com.bh.saleland.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bh.saleland.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LandPhotoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LandPhoto.class);
        LandPhoto landPhoto1 = new LandPhoto();
        landPhoto1.setId(1L);
        LandPhoto landPhoto2 = new LandPhoto();
        landPhoto2.setId(landPhoto1.getId());
        assertThat(landPhoto1).isEqualTo(landPhoto2);
        landPhoto2.setId(2L);
        assertThat(landPhoto1).isNotEqualTo(landPhoto2);
        landPhoto1.setId(null);
        assertThat(landPhoto1).isNotEqualTo(landPhoto2);
    }
}
