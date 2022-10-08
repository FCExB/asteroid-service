package com.spond.asteroidservice.model.neows;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
public record CloseApproach (
    @JsonProperty("close_approach_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date closeApproachDay,
    @JsonProperty("close_approach_date_full")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MMM-dd HH:mm")
    Date closeApproachDateFull,
    @JsonProperty("miss_distance") DistanceData missDistance,
    @JsonProperty("orbiting_body") String orbitingBody) {
}