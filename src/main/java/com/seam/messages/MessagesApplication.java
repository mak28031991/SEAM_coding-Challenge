package com.seam.messages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages={
		"com.seam.messages", "com.seam.services"})
@EnableSwagger2
public class MessagesApplication {

	
	
	public static void main(String[] args) {
		SpringApplication.run(MessagesApplication.class, args);
	}
}
