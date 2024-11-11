package nl.hanze.se4.automaat.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RepairTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Repair getRepairSample1() {
        return new Repair().id(1L).description("description1");
    }

    public static Repair getRepairSample2() {
        return new Repair().id(2L).description("description2");
    }

    public static Repair getRepairRandomSampleGenerator() {
        return new Repair().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
