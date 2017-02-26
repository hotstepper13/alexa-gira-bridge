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

	public final static String DISCOVERY_URL = "https://{0}:{1}/discovery?accessToken={2}&messageId=xyz";
	public final static String ONOFFVALUE_URL = "https://{0}:{1}/control?applianceId={2}&request=GetOnOffValueRequest&accessToken={3}&messageId=xyz";
	public final static String PERCENTVALUE_URL = "https://{0}:{1}/control?applianceId={2}&request=GetPercentValueRequest&accessToken={3}&messageId=xyz";
	public final static int SSDP_PORT = 1900;
	public final static String MULTICAST_ADDRESS = "239.255.255.250";
	public final static String SSDP_DISCOVER_STRING = "ssdp:discover";
	public final static String SSDP_DISCOVER_URN = "urn:schemas-upnp-org:device:basic:1";
	public final static String GIRA_REQUEST_TEMPLATE = "https://{0}:{1}/control?applianceId={2}&request={3}&accessToken={4}&messageId=xyz";
	public final static String GIRA_REQUEST_VALUE_TEMPLATE = "https://{0}:{1}/control?applianceId={2}&request={3}&accessToken={4}&value={5}&messageId=xyz";
	
	/**
	 * The following constants were taken from https://github.com/bwssytems/ha-bridge
	 * and slightly modified
	 */
	public final static String HUB_VERSION = "01036659";
	public final static String API_VERSION = "1.15.0";
	public final static String MODEL_ID = "BSB002";
	public final static String UUID_PREFIX = "2f402f80-da50-11e1-9b23-";
 
	public final static String responseTemplate1 = "HTTP/1.1 200 OK\r\n" +
			"HOST: " + Constants.MULTICAST_ADDRESS + ":" + Constants.SSDP_PORT + "\r\n" +
			"CACHE-CONTROL: max-age=100\r\n" +
			"EXT:\r\n" +
			"LOCATION: http://{0}:{1}/description.xml\r\n" +
			"SERVER: Linux/3.14.0 UPnP/1.0 IpBridge/" + Constants.API_VERSION + "\r\n" + 
			"hue-bridgeid: {2}\r\n" +
			"ST: upnp:rootdevice\r\n" +
			"USN: uuid:" + Constants.UUID_PREFIX + "{3}::upnp:rootdevice\r\n\r\n";
	public final static String responseTemplate2 = "HTTP/1.1 200 OK\r\n" +
			"HOST: " + Constants.MULTICAST_ADDRESS + ":" + Constants.SSDP_PORT + "\r\n" +
			"CACHE-CONTROL: max-age=100\r\n" +
			"EXT:\r\n" +
			"LOCATION: http://{0}:{1}/description.xml\r\n" +
			"SERVER: Linux/3.14.0 UPnP/1.0 IpBridge/" + Constants.API_VERSION + "\r\n" + 
			"hue-bridgeid: {2}\r\n" +
			"ST: uuid:" + Constants.UUID_PREFIX + "{3}\r\n" +
			"USN: uuid:" + Constants.UUID_PREFIX + "{3}\r\n\r\n";
	public final static String responseTemplate3 = "HTTP/1.1 200 OK\r\n" +
			"HOST: " + Constants.MULTICAST_ADDRESS + ":" + Constants.SSDP_PORT + "\r\n" +
			"CACHE-CONTROL: max-age=100\r\n" +
			"EXT:\r\n" +
			"LOCATION: http://{0}:{1}/description.xml\r\n" +
			"SERVER: Linux/3.14.0 UPnP/1.0 IpBridge/" + Constants.API_VERSION + "\r\n" + 
			"hue-bridgeid: {2}\r\n" +
			"ST: urn:schemas-upnp-org:device:basic:1\r\n" +
			"USN: uuid:" + Constants.UUID_PREFIX + "{3}\r\n\r\n";

	public final static String notifyTemplate = "NOTIFY * HTTP/1.1\r\n" +
			"HOST: " + Constants.MULTICAST_ADDRESS + ":" + Constants.SSDP_PORT + "\r\n" +
			"CACHE-CONTROL: max-age=100\r\n" +
			"LOCATION: http://{0}:{1}/description.xml\r\n" +
			"SERVER: Linux/3.14.0 UPnP/1.0 IpBridge/" + Constants.API_VERSION + "\r\n" + 
			"NTS: ssdp:alive\r\n" +
			"hue-bridgeid: {2}\r\n" +
			"NT: uuid:" + Constants.UUID_PREFIX + "{3}\r\n" +
			"USN: uuid:" + Constants.UUID_PREFIX + "{3}\r\n\r\n";
	
	public final static String HUE_DESCRIPTION = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" +
		"<root xmlns=\"urn:schemas-upnp-org:device-1-0\">\r\n" + 
		"<specVersion>\r\n" +
		"<major>1</major>\r\n" +
		"<minor>0</minor>\r\n" +
		"</specVersion>\r\n" +
		"<URLBase>http://{0}:{1}/</URLBase>\r\n" +
		"<device>\r\n" +
		"<deviceType>urn:schemas-upnp-org:device:Basic:1</deviceType>\r\n" +
		"<friendlyName>Philips hue ({0})</friendlyName>\r\n" +
		"<manufacturer>Royal Philips Electronics</manufacturer>\r\n" +
		"<manufacturerURL>http://www.philips.com</manufacturerURL>\r\n" +
		"<modelDescription>Philips hue Personal Wireless Lighting</modelDescription>\r\n" +
		"<modelName>Philips hue bridge 2015</modelName>\r\n" +
		"<modelNumber>BSB002</modelNumber>\r\n" +
		"<modelURL>http://www.meethue.com</modelURL>\r\n" +
		"<serialNumber>20cf3019ade2</serialNumber>\r\n" +
		"<UDN>uuid:2f402f80-da50-11e1-9b23-20cf3019ade2</UDN>\r\n" +
		"<serviceList>\r\n" +
		"<service>\r\n" +
		"<serviceType>(null)</serviceType>\r\n" +
		"<serviceId>(null)</serviceId>\r\n" +
		"<controlURL>(null)</controlURL>\r\n" +
		"<eventSubURL>(null)</eventSubURL>\r\n" +
		"<SCPDURL>(null)</SCPDURL>\r\n" +
		"</service>\r\n" +
		"</serviceList>\r\n" +
		"<presentationURL>index.html</presentationURL>\r\n" +
		"<iconList>\r\n" +
		"<icon>\r\n" +
		"<mimetype>image/png</mimetype>\r\n" +
		"<height>48</height>\r\n" +
		"<width>48</width>\r\n" +
		"<depth>24</depth>\r\n" +
		"<url>hue_logo_0.png</url>\r\n" +
		"</icon>\r\n" +
		"<icon>\r\n" +
		"<mimetype>image/png</mimetype>\r\n" +
		"<height>120</height>\r\n" +
		"<width>120</width>\r\n" +
		"<depth>24</depth>\r\n" +
		"<url>hue_logo_3.png</url>\r\n" +
		"</icon>\r\n" +
		"</iconList>\r\n" +
		"</device>\r\n" +
		"</root>\r\n";
	
	/*
	 * end "stolen" content
	 */
	
	

/*
 * 			" 	<serviceList>" +
			" 		<service>" +
			" 			<serviceType>urn:schemas-upnp-org:service: SwitchPower:1</serviceType>" +
			" 			<serviceId>urn:upnp-org:serviceId: SwitchPower:1</serviceId>" +
			" 			<controlURL>URL for control</controlURL>" +
			" 		</service>" +
			" 		<service>" +
			" 			<serviceType>urn:schemas-upnp-org:service:Dimming:1</serviceType>" +
			" 			<serviceId>urn:upnp-org:serviceId:Dimming:1</serviceId>" +
			" 			<controlURL>URL for control</controlURL>" +
			" 		</service>" +
			"		</serviceList>" +
	
 */

	public final static String TCP_RESPONSE_HEADER = "HTTP/1.1 200 OK\r\n" +
		//"CONTENT-LENGTH: {0}\r\n" +
		"Transfer-Encoding: chunked\r\n" +
		"CONTENT-TYPE: application/xml; charset=utf-8\r\n" +
		"DATE: {1}\r\n" +
		"SERVER: Jetty(9.3.2.v20150730)\r\n" + //Unspecified, UPnP/1.0, Unspecified
		"\r\n";
	
}
