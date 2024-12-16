package nl.hanze.se4.automaat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nr")
    private Integer nr;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "jhi_from")
    private LocalDate from;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User systemUser;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customer")
    @JsonIgnoreProperties(value = { "inspections", "customer", "car" }, allowSetters = true)
    private Set<Rental> rentals = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "customers", "routeStops" }, allowSetters = true)
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNr() {
        return this.nr;
    }

    public Customer nr(Integer nr) {
        this.setNr(nr);
        return this;
    }

    public void setNr(Integer nr) {
        this.nr = nr;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Customer lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Customer firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getFrom() {
        return this.from;
    }

    public Customer from(LocalDate from) {
        this.setFrom(from);
        return this;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public User getSystemUser() {
        return this.systemUser;
    }

    public void setSystemUser(User user) {
        this.systemUser = user;
    }

    public Customer systemUser(User user) {
        this.setSystemUser(user);
        return this;
    }

    public Set<Rental> getRentals() {
        return this.rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        if (this.rentals != null) {
            this.rentals.forEach(i -> i.setCustomer(null));
        }
        if (rentals != null) {
            rentals.forEach(i -> i.setCustomer(this));
        }
        this.rentals = rentals;
    }

    public Customer rentals(Set<Rental> rentals) {
        this.setRentals(rentals);
        return this;
    }

    public Customer addRental(Rental rental) {
        this.rentals.add(rental);
        rental.setCustomer(this);
        return this;
    }

    public Customer removeRental(Rental rental) {
        this.rentals.remove(rental);
        rental.setCustomer(null);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Customer location(Location location) {
        this.setLocation(location);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return getId() != null && getId().equals(((Customer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", nr=" + getNr() +
            ", lastName='" + getLastName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", from='" + getFrom() + "'" +
            "}";
    }
}
