package com.hotstepper13.alexa.gira.beans;

import java.util.List;

public class Room {
	
	private String room;
	private int id;
	private List<Item> items;
	
	public String getRoomName() {
		return room;
	}
	
	public int getId() {
		return id;
	}
	
	public List<Item> getItems() {
		return items;
	}
}
