package com.engineerbetter.converger.http;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.organizations.CreateOrganizationRequest;
import org.cloudfoundry.client.v2.organizations.CreateOrganizationResponse;
import org.cloudfoundry.client.v2.spaces.CreateSpaceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engineerbetter.converger.model.Declaration;
import com.engineerbetter.converger.model.Space;

@RestController
public class IndexController
{
	private final CloudFoundryClient cfClient;

	@Autowired
	public IndexController(CloudFoundryClient cfClient)
	{
		this.cfClient = cfClient;
	}

	@RequestMapping("/")
	public String index()
	{
		return "Up";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/", consumes = "application/x-yaml")
	public String upload(@RequestBody Declaration declaration) throws Exception
	{
		CreateOrganizationResponse response = cfClient.organizations().create(CreateOrganizationRequest.builder().name(declaration.org.name).build()).get();

		for(Space space : declaration.org.spaces) {
			cfClient.spaces().create(CreateSpaceRequest.builder().organizationId(response.getMetadata().getId()).name(space.name).build()).get();
		}

		return "Converged org " + declaration.org.name;
	}
}
