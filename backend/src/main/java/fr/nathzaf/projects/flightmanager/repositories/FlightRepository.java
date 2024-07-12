package fr.nathzaf.projects.flightmanager.repositories;

import fr.nathzaf.projects.flightmanager.models.Category;
import fr.nathzaf.projects.flightmanager.models.Flight;
import fr.nathzaf.projects.flightmanager.repositories.custom.FlightRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, String>, FlightRepositoryCustom {

    List<Flight> findByCategory(Category category);

}
