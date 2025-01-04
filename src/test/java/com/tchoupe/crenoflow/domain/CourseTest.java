package com.tchoupe.crenoflow.domain;

import static com.tchoupe.crenoflow.domain.AvailabilityTestSamples.*;
import static com.tchoupe.crenoflow.domain.CourseLocationTestSamples.*;
import static com.tchoupe.crenoflow.domain.CourseTestSamples.*;
import static com.tchoupe.crenoflow.domain.CycleTestSamples.*;
import static com.tchoupe.crenoflow.domain.SubjectTestSamples.*;
import static com.tchoupe.crenoflow.domain.TeacherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tchoupe.crenoflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = getCourseSample1();
        Course course2 = new Course();
        assertThat(course1).isNotEqualTo(course2);

        course2.setId(course1.getId());
        assertThat(course1).isEqualTo(course2);

        course2 = getCourseSample2();
        assertThat(course1).isNotEqualTo(course2);
    }

    @Test
    void subjectTest() throws Exception {
        Course course = getCourseRandomSampleGenerator();
        Subject subjectBack = getSubjectRandomSampleGenerator();

        course.setSubject(subjectBack);
        assertThat(course.getSubject()).isEqualTo(subjectBack);

        course.subject(null);
        assertThat(course.getSubject()).isNull();
    }

    @Test
    void cycleTest() throws Exception {
        Course course = getCourseRandomSampleGenerator();
        Cycle cycleBack = getCycleRandomSampleGenerator();

        course.setCycle(cycleBack);
        assertThat(course.getCycle()).isEqualTo(cycleBack);

        course.cycle(null);
        assertThat(course.getCycle()).isNull();
    }

    @Test
    void locationTest() throws Exception {
        Course course = getCourseRandomSampleGenerator();
        CourseLocation courseLocationBack = getCourseLocationRandomSampleGenerator();

        course.setLocation(courseLocationBack);
        assertThat(course.getLocation()).isEqualTo(courseLocationBack);

        course.location(null);
        assertThat(course.getLocation()).isNull();
    }

    @Test
    void teacherTest() throws Exception {
        Course course = getCourseRandomSampleGenerator();
        Teacher teacherBack = getTeacherRandomSampleGenerator();

        course.setTeacher(teacherBack);
        assertThat(course.getTeacher()).isEqualTo(teacherBack);

        course.teacher(null);
        assertThat(course.getTeacher()).isNull();
    }

    @Test
    void availabilityTest() throws Exception {
        Course course = getCourseRandomSampleGenerator();
        Availability availabilityBack = getAvailabilityRandomSampleGenerator();

        course.setAvailability(availabilityBack);
        assertThat(course.getAvailability()).isEqualTo(availabilityBack);

        course.availability(null);
        assertThat(course.getAvailability()).isNull();
    }
}
