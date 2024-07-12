package fr.nathzaf.projects.flightmanager.services;

import fr.nathzaf.projects.flightmanager.dto.create.CreateFeedbackRequest;
import fr.nathzaf.projects.flightmanager.dto.update.UpdateFeedbackRequest;
import fr.nathzaf.projects.flightmanager.exceptions.PassengerUneligibleForFeedbackException;
import fr.nathzaf.projects.flightmanager.exceptions.UneligibleForFeedbackException;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.FeedbackAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.FeedbackNotFoundException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.FlightNotFoundException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.PassengerNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Feedback;

import java.util.List;
import java.util.UUID;

public interface FeedbackService {
    List<Feedback> getFeedbacksByFlight(String flightNumber) throws FlightNotFoundException;

    List<Feedback> getAll();

    Feedback getById(UUID id) throws FeedbackNotFoundException;

    Feedback create(CreateFeedbackRequest request) throws FeedbackAlreadyExistsException, PassengerNotFoundException, FlightNotFoundException, UneligibleForFeedbackException, PassengerUneligibleForFeedbackException;

    Feedback update(UpdateFeedbackRequest request) throws FeedbackNotFoundException;

    void delete(UUID id) throws FeedbackNotFoundException;
}
