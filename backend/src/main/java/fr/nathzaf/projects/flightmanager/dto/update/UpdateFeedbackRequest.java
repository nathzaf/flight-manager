package fr.nathzaf.projects.flightmanager.dto.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFeedbackRequest {

    private UUID id;

    private Integer rating;

    private String comment;
}
