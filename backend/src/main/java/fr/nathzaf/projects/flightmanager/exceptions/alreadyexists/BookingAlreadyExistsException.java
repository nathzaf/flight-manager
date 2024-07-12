package fr.nathzaf.projects.flightmanager.exceptions.alreadyexists;

public class BookingAlreadyExistsException extends Exception {

    public BookingAlreadyExistsException(String flightNumber, String passengerUsername) {
        super(passengerUsername + " has already booked for flight " + flightNumber);
    }
}
