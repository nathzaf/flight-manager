package fr.nathzaf.projects.flightmanager.controllers;

import fr.nathzaf.projects.flightmanager.dto.create.CreateAirfieldRequest;
import fr.nathzaf.projects.flightmanager.dto.update.UpdateAirfieldRequest;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.AirfieldAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.AirfieldNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Airfield;
import fr.nathzaf.projects.flightmanager.services.AirfieldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Tag(
        name = "Airfield API",
        description = "Manages all operations about airfields : creation, get, update, delete."
)
@RequestMapping("/v1/airfields")
public class AirfieldController {

    private final AirfieldService airfieldService;

    public AirfieldController(AirfieldService airfieldService) {
        this.airfieldService = airfieldService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all airfields", description = "Returns a list of all airfields")
    public ResponseEntity<List<Airfield>> getAllAirfields() {
        return ResponseEntity.ok(airfieldService.getAll());
    }

    @GetMapping("/{icao}")
    @Operation(summary = "Retrieve an airfield by ICAO Code", description = "Returns the airfield with the specified ICAO Code")
    public ResponseEntity<Airfield> getAirfieldByICAO(@PathVariable String icao) throws AirfieldNotFoundException {
        return ResponseEntity.ok(airfieldService.getByICAO(icao));
    }

    @PostMapping
    @Operation(summary = "Create a new airfield", description = "Creates a new airfield")
    public ResponseEntity<Airfield> createAirfield(@RequestBody CreateAirfieldRequest request) throws AirfieldAlreadyExistsException {
        Airfield airfield = airfieldService.create(request);
        return ResponseEntity
                .created(URI.create("v1/airfields/" + airfield.getIcaoCode()))
                .body(airfield);
    }

    @PutMapping
    @Operation(summary = "Update an airfield", description = "Updates an existing airfield")
    public ResponseEntity<Airfield> updateAirfield(@RequestBody UpdateAirfieldRequest request) throws AirfieldNotFoundException {
        return ResponseEntity.ok(airfieldService.update(request));
    }

    @DeleteMapping("/{icao}")
    @Operation(summary = "Delete an airfield", description = "Deletes an existing airfield")
    public ResponseEntity<Void> deleteAirfield(@PathVariable String icao) throws AirfieldNotFoundException {
        airfieldService.delete(icao);
        return ResponseEntity.ok().build();
    }
}
