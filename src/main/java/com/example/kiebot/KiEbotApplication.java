package com.example.kiebot;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KiEbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(KiEbotApplication.class, args);
	}

	
}
