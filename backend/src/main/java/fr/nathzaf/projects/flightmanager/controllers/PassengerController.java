package fr.nathzaf.projects.flightmanager.controllers;

import fr.nathzaf.projects.flightmanager.dto.CreateUpdatePassengerRequest;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.PassengerUsernameAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.PassengerNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Flight;
import fr.nathzaf.projects.flightmanager.models.Passenger;
import fr.nathzaf.projects.flightmanager.services.BookingService;
import fr.nathzaf.projects.flightmanager.services.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Tag(
        name = "Passenger API",
        description = "Manages all operations about passengers : creation, get, update, delete."
)
@RequestMapping("/v1/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    private final BookingService bookingService;

    public PassengerController(PassengerService passengerService, BookingService bookingService) {
        this.passengerService = passengerService;
        this.bookingService = bookingService;
    }

    @GetMapping("/pilots")
    @Operation(summary = "Retrieve all pilots")
    public ResponseEntity<List<Passenger>> getPilots() {
        return ResponseEntity.ok(passengerService.getPilots());
    }

    @GetMapping("/{username}/flights")
    @Operation(summary = "Get all the flights a passenger has booked", description = "Get a list of flight containing all flight a passenger has booked by its username")
    public ResponseEntity<List<Flight>> getFlightsBooked(@PathVariable String username) throws PassengerNotFoundException {
        return ResponseEntity.ok(bookingService.getFlightsBookedByPassenger(username));
    }

    @GetMapping
    @Operation(summary = "Retrieve all passengers")
    public ResponseEntity<List<Passenger>> getPassengers() {
        return ResponseEntity.ok(passengerService.getAll());
    }

    @GetMapping("/{username}")
    @Operation(summary = "Retrieve a passenger", description = "Retrieve a passenger by username")
    public ResponseEntity<Passenger> getPassengerById(@PathVariable String username) throws PassengerNotFoundException {
        return ResponseEntity.ok(passengerService.getByUsername(username));
    }

    @PostMapping
    @Operation(summary = "Create a new passenger", description = "Creates a new passenger")
    public ResponseEntity<Passenger> createPassenger(@RequestBody CreateUpdatePassengerRequest request) throws PassengerUsernameAlreadyExistsException {
        Passenger passenger = passengerService.create(request);
        return ResponseEntity
                .created(URI.create("v1/passengers/" + passenger.getUsername()))
                .body(passenger);
    }

    @PutMapping
    @Operation(summary = "Update a passenger", description = "Update a passenger")
    public ResponseEntity<Passenger> updatePassenger(@RequestBody CreateUpdatePassengerRequest request) throws PassengerNotFoundException {
        Passenger passenger = passengerService.update(request);
        return ResponseEntity.ok(passenger);
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete a passenger", description = "Delete a passenger by ID")
    public ResponseEntity<Void> deletePassenger(@PathVariable String username) throws PassengerNotFoundException {
        passengerService.delete(username);
        return ResponseEntity.ok().build();
    }

}
