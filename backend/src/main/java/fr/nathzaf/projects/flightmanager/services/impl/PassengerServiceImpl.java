package fr.nathzaf.projects.flightmanager.services.impl;

import fr.nathzaf.projects.flightmanager.dto.CreateUpdatePassengerRequest;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.PassengerUsernameAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.PassengerNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Passenger;
import fr.nathzaf.projects.flightmanager.repositories.PassengerRepository;
import fr.nathzaf.projects.flightmanager.services.PassengerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    public PassengerServiceImpl(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Override
    public List<Passenger> getAll() {
        return passengerRepository.findAll();
    }

    @Override
    public Passenger getByUsername(String username) throws PassengerNotFoundException {
        return passengerRepository.findById(username)
                .orElseThrow(() -> new PassengerNotFoundException(username));
    }

    @Override
    public Passenger create(CreateUpdatePassengerRequest request) throws PassengerUsernameAlreadyExistsException {
        if (passengerRepository.findById(request.getUsername()).isPresent())
            throw new PassengerUsernameAlreadyExistsException(request.getUsername());
        Passenger passenger = new Passenger(request.getName(), request.getUsername(), request.getIsPilot());
        return passengerRepository.save(passenger);
    }

    @Override
    public void delete(String username) throws PassengerNotFoundException {
        if (!passengerRepository.existsById(username))
            throw new PassengerNotFoundException(username);
        passengerRepository.deleteById(username);
    }

    @Override
    public Passenger update(CreateUpdatePassengerRequest request) throws PassengerNotFoundException {
        Passenger passenger = getByUsername(request.getUsername()); //throws exception if not found
        passenger.setName(request.getName());
        passenger.setIsPilot(request.getIsPilot());
        return passengerRepository.save(passenger);
    }

    @Override
    public List<Passenger> getPilots() {
        return passengerRepository.findByIsPilotTrue();
    }
}
