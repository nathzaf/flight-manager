package fr.nathzaf.projects.flightmanager.exceptions.notfound;

public class FlightNotFoundException extends Exception {

    public FlightNotFoundException(String flightNumber) {
        super("No flight of number " + flightNumber + " found.");
    }
}
