package fr.nathzaf.projects.flightmanager.dto.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookingRequest {

    private UUID id;

    private String passengerUsername;

    private String flightNumber;
}
