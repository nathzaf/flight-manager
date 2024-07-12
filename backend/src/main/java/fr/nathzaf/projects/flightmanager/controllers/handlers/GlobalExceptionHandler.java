package fr.nathzaf.projects.flightmanager.controllers.handlers;

import fr.nathzaf.projects.flightmanager.exceptions.IsThePilotException;
import fr.nathzaf.projects.flightmanager.exceptions.NotAPilotException;
import fr.nathzaf.projects.flightmanager.exceptions.PassengerUneligibleForFeedbackException;
import fr.nathzaf.projects.flightmanager.exceptions.UneligibleForFeedbackException;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.*;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            CategoryNotFoundException.class,
            PassengerNotFoundException.class,
            BookingNotFoundException.class,
            FlightNotFoundException.class,
            FeedbackNotFoundException.class,
            AirfieldNotFoundException.class,
            PlaneNotFoundException.class,
    })
    public ResponseEntity<String> handleNotFoundException(Exception e) {
        LOG.warn("[NOT FOUND] {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler({
            CategoryNameAlreadyExistsException.class,
            AirfieldAlreadyExistsException.class,
            BookingAlreadyExistsException.class,
            FeedbackAlreadyExistsException.class,
            PassengerUsernameAlreadyExistsException.class,
            PlaneRegistrationAlreadyExistsException.class,
            NotAPilotException.class,
            UneligibleForFeedbackException.class,
            IsThePilotException.class,
            PassengerUneligibleForFeedbackException.class
    })
    public ResponseEntity<String> handleNameAlreadyExistsException(Exception e) {
        LOG.warn("[BAD REQUEST] {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
}
