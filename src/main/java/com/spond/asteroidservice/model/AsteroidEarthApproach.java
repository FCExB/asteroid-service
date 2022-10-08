package com.spond.asteroidservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Stores information about an asteroid and its close approach with Earth.
 *
 * @param asteroidName the name of the asteroid
 * @param asteroidEstimatedDiameterMax the maximum estimated diameter of the asteroid
 * @param closeApproachDate the date and time of the asteroid's close approach
 * @param closeApproachDistance the distance from the earth the asteroid gets at its closest approach, in kilometers
 */
public record AsteroidEarthApproach(
    String asteroidId,
    String asteroidName,
    double asteroidEstimatedDiameterMax,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    Date closeApproachDate,
    double closeApproachDistance) {
}
