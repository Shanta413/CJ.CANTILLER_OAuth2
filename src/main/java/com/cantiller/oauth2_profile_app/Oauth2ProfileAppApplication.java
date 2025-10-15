package com.cantiller.oauth2_profile_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.cantiller.oauth2_profile_app.entity")
@EnableJpaRepositories(basePackages = "com.cantiller.oauth2_profile_app")
@ComponentScan(basePackages = "com.cantiller.oauth2_profile_app")
public class Oauth2ProfileAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(Oauth2ProfileAppApplication.class, args);
	}
}
