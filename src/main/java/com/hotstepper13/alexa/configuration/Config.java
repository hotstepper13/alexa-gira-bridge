/*******************************************************************************
 * Copyright (C) 2017  Frank Mueller
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.hotstepper13.alexa.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

	private static String homeserverIp;
	private static String homeserverPort;
	private static String token;
	
	private final static Logger log = LoggerFactory.getLogger(Config.class);
	
	public Config() {
		
	}
	
	public static boolean isSetup() {
		if(Config.homeserverIp != null && !Config.homeserverIp.equals("") && 
				Config.homeserverPort != null && !Config.homeserverPort.equals("") &&
				Config.token != null && !Config.token.equals("")) {
			log.info("Initialized config");
			log.info("HomeserverIp: " + Config.homeserverIp);
			log.info("HomeserverPort: " + Config.homeserverPort);
			if(log.isDebugEnabled()) {
				log.debug("Token: " + Config.token);
			} else {
				log.info("Token: " + Config.token.replaceAll(".", "*") + " (Hint: enable debug to show cleartext!)");
			}
			return true;	 
		} else {
			return false;
		}
	}

	public static String getHomeserverIp() {
		return homeserverIp;
	}

	public static String getHomeserverPort() {
		return homeserverPort;
	}

	public static String getToken() {
		return token;
	}

	public static void setHomeserverIp(String homeserverIp) {
		Config.homeserverIp = homeserverIp;
	}

	public static void setHomeserverPort(String homeserverPort) {
		Config.homeserverPort = homeserverPort;
	}

	public static void setToken(String token) {
		Config.token = token;
	}

	
	
}
