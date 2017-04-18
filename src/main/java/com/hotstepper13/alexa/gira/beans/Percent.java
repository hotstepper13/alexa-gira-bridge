package com.hotstepper13.alexa.gira.beans;

public class Percent {

	private int type;
	private double value;

	@Override
	public String toString() {
		return "type: " + this.type + " / value: " + value;
	}

	public int getType() {
		return type;
	}

	public double getValue() {
		return value;
	}

}
