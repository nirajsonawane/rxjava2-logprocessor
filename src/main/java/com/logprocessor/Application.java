package com.logprocessor;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(LogFileProcessor.class);

	public static void main(String[] args) {

		validateInput(args);
		ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);		
		LogFileProcessor logFileProcessor = run.getBean(LogFileProcessor.class);
		Instant start = Instant.now();		
		logFileProcessor.processFile(args[0]);
		
		Instant end = Instant.now();
		
		log.info("Time Taken to Process File {} Minutes",java.time.Duration.between(end, start).toMinutes());
	}
	private static void validateInput(String[] args) {

		if (args.length != 1) {
			throw new InvalidParameterException("One Parameter File Path is Needed");
		}

		Optional.ofNullable(args[0])
				.orElseThrow(() -> new InvalidParameterException("File Path is Needed"));
		
	}
}
