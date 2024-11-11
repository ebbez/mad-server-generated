package nl.hanze.se4.automaat.domain;

import static nl.hanze.se4.automaat.domain.CarTestSamples.*;
import static nl.hanze.se4.automaat.domain.CustomerTestSamples.*;
import static nl.hanze.se4.automaat.domain.InspectionTestSamples.*;
import static nl.hanze.se4.automaat.domain.RentalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nl.hanze.se4.automaat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RentalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rental.class);
        Rental rental1 = getRentalSample1();
        Rental rental2 = new Rental();
        assertThat(rental1).isNotEqualTo(rental2);

        rental2.setId(rental1.getId());
        assertThat(rental1).isEqualTo(rental2);

        rental2 = getRentalSample2();
        assertThat(rental1).isNotEqualTo(rental2);
    }

    @Test
    void inspectionTest() {
        Rental rental = getRentalRandomSampleGenerator();
        Inspection inspectionBack = getInspectionRandomSampleGenerator();

        rental.addInspection(inspectionBack);
        assertThat(rental.getInspections()).containsOnly(inspectionBack);
        assertThat(inspectionBack.getRental()).isEqualTo(rental);

        rental.removeInspection(inspectionBack);
        assertThat(rental.getInspections()).doesNotContain(inspectionBack);
        assertThat(inspectionBack.getRental()).isNull();

        rental.inspections(new HashSet<>(Set.of(inspectionBack)));
        assertThat(rental.getInspections()).containsOnly(inspectionBack);
        assertThat(inspectionBack.getRental()).isEqualTo(rental);

        rental.setInspections(new HashSet<>());
        assertThat(rental.getInspections()).doesNotContain(inspectionBack);
        assertThat(inspectionBack.getRental()).isNull();
    }

    @Test
    void customerTest() {
        Rental rental = getRentalRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        rental.setCustomer(customerBack);
        assertThat(rental.getCustomer()).isEqualTo(customerBack);

        rental.customer(null);
        assertThat(rental.getCustomer()).isNull();
    }

    @Test
    void carTest() {
        Rental rental = getRentalRandomSampleGenerator();
        Car carBack = getCarRandomSampleGenerator();

        rental.setCar(carBack);
        assertThat(rental.getCar()).isEqualTo(carBack);

        rental.car(null);
        assertThat(rental.getCar()).isNull();
    }
}
