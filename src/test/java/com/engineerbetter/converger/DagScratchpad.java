package com.engineerbetter.converger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.junit.Test;

import com.engineerbetter.conveger.model.Declaration;
import com.engineerbetter.conveger.model.Intent;
import com.engineerbetter.conveger.model.Org;
import com.engineerbetter.conveger.model.OrgIntent;
import com.engineerbetter.conveger.model.Space;
import com.engineerbetter.conveger.model.SpaceIntent;

public class DagScratchpad
{
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
	public void intents() throws Exception
	{
		Declaration declaration = new Declaration(new Org("my-org", Arrays.asList(new Space("DEV"), new Space("PROD"))));
		DirectedAcyclicGraph<Intent, DefaultEdge> dag = new DirectedAcyclicGraph<Intent, DefaultEdge>(DefaultEdge.class);
		OrgIntent orgIntent = new OrgIntent(declaration.org.name);
		dag.addVertex(orgIntent);

		for(Space space : declaration.org.spaces) {
			SpaceIntent intent = new SpaceIntent(space.name);
			dag.addVertex(intent);
			dag.addDagEdge(orgIntent, intent);
		}

		List<Intent> ordered = orderDag(dag);
		assertThat(ordered, contains(new OrgIntent("my-org"), new SpaceIntent("DEV"), new SpaceIntent("PROD")));

		for(Intent intent : ordered) {
			//Do I have dependencies?
			for(DefaultEdge edge : dag.incomingEdgesOf(intent)) {
				Intent dependency = dag.getEdgeSource(edge);

				//Do they exist?
				if(dependency.resolved().isPresent()) {
					//Populate myself with stuff I need from my dependencies to tell if I exist
					//How to do this? Can't use overloading. I want the dependent to know how to populate itself.
				} else {
					throw new RuntimeException("b0rk in ordering of topological sort?");
				}
			}

			//Now I have all that I need, do I exist?
			intent.resolve();
			//If I exist, am I in the desired state?
		}
	}
}
