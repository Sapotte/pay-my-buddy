package com.openclassromm.paymybuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class PayMyBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyApplication.class, args);
	}

}
