package com.tchoupe.crenoflow.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PhotoPersonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PhotoPerson getPhotoPersonSample1() {
        return new PhotoPerson().id(1L);
    }

    public static PhotoPerson getPhotoPersonSample2() {
        return new PhotoPerson().id(2L);
    }

    public static PhotoPerson getPhotoPersonRandomSampleGenerator() {
        return new PhotoPerson().id(longCount.incrementAndGet());
    }
}
