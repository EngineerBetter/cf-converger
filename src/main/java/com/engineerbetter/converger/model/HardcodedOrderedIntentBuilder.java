package com.engineerbetter.converger.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import com.engineerbetter.converger.intents.CfUserIntent;
import com.engineerbetter.converger.intents.Intent;
import com.engineerbetter.converger.intents.OrgIntent;
import com.engineerbetter.converger.intents.OrgManagerIntent;
import com.engineerbetter.converger.intents.SpaceDeveloperIntent;
import com.engineerbetter.converger.intents.SpaceIntent;
import com.engineerbetter.converger.intents.UaaUserIntent;
import com.engineerbetter.converger.intents.UpsIntent;
import com.engineerbetter.converger.intents.UserOrgIntent;

public class HardcodedOrderedIntentBuilder implements OrderedIntentBuilder
{
	public List<Intent> getOrderedIntents(Declaration declaration)
	{
		try
		{
			DirectedAcyclicGraph<Intent, DefaultEdge> dag = buildGraph(declaration);
			return orderDag(dag);
		}
		catch (CycleFoundException e)
		{
			throw new RuntimeException("Cycle detected in intent graph");
		}
	}

	private DirectedAcyclicGraph<Intent, DefaultEdge> buildGraph(Declaration declaration) throws CycleFoundException
	{
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
			OrgManagerIntent orgManagerIntent = new OrgManagerIntent(orgIntent, cfUserIntent);
			// This is a bit dodgy - shouldn't we be adding two edges, one to org, one to user?
			addVertexAndEdge(userOrgIntent, orgManagerIntent, dag);
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

		return dag;
	}

	private void addVertexAndEdge(Intent fromVertex, Intent toVertex, DirectedAcyclicGraph<Intent, DefaultEdge> dag) throws CycleFoundException
	{
		dag.addVertex(toVertex);
		dag.addDagEdge(fromVertex, toVertex);
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
}
