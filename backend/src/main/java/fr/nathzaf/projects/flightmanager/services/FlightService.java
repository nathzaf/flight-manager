package fr.nathzaf.projects.flightmanager.services;

import fr.nathzaf.projects.flightmanager.dto.create.CreateFlightRequest;
import fr.nathzaf.projects.flightmanager.dto.update.UpdateFlightRequest;
import fr.nathzaf.projects.flightmanager.exceptions.NotAPilotException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.*;
import fr.nathzaf.projects.flightmanager.models.Flight;

import java.util.List;
import java.util.UUID;

public interface FlightService {

    List<Flight> getByCriteria(UUID categoryId, String startDate, String endDate, String departureAirfieldICAO, String arrivalAirfieldICAO, String planeRegistration, String pilotUsername) throws CategoryNotFoundException, AirfieldNotFoundException, PlaneNotFoundException, PassengerNotFoundException;

    List<Flight> getByCategory(UUID categoryId) throws CategoryNotFoundException;

    Flight getById(String flightNumber) throws FlightNotFoundException;

    Flight create(CreateFlightRequest request) throws NotAPilotException, PassengerNotFoundException, AirfieldNotFoundException, PlaneNotFoundException, CategoryNotFoundException;

    Flight update(UpdateFlightRequest request) throws FlightNotFoundException, NotAPilotException, PassengerNotFoundException, AirfieldNotFoundException, PlaneNotFoundException, CategoryNotFoundException;

    void delete(String flightNumber) throws FlightNotFoundException;

}
