package com.sony.remoteControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class RemoteController {

	@Autowired
	public Environment env;

	ObjectWriter objectWriter;

	@Bean
	public void objectWritedInitiator() {
		this.objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
	}

	@RequestMapping(method = RequestMethod.POST, path = "/volume/{offset}")
	public String volumeChange(@PathVariable String offset) throws IOException {
		ConnectionResource connResource = new ConnectionResource("/sony/audio", env);
		connResource.setRequestMethod("POST");

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json; charset=UTF-8");
		headers.put("X-Auth-PSK", env.getProperty("PSK"));
		headers.put("Accept", "application/json");
		connResource.setHeaders(headers);
		connResource.setDoOutput(true);

		RequestBody requestBody = new RequestBody();
		requestBody.setMethod("setAudioVolume");
		requestBody.setId(1);
		List<Map<String, String>> volumeBodyList = new ArrayList<Map<String, String>>();
		Map<String, String> volumeBodyMap = new HashMap<String, String>();
		volumeBodyMap.put("volume", offset);
		volumeBodyMap.put("ui", "on");
		volumeBodyMap.put("target", "speaker");
		volumeBodyList.add(volumeBodyMap);
		requestBody.setParams(volumeBodyList);
		requestBody.setVersion("1.0");

		String requestBodyJson = objectWriter.writeValueAsString(requestBody);

		OutputStream os = connResource.getConnectionOutputStream();
		byte[] input = requestBodyJson.getBytes("utf-8");
		os.write(input, 0, input.length);

		StringBuilder response = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(connResource.getInputStream(), "utf-8"));
		String responseLine = null;
		while ((responseLine = br.readLine()) != null) {
			response.append(responseLine.trim());
		}
		System.out.println(response.toString());

		return response.toString();
	}
}
