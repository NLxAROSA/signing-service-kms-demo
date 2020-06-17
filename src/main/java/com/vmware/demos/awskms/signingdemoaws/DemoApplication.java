package com.vmware.demos.awskms.signingdemoaws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import software.amazon.awssdk.services.kms.model.KmsInvalidSignatureException;

@SpringBootApplication
public class DemoApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner signAndValidate(SigningService signingService) {

		return args -> {
			String message = "Hello signing world!";
			byte[] signature = signingService.signMessage(message);
			LOGGER.info("Signed message {}, signature is {}", message, signature);
			Boolean isValidSignature = signingService.isValidSignature(message, signature);
			LOGGER.info("Valid signature? (expected: true) -> {}", isValidSignature);
			try {
				Boolean isNotValidSignature = signingService.isValidSignature(message,
						"thisisnotasignature".getBytes("UTF-8"));
				LOGGER.info("This should never happen (expecting a KmsInvalidSignatureException");
				LOGGER.info("Valid signature? (expected: false) -> {} ", isNotValidSignature);
			} catch (KmsInvalidSignatureException e) {
				LOGGER.info("Valid signature? (expected: false) -> false ");
			}

		};

	}
}
