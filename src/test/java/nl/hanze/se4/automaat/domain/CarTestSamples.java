package nl.hanze.se4.automaat.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CarTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Car getCarSample1() {
        return new Car()
            .id(1L)
            .brand("brand1")
            .model("model1")
            .options("options1")
            .licensePlate("licensePlate1")
            .engineSize(1)
            .modelYear(1)
            .nrOfSeats(1);
    }

    public static Car getCarSample2() {
        return new Car()
            .id(2L)
            .brand("brand2")
            .model("model2")
            .options("options2")
            .licensePlate("licensePlate2")
            .engineSize(2)
            .modelYear(2)
            .nrOfSeats(2);
    }

    public static Car getCarRandomSampleGenerator() {
        return new Car()
            .id(longCount.incrementAndGet())
            .brand(UUID.randomUUID().toString())
            .model(UUID.randomUUID().toString())
            .options(UUID.randomUUID().toString())
            .licensePlate(UUID.randomUUID().toString())
            .engineSize(intCount.incrementAndGet())
            .modelYear(intCount.incrementAndGet())
            .nrOfSeats(intCount.incrementAndGet());
    }
}
