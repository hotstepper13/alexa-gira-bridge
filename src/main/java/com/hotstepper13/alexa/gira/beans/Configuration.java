package com.hotstepper13.alexa.gira.beans;

import java.util.List;

public class Configuration {

	private HsConfig hsConfig;
	private bridgeConfig bridgeConfig;
	private List<Room> rooms;
		
	public HsConfig getHomeserver() {
		return hsConfig;
	}
	
	public bridgeConfig getBridgeConfig() {
		return bridgeConfig;
	}

	public List<Room> getRooms() {
		return rooms;
	}
	
}
