package com.tchoupe.crenoflow.domain;

import static com.tchoupe.crenoflow.domain.CycleTestSamples.*;
import static com.tchoupe.crenoflow.domain.SchoolLevelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tchoupe.crenoflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolLevelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolLevel.class);
        SchoolLevel schoolLevel1 = getSchoolLevelSample1();
        SchoolLevel schoolLevel2 = new SchoolLevel();
        assertThat(schoolLevel1).isNotEqualTo(schoolLevel2);

        schoolLevel2.setId(schoolLevel1.getId());
        assertThat(schoolLevel1).isEqualTo(schoolLevel2);

        schoolLevel2 = getSchoolLevelSample2();
        assertThat(schoolLevel1).isNotEqualTo(schoolLevel2);
    }

    @Test
    void cycleTest() throws Exception {
        SchoolLevel schoolLevel = getSchoolLevelRandomSampleGenerator();
        Cycle cycleBack = getCycleRandomSampleGenerator();

        schoolLevel.setCycle(cycleBack);
        assertThat(schoolLevel.getCycle()).isEqualTo(cycleBack);

        schoolLevel.cycle(null);
        assertThat(schoolLevel.getCycle()).isNull();
    }
}
