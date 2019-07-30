package com.petpool.interfaces.health.web;

import com.google.common.collect.ImmutableMap;
import com.petpool.application.util.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthController {

  /**
   * Returns the service up&running statuses.
   *
   * @return Map with running parameters.
   */
  @GetMapping("/health")
  public ResponseEntity<Response> checkRunning() {
    return Response.ok(ImmutableMap.of("applicationStatus", "UP"));
  }
}

