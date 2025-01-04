package com.tchoupe.crenoflow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SchoolLevelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SchoolLevel getSchoolLevelSample1() {
        return new SchoolLevel().id(1L).label("label1");
    }

    public static SchoolLevel getSchoolLevelSample2() {
        return new SchoolLevel().id(2L).label("label2");
    }

    public static SchoolLevel getSchoolLevelRandomSampleGenerator() {
        return new SchoolLevel().id(longCount.incrementAndGet()).label(UUID.randomUUID().toString());
    }
}
