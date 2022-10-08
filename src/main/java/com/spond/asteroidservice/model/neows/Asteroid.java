package com.spond.asteroidservice.model.neows;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Asteroid(
    String id,
    String name,
    @JsonProperty("is_potentially_hazardous_asteroid") boolean isPotentiallyHazardousAsteroid,
    @JsonProperty("absolute_magnitude_h") double absoluteMagnitudeH,
    @JsonProperty("estimated_diameter") EstimatedDiameter estimatedDiameter,
    @JsonProperty("close_approach_data") List<CloseApproach> closeApproachData,
    @JsonProperty("orbital_data") OrbitalData orbitalData) {
}
