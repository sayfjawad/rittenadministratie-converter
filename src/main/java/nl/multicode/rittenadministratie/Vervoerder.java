package nl.multicode.rittenadministratie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Vervoerder {
    @JsonProperty("Ondernemerskaart")
    private Ondernemerskaart ondernemerskaart;
}