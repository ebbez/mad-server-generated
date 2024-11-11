package nl.hanze.se4.automaat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "state_province")
    private String stateProvince;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    @JsonIgnoreProperties(value = { "systemUser", "rentals", "location" }, allowSetters = true)
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    @JsonIgnoreProperties(value = { "routeFromTo", "location" }, allowSetters = true)
    private Set<RouteStop> routeStops = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Location id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public Location streetAddress(String streetAddress) {
        this.setStreetAddress(streetAddress);
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Location postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return this.city;
    }

    public Location city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return this.stateProvince;
    }

    public Location stateProvince(String stateProvince) {
        this.setStateProvince(stateProvince);
        return this;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<Customer> customers) {
        if (this.customers != null) {
            this.customers.forEach(i -> i.setLocation(null));
        }
        if (customers != null) {
            customers.forEach(i -> i.setLocation(this));
        }
        this.customers = customers;
    }

    public Location customers(Set<Customer> customers) {
        this.setCustomers(customers);
        return this;
    }

    public Location addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.setLocation(this);
        return this;
    }

    public Location removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.setLocation(null);
        return this;
    }

    public Set<RouteStop> getRouteStops() {
        return this.routeStops;
    }

    public void setRouteStops(Set<RouteStop> routeStops) {
        if (this.routeStops != null) {
            this.routeStops.forEach(i -> i.setLocation(null));
        }
        if (routeStops != null) {
            routeStops.forEach(i -> i.setLocation(this));
        }
        this.routeStops = routeStops;
    }

    public Location routeStops(Set<RouteStop> routeStops) {
        this.setRouteStops(routeStops);
        return this;
    }

    public Location addRouteStop(RouteStop routeStop) {
        this.routeStops.add(routeStop);
        routeStop.setLocation(this);
        return this;
    }

    public Location removeRouteStop(RouteStop routeStop) {
        this.routeStops.remove(routeStop);
        routeStop.setLocation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return getId() != null && getId().equals(((Location) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", city='" + getCity() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            "}";
    }
}
