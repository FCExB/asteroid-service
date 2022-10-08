package com.spond.asteroidservice.model.neows;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrbitalData(
    double eccentricity,
    double inclination) {
    // TODO: Can be extended with further information from NeoWs, if needed
}
