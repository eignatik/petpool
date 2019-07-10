package com.petpool;

import com.petpool.config.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootApplication
@Import(ApplicationConfig.class)
@EnableConfigurationProperties
public class Application {

    public static void main(String[] args) {
        log.info("Starting Pet pool application ....");
        SpringApplication.run(Application.class, args);
    }

}
