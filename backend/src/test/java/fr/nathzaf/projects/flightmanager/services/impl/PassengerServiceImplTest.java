package fr.nathzaf.projects.flightmanager.services.impl;

import fr.nathzaf.projects.flightmanager.dto.CreateUpdatePassengerRequest;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.PassengerUsernameAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.PassengerNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Passenger;
import fr.nathzaf.projects.flightmanager.repositories.PassengerRepository;
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
public class PassengerServiceImplTest {

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    @Test
    void testGetAllPassengers() {
        Passenger testPassenger = new Passenger("John Doe", "johndoe", false);
        when(passengerRepository.findAll()).thenReturn(List.of(testPassenger));

        List<Passenger> passengers = passengerService.getAll();

        assertEquals(1, passengers.size());
        assertEquals(testPassenger.getUsername(), passengers.getFirst().getUsername());
    }

    @Test
    void testGetByUsernamePassengerFound() throws PassengerNotFoundException {
        Passenger testPassenger = new Passenger("John Doe", "johndoe", false);
        when(passengerRepository.findById("johndoe")).thenReturn(Optional.of(testPassenger));

        Passenger foundPassenger = passengerService.getByUsername("johndoe");

        assertEquals(testPassenger.getUsername(), foundPassenger.getUsername());
    }

    @Test
    void testGetByUsernamePassengerNotFound() {
        when(passengerRepository.findById("unknownuser")).thenReturn(Optional.empty());

        assertThrows(PassengerNotFoundException.class, () -> passengerService.getByUsername("unknownuser"));
    }

    @Test
    void testCreatePassenger() throws PassengerUsernameAlreadyExistsException {
        CreateUpdatePassengerRequest request = new CreateUpdatePassengerRequest("Jane Doe", "janedoe", false);
        when(passengerRepository.findById("janedoe")).thenReturn(Optional.empty());
        when(passengerRepository.save(any())).thenReturn(new Passenger("Jane Doe", "janedoe", false));

        Passenger createdPassenger = passengerService.create(request);

        assertNotNull(createdPassenger);
        assertEquals(request.getUsername(), createdPassenger.getUsername());
    }

    @Test
    void testCreatePassengerAlreadyExists() {
        Passenger testPassenger = new Passenger("John Doe", "johndoe", false);
        CreateUpdatePassengerRequest request = new CreateUpdatePassengerRequest("Jane Doe", "johndoe", false);
        when(passengerRepository.findById("johndoe")).thenReturn(Optional.of(testPassenger));

        assertThrows(PassengerUsernameAlreadyExistsException.class, () -> passengerService.create(request));
    }

    @Test
    void testDeletePassenger() throws PassengerNotFoundException {
        when(passengerRepository.existsById("johndoe")).thenReturn(true);

        passengerService.delete("johndoe");

        verify(passengerRepository).deleteById("johndoe");
    }

    @Test
    void testDeletePassengerNotFound() {
        when(passengerRepository.existsById("unknownuser")).thenReturn(false);

        assertThrows(PassengerNotFoundException.class, () -> passengerService.delete("unknownuser"));
    }

    @Test
    void testUpdatePassenger() throws PassengerNotFoundException {
        Passenger testPassenger = new Passenger("John Doe", "johndoe", false);
        CreateUpdatePassengerRequest request = new CreateUpdatePassengerRequest("Jane Doe Updated", "johndoe", true);
        when(passengerRepository.findById("johndoe")).thenReturn(Optional.of(testPassenger));
        when(passengerRepository.save(any())).thenReturn(new Passenger("Jane Doe Updated", "johndoe", true));

        Passenger updatedPassenger = passengerService.update(request);

        assertNotNull(updatedPassenger);
        assertEquals(request.getName(), updatedPassenger.getName());
        assertEquals(request.getIsPilot(), updatedPassenger.getIsPilot());
    }

    @Test
    void testUpdatePassengerNotFound() {
        CreateUpdatePassengerRequest request = new CreateUpdatePassengerRequest("Jane Doe Updated", "unknownuser", true);
        when(passengerRepository.findById("unknownuser")).thenReturn(Optional.empty());

        assertThrows(PassengerNotFoundException.class, () -> passengerService.update(request));
    }

    @Test
    void testGetPilots() {
        Passenger testPassenger = new Passenger("John Doe", "johndoe", true);
        when(passengerRepository.findByIsPilotTrue()).thenReturn(List.of(testPassenger));

        List<Passenger> pilots = passengerService.getPilots();

        assertEquals(1, pilots.size());
        assertTrue(pilots.getFirst().getIsPilot());
    }
}