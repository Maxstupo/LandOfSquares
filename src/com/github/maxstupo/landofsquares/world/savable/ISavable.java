package com.github.maxstupo.landofsquares.world.savable;

import com.github.maxstupo.landofsquares.entity.AbstractEntity;
import com.github.maxstupo.landofsquares.entity.EntityType;
import com.github.maxstupo.landofsquares.storage.DataStorageObject;

/**
 *
 * @author Maxstupo
 */
public interface ISavable {
	AbstractEntity load(DataStorageObject obj);

	DataStorageObject save(AbstractEntity e);

	EntityType getType();
}
