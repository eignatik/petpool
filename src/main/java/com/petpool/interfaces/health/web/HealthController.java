package com.petpool.interfaces.health.web;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
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
  public Map<String, Object> checkRunning() {
    return ImmutableMap.of(
        "applicationStatus", "UP"
    );
  }

}
