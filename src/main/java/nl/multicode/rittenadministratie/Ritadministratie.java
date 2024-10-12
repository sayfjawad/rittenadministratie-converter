package nl.multicode.rittenadministratie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Ritadministratie {
    @JsonProperty("Vervoerder")
    private Vervoerder vervoerder;
}