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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotstepper13.alexa.GiraBridge;
import com.hotstepper13.alexa.gira.beans.Appliance;
import com.hotstepper13.alexa.gira.beans.Item;
import com.hotstepper13.alexa.gira.beans.Room;

public class Discovery {

	private final static Logger log = LoggerFactory.getLogger(Discovery.class);

	public List<Appliance> appliances = new ArrayList<Appliance>();

	/**
	 * Custom Module is no longer required as configuration will be read from file and
	 * Homeserver offers a native rest api
	 */
	public Discovery() {
		appliances.add(new Appliance("Discovery", "0", Arrays.asList(Appliance.Actions.turnOn)));
		convertItemsToAppliances();
		reviewDiscoveryResponse();

	}

	private void convertItemsToAppliances() {
		Iterator<Room> rooms = GiraBridge.config.getRooms().iterator();
		while(rooms.hasNext()) {
			Room room = rooms.next();
			Iterator<Item> items = room.getItems().iterator();
			while(items.hasNext()) {
				Appliance appliance = new Appliance();
				Item item = items.next();
				
				appliance.setFriendlyName(room.getRoomName() + " " + item.getItemName());
				appliance.setApplianceId(room.getId() + "_" + item.getId());
				
				List<Appliance.Actions> actions = new ArrayList<Appliance.Actions>();
				
				if(item.getIdTrigger() != null) {
					actions.add(Appliance.Actions.turnOn);
					appliance.setIdTrigger(item.getIdTrigger());
				}
				
				if(item.getIdSwitch() != null) {
					actions.add(Appliance.Actions.turnOn);
					actions.add(Appliance.Actions.turnOff);
					appliance.setIdSwitch(item.getIdSwitch());
				}
				
				if(item.getIdPercentage() != null) {
					actions.add(Appliance.Actions.setPercentage);
					actions.add(Appliance.Actions.decrementPercentage);
					actions.add(Appliance.Actions.incrementPercentage);
					appliance.setIdPercentage(item.getIdPercentage());
				}
				appliance.setActions(actions);
				appliances.add(appliance);
			}
		}
		
		
		
	}
	

	private void reviewDiscoveryResponse() {
		log.info("Discoverered " + this.appliances.size() + " items from configuration");
		Iterator<Appliance> it = appliances.iterator();
		int i = 0;
		while (it.hasNext()) {
			Appliance item = it.next();
			log.info(item.getFriendlyName() + " with id " + item.getApplianceId() + " (ListItem: " + i + ") has the following actions: ");
			Iterator<Appliance.Actions> actions = item.getActions().iterator();
			while (actions.hasNext()) {
				Appliance.Actions action = (Appliance.Actions) actions.next();
				if (action != null) {
					if (checkSupportedActions(action)) {
						log.info("  * " + action.name());
					} else {
						log.warn("  * " + action.name() + " (currently not supported)");
					}
				} else {
					log.warn("  * unknown action (not supported)");
				}
			}
			i++;
		}
	}

	private boolean checkSupportedActions(Appliance.Actions action) {
		if (action.equals(Appliance.Actions.setPercentage)) {
			return true;
		} else if (action.equals(Appliance.Actions.turnOff)) {
			return true;
		} else if (action.equals(Appliance.Actions.turnOn)) {
			return true;
		} else if (action.equals(Appliance.Actions.incrementPercentage)) {
			return true;
		} else if (action.equals(Appliance.Actions.decrementPercentage)) {
			return true;
		}
		return false;
	}

}
