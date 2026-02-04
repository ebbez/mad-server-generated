package nl.hanze.se4.automaat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import nl.hanze.se4.automaat.domain.enumeration.Body;
import nl.hanze.se4.automaat.domain.enumeration.Fuel;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link nl.hanze.se4.automaat.domain.Car} entity. This class is used
 * in {@link nl.hanze.se4.automaat.web.rest.CarResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Fuel
     */
    public static class FuelFilter extends Filter<Fuel> {

        public FuelFilter() {}

        public FuelFilter(FuelFilter filter) {
            super(filter);
        }

        @Override
        public FuelFilter copy() {
            return new FuelFilter(this);
        }
    }

    /**
     * Class for filtering Body
     */
    public static class BodyFilter extends Filter<Body> {

        public BodyFilter() {}

        public BodyFilter(BodyFilter filter) {
            super(filter);
        }

        @Override
        public BodyFilter copy() {
            return new BodyFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter brand;

    private StringFilter model;

    private FuelFilter fuel;

    private StringFilter options;

    private StringFilter licensePlate;

    private IntegerFilter engineSize;

    private IntegerFilter modelYear;

    private LocalDateFilter since;

    private FloatFilter price;

    private IntegerFilter nrOfSeats;

    private BodyFilter body;

    private FloatFilter longitude;

    private FloatFilter latitude;

    private LongFilter inspectionId;

    private LongFilter reviewId;

    private LongFilter repairId;

    private LongFilter rentalId;

    private Boolean distinct;

    public CarCriteria() {}

    public CarCriteria(CarCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.brand = other.optionalBrand().map(StringFilter::copy).orElse(null);
        this.model = other.optionalModel().map(StringFilter::copy).orElse(null);
        this.fuel = other.optionalFuel().map(FuelFilter::copy).orElse(null);
        this.options = other.optionalOptions().map(StringFilter::copy).orElse(null);
        this.licensePlate = other.optionalLicensePlate().map(StringFilter::copy).orElse(null);
        this.engineSize = other.optionalEngineSize().map(IntegerFilter::copy).orElse(null);
        this.modelYear = other.optionalModelYear().map(IntegerFilter::copy).orElse(null);
        this.since = other.optionalSince().map(LocalDateFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(FloatFilter::copy).orElse(null);
        this.nrOfSeats = other.optionalNrOfSeats().map(IntegerFilter::copy).orElse(null);
        this.body = other.optionalBody().map(BodyFilter::copy).orElse(null);
        this.longitude = other.optionalLongitude().map(FloatFilter::copy).orElse(null);
        this.latitude = other.optionalLatitude().map(FloatFilter::copy).orElse(null);
        this.inspectionId = other.optionalInspectionId().map(LongFilter::copy).orElse(null);
        this.reviewId = other.optionalReviewId().map(LongFilter::copy).orElse(null);
        this.repairId = other.optionalRepairId().map(LongFilter::copy).orElse(null);
        this.rentalId = other.optionalRentalId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CarCriteria copy() {
        return new CarCriteria(this);
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

    public StringFilter getBrand() {
        return brand;
    }

    public Optional<StringFilter> optionalBrand() {
        return Optional.ofNullable(brand);
    }

    public StringFilter brand() {
        if (brand == null) {
            setBrand(new StringFilter());
        }
        return brand;
    }

    public void setBrand(StringFilter brand) {
        this.brand = brand;
    }

    public StringFilter getModel() {
        return model;
    }

    public Optional<StringFilter> optionalModel() {
        return Optional.ofNullable(model);
    }

    public StringFilter model() {
        if (model == null) {
            setModel(new StringFilter());
        }
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public FuelFilter getFuel() {
        return fuel;
    }

    public Optional<FuelFilter> optionalFuel() {
        return Optional.ofNullable(fuel);
    }

    public FuelFilter fuel() {
        if (fuel == null) {
            setFuel(new FuelFilter());
        }
        return fuel;
    }

    public void setFuel(FuelFilter fuel) {
        this.fuel = fuel;
    }

    public StringFilter getOptions() {
        return options;
    }

    public Optional<StringFilter> optionalOptions() {
        return Optional.ofNullable(options);
    }

    public StringFilter options() {
        if (options == null) {
            setOptions(new StringFilter());
        }
        return options;
    }

    public void setOptions(StringFilter options) {
        this.options = options;
    }

    public StringFilter getLicensePlate() {
        return licensePlate;
    }

    public Optional<StringFilter> optionalLicensePlate() {
        return Optional.ofNullable(licensePlate);
    }

    public StringFilter licensePlate() {
        if (licensePlate == null) {
            setLicensePlate(new StringFilter());
        }
        return licensePlate;
    }

    public void setLicensePlate(StringFilter licensePlate) {
        this.licensePlate = licensePlate;
    }

    public IntegerFilter getEngineSize() {
        return engineSize;
    }

    public Optional<IntegerFilter> optionalEngineSize() {
        return Optional.ofNullable(engineSize);
    }

    public IntegerFilter engineSize() {
        if (engineSize == null) {
            setEngineSize(new IntegerFilter());
        }
        return engineSize;
    }

    public void setEngineSize(IntegerFilter engineSize) {
        this.engineSize = engineSize;
    }

    public IntegerFilter getModelYear() {
        return modelYear;
    }

    public Optional<IntegerFilter> optionalModelYear() {
        return Optional.ofNullable(modelYear);
    }

    public IntegerFilter modelYear() {
        if (modelYear == null) {
            setModelYear(new IntegerFilter());
        }
        return modelYear;
    }

    public void setModelYear(IntegerFilter modelYear) {
        this.modelYear = modelYear;
    }

    public LocalDateFilter getSince() {
        return since;
    }

    public Optional<LocalDateFilter> optionalSince() {
        return Optional.ofNullable(since);
    }

    public LocalDateFilter since() {
        if (since == null) {
            setSince(new LocalDateFilter());
        }
        return since;
    }

    public void setSince(LocalDateFilter since) {
        this.since = since;
    }

    public FloatFilter getPrice() {
        return price;
    }

    public Optional<FloatFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public FloatFilter price() {
        if (price == null) {
            setPrice(new FloatFilter());
        }
        return price;
    }

    public void setPrice(FloatFilter price) {
        this.price = price;
    }

    public IntegerFilter getNrOfSeats() {
        return nrOfSeats;
    }

    public Optional<IntegerFilter> optionalNrOfSeats() {
        return Optional.ofNullable(nrOfSeats);
    }

    public IntegerFilter nrOfSeats() {
        if (nrOfSeats == null) {
            setNrOfSeats(new IntegerFilter());
        }
        return nrOfSeats;
    }

    public void setNrOfSeats(IntegerFilter nrOfSeats) {
        this.nrOfSeats = nrOfSeats;
    }

    public BodyFilter getBody() {
        return body;
    }

    public Optional<BodyFilter> optionalBody() {
        return Optional.ofNullable(body);
    }

    public BodyFilter body() {
        if (body == null) {
            setBody(new BodyFilter());
        }
        return body;
    }

    public void setBody(BodyFilter body) {
        this.body = body;
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

    public LongFilter getReviewId() {
        return reviewId;
    }

    public Optional<LongFilter> optionalReviewId() {
        return Optional.ofNullable(reviewId);
    }

    public LongFilter reviewId() {
        if (reviewId == null) {
            setReviewId(new LongFilter());
        }
        return reviewId;
    }

    public void setReviewId(LongFilter reviewId) {
        this.reviewId = reviewId;
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
        final CarCriteria that = (CarCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(brand, that.brand) &&
            Objects.equals(model, that.model) &&
            Objects.equals(fuel, that.fuel) &&
            Objects.equals(options, that.options) &&
            Objects.equals(licensePlate, that.licensePlate) &&
            Objects.equals(engineSize, that.engineSize) &&
            Objects.equals(modelYear, that.modelYear) &&
            Objects.equals(since, that.since) &&
            Objects.equals(price, that.price) &&
            Objects.equals(nrOfSeats, that.nrOfSeats) &&
            Objects.equals(body, that.body) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(inspectionId, that.inspectionId) &&
            Objects.equals(reviewId, that.reviewId) &&
            Objects.equals(repairId, that.repairId) &&
            Objects.equals(rentalId, that.rentalId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            brand,
            model,
            fuel,
            options,
            licensePlate,
            engineSize,
            modelYear,
            since,
            price,
            nrOfSeats,
            body,
            longitude,
            latitude,
            inspectionId,
            reviewId,
            repairId,
            rentalId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBrand().map(f -> "brand=" + f + ", ").orElse("") +
            optionalModel().map(f -> "model=" + f + ", ").orElse("") +
            optionalFuel().map(f -> "fuel=" + f + ", ").orElse("") +
            optionalOptions().map(f -> "options=" + f + ", ").orElse("") +
            optionalLicensePlate().map(f -> "licensePlate=" + f + ", ").orElse("") +
            optionalEngineSize().map(f -> "engineSize=" + f + ", ").orElse("") +
            optionalModelYear().map(f -> "modelYear=" + f + ", ").orElse("") +
            optionalSince().map(f -> "since=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalNrOfSeats().map(f -> "nrOfSeats=" + f + ", ").orElse("") +
            optionalBody().map(f -> "body=" + f + ", ").orElse("") +
            optionalLongitude().map(f -> "longitude=" + f + ", ").orElse("") +
            optionalLatitude().map(f -> "latitude=" + f + ", ").orElse("") +
            optionalInspectionId().map(f -> "inspectionId=" + f + ", ").orElse("") +
            optionalReviewId().map(f -> "reviewId=" + f + ", ").orElse("") +
            optionalRepairId().map(f -> "repairId=" + f + ", ").orElse("") +
            optionalRentalId().map(f -> "rentalId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
