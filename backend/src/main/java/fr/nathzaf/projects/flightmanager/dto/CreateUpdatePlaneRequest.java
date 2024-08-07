package fr.nathzaf.projects.flightmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdatePlaneRequest {

    private String registration;

    private String type;

    private String pictureLink;

}
