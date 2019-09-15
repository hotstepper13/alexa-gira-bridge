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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotstepper13.alexa.GiraBridge;
import com.hotstepper13.alexa.gira.beans.Appliance;
import com.hotstepper13.alexa.gira.beans.HomeserverResponse;
import com.hotstepper13.alexa.gira.beans.HueStateChange;

public class Trigger {

	private final static Logger log = LoggerFactory.getLogger(Trigger.class);

	private static List<Appliance> appliances;
	private static Homeserver homeserver;
	
	public Trigger(List<Appliance> appliances) {
		Trigger.appliances = appliances;
		homeserver = new Homeserver(GiraBridge.config.getHomeserver());
	}

	public boolean pull(int id, HueStateChange state) {
		log.info("Trigger pulled for id " + id + ": " + state.toString());
		Appliance appliance = appliances.get(id - 1);
		String action = "";
		String circuitBreaker = "doNotHandle";
		double value = 666.0;
		double maxValue = 255.0;
		int percMultiplier = 100;
		String actionId = "";

		// id = 0 == update Discovery
		// getBri != 0 == setPercentage
		// bri_inc > 0 == incrementPercentage
		// bri_inc < 0 == decrementPercentage
		// getBri = 0 && state = on == turn on
		// getBri = 0 && state = off == turn off
		if (id == Trigger.appliances.size()) {
			log.warn("Request for not available id");
			return true;
		} else if (!state.isOn() && state.getBri() == 0 && state.getBri_inc() == 0) {
			// turn off
			if (appliance.getActions().contains(Appliance.Actions.turnOff)) {
				log.info("Sending TurnOffRequest to " + appliance.getFriendlyName());
				action = "TurnOffRequest";
				value = 0.0;
				actionId = getActionIdForAppliance(appliance);
			} else if (!appliance.getActions().contains(Appliance.Actions.turnOff)
					&& appliance.getActions().contains(Appliance.Actions.turnOn)) {
				log.info("TurnOff Requested but TurnOff is not defined for appliance " + appliance.getFriendlyName()
						+ " overwriting to TurnOn which is defined for this appliance");
				action = "TurnOnRequest";
				value = 1.0;
				actionId = getActionIdForAppliance(appliance);
			} else {
				log.error("TurnOff was requested but neither TurnOff nor TurnOn is a valid action for appliance "
						+ appliance.getFriendlyName());
				action = circuitBreaker;
			}
		} else if (state.isOn() && state.getBri() == 0 && state.getBri_inc() == 0) {
			// turn on
			if (appliance.getActions().contains(Appliance.Actions.turnOn)) {
				log.info("Sending TurnOnRequest to " + appliance.getFriendlyName());
				action = "TurnOnRequest";
				value = 1.0;
				actionId = getActionIdForAppliance(appliance);
			} else if (!appliance.getActions().contains(Appliance.Actions.turnOn)
					&& appliance.getActions().contains(Appliance.Actions.turnOff)) {
				log.info("TurnOn Requested but TurnOn is not defined for appliance " + appliance.getFriendlyName()
						+ " overwriting to TurnOff which is defined for this appliance");
				action = "TurnOffRequest";
				value = 0.0;
				actionId = getActionIdForAppliance(appliance);
			} else {
				log.error("TurnOn was requested but neither TurnOn nor TurnOff is a valid action for appliance "
						+ appliance.getFriendlyName());
				action = circuitBreaker;
			}
		} else if (state.getBri() > 0 && state.getBri_inc() == 0) {
			// set perc
			if (appliance.getActions().contains(Appliance.Actions.setPercentage)) {
				action = "SetPercentageRequest";
				value = round(new Double((state.getBri() / maxValue) * percMultiplier).doubleValue(), 1);
				actionId = appliance.getIdPercentage();
				log.info("Sending SetPercentageRequest with value " + value + " to appliance " + appliance.getFriendlyName());
			} else {
				log.error("SetPercentage was requested but SetPercentation id not a valid action for appliance "
						+ appliance.getFriendlyName());
				action = circuitBreaker;
			}
		} else if (state.getBri() == 0 && state.getBri_inc() > 0) {
			// inc perc
			log.info("IncrementPercentatge requested but not yet implemented!");
		} else if (state.getBri() == 0 && state.getBri_inc() < 0) {
			// dec perc
			log.info("DecrementPercentatge requested but not yet implemented!");
		} else {
			log.error("Cannot determine needed action. State: " + state.toString());
		}

		if (action.equals(circuitBreaker) || actionId.equals("")) {
			return false;
		} else {
			return requestGiraChange(actionId, action, value);
		}
	}

	
	public boolean requestGiraChange(String groupaddress, String action, double value) {
		log.info("Received statechange for " + groupaddress + " with action " + action + " to value " + value);
		boolean result = false;
		HomeserverResponse response = homeserver.setDoubleValueForKey(groupaddress, value);
		log.debug("Homeserver response was " + response.getCode());
		if(response.getCode() == 0 ) {
			result = true;
		}
		return result;
	}

	private static String getActionIdForAppliance(Appliance appliance) {
		if(appliance.getIdSwitch() != null) {
			return appliance.getIdSwitch();
		} else if (appliance.getIdTrigger() != null) {
			return appliance.getIdTrigger();
		} else {
			log.error("Cannot determin the right group address for appliance " + appliance.getFriendlyName());
			return "";
		}
	}
	
	public static void updateAppliances() {
		log.info("Update Appliances in Trigger");
		Trigger.appliances = GiraBridge.discovery.appliances;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
