package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @EnableEurekaServer will configure a registry that will allow other applications to communicate.
 * @author James
 *
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerrApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerrApplication.class, args);
	}

}
