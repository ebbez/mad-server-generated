package nl.hanze.se4.automaat.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Customer getCustomerSample1() {
        return new Customer().id(1L).nr(1).lastName("lastName1").firstName("firstName1");
    }

    public static Customer getCustomerSample2() {
        return new Customer().id(2L).nr(2).lastName("lastName2").firstName("firstName2");
    }

    public static Customer getCustomerRandomSampleGenerator() {
        return new Customer()
            .id(longCount.incrementAndGet())
            .nr(intCount.incrementAndGet())
            .lastName(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString());
    }
}
