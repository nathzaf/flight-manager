package fr.nathzaf.projects.flightmanager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "plane")
public class Plane {

    @Id
    private String registration;

    private String type;

    private String pictureLink;

    public Plane(String registration, String type, String pictureLink) {
        this.registration = registration;
        this.type = type;
        this.pictureLink = pictureLink;
    }
}
