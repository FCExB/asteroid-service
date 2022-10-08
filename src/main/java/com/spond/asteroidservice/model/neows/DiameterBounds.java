package com.spond.asteroidservice.model.neows;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DiameterBounds (
    @JsonProperty("estimated_diameter_min") double min,
    @JsonProperty("estimated_diameter_max") double max) {
}

