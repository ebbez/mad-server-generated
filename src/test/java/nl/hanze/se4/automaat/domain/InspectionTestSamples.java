package nl.hanze.se4.automaat.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InspectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Inspection getInspectionSample1() {
        return new Inspection().id(1L).code("code1").odometer(1L).result("result1").description("description1");
    }

    public static Inspection getInspectionSample2() {
        return new Inspection().id(2L).code("code2").odometer(2L).result("result2").description("description2");
    }

    public static Inspection getInspectionRandomSampleGenerator() {
        return new Inspection()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .odometer(longCount.incrementAndGet())
            .result(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
