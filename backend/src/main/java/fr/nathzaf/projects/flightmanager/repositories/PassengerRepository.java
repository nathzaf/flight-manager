package fr.nathzaf.projects.flightmanager.repositories;

import fr.nathzaf.projects.flightmanager.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, String> {
    List<Passenger> findByIsPilotTrue();
}
