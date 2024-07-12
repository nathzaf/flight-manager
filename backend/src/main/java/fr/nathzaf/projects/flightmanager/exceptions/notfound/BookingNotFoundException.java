package fr.nathzaf.projects.flightmanager.exceptions.notfound;

import java.util.UUID;

public class BookingNotFoundException extends Exception {

    public BookingNotFoundException(UUID id) {
        super("No booking of id " + id + " found.");
    }
}
