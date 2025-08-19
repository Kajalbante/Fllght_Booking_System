package com.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserAuthAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthAppApplication.class, args);
	}

}
