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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotstepper13.alexa.configuration.Constants;

public class UDPSender {

	private static Logger log = LoggerFactory.getLogger(UDPSender.class);

	private InetAddress iaddress;
	private int port;
	private boolean broadcast;

	public UDPSender(InetAddress address, int port) {
		log.debug("Initialized UDP Sender with address " + address.getHostAddress() + " and port " + port);
		this.iaddress = address;
		this.port = port;
		this.broadcast = address.equals(Constants.MULTICAST_ADDRESS);

	}

	public void sendMessage(String message) {
		log.debug("Prepare to send message. iaddress: " + this.iaddress.getHostAddress());
		DatagramSocket c;
		try {
			// Open a random port to send the package
			c = new DatagramSocket(50000);
			c.setBroadcast(this.broadcast);
			byte[] sendData = message.getBytes();
			try {
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.iaddress, this.port);
				log.debug("Send packet to " + this.iaddress.getHostAddress() + ":" + this.port + ": " + message);
				c.send(sendPacket);
			} catch (Exception e) {
				log.error("Cannot send message", e);
			} finally {
				c.close();
			}
		} catch (Exception e) {
			log.error("Error during send message", e);
		}
	}

}
