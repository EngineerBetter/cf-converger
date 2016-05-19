package com.engineerbetter.converger.http;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class IndexController {

	@RequestMapping("/")
	public String index() {
		return "Up";
	}

	@RequestMapping(method=RequestMethod.POST, value="/")
	public String upload(@RequestParam("file") MultipartFile file) {
		return "Size was: "+file.getSize();
	}
}
