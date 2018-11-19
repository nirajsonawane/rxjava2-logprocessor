package com.logprocessor.rxjavalogprocessor;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RxjavaLogprocessorApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext run = SpringApplication.run(RxjavaLogprocessorApplication.class, args);
		String path = "C:\\Users\\SUJAN\\Downloads\\rxjava-logprocessor\\rxjava-logprocessor\\src\\main\\resources\\logData.txt";
		LogFileProcessor logFileProcessor = run.getBean(LogFileProcessor.class);
		logFileProcessor.processLogFile(path);

	}
}
