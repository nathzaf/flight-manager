package fr.nathzaf.projects.flightmanager.repositories.custom;

import fr.nathzaf.projects.flightmanager.models.*;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepositoryCustom {
    List<Flight> findByCriteria(Category category, LocalDateTime startDate, LocalDateTime endDate,
                                Airfield departureAirfield, Airfield arrivalAirfield,
                                Plane plane, Passenger pilot);
}
