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
import fr.nathzaf.projects.flightmanager.services.FeedbackService;
import fr.nathzaf.projects.flightmanager.services.FlightService;
import fr.nathzaf.projects.flightmanager.services.PassengerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    private final PassengerService passengerService;

    private final FlightService flightService;

    private final BookingService bookingService;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, PassengerService passengerService, FlightService flightService, BookingService bookingService) {
        this.feedbackRepository = feedbackRepository;
        this.passengerService = passengerService;
        this.flightService = flightService;
        this.bookingService = bookingService;
    }

    @Override
    public List<Feedback> getFeedbacksByFlight(String flightNumber) throws FlightNotFoundException {
        Flight flight = flightService.getById(flightNumber);
        return feedbackRepository.findByFlight(flight);
    }

    @Override
    public List<Feedback> getAll() {
        return feedbackRepository.findAll();
    }

    @Override
    public Feedback getById(UUID id) throws FeedbackNotFoundException {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new FeedbackNotFoundException(id));
    }

    @Override
    public Feedback create(CreateFeedbackRequest request) throws FeedbackAlreadyExistsException, PassengerNotFoundException, FlightNotFoundException, UneligibleForFeedbackException, PassengerUneligibleForFeedbackException {
        Passenger passenger = passengerService.getByUsername(request.getPassengerUsername());
        Flight flight = flightService.getById(request.getFlightNumber());
        List<Passenger> passengersBookedOnAFlight = bookingService.getPassengersBookedOnAFlight(request.getFlightNumber());
        if (!feedbackRepository.findByFlightAndPassenger(flight, passenger).isEmpty())
            throw new FeedbackAlreadyExistsException(flight.getFlightNumber(), passenger.getUsername());
        if (flight.getDate().isAfter(LocalDateTime.now()))
            throw new UneligibleForFeedbackException();
        if (passengersBookedOnAFlight.stream().noneMatch(p -> p.getUsername().equals(request.getPassengerUsername()))) {
            throw new PassengerUneligibleForFeedbackException(request.getPassengerUsername(), request.getFlightNumber());
        }

        Feedback feedback = new Feedback(passenger, flight, request.getRating(), request.getComment());
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback update(UpdateFeedbackRequest request) throws FeedbackNotFoundException {
        Feedback feedback = getById(request.getId());
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());
        return feedbackRepository.save(feedback);
    }

    @Override
    public void delete(UUID id) throws FeedbackNotFoundException {
        if (!feedbackRepository.existsById(id))
            throw new FeedbackNotFoundException(id);
        feedbackRepository.deleteById(id);
    }
}
