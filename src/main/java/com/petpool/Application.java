package com.petpool;

import com.petpool.config.ApplicationConfig;
import com.petpool.config.SpringSecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@Import({ApplicationConfig.class, SpringSecurityConfig.class})
@EnableConfigurationProperties
public class Application {

  public static void main(String[] args) {
    log.info("Starting Pet pool application ....");
    SpringApplication.run(Application.class, args);
  }

}
