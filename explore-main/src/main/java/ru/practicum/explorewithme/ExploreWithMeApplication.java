package ru.practicum.explorewithme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "ru.practicum")
@EnableFeignClients(basePackages = "ru.practicum")
public class ExploreWithMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExploreWithMeApplication.class, args);
	}

}
