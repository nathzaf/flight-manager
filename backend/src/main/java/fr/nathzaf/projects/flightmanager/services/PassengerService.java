package fr.nathzaf.projects.flightmanager.services;

import fr.nathzaf.projects.flightmanager.dto.CreateUpdatePassengerRequest;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.PassengerUsernameAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.PassengerNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Passenger;

import java.util.List;

public interface PassengerService {

    List<Passenger> getAll();

    Passenger getByUsername(String username) throws PassengerNotFoundException;

    Passenger create(CreateUpdatePassengerRequest request) throws PassengerUsernameAlreadyExistsException;

    void delete(String username) throws PassengerNotFoundException;

    Passenger update(CreateUpdatePassengerRequest request) throws PassengerNotFoundException;

    List<Passenger> getPilots();
}
