package fr.nathzaf.projects.flightmanager.services;

import fr.nathzaf.projects.flightmanager.dto.CreateUpdatePlaneRequest;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.PlaneRegistrationAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.PlaneNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Plane;

import java.util.List;

public interface PlaneService {
    List<Plane> getAll();

    Plane getByRegistration(String registration) throws PlaneNotFoundException;

    Plane create(CreateUpdatePlaneRequest request) throws PlaneRegistrationAlreadyExistsException;

    Plane update(CreateUpdatePlaneRequest request) throws PlaneNotFoundException, PlaneRegistrationAlreadyExistsException;

    void delete(String registration) throws PlaneNotFoundException;
}
