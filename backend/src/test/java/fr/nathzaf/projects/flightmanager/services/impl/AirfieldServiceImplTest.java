package fr.nathzaf.projects.flightmanager.services.impl;

import fr.nathzaf.projects.flightmanager.dto.create.CreateAirfieldRequest;
import fr.nathzaf.projects.flightmanager.dto.update.UpdateAirfieldRequest;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.AirfieldAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.AirfieldNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Airfield;
import fr.nathzaf.projects.flightmanager.repositories.AirfieldRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AirfieldServiceImplTest {

    @InjectMocks
    private AirfieldServiceImpl airfieldService;

    @Mock
    private AirfieldRepository airfieldRepository;

    @Test
    public void testGetAllAirfields() {
        List<Airfield> airfields = new ArrayList<>();
        airfields.add(new Airfield("Airport 1", "ICAO1", "IATA1"));
        airfields.add(new Airfield("Airport 2", "ICAO2", "IATA2"));
        when(airfieldRepository.findAll()).thenReturn(airfields);

        List<Airfield> result = airfieldService.getAll();

        assertEquals(2, result.size());
        assertEquals("Airport 1", result.get(0).getName());
        assertEquals("ICAO2", result.get(1).getIcaoCode());
    }

    @Test
    public void testGetByICAO_ValidICAO() throws AirfieldNotFoundException {
        Airfield expectedAirfield = new Airfield("Test Airport", "ICAO", "IATA");
        when(airfieldRepository.findById("ICAO")).thenReturn(Optional.of(expectedAirfield));

        Airfield result = airfieldService.getByICAO("ICAO");

        assertEquals("Test Airport", result.getName());
    }

    @Test
    public void testGetByICAO_InvalidICAO() {
        when(airfieldRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(AirfieldNotFoundException.class, () -> airfieldService.getByICAO("InvalidICAO"));
    }

    @Test
    public void testCreateAirfield_Success() throws AirfieldAlreadyExistsException {
        CreateAirfieldRequest request = new CreateAirfieldRequest("ICAO", "New Airport", "IATA");

        when(airfieldRepository.findByIataCode("IATA")).thenReturn(Optional.empty());
        when(airfieldRepository.findById("ICAO")).thenReturn(Optional.empty());
        when(airfieldRepository.save(any())).thenReturn(new Airfield("New Airport", "ICAO", "IATA"));

        Airfield result = airfieldService.create(request);

        assertEquals("New Airport", result.getName());
        assertEquals("ICAO", result.getIcaoCode());
        assertEquals("IATA", result.getIataCode());
    }

    @Test
    public void testCreateAirfield_DuplicateIATA() {
        CreateAirfieldRequest request = new CreateAirfieldRequest("New Airport", "ICAO", "IATA");
        when(airfieldRepository.findByIataCode("IATA")).thenReturn(Optional.of(new Airfield()));

        assertThrows(AirfieldAlreadyExistsException.class, () -> airfieldService.create(request));
    }

    @Test
    public void testUpdateAirfield_Success() throws AirfieldNotFoundException {
        UpdateAirfieldRequest request = new UpdateAirfieldRequest("ICAO", "Updated Airport");
        Airfield existingAirfield = new Airfield("Airport", "ICAO", "IATA");
        when(airfieldRepository.findById("ICAO")).thenReturn(Optional.of(existingAirfield));
        when(airfieldRepository.save(any())).thenReturn(existingAirfield);

        Airfield result = airfieldService.update(request);

        assertEquals("Updated Airport", result.getName());
    }

    @Test
    public void testUpdateAirfield_NotFound() {
        UpdateAirfieldRequest request = new UpdateAirfieldRequest("NonExistingICAO", "Updated Airport");
        when(airfieldRepository.findById("NonExistingICAO")).thenReturn(Optional.empty());

        assertThrows(AirfieldNotFoundException.class, () -> airfieldService.update(request));
    }

    @Test
    public void testDeleteAirfield_Success() throws AirfieldNotFoundException {
        String icao = "ICAO";
        when(airfieldRepository.existsById(icao)).thenReturn(true);

        airfieldService.delete(icao);

        verify(airfieldRepository).deleteById(icao);
    }

    @Test
    public void testDeleteAirfield_NotFound() {
        String icao = "NonExistingICAO";
        when(airfieldRepository.existsById(icao)).thenReturn(false);

        assertThrows(AirfieldNotFoundException.class, () -> airfieldService.delete(icao));
    }
}
