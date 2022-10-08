package com.spond.asteroidservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthcheckController {
    @RequestMapping("/_healthcheck")
    public String healthcheck() {
        return "Ok";
    }
}
