package nl.hanze.se4.automaat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import nl.hanze.se4.automaat.domain.enumeration.RentalState;

/**
 * A Rental.
 */
@Entity
@Table(name = "rental")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rental implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private RentalState state;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rental")
    @JsonIgnoreProperties(value = { "photos", "repairs", "car", "employee", "rental" }, allowSetters = true)
    private Set<Inspection> inspections = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "systemUser", "rentals", "location" }, allowSetters = true)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "inspections", "repairs", "rentals" }, allowSetters = true)
    private Car car;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rental id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Rental code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Float getLongitude() {
        return this.longitude;
    }

    public Rental longitude(Float longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return this.latitude;
    }

    public Rental latitude(Float latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public LocalDate getFromDate() {
        return this.fromDate;
    }

    public Rental fromDate(LocalDate fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return this.toDate;
    }

    public Rental toDate(LocalDate toDate) {
        this.setToDate(toDate);
        return this;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public RentalState getState() {
        return this.state;
    }

    public Rental state(RentalState state) {
        this.setState(state);
        return this;
    }

    public void setState(RentalState state) {
        this.state = state;
    }

    public Set<Inspection> getInspections() {
        return this.inspections;
    }

    public void setInspections(Set<Inspection> inspections) {
        if (this.inspections != null) {
            this.inspections.forEach(i -> i.setRental(null));
        }
        if (inspections != null) {
            inspections.forEach(i -> i.setRental(this));
        }
        this.inspections = inspections;
    }

    public Rental inspections(Set<Inspection> inspections) {
        this.setInspections(inspections);
        return this;
    }

    public Rental addInspection(Inspection inspection) {
        this.inspections.add(inspection);
        inspection.setRental(this);
        return this;
    }

    public Rental removeInspection(Inspection inspection) {
        this.inspections.remove(inspection);
        inspection.setRental(null);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Rental customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Rental car(Car car) {
        this.setCar(car);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rental)) {
            return false;
        }
        return getId() != null && getId().equals(((Rental) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rental{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
