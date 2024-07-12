package fr.nathzaf.projects.flightmanager.exceptions.notfound;

public class AirfieldNotFoundException extends Exception {

    public AirfieldNotFoundException(String icao) {
        super("No Airfield of ICAO Code " + icao + " found.");
    }
}
