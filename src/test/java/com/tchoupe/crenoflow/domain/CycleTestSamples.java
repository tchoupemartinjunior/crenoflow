package com.tchoupe.crenoflow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CycleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cycle getCycleSample1() {
        return new Cycle().id(1L).label("label1");
    }

    public static Cycle getCycleSample2() {
        return new Cycle().id(2L).label("label2");
    }

    public static Cycle getCycleRandomSampleGenerator() {
        return new Cycle().id(longCount.incrementAndGet()).label(UUID.randomUUID().toString());
    }
}
