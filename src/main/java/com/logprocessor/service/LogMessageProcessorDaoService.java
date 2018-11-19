package com.logprocessor.service;

import java.util.List;

import com.logprocessor.model.BadRecordReport;
import com.logprocessor.model.LogMessage;

public interface LogMessageProcessorDaoService {

	void insertBadRecord(BadRecordReport badRecord);

	void populateReport();

	void insertError(Throwable t);

	void insertBatchStaredMessage(List<LogMessage> batchMessage);

	void insertFinishedMessage(List<LogMessage> batchMessage);

}
