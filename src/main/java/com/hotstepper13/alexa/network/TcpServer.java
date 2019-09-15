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
package com.hotstepper13.alexa.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.Inet4Address;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Enumeration;

import static spark.Spark.get;
import static spark.Spark.put;
import static spark.Spark.port;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.hotstepper13.alexa.GiraBridge;
import com.hotstepper13.alexa.configuration.Constants;
import com.hotstepper13.alexa.gira.Trigger;
import com.hotstepper13.alexa.gira.beans.Appliance;
import com.hotstepper13.alexa.gira.beans.HueStateChange;

public class TcpServer {

	private static Logger log = LoggerFactory.getLogger(TcpServer.class);

	private int port;
	private InetAddress ip;
	//private static List<Appliance> appliances;
	private Trigger trigger;
	private Gson gson;

	public TcpServer() {
		this.port = GiraBridge.port;
		this.trigger = new Trigger(GiraBridge.discovery.appliances);
		this.gson = new Gson();
		this.ip = this.getLocalAddress();
		log.info("TCP Server found IP: " + this.ip.getHostAddress());
		if (!GiraBridge.config.getBridgeConfig().getIp().equals("")) {
			log.info("Overwriting IP for Discovery calls with: " + GiraBridge.config.getBridgeConfig().getIp());
		}

		// configure port for spark server
		port(port);

		// define handling for GET /description.xml
		get("/description.xml", "application/xml; charset=utf-8", (request, response) -> {
			log.info("Received request to /description.xml from " + request.ip());
			Object[] params;
			if (GiraBridge.config.getBridgeConfig().getIp().equals("")) {
				params = new Object[] { this.ip, "" + this.port };
			} else {
				params = new Object[] { GiraBridge.config.getBridgeConfig().getIp(), "" + this.port };
			}
			String xml = MessageFormat.format(Constants.HUE_DESCRIPTION, params);
			response.type("application/xml; charset=utf-8");
			response.status(HttpStatus.SC_OK);
			return xml;
		});

		// define handling for GET /api/<username>/lights
		get("/api/:userid/lights", "application/json", (request, response) -> {
			log.info("Received request to /api/" + request.params(":userid") + "/lights from " + request.ip());
			response.header("Access-Control-Allow-Origin", request.headers("Origin"));
			response.type("application/json");
			response.status(HttpStatus.SC_OK);
			return this.buildLights();
		});

		// define handling for GET /api/<username>/lights/<id>
		get("/api/:userid/lights/:id", "application/json", (request, response) -> {
			log.info("Received request to /api/" + request.params(":userid") + "/lights/" + request.params(":id") + " from "
					+ request.ip());
			response.header("Access-Control-Allow-Origin", request.headers("Origin"));
			response.type("application/json");
			String hueDevice = this.buildStateForID(new Integer(request.params(":id")).intValue());
			if (!hueDevice.equals("")) {
				response.status(HttpStatus.SC_OK);
				return hueDevice;
			} else {
				response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				return "";
			}
		});

		// define handling for PUT /api/{userId}/lights/{lightId}/state uses json
		// object to set the lights state
		put("/api/:userid/lights/:id/state", "application/json", (request, response) -> {
			log.info("Received request to /api/" + request.params(":userid") + "/lights/" + request.params(":id")
					+ "/state from " + request.ip());
			response.header("Access-Control-Allow-Origin", request.headers("Origin"));
			response.type("application/json");
			response.status(HttpStatus.SC_OK);
			log.debug(request.body());
			HueStateChange state = this.gson.fromJson(request.body(), HueStateChange.class);
			boolean result = trigger.pull(new Integer(request.params(":id")).intValue(), state);

			if (result) {
				return this.HueSuccess(request.params(":id"), state.isOn() ? "on" : "off");
			} else {
				return this.HueError(request.params(":id"), state.isOn() ? "on" : "off");
			}
		});

		// define handling for PUT /api/{userId}/lights/{lightId} uses json
		// object for a given light
		get("/api/:userid/lights/:id", "application/json", (request, response) -> {
			log.info("Received request to /api/" + request.params(":userid") + "/lights/" + request.params(":id") + " from "
					+ request.ip());
			response.header("Access-Control-Allow-Origin", request.headers("Origin"));
			response.type("application/json");
			response.status(HttpStatus.SC_OK);
			return GiraBridge.discovery.appliances.get(new Integer(request.params(":id")) - 1).getHueDevice();
		});

	}

	public String getAddress() {
		return this.ip.getHostAddress();
	}

	private String buildLights() {
		log.info("Handle lights request");
		String response = "{\r\n";
		for (int i = 0; i < GiraBridge.discovery.appliances.size(); i++) {
			GiraBridge.discovery.appliances.get(i).setHueId(i + 1);
			response = response + "	\"" + GiraBridge.discovery.appliances.get(i).getHueId() + "\": " + GiraBridge.discovery.appliances.get(i).getHueDevice();
			if (i < GiraBridge.discovery.appliances.size() - 1) {
				response = response + ",";
			}
		}

		response = response + "}";
		log.debug("Responsebody: \r\n" + response);
		return response;
	}

	private String HueSuccess(String id, String state) {
		Object[] params = new Object[] { id, state };
		String response = "\"/lights/{0}/state/{1}\"";
		return "{\"success\":{" + MessageFormat.format(response, params) + ":true}}";
	}

	private String HueError(String id, String state) {
		Object[] params = new Object[] { id, state };
		String response = "\"/lights/{0}/state/{1}\"";
		return "{\"error\":{" + MessageFormat.format(response, params) + ":false}}";
	}

	private String buildStateForID(int id) {
		// Bugfix if devices are removed
		if (id > GiraBridge.discovery.appliances.size()) {
			log.info("Called ID was larger amount of Appliances in List. Assume Discovery with removed Appliance was triggered");
			log.info("Return dummy device with Hue State");
			Appliance a = new Appliance("Discovery", "0", Arrays.asList(Appliance.Actions.turnOn));
			a.setOn(true);
			return a.getHueDevice();
		}

		Appliance app = GiraBridge.discovery.appliances.get(id - 1);
		if (app.getActions().contains(Appliance.Actions.turnOff) || app.getActions().contains(Appliance.Actions.turnOn)) {
			if (app.getApplianceId().equals("0")) {
				app.setOn(true);
			} else {
				// Fetch OnOff
				if(app.getIdSwitch() != null) {
					if(GiraBridge.hs.getStatusForSwitch(app.getIdSwitch()) > 0) {
						app.setOn(true);
					}
				} else if (app.getIdTrigger() != null){
					if(GiraBridge.hs.getStatusForTrigger(app.getIdTrigger()) > 0) {
						app.setOn(true);
					}
				} else {
					log.error("No Trigger or Switch ID found");
				}
			}
		}

		if (app.getActions().contains(Appliance.Actions.setPercentage)
				|| app.getActions().contains(Appliance.Actions.incrementPercentage)
				|| app.getActions().contains(Appliance.Actions.decrementPercentage)) {
			// Fetch Percent
			if(app.getIdPercentage() != null) {
				double returnValue = GiraBridge.hs.getStatusForPercentage(app.getIdPercentage());
				if (returnValue == 100.0) {
					app.setBri(255);
				} else if (returnValue > 0) {
					app.setBri(new Double((returnValue / 100) * 255).intValue());
				} else {
					app.setBri(0);
				}
			} else {
				log.error("No percentage id found");
			}
			
		}

		return app.getHueDevice();
	}

	public InetAddress getLocalAddress() {
		try {
			Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
			while (ifaces.hasMoreElements()) {
				NetworkInterface iface = ifaces.nextElement();
				Enumeration<InetAddress> addresses = iface.getInetAddresses();

				while (addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();
					if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
						return addr;
					}
				}
			}
		} catch (SocketException se) {
			log.error("Cannot determine local ipv4 address.", se);
		}

		return null;
	}

}
