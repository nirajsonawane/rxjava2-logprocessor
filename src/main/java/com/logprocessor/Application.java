package com.logprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
		String path ="D:\\Niraj\\Programming\\logFile.txt";
		LogFileProcessor logFileProcessor = run.getBean(LogFileProcessor.class);
		logFileProcessor.processLogFile("D:\\Niraj\\Programming\\rxjava2-logprocessor\\src\\main\\resources\\logData.txt");
		//logFileProcessor.processLogFile(path);
	}
}
