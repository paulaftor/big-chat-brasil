package com.sd.email;

import com.sd.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailApplication {

//	@Autowired
//	private EmailService emailService;

	public static void main(String[] args) {
		SpringApplication.run(EmailApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		emailService.enviarEmail("ra123565@uem.br", "Teste", "Teste aaaaaaaaaaaaa");
//	}
}
