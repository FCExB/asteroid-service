package com.spond.asteroidservice;

import config.NeoWsClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableConfigurationProperties(NeoWsClientProperties.class)
public class AsteroidServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AsteroidServiceApplication.class, args);
    }
}