package com.engineerbetter.converger.intents;

/**
 *
 * Doesn't have it's own ID in the CCDB, just links two entities
 *
 */
public interface RelationshipIntent extends Intent
{
	boolean present();
}
