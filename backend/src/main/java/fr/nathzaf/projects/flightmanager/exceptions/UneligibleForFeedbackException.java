package fr.nathzaf.projects.flightmanager.exceptions;

public class UneligibleForFeedbackException extends Exception {

    public UneligibleForFeedbackException() {
        super("Impossible to give an opinion on this flight as it has not yet taken place");
    }
}
