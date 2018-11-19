package com.logprocessor.service;

import java.util.List;

import com.logprocessor.model.LogMessage;

public interface DaoService {	
	
	void insertBatch(List<LogMessage> batchMessage);

}
