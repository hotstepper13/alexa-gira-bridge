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
import com.hotstepper13.alexa.network.TcpServer;
import com.hotstepper13.alexa.network.upnp.UpnpServer;

import ch.qos.logback.classic.Level;

public class GiraBridge {

	private final static Logger log = LoggerFactory.getLogger(GiraBridge.class);
	public static Config config;
	private final static int port = 4711;
	
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("java.net.preferIPv4Stack" , "true");

		GiraBridge.config = new Config();
		
		for(int i=0; i<args.length;i++) {
			if(args[i].equals("--homeserver-ip")) {
				i++;
				Config.setHomeserverIp(args[i]);
			} else if(args[i].equals("--homeserver-port")) {
				i++;
				Config.setHomeserverPort(args[i]);
			} else if(args[i].equals("--token")) {
				i++;
				Config.setToken(args[i]);
			} else if(args[i].equals("--debug")) {
				i++;
				if(new Boolean(args[i]).booleanValue()) {
					ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
					root.setLevel(Level.DEBUG);
				}
			}
		}

		if(!Config.isSetup()) {
			log.error("Not enough parameters provided!");
			GiraBridge.usage();
			System.exit(1);
		}

		// Start the discovery process
		Discovery discovery = new Discovery();

		// Start a TCP Server for echo <-> Hue communication
		TcpServer tcp = new TcpServer(GiraBridge.port, discovery.getDiscoveryItem().getPayload().getDiscoveredAppliances());
		
		// Start UPNP Server for discovery process
		UpnpServer upnpServer = new UpnpServer(tcp.getAddress(),GiraBridge.port);
		upnpServer.start();
	
	}

	private static void usage() {
		System.out.println("");
		System.out.println("Usage:");
		System.out.println("java -jar <jarfile> --homeserver-ip <homeserverIp> --homeserver-port <homeserverPort> --token <token> (Optional: --debug true)");
		System.out.println("");
		System.out.println("To start regular (Info logging):");
		System.out.println("java -jar GiraBridge-jar-with-dependencies.jar --homeserver-ip 192.168.0.15 --homeserver-port 30000 --token superCOOLpassword");
		System.out.println("");
		System.out.println("To start in debug mode just add \"--debug true\" (token/passwords will be visible!):");
		System.out.println("java -jar GiraBridge-jar-with-dependencies.jar --homeserver-ip 192.168.0.15 --homeserver-port 30000 --token superCOOLpassword --debug true");
		System.out.println("");
	}
	
	
}
