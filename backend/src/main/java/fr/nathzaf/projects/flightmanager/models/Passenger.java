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
@Table(name = "passenger")
public class Passenger {

    @Id
    private String username;

    private String name;

    private Boolean isPilot;

    public Passenger(String name, String username, boolean isPilot) {
        this.name = name;
        this.username = username;
        this.isPilot = isPilot;
    }
}
