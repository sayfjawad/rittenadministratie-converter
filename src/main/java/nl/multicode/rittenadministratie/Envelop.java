package nl.multicode.rittenadministratie;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Envelop {
    @JsonProperty("Ritadministratie")
    private Ritadministratie ritadministratie;
}
