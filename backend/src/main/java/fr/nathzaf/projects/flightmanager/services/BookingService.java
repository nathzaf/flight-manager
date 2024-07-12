package fr.nathzaf.projects.flightmanager.services;

import fr.nathzaf.projects.flightmanager.dto.create.CreateBookingRequest;
import fr.nathzaf.projects.flightmanager.dto.update.UpdateBookingRequest;
import fr.nathzaf.projects.flightmanager.exceptions.IsThePilotException;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.BookingAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.BookingNotFoundException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.FlightNotFoundException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.PassengerNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Booking;
import fr.nathzaf.projects.flightmanager.models.Flight;
import fr.nathzaf.projects.flightmanager.models.Passenger;

import java.util.List;
import java.util.UUID;

public interface BookingService {

    List<Passenger> getPassengersBookedOnAFlight(String flightNumber) throws FlightNotFoundException;

    List<Flight> getFlightsBookedByPassenger(String passengerUsername) throws PassengerNotFoundException;

    List<Booking> getAll();

    Booking getById(UUID id) throws BookingNotFoundException;

    Booking create(CreateBookingRequest request) throws BookingAlreadyExistsException, PassengerNotFoundException, FlightNotFoundException, IsThePilotException;

    Booking update(UpdateBookingRequest request) throws BookingNotFoundException, BookingAlreadyExistsException, PassengerNotFoundException, FlightNotFoundException;

    void delete(UUID id) throws BookingNotFoundException;
}
