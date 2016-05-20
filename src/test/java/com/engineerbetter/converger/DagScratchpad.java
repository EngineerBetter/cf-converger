package com.engineerbetter.converger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.junit.Test;

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

		TopologicalOrderIterator<String, DefaultEdge> topOrder = new TopologicalOrderIterator<String, DefaultEdge>(dag);

		List<String> ordered = new ArrayList<>();
		while(topOrder.hasNext()) {
			ordered.add(topOrder.next());
		}

		assertThat(ordered, contains("org", "space", "ups"));
	}
}
