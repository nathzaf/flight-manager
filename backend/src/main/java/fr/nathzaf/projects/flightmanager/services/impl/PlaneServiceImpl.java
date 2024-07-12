package fr.nathzaf.projects.flightmanager.services.impl;

import fr.nathzaf.projects.flightmanager.dto.CreateUpdatePlaneRequest;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.PlaneRegistrationAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.PlaneNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Plane;
import fr.nathzaf.projects.flightmanager.repositories.PlaneRepository;
import fr.nathzaf.projects.flightmanager.services.PlaneService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaneServiceImpl implements PlaneService {

    private final PlaneRepository planeRepository;

    public PlaneServiceImpl(PlaneRepository planeRepository) {
        this.planeRepository = planeRepository;
    }

    @Override
    public List<Plane> getAll() {
        return planeRepository.findAll();
    }

    @Override
    public Plane getByRegistration(String registration) throws PlaneNotFoundException {
        return planeRepository.findById(registration)
                .orElseThrow(() -> new PlaneNotFoundException(registration));
    }

    @Override
    public Plane create(CreateUpdatePlaneRequest request) throws PlaneRegistrationAlreadyExistsException {
        if (!planeRepository.findPlanesByRegistration(request.getRegistration()).isEmpty())
            throw new PlaneRegistrationAlreadyExistsException(request.getRegistration());
        Plane plane = new Plane(request.getRegistration(), request.getType(), request.getPictureLink());
        return planeRepository.save(plane);
    }

    @Override
    public Plane update(CreateUpdatePlaneRequest request) throws PlaneNotFoundException, PlaneRegistrationAlreadyExistsException {
        if (!planeRepository.findPlanesByRegistration(request.getRegistration()).isEmpty())
            throw new PlaneRegistrationAlreadyExistsException(request.getRegistration());
        Plane plane = getByRegistration(request.getRegistration());
        plane.setRegistration(request.getRegistration());
        plane.setType(request.getType());
        return planeRepository.save(plane);
    }

    @Override
    public void delete(String registration) throws PlaneNotFoundException {
        if (!planeRepository.existsById(registration))
            throw new PlaneNotFoundException(registration);
        planeRepository.deleteById(registration);
    }
}
