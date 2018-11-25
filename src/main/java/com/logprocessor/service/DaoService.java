package com.logprocessor.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.logprocessor.model.LogMessage;

public interface DaoService {	
	
	CompletableFuture<Boolean> insertBatch(List<LogMessage> batchMessage);
	Integer getTotalRowCount();

}
