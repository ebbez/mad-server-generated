package nl.hanze.se4.automaat.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CarCriteriaTest {

    @Test
    void newCarCriteriaHasAllFiltersNullTest() {
        var carCriteria = new CarCriteria();
        assertThat(carCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void carCriteriaFluentMethodsCreatesFiltersTest() {
        var carCriteria = new CarCriteria();

        setAllFilters(carCriteria);

        assertThat(carCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void carCriteriaCopyCreatesNullFilterTest() {
        var carCriteria = new CarCriteria();
        var copy = carCriteria.copy();

        assertThat(carCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(carCriteria)
        );
    }

    @Test
    void carCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var carCriteria = new CarCriteria();
        setAllFilters(carCriteria);

        var copy = carCriteria.copy();

        assertThat(carCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(carCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var carCriteria = new CarCriteria();

        assertThat(carCriteria).hasToString("CarCriteria{}");
    }

    private static void setAllFilters(CarCriteria carCriteria) {
        carCriteria.id();
        carCriteria.brand();
        carCriteria.model();
        carCriteria.fuel();
        carCriteria.options();
        carCriteria.licensePlate();
        carCriteria.engineSize();
        carCriteria.modelYear();
        carCriteria.since();
        carCriteria.price();
        carCriteria.nrOfSeats();
        carCriteria.body();
        carCriteria.longitude();
        carCriteria.latitude();
        carCriteria.inspectionId();
        carCriteria.reviewId();
        carCriteria.repairId();
        carCriteria.rentalId();
        carCriteria.distinct();
    }

    private static Condition<CarCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBrand()) &&
                condition.apply(criteria.getModel()) &&
                condition.apply(criteria.getFuel()) &&
                condition.apply(criteria.getOptions()) &&
                condition.apply(criteria.getLicensePlate()) &&
                condition.apply(criteria.getEngineSize()) &&
                condition.apply(criteria.getModelYear()) &&
                condition.apply(criteria.getSince()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getNrOfSeats()) &&
                condition.apply(criteria.getBody()) &&
                condition.apply(criteria.getLongitude()) &&
                condition.apply(criteria.getLatitude()) &&
                condition.apply(criteria.getInspectionId()) &&
                condition.apply(criteria.getReviewId()) &&
                condition.apply(criteria.getRepairId()) &&
                condition.apply(criteria.getRentalId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CarCriteria> copyFiltersAre(CarCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBrand(), copy.getBrand()) &&
                condition.apply(criteria.getModel(), copy.getModel()) &&
                condition.apply(criteria.getFuel(), copy.getFuel()) &&
                condition.apply(criteria.getOptions(), copy.getOptions()) &&
                condition.apply(criteria.getLicensePlate(), copy.getLicensePlate()) &&
                condition.apply(criteria.getEngineSize(), copy.getEngineSize()) &&
                condition.apply(criteria.getModelYear(), copy.getModelYear()) &&
                condition.apply(criteria.getSince(), copy.getSince()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getNrOfSeats(), copy.getNrOfSeats()) &&
                condition.apply(criteria.getBody(), copy.getBody()) &&
                condition.apply(criteria.getLongitude(), copy.getLongitude()) &&
                condition.apply(criteria.getLatitude(), copy.getLatitude()) &&
                condition.apply(criteria.getInspectionId(), copy.getInspectionId()) &&
                condition.apply(criteria.getReviewId(), copy.getReviewId()) &&
                condition.apply(criteria.getRepairId(), copy.getRepairId()) &&
                condition.apply(criteria.getRentalId(), copy.getRentalId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
