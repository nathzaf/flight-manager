package fr.nathzaf.projects.flightmanager.exceptions.notfound;

public class PassengerNotFoundException extends Exception {

    public PassengerNotFoundException(String username) {
        super("No Passenger of username " + username + " found.");
    }
}
