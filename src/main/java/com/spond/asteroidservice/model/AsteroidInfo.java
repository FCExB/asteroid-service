package com.spond.asteroidservice.model;

import com.spond.asteroidservice.model.neows.OrbitalData;

/**
 * Stores characteristic information about a Near Earth Orbiting asteroid to return from our API.
 */
public record AsteroidInfo(
        String id,
        String name,
        boolean isPotentiallyHazardousAsteroid,
        double absoluteMagnitudeH,
        double estimatedDiameterMax,
        double estimatedDiameterMin,
        OrbitalData orbitalData) {

    // TODO: The data here can be expanded or modified based on what Asteroid information we wish our service to return
}
