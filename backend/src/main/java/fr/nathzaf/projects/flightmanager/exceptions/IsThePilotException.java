package fr.nathzaf.projects.flightmanager.exceptions;

public class IsThePilotException extends Exception {

    public IsThePilotException(String username, String flightNumber) {
        super(username + " is the pilot of " + flightNumber + " flight so he can't book this flight");
    }
}
