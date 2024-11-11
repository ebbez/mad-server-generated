package nl.hanze.se4.automaat.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RentalCriteriaTest {

    @Test
    void newRentalCriteriaHasAllFiltersNullTest() {
        var rentalCriteria = new RentalCriteria();
        assertThat(rentalCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void rentalCriteriaFluentMethodsCreatesFiltersTest() {
        var rentalCriteria = new RentalCriteria();

        setAllFilters(rentalCriteria);

        assertThat(rentalCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void rentalCriteriaCopyCreatesNullFilterTest() {
        var rentalCriteria = new RentalCriteria();
        var copy = rentalCriteria.copy();

        assertThat(rentalCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(rentalCriteria)
        );
    }

    @Test
    void rentalCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var rentalCriteria = new RentalCriteria();
        setAllFilters(rentalCriteria);

        var copy = rentalCriteria.copy();

        assertThat(rentalCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(rentalCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var rentalCriteria = new RentalCriteria();

        assertThat(rentalCriteria).hasToString("RentalCriteria{}");
    }

    private static void setAllFilters(RentalCriteria rentalCriteria) {
        rentalCriteria.id();
        rentalCriteria.code();
        rentalCriteria.longitude();
        rentalCriteria.latitude();
        rentalCriteria.fromDate();
        rentalCriteria.toDate();
        rentalCriteria.state();
        rentalCriteria.inspectionId();
        rentalCriteria.customerId();
        rentalCriteria.carId();
        rentalCriteria.distinct();
    }

    private static Condition<RentalCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getLongitude()) &&
                condition.apply(criteria.getLatitude()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getToDate()) &&
                condition.apply(criteria.getState()) &&
                condition.apply(criteria.getInspectionId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getCarId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RentalCriteria> copyFiltersAre(RentalCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getLongitude(), copy.getLongitude()) &&
                condition.apply(criteria.getLatitude(), copy.getLatitude()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getToDate(), copy.getToDate()) &&
                condition.apply(criteria.getState(), copy.getState()) &&
                condition.apply(criteria.getInspectionId(), copy.getInspectionId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getCarId(), copy.getCarId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
