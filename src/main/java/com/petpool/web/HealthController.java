package com.petpool.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j//use log.error( ...
@RestController
public class HealthController {

    /**
     * Returns the service up&running statuses.
     * @return TODO: implement
     */
    @GetMapping("/health")
    public ResponseEntity checkRunning() {
        return ResponseEntity.ok().build();
    }

}
