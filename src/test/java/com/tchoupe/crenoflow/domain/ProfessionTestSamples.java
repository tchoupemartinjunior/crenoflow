package com.tchoupe.crenoflow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Profession getProfessionSample1() {
        return new Profession().id(1L).label("label1");
    }

    public static Profession getProfessionSample2() {
        return new Profession().id(2L).label("label2");
    }

    public static Profession getProfessionRandomSampleGenerator() {
        return new Profession().id(longCount.incrementAndGet()).label(UUID.randomUUID().toString());
    }
}
