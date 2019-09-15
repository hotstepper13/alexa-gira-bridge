package com.hotstepper13.alexa.gira.beans;

import com.hotstepper13.alexa.GiraBridge;

public class HsConfig {

	private String ip;
	private String username;
	private String password;
	
	public String getIp() {
		return ip;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String toString() {
		return "ip: " + ip + " / username: " + username + (GiraBridge.isDebug?" / password: " + password:"");
	}
}
