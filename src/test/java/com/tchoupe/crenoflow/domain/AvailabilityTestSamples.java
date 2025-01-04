package com.tchoupe.crenoflow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AvailabilityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Availability getAvailabilitySample1() {
        return new Availability()
            .id(1L)
            .startTime("startTime1")
            .endTime("endTime1")
            .comment("comment1")
            .videoLink("videoLink1")
            .address("address1");
    }

    public static Availability getAvailabilitySample2() {
        return new Availability()
            .id(2L)
            .startTime("startTime2")
            .endTime("endTime2")
            .comment("comment2")
            .videoLink("videoLink2")
            .address("address2");
    }

    public static Availability getAvailabilityRandomSampleGenerator() {
        return new Availability()
            .id(longCount.incrementAndGet())
            .startTime(UUID.randomUUID().toString())
            .endTime(UUID.randomUUID().toString())
            .comment(UUID.randomUUID().toString())
            .videoLink(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString());
    }
}
