package com.spond.asteroidservice;

import com.spond.asteroidservice.config.NeoWsClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(NeoWsClientProperties.class)
public class AsteroidServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AsteroidServiceApplication.class, args);
    }
}