package com.engineerbetter.converger.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.engineerbetter.converger.intents.CfUserIntent;
import com.engineerbetter.converger.intents.Intent;
import com.engineerbetter.converger.intents.OrgIntent;
import com.engineerbetter.converger.intents.OrgManagerIntent;
import com.engineerbetter.converger.intents.SpaceDeveloperIntent;
import com.engineerbetter.converger.intents.SpaceIntent;
import com.engineerbetter.converger.intents.UaaUserIntent;
import com.engineerbetter.converger.intents.UserOrgIntent;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class HardcodedOrderedIntentBuilderTest
{
	private Declaration fixture;
	private HardcodedOrderedIntentBuilder builder;

	@Before
	public void setup() throws Exception
	{
		YAMLMapper mapper = new YAMLMapper();
		ClassPathResource resource = new ClassPathResource("fixtures/declaration.yml");
		fixture = mapper.readValue(resource.getFile(), Declaration.class);
		builder = new HardcodedOrderedIntentBuilder();
	}

	@Test
	public void testAllTheThings()
	{
		List<Intent> intents = builder.getOrderedIntents(fixture);
		OrgIntent org = new OrgIntent("my-lovely-org");
		SpaceIntent devSpace = new SpaceIntent("DEV", org);
		SpaceIntent prodSpace = new SpaceIntent("PROD", org);
		assertThat(intents, hasItem(org));
		assertThisAppearsBeforeThat(org, devSpace, intents);
		assertThisAppearsBeforeThat(org, prodSpace, intents);

		UaaUserIntent uaaDanYoung = new UaaUserIntent("dan.young@engineerbetter.com");
		UaaUserIntent uaaDanJones = new UaaUserIntent("daniel.jones@engineerbetter.com");

		CfUserIntent cfDanYoung = new CfUserIntent(uaaDanYoung);
		CfUserIntent cfDanJones = new CfUserIntent(uaaDanJones);
		assertThisAppearsBeforeThat(uaaDanYoung, cfDanYoung, intents);
		assertThisAppearsBeforeThat(uaaDanJones, cfDanJones, intents);

		UserOrgIntent danYoungOrg = new UserOrgIntent(org, cfDanYoung);
		UserOrgIntent danJonesOrg = new UserOrgIntent(org, cfDanJones);
		assertThisAppearsBeforeThat(org, danYoungOrg, intents);
		assertThisAppearsBeforeThat(cfDanYoung, danYoungOrg, intents);
		assertThisAppearsBeforeThat(org, danJonesOrg, intents);
		assertThisAppearsBeforeThat(cfDanJones, danJonesOrg, intents);

		OrgManagerIntent orgManager = new OrgManagerIntent(org, cfDanYoung);
		assertThisAppearsBeforeThat(org, orgManager, intents);
		assertThisAppearsBeforeThat(cfDanYoung, orgManager, intents);

		SpaceDeveloperIntent devSpaceDeveloper = new SpaceDeveloperIntent(devSpace, cfDanJones);
		assertThisAppearsBeforeThat(devSpace, devSpaceDeveloper, intents);
		assertThisAppearsBeforeThat(danJonesOrg, devSpaceDeveloper, intents);

		SpaceDeveloperIntent prodSpaceDeveloper = new SpaceDeveloperIntent(prodSpace, cfDanJones);
		assertThisAppearsBeforeThat(prodSpace, prodSpaceDeveloper, intents);
		assertThisAppearsBeforeThat(danJonesOrg, prodSpaceDeveloper, intents);
	}

	private void assertThisAppearsBeforeThat(Intent before, Intent after, List<Intent> intents)
	{
		assertThat(intents, hasItem(before));
		assertThat(intents, hasItem(after));

		int beforeIndex = -1;
		int afterIndex = -1;

		for(int index = 0; index < intents.size(); index++)
		{
			Intent current = intents.get(index);
			if(current.equals(before))
			{
				beforeIndex = index;
			}

			if(current.equals(after))
			{
				afterIndex = index;
			}
		}

		assertThat("Intent did not appear in list", beforeIndex, not(-1));
		assertThat("Intent did not appear in list", afterIndex, not(-1));
		assertThat("Intents appeared to be the same", beforeIndex, not(afterIndex));
		assertThat(before+" did not appear before "+after, beforeIndex, lessThan(afterIndex));
	}
}
