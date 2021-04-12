package com.sony.remoteControl;

import java.io.IOException;
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
	ObjectMapper objectMapper;

	@Bean
	public void jsonInitiator() {
		this.objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
		this.objectMapper = new ObjectMapper();
	}

	@RequestMapping(method = RequestMethod.POST, path = "/volume/{offset}")
	public String volumeChange(@PathVariable String offset) throws IOException {
		ConnectionResource connResource = new ConnectionResource("/sony/audio", env);
		connResource.setRequestMethod("POST");
		connResource.setBaseRestHeaders();
		connResource.setDoOutput(true);

		String requestBodyJsonPayload;
		RequestBody requestBody = new RequestBody();
		List<Map<String, Object>> volumeBodyList = new ArrayList<Map<String, Object>>();
		Map<String, Object> volumeBodyMap = new HashMap<String, Object>();

		requestBody.setMethod("setAudioVolume");
		requestBody.setId(98);
		volumeBodyMap.put("volume", offset);
		volumeBodyMap.put("ui", "on");
		volumeBodyMap.put("target", "speaker");
		volumeBodyList.add(volumeBodyMap);
		requestBody.setParams(volumeBodyList);
		requestBody.setVersion("1.0");
		requestBodyJsonPayload = objectWriter.writeValueAsString(requestBody);

		return connResource.serviceCall(requestBodyJsonPayload);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/navigate/{direction}")
	public String navigate(@PathVariable String direction) throws IOException {
		ConnectionResource connResource = new ConnectionResource("/sony/ircc", env);
		connResource.setRequestMethod("POST");
		connResource.setBaseXMLHeaders();
		connResource.setDoOutput(true);

		String XMLPayload = String.format(env.getProperty("xml.payload"), env.getProperty(direction));
		return connResource.serviceCall(XMLPayload);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/confirm")
	public String confirm() throws IOException {
		ConnectionResource connResource = new ConnectionResource("/sony/ircc", env);
		connResource.setRequestMethod("POST");
		connResource.setBaseXMLHeaders();
		connResource.setDoOutput(true);

		String XMLPayload = String.format(env.getProperty("xml.payload"), env.getProperty("confirm"));
		return connResource.serviceCall(XMLPayload);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/home")
	public String home() throws IOException {
		ConnectionResource connResource = new ConnectionResource("/sony/ircc", env);
		connResource.setRequestMethod("POST");
		connResource.setBaseXMLHeaders();
		connResource.setDoOutput(true);

		String XMLPayload = String.format(env.getProperty("xml.payload"), env.getProperty("home"));
		return connResource.serviceCall(XMLPayload);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/power")
	public String power() throws IOException {
		ConnectionResource connResource = new ConnectionResource("/sony/system", env);
		connResource.setRequestMethod("GET");
		connResource.setBaseRestHeaders();
		connResource.setDoOutput(true);

		String requestBodyJsonPayload;
		RequestBody requestBody = new RequestBody();
		List<Map<String, Object>> volumeBodyList = new ArrayList<Map<String, Object>>();
		Map<String, Object> volumeBodyMap = new HashMap<String, Object>();

		requestBody.setMethod("getPowerStatus");
		requestBody.setId(50);
		requestBody.setParams(volumeBodyList);
		requestBody.setVersion("1.0");
		requestBodyJsonPayload = objectWriter.writeValueAsString(requestBody);

		String response = connResource.serviceCall(requestBodyJsonPayload);
		connResource.disconnect();

		ResponseBody responseBody = objectMapper.readValue(response, ResponseBody.class);
		connResource = new ConnectionResource("/sony/system", env);
		connResource.setRequestMethod("POST");
		connResource.setBaseRestHeaders();
		connResource.setDoOutput(true);

		requestBody.setMethod("setPowerStatus");
		requestBody.setId(55);
		if (responseBody.getResult().get(0).get("status").equals("active"))
			volumeBodyMap.put("status", false);
		else
			volumeBodyMap.put("status", true);
		volumeBodyList.add(volumeBodyMap);
		requestBody.setParams(volumeBodyList);
		requestBody.setVersion("1.0");
		requestBodyJsonPayload = objectWriter.writeValueAsString(requestBody);

		return connResource.serviceCall(requestBodyJsonPayload);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/reboot")
	public String reboot() throws IOException {
		ConnectionResource connResource = new ConnectionResource("/sony/system", env);
		connResource.setRequestMethod("POST");
		connResource.setBaseRestHeaders();
		connResource.setDoOutput(true);

		String requestBodyJsonPayload;
		RequestBody requestBody = new RequestBody();
		requestBody.setMethod("requestReboot");
		requestBody.setId(10);
		requestBody.setParams(new ArrayList<Map<String, Object>>());
		requestBody.setVersion("1.0");
		requestBodyJsonPayload = objectWriter.writeValueAsString(requestBody);

		return connResource.serviceCall(requestBodyJsonPayload);
	}
}
