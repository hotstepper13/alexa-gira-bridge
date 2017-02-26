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

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

	private static Logger log = LoggerFactory.getLogger(Util.class);

	
	/**
	 * Taken from https://github.com/bwssytems/ha-bridge and slightly modified
	 * @param addr
	 * @return
	 */
	public static String getMacAddress(String address) {
		InetAddress ip;
		StringBuilder sb = new StringBuilder();
		try {
			ip = InetAddress.getByName(address);
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));		
			}
		} catch (UnknownHostException e) {
			sb.append("00:00:88:00:bb:ee");
		} catch (SocketException e){
			sb.append("00:00:88:00:bb:ee");
		} catch (Exception e){
			sb.append("00:00:88:00:bb:ee");
		}
		return sb.toString();
	}

	public static String getSNUUIDFromMac(String address) {
    	StringTokenizer st = new StringTokenizer(Util.getMacAddress(address), ":");
    	String bridgeUUID = "";
    	while(st.hasMoreTokens()) {
    		bridgeUUID = bridgeUUID + st.nextToken();
    	}
    	bridgeUUID = bridgeUUID.toLowerCase();
		return bridgeUUID.toLowerCase();
	}

	public static String getHueBridgeIdFromMac(String address) {
		String cleanMac = Util.getSNUUIDFromMac(address);
    	String bridgeId = cleanMac.substring(0, 6) + "FFFE" + cleanMac.substring(6);
		return bridgeId.toUpperCase();
	}
	
	public static String triggerHttpGetWithCustomSSL(String requestUrl) {
    String result = null;
		try {
  		SSLContextBuilder builder = new SSLContextBuilder();
    	builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
	
	    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
	    CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
	
	    HttpGet httpGet = new HttpGet(requestUrl);
	    CloseableHttpResponse response = httpclient.execute(httpGet);
	    try {
	    	if( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
	        HttpEntity entity = response.getEntity();
	        result = EntityUtils.toString(entity);
	        log.debug("Received response: " + result);
	    	} else {
	    		log.error("Request not successful. StatusCode was " + response.getStatusLine().getStatusCode());
	    	}
	    }
	    finally {
        response.close();
	    }
		} catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			log.error("Error executing the request.", e);
		}
		return result;
	}

	public static String triggerHttpGet(String requestUrl) {
		String result = null;
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(requestUrl);
		try {
			HttpResponse response = client.execute(request);
    	if( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
        HttpEntity entity = response.getEntity();
        result = EntityUtils.toString(entity);
        log.debug("Received response: " + result);
    	} else {
    		log.error("Request not successful. StatusCode was " + response.getStatusLine().getStatusCode());
    	}
		} catch (IOException e) {
			log.error("Error executing the request.", e);
		}
		return result;
	}
	
}
