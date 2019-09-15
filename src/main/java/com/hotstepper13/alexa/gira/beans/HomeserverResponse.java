package com.hotstepper13.alexa.gira.beans;

public class HomeserverResponse {

	private Request request;
	private int code;
	private String type;
	private Data data;
	
	public Request getRequest() {
		return request;
	}
	public int getCode() {
		return code;
	}
	public String getType() {
		return type;
	}
	public Data getData() {
		return data;
	}
}
