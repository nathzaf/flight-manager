package fr.nathzaf.projects.flightmanager.exceptions.alreadyexists;

public class FeedbackAlreadyExistsException extends Exception {

    public FeedbackAlreadyExistsException(String flightNumber, String passengerUsername) {
        super(passengerUsername + " already gave a feedback for flight " + flightNumber);
    }
}
