package com.hotstepper13.alexa.gira.beans;

public class OnOff {

	private int type;
	private int value;

	@Override
	public String toString() {
		return "type: " + this.type + " / value: " + value;
	}

	public int getType() {
		return type;
	}

	public boolean isValue() {
		return (value > 0);
	}
	
	
	
}
