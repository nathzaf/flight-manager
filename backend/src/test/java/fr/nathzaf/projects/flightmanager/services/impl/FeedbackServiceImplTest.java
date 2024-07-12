package fr.nathzaf.projects.flightmanager.services.impl;

import fr.nathzaf.projects.flightmanager.dto.create.CreateFeedbackRequest;
import fr.nathzaf.projects.flightmanager.dto.update.UpdateFeedbackRequest;
import fr.nathzaf.projects.flightmanager.exceptions.PassengerUneligibleForFeedbackException;
import fr.nathzaf.projects.flightmanager.exceptions.UneligibleForFeedbackException;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.FeedbackAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.FeedbackNotFoundException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.FlightNotFoundException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.PassengerNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Feedback;
import fr.nathzaf.projects.flightmanager.models.Flight;
import fr.nathzaf.projects.flightmanager.models.Passenger;
import fr.nathzaf.projects.flightmanager.repositories.FeedbackRepository;
import fr.nathzaf.projects.flightmanager.services.BookingService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceImplTest {

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private PassengerService passengerService;

    @Mock
    private FlightService flightService;

    @Mock
    private BookingService bookingService;

    @Test
    public void testGetFeedbacksByFlight() throws FlightNotFoundException {
        String flightNumber = "FL123";
        Flight flight = new Flight(flightNumber, null, LocalDateTime.now(), null, null, null, null);
        List<Feedback> feedbacks = new ArrayList<>();
        feedbacks.add(new Feedback(null, flight, 5, "Great flight!"));
        when(flightService.getById(flightNumber)).thenReturn(flight);
        when(feedbackRepository.findByFlight(flight)).thenReturn(feedbacks);

        List<Feedback> result = feedbackService.getFeedbacksByFlight(flightNumber);

        assertEquals(1, result.size());
        assertEquals(flightNumber, result.getFirst().getFlight().getFlightNumber());
    }

    @Test
    public void testGetAllFeedbacks() {
        List<Feedback> feedbacks = new ArrayList<>();
        feedbacks.add(new Feedback());
        when(feedbackRepository.findAll()).thenReturn(feedbacks);

        List<Feedback> result = feedbackService.getAll();

        assertEquals(1, result.size());
    }

    @Test
    public void testGetFeedbackById() throws FeedbackNotFoundException {
        UUID feedbackId = UUID.randomUUID();
        Feedback feedback = new Feedback();
        feedback.setId(feedbackId);
        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(feedback));

        Feedback result = feedbackService.getById(feedbackId);

        assertEquals(feedbackId, result.getId());
    }

    @Test
    public void testCreateFeedback_Success() throws FeedbackAlreadyExistsException, PassengerNotFoundException, FlightNotFoundException, UneligibleForFeedbackException, PassengerUneligibleForFeedbackException {
        String passengerUsername = "john_doe";
        String flightNumber = "FL123";
        Integer rating = 5;
        String comment = "Great flight!";
        Passenger passenger = new Passenger("John Doe", passengerUsername, false);
        Flight flight = new Flight(flightNumber, null, LocalDateTime.now(), null, null, null, null);
        CreateFeedbackRequest request = new CreateFeedbackRequest(passengerUsername, flightNumber, rating, comment);
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger);

        when(passengerService.getByUsername(passengerUsername)).thenReturn(passenger);
        when(flightService.getById(flightNumber)).thenReturn(flight);
        when(bookingService.getPassengersBookedOnAFlight(flightNumber)).thenReturn(passengers);
        when(feedbackRepository.findByFlightAndPassenger(flight, passenger)).thenReturn(new ArrayList<>());
        when(feedbackRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Feedback result = feedbackService.create(request);

        assertNotNull(result);
        assertEquals(passengerUsername, result.getPassenger().getUsername());
        assertEquals(flightNumber, result.getFlight().getFlightNumber());
        assertEquals(rating, result.getRating());
        assertEquals(comment, result.getComment());
    }

    @Test
    public void testCreateFeedback_AlreadyExists() throws PassengerNotFoundException, FlightNotFoundException {
        String passengerUsername = "john_doe";
        String flightNumber = "FL123";
        Passenger passenger = new Passenger("John Doe", passengerUsername, false);
        Flight flight = new Flight(flightNumber, null, LocalDateTime.now(), null, null, null, null);
        CreateFeedbackRequest request = new CreateFeedbackRequest(passengerUsername, flightNumber, 5, "Great flight!");

        when(passengerService.getByUsername(passengerUsername)).thenReturn(passenger);
        when(flightService.getById(flightNumber)).thenReturn(flight);
        when(feedbackRepository.findByFlightAndPassenger(flight, passenger)).thenReturn(List.of(new Feedback()));

        assertThrows(FeedbackAlreadyExistsException.class, () -> feedbackService.create(request));
    }

    @Test
    public void testCreateFeedback_UneligibleForFeedback_FutureFlight() throws PassengerNotFoundException, FlightNotFoundException {
        String passengerUsername = "john_doe";
        String flightNumber = "FL123";
        Passenger passenger = new Passenger("John Doe", passengerUsername, false);
        Flight flight = new Flight(flightNumber, null, LocalDateTime.now().plusDays(1), null, null, null, null);
        CreateFeedbackRequest request = new CreateFeedbackRequest(passengerUsername, flightNumber, 5, "Great flight!");

        when(passengerService.getByUsername(passengerUsername)).thenReturn(passenger);
        when(flightService.getById(flightNumber)).thenReturn(flight);

        assertThrows(UneligibleForFeedbackException.class, () -> feedbackService.create(request));
    }

    @Test
    public void testCreateFeedback_PassengerUneligibleForFeedback() throws PassengerNotFoundException, FlightNotFoundException {
        String passengerUsername = "john_doe";
        String flightNumber = "FL123";
        String anotherPassengerUsername = "jane_smith";
        Passenger passenger = new Passenger("John Doe", passengerUsername, false);
        Passenger anotherPassenger = new Passenger("Jane Smith", anotherPassengerUsername, false);
        Flight flight = new Flight(flightNumber, null, LocalDateTime.now(), null, null, null, null);
        CreateFeedbackRequest request = new CreateFeedbackRequest(anotherPassengerUsername, flightNumber, 5, "Great flight!");
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger);

        when(passengerService.getByUsername(anotherPassengerUsername)).thenReturn(anotherPassenger);
        when(flightService.getById(flightNumber)).thenReturn(flight);
        when(bookingService.getPassengersBookedOnAFlight(flightNumber)).thenReturn(passengers);

        assertThrows(PassengerUneligibleForFeedbackException.class, () -> feedbackService.create(request));
    }

    @Test
    public void testUpdateFeedback_Success() throws FeedbackNotFoundException {
        UUID feedbackId = UUID.randomUUID();
        Integer newRating = 4;
        String newComment = "Good flight!";
        Feedback feedback = new Feedback();
        feedback.setId(feedbackId);
        UpdateFeedbackRequest request = new UpdateFeedbackRequest(feedbackId, newRating, newComment);

        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(feedback));
        when(feedbackRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Feedback result = feedbackService.update(request);

        assertEquals(feedbackId, result.getId());
        assertEquals(newRating, result.getRating());
        assertEquals(newComment, result.getComment());
    }

    @Test
    public void testUpdateFeedback_NotFound() {
        UUID feedbackId = UUID.randomUUID();
        UpdateFeedbackRequest request = new UpdateFeedbackRequest(feedbackId, 4, "Good flight!");

        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.empty());

        assertThrows(FeedbackNotFoundException.class, () -> feedbackService.update(request));
    }

    @Test
    public void testDeleteFeedback_Success() throws FeedbackNotFoundException {
        UUID feedbackId = UUID.randomUUID();
        when(feedbackRepository.existsById(feedbackId)).thenReturn(true);

        feedbackService.delete(feedbackId);

        verify(feedbackRepository).deleteById(feedbackId);
    }

    @Test
    public void testDeleteFeedback_NotFound() {
        UUID feedbackId = UUID.randomUUID();
        when(feedbackRepository.existsById(feedbackId)).thenReturn(false);

        assertThrows(FeedbackNotFoundException.class, () -> feedbackService.delete(feedbackId));
    }
}