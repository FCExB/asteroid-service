package com.spond.asteroidservice.controller;

import com.spond.asteroidservice.model.AsteroidInfo;
import com.spond.asteroidservice.model.AsteroidEarthApproach;
import com.spond.asteroidservice.service.AsteroidService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class AsteroidController {

    private final AsteroidService asteroidService;

    public AsteroidController(AsteroidService asteroidService) {
        this.asteroidService = asteroidService;
    }

    @GetMapping("closest-passing-asteroids")
    public List<AsteroidEarthApproach> getClosestPassingAsteroids(
            @RequestParam(value = "start_date") @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
            @RequestParam(value= "end_date") @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate) {
        return asteroidService.getClosestPassingAsteroids(startDate, endDate, 10);
    }

    @GetMapping("largest-passing-asteroid")
    public Optional<AsteroidInfo> getLargestAsteroidPassingInYear(
            @RequestParam(value = "year") @DateTimeFormat(pattern="yyyy") Date year) {
        return asteroidService.getLargestPassingAsteroid(year);
    }
}
