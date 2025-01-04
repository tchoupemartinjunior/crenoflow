package com.tchoupe.crenoflow.domain;

import static com.tchoupe.crenoflow.domain.BookingTestSamples.*;
import static com.tchoupe.crenoflow.domain.CourseTestSamples.*;
import static com.tchoupe.crenoflow.domain.PersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tchoupe.crenoflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Booking.class);
        Booking booking1 = getBookingSample1();
        Booking booking2 = new Booking();
        assertThat(booking1).isNotEqualTo(booking2);

        booking2.setId(booking1.getId());
        assertThat(booking1).isEqualTo(booking2);

        booking2 = getBookingSample2();
        assertThat(booking1).isNotEqualTo(booking2);
    }

    @Test
    void courseTest() throws Exception {
        Booking booking = getBookingRandomSampleGenerator();
        Course courseBack = getCourseRandomSampleGenerator();

        booking.setCourse(courseBack);
        assertThat(booking.getCourse()).isEqualTo(courseBack);

        booking.course(null);
        assertThat(booking.getCourse()).isNull();
    }

    @Test
    void parentTest() throws Exception {
        Booking booking = getBookingRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        booking.setParent(personBack);
        assertThat(booking.getParent()).isEqualTo(personBack);

        booking.parent(null);
        assertThat(booking.getParent()).isNull();
    }

    @Test
    void studentTest() throws Exception {
        Booking booking = getBookingRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        booking.setStudent(personBack);
        assertThat(booking.getStudent()).isEqualTo(personBack);

        booking.student(null);
        assertThat(booking.getStudent()).isNull();
    }
}
