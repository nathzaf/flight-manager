package fr.nathzaf.projects.flightmanager.dto.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateFeedbackRequest {

    private String passengerUsername;

    private String flightNumber;

    private Integer rating;

    private String comment;
}
