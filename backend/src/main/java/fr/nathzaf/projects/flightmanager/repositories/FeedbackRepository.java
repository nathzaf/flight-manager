package fr.nathzaf.projects.flightmanager.repositories;

import fr.nathzaf.projects.flightmanager.models.Feedback;
import fr.nathzaf.projects.flightmanager.models.Flight;
import fr.nathzaf.projects.flightmanager.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

    List<Feedback> findByFlightAndPassenger(Flight flight, Passenger passenger);

    List<Feedback> findByFlight(Flight flight);
}
