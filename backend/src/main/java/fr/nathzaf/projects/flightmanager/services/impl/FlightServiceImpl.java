package fr.nathzaf.projects.flightmanager.services.impl;

import fr.nathzaf.projects.flightmanager.dto.create.CreateFlightRequest;
import fr.nathzaf.projects.flightmanager.dto.update.UpdateFlightRequest;
import fr.nathzaf.projects.flightmanager.exceptions.NotAPilotException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.*;
import fr.nathzaf.projects.flightmanager.models.*;
import fr.nathzaf.projects.flightmanager.repositories.FlightRepository;
import fr.nathzaf.projects.flightmanager.services.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    private final PassengerService passengerService;

    private final PlaneService planeService;

    private final CategoryService categoryService;

    private final AirfieldService airfieldService;

    public FlightServiceImpl(FlightRepository flightRepository, PassengerService passengerService, PlaneService planeService, CategoryService categoryService, AirfieldService airfieldService) {
        this.flightRepository = flightRepository;
        this.passengerService = passengerService;
        this.planeService = planeService;
        this.categoryService = categoryService;
        this.airfieldService = airfieldService;
    }

    @Override
    public List<Flight> getByCriteria(UUID categoryId, String startDate, String endDate, String departureAirfieldICAO, String arrivalAirfieldICAO, String planeRegistration, String pilotUsername) throws CategoryNotFoundException, AirfieldNotFoundException, PlaneNotFoundException, PassengerNotFoundException {
        Category category = null;
        if (categoryId != null) {
            category = categoryService.getById(categoryId);
        }

        Airfield departureAirfield = null;
        if (departureAirfieldICAO != null) {
            departureAirfield = airfieldService.getByICAO(departureAirfieldICAO);
        }

        Airfield arrivalAirfield = null;
        if (arrivalAirfieldICAO != null) {
            arrivalAirfield = airfieldService.getByICAO(arrivalAirfieldICAO);
        }

        Plane plane = null;
        if (planeRegistration != null) {
            plane = planeService.getByRegistration(planeRegistration);
        }

        Passenger pilot = null;
        if (pilotUsername != null) {
            pilot = passengerService.getByUsername(pilotUsername);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        LocalDateTime sDate = null;
        if (startDate != null) {
            sDate = LocalDateTime.parse(startDate, formatter);
        }

        LocalDateTime eDate = null;
        if (endDate != null) {
            eDate = LocalDateTime.parse(endDate, formatter);
        }

        return flightRepository.findByCriteria(category, sDate, eDate, departureAirfield, arrivalAirfield, plane, pilot);
    }

    @Override
    public List<Flight> getByCategory(UUID categoryId) throws CategoryNotFoundException {
        Category category = categoryService.getById(categoryId);
        return flightRepository.findByCategory(category);
    }

    @Override
    public Flight getById(String flightNumber) throws FlightNotFoundException {
        return flightRepository.findById(flightNumber)
                .orElseThrow(() -> new FlightNotFoundException(flightNumber));
    }

    @Override
    public Flight create(CreateFlightRequest request) throws NotAPilotException, PassengerNotFoundException, AirfieldNotFoundException, PlaneNotFoundException, CategoryNotFoundException {
        Passenger pilot = passengerService.getByUsername(request.getPilotUsername());
        if (!pilot.getIsPilot()) throw new NotAPilotException(pilot.getUsername());
        Airfield departure = airfieldService.getByICAO(request.getDepartureIcao());
        Airfield arrival = airfieldService.getByICAO(request.getArrivalIcao());
        Plane plane = planeService.getByRegistration(request.getPlaneRegistration());
        Category category = categoryService.getById(request.getCategoryId());
        Flight flight = new Flight(generateFlightNumber(), category, request.getDate(), departure, arrival, plane, pilot);
        return flightRepository.save(flight);
    }

    private String generateFlightNumber() {
        String flightNumber;
        do {
            flightNumber = Flight.generateFlightNumber();
        } while (flightRepository.existsById(flightNumber));
        return flightNumber;
    }

    @Override
    public Flight update(UpdateFlightRequest request) throws FlightNotFoundException, NotAPilotException, PassengerNotFoundException, AirfieldNotFoundException, PlaneNotFoundException, CategoryNotFoundException {
        Passenger pilot = passengerService.getByUsername(request.getPilotUsername());
        if (!pilot.getIsPilot()) throw new NotAPilotException(pilot.getUsername());
        Plane plane = planeService.getByRegistration(request.getPlaneRegistration());
        Category category = categoryService.getById(request.getCategoryId());
        Airfield departure = airfieldService.getByICAO(request.getDepartureIcao());
        Airfield arrival = airfieldService.getByICAO(request.getArrivalIcao());
        Flight flight = getById(request.getFlightNumber());
        flight.setCategory(category);
        flight.setDate(request.getDate());
        flight.setDeparture(departure);
        flight.setArrival(arrival);
        flight.setPlane(plane);
        flight.setPilot(pilot);
        return flightRepository.save(flight);
    }

    @Override
    public void delete(String flightNumber) throws FlightNotFoundException {
        if (!flightRepository.existsById(flightNumber))
            throw new FlightNotFoundException(flightNumber);
        flightRepository.deleteById(flightNumber);
    }
}
