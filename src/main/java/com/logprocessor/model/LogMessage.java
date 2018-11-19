package com.logprocessor.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogMessage {

	@NotBlank
	private String id;
	@NotBlank
	private String state;
	private String type;
	private String host;
	@NotNull
	private Long timestamp;
	private Long lineNumber;

}
