package com.hotstepper13.alexa.gira;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotstepper13.alexa.configuration.Config;
import com.hotstepper13.alexa.configuration.Constants;
import com.hotstepper13.alexa.gira.beans.Appliance;
import com.hotstepper13.alexa.gira.beans.HueStateChange;
import com.hotstepper13.alexa.network.Util;

public class Trigger {

	private final static Logger log = LoggerFactory.getLogger(Trigger.class);
	
	private static ArrayList<Appliance> appliances;
	
	public Trigger(ArrayList<Appliance> appliances) {
		Trigger.appliances = appliances;
	}

	public boolean pull(int id, HueStateChange state) {
		log.info("Trigger pulled for id " + id + ": " + state.toString());
		boolean result = false;
		Appliance appliance = appliances.get(id-1);
		String action="TurnOffRequest";
		
		if(!state.isOn() && !appliance.getActions().contains(Appliance.Actions.turnOff) || state.isOn() && appliance.getActions().contains(Appliance.Actions.turnOn)) {
			// Received off, turn off unavailable -> send on even if off received (push button support)
			action="TurnOnRequest";
			log.info("Sending TurnOnRequest to " + appliance.getFriendlyName());
		} else {
			log.info("Sending TurnOffRequest to " + appliance.getFriendlyName());
		}
		
		Object[] params = new Object[]{Config.getHomeserverIp(),Config.getHomeserverPort(),appliance.getApplianceId(),action,Config.getToken()};
		String request = MessageFormat.format(Constants.GIRA_REQUEST_TEMPLATE, params);
		String response = Util.triggerHttpGetWithCustomSSL(request);
		if(response != null) {
			result = true;
		}
		
		return result;
	}
	
}
