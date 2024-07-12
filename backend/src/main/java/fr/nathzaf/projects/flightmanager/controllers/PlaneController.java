package fr.nathzaf.projects.flightmanager.controllers;

import fr.nathzaf.projects.flightmanager.dto.CreateUpdatePlaneRequest;
import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.PlaneRegistrationAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.PlaneNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Plane;
import fr.nathzaf.projects.flightmanager.services.PlaneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Tag(
        name = "Plane API",
        description = "Manages all operations about planes : creation, get, update, delete."
)
@RequestMapping("/v1/planes")
public class PlaneController {

    private final PlaneService planeService;

    public PlaneController(PlaneService planeService) {
        this.planeService = planeService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all planes")
    public ResponseEntity<List<Plane>> getPlanes() {
        return ResponseEntity.ok(planeService.getAll());
    }

    @GetMapping("/{registration}")
    @Operation(summary = "Retrieve a plane by registration")
    public ResponseEntity<Plane> getPlaneByRegistration(@PathVariable String registration) throws PlaneNotFoundException {
        return ResponseEntity.ok(planeService.getByRegistration(registration));
    }

    @PostMapping
    @Operation(summary = "Create a new plane")
    public ResponseEntity<Plane> createPlane(@RequestBody CreateUpdatePlaneRequest request) throws PlaneRegistrationAlreadyExistsException {
        Plane plane = planeService.create(request);
        return ResponseEntity
                .created(URI.create("v1/planes/" + plane.getRegistration()))
                .body(plane);
    }

    @PutMapping
    @Operation(summary = "Update a plane")
    public ResponseEntity<Plane> updatePlane(@RequestBody CreateUpdatePlaneRequest request) throws PlaneNotFoundException, PlaneRegistrationAlreadyExistsException {
        Plane plane = planeService.update(request);
        return ResponseEntity.ok(plane);
    }

    @DeleteMapping("/{registration}")
    @Operation(summary = "Delete a plane by ID")
    public ResponseEntity<Void> deletePlane(@PathVariable String registration) throws PlaneNotFoundException {
        planeService.delete(registration);
        return ResponseEntity.ok().build();
    }
}
