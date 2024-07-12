package fr.nathzaf.projects.flightmanager.exceptions.alreadyexists;

public class PlaneRegistrationAlreadyExistsException extends Exception {

    public PlaneRegistrationAlreadyExistsException(String registration) {
        super("A Plane with the registration " + registration + " already exists.");
    }
}
