package fr.nathzaf.projects.flightmanager.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "flight")
public class Flight {

    @Id
    private String flightNumber;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime date;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "departure_airfield_icao", referencedColumnName = "icaoCode", nullable = false)
    private Airfield departure;

    @ManyToOne
    @JoinColumn(name = "arrival_airfield_icao", referencedColumnName = "icaoCode", nullable = false)
    private Airfield arrival;

    @ManyToOne
    @JoinColumn(name = "plane_registration", referencedColumnName = "registration", nullable = false)
    private Plane plane;

    @ManyToOne
    @JoinColumn(name = "pilot_username", referencedColumnName = "username", nullable = false)
    private Passenger pilot;

    public Flight(String flightNumber, Category category, LocalDateTime date, Airfield departure, Airfield arrival, Plane plane, Passenger pilot) {
        this.flightNumber = flightNumber;
        this.category = category;
        this.date = date;
        this.departure = departure;
        this.arrival = arrival;
        this.plane = plane;
        this.pilot = pilot;
        this.creationDate = LocalDateTime.now();
    }

    public static String generateFlightNumber() {
        StringBuilder flightNumber = new StringBuilder();
        flightNumber.setLength(0);
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        Random random = new Random();

        // Ajouter deux lettres aléatoires
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(letters.length());
            flightNumber.append(letters.charAt(index));
        }

        // Ajouter trois chiffres aléatoires
        for (int i = 0; i < 3; i++) {
            int digit = random.nextInt(10); // Générer un chiffre entre 0 et 9
            flightNumber.append(digit);
        }
        return flightNumber.toString();
    }
}
