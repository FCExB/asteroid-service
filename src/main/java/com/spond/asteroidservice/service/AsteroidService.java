package com.spond.asteroidservice.service;

import com.spond.asteroidservice.model.AsteroidEarthApproach;
import com.spond.asteroidservice.model.AsteroidInfo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AsteroidService {
    /**
     * Returns the num asteroids that pass closest to Earth between the given startDate and endDate, inclusive.
     */
    List<AsteroidEarthApproach> getClosestPassingAsteroids(Date startDate, Date endDate, int num);

    /**
     * Gets information about the largest asteroid to pass close to Earth in the given year.
     */
    Optional<AsteroidInfo> getLargestPassingAsteroid(Date year);
}
