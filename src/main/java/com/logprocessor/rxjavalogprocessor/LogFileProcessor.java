package com.logprocessor.rxjavalogprocessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logprocessor.rxjavalogprocessor.model.LogMessage;
import com.logprocessor.rxjavalogprocessor.service.LogMessageProcessorDaoService;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.observables.GroupedObservable;

@Component
public class LogFileProcessor {

	private static final Logger log = LoggerFactory.getLogger(LogFileProcessor.class);

	@Autowired
	private LogMessageProcessorDaoService daoService;

	@Autowired
	private Util util;

	public void processLogFile(String filePath) {

		
		
		final Observable<GroupedObservable<String, LogMessage>> observable = Observable
				.defer(() -> new LogFileObservableSource(filePath))
				.map(util::convertStringToLogMessage)
				.filter(util::isValidatMessage)
				.groupBy(LogMessage::getState);

		
		
		
		observable.subscribe(
				messageData -> insertMessageInDataBase(messageData),
				error -> daoService.insertError(error), 
				() -> daoService.populateReport());

	}

	private void insertMessageInDataBase(GroupedObservable<String, LogMessage> groupedObservableMessages) {
		if (groupedObservableMessages.getKey()
				.equals("STARTED")) {
			groupedObservableMessages.subscribe(daoService::insertStaredMessage);
		} else {
			groupedObservableMessages.subscribe(daoService::insertFinishedMessage);
		}
	}
	

}
