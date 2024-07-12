package fr.nathzaf.projects.flightmanager.controllers;

import fr.nathzaf.projects.flightmanager.dto.create.CreateFlightRequest;
import fr.nathzaf.projects.flightmanager.dto.update.UpdateFlightRequest;
import fr.nathzaf.projects.flightmanager.exceptions.NotAPilotException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.*;
import fr.nathzaf.projects.flightmanager.models.Feedback;
import fr.nathzaf.projects.flightmanager.models.Flight;
import fr.nathzaf.projects.flightmanager.models.Passenger;
import fr.nathzaf.projects.flightmanager.services.BookingService;
import fr.nathzaf.projects.flightmanager.services.FeedbackService;
import fr.nathzaf.projects.flightmanager.services.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@Tag(
        name = "Flight API",
        description = "Manages all operations about flights : creation, get, update, delete."
)
@RequestMapping("/v1/flights")
public class FlightController {

    private final FlightService flightService;

    private final BookingService bookingService;

    private final FeedbackService feedbackService;

    public FlightController(FlightService flightService, BookingService bookingService, FeedbackService feedbackService) {
        this.flightService = flightService;
        this.bookingService = bookingService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/{number}/feedbacks")
    @Operation(summary = "Get all feedbacks of a flight", description = "Returns a list of feedbacks of a flight by its id")
    public ResponseEntity<List<Feedback>> getFeedbacksByFlight(@PathVariable String number) throws FlightNotFoundException {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByFlight(number);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/{number}/passengers")
    @Operation(summary = "Get all passengers booked on a flight", description = "Returns a list of passengers booked on flight by its id")
    public ResponseEntity<List<Passenger>> getPassengersBookedOnAFlight(@PathVariable String number) throws FlightNotFoundException {
        List<Passenger> passengers = bookingService.getPassengersBookedOnAFlight(number);
        return ResponseEntity.ok(passengers);
    }

    @GetMapping
    @Operation(summary = "Get all flights by criteria", description = "Returns a list of all flights using criteria, if none is set then returns all flights")
    public ResponseEntity<List<Flight>> getFlights(@RequestParam(required = false) UUID category,
                                                   @RequestParam(required = false) String startDate,
                                                   @RequestParam(required = false) String endDate,
                                                   @RequestParam(required = false) String departure,
                                                   @RequestParam(required = false) String arrival,
                                                   @RequestParam(required = false) String plane,
                                                   @RequestParam(required = false) String pilot) throws PassengerNotFoundException, AirfieldNotFoundException, PlaneNotFoundException, CategoryNotFoundException {
        List<Flight> flightList = flightService.getByCriteria(category, startDate, endDate, departure, arrival, plane, pilot);
        return ResponseEntity.ok(flightList);
    }

    @GetMapping("/{number}")
    @Operation(summary = "Get flight by number", description = "Returns a flight based on the provided flight number")
    public ResponseEntity<Flight> getFlightById(@PathVariable String number) throws FlightNotFoundException {
        return ResponseEntity.ok(flightService.getById(number));
    }

    @PostMapping
    @Operation(summary = "Create a new flight", description = "Creates a new flight")
    public ResponseEntity<Flight> createFlight(@RequestBody CreateFlightRequest request) throws NotAPilotException, PassengerNotFoundException, AirfieldNotFoundException, PlaneNotFoundException, CategoryNotFoundException {
        Flight flight = flightService.create(request);
        return ResponseEntity
                .created(URI.create("v1/flights/" + flight.getFlightNumber()))
                .body(flight);
    }

    @PutMapping
    @Operation(summary = "Update a flight", description = "Updates an existing flight")
    public ResponseEntity<Flight> updateFlight(@RequestBody UpdateFlightRequest request) throws FlightNotFoundException, NotAPilotException, PassengerNotFoundException, AirfieldNotFoundException, PlaneNotFoundException, CategoryNotFoundException {
        return ResponseEntity.ok(flightService.update(request));
    }

    @DeleteMapping("/{number}")
    @Operation(summary = "Delete a flight", description = "Deletes a flight based on the provided ID")
    public ResponseEntity<Void> deleteFlight(@PathVariable String number) throws FlightNotFoundException {
        flightService.delete(number);
        return ResponseEntity.ok().build();
    }
}
