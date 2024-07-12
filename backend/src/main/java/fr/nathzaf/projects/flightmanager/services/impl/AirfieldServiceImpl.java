package fr.nathzaf.projects.flightmanager.services.impl;

import fr.nathzaf.projects.flightmanager.dto.create.CreateAirfieldRequest;
import fr.nathzaf.projects.flightmanager.dto.update.UpdateAirfieldRequest;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.AirfieldAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.AirfieldNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Airfield;
import fr.nathzaf.projects.flightmanager.repositories.AirfieldRepository;
import fr.nathzaf.projects.flightmanager.services.AirfieldService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirfieldServiceImpl implements AirfieldService {

    private final AirfieldRepository airfieldRepository;

    public AirfieldServiceImpl(AirfieldRepository airfieldRepository) {
        this.airfieldRepository = airfieldRepository;
    }

    @Override
    public List<Airfield> getAll() {
        return airfieldRepository.findAll();
    }

    @Override
    public Airfield getByICAO(String icao) throws AirfieldNotFoundException {
        return airfieldRepository.findById(icao)
                .orElseThrow(() -> new AirfieldNotFoundException(icao));
    }

    @Override
    public Airfield create(CreateAirfieldRequest request) throws AirfieldAlreadyExistsException {
        if (airfieldRepository.findByIataCode(request.getIataCode()).isPresent() ||
                airfieldRepository.findById(request.getIcaoCode()).isPresent())
            throw new AirfieldAlreadyExistsException(request.getIataCode(), request.getIcaoCode());
        Airfield airfield = new Airfield(request.getName(), request.getIcaoCode(), request.getIataCode());
        return airfieldRepository.save(airfield);
    }

    @Override
    public Airfield update(UpdateAirfieldRequest request) throws AirfieldNotFoundException {
        Airfield airfield = getByICAO(request.getIcaoCode());
        airfield.setName(request.getName());
        return airfieldRepository.save(airfield);
    }

    @Override
    public void delete(String icao) throws AirfieldNotFoundException {
        if (!airfieldRepository.existsById(icao))
            throw new AirfieldNotFoundException(icao);
        airfieldRepository.deleteById(icao);
    }
}
