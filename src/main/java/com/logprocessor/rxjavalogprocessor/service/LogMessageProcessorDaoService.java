package com.logprocessor.rxjavalogprocessor.service;

import com.logprocessor.rxjavalogprocessor.model.BadRecordReport;
import com.logprocessor.rxjavalogprocessor.model.LogMessage;

public interface LogMessageProcessorDaoService {

	void insertBadRecord(BadRecordReport badRecord);

	void insertStaredMessage(LogMessage message);

	void insertFinishedMessage(LogMessage message);

	void populateReport() ;

	void insertError(Throwable t);

}
