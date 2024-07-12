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
import fr.nathzaf.projects.flightmanager.services.FlightService;
import fr.nathzaf.projects.flightmanager.services.PassengerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PassengerService passengerService;

    @Mock
    private FlightService flightService;

    private static Passenger generatePassenger(String passengerUsername) {
        return new Passenger("Test User", passengerUsername, false);
    }

    private static Flight generateFlight(String flightNumber, Passenger passenger) {
        return new Flight(flightNumber, null, LocalDateTime.now(), null, null, null, passenger);
    }

    @Test
    public void testGetFlightsBookedByPassenger() throws PassengerNotFoundException {
        String passengerUsername = "testuser";
        Passenger passenger = generatePassenger(passengerUsername);
        List<Flight> flights = new ArrayList<>();
        String flightNumber = "FL001";
        flights.add(generateFlight(flightNumber, passenger));
        when(passengerService.getByUsername(passengerUsername)).thenReturn(passenger);
        when(bookingRepository.findFlightsByPassenger(passenger)).thenReturn(flights);

        List<Flight> result = bookingService.getFlightsBookedByPassenger(passengerUsername);

        assertEquals(1, result.size());
        assertEquals(flightNumber, result.getFirst().getFlightNumber());
    }

    @Test
    public void testGetPassengersBookedOnAFlight() throws FlightNotFoundException {
        String flightNumber = "FL001";
        String passengerUsername = "testuser";
        Flight flight = generateFlight(flightNumber, null);
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(generatePassenger(passengerUsername));
        when(flightService.getById(flightNumber)).thenReturn(flight);
        when(bookingRepository.findPassengersByFlight(flight)).thenReturn(passengers);

        List<Passenger> result = bookingService.getPassengersBookedOnAFlight(flightNumber);

        assertEquals(1, result.size());
        assertEquals("testuser", result.getFirst().getUsername());
    }

    @Test
    public void testGetAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String passengerUsername = "testuser";
        Passenger passenger = generatePassenger(passengerUsername);
        Flight flight = generateFlight("FL001", passenger);
        bookings.add(new Booking(passenger, flight));
        when(bookingRepository.findAll()).thenReturn(bookings);

        List<Booking> result = bookingService.getAll();

        assertEquals(1, result.size());
        assertEquals(passengerUsername, result.getFirst().getPassenger().getUsername());
    }

    @Test
    public void testGetBookingById() throws BookingNotFoundException {
        UUID bookingId = UUID.randomUUID();
        String passengerUsername = "testuser";
        Passenger passenger = generatePassenger(passengerUsername);
        String flightNumber = "FL001";
        Flight flight = generateFlight(flightNumber, passenger);
        Booking booking = new Booking(passenger, flight);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getById(bookingId);

        assertEquals(passengerUsername, result.getPassenger().getUsername());
        assertEquals(flightNumber, result.getFlight().getFlightNumber());
    }

    @Test
    public void testCreateBooking_Success() throws BookingAlreadyExistsException, PassengerNotFoundException, FlightNotFoundException, IsThePilotException {
        String passengerUsername = "testuser";
        String flightNumber = "FL001";
        CreateBookingRequest request = new CreateBookingRequest(passengerUsername, flightNumber);
        Passenger passenger = generatePassenger(passengerUsername);
        Flight flight = generateFlight(flightNumber, generatePassenger("pilotUsername"));
        when(passengerService.getByUsername(request.getPassengerUsername())).thenReturn(passenger);
        when(flightService.getById(request.getFlightNumber())).thenReturn(flight);
        when(bookingRepository.findByFlightAndPassenger(flight, passenger)).thenReturn(new ArrayList<>());
        when(bookingRepository.save(any())).thenReturn(new Booking(passenger, flight));

        Booking result = bookingService.create(request);

        assertEquals(passengerUsername, result.getPassenger().getUsername());
        assertEquals(flightNumber, result.getFlight().getFlightNumber());
    }

    @Test
    public void testCreateBooking_AlreadyExists() throws PassengerNotFoundException, FlightNotFoundException {
        String passengerUsername = "testuser";
        String flightNumber = "FL001";
        CreateBookingRequest request = new CreateBookingRequest(passengerUsername, flightNumber);
        Passenger passenger = generatePassenger(passengerUsername);
        Flight flight = generateFlight(flightNumber, generatePassenger("pilotUsername"));
        List<Booking> existingBookings = new ArrayList<>();
        existingBookings.add(new Booking(passenger, flight));
        when(passengerService.getByUsername(request.getPassengerUsername())).thenReturn(passenger);
        when(flightService.getById(request.getFlightNumber())).thenReturn(flight);
        when(bookingRepository.findByFlightAndPassenger(flight, passenger)).thenReturn(existingBookings);

        assertThrows(BookingAlreadyExistsException.class, () -> bookingService.create(request));
    }

    @Test
    public void testCreateBooking_IsThePilot() throws PassengerNotFoundException, FlightNotFoundException {
        String pilotUsername = "pilotUsername";
        String flightNumber = "FL001";
        CreateBookingRequest request = new CreateBookingRequest(pilotUsername, flightNumber);
        Passenger pilot = generatePassenger(pilotUsername);
        Flight flight = generateFlight(flightNumber, pilot);
        when(passengerService.getByUsername(request.getPassengerUsername())).thenReturn(pilot);
        when(flightService.getById(request.getFlightNumber())).thenReturn(flight);

        assertThrows(IsThePilotException.class, () -> bookingService.create(request));
    }

    @Test
    public void testUpdateBooking_Success() throws BookingNotFoundException, BookingAlreadyExistsException, PassengerNotFoundException, FlightNotFoundException {
        UUID bookingId = UUID.randomUUID();
        String passengerUsername = "testuser";
        String flightNumber = "FL001";
        UpdateBookingRequest request = new UpdateBookingRequest(bookingId, passengerUsername, flightNumber);
        Passenger passenger = generatePassenger(passengerUsername);
        Flight flight = generateFlight(flightNumber, generatePassenger("pilotUsername"));
        Booking booking = new Booking(passenger, flight);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(passengerService.getByUsername(request.getPassengerUsername())).thenReturn(passenger);
        when(flightService.getById(request.getFlightNumber())).thenReturn(flight);
        when(bookingRepository.save(any())).thenReturn(new Booking(passenger, flight));

        Booking result = bookingService.update(request);

        assertEquals(passengerUsername, result.getPassenger().getUsername());
        assertEquals(flightNumber, result.getFlight().getFlightNumber());
    }

    @Test
    public void testUpdateBooking_NotFound() {
        UUID bookingId = UUID.randomUUID();
        UpdateBookingRequest request = new UpdateBookingRequest(bookingId, "testuser", "FL001");
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.update(request));
    }

    @Test
    public void testDeleteBooking_Success() throws BookingNotFoundException {
        UUID bookingId = UUID.randomUUID();
        when(bookingRepository.existsById(bookingId)).thenReturn(true);

        bookingService.delete(bookingId);

        verify(bookingRepository).deleteById(bookingId);
    }

    @Test
    public void testDeleteBooking_NotFound() {
        UUID bookingId = UUID.randomUUID();
        when(bookingRepository.existsById(bookingId)).thenReturn(false);

        assertThrows(BookingNotFoundException.class, () -> bookingService.delete(bookingId));
    }
}
