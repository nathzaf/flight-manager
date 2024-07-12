package fr.nathzaf.projects.flightmanager.exceptions.alreadyexists;

public class PassengerUsernameAlreadyExistsException extends Exception {

    public PassengerUsernameAlreadyExistsException(String username) {
        super("A Passenger with the username " + username + " already exists.");
    }
}
