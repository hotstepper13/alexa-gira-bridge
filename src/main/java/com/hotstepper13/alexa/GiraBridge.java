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
package com.hotstepper13.alexa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotstepper13.alexa.configuration.Config;
import com.hotstepper13.alexa.gira.Discovery;

import ch.qos.logback.classic.Level;

public class GiraBridge {

	private final static Logger log = LoggerFactory.getLogger(GiraBridge.class);
	private static Config config;
	
	public static void main(String[] args) {
		if( args.length >= 4 && args[3].equals("debug")) {
			ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
	    root.setLevel(Level.DEBUG);
		}
		if( args.length >= 3 ) {
			GiraBridge.config = new Config(args[0],args[1],args[2]);
		} else {
			log.error("Not enough parameters provided!");
			GiraBridge.usage();
		}
		
		Discovery discovery = new Discovery();
		
	}

	private static void usage() {
		System.out.println("");
		System.out.println("Usage:");
		System.out.println("");
		System.out.println("java -jar <jarfile> <homeserverIp> <homeserverPort> <token> (Optional: debug)");
		System.out.println("");
		System.out.println("To start regular (Info logging):");
		System.out.println("java -jar GiraBridge.jar 192.168.0.15 30000 superCOOLpassword");
		System.out.println("");
		System.out.println("To start in debug mode just add \"debug\":");
		System.out.println("java -jar GiraBridge.jar 192.168.0.15 30000 superCOOLpassword debug");
		System.out.println("");
	}
	
}
