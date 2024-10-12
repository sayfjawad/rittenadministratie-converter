package nl.multicode.rittenadministratie.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Data {
    @JsonProperty("RtVgNr")
    private String rtVgNr;
    @JsonProperty("DatTdReg")
    private String datTdReg;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("KmStdBeg")
    private String kmStdBeg;
    @JsonProperty("KmStdEnd")
    private String kmStdEnd;
    @JsonProperty("Prijs")
    private String prijs;
    @JsonProperty("Bestuurder")
    private Bestuurder bestuurder;
    @JsonProperty("LocBeg")
    private Location locBeg;
    @JsonProperty("LocEnd")
    private Location locEnd;
}