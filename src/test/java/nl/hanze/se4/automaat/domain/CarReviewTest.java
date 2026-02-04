package nl.hanze.se4.automaat.domain;

import static nl.hanze.se4.automaat.domain.CarReviewTestSamples.*;
import static nl.hanze.se4.automaat.domain.CarTestSamples.*;
import static nl.hanze.se4.automaat.domain.CustomerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import nl.hanze.se4.automaat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarReview.class);
        CarReview carReview1 = getCarReviewSample1();
        CarReview carReview2 = new CarReview();
        assertThat(carReview1).isNotEqualTo(carReview2);

        carReview2.setId(carReview1.getId());
        assertThat(carReview1).isEqualTo(carReview2);

        carReview2 = getCarReviewSample2();
        assertThat(carReview1).isNotEqualTo(carReview2);
    }

    @Test
    void customerTest() {
        CarReview carReview = getCarReviewRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        carReview.setCustomer(customerBack);
        assertThat(carReview.getCustomer()).isEqualTo(customerBack);

        carReview.customer(null);
        assertThat(carReview.getCustomer()).isNull();
    }

    @Test
    void carTest() {
        CarReview carReview = getCarReviewRandomSampleGenerator();
        Car carBack = getCarRandomSampleGenerator();

        carReview.setCar(carBack);
        assertThat(carReview.getCar()).isEqualTo(carBack);

        carReview.car(null);
        assertThat(carReview.getCar()).isNull();
    }
}
