package com.spond.asteroidservice.service;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.spond.asteroidservice.model.AsteroidInfo;
import com.spond.asteroidservice.model.AsteroidEarthApproach;
import com.spond.asteroidservice.model.neows.Asteroid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Service
public class CachedNeoWsAsteroidService implements AsteroidService {

    public static final int ASTEROID_INFO_CACHE_SIZE = 1_000;
    public static final int ASTEROID_INFO_CACHE_EXPIRY_HOURS = 12;

    public static final int ASTEROID_APPROACH_CACHE_SIZE = 50_000;
    public static final int ASTEROID_APPROACH_CACHE_EXPIRY_HOURS = 12;

    public static final Logger logger = LoggerFactory.getLogger(CachedNeoWsAsteroidService.class);

    private final NeoWsClient neoWsClient;
    private final LoadingCache<String, AsteroidInfo> asteroidInfoCache;
    private final LoadingCache<Date, List<AsteroidEarthApproach>> asteroidEarthApproachesCache;

    public CachedNeoWsAsteroidService(NeoWsClient neoWsClient) {
        this.neoWsClient = neoWsClient;

        asteroidInfoCache = Caffeine.newBuilder()
                .maximumSize(ASTEROID_INFO_CACHE_SIZE)
                .expireAfterWrite(ASTEROID_INFO_CACHE_EXPIRY_HOURS, TimeUnit.HOURS)
                .build(this::fetchAsteroidInfo);

        asteroidEarthApproachesCache = Caffeine.newBuilder()
                .maximumSize(ASTEROID_APPROACH_CACHE_SIZE)
                .expireAfterWrite(ASTEROID_APPROACH_CACHE_EXPIRY_HOURS, TimeUnit.HOURS)
                .build(new AsteroidEarthApproachCacheLoader(neoWsClient));
    }

    private AsteroidInfo fetchAsteroidInfo(String string) {
        Asteroid asteroid = neoWsClient.getAsteroid(string);
        return buildAsteroidInfo(asteroid);
    }

    private AsteroidInfo buildAsteroidInfo(Asteroid asteroid) {
        return new AsteroidInfo(
                asteroid.id(),
                asteroid.name(),
                asteroid.isPotentiallyHazardousAsteroid(),
                asteroid.absoluteMagnitudeH(),
                asteroid.estimatedDiameter().kilometers().max(),
                asteroid.estimatedDiameter().kilometers().min(),
                asteroid.orbitalData());
    }

    @Override
    public List<AsteroidEarthApproach> getClosestPassingAsteroids(Date startDate, Date endDate, int num) {

        Set<String> asteroidIdsSeen = new HashSet<>();

        return getAllAsteroidApproaches(startDate, endDate)
                .sorted(Comparator.comparingDouble(AsteroidEarthApproach::closeApproachDistance))
                // Filter out any asteroids seen more than once. We only want to show their closest approach within
                // the given duration
                .filter(asteroid -> asteroidIdsSeen.add(asteroid.asteroidId()))
                .limit(num)
                .toList();
    }

    @Override
    public Optional<AsteroidInfo> getLargestPassingAsteroid(Date year) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(year);
        cal.add(Calendar.YEAR, 1);
        // Remove 1 day so that we don't include the first day of the next year
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date endDate = cal.getTime();

        return getAllAsteroidApproaches(year, endDate)
                // Select the asteroid with the largest estimated diameter max
                .max(Comparator.comparingDouble(AsteroidEarthApproach::asteroidEstimatedDiameterMax))
                // Look up all data on that asteroid
                .map(asteroidApproach -> asteroidInfoCache.get(asteroidApproach.asteroidId()));
    }

    private Stream<AsteroidEarthApproach> getAllAsteroidApproaches(Date startDate, Date endDate) {

        logger.debug("Getting all asteroids with an approach between {} and {}", startDate, endDate);

        List<Date> requestedDates = new ArrayList<>();
        for(Date date = startDate; beforeOrEqual(date, endDate); date = addDay(date)) {
            requestedDates.add(date);
        }

        return asteroidEarthApproachesCache.getAll(requestedDates)
                .values()
                .stream()
                .flatMap(Collection::stream);
    }

    private boolean beforeOrEqual(Date lhs, Date rhs) {
        int value = lhs.compareTo(rhs);
        // If value is <0 or =0 then rhs is before or equal to lhs
        return value < 1;
    }

    private Date addDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime (date);
        cal.add (Calendar.DATE, 1);
        return cal.getTime();
    }
}
