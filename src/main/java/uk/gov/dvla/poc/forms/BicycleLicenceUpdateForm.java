package uk.gov.dvla.poc.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BicycleLicenceUpdateForm {

    private String name;

    private String email;

    private String telephone;

    private int penaltyPoints;

}
