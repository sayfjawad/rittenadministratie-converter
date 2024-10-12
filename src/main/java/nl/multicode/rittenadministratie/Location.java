package nl.multicode.rittenadministratie;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Location {
    @JsonProperty("Lat")
    private String lat;
    @JsonProperty("Lon")
    private String lon;
}