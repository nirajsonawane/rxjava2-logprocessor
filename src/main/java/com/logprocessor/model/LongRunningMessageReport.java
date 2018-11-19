package com.logprocessor.model;

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
	private Long eventDuration;
	private String host;
	private Boolean alert;
	private Boolean isValid =true;

}
