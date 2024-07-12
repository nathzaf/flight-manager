package fr.nathzaf.projects.flightmanager.exceptions.notfound;

import java.util.UUID;

public class FeedbackNotFoundException extends Exception {

    public FeedbackNotFoundException(UUID id) {
        super("No feedback of id " + id + " found.");
    }
}
