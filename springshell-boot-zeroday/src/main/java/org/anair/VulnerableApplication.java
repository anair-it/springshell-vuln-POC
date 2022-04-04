package org.anair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class VulnerableApplication extends SpringBootServletInitializer{
	public static void main(String[] args) {
		SpringApplication.run(VulnerableApplication.class, args);
	}

}
