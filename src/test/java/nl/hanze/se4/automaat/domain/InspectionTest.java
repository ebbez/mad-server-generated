package nl.hanze.se4.automaat.domain;

import static nl.hanze.se4.automaat.domain.CarTestSamples.*;
import static nl.hanze.se4.automaat.domain.EmployeeTestSamples.*;
import static nl.hanze.se4.automaat.domain.InspectionPhotoTestSamples.*;
import static nl.hanze.se4.automaat.domain.InspectionTestSamples.*;
import static nl.hanze.se4.automaat.domain.RentalTestSamples.*;
import static nl.hanze.se4.automaat.domain.RepairTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nl.hanze.se4.automaat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InspectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inspection.class);
        Inspection inspection1 = getInspectionSample1();
        Inspection inspection2 = new Inspection();
        assertThat(inspection1).isNotEqualTo(inspection2);

        inspection2.setId(inspection1.getId());
        assertThat(inspection1).isEqualTo(inspection2);

        inspection2 = getInspectionSample2();
        assertThat(inspection1).isNotEqualTo(inspection2);
    }

    @Test
    void photoTest() {
        Inspection inspection = getInspectionRandomSampleGenerator();
        InspectionPhoto inspectionPhotoBack = getInspectionPhotoRandomSampleGenerator();

        inspection.addPhoto(inspectionPhotoBack);
        assertThat(inspection.getPhotos()).containsOnly(inspectionPhotoBack);
        assertThat(inspectionPhotoBack.getInspection()).isEqualTo(inspection);

        inspection.removePhoto(inspectionPhotoBack);
        assertThat(inspection.getPhotos()).doesNotContain(inspectionPhotoBack);
        assertThat(inspectionPhotoBack.getInspection()).isNull();

        inspection.photos(new HashSet<>(Set.of(inspectionPhotoBack)));
        assertThat(inspection.getPhotos()).containsOnly(inspectionPhotoBack);
        assertThat(inspectionPhotoBack.getInspection()).isEqualTo(inspection);

        inspection.setPhotos(new HashSet<>());
        assertThat(inspection.getPhotos()).doesNotContain(inspectionPhotoBack);
        assertThat(inspectionPhotoBack.getInspection()).isNull();
    }

    @Test
    void repairTest() {
        Inspection inspection = getInspectionRandomSampleGenerator();
        Repair repairBack = getRepairRandomSampleGenerator();

        inspection.addRepair(repairBack);
        assertThat(inspection.getRepairs()).containsOnly(repairBack);
        assertThat(repairBack.getInspection()).isEqualTo(inspection);

        inspection.removeRepair(repairBack);
        assertThat(inspection.getRepairs()).doesNotContain(repairBack);
        assertThat(repairBack.getInspection()).isNull();

        inspection.repairs(new HashSet<>(Set.of(repairBack)));
        assertThat(inspection.getRepairs()).containsOnly(repairBack);
        assertThat(repairBack.getInspection()).isEqualTo(inspection);

        inspection.setRepairs(new HashSet<>());
        assertThat(inspection.getRepairs()).doesNotContain(repairBack);
        assertThat(repairBack.getInspection()).isNull();
    }

    @Test
    void carTest() {
        Inspection inspection = getInspectionRandomSampleGenerator();
        Car carBack = getCarRandomSampleGenerator();

        inspection.setCar(carBack);
        assertThat(inspection.getCar()).isEqualTo(carBack);

        inspection.car(null);
        assertThat(inspection.getCar()).isNull();
    }

    @Test
    void employeeTest() {
        Inspection inspection = getInspectionRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        inspection.setEmployee(employeeBack);
        assertThat(inspection.getEmployee()).isEqualTo(employeeBack);

        inspection.employee(null);
        assertThat(inspection.getEmployee()).isNull();
    }

    @Test
    void rentalTest() {
        Inspection inspection = getInspectionRandomSampleGenerator();
        Rental rentalBack = getRentalRandomSampleGenerator();

        inspection.setRental(rentalBack);
        assertThat(inspection.getRental()).isEqualTo(rentalBack);

        inspection.rental(null);
        assertThat(inspection.getRental()).isNull();
    }
}
