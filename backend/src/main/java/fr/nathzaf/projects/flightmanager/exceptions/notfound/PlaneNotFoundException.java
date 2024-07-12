package fr.nathzaf.projects.flightmanager.exceptions.notfound;

public class PlaneNotFoundException extends Exception {

    public PlaneNotFoundException(String registration) {
        super("No Plane of registration " + registration + " found.");
    }
}
