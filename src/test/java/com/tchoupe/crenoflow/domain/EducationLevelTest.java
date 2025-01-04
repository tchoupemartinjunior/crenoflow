package com.tchoupe.crenoflow.domain;

import static com.tchoupe.crenoflow.domain.EducationLevelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tchoupe.crenoflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EducationLevelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EducationLevel.class);
        EducationLevel educationLevel1 = getEducationLevelSample1();
        EducationLevel educationLevel2 = new EducationLevel();
        assertThat(educationLevel1).isNotEqualTo(educationLevel2);

        educationLevel2.setId(educationLevel1.getId());
        assertThat(educationLevel1).isEqualTo(educationLevel2);

        educationLevel2 = getEducationLevelSample2();
        assertThat(educationLevel1).isNotEqualTo(educationLevel2);
    }
}
