package fr.nathzaf.projects.flightmanager.exceptions.notfound;

import java.util.UUID;

public class CategoryNotFoundException extends Exception {

    public CategoryNotFoundException(UUID id) {
        super("No Category of id " + id + " found.");
    }
}
