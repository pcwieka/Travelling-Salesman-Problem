package pl.us.pawel.cwieka.travellingsalesmanproblem.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class City {

    @JsonProperty("city")
    private Integer city;

    @JsonProperty("x")
    private Double x;

    @JsonProperty("y")
    private Double y;

    public Integer getCity() {
        return city;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }
}
