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
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.put;
import static spark.Spark.port;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.hotstepper13.alexa.configuration.Constants;
import com.hotstepper13.alexa.gira.Trigger;
import com.hotstepper13.alexa.gira.beans.Appliance;
import com.hotstepper13.alexa.gira.beans.HueStateChange;

public class TcpServer{

	private static Logger log = LoggerFactory.getLogger(TcpServer.class);
	
	private int port;
	private InetAddress ip;
	private ArrayList<Appliance> appliances;
	private Trigger trigger;
	private Gson gson;
	
	public TcpServer(int port, List<Appliance> appliances) {
		System.setProperty("java.net.preferIPv4Stack" , "true");
		this.appliances = new ArrayList<Appliance>(appliances);
		this.port = port;
		this.trigger = new Trigger(this.appliances);
		this.gson = new Gson();
		try {
			this.ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			log.error("Cannot detemine local ip",e);
		}
		log.info("TCP Server found IP: " + this.ip.getHostAddress());		

		// configure port for spark server
		port(port);
		
		// define handling for GET /description.xml
		get("/description.xml", "application/xml; charset=utf-8", (request, response) -> {
			log.info("Received request to /description.xml from " + request.ip());
			Object[] params = new Object[]{this.ip,""+this.port};
			String xml = MessageFormat.format(Constants.HUE_DESCRIPTION, params);
			response.type("application/xml; charset=utf-8"); 
      response.status(HttpStatus.SC_OK);
      return xml;
    } );
		
		// define handling for GET /api/<username>/lights
		get("/api/:userid/lights", "application/json", (request, response) -> {
			log.info("Received request to /api/"+request.params(":userid")+"/lights from " + request.ip());
			response.header("Access-Control-Allow-Origin", request.headers("Origin"));
			response.type("application/json");
			response.status(HttpStatus.SC_OK);
			return this.buildLights();
		} );
		
		// define handling for PUT /api/{userId}/lights/{lightId}/state uses json
		// object to set the lights state
		put("/api/:userid/lights/:id/state", "application/json", (request, response) -> {
			log.info("Received request to /api/"+request.params(":userid")+"/lights/"+request.params(":id")+"/state from " + request.ip());
			response.header("Access-Control-Allow-Origin", request.headers("Origin"));
			response.type("application/json");
			response.status(HttpStatus.SC_OK);
			HueStateChange state = this.gson.fromJson(request.body(), HueStateChange.class);
			boolean result = trigger.pull(new Integer(request.params(":id")).intValue(), state);
			
			if(result) {
				return this.HueSuccess(request.params(":id"), state.isOn()?"on":"off");
			} else {
				return this.HueError(request.params(":id"), state.isOn()?"on":"off");
			}
		});
		
		// define handling for PUT /api/{userId}/lights/{lightId} uses json
		// object for a given light
		get("/api/:userid/lights/:id", "application/json", (request, response) -> {
			log.info("Received request to /api/"+request.params(":userid")+"/lights/"+request.params(":id")+" from " + request.ip());
			response.header("Access-Control-Allow-Origin", request.headers("Origin"));
			response.type("application/json");
			response.status(HttpStatus.SC_OK);
			return this.appliances.get(new Integer(request.params(":id"))-1).getHueDevice();
		});
		
	}

	public String getAddress() {
		return this.ip.getHostAddress();
	}
	
	private String buildLights() {
		log.info("Handle lights request");
		String response = "{\r\n";
		for(int i=0;i<appliances.size();i++) {
			appliances.get(i).setHueId(i+1);
			response = response + "	\"" + appliances.get(i).getHueId() + "\": " + appliances.get(i).getHueDevice();
			if(i<appliances.size()-1) {
				response = response + ",";
			}
		}

		response = response + "}";
		log.debug("Responsebody: \r\n" + response);
		return response;
	}

	private String HueSuccess(String id, String state) {
		Object[] params = new Object[]{id,state};
		String response = "\"/lights/{0}/state/{1}\"";
		return "{\"success\":{" + MessageFormat.format(response, params) + ":true}}";
	}
	
	private String HueError(String id, String state) {
		Object[] params = new Object[]{id,state};
		String response = "\"/lights/{0}/state/{1}\"";
		return "{\"error\":{" + MessageFormat.format(response, params) + ":false}}";
	}	

}


