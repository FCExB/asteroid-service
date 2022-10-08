package com.spond.asteroidservice;

import com.spond.asteroidservice.model.AsteroidInfo;
import com.spond.asteroidservice.model.neows.*;
import com.spond.asteroidservice.service.CachedNeoWsAsteroidService;
import com.spond.asteroidservice.service.NeoWsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CachedNeoWsAsteroidServiceTest {

    private NeoWsClient neoWsClient;

    private CachedNeoWsAsteroidService asteroidService;

    @BeforeEach
    public void setUp() {
        neoWsClient = mock(NeoWsClient.class);
        asteroidService = new CachedNeoWsAsteroidService(neoWsClient);
    }

    @Test
    public void largestAsteroidSelected() throws ParseException {

        // Set the default to return no data
        when(neoWsClient.queryFeed(any(), any())).thenReturn(new FeedResponse(new HashMap<>()));

        // Set two asteroid to return on the first of the year

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2015-01-01");

        Asteroid asteroid1 = new Asteroid(
                "1234",
                "Test Asteroid 1",
                false,
                0,
                new EstimatedDiameter(new DiameterBounds(8, 10)),
                List.of(new CloseApproach(
                        startDate,
                        startDate,
                        new DistanceData(100),
                        "Earth")),
                null);

        Asteroid asteroid2 = new Asteroid(
                "12345",
                "Test Asteroid 2",
                false,
                0,
                new EstimatedDiameter(new DiameterBounds(98, 100)),
                List.of(new CloseApproach(
                        startDate,
                        startDate,
                        new DistanceData(500),
                        "Earth")),
                null);

        Map<Date, List<Asteroid>> neos = new HashMap<>();
        neos.put(startDate, List.of(asteroid1, asteroid2));

        when(neoWsClient.queryFeed(eq(startDate), any())).thenReturn(new FeedResponse(neos));

        // Mock asteroid info endpoint to return expected data
        when(neoWsClient.getAsteroid(eq("1234"))).thenReturn(asteroid1);
        when(neoWsClient.getAsteroid(eq("12345"))).thenReturn(asteroid2);

        // Test that we get the expected result

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        Optional<AsteroidInfo> resultOptional =
                asteroidService.getLargestPassingAsteroid(yearFormat.parse("2015"));

        assertTrue(resultOptional.isPresent());

        AsteroidInfo asteroidInfo = resultOptional.get();

        AsteroidInfo expectedAsteroidInfo = new AsteroidInfo(
                "12345",
                "Test Asteroid 2",
                false,
                0,
                100,
                98,
                null);

        assertEquals(expectedAsteroidInfo, asteroidInfo);
    }

    // TODO: Further unit tests
}
