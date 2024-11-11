package nl.hanze.se4.automaat.domain;

import static nl.hanze.se4.automaat.domain.CarTestSamples.*;
import static nl.hanze.se4.automaat.domain.EmployeeTestSamples.*;
import static nl.hanze.se4.automaat.domain.InspectionTestSamples.*;
import static nl.hanze.se4.automaat.domain.RepairTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import nl.hanze.se4.automaat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RepairTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Repair.class);
        Repair repair1 = getRepairSample1();
        Repair repair2 = new Repair();
        assertThat(repair1).isNotEqualTo(repair2);

        repair2.setId(repair1.getId());
        assertThat(repair1).isEqualTo(repair2);

        repair2 = getRepairSample2();
        assertThat(repair1).isNotEqualTo(repair2);
    }

    @Test
    void carTest() {
        Repair repair = getRepairRandomSampleGenerator();
        Car carBack = getCarRandomSampleGenerator();

        repair.setCar(carBack);
        assertThat(repair.getCar()).isEqualTo(carBack);

        repair.car(null);
        assertThat(repair.getCar()).isNull();
    }

    @Test
    void employeeTest() {
        Repair repair = getRepairRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        repair.setEmployee(employeeBack);
        assertThat(repair.getEmployee()).isEqualTo(employeeBack);

        repair.employee(null);
        assertThat(repair.getEmployee()).isNull();
    }

    @Test
    void inspectionTest() {
        Repair repair = getRepairRandomSampleGenerator();
        Inspection inspectionBack = getInspectionRandomSampleGenerator();

        repair.setInspection(inspectionBack);
        assertThat(repair.getInspection()).isEqualTo(inspectionBack);

        repair.inspection(null);
        assertThat(repair.getInspection()).isNull();
    }
}
