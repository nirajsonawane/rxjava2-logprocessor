package com.logprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BadRecordReport {

	private Long lineNumber;
	private String errorMessage;
	
}
