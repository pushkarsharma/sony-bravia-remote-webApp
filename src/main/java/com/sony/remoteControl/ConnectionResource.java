package com.sony.remoteControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.Environment;

public class ConnectionResource extends Connection {

	public ConnectionResource(String uri, Environment env) throws IOException {
		super(uri, env);
	}

	public void setRequestMethod(String requestMethod) throws ProtocolException {
		this.conn.setRequestMethod(requestMethod);
	}

	public void setHeaders(Map<String, String> headers) {
		for (String key : headers.keySet()) {
			this.conn.setRequestProperty(key, headers.get(key));
		}
	}

	public void setDoOutput(boolean flag) {
		this.conn.setDoOutput(flag);
	}

	public OutputStream getConnectionOutputStream() throws IOException {
		return this.conn.getOutputStream();
	}

	public InputStream getInputStream() throws IOException {
		return this.conn.getInputStream();
	}

	public void setBaseRestHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json; charset=UTF-8");
		headers.put("X-Auth-PSK", env.getProperty("PSK"));
		headers.put("Accept", "application/json");
		setHeaders(headers);
	}

	public void setBaseXMLHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "text/xml; charset=UTF-8");
		headers.put("SOAPACTION", env.getProperty("SOAPACTION"));
		headers.put("X-Auth-PSK", env.getProperty("PSK"));
		setHeaders(headers);
	}

	public String serviceCall(String payload) throws UnsupportedEncodingException, IOException {
		OutputStream os = getConnectionOutputStream();
		byte[] input = payload.getBytes("utf-8");
		os.write(input, 0, input.length);

		StringBuilder response = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(getInputStream(), "utf-8"));
		String responseLine = null;
		while ((responseLine = br.readLine()) != null) {
			response.append(responseLine.trim());
		}
		return response.toString();
	}

}
