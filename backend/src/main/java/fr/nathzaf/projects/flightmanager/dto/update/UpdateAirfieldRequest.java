package fr.nathzaf.projects.flightmanager.dto.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAirfieldRequest {

    private String icaoCode;

    private String name;

}
