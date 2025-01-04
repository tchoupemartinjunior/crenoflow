package com.tchoupe.crenoflow.domain;

import static com.tchoupe.crenoflow.domain.CycleTestSamples.*;
import static com.tchoupe.crenoflow.domain.SubjectCycleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tchoupe.crenoflow.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CycleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cycle.class);
        Cycle cycle1 = getCycleSample1();
        Cycle cycle2 = new Cycle();
        assertThat(cycle1).isNotEqualTo(cycle2);

        cycle2.setId(cycle1.getId());
        assertThat(cycle1).isEqualTo(cycle2);

        cycle2 = getCycleSample2();
        assertThat(cycle1).isNotEqualTo(cycle2);
    }

    @Test
    void subjectCycleTest() throws Exception {
        Cycle cycle = getCycleRandomSampleGenerator();
        SubjectCycle subjectCycleBack = getSubjectCycleRandomSampleGenerator();

        cycle.addSubjectCycle(subjectCycleBack);
        assertThat(cycle.getSubjectCycles()).containsOnly(subjectCycleBack);

        cycle.removeSubjectCycle(subjectCycleBack);
        assertThat(cycle.getSubjectCycles()).doesNotContain(subjectCycleBack);

        cycle.subjectCycles(new HashSet<>(Set.of(subjectCycleBack)));
        assertThat(cycle.getSubjectCycles()).containsOnly(subjectCycleBack);

        cycle.setSubjectCycles(new HashSet<>());
        assertThat(cycle.getSubjectCycles()).doesNotContain(subjectCycleBack);
    }
}
