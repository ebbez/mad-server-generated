package nl.hanze.se4.automaat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A RouteFromTo.
 */
@Entity
@Table(name = "route_from_to")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RouteFromTo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private LocalDate date;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "routeFromTo")
    @JsonIgnoreProperties(value = { "routeFromTo", "location" }, allowSetters = true)
    private Set<RouteStop> routeStops = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "inspections", "repairs", "routeFromTos" }, allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RouteFromTo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public RouteFromTo code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public RouteFromTo description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public RouteFromTo date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<RouteStop> getRouteStops() {
        return this.routeStops;
    }

    public void setRouteStops(Set<RouteStop> routeStops) {
        if (this.routeStops != null) {
            this.routeStops.forEach(i -> i.setRouteFromTo(null));
        }
        if (routeStops != null) {
            routeStops.forEach(i -> i.setRouteFromTo(this));
        }
        this.routeStops = routeStops;
    }

    public RouteFromTo routeStops(Set<RouteStop> routeStops) {
        this.setRouteStops(routeStops);
        return this;
    }

    public RouteFromTo addRouteStop(RouteStop routeStop) {
        this.routeStops.add(routeStop);
        routeStop.setRouteFromTo(this);
        return this;
    }

    public RouteFromTo removeRouteStop(RouteStop routeStop) {
        this.routeStops.remove(routeStop);
        routeStop.setRouteFromTo(null);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public RouteFromTo employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouteFromTo)) {
            return false;
        }
        return getId() != null && getId().equals(((RouteFromTo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouteFromTo{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
