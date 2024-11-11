package nl.hanze.se4.automaat.domain;

import static nl.hanze.se4.automaat.domain.CarTestSamples.*;
import static nl.hanze.se4.automaat.domain.InspectionTestSamples.*;
import static nl.hanze.se4.automaat.domain.RentalTestSamples.*;
import static nl.hanze.se4.automaat.domain.RepairTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nl.hanze.se4.automaat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Car.class);
        Car car1 = getCarSample1();
        Car car2 = new Car();
        assertThat(car1).isNotEqualTo(car2);

        car2.setId(car1.getId());
        assertThat(car1).isEqualTo(car2);

        car2 = getCarSample2();
        assertThat(car1).isNotEqualTo(car2);
    }

    @Test
    void inspectionTest() {
        Car car = getCarRandomSampleGenerator();
        Inspection inspectionBack = getInspectionRandomSampleGenerator();

        car.addInspection(inspectionBack);
        assertThat(car.getInspections()).containsOnly(inspectionBack);
        assertThat(inspectionBack.getCar()).isEqualTo(car);

        car.removeInspection(inspectionBack);
        assertThat(car.getInspections()).doesNotContain(inspectionBack);
        assertThat(inspectionBack.getCar()).isNull();

        car.inspections(new HashSet<>(Set.of(inspectionBack)));
        assertThat(car.getInspections()).containsOnly(inspectionBack);
        assertThat(inspectionBack.getCar()).isEqualTo(car);

        car.setInspections(new HashSet<>());
        assertThat(car.getInspections()).doesNotContain(inspectionBack);
        assertThat(inspectionBack.getCar()).isNull();
    }

    @Test
    void repairTest() {
        Car car = getCarRandomSampleGenerator();
        Repair repairBack = getRepairRandomSampleGenerator();

        car.addRepair(repairBack);
        assertThat(car.getRepairs()).containsOnly(repairBack);
        assertThat(repairBack.getCar()).isEqualTo(car);

        car.removeRepair(repairBack);
        assertThat(car.getRepairs()).doesNotContain(repairBack);
        assertThat(repairBack.getCar()).isNull();

        car.repairs(new HashSet<>(Set.of(repairBack)));
        assertThat(car.getRepairs()).containsOnly(repairBack);
        assertThat(repairBack.getCar()).isEqualTo(car);

        car.setRepairs(new HashSet<>());
        assertThat(car.getRepairs()).doesNotContain(repairBack);
        assertThat(repairBack.getCar()).isNull();
    }

    @Test
    void rentalTest() {
        Car car = getCarRandomSampleGenerator();
        Rental rentalBack = getRentalRandomSampleGenerator();

        car.addRental(rentalBack);
        assertThat(car.getRentals()).containsOnly(rentalBack);
        assertThat(rentalBack.getCar()).isEqualTo(car);

        car.removeRental(rentalBack);
        assertThat(car.getRentals()).doesNotContain(rentalBack);
        assertThat(rentalBack.getCar()).isNull();

        car.rentals(new HashSet<>(Set.of(rentalBack)));
        assertThat(car.getRentals()).containsOnly(rentalBack);
        assertThat(rentalBack.getCar()).isEqualTo(car);

        car.setRentals(new HashSet<>());
        assertThat(car.getRentals()).doesNotContain(rentalBack);
        assertThat(rentalBack.getCar()).isNull();
    }
}
