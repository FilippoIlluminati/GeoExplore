package Geoexplore.POI;

import Geoexplore.User.Users;
import jakarta.persistence.*;


@Entity
public class POI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double latitude;
    private double longitude;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    private Users owner;

    private boolean approved;

    // Costruttori
    public POI() {}

    public POI(String name, String description, double latitude, double longitude, Category category, Users owner) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.owner = owner;
        this.approved = false; // Default a non approvato
    }

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public Users getOwner() { return owner; }
    public void setOwner(Users owner) { this.owner = owner; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
}