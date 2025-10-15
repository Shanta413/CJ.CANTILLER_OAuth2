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

		// Display message after successful startup
		System.out.println("\n✅ Application started successfully!");
		System.out.println("----------------------------------------------------");
		System.out.println("Access the application at: 👉 http://localhost:8080");
		System.out.println("Access the H2 Console:     👉 http://localhost:8080/h2-console  ");
		System.out.println("Access the H2 Console:     JDBC URL: jdbc:h2:mem:oauth2db ||  User Name : sa || Password: leave it blank ");
		System.out.println("----------------------------------------------------\n");
	}
}
