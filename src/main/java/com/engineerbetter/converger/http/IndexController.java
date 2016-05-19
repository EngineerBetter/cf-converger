package com.engineerbetter.converger.http;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@RestController
public class IndexController {

	@RequestMapping("/")
	public String index() {
		return "Up";
	}

	@RequestMapping(method=RequestMethod.POST, value="/", consumes="application/yaml")
	public String upload(@RequestBody byte[] declaration) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
		JsonNode tree = objectMapper.readTree(declaration);
		return "Converged org "+tree.get("org").get("name").asText();
	}
}
