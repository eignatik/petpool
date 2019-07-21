package com.petpool.interfaces.health.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthController {

  /**
   * Returns the service up&running statuses.
   *
   * @return TODO: implement
   */
  @GetMapping("/health")
  public ResponseEntity checkRunning() {
    return ResponseEntity.ok().build();
  }
  @PostMapping("/health")
  public ResponseEntity checkRunning4() {
    return ResponseEntity.ok().build();
  }

  @GetMapping("/info")
  public ResponseEntity info() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return ResponseEntity.ok(auth.getName());
  }


  @GetMapping("/")
  public ResponseEntity checkRunning2() {
    return ResponseEntity.ok("Main");
  }


}
