package com.tchoupe.crenoflow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CourseLocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CourseLocation getCourseLocationSample1() {
        return new CourseLocation().id(1L).address("address1").name("name1");
    }

    public static CourseLocation getCourseLocationSample2() {
        return new CourseLocation().id(2L).address("address2").name("name2");
    }

    public static CourseLocation getCourseLocationRandomSampleGenerator() {
        return new CourseLocation()
            .id(longCount.incrementAndGet())
            .address(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString());
    }
}
