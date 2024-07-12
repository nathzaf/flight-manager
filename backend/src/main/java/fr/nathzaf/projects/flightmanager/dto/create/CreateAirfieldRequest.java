package fr.nathzaf.projects.flightmanager.dto.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAirfieldRequest {

    private String icaoCode;

    private String name;

    private String iataCode;

}