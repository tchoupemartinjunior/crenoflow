package com.tchoupe.crenoflow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EducationLevelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EducationLevel getEducationLevelSample1() {
        return new EducationLevel().id(1L).label("label1");
    }

    public static EducationLevel getEducationLevelSample2() {
        return new EducationLevel().id(2L).label("label2");
    }

    public static EducationLevel getEducationLevelRandomSampleGenerator() {
        return new EducationLevel().id(longCount.incrementAndGet()).label(UUID.randomUUID().toString());
    }
}
