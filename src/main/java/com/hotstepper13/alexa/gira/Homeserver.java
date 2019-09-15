package com.hotstepper13.alexa.gira;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.hotstepper13.alexa.gira.beans.HomeserverResponse;
import com.hotstepper13.alexa.gira.beans.HsConfig;

public class Homeserver {

	private final static Logger log = LoggerFactory.getLogger(Homeserver.class);
	
	private HsConfig hsConfig;
	
	public Homeserver (HsConfig config) {
		this.hsConfig = config;
	}
	
	
	// get status https://10.18.96.224/endpoints/call?key=CO@0_4_108&method=get&user=AmazonEcho&pw=Universe2019!
	// value HomeserverResponse.getData.getValue()
	
	// set value https://10.18.96.224/endpoints/call?key=CO@4_0_0&method=set&value=1&user=AmazonEcho&pw=Universe2019!
	
	
	
	public int getStatusForTrigger(String key) {
		return getValueForKey(key).getData().getValue().intValue();
	}
	
	public int getStatusForSwitch(String key) {
		return getValueForKey(key).getData().getValue().intValue();
	}
	
	public Double getStatusForPercentage(String key) {
		return getValueForKey(key).getData().getValue();
	}

	public HomeserverResponse getValueForKey(String key) {
		Gson gson = new Gson();
		return gson.fromJson(sendRequestToHomeserver("/endpoints/call?key=CO@" + key + "&method=get"), HomeserverResponse.class);
	}
	
	public HomeserverResponse setIntValueForKey(String key, int value) {
		return setDoubleValueForKey(key, new Double(value).doubleValue());
	}
	
	public HomeserverResponse setDoubleValueForKey(String key, double value) {
		Gson gson = new Gson();
		return gson.fromJson(sendRequestToHomeserver("/endpoints/call?key=CO@" + key + "&method=set&value=" + value), HomeserverResponse.class);
	}

	private String sendRequestToHomeserver(String requestPath) {
		String result = null;
		try {
			CloseableHttpClient httpclient = 
					HttpClients
					.custom()
					.setSSLSocketFactory(getSSLConnectionSocketFactory())
					.build();
			
			log.debug("https://" + hsConfig.getIp() + requestPath);
			HttpGet httpGet = new HttpGet("https://" + hsConfig.getIp() + requestPath + "&authorization=" + createAuthorization());
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					result = EntityUtils.toString(entity);
					log.debug("Received response: " + result);
				} else {
					log.error("Request not successful. StatusCode was " + response.getStatusLine().getStatusCode());
				}
			} finally {
				response.close();
			}
		} catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			log.error("Error executing the request.", e);
		}
		return result;
	}
	
	/**
	 * Create SSL Socket Factory to turn off certificate validation because of self signes ssl cert
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	private SSLConnectionSocketFactory getSSLConnectionSocketFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy() {
			public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				return true;
			}
		});
		@SuppressWarnings("deprecation")
		HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(), hostnameVerifier);
		return sslsf;
	}
	
	/**
	 * Homeserver relies on preemptive basic auth or the encoded username:password as parameter
	 * @return
	 */
	private String createAuthorization() {
		String auth = hsConfig.getUsername() + ":" + hsConfig.getPassword();
		return Base64.getEncoder().encodeToString(auth.getBytes());
	}
	
}
