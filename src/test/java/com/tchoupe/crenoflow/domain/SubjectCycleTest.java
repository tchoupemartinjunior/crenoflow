package com.tchoupe.crenoflow.domain;

import static com.tchoupe.crenoflow.domain.CycleTestSamples.*;
import static com.tchoupe.crenoflow.domain.SubjectCycleTestSamples.*;
import static com.tchoupe.crenoflow.domain.SubjectTestSamples.*;
import static com.tchoupe.crenoflow.domain.TeacherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tchoupe.crenoflow.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SubjectCycleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubjectCycle.class);
        SubjectCycle subjectCycle1 = getSubjectCycleSample1();
        SubjectCycle subjectCycle2 = new SubjectCycle();
        assertThat(subjectCycle1).isNotEqualTo(subjectCycle2);

        subjectCycle2.setId(subjectCycle1.getId());
        assertThat(subjectCycle1).isEqualTo(subjectCycle2);

        subjectCycle2 = getSubjectCycleSample2();
        assertThat(subjectCycle1).isNotEqualTo(subjectCycle2);
    }

    @Test
    void teacherTest() throws Exception {
        SubjectCycle subjectCycle = getSubjectCycleRandomSampleGenerator();
        Teacher teacherBack = getTeacherRandomSampleGenerator();

        subjectCycle.addTeacher(teacherBack);
        assertThat(subjectCycle.getTeachers()).containsOnly(teacherBack);
        assertThat(teacherBack.getSubjectCycles()).containsOnly(subjectCycle);

        subjectCycle.removeTeacher(teacherBack);
        assertThat(subjectCycle.getTeachers()).doesNotContain(teacherBack);
        assertThat(teacherBack.getSubjectCycles()).doesNotContain(subjectCycle);

        subjectCycle.teachers(new HashSet<>(Set.of(teacherBack)));
        assertThat(subjectCycle.getTeachers()).containsOnly(teacherBack);
        assertThat(teacherBack.getSubjectCycles()).containsOnly(subjectCycle);

        subjectCycle.setTeachers(new HashSet<>());
        assertThat(subjectCycle.getTeachers()).doesNotContain(teacherBack);
        assertThat(teacherBack.getSubjectCycles()).doesNotContain(subjectCycle);
    }

    @Test
    void subjectTest() throws Exception {
        SubjectCycle subjectCycle = getSubjectCycleRandomSampleGenerator();
        Subject subjectBack = getSubjectRandomSampleGenerator();

        subjectCycle.addSubject(subjectBack);
        assertThat(subjectCycle.getSubjects()).containsOnly(subjectBack);
        assertThat(subjectBack.getSubjectCycles()).containsOnly(subjectCycle);

        subjectCycle.removeSubject(subjectBack);
        assertThat(subjectCycle.getSubjects()).doesNotContain(subjectBack);
        assertThat(subjectBack.getSubjectCycles()).doesNotContain(subjectCycle);

        subjectCycle.subjects(new HashSet<>(Set.of(subjectBack)));
        assertThat(subjectCycle.getSubjects()).containsOnly(subjectBack);
        assertThat(subjectBack.getSubjectCycles()).containsOnly(subjectCycle);

        subjectCycle.setSubjects(new HashSet<>());
        assertThat(subjectCycle.getSubjects()).doesNotContain(subjectBack);
        assertThat(subjectBack.getSubjectCycles()).doesNotContain(subjectCycle);
    }

    @Test
    void cycleTest() throws Exception {
        SubjectCycle subjectCycle = getSubjectCycleRandomSampleGenerator();
        Cycle cycleBack = getCycleRandomSampleGenerator();

        subjectCycle.addCycle(cycleBack);
        assertThat(subjectCycle.getCycles()).containsOnly(cycleBack);
        assertThat(cycleBack.getSubjectCycles()).containsOnly(subjectCycle);

        subjectCycle.removeCycle(cycleBack);
        assertThat(subjectCycle.getCycles()).doesNotContain(cycleBack);
        assertThat(cycleBack.getSubjectCycles()).doesNotContain(subjectCycle);

        subjectCycle.cycles(new HashSet<>(Set.of(cycleBack)));
        assertThat(subjectCycle.getCycles()).containsOnly(cycleBack);
        assertThat(cycleBack.getSubjectCycles()).containsOnly(subjectCycle);

        subjectCycle.setCycles(new HashSet<>());
        assertThat(subjectCycle.getCycles()).doesNotContain(cycleBack);
        assertThat(cycleBack.getSubjectCycles()).doesNotContain(subjectCycle);
    }
}
