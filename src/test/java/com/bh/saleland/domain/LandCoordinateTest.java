package com.bh.saleland.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bh.saleland.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LandCoordinateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LandCoordinate.class);
        LandCoordinate landCoordinate1 = new LandCoordinate();
        landCoordinate1.setId(1L);
        LandCoordinate landCoordinate2 = new LandCoordinate();
        landCoordinate2.setId(landCoordinate1.getId());
        assertThat(landCoordinate1).isEqualTo(landCoordinate2);
        landCoordinate2.setId(2L);
        assertThat(landCoordinate1).isNotEqualTo(landCoordinate2);
        landCoordinate1.setId(null);
        assertThat(landCoordinate1).isNotEqualTo(landCoordinate2);
    }
}
