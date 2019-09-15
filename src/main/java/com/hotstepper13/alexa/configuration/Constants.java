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
package com.hotstepper13.alexa.configuration;

public class Constants {

	public final static String DISCOVERY_URL = "{0}:{1}/discovery?accessToken={2}&messageId=xyz";
	public final static String ONOFFVALUE_URL = "{0}:{1}/control?applianceId={2}&request=GetOnOffValueRequest&accessToken={3}&messageId=xyz";
	public final static String PERCENTVALUE_URL = "{0}:{1}/control?applianceId={2}&request=GetPercentValueRequest&accessToken={3}&messageId=xyz";
	public final static int SSDP_PORT = 1900;
	public final static String MULTICAST_ADDRESS = "239.255.255.250";
	public final static String SSDP_DISCOVER_STRING = "ssdp:discover";
	public final static String SSDP_DISCOVER_URN = "urn:schemas-upnp-org:device:basic:1";
	public final static String SSDP_DISCOVER_URN_NEW = "urn:schemas-upnp-org:service:RenderingControl:1";
	

	/**
	 * The following constants were taken from
	 * https://github.com/bwssytems/ha-bridge and slightly modified
	 */
	public final static String HUB_VERSION = "01036659";
	public final static String API_VERSION = "1.15.0";
	public final static String MODEL_ID = "BSB002";
	public final static String UUID_PREFIX = "2f402f80-da50-11e1-9b23-";

	public final static String responseTemplate1 = "HTTP/1.1 200 OK\n" + "HOST: " + Constants.MULTICAST_ADDRESS + ":"
			+ Constants.SSDP_PORT + "\n" + "CACHE-CONTROL: max-age=100\n" + "EXT:\n"
			+ "LOCATION: http://{0}:{1}/description.xml\n" + "SERVER: Linux/3.14.0 UPnP/1.0 IpBridge/"
			+ Constants.API_VERSION + "\n" + "hue-bridgeid: {2}\n" + "ST: upnp:rootdevice\n" + "USN: uuid:"
			+ Constants.UUID_PREFIX + "{3}::upnp:rootdevice\n\n";
	public final static String responseTemplate2 = "HTTP/1.1 200 OK\n" + "HOST: " + Constants.MULTICAST_ADDRESS + ":"
			+ Constants.SSDP_PORT + "\n" + "CACHE-CONTROL: max-age=100\n" + "EXT:\n"
			+ "LOCATION: http://{0}:{1}/description.xml\n" + "SERVER: Linux/3.14.0 UPnP/1.0 IpBridge/"
			+ Constants.API_VERSION + "\n" + "hue-bridgeid: {2}\n" + "ST: uuid:" + Constants.UUID_PREFIX + "{3}\n"
			+ "USN: uuid:" + Constants.UUID_PREFIX + "{3}\n\n";
	public final static String responseTemplate3 = "HTTP/1.1 200 OK\n" + "HOST: " + Constants.MULTICAST_ADDRESS + ":"
			+ Constants.SSDP_PORT + "\n" + "CACHE-CONTROL: max-age=100\n" + "EXT:\n"
			+ "LOCATION: http://{0}:{1}/description.xml\n" + "SERVER: Linux/3.14.0 UPnP/1.0 IpBridge/"
			+ Constants.API_VERSION + "\n" + "hue-bridgeid: {2}\n" + "ST: urn:schemas-upnp-org:device:basic:1\n"
			+ "USN: uuid:" + Constants.UUID_PREFIX + "{3}\n\n";

	public final static String notifyTemplate = "NOTIFY * HTTP/1.1\n" + "HOST: " + Constants.MULTICAST_ADDRESS + ":"
			+ Constants.SSDP_PORT + "\n" + "CACHE-CONTROL: max-age=100\n" + "LOCATION: http://{0}:{1}/description.xml\n"
			+ "SERVER: Linux/3.14.0 UPnP/1.0 IpBridge/" + Constants.API_VERSION + "\n" + "NTS: ssdp:alive\n"
			+ "hue-bridgeid: {2}\n" + "NT: uuid:" + Constants.UUID_PREFIX + "{3}\n" + "USN: uuid:" + Constants.UUID_PREFIX
			+ "{3}\n\n";

	public final static String HUE_DESCRIPTION = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
			+ "<root xmlns=\"urn:schemas-upnp-org:device-1-0\">\n" + "<specVersion>\n" + "<major>1</major>\n"
			+ "<minor>0</minor>\n" + "</specVersion>\n" + "<URLBase>http://{0}:{1}/</URLBase>\n" + "<device>\n"
			+ "<deviceType>urn:schemas-upnp-org:device:Basic:1</deviceType>\n"
			+ "<friendlyName>Philips hue ({0})</friendlyName>\n"
			+ "<manufacturer>Royal Philips Electronics</manufacturer>\n"
			+ "<manufacturerURL>http://www.philips.com</manufacturerURL>\n"
			+ "<modelDescription>Philips hue Personal Wireless Lighting</modelDescription>\n"
			+ "<modelName>Philips hue bridge 2015</modelName>\n" + "<modelNumber>BSB002</modelNumber>\n"
			+ "<modelURL>http://www.meethue.com</modelURL>\n" + "<serialNumber>20cf3019ade2</serialNumber>\n"
			+ "<UDN>uuid:2f402f80-da50-11e1-9b23-20cf3019ade2</UDN>\n" + "<serviceList>\n" + "<service>\n"
			+ "<serviceType>(null)</serviceType>\n" + "<serviceId>(null)</serviceId>\n"
			+ "<controlURL>(null)</controlURL>\n" + "<eventSubURL>(null)</eventSubURL>\n"
			+ "<SCPDURL>(null)</SCPDURL>\n" + "</service>\n" + "</serviceList>\n"
			+ "<presentationURL>index.html</presentationURL>\n" + "<iconList>\n" + "<icon>\n"
			+ "<mimetype>image/png</mimetype>\n" + "<height>48</height>\n" + "<width>48</width>\n"
			+ "<depth>24</depth>\n" + "<url>hue_logo_0.png</url>\n" + "</icon>\n" + "<icon>\n"
			+ "<mimetype>image/png</mimetype>\n" + "<height>120</height>\n" + "<width>120</width>\n"
			+ "<depth>24</depth>\n" + "<url>hue_logo_3.png</url>\n" + "</icon>\n" + "</iconList>\n" + "</device>\n"
			+ "</root>\n";

	/*
	 * end "stolen" content
	 */

	/*
	 * " 	<serviceList>" + " 		<service>" +
	 * " 			<serviceType>urn:schemas-upnp-org:service: SwitchPower:1</serviceType>"
	 * + " 			<serviceId>urn:upnp-org:serviceId: SwitchPower:1</serviceId>" +
	 * " 			<controlURL>URL for control</controlURL>" + " 		</service>" +
	 * " 		<service>" +
	 * " 			<serviceType>urn:schemas-upnp-org:service:Dimming:1</serviceType>" +
	 * " 			<serviceId>urn:upnp-org:serviceId:Dimming:1</serviceId>" +
	 * " 			<controlURL>URL for control</controlURL>" + " 		</service>" +
	 * "		</serviceList>" +
	 * 
	 */

	public final static String TCP_RESPONSE_HEADER = "HTTP/1.1 200 OK\n" +
	// "CONTENT-LENGTH: {0}\n" +
			"Transfer-Encoding: chunked\n" + "CONTENT-TYPE: application/xml; charset=utf-8\n" + "DATE: {1}\n"
			+ "SERVER: Jetty(9.3.2.v20150730)\n\n";

}
