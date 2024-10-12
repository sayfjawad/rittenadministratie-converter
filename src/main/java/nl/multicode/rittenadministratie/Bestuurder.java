package nl.multicode.rittenadministratie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Bestuurder {
    @JsonProperty("ChIdNr")
    private String chIdNr;
}