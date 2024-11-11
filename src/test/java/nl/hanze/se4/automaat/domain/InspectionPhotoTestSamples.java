package nl.hanze.se4.automaat.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class InspectionPhotoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InspectionPhoto getInspectionPhotoSample1() {
        return new InspectionPhoto().id(1L);
    }

    public static InspectionPhoto getInspectionPhotoSample2() {
        return new InspectionPhoto().id(2L);
    }

    public static InspectionPhoto getInspectionPhotoRandomSampleGenerator() {
        return new InspectionPhoto().id(longCount.incrementAndGet());
    }
}
