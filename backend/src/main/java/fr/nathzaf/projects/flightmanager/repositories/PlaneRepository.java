package fr.nathzaf.projects.flightmanager.repositories;

import fr.nathzaf.projects.flightmanager.models.Plane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaneRepository extends JpaRepository<Plane, String> {
    List<Plane> findPlanesByRegistration(String registration);
}
