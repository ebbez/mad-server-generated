package nl.hanze.se4.automaat.domain;

import static nl.hanze.se4.automaat.domain.EmployeeTestSamples.*;
import static nl.hanze.se4.automaat.domain.RouteFromToTestSamples.*;
import static nl.hanze.se4.automaat.domain.RouteStopTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nl.hanze.se4.automaat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RouteFromToTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RouteFromTo.class);
        RouteFromTo routeFromTo1 = getRouteFromToSample1();
        RouteFromTo routeFromTo2 = new RouteFromTo();
        assertThat(routeFromTo1).isNotEqualTo(routeFromTo2);

        routeFromTo2.setId(routeFromTo1.getId());
        assertThat(routeFromTo1).isEqualTo(routeFromTo2);

        routeFromTo2 = getRouteFromToSample2();
        assertThat(routeFromTo1).isNotEqualTo(routeFromTo2);
    }

    @Test
    void routeStopTest() {
        RouteFromTo routeFromTo = getRouteFromToRandomSampleGenerator();
        RouteStop routeStopBack = getRouteStopRandomSampleGenerator();

        routeFromTo.addRouteStop(routeStopBack);
        assertThat(routeFromTo.getRouteStops()).containsOnly(routeStopBack);
        assertThat(routeStopBack.getRouteFromTo()).isEqualTo(routeFromTo);

        routeFromTo.removeRouteStop(routeStopBack);
        assertThat(routeFromTo.getRouteStops()).doesNotContain(routeStopBack);
        assertThat(routeStopBack.getRouteFromTo()).isNull();

        routeFromTo.routeStops(new HashSet<>(Set.of(routeStopBack)));
        assertThat(routeFromTo.getRouteStops()).containsOnly(routeStopBack);
        assertThat(routeStopBack.getRouteFromTo()).isEqualTo(routeFromTo);

        routeFromTo.setRouteStops(new HashSet<>());
        assertThat(routeFromTo.getRouteStops()).doesNotContain(routeStopBack);
        assertThat(routeStopBack.getRouteFromTo()).isNull();
    }

    @Test
    void employeeTest() {
        RouteFromTo routeFromTo = getRouteFromToRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        routeFromTo.setEmployee(employeeBack);
        assertThat(routeFromTo.getEmployee()).isEqualTo(employeeBack);

        routeFromTo.employee(null);
        assertThat(routeFromTo.getEmployee()).isNull();
    }
}
