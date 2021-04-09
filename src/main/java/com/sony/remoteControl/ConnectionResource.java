package com.sony.remoteControl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class ConnectionResource extends Connection {

	@Autowired
	public Environment env;

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

}
