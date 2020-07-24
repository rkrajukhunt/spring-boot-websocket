package com.rajukhunt.websocket.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericRes<T> {

	@Default
	private boolean status = true;

	@Default
	private String code = "200";

	private String message, description;
	
	private String type;
	
	private T data;

}
