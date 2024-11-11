package nl.hanze.se4.automaat.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class RouteStopAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRouteStopAllPropertiesEquals(RouteStop expected, RouteStop actual) {
        assertRouteStopAutoGeneratedPropertiesEquals(expected, actual);
        assertRouteStopAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRouteStopAllUpdatablePropertiesEquals(RouteStop expected, RouteStop actual) {
        assertRouteStopUpdatableFieldsEquals(expected, actual);
        assertRouteStopUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRouteStopAutoGeneratedPropertiesEquals(RouteStop expected, RouteStop actual) {
        assertThat(expected)
            .as("Verify RouteStop auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRouteStopUpdatableFieldsEquals(RouteStop expected, RouteStop actual) {
        assertThat(expected)
            .as("Verify RouteStop relevant properties")
            .satisfies(e -> assertThat(e.getNr()).as("check nr").isEqualTo(actual.getNr()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRouteStopUpdatableRelationshipsEquals(RouteStop expected, RouteStop actual) {
        assertThat(expected)
            .as("Verify RouteStop relationships")
            .satisfies(e -> assertThat(e.getRouteFromTo()).as("check routeFromTo").isEqualTo(actual.getRouteFromTo()))
            .satisfies(e -> assertThat(e.getLocation()).as("check location").isEqualTo(actual.getLocation()));
    }
}