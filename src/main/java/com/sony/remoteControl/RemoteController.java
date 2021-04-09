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

	@Bean
	public void objectWritedInitiator() {
		this.objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
	}

	@RequestMapping(method = RequestMethod.POST, path = "/volume/{offset}")
	public String volumeChange(@PathVariable String offset) throws IOException {
		ConnectionResource connResource = new ConnectionResource("/sony/audio", env);
		connResource.setRequestMethod("POST");
		connResource.setBaseRestHeaders();
		connResource.setDoOutput(true);

		String requestBodyJsonPayload;
		RequestBody requestBody = new RequestBody();
		List<Map<String, String>> volumeBodyList = new ArrayList<Map<String, String>>();
		Map<String, String> volumeBodyMap = new HashMap<String, String>();

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
		requestBody.setParams(new ArrayList<Map<String, String>>());
		requestBody.setVersion("1.0");
		requestBodyJsonPayload = objectWriter.writeValueAsString(requestBody);

		return connResource.serviceCall(requestBodyJsonPayload);
	}
}
