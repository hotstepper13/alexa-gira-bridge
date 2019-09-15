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
package com.hotstepper13.alexa.gira.beans;

import java.util.List;

public class Appliance {

	public enum Actions {
		turnOn, turnOff, incrementPercentage, decrementPercentage, setPercentage, incrementTargetTemperature, decrementTargetTemperature, setTargetTemperature
	}

	private String friendlyName;
	private String applianceId;
	private List<Actions> actions;
	private int hueId;
	private boolean isOn = false;
	private int bri = 0;
	private String idSwitch;
	private String idTrigger;
	private String idPercentage;

	public Appliance() {
		
	}
	
	public Appliance(String friendlyName, String applianceId, List<Actions> actions) {
		this.friendlyName = friendlyName;
		this.applianceId = applianceId;
		this.actions = actions;
	}
	
	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public void setApplianceId(String applianceId) {
		this.applianceId = applianceId;
	}

	public void setActions(List<Actions> actions) {
		this.actions = actions;
	}

	public int getHueId() {
		return hueId;
	}

	public void setHueId(int hueId) {
		this.hueId = hueId;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public String getApplianceId() {
		return applianceId;
	}

	public List<Actions> getActions() {
		return actions;
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
		this.bri = 255;
	}

	public int getBri() {
		return bri;
	}

	public void setBri(int bri) {
		this.bri = bri;
	}

	@Override
	public String toString() {
		return friendlyName + " - " + applianceId + " - " + actions;
	}

	public String getIdSwitch() {
		return idSwitch;
	}

	public void setIdSwitch(String idSwitch) {
		this.idSwitch = idSwitch;
	}

	public String getIdTrigger() {
		return idTrigger;
	}

	public void setIdTrigger(String idTrigger) {
		this.idTrigger = idTrigger;
	}

	public String getIdPercentage() {
		return idPercentage;
	}

	public void setIdPercentage(String idPercentage) {
		this.idPercentage = idPercentage;
	}

	public String getHueDevice() {
		String result = "{\r\n" + "\t\"state\": {\r\n" + "\t\t\"on\": " + (this.isOn ? "true" : "false") + ",\r\n"
				+ "\t\t\"bri\": " + this.bri + ",\r\n" + "\t\t\"hue\": 0,\r\n" + "\t\t\"sat\": 0,\r\n"
				+ "\t\t\"effect\": \"none\",\r\n" + "\t\t\"ct\": 0,\r\n" + "\t\t\"alert\": \"none\",\r\n"
				+ "\t\t\"reachable\": true\r\n" + "\t},\r\n" + "\t\"type\": \"Dimmable light\",\r\n" + "\t\"name\": \""
				+ this.getFriendlyName() + "\",\r\n" + "\t\"modelid\": \"LWB004\",\r\n"
				+ "\t\"manufacturername\":\"Philips\",\r\n" + "\t\"uniqueid\": \"00:17:88:5E:D3:"
				+ this.applianceId.replaceAll("_", "-") + "\",\r\n" + "\t\"swversion\": \"66012040\"\r\n" + "}";
		return result;
	}

}
