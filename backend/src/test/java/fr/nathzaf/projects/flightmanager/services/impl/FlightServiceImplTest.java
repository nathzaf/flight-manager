package fr.nathzaf.projects.flightmanager.services.impl;

import fr.nathzaf.projects.flightmanager.dto.create.CreateFlightRequest;
import fr.nathzaf.projects.flightmanager.dto.update.UpdateFlightRequest;
import fr.nathzaf.projects.flightmanager.exceptions.NotAPilotException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.*;
import fr.nathzaf.projects.flightmanager.models.*;
import fr.nathzaf.projects.flightmanager.repositories.FlightRepository;
import fr.nathzaf.projects.flightmanager.services.AirfieldService;
import fr.nathzaf.projects.flightmanager.services.CategoryService;
import fr.nathzaf.projects.flightmanager.services.PassengerService;
import fr.nathzaf.projects.flightmanager.services.PlaneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlightServiceImplTest {

    @InjectMocks
    private FlightServiceImpl flightService;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private PassengerService passengerService;

    @Mock
    private PlaneService planeService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private AirfieldService airfieldService;

    private Flight testFlight;
    private Passenger testPilot;
    private Airfield testDepartureAirfield;
    private Airfield testArrivalAirfield;
    private Plane testPlane;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testPilot = new Passenger("John Doe", "john.doe", true);
        testDepartureAirfield = new Airfield("DEPT", "Departure Airport", "DEP");
        testArrivalAirfield = new Airfield("ARRI", "Arrival Airport", "ARR");
        testPlane = new Plane("ABC123", "Boeing 737", "https://example.com/plane.jpg");
        testCategory = new Category("Test Category");

        testFlight = new Flight("ABC123", testCategory, LocalDateTime.now(),
                testDepartureAirfield, testArrivalAirfield, testPlane, testPilot);
    }

    @Test
    void testGetByCriteria() throws CategoryNotFoundException, AirfieldNotFoundException, PlaneNotFoundException, PassengerNotFoundException {
        when(flightRepository.findByCriteria(any(), any(), any(), any(), any(), any(), any())).thenReturn(List.of(testFlight));

        List<Flight> flights = flightService.getByCriteria(null, null, null, null, null, null, null);
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals(testFlight.getFlightNumber(), flights.getFirst().getFlightNumber());
    }

    @Test
    void testGetByCategory() throws CategoryNotFoundException {
        when(categoryService.getById(any())).thenReturn(testCategory);
        when(flightRepository.findByCategory(any())).thenReturn(List.of(testFlight));

        List<Flight> flights = flightService.getByCategory(UUID.randomUUID());
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals(testFlight.getFlightNumber(), flights.getFirst().getFlightNumber());
    }

    @Test
    void testGetByIdFlightFound() throws FlightNotFoundException {
        when(flightRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(testFlight));

        Flight foundFlight = flightService.getById("ABC123");
        assertNotNull(foundFlight);
        assertEquals(testFlight.getFlightNumber(), foundFlight.getFlightNumber());
    }

    @Test
    void testGetByIdFlightNotFound() {
        when(flightRepository.findById(any())).thenReturn(java.util.Optional.empty());

        assertThrows(FlightNotFoundException.class, () -> flightService.getById("XYZ999"));
    }

    @Test
    void testCreateFlight() throws NotAPilotException, PassengerNotFoundException, AirfieldNotFoundException, PlaneNotFoundException, CategoryNotFoundException {
        CreateFlightRequest request = new CreateFlightRequest(UUID.randomUUID(), LocalDateTime.now(),
                "DEPT", "ARRI", "ABC123", "john.doe");

        when(passengerService.getByUsername("john.doe")).thenReturn(testPilot);
        when(airfieldService.getByICAO("DEPT")).thenReturn(testDepartureAirfield);
        when(airfieldService.getByICAO("ARRI")).thenReturn(testArrivalAirfield);
        when(planeService.getByRegistration("ABC123")).thenReturn(testPlane);
        when(categoryService.getById(any())).thenReturn(testCategory);
        when(flightRepository.save(any())).thenReturn(testFlight);

        Flight createdFlight = flightService.create(request);
        assertNotNull(createdFlight);
        assertEquals(testFlight.getFlightNumber(), createdFlight.getFlightNumber());
    }

    @Test
    void testCreateFlightNotAPilot() throws PassengerNotFoundException {
        CreateFlightRequest request = new CreateFlightRequest(UUID.randomUUID(), LocalDateTime.now(),
                "DEPT", "ARRI", "ABC123", "john.doe");

        when(passengerService.getByUsername("john.doe")).thenReturn(new Passenger("John Doe", "john.doe", false));

        assertThrows(NotAPilotException.class, () -> flightService.create(request));
    }

    @Test
    void testUpdateFlight() throws FlightNotFoundException, NotAPilotException, PassengerNotFoundException, AirfieldNotFoundException, PlaneNotFoundException, CategoryNotFoundException {
        UpdateFlightRequest request = new UpdateFlightRequest("ABC123", UUID.randomUUID(), LocalDateTime.now(),
                "DEPT", "ARRI", "ABC123", "john.doe");

        when(passengerService.getByUsername("john.doe")).thenReturn(testPilot);
        when(airfieldService.getByICAO("DEPT")).thenReturn(testDepartureAirfield);
        when(airfieldService.getByICAO("ARRI")).thenReturn(testArrivalAirfield);
        when(planeService.getByRegistration("ABC123")).thenReturn(testPlane);
        when(categoryService.getById(any())).thenReturn(testCategory);
        when(flightRepository.findById("ABC123")).thenReturn(java.util.Optional.ofNullable(testFlight));
        when(flightRepository.save(any())).thenReturn(testFlight);

        Flight updatedFlight = flightService.update(request);
        assertNotNull(updatedFlight);
        assertEquals(testFlight.getFlightNumber(), updatedFlight.getFlightNumber());
    }

    @Test
    void testDeleteFlight() throws FlightNotFoundException {
        when(flightRepository.existsById("ABC123")).thenReturn(true);

        flightService.delete("ABC123");
        verify(flightRepository).deleteById("ABC123");
    }

    @Test
    void testDeleteFlightNotFound() {
        when(flightRepository.existsById("XYZ999")).thenReturn(false);

        assertThrows(FlightNotFoundException.class, () -> flightService.delete("XYZ999"));
    }
}
