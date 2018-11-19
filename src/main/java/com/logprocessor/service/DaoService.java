package com.logprocessor.service;

import java.util.List;

import com.logprocessor.model.LongRunningMessageReport;

public interface DaoService {	
	
	void insertBatch(List<LongRunningMessageReport> batchMessage);

}
