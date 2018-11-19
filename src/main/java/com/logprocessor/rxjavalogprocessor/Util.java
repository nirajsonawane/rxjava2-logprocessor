package com.logprocessor.rxjavalogprocessor;

import java.io.IOException;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logprocessor.rxjavalogprocessor.model.LogMessage;

import io.reactivex.exceptions.Exceptions;

@Component
public class Util {

	private static final Logger log = LoggerFactory.getLogger(LogFileProcessor.class);
	@Autowired
	private Validator validator;

	public Boolean isValidatMessage(LogMessage message) {
		return validator.validate(message).isEmpty();
	}
	
	public Boolean isValidJson(String json)
	{
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.readTree(json);			
		} catch (IOException e) {
			return false;
		}
		return true;
	}	
	
	public LogMessage convertStringToLogMessage(String str) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(str, LogMessage.class);			
		} catch (IOException e) {
			log.error("Error while Converting data ");
			throw Exceptions.propagate(e);
		}
	}
}
