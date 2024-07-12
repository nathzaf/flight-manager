package fr.nathzaf.projects.flightmanager.exceptions;

public class NotAPilotException extends Exception {

    public NotAPilotException(String pilotUsername) {
        super(pilotUsername + " is not a pilot, so (s)he can't be assigned to a flight");
    }
}
