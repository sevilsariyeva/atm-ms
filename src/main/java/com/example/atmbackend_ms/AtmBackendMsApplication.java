package com.example.atmbackend_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class AtmBackendMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtmBackendMsApplication.class, args);
	}

}
