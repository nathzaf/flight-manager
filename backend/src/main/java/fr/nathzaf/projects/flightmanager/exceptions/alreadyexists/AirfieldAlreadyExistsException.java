package fr.nathzaf.projects.flightmanager.exceptions.alreadyexists;

public class AirfieldAlreadyExistsException extends Exception {

    public AirfieldAlreadyExistsException(String iacoCode, String iataCode) {
        super("An airfield with IACO code " + iacoCode + " and/or IATA code " + iacoCode + " already exists");
    }
}
