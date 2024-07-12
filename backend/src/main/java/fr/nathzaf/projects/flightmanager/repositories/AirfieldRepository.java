package fr.nathzaf.projects.flightmanager.repositories;

import fr.nathzaf.projects.flightmanager.models.Airfield;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirfieldRepository extends JpaRepository<Airfield, String> {

    Optional<Airfield> findByIataCode(String iataCode);
}
