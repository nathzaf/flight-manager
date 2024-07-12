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
@Table(name = "airfield")
public class Airfield {

    @Id
    private String icaoCode;

    private String name;

    private String iataCode;

    public Airfield(String name, String icaoCode, String iataCode) {
        this.name = name;
        this.icaoCode = icaoCode;
        this.iataCode = iataCode;
    }
}
