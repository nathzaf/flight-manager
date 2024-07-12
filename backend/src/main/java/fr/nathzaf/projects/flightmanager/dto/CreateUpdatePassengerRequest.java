package fr.nathzaf.projects.flightmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdatePassengerRequest {

    private String name;

    private String username;

    private Boolean isPilot;

}
