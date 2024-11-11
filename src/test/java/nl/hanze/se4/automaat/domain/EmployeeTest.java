package nl.hanze.se4.automaat.domain;

import static nl.hanze.se4.automaat.domain.EmployeeTestSamples.*;
import static nl.hanze.se4.automaat.domain.InspectionTestSamples.*;
import static nl.hanze.se4.automaat.domain.RepairTestSamples.*;
import static nl.hanze.se4.automaat.domain.RouteFromToTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nl.hanze.se4.automaat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = getEmployeeSample1();
        Employee employee2 = new Employee();
        assertThat(employee1).isNotEqualTo(employee2);

        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);

        employee2 = getEmployeeSample2();
        assertThat(employee1).isNotEqualTo(employee2);
    }

    @Test
    void inspectionTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        Inspection inspectionBack = getInspectionRandomSampleGenerator();

        employee.addInspection(inspectionBack);
        assertThat(employee.getInspections()).containsOnly(inspectionBack);
        assertThat(inspectionBack.getEmployee()).isEqualTo(employee);

        employee.removeInspection(inspectionBack);
        assertThat(employee.getInspections()).doesNotContain(inspectionBack);
        assertThat(inspectionBack.getEmployee()).isNull();

        employee.inspections(new HashSet<>(Set.of(inspectionBack)));
        assertThat(employee.getInspections()).containsOnly(inspectionBack);
        assertThat(inspectionBack.getEmployee()).isEqualTo(employee);

        employee.setInspections(new HashSet<>());
        assertThat(employee.getInspections()).doesNotContain(inspectionBack);
        assertThat(inspectionBack.getEmployee()).isNull();
    }

    @Test
    void repairTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        Repair repairBack = getRepairRandomSampleGenerator();

        employee.addRepair(repairBack);
        assertThat(employee.getRepairs()).containsOnly(repairBack);
        assertThat(repairBack.getEmployee()).isEqualTo(employee);

        employee.removeRepair(repairBack);
        assertThat(employee.getRepairs()).doesNotContain(repairBack);
        assertThat(repairBack.getEmployee()).isNull();

        employee.repairs(new HashSet<>(Set.of(repairBack)));
        assertThat(employee.getRepairs()).containsOnly(repairBack);
        assertThat(repairBack.getEmployee()).isEqualTo(employee);

        employee.setRepairs(new HashSet<>());
        assertThat(employee.getRepairs()).doesNotContain(repairBack);
        assertThat(repairBack.getEmployee()).isNull();
    }

    @Test
    void routeFromToTest() {
        Employee employee = getEmployeeRandomSampleGenerator();
        RouteFromTo routeFromToBack = getRouteFromToRandomSampleGenerator();

        employee.addRouteFromTo(routeFromToBack);
        assertThat(employee.getRouteFromTos()).containsOnly(routeFromToBack);
        assertThat(routeFromToBack.getEmployee()).isEqualTo(employee);

        employee.removeRouteFromTo(routeFromToBack);
        assertThat(employee.getRouteFromTos()).doesNotContain(routeFromToBack);
        assertThat(routeFromToBack.getEmployee()).isNull();

        employee.routeFromTos(new HashSet<>(Set.of(routeFromToBack)));
        assertThat(employee.getRouteFromTos()).containsOnly(routeFromToBack);
        assertThat(routeFromToBack.getEmployee()).isEqualTo(employee);

        employee.setRouteFromTos(new HashSet<>());
        assertThat(employee.getRouteFromTos()).doesNotContain(routeFromToBack);
        assertThat(routeFromToBack.getEmployee()).isNull();
    }
}
