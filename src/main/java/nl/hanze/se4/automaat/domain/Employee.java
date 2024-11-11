package nl.hanze.se4.automaat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @JsonIgnoreProperties(value = { "photos", "repairs", "car", "employee", "rental" }, allowSetters = true)
    private Set<Inspection> inspections = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @JsonIgnoreProperties(value = { "car", "employee", "inspection" }, allowSetters = true)
    private Set<Repair> repairs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @JsonIgnoreProperties(value = { "routeStops", "employee" }, allowSetters = true)
    private Set<RouteFromTo> routeFromTos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNr() {
        return this.nr;
    }

    public Employee nr(Integer nr) {
        this.setNr(nr);
        return this;
    }

    public void setNr(Integer nr) {
        this.nr = nr;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Employee lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Employee firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getFrom() {
        return this.from;
    }

    public Employee from(LocalDate from) {
        this.setFrom(from);
        return this;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public Set<Inspection> getInspections() {
        return this.inspections;
    }

    public void setInspections(Set<Inspection> inspections) {
        if (this.inspections != null) {
            this.inspections.forEach(i -> i.setEmployee(null));
        }
        if (inspections != null) {
            inspections.forEach(i -> i.setEmployee(this));
        }
        this.inspections = inspections;
    }

    public Employee inspections(Set<Inspection> inspections) {
        this.setInspections(inspections);
        return this;
    }

    public Employee addInspection(Inspection inspection) {
        this.inspections.add(inspection);
        inspection.setEmployee(this);
        return this;
    }

    public Employee removeInspection(Inspection inspection) {
        this.inspections.remove(inspection);
        inspection.setEmployee(null);
        return this;
    }

    public Set<Repair> getRepairs() {
        return this.repairs;
    }

    public void setRepairs(Set<Repair> repairs) {
        if (this.repairs != null) {
            this.repairs.forEach(i -> i.setEmployee(null));
        }
        if (repairs != null) {
            repairs.forEach(i -> i.setEmployee(this));
        }
        this.repairs = repairs;
    }

    public Employee repairs(Set<Repair> repairs) {
        this.setRepairs(repairs);
        return this;
    }

    public Employee addRepair(Repair repair) {
        this.repairs.add(repair);
        repair.setEmployee(this);
        return this;
    }

    public Employee removeRepair(Repair repair) {
        this.repairs.remove(repair);
        repair.setEmployee(null);
        return this;
    }

    public Set<RouteFromTo> getRouteFromTos() {
        return this.routeFromTos;
    }

    public void setRouteFromTos(Set<RouteFromTo> routeFromTos) {
        if (this.routeFromTos != null) {
            this.routeFromTos.forEach(i -> i.setEmployee(null));
        }
        if (routeFromTos != null) {
            routeFromTos.forEach(i -> i.setEmployee(this));
        }
        this.routeFromTos = routeFromTos;
    }

    public Employee routeFromTos(Set<RouteFromTo> routeFromTos) {
        this.setRouteFromTos(routeFromTos);
        return this;
    }

    public Employee addRouteFromTo(RouteFromTo routeFromTo) {
        this.routeFromTos.add(routeFromTo);
        routeFromTo.setEmployee(this);
        return this;
    }

    public Employee removeRouteFromTo(RouteFromTo routeFromTo) {
        this.routeFromTos.remove(routeFromTo);
        routeFromTo.setEmployee(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return getId() != null && getId().equals(((Employee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", nr=" + getNr() +
            ", lastName='" + getLastName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", from='" + getFrom() + "'" +
            "}";
    }
}
