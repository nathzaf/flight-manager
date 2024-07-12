package fr.nathzaf.projects.flightmanager.repositories;

import fr.nathzaf.projects.flightmanager.models.Booking;
import fr.nathzaf.projects.flightmanager.models.Flight;
import fr.nathzaf.projects.flightmanager.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByFlightAndPassenger(Flight flight, Passenger passenger);

    @Query("SELECT b.passenger FROM Booking b WHERE b.flight=:flight")
    List<Passenger> findPassengersByFlight(@Param("flight") Flight flight);

    @Query("SELECT b.flight FROM Booking b WHERE b.passenger=:passenger")
    List<Flight> findFlightsByPassenger(@Param("passenger") Passenger passenger);
}
