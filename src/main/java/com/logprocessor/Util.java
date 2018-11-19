package com.logprocessor;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logprocessor.model.LogMessage;
import com.logprocessor.model.LongRunningMessageReport;

import io.reactivex.exceptions.Exceptions;

@Component
public class Util {
	
	private static final Logger log = LoggerFactory.getLogger(LogFileProcessor.class);
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	private static ConcurrentHashMap<String, LogMessage> previousElements = new ConcurrentHashMap<>();
	
	//@Value("${event.alert.threshold}")
	private static Integer threshold =4;
	
	 

	private Util()
	{}


	public static Boolean isValidatMessage(LogMessage message) {
		return validator.validate(message)
				.isEmpty();
	}
	public static Boolean isValidJson(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.readTree(json);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static LogMessage convertStringToLogMessage(String str) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(str, LogMessage.class);
		} catch (IOException e) {
			log.error("Error while Converting data ");
			throw Exceptions.propagate(e);
		}
	}

	private static boolean isLongRunning(long diff) {		
		return diff> threshold;
		
	}
	public static LongRunningMessageReport updateDuration(LogMessage message) {

		try {
			if (previousElements.containsKey(message.getId())) {
				LogMessage oldMessage = previousElements.get(message.getId());
				previousElements.remove(message.getId());
				Long diff = Math.abs(Math.abs(oldMessage.getTimestamp() - message.getTimestamp()));

				return LongRunningMessageReport.builder()
						.eventDuration(diff)
						.alert(isLongRunning(diff))
						.eventType(oldMessage.getType())
						.host(oldMessage.getHost())
						.id(message.getId())
						.isValid(true)
						.build();
			} else {
				previousElements.put(message.getId(), message);
				return LongRunningMessageReport.builder()
						.isValid(false)
						.build();
			}

		} catch (Exception e) {
			log.error("Error while Converting data ");
			throw Exceptions.propagate(e);
		}

	}

}
