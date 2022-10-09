package com.spond.asteroidservice.model.neows;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FeedResponse(@JsonProperty("near_earth_objects") Map<Date, List<Asteroid>> nearEarthObjects){ }
