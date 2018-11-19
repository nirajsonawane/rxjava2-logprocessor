package com.logprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RxjavaLogprocessorApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext run = SpringApplication.run(RxjavaLogprocessorApplication.class, args);
		String path ="D:\\Niraj\\Programming\\logFile.txt";
		LogFileProcessor logFileProcessor = run.getBean(LogFileProcessor.class);
		logFileProcessor.processLogFile(path);

	}
}
