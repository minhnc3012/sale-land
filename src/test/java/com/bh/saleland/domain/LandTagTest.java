package com.bh.saleland.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.bh.saleland.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LandTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LandTag.class);
        LandTag landTag1 = new LandTag();
        landTag1.setId(1L);
        LandTag landTag2 = new LandTag();
        landTag2.setId(landTag1.getId());
        assertThat(landTag1).isEqualTo(landTag2);
        landTag2.setId(2L);
        assertThat(landTag1).isNotEqualTo(landTag2);
        landTag1.setId(null);
        assertThat(landTag1).isNotEqualTo(landTag2);
    }
}
