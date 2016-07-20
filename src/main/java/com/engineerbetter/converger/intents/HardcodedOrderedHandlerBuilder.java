package com.engineerbetter.converger.intents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engineerbetter.converger.model.Declaration;
import com.engineerbetter.converger.model.Space;
import com.engineerbetter.converger.properties.NameProperty;
import com.engineerbetter.converger.resolution.Resolution;

@Service
public class HardcodedOrderedHandlerBuilder implements OrderedHandlerBuilder
{
	private static final Logger logger = LoggerFactory.getLogger(HardcodedOrderedHandlerBuilder.class);
	private final HandlerFactory handlerFactory;
	private final Map<Intent<? extends Resolution>, Intent<? extends Resolution>> intents;

	@Autowired
	public HardcodedOrderedHandlerBuilder(HandlerFactory handlerFactory)
	{
		this.handlerFactory = handlerFactory;
		intents = new HashMap<>();
	}


	@Override
	public List<Handler<? extends Intent<? extends Resolution>>> getOrderedHandlers(Declaration declaration)
	{
		try
		{
			DirectedAcyclicGraph<Handler<? extends Intent<? extends Resolution>>, DefaultEdge> dag = buildGraph(declaration);
			return orderDag(dag);
		}
		catch (CycleFoundException e)
		{
			throw new RuntimeException("Cycle detected in intent graph");
		}
	}

	private DirectedAcyclicGraph<Handler<? extends Intent<? extends Resolution>>, DefaultEdge> buildGraph(Declaration declaration) throws CycleFoundException
	{
		DirectedAcyclicGraph<Handler<? extends Intent<? extends Resolution>>, DefaultEdge> dag = new DirectedAcyclicGraph<>(DefaultEdge.class);
		OrgIntent orgIntent = dedupe(new OrgIntent(new NameProperty(declaration.org.name)));
		Handler<OrgIntent> orgHandler = handlerFactory.build(orgIntent);
		dag.addVertex(orgHandler);

		for(String manager : declaration.org.managers)
		{
			UaaUserIntent uaaUserIntent = dedupe(new UaaUserIntent(new NameProperty(manager)));
			addVertex(uaaUserIntent, dag);
			CfUserIntent cfUserIntent = dedupe(new CfUserIntent(uaaUserIntent));
			addVertexAndEdge(uaaUserIntent, cfUserIntent, dag);
			UserOrgIntent userOrgIntent = dedupe(new UserOrgIntent(orgIntent, cfUserIntent));
			addVertexAndEdge(cfUserIntent, userOrgIntent, dag);
			OrgManagerIntent orgManagerIntent = dedupe(new OrgManagerIntent(orgIntent, cfUserIntent));
			// This is a bit dodgy - shouldn't we be adding two edges, one to org, one to user?
			addVertexAndEdge(userOrgIntent, orgManagerIntent, dag);
		}

		for(Space space : declaration.org.spaces)
		{
			SpaceIntent spaceIntent = dedupe(new SpaceIntent(new NameProperty(space.name), orgIntent));
			addVertexAndEdge(orgIntent, spaceIntent, dag);

			for(String developer : space.developers)
			{
				// Multiple returns would have made this easier to DRY out!
				UaaUserIntent uaaUserIntent = dedupe(new UaaUserIntent(new NameProperty(developer)));
				addVertex(uaaUserIntent, dag);
				CfUserIntent cfUserIntent = dedupe(new CfUserIntent(uaaUserIntent));
				addVertexAndEdge(uaaUserIntent, cfUserIntent, dag);
				UserOrgIntent userOrgIntent = dedupe(new UserOrgIntent(orgIntent, cfUserIntent));
				addVertexAndEdge(cfUserIntent, userOrgIntent, dag);

				// This is pretty interesting. We depend on the datum of User and Space ID, but causally we depend on UserOrg
				SpaceDeveloperIntent spaceDeveloperIntent = dedupe(new SpaceDeveloperIntent(spaceIntent, cfUserIntent));
				addVertexAndEdge(userOrgIntent, spaceDeveloperIntent, dag);
				addVertexAndEdge(spaceIntent, spaceDeveloperIntent, dag);
			}
		}

		return dag;
	}

	private <I extends Intent<? extends Resolution>> void addVertex(I toIntent, DirectedAcyclicGraph<Handler<? extends Intent<? extends Resolution>>, DefaultEdge> dag) throws CycleFoundException
	{
		toIntent = dedupe(toIntent);
		dag.addVertex(handlerFactory.build(toIntent));
	}

	private void addVertexAndEdge(Intent<? extends Resolution> fromIntent,
			Intent<? extends Resolution> toIntent,
			DirectedAcyclicGraph<Handler<? extends Intent<? extends Resolution>>, DefaultEdge> dag) throws CycleFoundException
	{
		Handler<? extends Intent<? extends Resolution>> toHandler = handlerFactory.build(toIntent);
		dag.addVertex(toHandler);
		dag.addDagEdge(handlerFactory.build(fromIntent), toHandler);
	}

	private List<Handler<? extends Intent<? extends Resolution>>> orderDag(DirectedAcyclicGraph<Handler<? extends Intent<? extends Resolution>>, DefaultEdge> dag)
	{
		TopologicalOrderIterator<Handler<? extends Intent<? extends Resolution>>, DefaultEdge> topOrder = new TopologicalOrderIterator<>(dag);

		List<Handler<? extends Intent<? extends Resolution>>> ordered = new ArrayList<>();
		while(topOrder.hasNext()) {
			Handler<? extends Intent<? extends Resolution>> handler = topOrder.next();
			ordered.add(handler);
			logger.debug("Added [{}] to ordered handler list", handler);
		}
		return ordered;
	}

	/**
	 * Dedupe Intent instances
	 *
	 * We want there to only ever be one intent instance for one declaration of intent,
	 * as otherwise whenever we come across a user of "test@example.com" we'd need the
	 * handler builder mechanism to keep track of all these references, or we could just
	 * dedupe after construction.
	 *
	 * In an earlier implementation where intents and handlers were not separate, this
	 * was solved through object equality and that the DAG had Set semantics, and we
	 * looked up intents via the DAG.
	 *
	 * @param intent intent to dedupe
	 * @return existing instance, if an identical declaration had already been encountered
	 */
	@SuppressWarnings("unchecked")
	private <I extends Intent<? extends Resolution>> I dedupe(I intent)
	{
		intents.putIfAbsent(intent, intent);
		return (I) intents.get(intent);
	}
}
