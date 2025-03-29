package com.lullabyhomestay.homestay_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
// @SpringBootApplication(exclude =
// org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)

// @SpringBootApplication
public class HomestayManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomestayManagementApplication.class, args);
	}

}
