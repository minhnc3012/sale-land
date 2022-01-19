package com.bh.saleland.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bh.saleland.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Land.class);
        Land land1 = new Land();
        land1.setId(1L);
        Land land2 = new Land();
        land2.setId(land1.getId());
        assertThat(land1).isEqualTo(land2);
        land2.setId(2L);
        assertThat(land1).isNotEqualTo(land2);
        land1.setId(null);
        assertThat(land1).isNotEqualTo(land2);
    }
}
