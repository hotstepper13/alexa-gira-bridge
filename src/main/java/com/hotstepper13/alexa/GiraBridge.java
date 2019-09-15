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

import java.io.BufferedReader;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.hotstepper13.alexa.gira.Discovery;
import com.hotstepper13.alexa.gira.Homeserver;
import com.hotstepper13.alexa.gira.beans.Configuration;
import com.hotstepper13.alexa.network.TcpServer;
import com.hotstepper13.alexa.network.upnp.UpnpServer;

import ch.qos.logback.classic.Level;

public class GiraBridge {

	private final static Logger log = LoggerFactory.getLogger(GiraBridge.class);
	public static Configuration config = null;
	public static Discovery discovery;
	public static int port = 4711;
	private static String configFile = "";
	public static boolean isDebug = false;
	public static Homeserver hs;

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("java.net.preferIPv4Stack", "true");

		
		// read path for configuration file from parameters
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--config-file")) {
				i++;
				configFile = args[i];
			} else if (args[i].equals("--debug")) {
				i++;
				if (new Boolean(args[i]).booleanValue()) {
					ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
							.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
					root.setLevel(Level.DEBUG);
					isDebug = true;
				}
			}
		}
		
		// read the configuration
		Gson gson = new Gson();
		BufferedReader bufferedReader=null;
		try {
			bufferedReader = new BufferedReader(new FileReader(configFile));
			config = gson.fromJson(bufferedReader, Configuration.class);
		} catch (Throwable t) {
			log.error("Cannot read configfile:" +  t.toString());
			GiraBridge.usage();
			System.exit(1);
		}
		
		log.info("Config was read successfully");
		log.debug(config.getHomeserver().toString());
		GiraBridge.hs = new Homeserver(config.getHomeserver());
		if(GiraBridge.config.getBridgeConfig().getPort() != 0) {
			GiraBridge.port = GiraBridge.config.getBridgeConfig().getPort();
		}
		
		// Start the discovery process
		discovery = new Discovery();

		// Start a TCP Server for echo <-> Hue communication
		TcpServer tcp = new TcpServer();

		// Start UPNP Server for discovery process
		UpnpServer upnpServer = new UpnpServer(tcp.getAddress(), GiraBridge.port);
		log.debug("Starting UPNP on interface: " + tcp.getAddress());
		upnpServer.start();

		
	}

	private static void usage() {
		System.out.println("");
		System.out.println("Usage:");
		System.out.println(
				"java -jar <jarfile> --config-file <path_to_json_config> (Optional: --debug true)");
		System.out.println("");
	}

}
