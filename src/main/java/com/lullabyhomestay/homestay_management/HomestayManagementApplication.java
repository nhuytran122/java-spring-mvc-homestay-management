package com.lullabyhomestay.homestay_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @SpringBootApplication(exclude =
// org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)

// @SpringBootApplication
public class HomestayManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomestayManagementApplication.class, args);
	}

}
