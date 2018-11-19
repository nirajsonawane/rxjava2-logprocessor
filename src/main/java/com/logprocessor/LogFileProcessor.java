package com.logprocessor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.logprocessor.model.LogMessage;
import com.logprocessor.service.DaoService;

import io.reactivex.Observable;

/**
 * 
 * @author Niraj Sonawane
 * LogFileProcessor class is Responsible for deciding log processing Flow. 
 * 
 * Observable performs below tasks 
 * 		Step 1: LogFileObservableSource emits the complete json objects    
 * 		Step 2: Map  String Json to Java Objects 
 * 		Step 3: Filter messages with invalid json and missing Mandatory fields
 * 		Step 4: Calculate Duration and Alert flag     
 * 		Step 5: Pass Chunk to Consumer
 * 
 * Subscriber work on the data given by Observable and insert it to database 
 *   
 *   
 */ 

@Component
public class LogFileProcessor {

	private static final Logger log = LoggerFactory.getLogger(LogFileProcessor.class);

	@Autowired
	private DaoService daoService;	
	
	@Value("${message.chunk.size}")
	private int chunkSize;


	public void processFile(String filePath) {

		final Observable<List<LogMessage>> observable = Observable
				.defer(() -> new LogFileObservableSource(filePath))
				.map(Util::logMessageMapper)
				.filter(Util::isValidatMessage)
				.map(Util::durationMapper)
				.filter(LogMessage::getIsValid)
				.buffer(chunkSize);
	 

		//@Todo subscribeOn(Schedulers.newThread()) is not working, Need to work on this. This will improve performance further.
		observable.subscribe(
						  //messageData -> System.out.println(messageData.size()),
						   messageData -> daoService.insertBatch(messageData),
						   Throwable::printStackTrace,
						   () -> log.info("File Processing Completed")
						  );

	}

	

}
