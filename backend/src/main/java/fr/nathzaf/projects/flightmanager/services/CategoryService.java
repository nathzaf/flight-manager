package fr.nathzaf.projects.flightmanager.services;

import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.CategoryNameAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.CategoryNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<Category> getAll();

    Category getById(UUID id) throws CategoryNotFoundException;

    Category create(String name) throws CategoryNameAlreadyExistsException;

    Category update(UUID id, String name) throws CategoryNotFoundException, CategoryNameAlreadyExistsException;

    void delete(UUID id) throws CategoryNotFoundException;
}
