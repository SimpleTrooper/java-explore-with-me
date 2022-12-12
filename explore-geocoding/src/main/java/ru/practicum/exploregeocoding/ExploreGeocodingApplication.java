package ru.practicum.exploregeocoding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "ru.practicum")
@EnableFeignClients(basePackages = "ru.practicum")
public class ExploreGeocodingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExploreGeocodingApplication.class, args);
    }

}
