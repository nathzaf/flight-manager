package fr.nathzaf.projects.flightmanager.dto.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateFlightRequest {

    private UUID categoryId;

    private LocalDateTime date;

    private String departureIcao;

    private String arrivalIcao;

    private String planeRegistration;

    private String pilotUsername;

}
