package nl.multicode.rittenadministratie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Ondernemerskaart {
    @JsonProperty("Rit")
    private List<Rit> rit;
}