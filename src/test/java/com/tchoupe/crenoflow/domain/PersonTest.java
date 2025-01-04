package com.tchoupe.crenoflow.domain;

import static com.tchoupe.crenoflow.domain.PersonTestSamples.*;
import static com.tchoupe.crenoflow.domain.PhotoPersonTestSamples.*;
import static com.tchoupe.crenoflow.domain.SchoolLevelTestSamples.*;
import static com.tchoupe.crenoflow.domain.TeacherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tchoupe.crenoflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Person.class);
        Person person1 = getPersonSample1();
        Person person2 = new Person();
        assertThat(person1).isNotEqualTo(person2);

        person2.setId(person1.getId());
        assertThat(person1).isEqualTo(person2);

        person2 = getPersonSample2();
        assertThat(person1).isNotEqualTo(person2);
    }

    @Test
    void schoolLevelTest() throws Exception {
        Person person = getPersonRandomSampleGenerator();
        SchoolLevel schoolLevelBack = getSchoolLevelRandomSampleGenerator();

        person.setSchoolLevel(schoolLevelBack);
        assertThat(person.getSchoolLevel()).isEqualTo(schoolLevelBack);

        person.schoolLevel(null);
        assertThat(person.getSchoolLevel()).isNull();
    }

    @Test
    void teacherTest() throws Exception {
        Person person = getPersonRandomSampleGenerator();
        Teacher teacherBack = getTeacherRandomSampleGenerator();

        person.setTeacher(teacherBack);
        assertThat(person.getTeacher()).isEqualTo(teacherBack);
        assertThat(teacherBack.getPerson()).isEqualTo(person);

        person.teacher(null);
        assertThat(person.getTeacher()).isNull();
        assertThat(teacherBack.getPerson()).isNull();
    }

    @Test
    void photoPersonTest() throws Exception {
        Person person = getPersonRandomSampleGenerator();
        PhotoPerson photoPersonBack = getPhotoPersonRandomSampleGenerator();

        person.setPhotoPerson(photoPersonBack);
        assertThat(person.getPhotoPerson()).isEqualTo(photoPersonBack);
        assertThat(photoPersonBack.getPerson()).isEqualTo(person);

        person.photoPerson(null);
        assertThat(person.getPhotoPerson()).isNull();
        assertThat(photoPersonBack.getPerson()).isNull();
    }
}
