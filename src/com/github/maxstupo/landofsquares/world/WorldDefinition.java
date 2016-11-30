package com.github.maxstupo.landofsquares.world;

import com.github.maxstupo.flatengine.util.Util;
import com.github.maxstupo.landofsquares.core.LandOfSquares;

/**
 *
 * @author Maxstupo
 */
public class WorldDefinition {
	private static final WorldDefinition[] defs = new WorldDefinition[1];

	public static final WorldDefinition earth = new WorldDefinition(0).setGravity(0.03f);

	private final int id;
	private float gravity = 0f;

	public WorldDefinition(int id) {
		this.id = id;
		if (defs[id] == null) {
			defs[id] = this;
		} else {
			LandOfSquares.get().getLog().warn(getClass().getSimpleName(), "Conflicting  {0} ID: {1}", getClass().getSimpleName(), id);
			throw new IllegalArgumentException("Conflicting WorldDefinition ids: " + id);
		}
	}

	public int getID() {
		return id;
	}

	public float getGravity() {
		return gravity;
	}

	public WorldDefinition setGravity(float gravity) {
		this.gravity = gravity;
		return this;
	}

	public static WorldDefinition get(int id) {
		if (!Util.isValid(defs, id))
			return null;
		return defs[id];
	}

	@Override
	public String toString() {
		return "WorldDef [id=" + id + ", gravity=" + gravity + "]";
	}
}
