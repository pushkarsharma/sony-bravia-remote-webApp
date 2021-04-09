package com.sony.remoteControl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.core.env.Environment;

public class Connection {

	private URL url;
	protected HttpURLConnection conn;
	protected Environment env;

	public Connection() {
		super();
	}

	public Connection(String uri, Environment env) throws IOException {
		super();
		this.env = env;
		this.url = new URL("http://" + env.getProperty("sony.tv.ip") + uri);
		this.conn = (HttpURLConnection) url.openConnection();
	}

	public HttpURLConnection getConn() {
		return conn;
	}
}
