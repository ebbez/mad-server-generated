package nl.hanze.se4.automaat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import nl.hanze.se4.automaat.domain.enumeration.RentalState;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link nl.hanze.se4.automaat.domain.Rental} entity. This class is used
 * in {@link nl.hanze.se4.automaat.web.rest.RentalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rentals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RentalCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RentalState
     */
    public static class RentalStateFilter extends Filter<RentalState> {

        public RentalStateFilter() {}

        public RentalStateFilter(RentalStateFilter filter) {
            super(filter);
        }

        @Override
        public RentalStateFilter copy() {
            return new RentalStateFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private FloatFilter longitude;

    private FloatFilter latitude;

    private LocalDateFilter fromDate;

    private LocalDateFilter toDate;

    private RentalStateFilter state;

    private LongFilter inspectionId;

    private LongFilter customerId;

    private LongFilter carId;

    private Boolean distinct;

    public RentalCriteria() {}

    public RentalCriteria(RentalCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.longitude = other.optionalLongitude().map(FloatFilter::copy).orElse(null);
        this.latitude = other.optionalLatitude().map(FloatFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(LocalDateFilter::copy).orElse(null);
        this.toDate = other.optionalToDate().map(LocalDateFilter::copy).orElse(null);
        this.state = other.optionalState().map(RentalStateFilter::copy).orElse(null);
        this.inspectionId = other.optionalInspectionId().map(LongFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.carId = other.optionalCarId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RentalCriteria copy() {
        return new RentalCriteria(this);
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

    public FloatFilter getLongitude() {
        return longitude;
    }

    public Optional<FloatFilter> optionalLongitude() {
        return Optional.ofNullable(longitude);
    }

    public FloatFilter longitude() {
        if (longitude == null) {
            setLongitude(new FloatFilter());
        }
        return longitude;
    }

    public void setLongitude(FloatFilter longitude) {
        this.longitude = longitude;
    }

    public FloatFilter getLatitude() {
        return latitude;
    }

    public Optional<FloatFilter> optionalLatitude() {
        return Optional.ofNullable(latitude);
    }

    public FloatFilter latitude() {
        if (latitude == null) {
            setLatitude(new FloatFilter());
        }
        return latitude;
    }

    public void setLatitude(FloatFilter latitude) {
        this.latitude = latitude;
    }

    public LocalDateFilter getFromDate() {
        return fromDate;
    }

    public Optional<LocalDateFilter> optionalFromDate() {
        return Optional.ofNullable(fromDate);
    }

    public LocalDateFilter fromDate() {
        if (fromDate == null) {
            setFromDate(new LocalDateFilter());
        }
        return fromDate;
    }

    public void setFromDate(LocalDateFilter fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateFilter getToDate() {
        return toDate;
    }

    public Optional<LocalDateFilter> optionalToDate() {
        return Optional.ofNullable(toDate);
    }

    public LocalDateFilter toDate() {
        if (toDate == null) {
            setToDate(new LocalDateFilter());
        }
        return toDate;
    }

    public void setToDate(LocalDateFilter toDate) {
        this.toDate = toDate;
    }

    public RentalStateFilter getState() {
        return state;
    }

    public Optional<RentalStateFilter> optionalState() {
        return Optional.ofNullable(state);
    }

    public RentalStateFilter state() {
        if (state == null) {
            setState(new RentalStateFilter());
        }
        return state;
    }

    public void setState(RentalStateFilter state) {
        this.state = state;
    }

    public LongFilter getInspectionId() {
        return inspectionId;
    }

    public Optional<LongFilter> optionalInspectionId() {
        return Optional.ofNullable(inspectionId);
    }

    public LongFilter inspectionId() {
        if (inspectionId == null) {
            setInspectionId(new LongFilter());
        }
        return inspectionId;
    }

    public void setInspectionId(LongFilter inspectionId) {
        this.inspectionId = inspectionId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public Optional<LongFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public LongFilter customerId() {
        if (customerId == null) {
            setCustomerId(new LongFilter());
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
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
        final RentalCriteria that = (RentalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(toDate, that.toDate) &&
            Objects.equals(state, that.state) &&
            Objects.equals(inspectionId, that.inspectionId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(carId, that.carId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, longitude, latitude, fromDate, toDate, state, inspectionId, customerId, carId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RentalCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalLongitude().map(f -> "longitude=" + f + ", ").orElse("") +
            optionalLatitude().map(f -> "latitude=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalToDate().map(f -> "toDate=" + f + ", ").orElse("") +
            optionalState().map(f -> "state=" + f + ", ").orElse("") +
            optionalInspectionId().map(f -> "inspectionId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalCarId().map(f -> "carId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
