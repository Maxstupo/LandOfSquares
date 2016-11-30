package com.github.maxstupo.landofsquares.entity;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Maxstupo
 */
public enum EntityType {
	PLAYER(0),
	DROP(1),
	BLOCK(2),
	PARTICLE(3),
	MOB(10);

	private static final Map<Integer, EntityType> types = new HashMap<>();

	private final int id;

	EntityType(int id) {
		this.id = id;
	}

	public int getID() {
		return this.id;
	}

	static {
		for (EntityType e : values()) {
			if (types.put(e.getID(), e) != null)
				throw new IllegalArgumentException("Duplicate " + EntityType.class.getSimpleName() + " ID: " + e.getID());
		}
	}

	public static EntityType get(int id) {
		return types.get(id);
	}

}
