package com.hotstepper13.alexa.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotstepper13.alexa.configuration.Constants;

public class UDPSender {

	private static Logger log = LoggerFactory.getLogger(UDPSender.class);
	
	private InetAddress iaddress;
	private int port;
	private boolean broadcast;
	
	public UDPSender(String address, int port) {
		try {
			this.iaddress = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			log.error("Cannot find ip", e);
		}
		this.port = port;
		this.broadcast = address.equals(Constants.MULTICAST_ADDRESS);
	
	}
	
	public void sendMessage(String message) {
		DatagramSocket c;
		try {
			//Open a random port to send the package
			c = new DatagramSocket(50000);
			c.setBroadcast(this.broadcast);
			byte[] sendData = message.getBytes();
			try {
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, this.iaddress, this.port);
				log.debug("Send packet to " + this.iaddress + ":" + this.port + ": " + message);
				c.send(sendPacket);
			} catch (Exception e) {
				log.error("Cannot send message", e);
			} finally {
				c.close();
			}
			// Broadcast the message over all the network interfaces
//			Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
//			while (interfaces.hasMoreElements()) {
//				NetworkInterface networkInterface = (NetworkInterface)interfaces.nextElement();
//				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
//					continue; // Don't want to broadcast to the loopback interface
//				}
//				for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
//					InetAddress broadcast = interfaceAddress.getBroadcast();
//					if (broadcast == null) {
//						continue;
//					}
//					try {
//						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
//						c.send(sendPacket);
//					} catch (Exception e) {
//					}
//					System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
//				}
//			}
		} catch (Exception e) {
			log.error("Error during send message", e);
		}
	}
	
}
