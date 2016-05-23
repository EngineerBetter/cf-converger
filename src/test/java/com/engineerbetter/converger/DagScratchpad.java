package com.engineerbetter.converger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.uaa.UaaClient;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.engineerbetter.converger.facade.CloudFoundryFacade;
import com.engineerbetter.converger.facade.UaaFacade;
import com.engineerbetter.converger.intents.CfUserIntent;
import com.engineerbetter.converger.intents.Intent;
import com.engineerbetter.converger.intents.OrgIntent;
import com.engineerbetter.converger.intents.SpaceDeveloperIntent;
import com.engineerbetter.converger.intents.SpaceIntent;
import com.engineerbetter.converger.intents.UaaUserIntent;
import com.engineerbetter.converger.intents.UpsIntent;
import com.engineerbetter.converger.intents.UserOrgIntent;
import com.engineerbetter.converger.model.Declaration;
import com.engineerbetter.converger.model.Space;
import com.engineerbetter.converger.model.Ups;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ConvergerApplication.class)
@ActiveProfiles("test")
public class DagScratchpad
{
	@Autowired
	private ApplicationContext appContext;

	@Test
	public void wibble() throws Exception
	{
		DirectedAcyclicGraph<String, DefaultEdge> dag = new DirectedAcyclicGraph<String, DefaultEdge>(
				DefaultEdge.class);
		dag.addVertex("org");
		dag.addVertex("space");
		dag.addVertex("ups");
		dag.addDagEdge("org", "space");
		dag.addDagEdge("space", "ups");

		List<String> ordered = orderDag(dag);

		assertThat(ordered, contains("org", "space", "ups"));
	}

	private <V, E> List<V> orderDag(DirectedAcyclicGraph<V, E> dag)
	{
		TopologicalOrderIterator<V, E> topOrder = new TopologicalOrderIterator<V, E>(dag);

		List<V> ordered = new ArrayList<>();
		while(topOrder.hasNext()) {
			ordered.add(topOrder.next());
		}
		return ordered;
	}


	@Test
	public void vertexIdentity() throws Exception
	{
		DirectedAcyclicGraph<String, DefaultEdge> dag = new DirectedAcyclicGraph<>(DefaultEdge.class);
		dag.addVertex("root");
		dag.addVertex("root");
		assertThat(dag.vertexSet().size(), is(1));
		dag.addVertex("child a");
		dag.addDagEdge("root", "child a");
		dag.addVertex("child b");
		dag.addDagEdge("root", "child b");
		dag.addVertex("grandchild 1");
		dag.addDagEdge("child a", "grandchild 1");
		dag.addVertex("grandchild 1");
		dag.addDagEdge("child b", "grandchild 1");
		assertThat(dag.vertexSet().size(), is(4));
		assertThat(dag.edgeSet().size(), is(4));
	}


	@Test
	public void intents() throws Exception
	{
		ClassPathResource fixture = new ClassPathResource("fixtures/declaration.yml");
		YAMLMapper mapper = new YAMLMapper();
		Declaration declaration = mapper.readValue(fixture.getFile(), Declaration.class);
		DirectedAcyclicGraph<Intent, DefaultEdge> dag = new DirectedAcyclicGraph<>(DefaultEdge.class);
		OrgIntent orgIntent = new OrgIntent(declaration.org.name);
		dag.addVertex(orgIntent);

		for(String manager : declaration.org.managers)
		{
			UaaUserIntent uaaUserIntent = new UaaUserIntent(manager);
			dag.addVertex(uaaUserIntent);
			CfUserIntent cfUserIntent = new CfUserIntent(uaaUserIntent);
			addVertexAndEdge(uaaUserIntent, cfUserIntent, dag);
			UserOrgIntent userOrgIntent = new UserOrgIntent(orgIntent, cfUserIntent);
			addVertexAndEdge(cfUserIntent, userOrgIntent, dag);
		}

		for(Space space : declaration.org.spaces)
		{
			SpaceIntent spaceIntent = new SpaceIntent(space.name, orgIntent);
			addVertexAndEdge(orgIntent, spaceIntent, dag);

			for(String developer : space.developers)
			{
				// Multiple returns would have made this easier to DRY out!
				UaaUserIntent uaaUserIntent = new UaaUserIntent(developer);
				dag.addVertex(uaaUserIntent);
				CfUserIntent cfUserIntent = new CfUserIntent(uaaUserIntent);
				addVertexAndEdge(uaaUserIntent, cfUserIntent, dag);
				UserOrgIntent userOrgIntent = new UserOrgIntent(orgIntent, cfUserIntent);
				addVertexAndEdge(cfUserIntent, userOrgIntent, dag);

				// This is pretty interesting. We depend on the datum of User and Space ID, but causally we depend on UserOrg
				SpaceDeveloperIntent spaceDeveloperIntent = new SpaceDeveloperIntent(spaceIntent, cfUserIntent);
				addVertexAndEdge(userOrgIntent, spaceDeveloperIntent, dag);
				addVertexAndEdge(spaceIntent, spaceDeveloperIntent, dag);
			}

			for(Ups ups : space.upss)
			{
				UpsIntent upsIntent = new UpsIntent(ups.name, ups.credentials, spaceIntent);
				addVertexAndEdge(spaceIntent, upsIntent, dag);
			}
		}

		List<Intent> ordered = orderDag(dag);

		//Find out if I can exist, and if I can, do I
		for(Intent intent : ordered) {
			appContext.getAutowireCapableBeanFactory().autowireBean(intent);
			//I will have everything that I need because topological ordering
			intent.resolve();
		}
	}

	private void addVertexAndEdge(Intent fromVertex, Intent toVertex, DirectedAcyclicGraph<Intent, DefaultEdge> dag) throws CycleFoundException
	{
		dag.addVertex(toVertex);
		dag.addDagEdge(fromVertex, toVertex);
	}


	private Optional<Intent> findVertex(Intent needle, DirectedAcyclicGraph<Intent, DefaultEdge> dag)
	{
		return dag.vertexSet().stream().filter(i -> i.equals(needle)).findFirst();
	}

	@Configuration
	public static class TestConfig
	{
		@Bean
		public CloudFoundryClient cfClient()
		{
			return mock(CloudFoundryClient.class);
		}

		@Bean
		public UaaClient uaaClient()
		{
			return mock(UaaClient.class);
		}

		@Bean
		public CloudFoundryFacade cloudFoundryFacade()
		{
			return mock(CloudFoundryFacade.class);
		}

		@Bean
		public UaaFacade uaaFacade()
		{
			return mock(UaaFacade.class);
		}
	}
}
