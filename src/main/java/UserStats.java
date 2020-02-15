import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "playerstats")
@JsonIgnoreProperties({"achievements","steamID","gameName"})
public class UserStats {

    @JsonProperty("stats")
    private Stats[] stats;

    public Stats[] getStats() {
        return stats;
    }
}
