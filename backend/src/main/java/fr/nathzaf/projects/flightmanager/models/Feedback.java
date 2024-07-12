package fr.nathzaf.projects.flightmanager.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "passenger_username", referencedColumnName = "username", nullable = false)
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "flight_number", referencedColumnName = "flightNumber", nullable = false)
    private Flight flight;

    private Integer rating;

    private String comment;

    public Feedback(Passenger passenger, Flight flight, Integer rating, String comment) {
        this.id = UUID.randomUUID();
        this.passenger = passenger;
        this.flight = flight;
        this.rating = rating;
        this.comment = comment;
    }
}
