package com.engineerbetter.converger.http;

import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.organizations.CreateOrganizationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engineerbetter.conveger.model.Declaration;

@RestController
public class IndexController
{
	private final CloudFoundryOperations cfOps;

	@Autowired
	public IndexController(CloudFoundryOperations cfOps)
	{
		this.cfOps = cfOps;
	}

	@RequestMapping("/")
	public String index()
	{
		return "Up";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/", consumes = "application/x-yaml")
	public String upload(@RequestBody Declaration declaration) throws Exception
	{
		cfOps.organizations().create(CreateOrganizationRequest.builder().organizationName(declaration.org.name).build()).get();
		return "Converged org " + declaration.org.name;
	}
}
