package com.bytecoder.util;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage<T> {
	private boolean status;
	private String message;
	private T data;
	private List<?> dataList;
	
	public ResponseMessage(boolean status, String message, T data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}
	

	public ResponseMessage(boolean status, String message, List<?> dataList) {
		super();
		this.status = status;
		this.message = message;
		this.dataList = dataList;
	}
	

}