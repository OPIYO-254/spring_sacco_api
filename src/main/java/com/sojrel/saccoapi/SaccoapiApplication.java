package com.sojrel.saccoapi;

import com.sojrel.saccoapi.model.Role;
import com.sojrel.saccoapi.mpesa.dto.AcknowledgeResponse;
import com.sojrel.saccoapi.repository.RoleRepository;
import com.sojrel.saccoapi.service.StorageProperties;
import com.sojrel.saccoapi.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SaccoapiApplication {
	public static void main(String[] args) {
		SpringApplication.run(SaccoapiApplication.class, args);

	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
//			storageService.deleteAll();
			storageService.init();
		};
	}
	@Bean
	CommandLineRunner run(RoleRepository roleRepository){
		return args ->{
			if(roleRepository.findByAuthority("ADMIN").isPresent()) return;//To check whether the table is populated already
			roleRepository.save(new Role("USER"));
			roleRepository.save(new Role("MANAGER"));
			roleRepository.save(new Role("ADMIN"));

		};
	}

	@Bean
	public AcknowledgeResponse getAcknowledgeResponse(){
		AcknowledgeResponse acknowledgeResponse = new AcknowledgeResponse();
		acknowledgeResponse.setMessage("success");
		return  acknowledgeResponse;
	}

}
