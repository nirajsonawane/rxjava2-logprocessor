package com.logprocessor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logprocessor.model.LogMessage;

public class DataGenerator {
	public static void main(String[] args) {
		String path = "";
		ObjectMapper mapper = new ObjectMapper();
		try (BufferedWriter bufferedWriter = new BufferedWriter(
				new FileWriter("D:\\Niraj\\Programming\\logFile.txt"))) {

			LogMessage message;
			

			Instant instant = Instant.now();
			long timestamp = instant.toEpochMilli();
			for (int i = 0; i < 100000; i++) {
				
				long epochSecond = instant.getEpochSecond();
				epochSecond =epochSecond+1;
				message = LogMessage.builder()
						.id(String.valueOf(i))
						.host("host" + i)
						.state("STARTED")
						.type("Type" + i)
						.timestamp(String.valueOf(epochSecond))
						.build();											
				
				bufferedWriter.write(mapper.writeValueAsString(message));
				bufferedWriter.newLine();
				message = LogMessage.builder()
						.id(String.valueOf(i))
						.host("host" + i)
						.state("FINISHED")
						.type("Type" + i)
						.timestamp(String.valueOf(epochSecond+5))
						.build();											
				
				bufferedWriter.write(mapper.writeValueAsString(message));
				bufferedWriter.newLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}