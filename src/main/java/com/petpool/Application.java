package com.petpool;

import com.petpool.config.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ApplicationConfig.class)
@EnableConfigurationProperties
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
