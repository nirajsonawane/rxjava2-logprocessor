package com.logprocessor.rxjavalogprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LongRunningMessageReport  {
	
	private String id;
	private String eventType;
	private String eventDuration;
	private String host;
	private String alert;

}
