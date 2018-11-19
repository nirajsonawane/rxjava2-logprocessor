package com.logprocessor.rxjavalogprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BadRecordReport {

	private Long lineNumber;
	private String errorMessage;
	
}
