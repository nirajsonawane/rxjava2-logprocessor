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

@Component
public class LogFileProcessor {

	private static final Logger log = LoggerFactory.getLogger(LogFileProcessor.class);

	@Autowired
	private DaoService daoService;	
	
	@Value("${message.chunk.size}")
	private int chunkSize;


	public void processLogFile(String filePath) {

		final Observable<List<LogMessage>> observable = Observable
				.defer(() -> new LogFileObservableSource(filePath))
				.map(Util::convertStringToLogMessage)
				.filter(Util::isValidatMessage)
				.map(Util::updateDuration)
				.filter(LogMessage::getIsValid)
				.buffer(chunkSize);
	

		//@Todo subscribeOn is not working, Need to work on this.
		observable//.subscribeOn(Schedulers.newThread())
				   .subscribe(
						  // messageData -> System.out.println(messageData.size()),
						   messageData -> daoService.insertBatch(messageData),
						   Throwable::printStackTrace,
						   () -> log.info("File Processing Complited")
						  );

	}

	

}
