package com.engineerbetter.converger.intents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

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
import com.engineerbetter.converger.model.UaaUser;
import com.engineerbetter.converger.model.Ups;
import com.engineerbetter.converger.properties.NameProperty;
import com.engineerbetter.converger.properties.UaaUserProperties;
import com.engineerbetter.converger.properties.UpsProperties;
import com.engineerbetter.converger.resolution.RelationshipResolution;
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

		/*
		 * This smells awful.
		 *
		 * We want to define UaaUsers with all their properties once, but then be able to fetch the whole intent just by email later on in the
		 * YAML. Wanging stuff in a map doesn't seem scalable, and surely the YAML itself serves as this lookup map? Just looking up by path
		 * rather than scalar ID? Something to think about when we change to a treecrawl.
		 */
		Map<String, UaaUserIntent> uaaUserIntents = new HashMap<>();
		for(UaaUser user : declaration.uaaUsers)
		{
			UaaUserIntent uaaUserIntent = dedupe(new UaaUserIntent(new UaaUserProperties(user.email, user.givenName, user.familyName)));
			uaaUserIntents.put(user.email, uaaUserIntent);
		}


		OrgIntent orgIntent = dedupe(new OrgIntent(new NameProperty(declaration.org.name)));
		Handler<OrgIntent> orgHandler = handlerFactory.build(orgIntent);
		dag.addVertex(orgHandler);

		for(String manager : declaration.org.managers)
		{
			UaaUserIntent uaaUserIntent = uaaUserIntents.get(manager);
			addVertex(uaaUserIntent, dag);
			CfUserIntent cfUserIntent = dedupe(new CfUserIntent(uaaUserIntent));
			addVertexAndEdge(uaaUserIntent, cfUserIntent, dag);
			UserOrgIntent userOrgIntent = dedupe(new UserOrgIntent(orgIntent, cfUserIntent));
			addVertexAndEdge(cfUserIntent, userOrgIntent, dag);
			OrgManagerIntent orgManagerIntent = dedupe(new OrgManagerIntent(orgIntent, cfUserIntent));
			// This is a bit dodgy - shouldn't we be adding two edges, one to org, one to user?
			addVertexAndEdge(userOrgIntent, orgManagerIntent, dag);
		}

		for(String auditor : declaration.org.auditors)
		{
			UaaUserIntent uaaUserIntent = uaaUserIntents.get(auditor);
			addVertex(uaaUserIntent, dag);
			CfUserIntent cfUserIntent = dedupe(new CfUserIntent(uaaUserIntent));
			addVertexAndEdge(uaaUserIntent, cfUserIntent, dag);
			UserOrgIntent userOrgIntent = dedupe(new UserOrgIntent(orgIntent, cfUserIntent));
			addVertexAndEdge(cfUserIntent, userOrgIntent, dag);
			OrgAuditorIntent orgManagerIntent = dedupe(new OrgAuditorIntent(orgIntent, cfUserIntent));
			// This is a bit dodgy - shouldn't we be adding two edges, one to org, one to user?
			addVertexAndEdge(userOrgIntent, orgManagerIntent, dag);
		}

		for(Space space : declaration.org.spaces)
		{
			SpaceIntent spaceIntent = dedupe(new SpaceIntent(new NameProperty(space.name), orgIntent));
			addVertexAndEdge(orgIntent, spaceIntent, dag);

			space.auditors.stream().forEach(email -> addSpaceRoleIntent(email, orgIntent, spaceIntent, uaaUserIntents, dag, SpaceAuditorIntent::new));
			space.developers.stream().forEach(email -> addSpaceRoleIntent(email, orgIntent, spaceIntent, uaaUserIntents, dag, SpaceDeveloperIntent::new));
			space.managers.stream().forEach(email -> addSpaceRoleIntent(email, orgIntent, spaceIntent, uaaUserIntents, dag, SpaceManagerIntent::new));

			for(Ups ups : space.upss)
			{
				UpsIntent upsIntent = dedupe(new UpsIntent(new UpsProperties(ups.name, ups.credentials), spaceIntent));
				addVertexAndEdge(spaceIntent, upsIntent, dag);
			}
		}

		return dag;
	}

	private <I extends Intent<? extends Resolution>> void addVertex(I toIntent, DirectedAcyclicGraph<Handler<? extends Intent<? extends Resolution>>, DefaultEdge> dag)
	{
		toIntent = dedupe(toIntent);
		dag.addVertex(handlerFactory.build(toIntent));
	}

	private void addVertexAndEdge(Intent<? extends Resolution> fromIntent,
			Intent<? extends Resolution> toIntent,
			DirectedAcyclicGraph<Handler<? extends Intent<? extends Resolution>>, DefaultEdge> dag)
	{
		Handler<? extends Intent<? extends Resolution>> toHandler = handlerFactory.build(toIntent);
		dag.addVertex(toHandler);

		try
		{
			dag.addDagEdge(handlerFactory.build(fromIntent), toHandler);
		}
		catch (CycleFoundException e)
		{
			throw new RuntimeException("Cycle found building allegedly acyclical graph", e);
		}
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


	private void addSpaceRoleIntent(String manager, OrgIntent orgIntent, SpaceIntent spaceIntent, Map<String, UaaUserIntent> uaaUserIntents, DirectedAcyclicGraph<Handler<? extends Intent<? extends Resolution>>, DefaultEdge> dag, BiFunction<SpaceIntent, CfUserIntent, ? extends Intent<RelationshipResolution>> constructorFunction)
	{
		UaaUserIntent uaaUserIntent = uaaUserIntents.get(manager);
		addVertex(uaaUserIntent, dag);
		CfUserIntent cfUserIntent = dedupe(new CfUserIntent(uaaUserIntent));
		addVertexAndEdge(uaaUserIntent, cfUserIntent, dag);
		UserOrgIntent userOrgIntent = dedupe(new UserOrgIntent(orgIntent, cfUserIntent));
		addVertexAndEdge(cfUserIntent, userOrgIntent, dag);

		// We depend on the datum of User and Space ID, but causally we depend on UserOrg
		Intent<RelationshipResolution> spaceManagerIntent = dedupe(constructorFunction.apply(spaceIntent, cfUserIntent));
		addVertexAndEdge(userOrgIntent, spaceManagerIntent, dag);
		addVertexAndEdge(spaceIntent, spaceManagerIntent, dag);
	}
}
