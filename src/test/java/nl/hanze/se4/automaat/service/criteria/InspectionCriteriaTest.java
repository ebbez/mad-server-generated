package nl.hanze.se4.automaat.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InspectionCriteriaTest {

    @Test
    void newInspectionCriteriaHasAllFiltersNullTest() {
        var inspectionCriteria = new InspectionCriteria();
        assertThat(inspectionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void inspectionCriteriaFluentMethodsCreatesFiltersTest() {
        var inspectionCriteria = new InspectionCriteria();

        setAllFilters(inspectionCriteria);

        assertThat(inspectionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void inspectionCriteriaCopyCreatesNullFilterTest() {
        var inspectionCriteria = new InspectionCriteria();
        var copy = inspectionCriteria.copy();

        assertThat(inspectionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(inspectionCriteria)
        );
    }

    @Test
    void inspectionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var inspectionCriteria = new InspectionCriteria();
        setAllFilters(inspectionCriteria);

        var copy = inspectionCriteria.copy();

        assertThat(inspectionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(inspectionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var inspectionCriteria = new InspectionCriteria();

        assertThat(inspectionCriteria).hasToString("InspectionCriteria{}");
    }

    private static void setAllFilters(InspectionCriteria inspectionCriteria) {
        inspectionCriteria.id();
        inspectionCriteria.code();
        inspectionCriteria.odometer();
        inspectionCriteria.result();
        inspectionCriteria.description();
        inspectionCriteria.completed();
        inspectionCriteria.photoId();
        inspectionCriteria.repairId();
        inspectionCriteria.carId();
        inspectionCriteria.employeeId();
        inspectionCriteria.rentalId();
        inspectionCriteria.distinct();
    }

    private static Condition<InspectionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getOdometer()) &&
                condition.apply(criteria.getResult()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCompleted()) &&
                condition.apply(criteria.getPhotoId()) &&
                condition.apply(criteria.getRepairId()) &&
                condition.apply(criteria.getCarId()) &&
                condition.apply(criteria.getEmployeeId()) &&
                condition.apply(criteria.getRentalId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InspectionCriteria> copyFiltersAre(InspectionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getOdometer(), copy.getOdometer()) &&
                condition.apply(criteria.getResult(), copy.getResult()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCompleted(), copy.getCompleted()) &&
                condition.apply(criteria.getPhotoId(), copy.getPhotoId()) &&
                condition.apply(criteria.getRepairId(), copy.getRepairId()) &&
                condition.apply(criteria.getCarId(), copy.getCarId()) &&
                condition.apply(criteria.getEmployeeId(), copy.getEmployeeId()) &&
                condition.apply(criteria.getRentalId(), copy.getRentalId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
