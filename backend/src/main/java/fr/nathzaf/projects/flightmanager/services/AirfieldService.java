package fr.nathzaf.projects.flightmanager.services;

import fr.nathzaf.projects.flightmanager.dto.create.CreateAirfieldRequest;
import fr.nathzaf.projects.flightmanager.dto.update.UpdateAirfieldRequest;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.AirfieldAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.AirfieldNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Airfield;

import java.util.List;

public interface AirfieldService {
    List<Airfield> getAll();

    Airfield getByICAO(String icao) throws AirfieldNotFoundException;

    Airfield create(CreateAirfieldRequest request) throws AirfieldAlreadyExistsException;

    Airfield update(UpdateAirfieldRequest request) throws AirfieldNotFoundException;

    void delete(String icao) throws AirfieldNotFoundException;
}
