package fr.nathzaf.projects.flightmanager.controllers;

import fr.nathzaf.projects.flightmanager.dto.create.CreateBookingRequest;
import fr.nathzaf.projects.flightmanager.dto.update.UpdateBookingRequest;
import fr.nathzaf.projects.flightmanager.exceptions.IsThePilotException;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.BookingAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.BookingNotFoundException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.FlightNotFoundException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.PassengerNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Booking;
import fr.nathzaf.projects.flightmanager.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@Tag(
        name = "Booking API",
        description = "Manages all operations about bookings : creation, get, update, delete."
)
@RequestMapping("/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    @Operation(summary = "Get all bookings", description = "Returns a list of all bookings")
    public ResponseEntity<List<Booking>> getAllBooking() {
        return ResponseEntity.ok(bookingService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID", description = "Returns a booking by its ID")
    public ResponseEntity<Booking> getBookingById(@PathVariable UUID id) throws BookingNotFoundException {
        return ResponseEntity.ok(bookingService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new booking", description = "Creates a new booking")
    public ResponseEntity<Booking> createBooking(@RequestBody CreateBookingRequest request) throws BookingAlreadyExistsException, FlightNotFoundException, PassengerNotFoundException, IsThePilotException {
        Booking booking = bookingService.create(request);
        return ResponseEntity
                .created(URI.create("v1/bookings/" + booking.getId()))
                .body(booking);
    }

    @PutMapping
    @Operation(summary = "Update a booking", description = "Updates an existing booking")
    public ResponseEntity<Booking> updateBooking(@RequestBody UpdateBookingRequest request) throws BookingNotFoundException, BookingAlreadyExistsException, FlightNotFoundException, PassengerNotFoundException {
        return ResponseEntity.ok(bookingService.update(request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a booking", description = "Deletes a booking by its ID")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID id) throws BookingNotFoundException {
        bookingService.delete(id);
        return ResponseEntity.ok().build();
    }
}
