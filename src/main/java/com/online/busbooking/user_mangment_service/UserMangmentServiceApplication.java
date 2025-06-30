package com.online.busbooking.user_mangment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UserMangmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserMangmentServiceApplication.class, args);
	}

}
