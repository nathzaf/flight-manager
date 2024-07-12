package fr.nathzaf.projects.flightmanager.exceptions;

public class PassengerUneligibleForFeedbackException extends Exception {

    public PassengerUneligibleForFeedbackException(String username, String flightNumber) {
        super(username + " can't give a feedback on flight " + flightNumber + " as he hasn't booked this flight");
    }
}
