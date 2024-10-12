package nl.multicode.rittenadministratie;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Rit {

    @JsonProperty("Data")
    private RitData data;
}
