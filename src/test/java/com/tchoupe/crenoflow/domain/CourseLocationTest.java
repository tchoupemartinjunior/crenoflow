package com.tchoupe.crenoflow.domain;

import static com.tchoupe.crenoflow.domain.CourseLocationTestSamples.*;
import static com.tchoupe.crenoflow.domain.PersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tchoupe.crenoflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseLocation.class);
        CourseLocation courseLocation1 = getCourseLocationSample1();
        CourseLocation courseLocation2 = new CourseLocation();
        assertThat(courseLocation1).isNotEqualTo(courseLocation2);

        courseLocation2.setId(courseLocation1.getId());
        assertThat(courseLocation1).isEqualTo(courseLocation2);

        courseLocation2 = getCourseLocationSample2();
        assertThat(courseLocation1).isNotEqualTo(courseLocation2);
    }

    @Test
    void managerTest() throws Exception {
        CourseLocation courseLocation = getCourseLocationRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        courseLocation.setManager(personBack);
        assertThat(courseLocation.getManager()).isEqualTo(personBack);

        courseLocation.manager(null);
        assertThat(courseLocation.getManager()).isNull();
    }
}
