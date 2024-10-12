package nl.multicode.rittenadministratie;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class RitData {
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