package com.github.maxstupo.landofsquares.world;

import java.util.ArrayList;
import java.util.List;

import com.github.maxstupo.landofsquares.entity.AbstractEntity;

/**
 *
 * @author Maxstupo
 */
public class EntityChunk {
	private List<AbstractEntity> entities = new ArrayList<>();
	private List<AbstractEntity> entitiesToAdd = new ArrayList<>();

	public void update() {
		entities.addAll(entitiesToAdd);
		entitiesToAdd.clear();
	}

	public List<AbstractEntity> getEntities() {
		return entities;
	}

	public int totalEntities() {
		return entities.size();
	}

	public int queuedEntities() {
		return entitiesToAdd.size();
	}

	public void clear() {
		entities.clear();
		entitiesToAdd.clear();
	}

	public void addEntity(AbstractEntity e) {
		entitiesToAdd.add(e);
	}

}
