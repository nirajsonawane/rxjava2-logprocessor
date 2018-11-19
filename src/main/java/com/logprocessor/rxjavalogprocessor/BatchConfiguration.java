package com.logprocessor.rxjavalogprocessor;

import javax.annotation.PostConstruct;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hsqldb.util.DatabaseManagerSwing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfiguration {

	private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);

	@PostConstruct
	public void getDbManager() {
		System.setProperty("java.awt.headless", "false");
		DatabaseManagerSwing.main(
				new String[] { "--url", "jdbc:hsqldb:file:db/log-messagees-db", "--user", "sa", "--password", "" });
	}

	@Bean
	public Validator setupValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		return factory.getValidator();

	}


}
