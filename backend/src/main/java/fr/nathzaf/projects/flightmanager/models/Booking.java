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
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "passenger_username", referencedColumnName = "username", nullable = false)
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "flight_number", referencedColumnName = "flightNumber", nullable = false)
    private Flight flight;

    public Booking(Passenger passenger, Flight flight) {
        this.id = UUID.randomUUID();
        this.passenger = passenger;
        this.flight = flight;
    }
}
