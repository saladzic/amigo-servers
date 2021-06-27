package com.amigoservers.backend;

import com.amigoservers.backend.cronjob.Payment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AmigoServersApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmigoServersApplication.class, args);

		// Cronjobs
		Payment payment = new Payment();
		new Thread(payment).start();
	}

}
