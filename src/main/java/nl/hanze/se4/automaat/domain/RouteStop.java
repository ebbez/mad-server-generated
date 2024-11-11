package nl.hanze.se4.automaat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A RouteStop.
 */
@Entity
@Table(name = "route_stop")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RouteStop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nr")
    private Integer nr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "routeStops", "employee" }, allowSetters = true)
    private RouteFromTo routeFromTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "customers", "routeStops" }, allowSetters = true)
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RouteStop id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNr() {
        return this.nr;
    }

    public RouteStop nr(Integer nr) {
        this.setNr(nr);
        return this;
    }

    public void setNr(Integer nr) {
        this.nr = nr;
    }

    public RouteFromTo getRouteFromTo() {
        return this.routeFromTo;
    }

    public void setRouteFromTo(RouteFromTo routeFromTo) {
        this.routeFromTo = routeFromTo;
    }

    public RouteStop routeFromTo(RouteFromTo routeFromTo) {
        this.setRouteFromTo(routeFromTo);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public RouteStop location(Location location) {
        this.setLocation(location);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouteStop)) {
            return false;
        }
        return getId() != null && getId().equals(((RouteStop) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouteStop{" +
            "id=" + getId() +
            ", nr=" + getNr() +
            "}";
    }
}
