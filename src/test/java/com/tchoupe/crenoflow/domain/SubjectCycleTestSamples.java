package com.tchoupe.crenoflow.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SubjectCycleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SubjectCycle getSubjectCycleSample1() {
        return new SubjectCycle().id(1L);
    }

    public static SubjectCycle getSubjectCycleSample2() {
        return new SubjectCycle().id(2L);
    }

    public static SubjectCycle getSubjectCycleRandomSampleGenerator() {
        return new SubjectCycle().id(longCount.incrementAndGet());
    }
}
