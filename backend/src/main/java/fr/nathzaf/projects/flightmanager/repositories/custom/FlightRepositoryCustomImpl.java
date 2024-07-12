package fr.nathzaf.projects.flightmanager.repositories.custom;

import fr.nathzaf.projects.flightmanager.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightRepositoryCustomImpl implements FlightRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Flight> findByCriteria(Category category, LocalDateTime startDate, LocalDateTime endDate,
                                       Airfield departureAirfield, Airfield arrivalAirfield,
                                       Plane plane, Passenger pilot) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Flight> query = cb.createQuery(Flight.class);
        Root<Flight> flight = query.from(Flight.class);

        List<Predicate> predicates = new ArrayList<>();

        if (category != null) {
            predicates.add(cb.equal(flight.get("category"), category));
        }
        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(flight.get("date"), startDate));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(flight.get("date"), endDate));
        }
        if (departureAirfield != null) {
            predicates.add(cb.equal(flight.get("departure"), departureAirfield));
        }
        if (arrivalAirfield != null) {
            predicates.add(cb.equal(flight.get("arrival"), arrivalAirfield));
        }
        if (plane != null) {
            predicates.add(cb.equal(flight.get("plane"), plane));
        }
        if (pilot != null) {
            predicates.add(cb.equal(flight.get("pilot"), pilot));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(flight.get("creationDate")));

        return entityManager.createQuery(query).getResultList();
    }
}

