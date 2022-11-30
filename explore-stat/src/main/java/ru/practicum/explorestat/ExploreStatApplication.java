package ru.practicum.explorestat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.practicum")
public class ExploreStatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExploreStatApplication.class, args);
	}

}
