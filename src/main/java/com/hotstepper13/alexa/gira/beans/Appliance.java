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
