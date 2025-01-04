package com.tchoupe.crenoflow.domain;

import static com.tchoupe.crenoflow.domain.EducationLevelTestSamples.*;
import static com.tchoupe.crenoflow.domain.PersonTestSamples.*;
import static com.tchoupe.crenoflow.domain.ProfessionTestSamples.*;
import static com.tchoupe.crenoflow.domain.SpecialityTestSamples.*;
import static com.tchoupe.crenoflow.domain.SubjectCycleTestSamples.*;
import static com.tchoupe.crenoflow.domain.TeacherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tchoupe.crenoflow.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TeacherTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Teacher.class);
        Teacher teacher1 = getTeacherSample1();
        Teacher teacher2 = new Teacher();
        assertThat(teacher1).isNotEqualTo(teacher2);

        teacher2.setId(teacher1.getId());
        assertThat(teacher1).isEqualTo(teacher2);

        teacher2 = getTeacherSample2();
        assertThat(teacher1).isNotEqualTo(teacher2);
    }

    @Test
    void personTest() throws Exception {
        Teacher teacher = getTeacherRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        teacher.setPerson(personBack);
        assertThat(teacher.getPerson()).isEqualTo(personBack);

        teacher.person(null);
        assertThat(teacher.getPerson()).isNull();
    }

    @Test
    void educationLevelTest() throws Exception {
        Teacher teacher = getTeacherRandomSampleGenerator();
        EducationLevel educationLevelBack = getEducationLevelRandomSampleGenerator();

        teacher.setEducationLevel(educationLevelBack);
        assertThat(teacher.getEducationLevel()).isEqualTo(educationLevelBack);

        teacher.educationLevel(null);
        assertThat(teacher.getEducationLevel()).isNull();
    }

    @Test
    void professionTest() throws Exception {
        Teacher teacher = getTeacherRandomSampleGenerator();
        Profession professionBack = getProfessionRandomSampleGenerator();

        teacher.setProfession(professionBack);
        assertThat(teacher.getProfession()).isEqualTo(professionBack);

        teacher.profession(null);
        assertThat(teacher.getProfession()).isNull();
    }

    @Test
    void specialityTest() throws Exception {
        Teacher teacher = getTeacherRandomSampleGenerator();
        Speciality specialityBack = getSpecialityRandomSampleGenerator();

        teacher.setSpeciality(specialityBack);
        assertThat(teacher.getSpeciality()).isEqualTo(specialityBack);

        teacher.speciality(null);
        assertThat(teacher.getSpeciality()).isNull();
    }

    @Test
    void subjectCycleTest() throws Exception {
        Teacher teacher = getTeacherRandomSampleGenerator();
        SubjectCycle subjectCycleBack = getSubjectCycleRandomSampleGenerator();

        teacher.addSubjectCycle(subjectCycleBack);
        assertThat(teacher.getSubjectCycles()).containsOnly(subjectCycleBack);

        teacher.removeSubjectCycle(subjectCycleBack);
        assertThat(teacher.getSubjectCycles()).doesNotContain(subjectCycleBack);

        teacher.subjectCycles(new HashSet<>(Set.of(subjectCycleBack)));
        assertThat(teacher.getSubjectCycles()).containsOnly(subjectCycleBack);

        teacher.setSubjectCycles(new HashSet<>());
        assertThat(teacher.getSubjectCycles()).doesNotContain(subjectCycleBack);
    }
}
