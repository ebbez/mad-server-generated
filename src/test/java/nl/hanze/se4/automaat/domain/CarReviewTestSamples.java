package nl.hanze.se4.automaat.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CarReviewTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CarReview getCarReviewSample1() {
        return new CarReview().id(1L).review("review1");
    }

    public static CarReview getCarReviewSample2() {
        return new CarReview().id(2L).review("review2");
    }

    public static CarReview getCarReviewRandomSampleGenerator() {
        return new CarReview().id(longCount.incrementAndGet()).review(UUID.randomUUID().toString());
    }
}
