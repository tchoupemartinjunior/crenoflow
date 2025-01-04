package com.tchoupe.crenoflow.domain;

import static com.tchoupe.crenoflow.domain.ProfessionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tchoupe.crenoflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profession.class);
        Profession profession1 = getProfessionSample1();
        Profession profession2 = new Profession();
        assertThat(profession1).isNotEqualTo(profession2);

        profession2.setId(profession1.getId());
        assertThat(profession1).isEqualTo(profession2);

        profession2 = getProfessionSample2();
        assertThat(profession1).isNotEqualTo(profession2);
    }
}
