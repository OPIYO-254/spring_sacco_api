package com.sojrel.saccoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SaccoapiApplication {
	public static void main(String[] args) {
		SpringApplication.run(SaccoapiApplication.class, args);

	}

//	@Bean
//	public Docket productApi() {
//		return new Docket(DocumentationType.SWAGGER_2).select()
//				.apis(RequestHandlerSelectors.basePackage("com.sojrel.saccoapi")).build();
//	}

}
