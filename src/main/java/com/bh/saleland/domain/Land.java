package com.bh.saleland.domain;

import com.bh.saleland.domain.enumeration.FeeType;
import com.bh.saleland.domain.enumeration.LandStatus;
import com.bh.saleland.domain.enumeration.LandType;
import com.bh.saleland.domain.enumeration.PriceType;
import com.bh.saleland.domain.enumeration.UnitPriceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Land.
 */
@Entity
@Table(name = "land")
public class Land implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LandStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LandType type;

    @Column(name = "price")
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "init")
    private UnitPriceType init;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_type")
    private PriceType priceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "fee_type")
    private FeeType feeType;

    @Column(name = "description")
    private String description;

    @Column(name = "width")
    private Double width;

    @Column(name = "height")
    private Double height;

    @Column(name = "area")
    private Double area;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @OneToMany(mappedBy = "land")
    @JsonIgnoreProperties(value = { "land" }, allowSetters = true)
    private Set<LandTag> tags = new HashSet<>();

    @OneToMany(mappedBy = "land")
    @JsonIgnoreProperties(value = { "land" }, allowSetters = true)
    private Set<LandPhoto> photos = new HashSet<>();

    @OneToMany(mappedBy = "land")
    @JsonIgnoreProperties(value = { "land" }, allowSetters = true)
    private Set<LandCoordinate> coordinates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Land id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Land title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return this.address;
    }

    public Land address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LandStatus getStatus() {
        return this.status;
    }

    public Land status(LandStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(LandStatus status) {
        this.status = status;
    }

    public LandType getType() {
        return this.type;
    }

    public Land type(LandType type) {
        this.setType(type);
        return this;
    }

    public void setType(LandType type) {
        this.type = type;
    }

    public Double getPrice() {
        return this.price;
    }

    public Land price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public UnitPriceType getInit() {
        return this.init;
    }

    public Land init(UnitPriceType init) {
        this.setInit(init);
        return this;
    }

    public void setInit(UnitPriceType init) {
        this.init = init;
    }

    public PriceType getPriceType() {
        return this.priceType;
    }

    public Land priceType(PriceType priceType) {
        this.setPriceType(priceType);
        return this;
    }

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

    public FeeType getFeeType() {
        return this.feeType;
    }

    public Land feeType(FeeType feeType) {
        this.setFeeType(feeType);
        return this;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }

    public String getDescription() {
        return this.description;
    }

    public Land description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getWidth() {
        return this.width;
    }

    public Land width(Double width) {
        this.setWidth(width);
        return this;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return this.height;
    }

    public Land height(Double height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getArea() {
        return this.area;
    }

    public Land area(Double area) {
        this.setArea(area);
        return this;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Land latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Land longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Set<LandTag> getTags() {
        return this.tags;
    }

    public void setTags(Set<LandTag> landTags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setLand(null));
        }
        if (landTags != null) {
            landTags.forEach(i -> i.setLand(this));
        }
        this.tags = landTags;
    }

    public Land tags(Set<LandTag> landTags) {
        this.setTags(landTags);
        return this;
    }

    public Land addTags(LandTag landTag) {
        this.tags.add(landTag);
        landTag.setLand(this);
        return this;
    }

    public Land removeTags(LandTag landTag) {
        this.tags.remove(landTag);
        landTag.setLand(null);
        return this;
    }

    public Set<LandPhoto> getPhotos() {
        return this.photos;
    }

    public void setPhotos(Set<LandPhoto> landPhotos) {
        if (this.photos != null) {
            this.photos.forEach(i -> i.setLand(null));
        }
        if (landPhotos != null) {
            landPhotos.forEach(i -> i.setLand(this));
        }
        this.photos = landPhotos;
    }

    public Land photos(Set<LandPhoto> landPhotos) {
        this.setPhotos(landPhotos);
        return this;
    }

    public Land addPhotos(LandPhoto landPhoto) {
        this.photos.add(landPhoto);
        landPhoto.setLand(this);
        return this;
    }

    public Land removePhotos(LandPhoto landPhoto) {
        this.photos.remove(landPhoto);
        landPhoto.setLand(null);
        return this;
    }

    public Set<LandCoordinate> getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(Set<LandCoordinate> landCoordinates) {
        if (this.coordinates != null) {
            this.coordinates.forEach(i -> i.setLand(null));
        }
        if (landCoordinates != null) {
            landCoordinates.forEach(i -> i.setLand(this));
        }
        this.coordinates = landCoordinates;
    }

    public Land coordinates(Set<LandCoordinate> landCoordinates) {
        this.setCoordinates(landCoordinates);
        return this;
    }

    public Land addCoordinates(LandCoordinate landCoordinate) {
        this.coordinates.add(landCoordinate);
        landCoordinate.setLand(this);
        return this;
    }

    public Land removeCoordinates(LandCoordinate landCoordinate) {
        this.coordinates.remove(landCoordinate);
        landCoordinate.setLand(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Land)) {
            return false;
        }
        return id != null && id.equals(((Land) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Land{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", address='" + getAddress() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", price=" + getPrice() +
            ", init='" + getInit() + "'" +
            ", priceType='" + getPriceType() + "'" +
            ", feeType='" + getFeeType() + "'" +
            ", description='" + getDescription() + "'" +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", area=" + getArea() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            "}";
    }
}
