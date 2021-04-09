package com.sony.remoteControl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class Connection {

	public Environment env;
	URL url;
	HttpURLConnection conn;

	public Connection() {
		super();
		// TODO Auto-generated constructor stub
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
