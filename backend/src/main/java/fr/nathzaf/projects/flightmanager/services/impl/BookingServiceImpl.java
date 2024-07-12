package fr.nathzaf.projects.flightmanager.services.impl;

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
import fr.nathzaf.projects.flightmanager.repositories.BookingRepository;
import fr.nathzaf.projects.flightmanager.services.BookingService;
import fr.nathzaf.projects.flightmanager.services.FlightService;
import fr.nathzaf.projects.flightmanager.services.PassengerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final PassengerService passengerService;

    private final FlightService flightService;

    public BookingServiceImpl(BookingRepository bookingRepository, PassengerService passengerService, FlightService flightService) {
        this.bookingRepository = bookingRepository;
        this.passengerService = passengerService;
        this.flightService = flightService;
    }

    @Override
    public List<Flight> getFlightsBookedByPassenger(String passengerUsername) throws PassengerNotFoundException {
        Passenger passenger = passengerService.getByUsername(passengerUsername);
        return bookingRepository.findFlightsByPassenger(passenger);
    }

    @Override
    public List<Passenger> getPassengersBookedOnAFlight(String flightNumber) throws FlightNotFoundException {
        Flight flight = flightService.getById(flightNumber);
        return bookingRepository.findPassengersByFlight(flight);
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getById(UUID id) throws BookingNotFoundException {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    @Override
    public Booking create(CreateBookingRequest request) throws BookingAlreadyExistsException, PassengerNotFoundException, FlightNotFoundException, IsThePilotException {
        Passenger passenger = passengerService.getByUsername(request.getPassengerUsername());
        Flight flight = flightService.getById(request.getFlightNumber());
        if (!bookingRepository.findByFlightAndPassenger(flight, passenger).isEmpty())
            throw new BookingAlreadyExistsException(flight.getFlightNumber(), passenger.getUsername());
        if (flight.getPilot().getUsername().equals(request.getPassengerUsername()))
            throw new IsThePilotException(flight.getPilot().getUsername(), flight.getFlightNumber());
        Booking booking = new Booking(passenger, flight);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(UpdateBookingRequest request) throws BookingNotFoundException, BookingAlreadyExistsException, PassengerNotFoundException, FlightNotFoundException {
        Booking booking = getById(request.getId());
        Passenger passenger = passengerService.getByUsername(request.getPassengerUsername());
        Flight flight = flightService.getById(request.getFlightNumber());
        booking.setPassenger(passenger);
        booking.setFlight(flight);
        return bookingRepository.save(booking);
    }

    @Override
    public void delete(UUID id) throws BookingNotFoundException {
        if (!bookingRepository.existsById(id))
            throw new BookingNotFoundException(id);
        bookingRepository.deleteById(id);
    }
}
