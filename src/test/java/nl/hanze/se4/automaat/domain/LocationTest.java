package nl.hanze.se4.automaat.domain;

import static nl.hanze.se4.automaat.domain.CustomerTestSamples.*;
import static nl.hanze.se4.automaat.domain.LocationTestSamples.*;
import static nl.hanze.se4.automaat.domain.RouteStopTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import nl.hanze.se4.automaat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Location.class);
        Location location1 = getLocationSample1();
        Location location2 = new Location();
        assertThat(location1).isNotEqualTo(location2);

        location2.setId(location1.getId());
        assertThat(location1).isEqualTo(location2);

        location2 = getLocationSample2();
        assertThat(location1).isNotEqualTo(location2);
    }

    @Test
    void customerTest() {
        Location location = getLocationRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        location.addCustomer(customerBack);
        assertThat(location.getCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getLocation()).isEqualTo(location);

        location.removeCustomer(customerBack);
        assertThat(location.getCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getLocation()).isNull();

        location.customers(new HashSet<>(Set.of(customerBack)));
        assertThat(location.getCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getLocation()).isEqualTo(location);

        location.setCustomers(new HashSet<>());
        assertThat(location.getCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getLocation()).isNull();
    }

    @Test
    void routeStopTest() {
        Location location = getLocationRandomSampleGenerator();
        RouteStop routeStopBack = getRouteStopRandomSampleGenerator();

        location.addRouteStop(routeStopBack);
        assertThat(location.getRouteStops()).containsOnly(routeStopBack);
        assertThat(routeStopBack.getLocation()).isEqualTo(location);

        location.removeRouteStop(routeStopBack);
        assertThat(location.getRouteStops()).doesNotContain(routeStopBack);
        assertThat(routeStopBack.getLocation()).isNull();

        location.routeStops(new HashSet<>(Set.of(routeStopBack)));
        assertThat(location.getRouteStops()).containsOnly(routeStopBack);
        assertThat(routeStopBack.getLocation()).isEqualTo(location);

        location.setRouteStops(new HashSet<>());
        assertThat(location.getRouteStops()).doesNotContain(routeStopBack);
        assertThat(routeStopBack.getLocation()).isNull();
    }
}
