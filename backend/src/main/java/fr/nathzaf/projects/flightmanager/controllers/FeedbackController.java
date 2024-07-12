package fr.nathzaf.projects.flightmanager.controllers;

import fr.nathzaf.projects.flightmanager.dto.create.CreateFeedbackRequest;
import fr.nathzaf.projects.flightmanager.dto.update.UpdateFeedbackRequest;
import fr.nathzaf.projects.flightmanager.exceptions.PassengerUneligibleForFeedbackException;
import fr.nathzaf.projects.flightmanager.exceptions.UneligibleForFeedbackException;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.FeedbackAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.FeedbackNotFoundException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.FlightNotFoundException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.PassengerNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Feedback;
import fr.nathzaf.projects.flightmanager.services.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@Tag(
        name = "Feedback API",
        description = "Manages all operations about feedbacks : creation, get, update, delete."
)
@RequestMapping("/v1/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    @Operation(summary = "Get all feedbacks", description = "Returns a list of all feedbacks")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get feedback by ID", description = "Returns a feedback by its ID")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable UUID id) throws FeedbackNotFoundException {
        return ResponseEntity.ok(feedbackService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new feedback", description = "Creates a new feedback")
    public ResponseEntity<Feedback> createFeedback(@RequestBody CreateFeedbackRequest request) throws FeedbackAlreadyExistsException, FlightNotFoundException, PassengerNotFoundException, UneligibleForFeedbackException, PassengerUneligibleForFeedbackException {
        Feedback feedback = feedbackService.create(request);
        return ResponseEntity
                .created(URI.create("v1/feedbacks/" + feedback.getId()))
                .body(feedback);
    }

    @PutMapping
    @Operation(summary = "Update a feedback", description = "Updates an existing feedback")
    public ResponseEntity<Feedback> updateFeedback(@RequestBody UpdateFeedbackRequest request) throws FeedbackNotFoundException {
        return ResponseEntity.ok(feedbackService.update(request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a feedback", description = "Deletes a feedback by its ID")
    public ResponseEntity<Void> deleteFeedback(@PathVariable UUID id) throws FeedbackNotFoundException {
        feedbackService.delete(id);
        return ResponseEntity.ok().build();
    }
}
