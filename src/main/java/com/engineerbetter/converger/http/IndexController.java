package com.engineerbetter.converger.http;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engineerbetter.conveger.model.Declaration;

@RestController
public class IndexController {

	@RequestMapping("/")
	public String index() {
		return "Up";
	}

	@RequestMapping(method=RequestMethod.POST, value="/", consumes="application/x-yaml")
	public String upload(@RequestBody Declaration declaration) throws Exception {
		return "Converged org "+declaration.getOrg().getName();
	}
}
