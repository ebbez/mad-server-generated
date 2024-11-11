package nl.hanze.se4.automaat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import nl.hanze.se4.automaat.domain.enumeration.Body;
import nl.hanze.se4.automaat.domain.enumeration.Fuel;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel")
    private Fuel fuel;

    @Column(name = "options")
    private String options;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "engine_size")
    private Integer engineSize;

    @Column(name = "model_year")
    private Integer modelYear;

    @Column(name = "since")
    private LocalDate since;

    @Column(name = "price")
    private Float price;

    @Column(name = "nr_of_seats")
    private Integer nrOfSeats;

    @Enumerated(EnumType.STRING)
    @Column(name = "body")
    private Body body;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "latitude")
    private Float latitude;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    @JsonIgnoreProperties(value = { "photos", "repairs", "car", "employee", "rental" }, allowSetters = true)
    private Set<Inspection> inspections = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    @JsonIgnoreProperties(value = { "car", "employee", "inspection" }, allowSetters = true)
    private Set<Repair> repairs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    @JsonIgnoreProperties(value = { "inspections", "customer", "car" }, allowSetters = true)
    private Set<Rental> rentals = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Car id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return this.brand;
    }

    public Car brand(String brand) {
        this.setBrand(brand);
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return this.model;
    }

    public Car model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public Car picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public Car pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Fuel getFuel() {
        return this.fuel;
    }

    public Car fuel(Fuel fuel) {
        this.setFuel(fuel);
        return this;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public String getOptions() {
        return this.options;
    }

    public Car options(String options) {
        this.setOptions(options);
        return this;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getLicensePlate() {
        return this.licensePlate;
    }

    public Car licensePlate(String licensePlate) {
        this.setLicensePlate(licensePlate);
        return this;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Integer getEngineSize() {
        return this.engineSize;
    }

    public Car engineSize(Integer engineSize) {
        this.setEngineSize(engineSize);
        return this;
    }

    public void setEngineSize(Integer engineSize) {
        this.engineSize = engineSize;
    }

    public Integer getModelYear() {
        return this.modelYear;
    }

    public Car modelYear(Integer modelYear) {
        this.setModelYear(modelYear);
        return this;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public LocalDate getSince() {
        return this.since;
    }

    public Car since(LocalDate since) {
        this.setSince(since);
        return this;
    }

    public void setSince(LocalDate since) {
        this.since = since;
    }

    public Float getPrice() {
        return this.price;
    }

    public Car price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getNrOfSeats() {
        return this.nrOfSeats;
    }

    public Car nrOfSeats(Integer nrOfSeats) {
        this.setNrOfSeats(nrOfSeats);
        return this;
    }

    public void setNrOfSeats(Integer nrOfSeats) {
        this.nrOfSeats = nrOfSeats;
    }

    public Body getBody() {
        return this.body;
    }

    public Car body(Body body) {
        this.setBody(body);
        return this;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Float getLongitude() {
        return this.longitude;
    }

    public Car longitude(Float longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return this.latitude;
    }

    public Car latitude(Float latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Set<Inspection> getInspections() {
        return this.inspections;
    }

    public void setInspections(Set<Inspection> inspections) {
        if (this.inspections != null) {
            this.inspections.forEach(i -> i.setCar(null));
        }
        if (inspections != null) {
            inspections.forEach(i -> i.setCar(this));
        }
        this.inspections = inspections;
    }

    public Car inspections(Set<Inspection> inspections) {
        this.setInspections(inspections);
        return this;
    }

    public Car addInspection(Inspection inspection) {
        this.inspections.add(inspection);
        inspection.setCar(this);
        return this;
    }

    public Car removeInspection(Inspection inspection) {
        this.inspections.remove(inspection);
        inspection.setCar(null);
        return this;
    }

    public Set<Repair> getRepairs() {
        return this.repairs;
    }

    public void setRepairs(Set<Repair> repairs) {
        if (this.repairs != null) {
            this.repairs.forEach(i -> i.setCar(null));
        }
        if (repairs != null) {
            repairs.forEach(i -> i.setCar(this));
        }
        this.repairs = repairs;
    }

    public Car repairs(Set<Repair> repairs) {
        this.setRepairs(repairs);
        return this;
    }

    public Car addRepair(Repair repair) {
        this.repairs.add(repair);
        repair.setCar(this);
        return this;
    }

    public Car removeRepair(Repair repair) {
        this.repairs.remove(repair);
        repair.setCar(null);
        return this;
    }

    public Set<Rental> getRentals() {
        return this.rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        if (this.rentals != null) {
            this.rentals.forEach(i -> i.setCar(null));
        }
        if (rentals != null) {
            rentals.forEach(i -> i.setCar(this));
        }
        this.rentals = rentals;
    }

    public Car rentals(Set<Rental> rentals) {
        this.setRentals(rentals);
        return this;
    }

    public Car addRental(Rental rental) {
        this.rentals.add(rental);
        rental.setCar(this);
        return this;
    }

    public Car removeRental(Rental rental) {
        this.rentals.remove(rental);
        rental.setCar(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Car)) {
            return false;
        }
        return getId() != null && getId().equals(((Car) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Car{" +
            "id=" + getId() +
            ", brand='" + getBrand() + "'" +
            ", model='" + getModel() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", fuel='" + getFuel() + "'" +
            ", options='" + getOptions() + "'" +
            ", licensePlate='" + getLicensePlate() + "'" +
            ", engineSize=" + getEngineSize() +
            ", modelYear=" + getModelYear() +
            ", since='" + getSince() + "'" +
            ", price=" + getPrice() +
            ", nrOfSeats=" + getNrOfSeats() +
            ", body='" + getBody() + "'" +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            "}";
    }
}
