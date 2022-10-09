package com.spond.asteroidservice.service;

import com.spond.asteroidservice.model.neows.Asteroid;
import com.spond.asteroidservice.model.neows.FeedResponse;
import com.spond.asteroidservice.config.NeoWsClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class NeoWsClient {

    private final String FEED_PATH = "/feed";
    private final String ASTEROID_PATH ="/neo";

    public static final Logger logger = LoggerFactory.getLogger(CachedNeoWsAsteroidService.class);

    private final WebClient client;
    private final String apiKey;

    public NeoWsClient(NeoWsClientProperties properties) {
        this.client = WebClient.create(properties.getBaseUrl());
        this.apiKey = properties.getApiKey();
    }

    /**
     * Query to NASA NeoWs feed endpoint to fetch asteroids with a close approach of Earth between the given dates.
     *
     * Note that the time portion of the given dates are ignored and the date range is inclusive.
     */
    public FeedResponse queryFeed(Date startDate, Date endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        logger.debug("Querying NeoWs feed between {} and {}", startDate, endDate);

        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(FEED_PATH)
                        .queryParam("start_date", dateFormat.format(startDate))
                        .queryParam("end_date", dateFormat.format(endDate))
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(FeedResponse.class)
                .block();
    }

    public Asteroid getAsteroid(String id) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ASTEROID_PATH + "/" + id)
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(Asteroid.class)
                .block();
    }
}
