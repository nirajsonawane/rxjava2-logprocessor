package com.logprocessor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
	@Autowired
	private ThreadPoolTaskExecutor executor;
	
	List<CompletableFuture<Boolean>>  completableFuturelist = new LinkedList<>();
	
	public void processFile(String filePath) {
		final Observable<List<LogMessage>> observable = Observable
				.defer(() -> new LogFileObservableSource(filePath))				
				.map(Util::logMessageMapper)
				.filter(Util::isValidatMessage)
				.map(Util::durationMapper)
				.filter(LogMessage::getIsValid)
				.buffer(chunkSize);
				
		//@Todo subscribeOn(Schedulers.newThread()) Update Getting better performance using database asyn call instead of subscribeOn 
		observable.subscribe(
						    messageData -> completableFuturelist.add(daoService.insertBatch(messageData)),						    
						    Throwable::printStackTrace,						   
						    () ->{	
							      log.info("File Processing Completed");
							      log.info("Waiting for all database insert threads to complete  ");
							      //For Simplicity, I am checking if all CompletableFuture are complete or not in single call. But this can be improve.     
							      CompletableFuture.allOf(completableFuturelist.toArray(new CompletableFuture[completableFuturelist.size()])).join();
							      log.info("Total Message Count {} ", daoService.getTotalRowCount());
							      executor.shutdown();
						   }
						  );

	}

	

}
