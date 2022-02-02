package com.xavi.exams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EntityScan(
		basePackageClasses = { ExamsApplication.class, Jsr310JpaConverters.class }
)
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class ExamsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamsApplication.class, args);
	}

}
