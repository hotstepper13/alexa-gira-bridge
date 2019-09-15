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
package com.hotstepper13.alexa.network.upnp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotstepper13.alexa.GiraBridge;
import com.hotstepper13.alexa.configuration.Constants;
import com.hotstepper13.alexa.network.UDPSender;
import com.hotstepper13.alexa.network.Util;

public class UpnpServer extends Thread {

	private final static Logger log = LoggerFactory.getLogger(UpnpServer.class);
	private boolean terminated = false;
	private MulticastSocket msocket = null;
	private String address;
	private int port;

	public UpnpServer(String address, int port) {
		this.address = address;
		this.port = port;
	}

	public void run() {
		try {
			InetAddress multicastAddress = InetAddress.getByName(Constants.MULTICAST_ADDRESS);
			this.msocket = new MulticastSocket(Constants.SSDP_PORT);
			this.msocket.setReuseAddress(true);
			this.msocket.joinGroup(multicastAddress);
			//this.msocket.setInterface(InetAddress.getByName(address));
			
			byte[] buffer = new byte[2048];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			while (!this.terminated) {
				this.msocket.receive(packet);
				String message = new String(packet.getData(), "UTF-8");
				if (message.contains("M-SEARCH")) {
					log.debug("Received Search Request from " + packet.getSocketAddress().toString() + ":\n" + message);
					if (this.checkSearch(message) && packet.getPort() == 50000) {
						log.info("DiscoveryResponse needed for " + packet.getAddress() + ":" + packet.getPort());
						this.sendDiscoveryResponse(packet.getAddress(), packet.getPort());
					}
				}
				// Reset the length of the packet before reusing it.
				packet.setLength(buffer.length);
			}
		} catch (IOException e) {
			log.error("Error while listening for UDP packages", e);
		} finally {
			this.msocket.close();
		}
	}


	public void terminate() {
		this.terminated = true;
	}

	private boolean checkSearch(String message) {
		if (message.contains(Constants.SSDP_DISCOVER_STRING)){// && (message.contains(Constants.SSDP_DISCOVER_URN) || message.contains(Constants.SSDP_DISCOVER_URN_NEW))) {
			log.debug("Found potential Alexa Discovery Request");
			return true;
		}
		return false;
	}

	private void sendDiscoveryResponse(InetAddress requestAddress, int requestPort) {
		Object[] response_params;
		if (GiraBridge.config.getBridgeConfig().getIp().equals("")) {
			response_params = new Object[] { this.address, "" + this.port, Util.getHueBridgeIdFromMac(this.address),Util.getSNUUIDFromMac(this.address) };
		} else {
			response_params = new Object[] { GiraBridge.config.getBridgeConfig().getIp(), "" + this.port, Util.getHueBridgeIdFromMac(this.address),Util.getSNUUIDFromMac(this.address) };
		}
		UDPSender us = new UDPSender(requestAddress.getHostAddress(), requestPort);
		us.sendMessage(MessageFormat.format(Constants.responseTemplate1, response_params));
		us.sendMessage(MessageFormat.format(Constants.responseTemplate2, response_params));
		us.sendMessage(MessageFormat.format(Constants.responseTemplate3, response_params));
		// us.sendMessage(MessageFormat.format(Constants.responseTemplate1,
		// response_params);

	}

}
