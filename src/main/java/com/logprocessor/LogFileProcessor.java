package com.logprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.logprocessor.model.LogMessage;
import com.logprocessor.service.LogMessageProcessorDaoService;

import io.reactivex.Observable;
import io.reactivex.observables.GroupedObservable;

@Component
public class LogFileProcessor {

	private static final Logger log = LoggerFactory.getLogger(LogFileProcessor.class);

	@Autowired
	private LogMessageProcessorDaoService daoService;
	
	
	@Value("${message.chunk.size}")
	private int chunkSize;



	public void processLogFile(String filePath) {

		final Observable<GroupedObservable<String, LogMessage>> observable = Observable
				.defer(() -> new LogFileObservableSource(filePath))
				.map(Util::convertStringToLogMessage)
				.filter(Util::isValidatMessage)
				.groupBy(LogMessage::getState);
	

		observable.subscribe(
				messageData -> insertMessageInDataBase(messageData),
				error -> daoService.insertError(error), 
				() ->{ log.info("File Processing Complited");
						daoService.populateReport();
					});

	}

	private void insertMessageInDataBase(GroupedObservable<String, LogMessage> groupedObservableMessages){
	
		if (groupedObservableMessages.getKey().equals("STARTED")) {
			groupedObservableMessages.buffer(chunkSize)
			.subscribe(daoService::insertBatchStaredMessage);

		} else {
			groupedObservableMessages
			.buffer(chunkSize)
			.subscribe(daoService::insertFinishedMessage);
		}
	}

}
