package nl.hanze.se4.automaat.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RouteFromToTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RouteFromTo getRouteFromToSample1() {
        return new RouteFromTo().id(1L).code("code1").description("description1");
    }

    public static RouteFromTo getRouteFromToSample2() {
        return new RouteFromTo().id(2L).code("code2").description("description2");
    }

    public static RouteFromTo getRouteFromToRandomSampleGenerator() {
        return new RouteFromTo()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
