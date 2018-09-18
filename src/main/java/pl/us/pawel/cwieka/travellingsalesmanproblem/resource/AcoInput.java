package pl.us.pawel.cwieka.travellingsalesmanproblem.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AcoInput {

    @JsonProperty("alpha")
    private Double alpha;
    @JsonProperty("beta")
    private Double beta;
    @JsonProperty("evaporation")
    private Double evaporation;
    @JsonProperty("q")
    private Double q;
    @JsonProperty("antFactor")
    private Double antFactor;
    @JsonProperty("randomFactor")
    private Double randomFactor;
    @JsonProperty("attempts")
    private Integer attempts;
    @JsonProperty("iterations")
    private Integer iterations;

    @JsonProperty("cities")
    private List<City> cities;

    public Double getAlpha() {
        return alpha;
    }

    public Double getBeta() {
        return beta;
    }

    public Double getEvaporation() {
        return evaporation;
    }

    public Double getQ() {
        return q;
    }

    public Double getAntFactor() {
        return antFactor;
    }

    public Double getRandomFactor() {
        return randomFactor;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public Integer getIterations() {
        return iterations;
    }

    public List<City> getCities() {
        return cities;
    }
}
