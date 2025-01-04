package com.tchoupe.crenoflow.domain;

import static com.tchoupe.crenoflow.domain.SubjectCycleTestSamples.*;
import static com.tchoupe.crenoflow.domain.SubjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tchoupe.crenoflow.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SubjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subject.class);
        Subject subject1 = getSubjectSample1();
        Subject subject2 = new Subject();
        assertThat(subject1).isNotEqualTo(subject2);

        subject2.setId(subject1.getId());
        assertThat(subject1).isEqualTo(subject2);

        subject2 = getSubjectSample2();
        assertThat(subject1).isNotEqualTo(subject2);
    }

    @Test
    void subjectCycleTest() throws Exception {
        Subject subject = getSubjectRandomSampleGenerator();
        SubjectCycle subjectCycleBack = getSubjectCycleRandomSampleGenerator();

        subject.addSubjectCycle(subjectCycleBack);
        assertThat(subject.getSubjectCycles()).containsOnly(subjectCycleBack);

        subject.removeSubjectCycle(subjectCycleBack);
        assertThat(subject.getSubjectCycles()).doesNotContain(subjectCycleBack);

        subject.subjectCycles(new HashSet<>(Set.of(subjectCycleBack)));
        assertThat(subject.getSubjectCycles()).containsOnly(subjectCycleBack);

        subject.setSubjectCycles(new HashSet<>());
        assertThat(subject.getSubjectCycles()).doesNotContain(subjectCycleBack);
    }
}
