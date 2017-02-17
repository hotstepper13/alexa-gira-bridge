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
package com.hotstepper13.alexa.gira;

import java.text.MessageFormat;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.hotstepper13.alexa.configuration.Config;
import com.hotstepper13.alexa.configuration.Constants;
import com.hotstepper13.alexa.gira.beans.Appliance;
import com.hotstepper13.alexa.gira.beans.DiscoveryItem;
import com.hotstepper13.alexa.network.Util;

public class Discovery {

	private final static Logger log = LoggerFactory.getLogger(Discovery.class);
	
	private String objectJson="";
	private String discovery_url;
	private DiscoveryItem discoveryItem;
	
	public Discovery() {
		Object[] params = new Object[]{Config.getHomeserverIp(),Config.getHomeserverPort(), Config.getToken()};
		this.discovery_url = MessageFormat.format(Constants.DISCOVERY_URL, params);
		fetchDiscoverySSL();
		parseDiscoveryResponse();
	}

	/**
	 *  This Discovery requires a custom logic module to be installed
	 *  in the homeserver
	 *  
	 *  see https://github.com/Picpol/HS-AmazonEcho
	 *  https://knx-user-forum.de/forum/%C3%B6ffentlicher-bereich/knx-eib-forum/1010815-amazon-echo-logikbaustein
	 */
	private void fetchDiscoverySSL() {
		log.debug("Fetching Objects from Homeserver using url: " + this.discovery_url);

		this.objectJson = Util.triggerHttpGetWithCustomSSL(this.discovery_url);

	}

	private void parseDiscoveryResponse() {
		Gson gson = new Gson();
		this.discoveryItem = gson.fromJson(this.objectJson, DiscoveryItem.class);
		log.info("Discoverered " + this.discoveryItem.getPayload().getDiscoveredAppliances().size() + " items from Homeserver");
		Iterator<Appliance> appliances = this.discoveryItem.getPayload().getDiscoveredAppliances().iterator();
		while(appliances.hasNext()) {
			Appliance item = (Appliance)appliances.next();
			log.info(item.getFriendlyName() + " with id " + item.getApplianceId() + " has the following actions: ");
			Iterator<Appliance.Actions> actions = item.getActions().iterator();
			while(actions.hasNext()) {
				Appliance.Actions action = (Appliance.Actions)actions.next();
				log.info("  * " + action.name());
			}
		}
	}

	public String getObjectJson() {
		return objectJson;
	}

	public String getDiscovery_url() {
		return discovery_url;
	}

	public DiscoveryItem getDiscoveryItem() {
		return discoveryItem;
	}

	
	
}
