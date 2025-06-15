package com.mon_rdv.plateforme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EntityScan(basePackages = "com.mon_rdv.model")
@ComponentScan(basePackages = "com.mon_rdv")
@EnableJpaRepositories(basePackages = "com.mon_rdv.repository")
public class PlateformeApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.filename(".env")
				.load();

		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
	    SpringApplication.run(PlateformeApplication.class, args);

	}

}
