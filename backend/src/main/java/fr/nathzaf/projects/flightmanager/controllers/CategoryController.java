package fr.nathzaf.projects.flightmanager.controllers;

import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.CategoryNameAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.CategoryNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Category;
import fr.nathzaf.projects.flightmanager.models.Flight;
import fr.nathzaf.projects.flightmanager.services.CategoryService;
import fr.nathzaf.projects.flightmanager.services.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@Tag(
        name = "Category API",
        description = "Manages all operations about categories : creation, get, update, delete."
)
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    private final FlightService flightService;

    public CategoryController(CategoryService categoryService, FlightService flightService) {
        this.categoryService = categoryService;
        this.flightService = flightService;
    }

    @GetMapping("/{id}/flights")
    @Operation(summary = "Retrieve all flight by a category", description = "Get a list of flight containing all flight that has the given category")
    public ResponseEntity<List<Flight>> getFlightsByCategory(@PathVariable UUID id) throws CategoryNotFoundException {
        return ResponseEntity.ok(flightService.getByCategory(id));
    }

    @GetMapping
    @Operation(summary = "Retrieve all categories")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a category", description = "Retrieve a category by ID")
    public ResponseEntity<Category> getCategoryById(@PathVariable UUID id) throws CategoryNotFoundException {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new category", description = "Creates a new category")
    public ResponseEntity<Category> createCategory(@RequestBody String name) throws CategoryNameAlreadyExistsException {
        Category category = categoryService.create(name);
        return ResponseEntity
                .created(URI.create("v1/categories/" + category.getId()))
                .body(category);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing category", description = "Updates the name of a category by ID")
    public ResponseEntity<Category> updateCategory(@PathVariable UUID id, @RequestBody String name) throws CategoryNameAlreadyExistsException, CategoryNotFoundException {
        return ResponseEntity.ok(categoryService.update(id, name));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category", description = "Delete a category by ID")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) throws CategoryNotFoundException {
        categoryService.delete(id);
        return ResponseEntity.ok().build();
    }

}

