package nl.hanze.se4.automaat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link nl.hanze.se4.automaat.domain.Inspection} entity. This class is used
 * in {@link nl.hanze.se4.automaat.web.rest.InspectionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inspections?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InspectionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private LongFilter odometer;

    private StringFilter result;

    private StringFilter description;

    private ZonedDateTimeFilter completed;

    private LongFilter photoId;

    private LongFilter repairId;

    private LongFilter carId;

    private LongFilter employeeId;

    private LongFilter rentalId;

    private Boolean distinct;

    public InspectionCriteria() {}

    public InspectionCriteria(InspectionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.odometer = other.optionalOdometer().map(LongFilter::copy).orElse(null);
        this.result = other.optionalResult().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.completed = other.optionalCompleted().map(ZonedDateTimeFilter::copy).orElse(null);
        this.photoId = other.optionalPhotoId().map(LongFilter::copy).orElse(null);
        this.repairId = other.optionalRepairId().map(LongFilter::copy).orElse(null);
        this.carId = other.optionalCarId().map(LongFilter::copy).orElse(null);
        this.employeeId = other.optionalEmployeeId().map(LongFilter::copy).orElse(null);
        this.rentalId = other.optionalRentalId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InspectionCriteria copy() {
        return new InspectionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public LongFilter getOdometer() {
        return odometer;
    }

    public Optional<LongFilter> optionalOdometer() {
        return Optional.ofNullable(odometer);
    }

    public LongFilter odometer() {
        if (odometer == null) {
            setOdometer(new LongFilter());
        }
        return odometer;
    }

    public void setOdometer(LongFilter odometer) {
        this.odometer = odometer;
    }

    public StringFilter getResult() {
        return result;
    }

    public Optional<StringFilter> optionalResult() {
        return Optional.ofNullable(result);
    }

    public StringFilter result() {
        if (result == null) {
            setResult(new StringFilter());
        }
        return result;
    }

    public void setResult(StringFilter result) {
        this.result = result;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public ZonedDateTimeFilter getCompleted() {
        return completed;
    }

    public Optional<ZonedDateTimeFilter> optionalCompleted() {
        return Optional.ofNullable(completed);
    }

    public ZonedDateTimeFilter completed() {
        if (completed == null) {
            setCompleted(new ZonedDateTimeFilter());
        }
        return completed;
    }

    public void setCompleted(ZonedDateTimeFilter completed) {
        this.completed = completed;
    }

    public LongFilter getPhotoId() {
        return photoId;
    }

    public Optional<LongFilter> optionalPhotoId() {
        return Optional.ofNullable(photoId);
    }

    public LongFilter photoId() {
        if (photoId == null) {
            setPhotoId(new LongFilter());
        }
        return photoId;
    }

    public void setPhotoId(LongFilter photoId) {
        this.photoId = photoId;
    }

    public LongFilter getRepairId() {
        return repairId;
    }

    public Optional<LongFilter> optionalRepairId() {
        return Optional.ofNullable(repairId);
    }

    public LongFilter repairId() {
        if (repairId == null) {
            setRepairId(new LongFilter());
        }
        return repairId;
    }

    public void setRepairId(LongFilter repairId) {
        this.repairId = repairId;
    }

    public LongFilter getCarId() {
        return carId;
    }

    public Optional<LongFilter> optionalCarId() {
        return Optional.ofNullable(carId);
    }

    public LongFilter carId() {
        if (carId == null) {
            setCarId(new LongFilter());
        }
        return carId;
    }

    public void setCarId(LongFilter carId) {
        this.carId = carId;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public Optional<LongFilter> optionalEmployeeId() {
        return Optional.ofNullable(employeeId);
    }

    public LongFilter employeeId() {
        if (employeeId == null) {
            setEmployeeId(new LongFilter());
        }
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }

    public LongFilter getRentalId() {
        return rentalId;
    }

    public Optional<LongFilter> optionalRentalId() {
        return Optional.ofNullable(rentalId);
    }

    public LongFilter rentalId() {
        if (rentalId == null) {
            setRentalId(new LongFilter());
        }
        return rentalId;
    }

    public void setRentalId(LongFilter rentalId) {
        this.rentalId = rentalId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InspectionCriteria that = (InspectionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(odometer, that.odometer) &&
            Objects.equals(result, that.result) &&
            Objects.equals(description, that.description) &&
            Objects.equals(completed, that.completed) &&
            Objects.equals(photoId, that.photoId) &&
            Objects.equals(repairId, that.repairId) &&
            Objects.equals(carId, that.carId) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(rentalId, that.rentalId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, odometer, result, description, completed, photoId, repairId, carId, employeeId, rentalId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InspectionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalOdometer().map(f -> "odometer=" + f + ", ").orElse("") +
            optionalResult().map(f -> "result=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalCompleted().map(f -> "completed=" + f + ", ").orElse("") +
            optionalPhotoId().map(f -> "photoId=" + f + ", ").orElse("") +
            optionalRepairId().map(f -> "repairId=" + f + ", ").orElse("") +
            optionalCarId().map(f -> "carId=" + f + ", ").orElse("") +
            optionalEmployeeId().map(f -> "employeeId=" + f + ", ").orElse("") +
            optionalRentalId().map(f -> "rentalId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
