package com.spond.asteroidservice.model.neows;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DistanceData(double kilometers) { }

