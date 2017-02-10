package com.hotstepper13.alexa.gira.beans;

import java.util.List;

public class Appliance {

	public enum Actions {
    turnOn,turnOff,incrementPercentage,decrementPercentage,setPercentage
	}
	
	private String friendlyName;
	private String applianceId;
	private List<Actions> actions;
	
	
  public String getFriendlyName() {
		return friendlyName;
	}

	public String getApplianceId() {
		return applianceId;
	}

	public List<Actions> getActions() {
		return actions;
	}

	@Override
  public String toString() {
      return friendlyName + " - " + applianceId + " - " + actions;
  }	

}
