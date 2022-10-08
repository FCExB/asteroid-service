package com.spond.asteroidservice.service;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.spond.asteroidservice.model.AsteroidEarthApproach;
import com.spond.asteroidservice.model.neows.Asteroid;
import com.spond.asteroidservice.model.neows.CloseApproach;
import com.spond.asteroidservice.model.neows.FeedResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AsteroidEarthApproachCacheLoader implements CacheLoader<Date, List<AsteroidEarthApproach>> {

    private final NeoWsClient neoWsClient;

    public AsteroidEarthApproachCacheLoader(NeoWsClient neoWsClient) {
        this.neoWsClient = neoWsClient;
    }

    @Override
    public List<AsteroidEarthApproach> load(Date date) {
        FeedResponse feedResponse = neoWsClient.queryFeed(date, date);
        return buildAsteroidEarthApproaches(feedResponse.nearEarthObjects().get(date));
    }

    @Override
    public Map<Date, List<AsteroidEarthApproach>> loadAll(Iterable<? extends Date> keys) {
        Calendar cal = Calendar.getInstance();

        // Walk through keys and load approach data in 7 day chunks
        // When sequential keys are requested, this is efficient as it reduces calls to the client as much as possible.

        Map<Date, List<AsteroidEarthApproach>> result = new HashMap<>();
        for(Date date : keys) {
            // If we have already loaded data for this day then continue
            if(result.containsKey(date)) continue;

            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, 7);
            Date endDate = cal.getTime();

            Map<Date, List<Asteroid>> approachesByDay = neoWsClient.queryFeed(date, endDate).nearEarthObjects();

            // Update the cache results with all new information
            Map<Date, List<AsteroidEarthApproach>> newResults = new HashMap<>();
            for(var entry : approachesByDay.entrySet()) {
                newResults.put(entry.getKey(), buildAsteroidEarthApproaches(entry.getValue()));
            }

            result.putAll(newResults);
        }

        return result;
    }

    private List<AsteroidEarthApproach> buildAsteroidEarthApproaches(List<Asteroid> asteroids) {
       return asteroids
               .stream()
               // Attempt to map each asteroid into an AsteroidEarthApproach
               // If an empty optional is returned the flatmap with mean nothing is added to the resulting list
               .flatMap(asteroid -> buildAsteroidEarthApproach(asteroid).stream())
               .toList();
    }

    /**
     * Attempts to coverts an asteroid returned by the NeoWs feed endpoint into a AsteroidEarthApproach.
     * @param asteroid must only contain one set of close approach data
     */
    private Optional<AsteroidEarthApproach> buildAsteroidEarthApproach(Asteroid asteroid) {

        // We can safely fetch the first element as CloseApproach data from the feed endpoint only has one entry
        CloseApproach closeApproach = asteroid.closeApproachData().get(0);

        if(!closeApproach.orbitingBody().equals("Earth")) {
            // Return an empty result if this is not a close approach with Earth
            return Optional.empty();
        }

        AsteroidEarthApproach asteroidEarthApproach = new AsteroidEarthApproach(
                asteroid.id(),
                asteroid.name(),
                asteroid.estimatedDiameter().kilometers().max(),
                closeApproach.closeApproachDateFull(),
                closeApproach.missDistance().kilometers());

        return Optional.of(asteroidEarthApproach);
    }
}
