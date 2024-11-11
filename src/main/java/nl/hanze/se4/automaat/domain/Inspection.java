package nl.hanze.se4.automaat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Inspection.
 */
@Entity
@Table(name = "inspection")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Inspection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "odometer")
    private Long odometer;

    @Column(name = "result")
    private String result;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "completed")
    private ZonedDateTime completed;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inspection")
    @JsonIgnoreProperties(value = { "inspection" }, allowSetters = true)
    private Set<InspectionPhoto> photos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inspection")
    @JsonIgnoreProperties(value = { "car", "employee", "inspection" }, allowSetters = true)
    private Set<Repair> repairs = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "inspections", "repairs", "rentals" }, allowSetters = true)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "inspections", "repairs", "routeFromTos" }, allowSetters = true)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "inspections", "customer", "car" }, allowSetters = true)
    private Rental rental;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inspection id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Inspection code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getOdometer() {
        return this.odometer;
    }

    public Inspection odometer(Long odometer) {
        this.setOdometer(odometer);
        return this;
    }

    public void setOdometer(Long odometer) {
        this.odometer = odometer;
    }

    public String getResult() {
        return this.result;
    }

    public Inspection result(String result) {
        this.setResult(result);
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return this.description;
    }

    public Inspection description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Inspection photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Inspection photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public ZonedDateTime getCompleted() {
        return this.completed;
    }

    public Inspection completed(ZonedDateTime completed) {
        this.setCompleted(completed);
        return this;
    }

    public void setCompleted(ZonedDateTime completed) {
        this.completed = completed;
    }

    public Set<InspectionPhoto> getPhotos() {
        return this.photos;
    }

    public void setPhotos(Set<InspectionPhoto> inspectionPhotos) {
        if (this.photos != null) {
            this.photos.forEach(i -> i.setInspection(null));
        }
        if (inspectionPhotos != null) {
            inspectionPhotos.forEach(i -> i.setInspection(this));
        }
        this.photos = inspectionPhotos;
    }

    public Inspection photos(Set<InspectionPhoto> inspectionPhotos) {
        this.setPhotos(inspectionPhotos);
        return this;
    }

    public Inspection addPhoto(InspectionPhoto inspectionPhoto) {
        this.photos.add(inspectionPhoto);
        inspectionPhoto.setInspection(this);
        return this;
    }

    public Inspection removePhoto(InspectionPhoto inspectionPhoto) {
        this.photos.remove(inspectionPhoto);
        inspectionPhoto.setInspection(null);
        return this;
    }

    public Set<Repair> getRepairs() {
        return this.repairs;
    }

    public void setRepairs(Set<Repair> repairs) {
        if (this.repairs != null) {
            this.repairs.forEach(i -> i.setInspection(null));
        }
        if (repairs != null) {
            repairs.forEach(i -> i.setInspection(this));
        }
        this.repairs = repairs;
    }

    public Inspection repairs(Set<Repair> repairs) {
        this.setRepairs(repairs);
        return this;
    }

    public Inspection addRepair(Repair repair) {
        this.repairs.add(repair);
        repair.setInspection(this);
        return this;
    }

    public Inspection removeRepair(Repair repair) {
        this.repairs.remove(repair);
        repair.setInspection(null);
        return this;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Inspection car(Car car) {
        this.setCar(car);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Inspection employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Rental getRental() {
        return this.rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public Inspection rental(Rental rental) {
        this.setRental(rental);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inspection)) {
            return false;
        }
        return getId() != null && getId().equals(((Inspection) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inspection{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", odometer=" + getOdometer() +
            ", result='" + getResult() + "'" +
            ", description='" + getDescription() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", completed='" + getCompleted() + "'" +
            "}";
    }
}
