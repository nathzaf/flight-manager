package fr.nathzaf.projects.flightmanager.services.impl;

import fr.nathzaf.projects.flightmanager.dto.CreateUpdatePlaneRequest;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.PlaneRegistrationAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.PlaneNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Plane;
import fr.nathzaf.projects.flightmanager.repositories.PlaneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlaneServiceImplTest {

    @Mock
    private PlaneRepository planeRepository;

    @InjectMocks
    private PlaneServiceImpl planeService;


    @Test
    void testGetAllPlanes() {
        Plane testPlane = new Plane("ABC123", "Boeing 737", "https://example.com/plane.jpg");
        when(planeRepository.findAll()).thenReturn(List.of(testPlane));

        List<Plane> planes = planeService.getAll();

        assertEquals(1, planes.size());
        assertEquals(testPlane.getRegistration(), planes.getFirst().getRegistration());
    }

    @Test
    void testGetByRegistrationPlaneFound() throws PlaneNotFoundException {
        Plane testPlane = new Plane("ABC123", "Boeing 737", "https://example.com/plane.jpg");
        when(planeRepository.findById("ABC123")).thenReturn(Optional.of(testPlane));

        Plane foundPlane = planeService.getByRegistration("ABC123");

        assertEquals(testPlane.getRegistration(), foundPlane.getRegistration());
    }

    @Test
    void testGetByRegistrationPlaneNotFound() {
        when(planeRepository.findById("XYZ999")).thenReturn(Optional.empty());

        assertThrows(PlaneNotFoundException.class, () -> planeService.getByRegistration("XYZ999"));
    }

    @Test
    void testCreatePlane() throws PlaneRegistrationAlreadyExistsException {
        CreateUpdatePlaneRequest request = new CreateUpdatePlaneRequest("DEF456", "Airbus A320", "https://example.com/another-plane.jpg");
        when(planeRepository.findPlanesByRegistration("DEF456")).thenReturn(List.of());
        when(planeRepository.save(any())).thenReturn(new Plane("DEF456", "Airbus A320", "https://example.com/another-plane.jpg"));

        Plane createdPlane = planeService.create(request);

        assertNotNull(createdPlane);
        assertEquals(request.getRegistration(), createdPlane.getRegistration());
    }

    @Test
    void testCreatePlaneAlreadyExists() {
        Plane testPlane = new Plane("ABC123", "Boeing 737", "https://example.com/plane.jpg");
        CreateUpdatePlaneRequest request = new CreateUpdatePlaneRequest("ABC123", "Boeing 737", "https://example.com/plane.jpg");

        when(planeRepository.findPlanesByRegistration("ABC123")).thenReturn(List.of(testPlane));

        assertThrows(PlaneRegistrationAlreadyExistsException.class, () -> planeService.create(request));
    }

    @Test
    void testUpdatePlane() throws PlaneRegistrationAlreadyExistsException, PlaneNotFoundException {
        Plane testPlane = new Plane("ABC123", "Boeing 737", "https://example.com/plane.jpg");
        CreateUpdatePlaneRequest request = new CreateUpdatePlaneRequest("ABC123", "Boeing 737 MAX", "https://example.com/updated-plane.jpg");
        when(planeRepository.findPlanesByRegistration("ABC123")).thenReturn(List.of());
        when(planeRepository.findById("ABC123")).thenReturn(Optional.of(testPlane));
        when(planeRepository.save(any())).thenReturn(new Plane("ABC123", "Boeing 737 MAX", "https://example.com/updated-plane.jpg"));

        Plane updatedPlane = planeService.update(request);

        assertNotNull(updatedPlane);
        assertEquals(request.getRegistration(), updatedPlane.getRegistration());
        assertEquals(request.getType(), updatedPlane.getType());
        assertEquals(request.getPictureLink(), updatedPlane.getPictureLink());
    }

    @Test
    void testUpdatePlaneAlreadyExists() {
        CreateUpdatePlaneRequest request = new CreateUpdatePlaneRequest("DEF456", "Airbus A320", "https://example.com/another-plane.jpg");
        when(planeRepository.findPlanesByRegistration("DEF456")).thenReturn(List.of(new Plane("DEF456", "Airbus A320", "https://example.com/another-plane.jpg")));
        assertThrows(PlaneRegistrationAlreadyExistsException.class, () -> planeService.update(request));
    }

    @Test
    void testDeletePlane() {
        when(planeRepository.existsById("ABC123")).thenReturn(true);
        assertDoesNotThrow(() -> planeService.delete("ABC123"));
        verify(planeRepository).deleteById("ABC123");
    }

    @Test
    void testDeletePlaneNotFound() {
        when(planeRepository.existsById("XYZ999")).thenReturn(false);
        assertThrows(PlaneNotFoundException.class, () -> planeService.delete("XYZ999"));
    }
}
