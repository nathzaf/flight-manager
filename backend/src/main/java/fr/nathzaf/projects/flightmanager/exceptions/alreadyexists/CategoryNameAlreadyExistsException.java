package fr.nathzaf.projects.flightmanager.exceptions.alreadyexists;

public class CategoryNameAlreadyExistsException extends Exception {

    public CategoryNameAlreadyExistsException(String name) {
        super("A Category named " + name + " already exists.");
    }
}
